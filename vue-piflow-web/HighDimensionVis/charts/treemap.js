(function () {
  var tree = raw.models.tree();

  var chart = raw
    .chart()
    .title("矩形树图")
    .description(
      "矩形树图是分层数据的可视化，由一系列嵌套的矩形组成，这些矩形的大小与相应的数据值成比例。大矩形代表数据树的一个分支，然后细分为较小的矩形，代表该分支内每个节点的大小。"
    )
    .thumbnail("images/treemap.png")
    .category("层次结构（加权）型")
    .model(tree);

  var width = chart.number().title("宽度").defaultValue(1200).fitToWidth(true);

  var height = chart.number().title("高度").defaultValue(500);

  var padding = chart.number().title("填充").defaultValue(5);

  var colors = chart.color().title("色标");

  chart.draw(function (selection, data) {
    //unknown function
    var format = d3.format(",d");

    // get the drawing area
    var g = selection
      .attr("width", +width())
      .attr("height", +height())
      .append("g")
      .attr("transform", "translate(.5,.5)");

    // create the layout
    var layout = d3
      .treemap()
      .tile(d3.treemapResquarify)
      .padding(+padding())
      .size([+width(), +height()]);
    //.sticky(true)
    //.value(function(d) { return d.size; })

    // create the tree
    var root = d3.hierarchy(data).sum(function (d) {
      return +d.size;
    });

    // inform the layout
    layout(root);

    // define color scale
    colors.domain(
      root.descendants().filter(function (d) {
        return !d.children;
      }),
      function (d) {
        return d.data.color;
      }
    );

    // Create cells
    var cell = g
      .selectAll("g")
      .data(root.leaves())
      .enter()
      .append("g")
      .attr("class", "cell")
      .attr("transform", function (d) {
        return "translate(" + d.x0 + "," + d.y0 + ")";
      });

    cell
      .append("rect")
      .attr("width", function (d) {
        return d.x1 - d.x0;
      })
      .attr("height", function (d) {
        return d.y1 - d.y0;
      })
      .style("fill", function (d) {
        return colors()(d.data.color);
      })
      //.style("fill-opacity", function(d) { return d.children ? 0 : 1; })
      .style("stroke", "#fff");

    cell.append("title").text(function (d) {
      return d.name + ": " + format(d.size);
    });

    cell
      .append("text")
      .attr("x", function (d) {
        return (d.x1 - d.x0) / 2;
      })
      .attr("y", function (d) {
        return (d.y1 - d.y0) / 2;
      })
      .attr("dy", ".35em")
      .attr("text-anchor", "middle")
      .style("font-size", "11px")
      .style("font-family", "Arial, Helvetica")
      .text(function (d) {
        return d.data.label ? d.data.label.join(", ") : d.data.name;
      });
  });
})();
