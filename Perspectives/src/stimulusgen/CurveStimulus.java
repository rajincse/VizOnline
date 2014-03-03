package stimulusgen;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class CurveStimulus extends Stimulus {
	
	public CurveStimulus(String id) {
		super(id);

	}

	SineWaveShape[] curves;
	
	double amp;
	double freq;
	int colordif;
	int minDist;
	int maxDist;
	
	public void fromFile(String filename)
	{
		try{
			
			 FileInputStream fstream = new FileInputStream(new File(filename));
			 DataInputStream in = new DataInputStream(fstream);
			 BufferedReader br = new BufferedReader(new InputStreamReader(in));
			 String s;			 
			 int stimIndex=0;
			 
			
				 int numObjects = Integer.parseInt(br.readLine().trim().substring(11));
				 curves = new SineWaveShape[numObjects];
				 
				 String[] curvedata = br.readLine().trim().substring(7).split("\t");
				 for (int i=0; i<curvedata.length; i=i+5)
				 {
					 double amp = Double.parseDouble(curvedata[i]);
					 double freq = Double.parseDouble(curvedata[i+1]);
					 double x = Double.parseDouble(curvedata[i+2]);
					 double y = Double.parseDouble(curvedata[i+3]);
					 double width = Double.parseDouble(curvedata[i+4]);
					 curves[i/5] = new SineWaveShape((int)x,(int)y,Color.black, amp, freq, 0, 0);
				 }
				 minDist = Integer.parseInt(br.readLine().trim().substring(8));
				 maxDist = Integer.parseInt(br.readLine().trim().substring(8));
				 amp = Double.parseDouble(br.readLine().trim().substring(11));
				 freq = Double.parseDouble(br.readLine().trim().substring(5));
				 colordif = Integer.parseInt(br.readLine().trim().substring(8).split(",")[0]);
				 
			
			 in.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
	}
}
