package brain;

import perspectives.Viewer;
import perspectives.ViewerFactory.RequiredData;
import perspectives.ViewerFactory;


public class BrainStatsViewerFactory extends ViewerFactory{

	@Override
	public RequiredData requiredData() {
		RequiredData rd = new RequiredData("BrainData","1");
		return rd;
	}

	@Override
	public String creatorType() {
		return "Brain Stats Viewer";
	}

	@Override
	public Viewer create(String name) {
		if (this.isAllDataPresent())
			return new BrainStatsViewer(name, (BrainData)this.getData().get(0));
		return null;
	}
}
