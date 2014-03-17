function createLabel(name) {

    var label = document.createElement('label');
    label.setAttribute("id", "L" + name);
    label.setAttribute("class", "proplabel");

    label.innerHTML = getName(name);

    return label;
}

function createDiv() {
    var newDiv = document.createElement('div');
    newDiv.innerHTML = "";   
    return newDiv;
}

function addPairToTable(div1, div2)
{
    var table     = document.createElement("table");
	table.width = "100%";
    var tableBody = document.createElement("tbody");
    table.appendChild(tableBody);
    var row = document.createElement("tr");
    tableBody.appendChild(row);
    var cellLeft = document.createElement("td"); 			
    var cellRight = document.createElement("td");
    cellRight.align = "right";			 
    row.appendChild(cellLeft);
    row.appendChild(cellRight);

    cellLeft.appendChild(div1);
    cellRight.appendChild(div2);
    return table;
}


function PInteger(name, value, readonly, hidden, disabled, propertyManager)
{

	this.name = name;
	this.readOnly = readonly;
	this.hidden = hidden;
	this.disabled = disabled;

   	if (typeof(value) === 'undefined')
        	value = 100;

    	this.label = createLabel(name);

    	this.integerInputBox = document.createElement('input');
    	this.integerInputBox.setAttribute("value", value);
    	this.integerInputBox.setAttribute("type", "number");

    	
   
    	this.integerInputBox.onchange = (function(prop,pm) { return function() {			
			prop.value = ""+this.value;
			pm.updateValue(prop);
		};})(this,propertyManager);

    	//create a paragraph and add the label and the integerInputBox to it
    	this.div = createDiv();

	var table = addPairToTable(this.label, this.integerInputBox)
    	this.div.appendChild(table);
    



	this.setReadOnly = setReadOnly;
	function setReadOnly(readOnly)
	{
		this.readOnly = readOnly;
		if (this.readOnly || this.disabled)			
			this.integerInputBox.disabled = true;
		else
			this.integerInputBox.disabled = false;
	}


	this.setValue = setValue;
	function setValue(stringVal)
	{
		this.integerInputBox.value = parseInt(stringVal);
	}

	this.setHidden = setHidden;
	function setHidden(stringVal)
	{
		//booleanCheckBox.value = stringVal;
	}

	this.setDisabled = setDisabled;
	function setDisabled(disabled)
	{
		this.disabled = disabled;
		if (this.disabled || this.readOnly)	
			this.integerInputBox.disabled = true;	
		else
			this.integerInputBox.disabled = false;
		
			

		if (this.disabled)
			this.label.style.color = "#999999";
		else
			this.label.style.color = "#000000";

	}

}

function PDouble(name, value, readonly, hidden, disabled, propertyManager)
{

	this.name = name;
	this.readOnly = readonly;
	this.hidden = hidden;
	this.disabled = disabled;	

   	if (typeof(value) === 'undefined')
        	value = 100;
   
    
    	this.label = createLabel(name);
    	this.doubleInputBox = document.createElement('input');
   	this.doubleInputBox.value = value;
    	this.doubleInputBox.type = 'number';
    

    	    	this.doubleInputBox.onchange = (function(prop,pm) { return function() {			
			prop.value = ""+this.value;
			pm.updateValue(prop);
		};})(this,propertyManager);

    	//create a paragraph and add the label and the doubleInputBox to it
    	this.div = createDiv();
	var table = addPairToTable(this.label, this.doubleInputBox)
    	this.div.appendChild(table);



	this.setReadOnly = setReadOnly;
	function setReadOnly(readOnly)
	{
		this.readOnly = readOnly;
		if (this.readOnly || this.disabled)			
			this.doubleInputBox.disabled = true;
		else
			this.doubleInputBox.disabled = false;
	}


	this.setValue = setValue;
	function setValue(stringVal)
	{
		this.doubleInputBox.value = parseDouble(stringVal);
	}

	this.setHidden = setHidden;
	function setHidden(stringVal)
	{
		//booleanCheckBox.value = stringVal;
	}

	this.setDisabled = setDisabled;
	function setDisabled(disabled)
	{
		this.disabled = disabled;
		if (this.disabled || this.readOnly)	
			this.doubleInputBox.disabled = true;	
		else
			this.doubleInputBox.disabled = false;
		
			

		if (this.disabled)
			this.label.style.color = "#999999";
		else
			this.label.style.color = "#000000";

	}
}


function PBoolean(name, value, readonly, hidden, disabled, propertyManager)
{
	this.name = name;
	this.readOnly = readonly;
	this.hidden = hidden;
	this.disabled = disabled;
	this.value = value;

    	this.label = createLabel(name);
   	this.checkBox = document.createElement('input');
    	this.checkBox.type = "checkbox";
    	this.checkBox.onchange = (function(prop,pm) { return function() {
			if (this.checked) prop.value = "1";
			else prop.value = "0";
			pm.updateValue(prop);
		};})(this,propertyManager);
    	
	
    	if (value === "1") //set the checked property if it is checked
      		  this.checkBox.checked = true;
  
   	this.div = createDiv();
	var table = addPairToTable(this.label, this.checkBox)
    	this.div.appendChild(table);

	this.setReadOnly = setReadOnly;
	function setReadOnly(readOnly)
	{
		this.readOnly = readOnly;
		if (this.readOnly || this.disabled)			
			this.checkBox.disabled = true;
		else
			this.checkBox.disabled = false;
	}



	this.setValue = setValue;
	function setValue(stringVal)
	{
		this.checkBox.value = stringVal;
	}

	this.setHidden = setHidden;
	function setHidden(stringVal)
	{
		//booleanCheckBox.value = stringVal;
	}

	this.setDisabled = setDisabled;
	function setDisabled(disabled)
	{
		this.disabled = disabled;
		if (this.disabled || this.readOnly)	
			this.checkBox.disabled = true;	
		else
			this.checkBox.disabled = false;
		
			

		if (this.disabled)
			this.label.style.color = "#999999";
		else
			this.label.style.color = "#000000";

	}

}

function PString(name, value, readonly, hidden, disabled, propertyManager)
{

	this.name = name;
	this.readOnly = readonly;
	this.hidden = hidden;
	this.disabled = disabled;

    	if (typeof(value) === 'undefined') 
        	value = " ";
   
    	this.label = createLabel(name);

    	this.letterInputBox = document.createElement('input');
    	this.letterInputBox.value = value;
    	this.letterInputBox.type = "text";
    	this.letterInputBox.size = 20; //the size of the inputBoxes
    	this.letterInputBox.onchange = (function(prop,pm) { return function() {			
			prop.value = ""+this.value;
			pm.updateValue(prop);
		};})(this,propertyManager);

    	//create a paragraph and add the label and the integerInputBox to it
    	this.div = createDiv();
	var table = addPairToTable(this.label, this.letterInputBox)
    	this.div.appendChild(table);

	this.setReadOnly = setReadOnly;
	function setReadOnly(readOnly)
	{
		this.readOnly = readOnly;
		if (this.readOnly || this.disabled)			
			this.letterInputBox.disabled = true;
		else
			this.letterInputBox.disabled = false;
	}



	this.setValue = setValue;
	function setValue(stringVal)
	{
		this.letterInputBox.value = stringVal;
	}

	this.setHidden = setHidden;
	function setHidden(stringVal)
	{
		//booleanCheckBox.value = stringVal;
	}

	this.setDisabled = setDisabled;
	function setDisabled(disabled)
	{
		this.disabled = disabled;
		if (this.disabled || this.readOnly)	
			this.letterInputBox.disabled = true;	
		else
			this.letterInputBox.disabled = false;
		
			

		if (this.disabled)
			this.label.style.color = "#999999";
		else
			this.label.style.color = "#000000";

	}
}
function PSignal(name, value, readonly, hidden, disabled, propertyManager)
{

	this.name = name;
	this.readOnly = readonly;
	this.hidden = hidden;
	this.disabled = disabled;

    //this.label = createLabel(name);
    this.label =createDiv();
    this.button = document.createElement('button');
    this.button.style.cssText='margin-left: 20%;width: 50%;';
   this.button.innerHTML = name;
    this.button.onclick = (function(prop,pm) { return function() {			
			pm.updateValue(prop);
		};})(this,propertyManager);

   	this.div = createDiv();
	//var table = addPairToTable(this.label, this.button)
    	this.div.appendChild(this.button);
        
	this.setReadOnly = setReadOnly;
	function setReadOnly(readOnly)
	{
		this.readOnly = readOnly;
		if (this.readOnly || this.disabled)			
			this.button.disabled = true;
		else
			this.button.disabled = false;
	}


	this.setValue = setValue;
	function setValue(stringVal)
	{
		 this.button.value = stringVal;
	}

	this.setHidden = setHidden;
	function setHidden(stringVal)
	{
		//booleanCheckBox.value = stringVal;
	}

	this.setDisabled = setDisabled;
	function setDisabled(disabled)
	{
		this.disabled = disabled;
		if (this.disabled || this.readOnly)	
			this.button.disabled = true;	
		else
			this.button.disabled = false;
		
			

		if (this.disabled)
			this.label.style.color = "#999999";
		else
			this.label.style.color = "#000000";

	}

}
function PText(name, value, readonly, hidden, disabled, propertyManager)
{

	this.name = name;
	this.readOnly = readonly;
	this.hidden = hidden;
	this.disabled = disabled;

    this.label = createLabel(name);

    this.textArea = document.createElement('textarea');
        this.textArea.style.cssText ='width:155px;height:46px;'
        this.textArea.innerHTML = value;

   	this.div = createDiv();
	var table = addPairToTable(this.label, this.textArea)
    	this.div.appendChild(table);

	this.setReadOnly = setReadOnly;
	function setReadOnly(readOnly)
	{
		this.readOnly = readOnly;
		if (this.readOnly || this.disabled)			
			this.textArea.disabled = true;
		else
			this.textArea.disabled = false;
	}


	this.setValue = setValue;
	function setValue(stringVal)
	{
		 this.textArea.value = stringVal;
	}

	this.setHidden = setHidden;
	function setHidden(stringVal)
	{
		//booleanCheckBox.value = stringVal;
	}

	this.setDisabled = setDisabled;
	function setDisabled(disabled)
	{
		this.disabled = disabled;
		if (this.disabled || this.readOnly)	
			this.textArea.disabled = true;	
		else
			this.textArea.disabled = false;
		
			

		if (this.disabled)
			this.label.style.color = "#999999";
		else
			this.label.style.color = "#000000";

	}

}
function POptions(name, value, readonly, hidden, disabled, propertyManager)
{
	this.name = name;
	this.readOnly = readonly;
	this.hidden = hidden;
	this.disabled = disabled;
	this.value = value;


   
   	 this.div = createDiv();

    	this.label = createLabel(name);

   	this.optionBox = document.createElement('select');
    	this.optionBox.setAttribute("id", "I" + name);
    	this.optionBox.onchange = (function(prop,pm) { return function() {
			prop.value = "";
			for (var i=0; i<this.options.length; i++)
				prop.value += this.options[i].value + "-";
			prop.value += this.selectedIndex;						
			pm.updateValue(prop);
		};})(this,propertyManager);
 
	
   
    
    	//append the label and input box to the paragraph
	var table = addPairToTable(this.label, this.optionBox)
    	this.div.appendChild(table);

	this.setReadOnly = setReadOnly;
	function setReadOnly(readOnly)
	{

		this.readOnly = readOnly;
		if (this.readOnly || this.disabled)			
			this.optionBox.disabled = true;
		else
			this.optionBox.disabled = false;
	}

	this.setValue = setValue;
	function setValue(stringVal)
	{
		//first remove all options
    		for(var i=this.optionBox.options.length-1;i>=0;i--)    
        		this.optionBox.remove(i);
   
		this.value = stringVal;
		//now add new options
 		var options = this.value.split(";")[0].split("-"); //the options will be separated by "-"
		
    		for (var i = 0; i < options.length-1; i++) {
       	 		var option = document.createElement('option');
       	 		option.value = options[i];
       			option.innerHTML = options[i];
       	 		this.optionBox.appendChild(option);
    		}
		var selIndex = parseInt(options[options.length-1]);
		this.optionBox.selectedIndex = selIndex;
		
	}


	this.setHidden = setHidden;
	function setHidden(stringVal)
	{
		//booleanCheckBox.value = stringVal;
	}


	this.setDisabled = setDisabled;
	function setDisabled(disabled)
	{

		this.disabled = disabled;
		if (this.disabled || this.readOnly)	
			this.optionBox.disabled = true;	
		else
			this.optionBox.disabled = false;
		
			
		if (this.disabled)
			this.label.style.color = "#999999";
		else
			this.label.style.color = "#000000";

	}

	this.setValue(this.value);
}


function PPercent(name, value, readonly, hidden, disabled, valueChangeHandler)
{

	this.name = name;
	this.readOnly = readonly;
	this.hidden = hidden;
	this.disabled = disabled;
	
    	if (typeof(value) === 'undefined') 
       		value = 0.5;    
    
    	this.label = createLabel(name);

    	this.rangeInputBox = document.createElement('input');
    	this.rangeInputBox.value = value;
    	this.rangeInputBox.type = "range";   
    	this.rangeInputBox.min = 0;
    	this.rangeInputBox.max = 1;
    	this.rangeInputBox.step = 0.1;
    	this.rangeInputBox.onchange = (function(prop,pm) { return function() {			
			prop.value = ""+this.value;
			pm.updateValue(prop);
		};})(this,propertyManager);

    	//create a paragraph and add the label and the integerInputBox to it
   	this.div = createDiv();
	var table = addPairToTable(this.label, this.rangeInputBox)
    	this.div.appendChild(table);


	this.setReadOnly = setReadOnly;
	function setReadOnly(readOnly)
	{
		this.readOnly = readOnly;
		if (this.readOnly || this.disabled)			
			this.rangeInputBox.disabled = true;
		else
			this.rangeInputBox.disabled = false;
	}

	this.setValue = setValue;
	function setValue(stringVal)
	{
            if (newValue >= 0 && newValue <= 1)
                rangeInputBox.value = newValue;
	}

	this.setHidden = setHidden;
	function setHidden(stringVal)
	{
		//booleanCheckBox.value = stringVal;
	}


	this.setDisabled = setDisabled;
	function setDisabled(disabled)
	{
		this.disabled = disabled;
		if (this.disabled || this.readOnly)	
			this.rangeInputBox.disabled = true;	
		else
			this.rangeInputBox.disabled = false;
		
			

		if (this.disabled)
			this.label.style.color = "#999999";
		else
			this.label.style.color = "#000000";

	}

	this.setValue(this.value);

}


function PColor(name, value, readonly, hidden, disabled, propertyManager)
{

	this.name = name;
	this.readOnly = readonly;
	this.hidden = hidden;
	this.disabled = disabled;
	this.value = value;
    
    this.div = createDiv();

    this.label = createLabel(name);
    this.colorInputBox = document.createElement('input');
    //append the label and input box to the paragraph
    this.div.appendChild(this.label);
    this.div.appendChild(this.colorInputBox);
   
    this.colorPicker = new jscolor.color(this.colorInputBox, {})
	this.colorPicker.pickerClosable = true;

	var cp = this.colorPicker;

   this.colorPicker.onImmediateChange = (function(prop,pm) { return function() {	
		
			prop.value = ""+ hexToRgb(this);
alert(this + " " + prop.value);
			pm.updateValue(prop);
		};})(this,propertyManager);



	this.setReadOnly = setReadOnly;
	function setReadOnly(readOnly)
	{
		this.readOnly = readOnly;
		if (this.readOnly || this.disabled)			
			this.colorInputBox.disabled = true;
		else
			this.colorInputBox.disabled = false;
	}

	this.setValue = setValue;
	function setValue(stringVal)
	{
                this.colorInputBox.value = stringRgbToHex(stringVal);
	}

	this.setHidden = setHidden;
	function setHidden(stringVal)
	{
		//booleanCheckBox.value = stringVal;
	}


	this.setDisabled = setDisabled;
	function setDisabled(disabled)
	{
		this.disabled = disabled;
		if (this.disabled || this.readOnly)	
			this.colorInputBox.disabled = true;	
		else
			this.colorInputBox.disabled = false;
		
			

		if (this.disabled)
			this.label.style.color = "#999999";
		else
			this.label.style.color = "#000000";

	}

	function hexToRgb(hex) {
    		var bigint = parseInt(hex, 16);
    		var r = (bigint >> 16) & 255;
    		var g = (bigint >> 8) & 255;
    		var b = bigint & 255;

   		return r + "-" + g + "-" + b + "-255";
	}

	function componentToHex(c) {
   		 var hex = c.toString(16);
    		return hex.length == 1 ? "0" + hex : hex;
	}

	function rgbToHex(r, g, b) {
   		 return "#" + componentToHex(r) + componentToHex(g) + componentToHex(b);
	}

	function stringRgbToHex(v) {
		var split = v.split("-");
		return rgbToHex(parseInt(split[0]), parseInt(split[1]), parseInt(split[2]));
	}



	//this.setValue(this.value);
}

function PFileInput(name, value, readonly, hidden, disabled, propertyManager)
{

	this.name = name;
	this.readOnly = readonly;
	this.hidden = hidden;
	this.disabled = disabled;
	this.value = value;


    this.label = createLabel(name);

    this.fileBox = document.createElement('input');

 
    this.fileBox.type =  "file";
    this.fileBox.onchange = (function(prop,pm) { return function() {			
			prop.value = this.files[0];
			pm.updateFileInputValue(prop);
		};})(this,propertyManager);
    //create a paragraph and add the label and the checkbox to it

    this.div = createDiv();
	var table = addPairToTable(this.label, this.fileBox)
    	this.div.appendChild(table);



	this.setReadOnly = setReadOnly;
	function setReadOnly(readOnly)
	{
		this.readOnly = readOnly;
		if (this.readOnly || this.disabled)			
			this.fileBox.disabled = true;
		else
			this.fileBox.disabled = false;
	}

	this.setValue = setValue;
	function setValue(stringVal)
	{
		this.fileBox.value = stringVal;
	}

	this.setHidden = setHidden;
	function setHidden(stringVal)
	{
		//booleanCheckBox.value = stringVal;
	}

	this.setDisabled = setDisabled;
	function setDisabled(disabled)
	{
		this.disabled = disabled;
		if (this.disabled || this.readOnly)	
			this.fileBox.disabled = true;	
		else
			this.fileBox.disabled = false;
		
			

		if (this.disabled)
			this.label.style.color = "#999999";
		else
			this.label.style.color = "#000000";

	}



}

function PFileOutput(name, value, readonly, hidden, disabled, propertyManager)
{

	this.name = name;
	this.readOnly = readonly;
	this.hidden = hidden;
	this.disabled = disabled;

    this.label = createLabel(name);

    this.button = document.createElement('button');

   this.button.innerHTML = "Save";
    this.button.onclick = (function(prop,pm) { return function() {			
			pm.updateFileOutputValue(prop);
		};})(this,propertyManager);

   	this.div = createDiv();
	var table = addPairToTable(this.label, this.button)
    	this.div.appendChild(table);

	this.setReadOnly = setReadOnly;
	function setReadOnly(readOnly)
	{
		this.readOnly = readOnly;
		if (this.readOnly || this.disabled)			
			this.button.disabled = true;
		else
			this.button.disabled = false;
	}


	this.setValue = setValue;
	function setValue(stringVal)
	{
		 this.button.value = stringVal;
	}

	this.setHidden = setHidden;
	function setHidden(stringVal)
	{
		//booleanCheckBox.value = stringVal;
	}

	this.setDisabled = setDisabled;
	function setDisabled(disabled)
	{
		this.disabled = disabled;
		if (this.disabled || this.readOnly)	
			this.button.disabled = true;	
		else
			this.button.disabled = false;
		
			

		if (this.disabled)
			this.label.style.color = "#999999";
		else
			this.label.style.color = "#000000";

	}

}


function PProgress(name, value, readonly, hidden, disabled, projectManager, determinate)
{

	this.name = name;
	this.readOnly = readonly;
	this.hidden = hidden;
	this.disabled = disabled;

	this.progressintervalVar = -1;
    
    	this.div = createDiv();

    	this.label = createLabel(name);

	this.progressBar = new ProgressBar(determinate);

  
    	this.progressBarDiv = this.progressBar.div;
  
    
	var table = addPairToTable(this.label, this.progressBarDiv)
    	this.div.appendChild(table);


	this.setReadOnly = setReadOnly;
	function setReadOnly(readOnly)
	{
	}

	this.setValue = setValue;
	function setValue(stringVal)
	{
	}

	this.setHidden = setHidden;
	function setHidden(stringVal)
	{
	}

	this.setDisabled = setDisabled;
	function setDisabled(disabled)
	{
	}
    
}

function ProgressBar(determinate)
{
	this.div = createDiv();
	var thisObj = this;
	
	this.images = [];

	for (var i=0; i<=10; i++)
	{
		var im = new Image();
		if (determinate)
			im.src = "Images/progressbar_d_" + i + ".png";
		else
			im.src = "Images/progressbar_i_" + i + ".png";
		this.images.push(im);
	}

	this.value = 0;

	this.interval = -1;
	if (!determinate)
	{
		this.interval = setInterval(function()
		{
			thisObj.value++;
			if (thisObj.value > 10) thisObj.value = 0;
			while (thisObj.div.children.length > 0) 
				thisObj.div.removeChild(thisObj.div.children[0]);
			thisObj.div.appendChild(thisObj.images[thisObj.value]);
		}, 100);
	}	

	this.setValue = setValue;
	function setValue(val)
	{
		if (!determinate)
			return;

		val = parseInt(val * 10,10);
		while (this.div.children.length > 0) 
			this.div.removeChild(this.div.children[0]);
		this.div.appendChild(this.images[this.value]);
		
	}

	this.stop = stop;
	function stop()
	{
		if (determinate && this.interval >= 0)
			clearInterval(this.interval);
	}
}





