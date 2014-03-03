package Graph;
import java.io.File;

import javax.swing.JButton;

import perspectives.DataSource;
import perspectives.PropertyManager;
import perspectives.Task;
import properties.PFileInput;
import properties.PInteger;
import properties.POptions;
import properties.Property;
import properties.PropertyType;


public class GraphData extends DataSource {
	
	public Graph graph;
	
	boolean valid;
		

	public GraphData(String name) {
		super(name);
		
		valid = false;
		
		graph= new Graph(false);
		
		try {
			
			PFileInput f = new PFileInput();
			f.dialogTitle = "Open Graph File";
			f.extensions = new String[]{"xml","txt","*"};

			
			Property<PFileInput> p1 = new Property<PFileInput>("Graph File",f);		
			addProperty(p1);
			
			POptions o = new POptions(new String[]{"GraphML","EdgeList"});
			o.selectedIndex = 1;
			Property<POptions> p2 = new Property<POptions>("Format", o);
			this.addProperty(p2);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}	
	
	public <T extends PropertyType> boolean propertyBroadcast(Property p, T newvalue, PropertyManager origin)
	{
		return false;
	}

	
	public <T extends PropertyType> void propertyUpdated(Property p, T newvalue)
	{
		if (p.getName() == "Graph File")
		{
			final GraphData th = this;
			final T newvalue_ = newvalue;
			Task t = new Task("Load file")
			{
				public void task()
				{
					POptions pf = (POptions)th.getProperty("Format").getValue();						
					String fs = pf.options[pf.selectedIndex];
								
					if (fs.equals("GraphML"))
						graph.fromGraphML(new File(((PFileInput) newvalue_).path));
					else if (fs.equals("EdgeList"))
						graph.fromEdgeList(new File(((PFileInput)newvalue_).path));				
					
					
					if (graph.numberOfNodes() != 0)
					{
						th.setLoaded(true);
		
						th.removeProperty("Graph File");
						th.removeProperty("Format");
						
						try {
							Property<PInteger> p1 = new Property<PInteger>("Info.# nodes",new PInteger(graph.numberOfNodes()));						
							p1.setReadOnly(true);
							th.addProperty(p1);
							
							Property<PInteger> p2 = new Property<PInteger>("Info.# edges",new PInteger(graph.numberOfEdges()));						
							p2.setReadOnly(true);
							th.addProperty(p2);					
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}		
		
					}
					
					done();
				}
				
				
			};
			t.indeterminate = true;
			t.blocking = true;
			
			this.startTask(t);
		}
		else if (p.getName() == "Format")
		{
			String fs = ((POptions)newvalue).options[((POptions)newvalue).selectedIndex];
			
			if (fs.equals("GraphML"))
				((PFileInput)this.getProperty("Graph File").getValue()).currentExtension = 0;
			else if (fs.equals("EdgeList"))
				((PFileInput)this.getProperty("Graph File").getValue()).currentExtension = 1;
		}
	}
	
	public boolean isValid()
	{
		return valid;
	}

	public void setGraph(Graph g) {
		this.graph = g;
		valid = true;
		
	}

}
