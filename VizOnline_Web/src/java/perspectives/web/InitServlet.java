package perspectives.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import perspectives.base.Environment;


public abstract class InitServlet extends HttpServlet {

    
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
            
            System.out.println("Creating new environment ---------------");
            
            e.setLocalDataPath(getServletContext().getRealPath("data/"));
            
            environmentInit(e); 

            propsInit();    //call the propsInit again for new sessions.

            message = "Environment has been Initialized ---------------";
            
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
    	Hashtable<InitServlet, Environment> envs = (Hashtable<InitServlet, Environment>) this.getServletContext().getAttribute("envs");
       
    	Environment e = new Environment(true);

        envs.put(this, e);
        this.getServletContext().setAttribute("key", this);  //set the key of the attribute

     

    }
    
    
    public abstract void environmentInit(Environment e);

 
	

}
