package brain;

import java.awt.Color;
import java.awt.Graphics2D;

import perspectives.PropertyManager;
import perspectives.Viewer2D;
import properties.Property;
import properties.PString;
import properties.PropertyType;


public class BrainStatsViewer extends Viewer2D {

	private String selectedTubes = "";
	private BrainData brainData;
	public BrainStatsViewer(String name, BrainData d) {
		super(name);
		
		this.brainData = d;
		
		try
		{
			Property<PString> p = new Property<PString>("SelectedTubes",new PString(""));
			p.setPublic(true);
			p.setVisible(false);
			this.addProperty(p);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void render(Graphics2D g) {
		System.out.println("render stats " + selectedTubes);
		String[] st = selectedTubes.split(",");
		g.setColor(Color.black);
		for (int i=0; i<st.length; i++)
		{
			if (st[i].length() == 0)
				continue;
			
			int t = Integer.parseInt(st[i]);
			g.drawString(st[i], i*50, 100);			
			
		}
		
	}

	
	@Override
	public <T extends PropertyType> boolean propertyBroadcast(Property p,
			T newvalue, PropertyManager origin) {
		if (p.getName() == "SelectedTubes")
		{
			selectedTubes = ((PString)newvalue).stringValue();
			this.requestRender();
		}
		return super.propertyBroadcast(p, newvalue, origin);
	}

	@Override
	public void simulate() {
		// TODO Auto-generated method stub
		
	}
	
	

}
