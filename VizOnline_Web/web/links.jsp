<%-- 
    Document   : viewers
    Created on : Oct 19, 2013, 12:05:27 PM
    Author     : Raul Tobo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="buttons.jsp" %>
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
        <script type="text/javascript" src="ajax.js"></script>
        
        <script src="jQuery/jquery-1.10.2.js"></script>
        
    </head>

    <body>

        <div class="container">

            <div class="leftdiv" id="links">
                <h1>Select First Viewer</h1>
                <form action="javascript:makeRequest('linkViewers')"> 
                    Select First Viewer:<br></br> 
                    <select  class ="dropdown" name ="selViewers" id ="vlist1">
                    </select><script> makeRequest('currentViewersLink1');</script>
                    <br></br> Select Second Viewer:<br></br> 
                    <select  class ="dropdown" name ="selData" id ="vlist2">
                    </select><script> makeRequest('currentViewersLink2');</script><br></br>
                    <input type="submit" value="Link Viewers" class="small button blue"></form>
                <br></br> <br></br> 
            </div>

            <div class="rightdiv">
                <h1>Current Linked Viewers</h1>
                <div id="currlinks"><script>makeRequest('currentLinks');</script></div>

            </div>



            <!-- end .content --></div>

        <!-- end .container --></div>
    </body>
</html>

