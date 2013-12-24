package properties;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class POptionsWidget extends PropertyWidget {
	
	JComboBox control = null;
	
	public void widgetLayout()
	{
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(new JLabel(this.p.getDisplayName()));
		
		final PropertyWidget th = this;
		
		control = new JComboBox(((POptions)th.p.getValue()).options);
		
		control.setPreferredSize(new Dimension(100,20));
		control.setMaximumSize(new Dimension(100,20));
		
		ActionListener listener = new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  POptions o =  ((POptions)th.p.getValue()).copy();	
		    	  o.selectedIndex = control.getSelectedIndex();
		    	 th.propertyUpdated(o);			        
		      }
		    };			    
	
		control.addActionListener(listener);
		
		//this.add(Box.createRigidArea(new Dimension(5,1)));
		this.add(Box.createHorizontalGlue());
		this.add(control);
		
		p.setReadOnly(p.getReadOnly());
		p.setVisible(p.getVisible());
	}
	
	public <T extends PropertyType> void setPropertyValue(T newvalue)
	{
		control.setSelectedIndex(((POptions)newvalue).selectedIndex);
	}

	@Override
	protected void setPropertyReadOnly(boolean r) {
		if (control != null)
			control.setEnabled(!r);
		
		
	}
}	

