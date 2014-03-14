

import perspectives.base.Environment;
import perspectives.web.InitServlet;
import userstudy.UserStudyDataFactory;
import userstudy.UserStudyViewerFactory;

public class MyInitServlet extends InitServlet{

	@Override
	public void environmentInit(Environment e) {
            e.registerDataSourceFactory(new UserStudyDataFactory());
            e.registerViewerFactory(new UserStudyViewerFactory());
            
	}

}
