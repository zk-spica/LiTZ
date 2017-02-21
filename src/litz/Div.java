package litz;

import java.awt.Color;

import javax.swing.JPanel;

public class Div extends JPanel
{
    public Div()
    {
        super();
        setLayout(null);
    }

    public Div(int posx, int posy, int width, int height, Color col)
    {
        super();
        setLayout(null);
        setBackground(col);
        setBounds(posx, posy, width, height);
    }

    public Div(int posx, int posy, int width, int height)
    {
        super();
        setLayout(null);
        setBackground(new Color(255,255,255,0));
        setBounds(posx, posy, width, height);
    }
}