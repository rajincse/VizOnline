

import perspectives.base.Environment;
import perspectives.web.InitServlet;
import userstudy.graph.GraphUserStudyDataFactory;
import userstudy.graph.GraphUserStudyViewerFactory;

public class MyInitServlet extends InitServlet{

	@Override
	public void environmentInit(Environment e) {
            e.registerDataSourceFactory(new GraphUserStudyDataFactory());
            e.registerViewerFactory(new GraphUserStudyViewerFactory());
	}

}
