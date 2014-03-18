package perspectives.util;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Oval extends Rectangle {

	public Oval(double x, double y, double w, double h) {
		super(x, y, w, h);
	}
	
	protected void draw(Graphics2D g)
	{
		g.setColor(this.c);
		g.fillOval(0, 0, (int)w, (int)h);
	}
}
