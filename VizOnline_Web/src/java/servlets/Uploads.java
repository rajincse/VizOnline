
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileItemFactory;

/**
 *
 * @author Raul Tobo
 */
@WebServlet(name = "Uploads", urlPatterns = {"/Uploads"})
public class Uploads extends HttpServlet {

    protected File destinationDir = null;
    protected PrintWriter out = null;
//    private final String UPLOAD_DIRECTORY = "U:/Desktop/Uploads";
//    private boolean isMultipart;
    private String filePath;
//    private int maxFileSize = 50 * 1024;
//    private int maxMemSize = 4 * 1024;
//    private File file;
//    
    private HashMap<String, String> theDataSources = new HashMap<String, String>();

    public void init() {
        // Get the file location where it would be stored.
        //filePath = getServletContext().getRealPath(getInitParameter("file-upload"));
        filePath = getServletContext().getRealPath("/WEB-INF/Uploads/");


        System.out.println("UPLOADS -- Initialized");


        //Delete existing local files when the upload servlet is started.
        theDataSources = new HashMap<String, String>();
        deleteExistingLocalFiles();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        processRequest(request, response);
    }

    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, java.io.IOException {
        processRequest(request, response);

    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        out = response.getWriter();

        String thepage = request.getParameter("page");

        // System.out.println("THE UPLOAD PAGE REQUEST IS "+ thepage);


        try {


            if (thepage.equalsIgnoreCase("getDatas")) {//request to get the datas
             
                String dataSourceDataNamePairs = "";
                int cnt = 0;
                String value;
                //the key-value pair will be separated by a comma and a semicolon will separate different pairs
                for (String key : theDataSources.keySet()) {
                    value = theDataSources.get(key);
                    if (cnt == 0) {
                        dataSourceDataNamePairs = key + "," + value;
                    } else {
                        dataSourceDataNamePairs += ";" + key + "," + value;
                    }

                    cnt++;

                }

                if (dataSourceDataNamePairs.equals("")) {
                    out.write("No Content");
                } else {
                    //out.write(files);
                    out.write(dataSourceDataNamePairs);
                }
                out.flush();
                out.close();
            } else if (thepage.equalsIgnoreCase("deleteData")) { //request to delete a given data
                
                System.out.println("DELETE DATA REQUEST-----------------");

                String fileName = request.getParameter("del");
                File file = new File(filePath + "\\" + fileName);

                if (file.delete()) {
                    //remove it from the hashmap
                    removeValueFromHashMap(theDataSources, fileName);

                    out.write(file.getName() + "  was deleted successfully!");
                } else {
                    out.write("Delete operation failed.");
                }

            } else if (thepage.equalsIgnoreCase("uploadData")) {

                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                List uploadedItems = null;
                FileItem fileItem = null;

                String myFileName = "";
                // out = response.getWriter();
                //get the dataSourceName


                String dataSourceName = request.getParameter("dataSourceName");


                uploadedItems = upload.parseRequest(request);
                Iterator i = uploadedItems.iterator();
                System.out.println(uploadedItems.isEmpty() + " : " + i.hasNext());
                while (i.hasNext()) {
                    fileItem = (FileItem) i.next();
                    if (fileItem.isFormField() == false) {
                        if (fileItem.getSize() > 0) {
                            File uploadedFile = null;
                            String myFullFileName = fileItem.getName(), slashType = (myFullFileName.lastIndexOf("\\") > 0) ? "\\" : "/";
                            int startIndex = myFullFileName.lastIndexOf(slashType);
                            myFileName = myFullFileName.substring(startIndex + 1, myFullFileName.length());
                            uploadedFile = new File(filePath, myFileName);
                            fileItem.write(uploadedFile);

                            System.out.println("MYFILENAME IS " + myFileName);
                            //out.write(dataSourceName + ";" +myFileName);
                            out.write(myFileName);
                            out.flush();
                            out.close();
                        }
                    }
                    else{
                        String name = fileItem.getFieldName();
                        String value = fileItem.getString();
                        
                        //System.out.println("VALUESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS "+ name + "      -         "+ value);
                    }
                }

               System.out.println("DataSource Name  and file name " + dataSourceName + "-" + myFileName);

                //Put the name of the data in the hashmap
                theDataSources.put(dataSourceName, myFileName);
                
                
                
                 /*//now Send request to the VizOnlineServlet to update the PFile property
                
                String url = "VizOnlineServlet?updateProperty&newValue=";*/
                        
                         //split the file path and get the filename which will be the last item
    /*var slashType;
    if(propValue.indexOf("/") >= 0){
        slashType =  "/";
     }
    else if(propValue.indexOf("\\") >=0){
        slashType = "\\";
     }
    
    
    var pathSplit = propValue.split(slashType)
         //get the fileName;
    propValue = pathSplit[pathSplit.length-1];
    
    
    //update PFile property
    var factoryType = document.getElementById("factoryType").value;
    var factoryItemName = document.getElementById("factoryItemName").value;
       
    var url = "="+propValue+"&property="+propName;
        url +="&factoryType="+factoryType+"&factoryItemName="+factoryItemName;
        
    makeRequest(url);*/
                
                
                

            }



        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void deleteExistingLocalFiles() {
        String files = "";

        File folder = new File(filePath);
        File[] listOfFiles = folder.listFiles();
        String filename;
        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].isFile()) {
                filename = listOfFiles[i].getName();
                //delete the file
                if (listOfFiles[i].delete()) {
                    System.out.println("File " + filename + " successfully deleted");
                    //return false if you cannot delete any of those files
                    //return false;
                } else {
                    System.out.println("File " + filename + "Not Deleted");
                }
            }
        }
    }

    public void removeValueFromHashMap(HashMap<String, String> hashmap, String value) {

       
        for (String key : hashmap.keySet()) {
            if (hashmap.get(key).equalsIgnoreCase(value)) {
                hashmap.remove(key);
                break;
            }
        }

    }
}