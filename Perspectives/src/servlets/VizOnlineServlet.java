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
import perspectives.PropertyChangeListener;
import perspectives.PropertyManager;
import properties.PropertyType;
import perspectives.Viewer;
//import perspectives.DefaultProperties.*;

//import tree.RadialBrainConnectivityViewerFactory;
//import util.EyeCatcherFactory;
//import util.Hotspots;
//import util.HotspotsFactory;
//import util.ImageTilerFactory;

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
    String uploadsPath;
    int dataSourceIndex = 0;
    int userID = 0;
    HashMap<String, String> propCommandsSet = new HashMap<String, String>();

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
                //message will be whether the environment has been initialized or not 
                outResponse = message;

            } else if (request.getParameter("page").equals("getviewerfact")) {
                //Request to get Available Viewers from Perspectives
                System.out.println("get Viewer Fact....");
                outResponse = getViewerFact(e);
            } else if (request.getParameter("page").equals("getcurrviewers")) {
                //Request to get Current Viewers from Perspectives
                outResponse = getCurrViewers(e);
            } else if (request.getParameter("page").equals("createViewer")) {
                //Request to Create Viewer              
                System.out.println("create Viewer....");
                type = request.getParameter("type").toLowerCase();
                dataname = request.getParameter("data");
                String dataSourceName = request.getParameter("dataSourceName");
                int vindex = createViewer(e, type, dataSourceName);
                //int vindex = createViewer(type, dataname);
                outResponse = vindex + "";

                /*HashMap<String, Integer> viewerCallCounts = new HashMap<String, Integer>();
                
                session.setAttribute("viewerCallCounts", viewerCallCounts);*/
                session.setAttribute("environment", e);

            } else if (request.getParameter("page").equals("delViewer")) {
                //Request to Delete Viewer                
                int delIndex = Integer.parseInt(request.getParameter("index"));
                e.deleteViewer(e.getViewers().get(delIndex));
                System.out.println(allProp.get(delIndex));
                allProp.remove(delIndex);
                viewerIndex--;
                outResponse = getCurrViewers(e);
            } else if (request.getParameter("page").equals("deleteDataSource")) {

                String dataSourceName = request.getParameter("dataSourceName");

                int dsIndex = getDataSourceIndex(e, dataSourceName);

                //delete the data
                if (dsIndex >= 0) {//delete the dataSource
                    e.getDataSources().remove(dsIndex);
                }

            } else if (request.getParameter("page").equals("viewerLaunch")) {
                //Request to Launch Viewer Page               
                String viewerName = request.getParameter("viewerName");

                Viewer viewer = null;

                int index = getViewerIndex(e, viewerName);
                viewer = e.getViewers().get(index);

                if (viewer instanceof D3Viewer) {
                    System.out.println("D3-VIEWER");
                    outResponse = "d3viewer.html" + ";" + viewerName;
                } else {
                    outResponse = "viewer.html" + ";" + viewerName;
                }

            } else if (request.getParameter("page").equals("dataFactories")) {
                //return the dataFactories
                String dataFactNames = "";

                for (int i = 0; i < e.getDataFactories().size(); i++) {
                    if (i > 0) {
                        dataFactNames += ",";
                    }
                    dataFactNames += e.getDataFactories().get(i).creatorType();
                }
                outResponse = dataFactNames;
            } else if (request.getParameter("page").equals("dataSourceIndex")) {
                //return the current dataSourceIndex  which is like a  count from the session or return 0;
                if(session.getAttribute("dataSourceIndex")!= null){
                    outResponse = session.getAttribute("dataSourceIndex").toString();
                }
                else{
                    outResponse = "" + 0;
                }
                                
            } else if (request.getParameter("page").equals("dataFactoryProperties")) {

                String propCommands;

                String dataFactoryType = request.getParameter("dataFactoryType");
                String dataSourceName = request.getParameter("dataSourceName");
                String factoryItemName = "";
                DataSource ds = null;

                if (dataFactoryType != null) { //get the dataFactory and its properties
                    for (int i = 0; i < e.getDataFactories().size(); i++) {
                        if (dataFactoryType.equalsIgnoreCase(e.getDataFactories().get(i).creatorType())) {
                            // factoryItemName = dataSourceName;
                            ds = e.getDataFactories().get(i).create(dataSourceName);
                            //add the dataSource
                            e.addDataSource(ds, true);
                            //add the propertylistener
                            e.getDataSources().get(e.getDataSources().size() - 1).addPropertyChangeListener(listener);
                        }
                    }
                }


                propCommands = "";

                propCommands = getDataSourceProperties(e, dataSourceName);

                //increment the dataSourceIndex and put it in the session variable
                int dsIndex = 1;
                if(session.getAttribute("dataSourceIndex") != null){
                    dsIndex = Integer.parseInt(session.getAttribute("dataSourceIndex").toString()) ;
                    dsIndex++;
                }
                
                //dataSourceIndex++;
                session.setAttribute("dataSourceIndex", dsIndex);
                
                outResponse = propCommands;
                session.setAttribute("environment", e); //reset the environment session

            }else if(request.getParameter("page").equalsIgnoreCase("getDataSourceNames")) {
                outResponse = getDataSourceNames(e);
            }
            
            else if (request.getParameter("page").equalsIgnoreCase("datasetProperties")) {

                String dataSourceName = request.getParameter("dataSourceName");

                outResponse = getDataSourceProperties(e, dataSourceName);


            } else if (request.getParameter("page").equals("viewer")) {
                //Request to Display Viewer Image               
                // System.out.println("viewer Image....");                
                String viewerName = request.getParameter("viewerName");
                //System.out.println("VIEWER-NAME IS +++++++++" +request.getParameter("viewerName"));

                loadViewer(e, viewerName, request, response);
            } else if (request.getParameter("page").equals("pollprops")) {
                String factoryItemName = request.getParameter("factoryItemName");
                //call the pollProps method
                outResponse = pollProps(factoryItemName, request, response);
            } else if (request.getParameter("page").equals("properties")) {
                String viewerName = request.getParameter("viewerName");
                String propertyString = getViewerProperties(e, viewerName);

                outResponse = propertyString;
            } else if (request.getParameter("page").equals("d3viewer")) {

                String viewerName = request.getParameter("viewerName");

                //Request to get Initial Properties    
                String method = request.getParameter("method");
                if (method.equals("readViewerData")) {
                    loadD3Viewer(e, viewerName, request, response);
                } else if (method.equals("readCreatorType")) {

                    int index = getViewerIndex(e, viewerName);

                    Viewer viewer = e.getViewers().get(index);

                    Vector<ViewerFactory> factories = e.getViewerFactories();
                    String creatorType = "";
                    for (ViewerFactory factory : factories) {
                        Viewer factoryViewer = factory.create("sample");
                        if (factoryViewer != null && factoryViewer.getClass() == viewer.getClass()) {
                            creatorType = factory.creatorType();
                            break;
                        }
                    }
                    outResponse = creatorType;
                }
            } else if (request.getParameter("page").equals("getDatas")) {
                // System.out.println("GETTING DATAS");
                String filePath = getServletContext().getRealPath(uploadsPath);
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
            } else if (request.getParameter("page").equals("updateProperty")) {

                System.out.println("**********************************************************");
                //Request to update Properties    
                String newvalue = request.getParameter("newValue");
                String property = request.getParameter("property");
                String factoryType = request.getParameter("factoryType");
                String factoryItemName = request.getParameter("factoryItemName");

                //int factoryTypeIndex = -1;

                System.out.println("----------UpdateProperty: " + newvalue + " property: " + property);

                String type = "";
                //Do according to the factoryType
                if (factoryType.equals("DataSource")) {
                    //System.out.println("the Size of the DataSource is " + e.getDataSources().size());
                    
                    int index = getDataSourceIndex(e, factoryItemName);
                    
                    //System.out.println("The FactoryItem index is " + factoryItemIndex);

                    if (index >= 0) {
                        //get the type
                        type = e.getDataSources().get(index).getProperty(property).getValue().typeName();

                        System.out.println("::::::::::::::::Type : " + type);

                        if (type.equals("PBoolean")) {
                            newvalue = (newvalue.equals("true") ? "1" : "0");
                        } else if (type.equals("PFile")) {
                            String fileName = request.getParameter("fileName");
                            outResponse = fileName;

                        }

                        e.getDataSources().get(index).getProperty(property)
                                .setValue((e.getDataSources().get(index)).deserialize(type, newvalue));
                    }

                } else if (factoryType.equals("Viewer")) {
                    //TO-DO it may mean it is a property for a viewer at least for now

                    String viewerName = request.getParameter("factoryItemName");

                    int index = getViewerIndex(e, viewerName);

                    if (property != null && newvalue != null) {
                        type = e.getViewers().get(index).getProperty(property).getValue().typeName();

                        if (type.equals("PBoolean")) {
                            newvalue = (newvalue.equals("true") ? "1" : "0");
                        } else if (type.equals("PFile")) {
                            String fileName = request.getParameter("fileName");
                            outResponse = fileName;
                        }

                        e.getViewers().get(index).getProperty(property)
                                .setValue(e.getViewers().get(index).deserialize(type, newvalue));
                    }
                }

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

    public void loadD3Viewer(Environment e, String viewerName, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        HttpSession session = request.getSession();

        if (request.getParameter("isinitcall") != null
                && request.getParameter("isinitcall").equalsIgnoreCase("True")) {

            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Cache-control", "no-cache, no-store");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "-1");


            int index = getViewerIndex(e, viewerName);


            D3Viewer viewer = (D3Viewer) e.getViewers().get(index);
            PrintWriter out = response.getWriter();
            out.println(viewer.updateData(true));

            session.setAttribute("environment", e); //reset the environment session
        } else {
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Cache-control", "no-cache, no-store");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "-1");

            int index = getViewerIndex(e, viewerName);

            D3Viewer viewer = (D3Viewer) e.getViewers().get(index);
            PrintWriter out = response.getWriter();
            out.println(viewer.updateData(false));

            session.setAttribute("environment", e); //reset the environment session
        }



    }
    //Method to Obtain Images from Perspectives

    public void loadViewer(Environment e, String viewerName, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String cencoding = request.getParameter("changeEncoding");

        if (cencoding != null) {
            encoding = Integer.parseInt(cencoding);
            return;
        }

        String mType = request.getParameter("mousetype");
        String changeProp = request.getParameter("changeProp");

        int index = getViewerIndex(e, viewerName);

        if (index >= 0) {

            if (changeProp != null) {
                System.out.println("changerop");
                String name = request.getParameter("name");
                String value = request.getParameter("value");


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
                    System.out.println("MouseMoved");

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

                /*HashMap<String, Integer> viewerCallCounts = (HashMap<String, Integer>)session.getAttribute("viewerCallCounts");
                
                if(viewerCallCounts.get(viewerName) == null){
                    tx = -1;
                    ty = -1;
                    viewerCallCounts.put(viewerName, 1);
                    session.setAttribute("viewerCallCounts", viewerCallCounts);
                            
                }*/
                
                
                byte[] bim = e.getViewerContainers().get(index).getTile(tx, ty);

                this.sendImage(bim, response);

                long t2 = (new Date()).getTime();


            }


            session.setAttribute("environment", e); //reset the environment session
        }

    }

    public String pollProps(String factoryItemName, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //String factoryItemName = request.getParameter("factoryItemName");
        String currPropCommands = "";
        if (factoryItemName != null) {

            synchronized (pcsync) {
                response.setContentType("text/html");

                currPropCommands = propCommandsSet.get(factoryItemName);

                //reset the propCommandsSet
                propCommandsSet.put(factoryItemName, "");

                // response.getWriter().write(currPropCommands);

                System.out.println("POLLPROPS: " + propertyCommands + " FACTORY-ITEM-NAME:::::" + factoryItemName);
                System.out.println("-------------- HASHMAP-VALUE::::::::" + currPropCommands);

            }
        }

        return currPropCommands;
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


        listener = new PropertyChangeListener() {
            @Override
            public void propertyAdded(PropertyManager pm, Property p) {

                String currPropCommands = propCommandsSet.get(pm.getName());

                synchronized (pcsync) {
                    if (currPropCommands != null && currPropCommands.length() != 0) {
                        currPropCommands += ";";
                    }
                    currPropCommands = currPropCommands + "addProperty," + pm.getName() + ","
                            + p.getName() + "," + p.getValue().typeName() + "," + p.getValue().serialize()
                            + "," + p.getReadOnly();
                }
                //System.out.println("PROPERTY-ADDED " + propertyCommands);
                propCommandsSet.put(pm.getName(), currPropCommands);
            }

            @Override
            public void propertyRemoved(PropertyManager pm, Property p) {
                String currPropCommands = propCommandsSet.get(pm.getName());

                synchronized (pcsync) {
                    if (currPropCommands != null && currPropCommands.length() != 0) {
                        currPropCommands += ";";
                    }
                    currPropCommands = currPropCommands + "removeProperty," + pm.getName() + "," + p.getName() + "," + p.getValue();
                }

                propCommandsSet.put(pm.getName(), currPropCommands);

            }

            @Override
            public void propertyValueChanged(PropertyManager pm,
                    Property p, PropertyType newValue) {
                //TO-DO: No need for this right now
                /*synchronized (pcsync) {
                 if (propertyCommands.length() != 0) {
                 propertyCommands += ";";
                 }
                 propertyCommands = propertyCommands + "changeProperty," + pm.getName() + "," + p.getName() + "," + p.getValue();
                 }

                 System.out.println("PROPERTY-VALUE CHANGED " + propertyCommands);

                 propCommandsSet.put(pm.getName(), propertyCommands); */
            }

            @Override
            public void propertyReadonlyChanged(PropertyManager pm,
                    Property p, boolean newReadOnly) {
                // TODO Auto-generated method stub

                String currPropCommands = propCommandsSet.get(pm.getName());

                synchronized (pcsync) {
                    if (currPropCommands != null && currPropCommands.length() != 0) {
                        currPropCommands += ";";
                    }
                    currPropCommands = currPropCommands + "readOnlyChanged," + pm.getName() + "," + p.getName() + "," + newReadOnly;
                }

                propCommandsSet.put(pm.getName(), currPropCommands);
                //System.out.println("READ-ONLY CHANGED " + currPropCommands);

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

        envs.put(this, e);
        this.getServletContext().setAttribute("key", this);  //set the key of the attribute

        propsInitCnt++;

    }

    public void sendImage(byte[] bs, HttpServletResponse response) {
        int frameCount = 50;

        long t2 = new Date().getTime();
        try {

            //ImageIO.write(capture, "png", new File((getServletContext().getRealPath("/WEB-INF/fim"+t2+"capture.png"))));           

            //System.out.println("-2-");
            response.getOutputStream().write(bs);
            //System.out.println("-3-");
            response.flushBuffer();
            //System.out.println("-4-");

            long t3 = new Date().getTime();

            //  System.out.println("T: " + (t3 - t2) + "      Econding:" + encoding + "      size:" + (bs.length / 1024));

        } catch (Exception e) {
            System.out.println("-e-");
            e.printStackTrace();

        }
    }

    //Method to get Available Viewers from Perspectives
    public String getViewerFact(Environment e) {

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
    public String getCurrViewers(Environment e) {

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

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (allCurrViewers.equals("")) {
            return "No Content";
        } else {
            return allCurrViewers;
        }



    }

    public int createViewer(Environment e, String type, String dataSourceName) {


        Viewer v = null;

        propertyCommands = "";

        System.out.println("TYPE:  " + type);

        v = e.createNewViewer(dataSourceName, type);

        if (v != null) {
            viewerIndex++;

            int size = e.getViewers().size();
            //System.out.println(" ::::::::"+e.getViewers().get(size-1).getName()+ "  " +listener );

            e.getViewers().get(size - 1).addPropertyChangeListener(listener);

        } else {
            System.out.println("Viewer creation failed");
        }

        allProp.add(viewerIndex - 1, propertyCommands);

        return viewerIndex - 1;
    }

    /**
     * method to return the properties of a given viewerName
     *
     * @param env
     * @param viewerName
     * @return
     */
    public String getViewerProperties(Environment e, String viewerName) {

        String propCommands = "";

        int vindex = -1;
        for (int i = 0; i < e.getViewers().size(); i++) {
            if (viewerName.equalsIgnoreCase(e.getViewers().get(i).getName())) {
                vindex = i;
            }
        }
        if (vindex >= 0) {
            Viewer v = e.getViewers().get(vindex);

            if (v != null) {
                Property[] ps = v.getProperties();

                for (int i = 0; i < ps.length; i++) {
                    if (i != 0) {
                        propCommands += ";";
                    }

                    propCommands += "addProperty," + v.getName() + "," + ps[i].getName()
                            + "," + ps[i].getValue().typeName() + ","
                            + ps[i].getValue().serialize() + "," + ps[i].getReadOnly();

                }
            }
        }
        return propCommands;

    }

    /**
     * method to return the index of a given viewerName
     *
     * @param env
     * @param viewerName
     * @return
     */
    public int getViewerIndex(Environment e, String viewerName) {
        int index = -1;

        for (int i = 0; i < e.getViewers().size(); i++) {
            if (viewerName.equalsIgnoreCase(e.getViewers().get(i).getName())) {
                index = i;
                break;
            }
        }
        return index;
    }

    public int getDataSourceIndex(Environment e, String dataSourceName) {
        int index = -1;

        for (int i = 0; i < e.getDataSources().size(); i++) {
            if (dataSourceName.equalsIgnoreCase(e.getDataSources().get(i).getName())) {
                index = i;
                break;
            }
        }

        return index;
    }

    public String getDataSourceProperties(Environment e, String dataSourceName) {
        String propCommands = "";
        DataSource ds = null;
        int index = getDataSourceIndex(e, dataSourceName);


        if (index >= 0) {
            ds = e.getDataSources().get(index);
            Property[] ps = ds.getProperties();

            for (int i = 0; i < ps.length; i++) {
                if (i != 0) {
                    propCommands += ";";
                }

                propCommands += "addProperty," + ds.getName() + "," + ps[i].getName()
                        + "," + ps[i].getValue().typeName()
                        + "," + ps[i].getValue().serialize() + "," + ps[i].getReadOnly();

            }

        }

        return propCommands;
    }
    
    public String getDataSourceNames(Environment e){
        String dsNames = "";
        
        for (int i=0; i<e.getDataSources().size(); i++){
            
            if(i!=0){
                dsNames +=";";
            }
            
            dsNames += e.getDataSources().get(i).getName();            
        }
        
        return dsNames;
        
    }
}