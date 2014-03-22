import perspectives.base.DataSource;
import perspectives.base.DataSourceFactory;
import perspectives.graph.GraphData;


public class NameDataFactory extends DataSourceFactory{

	@Override
	public DataSource create(String name) {
		return new NameData(name);
	}

	@Override
	public String creatorType() {
		// TODO Auto-generated method stub
		return "NameData";
	}

}

