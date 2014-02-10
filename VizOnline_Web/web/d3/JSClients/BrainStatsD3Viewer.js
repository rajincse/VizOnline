/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function JSD3Client()
{ 
};

JSD3Client.prototype.dataUpdated = function (data, canvas, canvasWidth, canvasHeight)
{
    if(data.DataArray)
    {
        load(data, canvas, canvasWidth, canvasHeight);
    }
}
 
function load(data, canvas, canvasWidth, canvasHeight)
{
    $(canvas).empty();
    var margin = {top: 20, right: 20, bottom: 30, left: 40},
    width = canvasWidth-300 - margin.left - margin.right,
    height = canvasHeight-300 - margin.top - margin.bottom;
    x = d3.scale.ordinal()
            .rangeRoundBands([0, width], .1);
    
    y = d3.scale.linear()
    .range([height, 0]);

    var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom");

    var yAxis = d3.svg.axis()
    .scale(y)
    .orient("left");
    
     var svg = d3.select(canvas).append("svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
            
    x.domain(data.DataArray.map(function(d) { return d.Key; }));
    y.domain([0, d3.max(data.DataArray, function(d) { return d.Value; })]);

    svg.append("g")
    .attr("class", "x axis")
    .attr("transform", "translate(0," + height + ")")
    .call(xAxis);

    svg.append("g")
    .attr("class", "y axis")
    .call(yAxis)
    .append("text")
    .attr("transform", "rotate(-90)")
    .attr("y", 6)
    .attr("dy", ".71em")
    .style("text-anchor", "end")
    .text("Frequency");

    svg.selectAll(".bar")
    .data(data.DataArray)
    .enter().append("rect")
    .attr("class", "bar")
    .attr("x", function(d) { return x(d.Key); })
    .attr("width", x.rangeBand())
    .attr("y", function(d) { return y(d.Value); })
    .attr("height", function(d) { return height - y(d.Value); });
}
