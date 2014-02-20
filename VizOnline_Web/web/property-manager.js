// for Properties
var propID = 1;
var viewerIndex;
var hash;
var inputPropSize = 10;
var progressintervalVar;

function addProperties(div, propertiesString) {
    //alert(propertiesString);
    var propArr = propertiesString.split(";");
    //remove the div current properties if this is not an update from pollprops which means it is a fresh one
    var pollprops = document.getElementById("pollprops").value;
    var datasetprops = "";
    if (document.getElementById("datasetprops")) {//to ensure that the page has that element
        datasetprops = document.getElementById("datasetprops").value;  //get the value
    }
    if (pollprops === "false" || datasetprops === "true") {
        removeDivChildren(div);
    }

    var prop;
    for (var i = 0; i < propArr.length; i++) {
        prop = propArr[i];
        var tempPropArr = prop.split(",");
        var addremove = tempPropArr[0];
        var viewer_name = tempPropArr[1];

        //set the name of the viewer if it is the viewer.
        /*if (document.getElementById("viewerName")) {
         document.getElementById("viewerName").value = viewer_name;
         }*/


        if (addremove === "addProperty") {
            var label = tempPropArr[2];
            //"addProperty,property-manager-name,property-name, property-type, property-vaue, read-only-value 
            // example: addProperty,graphvi,Appearance.Node Size,IntegerPropertyType,10, false;  


            //NB: add the property only if it has not been added to the hash array yet or it does not exist 

            if (!(label in hash) || (!(document.getElementById("I"+ hash[label])))) {
                              
                hash[label] = propID;
                var type = tempPropArr[3];
                var value = tempPropArr[4];
                var readOnly = tempPropArr[5];

                switch (type) {
                    case "PInteger":     //value format: 10
                        showIntegerBox(div, propID, label, value);
                        break;
                    case "PColor":       //value format: 200-150-150-255
                        showColorPicker(div, propID, label, value);
                        jscolor.init();
                        break;
                    case "PPercent":     //value format: 0.5
                        showRange(div, propID, label, value);
                        break;
                    case "PFile":    //value format: null
                        if (value === "false") { //upload button
                            showUploadButton(div, propID, label);
                        }
                        else if (value === "true") {//save button
                            showSaveButton(div, propID, label);
                        }
                        break;
                    case "PDouble":      //value format: 100.0
                        showDoubleBox(div, propID, label, value);
                        break;
                    case "PBoolean":     //value format: 0
                        showCheckBox(div, propID, label, value);
                        break;
                    case "SaveFilePropertyType":    //value format: null
                        showUploadButton(div, propID, label);
                        break;
                    case "PString":      //value format:
                        showLetterBox(div, propID, label, value);
                        break;
                    case "POptions":
                        showOptionBox(div, propID, label, value);
                        break;
                    case "PProgress":
                        showProgressBar(div, propID, label, value);
                        //TO-DO
                        //do nothing for now;                     
                        break;

                    default:
                        //tbd
                }

                setPropertyReadOnly(propID, readOnly);

            }

        } else if (addremove === "removeProperty") {
            var label = tempPropArr[2];
            //remove the propertyElement
            removePropertyElement(div, hash[label]);
        } else if (addremove === "readOnlyChanged") {
            //Format:"readOnlyChanged,pm.getName(),p.getName(),newReadOnly;               
            var label = tempPropArr[2];
            var readOnly = tempPropArr[3];
            setPropertyReadOnly(hash[label], readOnly);
        }
        else if (addremove === "changeProperty") {
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

function showProgressBar(div, id, name, value){
    
    var paragraph = createParagraph();

    var label = createLabel(id, name);
  
    
    
       var hiddenInput = createHiddenInput(id, name);

    
    
    var progressBar = document.createElement("div");
    progressBar.setAttribute("id", "I" + id);
    
   
    
    
    div.appendChild(hiddenInput);
    
    var progressBegin = document.createElement('img');
    progressBegin.setAttribute("src", "Images/progressBegin.png");
    progressBegin.setAttribute("id", "progressBegin"+id);
    progressBegin.style.display="inline";
        
    var progressMiddle = document.createElement('img');
    progressMiddle.setAttribute("src", "Images/progressMiddle.png");
    progressMiddle.setAttribute("id", "progressMiddle"+id);
    progressMiddle.style.display ="none";
    
    
    var progressEnd = document.createElement('img');
    progressEnd.setAttribute("src", "Images/progressEnd.png");
    progressEnd.setAttribute("id", "progressEnd"+id);
    progressEnd.style.display = "none";
    
    paragraph.appendChild(label);
    paragraph.appendChild(progressMiddle);
    paragraph.appendChild(progressEnd);
    paragraph.appendChild(progressBegin);
    //progressBar.appendChild(p)
    
    
    
    progressBar.appendChild(paragraph);

    //append the paragraph to the properties
    div.appendChild(progressBar);
    
    //use an interval to display the progress bar images
    
    progressintervalVar = setInterval(function(){
        progressBarInterval(div, id);
    }, 100);
    
}

function progressBarInterval(div, id){
    if(div){
       if(typeof progressBarInterval.counter ==='undefined'){
           progressBarInterval.counter = 0;
       }
       var progressBegin = document.getElementById("progressBegin"+id);
       var progressMiddle = document.getElementById("progressMiddle"+id);
       var progressEnd = document.getElementById("progressEnd"+id);
       
       if (progressBarInterval.counter === 0){ //remove progressBegin and show progressMiddle
           
           if(progressBegin){
               progressBegin.style.display="none";
               progressMiddle.style.display = "inline";
               progressBarInterval.counter++;
               
               return;
           }
       }
       else if (progressBarInterval.counter === 1){ //remove progressMiddle and show progressEnd
           if(progressMiddle){
               progressMiddle.style.display="none";
               progressEnd.style.display = "inline";
               progressBarInterval.counter++;
               
               return;
           }
       }
       
       else if (progressBarInterval.counter === 2){ //remove progressEnd and show progressBegin
           if(progressEnd){
               progressEnd.style.display="none";
               progressBegin.style.display = "inline";
               progressBarInterval.counter = 0;
               
               return;
           }          
       }
    }
    
    //clear interval if it gets this far
    clearInterval(progressintervalVar);
}



/*function to set the readOnly of properties */
function setPropertyReadOnly(id, readOnly) {




    if (document.getElementById("I" + id)) { //ensure that the element exists

        var element = document.getElementById("I" + id);
        if (readOnly === "true") {
            if (!(element.readOnly))
                element.setAttribute("readonly", true);
        }
        else if (readOnly === "false") {
            if ((element.readOnly))
                element.removeAttribute("readonly");
        }


    }

}

function removePropertyElement(div, id) {

    var hiddenInput = document.getElementById("HL" + id);
    var propElement = document.getElementById("I" + id);
    var propElementLabel = document.getElementById("L" + id);

    hiddenInput.parentNode.removeChild(hiddenInput);
    propElement.parentNode.removeChild(propElement);
    propElementLabel.parentNode.removeChild(propElementLabel);

}

function removeDivChildren(div) {
    while (div.firstChild) {
        div.removeChild(div.firstChild);
    }
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
    var propValue = hexToRgb(color);

    var prop = get('HL' + id);
    var propName = prop.value;

    var factoryType = document.getElementById("factoryType").value;
    var factoryItemName = document.getElementById("factoryItemName").value;

    var url = "updateProperty&newValue=" + propValue + "&property=" + propName;
    url += "&factoryType=" + factoryType + "&factoryItemName=" + factoryItemName;

    if (document.getElementById("viewerName")) { //add the viewerName if it is a viewer. NB: all viewers will have a viewer name
        url += "&viewerName=" + document.getElementById("viewerName").value;
    }

    makeRequest(url);
}

function  updateInputValueInfo(id) {
    //get the name and value of the property and make the update request
    var propLabel = get('HL' + id);
    var propName = propLabel.value;
    var propInput = get("I" + id);
    var propValue = propInput.value;

    var factoryType = document.getElementById("factoryType").value;
    var factoryItemName = document.getElementById("factoryItemName").value;

    var url = "updateProperty&newValue=" + propValue + "&property=" + propName;
    url += "&factoryType=" + factoryType + "&factoryItemName=" + factoryItemName;

    if (document.getElementById("viewerName")) { //add the viewerName if it is a viewer. NB: all viewers will have a viewer name
        url += "&viewerName=" + document.getElementById("viewerName").value;
    }

    makeRequest(url);

}

//function to update files
function updateFileInfo(id) {
    var propLabel = get('HL' + id);
    var propName = propLabel.value;
    var file = get("I" + id);
    var propValue = file.value;

    var factoryItemName = document.getElementById("factoryItemName").value;
    var factoryType = document.getElementById("factoryType").value;
    var url = "Uploads?page=uploadData&factoryItemName=" + factoryItemName
            + "&property=" + propName + "&factoryType=" + factoryType;

    var formData = new FormData();
    formData.append("File", file.files[0]);

    var xmlHttpRequest = getXMLHttpRequest();
    xmlHttpRequest.onreadystatechange = getReadyStateHandler(xmlHttpRequest, 'upload');
    xmlHttpRequest.open("POST", url, true);
    xmlHttpRequest.send(formData);
}

//creation of a selection box and options
function showOptionBox(div, id, name, value) {
    var hiddenInput = createHiddenInput(id, name);

    div.appendChild(hiddenInput);

    var paragraph = createParagraph();

    var label = createLabel(id, name);

    var optionBox = document.createElement('select');
    optionBox.setAttribute("id", "I" + id);


    optionBox.setAttribute("onchange", "updateInputValueInfo(" + id + ");");
    ;

    var options = value.split("-"); //the options will be separated by "-"
    for (var i = 0; i < options.length; i++) {
        var option = document.createElement('option');
        option.setAttribute("value", options[i]);
        option.innerHTML = options[i];

        optionBox.appendChild(option);
    }

    //create the options and add them to the selectBox


    //append the label and input box to the paragraph
    paragraph.appendChild(label);
    paragraph.appendChild(optionBox);

    //append the paragraph to the properties
    div.appendChild(paragraph);





}

function showColorPicker(div, id, name, value) {
    //add hidden input to hold the name of the property
    var hiddenInput = createHiddenInput(id, name);

    div.appendChild(hiddenInput);

    var paragraph = createParagraph();

    var label = createLabel(id, name);
    var colorInputBox = document.createElement('input');
    colorInputBox.setAttribute("id", "I" + id);
    colorInputBox.setAttribute("size", inputPropSize);
    colorInputBox.setAttribute("class", "color {onImmediateChange:\'updateColorInfo(this," + id + ");\'}");
    //append the label and input box to the paragraph
    paragraph.appendChild(label);
    paragraph.appendChild(colorInputBox);

    //append the paragraph to the properties
    div.appendChild(paragraph);

    //setInitialColor(id, value);

}

// accepts only digits
function showIntegerBox(div, id, name, value) {
    if (typeof(value) === 'undefined') {
        value = 100;
    }

//alert("Integer Value is "+ value);

    //add hidden input to hold the name of the property
    var hiddenInput = createHiddenInput(id, name);

    div.appendChild(hiddenInput);


    var label = createLabel(id, name);

    var integerInputBox = document.createElement('input');
    integerInputBox.setAttribute("id", "I" + id);
    integerInputBox.setAttribute("value", value);
    integerInputBox.setAttribute("type", "number");
    integerInputBox.setAttribute("class", "inputProperty");
   // integerInputBox.setAttribute("size", inputPropSize); //the size of the inputBoxes

    integerInputBox.setAttribute("onchange", "updateInputValueInfo(" + id + ");");

    //create a paragraph and add the label and the integerInputBox to it
    var paragraph = createParagraph();
    paragraph.appendChild(label);
    paragraph.appendChild(integerInputBox);
    div.appendChild(paragraph);

}

// accepts digits and .
function showDoubleBox(div, id, name, value) {
    if (typeof(value) === 'undefined') {
        value = 100;
    }
    //add hidden input to hold the name of the property
    var hiddenInput = createHiddenInput(id, name);
    div.appendChild(hiddenInput);

    var label = createLabel(id, name);
    var doubleInputBox = document.createElement('input');
    doubleInputBox.setAttribute("id", "I" + id);
    doubleInputBox.setAttribute("value", value);
    doubleInputBox.setAttribute("type", "number");
    doubleInputBox.setAttribute("class", "inputProperty");
    //doubleInputBox.setAttribute("size", inputPropSize); //the size of the inputBoxes

    doubleInputBox.setAttribute("onchange", "updateInputValueInfo(" + id + ");");

    //create a paragraph and add the label and the doubleInputBox to it
    var paragraph = createParagraph();
    paragraph.appendChild(label);
    paragraph.appendChild(doubleInputBox);
    div.appendChild(paragraph);
}

function showLetterBox(div, id, name, value) {
    if (typeof(value) === 'undefined') {
        value = " ";
    }

    //add hidden input to hold the name of the property
    var hiddenInput = createHiddenInput(id, name);
    div.appendChild(hiddenInput);


    var label = createLabel(id, name);

    var letterInputBox = document.createElement('input');
    letterInputBox.setAttribute("id", "I" + id);
    letterInputBox.setAttribute("value", value);
    letterInputBox.setAttribute("type", "text");
    letterInputBox.setAttribute("size", inputPropSize); //the size of the inputBoxes

    letterInputBox.setAttribute("onchange", "updateInputValueInfo(" + id + ");");

    //create a paragraph and add the label and the integerInputBox to it
    var paragraph = createParagraph();
    paragraph.appendChild(label);
    paragraph.appendChild(letterInputBox);
    div.appendChild(paragraph);

}

function showCheckBox(div, id, name, value) {
    //add hidden input to hold the name of the property
    var hiddenInput = createHiddenInput(id, name);
    div.appendChild(hiddenInput);

    var label = createLabel(id, name);

    var booleanCheckBox = document.createElement('input');

    booleanCheckBox.setAttribute("id", "I" + id);
    booleanCheckBox.setAttribute("type", "checkbox");
    booleanCheckBox.setAttribute("onchange", "updateCheckBoxInfo(" + id + ");");
    //create a paragraph and add the label and the checkbox to it
    if (value === "1") { //set the checked property if it is checked
        booleanCheckBox.setAttribute("checked", "true");
    }

    var paragraph = createParagraph();
    paragraph.appendChild(label);
    paragraph.appendChild(booleanCheckBox);
    div.appendChild(paragraph);

}

function showUploadButton(div, id, name) {

    var hiddenInput = createHiddenInput(id, name);
    div.appendChild(hiddenInput);

    var label = createLabel(id, name);

    var fileBox = document.createElement('input');

    fileBox.setAttribute("id", "I" + id);
    fileBox.setAttribute("type", "file");
    fileBox.setAttribute("class", "fileProperty");
    fileBox.setAttribute("onchange", "updateFileInfo(" + id + ");");
    //create a paragraph and add the label and the checkbox to it

    var paragraph = createParagraph();
    paragraph.appendChild(label);
    paragraph.appendChild(fileBox);
    div.appendChild(paragraph);

}

function showSaveButton(div, id, name) {
    var hiddenInput = createHiddenInput(id, name);
    div.appendChild(hiddenInput);

    var label = createLabel(id, name);

    var button = document.createElement('button');

    button.setAttribute("id", "I" + id);
    button.innerHTML = "Save";
    button.setAttribute("onclick", "updateSaveInfo(" + id + ");");

    var paragraph = createParagraph();
    paragraph.appendChild(label);
    paragraph.appendChild(button);
    div.appendChild(paragraph);
}

function updateSaveInfo(id) {
    var propLabel = get('HL' + id);
    var propName = propLabel.value;

    var factoryItemName = document.getElementById("factoryItemName").value;
    var factoryType = document.getElementById("factoryType").value;
    var url = "Downloads?page=updateDownloadPath&factoryItemName=" + factoryItemName
            + "&property=" + propName + "&factoryType=" + factoryType;

    //var formData = new FormData();
    //formData.append("File", file.files[0]);

    var xmlHttpRequest = getXMLHttpRequest();
    xmlHttpRequest.onreadystatechange = getReadyStateHandler(xmlHttpRequest, 'download');
    xmlHttpRequest.open("GET", url, true);
    xmlHttpRequest.send(null);

}

function downloadData(fileName) {

    //set the url of the link that will be  used to download the file
    var url = "Downloads?page=downloadData&fileName=" + fileName;

    var downloadLink = document.getElementById("downloadFile");
    downloadLink.href = url;

    //actually download the file by clicking on the link automatically
    downloadLink.click();

}






function showRange(div, id, name, value) {
    if (typeof(value) === 'undefined') {
        value = 0.5;
    }
    //add hidden input to hold the name of the property
    var hiddenInput = createHiddenInput(id, name);
    div.appendChild(hiddenInput);

    var label = createLabel(id, name);

    var rangeInputBox = document.createElement('input');
    rangeInputBox.setAttribute("id", "I" + id);
    rangeInputBox.setAttribute("value", value);
    rangeInputBox.setAttribute("type", "range");
    rangeInputBox.setAttribute("class", "inputProperty");
    rangeInputBox.setAttribute("min", 0);
    rangeInputBox.setAttribute("max", 1);
    rangeInputBox.setAttribute("step", 0.1);
    //rangeInputBox.setAttribute("size", inputPropSize); //the size of the inputBoxes

    rangeInputBox.setAttribute("onchange", "updateInputValueInfo(" + id + ");");

    //create a paragraph and add the label and the integerInputBox to it
    var paragraph = createParagraph();
    paragraph.appendChild(label);
    paragraph.appendChild(rangeInputBox);
    div.appendChild(paragraph);

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

function updateCheckBoxInfo(id) {

    //get the name and value of the property and make the update request
    var propLabel = get('HL' + id);
    var propName = propLabel.value;
    var propInput = get("I" + id);
    var propValue = propInput.checked;

    var factoryType = document.getElementById("factoryType").value;
    var factoryItemName = document.getElementById("factoryItemName").value;

    var url = "updateProperty&newValue=" + propValue + "&property=" + propName;
    url += "&factoryType=" + factoryType + "&factoryItemName=" + factoryItemName;

    if (document.getElementById("viewerName")) { //add the viewerName if it is a viewer. NB: all viewers will have a viewer name
        url += "&viewerName=" + document.getElementById("viewerName").value;
    }

    makeRequest(url);
}

function setInitialColor(id, value) {
    // alert(value);
    var colorBox = get("I" + id);

    var split = value.split("-");
    alert(parseInt((split[0]) / 255) + "  " +
            Math.round(parseInt(split[1]) / 255)
            + "  " + parseInt(split[2]) / 255
            + "  " + parseInt(split[3]) / 255);

    document.getElementById("I" + id).color.fromRGB(parseInt((split[0]) / 255),
            Math.round(parseInt(split[1]) / 255),
            parseInt(split[2]) / 255);
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
