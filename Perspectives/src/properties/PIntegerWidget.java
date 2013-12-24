package properties;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PIntegerWidget extends PropertyWidget {
	
	JSpinner control = null;
	JLabel readOnlyControl = null;
	public void widgetLayout()
	{			
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		//this.add(Box.createHorizontalGlue());
		this.add(new JLabel(this.p.getDisplayName()));
		
		final PropertyWidget th = this;
		
		control = new JSpinner();
		control.setValue(((PInteger)th.p.getValue()).intValue());
		
		control.setPreferredSize(new Dimension(100,20));
		control.setMaximumSize(new Dimension(100,20));
		
		readOnlyControl = new JLabel();
		readOnlyControl.setText(((PInteger)th.p.getValue()).intValue()+"");
		
		
		ChangeListener listener = new ChangeListener() {
		      public void stateChanged(ChangeEvent e) {
		    	 th.propertyUpdated(new PInteger(new Integer(((JSpinner)e.getSource()).getValue().toString())));			        
		      }
		    };
		control.addChangeListener(listener);

		//this.add(Box.createRigidArea(new Dimension(5,1)));
		this.add(Box.createHorizontalGlue());
		this.add(control);	
		
		
		
		setPropertyReadOnly(p.getReadOnly());
	}
	
	public <T extends PropertyType> void setPropertyValue(T newvalue)
	{
		control.setValue(((PInteger)newvalue).intValue());
	}



	@Override
	protected void setPropertyReadOnly(boolean r) {
		if (control != null)
		{
			if (r)
			{
				this.remove(control);					
				this.add(readOnlyControl,2);
			}
			else
			{
				this.remove(readOnlyControl);					
				this.add(control,2);
			}
		}			
	}
}