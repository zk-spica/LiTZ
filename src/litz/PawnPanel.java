package litz;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class PawnPanel extends Div
{
    static Color pawnColor[] = new Color[]{new Color(150,230,209), new Color(86,152,241), new Color(179,119,251), new Color(243,103,111), new Color(245,217,55)};
    private int co_x, co_y, margin, pawnSize, gridWidth;
    private Graphics2D g2;

    public PawnPanel(int posx, int posy, int height, int width, int _co_x, int _co_y)
    {
        super(posx, posy, height, width);
        co_x = _co_x;
        co_y = _co_y;
        margin = (int)(width*0.1);
        pawnSize = width - margin*2;
        gridWidth = width;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
    	g2 = (Graphics2D)g;
    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g);
        if (Main.chessBoard.map[co_x][co_y] != 0)
        {
            g2.setColor(pawnColor[Main.chessBoard.map[co_x][co_y]-1]);
        	g2.fillOval(margin, margin, pawnSize, pawnSize);
        	if (Main.chessBoard.lastPawn[co_x][co_y] != 0) g2.drawRect(0, 0, gridWidth-1, gridWidth-1);
        }
    }
}