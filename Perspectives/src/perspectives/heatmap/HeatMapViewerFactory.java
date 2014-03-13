package perspectives.heatmap;

import perspectives.base.Viewer;
import perspectives.base.ViewerFactory;
import perspectives.util.TableData;


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
                else{
                    System.out.println("Heatmap data is Null");
                }
		return null;
	}

}
