

import perspectives.base.Environment;
import perspectives.base.Viewer;
import perspectives.properties.PFileInput;
import perspectives.properties.POptions;
import perspectives.web.InitServlet;
import userstudy.graph.GraphUserStudyData;
import userstudy.graph.GraphUserStudyDataFactory;
import userstudy.graph.GraphUserStudyViewer;
import userstudy.graph.GraphUserStudyViewerFactory;

public class MyInitServlet extends InitServlet{
	public static final String FILEPATH_EDGELIST ="PerspectivesUserStudy/data2/EdgeList.txt";
	public static final String FILEPATH_POSITION ="PerspectivesUserStudy/data2/VertexList.txt";
	public static final String FILEPATH_QUESTION ="PerspectivesUserStudy/data2/UserStudyData.xml";
	
//	public static final String FILEPATH_EDGELIST ="PerspectivesUserStudy/BigGraph/edgelist.txt";
//	public static final String FILEPATH_POSITION ="PerspectivesUserStudy/BigGraph/PositionRajin.txt";
//	public static final String FILEPATH_QUESTION ="PerspectivesUserStudy/BigGraph/UserStudyData.xml";
        
	@Override
	public void environmentInit(Environment e) {
            final MyInitServlet thisf = this;
            e.registerDataSourceFactory(new GraphUserStudyDataFactory());
            e.registerViewerFactory(new GraphUserStudyViewerFactory()
                    {
                        @Override
                        public Viewer create(String name) {
                                if (this.isAllDataPresent())
                                {
                                    String resultDirectoryPath = thisf.getServletContext().getRealPath("/WEB-INF/"+GraphUserStudyViewer.RESULT_DIR_PATH);
                                    return new GraphUserStudyViewer(name, (GraphUserStudyData)this.getData().get(0), resultDirectoryPath);
                                }
                                    
                                return null;
                        }
                    }
                );
            
//            System.out.println("Viewer and Data are registered");
//            GraphUserStudyData data = new GraphUserStudyData("Graph User Study data");
//
//            POptions format = (POptions)data.getProperty("Format").getValue(); 
//            format.selectedIndex =1;
//            data.getProperty("Format").setValue(format);
//
//            PFileInput graphFile = (PFileInput)data.getProperty("Graph File").getValue();
//            graphFile.path =  getServletContext().getRealPath("/WEB-INF/"+FILEPATH_EDGELIST);
//            data.getProperty("Graph File").setValue(graphFile);
//            
//            System.out.println("Trying to load:"+graphFile.path );
//            while(!data.isLoaded())
//            {
//
//            }
//            System.out.println("Graph is loaded");
//
//            PFileInput positionFile = (PFileInput)data.getProperty(GraphUserStudyData.PROPERTY_LOAD_POSITION).getValue();
//            positionFile.path = getServletContext().getRealPath("/WEB-INF/"+FILEPATH_POSITION);
//            data.getProperty(GraphUserStudyData.PROPERTY_LOAD_POSITION).setValue(positionFile);
//            System.out.println("Trying to load:"+positionFile.path );
//            while(!data.isPositionLoaded())
//            {
//
//            }
//            System.out.println("Position is loaded");
//            PFileInput questionFile = (PFileInput)data.getProperty(GraphUserStudyData.PROPERTY_LOAD_QUESTION).getValue();
//            questionFile.path = getServletContext().getRealPath("/WEB-INF/"+FILEPATH_QUESTION);
//            data.getProperty(GraphUserStudyData.PROPERTY_LOAD_QUESTION).setValue(questionFile);
//
//
//            System.out.println("Trying to load:"+questionFile.path );
//            while (!data.isAllDataLoaded())
//            {
////			System.out.println("Loading data");
//            }
//            System.out.println("all data loaded");
//            e.addDataSource(data);
//
//            String resultDirectoryPath = getServletContext().getRealPath("/WEB-INF/"+GraphUserStudyViewer.RESULT_DIR_PATH);
//
//            GraphUserStudyViewer userStudyViewer = new GraphUserStudyViewer("GraphUserStudyViewer", data, resultDirectoryPath);
//
//            while(!userStudyViewer.isLoaded())
//            {
//                    //System.out.println(" viewer loading");
//            }
//
//            System.out.println("Are you done?");
//            e.addViewer(userStudyViewer);
	}

}
