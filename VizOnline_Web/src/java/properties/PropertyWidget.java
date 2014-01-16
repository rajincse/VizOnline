package properties;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import perspectives.PropertyManager;


public abstract class PropertyWidget extends JPanel{
	
	private PropertyManager pm;
	//private PropertyManagerViewer pmviewer;
	public Property p;
	
	private boolean changeFromOutside = false;
	
	public PropertyWidget()
	{
		
	}
	
	public void setReferences(Property p, PropertyManager pm)
	{
		this.pm = pm;
		this.p = p;			
	}
	
	public abstract void widgetLayout();
	
	public <T extends PropertyType> void propertyUpdated(T newvalue)
	{
		pm.propertyUpdatedWrapper(p, newvalue);
		p.setPropertyManager(null);
		p.setValue(newvalue);
		p.setPropertyManager(pm);		
	}
	
	/**
	 * sets the actual widgets to reflect the value change
	 * @param newvalue
	 */
	protected abstract <T extends PropertyType> void setPropertyValue(T newvalue);
	
	public <T extends PropertyType> void setProperty(T newvalue)
	{
		changeFromOutside = true;
		setPropertyValue(newvalue);
		changeFromOutside = false;
	}
	
	public abstract void setPropertyReadOnly(boolean v);
	
	private ArrayList<Component> componentStatus1;
	private ArrayList<Boolean> componentStatus2;
	public void setPropertyDisabled(boolean disabled)
	{
		
		System.out.println("set disabled " + p.getName() + " " + disabled);
		Component[] c = getComponents(this);
		
		if (disabled)
		{
			componentStatus1 = new ArrayList<Component>();
			componentStatus2 = new ArrayList<Boolean>();
			for (int i=0; i<c.length; i++)
			{
				componentStatus1.add(c[i]);
				componentStatus2.add(c[i].isEnabled());
			}
		}
				
		for (int i=0; i<c.length; i++)
		{
			if (disabled)
				c[i].setEnabled(false);
			else
			{
				if (componentStatus1 == null) c[i].setEnabled(true);
				else
				{
					int index = componentStatus1.indexOf(c[i]);
					if (index < 0) c[i].setEnabled(true);
					else c[i].setEnabled(componentStatus2.get(index));
				}
			}
			
			c[i].repaint();
		}
	}
	
	private Component[] getComponents(Component container) {
        ArrayList<Component> list = null;

        try {
            list = new ArrayList<Component>(Arrays.asList(
                  ((Container) container).getComponents()));
            for (int index = 0; index < list.size(); index++) {
            for (Component currentComponent : getComponents(list.get(index))) {
                list.add(currentComponent);
            }
            }
        } catch (ClassCastException e) {
            list = new ArrayList<Component>();
        }

        return list.toArray(new Component[list.size()]);
      }
					
}



