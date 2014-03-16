package userstudy;

import perspectives.base.Environment;
import perspectives.graph.GraphDataFactory;
import userstudy.graph.GraphUserStudyDataFactory;
import userstudy.graph.GraphUserStudyViewerFactory;


public class UserStudyDemoMainClass {
	public static void main(String[] args)
	{
		Environment e = new Environment(false);
		
//		e.registerDataSourceFactory(new UserStudyDataFactory());
//		e.registerViewerFactory(new UserStudyViewerFactory());
		e.registerDataSourceFactory(new GraphUserStudyDataFactory());
		e.registerViewerFactory(new GraphUserStudyViewerFactory());
	}
}
