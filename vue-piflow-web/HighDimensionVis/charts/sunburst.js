(function () {
  var tree = raw.models.tree();

  var chart = raw
    .chart()
    .title("旭日图")
    .description(
      "旭日图与树形图类似，不同之处在于它使用了放射状布局。树的根节点位于中心，每个弧的面积（或角度，取决于实现方式）对应于其值。"
    )
    .thumbnail("images/sunburst.png")
    .category("层次结构（加权）型")
    .model(tree);

  var diameter = chart
    .number()
    .title("直径")
    .defaultValue(600)
    .fitToWidth(true);

  var colors = chart.color().title("色标");

  chart.draw((selection, data) => {
    var radius = +diameter() / 2;

    var root = d3.hierarchy(data);
    root.sum((d) => {
      return d.size;
    });

    var layout = d3.partition();

    var x = d3.scaleLinear().range([0, 2 * Math.PI]);

    var y = d3.scaleSqrt().range([0, radius]);

    var arc = d3
      .arc()
      .startAngle((d) => {
        return Math.max(0, Math.min(2 * Math.PI, x(d.x0)));
      })
      .endAngle((d) => {
        return Math.max(0, Math.min(2 * Math.PI, x(d.x1)));
      })
      .innerRadius((d) => {
        return Math.max(0, y(d.y0));
      })
      .outerRadius((d) => {
        return Math.max(0, y(d.y1));
      });

    var format = d3.format(",d");

    var g = selection
      .attr("width", +diameter())
      .attr("height", +diameter())
      .append("g")
      .attr("transform", `translate(${+diameter() / 2}, ${+diameter() / 2})`);

    var nodes = layout(root).descendants();

    colors.domain(nodes, (d) => {
      return seek(d);
    });

    var slicesGroups = g
      .selectAll("g")
      .data(nodes)
      .enter()
      .append("g")
      .attr("display", (d) => {
        return d.depth ? null : "none";
      }); // hide inner ring

    slicesGroups
      .append("path")
      .attr("d", arc)
      .style("stroke", "#fff")
      .style("fill", (d) => {
        return colors()(seek(d));
      })
      .style("fill-rule", "evenodd");

    slicesGroups
      .append("text")
      .attr("transform", (d) => {
        var ang = ((x((d.x0 + d.x1) / 2) - Math.PI / 2) / Math.PI) * 180;
        d.textAngle = ang > 90 ? 180 + ang : ang;
        return "translate(" + arc.centroid(d) + ")rotate(" + d.textAngle + ")";
      })
      .attr("text-anchor", "middle")
      .attr("dx", "6")
      .attr("dy", ".35em")
      .style("font-size", "11px")
      .style("font-family", "Arial, Helvetica")
      .text((d) => {
        return d.data.label ? d.data.label.join(", ") : d.data.name;
      });

    slicesGroups.append("title").text((d) => {
      var size = d.data.size ? format(d.data.size) : "none";
      return d.data.name + ": " + size;
    });

    function seek(d) {
      if (d.children) return seek(d.children[0]);
      else return d.data.color;
    }
  });
})();
