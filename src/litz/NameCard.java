package litz;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class NameCard extends Div
{
	static int height = (int)(0.07*Main.windowHeight), width = (int)(0.18*Main.windowWidth), margin = height/8;
    private int playerID;
    
    public NameCard(int posx, int posy, int _playerID)
    {
        super(posx, posy, width, height);
        playerID = _playerID;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(PawnPanel.pawnColor[playerID-1]);
        g.fillOval(margin, margin, height-margin*2, height-margin*2);
        g.setColor(Color.black);
        g.setFont(new Font("TimesRoman", Font.PLAIN, height/2-margin));
        g.drawString(Main.playerName[playerID-1], (int)(height*1.1), (int)(height*0.63));
    }
}