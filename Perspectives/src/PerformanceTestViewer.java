
import java.awt.Point;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Date;

import perspectives.graph.*;
import perspectives.two_d.*;
import perspectives.base.*;
import perspectives.properties.*;

public class PerformanceTestViewer extends GraphViewer{
	
	//EventManager emm;
	
	private boolean testing = false;
	
	private long testStartTime = -1;
	private long totalTestTime = 0;
	private long lastMeasureTime = 0;
	private long testingStarted = 0;
	
	private int lastFrames = 0;
	

	
	public PerformanceTestViewer(String v, GraphData gd)
	{
		super(v,gd);
		
		this.setTooltipDelay(150);
		
		final Viewer vf = this;
	
				
		
		try {
			addProperty(new Property<PInteger>("Measure",new PInteger(0))
			{
				@Override
				protected boolean updating(PInteger newvalue) {
					
					int frames = getContainer().getFrames();
					long bytes = getContainer().getBytes();

					System.out.println(vf.getName() + ":\t" + 
							(int)((frames-lastFrames)/(totalTestTime/1000.)) + " fps;\t" + 
							(int)(bytes/1024./1024.) + " MB (alltime);\t" + 
							totalTestTime/1000 + " sec interact_time;\t" + 
							(new Date().getTime()-lastMeasureTime)/1000 + " sec time;" + 
							(new Date().getTime() - testingStarted)/1000 + " sec (alltime)");
					
					totalTestTime = 0;
					lastMeasureTime = new Date().getTime();
					lastFrames = frames;
					return true;
				}

				@Override
				protected void receivedBroadcast(PInteger newvalue,
						PropertyManager sender) {
					setValue(newvalue);
				}
				
				
			});
			getProperty("Measure").setPublic(true);
			
			addProperty(new Property<PBoolean>("Testing",new PBoolean(false))
					{

						@Override
						protected boolean updating(PBoolean newvalue) {
							
						
							testing = newvalue.boolValue();
							
							totalTestTime = 0;
							lastMeasureTime = new Date().getTime();
							testingStarted = lastMeasureTime;
							lastFrames = getContainer().getFrames();
							
							((ViewerContainer2D)getContainer()).setTranslation(-1500, -1500);
						
							PFileInput pos = new PFileInput();
							if (posfile != null)
							{
								pos.path = posfile;
								getProperty("Load Positions").setValue(pos);
							}
							else
							{
								String p = getContainer().getEnvironment().getLocalDataPath();
								if (p != null)
									pos.path = p + "/edgelist_positions.txt";
								getProperty("Load Positions").setValue(pos);
							}
							
							requestRender();
							test();
							return true;
						}
				
					}
					);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	String posfile;
	public void setPositionsFile(String posfile)
	{
		//System.out.println("set positions file: " + posfile);
		this.posfile = posfile;
	}
	
	public ArrayList<Integer> getVisibleNodes()
	{
		ArrayList<String> nodes = graph.getNodes();
		
		ArrayList<Integer> vn = new ArrayList<Integer>();
		
		for (int i=0; i<nodes.size(); i++)
		{
			int[] c = getNodeCoordinates(i);			
			int[] tc = fromVisToScreen(c);
			
			if (tc[0] < 0 || tc[1] < 0 || tc[0] > this.getContainerWidth() || tc[1] > this.getContainerHeight())
				continue;
			
			vn.add(i);		
			
		}
		
	//	System.out.println("visible nodes = " +  vn.size());
		
		return vn;
	}
	
	public int[] fromVisToScreen(int[] p)
	{
		ViewerContainer2D c;
		
			Point pc =  ((ViewerContainer2D)getContainer()).modelToScreen(new Point(p[0],p[1]));
			return new int[]{(int)pc.getX(), (int)pc.getY()};
		
	}
	

	public void test()
	{
		if (testing)
		{
			//System.out.println("FPS: " + ((container.sentTiles/4) / (double)(container.interactionTime/1000)) + " " + container.sentTiles/4 + " " + container.interactionTime);
			//System.out.println("FPS_desktop: " + ((container.changedImages) / (double)(container.interactionTime/1000)) + " " + container.changedImages + " " + container.interactionTime);
			
			//select an operation and then do it
			double r = Math.random();
			
			testStartTime = new Date().getTime();
			
			if (r < 0.15)
				zoomTest();
			else if (r>=0.15 && r<0.35)
				translateTest();
			else if (r>=0.35 && r<0.6)
				hoverTest();
			else if (r>=0.6 && r<0.8)
				selectTest();
			else if (r>=0.8 && r<=1)
				moveTest();
		}
	}
	
	public void testEnded()
	{
		totalTestTime = totalTestTime + (new Date().getTime() - testStartTime);
		
		//wait for some time then test again
		double r = Math.random();
		long time = 1000+(long)(r*1000);
		
		
		em.scheduleEvent(new PEvent()
		{
			public void process() {
				test();				
			}
			
		},time);
	}
	
	int zoomDir = 1;
	public void zoomTest()
	{
	//	System.out.println("performance tester: zoom test");
		
		double zf  = zoomDir * Math.random() / 15;
		
		while (((ViewerContainer2D)getContainer()).getZoom() + 10 * zf >= 1.4)
			zf = zoomDir * Math.random() / 15;
		while (((ViewerContainer2D)getContainer()).getZoom() + 10 * zf < 0.6)
			zf = zoomDir * Math.random() / 15;
		
		final double zff = zf;
		
		
		zoomDir *= -1;		
		
		
		for (int i=0; i<10; i++)
		{
			final int fi = i;
			em.scheduleEvent(new PEvent()
			{@Override
				public void process() {
					((ViewerContainer2D)getContainer()).setZoom(((ViewerContainer2D)getContainer()).getZoom()+zff);
					requestRender();
					if (fi == 9)
					{
					//	System.out.println("performance tester: zoom test ended");
						testEnded();
					}
				}			
			},i*35);
		}
	}
	
	public void translateTest()
	{
	//	System.out.println("performance tester: start translate test");
		
		int tx = (int)(Math.random()*20) - 10;
		int ty = (int)(Math.random()*20) - 10;
		
		Point2D t = ((ViewerContainer2D)getContainer()).getTranslation();
		
		while (t.getX() + tx*20 >= (-1500+700) || t.getX() + tx*20 < (-1500-700)
				|| t.getY() + ty*20 >= (-1500+700) || t.getY() + ty*20 < (-1500-700))
		{
			tx = (int)(Math.random()*20) - 10;
			ty = (int)(Math.random()*20) - 10;
		}
		
		final int txf = tx;
		final int tyf = ty;
		
		
		for (int i=0; i<20; i++)
		{
			final int fi = i;
			em.scheduleEvent(new PEvent()
			{@Override
				public void process() {
					Point2D t = ((ViewerContainer2D)getContainer()).getTranslation();
					((ViewerContainer2D)getContainer()).setTranslation(t.getX() + txf, t.getY() + tyf);					
					requestRender();
					
					if (fi == 19)
					{
						//System.out.println("performance tester: end translate test");
						testEnded();
					}
					
				}			
			},i*30);
		}
		
		
		
		
	}
	
	public void hoverTest()
	{
		//System.out.println("performance tester: hover test");
		// pick a random visible node
		ArrayList<Integer> vn = getVisibleNodes();
		
		if (vn.size() == 0)
		{
			testEnded();
			return;
		}
		
		int r = (int)(Math.random()*(vn.size()-1));
		
		//get the screen position of this node
		int[] c = getNodeCoordinates(vn.get(r));			
		int[] tc = fromVisToScreen(c);
		
		((ViewerContainer2D)getContainer()).scheduleMouseMove(tc[0], tc[1]);
		
		em.scheduleEvent(new PEvent()
		{

			@Override
			public void process() {				
				//System.out.println("performance tester: hover test ended");
				testEnded();
			}
			
		},500);
		

	}
	
	
	public void selectTest()
	{
		//System.out.println("performance tester: select test");
		// pick a random visible node
		ArrayList<Integer> vn = getVisibleNodes();
		if (vn.size() == 0)
		{
			testEnded();
			return;
		}
		
		int r = (int)(Math.random()*(vn.size()-1));
		
		//get the screen position of this node
		int[] c = getNodeCoordinates(vn.get(r));			
		int[] tc = fromVisToScreen(c);
		
		((ViewerContainer2D)getContainer()).scheduleMouseMove(tc[0], tc[1]);
		
		this.requestRender();
		
		//System.out.println("performance tester: select test ended");
		testEnded();
	}
	
	public void moveTest()
	{
		// pick a random visible node
		ArrayList<Integer> vn = getVisibleNodes();
		
		if (vn.size() == 0)
		{
			testEnded();
			return;
		}
		
		int r = (int)(Math.random()*(vn.size()-1));	
		
		int radius = 25 + (int)(Math.random() * 100);
		
		//get the screen position of this node
		int[] c = getNodeCoordinates(vn.get(r));			
		int[] tc = fromVisToScreen(c);
		
		((ViewerContainer2D)getContainer()).scheduleMouseMove(tc[0], tc[1]);
		
		
		for (int i=0; i<30; i++)
		{
			final int x = c[0] + (int)(radius*Math.cos(2*i*Math.PI/(double)30));
			final int y = c[1] + (int)(radius*Math.sin(2*i*Math.PI/(double)30));
			final int iff = i;
			final int nodeIndex = vn.get(r);
			
			em.scheduleEvent(new PEvent()
			{

				@Override
				public void process() {
					// TODO Auto-generated method stub
					setNodeCoordinates(nodeIndex, x,y);
					
					int[] tc = fromVisToScreen(new int[]{x,y});
					((ViewerContainer2D)getContainer()).scheduleMouseMove(tc[0], tc[1]);
					
					requestRender();
					
					if (iff == 29)
					{
						//System.out.println("performance tester: move test ended");
						testEnded();
					}
				}
				
			},i*35);
			
		}
	}

}
