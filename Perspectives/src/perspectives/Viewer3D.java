package perspectives;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.media.opengl.FPSCounter;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.math.Quaternion;


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
