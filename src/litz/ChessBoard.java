package litz;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static litz.Main.*;

public class ChessBoard extends Div
{
	int map[][], lastPawn[][];
    private static int gridWidth, bodyMargin, stroke, dotRadius;
    private int row, col, nPlayer, currentPlayer;
    private Line line[] = new Line[100];
    private Dot dot[] = new Dot[100];
	private int nLine = 0, nDot = 0;

    public void putPawn(int x, int y, int player)
    {
        map[x][y] = player;
        for (int i=0; i<row; ++i)
        for (int j=0; j<col; ++j)
        if (lastPawn[i][j] == player) lastPawn[i][j] = 0;
        lastPawn[x][y] = player;
        currentPlayer = player % nPlayer + 1;
        repaint();
        Main.playerList.moveSelector(currentPlayer-1);
        Main.rootPanel.repaint();
    }

    public ChessBoard(int n, int m, int _nPlayer, int posx, int posy, int width)
    {
        int i, j, x ,y;
        row = n;
        col = m;
        nPlayer = _nPlayer;
        map = new int[row][col];
        lastPawn = new int[row][col];
        currentPlayer = 1;
        setBackground(new Color(255, 255, 255, 100));
        setBounds(posx, posy, width, width);
        gridWidth = (int)(width*0.95/n);
        dotRadius = gridWidth/6;
        bodyMargin = (width - gridWidth*(n-1))/2;
        if (n > 19) stroke = 1; else stroke = 2;

        for (i=0; i<row; ++i)
        for (j=0; j<col; ++j)
        {
            x = bodyMargin+gridWidth*j-gridWidth/2;
            y = bodyMargin+gridWidth*i-gridWidth/2;
            
            PawnPanel cross = new PawnPanel(x, y, gridWidth, gridWidth, i, j);
            add(cross);
            
            final int _i = i, _j = j;
            cross.addMouseListener(new MouseListener()
            {
                private int co_x = _i, co_y = _j; 
                
                public void mouseClicked(MouseEvent e)
                {
                    System.out.println("mouseClicked "+co_x+" "+co_y);    
                    Main.requestPutPawn(co_x, co_y, currentPlayer);
                }
                
                public void mouseEntered(MouseEvent e)
                {
                	Line line[] = new Line[100]; 
                	Dot dot[] = new Dot[100];
                	int nLine = 0, nDot = 0;
                	Connection[][] connection = gui.client.getConnections(new Point((byte)co_x, (byte)co_y));
                	for (int i=0; i<connection.length; ++i)
                	if (connection[i] != null)
                	{
                		for (Connection conn : connection[i])
                		{
                			int lstx = co_x, lsty = co_y;
                			for (Point p : conn.connected)
                			{
                				line[nLine++] = new Line(bodyMargin+lsty*gridWidth, bodyMargin+lstx*gridWidth, bodyMargin+p.y*gridWidth, bodyMargin+p.x*gridWidth, i);
                				lstx = p.x;
                				lsty = p.y;
                			}
                			lstx = co_x; lsty = co_y;
                			for (Point p : conn.separated)
                			{
                				line[nLine++] = new Line(bodyMargin+lsty*gridWidth, bodyMargin+lstx*gridWidth, bodyMargin+p.y*gridWidth, bodyMargin+p.x*gridWidth, i);
                				lstx = p.x;
                				lsty = p.y;
                			}
                			for (Point p : conn.blank)
                			{
                				dot[nDot++] = new Dot(bodyMargin+p.y*gridWidth-dotRadius, bodyMargin+p.x*gridWidth-dotRadius, i);
                			}
                		}
                	}
                	Main.chessBoard.line = line;
                	Main.chessBoard.nLine = nLine;
                	Main.chessBoard.dot = dot;
                	Main.chessBoard.nDot = nDot;
                }
                
                public void mouseExited(MouseEvent e)
                {
                	Main.chessBoard.nLine = 0;
                	Main.chessBoard.nDot = 0;
                }
                
                public void mousePressed(MouseEvent e)
                {}
                
                public void mouseReleased(MouseEvent e)
                {}
            });
        }
        
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D bodyGraph = (Graphics2D)g;
        
        bodyGraph.setStroke(new BasicStroke(stroke));
        bodyGraph.setColor(new Color(0,0,0));

        int i, j, x, y;
        for (j=0; j<col; ++j)
        {
            x = bodyMargin + j*gridWidth;
            bodyGraph.drawLine(x, bodyMargin, x, bodyMargin+(row-1)*gridWidth);
        }
        for (i=0; i<row; ++i)
        {
            y = bodyMargin + i*gridWidth;
            bodyGraph.drawLine(bodyMargin, y, bodyMargin+(col-1)*gridWidth, y);
        }
        
        bodyGraph.setStroke(new BasicStroke(2));
        for (i=0; i<nLine; ++i)
        {
        	bodyGraph.setColor(line[i].col);
        	bodyGraph.drawLine(line[i].x1, line[i].y1, line[i].x2, line[i].y2);
        }
        
        for (i=0; i<nDot; ++i)
        {
        	bodyGraph.setColor(dot[i].col);
        	bodyGraph.fillOval(dot[i].x, dot[i].y, dotRadius*2, dotRadius*2);
        }
    }
}
