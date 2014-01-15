package perspectives.navigation;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL2;

import perspectives.Viewer3D;

public class UserNavigation extends NavigationController{
	
	private Viewer3D viewer;
	   
	protected float transx;
	protected float transy;
	protected float transz;
	protected float rotationx;
	protected float rotationy;
	protected float rotationz;
	public UserNavigation(Viewer3D viewer)
	{
		this.viewer = viewer;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.isAltDown()) // for rotation
		{
			if (e.getKeyCode() == KeyEvent.VK_UP)
				this.rotationx+= UNIT_ANGLE_STEP;
			else if (e.getKeyCode() == KeyEvent.VK_DOWN)
				this.rotationx-=UNIT_ANGLE_STEP;
			else if (e.getKeyCode() == KeyEvent.VK_LEFT)
				this.rotationy-=UNIT_ANGLE_STEP;
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
				this.rotationy+=UNIT_ANGLE_STEP;
			else if (e.getKeyCode() == KeyEvent.VK_PLUS || e.getKeyCode() == 61)
				this.rotationz-=UNIT_ANGLE_STEP;
			else if (e.getKeyCode() == KeyEvent.VK_MINUS )
				this.rotationz+=UNIT_ANGLE_STEP;
		}
		else // linear navigations
		{
			if (e.getKeyCode() == KeyEvent.VK_UP)
				this.transy-= UNIT_STEP;
			else if (e.getKeyCode() == KeyEvent.VK_DOWN)
				this.transy+=UNIT_STEP;
			else if (e.getKeyCode() == KeyEvent.VK_LEFT)
				this.transx+=UNIT_STEP;
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
				this.transx-=UNIT_STEP;
			else if (e.getKeyCode() == KeyEvent.VK_PLUS || e.getKeyCode() == 61)
				this.transz+=UNIT_STEP;
			else if (e.getKeyCode() == KeyEvent.VK_MINUS )
				this.transz-=UNIT_STEP;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	private void performRotation(GL2 gl)
	{
		
		gl.glRotatef(this.rotationx,1.0f, 0.0f, 0.0f);
		gl.glRotatef(this.rotationy,0.0f, 1.0f, 0.0f);
		gl.glRotatef(this.rotationz,0.0f, 0.0f, 1.0f);
		
	}

	@Override
	public void render(GL2 gl) {
		// TODO Auto-generated method stub
		this.performRotation(gl);
		gl.glTranslatef(this.transx, this.transy, this.transz);
	}
	
}
