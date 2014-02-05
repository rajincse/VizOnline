
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileItemFactory;
import perspectives.Environment;

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
    //private String filePath;
//    private int maxFileSize = 50 * 1024;
//    private int maxMemSize = 4 * 1024;
//    private File file;
    private String uploadsPath;
    private HashMap<String, String> theDataSources = new HashMap<String, String>();

    public void init() {
        // Get the file location where it would be stored.
        //filePath = getServletContext().getRealPath(getInitParameter("file-upload"));
        //filePath = getServletContext().getRealPath("/WEB-INF/Uploads/");

        uploadsPath = "/WEB-INF/Uploads/";


        //System.out.println("UPLOADS -- Initialized");


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

        HttpSession session = request.getSession();

        Environment e = null;


        if (session.getAttribute("environment") != null) { //do this only if you can get the user's session variable

            try {

                e = (Environment) session.getAttribute("environment");

                ArrayList<String> dsNames = getDataSourceNames(e);



                if (thepage.equalsIgnoreCase("getDatas")) {//request to get the datas

                    int cnt = 0;
                    String dataSourceDataNamePairs = "", value;
                    for (String name : dsNames) {
                        if (theDataSources.get(name) != null) {
                            value = theDataSources.get(name);
                            if (cnt == 0) {
                                dataSourceDataNamePairs = name + "," + value;
                            } else {
                                dataSourceDataNamePairs += ";" + name + "," + value;
                            }

                            cnt++;
                        }

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

                    String dataSourceName = request.getParameter("dataSourceName");
                    String fileName = theDataSources.get(dataSourceName);

                    String filePath = getServletContext().getRealPath(uploadsPath + fileName);

                    //File file = new File(filePath + "\\" + fileName);
                    File file = new File(filePath);

                    if (file.delete()) {
                        //remove it from the hashmap
                        removeValueFromHashMap(fileName);

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

                    //get the dataSourceName
                    String factoryItemName = request.getParameter("factoryItemName");
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

                                String uploadsDirPath = getServletContext().getRealPath(uploadsPath);
                                uploadedFile = new File(uploadsDirPath, myFileName);
                                fileItem.write(uploadedFile);
                                //out.write(dataSourceName + ";" +myFileName);
                                //out.write(myFileName);
                                //out.flush();
                                ///    out.close();
                            }
                        } else {
                            /*String name = fileItem.getFieldName();
                             String value = fileItem.getString(); */
                        }
                    }

                    //Put the name of the data in the hashmap
                    theDataSources.put(factoryItemName, myFileName);

                    String filePath = getServletContext().getRealPath(uploadsPath + myFileName);

                    //send redirect to the vizonline servlet to update the property value
                    String factoryType = request.getParameter("factoryType");
                    String propertyName = request.getParameter("property");

                    String url = "VizOnlineServlet?page=updateProperty&newValue=" + filePath
                            + "&property=" + propertyName + "&factoryType=" + factoryType
                            + "&factoryItemName=" + factoryItemName
                            + "&fileName=" + myFileName;

                    System.out.println("UPLOADING ....... url: " + url);


                    response.sendRedirect(url);

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }

    public void deleteExistingLocalFiles() {
        String files = "";

        String filePath = getServletContext().getRealPath(uploadsPath);

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

    public void removeValueFromHashMap(String value) {
        Set<String> keys = theDataSources.keySet();
        ArrayList<String> keysToBeDeleted = new ArrayList<String>();

        for (String key : keys) {
            //remove all dataSources that are linked to the file name
            String keyValue = theDataSources.get(key);
            if (keyValue.equalsIgnoreCase(value)) {
                keysToBeDeleted.add(key);
            }
        }
        //NB: I used 2 steps to remove the key from the hashmap because I was getting ConcurrentModification error on the hashmap
        for (String key : keysToBeDeleted) {
            theDataSources.remove(key);
        }

    }

    public ArrayList<String> getDataSourceNames(Environment e) {
        ArrayList<String> dataSourceNames = new ArrayList<String>();

        for (int i = 0; i < e.getDataSources().size(); i++) {
            dataSourceNames.add(e.getDataSources().get(i).getName());
        }

        return dataSourceNames;
    }
}