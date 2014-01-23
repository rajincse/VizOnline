package perspectives.navigation;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import com.jogamp.newt.event.InputEvent;
import com.jogamp.newt.event.MouseEvent;

import perspectives.Viewer3D;
import perspectives.ViewerContainer3D;

public class UserNavigation3D extends Controller3D{
	private static final int INVALID = -109;
	private ViewerContainer3D container;
	private GLU glu;
	   
	private Point3D cameraLocation;
	private Point3D cameraTarget;
	private Point3D upVector;
	private double angleY; // in degree around Y axis;
	private double angleZ; // in degree around Y axis;
	private double r;
	
	private int lastX;
	private int lastY;
	public UserNavigation3D(ViewerContainer3D container,GLU glu)
	{
		this.container = container;
		this.glu = glu;
		
		this.angleY = 0;
		this.angleY =0;
		this.r =1;
		this.cameraLocation = new Point3D(0,0,0);
		this.cameraTarget = new Point3D(1,0,0);
		this.upVector = new Point3D(0,1,0);
		
		
		
		this.lastX = INVALID;
		this.lastY = INVALID;
	}

	
	
	@Override
	public void render(GL2 gl) {
		// TODO Auto-generated method stub
		//System.out.println("Look at"+this.cameraTarget[0]+","+this.cameraTarget[1]+","+ this.cameraTarget[2]);
		glu.gluLookAt(
				this.cameraLocation.x,this.cameraLocation.y, this.cameraLocation.z,
				this.cameraTarget.x,this.cameraTarget.y, this.cameraTarget.z, 
				this.upVector.x,this.upVector.y, this.upVector.z);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(this.isActivationPressed(e))
		{
			this.isEnabled = true;
		}

		if(this.isEnabled)
		{
			int keyCode = e.getKeyCode();
			switch(keyCode)
			{
				case KeyEvent.VK_UP:
					this.goForward();
					break;
				case KeyEvent.VK_DOWN:
					this.goBackWard();
					break;
				
			}
			
			this.updateCameraTarget();
			
		}
	}
	private void goForward()
	{
		double delX = this.cameraTarget.x - this.cameraLocation.x;
		this.cameraLocation.x =this.cameraLocation.x + delX * UNIT_STEP / Math.abs(delX);
		
		double delz = this.cameraTarget.z - this.cameraLocation.z;
		this.cameraLocation.z = this.cameraLocation.z+ delz * UNIT_STEP / Math.abs(delz);
	}
	
	private void goBackWard()
	{
		double delX = this.cameraTarget.x - this.cameraLocation.x;
		this.cameraLocation.x =this.cameraLocation.x - delX * UNIT_STEP / Math.abs(delX);
		
		double delz = this.cameraTarget.z - this.cameraLocation.z;
		this.cameraLocation.z = this.cameraLocation.z- delz * UNIT_STEP / Math.abs(delz);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		this.isEnabled = false;
		this.lastX= INVALID;
		this.lastY = INVALID;
	}

	@Override
	public void mousePressed(int x, int y, int button) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(int x, int y, int button) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(int x, int y) {
		// TODO Auto-generated method stub
		
		if(this.isEnabled)
		{
			if(this.lastX == INVALID && this.lastY == INVALID)
			{
				this.lastX = x;
				this.lastY = y;
			}
			else
			{
				int delX = x - this.lastX;
				int delY =y - this.lastY;
				
				if(delX < 0)
				{
					this.angleY -= UNIT_ANGLE_STEP;
				}
				else
				{
					this.angleY += UNIT_ANGLE_STEP;
				}
				
				if(this.angleY > 360)
				{
					this.angleY = this.angleY -360;
				}
				else if(this.angleY < -360)
				{
					this.angleY= this.angleY+360;
				}
				
				if(delY < 0)
				{
					this.angleZ += UNIT_ANGLE_STEP/3;
				}
				else
				{
					this.angleZ -= UNIT_ANGLE_STEP/3;
				}
				
				if(this.angleZ > 360)
				{
					this.angleZ = this.angleZ -360;
				}
				else if(this.angleZ < -360)
				{
					this.angleZ= this.angleZ+360;
				}
				
				this.updateCameraTarget();
				
				this.lastX = x;
				this.lastY = y;
			}
		}
		
	}
	
	private void updateCameraTarget()
	{
		double radianAngle = this.angleY * Math.PI / 180;
		this.cameraTarget.x =this.cameraLocation.x+ this.r * Math.cos(radianAngle);
		this.cameraTarget.z = this.cameraLocation.z+this.r * Math.sin(radianAngle);
		radianAngle = this.angleZ * Math.PI /180;
		this.cameraTarget.y =  this.cameraLocation.y+this.r * Math.sin(radianAngle);
	}



	@Override
	protected boolean isActivationPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		//return e.getKeyCode() == KeyEvent.VK_N;
		return e.isControlDown();
	}
	
}
