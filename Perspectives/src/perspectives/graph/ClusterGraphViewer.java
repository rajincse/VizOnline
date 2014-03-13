package perspectives.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;


import perspectives.base.Property;
import perspectives.properties.PFileInput;
import perspectives.base.PropertyType;
import perspectives.util.BubbleSets;
import perspectives.util.Util;


public class ClusterGraphViewer extends GraphViewer {
	
	
	int[] clusters;	
	ArrayList<String> clusterTypes;
	
	Color[] clusterColor;

	public ClusterGraphViewer(String name, GraphData g) {
		super(name, g);
		
	try {	
	
	Property<PFileInput> p1 = new Property<PFileInput>("Load Clusters", new PFileInput())
			{
				@Override
				public boolean updating(PFileInput newvalue)
				{
					loadClusters(newvalue.path);
					return true;
				}
			};
	this.addProperty(p1);	
	
	Property<PFileInput> p2 = new Property<PFileInput>("Load Cluster Colors", new PFileInput())
			{
				@Override
				public boolean updating(PFileInput newvalue)
				{
					loadClusterColors(newvalue.path);
					return true;
				}
			};
	this.addProperty(p2);
	}
	catch (Exception e) {		
		e.printStackTrace();
	}
	}


	@Override
	public void renderNode(int i, boolean selected, boolean hovered,
			Graphics2D g) {	
		if (selected)
			ovals.get(i).setColor(Color.red);			
		else if (hovered)
			ovals.get(i).setColor(Color.pink);	
		else
		{
			if (clusters != null)
				ovals.get(i).setColor(clusterColor[this.clusters[i]]);		
		}
		ovals.get(i).render(g);
	}
	
	public void loadClusters(String path)
	{
		ArrayList<String> nodes = graph.getNodes();
		
		clusters = new int[nodes.size()];
		
		clusterTypes = new ArrayList<String>();
		
		try{
		 FileInputStream fstream = new FileInputStream(path);
		 DataInputStream in = new DataInputStream(fstream);
		 BufferedReader br = new BufferedReader(new InputStreamReader(in));
		 String s;
		 while ((s = br.readLine()) != null)
		 {
			s = s.trim();
			
			String[] split = s.split("\t");
			
			if (split.length < 2) continue;
			
			int index = nodes.indexOf(split[0].trim());
			if (index < 0) continue;
			
			String c = split[1].trim();
			if (clusterTypes.indexOf(c) < 0)
				clusterTypes.add(c);
			
			clusters[index] = clusterTypes.indexOf(c);
		 }
		 in.close();
		 
		 clusterColor = new Color[clusterTypes.size()];
		 
		 Random r = new Random();
		 for (int i=0; i<clusterTypes.size(); i++)
		 {
			 Color c = new Color(r.nextFloat(), r.nextFloat(), r.nextFloat());
			 clusterColor[i] = c;
		 }
		}
		catch(Exception e){}
	}
	
	public void loadClusterColors(String path)
	{
		if (clusters != null)
		{
			ArrayList<String> nodes = graph.getNodes();
							
			try{
			 FileInputStream fstream = new FileInputStream(path);
			 DataInputStream in = new DataInputStream(fstream);
			 BufferedReader br = new BufferedReader(new InputStreamReader(in));
			 String s;
			 while ((s = br.readLine()) != null)
			 {
				s = s.trim();
				
				String[] split = s.split("\t");
				
				int index = clusterTypes.indexOf(split[0]);
				
				if (index < 0) continue;
				
				String[] cs = split[1].split(",");					
				
				 Color c = new Color(Integer.parseInt(cs[0]), Integer.parseInt(cs[1]), Integer.parseInt(cs[2]));
				 
				 clusterColor[index] = c;
				 
			 }
			 in.close();
			}catch(Exception e){}	
		}
	}
	
	
	
}
