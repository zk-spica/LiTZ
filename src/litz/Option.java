package litz;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Option extends Div
{
    static Image image_check, image_uncheck;
    static ImageIcon icon_check = null, icon_uncheck = null;
    JLabel checkbox;
    private OptionPanel optionPanel;
    private Color optionColor = new Color(216, 231, 237);

    public Option(String text, OptionPanel _optionPanel, int posx, int posy, int width, int height)
    {
        super(posx, posy, width, height);
        optionPanel = _optionPanel;

        if (icon_check == null)
        {
            icon_check = new ImageIcon(image_check.getScaledInstance((int)(optionPanel.height*1.208), optionPanel.height, Image.SCALE_SMOOTH));
            icon_uncheck = new ImageIcon(image_uncheck.getScaledInstance((int)(optionPanel.height*1.208), optionPanel.height, Image.SCALE_SMOOTH));
        }

        checkbox = new JLabel(icon_uncheck);
        checkbox.setSize((int)(optionPanel.height*1.208), optionPanel.height);
        add(checkbox);
        int fontSize = height;
        add(new TextDiv(text, (int)(optionPanel.height*1.3), 0, width-height, fontSize, optionColor));

        optionPanel.options[optionPanel.nOptions++] = this;

        this.addMouseListener(new MouseListener()
        {
            private int index = optionPanel.nOptions-1;

            public void mouseClicked(MouseEvent e) {}

            public void mouseEntered(MouseEvent e)
            {
            	if (!Main.mainMenu.optionEditable) return;
                setBackground(Main.color_transparent_60);
                Main.rootPanel.repaint();
            }

            public void mouseExited(MouseEvent e)
            {
            	if (!Main.mainMenu.optionEditable) return;
                setBackground(Main.color_transparent_0);
                Main.rootPanel.repaint();
            }

            public void mousePressed(MouseEvent e)
            {
            	if (!Main.mainMenu.optionEditable) return;
                optionPanel.selectedIndex = index;
                Main.mainMenu.edit();
            }

            public void mouseReleased(MouseEvent e) {}
        });

        repaint();
    }
}
