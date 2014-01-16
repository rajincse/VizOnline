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
       
         <script src="jQuery/jquery-1.10.2.js"></script>
        
         <script src="http://code.jquery.com/jquery-latest.min.js"></script>        
        
        <script type="text/javascript" src="ajax.js"></script>
       
    </head>

    <body>

        <div class="container">

            <div class="leftdiv" id="viewers">
                <h1>Create Viewers</h1>                
                <form action="javascript:makeRequest('createViewer')" > 
                    Select Viewer Type:<br></br> 
                    <select  class ="dropdown" name ="selViewers" id ="viewerlist">
                    </select><script> makeRequest('views');</script>
                    <br></br> Select Dataset:<br></br> 
                    <select  class ="dropdown" name ="selData" id ="datalist">
                    </select><script> makeRequest('viewdatas');</script><br></br>
                    <input type="submit" value="Create Viewer" class="small button blue"></form>
                <br></br> <br></br> 
                    <div>
                        <!--                <p><a href="viewer.jsp" target="_blank" class="small button blue">Create</a></p>
                                        <p><a href="viewerT.jsp" target="_blank" class="small button blue">CreateTEST</a></p>-->
                    </div>

                    <!-- end .leftdiv --></div>  

            <div class="rightdiv">
                <h1>Current Viewers</h1>
                <div id="currviewers"><script>makeRequest('currentviewers');</script></div>

            </div>


            <div class="content">
                
                <!-- end .content --></div>

            <!-- end .container --></div>
    </body>
</html>

