package userstudy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import perspectives.base.DataSource;
import perspectives.base.Property;
import perspectives.base.Task;
import perspectives.properties.PFileInput;
import perspectives.properties.PInteger;

public class UserStudyData extends DataSource{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1471449512478357871L;
	
	private ArrayList<Question> questions;
	
	
	public UserStudyData(String name) {
		super(name);
		try {
			PFileInput f = new PFileInput();
			f.dialogTitle = "Open UserStudy File";
			f.extensions = new String[]{"xml","*"};
			f.currentExtension = 0;
			
			final UserStudyData thisf = this;
			Property<PFileInput> p = new Property<PFileInput>("Load", f)
					{
					@Override
						public boolean updating(PFileInput newvalue)
						{
							final PFileInput newvaluef = newvalue;
							
							Task t = new Task("Loading...")
							{	
							
								@Override
								public void task() {
									fromFile(((PFileInput)newvaluef).path);
									thisf.removeProperty("Load");
									
									Property<PInteger> p = new Property<PInteger>("#Questions", new PInteger(thisf.questions.size()));
									p.setReadOnly(true);
									try {
										thisf.addProperty(p);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									loaded = true;
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
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void fromFile(String fileName)
	{
		try {
			File f = new File(fileName);
			this.questions = new ArrayList<Question>();
	
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(f);
			doc.getDocumentElement().normalize();
		
			dBuilder = dbFactory.newDocumentBuilder();
			
			NodeList questionList = doc.getElementsByTagName(Question.TAG_NAME_QUESTION);
			for(int i=0;i<questionList.getLength();i++)
			{
				Element question = (Element) questionList.item(i);
				String questionText = question.getElementsByTagName(Question.TAG_NAME_TEXT).item(0).getTextContent();
				NodeList choiceList = question.getElementsByTagName(Question.TAG_NAME_CHOICE);
				HashMap<String, String> choiceListValue = new HashMap<String, String>();
				ArrayList<String> indices = new ArrayList<String>();
				for(int j=0;j<choiceList.getLength();j++)
				{
					Element choice= (Element) choiceList.item(j);
					String choiceIndex = choice.getAttribute("name");
					String choiceText = choice.getTextContent();
					choiceListValue.put(choiceIndex, choiceText);
					indices.add(choiceIndex);
				}
				String correctAnswer =question.getElementsByTagName(Question.TAG_NAME_CORRECT).item(0).getTextContent();
				Question questionValue = new Question(questionText, indices, choiceListValue, correctAnswer);
				this.questions.add(questionValue);
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public ArrayList<Question> getQuestions() {
		return questions;
	}

	
	
}
