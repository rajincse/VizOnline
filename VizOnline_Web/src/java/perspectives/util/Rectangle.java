package perspectives.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Rectangle {
	
	public double x;
	public double y;
	public double w;
	public double h;
	double ax, ay;
	double r;
	
	
	Color c;
	public Rectangle(double x, double y, double w, double h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		r = 0;
		this.c = Color.gray;
	}
	
	public void setRotationAngle(double rad)
	{
		r = rad;
	}
	public double getRotationAngle()
	{
		return r;
	}
	public void setAnchor(double anchorX, double anchorY)
	{
		ax = anchorX;
		ay = anchorY;
	}
	
	public void setColor(Color c)
	{
		this.c = c;
	}
	
	protected void draw(Graphics2D g)
	{
		g.setColor(this.c);
		g.fillRect(0, 0, (int)w, (int)h);
	}
	
	public void render(Graphics2D g)
	{
		g.translate(x, y);
		g.translate(ax,ay);
		
		g.rotate(r);
		g.translate(-w/2, -h/2);
		g.translate(-ax,-ay);
		
		draw(g);
		
		g.translate(ax, ay);
		g.translate(w/2, h/2);
		g.rotate(-r);	
		g.translate(-ax,-ay);
		g.translate(-x, -y);		
	}
	
	public boolean contains(double xx, double yy)
	{
		AffineTransform at = new AffineTransform();
		
		
		at.translate(ax, ay);
		at.translate(w/2, h/2);
		at.rotate(-r);	
		at.translate(-ax,-ay);
		at.translate(-x, -y);
		
		Point2D.Double p = new Point2D.Double();
		at.transform(new Point2D.Double(xx,yy), p);
		if (p.x >= 0 && p.y>=0 && p.x<=w && p.y<=h)
		{			
			return true;
		}
		
		return false;

	}
	
	
}
