function ViewerManager(div, dataSourceManager)
{
	this.div = div;

	var thisObject = this;

	this.viewers = [];
	this.selectedViewerIndex = -1;

	var selectedViewer = "";

	////// viewer creation /////////////

	var divElement1 = document.createElement("div");
	applyDivStyle(divElement1);
	divElement1.style.width = "820px";
	var createTable = new Table(4);
	divElement1.appendChild(createTable.table);
	this.div.appendChild(divElement1);

	var cdiv1 = document.createElement("div");
	cdiv1.innerHTML = "Viewer type";
	var cdiv2 = document.createElement("div");
	cdiv2.innerHTML = "<select id='viewerFactories'>  <option value='selectOne'>Select One</option> </select>";
	createTable.cells[0].appendChild(cdiv1);
	createTable.cells[0].appendChild(cdiv2);
	document.getElementById("viewerFactories").onchange = viewerTypeChanged;
	createTable.cells[0].width = "200px";


	var cdiv3 = document.createElement("div");
	cdiv3.innerHTML = "Viewer name";
	var cdiv4 = document.createElement("div");
	cdiv4.innerHTML = "<input type='text' id='viewerName' />";
	createTable.cells[1].appendChild(cdiv3);
	createTable.cells[1].appendChild(cdiv4);
	createTable.cells[1].width = "200px";


	var cdiv5 = document.createElement("div");
	cdiv5.innerHTML = "<font size='1'>*This viewer requires: " + "data" + "</font>";
	var cdiv6 = document.createElement("div");
	cdiv6.innerHTML = "Available data sources";
	var createDataList = document.createElement("select");
	createDataList.multiple = true;
	createTable.cells[2].appendChild(cdiv6);
	createTable.cells[2].appendChild(createDataList);
	createTable.cells[2].appendChild(cdiv5);
	createTable.cells[2].width = "200px";
	createDataList.width='150px';
	createDataList.style.width = '150px'
	cdiv5.style.display = "none";
	cdiv6.style.display = "none";
	createDataList.style.display = "none";

	var cdiv7 = document.createElement("div");
	var be = document.createElement("button");
	be.class = 'small button blue';
	be.type = 'button';
	be.innerHTML = "Create Viewer";
	be.onclick = createViewer;
	cdiv7.appendChild(be);
	cdiv7.style.display = "none";
	createTable.cells[3].appendChild(cdiv7);
	createTable.cells[3].width = "200px";
	createTable.cells[3].style.verticalAlign = "middle";
	createTable.cells[3].align = "right";


	////////////// Viewers and Links ///////////////

	var table = new Table(2);
	this.div.appendChild(table.table);
	var divElement2 = document.createElement("div");
	applyDivStyle(divElement2);
	var divElement3 = document.createElement("div");
	applyDivStyle(divElement3);
	divElement3.style.width = "500px";			
	table.cells[0].appendChild(divElement2);
	table.cells[1].appendChild(divElement3);

	
		/// Viewers ////
		
	
 	var viewerListHeader = document.createElement("h4");
	viewerListHeader.style.backgroundColor = "#99BBCC";
	viewerListHeader.innerHTML = "Available Viewers";
 	divElement2.appendChild(viewerListHeader);
	var viewersElement = document.createElement("div");	
	divElement2.appendChild(viewersElement);


		// Links ////
 	var linkListHeader = document.createElement("h4");
	linkListHeader.style.backgroundColor = "#99BBCC";
	linkListHeader.innerHTML = "Links between viewers";
 	divElement3.appendChild(linkListHeader);
	var linksElement = document.createElement("div");	
	divElement3.appendChild(linksElement);


	var propertyManagers = [];
	var propertyManagerDivs = []; 

	var divs = [];
	var deleteButtons = [];
	var launchButtons = [];

	var links1 = [];
	var links2 = [];
		
	

        var dfRequest = getXMLHttpRequest();
	dfRequest.onreadystatechange = function()
	{
			if (dfRequest.readyState === 4 && dfRequest.status === 200)
                       	{
				var data = dfRequest.responseText;				

		                 //we will be creating a combo box options for the datafactories
                    		var dataSplit = (data).split(",");

                    		var viewerFactories = document.getElementById("viewerFactories");

                    		for (var i = 0; i < dataSplit.length; i++)
				{

                        		var opt = document.createElement('option');
                        		opt.setAttribute("value", dataSplit[i]);
                        		opt.innerHTML = dataSplit[i];

                        		viewerFactories.appendChild(opt);
                    		}

				
				//createInputElement.style.display = "block";
				
				
				
			}	
	};
        dfRequest.open("GET", "ViewerManagement?page=getViewerFactories", true);
        dfRequest.send(null);


        //make request for viewer creation
	var cdRequest = getXMLHttpRequest();
	cdRequest.onreadystatechange = function()
	{
			if (cdRequest.readyState === 4 && cdRequest.status === 200)
			{						
				refreshViewers(cdRequest.response, false);	

			}
	};
        cdRequest.open("GET", "ViewerManagement?page=getCurrentViewers", true);
        cdRequest.send(null)



	this.viewerTypeChanged = viewerTypeChanged;
        function viewerTypeChanged()
	{
                	var selectBox = document.getElementById("viewerFactories");
                
                   	if (selectBox.selectedIndex > 0)
			{                    
	                    document.getElementById("viewerName").value = getUniqueName(selectBox.options[selectBox.selectedIndex].value);
			    cdiv5.style.display = "block";
			    cdiv6.style.display = "block";
		            createDataList.style.display = "block";
			    cdiv7.style.display = "block";

				while (createDataList.children.length > 0)
					createDataList.removeChild(createDataList.children[0]);

			    for (var i=0; i<dataSourceManager.dataSources.length; i++)
				{
					
					var o = document.createElement("option");
					o.value = dataSourceManager.dataSources[i];
					o.innerHTML = dataSourceManager.dataSources[i];
					createDataList.appendChild(o);
				}
                	}
			else
			{
	                    document.getElementById("viewerName").value = "";
			    cdiv5.style.display = "none";
			    cdiv6.style.display = "none";
		            createDataList.style.display = "none";
			    cdiv7.style.display = "none";
			}

         }




        function createViewer() {

		var selectBox = document.getElementById("viewerFactories");
		var factoryType = selectBox.options[selectBox.selectedIndex].value;
		var viewerName = document.getElementById("viewerName").value;

		var data = "";
		for (var i=0; i<createDataList.options.length; i++)
		{
			if (data.length != 0) data +=  ";";
			if (createDataList.options[i].selected)
				data += createDataList.options[i].value;
		}		

	        document.getElementById("viewerName").value = "";
		cdiv5.style.display = "none";
		cdiv6.style.display = "none";
		createDataList.style.display = "none";
		cdiv7.style.display = "none";
                document.getElementById("viewerFactories").selectedIndex = 0;

                //make request for data creation
		var cdRequest = getXMLHttpRequest();
		cdRequest.onreadystatechange = function()
		{
			if (cdRequest.readyState === 4 && cdRequest.status === 200)
			{
				if (cdRequest.response.indexOf("Error") == 0)
					alert(cdRequest.response);
				else
					refreshViewers(cdRequest.response, true);	
			}
		};
		var url = "ViewerManagement?page=createViewer&type="+factoryType+"&viewerName="+viewerName+"&data=" + data;
		alert(url);
        	cdRequest.open("GET", url, true);
        	cdRequest.send(null);
		

         }

        function deleteViewer(which) {

                //make request for data deletion
		var cdRequest = getXMLHttpRequest();
		cdRequest.onreadystatechange = function()
		{
			if (cdRequest.readyState === 4 && cdRequest.status === 200)
				refreshViewers(cdRequest.response, false);	
		};
        	cdRequest.open("GET", "ViewerManagement?page=deleteViewer&viewerName="+thisObject.viewers[which], true);
        	cdRequest.send(null);
         }



	function refreshViewers(viewerlist, added)
	{
		if (viewerlist === ""){			
        		viewersElement.innerHTML = "No viewers available yet/";
			return;
		}    		

        	var viewerArray = viewerlist.split(";");

		//check if it is the same as before
		var sameAsBefore = true;
		if (viewerArray.length != thisObject.viewers.length)
			sameAsBefore = false;
		else
			for (var i=0; i<viewerArray.length; i++)
				if (viewerArray[i] !== thisObject.viewers[i])
				{
					sameAsBefore = false;
					break;
				}
		if (sameAsBefore) return;


		viewersElement.innerHTML = "";
		thisObject.viewers = viewerArray;

		divs = [];
		deleteButtons = [];
		launchButtons = [];

        	for (var i = 0; i < viewerArray.length; i++)
		{              		

			var div = document.createElement('div');			
			div.onmouseover = (function(i) { return function() {viewerHovered(i);};})(i); 
			div.onclick = (function(i) { return function() {viewerSelected(i);};})(i);			

			divs.push(div);

     			var table     = document.createElement("table");
			table.width = '100%';
        		var tableBody = document.createElement("tbody");
			var row = document.createElement("tr");
			var cellLeft = document.createElement("td"); 			
			var cellRight = document.createElement("td");
			cellRight.style.backgrounColor = "#00FF00";
			cellRight.align = 'right'; 

			var label = document.createElement('div');
			label.style.height = '30px';
        		label.innerHTML = viewerArray[i];
			label.style.float = 'left';
			cellLeft.appendChild(label);
			row.appendChild(cellLeft);    		

            		var but = document.createElement('button');
			but.style.display = "none";
            		but.style.float = 'right';
            		but.innerHTML = 'Delete';
            		but.class = 'small button blue';	        		
            		but.onclick = (function(i) { return function() {deleteViewer(i);};})(i);
			deleteButtons.push(but);

            		var but2 = document.createElement('button');
			but2.style.display = "none";
            		but2.style.float = 'right';
            		but2.innerHTML = 'Launch';
            		but2.class = 'small button blue';	        		
            		but2.onclick = (function(i) { return function() {launchViewer(i);};})(i);
			launchButtons.push(but2);

			cellRight.appendChild(but2);
            		cellRight.appendChild(but);
			row.appendChild(cellRight);

			tableBody.appendChild(row);
			table.appendChild(tableBody);

			div.appendChild(table);

			viewersElement.appendChild(div); 

			
			
        	}

		if (added)
		{
			thisObject.selectedViewerIndex = thisObject.viewers.length-1;
			viewerSelected(thisObject.viewers.length-1);
		}


		showLinking();


	}


	function showLinking()
	{
		//get the linking information
    		var xmlHttpRequest = getXMLHttpRequest();
   		xmlHttpRequest.onreadystatechange = function()
		{
			
			if (xmlHttpRequest.readyState === 4 && xmlHttpRequest.status === 200)
                       	{
				var items = xmlHttpRequest.responseText.split("\t");

				links1 = [];
				links2 = [];

				for (var i=0; i<items.length; i+=2)
				{
					links1.push(items[i]);
					links2.push(items[i+1]);
				}				
				
           			linksElement.innerHTML = "";
				for (var i=0; i<thisObject.viewers.length-1; i++)
					for (var j=i+1; j<thisObject.viewers.length; j++)
						addLink(i,j);
			}
		};

   		var url = "ViewerManagement?page=getViewerLinks";		
    		xmlHttpRequest.open("GET", url, true);
    		xmlHttpRequest.send(null);
	}

	function launchViewer(which)
	{
    		var xmlHttpRequest = getXMLHttpRequest();
   		 xmlHttpRequest.onreadystatechange = function()
		{
			
			if (xmlHttpRequest.readyState === 4 && xmlHttpRequest.status === 200)
                       	{
				
                		var response = xmlHttpRequest.responseText.split(";");  //e.g viewer.html;name-of-viewer

                    		var url = getURL() + response[0] + "?viewerName=" + response[1];
                   		
				var win = window.open(url);
                    		win.focus();
           
			}
		};

   		var url = "ViewerManagement?page=launchViewer&viewerName=" + thisObject.viewers[which];
    		xmlHttpRequest.open("GET", url, true);
    		xmlHttpRequest.send(null);
	}


	function viewerHovered(which)
	{
		for (var i=0; i<thisObject.viewers.length; i++)
			if (thisObject.selectedViewerIndex === i)
				divs[i].style.backgroundColor = "#88CCFF";
			else
				divs[i].style.backgroundColor = 'transparent';

		if (thisObject.selectedDataSourceIndex === which)
			divs[which].style.backgroundColor = "#66AACC";
		else
			divs[which].style.backgroundColor = "#BBBBBB";
	}

	function viewerSelected(which)
	{

		for (var i=0; i<thisObject.viewers.length; i++)
		{
			divs[i].style.backgroundColor = 'transparent';
			deleteButtons[i].style.display = 'none';
			launchButtons[i].style.display = 'none';
		}
		divs[which].style.backgroundColor = "#88CCFF";
		deleteButtons[which].style.display = 'block';
		launchButtons[which].style.display = 'block';
		thisObject.selectedViewerIndex = which;
		selectedViewer = thisObject.viewers[which];		

	}

	function getUniqueName(viewerType)
	{
		var cnt = 1000;
		while (true)
		{			
			var exists = false;
			for (var i=0; i<thisObject.viewers.length; i++)
				if (thisObject.viewers[i] === ("Viewer-"+viewerType+cnt))
				{	
					exists = true;
					break;
				}
			if (exists || cnt == 0) return ("Viewer-"+viewerType+(cnt+1));
			cnt--;
		}
	}


	function applyDivStyle(div)
	{
		div.style.marginTop = "10px";
    		div.style.textAlign = "left";
    		//div.style.position = " relative";
    		//div.style.float = "left";
    		div.style.width = "300px";
		div.style.backgroundColor = "#EEEEFF"; 

    		div.style.padding = "5px 10px 6px"; 
    		div.style.lineHeight = "100%";
    		div.style.textDecoration = "none";
	
	}


	function Table(nrcols)
	{

   		this.table     = document.createElement("table");
		this.table.width = '800px';
        	var tableBody = document.createElement("tbody");
		this.table.appendChild(tableBody);
		var row = document.createElement("tr");
		tableBody.appendChild(row);

		this.cells = [];
		for (var i=0; i<nrcols; i++)
		{
			var cell = document.createElement("td"); 
			row.appendChild(cell);
			this.cells.push(cell);
			cell.style.verticalAlign = "top";
		}			
	}

	function addLink(v1, v2)
	{

			var div = document.createElement("div");

			var table = new Table(3);     			
			table.table.width = '100%';
        		

			var v1label = document.createElement('div');
			v1label.style.height = '30px';
        		v1label.innerHTML = thisObject.viewers[v1];			
			table.cells[0].appendChild(v1label);
			table.cells[0].align = "left";

			var v2label = document.createElement('div');
			v2label.style.height = '30px';
        		v2label.innerHTML = thisObject.viewers[v2];			
			table.cells[2].appendChild(v2label);
			table.cells[2].align = "right";					

            		var but = document.createElement('button');            		
            		but.innerHTML = 'Link';
			if (areLinked(v1,v2))
				but.innerHTML = 'Unlink';
            		but.class = 'small button blue';	        		
            		but.onclick = (function(v1,v2,but) { return function() {linkViewers(v1,v2,but);};})(v1,v2,but);
			table.cells[1].appendChild(but);			
			table.cells[1].align = "center";

			div.appendChild(table.table);

			linksElement.appendChild(div); 
	}

	function linkViewers(v1,v2,but)
	{
    		var xmlHttpRequest = getXMLHttpRequest();
   		xmlHttpRequest.onreadystatechange = function()
		{			
			if (xmlHttpRequest.readyState === 4 && xmlHttpRequest.status === 200)
				showLinking();
		};

		var url = ""
		if (but.innerHTML === "Link")
		{
			//but.innerHTML = "Unlink";
			url = "ViewerManagement?page=linkViewers&viewer1=" + thisObject.viewers[v1] + "&viewer2=" + thisObject.viewers[v2];			
		}
		else
		{
			//but.innerHTML = "Link";			
			url = "ViewerManagement?page=unlinkViewers&viewer1=" + thisObject.viewers[v1] + "&viewer2=" + thisObject.viewers[v2];			
		}
	
    		xmlHttpRequest.open("GET", url, true);
    		xmlHttpRequest.send(null);
	}

	function areLinked(v1, v2)
	{
		for (var i=0; i<links1.length; i++)
			if (links1[i] === thisObject.viewers[v1] && links2[i] === thisObject.viewers[v2])
				return true;
			else if (links1[i] === thisObject.viewers[v2] && links2[i] === thisObject.viewers[v1])
				return true;
		return false;
	}


}










