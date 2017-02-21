package litz;

import java.awt.Color;

public class Dot
{
	int x, y;
	Color col;
	
	public Dot() {}
	
	public Dot(int _x, int _y, int colorIndex)
	{
		x = _x;
		y = _y;
		col = PawnPanel.pawnColor[colorIndex];
	}
}