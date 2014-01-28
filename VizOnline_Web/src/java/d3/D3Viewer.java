package d3;
import Graph.Graph;
import Graph.GraphData;
import java.awt.Graphics2D;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import perspectives.Viewer2D;
import properties.PDouble;
import properties.PFile;
import properties.Property;


public class D3Viewer extends Viewer2D{
    private Graph graph;
    public D3Viewer(String name, GraphData graphData)            
    {
        super(name);
        this.graph = graphData.graph;
        try {

            Property<PFile> p33 = new Property<PFile>("Load Positions", new PFile());
            this.addProperty(p33);

            Property<PDouble> p = new Property<PDouble>("Simulation.SPRING_LENGTH", new PDouble(300.));
            //((BarnesHutGraphDrawer) drawer).setSpringLength(300.);
            this.addProperty(p);

           

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject updateData()
    {
        JSONObject graph = new JSONObject();
        ArrayList<String> nodes = this.graph.getNodes();
        ArrayList<Integer> e1 = new ArrayList<Integer>();
        ArrayList<Integer> e2 = new ArrayList<Integer>();
        this.graph.getEdgesAsIndeces(e1, e2);
        
        JSONArray nodeArray = new JSONArray();
        for(String node: nodes)
        {
            JSONObject obj = new JSONObject();
            obj.put("name", node);
            nodeArray.put(obj);
        }
        graph.put("nodes", nodeArray);
        
        JSONArray linkArray = new JSONArray();
        for(int i=0;i<e1.size();i++)
        {
            int source = e1.get(i).intValue();
            int destination = e2.get(i).intValue();
            JSONObject obj = new JSONObject();
            obj.put("source", source);
            obj.put("target", destination);
            linkArray.put(obj); 
        }
        graph.put("links", linkArray);
        return graph;
    }
    @Override
    public void render(Graphics2D g) {
        
        
    }

    @Override
    public void simulate() {
        
    }

	
}
