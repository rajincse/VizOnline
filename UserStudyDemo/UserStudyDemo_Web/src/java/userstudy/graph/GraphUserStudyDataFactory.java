package userstudy.graph;

import perspectives.base.DataSource;
import perspectives.base.DataSourceFactory;

public class GraphUserStudyDataFactory extends DataSourceFactory{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7864406921138304310L;

	@Override
	public DataSource create(String name) {
		// TODO Auto-generated method stub
		return new GraphUserStudyData(name);
	}

	@Override
	public String creatorType() {
		// TODO Auto-generated method stub
		return "GraphUserStudyData";
	}

}
