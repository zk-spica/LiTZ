package litz;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class TextDiv extends JLabel
{
    public TextDiv(String text, int posx, int posy, int width, int height)
    {
        super(text);
        int fontSize = height;
        setBounds(posx, posy, width, height);
        setFont(Main.font_twcenmt.deriveFont(Font.PLAIN, fontSize));
        setForeground(Color.white);
    }

    public TextDiv(String text, int posx, int posy, int width, int height, Color col)
    {
        this(text, posx, posy, width, height);
        setForeground(col);
    }
}