package litz;

import java.util.ArrayList;

import javax.swing.JOptionPane;

public class GUI extends UserInterface {
    public String newHost() {
        //if (server != null) return server.getCode();
        server = new Server();
        server.ready();
        server.listen();
        String serverCode = server.getCode();
        if (serverCode.length() == 0 || !newGuest(serverCode)) {
            server.close();
            return null;
        }
        return serverCode;
    }
    public boolean newGuest(String serverCode) {
        client = new Client();
        client.bind(this);
        boolean success = client.connect(serverCode);
        if (!success) client.close();
        return success;
    }
    public void startGame() {
        server.finish();
        client.startGame();
    }
    public void exitGame() {
        doQuit(false);
    }/*
    public void finish() {
        if (client != null) {
            client.close();
            client = null;
        }
        if (server != null) {
            server.close();
            server = null;
        }
        back();
    }*/

    @Override
    public void getIndex(byte index) {
    	Main.mainMenu.roomPlayerList.initialize(index);
    }

    @Override
    public void changeRule(byte connectNumber, boolean winByConnect, boolean allowOverline, boolean allowSlant) {
    	Main.mainMenu.optionPanel1.selectOption(connectNumber - 4);
    	Main.mainMenu.optionPanel3.selectOption(winByConnect ? 0 : 1);
    	Main.mainMenu.optionPanel2.selectOption(allowOverline ? 0 : 1);
    	Main.mainMenu.optionPanel4.selectOption(allowSlant ? 0 : 1);
    }

    @Override
    public void changePlayer(byte playerIndex, String playerName, byte pieceStyleIndex) {
    	Main.mainMenu.roomPlayerList.updatePlayer(playerIndex, playerName);
    }

    @Override
    public void removePlayer(byte playerIndex) {
    	Main.mainMenu.roomPlayerList.updatePlayer(playerIndex, "---");
    }

    @Override
    public void changeBoardSize(byte boardSize) {
    	Main.mainMenu.optionPanel0.selectOption(boardSize/5 - 2);
    }

    @Override
    public void start() {
    	ArrayList<String> playerNames = new ArrayList<>();
        for (Player player : client.setting.players) {
            if (player != null) playerNames.add(player.name);
        }
        Main.rootPanel.remove(Main.mainMenu);
        Object obja[] = playerNames.toArray();
        String stra[] = new String[obja.length];
        for (int i=0; i<stra.length; ++i) stra[i] = (String)obja[i];
        Main.initializeGameView(Main.rootPanel, client.game.getPlayerNumber(), client.game.board.size, stra);
    }

    @Override
    public void next(byte playerIndex) {
    	System.out.println("next " + playerIndex);
    }

    @Override
    public void turn(boolean ownTurn) {
    	System.out.println("turn " + ownTurn);
    	if (ownTurn) Main.gameView.yourTurn.setVisible(true);
    	else Main.gameView.yourTurn.setVisible(false);
    	Main.rootPanel.repaint();
    }

    @Override
    public void place(byte x, byte y, byte pieceStyleIndex) {
    	Main.chessBoard.putPawn(x, y, pieceStyleIndex + 1);
    }

    @Override
    public void win(byte playerIndex) {
    	JOptionPane.showMessageDialog(null, Main.playerName[playerIndex]+" wins!");
    	Main.gameView.buttonQuit.setVisible(true);
    	Main.rootPanel.repaint();
    }

    @Override
    public void forceQuit() {
    	JOptionPane.showMessageDialog(null, " You've been disconnected from the server.");
    }
    
    @Override
    public void back() {
    	if (Main.gameView != null)
    	{
    		Main.rootPanel.remove(Main.gameView);
	    	Main.gameView = null;
	    	Main.mainMenu = new MainMenu();
	    	Main.rootPanel.add(Main.mainMenu);
    	}
    	else
    	{
    		Animator.animate(Main.mainMenu.menu0, Main.windowWidth, 0);
            Animator.animate(Main.mainMenu.menu1, Main.windowWidth, 0);
    	}
    }
}
