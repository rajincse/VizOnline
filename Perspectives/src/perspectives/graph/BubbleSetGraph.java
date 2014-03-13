package perspectives.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import perspectives.base.Property;
import perspectives.properties.PBoolean;
import perspectives.properties.PDouble;
import perspectives.properties.PInteger;
import perspectives.properties.PString;
import perspectives.base.PropertyType;
import perspectives.util.BubbleSets;
import perspectives.util.Util;

public class BubbleSetGraph extends ClusterGraphViewer {
	
	Object bigGridCompute = new Object();
	BubbleSets bubbleSets = null;	
	BufferedImage bigGrid;
	int bigGridSX, bigGridSY;
	
	Path2D[] bubbles = null;

	public BubbleSetGraph(String name, GraphData g) {
		super(name, g);
		try {			
			
			Property<PInteger> p44 = new Property<PInteger>("Cluster Bubbles", new PInteger(0));			
			this.addProperty(p44);	
			
			Property<PInteger> p4 = new Property<PInteger>("Bubble",  new PInteger(0));		
			this.addProperty(p4);	
			
			Property<PInteger> p5 = new Property<PInteger>("BubbleCellSize",  new PInteger(10));			
			this.addProperty(p5);	
			
			Property<PInteger> p6 = new Property<PInteger>("BubbleR1",  new PInteger(0));			
			this.addProperty(p6);	
			
			Property<PDouble> p8 = new Property<PDouble>("BubbleThresh",  new PDouble(0.5));		
			this.addProperty(p8);
			
			Property<PString> p9 = new Property<PString>("Smaller", new PString(""));
			this.addProperty(p9);	
			}
			catch (Exception e) {		
				e.printStackTrace();
			}
	}
	
	public <T extends PropertyType> void propertyUpdated(Property p, T newvalue)
	{			
		if (p.getName() == "Cluster Bubbles")
			this.createBubbles();
		else if (p.getName() == "Bubble")
		{
			this.createBubbles();
		}
		else if (p.getName() == "BubbleCellSize")
		{
			this.createBubbles();
		}
		else if (p.getName() == "BubbleR1")
		{
			this.createBubbles();
		}
	}

	
	@Override
	public void simulate() {
		// TODO Auto-generated method stub
		super.simulate();
		if (((PBoolean)getProperty("Simulation.Simulate").getValue()).boolValue())
			createBubbles();
			
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub	
	
		if (bubbles != null)
		{
			
			for (int i=0; i<bubbles.length; i++)
			{
				g.setColor(clusterColor[i]);
				g.fill(bubbles[i]);
			}
			g.setColor(Color.DARK_GRAY);
			for (int i=0; i<bubbles.length; i++)
				g.draw(bubbles[i]);
			
		}
		  
	
		 super.render(g);
	}
	
	
	public void createBubbles()
	{
		if (clusters == null)
			return;
		
		
		
		synchronized(this.bigGridCompute)
		{			
			ArrayList<String> nodes = graph.getNodes();	
			
			Rectangle2D.Double[] points = new Rectangle2D.Double[nodes.size()];
			
		int[][] sets = new int[clusterTypes.size()][];
		for (int j=0; j<clusterTypes.size(); j++)
		{
			int ct = 0;
			for (int i=0; i<nodes.size(); i++)
				if (j == clusters[i])
					ct++;
				
			
			int[] s1 = new int[ct];
			
			ct = 0;
			for (int i=0; i<nodes.size(); i++)
			{			
				
				if (j == clusters[i])
				{
					points[i] =  new Rectangle2D.Double(this.ovals.get(i).x, this.ovals.get(i).y, this.ovals.get(i).w, this.ovals.get(i).h);
					s1[ct] = i;
					ct++;
				}
				
			}				
			
			sets[j] = s1;
		}
				
			
			BubbleSets bs = new BubbleSets(points,sets);
			
			
			int r1 = ((PInteger)this.getProperty("BubbleR1").getValue()).intValue();
			int r0 = 10;
			int cellSize = ((PInteger)this.getProperty("BubbleCellSize").getValue()).intValue();			
			double contThresh = ((PDouble)this.getProperty("BubbleThresh").getValue()).doubleValue();						

			bubbles  = bs.computeAll(cellSize, r1, r0, contThresh);
		
		
		
	}
	}

}
