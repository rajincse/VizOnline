<%-- 
    Document   : viewer
    Created on : Jan 7, 2014, 2:13:44 AM
    Author     : Mershack
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>The Viewer</title>

        <title>A Viewer with Properties</title>
        <link href="Styles.css" rel="stylesheet" type="text/css" />
        <script src="jQuery/jquery-1.10.2.js"></script>
        <script src="http://code.jquery.com/jquery-latest.min.js"></script>
        <script type="text/javascript" src="jscolor/jscolor.js"></script>
        <!--      <script type="text/javascript" src="proplist.js"></script> -->
        
         <!--script and style for jquery slider and spinner -->
        <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
        <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
        <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
        
        
        
        
        <script type="text/javascript" src="ajax.js"></script>


        <script type="text/javascript">
            window.onload = function() {
                makeRequest('properties');

                firstTime();

                var xMargin = 300;
                var yMargin = 10;

                var ims = [];

                var dragFlag = 0;
                var myString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                var randLength = 6;
                var randStr = "";
                var theUrl = "";
                var cnt = 0;
                var prop_cnt = 0;

                var mousePos,
                        posDisplay = document.getElementById("posDisplay");
                // counterDisplay = document.getElementById("counterDisplay"),
                // counter = 0;

                var mouseType = "left";
                document.getElementById("canv").onmousedown = handleMouseDown; //to handle mouse-clicks
                document.getElementById("canv").onmouseup = handleMouseUp;
                document.getElementById("canv").onmousemove = handleMouseMove;

                var prevx = 0;
                var prevy = 0;
                var prevtime = 0;
                var starttime = (new Date()).getTime();
                var rendercnt = 0;
                var prevtimerender = starttime;

                var cttt = 0;
                var ttt = 0;
                var loading = 0;


                var inmousemove = false;

                var ts = 0;

                var interval = setInterval(clientImageUpdate, 100);

                setInterval(pollprops, 500);

                window.oncontextmenu = handleContextMenu;
                window.onunload = myUnloadFunction;

                var counter = 0;


                function fimloaded(images) {



                    if (images[0].loaded && images[1].loaded && images[2].loaded && images[3].loaded)
                    {
                        document.getElementById("canv").appendChild(images[0]);
                        images[0].style.width = "500px";
                        images[0].style.height = "350px";
                        images[0].style.position = "absolute";
                        images[0].style.top = "0px";
                        images[0].style.left = "0px";

                        document.getElementById("canv").appendChild(images[1]);
                        images[1].style.width = "500px";
                        images[1].style.height = "350px";
                        images[1].style.position = "absolute";
                        images[1].style.top = "0px";
                        images[1].style.left = "500px";

                        document.getElementById("canv").appendChild(images[2]);
                        images[2].style.width = "500px";
                        images[2].style.height = "350px";
                        images[2].style.position = "absolute";
                        images[2].style.top = "350px";
                        images[2].style.left = "0px";

                        document.getElementById("canv").appendChild(images[3]);
                        images[3].style.width = "500px";
                        images[3].style.height = "350px";
                        images[3].style.position = "absolute";
                        images[3].style.top = "350px";
                        images[3].style.left = "500px";


                        ims.push(images[0]);
                        ims.push(images[1]);
                        ims.push(images[2]);
                        ims.push(images[3]);
                        if (ims.length > 240)
                        {
                            document.getElementById("canv").removeChild(ims.shift());
                            document.getElementById("canv").removeChild(ims.shift());
                            document.getElementById("canv").removeChild(ims.shift());
                            document.getElementById("canv").removeChild(ims.shift());
                        }
                        rendercnt++;



                        var frametime = ((new Date()).getTime() - prevtimerender);
                        prevtimerender = (new Date()).getTime();

                        var fps = (prevtimerender - starttime);
                        if (fps < 50000)
                            fps = rendercnt / fps * 1000;
                        else
                        {
                            fps = rendercnt / fps * 1000;
                            rendercnt = 0;
                            starttime = prevtimerender;
                        }



                        var t2 = (new Date()).getTime();
                        ttt = (t2 - ts);

                        //	document.getElementById("fps").innerHTML = frametime + "sec;" + "    " + fps + "fps" + ";chn" + cttt;

                        loading--;

                    }
                }


                function clientImageUpdate() {
                    // alert("clientImage update");
                    if (loading >= 2)
                    {
                        cttt++;
                        return;
                    }

                    //alert("loading" + loading);

                    ts = (new Date()).getTime();

                    cnt++;


                    loading++;
                    document.getElementById("loading").value = "" + loading;

                    var imurl1 = "VizOnlineServlet?page=viewer&tile=0&r=" + cnt;
                    var imurl2 = "VizOnlineServlet?page=viewer&tile=1&r=" + cnt;
                    var imurl3 = "VizOnlineServlet?page=viewer&tile=2&r=" + cnt;
                    var imurl4 = "VizOnlineServlet?page=viewer&tile=3&r=" + cnt;



                    var images = [];
                    images.push(new Image());
                    images.push(new Image());
                    images.push(new Image());
                    images.push(new Image());

                    images[0].src = imurl1;
                    images[1].src = imurl2;
                    images[2].src = imurl3;
                    images[3].src = imurl4;

                    images[0].loaded = false;
                    images[1].loaded = false;
                    images[2].loaded = false;
                    images[3].loaded = false;


                    images[0].onload = function() {
                        images[0].loaded = true;
                        fimloaded(images);
                    };
                    images[1].onload = function() {
                        images[1].loaded = true;
                        fimloaded(images);
                    };
                    images[2].onload = function() {
                        images[2].loaded = true;
                        fimloaded(images);
                    };
                    images[3].onload = function() {
                        images[3].loaded = true;
                        fimloaded(images);
                    };

                }


                var inpollprops = false;
                function pollprops()
                {
                    if (inpollprops)
                        return;
                    inpollprops = true;

                    cnt++;
                    xmlhttp = new XMLHttpRequest();
                    xmlhttp.onreadystatechange = function()
                    {

                    if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
                        {

                            if (xmlhttp.responseText.length != 0)
                            {
                                // alert("in pollprops = 4");

                                var split = xmlhttp.responseText.split(";");
                                
                                for (var i = 0; i < split.length; i++)
                                {

                                    var pchange = split[i].split(",");

                                    addProperty(pchange[2], pchange[3], pchange[4]);
                                }
                            }
                            inpollprops = false;
                        }
                    }
                    xmlhttp.open("GET", 'VizOnlineServlet?page=viewer&pollprops=1&r=' + cnt, true);
                    xmlhttp.send();
                }


                function sendCommand(url) {

                    xmlhttp = new XMLHttpRequest();
                    xmlhttp.open("GET", url, true);
                    xmlhttp.send();
                }


                function handleMouseUp(event) {

                    cnt++;

                    event = event || window.event; // IE-ism

                    event.preventDefault();

                    mousePos = {
                        x: event.clientX + document.body.scrollLeft - xMargin,
                        y: event.clientY + document.body.scrollTop - yMargin
                    };


                    //check if mousedrag or mouse click
                    var elem = event.target;

                    if ((mousePos.x - xMargin) > 1000)
                        return true;


                    dragFlag = 0;


                    if (event.which === 3) {
                        mouseType = "right";
                    }
                    else {
                        mouseType = "left ";
                    }

                    theUrl = 'VizOnlineServlet?page=viewer&mousetype=' + mouseType + '&cmd=mouseUp&mx=' + mousePos.x + '&my=' + mousePos.y + "&updateType=server" + "&r=" + cnt;
                    sendCommand(theUrl);

                }


                function handleContextMenu(event) {
                    event = event || window.event; // IE-ism                  
                    var elem = event.target;
                    if (elem.parentNode.id === "canv") { //avoid right-click menu on the canv
                        return false;
                    }
                }

                function handleMouseDown(event) {
                    cnt++;
                    event = event || window.event; // IE-ism                  
                    var elem = event.target;


                    mousePos = {
                        x: event.clientX + document.body.scrollLeft - xMargin,
                        y: event.clientY + document.body.scrollTop - yMargin
                    };
                    //alert(mousePos.x  + " " + mousePos.y);  

                    if ((mousePos.x) >= 1000 || mousePos.y >= 800)
                    {
                        return true;
                    }



                    if (event.target.id == "button1")
                    {
                        window.clearInterval(interval);
                        alert(parseInt(document.getElementById("refresh").value));
                        interval = setInterval(clientImageUpdate, parseInt(document.getElementById("refresh").value));
                    }
                    else if (event.target.id == "button2")
                    {
                        theUrl = 'VizOnlineServlet?changeEncoding=' + document.getElementById("encoding").value + "&r=" + cnt;
                        //alert(theUrl);  
                        sendCommand(theUrl);
                        return true;
                    }

                    dragFlag = 1;









                    if (event.which === 3) {
                        mouseType = "right";
                    }
                    else {
                        mouseType = "left ";
                    }


                    theUrl = 'VizOnlineServlet?page=viewer&mousetype=' + mouseType + '&cmd=mouseDown&mx=' + mousePos.x
                            + '&my=' + mousePos.y + "&updateType=server" + "&r=" + cnt;


                    sendCommand(theUrl);



                    return false; //this is to prevent image dragging.
                }




                function handleMouseMove(event) {

                    if (inmousemove)
                        return;
                    inmousemove = true;

                    cnt++;
                    event = event || window.event; // IE-ism
                    var elem = event.target;


                    mousePos = {
                        x: event.clientX - xMargin,
                        y: event.clientY - yMargin
                    };

                    document.getElementById("fps2").innerHTML = mousePos.x + " " + mousePos.y;
                    document.getElementById("fps").innerHTML = document.body.scrollLeft + " " + document.body.scrollTop;

                    if ((mousePos.x) >= 1000 || mousePos.y >= 800)
                    {
                        return true;
                    }

                    if (((new Date()).getTime() - prevtime < 50))
                    {
                        inmousemove = false;
                        return;
                    }
                    prevx = mousePos.x;
                    prevy = mousePos.y;
                    prevtime = (new Date()).getTime();


                    if (event.which === 3) {
                        mouseType = "right";
                    }
                    else {
                        mouseType = "left ";
                    }

                    if (dragFlag === 1) {//mouse is being dragged
                        theUrl = 'VizOnlineServlet?page=viewer&mousetype=' + mouseType + '&cmd=mouseDragged&mx='
                                + mousePos.x + '&my=' + mousePos.y + "&updateType=server" + "&r=" + cnt;
                        sendCommand(theUrl);
                    }
                    else { //normal mouse move
                        theUrl = 'VizOnlineServlet?page=viewer&mousetype=' + mouseType + '&cmd=mouseMoved&mx='
                                + mousePos.x + '&my=' + mousePos.y + "&updateType=server" + "&r=" + cnt;
                        sendCommand(theUrl);
                    }
                    inmousemove = false;
                }

                function getMousePosition() {
                    var pos = mousePos;
                    if (!pos) {
                        // We haven't seen any movement yet
                        pos = {x: "?", y: "?"};
                    }
                    posDisplay.innerHTML = "(" + pos.x + "," + pos.y + ")";

                    //  counterDisplay.innerHTML = ++counter;

                }

                var cpt = 0;
                var sliderName, sliderValue;

                function addProperty(name, type, value)
                {
                   /*

                    cpt++;
                    var split = name.split(".");
                    var name2, name3;
                    if (split.length > 1) {
                        name2 = (name.split("."))[0]; //get the first part of the name                   
                        name3 = (name.split("."))[1]; //the second part of the name to be used for the buttons                   
                    }
                    else {//for names that have only one part of the name.
                        name2 = (name.split("."))[0]; //get the first part of the name
                        name3 = name2;
                    }

                    var propslot = document.getElementById("propslot");

                    // var rowDiv = document.createElement("div");


                    var newlabel = document.createElement("div");
                    newlabel.setAttribute("class", "propLabel");
                    // newlabel.appendChild(document.createTextNode(name3));
                    newlabel.innerHTML = name3;
                    propslot.appendChild(newlabel);
                    // rowDiv.appendChild(newlabel);

                    var input = null;
                    if (type === "PDouble") {//create a textbox control
                        input = document.createElement("input");
                        input.type = "text";

                        input.setAttribute("id", "prop_" + name);

                        input.value = value;
                        input.setAttribute("class", "txtbox");
                        input.onblur = function() {
                            var value2;
                            if (input.value === "") {
                                value2 = 0; //
                            }
                            else {
                                value2 = input.value;
                            }

                            theUrl = 'VizOnlineServlet?changeProp=' + 1
                                    + '&name=' + name + '&value=' + value2
                                    + "&updateType=server" + "&r=" + cpt;

                            sendCommand(theUrl);

                        };

                    }
                    else if (type === "PColor") {
                        input = document.createElement("input");
                        input.type = "text";

                        input.setAttribute("id", "prop_" + name);

                        input.setAttribute("class", "color");

                    }



                    else if (type === "PPercent") { //create a slider control

                        input = document.createElement("div");
                        name2 = name2 + prop_cnt;
                        input.setAttribute("id", "prop_" + name2);
                        input.setAttribute("class", "slider");

                    } else if (type === "PInteger") { //create a spinner control
                        input = document.createElement("input");
                        name2 = name2 + prop_cnt;
                        input.setAttribute("id", "prop_" + name2);
                        input.setAttribute("class", "spinner");
                        input.setAttribute("value", value);

                        alert("after PInteger p1a");

                    } else if (type === "PBoolean") {
                        input = document.createElement("input");
                        input.type = "checkbox"
                        name2 = name2 + prop_cnt;
                        input.setAttribute("id", "prop_" + name2);
                        input.setAttribute("class", "chkbx");
                        input.onclick = function() {

                            var value2 = 0;  //not checked 

                            if (input.checked) {
                                value2 = 1;  //checked
                            }


                            theUrl = 'VizOnlineServlet?changeProp=' + 1 + '&name=' + name + '&value=' + value2 + "&updateType=server" + "&r=" + cpt;

                            sendCommand(theUrl);

                        };
                    }

                    if (input !== null) { //we append it and create create a button for it
                        propslot.appendChild(input);  // put it into the DOM

                        var myPicker, spinner;
                        if (type === "PColor") {
                            myPicker = new jscolor.color(document.getElementById("prop_" + name), {});
                            //set the color with the rgb values;

                            var split = value.split("-");
                            var red =
                                    myPicker.fromRGB(parseInt((split[0]) / 255), Math.round(parseInt(split[1]) / 255), parseInt(split[2]) / 255);


                            input.onchange = function() {
                                //set the color for it

                                var r = Math.round(myPicker.rgb[0] * 255);
                                var b = Math.round(myPicker.rgb[1] * 255);
                                var g = Math.round(myPicker.rgb[2] * 255);
                                var value2 = r + "-" + g + "-" + b;

                                theUrl = 'VizOnlineServlet?changeProp=' + 1 + '&name=' + name + '&value=' + value2 + "&updateType=server" + "&r=" + cpt;
                                sendCommand(theUrl);

                            };



                            // }

                        }
                        else if (type === "PPercent") {
                            var x = parseInt(value) * 100;
                            $("#prop_" + name2).slider({
                                range: "min",
                                max: 100,
                                value: x,
                                slide: function(event, ui) {
                                    var value2 = ui.value;  //get the value of 
                                    value2 = value2 / 100;  //find the fraction of the percentage.
                                    theUrl = 'VizOnlineServlet?changeProp=' + 1 + '&name=' + name + '&value=' + value2 + "&updateType=server" + "&r=" + cpt;
                                    sendCommand(theUrl);
                                }
                            });
                        }
                        else if (type === "PInteger") {
                            spinner = $("#prop_" + name2).spinner({
                                min: 0,
                                stop: function(event, ui) {
                                    var value2 = $(this).spinner("value");
                                    theUrl = 'VizOnlineServlet?changeProp=' + 1 + '&name=' + name + '&value=' + value2 + "&updateType=server" + "&r=" + cpt;
                                    sendCommand(theUrl);
                                    alert("After PInteger p1")
                                }

                            });

                        }




                        prop_cnt++;

                    }

                    setTimeout(function() {
                        document.getElementById("set_" + name).onclick = function()
                        {

                            if (type === "PColor") {
                                var r = document.getElementById("set_" + name).color.rgb[0] * 255;
                                var g = document.getElementById("set_" + name).color.rgb[1] * 255;
                                var b = document.getElementById("set_" + name).color.rgb[2] * 255;
                                var value = r + "," + g + "," + b;
                                alert("The colors are " + value);
                                theUrl = 'VizOnlineServlet?changeProp=' + 1 + '&name=' + name + '&value=' + value + "&updateType=server" + "&r=" + 1;
                            }

                            else {
                                theUrl = 'VizOnlineServlet?changeProp=' + 1 + '&name=' + name + '&value=' + document.getElementById("prop_" + name).value + "&updateType=server" + "&r=" + 1;

                            }
                            // alert("ha2 " + theUrl);
                            xmlhttp = new XMLHttpRequest();
                            //alert("ha3 " + theUrl);
                            xmlhttp.open("GET", theUrl, true);
                            //alert("ha4 " + theUrl);
                            xmlhttp.send();
                            //alert("ha5 " + theUrl);

                        };
                    }, 2000);*/
                }

                function firstTime() {

                    //   alert("firstTime");

                    xmlhttp = new XMLHttpRequest();
                    xmlhttp.open("GET", 'VizOnlineServlet?page=viewer&firstTime=1&r=' + cnt, true);
                    xmlhttp.send();
                }
            };

        </script>


    </head>

    <body>
        <div id="generalHolder">

            <div class ="container"> 

                <div class="leftVdiv" >

                    <div id="fps" style="left:0px;">frame/sec</div>
                    <div id="fps2" style="left:0px;">frame/sec</div> <br>
                    <div id="controls" style="left:0;">
                        <input type="text" id="refresh" /><button id = "button1" onclick="alert('fff');"></button><br>
                        <input type="text" id="encoding" /><button  id="button2"></button><br>
                        <input type="text" id="loading" /><br>

                        <div id="propslot" style="position: absolute;"></div>
                    </div>


                    <div id='sent'></div>
                    <h1>Viewer Properties</h1>
                    <form>

                        <!--   <input type ="button" class="small button blue" value="Show Properties"
                                         onclick="makeRequest('properties')"/>  -->    
                        <div  id='properties' > 
                            <!-- end innerleftdiv --> </div>              
                    </form> 
                    <!-- end .leftdiv -->    
                </div>   



                <!-- IMAGE DIV -->

                <div id='output'  class="rightVdiv">
                    <div id="canv" style="width:800pt; height:450pt;"></div>                
                </div>

                <!--  -->


            </div>



        </div>


    </body>

</html>
