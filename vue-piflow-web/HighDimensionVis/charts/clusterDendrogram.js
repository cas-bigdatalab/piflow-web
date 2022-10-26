(function () {
  var tree = raw.models.tree();

  tree.dimensions().remove("size");
  tree.dimensions().remove("color");
  tree.dimensions().remove("label");

  var chart = raw
    .chart()
    .title("树状图")
    .description("树状图是将树的叶节点放置在相同深度的树状图。")
    .thumbnail("images/dendrogram.png")
    .category("层次结构型")
    .model(tree);

  var width = chart.number().title("宽度").defaultValue(1200).fitToWidth(true);

  var height = chart.number().title("高度").defaultValue(500);

  function linkDiagonal(d) {
    return (
      "M" +
      d.y +
      "," +
      d.x +
      "C" +
      (d.parent.y + 100) +
      "," +
      d.x +
      " " +
      (d.parent.y + 100) +
      "," +
      d.parent.x +
      " " +
      d.parent.y +
      "," +
      d.parent.x
    );
  }

  chart.draw((selection, data) => {
    var g = selection
      .attr("width", +width())
      .attr("height", +height())
      .append("g")
      .attr("transform", "translate(40,0)");

    var cluster = d3.cluster().size([+height(), +width() - 160]);

    root = d3.hierarchy(data);

    cluster(root);

    var link = g
      .selectAll(".link")
      .data(root.descendants().slice(1))
      .enter()
      .append("path")
      .attr("class", "link")
      .style("fill", "none")
      .style("stroke", "#cccccc")
      .style("stroke-width", "1px")
      .attr("d", linkDiagonal);

    var node = g
      .selectAll(".node")
      .data(root.descendants())
      .enter()
      .append("g")
      .attr("class", "node")
      .attr("transform", (d) => {
        return `translate(${d.y}, ${d.x})`;
      });

    node
      .append("circle")
      .attr("r", 4.5)
      .style("fill", "#eeeeee")
      .style("stroke", "#999999")
      .style("stroke-width", "1px");

    node
      .append("text")
      .style("font-size", "11px")
      .style("font-family", "Arial, Helvetica")
      .attr("dx", (d) => {
        return d.children ? -8 : 8;
      })
      .attr("dy", 3)
      .style("text-anchor", (d) => {
        return d.children ? "end" : "start";
      })
      .text((d) => {
        return d.data.name;
      });
  });
})();
