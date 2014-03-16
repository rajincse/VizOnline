package userstudy.graph;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import perspectives.base.DataSource;
import perspectives.base.Property;
import perspectives.base.Task;
import perspectives.graph.GraphData;
import perspectives.properties.PFileInput;
import perspectives.properties.PInteger;

public class GraphUserStudyData extends GraphData{

	public static String PROPERTY_LOAD_QUESTION ="Load Questions";
	public static String PROPERTY_LOAD_POSITION ="Load Positions";
	/**
	 * 
	 */
	private static final long serialVersionUID = 2813556898604436356L;
	private ArrayList<GraphUserStudyQuestion> questions;
	private ArrayList<Point> vertexPositions ;
	
	private boolean isPositionFileLoaded = false;
	private boolean isQuestionFileLoaded = false;

	public GraphUserStudyData(String name) {
		super(name);
		
		try {
			PFileInput f = new PFileInput();
			f.dialogTitle = "Open UserStudy File";
			f.extensions = new String[]{"xml","*"};
			f.currentExtension = 0;
			
			final GraphUserStudyData thisf = this;
			Property<PFileInput> p = new Property<PFileInput>(PROPERTY_LOAD_QUESTION, f)
					{
					@Override
						public boolean updating(PFileInput newvalue)
						{
							final PFileInput newvaluef = newvalue;
							
							Task t = new Task("Loading...")
							{	
							
								@Override
								public void task() {
									
									try {
										thisf.questions =  GraphUserStudyQuestion.fromFile(((PFileInput)newvaluef).path);
										thisf.removeProperty(PROPERTY_LOAD_QUESTION);
										
										Property<PInteger> p = new Property<PInteger>("#Questions", new PInteger(thisf.questions.size()));
										p.setReadOnly(true);
										thisf.addProperty(p);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									thisf.isQuestionFileLoaded = true;
									done();
								}
							};
							t.indeterminate = true;
							t.blocking = true;
							thisf.startTask(t);
							
							return true;
						}
					};
			this.addProperty(p);
			
			PFileInput f2 = new PFileInput();
			f2.dialogTitle = "Open UserStudy File";
			f2.extensions = new String[]{"txt","*"};
			f2.currentExtension = 0;
			Property<PFileInput> p1 = new Property<PFileInput>(PROPERTY_LOAD_POSITION, f2)
            		{                    	
            			@Override
            			public boolean updating(PFileInput newvalue)
            			{
            				final PFileInput newvaluef = newvalue;
            				Task t = new Task("Loading Positions ...")
							{	
							
								@Override
								public void task() {
									try {
										thisf.vertexPositions = new ArrayList<Point>();
			            				loadPositions(newvaluef.path);
			            				
			            				thisf.removeProperty(PROPERTY_LOAD_POSITION);
			            				Property<PInteger> p = new Property<PInteger>("#Vertices", new PInteger(thisf.vertexPositions.size()));
										p.setReadOnly(true);
									
										thisf.addProperty(p);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									thisf.isPositionFileLoaded = true;
									done();
									
								}
							};
							t.indeterminate = true;
							t.blocking = true;
							thisf.startTask(t);
            				
            				return true;
            			}

						                  			
            		};
            this.addProperty(p1);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean isGraphLoaded()
	{
		return super.isLoaded();
	}
	
	public boolean isPositionLoaded()
	{
		return this.isPositionFileLoaded;
	}
	
	public boolean isQuestionLoaded()
	{
		return this.isQuestionFileLoaded;
	}
	
	public boolean isAllDataLoaded()
	{
		return super.isLoaded() && this.isPositionFileLoaded && this.isQuestionFileLoaded;
	}
	
	public ArrayList<GraphUserStudyQuestion> getQuestions() {
		return questions;
	}
	private void loadPositions(String path) {

        ArrayList<String> nodes = graph.getNodes();

        try {
            FileInputStream fstream = new FileInputStream(path);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String s;
            while ((s = br.readLine()) != null) {
                s = s.trim();

                String[] split = s.split("\t");

                if (split.length < 2) {
                    continue;
                }

                int index = nodes.indexOf(split[0].trim());
                if (index < 0) {
                    String s2 = split[0].trim().toLowerCase();
                    for (int i = 0; i < nodes.size(); i++) {
                        String s1 = nodes.get(i).toLowerCase();
                        if (s1.equals(s2)) {
                            index = i;
                            break;
                        }
                    }
                    if (index < 0) {
                        continue;
                    }
                }

                String[] poss = split[1].split(",");
                double x = Double.parseDouble(poss[0]);
                double y = Double.parseDouble(poss[1]);

                //x = x*1.335;
                //y = -y*1.335;
                Point point = new Point((int)x, (int)y);
                this.vertexPositions.add( point);
             }

            in.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
		
	}  
	public ArrayList<Point> getVertexPositions() {
		return vertexPositions;
	}
}
