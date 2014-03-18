package perspectives.tree;

import java.io.File;

import perspectives.base.DataSource;
import perspectives.base.Property;
import perspectives.base.PropertyType;
import perspectives.properties.PFileInput;
import perspectives.properties.POptions;


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
			Property<PFileInput> p1 = new Property<PFileInput>("Tree File",f)
			{

				@Override
				protected boolean updating(PFileInput newvalue) {
					POptions format = (POptions)getProperty("Format").getValue();
					tree = new Tree(new File(((PFileInput)newvalue).path),format.options[format.selectedIndex]);					
					setLoaded(true);
					return true;
				}
				
			};
			this.addProperty(p1);
			
			POptions o = new POptions(new String[]{"Newick", "GraphML"});			
			Property<POptions> p2 = new Property<POptions>("Format", o)
					{
						@Override
						protected boolean updating(POptions newvalue) {
							String fs = newvalue.options[newvalue.selectedIndex];
							
							if (fs.equals("GraphML"))
								((PFileInput)getProperty("Graph File").getValue()).currentExtension = 1;
							else if (fs.equals("Newick"))
								((PFileInput)getProperty("Graph File").getValue()).currentExtension = 0;
							
							return true;
						}
					};
			p2.setValue(o);
			this.addProperty(p2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

}
