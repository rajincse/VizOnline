import java.awt.event.MouseEvent;


import perspectives.base.*;
import perspectives.graph.GraphDataFactory;
import perspectives.graph.GraphViewerFactory;
import brain.*;

public class main {

	  public static void main(String[] args) {  
		  
		  Environment e = new Environment(false);
	    
	      
	      
	      
	     e.registerDataSourceFactory(new BrainDataFactory());
	     e.registerViewerFactory(new BrainViewerFactory());
	    
	     e.registerDataSourceFactory(new BrainDataModifierFactory());
	     
	      
	  }
}
