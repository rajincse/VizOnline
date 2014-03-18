

import perspectives.base.Environment;
import perspectives.graph.GraphDataFactory;
import perspectives.graph.GraphViewerFactory;
import perspectives.web.InitServlet;

public class MyInitServlet extends InitServlet{

	@Override
	public void environmentInit(Environment e) {
            e.registerDataSourceFactory(new GraphDataFactory());
            e.registerViewerFactory(new GraphViewerFactory());
            
	}

}
