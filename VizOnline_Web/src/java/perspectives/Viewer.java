package perspectives;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import properties.Property;
import properties.PropertyType;

import util.Animation;

/**
 * 
 * Base class for viewers. Possible Viewer implementations are Viewer2D, Viewer3D and ViewerGUI. Developers should overload one of these three.
 * 
 * @author rdjianu
 *
 */
public abstract class Viewer extends PropertyManager
{
	EventManager em;

	boolean simulating = false;
	
	protected ViewerContainer container;
	
	private boolean tooltip;
	private String tooltipText;
	private int tooltipX = 0;
	private int tooltipY = 0;
	private long tooltipDelay = 1000;	
	
	protected int width;
	protected int height;
	
	
	public void simulate()
	{
		
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
				em.scheduleEvent(new SimulateEvent(delay), delay);
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
			animate();						
			if (animating)
				em.scheduleEvent(new AnimateEvent(), 50);	
		}		
	}	
	public void createAnimation(Animation a)
	{
		synchronized(animations)
		{
			animations.add(a);
			if (!animating)
			{
				animating = true;
				em.scheduleEvent(new AnimateEvent());
			}
		}

	}
	
	public void setContainer(ViewerContainer c)
	{
		container = c;
	}
	
	public void requestRender()
	{
		if (container != null)
			container.render();
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

	@Override
	public <T extends PropertyType> void propertyUpdatedWrapper(Property p, T newvalue) {
		// TODO Auto-generated method stub
		
		final Property pf = p;
		final T newvaluef = newvalue;
		em.scheduleEvent(new PEvent()
		{
			public void process(){
				propertyUpdated(pf, newvaluef);
			}
		});
                
                requestRender();
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
		tooltipText = t;
	}
	
	public String getToolTipText()
	{
		return tooltipText;
	}
	
	public void setToolTipCoordinates(int x, int y)
	{
		tooltipX = x;
		tooltipY = y;
	}
	
	public long getTooltipDelay()
	{
		return tooltipDelay;
	}
	
	public void setTooltipDelay(long ms)
	{
		tooltipDelay = ms;
	}	
	
}


