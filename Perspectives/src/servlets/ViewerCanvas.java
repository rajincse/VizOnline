package servlets;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Graph.GraphDataFactory;
import Graph.GraphViewerFactory;
import HeatMap.HeatMapViewerFactory;
import ParallelCoord.ParallelCoordinateViewerFactory;
import brain.BrainDataFactory;
import brain.BrainStatsD3ViewerFactory;
import brain.BrainViewerFactory;
import d3.D3Viewer;
import d3.GraphD3ViewerFactory;
import data.TableDataFactory;

import perspectives.DataSource;
import perspectives.Environment;
import perspectives.PEvent;
import perspectives.PropertyManager;
import perspectives.Viewer;
import perspectives.ViewerContainer;
import perspectives.ViewerFactory;

public class ViewerCanvas extends HttpServlet {
	
	boolean dragging = false;
	
	   public void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws IOException, ServletException {
	        processRequest(request, response);
	    }

	    @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        processRequest(request, response);
	    }
		
		
		   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
		            throws ServletException, IOException {
			   
		        PrintWriter out;
		        String outResponse = null;
		        HttpSession session = request.getSession();
		        String message = "";

		        Environment e = null;
		        //get the environment from the session or create a new one if the session doesn't have the environment initialized
		        if (session.getAttribute("environment") == null) {
		          

		        } else {
		            e = (Environment) session.getAttribute("environment");
		            message = "Environment already exists";
		       
			        try {
			        	
			        
		            if (request.getParameter("page").equals("mouseEvent")) {
		            	
		            	//System.out.println("processing moouse events");
		            	
		                response.setContentType("text/html");
		                response.setHeader("Cache-control", "no-cache, no-store");
		                response.setHeader("Pragma", "no-cache");
		                response.setHeader("Expires", "-1");
		                          
		                String viewerName = request.getParameter("viewerName");
		                String cmd = request.getParameter("cmd");
		                String xs = request.getParameter("mx");
		                String ys = request.getParameter("my");
		                String bs = request.getParameter("mb");
		                
		             //   System.out.println("pme:  " + viewerName + " , " + cmd + " , " + xs + " , " + ys);
		               
		                if (viewerName != null && cmd != null && xs != null && ys != null)
		                {
		                	ViewerContainer vc = getViewerContainer(e, viewerName);
		                	if (vc != null)
		                		mouseEvent(vc, cmd, xs, ys, bs);
		                }
		                session.setAttribute("environment", e); //reset the environment session 
		            } 
		            
		            else if (request.getParameter("page").equals("imageUpdate")) {
		            	String viewerName = request.getParameter("viewerName");
		            	
		            	//System.out.println("processing image update event: " + viewerName);
		            	
		                if (viewerName != null)
		                {
		                	ViewerContainer vc = getViewerContainer(e, viewerName);
		                	if (vc != null)
		                		imageUpdate(vc, request, response);
		                }
		                session.setAttribute("environment", e); //reset the environment session 
		            }
		            
		            else if (request.getParameter("page").equals("dataUpdate")) {
	
		            	String viewerName = request.getParameter("viewerName");
		                if (viewerName != null)
		                {
		                	ViewerContainer vc = getViewerContainer(e, viewerName);
		                	if (vc != null)
		                		dataUpdate(vc, request, response);
		                }
		                session.setAttribute("environment", e); //reset the environment session 
		               
		            } 
		            else if (request.getParameter("page").equals("resize")) {
		            	
		            	String viewerName = request.getParameter("viewerName");
		            	int width = Integer.parseInt(request.getParameter("width"));
		            	int height = Integer.parseInt(request.getParameter("height"));
		            	
		            	System.out.println("resizing: " + width + " " + height);
		                if (viewerName != null)
		                {
		                	ViewerContainer vc = getViewerContainer(e, viewerName);
		                	if (vc != null)
		                	{
		                		vc.resize(width, height);		                	             		
		                		vc.resetTiles();
		                	}
		                	outResponse = "";
		                }
		                session.setAttribute("environment", e); //reset the environment session 
		               
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
	
	   //Method to Obtain Images from Perspectives
	   
	public ViewerContainer getViewerContainer(Environment e, String name)
	{
		    	Vector<ViewerContainer> viewers = e.getViewerContainers();
		    	for (int i=0; i<viewers.size(); i++)
		    		if (viewers.get(i).getViewer().getName().equals(name))
		    			return viewers.get(i);
		    			    	
		    	return null;   	
	}
	
	public void mouseEvent(ViewerContainer vc, String cmd, String xs, String ys, String button)
	{
        int x = Integer.parseInt(xs);
        int y = Integer.parseInt(ys);
        
     

        boolean leftMouse = false;
        if (button.trim().equalsIgnoreCase("left")) {
            leftMouse = true;
        }
        
      //  System.out.println("cmd: " + cmd);

        if (cmd.equalsIgnoreCase("mouseUp"))
        {
            if (leftMouse)
                vc.mouseReleased(x, y, MouseEvent.BUTTON1);
            else 
            	vc.mouseReleased(x, y, MouseEvent.BUTTON3);            
        } 
        
        else if (cmd.equalsIgnoreCase("mouseDown"))
        {
        	//System.out.println("mousedown + " + x  + " " + y + "  " + leftMouse);
            if (leftMouse) 
            	vc.mousePressed(x, y, MouseEvent.BUTTON1);
            else
            	vc.mousePressed(x, y, MouseEvent.BUTTON3);
        } 
        
        else if (cmd.equalsIgnoreCase("mouseMoved")) {
           // System.out.println("MouseMoved");
            vc.mouseMoved(x, y);    
        } 
        
        else if (cmd.equalsIgnoreCase("mouseDragged")) {
        	
			if (dragging) return;
			
			final int xf = x;
			final int yf = y;
			
			dragging = true;
			
			final ViewerContainer vcf = vc; 
		
			vc.getViewer().em.scheduleEvent(new PEvent()
			{
				public void process() {
					vcf.mouseDragged(xf,yf);	
					vcf.lastMouseMove = new Date().getTime();
					vcf.getViewer().em.scheduleEvent(new PEvent()
					{
						public void process() {
							vcf.renderTooltip();
						}								
					}, vcf.getViewer().getTooltipDelay());
					
					dragging = false;
				}						
			});
        }       
     
	}

    public void imageUpdate(ViewerContainer vc, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        
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

       byte[] bim = vc.getTile(tx, ty);
       this.sendImage(bim, response);

       long t2 = (new Date()).getTime();
    }
    
    
    public void dataUpdate(ViewerContainer vc, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        HttpSession session = request.getSession();

        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");

            D3Viewer viewer = (D3Viewer) vc.getViewer();
            PrintWriter out = response.getWriter();
            out.println(viewer.updateData(false));

    }
    
    public void sendImage(byte[] bs, HttpServletResponse response) {
        

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


}
