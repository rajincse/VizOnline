

function computeForceLayout(graph, canvas, canvasWidth,canvasHeight)
{


var color = d3.scale.category20();

var force = d3.layout.force()
    .charge(-120)
    .linkDistance(30)
    .size([canvasWidth, canvasHeight]);

var svg = d3.select(canvas).append("svg")
    .attr("width", canvasWidth)
    .attr("height", canvasHeight);


  force
      .nodes(graph.nodes)
      .links(graph.links)
      .start();
layout = force;
  var link = svg.selectAll(".link")
      .data(graph.links)
    .enter().append("line")
      .attr("class", "link")
      .style("stroke-width","1px");

  var node = svg.selectAll(".node")
      .data(graph.nodes)
    .enter().append("circle")
      .attr("class", "node")
      .attr("r", 5)
      .style("fill", function(d) { return color(0); })
      .call(force.drag);
//
  node.append("title")
      .text(function(d) { return d.name; });

  force.on("tick", function() {
    link.attr("x1", function(d) {
                        return d.source.x; 
                    })
        .attr("y1", function(d) { return d.source.y; })
        .attr("x2", function(d) { return d.target.x; })
        .attr("y2", function(d) { return d.target.y; });

    node.attr("cx", function(d) { return d.x; })
        .attr("cy", function(d) { return d.y; });
  });

}





