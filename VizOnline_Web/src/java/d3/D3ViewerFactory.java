package d3;

import Graph.GraphData;
import data.TableData;
import perspectives.Viewer;
import perspectives.ViewerFactory;


public class D3ViewerFactory extends ViewerFactory {
        public static final String CREATOR_TYPE ="D3 Viewer";
    

	@Override
	public String creatorType() {
		// TODO Auto-generated method stub
		return CREATOR_TYPE;
	}

	@Override
	public Viewer create(String name) {
		if (this.isAllDataPresent()) {
                return new D3Viewer(name, (GraphData)this.getData().get(0));
            }
		return null;
	}

    @Override
    public RequiredData requiredData() {
        RequiredData rd = new RequiredData("GraphData","1");
        return rd;
    }
	
}
