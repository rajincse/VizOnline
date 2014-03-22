

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import perspectives.base.Environment;
import perspectives.base.Viewer;
import perspectives.graph.GraphData;
import perspectives.properties.PBoolean;
import perspectives.properties.PFileInput;
import perspectives.properties.PInteger;

public class PerformanceTestServlet extends HttpServlet {
	Environment e; 
	int nrViewers = 0;
	GraphData graphdata;
	
	long servletstarted = -1;
	long reqcount = 0;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	
    	
    	reqcount++;
    	
    	if (servletstarted >= 0) //this will be true always except while setting up the viewers and such
    	{  
    		if (reqcount % 1000 == 0)
    		System.out.println(reqcount + " requests; " + (new Date().getTime() - servletstarted ) + " time; " + (1000*(double)reqcount/(new Date().getTime() - servletstarted )) + " req/sec;");
 
        	//even if I return here then it works
        	//response.getWriter().write("out");
        	//if (true)
        	//	return;
    	}      
    	
    
    	    		
    		String page = request.getParameter("page");
    		
    		String outResponse = null;

    		
    		if (page != null && page.equals("newViewer"))
    		{
    			PerformanceTestViewer v = new PerformanceTestViewer("v"+nrViewers++, graphdata);
    			v.setPositionsFile(getServletContext().getRealPath("/WEB-INF/Uploads/edgelist_positions.txt"));
    			e.addViewer(v);
    			System.out.println("new number of viewres: " + e.getViewers().size());    			
    			outResponse = v.getName();
    			 response.setContentType("text/html");
    		}
    		else if (page!= null && page.equals("tile"))
    		{
    			if (servletstarted < 0) servletstarted = new Date().getTime();
    			
    			
    			
    			String vn = request.getParameter("viewer");
    			int x = Integer.parseInt(request.getParameter("x"));
    			int y = Integer.parseInt(request.getParameter("y"));
    			
    	        response.setContentType("image/png");
    	         
    	        
    	       // response.setHeader("Cache-control", "no-cache, no-store");
    	     //   response.setHeader("Pragma", "no-cache");
    	     //   response.setHeader("Expires", "-1");
    			
    			Vector<Viewer> vs = e.getViewers();
    			for (int i=0; i<vs.size(); i++)
    			{
    				if (vs.get(i).getName().equals(vn))
    				{
    					byte[] bs = e.getViewerContainers().get(i).getTile(x,y);
    					 response.getOutputStream().write(bs);     		                
    		           // response.flushBuffer();
    		          
    		            break;
    		       
    				}
    			}
    		}
    		else if (page != null && page.equals("startTesting"))
    		{
    			System.out.println("in start testing");
    			String vn = request.getParameter("viewer");
    			Vector<Viewer> vs = e.getViewers();
    			for (int i=0; i<vs.size(); i++)
    			{
    				System.out.println("comparing " + vs.get(i).getName() + " against " + vn);
    				if (vs.get(i).getName().equals(vn))
    				{
    					System.out.println("starting test on viewer " + vs.get(i).getName());
    					vs.get(i).getProperty("Testing").setValue(new PBoolean(true));
    				}
    			}
    		}
    		
    		else if (page != null && page.equals("measures"))
    		{
    			Vector<Viewer> vs = e.getViewers();
    			for (int i=0; i<vs.size(); i++)
    				vs.get(i).getProperty("Measure").setValue(new PInteger(0));
    		}
    		
    		if (outResponse != null)
    		{
    		 PrintWriter out;    		
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
     
    	System.out.println("create performance test environment");
    	e = new Environment(true);
    	
		System.out.println("here");
		graphdata = new GraphData("graphdata");
		PFileInput f = new PFileInput();
		f.path = getServletContext().getRealPath("/WEB-INF/Uploads/edgelist.txt");
		 
		System.out.println("here2");
		e.addDataSource(graphdata, true);
		graphdata.getProperty("Graph File").setValue(f);
		System.out.println("here3");
    }

   
 

}
