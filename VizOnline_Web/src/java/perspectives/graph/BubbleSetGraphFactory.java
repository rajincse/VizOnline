package perspectives.graph;

import perspectives.base.Viewer;

public class BubbleSetGraphFactory extends ClusterGraphViewerFactory{
	public Viewer create(String name) {
		if (this.isAllDataPresent())
			return new BubbleSetGraph(name, (GraphData)this.getData().get(0));
		return null;
	}
	
	@Override
	public String creatorType() {
		// TODO Auto-generated method stub
		return "BubbleGraphViewer";
	}
}
