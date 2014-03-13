package perspectives.base;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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

import perspectives.base.PropertyType;
import perspectives.base.PropertyWidgetFactory;


public class PropertyManagerViewer extends JPanel
{
	private PropertyManager pm;
	
	private Font font;
	
	Vector<JPanel> groupPanels = new Vector<JPanel>();
	ArrayList<PropertyWidget> widgets;	
	
	Vector<Boolean> groupStates = new Vector<Boolean>();
	Vector<String> groupNames = new Vector<String>();	
	
	ArrayList<Property> props;
	
	private static Hashtable<String,PropertyWidgetFactory> widgetFactories = new Hashtable<String, PropertyWidgetFactory>();
	public static void registerNewType(PropertyType c, PropertyWidgetFactory pwf) {	
		if (!widgetFactories.containsKey(c.typeName()))
			widgetFactories.put(c.typeName(),pwf);		
	}

	
	public PropertyManagerViewer(PropertyManager pm)
	{		
		props = new ArrayList<Property>();		
		
		this.pm = pm;
		
		widgets = new ArrayList<PropertyWidget>();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel wrapper = new JPanel();
		wrapper.setBorder(null);
		wrapper.setLayout(new BoxLayout(wrapper,BoxLayout.X_AXIS));
		wrapper.add(Box.createRigidArea(new Dimension(180,1)));		
		
		this.add(wrapper);		
	
		setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Properties"),
                BorderFactory.createEmptyBorder(5,5,5,5)));
		
		for (int i = 0; i<pm.props.size(); i++)
		{
			addProperty(pm.props.get(i));
			if (pm.props.get(i).getDisabled())
				pm.props.get(i).setDisabled(true);
		}
		
		
		/*for (int i = 0; i<pm.props.size(); i++)
		{
			if (pm.props.get(i).getDisabled())
				pm.props.get(i).setDisabled(true);
		}*/
		
		final PropertyManagerViewer thisf = this;
		PropertyManagerChangeListener pmcl = new PropertyManagerChangeListener()
		{
			

			@Override
			public void propertyValueChanges(PropertyManager pm, Property p,
					PropertyType newValue) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void propertyReadonlyChanges(PropertyManager pm, Property p,
					boolean newReadOnly) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void propertyVisibleChanges(PropertyManager pm, Property p,
					boolean newVisible) {
				thisf.processLastDivider();
				thisf.revalidate();				
			}

			@Override
			public void propertyPublicChanges(PropertyManager pm, Property p,
					boolean newPublic) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void propertyDisabledChanges(PropertyManager pm, Property p,
					boolean enabled) {
				// TODO Auto-generated method stub
				
			}


			@Override
			public void propertyAdded(PropertyManager pm, Property p) {
				thisf.addProperty(p);
			}


			@Override
			public void propertyRemoved(PropertyManager pm, Property p) {
				groupPanels.add(0,thisf);
				for (int i=0; i<props.size(); i++)
					if (props.get(i) == p)
					{
						PropertyWidget pw = widgets.get(i);				
						widgets.remove(i);		
						props.remove(i);
						
						for (int j=0; j<groupPanels.size(); j++)
						{
							if (pw.getParent().getParent() == groupPanels.get(j)) //double parent to go through the wrapper
							{
								groupPanels.get(j).remove(pw.getParent());						
								
								if (groupPanels.get(j).getComponentCount() == 0 && groupPanels.get(j) != thisf)
									groupPanels.get(j).getParent().getParent().remove(groupPanels.get(j).getParent());
							}
						}
						
						break;
					}
				
				groupPanels.remove(0);	
			

				processLastDivider();
				
				thisf.revalidate();
				
			}
			
		};
		
		this.pm.addChangeListener(pmcl);
	}
	
	public PropertyManager getPropertyManager()
	{
		return pm;
	}
	
	
	public void setFont(Font f)
	{
		this.font = f;
		setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(null,"Properties",TitledBorder.LEFT, TitledBorder.TOP,font),
                BorderFactory.createEmptyBorder(5,5,5,5)));
		
		setRecursiveFont(this,f);
	}
	private void setRecursiveFont(JComponent c, Font f)
	{
		if (c != this)
			c.setFont(f);
		for (int i=0; i<c.getComponentCount(); i++)
			setRecursiveFont((JComponent)c.getComponent(i),f);
	}
	
	PropertyWidget getPropertyWidget(Property p)
	{
		for (int i=0; i<props.size(); i++)
			if (props.get(i) == p)
				return widgets.get(i);
		return null;
	}
	
	void setPropertyWidget(Property p, PropertyWidget pw)
	{
		int index = props.indexOf(p);
		if (index < 0)
			return;
		
		widgets.remove(index);
		widgets.add(index, pw);
	}

	
	void processLastDivider()
	{

		
		groupPanels.add(0,this);
		
		for (int i=0; i<groupPanels.size(); i++)
		{
			//each wrapper has to contain a divider except the last VISIBLE one
			int lastVisible = groupPanels.get(i).getComponentCount()-1;
			while (!groupPanels.get(i).getComponent(lastVisible).isVisible())
				lastVisible--;
			
			for (int j=0; j<groupPanels.get(i).getComponentCount(); j++)
			{
				if (j==0)
					continue;
				
				JPanel w = (JPanel)groupPanels.get(i).getComponent(j);
				
				if (w.getComponentCount() == 1 && j != lastVisible)
				{
					w.add(Box.createRigidArea(new Dimension(1,3)));				
				}
				else if (w.getComponentCount() > 1 && j == lastVisible)
				{
					while (w.getComponentCount() > 1)
						w.remove(w.getComponentCount()-1);
				}
			}
		}		
		groupPanels.remove(0);
	}
	
	public void addProperty(Property p)
	{
		PropertyWidget pw = widgetFactories.get(p.getValue().typeName()).createWidget(p);
		
		int preferredWidth = 180;	
	
		
		String[] split = p.getName().split("\\.");
		JPanel parent = this;
		String s = "";
		
		final JPanel thisPM = this;
		
		for (int i=0; i<split.length-1; i++)
		{
			s = s + split[i];
			preferredWidth -= 10;
			
			int index = groupNames.indexOf(s);
			if (index >= 0) 
				parent = groupPanels.get(index);
			else
			{
				final JPanel parent2 = new JPanel();
				groupStates.add(new Boolean(true));
				
				final String title = split[i]; 
								
				parent2.setBorder(BorderFactory.createTitledBorder(title + "    -"));
				parent2.setLayout(new BoxLayout(parent2, BoxLayout.Y_AXIS));	
				
				JPanel wrapper = new JPanel();
				wrapper.setBorder(null);
				wrapper.setLayout(new BoxLayout(wrapper,BoxLayout.X_AXIS));
				wrapper.add(Box.createRigidArea(new Dimension(preferredWidth,1)));
				parent2.add(wrapper);
				
				wrapper = new JPanel();
				wrapper.setBorder(null);
				wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
		
				wrapper.add(parent2);
			
				//find the right position
				int pos = pm.props.indexOf(p);
				//props.add(pos,p);
				//widgets.add(pos,pw);
				for (pos=pos-1; pos>=0; pos--)
				{
					if (widgets.get(pos).getParent().getParent() == parent)
						break;
				}
				if (pos < 0) pos = 0;
				else
				{
					Component[] c = parent.getComponents();
					for (int j=0; j<c.length; j++)
						if (c[j] == widgets.get(pos).getParent())
							pos = j;
					pos++;
				}		
				
				parent.add(wrapper, pos);
				
				
				parent2.addMouseListener(new MouseListener()
				{
					public void mouseClicked(MouseEvent e) {
						if (e.getY() < 20)
						{
							int index = groupPanels.indexOf(parent2);							
							Boolean visible = groupStates.get(index);
							
							if (visible.booleanValue())
							{
								groupStates.set(index,new Boolean(false));
								parent2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),title + "    +"));
								//parent2.setBorder(BorderFactory.createTitledBorder(title + "    +"));
								
								int maxx = 0;
								for (int i=0; i<parent2.getComponentCount(); i++)
								{
									int sx = parent2.getComponent(i).getWidth();
									if (sx > maxx) maxx = sx;
								}
								
							//	System.out.println(maxx);
								
							
								parent2.add(Box.createRigidArea(new Dimension(maxx,0)),0);
								
								for (int i=1; i<parent2.getComponentCount(); i++)
									parent2.getComponent(i).setVisible(false);
								
								//for (int i=0; i<thisPM.getComponentCount(); i++)
								//	System.out.println(thisPM.getComponent(i));
								
								parent2.revalidate();
							}
							else
							{
								groupStates.set(index,new Boolean(true));
								
								parent2.remove(0);
								
								parent2.setBorder(BorderFactory.createTitledBorder(title + "    -"));
								
								parent2.setMinimumSize(new Dimension(10,10));
								
								for (int i=0; i<parent2.getComponentCount(); i++)
									parent2.getComponent(i).setVisible(true);
								
								parent2.revalidate();
							
							}
							
							thisPM.revalidate();
						}
					}

					@Override
					public void mouseEntered(MouseEvent arg0) {	}
					@Override
					public void mouseExited(MouseEvent arg0) {}
					@Override
					public void mousePressed(MouseEvent arg0) {}
					@Override
					public void mouseReleased(MouseEvent arg0) {}
					
				});
				
				groupNames.add(s);
				groupPanels.add(parent2);
				
				parent = parent2;
			}			
			s = s + ".";			
		}
		
		JPanel wrapper = new JPanel();
		wrapper.setBorder(null);
		wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));	
		
		wrapper.add(pw);
		
		//parent.add(wrapper, parent.getComponentCount());
		//find the right position		
		int pos = pm.props.indexOf(p);
		props.add(pos,p);
		widgets.add(pos,pw);	
		Component child = null;
		for (pos = pos-1; pos>=0; pos--)
		{			
			Component sameParent = widgets.get(pos).getParent();
			child = widgets.get(pos);
			while (sameParent != null && sameParent != parent)
			{
				child = sameParent;
				sameParent = sameParent.getParent();
			}
			if (sameParent != null)
				break;
			
				
		}
		if (pos < 0) pos = 0;
		else
		{
			Component[] c = parent.getComponents();
			for (int i=0; i<c.length; i++)
				if (c[i] == child)
					pos = i;
			pos++;
		}				
		
		parent.add(wrapper, pos);				
	
		pw.widgetLayout();
		
				
		
		pw.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, new Color(200,200,230), new Color(230,230,240)),BorderFactory.createEmptyBorder(2,2,2,2)));
		pw.setBackground(new Color(220,220,230));		
		
		processLastDivider();
		
		this.revalidate();
		
	}

	
	public void setWidgetFactories(Hashtable f)
	{
		widgetFactories = f;
	}
}
