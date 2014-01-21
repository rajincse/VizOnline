

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
//			gl.glLoadIdentity();
			this.drawBox(gl, 0.0f, 0.0f, 0.0f,1.0f);
			
			gl.glPushMatrix();
			gl.glTranslatef(1.0f, 0.0f, 0.0f);
			this.drawBox(gl, 1.0f, 0.0f, 0.0f,1.0f);
			gl.glPopMatrix();
			
			gl.glPushMatrix();
			gl.glTranslatef(0.0f, 1.0f, 0.0f);
			this.drawBox(gl, 0.0f, 1.0f, 0.0f,1.0f);
			gl.glPopMatrix();
			
			gl.glPushMatrix();
			gl.glTranslatef(0.0f, 0.0f, 1.0f);
			this.drawBox(gl, 0.0f, 0.0f, 1.0f,1.0f);
			gl.glPopMatrix();
			
			this.drawQuad(gl);
	        gl.glTranslatef(2.0f, 2.0f, -2.0f);
	        
	        this.sphere(gl);
	        
	        gl.glTranslatef(-2.0f, -3.0f, 2.0f);
	        
	        float size =30.0f; 
	        this.drawGround(gl,size,0.02f, 0.32f, 0.03f );
	        
	        gl.glPushMatrix();
			gl.glTranslatef(-size, 0.0f, size);
			this.drawBox(gl, 0.0f, 0.0f, 1.0f,1.0f);
			gl.glPopMatrix();
			
			gl.glPushMatrix();
			gl.glTranslatef(size, 0.0f, size);
			this.drawBox(gl, 0.0f, 0.0f, 1.0f,1.0f);
			gl.glPopMatrix();
			
			gl.glPushMatrix();
			gl.glTranslatef(size, 0.0f, -size); 
			this.drawBox(gl, 0.0f, 0.0f, 1.0f,1.0f);
			gl.glPopMatrix();
			
			gl.glPushMatrix();
			gl.glTranslatef(-size, 0.0f, -size);
			this.drawBox(gl, 0.0f, 0.0f, 1.0f,1.0f);
			gl.glPopMatrix();
			
			 gl.glPushMatrix();
			gl.glTranslatef(0.0f, size, 0.0f);
			this.drawGround(gl, size, 1.0f, 0.0f, 0.0f);
			gl.glPopMatrix();
			
			
			gl.glPushMatrix();
			gl.glTranslatef(0.0f, -size, 0.0f);
			this.drawGround(gl, size, 0.0f, 0.0f, 1.0f);
			gl.glPopMatrix();
			
	        rotateT += ((PInteger)this.getProperty("speed").getValue()).intValue();
	        //        
	        
	        
	}
	private void drawBox(GL2 gl, float colorRed, float colorGreen, float colorBlue, float size)
	{
		gl.glPushMatrix();
		gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(colorRed,colorGreen, colorBlue);   // set the color of the quad
        
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);   // Top Left
        gl.glVertex3f(1.0f, 1.0f, -1.0f);   // Top Right
        gl.glVertex3f(1.0f, -1.0f, -1.0f);   // Bottom Right
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);   // Bottom Left
        
        
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);   // Top Left
        gl.glVertex3f(1.0f, 1.0f, 0.0f);   // Top Right
        gl.glVertex3f(1.0f, -1.0f, 0.0f);   // Bottom Right
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);   // Bottom Left
        
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);   // Top Left
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);   // Top Right
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);   // Bottom Right
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);   // Bottom Left


        gl.glVertex3f(1.0f, 1.0f, -1.0f);   // Top Left
        gl.glVertex3f(1.0f, 1.0f, 0.0f);   // Top Right
        gl.glVertex3f(1.0f, -1.0f, 0.0f);   // Bottom Right
        gl.glVertex3f(1.0f, -1.0f, -1.0f);   // Bottom Left
        
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);   // Top Left
        gl.glVertex3f(1.0f, -1.0f, -1.0f);   // Top Right
        gl.glVertex3f(1.0f, -1.0f, 0.0f);   // Bottom Right
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);   // Bottom Left
        
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);   // Top Left
        gl.glVertex3f(1.0f, 1.0f, -1.0f);   // Top Right
        gl.glVertex3f(1.0f, 1.0f, 0.0f);   // Bottom Right
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);   // Bottom Left
        gl.glEnd();	
		gl.glPopMatrix();
	}
	private void drawQuad(GL2 gl)
	{
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
	}
	private void drawGround(GL2 gl, float size, float red, float green, float blue)
	{
		gl.glPushMatrix();

        gl.glBegin(GL2.GL_QUADS);
		//gl.glColor3f(0.02f, 0.32f, 0.03f);   // set the color of the quad
        gl.glColor3f(red, green, blue);
        gl.glVertex3f(-size, 0.0f, size);   // Top Left
        gl.glVertex3f(size, 0.0f, size);   // Top Right
        gl.glVertex3f(size, 0.0f, -size);   // Bottom Right
        gl.glVertex3f(-size, 0.0f, -size);
        gl.glEnd();	 
		gl.glPopMatrix();
		
	}
	
	
	// Draw a sphere
	private void sphere(GL2 gl) {
		gl.glPushMatrix();
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

        gl.glPopMatrix();
	}

}
