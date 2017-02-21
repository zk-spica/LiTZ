package litz;

public class OptionPanel extends Div
{
    int selectedIndex, height, nOptions;
    Option[] options = new Option[5];

    public OptionPanel(int posx, int posy, int width, int _height)
    {
        super(posx, posy, width, _height);
        height = _height;
    }

    public void selectOption(int index)
    {
        int i;
        for (i=0; i<nOptions; ++i) options[i].checkbox.setIcon(Option.icon_uncheck);
        options[index].checkbox.setIcon(Option.icon_check);
        selectedIndex = index;
        Main.rootPanel.repaint();
    }
}
