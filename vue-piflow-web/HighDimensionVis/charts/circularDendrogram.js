(function () {
  var tree = raw.models.tree();

  tree.dimensions().remove("size");
  tree.dimensions().remove("color");
  tree.dimensions().remove("label");

  var chart = raw
    .chart()
    .title("圆形树状图")
    .description(
      "圆形树状图是用来表示层次聚类分布的一种图形，每个节点表示的不同不同深度级别在水平轴上的可视化。"
    )
    .thumbnail("images/circularDendrogram.png")
    .category("层次结构型")
    .model(tree);

  var diameter = chart
    .number()
    .title("半径")
    .defaultValue(1200)
    .fitToWidth(true);

  function linkDiagonal(d) {
    return (
      "M" +
      project(d.x, d.y) +
      "C" +
      project(d.x, (d.y + d.parent.y) / 2) +
      " " +
      project(d.parent.x, (d.y + d.parent.y) / 2) +
      " " +
      project(d.parent.x, d.parent.y)
    );
  }

  function project(x, y) {
    var angle = ((x - 90) / 180) * Math.PI,
      radius = y;
    return [radius * Math.cos(angle), radius * Math.sin(angle)];
  }

  chart.draw((selection, data) => {
    var g = selection
      .attr("width", +diameter())
      .attr("height", +diameter())
      .append("g")
      .attr("transform", `translate(${diameter() / 2}, ${diameter() / 2})`);

    var cluster = d3.cluster().size([360, diameter() / 2 - 120]);

    root = d3.hierarchy(data);

    cluster(root);

    var link = g
      .selectAll("path.link")
      .data(root.descendants().slice(1))
      .enter()
      .append("path")
      .attr("class", "link")
      .style("fill", "none")
      .style("stroke", "#cccccc")
      .style("stroke-width", "1px")
      .attr("d", linkDiagonal);

    var node = g
      .selectAll("g.node")
      .data(root.descendants())
      .enter()
      .append("g")
      .attr("class", "node")
      .attr("transform", (d) => {
        return `rotate(${d.x - 90})translate(${d.y})`;
      });

    node
      .append("circle")
      .attr("r", 4.5)
      .style("fill", "#eeeeee")
      .style("stroke", "#999999")
      .style("stroke-width", "1px");

    node
      .append("text")
      .attr("dy", ".31em")
      .attr("text-anchor", (d) => {
        return d.x < 180 ? "start" : "end";
      })
      .attr("transform", (d) => {
        return d.x < 180 ? "translate(8)" : "rotate(180)translate(-8)";
      })
      .text((d) => {
        return d.data.name;
      })
      .style("font-size", "11px")
      .style("font-family", "Arial, Helvetica");
  });
})();
