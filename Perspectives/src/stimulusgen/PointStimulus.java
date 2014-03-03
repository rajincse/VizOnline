package stimulusgen;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PointStimulus extends Stimulus{	


	Point[] coords; 
	int pointSize = 20;
	
	int minDist;
	int maxDist;
	int size;
	int noise;
	int colordif;
	
	public PointStimulus(String id) {
		super(id);
	}
	
	public void fromFile(String filename)
	{
		try{
			
			 FileInputStream fstream = new FileInputStream(new File(filename));
			 DataInputStream in = new DataInputStream(fstream);
			 BufferedReader br = new BufferedReader(new InputStreamReader(in));
			 String s;			 
			 int stimIndex=0;
			 
			
				 int numObjects = Integer.parseInt(br.readLine().trim().substring(11));
				 coords = new Point[numObjects];
				 
				 String[] pointdata = br.readLine().trim().substring(7).split(";");
				 for (int i=0; i<pointdata.length; i=i++)
				 {
					 String[] xy = pointdata[i].split(",");					 
					 coords[i] = new Point((int)Double.parseDouble(xy[0]), (int)Double.parseDouble(xy[1]));
				 }
				 
				 minDist = Integer.parseInt(br.readLine().trim().substring(8));
				 maxDist = Integer.parseInt(br.readLine().trim().substring(8));
				 colordif = (int)Double.parseDouble(br.readLine().trim().substring(9));
				 size = (int)Double.parseDouble(br.readLine().trim().substring(5));
				 noise = (int)Integer.parseInt(br.readLine().trim().substring(6));
				 
			
			 in.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
	}
}
