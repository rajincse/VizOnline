package stimulusgen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import perspectives.Viewer2D;
import properties.PFileInput;
import properties.PFileOutput;
import properties.PList;
import properties.Property;
import properties.PropertyType;

public class GazeAnalyzer extends Viewer2D {
	
	
	ArrayList<UserData> userData;
	AggregatedUserData aggregateUser;
	ArrayList<Stimulus> stimuli;
	
	BufferedImage image = null;
	

	public GazeAnalyzer(String name) {
		super(name);
		
		userData = new ArrayList<UserData>();
		stimuli = new ArrayList<Stimulus>();		
		
		try {
			Property<PFileInput> p = new Property<PFileInput>("AddUserData", new PFileInput());
			this.addProperty(p);
			
			Property<PList> users = new Property<PList>("Users", new PList());
			this.addProperty(users);
			
			Property<PList> stim = new Property<PList>("Stimuli", new PList());
			this.addProperty(stim);
			
			Property<PFileOutput> agPerStim = new Property<PFileOutput>("AggregatePerStim", new PFileOutput());
			this.addProperty(agPerStim);
			Property<PFileInput> agPerUser = new Property<PFileInput>("AggregatePerUser", new PFileInput());
			this.addProperty(agPerStim);
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
	
	}
	
	

	@Override
	protected <T extends PropertyType> void propertyUpdated(Property p,
			T newvalue) {
		if (p.getName().equals("AddUserData"))
		{
			this.loadUserData(((PFileInput)newvalue).path);
		}
		else if (p.getName().equals("Users"))
		{
			PList ulist = (PList)newvalue;
			int[] selInd = ulist.selectedIndeces;
			
			PList slist = (PList)getProperty("Stimuli").getValue();
			int[] selInds = slist.selectedIndeces;
			
			if (selInd.length > 0 && selInds.length > 0)
			{
				String user = ulist.items[selInd[0]];
				String stim = slist.items[selInds[0]];
				updateUserStimulus(user, stim);
			}
		}
		else if (p.getName().equals("Stimuli"))
		{
			PList ulist = (PList)getProperty("Users").getValue();
			int[] selInd = ulist.selectedIndeces;
			
			PList slist = (PList)newvalue;
			int[] selInds = slist.selectedIndeces;
			
			if (selInd.length > 0 && selInds.length > 0)
			{
				String user = ulist.items[selInd[0]];
				String stim = slist.items[selInds[0]];
				updateUserStimulus(user, stim);
			}
		}
		else if (p.getName().equals("AggregatePerStim"))
		{
			//find the currently selected stimulus
			PList slist = (PList)getProperty("Stimuli").getValue();
			int[] selInds = slist.selectedIndeces;
			if (selInds.length == 0)
				return;
			
			int selected = selInds[0];
						
			//add all data			
			

			
			
			try{
				File file = new File(((PFileOutput)newvalue).path);
				 PrintWriter bw = new PrintWriter(new FileWriter(file));
				 
					int sindex = this.aggregateUser.stimuli.indexOf(stimuli.get(selected));
					for (int i=0; i<this.aggregateUser.fixations.get(sindex).size(); i++)
					{
						Fixation f = this.aggregateUser.fixations.get(sindex).get(i);
						String prov = this.aggregateUser.fixationProvenance.get(sindex).get(i);  
						String s = prov + "\t" + f.x + "\t" + f.y + " \t" + f.startTime + "\t" + f.endTime + "\tFixation";
						bw.println(s);	
					} 

				 bw.close();
				}
				catch(Exception e)
				{
					
				}		
			
		}
		super.propertyUpdated(p, newvalue);
	}
	
	private Stimulus getStimulus(String stimId)
	{	
		for (int i=0; i<stimuli.size(); i++)
			if (stimuli.get(i).id.equals(stimId))
				return stimuli.get(i);
		return null;
	}
	
	private UserData getUserData(String userId)
	{
		for (int i=0; i<userData.size(); i++)
			if (userData.get(i).user.equals(userId))
				return userData.get(i);	
		return null;
	}
	
	private void createVisualization(BufferedImage stimulusImage, ArrayList<Fixation> fixations, ArrayList<Saccade> saccades, Graphics2D g)
	{
		g.drawImage(stimulusImage, 0,0, null);
				
		g.setColor(new Color(255,150,150,100));
		for (int i=0; i<fixations.size(); i++)
		{
			Fixation f = fixations.get(i);
			int x = f.x;
			int y = f.y;
			int d = (int)(f.duration/10);
			g.fillOval(x-d, y-d, 2*d, 2*d);
		}
		
	}
	
	
	public void updateUserStimulus(String userId, String stimId)
	{

		Stimulus stimulus = getStimulus(stimId);
		if (stimulus == null) return;
		
		UserData userData = getUserData(userId);
		if (userData == null) return;
		
		int sindex = userData.stimuli.indexOf(stimulus);
		
		if (sindex < 0)
		{
			System.out.println("Couldn't find stimulus " + stimulus.id + " for user " + userId);
			return;
		}
		
		ArrayList<Fixation> fixations = userData.fixations.get(sindex);
		ArrayList<Saccade> saccades = userData.saccades.get(sindex);
		
		BufferedImage simage = stimulus.getImage();	
		
		if (simage == null) return;
		
		image = new BufferedImage(simage.getWidth(), simage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		
		createVisualization(simage, fixations, saccades, g);		

	}



	@Override
	public void render(Graphics2D g) {
		if (image != null)
			g.drawImage(image, 0, 0 , null);
		
	}

	@Override
	public void simulate() {
		// TODO Auto-generated method stub
		
	}
	
	String[] data = new String[250];
	
	public void loadUserData(String filename)
	{
		try{
			File f = new File(filename);
			 FileInputStream fstream = new FileInputStream(f);
			 DataInputStream in = new DataInputStream(fstream);
			 BufferedReader br = new BufferedReader(new InputStreamReader(in));
			 String s;			 
			 int stimIndex=0;	 
			 
			 Stimulus currentStimulus = null;	
			 int currentWidthOffset = 0;
			 int currentHeightOffset = 0;
			 String id = filename.substring(filename.lastIndexOf("\\")+1, filename.length());
			 UserData userData = new UserData(id);			
			 
			 this.userData.add(userData);
			 
			 int ag = 1;
			 if (this.userData.size() == 1)
			 {
				 aggregateUser = new AggregatedUserData("aggregate");
				 this.userData.add(aggregateUser);
				 ag = 2;
			 }
			 
			 PList userList = (PList)getProperty("Users").getValue();
			 String[] items = new String[userList.items.length+ag];
			 if (ag == 2)
				 items[0] = "aggregate";
			 for (int i=0; i<userList.items.length; i++)
				 items[i+ag-1] = userList.items[i];
			 items[items.length-1] = id;
			 userList.items = items;			 
			 this.getProperty("Users").setValue(userList);

			 
			 while ((s = br.readLine()) != null)
			 {
				s = s.trim();
				
				if (!s.startsWith("Saccade") && !s.startsWith("Fixation") && !s.startsWith("UserEvent"))
					continue;
				
				if (s.startsWith("UserEvent"))
				{
					String[] fields = s.split("\t");
					String[] f4 = fields[4].split(" ");					
					String stimname = f4[2].substring(0,f4[2].length()-4);
					
					currentStimulus = null;
					for (int i=0; i<stimuli.size(); i++)
						if (stimuli.get(i).id.equals(stimname))
						{
							currentStimulus = stimuli.get(i);
							break;
						}
					if (currentStimulus == null)
					{
						currentStimulus = new Stimulus(stimname);
						currentStimulus.fromFile(f.getParent() + "\\"+ stimname + ".tex");
						currentStimulus.imagePath = f.getParent() + "\\"+ stimname + ".png";
						//currentStimulus.imagePath = f.getParent() + "\\"+ stimname;
						stimuli.add(currentStimulus);
						
					}
					userData.addStimulus(currentStimulus);
					
					BufferedImage im = currentStimulus.getImage();
					if (im != null)
					{
						//currentWidthOffset =  (800-im.getWidth()/2);
						//currentHeightOffset =  (450-im.getHeight()/2);
						String wis = stimname.substring(1,stimname.length());
						int wi = Integer.parseInt(wis);
						if (wi <= 21)
						{
							currentWidthOffset =  400;
							currentHeightOffset = 185;
						}
						else
						{
							currentWidthOffset =  207;
							currentHeightOffset = 161;
						}
						
					}
					else
					{
						currentWidthOffset =  0;
						currentHeightOffset = 0;
					}
				}
				else if (s.startsWith("Fixation") && currentStimulus != null)
				{
					String[] fields = s.split("\t");
					Fixation fix = new Fixation();
					fix.startTime = Long.parseLong(fields[3])/1000;
					fix.endTime = Long.parseLong(fields[4])/1000;					
					fix.duration = Long.parseLong(fields[5])/1000;
					fix.dispX = Integer.parseInt(fields[8]);
					fix.dispY = Integer.parseInt(fields[9]);
					fix.x = (int)Double.parseDouble(fields[6]) - currentWidthOffset;
					fix.y = (int)Double.parseDouble(fields[7]) -  currentHeightOffset;
					
					userData.addFixation(currentStimulus, fix);
				}				
				else if (s.startsWith("Saccade")  && currentStimulus != null)
				{
					String[] fields = s.split("\t");
					Saccade sac = new Saccade();
					
					sac.time = Long.parseLong(fields[5])/1000;
				
					double x1 = Double.parseDouble(fields[6])  -  currentWidthOffset;
					double y1 = Double.parseDouble(fields[7])  -  currentHeightOffset;
					double x2 = Double.parseDouble(fields[8])  -  currentWidthOffset;
					double y2 = Double.parseDouble(fields[9]) -  currentHeightOffset;
					
					sac.dist = Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
					sac.amp = Double.parseDouble(fields[10]);					
					sac.speed = Double.parseDouble(fields[13]);
					sac.x1 = (int) x1;
					sac.y1 = (int) y1;
					sac.x2 = (int) x2;
					sac.y2 = (int) y2;
					
					userData.addSaccade(currentStimulus, sac);
				}
			 }
			 
			 userData.cleanTimes();
			 
			 //add to aggregate
			 for (int i=0; i<userData.stimuli.size(); i++)
			 {
				 Stimulus ss = userData.stimuli.get(i);
				 aggregateUser.addStimulus(ss);
				 for (int j=0; j<userData.fixations.get(i).size(); j++)
					 aggregateUser.addFixation(ss, userData.fixations.get(i).get(j), userData.user);
				 for (int j=0; j<userData.saccades.get(i).size(); j++)
					 aggregateUser.addSaccade(ss, userData.saccades.get(i).get(j), userData.user);
			 }
			 in.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		
		 PList stimList = (PList)getProperty("Stimuli").getValue();
		 String[] itemss = new String[stimuli.size()];
		 for (int i=0; i<stimuli.size(); i++)
			 itemss[i] = stimuli.get(i).id;		
		 stimList.items = itemss;			 
		 this.getProperty("Stimuli").setValue(stimList);
		
		for (int i=0; i<data.length; i++)
			System.out.println(data[i]);
	}
	

}
