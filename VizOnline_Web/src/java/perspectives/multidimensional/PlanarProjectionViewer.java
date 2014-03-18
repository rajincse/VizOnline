package perspectives.multidimensional;

import perspectives.*;
import perspectives.base.Property;
import perspectives.base.PropertyType;
import perspectives.properties.PFileOutput;
import perspectives.properties.PInteger;
import perspectives.two_d.Viewer2D;
import perspectives.util.DistanceMatrix2;
import perspectives.util.TableData;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class PlanarProjectionViewer extends Viewer2D 
{
	
	Embedder2D embedder = null;	
	DistanceMatrix2 dm;
	Color[] colors = null;

	public PlanarProjectionViewer(String name, TableData t)
	{
		super(name);
		
		this.dm = t.toDistanceMatrix2();
		embedder = new SpringEmbedder(dm);
		
		this.startSimulation(30);
		

		
		try
		{

			Property<PFileOutput> p3 = new Property<PFileOutput>("Save Colors", new PFileOutput())
					{
						@Override
						protected boolean updating(PFileOutput newvalue) {
							saveColors(newvalue.path);
							return true;
						}
						
					};
			this.addProperty(p3);
			
		}
		catch(Exception e)
		{
			
		}
		
	}

	@Override
	public void simulate() {
		if (embedder != null)
		{
			for (int i=0; i<10; i++)
				embedder.iteration();
			this.requestRender();
		}
		
	}
//
//	@Override
	public void render(Graphics2D g) {
	
		if (embedder == null)
			return;
		
		for (int i=0; i<dm.getCount(); i++)
		{
			int x = (int)(embedder.getX(i)*500);
			int y = (int)(embedder.getY(i)*500);
			
			System.out.println(x + " " + y);
			
			if (colors != null && i<colors.length)
				g.setColor(colors[i]);
			else		
				g.setColor(Color.black);
			g.fillOval(x-10, y-10, 20, 20);			
			
		}
	}
	
	public void saveColors(String path)
	{
		try{
			
			PrintWriter bw = new PrintWriter(new FileWriter(new File(path)));								 
			for (int i=0; i<dm.getCount(); i++)
			{
					Color c = this.embedder.getColor(i);						
					String s = c.getRed() + "\t" + c.getGreen() + " \t" + c.getBlue();
					bw.write(s);
					bw.println();	
			}		 	 
			 bw.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			};
	}
}
