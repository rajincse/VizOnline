package perspectives.base;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

import perspectives.base.PropertyType;

public class EventManager implements Runnable {
	
	private ArrayList<PEvent> events;
	private ArrayList<Long> when;
	private ArrayList<String> ids; 
	
	private long lastProcessTime;
	
	boolean processing = false;
	
	
	public EventManager()
	{

		events = new ArrayList<PEvent>();	
		when = new ArrayList<Long>();
		ids = new ArrayList<String>();
				
		Thread t = new Thread(this);
		t.start();
	}
	
	public void replaceEvent(PEvent e, String id)
	{
		replaceEvent(e,id,0);
	}
	
	public void replaceEvent(PEvent e, String id, long afterTime)
	{
		synchronized(events)
		{	
			for (int i=0; i<events.size(); i++)
				if (ids.get(i).equals(id))
				{
					events.remove(i);
					when.remove(i);
					ids.remove(i);
					i--;
				}
				
			long t = new Date().getTime() + afterTime;
			
			int i=0;
			while (i<events.size() && t > when.get(i))
				i++;
			
			events.add(i,e);	
			when.add(i, t);
			ids.add(i,id);
				
				synchronized(this)
				{
					this.notifyAll();
				}
				
				
		}
	}
	
	public void scheduleEvent(PEvent e)
	{
		scheduleEvent(e, 0, "event");
	}
	
	public void scheduleEvent(PEvent e, long afterTime)
	{
		scheduleEvent(e, afterTime, "event");
	}
	
	public void scheduleEvent(PEvent e, String id)
	{
		
		scheduleEvent(e, 0, id);
	}
	
	public void scheduleEvent(PEvent e, long afterTime, String id)
	{
		synchronized(events)
		{
			long t = new Date().getTime() + afterTime;
			
			int i=0;
			while (i<events.size() && t > when.get(i))
				i++;
			
			events.add(i,e);	
			when.add(i, t);
			ids.add(id);
			
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
		while (true)
		{	
			PEvent e = null;			
			synchronized(events)
			{
				if (events.size() > 0)
				{
					long t = new Date().getTime();
					if (when.get(0) <= t)
					{					
						e = events.get(0); 
						events.remove(0);
						when.remove(0);
						ids.remove(0);
					}
					else
					{
						time = when.get(0) - t;
						break;
					}
				}
			}
			
			if (e != null)
			{
				
				lastProcessTime = (new Date()).getTime();				
				e.process();		
			}
			
			synchronized(events)
			{
				if (events.size() == 0) break;
			}
		}
		synchronized(this)
		{
			processing = false;
		}
		synchronized(this)
		{
		try {			
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
