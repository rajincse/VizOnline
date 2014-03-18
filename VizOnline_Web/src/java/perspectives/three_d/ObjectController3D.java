package perspectives.three_d;

import java.awt.event.KeyEvent;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;



public class ObjectController3D{
	
	
	
	perspectives.three_d.Trackball tb;
	int dragPrevX;
	int dragPrevY;
		
	boolean rotating = false;
	
	public double[] mvmatrix = new double[]{1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,0,1};
	
	private double[] mvmatrixrot = new double[]{1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,0,1};
	
	public ObjectController3D()
	{
		System.out.println("create object controller 3D");
		tb = new Trackball(0,0,0,5);
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
			
			System.out.println("rotating");
				
									
			tb.setRadius(10/2.2f);
			tb.setCenter(0, 0, 0);
			tb.drag(ex, ey);
			
			//tb.rot is now a quaternion indicating the axis and magnitude of the last rotation increment
			//this gets PRE-multiplied to the existing transformation, and then the whole thing gets re-stored into mvmatrix				
				 GL11.glMatrixMode (GL11.GL_MODELVIEW);
				 GL11.glLoadIdentity();
				 DoubleBuffer mvmatrixbuf = BufferUtils.createDoubleBuffer(mvmatrix.length);
				 mvmatrixbuf.put(mvmatrix);
				 mvmatrixbuf.rewind();
				 FloatBuffer rotbuf = BufferUtils.createFloatBuffer(16);
				 Matrix4f mrot = new Matrix4f();
				 mrot.rotate(tb.getRot().getW(), new Vector3f(tb.getRot().getX(), tb.getRot().getY(), tb.getRot().getZ()));
				 rotbuf.put(new float[]{mrot.m00, mrot.m01, mrot.m02, mrot.m03, mrot.m10, mrot.m11, mrot.m12, mrot.m13, mrot.m20, mrot.m21, mrot.m22,mrot.m23, mrot.m30, mrot.m31, mrot.m32, mrot.m33});
				 rotbuf.rewind();
				 GL11.glMultMatrix(rotbuf);
				 GL11.glMultMatrix(mvmatrixbuf); mvmatrixbuf.rewind();
				 GL11.glGetDouble(GL11.GL_MODELVIEW_MATRIX, mvmatrixbuf);
				 mvmatrixbuf.get(mvmatrix); 
			
		    //this computes the global rotation that should be applied to the trackball representation (it ignores all the translations...)
				 GL11.glMatrixMode (GL11.GL_MODELVIEW);
				 GL11.glLoadIdentity();
				 
				 rotbuf.rewind();
				 GL11.glMultMatrix(rotbuf); mvmatrixbuf.rewind();
				 GL11.glMultMatrix(mvmatrixbuf);mvmatrixbuf.rewind();
				 GL11.glGetDouble(GL11.GL_MODELVIEW_MATRIX, mvmatrixbuf);
				 mvmatrixbuf.get(mvmatrixrot);
				 
				 

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
		
	
		public void keyPressed(String keyText, String modifiersText, Pbuffer buff)
		{	
			System.out.println("obj controller 3D key press");
			try {
				buff.makeCurrent();
			} catch (LWJGLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			int transx = 0;
			int transy = 0;
			int transz = 0;
			if (keyText == "Up")
				transy = 1;
			else if (keyText == "Down")
				transy = -1;
			else if (keyText == "Left")
				transx = -1;
			else if (keyText == "Right")
				transx = 1;
			else if (keyText == "Equals")
				transz = -1;
			else if (keyText == "Minus")
				transz = 1;
			else
				return;
			
			//the translation is premultiplied to the existing transformation 
			 GL11.glMatrixMode (GL11.GL_MODELVIEW);
			 GL11.glLoadIdentity();
			 GL11.glTranslated(transx,transy,transz);
			 DoubleBuffer mvmatrixbuf = BufferUtils.createDoubleBuffer(mvmatrix.length);
			 mvmatrixbuf.put(mvmatrix);mvmatrixbuf.rewind();
			 GL11.glMultMatrix(mvmatrixbuf);mvmatrixbuf.rewind();
			 GL11.glGetDouble(GL11.GL_MODELVIEW_MATRIX, mvmatrixbuf);
			 mvmatrixbuf.get(mvmatrix);
			 
			 for (int i=0; i<mvmatrix.length; i++)
				 System.out.println(mvmatrix[i]);
			 
			try {
				buff.releaseContext();
			} catch (LWJGLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
		public void keyReleased(String keyText, String modifiersText, Pbuffer buff)
		{
		}

		//renders anything related to this controller (in this case the trackball)
		public void render()
		{
			if (rotating)
			{
				 GL11.glMatrixMode ( GL11.GL_MODELVIEW);
				 DoubleBuffer cambuf = BufferUtils.createDoubleBuffer(16);
				 cambuf.put(new double[]{1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,-10,1});
				 GL11.glLoadMatrix(cambuf);
				 DoubleBuffer mvmatrixbuf = BufferUtils.createDoubleBuffer(mvmatrix.length);
				 mvmatrixbuf.put(mvmatrix);
				 GL11.glMultMatrix(mvmatrixbuf);			 
		      
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
