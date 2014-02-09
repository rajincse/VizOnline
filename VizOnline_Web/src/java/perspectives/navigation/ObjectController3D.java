package perspectives.navigation;

import java.awt.event.KeyEvent;
import java.nio.DoubleBuffer;
import java.util.Date;

import javax.media.opengl.FPSCounter;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.Pbuffer;

import perspectives.Trackball;

import com.jogamp.common.nio.Buffers;
import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.math.Quaternion;

public class ObjectController3D {
	
	
	Trackball tb = new Trackball(0,0,0,5);
	
	int dragPrevX;
	int dragPrevY;
		
	boolean rotating = false;
	
	public double[] mvmatrix = new double[]{1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,0,1};
	
	private double[] mvmatrixrot = new double[]{1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,0,1};
	
	public ObjectController3D()
	{
		
	}
	
	
	public void mouseMoved(int ex, int ey, Pbuffer buff) {
	 }
	 
		public void mouseDragged(int ex, int ey, Pbuffer buff) {
			
			
			if (rotating)
			{
			try {
				buff.makeCurrent();
			} catch (LWJGLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
									
			tb.setRadius(10/2.2f);
			tb.setCenter(0, 0, 0);
			tb.drag(ex, ey);
			
			//tb.rot is now a quaternion indicating the axis and magnitude of the last rotation increment
			//this gets PRE-multiplied to the existing transformation, and then the whole thing gets re-stored into mvmatrix				
				 GL11.glMatrixMode (GL11.GL_MODELVIEW);
				 GL11.glLoadIdentity();
				 GL11.glMultMatrix(Buffers.newDirectFloatBuffer(tb.getRot().toMatrix()));
				 GL11.glMultMatrix(Buffers.newDirectDoubleBuffer(mvmatrix));
				 DoubleBuffer mvbuff = Buffers.newDirectDoubleBuffer(mvmatrix);
				 GL11.glGetDouble(GL11.GL_MODELVIEW_MATRIX, mvbuff);
				 mvbuff.get(mvmatrix); 
			
		    //this computes the global rotation that should be applied to the trackball representation (it ignores all the translations...)
				 GL11.glMatrixMode (GL11.GL_MODELVIEW);
				 GL11.glLoadIdentity();
				 GL11.glMultMatrix(Buffers.newDirectFloatBuffer(tb.getRot().toMatrix()));
				 GL11.glMultMatrix(Buffers.newDirectDoubleBuffer(this.mvmatrixrot));
				 DoubleBuffer mvbuff2 = Buffers.newDirectDoubleBuffer(mvmatrixrot);
				 GL11.glGetDouble(GL11.GL_MODELVIEW_MATRIX, mvbuff2);
				 mvbuff2.get(mvmatrixrot);
				 
				 

			dragPrevX = ex;
			dragPrevY = ey;				
			
			try {
				buff.releaseContext();
			} catch (LWJGLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}

		
		public void mousePressed(int x, int y, int button, Pbuffer buff)
		{
			if (button == 3)
			{
				rotating = true;			
				tb.click(x, y);
			}
			
			
						
			dragPrevX = x;
			dragPrevY = y;
			
			
		}
		
		public void mouseReleased(int x, int y, int button, Pbuffer buff)
		{
			if (rotating && button == 3)
			{
				tb.click(x, y);	
				rotating = false;
			}
		}
		
		public void keyPressed(KeyEvent e, Pbuffer buff)
		{	
			try {
				buff.makeCurrent();
			} catch (LWJGLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			int transx = 0;
			int transy = 0;
			int transz = 0;
			if (e.getKeyCode() == e.VK_UP)
				transy = 1;
			else if (e.getKeyCode() == e.VK_DOWN)
				transy = -1;
			else if (e.getKeyCode() == e.VK_LEFT)
				transx = -1;
			else if (e.getKeyCode() == e.VK_RIGHT)
				transx = 1;
			else if (e.getKeyCode() == e.VK_EQUALS)
				transz = -1;
			else if (e.getKeyCode() == e.VK_MINUS)
				transz = 1;
			else
				return;
			
			//the translation is premultiplied to the existing transformation 
			 GL11.glMatrixMode (GL11.GL_MODELVIEW);
			 GL11.glLoadIdentity();
			 GL11.glTranslated(transx,transy,transz);
			 GL11.glMultMatrix(Buffers.newDirectDoubleBuffer(mvmatrix));
			 DoubleBuffer mvbuff = Buffers.newDirectDoubleBuffer(mvmatrix);
			 GL11.glGetDouble(GL11.GL_MODELVIEW_MATRIX, mvbuff);
			 mvbuff.get(mvmatrix);
			 
			 for (int i=0; i<mvmatrix.length; i++)
				 System.out.println(mvmatrix[i]);
			 
			try {
				buff.releaseContext();
			} catch (LWJGLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		public void keyReleased(KeyEvent e, Pbuffer buff)
		{
		}

		//renders anything related to this controller (in this case the trackball)
		public void render()
		{
			if (rotating)
			{
				 GL11.glMatrixMode ( GL11.GL_MODELVIEW);
				 GL11.glLoadMatrix(Buffers.newDirectDoubleBuffer(new double[]{1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,-10,1}));
				 GL11.glMultMatrix(Buffers.newDirectDoubleBuffer(this.mvmatrixrot));			 
		      
				 GL11.glDisable(GL11.GL_LIGHTING);
				 
				 GL11.glEnable( GL11.GL_BLEND);
				 GL11.glBlendFunc( GL11.GL_SRC_ALPHA,  GL11.GL_ONE_MINUS_SRC_ALPHA);
				 GL11.glColor4f(1.0f, 0.0f, 0.0f, 0.5f);
				 GL11.glPolygonMode(  GL11.GL_FRONT_AND_BACK,  GL11.GL_LINE );
		        
		   
		        sphere(10/2.2);	
	            
		        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_POLYGON);
		        
			}
		}
		
		private void sphere(double r) {

			final double deg_to_rad = Math.PI / 180;
			final double rad_to_deg = 180 / Math.PI;
		    // Quadrilateral strips
		    for (double phi = -90.0; phi < 90.0; phi += 10.0) {
			GL11.glBegin (GL11.GL_QUAD_STRIP);
			
			for (double thet = -180.0; thet <= 180.0; thet += 10.0) {
				GL11.glVertex3d (r*Math.sin (deg_to_rad * thet) * Math.cos (deg_to_rad * (phi + 10.0)),
			    		r*Math.sin (deg_to_rad * (phi + 10.0)),
			    		r*Math.cos (deg_to_rad * thet) * Math.cos (deg_to_rad * (phi + 10.0))	);
				GL11.glVertex3d (r*Math.sin (deg_to_rad * thet) * Math.cos (deg_to_rad * phi),
			    		 r*Math.sin (deg_to_rad * phi),
					   r*Math.cos (deg_to_rad * thet) * Math.cos (deg_to_rad * phi)  );

			}
			GL11.glEnd();
		    }

		}


}
