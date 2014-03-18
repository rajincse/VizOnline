package perspectives.properties;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import perspectives.base.Property;
import perspectives.base.PropertyWidget;
import perspectives.base.PropertyType;

public class PIntegerWidget extends PropertyWidget {
	
	public PIntegerWidget(Property p) {
		super(p);	
	}



	JSpinner control = null;
	JLabel readOnlyControl = null;
	public void widgetLayout()
	{			
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		//this.add(Box.createHorizontalGlue());
		this.add(new JLabel(this.p.getDisplayName()));
		
		final PropertyWidget th = this;
		
		control = new JSpinner();
		control.setValue(((PInteger)this.p.getValue()).intValue());
		
		control.setPreferredSize(new Dimension(100,20));
		control.setMaximumSize(new Dimension(100,20));
		
		readOnlyControl = new JLabel();
		readOnlyControl.setText(((PInteger)this.p.getValue()).intValue()+"");
		
		
		ChangeListener listener = new ChangeListener() {
		      public void stateChanged(ChangeEvent e) {
		    	 th.updateProperty(new PInteger(new Integer(((JSpinner)e.getSource()).getValue().toString())));			        
		      }
		    };
		control.addChangeListener(listener);

		//this.add(Box.createRigidArea(new Dimension(5,1)));
		this.add(Box.createHorizontalGlue());
		this.add(control);			
		
		propertyReadonlyUpdated(p.getReadOnly());
	}
	



	@Override
	public <T extends PropertyType> void propertyValueUpdated(T newvalue) {
		control.setValue(((PInteger)newvalue).intValue());
	}

	@Override
	public void propertyReadonlyUpdated(boolean r) {
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

	@Override
	public void propertyVisibleUpdated(boolean newvalue) {
		// TODO Auto-generated method stub
		
	}
}