package perspectives;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

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
import javax.media.opengl.glu.GLU;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.math.Quaternion;


public abstract class Viewer3D extends Viewer implements GLEventListener{

	GL2 gl;
	
	int cnt = 0;
	
	GLProfile glp;
	
	boolean reshaped = false;
	
	public GLWindow windowOffscreen = null;	

	
	BufferedImage image = null;
	
	public abstract void render(GL2 gLDrawable);
	
	
	private GLWindow createOffscreen(GLCapabilities caps, int w, int h) {
		
		width = w;
		height = h;
		
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
	
	public void initGLWindow(int width, int height)
	{
		try {
			
			if (windowOffscreen != null)
				windowOffscreen.destroy();
			
			glp = GLProfile.getDefault();
			
            GLCapabilities caps = new GLCapabilities(glp);
            
            
            windowOffscreen = createOffscreen(caps, width, height);
            
           // windowOffscreen.addGLEventListener(new JOGLQuad());  //adding it to the offscreen window
            


        } catch (GLException e) {
            e.printStackTrace();
        }
    }
	
	
	public Viewer3D(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		

        
	}
	
	float[] camloc = new float[]{0,0,10};
	
	public float[] getCameraLocation()
	{
		return camloc;
	}
	
	public void setCameraLocation(float[] c)
	{
		camloc = c;
	}
	
	
	   private float rotateT = 0.0f;
	   
	
	
	
	
	Quaternion q = new Quaternion(0,0,0,1);
	
	
	private void sphere(GL2 gl, double r) {

		final double deg_to_rad = Math.PI / 180;
		final double rad_to_deg = 180 / Math.PI;
	    // Quadrilateral strips
	    for (double phi = -90.0; phi < 90.0; phi += 10.0) {
		gl.glBegin (gl.GL_QUAD_STRIP);
		
		for (double thet = -180.0; thet <= 180.0; thet += 10.0) {
		    gl.glVertex3d (r*Math.sin (deg_to_rad * thet) * Math.cos (deg_to_rad * (phi + 10.0)),
		    		r*Math.sin (deg_to_rad * (phi + 10.0)),
		    		r*Math.cos (deg_to_rad * thet) * Math.cos (deg_to_rad * (phi + 10.0))	);
		    gl.glVertex3d (r*Math.sin (deg_to_rad * thet) * Math.cos (deg_to_rad * phi),
		    		 r*Math.sin (deg_to_rad * phi),
				   r*Math.cos (deg_to_rad * thet) * Math.cos (deg_to_rad * phi)  );

		}
		gl.glEnd();
	    }

	}
	
	
    int viewport[] = new int[4];
    double mvmatrix[] = new double[16];
    double projmatrix[] = new double[16];
    double wcoord[] = new double[4];// wx, wy, wz;// returned xyz coords
    boolean rotating = false;
	
	public void display(GLAutoDrawable gLDrawable) {		
		
	        gl = gLDrawable.getGL().getGL2();
	        
	       // System.out.println("--display ");
	        
			 gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		        gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
	        
	    
		      //  gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		        
		        final GLU glu = new GLU();
		        gl.glMatrixMode (gl.GL_MODELVIEW);
			    gl.glLoadIdentity ();
			    // Camera	    




			    // Set up camera for Orthographic projection:
			    gl.glMatrixMode(gl.GL_PROJECTION);
			    gl.glLoadIdentity();
			    glu.gluPerspective(60, width/(float)height, 1, 100.0);
	        
			    gl.glMatrixMode (gl.GL_MODELVIEW);
			    
	        gl.glLoadIdentity();
	        

			float[] camloc = getCameraLocation();	 
			
	        
	        glu.gluLookAt(
		            camloc[0], camloc[1], camloc[2], // eye
		            camloc[0], camloc[1], 0, // at
		            0, 1, 0 // up
		            );

	        
	        
	        gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
	        gl.glGetDoublev(gl.GL_MODELVIEW_MATRIX, mvmatrix, 0);
	        gl.glGetDoublev(gl.GL_PROJECTION_MATRIX, projmatrix, 0);
	        
	        
	        gl.glMatrixMode (gl.GL_MODELVIEW);
	      
	        gl.glMultMatrixf(q.toMatrix(), 0);
	       
	     
	        if (rotating)
	        {
		        float camdist = (float)Math.sqrt(camloc[0]*camloc[0] + camloc[1]*camloc[1] + camloc[2]*camloc[2]);
		        
		        gl.glEnable(GL.GL_BLEND);
		        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		        gl.glColor4f(1.0f, 0.0f, 0.0f, 0.05f);
		        gl.glPolygonMode( gl.GL_FRONT, gl.GL_LINE );
		        sphere(gl,camdist/2.2);	
	        }
	        
	        gl.glPolygonMode( gl.GL_FRONT_AND_BACK, gl.GL_POLYGON);
	        
	        ((ViewerContainer3D)this.container).navigationRender(gl);
	        render(gl);
	       
	        this.writeBufferToImage(gl);
	        
	      
	    }

	    @Override
	    public void init(GLAutoDrawable glDrawable) {
	    	
	        gl = glDrawable.getGL().getGL2();
	        
	      //  System.out.println("--init ");
	        
	        gl.glShadeModel(GLLightingFunc.GL_SMOOTH);
	        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	        gl.glClearDepth(1.0f);
	        gl.glEnable(GL.GL_DEPTH_TEST);
	        gl.glDepthFunc(GL.GL_LEQUAL);
	        gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
	        
	    }

	    @Override
	    public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) {
	    	//System.out.println("--reshape ");
	        gl = gLDrawable.getGL().getGL2();
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

	        File outputFile = new File("file" + (++cnt) + ".png");


	        //GL gl = drawable.getGL();

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
