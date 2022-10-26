(function () {
  var model = raw.model();

  var list = model
    .dimension()
    .title("平行轴")
    .multiple(true)
    .types(Number)
    .required(2);

  var color = model.dimension().title("颜色");

  model.map((data) => {
    if (!list()) return;
    return data.map((d) => {
      var obj = {
        dimensions: {},
        color: color(d),
      };
      list().forEach((l) => {
        obj.dimensions[l] = d[l];
      });
      return obj;
    });
  });

  var chart = raw
    .chart()
    .title("平行坐标图")
    .description(
      "平行坐标图是高维几何图形可视化和多维数据分析的常用方法，为了显示n维空间中的一组点，绘制一个由n条平行线组成的背景，这些平行线通常是垂直的，且间隔相等。将n维空间中的点表示为顶点在平行轴上的折线，顶点在第i轴上的位置对应于该点的第i坐标。"
    )
    .thumbnail("images/parallelCoordinates.png")
    .category("多变量型")
    .model(model);

  var width = chart.number().title("宽度").defaultValue(1200).fitToWidth(true);

  var height = chart.number().title("高度").defaultValue(500);

  var colors = chart.color().title("色标");

  chart.draw((selection, data) => {
    var m = [30, 40, 10, 40],
      w = +width() - m[1] - m[3],
      h = +height() - m[0] - m[2];

    var x = d3.scalePoint().range([0, w]),
      y = {},
      dragging = {};

    var line = d3.line(),
      background,
      foreground;

    var svg = selection
      .attr("width", +width())
      .attr("height", +height())
      .append("g")
      .attr("width", w + m[1] + m[3])
      .attr("height", h + m[0] + m[2])
      .style("font-size", "10px")
      .style("font-family", "Arial, Helvetica")
      .append("g")
      .attr("transform", `translate(${m[3]}, ${m[0]})`);

    x.domain(
      (dimensions = d3.keys(data[0].dimensions).filter((d) => {
        return (
          d != "name" &&
          (y[d] = d3
            .scaleLinear()
            .domain(
              d3.extent(data, (p) => {
                return +p.dimensions[d];
              })
            )
            .range([h, 0]))
        );
      }))
    );

    colors.domain(data, (d) => {
      return d.color;
    });

    background = svg
      .append("g")
      .attr("class", "background")
      .selectAll("path")
      .data(data)
      .enter()
      .append("path")
      .style("fill", "none")
      .style("stroke", (d) => {
        return colors()(d.color);
      })
      .style("stroke-opacity", ".4")
      .attr("d", path);

    var g = svg
      .selectAll(".dim")
      .data(dimensions)
      .enter()
      .append("g")
      .attr("class", "dim")
      .attr("transform", (d) => {
        return `translate(${x(d)})`;
      });

    g.append("g")
      .attr("class", "axis")
      .each(function (d) {
        d3.select(this).call(d3.axisLeft(y[d]));
      })
      .append("text")
      .attr("text-anchor", "middle")
      .style("font-size", "10px")
      .style("font-family", "Arial, Helvetica")
      .attr("y", -9)
      .text(String);

    d3.selectAll("text")
      .style("font-size", "10px")
      .style("font-family", "Arial, Helvetica");

    d3.selectAll(".axis line, .axis path")
      .style("fill", "none")
      .style("stroke", "#000")
      .style("stroke-width", "1px")
      .style("shape-rendering", "crispEdges");

    function position(d) {
      var v = dragging[d];
      return v == null ? x(d) : v;
    }

    function path(d) {
      return line(
        dimensions.map((p) => {
          return [position(p), y[p](d.dimensions[p])];
        })
      );
    }
  });
})();
