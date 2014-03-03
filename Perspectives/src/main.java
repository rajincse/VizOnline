import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import brain.BrainDataFactory;
import brain.BrainStatsViewerFactory;
import brain.BrainViewerFactory;

import multidimensional.PlanarProjectionViewerFactory;

import Graph.BubbleSetGraphFactory;
import Graph.BundledGraphFactory;

import Graph.GraphData;
import Graph.GraphDataFactory;
import Graph.GraphViewer;
import Graph.GraphViewerFactory;

import ParallelCoord.ParallelCoordinateViewerFactory;

import perspectives.*;

import properties.POptions;


import stimulusgen.GazeAnalyzer;
import tree.*;


import data.*;


public class main {

	  public static void main(String[] args) {  
		  
		  Environment e = new Environment(false);
	    
	       
	     e.registerDataSourceFactory(new GraphDataFactory());
	      
	     e.registerDataSourceFactory(new TableDataFactory()); 
	      
	      e.registerViewerFactory(new GraphViewerFactory());
	      
	      e.registerViewerFactory(new BundledGraphFactory());
	      
	      e.registerViewerFactory(new ParallelCoordinateViewerFactory());
	      
	      e.registerDataSourceFactory(new BrainDataFactory());
	      e.registerViewerFactory(new BrainViewerFactory());
	      e.registerViewerFactory(new BrainStatsViewerFactory());
	      
	      e.addViewer(new GazeAnalyzer("bla"));
	      
	      
	  }
}
