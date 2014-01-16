package properties;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PPercentWidget extends PropertyWidget {
	
	JSlider control = null;
	JLabel readOnlyControl = null;
	public void widgetLayout()
	{			
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(new JLabel(this.p.getDisplayName()));
		
		final PropertyWidget th = this;
		
		control = new JSlider(0,100);
		control.setValue(((PPercent)th.p.getValue()).getPercent());
		control.setPreferredSize(new Dimension(70,20));
		control.setBackground(new Color(0,0,0,0));
		
		readOnlyControl = new JLabel();
		readOnlyControl.setText(((PPercent)th.p.getValue()).getPercent()+"%");
		
		
		ChangeListener listener = new ChangeListener() {
		      public void stateChanged(ChangeEvent e) {
		    	 th.propertyUpdated(new PPercent(((JSlider)e.getSource()).getValue()));			        
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
		control.setValue(((PPercent)newvalue).getPercent());
	}



	@Override
	public void setPropertyReadOnly(boolean r) {
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