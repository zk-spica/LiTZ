package litz;

import java.util.Iterator;
import java.util.TimerTask;

public class TaskAnimateBackground extends TimerTask
{
    private AnimatedBackground bg;
    private int i, cnt, w;
    private Iterator<Primitive> it;
    private Primitive p;

    public TaskAnimateBackground(AnimatedBackground _bg)
    {
        bg = _bg;
    }

    @Override
    public void run()
    {
        it = bg.S.iterator();
        cnt = 0;
        while (it.hasNext())
        {
            p = (Primitive)it.next();
            if (p.posx < Main.windowWidth/2 && p.posx+p.vel_translation >= Main.windowWidth/2) ++cnt;
            p.posx += p.vel_translation;
            p.theta += p.vel_rotation;
            for (i=0; i<p.n; ++i)
            {
                p.vx[i] = (int)(Math.cos(p.theta + Math.PI / 2 * i) * p.diag + p.posx);
                p.vy[i] = (int)(Math.sin(p.theta + Math.PI / 2 * i) * p.diag + p.posy);
            }
            if (p.posx-p.diag > Main.windowWidth) it.remove();
        }

        while (cnt-- > 0)
        {
            w = (int)(Math.random()*100+100);
            p = new Primitive("Rect", w);
            p.posx = -w*(1+Math.random()*0.5);
            bg.S.add(p);
        }
        Main.appWindow.repaint();
    }
}