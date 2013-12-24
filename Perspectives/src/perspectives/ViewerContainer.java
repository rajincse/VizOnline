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
public class ViewerContainer{
	
	//used to implement double buffering for 2d and 3d viewers
	BufferedImage finalImage = null;
	
	 
	//used to do rendering and simulation in the background and display results only when done so that the window remains responsive in between
	
	boolean working = false;//indicates whether the thread is still working in the background
	 
	Viewer viewer;
	private Viewer2D v2d;
	
	private Viewer3D v3d;
	
	
	//a pointer to the parent Environment class (needed for instance to delete the viewer from the Environment if the user activates the 'X')
	Environment env;
	
	int width, height;
	
	Thread thread = null;
	

	
	public ViewerContainer(Viewer v, Environment env, int width, int height)
	{		
		this.env = env;		
		final Environment envtmp = env;
		
		this.width = width;
		this.height = height;
		
		viewer = v;

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

	
	
	
	public BufferedImage getImage()
	{
		return finalImage;
	}
	
	
	

	
	public void keyPressed(KeyEvent e)
	{
		
	}

	public void keyReleased(KeyEvent e) {					
	}
	public void keyTyped(KeyEvent arg0) {
	}	
	
	
	public void mouseClicked(int ex, int ey, int button) {
	}
	public void mouseEntered(int ex, int ey) {
	}
	public void mouseExited(int ex, int ey) {
	}
	public void mousePressed(int ex, int ey, int button) {

	}
	public void mouseReleased(int ex, int ey, int button)
	{
		
	}	
	
	
	
	
	public void mouseDragged(int ex, int ey) {
		

	}
	public void mouseMoved(int ex, int ey)
	{
		
	}





}
