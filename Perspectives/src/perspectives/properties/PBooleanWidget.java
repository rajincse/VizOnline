package perspectives.properties;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import perspectives.base.Property;
import perspectives.base.PropertyWidget;
import perspectives.base.PropertyType;


public class PBooleanWidget extends PropertyWidget {
	
	public PBooleanWidget(Property p) {
		super(p);
	}


	JCheckBox control = null;
	
	public void widgetLayout()
	{
		
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(new JLabel(this.p.getDisplayName()));
		
		final PropertyWidget th = this;
		
		control = new JCheckBox();
		control.setSelected(((PBoolean)this.p.getValue()).boolValue());
		ItemListener listener = new ItemListener() {
		     
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getSource() == control)
				{
				 boolean b = control.isSelected();
		    	  System.out.println("booleanwidget: " + b);
		    	  th.updateProperty(new PBoolean(b));
				}
				
			}
		    };
		control.addItemListener(listener);
		
		control.setBackground(new Color(0,0,0,0));

		//this.add(Box.createRigidArea(new Dimension(5,1)));
		this.add(Box.createHorizontalGlue());
		this.add(control);		
		//this.add(Box.createHorizontalGlue());
		
		
		propertyReadonlyUpdated(p.getReadOnly());
	}
	

	@Override
	public <T extends perspectives.base.PropertyType> void propertyValueUpdated(T newvalue) {
		control.setSelected(((PBoolean)newvalue).boolValue());
		
	}

	@Override
	public void propertyReadonlyUpdated(boolean r) {
		if (control != null)
			control.setEnabled(!r);
		
	}

	@Override
	public void propertyVisibleUpdated(boolean newvalue) {
		// TODO Auto-generated method stub
		
	}


}	