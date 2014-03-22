import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import brain.BrainDataFactory;
import brain.BrainViewerFactory;

import perspectives.*;
import perspectives.base.Environment;
import perspectives.graph.BundledGraphFactory;
import perspectives.graph.GraphDataFactory;
import perspectives.graph.GraphViewerFactory;
import perspectives.parallel_coordinates.ParallelCoordinateViewerFactory;
import perspectives.util.TableDataFactory;


public class main {

	  public static void main(String[] args) {  
		  
		  System.out.println(args.length);
		  
		  Environment e = new Environment(false);
		  e.setLocalDataPath("c:/work/code/perspectives/data");
	    
	       
	     e.registerDataSourceFactory(new GraphDataFactory());
	      
	     e.registerDataSourceFactory(new TableDataFactory()); 
	      
	      e.registerViewerFactory(new GraphViewerFactory());
	      
	      e.registerViewerFactory(new BundledGraphFactory());
	      
	      e.registerViewerFactory(new ParallelCoordinateViewerFactory());
	      
	      e.registerDataSourceFactory(new BrainDataFactory());
	      e.registerViewerFactory(new BrainViewerFactory());
//	      
	      e.registerViewerFactory(new PerformanceViewerFactory());
	      
	      e.addViewer(new PerformanceTester("g"));
	      
	    //  e.registerDataSourceFactory(new NameDataFactory());
	    //  e.registerViewerFactory(new NameViewerFactory()); 
  
	  }
}
