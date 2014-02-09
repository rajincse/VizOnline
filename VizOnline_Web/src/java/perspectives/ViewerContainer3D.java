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
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.media.opengl.FPSCounter;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
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

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ARBBufferObject;
import org.lwjgl.opengl.ARBPixelBufferObject;
import org.lwjgl.opengl.EXTBgra;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

import perspectives.navigation.ObjectController3D;

import com.jogamp.common.nio.Buffers;
import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;



/**
 * This is a window class for Viewers. It will be able to accommodate any of the three main types of Viewers: 2D, 3D, GUI. It will call their rendering, simulation, and interactivity functions (if appropriate), it will implement double buffering for them, and it can be added to the viewer area
 * of the Environment. ViewerContainers automatically implementing panning and zooming for 2D viewers via 2dtransforms applied to the Graphics2D objects passed onto the Viewer2D's render function.
 * @author rdjianu
 *
 */
public class ViewerContainer3D extends ViewerContainer{
	
	//a pointer to the parent Environment class (needed for instance to delete the viewer from the Environment if the user activates the 'X')
	final Environment env;
	
	 	
	Thread thread = null;
	
	
	int dragPrevX;
	int dragPrevY;
	
	int renderCount = 0;	
	
	ObjectController3D controller;
	
	BufferedImage image = null;
	
	int oldx = 0, oldy = 0;
	
	
	Pbuffer pbuffer;
	 
	
	
	public ViewerContainer3D(Viewer3D v, Environment env, int width, int height)
	{	
		super(v,env,width,height);

		this.env = env;				

		GLProfile glp = GLProfile.getDefault();	       

		try {
			
			pbuffer = new Pbuffer(width, height, new PixelFormat(), null, null);
			
			pbuffer.makeCurrent();
			
	       // int pbo1 = ARBBufferObject.glGenBuffersARB();
	       // int pbo2 = ARBBufferObject.glGenBuffersARB();
	        
	        
	      //  ARBBufferObject.glBindBufferARB(ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_ARB, pbo1);
	       // ARBBufferObject.glBufferDataARB(ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_ARB, 1800*1200*4, ARBPixelBufferObject.GL_STREAM_READ_ARB);
	        
	      //  ARBBufferObject.glBindBufferARB(ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_ARB, pbo2);
	       // ARBBufferObject.glBufferDataARB(ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_ARB, 1800*1200*4, ARBPixelBufferObject.GL_STREAM_READ_ARB);
	        
	        //unbind?
	     //  ARBBufferObject.glBindBufferARB(ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_ARB, 0);
			
			controller = new ObjectController3D();
	        
	        pbuffer.releaseContext();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void render()
	{
		if (renderCount >= 1)
			return;

		renderCount++;
		
		final ViewerContainer3D th = this;
		viewer.em.scheduleEvent(new PEvent()
		{
			public void process() {
				th.display();
				((Viewer3D)viewer).render2DOverlay(image.createGraphics());
				renderDoneCallback();	
			}
		});
	}
	
	public void renderDoneCallback()
	{
		renderCount--;		
		setViewerImage(image);
		this.window.redraw();
	}


	Object o = new Object();
	public void setWidth(int w)
	{
		synchronized(o)
		{
			super.setWidth(w);
			try {
				Pbuffer old = pbuffer;
				pbuffer = new Pbuffer(w, getHeight(), new PixelFormat(), null, null);
				
				old.destroy();
			} catch (LWJGLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			((Viewer3D)viewer).width = w;
		}
	}
	public void setHeight(int h)
	{
		synchronized(o)
		{
			super.setHeight(h);
			try {
				Pbuffer old = pbuffer;
				pbuffer = new Pbuffer(getWidth(), h, new PixelFormat(), null, null);
				old.destroy();
			} catch (LWJGLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			((Viewer3D)viewer).height = h;
		}
	}
	
	

	public BufferedImage getImage()
	{
		return image;
	}
		 
	 
	
	 public void mouseMoved(int x, int y) {
		 lastMouseMove = new Date().getTime();
		 ((Viewer3D)viewer).mousemoved(x, y);
	 }
	 
		public void mouseDragged(int x, int y) {
			if (!((Viewer3D)viewer).mousedragged(x, y, oldx, oldy))
			{
			controller.mouseDragged(x, y, pbuffer);			
			viewer.requestRender();
			}
			oldx = x;
			oldy = y;
		}

		
		public void mousePressed(int x, int y, int button)
		{
			if (!((Viewer3D)viewer).mousepressed(x, y, button))
			{
				controller.mousePressed(x, y,button, pbuffer);	
				viewer.requestRender();
			}
			oldx = x;
			oldy = y;

		}
		
		public void mouseReleased(int x, int y, int button)
		{
	
			if (!((Viewer3D)viewer).mousereleased(x, y, button))
			{
				controller.mouseReleased(x, y, button, pbuffer);		
				viewer.requestRender();
			}
		}
		
		public void keyPressed(KeyEvent e)
		{
		
			controller.keyPressed(e, pbuffer);			
			viewer.requestRender();
		}
		
		public void keyReleased(KeyEvent e)
		{
			controller.keyReleased(e, pbuffer);			
			viewer.requestRender();
		}
		
		
		ByteBuffer pixelsRGB = null;
		
		public void display() {
			synchronized(o)
			{			
				try {
					this.pbuffer.makeCurrent();
				} catch (LWJGLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		   
				// setup gl
			    GL11.glMatrixMode(GL11.GL_PROJECTION);
			    GL11.glLoadIdentity();
			    GLU.gluPerspective(60f, width/(float)height, 1f, 100.0f);
			    GL11.glMatrixMode(GL11.GL_MODELVIEW);		
			    
			    GL11.glLoadMatrix(Buffers.newDirectDoubleBuffer(new double[]{1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,-5,1}));
			    GL11.glMultMatrix(Buffers.newDirectDoubleBuffer(controller.mvmatrix));
			    
			 			 
			    GL11.glViewport(0, 0, width, height);
			    GL11.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);			  
			    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			    GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			   
			    ((Viewer3D)viewer).render();
			  
			
		  
		      //  controller.render();
		       
		       
		       this.glToImage();
		       
		       try {
				pbuffer.releaseContext();
			} catch (LWJGLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
	       
 
	    }
		


	    int pbo1, pbo2;
	    
	    private void glToImagePBO()
	    {
			image = new BufferedImage(1800, 1200, BufferedImage.TYPE_INT_ARGB);
				
			int[] pixelInts = new int[1800*1200];
						  
		     // set the framebuffer to read
		     GL11.glReadBuffer(GL.GL_FRONT);
		     
		     
		     //create a read to the "next" buffer, pbo 2
		     ARBBufferObject.glBindBufferARB(ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_BINDING_ARB, pbo2);
		       
		     long ttt = new Date().getTime();
		     GL11.glReadPixels(0, 0, 1800, 1200, GL.GL_BGRA, GL.GL_UNSIGNED_BYTE, 0);	       
		     System.out.println("time: " + (new Date().getTime()-ttt));
		          
		     //process the data from the "current" buffer, pbo1
		     ARBBufferObject.glBindBufferARB(ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_BINDING_ARB, pbo1);
		       if (pixelsRGB == null)
		    	   pixelsRGB = ARBBufferObject.glMapBufferARB(ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_ARB, ARBPixelBufferObject.GL_READ_ONLY_ARB, null);
		       else
		    	   pixelsRGB = ARBBufferObject.glMapBufferARB(ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_ARB, ARBPixelBufferObject.GL_READ_ONLY_ARB, pixelsRGB);
		        
		        
		        if (pixelsRGB == null)
		        {
		        	//switch the buffers and exit
				       int aux = pbo1;
				       pbo1 = pbo2;
				       pbo2 = aux;
		        	return;
		        }
		        
		       ARBBufferObject.glUnmapBufferARB(ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_ARB);     // release pointer to the mapped buffer		    
		        
		       //switch the buffers to prepare for the next frame.
		       int aux = pbo1;
		       pbo1 = pbo2;
		       pbo2 = aux;
		    
		       //put the pixels in the right format
	        int p = width * height * 4; // Points to first byte (red) in each row.
	        int q;                  // Index into ByteBuffer
	        int i = 0;                  // Index into target int[]
	        int w3 = width * 4;         // Number of bytes in each row

	        for (int row = 0; row < height; row++) {
	            p -= w3;
	            q = p;
	            for (int col = 0; col < width; col++) {
	            	
	            	int iB = pixelsRGB.get(q++);
	            	int iG = pixelsRGB.get(q++);	                
	                int iR = pixelsRGB.get(q++);	                
	                
	                int iA = pixelsRGB.get(q++);

	                pixelInts[i++] = 0xFF000000
	                        | ((iR & 0x000000FF) << 16)
	                        | ((iG & 0x000000FF) << 8)
	                        | (iB & 0x000000FF);
	            }

	        }
	        
	        image.setRGB(0, 0, width, height, pixelInts, 0, width);
	    }
	    
	    private  void glToImage() {
	    	
	    	try{
	    	    		
	    	
	    	ByteBuffer pixelsRGB = Buffers.newDirectByteBuffer(width * height * 4);
	    	 
	    	long ttt1 = new Date().getTime();
	      
	      //  int[] pixelInts = new int[width * height];
	        
	        GL11.glReadBuffer(GL.GL_BACK);
	        
	       GL11.glReadPixels(0, 0, width, height, GL.GL_BGRA, GL.GL_UNSIGNED_BYTE, pixelsRGB);
	               
	        long ttt2 = new Date().getTime();

		       //put the pixels in the right format
	        int p = width * height * 4; // Points to first byte (red) in each row.
	        int q;                  // Index into ByteBuffer
	        int i = 0;                  // Index into target int[]
	        int w3 = width * 4;         // Number of bytes in each row
	        
	        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	        int[] pixelInts = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	        for (int row = 0; row < height; row++) {
	            p -= w3;
	            q = p;
	            for (int col = 0; col < width; col++) {
	            	
	            	int iB = pixelsRGB.get(q++);
	            	int iG = pixelsRGB.get(q++);	                
	                int iR = pixelsRGB.get(q++);	                
	                
	                int iA = pixelsRGB.get(q++);

	                pixelInts[i++] = 0xFF000000
	                        | ((iR & 0x000000FF) << 16)
	                        | ((iG & 0x000000FF) << 8)
	                        | (iB & 0x000000FF);
	            }

	        }
	        
	       
	        
	        long ttt3 = new Date().getTime();

	      	        
	        long ttt4 = new Date().getTime();
	        System.out.println("write buffer to image: " + (ttt2 - ttt1) + " " + (ttt3-ttt2) + " " + (ttt4-ttt3));
	        
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    		image =  new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
	    	}

	    }

}