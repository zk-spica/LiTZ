package litz;

import java.awt.Color;

public class Primitive
{
    int[] vx, vy;
    int n, diag;
    Color col;
    double posx, posy, vel_translation, vel_rotation, theta;

    public static Color generateColor()
    {
        int r, g, b, range=30;
        int bri = (int)(Math.random()*5);
        r = (int)(120+(Math.random()-0.5)*range + bri);
        g = (int)(153+(Math.random()-0.5)*range + bri);
        b = (int)(171+(Math.random()-0.5)*range + bri);
        if (r > 255) r = 255;
        if (r < 0) r = 0;
        if (g > 255) g = 255;
        if (g < 0) g = 0;
        if (b > 255) b = 255;
        if (b < 0) b = 0;
        return new Color(r, g, b);
    }

    public Primitive(String type, int _diag)
    {
        if (type.equals("Rect"))
        {
            int i;
            n = 4;
            diag = _diag;
            theta = Math.PI*Math.random();
            col = generateColor();
            posx = -diag;
            posy = (int)(Math.random()*Main.windowHeight);

            vel_translation = Math.random()*1+0.2;
            vel_rotation = Math.random()*0.005+0.002;
            vx = new int[4];
            vy = new int[4];
            for (i=0; i<n; ++i)
            {
                vx[i] = (int)(Math.cos(theta + Math.PI / 2 * i) * diag + posx);
                vy[i] = (int)(Math.sin(theta + Math.PI / 2 * i) * diag + posy);
            }
        }
    }
}
