package perspectives.properties;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import perspectives.base.Property;
import perspectives.base.PropertyWidget;
import perspectives.base.PropertyType;

public class PProgressWidget extends PropertyWidget {
	
	public PProgressWidget(Property p) {
		super(p);
		// TODO Auto-generated constructor stub
	}



	JProgressBar control = null;
	public void widgetLayout()
	{			
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(new JLabel(this.p.getDisplayName()));
		
		final PropertyWidget th = this;
		
		control = new JProgressBar(0,100);
		control.setValue((int)(100*((PProgress)this.p.getValue()).getValue()));
		control.setPreferredSize(new Dimension(70,20));
		control.setBackground(new Color(0,0,0,0));	
		
		if (((PProgress)this.p.getValue()).indeterminable)
			control.setIndeterminate(true);		

		//this.add(Box.createRigidArea(new Dimension(5,1)));
		this.add(Box.createHorizontalGlue());
		this.add(control);		
	}
	

	@Override
	public <T extends PropertyType> void propertyValueUpdated(T newvalue) {
		control.setValue( (int)(100*((PProgress)newvalue).getValue()));
		
	}

	@Override
	public void propertyReadonlyUpdated(boolean newvalue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propertyVisibleUpdated(boolean newvalue) {
		// TODO Auto-generated method stub
		
	}
}