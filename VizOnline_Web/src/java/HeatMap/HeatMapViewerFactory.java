package HeatMap;

import java.util.Vector;

import perspectives.Viewer;
import perspectives.ViewerFactory;
import data.*;


public class HeatMapViewerFactory extends ViewerFactory {
     public RequiredData requiredData() {
		
		RequiredData rd = new RequiredData("TableData","1");
		return rd;
     
     }
     
    
   
  @Override
	public String creatorType() {
		// TODO Auto-generated method stub
		return "HeatMap";
	}
  
    @Override
	public Viewer create(String name) {
		// TODO Auto-generated method stub
		if (this.isAllDataPresent()){
			return new HeatMapViewer(name, (TableData)this.getData().get(0));
                }
		return null;
	}

}
