package perspectives.properties;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import perspectives.base.Property;
import perspectives.base.PropertyType;
import perspectives.base.PropertyWidget;

public class PTextWidget extends PropertyWidget{
	public PTextWidget(Property p) {
		super(p);
		// TODO Auto-generated constructor stub
	}


	JLabel control = null;
	JLabel readOnlyControl = null;
	
	public void widgetLayout()
	{			
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(new JLabel(this.p.getDisplayName()));

		final PropertyWidget th = this;
		
		control = new JLabel();
		control.setText(((PText)this.p.getValue()).stringValue());
		control.setMaximumSize(new Dimension(70,20));
		control.setPreferredSize(new Dimension(70,20));
		
		readOnlyControl = new JLabel();
		readOnlyControl.setText(((PText)this.p.getValue()).stringValue());
		//readOnlyControl.setMaximumSize(new Dimension(200,20));
		
		
		this.add(Box.createRigidArea(new Dimension(5,1)));
		this.add(control);	
		this.add(Box.createHorizontalGlue());
	
		
		propertyReadonlyUpdated(p.getReadOnly());	
	}		


	@Override
	public <T extends PropertyType> void propertyValueUpdated(T newvalue) {
		control.setText(((PText)newvalue).stringValue());		
	}

	@Override
	public void propertyReadonlyUpdated(boolean r) {
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

	@Override
	public void propertyVisibleUpdated(boolean newvalue) {
		// TODO Auto-generated method stub
		
	}
}
