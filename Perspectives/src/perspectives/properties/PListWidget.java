package perspectives.properties;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import perspectives.base.Property;
import perspectives.base.PropertyWidget;
import perspectives.base.PropertyType;

public class PListWidget extends PropertyWidget {
	
	public PListWidget(Property p) {
		super(p);	
	}

	JList<String> control = null;
	
	public void widgetLayout()
	{
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		JPanel subpanel = new JPanel();
		subpanel.setBorder(null);
		//subpanel.setLayout(new BoxLayout(subpanel, BoxLayout.Y_AXIS));
		subpanel.setLayout(new BorderLayout());
		
		this.add(subpanel);
		
		subpanel.add(new JLabel(this.p.getDisplayName()),BorderLayout.NORTH);
		
		subpanel.setOpaque(false);
		
		final PropertyWidget th = this;
		
					
		control = new JList<String>(((PList)this.p.getValue()).items);
		
		JScrollPane listScroller = new JScrollPane(control);
		listScroller.setPreferredSize(new Dimension(180, 80));			
		//listScroller.setMaximumSize(new Dimension(180,500));
		
		ListSelectionListener listener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == false)
				{
					PList l = ((PList)th.getProperty().getValue()).copy();
					l.selectedIndeces = control.getSelectedIndices();
			    	th.updateProperty(l);		   
				}					
			}
		    };
		    
		   KeyListener keyListener = new KeyListener()
		    {
			   String full = "";
			   long lastType = -1;

				@Override
				public void keyPressed(KeyEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void keyReleased(KeyEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void keyTyped(KeyEvent e) {
					Date d = new Date();
						if (d.getTime() - lastType > 500)
							full = "";	
						
						full = full + e.getKeyChar();
						
						for (int i=0; i < control.getModel().getSize(); i++) {
						    String str = ((String)control.getModel().getElementAt(i)).toLowerCase();
						    if (str.startsWith(full)) {
						        control.setSelectedIndex(i); 
						        control.ensureIndexIsVisible(i); 
						        break;
						    }
						}
						
						lastType = d.getTime();
						
				}
		    	
		    };
		   
				    
		 
		control.addListSelectionListener(listener);
		control.addKeyListener(keyListener);
	
		subpanel.add(listScroller,BorderLayout.CENTER);
		subpanel.invalidate();
		
		this.add(Box.createHorizontalGlue());

		p.setReadOnly(p.getReadOnly());
		p.setVisible(p.getVisible());
	}


	@Override
	public <T extends PropertyType> void propertyValueUpdated(T newvalue) {
		control.setListData(((PList)newvalue).items);
		control.setSelectedIndices(((PList)newvalue).selectedIndeces);			
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