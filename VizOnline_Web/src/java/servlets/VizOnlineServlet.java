package servlets;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.*;
import java.util.Date;
import java.util.Hashtable;

import com.keypoint.*;

import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import multidimensional.PlanarProjectionViewerFactory;
import Graph.BubbleSetGraphFactory;
import Graph.Graph;
import Graph.GraphData;
import Graph.GraphDataFactory;
import Graph.GraphViewer;
import Graph.GraphViewerFactory;
//import Graph.LineSetGraphFactory;
//import Graph.ViewFocusGraphViewerFactory;
import ParallelCoord.ParallelCoordViewer;
import ParallelCoord.ParallelCoordinateViewerFactory;
import data.TableData;
import data.TableDataFactory;
import data.TableDistances;
import javax.servlet.http.HttpSession;

import perspectives.Environment;
import properties.Property;
import perspectives.PropertyChangeListener;
import perspectives.PropertyManager;
import properties.PropertyType;
import perspectives.Viewer;
//import perspectives.DefaultProperties.*;

import tree.HierarchicalClusteringViewerFactory;
//import tree.RadialBrainConnectivityViewerFactory;
//import util.EyeCatcherFactory;
//import util.Hotspots;
//import util.HotspotsFactory;
//import util.ImageTilerFactory;

import HeatMap.*;
import java.util.ArrayList;
import java.util.Vector;
import perspectives.ViewerFactory;
import properties.PFile;

public class VizOnlineServlet extends HttpServlet {

    BufferedImage[] screen = null;
    BufferedImage[] splitImage = null;
    boolean[] compressed;
    private Object[] syncobj;
    int[] cnt;
    int[] step;
    int encoding = 2;
    PropertyChangeListener listener;
    String propertyCommands = "";
    Object pcsync = new Object();
    int propsInitCnt = 0;
    Environment e;
    String type = "";
    String dataname = "";
    String page = "";
    ArrayList<String> allProp = new ArrayList<String>();
    int currentViewerIndex = 0;
    int viewerIndex = 0;
    int imgCount = 0;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out;
        String outResponse = null;
        HttpSession session = request.getSession();

        try {
            if (request.getParameter("page").equals("home")) {
                System.out.println("Home....");
                if (e == null) {
                    session = request.getSession(true);
                    e = new Environment(true);

                    propsInit();    //call the propsInit again for new sessions.

                    //Thread.sleep(3000);


                    e.registerDataSourceFactory(new TableDataFactory());
                    e.registerViewerFactory(new HeatMapViewerFactory());
                    e.registerViewerFactory(new GraphViewerFactory());
                    e.registerViewerFactory(new ParallelCoordinateViewerFactory());

                    outResponse = "Environment has been Initialized";

                } else {
                    outResponse = "Environment already exists";
                }
            } else if (request.getParameter("page").equals("getviewerfact")) {
                //Request to get Available Viewers from Perspectives

                System.out.println("get Viewer Fact....");
                outResponse = getViewerFact();
            } else if (request.getParameter("page").equals("getcurrviewers")) {
                //Request to get Current Viewers from Perspectives

                System.out.println("get Current Viewers....");
                outResponse = getCurrViewers();
            } else if (request.getParameter("page").equals("createViewer")) {
                //Request to Create Viewer              
                System.out.println("create Viewer....");
                type = request.getParameter("type").toLowerCase();
                dataname = request.getParameter("data");
                int vindex = createViewer(type, dataname);
                outResponse = vindex + "";

            } else if (request.getParameter("page").equals("delViewer")) {
                //Request to Delete Viewer                
                int delIndex = Integer.parseInt(request.getParameter("index"));
                e.deleteViewer(e.getViewers().get(delIndex));
                System.out.println(allProp.get(delIndex));
                allProp.remove(delIndex);
                viewerIndex--;
                outResponse = getCurrViewers();
            } else if (request.getParameter("page").equals("viewerLaunch")) {
                //Request to Launch Viewer Page               
                int theIndex = Integer.parseInt(request.getParameter("index"));
                currentViewerIndex = theIndex;
                outResponse = "viewer.html";

            } //Request to Link Viewers
            //            case "linkViewers":
            //                int firstV = Integer.parseInt(request.getParameter("first"));
            //                int secondV = Integer.parseInt(request.getParameter("second"));
            //                e.linkViewers(firstV, secondV);
            //                outResponse = getLinks();
            //                System.out.println(outResponse);
            //                break;
            //            case "unlinkViewers":
            //                int linkIndex = Integer.parseInt(request.getParameter("index"));
            //                e.unlinkViewers(linkIndex);
            //                outResponse = getLinks();
            //                break;
            else if (request.getParameter("page").equals("viewer")) {
                //Request to Display Viewer Image               
                // System.out.println("viewer Image....");
                //loadViewer(currentViewerIndex, request, response);

                String requestViewerIndex = request.getParameter("viewerIndex");

                if (requestViewerIndex != null) { //we expect the viewerIndex to be passed all the time            
                    int index = Integer.parseInt(requestViewerIndex.trim());
                    if (index >= 0) //by default it is -1
                    {
                        loadViewer(index, request, response);
                    }
                } else {
                    System.out.println("ERROR: Request for a Viewer without viewerIndex parameter");
                }


            } else if (request.getParameter("page").equals("properties")) { //Request to get Initial Properties    

                //append the ViewerIndex to the properties
                String properties = allProp.get(currentViewerIndex) + ";setViewerIndex, " + currentViewerIndex;
                outResponse = properties;
            } else if (request.getParameter("page").equals("getDatas")) {
                // System.out.println("GETTING DATAS");
                String filePath = getServletContext().getRealPath("/WEB-INF/Uploads/");
                System.out.println(filePath + "***********************");
                String files = "";
                File folder = new File(filePath);
                File[] listOfFiles = folder.listFiles();

                for (int i = 0; i < listOfFiles.length; i++) {

                    if (listOfFiles[i].isFile()) {
                        if (files.equals("")) {
                            files = listOfFiles[i].getName();
                        } else {
                            files = files + "," + listOfFiles[i].getName();
                        }
                    }
                }
                if (files.equals("")) {
                    outResponse = "No Content";
                } else {
                    outResponse = files;
                }
            } else if (request.getParameter("page").equals("updateProperty")) { //Request to update Properties    
                String newvalue = request.getParameter("newValue");
                String property = request.getParameter("property");
                System.out.println("----------UpdateProperty: " + newvalue + " property: " + property);

                String requestViewerIndex = request.getParameter("viewerIndex");

                if (property != null && newvalue != null && requestViewerIndex != null) {
                    int index = Integer.parseInt(requestViewerIndex.trim());
                    if (index >= 0) { //do the actual update
                        String type = e.getViewers().get(index).getProperty(property).getValue().typeName();
                        if ("PBoolean".equals(type)) {
                            newvalue = (newvalue.equals("true") ? "1" : "0");
                        }
                        e.getViewers().get(index).getProperty(property).setValue(e.getViewers().get(index).deserialize(type, newvalue));
                    } else {
                        System.out.println("ERROR: viewerIndex is Null");
                    }
                } else {
                    System.out.println("ERROR: Either property, newValue, or viewerIndex is null");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (outResponse != null) {
            out = response.getWriter();
            out.write(outResponse);
            out.flush();
            out.close();
        }

        session.setAttribute("environment", e); //reset the environment session


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

    //Method to Obtain Images from Perspectives
    public void loadViewer(int index, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // System.out.println("loadViewer");
        //System.out.println("Property Commands is " + propertyCommands);

        Hashtable<VizOnlineServlet, Environment> envs = (Hashtable<VizOnlineServlet, Environment>) this.getServletContext().getAttribute("envs");
        Environment e;

        //System.out.println("The PropsInitCnt is "+propsInitCnt);

        HttpSession session = request.getSession();

        if (request.getParameter("firstTime") != null) {
            session = request.getSession(true);


            // System.out.println("First Time. Loading Properties ....");
            propsInit();    //call the propsInit again for new sessions.

        } else {

            if (session.getAttribute("environment") == null) {
                e = envs.get(this);
                session.setAttribute("environment", e);
            } else {
                e = (Environment) session.getAttribute("environment");
            }


            String cencoding = request.getParameter("changeEncoding");

            if (cencoding != null) {
                encoding = Integer.parseInt(cencoding);
                return;
            }

            String mType = request.getParameter("mousetype");
            String changeProp = request.getParameter("changeProp");
            String pollprops = request.getParameter("pollprops");

            if (pollprops != null) {

                synchronized (pcsync) {
                    response.setContentType("text/html");
                    response.getWriter().write(propertyCommands);
                    //System.out.println("command sent: " + propertyCommands);

                    response.flushBuffer();
                    propertyCommands = "";

                }
            } else if (changeProp != null) {
                System.out.println("changerop");
                String name = request.getParameter("name");
                String value = request.getParameter("value");

                // System.out.println("$$$$$$$$$$ Color : "+value);

                String type = e.getViewers().get(index).getProperty(name).getValue().typeName();
                e.getViewers().get(index).getProperty(name).setValue(e.getViewers().get(index).deserialize(type, value));

            } else if (mType != null) {
                response.setContentType("text/html");
                response.setHeader("Cache-control", "no-cache, no-store");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Expires", "-1");

                String cmd = request.getParameter("cmd");
                String xs = request.getParameter("mx");
                String ys = request.getParameter("my");


                int x = Integer.parseInt(xs);
                int y = Integer.parseInt(ys);


                boolean leftMouse = false;
                if (mType.trim().equalsIgnoreCase("left")) {
                    leftMouse = true;
                }



                if (cmd.equalsIgnoreCase("mouseUp")) {

                    if (leftMouse) {
                        e.getViewerContainers().get(index).mouseReleased(x, y, MouseEvent.BUTTON1);
                    } else {
                        e.getViewerContainers().get(index).mouseReleased(x, y, MouseEvent.BUTTON3);
                    }
                } else if (cmd.equalsIgnoreCase("mouseDown")) {

                    //System.out.println("MOUSE-DOWN");
                    if (leftMouse) {
                        e.getViewerContainers().get(index).mousePressed(x, y, MouseEvent.BUTTON1);
                    } else {
                        e.getViewerContainers().get(index).mousePressed(x, y, MouseEvent.BUTTON3);
                    }

                } else if (cmd.equalsIgnoreCase("mouseMoved")) {
                    //System.out.println("MouseMoved");

                    e.getViewerContainers().get(index).mouseMoved(x, y);

                    //System.out.println("The class of the viewer is: "+ (e.getViewerContainers().get(index).getClass().getName()));

                } else if (cmd.equalsIgnoreCase("mouseDragged")) {
                    // System.out.println("MOUSE-DRAGGED");
                    e.getViewerContainers().get(index).mouseDragged(x, y);
                }
            } else {
                response.setContentType("image/png");
                response.setHeader("Cache-control", "no-cache, no-store");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Expires", "-1");


                String tilesX = request.getParameter("tileX");
                String tilesY = request.getParameter("tileY");
                String difs = request.getParameter("diff");
                int tx = Integer.parseInt(tilesX);
                int ty = Integer.parseInt(tilesY);
                int dif = Integer.parseInt(difs);

                BufferedImage bim = e.getViewerContainers().get(index).getTile(tx, ty, dif == 1, true);

                this.sendImage(bim, response);

                long t2 = (new Date()).getTime();


            }


            session.setAttribute("environment", e); //reset the environment session
        }




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

        propsInit();

        super.init();
    }

    public void propsInit() {
        Hashtable<VizOnlineServlet, Environment> envs = (Hashtable<VizOnlineServlet, Environment>) this.getServletContext().getAttribute("envs");
        screen = new BufferedImage[6];
        compressed = new boolean[6];
        syncobj = new Object[6];
        cnt = new int[6];
        step = new int[6];

        splitImage = new BufferedImage[6];
        for (int i = 0; i < 6; i++) {
            splitImage[i] = null;
            screen[i] = null;
            compressed[i] = false;
            syncobj[i] = new Object();
            cnt[i] = 0;
            step[i] = 0;
        }


        propertyCommands = "";



        //if (envs.get(this) == null) //first time
        //  {
        listener = new PropertyChangeListener() {
            @Override
            public void propertyAdded(PropertyManager pm, Property p) {
                synchronized (pcsync) {
                    if (propertyCommands.length() != 0) {
                        propertyCommands += ";";
                    }
                    propertyCommands = propertyCommands + "addProperty," + pm.getName() + "," + p.getName() + "," + p.getValue().typeName() + "," + p.getValue();
                }
                System.out.println(propertyCommands);

            }

            @Override
            public void propertyRemoved(PropertyManager pm, Property p) {
                synchronized (pcsync) {
                    if (propertyCommands.length() != 0) {
                        propertyCommands += ";";
                    }
                    propertyCommands = propertyCommands + "removeProperty," + pm.getName() + "," + p.getName() + "," + p.getValue();
                }

            }

            @Override
            public void propertyValueChanged(PropertyManager pm,
                    Property p, PropertyType newValue) {
                synchronized (pcsync) {
                    if (propertyCommands.length() != 0) {
                        propertyCommands += ";";
                    }
                    propertyCommands = propertyCommands + "changeProperty," + pm.getName() + "," + p.getName() + "," + p.getValue();
                }

            }

            @Override
            public void propertyReadonlyChanged(PropertyManager pm,
                    Property p, boolean newReadOnly) {
                // TODO Auto-generated method stub
            }

            @Override
            public void propertyVisibleChanged(PropertyManager pm,
                    Property p, boolean newVisible) {
                // TODO Auto-generated method stub
            }

            @Override
            public void propertyPublicChanged(PropertyManager pm,
                    Property p, boolean newPublic) {
                // TODO Auto-generated method stub
            }

            @Override
            public void propertyDisabledChanged(PropertyManager pm,
                    Property p, boolean newPublic) {
                // TODO Auto-generated method stub
            }
        };


        Environment e = new Environment(true);



        // String path = (getServletContext().getRealPath("/WEB-INF/table_data_sample.txt"));
        //  TableData tb = new TableData(path);
        // TableDistances table = new TableDistances();


        // table.fromFile(path, "\t", true, true);  //Function to Get the Data from File

        //tb.setTable(table);

        //System.out.println("Finished loading Table data");

//		        Viewer v = new ParallelCoordViewer("PC", tb);
//                        
//		        e.addViewer(v);

        /*Trying HeatMap */
        //Viewer v = new HeatMapViewer("HM", tb);
        // e.addViewer(v);



//        GraphData gd = new GraphData("d1");
//
//        Graph g = new Graph(false);
//        g.fromEdgeList(new File((getServletContext().getRealPath("/WEB-INF/edge_list.txt"))));
//        gd.setGraph(g);
//
//        Viewer v = new GraphViewer("graphvi", gd);
//        System.out.println(g.numberOfNodes());
//        OpenFilePropertyType opt = new OpenFilePropertyType();
//        opt.path = getServletContext().getRealPath("/WEB-INF/pos5.txt");
//        v.getProperty("Load Positions").setValue(opt);
//        e.addViewer(v);
//                        
//                        System.out.println("Finished Loading Graph data");
//		        
        //Property[] ps = v.getProperties();

//        synchronized (pcsync) {
//            String value;
//            for (int i = 0; i < ps.length; i++) {
//                if (i != 0) {
//                    propertyCommands += ";";
//                }
//
//                propertyCommands += "addProperty," + v.getName() + "," + ps[i].getName() + "," + ps[i].getValue().typeName() + "," + ps[i].getValue().serialize();
//
//            }
//        }
        // v.addPropertyChangeListener(listener);

        envs.put(this, e);
        this.getServletContext().setAttribute("key", this);  //set the key of the attribute

        // session.setAttribute("environment", e);  //set the environment variable here

        propsInitCnt++;



        //}
        //Environment e = envs.get(this);
        //super.init();
    }

    public void sendImage(BufferedImage capture, HttpServletResponse response) {
        int frameCount = 50;

        long t2 = new Date().getTime();
        try {

            //ImageIO.write(capture, "png", new File((getServletContext().getRealPath("/WEB-INF/fim"+t2+"capture.png"))));

            PngEncoder p = new PngEncoder(capture, true);

            System.out.println(capture.getWidth() + " " + capture.getHeight());
            p.setFilter(PngEncoder.FILTER_NONE);

            p.setCompressionLevel(encoding);
            //System.out.println("-1-");
            byte[] bs = p.pngEncode(true);
            //System.out.println("-2-");
            response.getOutputStream().write(bs);
            //System.out.println("-3-");
            response.flushBuffer();
            //System.out.println("-4-");

            long t3 = new Date().getTime();

            //System.out.println("T1: " +  (t2-t1) + ";       T2: " + (t3-t2) + "      Econding:" + encoding);

        } catch (Exception e) {
            System.out.println("-e-");
            e.printStackTrace();

        }
    }

    //Method to get Available Viewers from Perspectives
    public String getViewerFact() {

        System.out.println("Viewer factories");

        String allViewers = "";
        Vector<ViewerFactory> viewers = e.getViewerFactories();
        for (int i = 0; i < viewers.size(); i++) {
            if (allViewers.equals("")) {
                allViewers = viewers.get(i).creatorType();
            } else {
                allViewers += "," + viewers.get(i).creatorType();
            }
        }
        return allViewers;
    }

    //Method to get Current Viewers from Perspectives
    public String getCurrViewers() {

        String allCurrViewers = "";
        try {
            Vector<Viewer> viewers = e.getViewers();

            for (int i = 0; i < viewers.size(); i++) {
                if (allCurrViewers.equals("")) {
                    allCurrViewers = i + ":" + viewers.get(i).getName();
                } else {
                    allCurrViewers += "," + i + ":" + viewers.get(i).getName();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (allCurrViewers.equals("")) {
            return "No Content";
        } else {
            return allCurrViewers;
        }



    }

    public int createViewer(String type, String data) {

        Viewer v = null;

        System.out.println(data);
        String filePath = (getServletContext().getRealPath("/WEB-INF/Uploads/") + "\\" + data);
        System.out.println("TEST #2: " + filePath);
        propertyCommands = "";

        /**
         * ***********For Heatmap Viewer and Table Data*****************
         */
        if (type.equalsIgnoreCase("heatmap")) {

            TableData tb = new TableData(filePath);
            TableDistances table = new TableDistances();

            table.fromFile(filePath, "\t", true, true);  //Function to Get the Data from File

            tb.setTable(table);
            v = new HeatMapViewer("HeatMap Viewer", tb);
            e.addViewer(v);
            viewerIndex++;

            Property[] ps = v.getProperties();

            for (int i = 0; i < ps.length; i++) {
                if (i != 0) {
                    propertyCommands += ";";
                }

                propertyCommands += "addProperty," + v.getName() + "," + ps[i].getName() + "," + ps[i].getValue().typeName() + "," + ps[i].getValue().serialize();

            }

            v.addPropertyChangeListener(listener);

        } // System.out.println("Property Commands -------After Creating viewer " + propertyCommands);
        /**
         * ***End of Heatmap Viewer.
         *
         *
         *********** For Graphs Viewer and Graph Data*****************
         */
        else if (type.equalsIgnoreCase("graph viewer")) {
            GraphData gd = new GraphData("d1");

            Graph g = new Graph(false);
            g.fromEdgeList(new File(filePath));
            gd.setGraph(g);
            v = new GraphViewer("Graph Viewer", gd);
            System.out.println(g.numberOfNodes());
            PFile opt = new PFile();
            // opt.path = getServletContext().getRealPath("/WEB-INF/pos5.txt");
            //v.getProperty("Load Positions").setValue(opt);
            e.addViewer(v);
            viewerIndex++;

            Property[] ps = v.getProperties();

            for (int i = 0; i < ps.length; i++) {
                if (i != 0) {
                    propertyCommands += ";";
                }

                propertyCommands += "addProperty," + v.getName() + "," + ps[i].getName() + "," + ps[i].getValue().typeName() + "," + ps[i].getValue().serialize();

            }

            v.addPropertyChangeListener(listener);
        } else if (type.equalsIgnoreCase("parallel coordinates")) {


            TableData tb = new TableData(filePath);
            TableDistances table = new TableDistances();

            table.fromFile(filePath, "\t", true, true);  //Function to Get the Data from File

            tb.setTable(table);

            v = new ParallelCoordViewer("Parallel Coordinates", tb);
            e.addViewer(v);


            //        v = new HeatMapViewer("HeatMap Viewer", tb);
            //      e.addViewer(v);
            viewerIndex++;

            Property[] ps = v.getProperties();

            for (int i = 0; i < ps.length; i++) {
                if (i != 0) {
                    propertyCommands += ";";
                }

                propertyCommands += "addProperty," + v.getName() + "," + ps[i].getName() + "," + ps[i].getValue().typeName() + "," + ps[i].getValue().serialize();

            }

            v.addPropertyChangeListener(listener);
        }
        /**
         * ******* End of graph Viewer ************************************
         */
        allProp.add(viewerIndex - 1, propertyCommands);

        return viewerIndex - 1;
    }
}