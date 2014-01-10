package perspectives.navigation;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL2;

public abstract class NavigationController {
	public static float UNIT_STEP =0.5f;
	
	abstract public void keyPressed(KeyEvent e);
	abstract public void keyReleased(KeyEvent e);
	abstract public void render(GL2 gl);
	
}
