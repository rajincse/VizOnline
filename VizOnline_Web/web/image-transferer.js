function ImageTransferer(div, w,h, tch, tcv)
{
	this.div = div;
	this.tch = tch;
	this.tcv = tcv;
	this.w = w;
	this.h = h;	

	this.tw = w/tch;
	this.th = h/tcv;

	this.images = null;

	
	this.needsResize = false;

	this.cnt = 0;
	this.loaded = 0;	



	this.imageUpdate = imageUpdate;
        function imageUpdate() {


		if (this.images != null)
			return;

		

		this.images = [];
		this.cnt++;
		this.loaded = 0;
		for (var i=0; i<this.tcv; i++)
		{
			var rowImages = [];
			for (var j=0; j<tch; j++)
			{
				var url = "VizOnlineServlet?page=viewer&tileX="+j+"&tileY="+i+"&diff=1&r=" + this.cnt;
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
			if (this.images[0][0].width == 1)
			{
				this.images = null;
				return;
			}

			else if (this.images[0][0].width != 500 )
			{
				var last;
    				while (last = this.div.lastChild) this.div.removeChild(last);
				//alert(this.images[0][0].width);
			}


			for (var i=0; i<this.images.length; i++)
				for (var j=0; j<this.images[i].length; j++)
				{
					console.log(this.images[i][j].width);
					this.div.appendChild(this.images[i][j]);
                        		this.images[i][j].style.width = this.tw + "px";
                        		this.images[i][j].style.height = this.th + "px";
                        		this.images[i][j].style.position = "absolute";
                        		this.images[i][j].style.top = i*this.th + "px";
                        		this.images[i][j].style.left = j*this.tw + "px";
				}

                       
			this.images = null;			
                    }
                }


	

}



