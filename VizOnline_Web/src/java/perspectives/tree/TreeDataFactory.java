package perspectives.tree;

import perspectives.base.DataSource;
import perspectives.base.DataSourceFactory;

public class TreeDataFactory extends DataSourceFactory
{

	@Override
	public DataSource create(String name) {
		
		return new TreeData(name);
	}

	@Override
	public String creatorType() {
		// TODO Auto-generated method stub
		return "TreeData";
	}

}
