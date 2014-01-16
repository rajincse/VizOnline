package properties;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class PStringWidget extends PropertyWidget {
	
	JTextField control = null;
	JLabel readOnlyControl = null;
	
	public void widgetLayout()
	{			
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(new JLabel(this.p.getDisplayName()));

		final PropertyWidget th = this;
		
		control = new JTextField();
		control.setText(((PString)th.p.getValue()).stringValue());
		control.setMaximumSize(new Dimension(70,20));
		control.setPreferredSize(new Dimension(70,20));
		
		readOnlyControl = new JLabel();
		readOnlyControl.setText(((PString)th.p.getValue()).stringValue());
		//readOnlyControl.setMaximumSize(new Dimension(200,20));
		
		control.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {

		        th.propertyUpdated(new PString(control.getText()));      
		     }
		});
		
		this.add(Box.createRigidArea(new Dimension(5,1)));
		this.add(control);	
		this.add(Box.createHorizontalGlue());
	
		
		setPropertyReadOnly(p.getReadOnly());	
	}		
	
	public <T extends PropertyType> void setPropertyValue(T newvalue)
	{
		control.setText(((PString)newvalue).stringValue());
		
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