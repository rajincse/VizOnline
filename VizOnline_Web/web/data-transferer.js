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
    var url = 'ViewerManagement?page=d3viewer&method=readCreatorType';
    url += "&viewerName=" + window.viewerName;
    $.get(url, callback);
};

DataTransfer.prototype.readViewerData = function(callbackMethod, isInitialCall)
{

    var url = 'ViewerManagement?page=d3viewer&method=readViewerData&isinitcall='+isInitialCall;
    url += "&viewerName=" + window.viewerName;

    $.get(url, function(data) {

        callbackMethod(data, canvas, canvasWidth, canvasHeight);
    });

};







