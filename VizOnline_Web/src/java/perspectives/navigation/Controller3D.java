package perspectives.navigation;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL2;

public abstract class Controller3D {
	//transformation matrix
	public double[] mvmatrix = new double[]{1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,0,1};
	
	protected double[] mvmatrixrot = new double[]{1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,0,1};
	
	public static double UNIT_STEP =0.5;
	public static double UNIT_ANGLE_STEP =0.005;
	
	protected boolean isEnabled;
	
	abstract protected boolean isActivationPressed(KeyEvent e);
	abstract public void mouseMoved(int x, int y);
	abstract public void mousePressed(int x, int y, int button);
	abstract public void mouseReleased(int x, int y, int button);
	abstract public void keyPressed(KeyEvent e);
	abstract public void keyReleased(KeyEvent e);
	abstract public void render(GL2 gl);
	
}
