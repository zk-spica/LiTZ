package litz;

import static litz.Main.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

public class RoomPlayerList extends ButtonList
{
	public int myIndex = -1;
	private static int maxnPlayer = 5;
	private TextField nameEditor = null;
	private TextDiv player[] = new TextDiv[maxnPlayer];

	public RoomPlayerList(int posx, int posy, int _buttonWidth, int _buttonHeight)
	{
		super(posx, posy, maxnPlayer, _buttonWidth, _buttonHeight);
		for (int i=0; i<maxnPlayer; ++i)
		{
			player[i] = new TextDiv("---", (int)(buttonWidth*0.1), (int)(buttonHeight*0.1), buttonWidth, (int)(buttonHeight*0.8), Color.black);
			player[i].setFont(Main.font_twcenmt.deriveFont(Font.PLAIN, (int)(buttonHeight*0.8)));
			list[i].add(player[i]);
		}
	}

	public void initialize(int _myIndex)
	{
		myIndex = _myIndex;

        player[myIndex].setVisible(false);
		nameEditor = new TextField(player[myIndex].getText(), 8);
		nameEditor.setBounds((int)(buttonWidth*0.1), (int)(buttonHeight*0.1), (int)(buttonWidth*0.7), (int)(buttonHeight*0.8));
		nameEditor.setFont(Main.font_twcenmt.deriveFont(Font.PLAIN, (int)(buttonHeight*0.6)));
		nameEditor.addTextListener(new TextListener(){
			public void textValueChanged(TextEvent arg0)
			{
				System.out.println("send name gettext="+nameEditor.getText());
				gui.client.editPlayer(nameEditor.getText(), (byte)-1);
			}
		});
		list[myIndex].add(nameEditor);
		System.out.println("initialize myindex="+myIndex+" gettext="+player[myIndex].getText());
		Main.mainMenu.playerArea.setVisible(true);
	}

	public void updatePlayer(int index, String name)
	{
		System.out.println("update player index="+index+" name="+name);
		if (index == myIndex && nameEditor != null)
		{
			System.out.println("initialize my name");
			String tmp = nameEditor.getText();
			System.out.println(tmp);
			if (tmp.equals("---")) nameEditor.setText(name);
			return;
		}
		player[index].setText(name);
	}

	void reset()
	{
		myIndex = -1;
		for (int i=0; i<maxnPlayer; ++i)
		{
			list[i].removeAll();
			player[i] = new TextDiv("---", (int)(buttonWidth*0.1), (int)(buttonHeight*0.1), buttonWidth, (int)(buttonHeight*0.8), Color.black);
			player[i].setFont(Main.font_twcenmt.deriveFont(Font.PLAIN, (int)(buttonHeight*0.8)));
			list[i].add(player[i]);
		}
	}
}