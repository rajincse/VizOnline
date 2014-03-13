package perspectives.web;



import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.util.Hashtable;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import perspectives.base.Environment;
import perspectives.base.ViewerContainer;

/**
 *
 * @author mokoe001
 */
public class SessionListener implements HttpSessionListener {
    
    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
        //totalActiveSessions++;
         HttpSession session = sessionEvent.getSession();
         //System.out.println(" User Session ID "+ session.getId() + " has been CREATED");
         session.setAttribute("sessionID", session.getId());
       //  session.setAttribute("sessionCnt", 0);
         System.out.println("The session ID has been created  and sessionID is "+ session.getId());
      //   Environment e = new Environment(true);
       //  session.setAttribute("environment", );
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
       
        HttpSession session = sessionEvent.getSession();        
        
    //    Environment e = (Environment) session.getAttribute("environment");
        
        /*
        if(e == null){
            System.out.println("The environment session is null");
        }else{
             System.out.println("The environment session is not null");
        }
        */
        
        //getViewerContainer  and terminate Thread
      
        session.removeAttribute("sessionID");
        session.removeAttribute("sessionCnt");
        session.removeAttribute("environment");
        
         System.out.println("sessionDestroyed - ************************************  ----> ");       
    }
}
