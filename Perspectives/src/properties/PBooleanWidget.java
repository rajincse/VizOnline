package properties;

import java.awt.Color;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PBooleanWidget extends PropertyWidget {
	
	JCheckBox control = null;
	
	public void widgetLayout()
	{
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(new JLabel(this.p.getDisplayName()));
		
		final PropertyWidget th = this;
		
		control = new JCheckBox();
		control.setSelected(((PBoolean)th.p.getValue()).boolValue());
		ChangeListener listener = new ChangeListener() {
		      public void stateChanged(ChangeEvent e) {
		    	  boolean b = control.isSelected();
		    	  System.out.println(b);
		    	  th.propertyUpdated(new PBoolean(b));			        
		      }
		    };
		control.addChangeListener(listener);
		
		control.setBackground(new Color(0,0,0,0));

		//this.add(Box.createRigidArea(new Dimension(5,1)));
		this.add(Box.createHorizontalGlue());
		this.add(control);		
		//this.add(Box.createHorizontalGlue());
		
		
		setPropertyReadOnly(p.getReadOnly());
	}
	
	public <T extends PropertyType> void setPropertyValue(T newvalue)
	{
		control.setSelected(((PBoolean)newvalue).boolValue());
	}


	@Override
	public void setPropertyReadOnly(boolean r) {
		if (control != null)
			control.setEnabled(!r);
	}
}	