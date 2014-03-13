package perspectives.three_d;

import java.awt.Graphics2D;

import perspectives.base.Viewer;



public abstract class Viewer3D extends Viewer{

	public abstract void render();
	
	public void render2DOverlay(Graphics2D g)
	{
		
	}
	
		
	public Viewer3D(String name) {
		super(name);	
	}
	
	public boolean mousepressed(int x, int y, int button) {return false;};
	public boolean mousereleased(int x, int y, int button) {return false;};
	public boolean mousemoved(int x, int y)
	{ 
		return false;
	};
	public boolean mousedragged(int currentx, int currenty, int oldx, int oldy){return false;};
	
	
	
	  

}
