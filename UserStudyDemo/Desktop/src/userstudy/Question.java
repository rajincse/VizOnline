package userstudy;

import java.util.ArrayList;
import java.util.HashMap;

public class Question {
	public static final String TAG_NAME_QUESTION ="Question";
	public static final String TAG_NAME_TEXT ="text";
	public static final String TAG_NAME_CHOICE ="choice";
	public static final String TAG_NAME_CORRECT ="correct";
	
	private String questionText;
	private ArrayList<String> indices;
	private HashMap<String, String> choiceList;
	private String correctAnswer;
	
	
	public Question(String questionText,ArrayList<String> indices, HashMap<String, String> choiceList,String correctAnswer)	
	{
		this.questionText = questionText;
		this.indices = indices;
		this.choiceList = choiceList;
		this.correctAnswer = correctAnswer;
		
	}


	public String getQuestionText() {
		return questionText;
	}


	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}


	public HashMap<String, String> getChoiceList() {
		return choiceList;
	}


	public void setChoiceList(HashMap<String, String> choiceList) {
		this.choiceList = choiceList;
	}


	public String getCorrectAnswer() {
		return correctAnswer;
	}


	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}


	public ArrayList<String> getIndices() {
		return indices;
	}


	public void setIndices(ArrayList<String> indices) {
		this.indices = indices;
	}


	
	
}
