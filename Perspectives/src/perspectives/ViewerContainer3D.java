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
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.concurrent.ExecutionException;

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
import javax.media.opengl.glu.GLU;
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

import perspectives.navigation.ObjectController3D;
import perspectives.navigation.UserNavigation3D;

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
public class ViewerContainer3D extends ViewerContainer implements GLEventListener{
	
	//a pointer to the parent Environment class (needed for instance to delete the viewer from the Environment if the user activates the 'X')
	final Environment env;
	 	
	Thread thread = null;
	
	GLWindow windowOffscreen;
	final GLU glu = new GLU();  
	int dragPrevX;
	int dragPrevY;
	
	int renderCount = 0;	
	
	ObjectController3D controller;
	UserNavigation3D navigation;
	
	BufferedImage image = null;
	
	
	public ViewerContainer3D(Viewer3D v, Environment env, int width, int height)
	{	
		super(v,env,width,height);
		

		this.env = env;				

		GLProfile glp = GLProfile.getDefault();	       

		try {
			GLCapabilities caps = new GLCapabilities(glp);

			windowOffscreen = createOffscreen(caps, width, height);


			viewer.width = width;
			viewer.height = height;

			windowOffscreen.addGLEventListener(this);  //adding it to the offscreen window
			
			controller = new ObjectController3D(windowOffscreen);
			this.navigation = new UserNavigation3D(this, this.glu);
			

		} catch (GLException e) {
			e.printStackTrace();
		}
	}
	
	public void render()
	{		
		viewer.em.scheduleEvent(new PEvent()
		{
			public void process() {
				windowOffscreen.display();			
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


	public void setWidth(int w)
	{
		super.setWidth(w);
		windowOffscreen.setSize(w, this.getHeight());
		((Viewer3D)viewer).width = w;
	}
	public void setHeight(int h)
	{
		super.setHeight(h);
		windowOffscreen.setSize(this.getWidth(),h);
		((Viewer3D)viewer).height = h;
	}
	
	

	public BufferedImage getImage()
	{
		return image;
	}
	
	
	 public GLWindow createOffscreen(GLCapabilities caps, int w, int h) {
	        GLCapabilities capsOffscreen = (GLCapabilities) caps.clone();
	        capsOffscreen.setOnscreen(false);
	        //capsOffscreen.setPBuffer(pbuffer);
	        capsOffscreen.setDoubleBuffered(false);

	        Display nDisplay = NewtFactory.createDisplay(null); // local display
	        Screen nScreen = NewtFactory.createScreen(nDisplay, 0); // screen 0
	        com.jogamp.newt.Window nWindow = NewtFactory.createWindow(nScreen, capsOffscreen);

	        GLWindow windowOffscreen = GLWindow.create(nWindow);
	        windowOffscreen.setUpdateFPSFrames(FPSCounter.DEFAULT_FRAMES_PER_INTERVAL, System.err);
	        windowOffscreen.setSize(w, h);
	        windowOffscreen.setVisible(true);
	        return windowOffscreen;
	    }
	 
	 
	
	 public void mouseMoved(int ex, int ey) {
		 lastMouseMove = new Date().getTime();
		 navigation.mouseMoved(ex, ey);
		 viewer.requestRender();
	 }
	 
		public void mouseDragged(int ex, int ey) {
			controller.mouseDragged(ex, ey);			
			viewer.requestRender();
		}

		
		public void mousePressed(int x, int y, int button)
		{
			controller.mousePressed(x, y,button);			
			viewer.requestRender();

		}
		
		public void mouseReleased(int x, int y, int button)
		{
			controller.mouseReleased(x, y, button);		
			viewer.requestRender();
		}
		
		public void keyPressed(KeyEvent e)
		{
			navigation.keyPressed(e);
			controller.keyPressed(e);			
			viewer.requestRender();
		}
		
		public void keyReleased(KeyEvent e)
		{
			navigation.keyReleased(e);
			controller.keyReleased(e);			
			viewer.requestRender();
		}


		public void display(GLAutoDrawable gLDrawable) {		
			
			GL2 gl = gLDrawable.getGL().getGL2();
	        
			   
			gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		    gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
	        
		 // Set up camera for Orthographic projection:
		    			
			gl.glMatrixMode(gl.GL_PROJECTION);
			gl.glLoadIdentity();
			glu.gluPerspective(60, width/(float)height, 1, 100.0);
			navigation.render(gl);

			 gl.glMatrixMode (gl.GL_MODELVIEW);
			 gl.glLoadMatrixd(new double[]{1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,-10,1}, 0);
			 gl.glMultMatrixd(controller.mvmatrix, 0);
			 
	    
	        ((Viewer3D)viewer).render(gl);
	        
	        controller.render(gl);
	       
	        this.writeBufferToImage(gl);
	       
 
	    }

	    @Override
	    public void init(GLAutoDrawable glDrawable) {
	    	
	    	GL2 gl = glDrawable.getGL().getGL2();
	        
	        gl.glShadeModel(GLLightingFunc.GL_SMOOTH);
	        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	        gl.glClearDepth(1.0f);
	        gl.glEnable(GL.GL_DEPTH_TEST);
	        gl.glDepthFunc(GL.GL_LEQUAL);
	        gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
	        
	    }

	    @Override
	    public void reshape(GLAutoDrawable glDrawable, int x, int y, int width, int height) {
	    	//System.out.println("--reshape ");
	    	GL2 gl = glDrawable.getGL().getGL2();
	        final float aspect = (float) width / (float) height;
	        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
	        gl.glLoadIdentity();
	        final float fh = 1.f;
	        final float fw = fh * aspect;
	        gl.glFrustumf(-fw, fw, -fh, fh, 1.0f, 1000.0f);
	        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
	        gl.glLoadIdentity();
	    }

	    @Override
	    public void dispose(GLAutoDrawable gLDrawable) {
	    }
	    
	    
	    
	    private  void writeBufferToImage(GL2 gl) {

	 
	        ByteBuffer pixelsRGB = ByteBuffer.allocate(width * height * 3);

	   
	        gl.glReadBuffer(GL.GL_BACK);
	        gl.glPixelStorei(GL.GL_PACK_ALIGNMENT, 1);

	        gl.glReadPixels(0, // GLint x
	                0, // GLint y
	                width, // GLsizei width
	                height, // GLsizei height
	                GL.GL_RGB, // GLenum format
	                GL.GL_UNSIGNED_BYTE, // GLenum type
	                pixelsRGB);               // GLvoid *pixels

	        int[] pixelInts = new int[width * height];

	        // Convert RGB bytes to ARGB ints with no transparency. Flip image vertically by reading the
	        // rows of pixels in the byte buffer in reverse - (0,0) is at bottom left in OpenGL.

	        int p = width * height * 3; // Points to first byte (red) in each row.
	        int q;                  // Index into ByteBuffer
	        int i = 0;                  // Index into target int[]
	        int w3 = width * 3;         // Number of bytes in each row

	        for (int row = 0; row < height; row++) {
	            p -= w3;
	            q = p;
	            for (int col = 0; col < width; col++) {
	                int iR = pixelsRGB.get(q++);
	                int iG = pixelsRGB.get(q++);
	                int iB = pixelsRGB.get(q++);

	                pixelInts[i++] = 0xFF000000
	                        | ((iR & 0x000000FF) << 16)
	                        | ((iG & 0x000000FF) << 8)
	                        | (iB & 0x000000FF);
	            }

	        }

	        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	        image.setRGB(0, 0, width, height, pixelInts, 0, width);

	    }

}