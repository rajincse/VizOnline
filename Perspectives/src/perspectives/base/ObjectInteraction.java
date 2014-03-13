package perspectives.base;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;



public class ObjectInteraction {
	
	
	public abstract class VisualItem
	{
		abstract void displace(double dx, double dy);
		abstract boolean contains(double x, double y);
		
		public boolean selected = false;
		public boolean hovered = false;
	}

	public class PointItem extends VisualItem
	{
		public double x,y,r;
		
		public PointItem(double x, double y, double r)
		{
			this.x = x;
			this.y = y;
			this.r = r;
		}
		
		@Override
		public void displace(double dx, double dy) {	
			x += dx;
			y += dy;
		}

		@Override
		public boolean contains(double x, double y) {
			double d = Math.sqrt((this.x-x)*(this.x-x) +(this.y-y)*(this.y-y));
			if (d <= r)
				return true;
			return false;
			
		}	
	}

	public class LineItem extends VisualItem
	{
		public double x1,y1,x2,y2;
		public double thick;
		
		public LineItem(double x1, double y1, double x2, double y2, double thick)
		{
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.thick = thick;
		}
		
		@Override
		public void displace(double dx, double dy) {	
		}

		@Override
		public boolean contains(double x, double y) {
			double d = Line2D.ptSegDist(this.x1, this.y1, this.x2, this.y2, x, y);
			if (d <= thick)
				return true;
			return false;
			
		}	
	}

	public class RectangleItem extends VisualItem
	{
		public perspectives.util.Rectangle r;
		
		public RectangleItem(perspectives.util.Rectangle r)
		{
			this.r = r;
		}
		
		@Override
		public void displace(double dx, double dy) {
			r.x += dx;
			r.y += dy;
		}

		@Override
		public boolean contains(double x, double y) {
			return r.contains(x, y);
			
		}	
	}
	
	ArrayList<VisualItem> items;
	ArrayList< ArrayList<Integer> > layers;

	
	double oldX, oldY;
	
	boolean dragging;
	
	int nrSelected = 0;
	
	int selectMode = 0;
	
	boolean ctrlDown = false;
	
	boolean weDragged = false;
	
	public ObjectInteraction()
	{
		items = new ArrayList<VisualItem>();
		layers = new ArrayList< ArrayList<Integer> >();	
		layers.add(new ArrayList<Integer>());
	}
	
	public void addItem(VisualItem item)
	{
		items.add(item);
		layers.get(0).add(items.size()-1);
	}
	
	public void addItem(VisualItem item, int layer)
	{
		while (layer >= layers.size())
			layers.add(new ArrayList<Integer>());
		
		addItem(item);
		layers.get(layer).add(items.size()-1);			
	}
	
	public VisualItem getItem(int item)
	{
		return items.get(item);
	}
	
	public int size()
	{
		return items.size();
	}
	
	public void clearSelection()
	{
		nrSelected = 0;
		for (int i=0; i<items.size(); i++)
			items.get(0).selected = false;
	}
	
	
	
	//callbacks
	protected void mouseIn(int object)
	{
		
	}
	
	protected void mouseOut(int object)
	{
		
	}
	
	protected void itemDragged(int item, Point2D delta)
	{
		
	}
	
	protected void itemsSelected(int[] objects)
	{
		
	}
	
	protected void itemsDeselected(int[] objects)
	{
		
	}
	
	
	
	public boolean mouseMove(double x, double y)
	{
		boolean ret = false;
		
		
		
		//not dragging
		for (int i=0; !dragging && i<layers.size(); i++)
		{
			boolean hoveredOnce = false;
			for (int j=0; j<layers.get(i).size(); j++)
			{
				int index = layers.get(i).get(j);
				
				VisualItem item = items.get(index);
				if (item == null) continue;
				
				boolean contains = item.contains(x,y);
				if (contains && !item.hovered && !hoveredOnce)
				{
					mouseIn(index);
					hoveredOnce = true;
					item.hovered = true;
				}
				else if (!contains && item.hovered)
				{
					mouseOut(index);
					item.hovered = false;
				}
			}
		}
		
		if (selectMode == 0)
		{
			if (dragging && (oldX != x || oldY!=y))
				weDragged = true;
			oldX = x;
			oldY = y;
			return false;
		}
		
		//dragging
		for (int i=0; dragging && i<items.size(); i++)
		{
			if (items.get(i).selected)
			{
				items.get(i).displace(x - oldX, y - oldY);
				itemDragged(i, new Point2D.Double(x - oldX, y - oldY));
				ret = true;
			}
		}
		
		if (dragging && (oldX != x || oldY!=y))
			weDragged = true;
		
		oldX = x;
		oldY = y;
		
		return ret;
	}
	
	public void mousePress(double x, double y)
	{
		dragging = true;
		weDragged = false;
		
		
		int index = -1;
		selectMode = 0;
		for (int i=0; i<items.size(); i++)
		{
			if (!items.get(i).hovered)
				continue;
			
			index = i;
			
			if (items.get(i).selected)
				selectMode = 2;
			else
				selectMode = 1;
			
			break;
			
		}
		
		if (selectMode == 1)
		{
			if (!ctrlDown)
			{
				int[] desel = new int[nrSelected];
				int ct = 0;
				for (int i=0; i<items.size(); i++)
					if (items.get(i).selected)
					{
						items.get(i).selected = false;
						desel[ct] = i;
						ct++;					
					}
				itemsDeselected(desel);
				nrSelected -= desel.length;
			}
			
			if (selectMode == 1)
			{
				int[] sel = new int[1];
				sel[0] = index;
				items.get(index).selected = true;
				itemsSelected(sel);
				nrSelected += sel.length;
			}
		}
	}
	
	public void mouseRelease(double x, double y)
	{
		if (selectMode == 2 || selectMode == 0)
		{
			if (!weDragged)
			{
				if (!ctrlDown)
				{
					int[] desel = new int[nrSelected];
					int ct = 0;
					for (int i=0; i<items.size(); i++)
						if (items.get(i).selected)
						{
							items.get(i).selected = false;
							desel[ct] = i;
							ct++;					
						}
					itemsDeselected(desel);	
					nrSelected -= desel.length;
				}
				
				for (int i=0; i<items.size(); i++)
				{
					if (!items.get(i).hovered)
						continue;					
							
					int[] sel = new int[1];
					sel[0] = i;
					
					if (!ctrlDown)
					{
					items.get(i).selected = true;
					itemsSelected(sel);
					nrSelected += sel.length;
					}
					else
					{
						items.get(i).selected = false;
						itemsDeselected(sel);
						nrSelected -= sel.length;
					}
					break;
				}
			}
		}
			
		dragging = false;
	}
	
	public void ctrlPress()
	{
		ctrlDown = true;
	}
	
	public void ctrlRelease()
	{
		ctrlDown = false;
	}

}
