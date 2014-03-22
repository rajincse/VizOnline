package perspectives.base;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.Timer;

import perspectives.base.PropertyType;

import perspectives.base.Animation;
import perspectives.three_d.Viewer3D;
import perspectives.three_d.ViewerContainer3D;

/**
 * 
 * Base class for viewers. Possible Viewer implementations are Viewer2D, Viewer3D and ViewerGUI. Developers should overload one of these three.
 * 
 * @author rdjianu
 *
 */
public abstract class Viewer extends PropertyManager
{
	public EventManager em;

	boolean simulating = false;
	
	private ViewerContainer container;
	
	private boolean tooltip;
	private String tooltipText;
	private int tooltipX = 0;
	private int tooltipY = 0;
	private long tooltipDelay = 1000;	
	
	
	public void simulate()
	{
		
	}	
	public ViewerContainer getContainer()
	{
		return container;
	}
	
	private class SimulateEvent implements PEvent
	{
		long delay;
		public SimulateEvent(long delay)
		{
			this.delay = delay;
		}
		public void process() {
		
			if (simulating)
				simulate();
			if (simulating)
			{			
				em.scheduleEvent(new SimulateEvent(delay), delay,"simevent");
			}
		}
		
	}
	public void startSimulation(int timeStep)
	{
		synchronized(this)
		{
			if (!simulating)
			{
				em.scheduleEvent(new SimulateEvent(timeStep));
				simulating = true;
			}
		}
		
	}
	public void stopSimulation()
	{	
		synchronized(this)
		{
			simulating = false;
		}
	}

	public Viewer(String name)
	{
		super(name);
		
		tooltip = true;
		tooltipText = "";
		
		animations = new ArrayList<Animation>();
		em = new EventManager();
	}
	
	public abstract String getViewerType();
	
	ArrayList<Animation> animations;
	boolean animating = false;	
	private class AnimateEvent implements PEvent
	{
		public void process() {
			//System.out.println("animation 2");
			animate();						
			if (animating)
				em.replaceEvent(new AnimateEvent(), "animate", 50);	
		}		
	}	
	public void createAnimation(Animation a)
	{
		synchronized(animations)
		{
			animations.add(a);
			animating = true;
			em.replaceEvent(new AnimateEvent(),"animate");
		}
	}
	
	public void setContainer(ViewerContainer c)
	{
		container = c;
	}
	
	public void requestRender()
	{
		if (container != null)
		{
			em.replaceEvent(new PEvent()
			{
				public void process() {
					container.render();					
				}
			},"render");
		}
	}
	
	public void animate()
	{
		int animationCount;
		synchronized(animations)
		{
			animationCount = animations.size();
		}
		
		for (int i=0; i<animationCount; i++)
		{
			Animation a;
			synchronized(animations)
			{
				a = animations.get(i);
			}
			
			a.step();
			if (a.done)
			{
				synchronized(animations)
				{
					animations.remove(i);
					i--;
				}
			}
			
			synchronized(animations)
			{
				animationCount = animations.size();
			}
		}
		
		synchronized(animations)
		{
			if (animations.size() == 0)
				animating = false;
		}
	}

	
	public void renderTooltip(Graphics2D g)
	{
		g.setFont(new Font("Sans-serif",Font.PLAIN, 11));
			int sizeX = g.getFontMetrics().stringWidth(tooltipText)+6;
			int sizeY = g.getFontMetrics().getHeight()+1;
			g.setColor(new Color(235,235,100));
			g.fillRect(tooltipX, tooltipY-sizeY, sizeX, sizeY);
			g.setColor(Color.DARK_GRAY);
			g.setStroke(new BasicStroke(1));
			g.drawRect(tooltipX, tooltipY-sizeY, sizeX, sizeY);
			g.drawString(tooltipText, tooltipX+4, tooltipY-3);	
	}
	
	public void enableToolTip(boolean t)
	{
		tooltip = t;
	}
	
	public boolean getToolTipEnabled()
	{
		return tooltip;
	}
	
	public void setToolTipText(String t)
	{
		boolean needRender = false;
		if (!tooltipText.equals(t))
			needRender = true;
		tooltipText = t;
		if (needRender)
		this.requestRender();
	}
	
	public String getToolTipText()
	{
		return tooltipText;
	}
	
	public void setToolTipCoordinates(int x, int y)
	{
		tooltipX = x;
		tooltipY = y;
		
		if (tooltipText.length() != 0)
			requestRender();
	}
	
	public long getTooltipDelay()
	{
		return tooltipDelay;
	}
	
	public void setTooltipDelay(long ms)
	{
		tooltipDelay = ms;
	}
	@Override
	protected boolean addProperty(Property p, int where){		
		boolean ret = super.addProperty(p, where);
		if (ret)
			p.setEventManager(em);
		return ret;
	}	
	
	public int getContainerWidth()
	{
		return container.getWidth();
	}
	
	public int getContainerHeight()
	{
		return container.getHeight();
	}
	
	
	
}


