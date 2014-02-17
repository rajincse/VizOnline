package perspectives.navigation;

import java.awt.event.KeyEvent;


import org.lwjgl.opengl.GL11;

import perspectives.Viewer3D;

public class UserNavigation extends NavigationController{
	
	private Viewer3D viewer;
	   
	protected float transx;
	protected float transy;
	protected float transz;
	
	public UserNavigation(Viewer3D viewer)
	{
		this.viewer = viewer;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
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
		else if (e.getKeyCode() == KeyEvent.VK_MINUS)
			this.transz-=UNIT_STEP;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(GL11 gl) {
		// TODO Auto-generated method stub
		gl.glTranslatef(this.transx, this.transy, this.transz);
	}
	
}
