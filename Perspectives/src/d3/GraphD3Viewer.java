/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package d3;

import Graph.Graph;
import Graph.GraphData;
import java.awt.Color;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import properties.PColor;
import properties.Property;

/**
 *
 * @author rajin
 */
public class GraphD3Viewer extends D3Viewer {
    private final String PROPERTY_NODE_COLOR ="Node Color";
    private final String PROPERTY_LINK_COLOR ="Link Color";
    private Graph graph;
    public GraphD3Viewer(String name, GraphData graphData)            
    {
        super(name);
        this.graph = graphData.graph;
        try {

           Property<PColor> p1 = new Property<PColor>(PROPERTY_NODE_COLOR,new PColor(new Color(0, 255, 0)));
          
            this.addProperty(p1);
            
             Property<PColor> p2 = new Property<PColor>(PROPERTY_LINK_COLOR,new PColor(new Color(0, 0, 0)));
          
            this.addProperty(p2);

           

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Color getColor(String propertyName)
    {
        Property<PColor> p = this.getProperty(propertyName);
        return p.getValue().colorValue();
    }
    private String getHexString(Color color)
    {
        String hex = String.format("#%02x%02x%02x",color.getRed(), color.getGreen(), color.getBlue());
        return hex;
    }

    @Override
    public JSONObject updateData(boolean isInitialCall)
    {
        JSONObject data = new JSONObject();
        //InitialCall
        data.put("IsInitialCall", isInitialCall);
        // Property Data
        JSONObject propertyData= new JSONObject();
        
        String nodeColor = this.getHexString(this.getColor(PROPERTY_NODE_COLOR));
        propertyData.put("NodeColor", nodeColor);
        
        String edgeColor = this.getHexString(this.getColor(PROPERTY_LINK_COLOR));
        propertyData.put("LinkColor", edgeColor);
        
        data.put("PropertyData", propertyData);
        //Graph Data
        JSONObject graphJSON = new JSONObject();
        ArrayList<String> nodes = this.graph.getNodes();
        ArrayList<Integer> e1 = new ArrayList<Integer>();
        ArrayList<Integer> e2 = new ArrayList<Integer>();
        this.graph.getEdgesAsIndeces(e1, e2);
        
        JSONArray nodeArray = new JSONArray();
        for(String node: nodes)
        {
            JSONObject obj = new JSONObject();
            obj.put("name", node);
            nodeArray.add(obj);
        }
        graphJSON.put("nodes", nodeArray);
        
        JSONArray linkArray = new JSONArray();
        for(int i=0;i<e1.size();i++)
        {
            int source = e1.get(i).intValue();
            int destination = e2.get(i).intValue();
            JSONObject obj = new JSONObject();
            obj.put("source", source);
            obj.put("target", destination);
            linkArray.add(obj); 
        }
        graphJSON.put("links", linkArray);
        
        data.put("graph", graphJSON);
        return data;
    }
   
}
