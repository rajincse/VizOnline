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
import java.awt.image.DataBufferInt;
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

import com.keypoint.PngEncoder;

import properties.Property;


/**
 * This is a window class for Viewers. It will be able to accommodate any of the three main types of Viewers: 2D, 3D, GUI. It will call their rendering, simulation, and interactivity functions (if appropriate), it will implement double buffering for them, and it can be added to the viewer area
 * of the Environment. ViewerContainers automatically implementing panning and zooming for 2D viewers via 2dtransforms applied to the Graphics2D objects passed onto the Viewer2D's render function.
 * @author rdjianu
 *
 */
public class ViewerContainer{
	
	//used to implement double buffering for 2d and 3d viewers
	private BufferedImage viewerImage = null;
	private BufferedImage image = null;
	private BufferedImage lastImage = null;
	
	private BufferedImage[][] tiles;
	private BufferedImage[][] outTiles;
	private BufferedImage[][] tilesDif;
	
	Object o1 = new Object();
	Object o2 = new Object();
	
	int tilesX = 2;
	int tilesY = 2;
	
	
	ViewerWindow window = null;
		 
	Viewer viewer;	
		
	//a pointer to the parent Environment class (needed for instance to delete the viewer from the Environment if the user activates the 'X')
	Environment env;
	
	int width, height;
	
	long lastMouseMove;
	
	boolean tooltipOn;
	String prevTooltip;
	
	BufferedImage savedImage = null;
	boolean blocked = false;
	
	public ViewerContainer(Viewer v, Environment env, int width, int height)
	{		
		this.env = env;		
		
		this.width = width;
		this.height = height;
		
		tooltipOn = false;
		String prevTooltip = "";
		
		viewer = v;
		viewer.setContainer(this);	
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
	
	public void render()
	{

	}
	
	
	public BufferedImage getImage()
	{
		return image;
	}
	
	public void setViewerImage(BufferedImage im)
	{		
		this.viewerImage = im;
		
		if (!blocked)
			changeImage(im);
		else
			this.savedImage = im;
		
	    if (tooltipOn && prevTooltip.equals(viewer.getToolTipText()))
	     {
	        		BufferedImage imm = new BufferedImage(viewerImage.getWidth(), viewerImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
					Graphics2D gc = (Graphics2D)imm.getGraphics();
					
					gc.drawImage(viewerImage, 0, 0, null);					
					viewer.renderTooltip(gc);
					if (!blocked)					
						changeImage(imm);
					else
						savedImage = imm;
	     }
	     else
	     {
	    	 tooltipOn = false;
	    	 prevTooltip = "";
	     }
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
	
	public boolean unresponsive()
	{
		return viewer.em.unresponsive(2000);
	}
	

	public void block(boolean blocked)
	{
		if (this.blocked == blocked)
			return;
		
		this.blocked = blocked;
		
		Property[] ps = viewer.getProperties();
		for (int i=0; i<ps.length; i++)
			ps[i].setDisabled(blocked);
		
		lastImage = image;
		
		if (blocked)
		{
			BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D)im.createGraphics();
			g.drawImage(image, 0, 0, null);
			g.setColor(new Color(100,100,100,100));
			g.fillRect(0,0,im.getWidth(),im.getHeight());
			savedImage = image;
			changeImage(im);
		}
		else if (savedImage != null)
			changeImage(savedImage);
		
		
	}
	
	public void renderTooltip()
	{
		long delta = new Date().getTime()- lastMouseMove;	
		if (delta >= viewer.getTooltipDelay())
		{
			if (viewer.getToolTipText().length() > 0)
			{
				tooltipOn = true;
				prevTooltip = viewer.getToolTipText();
			}
		}
			
	     if (tooltipOn)
	     {
	        		BufferedImage im = new BufferedImage(viewerImage.getWidth(), viewerImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
					Graphics2D gc = (Graphics2D)im.getGraphics();
					
					gc.drawImage(viewerImage, 0, 0, null);					
					viewer.renderTooltip(gc);
					
					changeImage(im);
					
	     }  		
	}
	
	public void changeImage(BufferedImage newimage)
	{
		synchronized(o2)
		{
			image = newimage;
			tiles = tileImage(image, tilesX, tilesY);				
		
			BufferedImage difImage = diffImage(image,lastImage);		
			tilesDif = tileImage(difImage, tilesX, tilesY);		
			
			o2.notifyAll();
		}
	}
	
	
	
	
	public BufferedImage getTile(int x, int y, boolean diff, boolean wait)
	{
		BufferedImage ret = null;
		synchronized(o1)
		{
		if (outTiles == null)
		{
			System.out.println("out tiles ============null");
			synchronized(o2)
			{
				if (wait && lastImage == image)
				{System.out.println("-------before waiting");
					try {
						o2.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}			
			
				lastImage = image;
				boolean sendFullTiles = false;
				if (diff)
				{
					outTiles = tilesDif; 
					//we can't send the diff images multiple times; if one of the
					//dif tiles is null it means it was already sent; so we will send full tiles instead
					
					for (int i=0; i<outTiles.length; i++)
						for (int j=0; j<outTiles[i].length; j++)
							if (outTiles[i][j] == null)
								sendFullTiles = true;
				}
				
				if (!diff || sendFullTiles)
				{
					outTiles = new BufferedImage[tiles.length][];
					for (int i=0; i<outTiles.length; i++)
					{
						outTiles[i] = new BufferedImage[tiles[i].length];
						for (int j=0; j<outTiles[i].length; j++)
							outTiles[i][j] = tiles[i][j];
					}
				}
			}			
			
			o1.notify();	
		}
		else if (outTiles != null && outTiles[x][y] == null)
		{
			synchronized(o1)
			{
				try {
					o1.wait();
				
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		ret = outTiles[x][y];		
		
		outTiles[x][y] = null;
		for (int i=0; i<outTiles.length; i++)
			for (int j=0; j<outTiles[i].length; j++)
				if (outTiles[i][j] != null)
					return ret;
		
		outTiles = null;
		}
		return ret;
	}
	
	
	
	private BufferedImage diffImage(BufferedImage image, BufferedImage lastImage)
	{   

	        BufferedImage dif = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
	        dif.createGraphics().drawImage(image, 0,0,null);
	        
	        if (lastImage == null) return dif;
	        
	        int[] cpixels = ((DataBufferInt) dif.getRaster().getDataBuffer()).getData();
	        int[] spixels = ((DataBufferInt) lastImage.getRaster().getDataBuffer()).getData();

	        int[] calpha = ((DataBufferInt) dif.getAlphaRaster().getDataBuffer()).getData();

	        for (int i = 0; i < cpixels.length; i += 1)
	        {
	                int r1 = (cpixels[i]) & 0xFF;
	                int g1 = (cpixels[i] >> 8) & 0xFF;
	                int b1 = (cpixels[i] >> 16) & 0xFF;
	                int r2 = (spixels[i]) & 0xFF;
	                int g2 = (spixels[i] >> 8) & 0xFF;
	                int b2 = (spixels[i] >> 16) & 0xFF;

	                if (r1 == r2 && g1 == g2 && b1 == b2)
	                {
	                    cpixels[i] = 0;
	                    calpha[i] = 0;
	                }
	        }

	        return dif;
	}
	
	private BufferedImage[][] tileImage(BufferedImage image, int tileX, int tileY)
	{
		BufferedImage[][] tiles = new BufferedImage[tileX][];
		int tileWidth = image.getWidth()/tileX;
		int tileHeight = image.getHeight()/tileY;
		for (int i=0; i<tileX; i++)
		{
			tiles[i] = new BufferedImage[tileY];
			for (int j=0; j<tileY; j++)
			{
				tiles[i][j] = new BufferedImage(tileWidth, tileHeight, BufferedImage.TYPE_INT_ARGB);
				tiles[i][j].createGraphics().drawImage(image, 0, 0, tileWidth, tileHeight,  i*tileWidth, j*tileHeight, (i+1)*tileWidth, (j+1)*tileHeight, null);
			}
		}
		
		return tiles;
	}

}
