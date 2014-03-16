package userstudy.graph;

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

import userstudy.Question;

public class GraphUserStudyQuestion {
	public static final String TAG_NAME_QUESTION ="Question";
	public static final String ATTRIBUTE_TYPE ="type";
	public static final String ATTRIBUTE_TYPE_VALUE ="Path";
	public static final String TAG_NAME_SOURCE ="source";
	public static final String TAG_NAME_TARGET ="target";
	public static final String ATTRIBUTE_NODE_ID ="nodeid";
	
	private int source;
	private int target;
	
	public GraphUserStudyQuestion(int source, int target)
	{
		this.source = source;
		this.target = target;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}
	
	public static ArrayList<GraphUserStudyQuestion> fromFile(String filePath) 
			throws ParserConfigurationException, SAXException, IOException
	{
		File f = new File(filePath);
		ArrayList<GraphUserStudyQuestion> questions= new ArrayList<GraphUserStudyQuestion>();

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(f);
		doc.getDocumentElement().normalize();
	
		dBuilder = dbFactory.newDocumentBuilder();
		
		NodeList questionList = doc.getElementsByTagName(Question.TAG_NAME_QUESTION);
		
		for(int i=0;i<questionList.getLength();i++)
		{
			Element questionElement = (Element) questionList.item(i);
			if(questionElement.getAttribute(ATTRIBUTE_TYPE).equals(ATTRIBUTE_TYPE_VALUE))
			{
				int source = Integer.parseInt( ((Element)questionElement.getElementsByTagName(TAG_NAME_SOURCE).item(0)).getAttribute(ATTRIBUTE_NODE_ID));
				int target = Integer.parseInt( ((Element)questionElement.getElementsByTagName(TAG_NAME_TARGET).item(0)).getAttribute(ATTRIBUTE_NODE_ID));
				GraphUserStudyQuestion question = new GraphUserStudyQuestion(source, target);
				questions.add(question);
			}
			
		}
		
		return questions;
	}
}
