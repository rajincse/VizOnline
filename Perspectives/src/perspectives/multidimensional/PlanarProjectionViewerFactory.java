package perspectives.multidimensional;

import perspectives.base.Viewer;
import perspectives.base.ViewerFactory;
import perspectives.util.TableData;



public class PlanarProjectionViewerFactory extends ViewerFactory 
{	

		public RequiredData requiredData() {
			
			RequiredData rd = new RequiredData("TableData","1");
			return rd;
		}

		@Override
		public String creatorType() {
			// TODO Auto-generated method stub
			return "Planar Projection";
		}

		@Override
		public Viewer create(String name) {
			if (this.isAllDataPresent())
			{
				TableData t = (TableData)this.getData().get(0);
				return new PlanarProjectionViewer(name, t);
			}
			return null;
		}
}
