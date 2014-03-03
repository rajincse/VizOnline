function DataSourceManager(div)
{
	this.div = div;

	var thisObject = this;

	this.dataSources = [];
	this.selectedDataSourceIndex = -1;

	var selectedDataSource = "";

	var divElement1 = document.createElement("div");
	applyDivStyle(divElement1);
	divElement1.style.width = "620px";
	var createDataHeader = document.createElement("h4");
	createDataHeader.style.backgroundColor = "#99BBCC";
	createDataHeader.innerHTML = "Create New Data Source";
 	//divElement1.appendChild(createDataHeader);
	this.div.appendChild(divElement1);


	var divElement2 = document.createElement("div");	
	applyDivStyle(divElement2);
 	var dataListHeader = document.createElement("h4");
	dataListHeader.style.backgroundColor = "#99BBCC";
	dataListHeader.innerHTML = "Available Data Sources";
 	divElement2.appendChild(dataListHeader);


     	var table     = new Table(2);			
	
	table.cells[0].appendChild(divElement2);

	var propertyManagers = [];
	var propertyManagerDivs = []; 

	var divElement3 = document.createElement("div");
	table.cells[1].appendChild(divElement3);

	this.div.appendChild(table.table);

	var divs = [];
	var deleteButtons = [];
	


	////// the create area /////////////////////////

	var createTable = new Table(3);
	divElement1.appendChild(createTable.table);

	var cdiv1 = document.createElement("div");
	cdiv1.innerHTML = "Data type";
	createTable.cells[0].appendChild(cdiv1);
	var cdiv2 = document.createElement("div");
	cdiv2.innerHTML = "<select id='dataFactories' width='150' style='width: 150px;'>  <option value='selectOne'>Select One</option> </select>";
	createTable.cells[0].appendChild(cdiv2);
	createTable.cells[0].width = 200;
	document.getElementById("dataFactories").onchange = dataTypeChanged;

	var cdiv3 = document.createElement("div");
	cdiv3.innerHTML = "Data name";
	createTable.cells[1].appendChild(cdiv3);
	var cdiv4 = document.createElement("div");
	cdiv4.innerHTML = "<input type='text' id='dataSourceName' />";
	createTable.cells[1].appendChild(cdiv4);
	createTable.cells[1].width = 200;

	var cdiv5 = document.createElement("div");
	var be = document.createElement("button");
	be.type = 'button';
	be.innerHTML = "Create Dataset";
	be.onclick = createData;
	cdiv5.appendChild(be);
	cdiv5.style.display = "none";	
	createTable.cells[2].style.verticalAlign = "middle";
	createTable.cells[2].align = "right";
	createTable.cells[2].width = 200;
	createTable.cells[2].appendChild(cdiv5);

	

	////////////////

	var dataSourcesElement = document.createElement("div");	
	divElement2.appendChild(dataSourcesElement);	


        var dfRequest = getXMLHttpRequest();
	dfRequest.onreadystatechange = function()
	{
			if (dfRequest.readyState === 4 && dfRequest.status === 200)
                       	{
				var data = dfRequest.responseText;				

		                 //we will be creating a combo box options for the datafactories
                    		var dataSplit = (data).split(",");

                    		var dataFactories = document.getElementById("dataFactories");

                    		for (var i = 0; i < dataSplit.length; i++)
				{

                        		var opt = document.createElement('option');
                        		opt.setAttribute("value", dataSplit[i]);
                        		opt.innerHTML = dataSplit[i];

                        		dataFactories.appendChild(opt);
                    		}
				
				//createInputElement.style.display = "block";
				
				
				
			}	
	};
        dfRequest.open("GET", "DataManagement?page=getDataFactories", true);
        dfRequest.send(null);


        //make request for data creation
	var cdRequest = getXMLHttpRequest();
	cdRequest.onreadystatechange = function()
	{
			if (cdRequest.readyState === 4 && cdRequest.status === 200)
			{	

				var dataArray = cdRequest.response.split(";");			
				for (var i=0;  cdRequest.response.trim().length !== 0 && i<dataArray.length; i++)
				{					
					var pmDiv = document.createElement("div");
					var pm = new PropertyManager(pmDiv, dataArray[i]);
					propertyManagers.push(pm);
					propertyManagerDivs.push(pmDiv);
					
				}				
			
				refreshDataSources(cdRequest.response, false);	

			}
	};
        cdRequest.open("GET", "DataManagement?page=getDataSourceNames", true);
        cdRequest.send(null)



	this.dataTypeChanged = dataTypeChanged;
        function dataTypeChanged()
	{
                	var selectBox = document.getElementById("dataFactories");
                
                   	if (selectBox.selectedIndex > 0)
			{                    
	                    document.getElementById("dataSourceName").value = getUniqueName(selectBox.options[selectBox.selectedIndex].value);
			    cdiv5.style.display = "block";
                	}
			else
			{
	                    document.getElementById("dataSourceName").value = "";
			    cdiv5.style.display = "none";
			}

         }




        function createData() {



		var selectBox = document.getElementById("dataFactories");
		var factoryType = selectBox.options[selectBox.selectedIndex].value;
		var dataSourceName = document.getElementById("dataSourceName").value;

		document.getElementById("dataSourceName").value = "";
		cdiv5.style.display = "none";
                document.getElementById("dataFactories").selectedIndex = 0;

                //make request for data creation
		var cdRequest = getXMLHttpRequest();
		cdRequest.onreadystatechange = function()
		{
			if (cdRequest.readyState === 4 && cdRequest.status === 200)
			{
				
						
				var pmDiv = document.createElement("div");
				var pm = new PropertyManager(pmDiv, dataSourceName);
				propertyManagers.push(pm);
				propertyManagerDivs.push(pmDiv);

				refreshDataSources(cdRequest.response, true);	
			}
		};
        	cdRequest.open("GET", "DataManagement?page=createDataSource&dataFactoryType="+factoryType+"&dataSourceName="+dataSourceName, true);
        	cdRequest.send(null);
		

         }

        function deleteData(which) {

                //make request for data deletion
		var cdRequest = getXMLHttpRequest();
		cdRequest.onreadystatechange = function()
		{
			if (cdRequest.readyState === 4 && cdRequest.status === 200)
				refreshDataSources(cdRequest.response, false);	
		};
        	cdRequest.open("GET", "DataManagement?page=deleteDataSource&dataSourceName="+thisObject.dataSources[which], true);
        	cdRequest.send(null);
		createdDataSourceCount++;

         }



	function refreshDataSources(datalist, added)
	{
		if (datalist === ""){
			
        		dataSourcesElement.innerHTML = "No datasets available yet.";
			return;
		}    		

        	var dataArray = datalist.split(";");

		//check if it is the same as before
		var sameAsBefore = true;
		if (dataArray.length != thisObject.dataSources.length)
		{
			sameAsBefore = false;
		}
		else
			for (var i=0; i<dataArray.length; i++)
				if (dataArray[i] !== thisObject.dataSources[i])
				{
					sameAsBefore = false;
					break;
				}



		if (sameAsBefore) return;


		dataSourcesElement.innerHTML = "";
		thisObject.dataSources = dataArray;
		divs = [];
		deleteButtons = [];


        	for (var i = 0; i < dataArray.length; i++)
		{ 
         		

			var div = document.createElement('div');
			div.id ='dataSourceLine' + i;
			div.onmouseover = (function(i) { return function() {dataSourceHovered(i);};})(i); 
			div.onclick = (function(i) { return function() {dataSourceSelected(i);};})(i);			

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
        		label.innerHTML = dataArray[i];
			label.style.float = 'left';
			cellLeft.appendChild(label);
			row.appendChild(cellLeft);    		

            		var but = document.createElement('button');
			but.style.display = "none";
            		but.style.float = 'right';
            		but.innerHTML = 'Delete';
            		but.class = 'small button blue';	        		
            		but.onclick = (function(i) { return function() {deleteData(i);};})(i);
			deleteButtons.push(but);

            		cellRight.appendChild(but);
			row.appendChild(cellRight);

			tableBody.appendChild(row);
			table.appendChild(tableBody);

			div.appendChild(table);

			dataSourcesElement.appendChild(div); 

			
			
        	}

		if (added)
		{

			thisObject.selectedDataSourceIndex = thisObject.dataSources.length-1;
			dataSourceSelected(thisObject.dataSources.length-1);

		}

	}

	function dataSourceHovered(which)
	{
		for (var i=0; i<thisObject.dataSources.length; i++)
			if (thisObject.selectedDataSourceIndex === i)
				document.getElementById('dataSourceLine'+i).style.backgroundColor = "#88CCFF";
			else
				document.getElementById('dataSourceLine'+i).style.backgroundColor = 'transparent';

		if (thisObject.selectedDataSourceIndex === which)
			document.getElementById('dataSourceLine'+which).style.backgroundColor = "#66AACC";
		else
			document.getElementById('dataSourceLine'+which).style.backgroundColor = "#BBBBBB";
	}

	function dataSourceSelected(which)
	{

		for (var i=0; i<thisObject.dataSources.length; i++)
		{
			divs[i].style.backgroundColor = 'transparent';
			deleteButtons[i].style.display = 'none';
		}
		divs[which].style.backgroundColor = "#88CCFF";
		deleteButtons[which].style.display = 'block';
		thisObject.selectedDataSourceIndex = which;
		selectedDataSource = thisObject.dataSources[which];


		for (var i=0; i<divElement3.children.length; i++)
			divElement3.removeChild(divElement3.children[i]);
		divElement3.appendChild(propertyManagerDivs[which]);

	}

	function getUniqueName(dataType)
	{
		var cnt = 1000;
		while (true)
		{			
			var exists = false;
			for (var i=0; i<thisObject.dataSources.length; i++)
				if (thisObject.dataSources[i] === ("Data-"+dataType+cnt))
				{	
					exists = true;
					break;
				}
			if (exists || cnt == 0) return ("Data-"+dataType+(cnt+1));
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
		this.table.width = '600px';
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

  


}










