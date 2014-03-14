package userstudy;

import perspectives.base.DataSource;
import perspectives.base.DataSourceFactory;

public class UserStudyDataFactory extends DataSourceFactory{

	@Override
	public DataSource create(String name) {
		// TODO Auto-generated method stub
		return new UserStudyData(name);
	}

	@Override
	public String creatorType() {
		// TODO Auto-generated method stub
		return "UserStudyData";
	}

}
