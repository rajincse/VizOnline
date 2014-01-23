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


public abstract class Viewer3D extends Viewer{

	public abstract void render(GL2 gLDrawable);
	
		
	public Viewer3D(String name) {
<<<<<<< HEAD
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
	   
	
	
	float rotationx;
	float rotationy;
	float rotationz;
	
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

=======
		super(name);	
>>>>>>> upstream/master
	}
	  

}
