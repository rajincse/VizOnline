package userstudy;

import perspectives.base.Viewer;
import perspectives.base.ViewerFactory;


public class UserStudyViewerFactory extends ViewerFactory{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7209011208217946809L;
	public static final String CREATOR_TYPE ="UserStudyViewerFactory";
	

	@Override
	public String creatorType() {
		// TODO Auto-generated method stub
		return CREATOR_TYPE;
	}

	@Override
	public Viewer create(String name) {
		if (this.isAllDataPresent()) {
             return new UserStudyViewer(name, (UserStudyData)this.getData().get(0));
			 
            }
		return null;
	}

    @Override
    public ViewerFactory.RequiredData requiredData() {
        ViewerFactory.RequiredData rd = new ViewerFactory.RequiredData("UserStudyData","1");
        return rd;
    }

}
