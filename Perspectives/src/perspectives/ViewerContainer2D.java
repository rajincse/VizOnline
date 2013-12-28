package perspectives;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SwingWorker;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 * This is a window class for Viewers. It will be able to accommodate any of the three main types of Viewers: 2D, 3D, GUI. It will call their rendering, simulation, and interactivity functions (if appropriate), it will implement double buffering for them, and it can be added to the viewer area
 * of the Environment. ViewerContainers automatically implementing panning and zooming for 2D viewers via 2dtransforms applied to the Graphics2D objects passed onto the Viewer2D's render function.
 * @author rdjianu
 *
 */
public class ViewerContainer2D extends ViewerContainer{
	
	
	int renderCount = 0;
	
	//apply to 2d viewers
	public double zoom = 1;
	public double translatex = 0;
	public double translatey = 0;
	
	//keeping track of dragging; 
	public boolean rightButtonDown = false;
	public int dragPrevX = 0;
	public int dragPrevY = 0;
	
	public int zoomOriginX = 0;
	public int zoomOriginY = 0;
	
	public AffineTransform transform = AffineTransform.getTranslateInstance(0, 0);
		
	JPanel drawArea;
	
	public ViewerContainer2D(Viewer2D v, Environment env, int width, int height)
	{
		super(v,env,width,height);
			
		zoom = v.getDefaultZoom();
		v.requestRender();
	
	}
	
	public void render()
	{
		if (renderCount >= 1)
			return;

		renderCount++;


		BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(),BufferedImage.TYPE_INT_ARGB);


		Graphics2D gc = image.createGraphics();

		gc.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

		gc.setColor(((Viewer2D)viewer).getBackgroundColor());
		gc.fillRect(0, 0, this.getWidth(), this.getHeight()); // fill in background

		if (!((Viewer2D)viewer).antialiasingSet)
		{
			if (((Viewer2D)viewer).antialiasing)
			{	            		
				gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);

				gc.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
						RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			}
			else
			{
				gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_OFF);

				gc.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
						RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
			}
		}

		zoomOriginX = this.getWidth()/2;
		zoomOriginY = this.getHeight()/2;

		//sets the proper transformation for the Graphics context that will passed into the rendering function	            


		gc.setTransform(AffineTransform.getTranslateInstance(zoomOriginX, zoomOriginY));	          
		gc.transform(AffineTransform.getScaleInstance(zoom,zoom));		           
		gc.transform(AffineTransform.getTranslateInstance(translatex-zoomOriginX, translatey-zoomOriginY));


		transform = gc.getTransform();

		final BufferedImage fimage = image;
		final Graphics2D gcf = gc;
		viewer.em.scheduleEvent(new PEvent()
		{
			public void process() {
				((Viewer2D)viewer).render(gcf);
				renderDoneCallback(fimage);
			}
		});	
	}
	
	public void renderDoneCallback(BufferedImage im)
	{
		renderCount--;		
		this.setViewerImage(im);
	}

	
	public void setWidth(int w)
	{
		width = w;
	}
	public void setHeight(int h)
	{
		height = h;
	}
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}

		

	//for 2D viewers we add automatic zooming and panning; this can happen using the arrow keys and +,- keys; the key strokes are also sent to the viewer as interaction events
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == e.VK_UP)
		{	translatey++; this.render();	}
		else if (e.getKeyCode() == e.VK_DOWN)
		{	translatey--;this.render();	}
		else if (e.getKeyCode() == e.VK_LEFT)
		{	translatex--;this.render();	}
		else if (e.getKeyCode() == e.VK_RIGHT)
		{	translatex++;this.render();	}
		else if (e.getKeyCode() == e.VK_PLUS || e.getKeyCode() == 61)
		{	zoom = zoom + 0.1;this.render();	}
		else if (e.getKeyCode() == e.VK_MINUS)
		{
			zoom = zoom - 0.1;
			if (zoom < 0.1)
				zoom = 0.1;
			this.render();
		}
		
		((Viewer2D)viewer).keyPressed(e.getKeyCode());
	}

	public void keyReleased(KeyEvent e) {
		((Viewer2D)viewer).keyReleased(e.getKeyCode());				
	}
	public void keyTyped(KeyEvent arg0) {
	}	
	
	

	//we add listeners both to send those events to the 2D viewer but also to implement zooming an panning; there's a 25pixel offset on the y-axes because of the title bar of ViewerContainers; for zooming and panning we use
	//the raw coordinates; when passing the coordinates to the viewer they need to be transformed so they are in the Viewer's space, hence the operations based on translate and zoom.
	
	public void mouseClicked(int ex, int ey, int button) {
	}
	public void mouseEntered(int ex, int ey) {
	}
	public void mouseExited(int ex, int ey) {
	}
	public void mousePressed(int ex, int ey, int button) {
		
		try{
			Point2D.Double tp = new Point2D.Double();
			transform.inverseTransform(new Point2D.Double(ex,ey), tp);
			int x = (int)tp.x;
			int y = (int)tp.y;
			
			((Viewer2D)viewer).mousepressed(x,y, button);
			
			
			dragPrevX = ex;
			dragPrevY = ey;
			
			if (button == MouseEvent.BUTTON3)
			{
				rightButtonDown = true;
				zoomOriginX = dragPrevX;
				zoomOriginY = dragPrevY;
			}
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
			
		}
	}
	public void mouseReleased(int ex, int ey, int button)
	{
		try{
			Point2D.Double tp = new Point2D.Double();
			transform.inverseTransform(new Point2D.Double(ex, ey), tp);
			int x = (int)tp.x;
			int y = (int)tp.y;
		
		
			((Viewer2D)viewer).mousereleased(x,y, button);
			
			if (button == MouseEvent.BUTTON3)
				rightButtonDown = false;

		}
		catch(Exception ee)
		{
			
		}
	}	
	
	public void mouseDragged(int ex, int ey) {
		
		((Viewer2D)viewer).setToolTipCoordinates(ex,ey);
		
		try{
			Point2D.Double tp = new Point2D.Double();
			transform.inverseTransform(new Point2D.Double(ex, ey), tp);
			int x = (int)tp.x;
			int y = (int)tp.y;
			
			transform.inverseTransform(new Point2D.Double(dragPrevX, dragPrevY), tp);
			int px = (int)tp.x;
			int py = (int)tp.y;
		
		if (!((Viewer2D)viewer).mousedragged(x,y, px, py))
		{
			if (rightButtonDown) //zoom
			{
				if (Math.abs(ex-dragPrevX) > Math.abs(ey-0 - dragPrevY))
					zoom *= (1+(ex-dragPrevX)/100.);
				else
					zoom *= (1+(ey-0-dragPrevY)/100.);
				if (zoom <0.01)
					zoom = 0.01;
			}
			else
			{
				translatex += (ex-dragPrevX)/zoom;
				translatey += (ey-dragPrevY)/zoom;
			}
			this.render();
		}
		dragPrevX = ex;
		dragPrevY = ey;
		
		}
		catch(Exception ee)
		{
			
			
		}
	}
	public void mouseMoved(int ex, int ey)
	{
		if (((Viewer2D)viewer) == null)
			return;
		
		((Viewer2D)viewer).setToolTipCoordinates(ex,ey);
		
		try{
			Point2D.Double tp = new Point2D.Double();
			transform.inverseTransform(new Point2D.Double(ex,ey), tp);
			int x = (int)tp.x;
			int y = (int)tp.y;	

		((Viewer2D)viewer).mousemoved(x,y);
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
		}
	}	
	

}
