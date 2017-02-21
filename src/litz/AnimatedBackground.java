package litz;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashSet;
import java.util.Iterator;

public class AnimatedBackground extends Div
{
    HashSet<Primitive> S = new HashSet<Primitive>();
    private Iterator<Primitive> it;
    private Graphics2D g2;
    private Primitive p;

    AnimatedBackground(int posx, int posy, int height, int width, Color col)
    {
        super(posx, posy, height, width, col);

        Primitive a = new Primitive("Rect", (int)(0.68*Main.windowHeight));
        a.posx = (int)(0.12*Main.windowHeight);
        a.posy = (int)(0.72*Main.windowHeight);
        a.vel_translation = 0;
        a.vel_rotation = 0.0045;
        a.col = new Color(156, 183, 197);
        S.add(a);
        
        int i, w, tot=8;
        for (i=1; i<=tot; ++i)
        {
            w = (int)(Math.random()*100+100);
            a = new Primitive("Rect", w);
            a.posx = Math.random()*Main.windowWidth/2;
            if (i <= tot/2) a.posx += Main.windowWidth/2;
            S.add(a);
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
    	g2 = (Graphics2D)g;
    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g2);
        
        it = S.iterator();
        while (it.hasNext())
        {
            p = (Primitive)it.next();
            g2.setColor(p.col);
            g2.fillPolygon(p.vx, p.vy, p.n);
        }
    }
}