function PropertyManager(div, name)
{
	this.div = div;
	this.name = name;
	var props = [];

	var thispm = this;	

	applyDivStyle(this.div);

	var heading = document.createElement("h4");
	heading.style.backgroundColor = "#99BBCC";
	heading.innerHTML = name;
	this.div.appendChild(heading);

	//first, get all properties
	var xmlHttpRequest = getXMLHttpRequest();
	xmlHttpRequest.onreadystatechange = function()
	{
		if (xmlHttpRequest.readyState === 4 && xmlHttpRequest.status === 200)
		{
			alert(thispm.name + " " + xmlHttpRequest.response);
			thispm.updateCommand(xmlHttpRequest.response);
			
			//once the property manager is inited on the server, start polling
			var interval = setInterval(function()
			{
				var xmlrequest2 = getXMLHttpRequest();
				xmlrequest2.onreadystatechange = function()
				{
					if (xmlrequest2.readyState === 4 && xmlrequest2.status === 200)
					{
						thispm.updateCommand(xmlrequest2.response);
					}
				};	
	
				xmlrequest2.open("GET", "PropertyManagement?page=pollprops&propManagerName="+thispm.name, true);
        			xmlrequest2.send(null);
			}, 1000);

		
		}
	};
	
        xmlHttpRequest.open("GET", "PropertyManagement?page=init&propManagerName=" + this.name, true);	
        xmlHttpRequest.send(null);

	


	this.updateCommand = updateCommand;
	function updateCommand(propertiesString)
	{
		if (propertiesString === "")
			return;


		var propArr = propertiesString.split(";");		

   		for (var i = 0; i < propArr.length; i++) {

        	
       			var prop = propArr[i].split(",");
        		var type = prop[0];

			
			if (type === "addProperty")
			{
				var propName = prop[2];

				var propType = prop[3];

				var propValue = prop[4];
				
				var propReadOnly = true;
				if (prop[5] === "false") propReadOnly = false;

				var propHidden = true
				if (prop[6] === "false") propHidden = false;

				var propDisabled = true;
				if (prop[7] === "false") propDisabled = false;

				this.addProperty(propName, propType, propValue, propReadOnly, propHidden, propDisabled);
			}
        		     
       			else if (type === "readOnlyChanged")
			{
                        	var propName = prop[2];				
				var propReadOnly = true;
				if (prop[3] === "false") propReadOnly = false;				

				this.readOnlyChanged(propName, propReadOnly);
			}

       			else if (type === "valueOnlyChanged")
			{
                        	var propName = prop[2];				
				var propValue = prop[3];				

				this.valueChanged(propName, propReadOnly);
			}

       			else if (type === "hiddenChanged")
			{
                        	var propName = prop[2];				
				var propHidden = true;
				if (prop[3] === "false") propHidden = false;					

				this.hiddenChanged(propName, propHidden);
			}

       			else if (type === "disabledChanged")
			{
                        	var propName = prop[2];					
				var propDisabled = true;
				if (prop[3] === "false") propDisabled = false;					

				this.disabledChanged(propName, propDisabled);
			}

       			else if (type === "removeProperty")
			{
                        	var propName = prop[2];
				this.removeProperty(propName);
			}

    		}

		
 
	}

	this.addProperty = addProperty;
	function addProperty(name, type, value, readOnly, hidden, disabled)
	{  

		var prop = null; 
   
                switch (type) {
                    case "PInteger":     //value format: 10
                        prop = new PInteger(name, value, readOnly, hidden, disabled,this);
                        break;
                    case "PColor":       //value format: 200-150-150-255
                        prop = new PColor(name, value, readOnly, hidden, disabled,this);
                        break;
                    case "PPercent":     //value format: 0.5
                        prop = new PPercent(name, value, readOnly, hidden, disabled,this);
                        break;
                    case "PFileOutput":    //value format: null
                        prop = new PFileOutput(name, value, readOnly, hidden, disabled,this);
                        break;
                    case "PFileInput":    //value format: null
                        prop = new PFileInput(name, value, readOnly, hidden, disabled, this);
                        break;
                    case "PDouble":      //value format: 100.0
                        prop = new PDouble(name, value, readOnly, hidden, disabled,this);
                        break;
                    case "PBoolean":     //value format: 0
                        prop =  new PBoolean(name, value, readOnly, hidden, disabled,this);
                        break;                  
                    case "PString":      //value format:
                        prop = new PString(name, value, readOnly, hidden, disabled,this);
                        break;
                    case "POptions":
                       prop =  new POptions(name, value, readOnly, hidden, disabled,this);
                        break;
                    case "PProgress":
                        prop = new PProgress(name, value, readOnly, hidden, disabled,this);                 
                        break;
                    default:
                        //tbd
                }


		if (prop == null) return;

                prop.setReadOnly(readOnly);
                prop.setHidden(hidden);
                prop.setDisabled(disabled);

		props.push(prop);
		
		this.div.appendChild(prop.div);
	}

	this.removeProperty = removeProperty;
	function removeProperty(name)
	{
	    for (var i=0; i<props.length; i++)
		if (props[i].name === name)
		{     
	        	this.div.removeChild(props[i].div);			
			props.splice(i,1);			
			return;
		}
	}

	this.valueChanged = valueChanged;
	function valueChanged(name, newvalue)
	{
	    for (var i=0; i<props.length; i++)
		if (props[i].name.equals(name))
		{     
	        	props[i].setValue(newvalue);
			return;
		}
	}

	this.readOnlyChanged = readOnlyChanged;
	function readOnlyChanged(name, newvalue)
	{
	    for (var i=0; i<props.length; i++)
		if (props[i].name == name)
		{     
	        	props[i].setReadOnly(newvalue);
			return;
		}
	}

	this.hiddenChanged = hiddenChanged;
	function hiddenChanged(name, newvalue)
	{
	    for (var i=0; i<props.length; i++)
		if (props[i].name.equals(name))
		{     
	        	props[i].setHidden(newvalue);
			return;
		}
	}

	this.disabledChanged = disabledChanged;
	function disabledChanged(name, newvalue)
	{
	    for (var i=0; i<props.length; i++)
		if (props[i].name === name)
		{     
	        	props[i].setDisabled(newvalue);
			return;
		}
	}



	this.updateValue = updateValue;
	function  updateValue(prop) {

   	 var propName = prop.name;    
   	 var propValue = prop.value;

   	 var propertyManagerName = this.name;

   
   	 var url = "PropertyManagement?page=updateProperty&newValue=" + propValue + "&property=" + propName;
   	 url += "&propertyManager=" + propertyManagerName;




   	
	var xmlHttpRequest = getXMLHttpRequest();
	xmlHttpRequest.onreadystatechange = function()
	{
		if (xmlHttpRequest.readyState === 4 && xmlHttpRequest.status === 200)
		{
		}
	};

        xmlHttpRequest.open("GET", url, true);	
        xmlHttpRequest.send(null);

	}



	this.updateFileInputValue = updateFileInputValue;
	function updateFileInputValue(prop) {

		prop.setDisabled(true);

		var para = document.createElement("p");
		var div1 = document.createElement("div");
		div1.innerHTML = "Uploading file to the server.. <br>";
		para.appendChild(div1);
		var pb = new ProgressBar(false);
		para.appendChild(pb.div);
		prop.div.appendChild(para);
   
 
    		var propName = prop.name;
    		var propValue = prop.value;
		var propertyManagerName = this.name;

    
    		var url = "Uploads?page=uploadData&propertyManager=" + propertyManagerName
         	   + "&property=" + propName;

    		var formData = new FormData();
    		formData.append("File", prop.value);

		xmlHttpRequest.onreadystatechange = function()
		{
			if (xmlHttpRequest.readyState === 4 && xmlHttpRequest.status === 200)
			{
				prop.setDisabled(false);
				pb.stop();
				prop.div.removeChild(para);
				
			}
		};
    	
    		xmlHttpRequest.open("POST", url, true);
    		xmlHttpRequest.send(formData);

	}





	this.updateFileOutputValue = updateFileOutputValue;
	function updateFileOutputValue(prop) {
   

   	 //set the url of the link that will be  used to download the file
   	 var url = "Downloads?page=downloadData&fileName=" + fileName;

	window.open(url);

	}


	function applyDivStyle(div)
	{
		div.style.marginTop = "10px";
    		div.style.textAlign = "left";
    		div.style.width = "300px";
		div.style.backgroundColor = "#EEEEFF"; 

    		div.style.padding = "5px 10px 6px"; 
    		div.style.lineHeight = "100%";
    		div.style.textDecoration = "none";
	
	}

}










