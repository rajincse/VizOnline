package perspectives.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import perspectives.base.Property;
import perspectives.properties.PInteger;
import perspectives.properties.PSignal;
import perspectives.base.PropertyType;
import perspectives.util.*;

public class BundledGraph extends GraphViewer {
	
	ForceDirectedEdgeBundler eb;

	public BundledGraph(String name, GraphData g) {
		super(name, g);
		// TODO Auto-generated constructor stub
		
		eb = null;
		
		final BundledGraph thisf = this;
		
		try {
		Property<PSignal> p1 = new Property<PSignal>("bundle",new PSignal())
				{
					@Override
					public boolean updating(PSignal newvalue)
					{
						if (eb == null) eb = new ForceDirectedEdgeBundler(thisf);
						eb.compute();
						return true;
					}
				};
		this.addProperty(p1);
		
		
		Property<PSignal> p2 = new Property<PSignal>("smooth",new PSignal())
				{
					@Override
					public boolean updating(PSignal newvalue)
					{
						if (eb == null) eb = new ForceDirectedEdgeBundler(thisf);
						eb.smoothEdges();
						return true;
					}
				};
		this.addProperty(p2);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	int e1 = -1;
	int e2 = -1;
	@Override
	public boolean mousepressed(int x, int y, int button) {
		
		if (eb != null && eb.segX != null)
		{
			for (int i=0; i<eb.segX.length; i++)
			{
				for (int j=0; j<eb.segX[i].length; j++)
				{
					double d = (x-eb.segX[i][j])*(x-eb.segX[i][j]) + (y-eb.segY[i][j])*(y-eb.segY[i][j]);
					if (d<8)
					{
						if (e1>=0 && e2>=0)
						{
							e1 = -1;
							e2 = -1;
						}
						if (e1<0)
							e1 = i;
						else if (e2<0)
							e2 = i;
						
						if (e1>=0 && e2>=0)
						{
							ArrayList<Integer> ee1 =new ArrayList<Integer>();
							ArrayList<Integer> ee2 =new ArrayList<Integer>();
							graph.getEdgesAsIndeces(ee1, ee2);
							double px1 = drawer.getX(ee1.get(e1));
							double px2 =drawer.getX(ee2.get(e1));
							double py1 = drawer.getY(ee1.get(e1));
							double py2 = drawer.getY(ee2.get(e1));
							double qx1 = drawer.getX(ee1.get(e2));
							double qx2 =drawer.getX(ee2.get(e2));
							double qy1 = drawer.getY(ee1.get(e2));
							double qy2 = drawer.getY(ee2.get(e2));
							
							System.out.println("compat1: " + eb.compat.getDistance(e1, e2));
							System.out.println("compat2: " + new EdgeCompatibility().getCompatibility(new Point2D.Double(px1,py1), new Point2D.Double(px2,py2), new Point2D.Double(qx1,qy1), new Point2D.Double(qx2,qy2), true));
							
						}
						
						break;
						
						
					}
				}
			}
		}
		super.mousepressed(x, y, button);
		
		return false;
	}
	
	

	@Override
	public void renderEdge(int p1, int p2, int edgeIndex, boolean selected,
			boolean hovered, Graphics2D g) {
		// TODO Auto-generated method stub
		int x1 = (int)drawer.getX(p1);
		int y1 =  (int)drawer.getY(p1);
		int x2 = (int)drawer.getX(p2);
		int y2 =  (int)drawer.getY(p2);
		
		if (selected) g.setColor(Color.red);
		else if (hovered) g.setColor(Color.pink);
		else g.setColor(new Color(100,100,100,120));
		
		g.setStroke(new BasicStroke(2));
		
		if (eb == null)
			g.drawLine(x1, y1, x2, y2);
		else
		{			
			Point2D.Double[] ep = eb.getEdgePolyline(edgeIndex);
			
			for (int i=0; ep!=null &&  i<ep.length-1; i++)
			{
				g.drawLine((int)ep[i].x, (int)ep[i].y, (int)ep[i+1].x, (int)ep[i+1].y);
			}
			
			for (int i=0; ep!=null &&  i<ep.length; i++)
			{
				//g.fillOval((int)ep[i].x-3, (int)ep[i].y-3,6,6);
			}
		}
	}

}
