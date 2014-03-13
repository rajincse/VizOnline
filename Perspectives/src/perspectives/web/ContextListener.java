package perspectives.web;

import java.util.Hashtable;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import perspectives.base.Environment;

public class ContextListener implements ServletContextListener {
    
    Hashtable<InitServlet, Environment> envs = null;

    public void contextInitialized(ServletContextEvent sce) {
    	System.out.println("creating the vizonline context");
    	envs = new Hashtable<InitServlet, Environment>();
    	
    	sce.getServletContext().setAttribute("envs", envs);
    	
    }

    public void contextDestroyed(ServletContextEvent sce){
    	System.out.println("clearing the vizonline context");
    	((Hashtable<InitServlet, Environment>)sce.getServletContext().getAttribute("envs")).clear();
    }

}
