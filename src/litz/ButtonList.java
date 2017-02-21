package litz;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ButtonList extends Div
{
    int height, width, buttonHeight, buttonWidth;
    Div[] list;
    static Image selectorImage;
    boolean operatable = true;
    private JLabel selector;
    private int nButton, margin, movePadding, selectorIndex = 0;
    
    public ButtonList(int posx, int posy, int _nButton, int _buttonWidth, int _buttonHeight)
    {
        super();

        nButton = _nButton;
        buttonHeight = _buttonHeight;
        buttonWidth = _buttonWidth;
        margin = buttonHeight/4;
        height = _buttonHeight*nButton+margin*2;
        width = (int)(_buttonWidth*1.2)+margin*2;
        movePadding = width-margin*2-buttonWidth;
        setBounds(posx, posy, width, height);
        setBackground(new Color(255, 255, 255, 60));

        list = new Div[nButton];
        for (int i=0; i<nButton; ++i)
        {
            list[i] = new Div(0, margin + i*buttonHeight, (int)(buttonWidth*1.1), buttonHeight);
            add(list[i]);
            final int _i = i;
            list[i].addMouseListener(new MouseAdapter(){
                public void mouseEntered(MouseEvent e)
                {
                	if (operatable) moveSelector(_i);
                }
            });
        }

        ImageIcon icon = new ImageIcon(selectorImage.getScaledInstance(width, buttonHeight, Image.SCALE_SMOOTH));
        selector = new JLabel();
        selector.setIcon(icon);
        selector.setBounds(0, margin, width, buttonHeight);
        add(selector);

        Animator.animate(list[0], movePadding, 0);
    }

    public synchronized void moveSelector(int index)
    {
        Animator.animate(selector, 0, (index-selectorIndex)*buttonHeight);
        Animator.animate(list[selectorIndex], -movePadding, 0);
        Animator.animate(list[index], movePadding, 0);
        selectorIndex = index;
    }
}
