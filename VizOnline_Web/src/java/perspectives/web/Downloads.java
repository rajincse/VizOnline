/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package perspectives.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mershack
 */
public class Downloads extends HttpServlet {

    private String downloadPath;
    private int cnt = 0;

    public void init() {
        // Give a default name to the file to be downloaded
        downloadPath = "/WEB-INF/Downloads/";

        //delete the existing files in the Downloads folder when the servlet initializes
        deleteExistingLocalFiles();
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // response.setContentType("text/html;charset=UTF-8");
        try {


            String thepage = request.getParameter("page");

            if (thepage.equalsIgnoreCase("updateDownloadPath")) {
                String fileName = "download_" + cnt + ".txt";
                String filePath = getServletContext().getRealPath(downloadPath + fileName);
                cnt++;

                //send redirect to the vizonline servlet to update the property value
                String factoryType = request.getParameter("factoryType");
                String propertyName = request.getParameter("property");
                String factoryItemName = request.getParameter("factoryItemName");

                String url = "VizOnlineServlet?page=updateProperty&newValue=" + filePath
                        + "&property=" + propertyName + "&factoryType=" + factoryType
                        + "&factoryItemName=" + factoryItemName
                        + "&fileName=" + fileName;  


                response.sendRedirect(url);
            }
            else if(thepage.equalsIgnoreCase("downloadData")){
               String fileName = request.getParameter("fileName");
               
               response.setContentType("Application/octet-stream");
               response.setHeader("Content-Disposition", "attachment;filename=" +fileName);
               
                InputStream is =  getServletContext().getResourceAsStream(downloadPath+fileName);
                
                int read=0;
                int BYTES_DOWNLOAD = 1024;
                byte[] bytes = new byte[BYTES_DOWNLOAD];
               
                OutputStream os = response.getOutputStream();
                
                while((read = is.read(bytes))!=-1){
                   os.write(bytes, 0, read);
                }
                
                is.close();
                
                os.flush();
                os.close();
                
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public void deleteExistingLocalFiles() {
        String files = "";

        String filePath = getServletContext().getRealPath(downloadPath);
        File folder = new File(filePath);
        File[] listOfFiles = folder.listFiles();
        String filename;
        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].isFile()) {
                //delete the file
                listOfFiles[i].delete();

            }
        }
    }
}
