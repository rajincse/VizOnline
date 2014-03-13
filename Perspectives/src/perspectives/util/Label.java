package perspectives.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Label extends Rectangle {
	
	
	String text;
	Font font;
	
	Color fontColor;
	
	public Label(int x, int y, String label)
	{
		super(x,y,0,0);
		text = label;
		font = new Font("Sans-Serif",Font.PLAIN,10);
		fontColor = Color.black;
		fitSize();	
	}
	

	public void draw(Graphics2D g)
	{
		super.draw(g);
		g.setColor(fontColor);
		g.setFont(font);
		g.drawString(text,0+font.getSize()/2,(int)h-font.getSize()/4);
	}
	
	public void fitSize()
	{
		BufferedImage bi = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);			
		FontMetrics fm = bi.getGraphics().getFontMetrics(font);
		java.awt.geom.Rectangle2D rect = fm.getStringBounds(text, bi.getGraphics());
		w = rect.getWidth() + font.getSize();
		h = rect.getHeight();
	}
	
	public void setFont(Font f)
	{
		this.font = f;
		fitSize();
	}
	
	public Font getFont()
	{
		return font;
	}
	
}
