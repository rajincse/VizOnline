import java.awt.event.MouseEvent;


import perspectives.base.*;
import perspectives.graph.GraphDataFactory;
import perspectives.graph.GraphViewerFactory;
import brain.*;

public class main {

	  public static void main(String[] args) {  
		  
		  Environment e = new Environment(false);
	    
	       
	     e.registerDataSourceFactory(new GraphDataFactory());	      
	    //e.registerDataSourceFactory(new TableDataFactory()); 
	      
	     e.registerViewerFactory(new GraphViewerFactory());
		  
		  
	      
	     // e.registerViewerFactory(new BundledGraphFactory());
	      
	    //  e.registerViewerFactory(new ParallelCoordinateViewerFactory());
	      
	     e.registerDataSourceFactory(new BrainDataFactory());
	      e.registerViewerFactory(new BrainViewerFactory());
	    //  e.registerViewerFactory(new BrainStatsViewerFactory());
	      
	   //   e.registerViewerFactory(new PlanarProjectionViewerFactory());
	      
	    //  e.addViewer(new GazeAnalyzer("bla"));
	      
	   
	      
	      
	  }
}
