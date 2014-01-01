package multidimensional;

import perspectives.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import Graph.GraphViewer;


import properties.Property;
import properties.PropertyType;
import data.Table;
import data.TableData;
import properties.*;
import util.Animation;
import util.ObjectInteraction;
import util.Oval;
import util.Rectangle;
import util.ObjectInteraction.RectangleItem;

public class PlanarProjectionViewer //extends Viewer2D 
{
//	
//	Embedder2D embedder = null;
//	
//	ObjectInteraction ovalInteraction;
//	ArrayList<Rectangle> ovals;
//	
//	public PlanarProjectionViewer(String name, TableData t)
//	{
//		super(name);
//		
//		
//		
//		ovals = new ArrayList<Rectangle>();
//		final PlanarProjectionViewer ppv = this;
//			
//		ovalInteraction = new ObjectInteraction()
//		{
//			@Override
//			protected void mouseIn(int obj)
//			{
//				ppv.setToolTipText(ppv.getTable().getPointId(obj));
//				final int o = obj;
//				gv.createAnimation(new Animation.IntegerAnimation(10,16,300)
//				{
//					public void step(int v)
//					{
//						Rectangle l= ((RectangleItem)ovalInteraction.getItem(o)).r;
//						l.w = v;
//						l.h = v;						
//					}
//				});
//			}
//			
//			protected void mouseOut(int obj)
//			{
//				gv.setToolTipText(graph.getNodes().get(obj));
//				final Rectangle l= ((RectangleItem)ovalInteraction.getItem(obj)).r;
//				gv.createAnimation(new Animation.IntegerAnimation(16,10,300)
//				{
//					public void step(int v)
//					{						
//						l.w = v;
//						l.h = v;					
//					}
//				});
//			}
//			
//			protected void itemDragged(int item, Point2D delta)
//			{
//				gv.drawer.setX(item, gv.drawer.getX(item) + (int)delta.getX());
//				gv.drawer.setY(item, gv.drawer.getY(item) + (int)delta.getY());
//			}
//		};
//		
//		
//		ArrayList<String> nodes = graph.getNodes();
//		for (int i=0; i<nodes.size(); i++)
//		{
//			Oval o = new Oval(0,0,10,10);
//			ovals.add(o);
//			ovalInteraction.addItem(ovalInteraction.new RectangleItem(o));
//		}
//		
//
//		embedder = new SpringEmbedder(t.getTable());
//		
//		this.setPointSize(25);
//		
//		try
//		{
//			Property<PString> p = new Property<PString>("Color", new PString(""));			
//			addProperty(p);			
//			
//			Property<PFile> p3 = new Property<PFile>("Save Colors", new PFile());			
//			this.addProperty(p3);
//			
//		}
//		catch(Exception e)
//		{
//			
//		}
//		
//	}
//
//	@Override
//	public <T extends PropertyType> void propertyUpdated(Property p, T newvalue) {
//		if (p.getName() == "Color")
//		{
//			for (int i=0; i<this.getNumberOfPoints(); i++)
//			{
//				Color c = this.embedder.getColor(i);
//				System.out.println(i + " -> " + c.getRed() + " " + c.getGreen() + " " + c.getBlue());
//				this.setColor(i,c);
//			}
//		}
//		else if (p.getName() == "Save Colors")
//		{
//			try{
//				 
//				 FileOutputStream fstream = new FileOutputStream(new File(((PFile)newvalue).path));
//				 DataOutputStream out = new DataOutputStream(fstream);
//				 
//				for (int i=0; i<this.getNumberOfPoints(); i++)
//				{
//						Color c = this.embedder.getColor(i);						
//						String s = this.getPointLabel(i) + "\t" + c.getRed() + "\t" + c.getGreen() + " \t" + c.getBlue();
//						out.writeChars(s);
//						
//				}		 	 
//				 out.close();
//				}
//				catch(Exception e)
//				{
//					
//				};
//		}
//		else
//			super.propertyUpdated(p, newvalue);
//	}
//
//	@Override
//	protected String getPointLabel(int p) {
//		return table.getRowName(p);
//	}
//
//	@Override
//	protected int getNumberOfPoints() {
//		return table.getRowCount();
//	}
//
//	@Override
//	protected int getPointX(int p) {
//		return (int)(embedder.getX(p)*400);
//	}
//
//	@Override
//	protected int getPointY(int p) {
//		return (int)(embedder.getY(p)*400);
//	}
//
//	@Override
//	protected void setPointX(int p, int x) {
//		// TODO Auto-generated method stub
//		embedder.setX(p, x/400.);
//		
//	}
//
//	@Override
//	protected void setPointY(int p, int y) {
//		embedder.setY(p, y/400.);
//		
//	}
//
//	@Override
//	public void simulate() {
//		if (embedder != null)
//			embedder.iteration();
//		
//	}
//
//	@Override
//	public void render(Graphics2D g) {
//		// TODO Auto-generated method stub
//		
//	}
}
