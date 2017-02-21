package litz;

import java.awt.Point;
import java.util.TimerTask;

import javax.swing.JComponent;

public class TaskTranslationGrav extends TimerTask
{
	static double dt = 1000.0/60;
	static private double dL=50, dT=800, k=1-Math.pow(dL, -dt/dT);
    private JComponent com;
    private Animator ani;
    private Point p = new Point();
    private double posx, posy;

    public TaskTranslationGrav(JComponent _com, Animator _ani)
    {
        com = _com;
        ani = _ani;
        posx = com.getLocation().x;
        posy = com.getLocation().y;
    }

    @Override
    public void run()
    {
        posx += k*(ani.aimx-posx);
        posy += k*(ani.aimy-posy);
        if (Math.abs(posx-ani.aimx) < 0.1) posx = ani.aimx;
        if (Math.abs(posy-ani.aimy) < 0.1) posy = ani.aimy;
        p.x = (int)posx;
        p.y = (int)posy;
        com.setLocation(p);
        Main.rootPanel.repaint();
        if (p.x == ani.aimx && p.y == ani.aimy)
        {
        	Main.animatorMap.remove(com);
            cancel();
        }
    }
}