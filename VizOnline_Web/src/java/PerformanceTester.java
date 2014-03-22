import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import javax.imageio.ImageIO;

import org.xml.sax.SAXException;

import perspectives.base.EventManager;
import perspectives.base.PEvent;
import perspectives.base.Property;
import perspectives.base.Task;
import perspectives.properties.PInteger;
import perspectives.two_d.Viewer2D;
import perspectives.web.InitServlet;
import perspectives.web.ViewerManagement;

import com.meterware.httpunit.*;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;



public class PerformanceTester extends Viewer2D{
	
	String root = "http://vizlab.cs.fiu.edu/vizonline_rr/";
	//String root = "http://localhost:8080/vizonline/";
	
	EventManager em; 
	
	String vn;
	WebConversation wc;
	
	BufferedImage image;
	
	int nrt = 2;
	int refreshTime = 50;

	public PerformanceTester(String name) {
		super(name);
		
		em = new EventManager();
		
		try
		{
			addProperty(new Property<PInteger>("Measures", new PInteger(0))
					{

						@Override
						protected boolean updating(PInteger newvalue) {
							WebRequest request = new GetMethodWebRequest(root + "PerformanceTestServlet");
							request.setParameter("page", "measures");
							
							try{
							WebResponse   wr = wc.getResponse( request);
							}
							catch(Exception e)
							{
								
							}
							return true;
						}
				
					}
					);
		}
		catch(Exception e)
		{
			
		}
	  
		try {
			
			wc = new WebConversation();
			WebRequest request = new GetMethodWebRequest(root + "PerformanceTestServlet");
			request.setParameter("page", "newViewer");			
			WebResponse   wr = wc.getResponse( request);
			vn = wr.getText();
			
			
			em.scheduleEvent(new PEvent()
			{

				@Override
				public void process() {	
					
					WebRequest request = new GetMethodWebRequest(root + "PerformanceTestServlet");
					request.setParameter("viewer", vn);		
					request.setParameter("page", "startTesting");			
					try {
						WebResponse wr = wc.getResponse( request);
					} catch (Exception e) {					
						e.printStackTrace();
					} 
					
					getTilesProcess();					
				}
				
			}
			,500);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	int tc = 0;
	boolean gettingTiles = false;
	Object o1 = new Object();
	Object o2 = new Object();
	long lastGetTiles = -1;
	int requests = 0;
	long startreq = -1;
	
	WebConversation[][] wcs;
	
	
	

	public void getTiles(String vn, WebConversation wc)
	{
		
		System.setProperty("http.maxConnections","100000"); //has no effect
		System.setProperty("http.keepAlive","false"); //has no effect
		

		long tt = new Date().getTime();
		if (startreq == -1)
			startreq = tt;
		//System.out.println("time between gettiles: " + (tt - lastGetTiles)+"; number of requests/sec: "  +((1000.*requests)/(tt-startreq)) + "(should be: " + nrt*nrt*(1000/refreshTime) + ")");
		lastGetTiles = tt;
		synchronized(o1){
		if (gettingTiles) return;		
			gettingTiles=true;
		}
		
		if (wcs == null)
		{
			wcs = new WebConversation[nrt][];
			for (int i=0; i<wcs.length; i++)
				wcs[i] = new WebConversation[nrt];
		}
		
		tc=0;	
		for (int x = 0; x<nrt; x++)
			for (int y=0; y<nrt; y++)
			{
				final WebRequest request = new GetMethodWebRequest(root + "PerformanceTestServlet");
				request.setParameter("page", "tile");
				request.setParameter("viewer", vn);
				request.setParameter("x", ""+x);
				request.setParameter("y", ""+y);
				
				final int xf = x;
				final int yf = y;
				
				
				final WebConversation wcf = wc;
				
				Task t = new Task("t")
				{

					@Override
					public void task() {
						
						try{
							
							if (wcs[xf][yf] == null)
								wcs[xf][yf] = new WebConversation();
							
						
							wcs[xf][yf].clearContents();
							wcs[xf][yf].clearProxyServer();
							
							WebResponse wr = wcs[xf][yf].getResponse( request);			

							image = ImageIO.read(wr.getInputStream());
						
							requests++;
							
							requestRender();
							
							synchronized(o2){
							tc++;
							getTilesDone();
							done();
							}
						}
						catch(Exception e)
						{
							synchronized(o2){
							tc++;
							getTilesDone();
							done();}
						}
					}
					
				};
				t.start();
				
			}	
	}
	
	public void getTilesDone()
	{
		if (tc == nrt*nrt)
			synchronized(o1){
			gettingTiles = false;}
	}
	
	public void getTilesProcess()
	{
		em.replaceEvent(new PEvent()
		{

			
			
			@Override
			public void process() {
			
					getTiles(vn, wc);	
					getTilesProcess();
			}
			
		}, "gettiles", refreshTime);
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(image,  0, 0, null);
		
	}

	@Override
	public void simulate() {
		// TODO Auto-generated method stub
		
	}
	
	

}
