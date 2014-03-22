import perspectives.base.Environment;
import perspectives.graph.GraphData;
import perspectives.graph.GraphDataFactory;
import perspectives.graph.GraphViewerFactory;
import perspectives.properties.PFileInput;
import perspectives.web.InitServlet;


public class MyInitServlet extends InitServlet{

	@Override
	public void environmentInit(Environment e) {

			e.registerDataSourceFactory(new GraphDataFactory());
			e.registerViewerFactory(new PerformanceViewerFactory());
			e.registerViewerFactory(new GraphViewerFactory());
			
			System.out.println("here");
			GraphData d = new GraphData("graphdata");
			PFileInput f = new PFileInput();
			f.path = getServletContext().getRealPath("/WEB-INF/Uploads/edgelist.txt");
			 
			System.out.println("here2");
			e.addDataSource(d, true);
			d.getProperty("Graph File").setValue(f);
			System.out.println("here3");
			
		
	}

}
