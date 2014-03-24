function ViewerCanvas(div, window, viewerName, w, h)
{
	this.div = div;
	this.viewerName = viewerName;

	this.window = window;

	var thisObj = this;

	var imageTransf;

	var inmousemove = false;
	var prevTime = 0;
	var prevx = 0;
        var prevy = 0;
	var mousePos;
	var dragFlag = 0;	

	var width = w;
	var height = h;

	this.focus = false;

	var resizeInterval = -1;

	var cnt = 0;
	
	thisObj.div.onmousedown = handleMouseDown;
        thisObj.div.onmouseup = handleMouseUp;
        thisObj.div.onmousemove = handleMouseMove;        
        window.onkeydown = handleKeyDown;
        window.onkeyup = handleKeyUp;               
	thisObj.div.oncontextmenu = handleContextMenu;

        var imageTransf = new ImageTransferer(thisObj.div, this.viewerName, width, height, 2,2);

	thisObj.div.style.width = width + "px";
	thisObj.div.style.height = height + "px";

	this.divMoved = imageTransf.divMoved;
     
	this.resize = resize;
	function resize(w, h)
	{

		if (width == w && height == h)
			return;


		width = w;
		height = h;

		thisObj.div.style.width = w + "px";
		thisObj.div.style.height = h + "px";

		imageTransf.resize(w,h);
     
	}


	function handleMouseUp(event) {

                cnt++;
                event = event || thisObj.window.event;      

		var divPos = getPos(thisObj.div);

		var x = Math.round(event.clientX - divPos.x);
		var y = Math.round(event.clientY - divPos.y);

                if ((x) >= div.width || y >= div.height)         
                    return true; 

                dragFlag = 0;

		var mouseType = "left";
                if (event.which === 3)
                    mouseType = "right";               

                var theUrl = 'ViewerCanvas?page=mouseEvent&mb=' + mouseType + '&cmd=mouseUp&mx=' + x
                        + '&my=' + y + "&viewerName=" + thisObj.viewerName + "&r=" + cnt;
                sendCommand(theUrl);
                
       }





       function handleContextMenu(event) {
		return false;
       }




       function handleMouseDown(event) {

                cnt++;
                event = event || thisObj.window.event;      

		var divPos = getPos(thisObj.div);

		var x = Math.round(event.clientX - divPos.x);
		var y = Math.round(event.clientY - divPos.y);

                if (x >= div.width || y >= div.height)         
                    return true; 

                dragFlag = 1;

		var mouseType = "left";
                if (event.which === 3)
                    mouseType = "right"; 
           
                var theUrl = 'ViewerCanvas?page=mouseEvent&mb=' + mouseType + '&cmd=mouseDown&mx=' + x
                        + '&my=' + y + "&viewerName=" + thisObj.viewerName + "&r=" + cnt;
                sendCommand(theUrl);

                return false;
       }




       function handleMouseMove(event) {

                if (inmousemove)
                    return;
                inmousemove = true;


                cnt++;
                event = event || window.event; // IE-ism            

		var divPos = getPos(thisObj.div);

		var x = Math.round(event.clientX - divPos.x);
		var y = Math.round(event.clientY - divPos.y);

                if (x >= div.width || y >= div.height)  
		{      
			inmousemove = false; 
                   	 return true; 
		}

		var ct = (new Date()).getTime();

                if ( ct - prevTime < 50)
                {
                    inmousemove = false;
                    return;
                }

                prevx = x;
                prevy = y;
                prevtime = (new Date()).getTime();

		var mouseType = "left";
                if (event.which === 3)
                    mouseType = "right"; 

                
		var theUrl = "";
                if (dragFlag === 1) {
                    
                     theUrl = 'ViewerCanvas?page=mouseEvent&mb=' + mouseType + '&cmd=mouseDragged&mx=' + x
                        + '&my=' + y + "&viewerName=" + thisObj.viewerName + "&r=" + cnt;
                    sendCommand(theUrl);
                }
                else { //normal mouse move
                   
                     theUrl = 'ViewerCanvas?page=mouseEvent&mb=' + mouseType + '&cmd=mouseMoved&mx=' + x
                        + '&my=' + y + "&viewerName=" + thisObj.viewerName + "&r=" + cnt;
                    sendCommand(theUrl);
                }

                inmousemove = false;
       }



            
       function handleKeyDown(event){

		if (!thisObj.focus)
			return;

                event = event || window.event; // IE-ism
                
                var keyCode = event.keyCode; 
		var meta = ""+ event.shiftKey + "," + event.ctrlKey + "," + event.altKey;
      
                 
                theUrl = 'ViewerCanvas?page=keyEvent&keyAction=keyDown&keyCode=' 
                                + keyCode + "&keyModifiers=" + meta + "&viewerName=" + thisObj.viewerName + "&r=" + cnt;
                           
                sendCommand(theUrl);
        }



            
       function handleKeyUp(event){

		if (!thisObj.focus)
			return;

                event = event || window.event; // IE-ism
                
                var keyCode = event.keyCode;
		var meta = ""+ event.shiftKey + "," + event.ctrlKey + "," + event.altKey;
                
               var theUrl = 'ViewerCanvas?page=keyEvent&keyAction=keyUp&keyCode=' 
                                + keyCode + "&keyModifiers=" + meta + "&viewerName=" + thisObj.viewerName + "&r=" + cnt;
                           
               sendCommand(theUrl);
        }
}










