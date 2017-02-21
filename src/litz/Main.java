package litz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main
{
    public static JFrame appWindow;
    public static AnimatedBackground rootPanel;
    public static String[] playerName = new String[10];
    public static int nPlayer, boardSize;
    public static int windowHeight = 768, windowWidth = 1024;
    public static ChessBoard chessBoard;
    public static ButtonList playerList;
    public static HashMap<JComponent,Animator> animatorMap = new HashMap<JComponent,Animator>();
    public static Font font_twcenmt;
    public static Color color_transparent_0 = new Color(255, 255, 255, 0);
    public static Color color_transparent_60 = new Color(255, 255, 255, 60);
    public static MainMenu mainMenu;
    public static GameView gameView;
    public static Color backgroundColor = new Color(0x778899);
    public static GUI gui;

    public Main()
    {
        appWindow = new JFrame("LiTZ!");
        appWindow.setSize(windowWidth, windowHeight);
        appWindow.setResizable(false);
        appWindow.setVisible(true);
        Dimension contentSize = appWindow.getContentPane().getSize();
        windowHeight = contentSize.height;
        windowWidth = contentSize.width;
        System.out.println(windowHeight+" . "+windowWidth);
        appWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        rootPanel = new AnimatedBackground(0, 0, windowWidth, windowHeight, backgroundColor);
        (new Timer()).schedule(new TaskAnimateBackground(rootPanel), 0, 30);
        appWindow.add(rootPanel);

        try
        {
            ButtonList.selectorImage = ImageIO.read(Main.class.getResourceAsStream("listSelection.png"));
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }

        try
        {
            font_twcenmt = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("TCCEB.TTF"));
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

        try
        {
            Option.image_check = ImageIO.read(Main.class.getResource("check.png"));
            Option.image_uncheck = ImageIO.read(Main.class.getResource("uncheck.png"));
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    static void initializeGameView(JPanel rootPanel, int _nPlayer, int _boardSize, String[] _playerName)
    {
    	Main.nPlayer = _nPlayer;
    	Main.boardSize = _boardSize;
    	Main.playerName = _playerName;
        gameView = new GameView();
        rootPanel.add(gameView);
        rootPanel.repaint();
    }

    static void initializeMainMenu(JPanel rootPanel)
    {
        mainMenu = new MainMenu();
        rootPanel.add(mainMenu);
        rootPanel.repaint();
    }

    static void requestPutPawn(int x, int y, int player)
    {
        gui.client.placePiece((byte)x, (byte)y);
    }

    public static void main(String args[])
    {
        System.out.println("run litz");
        gui = new GUI();
        new Main();
        //initializeGameView(guiWindow.rootPanel, 5, 20, new String[]{"minami chiaki","li caihua","zhou zikai","zhong yuan","tang yi"});
        initializeMainMenu(Main.rootPanel);
    }
}
