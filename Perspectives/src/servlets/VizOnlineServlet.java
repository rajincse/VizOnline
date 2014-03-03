package servlets;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;
import java.util.Hashtable;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Graph.GraphDataFactory;
import Graph.GraphViewerFactory;
//import Graph.LineSetGraphFactory;
//import Graph.ViewFocusGraphViewerFactory;
import ParallelCoord.ParallelCoordinateViewerFactory;
import data.TableDataFactory;
import javax.servlet.http.HttpSession;

import perspectives.Environment;
import properties.Property;

import perspectives.PropertyManager;
import properties.PropertyType;
import perspectives.Viewer;

import HeatMap.*;
import brain.BrainDataFactory;
import brain.BrainStatsD3ViewerFactory;
import brain.BrainViewerFactory;
import d3.D3Viewer;
import d3.GraphD3ViewerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import perspectives.DataSource;
import perspectives.ViewerFactory;

public class VizOnlineServlet extends HttpServlet {

    
    Environment e;
    String type = "";
    String dataname = "";
    String page = "";
    ArrayList<String> allProp = new ArrayList<String>();
    int currentViewerIndex = 0;
    int viewerIndex = 0;
    int imgCount = 0;
    String uploadsPath;
    int dataSourceIndex = 0;
    int userID = 0;
    


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out;
        String outResponse = null;
        HttpSession session = request.getSession();
        String message = "";

        Environment e = null;
        //get the environment from the session or create a new one if the session doesn't have the environment initialized
        if (session.getAttribute("environment") == null) {
            // session = request.getSession(true);
            e = new Environment(true);
            e.registerDataSourceFactory(new TableDataFactory());
            e.registerDataSourceFactory(new GraphDataFactory());
            e.registerDataSourceFactory(new BrainDataFactory());
            //dataSourceIndex = 0;

            //register Viewers
            e.registerViewerFactory(new HeatMapViewerFactory());
            e.registerViewerFactory(new GraphViewerFactory());
            e.registerViewerFactory(new ParallelCoordinateViewerFactory());
            e.registerViewerFactory(new GraphD3ViewerFactory());
            e.registerViewerFactory(new BrainViewerFactory());
            e.registerViewerFactory(new BrainStatsD3ViewerFactory());
            

            propsInit();    //call the propsInit again for new sessions.

            message = "Environment has been Initialized";
            
            userID++;
            session.setAttribute("userID", userID);

        } else {
            e = (Environment) session.getAttribute("environment");
            message = "Environment already exists";
        }





        try {
            if (request.getParameter("page").equals("home")) {
                System.out.println("Home....");
                outResponse = message;

            }
           
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        session.setAttribute("environment", e); //reset the environment session

        if (outResponse != null) {
            out = response.getWriter();
            out.write(outResponse);
            out.flush();
            out.close();
        }




    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        //System.out.println("The PropsInitCnt is "+propsInitCnt);
        // }
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    } 
  

    @Override
    public void destroy() {
        System.out.println("servlet destroyed");
        super.destroy();
    }

    @Override
    public void init() throws ServletException {
        // TODO Auto-generated method stub
        //System.out.println("servlet init");

        uploadsPath = "/WEB-INF/Uploads/";

        propsInit();

        super.init();
    }

    public void propsInit() {
    	Hashtable<VizOnlineServlet, Environment> envs = (Hashtable<VizOnlineServlet, Environment>) this.getServletContext().getAttribute("envs");
       
    	Environment e = new Environment(true);

        envs.put(this, e);
        this.getServletContext().setAttribute("key", this);  //set the key of the attribute

     

    }

 
}