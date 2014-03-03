function ImageTransferer(div, viewerName, w, h, tch, tcv)
{
	this.div = div;
	this.viewerName = viewerName;

	var tch = tch;
	var tcv = tcv;
	var width = w;
	var height = h;
	var tw = w / tch;
	var th = h / tcv;

	this.images = null;

	this.cnt = 0;
	this.loaded = 0;

	var thisObj = this;

	var desiredWidth = w;
	var desiredHeight = h;

	var resizing = true;


	var url = "ViewerCanvas?page=resize&viewerName=" + viewerName
                        + "&width=" + width + "&height=" + height;




	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function()
	{
		if (xmlhttp.readyState === 4 && xmlhttp.status === 200)
		{
			resizing = false;
			setInterval(thisObj.imageUpdate, 100);
		}
	}
	xmlhttp.open("GET", url, true);
	xmlhttp.send(null);
 

	this.resize = resize;
	function resize(w,h)
	{
		//has to divide by tch,tcv
		while (w % tch != 0) w--;
		while (w % tcv != 0) h--;

		desiredWidth = w;
		desiredHeight = h;
	}


	function actualResize(w,h)
	{
		resizing = true;

                var url = "ViewerCanvas?page=resize&viewerName=" + thisObj.viewerName
                        + "&width=" + w + "&height=" + h;


                var xmlhttp = new XMLHttpRequest();
		xmlhttp.onreadystatechange = function()
		{
			if (xmlhttp.readyState === 4 && xmlhttp.status === 200)
			{
				width = w;
				height = h;
				tw = w / tch;
				th = h / tcv;
				resizing = false;				
			}
		}
                xmlhttp.open("GET", url, true);
                xmlhttp.send(null);
	}



    this.imageUpdate = imageUpdate;
    function imageUpdate() {

	if (resizing)
		return;

        if (thisObj.images !== null)
            return;
      

        if (thisObj.viewerName === "") {
            return;
        }

	if (desiredWidth != width || desiredHeight != height)
	{
		actualResize(desiredWidth, desiredHeight);
		return;
	}

        thisObj.images = [];
        thisObj.cnt++;
        thisObj.loaded = 0;

        for (var i = 0; i < tcv; i++)
        {
            var rowImages = [];

            for (var j = 0; j < tch; j++)
            {
                var url = "ViewerCanvas?page=imageUpdate&viewerName=" + thisObj.viewerName
                        + "&tileX=" + j + "&tileY=" + i + "&diff=1&r=" + thisObj.cnt + ((new Date()).getTime());

                var image = new Image();
                rowImages.push(image);

                var imtr = thisObj;

                image.onload = function() {
                    imtr.loaded++;
                    imtr.fimloaded();

                };

                image.src = url;
                //console.log(url);
            }
            thisObj.images.push(rowImages);
        }
    }

	


    this.fimloaded = fimloaded;
    function fimloaded() {

        if (this.loaded === tch * tcv)
        {



            if (this.images[0][0].width === 1)
            {
                this.images = null;
                return;
            }
            else if (this.images[0][0].width !== tw)  //this.tw is the width of a tile, it used to be 500 which was half of a thousand
            {
                var last;
                while (last = this.div.lastChild)
                    this.div.removeChild(last);

		
            }

	var divPos = getPos(this.div);	
	divPos.y = 0;


      
            for (var i = 0; i < this.images.length; i++)
                for (var j = 0; j < this.images[i].length; j++)
                {

                    //console.log(this.images[i][j].width);
                    this.div.appendChild(this.images[i][j]);

			this.images[i][j].style.position = "absolute";

			if (this.images[i][j].width < tw)
			{
                    		this.images[i][j].style.width = tw + "px";
                    		this.images[i][j].style.height = th + "px";
                   		this.images[i][j].style.top = i * th + divPos.y + "px";
                   		this.images[i][j].style.left = j * tw + divPos.x + "px";
			}
			else
			{
				this.images[i][j].style.top = i * this.images[i][j].height + divPos.y + "px";
                   		this.images[i][j].style.left = j * this.images[i][j].width + divPos.x + "px";
			}
                    

                }

	this.images = null;
        }
    }


	function getPos(el) {
    
    		for (var lx=0, ly=0;
        		 el != null;
         		lx += el.offsetLeft, ly += el.offsetTop, el = el.offsetParent);
    		return {x: lx,y: ly};
	}




}



