package perspectives.two_d;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;

import perspectives.base.Viewer;
import perspectives.base.ViewerContainer;


/**
 * Base class for Viewers doing 2D graphics. Developers should overload render for the actual graphics. They should overload simulate for animation or computational changes. Developers should not try to overload paint, repaint etc. The viewer rendering is refreshed automatically
 * with double buffering. Developers can block automatic rendering by reimplementing the method skipRendering. As long as skipRendering returns true the paint method will not be called. <br>
 * 
 * Viewer2D is generally contained in a ViewerContainer which provides automatic panning (left mouse button down drag, or arrows), and zooming (right mouse button down, or + - keys). If a Viewer2D implementation needs to use mouse press and dragging for something else (e.g.,
 * dragging nodes in a graph visualization) then those functions should return true indicating that the functionality has already been processed. Consult some of the examples.
 * <br>
 * @author rdjianu
 *
 */
public abstract class Viewer2D extends Viewer {
	
	protected boolean antialiasing = true;
	public boolean antialiasingSet = false;	
	
	Color backgroundColor;
	
		
	int mouseX;
	int mouseY;	
	
	double defaultZoom;
	
	public Viewer2D(String name)
	{
		super(name);
		
		mouseX = 0;
		mouseY = 0;
		
		
		backgroundColor = Color.white;
		defaultZoom = 1;
		
	}
	public abstract void render(Graphics2D g);
		

	public abstract void simulate();
	
	public boolean mousepressed(int x, int y, int button) {return false;};
	public boolean mousereleased(int x, int y, int button) {return false;};
	public boolean mousemoved(int x, int y)
	{ 
		return false;
	};
	public boolean mousedragged(int currentx, int currenty, int oldx, int oldy){return false;};
	public void zoomed(double zoom){};
	public void keyPressed(String key, String modifiers) {};
	public void keyReleased(String key, String modifiers) {};
	
	public String getViewerType()
	{
		return "Viewer2D";
	}
	
	public void setBackgroundColor(Color c)
	{
		backgroundColor = c;
	}
	
	public Color getBackgroundColor()
	{
		return backgroundColor;
	}
	
	public double getDefaultZoom()
	{
		return defaultZoom;
	}
	
	public void setDefaultZoom(double z)
	{
		defaultZoom = z;
	}
	
	public void enableAntialising(boolean yes)
	{
		antialiasing = yes;
		antialiasingSet = false;
	}

	
	public void setZoom(double z)
	{
		if (getContainer() != null)
		{
			((ViewerContainer2D)getContainer()).setZoom(z);
			this.requestRender();
		}
	}
	
	public void setTranslation(int tx, int ty)
	{
		if (getContainer() != null)
		{
			((ViewerContainer2D)getContainer()).setTranslation(tx,ty);
			this.requestRender();
		}
	}
	


}
