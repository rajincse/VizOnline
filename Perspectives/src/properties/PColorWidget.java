package properties;

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

public class PColorWidget extends PropertyWidget {
	
	JButton control = null;
			
	public void widgetLayout()
	{
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		final PropertyWidget th = this;
		
		control = new JButton(this.p.getDisplayName(), new ImageIcon(Toolkit.getDefaultToolkit().getImage("color_picker.GIF")));
		//control = new JButton("coo");
		control.setMaximumSize(new Dimension(2000,20));
		control.setPreferredSize(new Dimension(130,20));
		ActionListener listener = new ActionListener() {
		      public void actionPerformed(ActionEvent e) {  
		    	 
		    	  Color newColor = JColorChooser.showDialog(th,"Choose Color",((PColor)th.p.getValue()).colorValue());
		    	  th.propertyUpdated(new PColor(newColor));
		    	  
		      }
		    };
		control.addActionListener(listener);

		this.add(Box.createHorizontalGlue());
		this.add(control);
		this.add(Box.createHorizontalGlue());
		
		p.setReadOnly(p.getReadOnly());
		p.setVisible(p.getVisible());
	}		
	
	public <T extends PropertyType> void setPropertyValue(T newvalue)
	{
		this.propertyUpdated(newvalue);
	}

	@Override
	public void setPropertyReadOnly(boolean r) {
		if (control != null)
			control.setEnabled(!r);	
	}
}