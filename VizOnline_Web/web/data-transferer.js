var graph = {
         nodes:[],links:[]
    };
var nodeNames=[];
var nodeDegree={};

var canvas , canvasWidth, canvasHeight;
var callbackMethod;
function DataTransfer(div,w,h, callback)
{
    canvas = div;
    canvasWidth = w;
    canvasHeight= h;
    callbackMethod = callback
    
    readData();
   
}
 function readData()
{
    var url = 'VizOnlineServlet?page=d3viewer&method=readData';
    $.get(url, function(data) {
        
        callbackMethod(data, canvas, canvasWidth, canvasHeight);
    });
}






