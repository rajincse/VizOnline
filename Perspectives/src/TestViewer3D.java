

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import perspectives.Viewer3D;
import properties.*;



public class TestViewer3D extends Viewer3D{

	float rotateT = 0;
	
	public TestViewer3D(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		
		
		Property<PInteger> p = new Property<PInteger>("speed", new PInteger(0));
		
		try {
			this.addProperty(p);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected <T extends PropertyType> void propertyUpdated(Property p, T newvalue)
	{
		if (p.getName() == "speed")
		{
			this.requestRender();
		}
	}

	@Override
	public String getViewerType()
	{
		return "Viewer3D";	
	}
	
	public void render(GL2 gl)
	{

	       // gl.glLoadIdentity();
	       

	        // rotate about the three axes
//	        gl.glRotatef(rotateT, 1.0f, 0.0f, 0.0f);
//	        gl.glRotatef(rotateT, 0.0f, 1.0f, 0.0f);
//	        gl.glRotatef(rotateT, 0.0f, 0.0f, 1.0f);
		
	//	sphere(gl);

	        // Draw A Quad
			gl.glPushMatrix();
	        gl.glBegin(GL2.GL_QUADS);
	        gl.glColor3f(0.0f, 0.0f, 0.0f);   // set the color of the quad
	        
	        gl.glVertex3f(-1.0f, 1.0f, -1.0f);   // Top Left
	        gl.glVertex3f(1.0f, 1.0f, -1.0f);   // Top Right
	        gl.glVertex3f(1.0f, -1.0f, -1.0f);   // Bottom Right
	        gl.glVertex3f(-1.0f, -1.0f, -1.0f);   // Bottom Left
	        
	        gl.glColor3f(0.0f, 1.0f, 0.0f);   // set the color of the quad
	        gl.glVertex3f(-1.0f, 1.0f, 0.0f);   // Top Left
	        gl.glVertex3f(1.0f, 1.0f, 0.0f);   // Top Right
	        gl.glVertex3f(1.0f, -1.0f, 0.0f);   // Bottom Right
	        gl.glVertex3f(-1.0f, -1.0f, 0.0f);   // Bottom Left
	        
	        gl.glColor3f(0.0f, 0.0f, 0.0f);   // set the color of the quad
	        gl.glVertex3f(-1.0f, 1.0f, -1.0f);   // Top Left
	        gl.glVertex3f(-1.0f, 1.0f, 0.0f);   // Top Right
	        gl.glVertex3f(-1.0f, -1.0f, 0.0f);   // Bottom Right
	        gl.glVertex3f(-1.0f, -1.0f, -1.0f);   // Bottom Left
	        
	        gl.glColor3f(1.0f, 1.0f, 0.0f);   // set the color of the quad
	        gl.glVertex3f(1.0f, 1.0f, -1.0f);   // Top Left
	        gl.glVertex3f(1.0f, 1.0f, 0.0f);   // Top Right
	        gl.glVertex3f(1.0f, -1.0f, 0.0f);   // Bottom Right
	        gl.glVertex3f(1.0f, -1.0f, -1.0f);   // Bottom Left
	        
	        gl.glColor3f(1.0f, 0.0f, 1.0f);   // set the color of the quad
	        gl.glVertex3f(-1.0f, -1.0f, -1.0f);   // Top Left
	        gl.glVertex3f(1.0f, -1.0f, -1.0f);   // Top Right
	        gl.glVertex3f(1.0f, -1.0f, 0.0f);   // Bottom Right
	        gl.glVertex3f(-1.0f, -1.0f, 0.0f);   // Bottom Left
	        
	        gl.glColor3f(0.0f, 1.0f, 1.0f);   // set the color of the quad
	        gl.glVertex3f(-1.0f, 1.0f, -1.0f);   // Top Left
	        gl.glVertex3f(1.0f, 1.0f, -1.0f);   // Top Right
	        gl.glVertex3f(1.0f, 1.0f, 0.0f);   // Bottom Right
	        gl.glVertex3f(-1.0f, 1.0f, 0.0f);   // Bottom Left
	        gl.glEnd();	 
	        
	        gl.glPopMatrix();
	
	        gl.glTranslatef(2.0f, 2.0f, -2.0f);
	        gl.glPushMatrix();
	        this.sphere(gl);
	        gl.glPopMatrix();
	        
	        rotateT += ((PInteger)this.getProperty("speed").getValue()).intValue();
	        //        
	        
	        
	}
	
	
	// Draw a sphere
		private void sphere(GL2 gl) {

			final double deg_to_rad = Math.PI / 180;
			final double rad_to_deg = 180 / Math.PI;
		    // Quadrilateral strips
		    for (double phi = 0.0; phi < 10.0; phi += 10.0) {
			gl.glBegin (gl.GL_QUAD_STRIP);
			gl.glColor3f(1.0f, 0.0f, 0.0f);
			for (double thet = -180.0; thet <= 180.0; thet += 10.0) {
			    gl.glVertex3d (Math.sin (deg_to_rad * thet) * Math.cos (deg_to_rad * phi),
					   Math.cos (deg_to_rad * thet) * Math.cos (deg_to_rad * phi),
					   Math.sin (deg_to_rad * phi));
			    gl.glVertex3d (Math.sin (deg_to_rad * thet) * Math.cos (deg_to_rad * (phi + 10.0)),
			    		Math.cos (deg_to_rad * thet) * Math.cos (deg_to_rad * (phi + 10.0)),
			    		Math.sin (deg_to_rad * (phi + 10.0)));
			}
			gl.glEnd();
		    }

		    // North pole
		    gl.glBegin (GL.GL_TRIANGLE_FAN);
		    gl.glVertex3d (0.0, 0.0, 1.0);
		    for (double thet = -180.0; thet <= 180.0; thet += 10.0) {
			gl.glVertex3d (Math.sin (deg_to_rad * thet) * Math.cos (deg_to_rad * 80.0),
					Math.cos (deg_to_rad * thet) * Math.cos (deg_to_rad * 80.0),
					Math.sin (deg_to_rad * 80.0));
		    }
		    gl.glEnd();

		    // South pole
		    gl.glBegin (GL.GL_TRIANGLE_FAN);
		    gl.glVertex3d (0.0, 0.0, -1.0);
		    for (double thet = -180.0; thet <= 180.0; thet += 10.0) {
			gl.glVertex3d (Math.sin (deg_to_rad * thet) * Math.cos (deg_to_rad * -80.0),
					Math.cos (deg_to_rad * thet) * Math.cos (deg_to_rad * -80.0),
					Math.sin (deg_to_rad * -80.0));
		    }
		    gl.glEnd();
		}

}
