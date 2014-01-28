var canvas , canvasWidth, canvasHeight;
function DataTransfer(div,w,h)
{
    canvas = div;
    canvasWidth = w;
    canvasHeight= h;
   
};

DataTransfer.prototype.readCreatorType = function (callback)
{
    var url = 'VizOnlineServlet?page=d3viewer&method=readCreatorType';
    $.get(url, callback);
};
    
DataTransfer.prototype.readViewerData = function (callbackMethod)
{
    var url = 'VizOnlineServlet?page=d3viewer&method=readViewerData';
    $.get(url, function(data) {

        callbackMethod(data, canvas,canvasWidth,canvasHeight);
    });
};







