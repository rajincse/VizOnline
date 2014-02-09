package brain;

import perspectives.DataSource;
import perspectives.DataSourceFactory;


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
