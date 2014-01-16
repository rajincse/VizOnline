
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

    public void init() {
        // Get the file location where it would be stored.
        //filePath = getServletContext().getRealPath(getInitParameter("file-upload"));
        filePath = getServletContext().getRealPath("/WEB-INF/Uploads/");
       
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List uploadedItems = null;
        FileItem fileItem = null;

        out = response.getWriter();
        try {
            uploadedItems = upload.parseRequest(request);
            Iterator i = uploadedItems.iterator();
            System.out.println(uploadedItems.isEmpty() + " : " + i.hasNext());
            while (i.hasNext()) {
                fileItem = (FileItem) i.next();
                if (fileItem.isFormField() == false) {
                    if (fileItem.getSize() > 0) {
                        File uploadedFile = null;
                        String myFullFileName = fileItem.getName(), myFileName = "", slashType = (myFullFileName.lastIndexOf("\\") > 0) ? "\\" : "/";
                        int startIndex = myFullFileName.lastIndexOf(slashType);
                        myFileName = myFullFileName.substring(startIndex + 1, myFullFileName.length());
                        uploadedFile = new File(filePath, myFileName);
                        fileItem.write(uploadedFile);
                        out.write(myFileName);
                        out.flush();
                        out.close();
                    }
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, java.io.IOException {

        out = response.getWriter();
        if (request.getParameter("del") == null) {
            String files = "";
            File folder = new File(filePath);
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {

                if (listOfFiles[i].isFile()) {
                    if (files.equals("")) {
                        files = listOfFiles[i].getName();
                    } else {
                        files = files + "," + listOfFiles[i].getName();
                    }
                }
            }
            if (files.equals("")) {
                out.write("No Content");
            } else {
                out.write(files);
            }
            out.flush();
            out.close();
        } else {
            try {
                
                System.out.println("The file " + filePath + request.getParameter("del"));

                File file = new File(filePath + "\\" + request.getParameter("del"));

                if (file.delete()) {
                    out.write(file.getName() + "  was deleted successfully!");
                } else {
                    out.write("Delete operation is failed.");
                }

            } catch (Exception e) {

                e.printStackTrace();

            }
        }
    }
}