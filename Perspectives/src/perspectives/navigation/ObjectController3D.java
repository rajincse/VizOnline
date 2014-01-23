package perspectives.navigation;

import java.awt.event.KeyEvent;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;
import perspectives.Trackball;


import com.jogamp.newt.opengl.GLWindow;

public class ObjectController3D extends Controller3D{
	
		
	GLWindow glw;
	
	Trackball tb = new Trackball(0,0,0,5);
	
	int dragPrevX;
	int dragPrevY;
		
	boolean rotating = false;
	
	
	
	public ObjectController3D(GLWindow glw)
	{
		this.glw = glw;
	}
	
	
	
	 
		public void mouseDragged(int ex, int ey) {
			
			glw.getContext().makeCurrent();
			GL2 gl = GLContext.getCurrent().getGL().getGL2();
			
			rotating = true;
									
			tb.setRadius(10/2.2f);
			tb.setCenter(0, 0, 0);
			tb.drag(ex, ey);
			
			//tb.rot is now a quaternion indicating the axis and magnitude of the last rotation increment
			//this gets PRE-multiplied to the existing transformation, and then the whole thing gets re-stored into mvmatrix				
				 gl.glMatrixMode (gl.GL_MODELVIEW);
				 gl.glLoadIdentity();
				 gl.glMultMatrixf(tb.getRot().toMatrix(), 0);
				 gl.glMultMatrixd(mvmatrix, 0);
				 gl.glGetDoublev(gl.GL_MODELVIEW_MATRIX, mvmatrix, 0);
			
		    //this computes the global rotation that should be applied to the trackball representation (it ignores all the translations...)
				 gl.glMatrixMode (gl.GL_MODELVIEW);
				 gl.glLoadIdentity();
				 gl.glMultMatrixf(tb.getRot().toMatrix(), 0);
				 gl.glMultMatrixd(this.mvmatrixrot, 0);
				 gl.glGetDoublev(gl.GL_MODELVIEW_MATRIX, mvmatrixrot, 0);	

			dragPrevX = ex;
			dragPrevY = ey;				
			
			GLContext.getCurrent().release();
		}

		
		public void mousePressed(int x, int y, int button)
		{
			tb.click(x, y);
						
			dragPrevX = x;
			dragPrevY = y;
		}
		
		public void mouseReleased(int x, int y, int button)
		{
			tb.click(x, y);			
		}
		
		public void keyPressed(KeyEvent e)
		{	
			if(this.isActivationPressed(e))
			{
				this.isEnabled =true;
			}
			if(this.isEnabled)
			{
				glw.getContext().makeCurrent();
				GL2 gl = GLContext.getCurrent().getGL().getGL2();
				
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
				 gl.glMatrixMode (gl.GL_MODELVIEW);
				 gl.glLoadIdentity();
				 gl.glTranslated(transx,transy,transz);
				 gl.glMultMatrixd(mvmatrix, 0);
				 gl.glGetDoublev(gl.GL_MODELVIEW_MATRIX, mvmatrix, 0);
				 
				 GLContext.getCurrent().release();
			}
			
		}
		
		public void keyReleased(KeyEvent e)
		{
			this.isEnabled = false;
		}

		//renders anything related to this controller (in this case the trackball)
		public void render(GL2 gLDrawable)
		{
			glw.getContext().makeCurrent();
			GL2 gl = GLContext.getCurrent().getGL().getGL2();
			
			 gl.glMatrixMode (gl.GL_MODELVIEW);
			 gl.glLoadMatrixd(new double[]{1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,-10,1}, 0);
			 gl.glMultMatrixd(this.mvmatrixrot, 0);			 
	      
	        
	        gl.glEnable(GL.GL_BLEND);
	        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
	        gl.glColor4f(1.0f, 0.0f, 0.0f, 0.05f);
	        gl.glPolygonMode( gl.GL_FRONT, gl.GL_LINE );
	        sphere(gl,10/2.2);	
            
	        gl.glPolygonMode( gl.GL_FRONT_AND_BACK, gl.GL_POLYGON);
	        
	        GLContext.getCurrent().release();
		}
		
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


		@Override
		public void mouseMoved(int x, int y) {
			// TODO Auto-generated method stub
			
		}




		@Override
		protected boolean isActivationPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			return e.isShiftDown();
		}





}
