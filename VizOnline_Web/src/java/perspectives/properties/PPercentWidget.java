package perspectives.properties;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import perspectives.base.Property;
import perspectives.base.PropertyWidget;
import perspectives.base.PropertyType;

public class PPercentWidget extends PropertyWidget {
	
	public PPercentWidget(Property p) {
		super(p);
	}



	JSlider control = null;
	JLabel readOnlyControl = null;
	public void widgetLayout()
	{			
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(new JLabel(this.p.getDisplayName()));
		
		final PropertyWidget th = this;
		
		control = new JSlider(0,100);
		control.setValue(((PPercent)this.p.getValue()).getPercent());
		control.setPreferredSize(new Dimension(70,20));
		control.setBackground(new Color(0,0,0,0));
		
		readOnlyControl = new JLabel();
		readOnlyControl.setText(((PPercent)this.p.getValue()).getPercent()+"%");
		
		
		ChangeListener listener = new ChangeListener() {
		      public void stateChanged(ChangeEvent e) {
		    	 th.updateProperty(new PPercent(((JSlider)e.getSource()).getValue()));			        
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
		control.setValue(((PPercent)newvalue).getPercent());
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