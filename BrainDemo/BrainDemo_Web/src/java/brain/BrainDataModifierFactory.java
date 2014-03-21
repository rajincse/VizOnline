package brain;

import perspectives.base.DataSource;
import perspectives.base.DataSourceFactory;

public class BrainDataModifierFactory extends DataSourceFactory{
	@Override
	public DataSource create(String name) {
		return new BrainDataModifier(name);
		
	}

	@Override
	public String creatorType() {
		// TODO Auto-generated method stub
		return "BrainDataModifier";
	}
}
