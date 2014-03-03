package stimulusgen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

import properties.Property;
import perspectives.Viewer2D;
import properties.PColor;
import properties.PInteger;
import util.Util;

public class StimulusGenViewer extends Viewer2D{
	public static final String PROPERTY_NAME_MIN_DISTANCE = "Distance.Minimum Distance";
	public static final String PROPERTY_NAME_MAX_DISTANCE = "Distance.Maximum Distance";
	public static final String PROPERTY_NAME_SIZE = "Size";
	public static final String PROPERTY_NAME_FORECOLOR = "Forecolor";
	public static final String PROPERTY_NAME_BACKGROUND_COLOR = "BackgroundColor";
	public static final String PROPERTY_NAME_COLOR_DIFFERENCE = "ColorDifference";
	public static final String PROPERTY_NAME_OBJECT_COUNT = "Object Count";
	//noisy background stuffs
	public static final String PROPERTY_NAME_BACKGROUND_NOISE = "Background.Noise";
	
	StimulusGenPlotter plotter; 
	
	BufferedImage backgroundImage = null;
	
	
	private LabColorGenerator[] labs ;
	public StimulusGenViewer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		
		
		this.labs = LabColorGenerator.generate(6);
		this.loadProperties();
	
	}
	
	private void loadProperties()
	{
		try {
			Property<PInteger> minDistance = new Property<PInteger>(PROPERTY_NAME_MIN_DISTANCE, new PInteger(50));
			this.addProperty(minDistance);
			
			Property<PInteger> maxDistance = new Property<PInteger>(PROPERTY_NAME_MAX_DISTANCE, new PInteger(100));
			this.addProperty(maxDistance);
			
			Property<PInteger> size = new Property<PInteger>(PROPERTY_NAME_SIZE, new PInteger(20));
			this.addProperty(size);
			
			Property<PInteger> colorDifference = new Property<PInteger>(PROPERTY_NAME_COLOR_DIFFERENCE, new PInteger(7));
			this.addProperty(colorDifference);
			
			Property<PInteger> objectCount = new Property<PInteger>(PROPERTY_NAME_OBJECT_COUNT, new PInteger(15));
			this.addProperty(objectCount);
			
			
			Property<PInteger> backgroundCopies = new Property<PInteger>(PROPERTY_NAME_BACKGROUND_NOISE, new PInteger(11));
			this.addProperty(backgroundCopies);
			
			Property<PInteger> save = new Property<PInteger>("SaveStimulus", new PInteger(0));
			this.addProperty(save);
			
			
			plotter = this.getPlotter(15, 50, 100);
			
		}
		catch (Exception e) {		
			e.printStackTrace();
		}
	}
	
	private int getPropertyIntValue(String name)
	{
		Property<PInteger> prop = this.getProperty(name);
		return prop.getValue().intValue();
	}
	private Color getPropertyColorValue(String name)
	{
		Property<PColor> prop = this.getProperty(name);
		return prop.getValue().colorValue();
	}


	private StimulusGenPlotter getPlotter(int objectCount, int minDistance, int maxDistance)
	{
		StimulusGenPlotter plotter = new RadialDistancePlotter(objectCount, minDistance, maxDistance);
		
		return plotter;
	}
	public <T extends properties.PropertyType> void propertyUpdated(Property p, T newvalue)
	{
		int minDist = this.getPropertyIntValue(PROPERTY_NAME_MIN_DISTANCE);
		int maxDist = this.getPropertyIntValue(PROPERTY_NAME_MAX_DISTANCE);		
		int objectCount = this.getPropertyIntValue(PROPERTY_NAME_OBJECT_COUNT);
		
		if (p.getName().equals(PROPERTY_NAME_MIN_DISTANCE))
			plotter = this.getPlotter(objectCount, ((PInteger)newvalue).intValue(), maxDist);	
		else if (p.getName().equals(this.PROPERTY_NAME_MAX_DISTANCE))
			plotter = this.getPlotter(objectCount, minDist, ((PInteger)newvalue).intValue());
		else if (p.getName().equals(this.PROPERTY_NAME_OBJECT_COUNT))
			plotter = this.getPlotter(((PInteger)newvalue).intValue(), minDist, maxDist);
		else if (p.getName().equals(this.PROPERTY_NAME_COLOR_DIFFERENCE))
		{
			colorDif = ((PInteger)newvalue).intValue();
		}
		else if (p.getName().equals("SaveStimulus"))
		{
			this.saveStimulus();
		}
		
		backgroundImage = createBackgroundImage();
		
		this.requestRender();
	}
	int colorDif = 0;

	@Override
	public void render(Graphics2D g) {
		
		g.setColor(Color.white);
		g.fillRect(-800, -800, 1600,1600);
		
		g.setColor(Color.red);
		g.drawRect(-401, -401, 802, 802);
		
		g.drawImage(backgroundImage, -400, -400,null);
		renderForeground(g);
	}
	
	
	
	public BufferedImage createBackgroundImage()
	{

		BufferedImage bim = new BufferedImage(800,800, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gbim = bim.createGraphics();
		
		gbim.setColor(Color.black);
		gbim.setFont(gbim.getFont().deriveFont(30f));
		String chars = "abcdefghmnopqrstuv";
		int n = 500*((PInteger)(getProperty(this.PROPERTY_NAME_BACKGROUND_NOISE).getValue())).intValue();
		
		if (n<0) return bim;
		
		for (int i=0; i<n; i++)
		{
			int tx = (int)(Math.random()*800);
			int ty = (int)(Math.random()*800);
			gbim.translate(tx,ty);
			//renderForeground(gbim);
			int c = (int)(Math.random()*(chars.length()-1));
			String s = ""+ chars.charAt(c);
			gbim.drawString(s, 0, 0);
			
			gbim.translate(-tx,-ty);
		}
		
		return bim;
	}
	
	public void renderForeground(Graphics2D g)
	{
		int cW = this.getPropertyIntValue(PROPERTY_NAME_SIZE);
		int cH = cW;
			
		int cdif = 6-colorDif;
		if (cdif < 0) cdif = 0;
		if (cdif >= labs.length) cdif = labs.length-1;
		Color col = labs[cdif].getColor();
		
		g.setColor(col);
		
		for (int i=0; i<plotter.objectCount; i++)
		{
			Point p = plotter.getPosition(i);
			int x = p.x;
			int y = p.y;
			
			g.fillOval(x-cW/2, y-cH/2, cW, cH);
		}		
	}

	
	@Override
	public void simulate() {
	}
	
	int stimulus = 0;
	public void saveStimulus()
	{
		stimulus++;
		
		BufferedImage bim = new BufferedImage(800,800, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = bim.createGraphics();
		
		g.translate(400, 400);
		
		this.render(g);
		
		Calendar c = Calendar.getInstance();
		String filename ="" + c.get(c.DAY_OF_YEAR) + c.get(c.HOUR) + c.get(c.MINUTE) + c.get(c.SECOND);
		
		try {
			ImageIO.write(bim, "PNG", new File("c:\\work\\stim_" + filename + ".PNG"));
			saveData("c:\\work\\stim_" + filename + ".tex");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveData(String filename)
	{
		 PrintWriter bw;
		try {
			bw = new PrintWriter(new FileWriter(new File(filename)));
		
		 
		bw.write("NUMOBJECTS:"+plotter.objectCount);
		bw.println();
		
		bw.write("COORDS:");
		for (int i=0; i<plotter.objectCount; i++)
			if (i != plotter.objectCount-1)
				bw.write(plotter.getPosition(i).x +"," + plotter.getPosition(i).y + ";");
			else
				bw.write(plotter.getPosition(i).x +"," + plotter.getPosition(i).y);
		
		bw.println();
		
		bw.println("MINDIST:" +  this.getPropertyIntValue(PROPERTY_NAME_MIN_DISTANCE));
		bw.println("MAXDIST:" +  this.getPropertyIntValue(PROPERTY_NAME_MAX_DISTANCE));
		bw.println("COLORDIF:" +  this.getPropertyIntValue(this.PROPERTY_NAME_COLOR_DIFFERENCE));
		bw.println("SIZE:" +  this.getPropertyIntValue(this.PROPERTY_NAME_SIZE));
		bw.println("NOISE:" +  this.getPropertyIntValue(this.PROPERTY_NAME_BACKGROUND_NOISE));
			
		 bw.close();
		 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
