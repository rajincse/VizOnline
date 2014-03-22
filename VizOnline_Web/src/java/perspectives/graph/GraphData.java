package perspectives.graph;
import java.io.File;

import javax.swing.JButton;

import perspectives.base.DataSource;
import perspectives.base.Property;
import perspectives.base.PropertyManager;
import perspectives.base.Task;
import perspectives.properties.PFileInput;
import perspectives.properties.PInteger;
import perspectives.properties.POptions;
import perspectives.base.PropertyType;


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
			f.currentExtension = 1;

			final GraphData thisf = this;
			Property<PFileInput> p1 = new Property<PFileInput>("Graph File",f)
					{
						@Override
						protected boolean updating(PFileInput newvalue) {
						
							final GraphData th = thisf;
							final PFileInput newvalue_ = newvalue;
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
							
							thisf.startTask(t);	
							
							return true;
							
						}
				
					};
					
					
			addProperty(p1);
			
			
			
			POptions o = new POptions(new String[]{"GraphML","EdgeList"});
			o.selectedIndex = 1;
			Property<POptions> p2 = new Property<POptions>("Format", o)
					{

						@Override
						protected boolean updating(POptions newvalue) {
							String fs = ((POptions)newvalue).options[((POptions)newvalue).selectedIndex];
							
							if (fs.equals("GraphML"))
								((PFileInput)thisf.getProperty("Graph File").getValue()).currentExtension = 0;
							else if (fs.equals("EdgeList"))
								((PFileInput)thisf.getProperty("Graph File").getValue()).currentExtension = 1;
							
							return true;
						}
				
					};
			this.addProperty(p2);
			
			
		} catch (Exception e) {
			e.printStackTrace();
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
