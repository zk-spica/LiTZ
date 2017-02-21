package litz;

import static litz.Player.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.Semaphore;

import framework.*;

public class Server extends framework.Server {
    private <T> T recv(SocketStream client, byte clientIndex) {
        try {
            return ObjectStream.recv(client);
        } catch (IOException ex) {
            close(clientIndex);
            return null;
        }
    }
    private void send(SocketStream client, byte clientIndex, Object... objects) {
        try {
            ObjectStream.sendWithLock(client, objects);
        } catch (IOException ex) {
            close(clientIndex);
        }
    }
    private void sendAll(Object... objects) {
    	if (clients == null) return;
        for (byte clientIndex = 0; clientIndex < clients.size(); ++clientIndex) {
            send(clients.get(clientIndex), clientIndex, objects);
        }
    }

    private Setting setting;
    private Game game = null;

    private volatile int readCount = 0;
    private final Semaphore readLock = new Semaphore(1);
    private final Semaphore editLock = new Semaphore(1);
    private void readerAcquire() {
        readLock.acquireUninterruptibly();
        if (readCount++ == 0) {
            editLock.acquireUninterruptibly();
        }
        readLock.release();
    }
    private void readerRelease() {
        readLock.acquireUninterruptibly();
        if (--readCount == 0) {
            editLock.release();
        }
        readLock.release();
    }
    private void writerAcquire() {
        editLock.acquireUninterruptibly();
    }
    private void writerRelease() {
        editLock.release();
    }

    private ArrayList<Byte> turnOrder;
    private ListIterator<Byte> currentTurn;
    private volatile byte currentPlayer;
    private void initTurn() {
        turnOrder = new ArrayList<>();
        readerAcquire();
        for (Player player : setting.players) {
            if (player != null) turnOrder.add(player.clientIndex);
        }
        readerRelease();
        //Collections.shuffle(turnOrder);
        currentTurn = turnOrder.listIterator();
        nextTurn();
    }
    private boolean nextTurn() {
        while (true) {
            writerAcquire();
            if (!currentTurn.hasNext()) {
                currentTurn = turnOrder.listIterator();
            }
            currentPlayer = currentTurn.next();
            sendAll(SERVICE_TURN, currentPlayer);
            writerRelease();
            readerAcquire();
            if (game.players[currentPlayer].isHuman) {
                readerRelease();
                break;
            }
            Point calc = Robot.calc(game, game.players[currentPlayer].pieceStyleIndex);
            Piece piece = new Piece(calc, currentPlayer);
            game.place(piece);
            sendAll(SERVICE_PLACE, piece);
            if (game.win(calc)) {
            	sendAll(SERVICE_WIN, currentPlayer);
                readerRelease();
                return true;
            }
            readerRelease();
        }
        return false;
    }
    private void win(Point position) {
        if (game.win(position)) {
            sendAll(SERVICE_WIN, currentPlayer);
        }
        if (nextTurn()) {
            close();
        }
    }

    private boolean[] pieceStyleUsage;
    private final Semaphore lockPieceStyleUsage = new Semaphore(1);
    private void clearPieceStyleUsage() {
        lockPieceStyleUsage.acquireUninterruptibly();
        pieceStyleUsage = new boolean[MAX_STYLE_NUMBER];
        for (byte i = 0; i < MAX_STYLE_NUMBER; ++i) {
            pieceStyleUsage[i] = false;
        }
        lockPieceStyleUsage.release();
    }
    private byte useAvailablePieceStyle() {
        lockPieceStyleUsage.acquireUninterruptibly();
        byte result = -1;
        for (byte i = 0; i < MAX_STYLE_NUMBER; ++i) {
            if (!pieceStyleUsage[i]) {
                pieceStyleUsage[i] = true;
                result = i;
                break;
            }
        }
        lockPieceStyleUsage.release();
        return result;
    }
    private boolean usePieceStyle(byte pieceStyleIndex) {
        lockPieceStyleUsage.acquireUninterruptibly();
        boolean result = false;
        if (pieceStyleIndex >= 0 && pieceStyleIndex < MAX_STYLE_NUMBER && !pieceStyleUsage[pieceStyleIndex]) {
            pieceStyleUsage[pieceStyleIndex] = true;
            result = true;
        }
        lockPieceStyleUsage.release();
        return result;
    }
    private void freePieceStyle(byte pieceStyleIndex) {
        lockPieceStyleUsage.acquireUninterruptibly();
        if (pieceStyleIndex >= 0 && pieceStyleIndex < MAX_STYLE_NUMBER) {
            pieceStyleUsage[pieceStyleIndex] = false;
        }
        lockPieceStyleUsage.release();
    }

    @Override
    protected int maxClientNumber() {
        return MAX_PLAYER_NUMBER;
    }
    @Override
    protected void onConnect(SocketStream client) {
        readerAcquire();
        // assume success
        try {
            ObjectStream.send(client, setting);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        readerRelease();
    }
    @Override
    protected void onArrayed(int clientIndexInt) {
    	byte clientIndex = (byte)clientIndexInt;
        threads[clientIndexInt] = new MainThread(this, clientIndex);
        threads[clientIndexInt].start();
        Player player = new Player(clientIndex, useAvailablePieceStyle(), "player" + clientIndex);
        writerAcquire();
        setting.players[clientIndexInt] = player;
        sendAll(SERVICE_EDIT_ENTR, player);
        writerRelease();
    }

    static final int SERVICE_EXIT = -1;
    static final int SERVICE_START = 0;
    static final int SERVICE_TURN = 1;
    static final int SERVICE_END = 2;
    static final int SERVICE_PLACE = 3;
    static final int SERVICE_INVALID = 4;
    static final int SERVICE_WIN = 5;
    static final int SERVICE_EDIT_EXIT = 16;
    static final int SERVICE_EDIT_ENTR = 17;
    static final int SERVICE_EDIT_RULE = 18;
    static final int SERVICE_EDIT_PLYR = 19;
    static final int SERVICE_EDIT_SIZE = 20;
    static final int SERVICE_INDEX = 32;

    private class MainThread extends Thread {
    	private final Server out;
        private final byte clientIndex;
        private final SocketStream client;
        public MainThread(Server server, byte index) {
            this.out = server;
            this.clientIndex = index;
            this.client = clients.get(index);
        }
        private <T> T _recv() {
            return recv(client, clientIndex);
        }
        private void _send(Object... objects) {
            send(client, clientIndex, objects);
        }
        private volatile boolean running;
        @Override
        public void run() {
            running = true;
            while (running) {
                if (client == null) break;
                int code = _recv();
                switch (code) {
                    case SERVICE_INDEX: {
                    	_send(SERVICE_INDEX, clientIndex);
                        break;
                    }
                    case SERVICE_EXIT: {
                        close(clientIndex);
                        break;
                    }
                    case SERVICE_EDIT_RULE: {
                        Rule recv = _recv();
                        writerAcquire();
                        setting.rule = recv;
                        sendAll(SERVICE_EDIT_RULE, recv);
                        writerRelease();
                        break;
                    }
                    case SERVICE_EDIT_PLYR: {
                        String recv1 = _recv();
                        byte recv2 = _recv();
                        writerAcquire();
                        Player player = setting.players[clientIndex];
                        boolean change = false;
                        if (!recv1.equals(player.name)) {
                            player.name = recv1;
                            change = true;
                        }
                        if (recv2 != player.pieceStyleIndex && usePieceStyle(recv2)) {
                            freePieceStyle(player.pieceStyleIndex);
                            player.pieceStyleIndex = recv2;
                            change = true;
                        }
                        if (change) {
                            sendAll(SERVICE_EDIT_PLYR, player);
                        }
                        writerRelease();
                        break;
                    }
                    case SERVICE_EDIT_SIZE: {
                        byte recv = _recv();
                        writerAcquire();
                        setting.boardSize = recv;
                        sendAll(SERVICE_EDIT_SIZE, recv);
                        writerRelease();
                        break;
                    }
                    case SERVICE_START: {
                    	out.finish();
                        sendAll(SERVICE_START);
                        writerAcquire();
                        game = new Game(setting);
                        writerRelease();
// following belongs to gaming logic
// there is no competition in gaming logic so no lock is used
// but some problems may be caused by implementation
                        initTurn();
                        break;
                    }
                    case SERVICE_PLACE: {
                        Point recv = _recv();
                        Piece piece = new Piece(recv, currentPlayer);
                        if (game.canPlace(piece)) {
                            game.place(piece);
                            sendAll(SERVICE_PLACE, piece);
                            win(recv);
                        } else {
                            _send(SERVICE_INVALID);
                        }
                        break;
                    }
                }
            }
        }
        void finish() {
            running = false;
        }
    }
    private MainThread[] threads = null;

    @Override
    public void ready() {
        super.ready();
        setting = new Setting();
        clearPieceStyleUsage();
        threads = new MainThread[MAX_PLAYER_NUMBER];
    }

    @Override
    public void close(int clientIndexInt) {
        byte clientIndex = (byte)clientIndexInt;
        MainThread thread = threads[clientIndex];
        if (thread != null) {
            thread.finish();
            thread.interrupt();
            threads[clientIndex] = null;
        }
        super.close(clientIndexInt);
        writerAcquire();
        if (game != null) {
            Player player = setting.players[clientIndex];
            player.isHuman = false;
            writerRelease();
            if (currentPlayer == clientIndex) {
                Point calc = Robot.calc(game, game.players[currentPlayer].pieceStyleIndex);
                Piece piece = new Piece(calc, currentPlayer);
                sendAll(SERVICE_PLACE, piece);
                win(calc);
            }
            readerAcquire();
            sendAll(SERVICE_EDIT_PLYR, player);
            readerRelease();
        } else {
            writerRelease();
            sendAll(SERVICE_EDIT_EXIT, clientIndex);
        }
        
    }
    @Override
    public void close() {
    	game = null;
        for (byte clientIndex = 0; clientIndex < MAX_PLAYER_NUMBER; ++clientIndex) {
            close(clientIndex);
        }
        super.close();
    }

}
