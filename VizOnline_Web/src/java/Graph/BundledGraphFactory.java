package Graph;


import perspectives.Viewer;

public class BundledGraphFactory extends GraphViewerFactory{
	public Viewer create(String name) {
		if (this.isAllDataPresent())
			return new BundledGraph(name, (GraphData)this.getData().get(0));
		return null;
	}
	
	@Override
	public String creatorType() {
		// TODO Auto-generated method stub
		return "BundledGraphViewer";
	}
}