import perspectives.base.Viewer;
import perspectives.base.ViewerFactory;
import perspectives.base.ViewerFactory.RequiredData;
import perspectives.graph.GraphData;
import perspectives.graph.GraphViewer;


public class PerformanceViewerFactory  extends ViewerFactory {

	public RequiredData requiredData() {
		
		RequiredData rd = new RequiredData("GraphData","1");
		return rd;
	}

	@Override
	public String creatorType() {
		// TODO Auto-generated method stub
		return "PerformanceTest";
	}

	@Override
	public Viewer create(String name) {
		if (this.isAllDataPresent())
			return new PerformanceTestViewer(name, (GraphData)this.getData().get(0));
		return null;
	}
	
}
