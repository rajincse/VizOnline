package perspectives.three_d;

import java.awt.Point;
import java.awt.event.KeyEvent;


import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.util.glu.GLU;

public class UserNavigation3D extends Controller3D{
	private static final int INVALID = -109;
	
	private static final int CENTER_X =520;
	private static final int CENTER_Y = 374;
	private static final int THRESHOLD_RADIUS = 50;
	private ViewerContainer3D container;
	   
	private Point3D cameraLocation;
	private Point3D cameraTarget;
	private Point3D upVector;
	private double angleY; // in degree around Y axis;
	private double angleZ; // in degree around Y axis;
	private double r;
	
	private int lastX;
	private int lastY;
	public UserNavigation3D(ViewerContainer3D container)
	{
		this.container = container;
		
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
	public void render(GL11 gl) {
		// TODO Auto-generated method stub
		//System.out.println("Look at"+this.cameraTarget[0]+","+this.cameraTarget[1]+","+ this.cameraTarget[2]);
		
		GLU.gluLookAt(
				(float)this.cameraLocation.x,(float)this.cameraLocation.y,(float) this.cameraLocation.z,
				(float)this.cameraTarget.x,(float)this.cameraTarget.y,(float) this.cameraTarget.z, 
				(float)this.upVector.x,(float)this.upVector.y,(float) this.upVector.z);
	}
	
//	@Override
//	public void keyPressed(KeyEvent e) {
//		if(this.isActivationPressed(e))
//		{
//			this.isEnabled = true;
//		}
//
//		if(this.isEnabled)
//		{
//			int keyCode = e.getKeyCode();
//			switch(keyCode)
//			{
//				case KeyEvent.VK_UP:
//					this.goForward();
//					break;
//				case KeyEvent.VK_DOWN:
//					this.goBackWard();
//					break;
//				
//			}
//			
//			this.updateCameraTarget();
//			
//		}
//	}
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

//	@Override
//	public void keyReleased(KeyEvent e) {
//		// TODO Auto-generated method stub
//		this.isEnabled = false;
//		this.lastX= INVALID;
//		this.lastY = INVALID;
//	}

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
				int delX = x - CENTER_X;
				int delY =y - CENTER_Y;
				double distance = Point.distance(x, y, CENTER_X, CENTER_Y);
				if(distance > THRESHOLD_RADIUS)
				{
					this.angleY += UNIT_ANGLE_STEP * delX;
				}
				
				if(this.angleY > 360)
				{
					this.angleY = this.angleY -360;
				}
				else if(this.angleY < -360)
				{
					this.angleY= this.angleY+360;
				}
				
				if(distance > THRESHOLD_RADIUS)
				{
					this.angleZ -= UNIT_ANGLE_STEP* delY / 2;
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



	@Override
	public void keyPressed(String keyText, String modifiersText, Pbuffer buff) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void keyReleased(String keyText, String modifiersText, Pbuffer buff) {
		// TODO Auto-generated method stub
		
	}
	
}
