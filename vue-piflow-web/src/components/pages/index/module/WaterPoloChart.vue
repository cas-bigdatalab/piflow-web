<template>
  <div class="chart" ref="chart"></div>
</template>

<script>
import "echarts-liquidfill/src/liquidFill.js"; //在这里引入
export default {
  name: "chart",
  data: () => ({
    ele: null,
    content: null,
    myEcharts: null
  }),

  // 使用 props 传参
  //   props: ["option", "theme"],
  //   watch: {
  //     option(obj) {
  //       this.setChart(obj, this.theme, this.loading);
  //     },
  //     theme(obj) {
  //       this.setChart(this.option, obj, this.loading);
  //     }
  //   },

  mounted() {
    this.$nextTick(() => {
      this.initChart();
    });
  },

  methods: {
    // 使用 function 传参
    initChart() {
      //防止错误阻断，进行参数校验
      // let theme = theme ? theme : {};

      //   this.ele = ;

      // 初始化
      let echarts = this.$echarts;
      this.myEcharts = echarts.init(this.$refs.chart);

      //显示加载动画（ 根据需求自行添加 ）
      // this.myEcharts.showLoading({
      //   text: '加载中...',
      //   color: '#c23531',
      //   textColor: '#fff',
      //   maskColor: 'transparent',
      // });

      //数值大小
      let max = 500; //满刻度大小
      let arr = 0.6;
      let data = max * arr;
      let option = {
        // backgroundColor: "#000",
        // title: {
        //   top: "47%",
        //   left: "center",
        //   text: ((arr * 10000) / 100).toFixed(2) + "%",
        //   textStyle: {
        //     color: "#fff",
        //     fontStyle: "normal",
        //     fontWeight: "normal",
        //     fontSize: 38
        //   }
        // },
        series: [
          {
            type: "liquidFill",
            itemStyle: {
              normal: {
                opacity: 0.4,
                shadowBlur: 0,
                color: "#3B7BF8",
                shadowColor: "#3B7BF8"
              }
            },
            data: [
              {
                value: 0.6,
                itemStyle: {
                  normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                      {
                        offset: 0,
                        color: "#4A87FF"
                      },
                      {
                        offset: 1,
                        color: "#3B7BF8"
                      }
                    ]),
                    opacity: 1
                  }
                }
              },
              {
                value: 0.6,
                itemStyle: {
                  normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                      {
                        offset: 0,
                        color: "#4A87FF"
                      },
                      {
                        offset: 1,
                        color: "#3B7BF8"
                      }
                    ]),
                    opacity: 0.5
                  }
                }
              },
              {
                value: 0.6,
                itemStyle: {
                  normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                      {
                        offset: 0,
                        color: "#4A87FF"
                      },
                      {
                        offset: 1,
                        color: "#3B7BF8"
                      }
                    ]),
                    opacity: 0.3
                  }
                }
              }
            ],
            background: "#000",
            center: ["50%", "50%"],
            radius: "65%",
            outline: {
              itemStyle: {
                borderColor: "#3F1EB9",
                borderWidth: 8
              },
              borderDistance: 3
            }
          },
          {
            color: [
              new echarts.graphic.LinearGradient(0, 1, 0, 0, [
                {
                  offset: 0,
                  color: "#5A48E6"
                },
                {
                  offset: 1,
                  color:  "#fff"
                }
              ]),
              "transparent"
            ],
            type: "pie",
            center: ["50%", "50%"],
            radius: ["70%", "71%"],
            // hoverAnimation: false,
            data: [
              {
                name: "",
                value: data,
                label: {
                  show: false,
                  position: "center",
                  color: "#fff",
                  fontSize: 38,
                  fontWeight: "bold",
                  formatter: function(o) {
                    return data;
                  }
                }
              },
            
              {
                //画剩余的刻度圆环
                name: "",
                value: max - data,
                label: {
                  show: false
                },
                labelLine: {
                  show: false
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
