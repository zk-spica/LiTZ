package litz;

import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;

public class ButtonText extends JLabel
{
    static int margin = (int)(Main.windowHeight*0.03);
    static int fontSize = (int)(Main.windowHeight*0.14);

    public ButtonText(String text, ButtonList menu)
    {
        super(text);
        setBounds(menu.width/10, 0, menu.width, menu.buttonHeight);
        setFont(new Font("Arial", Font.PLAIN, menu.buttonHeight/2));
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }
}
