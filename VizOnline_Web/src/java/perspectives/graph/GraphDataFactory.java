package perspectives.graph;

import perspectives.base.DataSource;
import perspectives.base.DataSourceFactory;

public class GraphDataFactory extends DataSourceFactory{

	@Override
	public DataSource create(String name) {
		return new GraphData(name);
	}

	@Override
	public String creatorType() {
		// TODO Auto-generated method stub
		return "Graph";
	}

}

