package perspectives;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

import properties.Property;
import properties.PropertyType;

public class EventManager implements Runnable {
	
	private ArrayList<PEvent> events;
	private ArrayList<Long> when;
	
	private long lastProcessTime;
	
	boolean processing = false;
	
	
	public EventManager()
	{

		events = new ArrayList<PEvent>();	
		when = new ArrayList<Long>();
				
		Thread t = new Thread(this);
		t.start();
	}
	
	public void scheduleEvent(PEvent e)
	{
		scheduleEvent(e, 0);
	}
	
	public void scheduleEvent(PEvent e, long afterTime)
	{
		synchronized(events)
		{
			long t = new Date().getTime() + afterTime;
			
			int i=0;
			while (i<events.size() && t > when.get(i))
				i++;
			
			events.add(i,e);	
			when.add(i, t);
			//System.out.println("Scheduled events:");
			//for (int j=0; j<events.size(); j++)
			//	System.out.print(events.get(j)+ ";   ");
			//System.out.println();
			
			synchronized(this)
			{
			this.notifyAll();
			}
		}
	}
	
	public void processEvents()
	{		
		long time = -1;
		synchronized(this)
		{
			processing = true;
		}
		while (events.size() > 0)
		{		
			PEvent e = null;		
			
			synchronized(events)
			{
				long t = new Date().getTime();
				if (when.get(0) <= t)
				{
					e = events.get(0); 
					events.remove(0);
					when.remove(0);
				}
				else
				{
					time = when.get(0) - t;
					break;
				}
			}
			
			if (e != null)
			{
				lastProcessTime = (new Date()).getTime();
				//System.out.println("Processing: " + e);
				e.process();		
			}
		}
		synchronized(this)
		{
			processing = false;
		}
		synchronized(this)
		{
		try {
			//System.out.println("Remaining events:");
			//for (int j=0; j<events.size(); j++)
			//	System.out.print(events.get(j)+ ";   ");
			//System.out.println();
			
			if (time >= 0)
			this.wait(time);
			else this.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

	@Override
	public void run() {
		
		while (true)
		{
			processEvents();
		}
		
	}
	public long lastProcess()
	{
		return lastProcessTime;
	}
	
	public boolean hasEvents()
	{
		if (events.size() != 0)
			return true;
		return false;
	}
	
	
	public boolean unresponsive(long time)
	{
		synchronized(this)
		{
			if (processing && new Date().getTime() - lastProcess() > time)
				return true;
			return false;
		}
		
	}

}
