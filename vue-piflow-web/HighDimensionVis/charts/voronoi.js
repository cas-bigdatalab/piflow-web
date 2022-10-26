(function () {
  var points = raw.models.points();

  points.dimensions().remove("size");
  points.dimensions().remove("label");

  var chart = raw
    .chart()
    .title("韦恩图")
    .description(
      "它在由两个变量定义的每个点周围创建最小面积。 当应用于散点图时，显示点之间的距离非常有用。"
    )
    .thumbnail("images/voronoi.png")
    .category("离散型")
    .model(points);

  var width = chart.number().title("宽度").defaultValue(1200).fitToWidth(true);

  var height = chart.number().title("高度").defaultValue(500);

  var colors = chart.color().title("色标");

  var showPoints = chart.checkbox().title("显示点").defaultValue(true);

  chart.draw(function (selection, data) {
    var x = d3
        .scaleLinear()
        .range([0, +width()])
        .domain(
          d3.extent(data, function (d) {
            return d.x;
          })
        ),
      y = d3
        .scaleLinear()
        .range([+height(), 0])
        .domain(
          d3.extent(data, function (d) {
            return d.y;
          })
        );

    var voronoi = d3
      .voronoi()
      .x(function (d) {
        return x(d.x);
      })
      .y(function (d) {
        return y(d.y);
      })
      .extent([
        [0, 0],
        [+width(), +height()],
      ]);

    var g = selection
      .attr("width", +width())
      .attr("height", +height())
      .append("g");

    colors.domain(data, function (d) {
      return d.color;
    });

    var path = g
      .selectAll("path")
      .data(voronoi.polygons(data))
      .enter()
      .append("path")
      .style("fill", function (d) {
        return d && colors() ? colors()(d.data.color) : "#dddddd";
      })
      .style("stroke", "#fff")
      .attr("d", polygon);

    path.order();

    g.selectAll("circle")
      .data(
        data.filter(function () {
          return showPoints();
        })
      )
      .enter()
      .append("circle")
      .style("fill", "#000000")
      .style("pointer-events", "none")
      .attr("transform", function (d) {
        return "translate(" + x(d.x) + ", " + y(d.y) + ")";
      })
      .attr("r", 1.5);

    function polygon(d) {
      if (!d) return;
      return "M" + d.join("L") + "Z";
    }
  });
})();
