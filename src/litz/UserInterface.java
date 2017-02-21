package litz;

public abstract class UserInterface {
    protected Server server = null;
    protected Client client = null;

    public void doQuit(boolean isForced) {
        if (client != null) {
        	if (!isForced) client.exitGame();
            client.close();
            client = null;
        }
        if (server != null) {
            server.close();
            server = null;
        }
        if (isForced) {
            forceQuit();
        }
        back();
    }

    public abstract void forceQuit();
    public abstract void back();
    /*public String newServer() {
        if (server != null) return server.getCode();
        server = new Server();
        server.ready();
        server.run();
        return server.getCode();
    }
    public void closeServer() {
        if (server == null) return;
        server.close();
        server = null;
    }
    public boolean newClient(String serverCode) {
        if (client != null) return false;
        client = new Client();
        client.bind(this);
        boolean re = client.connect(serverCode);
        if (re) client.run();
        return re;
    }
    public void closeClient() {
        if (client == null) return;
        client.exitGame();
        client.close();
        client = null;
    }*/

    public abstract void getIndex(byte index);
    public abstract void changeRule(byte connectNumber, boolean winByConnect, boolean allowOverline, boolean allowSlant);
    public abstract void changePlayer(byte playerIndex, String playerName, byte pieceStyleIndex);
    public abstract void removePlayer(byte playerIndex);
    public abstract void changeBoardSize(byte boardSize);
    public abstract void start();
    public abstract void next(byte playerIndex);
    public abstract void turn(boolean ownTurn);
    public abstract void place(byte x, byte y, byte pieceStyleIndex);
    public abstract void win(byte playerIndex);

}
