<%-- 
    Document   : data
    Created on : Oct 19, 2013, 12:32:48 PM
    Author     : Raul Tobo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="buttons.jsp" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Perspectives Online</title>
        <link href="Styles.css" rel="stylesheet" type="text/css" />
        <!--        <style>
                .content { margin-right: -1px; } /* this 1px negative margin can be placed on any of the columns in this layout with the same corrective effect. */
                ul.nav a { zoom: 1; }  /* the zoom property gives IE the hasLayout trigger it needs to correct extra whiltespace between the links */
                </style>-->
        <script src="datalist.js"></script>
        <script src="jQuery/jquery-1.10.2.js"></script>
        <script type="text/javascript" src="ajax.js"></script>
        <script>
            window.onload = function() {
                var iframe = document.getElementById("iframeid"),
                textBox = document.getElementById("textBox");
                iframe.onload = function() {
                /* parsing the data i.e. send from server side */
                var filePath = (iframe.contentWindow.document).getElementsByTagName('pre')[0].childNodes[0].nodeValue;
                alert("file is saved at your disk " + "\n" + filePath);

                }
            }</script>
    </head>

    <body>
        <div class="container">

            <div class="leftdiv">
                <h1>Create Dataset</h1>
<!--                <input type="Text" id="dname" placeholder="Enter Name" size="28" maxlength="28"> 
                <button onClick="popUp()" class="small button blue">Create</button>
                <div id="cdata" class="popup">
                    <h1 id="Title"> </h1>                -->
                <form action="javascript:makeRequest('upload')" enctype="multipart/form-data" method="post">
                    <input id ="theFile" type="file" name="fileTxtUpload">
                    <input type="submit" value="Upload" class="small button blue">
                    </form>               
                    
<!--                    <input type="file" id ="theFile" name="File"/>
                    <button onClick="makeRequest('upload')" name ="upbttn" class="blue button small">Upload</button>-->
<!--                    <iframe name="iframe" id="iframeid" width="0" height="0" style="visibility: hidden">  </iframe>
                    <button onClick="closePopup()" class="small button blue">Cancel</button>-->
                </div>               

            

            <div class="rightdiv">
                <h1>Current Datasets</h1>
                <!--<input type ="button" class="small button blue" onclick="makeRequest('data')" name ="bt1" value="Display Data">-->
                <div id="cdatas"><script>makeRequest('data');</script></div>
                </div>
            
            
        </div>

    </body>
</html>

