package Graph;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;

import perspectives.PropertyManager;
import perspectives.Task;
import perspectives.Viewer2D;
import properties.PBoolean;
import properties.PDouble;
import properties.PFile;
import properties.PInteger;
import properties.PString;
import properties.Property;
import properties.PropertyType;
import util.Animation;
import util.BubbleSets;
import util.Label;
import util.Oval;
import util.Rectangle;
import util.Util;

import util.ObjectInteraction;


public class GraphViewer extends Viewer2D{

	protected Graph graph;
	
	GraphDrawer drawer;
	
	int[] edgeSources;
	int [] edgeTargets;

	ObjectInteraction ovalInteraction;
	ArrayList<Rectangle> ovals;
	
	
	Task initTask;
	

	public GraphViewer(String name, GraphData g) {
		super(name);		
		
		graph = g.graph;
		
		
		
		
		final GraphViewer gv = this;
		
		initTask = new Task("Initializing"){			
	
			
		public void task()
		{
			ovals = new ArrayList<Rectangle>();
		
			
			ovalInteraction = new ObjectInteraction()
			{
				@Override
				protected void mouseIn(int obj)
				{
					gv.setToolTipText(graph.getNodes().get(obj));
					final int o = obj;
					gv.createAnimation(new Animation.IntegerAnimation(22,30,300)
					{
						public void step(int v)
						{
							Rectangle l= ((RectangleItem)ovalInteraction.getItem(o)).r;
							l.w = v;
							l.h = v;	
							gv.requestRender();
						}
					});
				}
				
				protected void mouseOut(int obj)
				{
					gv.setToolTipText("");
					final Rectangle l= ((RectangleItem)ovalInteraction.getItem(obj)).r;
					gv.createAnimation(new Animation.IntegerAnimation(30,22,300)
					{
						public void step(int v)
						{						
							l.w = v;
							l.h = v;	
							gv.requestRender();
						}
					});
				}
				
				protected void itemDragged(int item, Point2D delta)
				{
					gv.drawer.setX(item, gv.drawer.getX(item) + (int)delta.getX());
					gv.drawer.setY(item, gv.drawer.getY(item) + (int)delta.getY());
					gv.requestRender();
				}
				
				@Override
				protected void itemsSelected(int[] obj)
				{
					gv.requestRender();
				}
				
				@Override
				protected void itemsDeselected(int[] obj)
				{
					gv.requestRender();
				}
	
			};
			
		
			ArrayList<String> nodes = graph.getNodes();
			for (int i=0; i<nodes.size(); i++)
			{
				Oval o = new Oval(0,0,22,22);
				ovals.add(o);
				ovalInteraction.addItem(ovalInteraction.new RectangleItem(o));
			}
			
			ArrayList<Integer> e1 = new ArrayList<Integer>();
			ArrayList<Integer> e2 = new ArrayList<Integer>();
			graph.getEdgesAsIndeces(e1, e2);
			
			edgeSources = new int[e1.size()];
			edgeTargets = new int[e2.size()];
			for (int i=0; i<e1.size(); i++)
			{
				edgeSources[i] = e1.get(i);
				edgeTargets[i] = e2.get(i);
			}
			
			drawer = new BarnesHutGraphDrawer(graph);
		
		
			try {
				
				Property<PFile> p33 = new Property<PFile>("Load Positions", new PFile());			
				gv.addProperty(p33);
				
				Property<PDouble> p = new Property<PDouble>("Simulation.SPRING_LENGTH",new PDouble(300.));	
				((BarnesHutGraphDrawer)drawer).setSpringLength(300.);
				gv.addProperty(p);			
				
				p = new Property<PDouble>("Simulation.MAX_STEP",new PDouble(100.));			
				((BarnesHutGraphDrawer)drawer).max_step = 200.;
				gv.addProperty(p);
				
				Property<PBoolean> p2= new Property<PBoolean>("Simulation.Simulate",new PBoolean(false));		
				gv.addProperty(p2);
				
				Property<PFile> p3 = new Property<PFile>("Save", new PFile());			
				gv.addProperty(p3);
				
				PFile f = new PFile();
				f.save = true;
				Property<PFile> p4 = new Property<PFile>("Save Positions", f);			
				gv.addProperty(p4);
				
				Property<PInteger> p77 = new Property<PInteger>("ToImage",new PInteger(0));			
				gv.addProperty(p77);
				
				Property<PString> p99 = new Property<PString>("SelectedNodes",new PString(""));			
				gv.addProperty(p99);
				p99.setPublic(true);			
				
				Property<PInteger> nodeSize = new Property<PInteger>("Appearance.Node Size",new PInteger(22));			
				addProperty(nodeSize);
				
			} 
			catch (Exception e) {		
				e.printStackTrace();
			}
			done();
			requestRender();
		}
	
		};
		
		initTask.indeterminate = true;	
		initTask.blocking = true;
		this.startTask(initTask);

	}

	public void simulate() {
		
		if (!initTask.done)
			return;
		
		long t = new Date().getTime();
		for (int i=0; i<2; i++)
			drawer.iteration();
		for (int i=0; i<ovals.size(); i++)
		{
			ovals.get(i).x = this.drawer.getX(i);
			ovals.get(i).y = this.drawer.getY(i);
		}
		this.requestRender();
	}
	
	
	public <T extends PropertyType> boolean propertyBroadcast(Property p, T newvalue, PropertyManager origin)
	{
		/*if (p.getName() == "SelectedNodes")
		{
			itemInteraction.clearSelection();
			String[] split = ((StringPropertyType)newvalue).stringValue().split("\t");
			ArrayList<String> nodes = graph.getNodes();
			for (int i=0; i<split.length; i++)
			{
				int index = nodes.indexOf(split[i]);
				if (index >= 0 && index<nodes.size())
				itemInteraction.selectNode(index);
			}
		}*/
		
	
		return true;
	}
	
	@Override
	protected <T extends PropertyType> void propertyUpdated(Property p, T newvalue)
	{
		if (p.getName() == "Simulation.K_REP")
			((BarnesHutGraphDrawer)drawer).k_rep = ((PDouble)newvalue).doubleValue();
		else if (p.getName() == "Simulation.K_ATT")
			((BarnesHutGraphDrawer)drawer).k_att = ((PDouble)newvalue).doubleValue();
		else if (p.getName() == "Simulation.SPRING_LENGTH")
			((BarnesHutGraphDrawer)drawer).setSpringLength(((PDouble)newvalue).doubleValue());
		else if (p.getName() == "Simulation.MAX_STEP")
			((BarnesHutGraphDrawer)drawer).max_step = ((PDouble)newvalue).doubleValue();
		else if (p.getName() == "Save")
			this.save(new File(((PFile)newvalue).path));
		else if (p.getName() == "Simulation.Simulate")
		{
			if (((PBoolean)newvalue).boolValue())
				this.startSimulation(50);
			else
				this.stopSimulation();
		}
		else if (p.getName() == "Appearance.Node Size")
		{
			int s = ((PInteger)newvalue).intValue();
			for (int i=0; i<ovals.size(); i++)
			{
				ovals.get(i).h = s;
				ovals.get(i).w = s;
			}
		}
		else if (p.getName() == "Save Positions")
		{
			ArrayList<String> nodes = graph.getNodes();
			try {
				FileWriter fstream;
				
					fstream = new FileWriter(new File(((PFile)newvalue).path));
				
					BufferedWriter br = new BufferedWriter(fstream);
					
					for (int i=0; i<nodes.size(); i++)
					{
						br.write(nodes.get(i) + "\t" + drawer.getX(i) + "," + drawer.getY(i));
						br.newLine();
					}
					
					br.close();
					
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}
		else if (p.getName() == "Load Positions")
		{
			ArrayList<String> nodes = graph.getNodes();
			
			try{
			 FileInputStream fstream = new FileInputStream(((PFile)newvalue).path);
			 DataInputStream in = new DataInputStream(fstream);
			 BufferedReader br = new BufferedReader(new InputStreamReader(in));
			 String s;
			 while ((s = br.readLine()) != null)
			 {
				s = s.trim();
				
				String[] split = s.split("\t");
				
				if (split.length < 2) continue;
				
				int index = nodes.indexOf(split[0].trim());
				if (index < 0)
				{
					String s2 = split[0].trim().toLowerCase();
					for (int i=0; i<nodes.size(); i++)
					{
						String s1 = nodes.get(i).toLowerCase();
						if (s1.equals(s2))
						{
							index = i;
							break;
						}
					}
					if (index < 0)
					continue;
				}
				
				String[] poss = split[1].split(",");
				double x = Double.parseDouble(poss[0]);
				double y = Double.parseDouble(poss[1]);
				
				//x = x*1.335;
				//y = -y*1.335;
				
				if (drawer != null)
				{
					drawer.setX(index, (int)(x));
					drawer.setY(index, (int)(y));
					
					ovals.get(index).x = (int)(x);
					ovals.get(index).y = (int)(y);
					ovals.get(index).setAnchor(-5, -5);
					
				}
			 }
			 
			 in.close();
			}
			catch(Exception e)
			{
				
			}
		}		
		else if (p.getName() == "ToImage")
		{
			
			
			ArrayList<String> nodes = graph.getNodes();
			int maxX = 0;
			int maxY = 0;
			int minX = 999999;
			int minY = 999999;
			for (int i=0; i<nodes.size(); i++)
			{
				int x = (int)drawer.getX(i);
				int y = (int)drawer.getY(i);
				if (x >  maxX) maxX = x;
				if (y>maxY) maxY = y;
				if (x< minX) minX = x;
				if (y<minY) minY = y;
			}
			
			
			BufferedImage img2 = new BufferedImage((int)(1.1*(maxX-minX)), (int)(1.1*(maxY-minY)),BufferedImage.TYPE_INT_RGB);						
			Graphics2D g = img2.createGraphics();
			g.setColor(this.getBackgroundColor());
			g.fillRect(0, 0, (int)(1.1*(maxX-minX)), (int)(1.1*(maxY-minY)));
			
        	
        	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        	                         RenderingHints.VALUE_ANTIALIAS_ON);

        	g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        	                         RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        	
        	g.translate(-(minX-200), -(minY-100));
        	
        	this.render(g);
        	
        	try {
				ImageIO.write(img2,"PNG",new File("C:/Work/graphviewer.PNG"));
			} catch (IOException e) {
				e.printStackTrace();
			}
        	           	    
        	    
			
		}
		else
			super.propertyUpdated(p, newvalue);
	}
	
	
	
	public void save(File f)
	{
		ArrayList<String> nodes = graph.getNodes();
		for (int i=0; i<nodes.size(); i++)
		{
			int x = (int)drawer.getX(i);
			int y = (int)drawer.getY(i);
			
			
			Property<PInteger> px = graph.nodeProperty(nodes.get(i), "x");
			if (px == null)
			{
				px = new Property<PInteger>("x", new PInteger(x));
				graph.addNodeProperty(nodes.get(i), px);
			}
			px.setValue(new PInteger(x));
			
			Property<PInteger> py = graph.nodeProperty(nodes.get(i), "y");
			if (py == null)
			{
				py = new Property<PInteger>("y", new PInteger(y));
				graph.addNodeProperty(nodes.get(i), py);
			}
			py.setValue(new PInteger(y));
						
		}		
		graph.toGraphML(f);		
	}
	
	public void load(File f)
	{
		graph.fromGraphML(f);
		
		ArrayList<String> nodes = graph.getNodes();
		for (int i=0; i<nodes.size(); i++)
		{
			Property<PInteger> p = graph.nodeProperty(nodes.get(i), "x");
			if (p != null)
				drawer.setX(i,p.getValue().intValue());
			else
				drawer.setX(i,0);
			
			p = graph.nodeProperty(nodes.get(i), "y");
			if (p != null)
				drawer.setY(i,p.getValue().intValue());
			else
				drawer.setY(i,0);
					}
	}


	
	ArrayList<Integer> pairs1 = new ArrayList<Integer>();
	ArrayList<Integer> pairs2 = new ArrayList<Integer>();
	public void render(Graphics2D g)
	{
		if (!initTask.done)
			return;
		
		long t1 = new Date().getTime();
		
		ArrayList<String> nodes = graph.getNodes();
		boolean[] hov = new boolean[nodes.size()];
		boolean[] sel = new boolean[nodes.size()];
		
		ArrayList<Integer> selindex = new ArrayList<Integer>();
		for (int i=0; i<nodes.size(); i++)
		{
			ObjectInteraction.VisualItem item = ovalInteraction.getItem(i);
			
			if (item.selected)
			{
				ovals.get(i).setColor(Color.red);
				sel[i] = true;
				selindex.add(i);
			}
			else if (item.hovered)
			{
				ovals.get(i).setColor(Color.pink);
				hov[i] = true;
			}
			else
			{
				ovals.get(i).setColor(new Color(50,50,255));
			}
			
		}
		
		this.renderNodes(g, sel, hov);
		

		
		long t2 = new Date().getTime();
		
		renderEdges(g, sel, hov);
		
		long t3 = new Date().getTime();
		
	//	System.out.println("render times " + (t2-t1) + " " + (t3-t2));
		
		if (selindex.size() == 2)
		{
			boolean found = false;
			for (int i=0; i<pairs1.size(); i++)
				if ((pairs1.get(i).intValue() == selindex.get(0).intValue() && pairs2.get(i).intValue() == selindex.get(1).intValue()) ||
						(pairs1.get(i).intValue() == selindex.get(0).intValue() && pairs1.get(i).intValue() == selindex.get(1).intValue()))
				{
					found = true;
					break;
				}
			if (!found)
			{
				pairs1.add(selindex.get(0));
				pairs2.add(selindex.get(1));
				System.out.println(selindex.get(0) + " \t" + selindex.get(1));
			}
		}
	
	}
	
	public void renderNodes(Graphics2D g, boolean sel[], boolean[] hov)
	{
		for (int i=0; i<sel.length; i++)
			renderNode(i, sel[i], hov[i], g);
	}
	
	public void renderNode(int i, boolean selected, boolean hovered, Graphics2D g)
	{
		if (selected)
			ovals.get(i).setColor(Color.red);			
		else if (hovered)
			ovals.get(i).setColor(Color.pink);			
		
		ovals.get(i).render(g);
		
	}
	
	public void renderEdges(Graphics2D g, boolean[] sel, boolean[] hov)
	{
		ArrayList<Integer> e1 = new ArrayList<Integer>();
		ArrayList<Integer> e2 = new ArrayList<Integer>();
		graph.getEdgesAsIndeces(e1, e2);
		for (int i=0; i<e1.size(); i++)
		{
			int index1 = e1.get(i);
			int index2 = e2.get(i);
			
			
			renderEdge(index1, index2, i, sel[index1] || sel[index2], hov[index1] || hov[index2], g);
			

		}
	}
	
	public void renderEdge(int p1, int p2, int edgeIndex, boolean selected, boolean hovered, Graphics2D g)
	{
		int x1 = (int)drawer.getX(p1);
		int y1 =  (int)drawer.getY(p1);
		int x2 = (int)drawer.getX(p2);
		int y2 =  (int)drawer.getY(p2);
		
		if (selected) g.setColor(new Color(255,0,0,200));
		else if (hovered) g.setColor(new Color(255,100,100,200));
		else g.setColor(new Color(150,150,150,200));
		
		g.setColor(new Color(150,150,150,200));
		
		g.setStroke(new BasicStroke(2));
		
		g.drawLine(x1, y1, x2, y2);
	}
	
	
	@Override
	public boolean mousepressed(int x, int y, int button) {
			ovalInteraction.mousePress(x, y);
		return false;
	}


	@Override
	public boolean mousereleased(int x, int y, int button) {
			ovalInteraction.mouseRelease(x, y);
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean mousemoved(int x, int y) {
		boolean ret = ovalInteraction.mouseMove(x, y);
		return ret;
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean mousedragged(int x, int y, int px, int py) {
		boolean ret;
		ret = ovalInteraction.mouseMove(x, y);
		return ret;
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyPressed(int keycode) {
		ovalInteraction.ctrlPress();
		// TODO Auto-generated method stub
		super.keyPressed(keycode);
	}


	@Override
	public void keyReleased(int keycode) {
		ovalInteraction.ctrlRelease();
		// TODO Auto-generated method stub
		super.keyReleased(keycode);
	}

	
}
