var canvas, canvasWidth, canvasHeight;
function DataTransfer(div, w, h)
{
    canvas = div;
    canvasWidth = w;
    canvasHeight = h;

}
;

DataTransfer.prototype.readCreatorType = function(callback)
{
    var viewerName;
    var test = function() {//recurring function to ensure the viewer is set
        viewerName = document.getElementById("viewerName").value;
        if (viewerName !== "") {

            var url = 'VizOnlineServlet?page=d3viewer&method=readCreatorType';
            url += "&viewerName=" + document.getElementById("viewerName").value;
            $.get(url, callback);

            clearInterval(id);
        }
    };
    var id = setInterval(test, 100);
};

DataTransfer.prototype.readViewerData = function(callbackMethod)
{

    //make sure the viewer name is set
    var viewerName;
    var test = function() {//recurring function to ensure the viewer is set
        viewerName = document.getElementById("viewerName").value;
        if (viewerName !== "") {

            var url = 'VizOnlineServlet?page=d3viewer&method=readViewerData';
            url += "&viewerName=" + document.getElementById("viewerName").value;

            $.get(url, function(data) {

                callbackMethod(data, canvas, canvasWidth, canvasHeight);
            });


            clearInterval(id);
        }
    };
    var id = setInterval(test, 100);
};







