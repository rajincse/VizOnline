package perspectives.base;

import java.util.ArrayList;

import perspectives.base.PropertyType;


	public class Property<T extends PropertyType> {
		
		private String name;
		private T value;
		private boolean visible;		
		private boolean readOnly;
		private boolean publ;
		
		private boolean disabled;
		
		EventManager eventManager = null;

		
		private ArrayList<PropertyChangeListener> pcl;
	
		protected boolean updating(T newvalue)
		{
			return true;
		}
		protected void receivedBroadcast(T newvalue, PropertyManager sender)
		{
		}
		
		
		public Property(String n, T defaultValue) {
			this.name = n;
			
			visible = true;
			readOnly = false;
			publ = false;	
			disabled = false;						
			value = defaultValue;
			pcl = new ArrayList<PropertyChangeListener>();
		}
		
		
		public void setEventManager(EventManager e)
		{
			eventManager = e;
		}
		
		public void addPropertyChangeListener(PropertyChangeListener pm)
		{
			if (pcl.indexOf(pm) < 0)
				pcl.add(pm);
		}
		
		public void removePropertyChangeListener(PropertyChangeListener pm)
		{
			int index = pcl.indexOf(pm);
			if (index >= 0) pcl.remove(index);
		}
				
		public String getName()
		{
			return name;
		}
		
		public String getDisplayName()
		{
			String[] s = name.split("\\.");
			String labelText =  s[s.length-1];
			String ext = "";
			if (labelText.length() > 13)
				ext = "..";
			labelText = labelText.substring(0, Math.min(labelText.length(), 13)) + ext;		
			return labelText;
		}
		

		
		public boolean setValueDelayed(T newValue)
		{
			final T newValuef = newValue;
			if (eventManager != null)
			{
				eventManager.scheduleEvent(new PEvent()
				{
					public void process(){
						setValue(newValuef);
					}
				});
				return true;
			}
			else
				return setValue(newValue);			
		}		
		
		public boolean setValue(T newValue)
		{
			if (updating(newValue))
			{
				value = newValue;
				for (int i=0; i<pcl.size(); i++)
					pcl.get(i).propertyValueChanges(this, newValue);
				return true;
			}
			return false;
		}

		
		public T getValue()
		{
			return value;
		}
		
		
		public void setVisible(boolean v)
		{
			for (int i=0; i<pcl.size(); i++)
				pcl.get(i).propertyVisibleChanges(this, v);			
			visible = v;
		}
		
		
		public boolean getVisible()
		{
			return visible;
		}
		
		public void setReadOnly(boolean r)
		{
			for (int i=0; i<pcl.size(); i++)
				pcl.get(i).propertyReadonlyChanges(this, r);
			readOnly = r;
		}
		

		
		public boolean getReadOnly()
		{
			return readOnly;
		}
				
		
		public void setPublic(boolean s)
		{
			for (int i=0; i<pcl.size(); i++)
				pcl.get(i).propertyPublicChanges(this, s);			
			publ = s;
		}
		
		public boolean getPublic()
		{
			return publ;
		}

		
		public void setDisabled(boolean d)
		{
			for (int i=0; i<pcl.size(); i++)
				pcl.get(i).propertyDisabledChanges(this, d);
			disabled = d;

		}
		
		public boolean getDisabled()
		{
			return disabled;
		}
	}