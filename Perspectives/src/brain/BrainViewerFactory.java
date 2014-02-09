package brain;


import Graph.GraphData;
import Graph.GraphViewer;
import perspectives.Viewer;
import perspectives.ViewerFactory;
import perspectives.ViewerFactory.RequiredData;


public class BrainViewerFactory extends ViewerFactory {

	@Override
	public RequiredData requiredData() {
		RequiredData rd = new RequiredData("BrainData","1");
		return rd;
	}

	@Override
	public String creatorType() {
		return "Brain Viewer";
	}

	@Override
	public Viewer create(String name) {
		if (this.isAllDataPresent())
			return new BrainViewer(name, (BrainData)this.getData().get(0));
		return null;
	}

}
