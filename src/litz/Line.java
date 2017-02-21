package litz;

import java.awt.Color;

public class Line
{
	int x1, x2, y1, y2;
	Color col;
	
	public Line() {}
	
	public Line(int _x1, int _y1, int _x2, int _y2, int colorIndex)
	{
		x1 = _x1;
		y1 = _y1;
		x2 = _x2;
		y2 = _y2;
		col = PawnPanel.pawnColor[colorIndex];
	}
}