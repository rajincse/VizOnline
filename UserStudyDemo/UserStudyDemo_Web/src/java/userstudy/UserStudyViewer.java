package userstudy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;


import perspectives.base.DataSource;
import perspectives.base.Property;
import perspectives.properties.PFileOutput;
import perspectives.properties.POptions;
import perspectives.properties.PSignal;
import perspectives.properties.PString;
import perspectives.two_d.Viewer2D;


public class UserStudyViewer extends Viewer2D {
	private static final String PROPERTY_ANSWER ="Answer";
	private static final String PROPERTY_SUBMIT ="Submit";
	private static final String PROPERTY_SAVE_RESULT ="Save Result";

	private ArrayList<Question> questions ;
	private ArrayList<Boolean> isCorrectAnswers;
	private int currentIndex;
	private boolean isEndOfStudy;
	private int totalCorrect;
	public UserStudyViewer(String name, UserStudyData data) {
		super(name);
		this.questions = data.getQuestions();
		this.currentIndex =0;
		this.isCorrectAnswers = new ArrayList<Boolean>();
		this.isEndOfStudy = false;
		this.totalCorrect =0;
		try
		{
			final UserStudyViewer thisf = this;
			Property<POptions> answerProperty = new Property<POptions>(PROPERTY_ANSWER,new POptions(new String[]{"a","b","c","d"}))
					{
							@Override
							protected boolean updating(POptions newvalue) {
								// TODO Auto-generated method stub
								return super.updating(newvalue);
							}
					};
					
			this.addProperty(answerProperty);
			
			Property<PSignal> submitAnswerProperty = new Property<PSignal>(PROPERTY_SUBMIT, new PSignal())
					{
						@Override
						protected boolean updating(PSignal newvalue) {
							POptions options =(POptions)thisf.getProperty(PROPERTY_ANSWER).getValue(); 
							String answer = options.options[options.selectedIndex];
							
							Question q = thisf.questions.get(currentIndex);
							boolean isCorrect = answer.equalsIgnoreCase(q.getCorrectAnswer());
							thisf.isCorrectAnswers.add(isCorrect);
							thisf.currentIndex++;
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
		}catch (Exception e) {
            e.printStackTrace();
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
                br.write("Question "+(i+1)+" "+correctString+" correctAnswer:"+this.questions.get(i).getCorrectAnswer());
                br.newLine();
            }
            
            br.write("Total correct : "+totalCorrect+" / "+this.questions.size()+" ("+String.format("%.2f%%",totalCorrect * 100.0/this.questions.size() )+")");
            br.close();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
		this.getProperty(PROPERTY_SAVE_RESULT).setDisabled(false);		
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		int x=0;
		int y =0;
		int step =5;
		Font questionfont = new Font("Arial", Font.BOLD, 20);
		Font choiceFont = new Font("Arial", Font.PLAIN,20);
		Font resultfont = new Font("Arial", Font.BOLD, 20);
		if(this.questions != null && !this.questions.isEmpty() && this.isEndOfStudy)
		{
			g.setColor(Color.blue);
			g.setFont(resultfont);
			g.drawString("Total correct : "+totalCorrect+" / "+this.questions.size(), x, y);
			
		}
		else if(this.questions != null && !this.questions.isEmpty() && this.currentIndex< this.questions.size())
		{
			Question question = this.questions.get(this.currentIndex);
			
			
			g.setColor(Color.black);
			g.setFont(questionfont);
			g.drawString(question.getQuestionText(), x, y);
			
			g.setFont(choiceFont);
			y+= step;
			
			for(String index: question.getIndices())
			{
				String value = question.getChoiceList().get(index);
				g.drawString(index+") "+value, x, y);
				y+= step;
			}
		}
	}

	@Override
	public void simulate() {
		// TODO Auto-generated method stub
		
	}

}
