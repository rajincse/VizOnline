package userstudy;

import perspectives.base.Environment;
import perspectives.base.Property;
import perspectives.graph.GraphDataFactory;
import perspectives.graph.GraphViewerFactory;
import perspectives.properties.PFileInput;
import perspectives.properties.POptions;
import userstudy.graph.GraphUserStudyData;
import userstudy.graph.GraphUserStudyDataFactory;
import userstudy.graph.GraphUserStudyViewer;
import userstudy.graph.GraphUserStudyViewerFactory;


public class UserStudyDemoMainClass {
	
	
//	public static final String FILEPATH_EDGELIST ="PerspectivesUserStudy/data2/EdgeList.txt";
//	public static final String FILEPATH_POSITION ="PerspectivesUserStudy/data2/VertexList.txt";
//	public static final String FILEPATH_QUESTION ="PerspectivesUserStudy/data2/UserStudyData.xml";
	
	public static final String FILEPATH_EDGELIST ="PerspectivesUserStudy/BigGraph/edgelist.txt";
	public static final String FILEPATH_POSITION ="PerspectivesUserStudy/BigGraph/PositionRajin.txt";
	public static final String FILEPATH_QUESTION ="PerspectivesUserStudy/BigGraph/UserStudyData.xml";
	
	public static void main(String[] args)
	{
		Environment e = new Environment(false);
		e.registerDataSourceFactory(new GraphUserStudyDataFactory());
		e.registerViewerFactory(new GraphUserStudyViewerFactory());
		e.registerDataSourceFactory(new GraphDataFactory());
		e.registerViewerFactory(new GraphViewerFactory());
		
		GraphUserStudyData data = new GraphUserStudyData("Graph User Study data");
		
		POptions format = (POptions)data.getProperty("Format").getValue(); 
		format.selectedIndex =1;
		data.getProperty("Format").setValue(format);
		
		PFileInput graphFile = (PFileInput)data.getProperty("Graph File").getValue();
		graphFile.path = FILEPATH_EDGELIST;
		data.getProperty("Graph File").setValue(graphFile);
		
		while(!data.isLoaded())
		{
			
		}
		
		PFileInput positionFile = (PFileInput)data.getProperty(GraphUserStudyData.PROPERTY_LOAD_POSITION).getValue();
		positionFile.path = FILEPATH_POSITION;
		data.getProperty(GraphUserStudyData.PROPERTY_LOAD_POSITION).setValue(positionFile);
		
		while(!data.isPositionLoaded())
		{
			
		}
		PFileInput questionFile = (PFileInput)data.getProperty(GraphUserStudyData.PROPERTY_LOAD_QUESTION).getValue();
		questionFile.path = FILEPATH_QUESTION;
		data.getProperty(GraphUserStudyData.PROPERTY_LOAD_QUESTION).setValue(questionFile);
		
		
		
		while (!data.isAllDataLoaded())
		{
//			System.out.println("Loading data");
		}
		
		e.addDataSource(data);
		
		
		 
		GraphUserStudyViewer userStudyViewer = new GraphUserStudyViewer("GraphUserStudyViewer", data, GraphUserStudyViewer.RESULT_DIR_PATH);
		
		while(!userStudyViewer.isLoaded())
		{
			//System.out.println(" viewer loading");
		}
		
		System.out.println("Are you done?");
		e.addViewer(userStudyViewer);
	}
}
