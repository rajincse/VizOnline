package perspectives.properties;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import perspectives.base.Property;
import perspectives.base.PropertyWidget;
import perspectives.base.PropertyType;

public class PFileInputWidget extends PropertyWidget {
	
	public PFileInputWidget(Property p) {
		super(p);	
	}

	JButton control = null;
	FileFilter[] fileFilters;
	
	public void widgetLayout()
	{
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		final PropertyWidget th = this;
		
		control = new JButton(this.p.getDisplayName(), new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/Open16.gif")));			
		control.setPreferredSize(new Dimension(130,20));
		ActionListener listener = new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  
		    	  JFileChooser fc = new JFileChooser();
		    	  
		    	  final PFileInput prop = (PFileInput)th.getProperty().getValue();
		    	  
		    	  if (prop.dialogTitle.length() > 0) fc.setDialogTitle(prop.dialogTitle);
		    	  
		    	  if (prop.extensions.length > 0)
		    	  {
		    		  fc.setAcceptAllFileFilterUsed(false);
		    		  
		    		  fileFilters = new FileFilter[prop.extensions.length];
		    		  
		    		  for (int i=0; i<prop.extensions.length; i++)
		    		  {
		    			  final int ii = i;
		    			  FileFilter ff = new FileFilter(){			    				  
		    				  @Override
		    				  public boolean accept(File f) {
		    					  if (f.isDirectory() || prop.extensions[ii].equals("*"))
		    						  return true;
		    					 

		    					String extension = getExtension(f);
							    if (extension != null && extension.equals(prop.extensions[ii]))
							    	return true;
							    else
							    	return false;
		    				  }
		    			  			

		    				  @Override
		    				  public String getDescription() {
		    					  if (prop.extensions[ii].equals("*"))
		    						  return "All Files";
		    					  return prop.extensions[ii]; 
								}			    			  
		    			  };
		    			  
		    			  fc.addChoosableFileFilter(ff);
		    			  
		    			  fileFilters[i] = ff;
		    		  }
		    		  
		    		  if (prop.currentExtension >=0 && prop.currentExtension<fileFilters.length)
		    			  fc.setFileFilter(fileFilters[prop.currentExtension]);
		    		
		    	  }
		    	  
		    	  if (prop.onlyDirectories)
		    		  fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    	  else if (prop.filesAndDirectories)
		    		  fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		    	  			    	  
		    	  int returnVal;
		    	  
		    	  returnVal = fc.showOpenDialog(th);
		    	  
		    	  if (returnVal == JFileChooser.APPROVE_OPTION) {
		    		  PFileInput v = prop.copy();
		    		  v.path = fc.getSelectedFile().getAbsolutePath();			        
		              th.updateProperty(v);	
		    	  }
		      }
		    };
		control.addActionListener(listener);

		this.add(Box.createHorizontalGlue());
		this.add(control);
		this.add(Box.createHorizontalGlue());
		
		p.setReadOnly(p.getReadOnly());
		p.setVisible(p.getVisible());
	}
	
	 private String getExtension(File f) {
	        String ext = null;
	        String s = f.getName();
	        int i = s.lastIndexOf('.');
	 
	        if (i > 0 &&  i < s.length() - 1) {
	            ext = s.substring(i+1).toLowerCase();
	        }
	        return ext;
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
