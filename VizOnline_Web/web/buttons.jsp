<%-- 
    Document   : index
    Created on : Aug 13, 2013, 5:28:11 PM
    Author     : Raul Tobo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
            <a href="#"><img src="Images/VizOnline.png" alt="Logo" name="Logo" width="350" height="109" id="Insert_logo" float=left;" /></a>  

            <!-- end .header -->

            <div class="nav">                

                <a rel="tab" href="index.jsp" class="large button blue">HOME</a>
                <a rel="tab" href="data.jsp" class="large button blue">DATA SETS</a>
                <a rel="tab" href="viewers.jsp" class="large button blue">VIEWERS</a>
                <a rel="tab" href="links.jsp" class="large button blue">LINKS</a>
            </div>



            <!-- end .container --></div>
    </body>
</html>
