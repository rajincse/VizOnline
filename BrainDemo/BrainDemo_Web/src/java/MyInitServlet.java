
import brain.BrainDataFactory;
import brain.BrainStatsD3ViewerFactory;
import brain.BrainViewerFactory;
import d3.GraphD3ViewerFactory;
import perspectives.base.Environment;
import perspectives.graph.GraphDataFactory;
import perspectives.web.InitServlet;

public class MyInitServlet extends InitServlet{

	@Override
	public void environmentInit(Environment e) {
            e.registerDataSourceFactory(new BrainDataFactory());
            e.registerViewerFactory(new BrainViewerFactory());
            e.registerViewerFactory(new BrainStatsD3ViewerFactory());
            
	}

}
