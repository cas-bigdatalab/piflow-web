(function () {
  var tree = raw.models.tree();

  var chart = raw
    .chart()
    .title("圆堆图")
    .description(
      "圆堆图可表示层次结构并比较值，可以显示元素在其区域中的比例以及在层次结构中的位置。"
    )
    .thumbnail("images/circlePacking.png")
    .category("层次结构（加权）型")
    .model(tree);

  var diameter = chart
    .number()
    .title("直径")
    .defaultValue(1200)
    .fitToWidth(true);

  var padding = chart.number().title("填充").defaultValue(5);

  var sort = chart.checkbox().title("按大小排序").defaultValue(false);

  var colors = chart.color().title("色标");

  var showLabels = chart.checkbox().title("显示标签").defaultValue(true);

  chart.draw(function (selection, data) {
    if (!data.children.length) return;

    var margin = 10,
      outerDiameter = +diameter(),
      innerDiameter = outerDiameter - margin - margin;

    var x = d3.scaleLinear().range([0, innerDiameter]);

    var y = d3.scaleLinear().range([0, innerDiameter]);

    var pack = d3
      .pack()
      .padding(+padding())
      .size([innerDiameter, innerDiameter]);

    //compute the hierarchy
    var hierarchy = d3.hierarchy(data).sum(function (d) {
      return +d.size;
    });
    var nodes = hierarchy
      .sort(function (a, b) {
        return sort() ? b.value - a.value : null;
      })
      .descendants();
    pack(hierarchy);

    var g = selection
      .attr("width", outerDiameter)
      .attr("height", outerDiameter)
      .append("g")
      .attr("transform", "translate(" + margin + "," + margin + ")");

    colors.domain(
      nodes.filter(function (d) {
        return !d.children;
      }),
      function (d) {
        return d.data.color;
      }
    );

    g.append("g")
      .selectAll("circle")
      .data(nodes)
      .enter()
      .append("circle")
      .attr("class", function (d) {
        return d.parent
          ? d.children
            ? "node"
            : "node node--leaf"
          : "node node--root";
      })
      .attr("transform", function (d) {
        return "translate(" + d.x + "," + d.y + ")";
      })
      .attr("r", function (d) {
        return d.r;
      })
      .style("fill", function (d) {
        return !d.children ? colors()(d.data.color) : "";
      })
      .style("fill-opacity", function (d) {
        return !d.children ? 1 : 0;
      })
      .style("stroke", "#ddd")
      .style("stroke-opacity", function (d) {
        return !d.children ? 0 : 1;
      });

    g.append("g")
      .selectAll("text")
      .data(
        nodes.filter(function (d) {
          return showLabels();
        })
      )
      .enter()
      .append("text")
      .attr("text-anchor", "middle")
      .style("font-size", "11px")
      .style("font-family", "Arial, Helvetica")
      .attr("transform", function (d) {
        return "translate(" + d.x + "," + d.y + ")";
      })
      .text(function (d) {
        return d.data.label ? d.data.label.join(", ") : d.data.name;
      });
  });
})();
