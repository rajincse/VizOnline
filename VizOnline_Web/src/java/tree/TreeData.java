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
			PFile f = new PFile();
			f.dialogTitle = "Open Graph File";
			f.extensions = new String[]{"txt","xml","*"};			
			Property<PFile> p1 = new Property<PFile>("Tree File",f);
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
			tree = new Tree(new File(((PFile)newvalue).path),format.options[format.selectedIndex]);
			
			this.setLoaded(true);
		}
		if (p.getName() == "Format")
		{
			String fs = ((POptions)newvalue).options[((POptions)newvalue).selectedIndex];
			
			if (fs.equals("GraphML"))
				((PFile)this.getProperty("Graph File").getValue()).currentExtension = 1;
			else if (fs.equals("Newick"))
				((PFile)this.getProperty("Graph File").getValue()).currentExtension = 0;	
		}
	}

	

	
	

}
