package perspectives.graph;

import java.awt.geom.Point2D;

public abstract class EdgeBundler {
	
	protected GraphViewer graphViewer;
	public EdgeBundler(GraphViewer g)
	{
		graphViewer = g;
	}
	
	public abstract void compute();
	
	public abstract Point2D.Double[] getEdgePolyline(int edgeIndex);
	
	//public abstract void getEdge(int edgeIndex, Point2D e1, Point2D e2);
	
}
