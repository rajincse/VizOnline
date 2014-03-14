package userstudy;

import perspectives.base.Environment;


public class UserStudyDemoMainClass {
	public static void main(String[] args)
	{
		Environment e = new Environment(false);
		
		e.registerDataSourceFactory(new UserStudyDataFactory());
		e.registerViewerFactory(new UserStudyViewerFactory());
	}
}
