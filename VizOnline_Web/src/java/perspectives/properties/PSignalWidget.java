package perspectives.properties;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;

import perspectives.base.Property;
import perspectives.base.PropertyWidget;
import perspectives.base.PropertyType;

public class PSignalWidget extends PropertyWidget {
	
	public PSignalWidget(Property p) {
		super(p);
	}

	JButton control = null;
			
	public void widgetLayout()
	{
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		final PropertyWidget th = this;
		
		control = new JButton(this.p.getDisplayName(), new ImageIcon(Toolkit.getDefaultToolkit().getImage("color_picker.GIF")));
		control.setMaximumSize(new Dimension(2000,20));
		control.setPreferredSize(new Dimension(130,20));
		ActionListener listener = new ActionListener() {
		      public void actionPerformed(ActionEvent e) {		    	
		    	  th.updateProperty(new PSignal());  
		      }
		    };
		control.addActionListener(listener);

		this.add(Box.createHorizontalGlue());
		this.add(control);
		this.add(Box.createHorizontalGlue());
		
		p.setReadOnly(p.getReadOnly());
		p.setVisible(p.getVisible());
	}		
	

	@Override
	public <T extends PropertyType> void propertyValueUpdated(T newvalue) {
		// TODO Auto-generated method stub
		
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
