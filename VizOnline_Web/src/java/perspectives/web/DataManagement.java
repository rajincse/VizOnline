package perspectives.web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import perspectives.base.DataSource;
import perspectives.base.Environment;
import perspectives.base.Property;

public class DataManagement extends HttpServlet {

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
                if (request.getParameter("page").equals("deleteDataSource")) {

                    String dataSourceName = request.getParameter("dataSourceName");
                    deleteDataSource(e, dataSourceName);
                    outResponse = getDataSourceNames(e);

                    session.setAttribute("environment", e); //reset the environment session

                } else if (request.getParameter("page").equals("getDataFactories")) {
                    //return the dataFactories
                    String dataFactNames = "";
                    for (int i = 0; i < e.getDataFactories().size(); i++) {
                        if (i > 0) {
                            dataFactNames += ",";
                        }
                        dataFactNames += e.getDataFactories().get(i).creatorType();
                    }
                    outResponse = dataFactNames;
                } else if (request.getParameter("page").equals("createDataSource")) {

                    String dataFactoryType = request.getParameter("dataFactoryType");
                    String dataSourceName = request.getParameter("dataSourceName");

                    DataSource ds = null;

                    if (dataFactoryType != null) { //get the dataFactory and its properties
                        for (int i = 0; i < e.getDataFactories().size(); i++) {
                            if (dataFactoryType.equalsIgnoreCase(e.getDataFactories().get(i).creatorType())) {
                                ds = e.getDataFactories().get(i).create(dataSourceName);
                                e.addDataSource(ds, true);
                            }
                        }
                    }

                    outResponse = getDataSourceNames(e);

                    session.setAttribute("environment", e); //reset the environment session

                } else if (request.getParameter("page").equalsIgnoreCase("getDataSourceNames")) {
                    System.out.println("getting datasources");
                    outResponse = getDataSourceNames(e);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            session.setAttribute("environment", e); //reset the environment session

            if (outResponse != null) {

                response.setHeader("Cache-control", "no-cache, no-store");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Expires", "-1");

                out = response.getWriter();
                out.write(outResponse);
                out.flush();
                out.close();
            }
        }

    }

    private void deleteDataSource(Environment e, String d) {
        DataSource ds = getDataSource(e, d);
        if (ds != null) {
            e.deleteDataSource(ds);
        }
    }

    private DataSource getDataSource(Environment e, String d) {
        Vector<DataSource> ds = e.getDataSources();
        for (int i = 0; i < ds.size(); i++) {
            if (ds.get(i).getName().equals(d)) {
                return ds.get(i);
            }
        }
        return null;
    }

    public String getDataSourceNames(Environment e) {
        String dsNames = "";

        for (int i = 0; i < e.getDataSources().size(); i++) {

            if (i != 0) {
                dsNames += ";";
            }

            dsNames += e.getDataSources().get(i).getName();
        }

        return dsNames;

    }

}
