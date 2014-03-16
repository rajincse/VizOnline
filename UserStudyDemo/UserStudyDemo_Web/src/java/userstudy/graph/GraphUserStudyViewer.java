package userstudy.graph;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.AllPermission;
import java.util.ArrayList;
import java.util.Date;

import perspectives.base.Property;
import perspectives.base.Task;
import perspectives.graph.GraphData;
import perspectives.graph.GraphViewer;
import perspectives.properties.PFileInput;
import perspectives.properties.PFileOutput;
import perspectives.properties.POptions;
import perspectives.properties.PSignal;
import perspectives.properties.PText;
import userstudy.Question;
import userstudy.UserStudyDemoMainClass;
import userstudy.UserStudyViewer;

public class GraphUserStudyViewer extends GraphViewer{
	public static final String PROPERTY_QUESTION ="Q:";
	public static final String PROPERTY_ANSWER ="Answer";
	public static final String PROPERTY_NEXT ="Next";
	public static final String PROPERTY_REFRESH ="Refresh";
	
	public static final String RESULT_DIR_PATH = "PerspectivesUserStudy/Result";
	
	
	private ArrayList<GraphUserStudyQuestion> questions ;
	private ArrayList<Boolean> isCorrectAnswers;
	private int currentIndex;
	private boolean isEndOfStudy;
	private int totalCorrect;
	
	private GraphUserStudyData data;
	private String resultDirectoryPath;
	
	private boolean isPositionLoaded =false;
	private boolean isQuestionLoaded =false;
	
	public GraphUserStudyViewer(String name, GraphUserStudyData data)
	{
		this(name, data, RESULT_DIR_PATH);
	}
		
	public GraphUserStudyViewer(String name, GraphUserStudyData data, String resultDirectoryPath) {
		
		
		super(name, data);
		this.questions = data.getQuestions();
		this.data = data;
		this.currentIndex =0;
		this.isCorrectAnswers = new ArrayList<Boolean>();
		this.isEndOfStudy = false;
		this.totalCorrect =0;
		this.resultDirectoryPath = resultDirectoryPath;
		try
		{
			final GraphUserStudyViewer thisf = this;
			Property<PText> questionProperty = new Property<PText>(PROPERTY_QUESTION, new PText(""));
			this.addProperty(questionProperty);
			
			Property<POptions> answerProperty = new Property<POptions>(PROPERTY_ANSWER,new POptions(new String[]{"1","2","3","4","5","6"}))
					{
							@Override
							protected boolean updating(POptions newvalue) {
								// TODO Auto-generated method stub
								return super.updating(newvalue);
							}
					};
					
			this.addProperty(answerProperty);
			
			Property<PSignal> nextProperty = new Property<PSignal>(PROPERTY_NEXT, new PSignal())
					{
						@Override
						protected boolean updating(PSignal newvalue) {
							POptions options =(POptions)thisf.getProperty(PROPERTY_ANSWER).getValue(); 
							String answer = options.options[options.selectedIndex];
							if(thisf.currentIndex < thisf.questions.size())
							{
								GraphUserStudyQuestion q = thisf.questions.get(currentIndex);
								boolean isCorrect = answer.equalsIgnoreCase(""+q.getCorrect());
								thisf.isCorrectAnswers.add(isCorrect);
								thisf.currentIndex++;
								thisf.showUserStudy();
								if(thisf.currentIndex ==thisf.questions.size())
								{
									endOfTest();
								}
							}
							
							thisf.requestRender();
							return true;
						}
					};
			
			this.addProperty(nextProperty);
			
			Property<PSignal> refresh = new Property<PSignal>(PROPERTY_REFRESH, new PSignal())
					{
						@Override
						protected boolean updating(PSignal newvalue) {
							thisf.showUserStudy();
							thisf.requestRender();
							return true;
						}
					};
			
			this.addProperty(refresh);
			
			
			Task t = new Task("draw Position") {
				
				@Override
				public void task() {
					while(thisf.drawer == null)
					{
//						System.out.println("Drawer null");
					}
					thisf.drawPosition();
					thisf.showUserStudy();
					
					thisf.requestRender();
					
					done();
					thisf.isPositionLoaded = true;
					System.out.println("positions Done");
				}
			};
			t.indeterminate = true;
			t.blocking = true;
			thisf.startTask(t);
		}catch (Exception e) {
            e.printStackTrace();
        }
		
	}
	
	public boolean isLoaded()
	{
		return this.isPositionLoaded && this.isQuestionLoaded;
	}
	@Override
	protected void setTooltipContent(int index) {
		// TODO Auto-generated method stub
		this.setToolTipText(""+index);
	}
	private void endOfTest()
	{
		this.totalCorrect =0;
		for(Boolean b: this.isCorrectAnswers )
		{
			if(b)
			{
				this.totalCorrect++;
			}
		}
		this.isEndOfStudy = true;
		String result ="Total correct : "+totalCorrect+" / "+this.questions.size()+" ("+String.format("%.2f%%",totalCorrect * 100.0/this.questions.size() )+")";
		System.out.println(result);
		
		String outputFilePath = this.resultDirectoryPath+"/"+new Date().toString().replace(" ", "_").replace(":","")+".txt";
		this.saveResult(outputFilePath);
		
		
	}
	
	
	public void showUserStudy()
	{
		if(currentIndex < this.questions.size())
		{
			GraphUserStudyQuestion question =this.questions.get(currentIndex);
			PText questionText =(PText) this.getProperty(PROPERTY_QUESTION).getValue();
			questionText.setValue(question.getQuestionText());
			this.getProperty(PROPERTY_QUESTION).setValue(questionText);
			
			int x1 = (int)this.drawer.getX(question.getSource());
			int y1 = (int)this.drawer.getY(question.getSource());
			
			int x2 = (int)this.drawer.getX(question.getTarget());
			int y2 = (int)this.drawer.getY(question.getTarget());
			
			
			int tx = -(x1+x2)/2 + 600;
			int ty = -(y1+y2)/2 + 400;
			
			
			this.setTranslation(tx,ty);
			this.requestRender();
	
			this.isQuestionLoaded = true;
			System.out.println("current index :"+this.currentIndex+" translate :"+tx+", "+ty);

			this.requestRender();
		}
		
	}
	
	private void saveResult(String filePath)
	{
        try {
            FileWriter fstream;
            File outputFile = new File(filePath);
            fstream = new FileWriter(outputFile);

            BufferedWriter br = new BufferedWriter(fstream);
            
            for (int i = 0; i < questions.size(); i++) {
            	Boolean isCorrect = this.isCorrectAnswers.get(i);
            	
            	String correctString = isCorrect?"Correct":"Incorrect";
                br.write("Question "+(i+1)+" "+correctString+" correctAnswer:"+this.questions.get(i).getCorrect());
                br.newLine();
            }
            
            br.write("Total correct : "+totalCorrect+" / "+this.questions.size()+" ("+String.format("%.2f%%",totalCorrect * 100.0/this.questions.size() )+")");
            br.close();
            System.out.println("Result saved to "+outputFile.getAbsolutePath());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	@Override
	public void renderNodes(Graphics2D g, boolean[] sel, boolean[] hov) {
		// TODO Auto-generated method stub
		super.renderNodes(g, sel, hov);
		if(this.currentIndex< this.questions.size())
		{
			this.renderNode(this.questions.get(currentIndex).getSource(), true, false, g);
			this.renderNode(this.questions.get(currentIndex).getTarget(), true, false, g);
		}
		
	}
	private void  drawPosition()
	{
		System.out.println("Drawing positions: "+data.graph.getNodes().size()+", "+data.getVertexPositions().size());
		for(int i=0;i<data.graph.getNodes().size();i++)
		{
			Point point = data.getVertexPositions().get(i);
			drawer.setX(i, point.x);
            drawer.setY(i, point.y);

            ovals.get(i).x =  point.x;
            ovals.get(i).y = point.y;
            ovals.get(i).setAnchor(-5, -5);
		}
		System.out.println("Drawn positions");
		this.requestRender();
	}
}

