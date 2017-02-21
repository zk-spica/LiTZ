package litz;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Button extends Div
{
	static Color normalBackgroundColor = new Color(255,255,255,80);
	static Color hoverBackgroundColor = new Color(255,255,255,160);
	static Color normalTextColor = Color.black;
	static Color pressTextColor = Color.white;
	private TextDiv textDiv;

	public Button(String text, int posx, int posy, int width, int height)
	{
		super(posx, posy, width, height, normalBackgroundColor);
		textDiv = new TextDiv(text, (int)(width*0.1), (int)(height*0.1), (int)(width*0.8), (int)(height*0.8), normalTextColor);
		add(textDiv);

		this.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e) {}

            public void mouseEntered(MouseEvent e)
            {
                setBackground(hoverBackgroundColor);
                Main.rootPanel.repaint();
            }

            public void mouseExited(MouseEvent e)
            {
                setBackground(normalBackgroundColor);
                textDiv.setForeground(normalTextColor);
                Main.rootPanel.repaint();
            }

            public void mousePressed(MouseEvent e)
            {
                textDiv.setForeground(pressTextColor);
                Main.rootPanel.repaint();
            }

            public void mouseReleased(MouseEvent e)
            {
            	textDiv.setForeground(normalTextColor);
                Main.rootPanel.repaint();
            }
        });
	}
}