function ImageTransferer(div, w,h, tch, tcv)
{
	this.div = div;
	this.tch = tch;
	this.tcv = tcv;
	this.w = w;
	this.h = h;
	this.imageCounter = 29;

	this.tw = w/tch;
	this.th = h/tcv;

	this.images = [];

	this.resize = resize;

	this.needsResize = false;

	this.cnt = 0;
	this.loaded = 0;

	function resize(w,h)
	{
		this.w = w;
		this.h = h;
		this.needsResize = true;
	}


	this.imageUpdate = imageUpdate;
        function imageUpdate() {


		this.cnt++;

		if (this.needsResize)
		{
			this.tw = this.w/this.tch;
			this.th = this.h/this.tcv;
			this.needsResize = false
			this.imageCounter = 29;
		}


		this.imageCounter = this.imageCounter+1;

		var dif = 1;
		if (this.imageCounter == 30)
			dif = 0;
           
		this.images = [];
		this.loaded = 0;
		for (var i=0; i<this.tcv; i++)
		{
			var rowImages = [];
			for (var j=0; j<tch; j++)
			{
				var url = "VizOnlineServlet?page=viewer&tileX="+j+"&tileY="+i+"&diff="+dif+"&r=" + this.cnt;
				var image = new Image();
				rowImages.push(image);
				image.src = url;
				console.log(url);				

				var imtr = this;


                  		  image.onload = function() {
                     		  	imtr.loaded++;
                      		 	imtr.fimloaded();
                   		  };
			}
			this.images.push(rowImages);	
		} 
       }


	this.fimloaded = fimloaded;
	function fimloaded() {
console.log(this.loaded + " " + tch*tcv);
		if (this.loaded == tch*tcv)
		{

			if (this.imageCounter == 30)
			{
				this.imageCounter = 0;
				var last;
    				while (last = this.div.lastChild) this.div.removeChild(last);
			}

			for (var i=0; i<this.images.length; i++)
				for (var j=0; j<this.images[i].length; j++)
				{
					this.div.appendChild(this.images[i][j]);
                        		this.images[i][j].style.width = this.tw + "px";
                        		this.images[i][j].style.height = this.th + "px";
                        		this.images[i][j].style.position = "absolute";
                        		this.images[i][j].style.top = i*this.th + "px";
                        		this.images[i][j].style.left = j*this.tw + "px";
				}

                       
			this.images = [];
console.log("next");
			this.imageUpdate();
			
                    }
                }

}



