package properties;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PProgressWidget extends PropertyWidget {
	
	JProgressBar control = null;
	public void widgetLayout()
	{			
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(new JLabel(this.p.getDisplayName()));
		
		final PropertyWidget th = this;
		
		control = new JProgressBar(0,100);
		control.setValue((int)(100*((PProgress)th.p.getValue()).getValue()));
		control.setPreferredSize(new Dimension(70,20));
		control.setBackground(new Color(0,0,0,0));	
		
		if (((PProgress)th.p.getValue()).indeterminable)
			control.setIndeterminate(true);		

		//this.add(Box.createRigidArea(new Dimension(5,1)));
		this.add(Box.createHorizontalGlue());
		this.add(control);		
	}
	
	public <T extends PropertyType> void setPropertyValue(T newvalue)
	{
		control.setValue( (int)(100*((PProgress)newvalue).getValue()));
	}



	@Override
	public void setPropertyReadOnly(boolean r) {
	}
}