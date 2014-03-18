package perspectives.properties;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import perspectives.base.Property;
import perspectives.base.PropertyWidget;
import perspectives.base.PropertyType;

public class POptionsWidget extends PropertyWidget {
	
	public POptionsWidget(Property p) {
		super(p);		
	}

	JComboBox control = null;
	
	public void widgetLayout()
	{
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(new JLabel(this.p.getDisplayName()));
		
		final PropertyWidget th = this;
		
		control = new JComboBox(((POptions)this.p.getValue()).options);
		
		control.setPreferredSize(new Dimension(100,20));
		control.setMaximumSize(new Dimension(100,20));
		
		control.setSelectedIndex(((POptions)this.p.getValue()).selectedIndex);
		
		ActionListener listener = new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  POptions o =  ((POptions)th.getProperty().getValue()).copy();	
		    	  o.selectedIndex = control.getSelectedIndex();
		    	 th.updateProperty(o);			        
		      }
		    };			    
	
		control.addActionListener(listener);
		
		//this.add(Box.createRigidArea(new Dimension(5,1)));
		this.add(Box.createHorizontalGlue());
		this.add(control);
		
		p.setReadOnly(p.getReadOnly());
		p.setVisible(p.getVisible());
	}
	

	@Override
	public <T extends PropertyType> void propertyValueUpdated(T newvalue) {
		control.setSelectedIndex(((POptions)newvalue).selectedIndex);		
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

