<template>
  <div class="chart" ref="chart"></div>
</template>

<script>
import "echarts-liquidfill/src/liquidFill.js"; //在这里引入
export default {
  name: "chart",
  data: () => ({
    content: null,
    myEcharts: null
  }),
  mounted() {
    this.$nextTick(() => {
      this.initChart();
    });
  },

  methods: {
    // 使用 function 传参
    initChart() {
      let echarts = this.$echarts;
      this.myEcharts = echarts.init(this.$refs.chart);

      let option = {
        tooltip: {
          trigger: "item",
          formatter: function(params) {
            return ` ${params.name}：${params.value}% `
          }
        },
        series: [
          {
            // name: "业务警种",
            type: "pie",
            radius: "60%",
            label: {
              show: false
            },

            color: ["#8378ea", "#e7bcf3"],
            data: [
              { value: 30, name: "已使用" },
              { value: 70, name: "未使用" }
            ]
          },
          {
            type: "pie",
            radius: ["65%", "68%"],

            label: {
              show: true,
              position: "outside",
              // formatter: "{a|{b}：{d}%}\n{hr|}",
              rich: {
                hr: {
                  backgroundColor: "t",
                  borderRadius: 3,
                  width: 3,
                  height: 3,
                  padding: [3, 3, 0, -12]
                },
                a: {
                  padding: [-30, 15, -20, 15]
                }
              }
            },
            labelLine: {
              normal: {
                length: 20,
                length2: 30,
                lineStyle: {
                  width: 1
                }
              }
            },
            center: ["50%", "50%"],
            data: [
              {
                value: 30,
                name: "已使用",
                itemStyle: {
                  normal: {
                    color: "#a181fc",
                    shadowColor: "#a181fc"
                  }
                }
              },
              {
                value: 100 - 30,
                name: "未使用",
                itemStyle: {
                  normal: {
                    color: "#e3d9fe"
                  }
                }
              }
            ]
          }
        ]
      };
      if (option) {
        // 将 option 注册到 echarts 中
        this.myEcharts.setOption(option, true);

        // 添加点击事件（ 根据需求自行添加 ）
        this.myEcharts.off("click");
        this.myEcharts.on("click", param => {
          //根据需求做逻辑处理
          console.log(param);
        });

        //监听窗口变化 自适应（ 根据需求自行添加 ）
        window.addEventListener("resize", () => {
          this.myEcharts.resize();
        });

        //隐藏动画效果（ 根据需求自行添加 ）
        this.myEcharts.hideLoading();
      }
    }
  },

  beforeDestroy() {
    if (this.myEcharts) {
      this.myEcharts.clear();
    }
  }
};
</script>

<style lang="scss" scoped>
.chart {
  height: 100%;
  width: 100%;
  // margin: 0px 15px;
}
</style>
