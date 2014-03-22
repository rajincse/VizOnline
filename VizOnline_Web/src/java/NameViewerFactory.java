import perspectives.base.Viewer;
import perspectives.base.ViewerFactory;
import perspectives.base.ViewerFactory.RequiredData;
import perspectives.graph.GraphData;


public class NameViewerFactory  extends ViewerFactory {

	public RequiredData requiredData() {
		
		RequiredData rd = new RequiredData("NameData","1");
		return rd;
	}

	@Override
	public String creatorType() {
		// TODO Auto-generated method stub
		return "NameViewer";
	}

	@Override
	public Viewer create(String name) {
		if (this.isAllDataPresent())
			return new NameViewer(name, (NameData)this.getData().get(0));
		return null;
	}
	
}