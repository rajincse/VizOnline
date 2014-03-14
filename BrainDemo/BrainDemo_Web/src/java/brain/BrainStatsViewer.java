package brain;

import java.awt.Color;
import java.awt.Graphics2D;

import perspectives.base.Property;
import perspectives.base.PropertyManager;
import perspectives.two_d.Viewer2D;
import perspectives.properties.PString;
import perspectives.base.PropertyType;


public class BrainStatsViewer extends Viewer2D {

	private String selectedTubes = "";
	private BrainData brainData;
	public BrainStatsViewer(String name, BrainData d) {
		super(name);
		
		this.brainData = d;
		
		try
		{
			Property<PString> p = new Property<PString>("SelectedTubes",new PString(""))
					{
						protected void receivedBroadcast(PString newvalue, PropertyManager origin)
						{
							selectedTubes = newvalue.stringValue();
//							return true;
						}
					};
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
	public void simulate() {
		// TODO Auto-generated method stub
		
	}
	
	

}
