package perspectives.base;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JPanel;
import javax.swing.SwingWorker;

import perspectives.properties.PProgress;

public abstract class Task{
	
	
	private MySwingWorker worker = null;	
	
	
	public boolean done;
	
	public boolean indeterminate = false;
	
	Property<PProgress> pprogress = null;
	double progress;
	
	public boolean blocking = false; 
	
	public String name;
	
	public Task(String name)
	{
		this.name = name;
	}
	
	protected void setProgress(double progress)
	{
		this.progress = progress;
		if (pprogress != null)
			pprogress.setValue(new PProgress(progress));
	}
	
	public abstract void task();
	
	public void done()
	{
		if (pprogress != null)
			pprogress.setValue(new PProgress(2.));	
		done = true;
	}
	
	
	public void start()
	{
		done = false;	
		worker = new MySwingWorker();
		//ExecutorService threadPool = Executors.newFixedThreadPool(5);
		//threadPool.submit(worker);	
		worker.execute();
	}
	
	public void cancel()
	{		
		worker.cancel(true);
		done = true;
	}
	
	
	//swingworker class doing simulation and rendering in the background for some classes of viewers
	class MySwingWorker extends SwingWorker<Integer, Void>
	{
		public MySwingWorker()
		{
		}		
		 
 	    public Integer doInBackground() {
 	    	
 	    	task(); 
 	    	return new Integer(0);
 	       
 	    }

 	    @Override
 	    public void done() {
 	    	done = true; 	    	
 	    }
		
	}
	
	
}
