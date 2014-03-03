package tree;

import java.io.File;

import perspectives.DataSource;
import properties.*;


public class TreeData extends DataSource{
	
	Tree tree;
	
	boolean valid;

	public TreeData(String name) {
		super(name);
		
		tree = null;
		valid = false;
		
		try {
			PFileInput f = new PFileInput();
			f.dialogTitle = "Open Graph File";
			f.extensions = new String[]{"txt","xml","*"};			
			Property<PFileInput> p1 = new Property<PFileInput>("Tree File",f);
			this.addProperty(p1);
			
			POptions o = new POptions(new String[]{"Newick", "GraphML"});			
			Property<POptions> p2 = new Property<POptions>("Format", o);
			p2.setValue(o);
			this.addProperty(p2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	@Override
	public <T extends PropertyType> void propertyUpdated(Property p, T newvalue) {
		if (p.getName() == "Tree File")
		{
			POptions format = (POptions)getProperty("Format").getValue();
			tree = new Tree(new File(((PFileInput)newvalue).path),format.options[format.selectedIndex]);
			
			this.setLoaded(true);
		}
		if (p.getName() == "Format")
		{
			String fs = ((POptions)newvalue).options[((POptions)newvalue).selectedIndex];
			
			if (fs.equals("GraphML"))
				((PFileInput)this.getProperty("Graph File").getValue()).currentExtension = 1;
			else if (fs.equals("Newick"))
				((PFileInput)this.getProperty("Graph File").getValue()).currentExtension = 0;	
		}
	}

	

	
	

}
