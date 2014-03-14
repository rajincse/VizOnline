package d3;

import perspectives.base.Viewer;
import perspectives.base.ViewerFactory;
import perspectives.graph.GraphData;


public class GraphD3ViewerFactory extends ViewerFactory {
        public static final String CREATOR_TYPE ="GraphD3Viewer";
    

	@Override
	public String creatorType() {
		// TODO Auto-generated method stub
		return CREATOR_TYPE;
	}

	@Override
	public Viewer create(String name) {
		if (this.isAllDataPresent()) {
                return new GraphD3Viewer(name, (GraphData)this.getData().get(0));
            }
		return null;
	}

    @Override
    public RequiredData requiredData() {
        RequiredData rd = new RequiredData("GraphData","1");
        return rd;
    }
	
}
