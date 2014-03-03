package stimulusgen;


import java.awt.Color;
import java.awt.image.BufferedImage;

import util.Util;

public class LabColorGenerator {
	public double l;
	public double a;
	public double b;
	public LabColorGenerator(double l, double a, double b)
	{
		this.l = l;
		this.a = a;
		this.b = b;
	}
	
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		Color c = Util.labToRgb(l, a, b);
		String hex = String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
		return l+","+a+", "+b+"= "+hex;
	}
	public String getDivHtml()
	{
		Color c = Util.labToRgb(l, a, b);
		String hex = String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
		return "<div style=\"background-color:"+hex+"\"></div>";
	}
	public Color getColor()
	{
		Color c = Util.labToRgb(l, a, b);
		//System.out.println("getcolor " + c.getRed() + " " + c.getGreen() + " " + c.getBlue());
		return c;
	}
	public static LabColorGenerator [] generate(int size)
	{
		LabColorGenerator []labs = new LabColorGenerator[size];
		
		/*
		double l1 = 14.5;
		double a1 = 127;
		double b1 = -128;
		*/
		//labs[0] = new LabColorGenerator( 14.5,127,-128);
		
		labs[0] = new LabColorGenerator(0,0,0);
		/*
		double l2 = 90;
		double a2 = -126;
		double b2 = 125;
		*/
		//labs[labs.length-1] = new LabColorGenerator(90,-126,125);
		
		labs[labs.length-1] = new LabColorGenerator(124.79, 0, 0);
		
//		System.out.println(labs[0]);
//		System.out.println(labs[9]);
		
		for(int i=1;i< labs.length-1;i++)
		{
			double perc = (double)i/(labs.length+1);
			
			double l = labs[0].l + (labs[labs.length-1].l-labs[0].l)*perc;
			double a = labs[0].a + (labs[labs.length-1].a-labs[0].a)*perc;
			double b = labs[0].b + (labs[labs.length-1].b-labs[0].b)*perc;
			
			labs[i] = new LabColorGenerator(l,a,b);
			

		}
		
		for (int i=0; i<labs.length; i++)
		{
			Color c = Util.labToRgb(labs[i].l, labs[i].a, labs[i].b);
			System.out.println(c.getRed() + " " +  c.getGreen() + " " + c.getBlue());
		}
		return labs; 
	}
}
