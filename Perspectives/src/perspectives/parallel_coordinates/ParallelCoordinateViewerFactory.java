package perspectives.parallel_coordinates;
import java.util.Vector;

import perspectives.base.Viewer;
import perspectives.base.ViewerFactory;
import perspectives.util.TableData;
/**
 *
 * @author mershack
 */
public class ParallelCoordinateViewerFactory extends ViewerFactory {
   @Override
       public RequiredData requiredData() {
		
		RequiredData rd = new RequiredData("TableData","1");
		return rd;
	}
     
    
   
  @Override
	public String creatorType() {
		// TODO Auto-generated method stub
		return "Parallel Coordinates";
	}
  
  @Override
	public Viewer create(String name) {
		// TODO Auto-generated method stub
		if (this.isAllDataPresent()){
			return new ParallelCoordViewer(name, (TableData) this.getData().get(0));
                }
		return null;
	}
}



