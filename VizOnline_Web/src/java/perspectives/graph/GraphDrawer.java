package perspectives.graph;
import java.util.ArrayList;
import java.util.HashMap;

import perspectives.two_d.Vector2D;
import perspectives.base.Property;
import perspectives.properties.PInteger;


public abstract class GraphDrawer {
	
	protected Graph graph;
	
	protected Vector2D[] xy;
	ArrayList<String> nodes = null; //saved nodes in case the graph is modified
	
	long graphLastUpdated;
	
	
	public GraphDrawer(Graph g)
	{
		this.graph = g;
		graphLastUpdated = g.lastUpdate();
		
		ArrayList<String> nodes = new ArrayList<String>(graph.getNodes());
		
		xy = new Vector2D[nodes.size()];	
		
		for (int i=0; i<nodes.size(); i++)
		{
			xy[i] = new Vector2D();
			
			Property<PInteger> p = graph.nodeProperty(nodes.get(i), "x");
			if (p != null)
				xy[i].x = p.getValue().intValue();
			else
				xy[i].x = 0; 
			
			p = graph.nodeProperty(nodes.get(i), "y");
			if (p != null)
				xy[i].y = p.getValue().intValue();
			else
				xy[i].y = 0;
		}
	}
	
	public abstract void iteration();	
	
	public double getX(int index)
	{
		if (index < 0 || index >= xy.length)
		{
			System.out.println("error in GraphDrawer, getY; no such id in graph");
			System.exit(0);
		}
		
		long u = graph.lastUpdate();
		if (graphLastUpdated != u )
			updateGraphStructure(u);
		
		return xy[index].x;
	}
	
	public double getY(int index)
	{
		if (index < 0 || index >= xy.length)
		{
			System.out.println("error in GraphDrawer, getY; no such id in graph");
			System.exit(0);
		}		
		
		long u = graph.lastUpdate();
		if (graphLastUpdated != u )
			updateGraphStructure(u);
		
		return xy[index].y;
	}
	
	
	public void setX(int index, double x)
	{
		if (index < 0 || index >= xy.length)
		{
			System.out.println("error in GraphDrawer, setX; no such id in graph");
			System.exit(0);
		}
		
		long u = graph.lastUpdate();
		if (graphLastUpdated != u )
			updateGraphStructure(u);
		
		xy[index].x = x;
		
	}
	
	public void setY(int index, double y)
	{
		if (index < 0 || index >= xy.length)
		{
			System.out.println("error in GraphDrawer, setY; no such id in graph");
			System.exit(0);
		}
		
		long u = graph.lastUpdate();
		if (graphLastUpdated != u )
			updateGraphStructure(u);
		
		xy[index].y = y;
	}
	
	private void updateGraphStructure(long u)
	{
		graphLastUpdated = u;
		
		ArrayList<String> newNodes = graph.getNodes();
		Vector2D[] newxy = new Vector2D[newNodes.size()];		
		
		for (int i=0; i<newxy.length; i++)
		{
			int index = nodes.indexOf(newNodes.get(i));
			if (index >=0 && index < nodes.size())
				newxy[i] = xy[index];
			else
				newxy[i] = new Vector2D();
		}
		
		xy = newxy;
		nodes = newNodes;
	}
}
