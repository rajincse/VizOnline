<%-- 
    Document   : index
    Created on : Aug 13, 2013, 5:28:11 PM
    Author     : Raul Tobo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include  file="buttons.jsp" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Perspectives Online</title>
        <link href="Styles.css" rel="stylesheet" type="text/css" /><!--[if lte IE 7]>
        <style>
        .content { margin-right: -1px; } /* this 1px negative margin can be placed on any of the columns in this layout with the same corrective effect. */
        ul.nav a { zoom: 1; }  /* the zoom property gives IE the hasLayout trigger it needs to correct extra whiltespace between the links */
        </style>
        <![endif]-->
        <script src="ajax.js"></script>
    </head>

    <body>
        <div class="container">

            <div class="sidebar1">
                <h2 align="center">Welcome to Perspectives!</h2>
                <script>makeRequest('home');</script>        
                
                <p align="center">                
                   Perspectives is a framework that facilitates data visualization.<br>
                   It will allow data files, called Data Sets, to be uploaded and then displayed in different interactive "viewers".<br>
                </p>
            <!-- end .sidebar1 --></div>
            <div class="leftdiv">
                <h2 align="center">Getting Started<br></h2> 
                <p> <br>
                   1. Create a Data Set by clicking the "DATA SETS" link above.<br>
                   2. Upload a valid data file.<br>                   
                   3. Create a Viewer by clicking the "VIEWERS" link above.<br>
                   4. Select an appropriate Viewer type and the data file previously uploaded.<br>
                   5. Create and Launch the Viewer!<br><br>

                   Once the interactive viewer is launched, it will allow:<br><br>
                   - Changing of properties<br>
                   - Zooming in/out of the image (right mouse drag)<br> 
                   - Dragging the image (left mouse drag)<br>
                    ... and depending on the type of viewer, <br>
                    simulations and node interaction may also be available.<br><br>

                </p>
            <p>Current Environment status: </p>
            <p id="epar"></p>

            <!-- end .leftDiv --></div>

            <div class="rightdiv">
                <h2 align="center">Example Viewers<br><br></h2>
                <h1>Graph Viewer</h1>
                <img src="Images/graphviewer2.png" alt="Example Graph Viewer" height="250" width="550"> 
                <h1>Heat Map</h1>
                <img src="Images/heatmap.png" alt="Example Heat Map" height="250" width="550"> 
            <!-- end .rightdiv --></div>

        <!-- end .container --></div>
    </body>
</html>
