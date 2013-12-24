package perspectives;

import java.util.ArrayList;

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
	public Viewer(String name)
	{
		super(name);
		animations = new ArrayList<Animation>();
	}
	
	public abstract String getViewerType();
	
	ArrayList<Animation> animations;
	public void createAnimation(Animation a)
	{
		animations.add(a);
	}
	
	public void animate()
	{
		for (int i=0; i<animations.size(); i++)
		{
			animations.get(i).step();
			if (animations.get(i).done)
			{
				animations.remove(i);
				i--;
			}
		}
	}
}


