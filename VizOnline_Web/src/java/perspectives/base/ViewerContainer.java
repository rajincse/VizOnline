package perspectives.base;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import perspectives.base.Task;

import javax.imageio.ImageIO;
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
	
	Object o1 = new Object();
	Object o2 = new Object();
	
	int tilesX = 2;
	int tilesY = 2;
	
	
	
	protected ViewerWindow window = null;
		 
	protected Viewer viewer;
	
	public int sentTiles = 0;
	public int changedImages = 0;
	public long interactionTime = 0;
	public long startInteractionTime = -1;
	public long lastInteractionTime = -1;
	public long allPngTime = 0;
	public int allPngCount = 0;
	public long allDifTime = 0;
	public int allDifCount = 0;
	public long allBytesSent = 0;
	public int allBytesCount = 0;
	public long allTaskTime = 0;
	public int allTaskCount = 0;
	public long firstGetTileTime = -1;
	public int allGetTile = 0;
		
	//a pointer to the parent Environment class (needed for instance to delete the viewer from the Environment if the user activates the 'X')
	private Environment env;
	
	protected int width, height;
	
	public long lastMouseMove;
	
	boolean tooltipOn;
	String prevTooltip;
	
	BufferedImage savedImage = null;
	boolean blocked = false;
	
	
	long MAXDIFF = 25000;
	int keyFrameRate = 40;
	
	
		
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

	public Viewer getViewer()
	{
		return viewer;
	}
	
	public Environment getEnvironment()
	{
		return env;
	}
	
	public void resize(int w, int h)
	{
		width = w;
		height = h;
		getViewer().requestRender();
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
	    
	    if (window != null)
	    	this.window.redraw();
	}
	

	

	
	protected void keyPressed(String keyText, String modifiersText)
	{
	}
	
	public void scheduleKeyPress(String keyText, String modifiersText)
	{
		final String s1 = keyText;
		final String s2 = modifiersText;
		final ViewerContainer vcf = this;
		//System.out.println("scheduling key press");
		getViewer().em.scheduleEvent(new PEvent()
		{
			public void process() {
				System.out.println("processing key press");
				vcf.keyPressed(s1,s2);	
			}
		});
	}

	protected void keyReleased(String keyText, String modifiersText)
	{					
	}
	
	public void scheduleKeyRelease(String keyText, String modifiersText)
	{
		final String s1 = keyText;
		final String s2 = modifiersText;
		final ViewerContainer vcf = this;
		getViewer().em.scheduleEvent(new PEvent()
		{
			public void process() {
				vcf.keyReleased(s1,s2);	
			}
		});
	}
	
	protected void keyTyped(KeyEvent arg0) {
	}	
	
	
	protected void mouseClicked(int ex, int ey, int button) {
	}
	protected void mouseEntered(int ex, int ey) {
	}
	protected void mouseExited(int ex, int ey) {
	}
	
	protected void mousePressed(int ex, int ey, int button)
	{
	}	
	public void scheduleMousePress(int x, int y, int button)
	{
		final int xf = x;
		final int yf = y;
		final int buttonf = button;
		final ViewerContainer vcf = this;
		getViewer().em.scheduleEvent(new PEvent()
		{
			public void process() {
				vcf.mousePressed(xf,yf,buttonf);							
			}						
		});
	}
	
	
	protected void mouseReleased(int ex, int ey, int button)
	{	
	}
	public void scheduleMouseRelease(int x, int y, int button)
	{
		final int xf = x;
		final int yf = y;
		final int buttonf = button;
		final ViewerContainer vcf = this;
		getViewer().em.scheduleEvent(new PEvent()
		{
			public void process() {
				vcf.mouseReleased(xf,yf,buttonf);							
			}						
		});	
	}
	
	protected void mouseDragged(int ex, int ey) {
	}
	
	boolean dragging;
	public void scheduleMouseDrag(int x, int y) {
		
		//if (dragging) return;
		
		final int xf = x;
		final int yf = y;
		
		//dragging = true;
		
		final ViewerContainer vcf = this; 
	
		getViewer().em.replaceEvent(new PEvent()
		{
			public void process() {
				vcf.mouseDragged(xf,yf);	
				vcf.lastMouseMove = new Date().getTime();
				vcf.getViewer().em.scheduleEvent(new PEvent()
				{
					public void process() {
						vcf.renderTooltip();
					}								
				}, vcf.getViewer().getTooltipDelay());
				
				dragging = false;
			}						
		},"mousedrag");
	}
	
	protected void mouseMoved(int ex, int ey)
	{
	}
	
	public void scheduleMouseMove(int x, int y)
	{	
		final int xf = x;
		final int yf = y;
		
		final ViewerContainer vcf = this;
		getViewer().em.replaceEvent(new PEvent()
		{
			
			public void process() {
				vcf.mouseMoved(xf,yf);
				vcf.lastMouseMove = new Date().getTime();
				vcf.viewer.em.scheduleEvent(new PEvent()
				{
					public void process() {
						vcf.renderTooltip();
					}								
				}, vcf.viewer.getTooltipDelay());
				
				dragging = false;
			}						
		},"mousemove");
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
		
		//lastImage = image;
		
		if (blocked)
		{
			if (image == null)
				return;
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
		changedImages++;
		long t = new Date().getTime();	

		
		if (lastInteractionTime < 0)
		{
			System.out.println("init interaction time");
			lastInteractionTime = t;
			startInteractionTime = t;
		}
		
		if (t-lastInteractionTime > 100)
		{
			if (startInteractionTime >= 0)
			{
				interactionTime += (lastInteractionTime - startInteractionTime);
			}
			startInteractionTime = t;
			lastInteractionTime = t;
		}
		else
			lastInteractionTime = t;
	
		synchronized(o2)
		{
			image = newimage;
		}

	}
	
	Object o3 = new Object();
	byte[][][] tilePngs = null;
	byte[][][] tileDifPngs = null;
	private BufferedImage[][] tiles;
	private BufferedImage[][] tileDifs;
	
	ArrayList<byte[][][]> outTiles = new ArrayList<byte[][][]>();

	
	byte[] noImage = null;
	
	boolean working = false;
	Object o6 = new Object();
	
		
	int diffcount = 0;
	
	int history = 1;
	
	boolean changeSequenceTest = false;
	boolean useChangeSequenceTest = true;
	BufferedImage lastImageSaved = null;
	long lastScaledImageTime = -1;
	
	public String getPerformanceMeasures()
	{
		double fps = ((sentTiles/4) / ((double)interactionTime/1000.));
		long pngtime = allPngTime / allPngCount;
		long difftime = allDifTime / allDifCount;
		long bytes = allBytesSent;
		long tasks = allTaskTime / allTaskCount;
		double requests = (1000. * allGetTile) / (new Date().getTime() - firstGetTileTime);
		String ret = fps + " fps;" + bytes + " bytes;" + requests + " requests;" + (interactionTime/1000) + " time;";

		return ret;
	}
	
	public int getFrames()
	{
		return sentTiles/4;
	}
	
	public long getBytes()
	{
		return allBytesSent;
	}
	
	private void tileTasks(BufferedImage image)
	{		
		if (image == null)
			return;
		
		synchronized(o6)
		{
			if (working) return;
			working = true;
		}
				
		
		if (allPngCount != 0 && allDifTime != 0 && allBytesCount != 0) //for stats purposes
		{
			double fps = ((sentTiles/4) / ((double)interactionTime/1000.));
			long pngtime = allPngTime / allPngCount;
			long difftime = allDifTime / allDifCount;
			long bytes = allBytesSent/allBytesCount;
			long tasks = allTaskTime / allTaskCount;
			double requests = (1000. * allGetTile) / (new Date().getTime() - firstGetTileTime);
			//System.out.println(" ");
			//System.out.println("web fps: " + fps + " (" + (sentTiles/4) + "," + (interactionTime/1000) + ")");
			//System.out.println("web png: " + pngtime + " (" + allPngCount + "," + allPngTime + ")");
		//	System.out.println("web dif: " + difftime + " (" + allDifTime + "," + allDifCount + ")");
			//System.out.println("web bytes: " + bytes + " (" + allBytesSent/4 + "," + allBytesCount + ")");
			//System.out.println("web task: " +  + tasks + " (" + allTaskTime/4 + "," + allTaskCount + ")");
			//System.out.println("web requests: " +  + requests + " (" + allGetTile/4 + "," + (new Date().getTime() - firstGetTileTime) + ")");
		}		
		
		
		final long t = new Date().getTime();
		
		int[] quadDiff = new int[]{0,0,0,0};	
		BufferedImage difImage = diffImage(image, lastImage, quadDiff);
		
			if (diffcount == 0 && history == 0 && !changeSequenceTest) //all is up to data, there's nothing to do
			{
				synchronized(o6)
				{
					working = false;
					lastImage = image;
					return;
				}
			}
			else if (diffcount > MAXDIFF && history == 0 && !changeSequenceTest) //this is a big change, we skip one round to see if it's a one time change
			{				
				synchronized(o6)
				{
					changeSequenceTest = true;	
					lastImageSaved = lastImage;
					lastImage = image;
					working = false;
					return;
				}
			}
			else if (diffcount > 0 && changeSequenceTest) //there's a second change so we're starting to send compressed images
			{
				changeSequenceTest = false;			
				difImage = diffImage(image, lastImageSaved, quadDiff);
				history=1;
			}
			else if (diffcount == 0 && changeSequenceTest) //there's no second change so we just send the regular image
			{
				changeSequenceTest = false;				
				difImage = diffImage(image, lastImageSaved, quadDiff);
				history = 0;
			}
			
		
		int tileCase = 0;
		if (diffcount == 0 && history == 1 && lastScaledImageTime > 0 && (new Date().getTime() - lastScaledImageTime) < 300)
		{		
			synchronized(o6)
			{
				working = false;
				lastImage = image;
				return;
			}
		}
		if ( history == 1 && lastScaledImageTime>0 && (new Date().getTime() - lastScaledImageTime) >= 300) //fluid interaction stopped, we send full image
		{		
			tileCase = 2;			
			tiles = this.tileImage(image, tilesX, tilesY, true);
			history = 0;
			difsSent = 0;
			lastScaledImageTime = -1;
		}
		else if (history == 1 && diffcount > 0) // this is a fluid interaction; we send scaled down images
		{		
			BufferedImage halfIm = resizeImage(image, 0.25);
			tiles = this.tileImage(halfIm, tilesX, tilesY, false);
			lastImage = image;
			difsSent = 0;
			
			lastScaledImageTime = new Date().getTime();
		}
		else if (history == 0 && diffcount > MAXDIFF) //this was a single shot action (changeSequenceTest)
		{
			tiles = this.tileImage(image, tilesX, tilesY, true);
			lastImage = image;
			difsSent = 0;
			lastScaledImageTime = -1;
		}
		else if (history == 0 && diffcount<=MAXDIFF && difsSent > this.keyFrameRate)
		{
			tiles = this.tileImage(image, tilesX, tilesY, true);
			difsSent=0;
			lastImage = image;
			lastScaledImageTime = -1;
		}
		else if (history == 0 && diffcount<=MAXDIFF && difsSent <= this.keyFrameRate)
		{
			tiles = this.tileImage(difImage, tilesX, tilesY, false);
			difsSent++;
			lastImage = image;
			lastScaledImageTime = -1;
		}		

		
		tilePngs = new byte[tiles.length][][];
		for (int i=0; i<tilesX; i++)
		{
			tilePngs[i] = new byte[tiles[i].length][];
			for (int j=0; j<tilesY; j++)
				tilePngs[i][j] = null;
		}


		for (int i=0; i<tilesX; i++)
			for (int j=0; j<tilesY; j++)	{
				
				if (quadDiff[i + j*tilesY] == 0 && tileCase != 2)
				{
					tilePngs[i][j] = noImage();
					allTasksDone();
					continue;
				}
				
				final int i_f = i;	final int j_f = j;
				Task tt = new Task("t"){							
					public void task() {
						long t2 = new Date().getTime();
						 PngEncoder p = new PngEncoder(tiles[i_f][j_f], true);				      
				             p.setCompressionLevel(8);
				         byte[] bs = p.pngEncode(true);
				         
				         allPngTime += (new Date().getTime() - t2);
				         allPngCount++;
				         synchronized (o3)
				         {
				        	 tilePngs[i_f][j_f] = bs;					         
				        	 allTasksDone();
				        	 allTaskTime += (new Date().getTime() - t);
				        	 allTaskCount++;
				         }
					}
				};				
				tt.start();
			}
	}
	
	private void allTasksDone()
	{	
		
		for (int i=0; i<tilePngs.length; i++)
			for (int j=0; j<tilePngs[i].length; j++)
				if (tilePngs[i][j] == null) return;
		

	
		synchronized(o8)
		{
			
			outTiles.add(tilePngs);
			o8.notifyAll();
	
		}
		
		synchronized(o6)
		{	working = false; }	
		
		o9.notifyAll();
		return;
	}
	
	private void getSendTiles()
	{
		synchronized(o8)
		{
			if (outTiles.size() == 0)
			{	
				sendTiles = null;
				return;
			}
			
			sendTiles = new byte[outTiles.get(0).length][][];
			for (int i=0; i<outTiles.get(0).length; i++)
			{
				sendTiles[i] = new byte[outTiles.get(0)[i].length][];
				for (int j=0; j<outTiles.get(0)[i].length; j++)
					sendTiles[i][j] = outTiles.get(0)[i][j];
			}			
			outTiles.remove(0);
			//System.out.println("outtiles size: " + outTiles.size());
		}
	}
	
	
	
	
	Object o5 = new Object();
	int availableTiles = 0;
	int round = 0;
	Object o7 = new Object();
	Object o8 = new Object();  //use to synchronize transfer from outTile to sendTiles and from tilePngs to outTiles
	
	byte[][][] sendTiles = null;
	int difsSent = 0;
	
	
	public void createTiles(boolean resetting)
	{
		synchronized(o2)
		{
			if (image != lastImage || history > 0 || resetting)
			{	
				if (resetting)
					history = 0;
				
				final BufferedImage im = image;				
				Task t = new Task("t")
				{	public void task()
					{	tileTasks(im);	}};				
				t.start();
			}
		}
	}
	
	Object o9 = new Object();
	public void resetTiles()
	{
		synchronized(o6)
		{
			if (working)
				try {
					o9.wait();
				} catch (InterruptedException e) {					
					e.printStackTrace();
				}		
			
			outTiles.clear();
			sendTiles = null;
			round = 0;
			lastImage = null;			
			createTiles(true);
		}		
	}
	
	BufferedImage prevImage = null;
	public byte[] getTileEmpty(int x, int y)
	{
		byte[] ret = noImage();
		if (image != prevImage)
		{
			sentTiles+=4;
			allBytesSent += ret.length;
			allBytesCount++;
		}
		
		prevImage = image;
		return ret;
	}
	
	public byte[] getTile(int x, int y)
	{
		//for stats purposes
		allGetTile++;
		if (firstGetTileTime < 0)
			firstGetTileTime = new Date().getTime();
		
		synchronized(o5)
		{
			if (x < 0 || y < 0)
			{				
				lastImage = null;
				round = 0;
				return noImage();
			}
			
			if (round == 0)	
				getSendTiles();	
		
			byte[] ret = null;
			if (sendTiles == null || sendTiles[x][y] == null)
				ret = noImage();
			else
			{				
				ret = sendTiles[x][y];
				sendTiles[x][y] = null;	
			}
			
			round++;
			if (round >= tilesX*tilesY) round = 0;
			
			if (round == 0)
				createTiles(false);	
			
			
			
			
			if (ret.length > 100)  //for stats purposes
			{
				sentTiles++;
				//ret = noImage();
				allBytesSent += ret.length;
				allBytesCount++;
				
				
			}
			return ret;
		}
	}
	
	public byte[] noImage()
	{
		if (noImage == null)
		{
			BufferedImage im = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D)im.createGraphics();
			g.setColor(new Color(0,0,0,0));
			g.fillRect(0, 0, 1, 1);
			PngEncoder p = new PngEncoder(im, true);
	        p.setFilter(PngEncoder.FILTER_NONE);
	         p.setCompressionLevel(2);
	         noImage = p.pngEncode(true);	
		}
		
		return noImage;
	}

	Object o11 = new Object();
	Object o12 = new Object();
	
	
	private BufferedImage diffImage(BufferedImage image, BufferedImage lastImage, int[] quaddiff)
	{
			long ttt = new Date().getTime();
			
			for (int i=0; i<quaddiff.length; i++)
				quaddiff[i] = 0;
		
			diffcount = 0;			
		
	        BufferedImage dif = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);      
	        
	        dif.createGraphics().drawImage(image, 0,0,null);	  
	       
	        if (lastImage == null || lastImage.getWidth() != image.getWidth() || lastImage.getHeight() != image.getHeight())
	        {	     
	        	diffcount = image.getWidth()*image.getHeight();
				for (int i=0; i<quaddiff.length; i++)
					quaddiff[i] = 1;
	        	return dif;    
	        }     
	        
	         int[] cpixels = ((DataBufferInt) dif.getRaster().getDataBuffer()).getData();
	         int[] spixels;	        
	      
	       	spixels = ((DataBufferInt) lastImage.getRaster().getDataBuffer()).getData();	       

	        int[] calpha = ((DataBufferInt) dif.getAlphaRaster().getDataBuffer()).getData();
	        
	        int w = image.getWidth();
	        int h = image.getHeight();
	        int col = 0; 
	        int row = 0;
	        int quad = 0;
	        
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
	                else
	                {
	                	diffcount++;
	                	quaddiff[quad]++;
	                }
	                
	                col++;
	                if (col == w)
	                {
	                	col=0;
	                	row++;
	                	if (row < h/2)
	                		quad = 0;
	                	else quad = 2;
	                }
	                if (col == w/2)
	                {
	                	if (row < h/2)
	                		quad = 1;
	                	else quad = 3;
	                }
	                
	                if (diffcount > MAXDIFF)
	                {
	    				for (int j=0; j<quaddiff.length; j++)
	    					quaddiff[j] = 1;
	                	break;
	                }
	        }	        
	        allDifTime +=  (new Date().getTime()-ttt);
	        allDifCount++;
	      
	        return dif;
	}
	
	
	private BufferedImage resizeImage(BufferedImage im, double f)
	{
		BufferedImage res = new BufferedImage((int)(im.getWidth()*f), (int)(im.getHeight()*f), BufferedImage.TYPE_INT_ARGB);
		res.createGraphics().drawImage(im, 0, 0, res.getWidth()-1, res.getHeight()-1, 0, 0, im.getWidth()-1, im.getHeight()-1, null);
		return res;
	}
	
	
	private BufferedImage[][] tileImage(BufferedImage image, int tileX, int tileY, boolean extraWidth)
	{
		BufferedImage[][] tiles = new BufferedImage[tileX][];
		int tileWidth = image.getWidth()/tileX;
		int tileHeight = image.getHeight()/tileY;
		if (extraWidth) tileWidth += 1;
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
	
	
	
	
	
	ArrayList<byte[]> testImages;
	ArrayList<long[]> testImageTimes;
	ArrayList<int[]> testImageCounts;
	
	long[][][] lastTileTimes;
	
	
	Object otest = new Object();
	int tileTimerTest = 0;
}
