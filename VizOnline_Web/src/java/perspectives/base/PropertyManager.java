package perspectives.base;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import perspectives.properties.PProgress;
import perspectives.base.PropertyType;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;


public class PropertyManager{
	
	protected String name;	
	ArrayList<Property> props;		
	PropertyManagerGroup propertyManagerGroup;	
	private boolean blockedd = false;	
	
	ArrayList<PropertyManagerChangeListener> pmcl;
	
	PropertyChangeListener pcl;
	
	 private static ArrayList<PropertyType> acceptedTypes = new ArrayList<PropertyType>();
	 
	 public static void registerType(PropertyType c)
	 {
		 int found = -1;
		 for (int i=0; i<acceptedTypes.size(); i++)
			 if (acceptedTypes.get(i).typeName().equals(c.typeName()))
			 {
				 found = i;
				 break;
			 }
		 
		 if (found < 0)
				acceptedTypes.add(c);
	 }
	 
	 public static PropertyType deserialize(String type, String value)
	 {
		 int found = -1;
		 for (int i=0; i<acceptedTypes.size(); i++)
			 if (acceptedTypes.get(i).typeName().equals(type))
			 {
				 found = i;
				 break;
			 }
		 if (found < 0) return null;
		 
		 return acceptedTypes.get(found).deserialize(value);
	 }
	
	
	public PropertyManager(String name) {		
		
		this.name = name;			
		props = new ArrayList<Property>();	
		pmcl = new ArrayList<PropertyManagerChangeListener>();
		
		final PropertyManager thisf = this;
		
		pcl = new PropertyChangeListener()
		{

			@Override
			public void propertyValueChanges(Property p, PropertyType newValue) {
				
				if (tasks.indexOf(p) >= 0 )
				{
					if (((PProgress)newValue).getValue() > 1)
					{
						thisf.removeProperty(p.getName());
						tasks.remove(tasks.indexOf(p));
						Property[] ps = getProperties();
						for (int i=0; i<ps.length; i++)
							ps[i].setDisabled(false);
						
						blockedd = false;
					}
					//return;
				}
				
				newValue.sources.add(thisf) ;	
				
				for (int i=0; i<thisf.pmcl.size(); i++)
					thisf.pmcl.get(i).propertyValueChanges(thisf, p, newValue);				
							
				if (thisf.propertyManagerGroup != null && p.getPublic())
					propertyManagerGroup.broadcast(thisf, p, newValue);
				
			}

			@Override
			public void propertyReadonlyChanges(Property p, boolean newReadOnly) {
				for (int i=0; i<thisf.pmcl.size(); i++)
					thisf.pmcl.get(i).propertyReadonlyChanges(thisf, p, newReadOnly);
			}

			@Override
			public void propertyVisibleChanges(Property p, boolean newVisible) {
				for (int i=0; i<thisf.pmcl.size(); i++)
					thisf.pmcl.get(i).propertyVisibleChanges(thisf, p, newVisible);
			}

			@Override
			public void propertyPublicChanges(Property p, boolean newPublic) {
				for (int i=0; i<thisf.pmcl.size(); i++)
					thisf.pmcl.get(i).propertyPublicChanges(thisf, p, newPublic);
			}

			@Override
			public void propertyDisabledChanges(Property p, boolean enabled) {
				for (int i=0; i<thisf.pmcl.size(); i++)
					thisf.pmcl.get(i).propertyDisabledChanges(thisf, p, enabled);
			}
			
		};
	}
	
	public void addChangeListener(PropertyManagerChangeListener pmcl)
	{
		if (this.pmcl.indexOf(pmcl) < 0)
			this.pmcl.add(pmcl);
	}
	
	public void removeChangeListener(PropertyManagerChangeListener pmcl)
	{
		int index = this.pmcl.indexOf(pmcl);
		if (index < 0)
			this.pmcl.remove(index);
	}
	
	public String getName()
	{
		return name;
	}	

	protected boolean addProperty(Property p, int where){
		
		int index = -1;
		for (int i=0; i<acceptedTypes.size(); i++)
			if (acceptedTypes.get(i).typeName().equals(p.getValue().typeName()))
			{
				index = i;
				break;
			}
		if (index < 0)
			return false;
		
		props.add(where,p);
		p.addPropertyChangeListener(pcl);
		
		for (int i=0; i<pmcl.size(); i++)
			pmcl.get(i).propertyAdded(this, p);
		
		if (blockedd)
			p.setDisabled(true);
		
		return true;
		
	}
	
	protected boolean addProperty(Property p){
		return addProperty(p, props.size());		
	}
	
	protected void removeProperty(String name)
	{
		for (int i=0; i<props.size(); i++)
			if (props.get(i).getName().equals(name))
			{
				for (int j=0; j<pmcl.size(); j++)
					pmcl.get(j).propertyRemoved(this, props.get(i));
				
				props.remove(i);
				break;
			}
	}
	

	
	public Property getProperty(String n) {
		for (int i=0; i<props.size(); i++)
			if (props.get(i).getName().equals(n))
				return props.get(i);
		return null;
	}
	

	<T extends PropertyType> void receivePropertyBroadcast(PropertyManager origin, String propName, T newvalue)
	{
		Property p = null;
		for (int i=0; i<this.props.size(); i++)
			if (props.get(i).getName().equals(propName))
			{
				p = props.get(i);
				break;
			}
		
		if (p == null || !p.getPublic()) return;
		
		if (newvalue.sources.indexOf(this) >= 0)
			return;
		
		T nvc = newvalue.copy();
		nvc.sources = newvalue.sources;
		p.receivedBroadcast(nvc, origin);
	}

	
	public void setAcceptedTypes(ArrayList<PropertyType> t)
	{
		acceptedTypes = t;
	}
	
	
	public void setPropertyManagerGroup(PropertyManagerGroup g)
	{
		propertyManagerGroup = g;		
	}
	
	public PropertyManagerGroup getPropertyManagerGroup()
	{
		return propertyManagerGroup;
	}
	
	
	public Property[] getProperties()
	{
		Property[] p = new Property[props.size()];
		for (int i=0; i<p.length; i++)
			p[i] = props.get(i);
		return p;
	}
	
	////////////tasks/////////////////
	private ArrayList<Property> tasks = new ArrayList<Property>();
	
	public void startTask(Task t)
	{		
		PProgress pp = new PProgress(0);
		pp.indeterminable = t.indeterminate;
		Property<PProgress> p = new Property<PProgress>(t.name, pp);	
		t.pprogress = p;
		
		if (t.blocking)
		{
			Property[] ps = getProperties();
			for (int i=0; i<ps.length; i++)
				ps[i].setDisabled(true);
		}
		
		tasks.add(p);
		
		try {
			this.addProperty(p,0);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		if (t.blocking)
			blockedd = true;
		
		t.start();
	}


	
}
