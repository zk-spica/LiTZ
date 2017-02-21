package litz;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import static litz.Main.*;

public class GameView extends Div
{
	public Button buttonQuit;
	public TextDiv yourTurn;

    public GameView()
    {
        super(1, 1, Main.windowWidth, Main.windowHeight, Main.color_transparent_0);
        int height1 = Main.windowHeight;

        ChessBoard chessBoard = new ChessBoard(Main.boardSize, Main.boardSize, Main.nPlayer, (int)(height1*0.42), (int)(height1*0.04), (int)(height1*0.92));
        Main.chessBoard = chessBoard;
        add(chessBoard);
        add(new TextDiv("PLAYERS", (int)(height1*0.05), (int)(height1*0.042), NameCard.width, (int)(height1*0.07)));
        ButtonList playerList = new ButtonList((int)(height1*0.05), (int)(height1*0.12), Main.nPlayer, NameCard.width, NameCard.height);
        playerList.operatable = false;
        int i;
        for (i=0; i<Main.nPlayer; ++i)
        {
            NameCard a = new NameCard(playerList.buttonHeight/5, 0, i+1);
            playerList.list[i].add(a);
        }
        Main.playerList = playerList;
        add(playerList);
        yourTurn = new TextDiv("YOUR TURN!", (int)(height1*0.05), (int)(height1*0.14) + playerList.height, playerList.width, (int)(height1*0.05));
        yourTurn.setVisible(false);
        add(yourTurn);

        buttonQuit = new Button(" QUIT GAME", (int)(height1*0.05), (int)(height1*0.89), playerList.width, (int)(height1*0.07));
        buttonQuit.addMouseListener(new MouseAdapter(){
        	public void mousePressed(MouseEvent e)
        	{
        		gui.exitGame();
        	}
        });
        add(buttonQuit);
    }
}