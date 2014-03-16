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
import perspectives.properties.PFileOutput;
import perspectives.properties.POptions;
import perspectives.properties.PSignal;
import userstudy.Question;
import userstudy.UserStudyViewer;

public class GraphUserStudyViewer extends GraphViewer{
	private static final String PROPERTY_ANSWER ="Answer";
	private static final String PROPERTY_SUBMIT ="Submit";
	private static final String PROPERTY_SAVE_RESULT ="Save Result";
	
	private ArrayList<GraphUserStudyQuestion> questions ;
	private ArrayList<Boolean> isCorrectAnswers;
	private int currentIndex;
	private boolean isEndOfStudy;
	private int totalCorrect;
	
	private GraphUserStudyData data;
	
	public GraphUserStudyViewer(String name, GraphUserStudyData data) {
		super(name, data);
		this.questions = data.getQuestions();
		this.data = data;
		this.currentIndex =0;
		this.isCorrectAnswers = new ArrayList<Boolean>();
		this.isEndOfStudy = false;
		this.totalCorrect =0;
		
		try
		{
			final GraphUserStudyViewer thisf = this;
			
			
			
			Property<PSignal> submitAnswerProperty = new Property<PSignal>(PROPERTY_SUBMIT, new PSignal())
					{
						@Override
						protected boolean updating(PSignal newvalue) {
                                                        
							thisf.currentIndex++;
                                                        System.out.println("Current Index:"+thisf.currentIndex);
							thisf.showUserStudy();
							if(thisf.currentIndex ==thisf.questions.size())
							{
								endOfTest();
							}
							thisf.requestRender();
							return true;
						}
					};
			
			this.addProperty(submitAnswerProperty);
			Property<PFileOutput> saveResultProperty = new Property<PFileOutput>(PROPERTY_SAVE_RESULT, new PFileOutput())
					{
						@Override
						protected boolean updating(PFileOutput newvalue) {
							// TODO Auto-generated method stub
							thisf.saveResult(newvalue.path);
							return true;
						}
					};
			saveResultProperty.setDisabled(true);
			this.addProperty(saveResultProperty);
			
			Task t = new Task("draw Position") {
				
				@Override
				public void task() {
					while(drawer == null)
					{
						System.out.println("Drawer null");
					}
					thisf.drawPosition();
					thisf.showUserStudy();
					done();
				}
			};
			t.indeterminate = true;
			t.blocking = true;
			thisf.startTask(t);
			
		}catch (Exception e) {
            e.printStackTrace();
        }
	}
	@Override
	protected void setTooltipContent(int index) {
		// TODO Auto-generated method stub
		this.setToolTipText(""+index);
	}
	private void endOfTest()
	{
		this.currentIndex =0;
	}
	
	
	public void showUserStudy()
	{
		if(currentIndex < this.questions.size())
		{
			GraphUserStudyQuestion question =this.questions.get(currentIndex);
			
			
			int x1 = (int)this.drawer.getX(question.getSource());
			int y1 = (int)this.drawer.getY(question.getSource());
			
			int x2 = (int)this.drawer.getX(question.getTarget());
			int y2 = (int)this.drawer.getY(question.getTarget());
			
			System.out.println(question.getSource()+"=>("+x1+","+y1+")");
			System.out.println(question.getTarget()+"=>("+x2+","+y2+")");
			int tx = -(x1+x2)/2 + 600;
			int ty = -(y1+y2)/2 + 400;
			
			System.out.println("tx,ty =>"+tx+", "+ty);
			this.setTranslation(tx,ty);
			//this.setTranslation(-x1+600, -y1+400);
		}
		
	}
	
	private void saveResult(String filePath)
	{
        try {
            FileWriter fstream;

            fstream = new FileWriter(new File(filePath));

            BufferedWriter br = new BufferedWriter(fstream);
            
            for (int i = 0; i < questions.size(); i++) {
            	Boolean isCorrect = this.isCorrectAnswers.get(i);
            	
            	String correctString = isCorrect?"Correct":"Incorrect";
                br.write("Question "+(i+1)+" "+correctString);//+" correctAnswer:"+this.questions.get(i).getCorrectAnswer());
                br.newLine();
            }
            
            br.write("Total correct : "+totalCorrect+" / "+this.questions.size()+" ("+String.format("%.2f%%",totalCorrect * 100.0/this.questions.size() )+")");
            br.close();


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
		
		for(int i=0;i<data.graph.getNodes().size();i++)
		{
			Point point = data.getVertexPositions().get(i);
			drawer.setX(i, point.x);
            drawer.setY(i, point.y);

            ovals.get(i).x =  point.x;
            ovals.get(i).y = point.y;
            ovals.get(i).setAnchor(-5, -5);
		}
		this.requestRender();
	}
}

