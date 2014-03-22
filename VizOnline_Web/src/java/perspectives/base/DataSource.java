package perspectives.base;

import java.awt.Container;
import java.io.Serializable;
import java.util.Date;

import javax.swing.JInternalFrame;

import perspectives.base.PropertyType;

/**
 * 
 * @author rdjianu
 * 
 * Datasource is a the base class that any type of data source (e.g., graph, matrix, mesh) should implement. Each datasource should have a name that will be displayed in the GUI. For each new datasource you implement, you also need to create a DataSourceFactory
 * 
 * @see DataSourceFactory
 *
 */
public abstract class DataSource extends PropertyManager implements Serializable
{
	EventManager em;
	
	public DataSource(String name)
	{
		super(name);
		loaded = false;
		
		em = new EventManager();
		
                lastUpdate = new Date().getTime();
	}
	
	
	protected boolean loaded;
	protected long lastUpdate;
	
	public boolean isLoaded()
	{
		return loaded;
	}
	
	public void setLoaded(boolean l)
	{
		loaded = l;
	}
	
	public void wasUpdated()
	{
		lastUpdate = new Date().getTime();
	}
	
	public long lastUpdate()
	{
		return lastUpdate;
	}
	
	
	private boolean blocked = false; 
	
	public boolean unresponsive()
	{
		if (em.unresponsive(2000))
			return true;
		else return false;
	}
	
	public void block(boolean blocked)
	{
		if (this.blocked == blocked) return;
		this.blocked = blocked;
		
		Property[] ps = this.getProperties();
		for (int i=0; i<ps.length; i++)
			ps[i].setDisabled(blocked);
	}
        
        public String getName(){
            return this.name;
        }

		@Override
		protected boolean addProperty(Property p, int where){
			boolean ret = super.addProperty(p, where);
			if (ret)
				p.setEventManager(em);
			 return ret;
			
		}
}
