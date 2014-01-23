

var sourceFiles = new Array();
var fileID = 0;
var count = 0;
var current;

// for Properties
var propID = 1;
var hash = {};
hash['first'] = 0;
var viewerIndex;

/* 
 * creates a new XMLHttpRequest object which is the backbone of AJAX  
 * or returns false if the browser doesn't support it 
 */
function getXMLHttpRequest() {
    var xmlHttpReq;
    // to create XMLHttpRequest object in non-Microsoft browsers  
    if (window.XMLHttpRequest) {
        xmlHttpReq = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        try {
            //to create XMLHttpRequest object in later versions of Internet Explorer  
            xmlHttpReq = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (exp1) {
            try {
                //to create XMLHttpRequest object in later versions of Internet Explorer  
                xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (exp2) {
                //xmlHttpReq = false;  
                alert("Exception in getXMLHttpRequest()!");
            }
        }
    }
    return xmlHttpReq;
}

/* 
 * AJAX call starts with this function 
 */
function makeRequest(thepage) {
    // alert(thepage);
    var xmlHttpRequest = getXMLHttpRequest();
    //alert("xmlHttpRequest=" + xmlHttpRequest);
    if (thepage === 'upload') {
        //   alert("here");
        var formData = new FormData();
        formData.append("File", document.getElementById("theFile").files[0]);
        xmlHttpRequest.onreadystatechange = getReadyStateHandler(xmlHttpRequest, thepage);
        xmlHttpRequest.open("POST", "Uploads", true);
        xmlHttpRequest.send(formData);
    } else if (thepage === 'data' || thepage === 'viewdatas') {
        xmlHttpRequest.onreadystatechange = getReadyStateHandler(xmlHttpRequest, thepage);
        xmlHttpRequest.open("GET", "VizOnlineServlet?page=getDatas", true);
        xmlHttpRequest.send(null);

    } else if (thepage === 'createViewer') {
        var vindex = get("viewerlist").selectedIndex;
        var voptions = get("viewerlist").options;
        var type = voptions[vindex].text;
        var dindex = get("datalist").selectedIndex;
        var doptions = get("datalist").options;
        var data = doptions[dindex].text;
        xmlHttpRequest.onreadystatechange = getReadyStateHandler(xmlHttpRequest, thepage);
        xmlHttpRequest.open("GET", "VizOnlineServlet?page=" + thepage + "&type=" + type + "&data=" + data, true);
        xmlHttpRequest.send(null);

    } else if (thepage === 'views') {
        xmlHttpRequest.onreadystatechange = getReadyStateHandler(xmlHttpRequest, thepage);
        xmlHttpRequest.open("GET", "VizOnlineServlet?page=getviewerfact", true);
        xmlHttpRequest.send(null);

    } else if (thepage === 'currentviewers') {
        xmlHttpRequest.onreadystatechange = getReadyStateHandler(xmlHttpRequest, thepage);
        xmlHttpRequest.open("GET", "VizOnlineServlet?page=getcurrviewers", true);
        xmlHttpRequest.send(null);

    } else if (thepage === 'currentLinks') {
        xmlHttpRequest.onreadystatechange = getReadyStateHandler(xmlHttpRequest, 'linkViewers');
        xmlHttpRequest.open("GET", "VizOnlineServlet?page=getCurrentLinks", true);
        xmlHttpRequest.send(null);

    } else if (thepage === 'currentViewersLink1') {
        xmlHttpRequest.onreadystatechange = getReadyStateHandler(xmlHttpRequest, thepage);
        xmlHttpRequest.open("GET", "VizOnlineServlet?page=getcurrviewers", true);
        xmlHttpRequest.send(null);

    } else if (thepage === 'currentViewersLink2') {
        xmlHttpRequest.onreadystatechange = getReadyStateHandler(xmlHttpRequest, thepage);
        xmlHttpRequest.open("GET", "VizOnlineServlet?page=getcurrviewers", true);
        xmlHttpRequest.send(null);

    } else if (thepage === 'update') {
        xmlHttpRequest.onreadystatechange = getReadyStateHandler(xmlHttpRequest, thepage);
        xmlHttpRequest.open("GET", "Uploads", true);
        xmlHttpRequest.send(null);

    } else if (thepage === 'deleteData') {
        xmlHttpRequest.onreadystatechange = getReadyStateHandler(xmlHttpRequest, thepage);
        xmlHttpRequest.open("GET", "Uploads?del=y", true);
        xmlHttpRequest.send(document.getElementById());

    } else if (thepage === 'linkViewers') {
        var v1index = get("vlist1").selectedIndex;
        var voptions = get("vlist1").options;
        var first = voptions[v1index].text;
        var farray = first.split(":");
        var findex = farray[0];
        var v2index = get("vlist2").selectedIndex;
        var doptions = get("vlist2").options;
        var second = doptions[v2index].text;
        var sarray = second.split(":");
        var sindex = sarray[0];
        xmlHttpRequest.onreadystatechange = getReadyStateHandler(xmlHttpRequest, thepage);
        xmlHttpRequest.open("GET", "VizOnlineServlet?page=" + thepage + "&first=" + findex + "&second=" + sindex, true);
        xmlHttpRequest.send(null);


    } else {

        xmlHttpRequest.onreadystatechange = getReadyStateHandler(xmlHttpRequest, thepage);
        xmlHttpRequest.open("GET", "VizOnlineServlet?page=" + thepage, true);
        //xmlHttpRequest.setRequestHandler("Content-Type", "application/x-www-form-urlencoded");  
        //alert("inside makeRequest()!");
        xmlHttpRequest.send(null);
        //alert("sent!");
    }
}

/* 
 * Returns a function that waits for the state change in XMLHttpRequest 
 */
function getReadyStateHandler(xmlHttpRequest, thepage) {
    // an anonynous function returned  
    // it listens to the XMLHttpRequest instance  
    return function() {
        if (xmlHttpRequest.readyState === 4) {
            if (xmlHttpRequest.status === 200) {
                if (thepage === 'data') {
                    getDatasets(xmlHttpRequest.responseText);
                }
                else if (thepage === 'views') {
                    getViewers(xmlHttpRequest.responseText);
                }
                else if (thepage === 'currentviewers') {
                    getCurrentViewers(xmlHttpRequest.responseText);
                }
                else if (thepage === 'currentViewersLink1') {
                    getCurrentViewersLink1(xmlHttpRequest.responseText);
                }
                else if (thepage === 'currentViewersLink2') {
                    getCurrentViewersLink2(xmlHttpRequest.responseText);
                }
                else if (thepage === 'viewdatas') {
                    getDataForViewer(xmlHttpRequest.responseText);
                    //alert(xmlHttpRequest.responseText);
                }
                else if (thepage === 'home') {
                    document.getElementById("epar").innerHTML = xmlHttpRequest.responseText;
                }
                else if (thepage === 'properties') {
                    //alert(xmlHttpRequest.responseText)
                    //document.getElementById("properties").innerHTML = xmlHttpRequest.responseText;
                    addProperties(xmlHttpRequest.responseText);
                }
                else if (thepage === 'createViewer') {
                    document.getElementById("currviewers").innerHTML = "";
                    makeRequest('currentviewers');
                    //viewerRequest(xmlHttpRequest.responseText);
                }
                else if (thepage === 'delViewer') {
                    document.getElementById("currviewers").innerHTML = "";
                    makeRequest('currentviewers');
                    //viewerRequest(xmlHttpRequest.responseText);
                }
                else if (thepage === 'launchViewer') {
                    var url = getURL() + xmlHttpRequest.responseText;
                    var win = window.open(url, '_blank');
                    win.focus();
                }
                else if (thepage === 'upload') {
                    sourceFiles[fileID] = xmlHttpRequest.responseText;
                    var tempSourceArr = sourceFiles.toString().split(",");
                    //document.getElementById("cdatas").innerHTML = sourceFiles.toString();
                    if (sourceFiles[fileID] !== "") {
                        alert("File:  " + sourceFiles[fileID] + " was uploaded successfully");
                        count = 1;
                        makeRequest('update');
                    } else {
                        alert("Please choose a valid file");
                    }
                    fileID++;

                } else if (thepage === 'update') {
                    document.getElementById("cdatas").innerHTML = "";
                    getDatasets(xmlHttpRequest.responseText);


                } else if (thepage === 'deleteData') {
                    alert(xmlHttpRequest.responseText);
                    document.getElementById("cdatas").innerHTML = "";
                    makeRequest('update');

                } else if (thepage === 'linkViewers') {
                    document.getElementById("currlinks").innerHTML = "";
                    currentLinks(xmlHttpRequest.responseText);


                } else {
                    //alert("Http error " + xmlHttpRequest.status + ":" + xmlHttpRequest.statusText);
                }
            }
        }
    };
}


//FUNCTIONS TO CREATE NESTED DIVS
function popUp() {

    document.getElementById("cdata").style.display = 'block';
    var name = document.getElementById("dname").value;
    var x = document.getElementById("Title");
    x.innerHTML = "Properties for: " + name;
}

function closePopup() {
    document.getElementById('cdata').style.display = 'none';
}

/*var get = function(id) {
 return document.getElementById(id);
 };*/

function get(id) {
    return document.getElementById(id);
}

/* Example Property String:
 "addProperty,graphvi,Appearance.Node Size,IntegerPropertyType,10;addProperty,graphvi,Appearance.Node Color,ColorPropertyType,200-150-150-255;addProperty,graphvi,Appearance.Node Alpha,PercentPropertyType,0.5;addProperty,graphvi,Appearance.Edge Color,ColorPropertyType,200-150-150-255;addProperty,graphvi,Appearance.Sel Edge Color,ColorPropertyType,100-50-50-255;addProperty,graphvi,Appearance.Edge Alpha,PercentPropertyType,0.2;addProperty,graphvi,bgimx,IntegerPropertyType,0;addProperty,graphvi,bgimy,IntegerPropertyType,0;addProperty,graphvi,Load Positions,OpenFilePropertyType,null;addProperty,graphvi,Simulation.K_REP,DoublePropertyType,5000000.0;addProperty,graphvi,Simulation.K_ATT,DoublePropertyType,100.0;addProperty,graphvi,Simulation.SPRING_LENGTH,DoublePropertyType,30.0;addProperty,graphvi,Simulation.MAX_STEP,DoublePropertyType,100.0;addProperty,graphvi,Simulation.Simulate,BooleanPropertyType,0;addProperty,graphvi,Save,SaveFilePropertyType,null;addProperty,graphvi,Tiles,IntegerPropertyType,0;addProperty,graphvi,ToImage,IntegerPropertyType,0;addProperty,graphvi,SelectedNodes,StringPropertyType,"
 "changeProperty,graphvi,Appearance.Node Size,12"
 "removeProperty,graphvi,Appearance.Node Size,10"
 */
function hexToRgb(hex) {
    var bigint = parseInt(hex, 16);
    var r = (bigint >> 16) & 255;
    var g = (bigint >> 8) & 255;
    var b = bigint & 255;

    return r + "-" + g + "-" + b + "-255";
}

function updateColorInfo(color, id) {
    var newcolor = hexToRgb(color);
    //alert("hey");

    //var label2 = get('HL' + id);
    //var label2 = document.getElementById('HL'+id);

    //alert(document.getElementById('HL'+id).value);

    var prop = get('HL' + id);
    var propName = prop.value;

    makeRequest("updateProperty&newValue=" + newcolor + "&property=" + propName +"&viewerIndex="+viewerIndex );
    //alert("after update");
    //makeRequest('updateProperty&newValue='+color.rgb+'&property='+get('L'+id).text());
}


function  updateInputValueInfo(id){
 //get the name and value of the property and make the update request
    var propLabel = get('HL' + id);
    var propName = propLabel.value;
    var propInput = get("I"+id);
    var propValue = propInput.value;
     
    makeRequest("updateProperty&newValue="+propValue+"&property="+propName + "&viewerIndex="+viewerIndex);
 }


function showColorPicker(id, name, value) {
    //add hidden input to hold the name of the property
    var hiddenInput = createHiddenInput(id, name);
    var properties = get("properties");
    properties.appendChild(hiddenInput);

    var paragraph = createParagraph();

    var label = createLabel(id, name);
    var colorInputBox = document.createElement('input');
    colorInputBox.setAttribute("id", "I" + id);
    colorInputBox.setAttribute("class", "color {onImmediateChange:\'updateColorInfo(this," + id + ");\'}");
    //append the label and input box to the paragraph
    paragraph.appendChild(label);
    paragraph.appendChild(colorInputBox);

    //append the paragraph to the properties
    properties.appendChild(paragraph);
    
    //setInitialColor(id, value);
    
}

// accepts only digits
function showIntegerBox(id, name, value) {
    if (typeof(value) === 'undefined') {
        value = 100;
    }


    //add hidden input to hold the name of the property
    var hiddenInput = createHiddenInput(id, name);
    var properties = get("properties");
    properties.appendChild(hiddenInput);


    var label = createLabel(id, name);

    var integerInputBox = document.createElement('input');
    integerInputBox.setAttribute("id", "I" + id);
    integerInputBox.setAttribute("value", value);
    integerInputBox.setAttribute("type", "number");
    integerInputBox.setAttribute("size", 20); //the size of the inputBoxes

    integerInputBox.setAttribute("onchange", "updateInputValueInfo("+ id +");");

    //create a paragraph and add the label and the integerInputBox to it
    var paragraph = createParagraph();
    paragraph.appendChild(label);
    paragraph.appendChild(integerInputBox);
    properties.appendChild(paragraph);

}

// accepts digits and .
function showDoubleBox(id, name, value) {
    if (typeof(value) === 'undefined') {
        value = 100;
    }
   //add hidden input to hold the name of the property
    var hiddenInput = createHiddenInput(id, name);
    var properties = get("properties");
    properties.appendChild(hiddenInput); 
    
    var label = createLabel(id, name);    
    var doubleInputBox = document.createElement('input');
    doubleInputBox.setAttribute("id", "I" + id);
    doubleInputBox.setAttribute("value", value);
    doubleInputBox.setAttribute("type", "number");
    doubleInputBox.setAttribute("size", 20); //the size of the inputBoxes

    doubleInputBox.setAttribute("onchange", "updateInputValueInfo("+ id +");");

    //create a paragraph and add the label and the doubleInputBox to it
    var paragraph = createParagraph();
    paragraph.appendChild(label);
    paragraph.appendChild(doubleInputBox);
    properties.appendChild(paragraph);
      
  /*  get("properties").innerHTML += "<p>" +
            '<label id=L' + id + ' for=' + id + ' class="proplabel">' + getName(name) + '</label><input id=I' + id + ' value=' + value
            + ' size="23" type="number" onchange="makeRequest(\'updateProperty&newValue=\'+value+\'&property=\'+get(\'HL\'+id).text());" /><br></p>';*/
}

function showLetterBox(id, name, value) {
    if (typeof(value) === 'undefined') {
        value = " ";
    }
    
     //add hidden input to hold the name of the property
    var hiddenInput = createHiddenInput(id, name);
    var properties = get("properties");
    properties.appendChild(hiddenInput);


    var label = createLabel(id, name);

    var letterInputBox = document.createElement('input');
    letterInputBox.setAttribute("id", "I" + id);
    letterInputBox.setAttribute("value", value);
    letterInputBox.setAttribute("type", "text");
    letterInputBox.setAttribute("size", 20); //the size of the inputBoxes

    letterInputBox.setAttribute("onchange", "updateInputValueInfo("+ id +");");

    //create a paragraph and add the label and the integerInputBox to it
    var paragraph = createParagraph();
    paragraph.appendChild(label);
    paragraph.appendChild(letterInputBox);
    properties.appendChild(paragraph);
    
    
    
   /* get("properties").innerHTML += "<p>" +
            '<label id=HL' + id + ' class="hiddenLabel">' + name + '</label>';*/
    /*get("properties").innerHTML += "<p>" +
            '<label id=L' + id + ' for=' + id + ' class="proplabel">' + getName(name) + '</label><input id=I' + id + ' value=' + value
            + ' size="23" type="number" onchange="makeRequest(\'updateProperty&newValue=\'+value+\'&property=\'+get(\'HL\'+id).text());" /><br></p>';
    */
}

function showCheckBox(id, name, value) {
    
     //add hidden input to hold the name of the property
    var hiddenInput = createHiddenInput(id, name);
    var properties = get("properties");
    properties.appendChild(hiddenInput);
    
    var label = createLabel(id, name);

    var booleanCheckBox = document.createElement('input');
    booleanCheckBox.setAttribute("id", "I" + id);
    booleanCheckBox.setAttribute("value", value);
    booleanCheckBox.setAttribute("type", "checkbox");
    
    booleanCheckBox.setAttribute("onchange", "updateCheckBoxInfo("+ id +");");

    //create a paragraph and add the label and the checkbox to it
    var paragraph = createParagraph();
    paragraph.appendChild(label);
    paragraph.appendChild(booleanCheckBox);
    properties.appendChild(paragraph);    
}

function showUploadButton(id, name) {
    get("properties").innerHTML += "<p>" +
            '<label id=L' + id + ' for=' + id + ' >' + getName(name) + '</label>' +
            '<input id=I' + id + ' type="file" /><br></p>';
}

function showRange(id, name, value) {
    if (typeof(value) === 'undefined') {
        value = 0.5;
    }    
     //add hidden input to hold the name of the property
    var hiddenInput = createHiddenInput(id, name);
    var properties = get("properties");
    properties.appendChild(hiddenInput);
    
    var label = createLabel(id, name);

    var rangeInputBox = document.createElement('input');
    rangeInputBox.setAttribute("id", "I" + id);
    rangeInputBox.setAttribute("value", value);
    rangeInputBox.setAttribute("type", "range");
    rangeInputBox.setAttribute("min", 0);
    rangeInputBox.setAttribute("max", 1);
    rangeInputBox.setAttribute("step", 0.01);
    //rangeInputBox.setAttribute("size", 20); //the size of the inputBoxes

    rangeInputBox.setAttribute("onchange", "updateInputValueInfo("+ id +");");

    //create a paragraph and add the label and the integerInputBox to it
    var paragraph = createParagraph();
    paragraph.appendChild(label);
    paragraph.appendChild(rangeInputBox);
    properties.appendChild(paragraph);
    
}

//get the String with all properties and add/remove/change the properties as displayed in the String
// adds to the "properties" div
function addProperties(string) {
    //var propString = "addProperty,graphvi,Appearance.Node Size,IntegerPropertyType,10;addProperty,graphvi,Appearance.Node Color,ColorPropertyType,200-150-150-255;addProperty,graphvi,Appearance.Node Alpha,PercentPropertyType,0.5;addProperty,graphvi,Appearance.Edge Color,ColorPropertyType,200-150-150-255;addProperty,graphvi,Appearance.Sel Edge Color,ColorPropertyType,100-50-50-255;addProperty,graphvi,Appearance.Edge Alpha,PercentPropertyType,0.2;addProperty,graphvi,bgimx,IntegerPropertyType,0;addProperty,graphvi,bgimy,IntegerPropertyType,0;addProperty,graphvi,Load Positions,OpenFilePropertyType,null;addProperty,graphvi,Simulation.K_REP,DoublePropertyType,5000000.0;addProperty,graphvi,Simulation.K_ATT,DoublePropertyType,100.0;addProperty,graphvi,Simulation.SPRING_LENGTH,DoublePropertyType,30.0;addProperty,graphvi,Simulation.MAX_STEP,DoublePropertyType,100.0;addProperty,graphvi,Simulation.Simulate,BooleanPropertyType,0;addProperty,graphvi,Save,SaveFilePropertyType,null;addProperty,graphvi,Tiles,IntegerPropertyType,0;addProperty,graphvi,ToImage,IntegerPropertyType,0;addProperty,graphvi,SelectedNodes,StringPropertyType,;changeProperty,graphvi,Appearance.Node Size,12;removeProperty,graphvi,Appearance.Node Alpha,0.5";
    var propString = string;
    //alert("hey");
    var propArr = propString.split(";");
  //  alert(string);
    var prop;
    for (var i = 0; i < propArr.length; i++) {
        prop = propArr[i];
        var tempPropArr = prop.split(",");
        var addremove = tempPropArr[0];
        var viewer_name = tempPropArr[1];
        

        if (addremove === "addProperty") {
            var label = tempPropArr[2];
            // example: addProperty,graphvi,Appearance.Node Size,IntegerPropertyType,10;
            hash[label] = propID;
            var type = tempPropArr[3];
            var value = tempPropArr[4];

            /* alert("propID "+propID);
             alert("label "+label);
             alert("value "+value);*/

            switch (type) {
                case "PInteger":     //value format: 10
                    showIntegerBox(propID, label, value);
                    break;
                case "PColor":       //value format: 200-150-150-255
                    showColorPicker(propID, label, value);
                    jscolor.init();
                    break;
                case "PPercent":     //value format: 0.5
                    showRange(propID, label, value);
                    break;
                case "PFile":    //value format: null
                    showUploadButton(propID, label);
                    break;
                case "PDouble":      //value format: 100.0
                    showDoubleBox(propID, label, value);
                    break;
                case "PBoolean":     //value format: 0
                    if (value === 1)
                        showCheckBox(propID, label, 'checked=true', value);
                    else
                        showCheckBox(propID, label, '', value);
                    break;
                case "SaveFilePropertyType":    //value format: null
                    showUploadButton(propID, label);
                    break;
                case "PString":      //value format:
                    showLetterBox(propID, label, value);
                    break;
                default:
                    //tbd
            }
        } else if(addremove === "setViewerIndex"){ //set the viewerIndex
                value = tempPropArr[1];
                 viewerIndex = value;
                document.getElementById("viewerIndex").value= value;
                
        }
        
    else if (addremove === "removeProperty") {
        var label = tempPropArr[2];
            // example: removeProperty,graphvi,Appearance.Node Size,10
            alert("remove " + hash[label]);
            removeElement(hash[label]);
        } else if (addremove === "changeProperty") {
            var label = tempPropArr[2];
            // example: changeProperty,graphvi,Appearance.Node Size,12
            var newValue = tempPropArr[3];
            //alert("update " + hash[label] + " to " + newValue);
            updateValue(hash[label], newValue);
        }
        propID++;
        jscolor.init();
    }
}

//Method to clean the name on the label of the properties
function getName(fullname) {
    var tempNameArr = fullname.split(".");
    if (tempNameArr.length > 1)
        return tempNameArr[1];
    else
        return tempNameArr[0];
}

function updateValue(id, newValue) {
    //alert("in updateValue");
    var element = get(id);
    //alert("element.tagName: "+element.tagName);
    //alert("element.type: "+element.type);
    switch (element.type) {
        case 'number':
            element.value = parseInt(newValue);
            break;
        case 'checkbox':
            element.checked = newValue;
            break;
        case 'range':
            if (newValue >= 0 && newValue <= 1)
                element.value = newValue;
            break;
        default:
            element.value = newValue;
    }
}

function updateCheckBoxInfo(id){
   
    //get the name and value of the property and make the update request
    var propLabel = get('HL' + id);
    var propName = propLabel.value;
    var propInput = get("I"+id);
    var propValue = propInput.checked;
    
    makeRequest("updateProperty&newValue="+propValue+"&property="+propName+"&viewerIndex="+viewerIndex);
    
    
    
    
    
}

function setInitialColor(id, value){
    alert(value);
    var colorBox = get("I"+id);
    
      var split = value.split("-");     
      alert(parseInt((split[0]) / 255)+ "  "  +
                             Math.round(parseInt(split[1]) / 255)  
                              + "  "  +parseInt(split[2]) / 255
                               + "  "  +parseInt(split[3]) / 255);
    
   document.getElementById("I"+id).color.fromRGB(parseInt((split[0]) / 255), 
                             Math.round(parseInt(split[1]) / 255), 
                              parseInt(split[2]) / 255);
}


function removeElement(id) {
    var element = get(id);
    element.parentNode.removeChild(element);
    var label = get('L' + id);
    label.parentNode.removeChild(label);
}

function getDatasets(datalist) {
    if (datalist === "No Content") {
        get("cdatas").innerHTML = "No Datasets available";
    } else {
        var dataArray = datalist.split(",");
        for (var i = 0; i < dataArray.length; i++) {
            current = dataArray[i];
            var par = document.createElement('p');
            par.setAttribute('id', 'par' + i);
            par.setAttribute('name', current);
            par.setAttribute('class', 'listpara');
            par.textContent = current;
            document.getElementById("cdatas").appendChild(par);
            var but = document.createElement('button');
            but.setAttribute('id', i);
            but.setAttribute('name', current);
            but.setAttribute('style', 'float: right');
            but.innerHTML = 'Delete';
            but.setAttribute('class', 'small button blue');
            var theSet = document.getElementById('par' + i).getAttribute('name').toString();
            but.setAttribute('onclick', 'deleteRequest("' + theSet + '")');
            document.getElementById('par' + i).appendChild(but);
        }
    }
}

function getDataForViewer(datalist) {
    if (datalist === "No Content") {
        get("viewers").innerHTML = "No Datasets available";
    } else {
        var dataArray = datalist.split(",");
        for (var i = 0; i < dataArray.length; i++) {

            var list = get("datalist");
            current = dataArray[i];
            var option = document.createElement('option');
            option.setAttribute('value', current);
            option.text = current;
            list.add(option, null);
        }
        get("viewers").appendChilds(list);
    }
}

function getViewers(datalist) {

    if (datalist === "") {
        get("viewers").innerHTML = "No Viewers available";
    } else {
        var dataArray = datalist.split(",");
        for (var i = 0; i < dataArray.length; i++) {

            var list = get("viewerlist");
            current = dataArray[i];
            var option = document.createElement('option');
            option.setAttribute('value', current);
            option.text = current;
            list.add(option, null);
        }
        get("viewers").appendChilds(list);
    }
}

function getCurrentViewers(datalist) {
    if (datalist === "No Content") {
        get("currviewers").innerHTML = "No Viewers available";
    } else {
        var dataArray = datalist.split(",");
        for (var i = 0; i < dataArray.length; i++) {
            current = dataArray[i];
            var nameArray = current.split(":");
            var currIndex = nameArray[0];
            var currName = nameArray[1];
            var printNum = parseInt(currIndex, 10);
            printNum++;
            var par = document.createElement('p');
            par.setAttribute('id', 'v' + currIndex);
            par.setAttribute('name', currIndex);
            par.setAttribute('class', 'listpara');
            par.textContent = printNum + ": " + currName;
            document.getElementById("currviewers").appendChild(par);
            var but1 = document.createElement('button');
            but1.setAttribute('id', 'b' + currIndex);
            but1.setAttribute('name', currIndex);
            but1.setAttribute('style', 'float: right');
            but1.innerHTML = 'Launch';
            but1.setAttribute('class', 'small button blue');
            var theViewer = document.getElementById('v' + currIndex).getAttribute('name');
            but1.setAttribute('onclick', 'launchRequest("' + theViewer + '")');
            var but2 = document.createElement('button');
            but2.setAttribute('id', 'bdel' + currIndex);
            but2.setAttribute('name', currIndex);
            but2.setAttribute('style', 'float: right');
            but2.innerHTML = 'Delete';
            but2.setAttribute('class', 'small button blue');
            but2.setAttribute('onclick', 'delVRequest("' + theViewer + '")');
            document.getElementById('v' + currIndex).appendChild(but2);
            document.getElementById('v' + currIndex).appendChild(but1);
        }
    }
}

function getCurrentViewersLink1(datalist) {
    if (datalist === "No Content") {
        get("links").innerHTML = "No Viewers available";
    } else {
        var dataArray = datalist.split(",");
        for (var i = 0; i < dataArray.length; i++) {
            var list = get("vlist1");
            current = dataArray[i];
            var option = document.createElement('option');
            option.setAttribute('value', current);
            option.text = current;
            list.add(option, null);
        }
        get("links").appendChilds(list);
    }
}

function getCurrentViewersLink2(datalist) {
    if (datalist === "No Content") {
        get("links").innerHTML = "No Viewers available";
    } else {
        var dataArray = datalist.split(",");
        for (var i = 0; i < dataArray.length; i++) {
            var list2 = get("vlist2");
            current = dataArray[i];
            var option = document.createElement('option');
            option.setAttribute('value', current);
            option.text = current;
            list2.add(option, null);
        }
        get("links").appendChilds(list2);
    }
}

function deleteRequest(string) {

    var xmlHttpRequest = getXMLHttpRequest();
    xmlHttpRequest.onreadystatechange = getReadyStateHandler(xmlHttpRequest, "deleteData");
    xmlHttpRequest.open("GET", "Uploads?del=" + string, true);
    xmlHttpRequest.send(null);

}

function launchRequest(index) {
    var xmlHttpRequest = getXMLHttpRequest();
    xmlHttpRequest.onreadystatechange = getReadyStateHandler(xmlHttpRequest, "launchViewer");
    xmlHttpRequest.open("GET", "VizOnlineServlet?page=viewerLaunch&index=" + index, true);
    
    xmlHttpRequest.send(null);
}

function delVRequest(index) {
    var xmlHttpRequest = getXMLHttpRequest();
    xmlHttpRequest.onreadystatechange = getReadyStateHandler(xmlHttpRequest, "delViewer");
    xmlHttpRequest.open("GET", "VizOnlineServlet?page=delViewer&index=" + index, true);
    xmlHttpRequest.send(null);
}

function currentLinks(datalist) {
    if (datalist === "No Content") {
        get("currlinks").innerHTML = "No Links available";
    } else {
        var dataArray = datalist.split(",");
        for (var i = 0; i < dataArray.length; i++) {
            current = dataArray[i];
            var linkArray = current.split(":");
            var currIndex = linkArray[0];
            var firstView = linkArray[1];
            var secondView = linkArray[2];
            var par = document.createElement('p');
            par.setAttribute('id', 'l' + currIndex);
            par.setAttribute('name', currIndex);
            par.setAttribute('class', 'listpara');
            par.textContent = "Viewer: " + firstView + " - Viewer: " + secondView;
            document.getElementById("currlinks").appendChild(par);
            var but1 = document.createElement('button');
            but1.setAttribute('id', 'b' + currIndex);
            but1.setAttribute('name', currIndex);
            but1.setAttribute('style', 'float: right');
            but1.innerHTML = 'Unlink';
            but1.setAttribute('class', 'small button blue');
            var theLink = document.getElementById('l' + currIndex).getAttribute('name');
            but1.setAttribute('onclick', 'unlinkViewers("' + theLink + '")');
            document.getElementById('l' + currIndex).appendChild(but1);
        }
    }
}

function unlinkViewers(index) {
    var xmlHttpRequest = getXMLHttpRequest();
    xmlHttpRequest.onreadystatechange = getReadyStateHandler(xmlHttpRequest, "linkViewers");
    xmlHttpRequest.open("GET", "VizOnlineServlet?page=unlinkViewers&index=" + index, true);
    xmlHttpRequest.send(null);

}

function getURL() {
    var arr = window.location.href.split("/");
    delete arr[arr.length - 1];
    return arr.join("/");
}

function createParagraph() {
    var newParagraph = document.createElement('p');
    newParagraph.innerHTML = "";
    return newParagraph;
}

function createHiddenInput(id, value) {

    var inputParagraph = createParagraph();
    var input = document.createElement('input');
    input.id = "HL" + id;
    input.type = 'hidden';
    input.value = value;

    inputParagraph.appendChild(input);

    return inputParagraph;
}

function createLabel(id, name) {

    var label = document.createElement('label');
    label.setAttribute("id", "L" + id);
    label.setAttribute("for", "I" + id);
    label.setAttribute("class", "proplabel");

    label.innerHTML = getName(name);

    return label;
}
