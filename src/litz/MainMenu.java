package litz;

import static litz.Main.gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu extends Div
{
	boolean optionEditable;
    OptionPanel optionPanel0, optionPanel1, optionPanel2, optionPanel3, optionPanel4;
    Div optionArea, playerArea, menu1;
    ButtonList menu0, menu2, menu3;
    TextField invitationCodeField;
    Button buttonJoin;
    RoomPlayerList roomPlayerList;
    private int titleFontSize, menu1optionSize1, menu1optionSize2, height1, i;
    
    public MainMenu()
    {
        super(1, 1, Main.windowWidth, Main.windowHeight, Main.color_transparent_0);
        titleFontSize = (int)(Main.windowHeight * 0.48);
        menu0 = new ButtonList((int)(Main.windowWidth*0.5), (int)(Main.windowHeight*0.5), 3, (int)(Main.windowWidth*0.35), (int)(Main.windowHeight*0.12));
        menu1 = new Div((int)(Main.windowWidth*0.05) + Main.windowWidth, (int)(Main.windowHeight*0.45), (int)(Main.windowWidth*0.9), (int)(Main.windowHeight*0.53), new Color(255,255,255,60));

        height1 = menu1.getHeight();
        menu1optionSize1 = (int)(height1*0.08);
        menu1optionSize2 = (int)(height1*0.08);

        for (i=0; i<=2; ++i)
        {
        	String text = "null";
            if (i == 0) text = "NEW GAME";
            else
            if (i == 1) text = "JOIN GAME";
            else
            if (i == 2) text = "EXIT";
            menu0.list[i].add(new ButtonText(text, menu0));
            menu0.setComponentZOrder(menu0.list[i], 0);
        }

        menu0.list[0].addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e)
            {

                optionArea.setVisible(true);
                buttonJoin.setVisible(false);
                menu2.setVisible(true);
                menu3.setVisible(false);
                optionEditable = true;
                invitationCodeField.setEditable(false);
                roomPlayerList.reset();

                String serverCode = gui.newHost();
                if (serverCode == null) return;
                invitationCodeField.setText(serverCode);

                Animator.animate(menu0, -Main.windowWidth, 0);
                Animator.animate(menu1, -Main.windowWidth, 0);

            }
        });

        menu0.list[1].addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e)
            {
                optionArea.setVisible(false);
                buttonJoin.setVisible(true);
                playerArea.setVisible(false);
                menu2.setVisible(false);
                menu3.setVisible(true);
                optionEditable = false;
                invitationCodeField.setEditable(true);
                invitationCodeField.setText("");
                roomPlayerList.reset();
                Animator.animate(menu0, -Main.windowWidth, 0);
                Animator.animate(menu1, -Main.windowWidth, 0);
            }
        });
        
        menu0.list[2].addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e)
            {
                System.exit(0);
            }
        }); 
        
        add(menu0);
        menu0.repaint();

        optionArea = new Div(0, 0, (int)(height1*1.25), height1);
        menu1.add(optionArea);
        optionArea.add(new TextDiv("BOARD SIZE:", (int)(height1*0.392), (int)(height1*0.022), (int)(height1*0.98), menu1optionSize1));
        optionArea.add(new TextDiv("TARGET CONNECTION COUNT:", (int)(height1*0.16), (int)(height1*0.222), (int)(height1*0.98), menu1optionSize1));
        optionArea.add(new TextDiv("TABOO MOVE:", (int)(height1*0.392), (int)(height1*0.422), (int)(height1*0.48), menu1optionSize1));
        optionArea.add(new TextDiv("WIN CONDITION:", (int)(height1*0.343), (int)(height1*0.622), (int)(height1*0.59), menu1optionSize1));
        optionArea.add(new TextDiv("SLANT MODE:", (int)(height1*0.392), (int)(height1*0.822), (int)(height1*0.48), menu1optionSize1));

        optionPanel0 = new OptionPanel(0, (int)(height1*0.11), (int)(height1*1.25), menu1optionSize2);
        optionArea.add(optionPanel0);
        optionPanel0.add(new Option("10", optionPanel0, (int)(height1*0.10), 0, (int)(height1*0.2), menu1optionSize2));
        optionPanel0.add(new Option("15", optionPanel0, (int)(height1*0.42), 0, (int)(height1*0.2), menu1optionSize2));
        optionPanel0.add(new Option("20", optionPanel0, (int)(height1*0.74), 0, (int)(height1*0.2), menu1optionSize2));
        optionPanel0.add(new Option("25", optionPanel0, (int)(height1*1.06), 0, (int)(height1*0.2), menu1optionSize2));
        optionPanel0.selectOption(1);

        optionPanel1 = new OptionPanel(0, (int)(height1*0.31), (int)(height1*1.25), menu1optionSize2);
        optionArea.add(optionPanel1);
        optionPanel1.add(new Option("4", optionPanel1, (int)(height1*0.10), 0, (int)(height1*0.2), menu1optionSize2));
        optionPanel1.add(new Option("5", optionPanel1, (int)(height1*0.42), 0, (int)(height1*0.2), menu1optionSize2));
        optionPanel1.add(new Option("6", optionPanel1, (int)(height1*0.74), 0, (int)(height1*0.2), menu1optionSize2));
        optionPanel1.add(new Option("7", optionPanel1, (int)(height1*1.06), 0, (int)(height1*0.2), menu1optionSize2));
        optionPanel1.selectOption(1);

        optionPanel2 = new OptionPanel(0, (int)(height1*0.51), (int)(height1*1.25), menu1optionSize2);
        optionArea.add(optionPanel2);
        optionPanel2.add(new Option("ALLOWED", optionPanel2, (int)(height1*0.10), 0, (int)(height1*0.5), menu1optionSize2));
        optionPanel2.add(new Option("BANNED", optionPanel2, (int)(height1*0.74), 0, (int)(height1*0.5), menu1optionSize2));
        optionPanel2.selectOption(0);

        optionPanel3 = new OptionPanel(0, (int)(height1*0.71), (int)(height1*1.25), menu1optionSize2);
        optionArea.add(optionPanel3);
        optionPanel3.add(new Option("TRADITIONAL", optionPanel3, (int)(height1*0.1), 0, (int)(height1*0.6), menu1optionSize2));
        optionPanel3.add(new Option("ADVANCED", optionPanel3, (int)(height1*0.74), 0, (int)(height1*0.5), menu1optionSize2));
        optionPanel3.selectOption(0);

        optionPanel4 = new OptionPanel(0, (int)(height1*0.91), (int)(height1*1.25), menu1optionSize2);
        optionArea.add(optionPanel4);
        optionPanel4.add(new Option("ENABLED", optionPanel4, (int)(height1*0.1), 0, (int)(height1*0.6), menu1optionSize2));
        optionPanel4.add(new Option("DISABLED", optionPanel4, (int)(height1*0.74), 0, (int)(height1*0.5), menu1optionSize2));
        optionPanel4.selectOption(0);


        menu1.add(new TextDiv("INVITATION CODE:", (int)(height1*1.4), (int)(height1*0.058), (int)(height1*0.98), menu1optionSize1));

        invitationCodeField = new TextField("hello", 10);
        invitationCodeField.setBounds((int)(height1*1.4), (int)(height1*0.171), (int)(height1*0.5), menu1optionSize2);
        invitationCodeField.setFont(Main.font_twcenmt.deriveFont(Font.PLAIN, (int)(menu1optionSize2*0.8)));
        menu1.add(invitationCodeField);

        buttonJoin = new Button(" JOIN", (int)(height1*1.95), (int)(height1*0.171), (int)(height1*0.2), menu1optionSize2);
        buttonJoin.addMouseListener(new MouseAdapter(){
        	public void mousePressed(MouseEvent e)
        	{
                String serverCode = invitationCodeField.getText();
                if (gui.newGuest(serverCode))
                {
                    optionArea.setVisible(true);
                }
            }
        });
        menu1.add(buttonJoin);


        playerArea = new Div((int)(height1*1.4), (int)(height1*0.33), (int)(height1*0.5), (int)(height1*0.7));
        menu1.add(playerArea);
        playerArea.add(new TextDiv("PLAYERS:", 0, 0, (int)(height1*0.5), menu1optionSize1));
        roomPlayerList = new RoomPlayerList(0, (int)(height1*0.1), playerArea.getWidth(), (int)(height1*0.1));
        playerArea.add(roomPlayerList);

        menu2 = new ButtonList((int)(height1*1.95), (int)(height1*0.6), 2, (int)(height1*0.25), (int)(height1*0.15));
        menu2.list[0].add(new ButtonText("Start", menu2));
        menu2.list[0].addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e)
            {
                gui.startGame();
            }
        });
        menu2.list[1].add(new ButtonText("Back", menu2));
        menu2.list[1].addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e)
            {
                gui.exitGame();
            }
        });

        menu1.add(menu2);

        menu3 = new ButtonList((int)(height1*1.95), (int)(height1*0.75), 1, (int)(height1*0.25), (int)(height1*0.15));
        menu3.list[0].add(new ButtonText("Back", menu3));
        menu3.list[0].addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e)
            {
                gui.exitGame();
            }
        });
        menu1.add(menu3);

        add(menu1);
        menu1.repaint();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setFont(Main.font_twcenmt.deriveFont(Font.PLAIN, titleFontSize));
        g.setColor(Color.white);
        g.drawString("LiTZ!", Main.windowWidth - 2*titleFontSize, (int)(titleFontSize*0.8));
    }

    public void edit() {
        gui.client.editRule((byte)(optionPanel1.selectedIndex + 4), optionPanel3.selectedIndex == 0, optionPanel2.selectedIndex == 0, optionPanel4.selectedIndex == 0);
        gui.client.editBoardSize((byte)((optionPanel0.selectedIndex+2)*5));
    }
}
