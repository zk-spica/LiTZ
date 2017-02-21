package litz;

import java.awt.Point;
import java.util.Timer;

import javax.swing.JComponent;

public class Animator
{
    int aimx, aimy;

    private Animator(int x, int y)
    {
        aimx = x;
        aimy = y;
    }

    static public void animate(JComponent com, int dx, int dy)
    {
        Animator ani = Main.animatorMap.get(com);
        if (ani == null)
        {
            Point p = com.getLocation();
            ani = new Animator(p.x+dx, p.y+dy);
            Main.animatorMap.put(com, ani);
            (new Timer()).scheduleAtFixedRate(new TaskTranslationGrav(com, ani), 0, (long)TaskTranslationGrav.dt);
        }
        else
        {
            ani.aimx += dx;
            ani.aimy += dy;
        }
    }
}
