package brain;

import perspectives.base.DataSource;
import perspectives.base.DataSourceFactory;


public class BrainDataFactory extends DataSourceFactory{

	@Override
	public DataSource create(String name) {
		return new BrainData(name);
		
	}

	@Override
	public String creatorType() {
		// TODO Auto-generated method stub
		return "BrainData";
	}

}
