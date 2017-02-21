package litz;

import static litz.Server.*;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import framework.*;

public class Client extends framework.Client {
    private void socketError() {
        if (UI != null) UI.doQuit(true);
        else close();
    }
    private <T> T recv() {
        try {
            return ObjectStream.recv(server);
        } catch (IOException ex) {
            if (thread != null && thread.running) socketError();
            return null;
        }
    }
    private void send(Object... objects) {
        try {
            ObjectStream.send(server, objects);
        } catch (IOException ex) {
        	if (thread != null && thread.running) socketError();
        }
    }

    private byte index;

    Setting setting;
    Game game;
    private final Semaphore activeTurn = new Semaphore(0);

    private UserInterface UI = null;

    public void bind(UserInterface UI) {
        this.UI = UI;
    }
    @Override
    public boolean connect(String serverCode) {
        if (super.connect(serverCode)) {
            setting = recv();
            UIChangeRule(setting.rule);
            for (Player player : setting.players) {
                if (player != null) UIChangePlayer(player);
            }
            if (UI != null) UI.changeBoardSize(setting.boardSize);
            send(SERVICE_INDEX);
            thread = new MainThread();
            thread.start();
            return true;
        } else {
            return false;
        }
    }

    public void editRule(byte connectNumber, boolean winByConnect, boolean allowOverline, boolean allowSlant) {
        send(SERVICE_EDIT_RULE, new Rule(connectNumber, winByConnect, allowOverline, allowSlant));
    }
    public void editPlayer(String playerName, byte pieceStyleIndex) {
        send(SERVICE_EDIT_PLYR, playerName, pieceStyleIndex);
    }
    public void editBoardSize(byte boardSize) {
        send(SERVICE_EDIT_SIZE, boardSize);
    }
    public void startGame() {
        send(SERVICE_START);
    }
    public void placePiece(byte x, byte y) {
        Point position = new Point(x, y);
        if (activeTurn.tryAcquire()) {
            send(SERVICE_PLACE, position);
        }
    }
    public void exitGame() {
        send(SERVICE_EXIT);
    }

    Connection[][] getConnections(Point position) {
        return game.getConnections(position);
        /** usage:
         *  Connection[][] conns = getConnections(position);
         *  for (int pieceStyleIndex = 0; pieceStyleindex < Player.MAX_PLAYER_NUMBER; ++pieceStyleIndex) {
         *      for (Connection conn : conns[pieceStyleIndex]) {
         *          if (conn == null) continue;
         *          for (Point p : conn.onConnect) {
         *              link p
         *          }
         *          for (Point p : conn.separated) {
         *              link p
         *          }
         *      }
         *  }
         */
    }

    private void UIChangeRule(Rule rule) {
        if (UI != null) UI.changeRule(rule.connectNumber, rule.winByConnect, rule.allowOverline, rule.allowSlant);
    }
    private void UIChangePlayer(Player player) {
        if (UI != null) UI.changePlayer(player.clientIndex, player.name, player.pieceStyleIndex);
    }
    private void UIPlace(Piece piece) {
        if (UI != null) UI.place(piece.position.x, piece.position.y, piece.styleIndex);
    }

    private class MainThread extends Thread {
        volatile boolean running;
        @Override
        public void run() {
            running = true;
            while (running) {
                if (server == null) break;
                int code = recv();
                switch (code) {
                    case SERVICE_INDEX: {
                        byte recv = recv();
                        index = recv;
                        if (UI != null) UI.getIndex(recv);
                        break;
                    }
                    case SERVICE_EDIT_EXIT: {
                        byte recv = recv();
                        setting.players[recv] = null;
                        if (UI != null) UI.removePlayer(recv);
                        break;
                    }
                    case SERVICE_EDIT_RULE: {
                        Rule recv = recv();
                        setting.rule = recv;
                        UIChangeRule(recv);
                        break;
                    }
                    case SERVICE_EDIT_ENTR:
                    case SERVICE_EDIT_PLYR: {
                        Player recv = recv();
                        setting.players[recv.clientIndex] = recv;
                        UIChangePlayer(recv);
                        break;
                    }
                    case SERVICE_EDIT_SIZE: {
                        byte recv = recv();
                        setting.boardSize = recv;
                        if (UI != null) UI.changeBoardSize(recv);
                        break;
                    }
                    case SERVICE_START: {
                        game = new Game(setting);
                        if (UI != null) UI.start();
                        break;
                    }
                    case SERVICE_TURN: {
                        byte recv = recv();
                        if (UI != null) UI.next(recv);
                        if (recv == index) {
                            activeTurn.release();
                            if (UI != null) UI.turn(true);
                        } else {
                            if (UI != null) UI.turn(false);
                        }
                        break;
                    }
                    case SERVICE_END: {
                        //
                        break;
                    }
                    case SERVICE_PLACE: {
                        Piece recv = recv();
                        game.place(recv);
                        UIPlace(recv);
                        break;
                    }
                    case SERVICE_INVALID: {
                        activeTurn.release();
                        break;
                    }
                    case SERVICE_WIN: {
                        byte recv = recv();
                        if (UI != null) UI.win(recv);
                        running = false;
                        break;
                    }
                }
            }
        }
        void finish() {
            running = false;
        }
    }
    private MainThread thread = null;

    @Override
    public void close() {
        if (thread != null) {
            thread.finish();
            thread.interrupt();
            thread = null;
        }
        super.close();
    }

}
