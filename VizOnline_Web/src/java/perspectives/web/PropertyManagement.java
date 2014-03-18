package perspectives.web;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import perspectives.base.DataSource;
import perspectives.base.Environment;
import perspectives.base.Property;
import perspectives.base.PropertyChangeListener;
import perspectives.base.PropertyManager;
import perspectives.base.Viewer;
import perspectives.base.PropertyType;
import perspectives.base.PropertyManagerChangeListener;

public class PropertyManagement extends HttpServlet {
	
	 PropertyManagerChangeListener listener;
	 Object pcsync = new Object();
	 HashMap<PropertyManager, String> propCommandsSet = new HashMap<PropertyManager, String>();
	 
	 Set<PropertyManager> withListeners = new HashSet<PropertyManager>();
	 
	 String propertyCommands = "";
	 
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws IOException, ServletException {
	        processRequest(request, response);
	    }

	    @Override
	 protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        processRequest(request, response);
	    }
	    
	    
	 @Override
	 public void init() throws ServletException {
	       propsInit();
	        super.init();
	    }

	
	
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out;
        String outResponse = null;
        HttpSession session = request.getSession();
        String message = "";

        Environment e = (Environment) session.getAttribute("environment");
        
        if (session.getAttribute("environment") == null) {
        	outResponse = null;
        }         
        else {    
	        try {
	        
	           if (request.getParameter("page").equals("pollprops")) {
	        	  
	                String pmn = request.getParameter("propManagerName");
	               
	                PropertyManager pm = getPropertyManager(e,pmn);
	                if (pm != null)
	                	outResponse = pollProps(pm, request, response);
	               // System.out.println("pollprops " + pmn + ": " + outResponse + " end poll props");
	           } 
	           
	           else if (request.getParameter("page").equals("init")) {
	                String pmn = request.getParameter("propManagerName");
	                
	                PropertyManager pm = getPropertyManager(e,pmn);
	                
	              //  System.out
	                
	                if (pm != null)
	                {	                		                	
	                	String propertyString = getProperties(e, pm); 	                	

	                	if (!withListeners.contains(pm))
	                		pm.addChangeListener(listener);	
	                	if (propCommandsSet.containsKey(pm))	                		
	                		propCommandsSet.put(pm, "");
	                	
	                	outResponse = propertyString;
	                }
	            } 
	           
	           else if (request.getParameter("page").equals("updateProperty")) {

	                //Request to update Properties    
	                String newvalue = request.getParameter("newValue");
	                String property = request.getParameter("property");
	                String propManagerName = request.getParameter("propertyManager");
	              
	                System.out.println("----------UpdateProperty: " + newvalue + " property: " + property);
	
	                PropertyManager pm = getPropertyManager(e,propManagerName);
	                
	                System.out.println("PropM: " + pm);
	                System.out.println("Prop: " + pm.getProperty(property));
	                System.out.println("PropV: " + pm.getProperty(property).getValue());
	                String type = pm.getProperty(property).getValue().typeName();

	               if (type.equals("PFileInput")) {
	                            String fileName = request.getParameter("fileName");
	                            outResponse = fileName;
	                }
	
	                pm.getProperty(property).setValue(pm.deserialize(type, newvalue));
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
    }

    public void propsInit() {
       
       
        listener = new PropertyManagerChangeListener() {
        	
            @Override
            public void propertyAdded(PropertyManager pm, Property p) {

               

                synchronized (pcsync) {
                	
                	 String currPropCommands = propCommandsSet.get(pm);
                    if (currPropCommands != null && currPropCommands.length() != 0) {
                        currPropCommands += ";";
                    }
                    else
                   	currPropCommands = "";
                	currPropCommands += "addProperty," + pm.getName() + "," + p.getName()
                            + "," + p.getValue().typeName() + ","
                            + p.getValue().serialize() + "," + p.getReadOnly() + "," + p.getVisible() + "," + p.getDisabled();
                	propCommandsSet.put(pm, currPropCommands);
                	
                	System.out.println("adding prop: " + pm.getName() + " \t " + p.getName());
                	
                	
                }
             
                
            }

            @Override
            public void propertyRemoved(PropertyManager pm, Property p) {
               

                synchronized (pcsync) {
                	 String currPropCommands = propCommandsSet.get(pm);
                    if (currPropCommands != null && currPropCommands.length() != 0) {
                        currPropCommands += ";";
                    }
                    else
                    	currPropCommands = "";
                    currPropCommands+= "removeProperty," + pm.getName() + "," + p.getName();
                    propCommandsSet.put(pm, currPropCommands);
                    
                  
                }

               

            }

            @Override
            public void propertyValueChanges(PropertyManager pm,
                    Property p, PropertyType newValue) { 
               

                synchronized (pcsync) {
                	 String currPropCommands = propCommandsSet.get(pm);
                    if (currPropCommands != null && currPropCommands.length() != 0) {
                        currPropCommands += ";";
                    }
                    else
                    	currPropCommands = "";
                    currPropCommands+= "valueChanged," + pm.getName() + "," + p.getName() + "," + p.getValue().serialize();
                    propCommandsSet.put(pm, currPropCommands); 
                    
                    System.out.println("pvalue changed: " + p.getName() + " " + p.getValue().serialize());
                }

                       
            }

            @Override
            public void propertyReadonlyChanges(PropertyManager pm,
                    Property p, boolean newReadOnly) {           

               

                synchronized (pcsync) {
                	 String currPropCommands = propCommandsSet.get(pm);
                    if (currPropCommands != null && currPropCommands.length() != 0) {
                        currPropCommands += ";";
                    }
                    else
                    	currPropCommands = "";
                    currPropCommands+=  "readOnlyChanged," + pm.getName() + "," + p.getName() + "," + newReadOnly;
                    propCommandsSet.put(pm, currPropCommands); 
                }

                       

            }

            @Override
            public void propertyVisibleChanges(PropertyManager pm,
                    Property p, boolean newVisible) {
              

                synchronized (pcsync) {
                	  String currPropCommands = propCommandsSet.get(pm);
                    if (currPropCommands != null && currPropCommands.length() != 0) {
                        currPropCommands += ";";
                    }
                    else
                    	currPropCommands = "";
                    currPropCommands+=  "visibleChanged," + pm.getName() + "," + p.getName() + "," + newVisible;
                    propCommandsSet.put(pm, currPropCommands);    
                }

                    
            }

            @Override
            public void propertyPublicChanges(PropertyManager pm,
                    Property p, boolean newPublic) {
                // TODO Auto-generated method stub
            }

            @Override
            public void propertyDisabledChanges(PropertyManager pm,
                    Property p, boolean newDisabled) {
                

                synchronized (pcsync) {
                	String currPropCommands = propCommandsSet.get(pm);
                    if (currPropCommands != null && currPropCommands.length() != 0) {
                        currPropCommands += ";";
                    }
                    else
                    	currPropCommands = "";
                    currPropCommands+= "disabledChanged," + pm.getName() + "," + p.getName() + "," + newDisabled;
                    propCommandsSet.put(pm, currPropCommands);   
                }

                    
            }
        };

    }
    
    
    public String pollProps(PropertyManager propManager, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String currPropCommands = "";
        
        
        if (propManager != null) {

            synchronized (pcsync) {
                response.setContentType("text/html");

                currPropCommands = propCommandsSet.get(propManager);
                propCommandsSet.put(propManager, "");

            }
        }

        return currPropCommands;
    }
    
    
    public PropertyManager getPropertyManager(Environment e, String name)
    {
    	if (name == null)
    		return null;
    	
    	Vector<Viewer> viewers = e.getViewers();
    	for (int i=0; i<viewers.size(); i++)
    		if (viewers.get(i).getName().equals(name))
    			return viewers.get(i);
    	
    	Vector<DataSource> ds = e.getDataSources();
    	for (int i=0; i<ds.size(); i++)
    		if (ds.get(i).getName().equals(name))
    			return ds.get(i);
    	
    	return null;   	
    }
  
    public String getProperties(Environment e, PropertyManager pm) {
    	
      	
    	if (pm == null)
    		return "";    	

        String propCommands = "";

        Property[] ps = pm.getProperties();

        for (int i = 0; i < ps.length; i++) {
        	if (i != 0)
        		propCommands += ";";
        	
        	String command = "addProperty," + pm.getName() + "," + ps[i].getName()
                    + "," + ps[i].getValue().typeName() + ","
                    + ps[i].getValue().serialize() + "," + ps[i].getReadOnly() + "," + ps[i].getVisible() + "," + ps[i].getDisabled();
        	
        	propCommands += command;
        	
        	System.out.println("ps" + i + "," + ps.length + " " + command);

       }
      return propCommands;
    }
    
    





}
