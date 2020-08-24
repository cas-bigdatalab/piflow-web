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

  props:['contentData'],
  watch: {
    contentData: {
      handler(newVal){
        this.content = newVal;
        this.initChart();
      },
      deep:true,
    },
  },
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

      let RVCores = {}, TVC = {}, AVC = {};
      for (let key in this.content){
        if (key === 'remainingCapacityGB'){
          RVCores.value = this.content[key];
          RVCores.name = key;
        }
        if (key === 'TotalCapacityGB'){
          TVC.value = this.content[key];
          TVC.name = key;
        }
        if (key === 'allocatedCapacityGB'){
          AVC.value = this.content[key];
          AVC.Val = this.content[key];
          AVC.name = key;
          AVC.value = AVC.value / TVC.value;
        }
      }

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
        tooltip: {
          trigger: "item",
          formatter: function(params) {
            if (params.name !== 'allocatedCapacityGB'){
              return ` ${params.name}：${params.value}GB `
            }else {
              // return ` ${params.name}：`+`${params.value}` * 100 + '%'
              return ` ${params.name}：`+`${ Math.round(params.value * TVC.value)}GB`
            }
          }
        },
        series: [
          {
            type: "liquidFill",
            // data: [0.6, 0.55, 0.4, 0.25],
            outline: {
              show: false
            },
            backgroundStyle: {
              borderColor: '#156ACF',
              borderWidth: 1,
              shadowColor: 'rgba(0, 0, 0, 0.4)',
              shadowBlur: 20
            },
            shape: 'path://M367.855,428.202c-3.674-1.385-7.452-1.966-11.146-1.794c0.659-2.922,0.844-5.85,0.58-8.719 c-0.937-10.407-7.663-19.864-18.063-23.834c-10.697-4.043-22.298-1.168-29.902,6.403c3.015,0.026,6.074,0.594,9.035,1.728 c13.626,5.151,20.465,20.379,15.32,34.004c-1.905,5.02-5.177,9.115-9.22,12.05c-6.951,4.992-16.19,6.536-24.777,3.271 c-13.625-5.137-20.471-20.371-15.32-34.004c0.673-1.768,1.523-3.423,2.526-4.992h-0.014c0,0,0,0,0,0.014 c4.386-6.853,8.145-14.279,11.146-22.187c23.294-61.505-7.689-130.278-69.215-153.579c-61.532-23.293-130.279,7.69-153.579,69.202 c-6.371,16.785-8.679,34.097-7.426,50.901c0.026,0.554,0.079,1.121,0.132,1.688c4.973,57.107,41.767,109.148,98.945,130.793 c58.162,22.008,121.303,6.529,162.839-34.465c7.103-6.893,17.826-9.444,27.679-5.719c11.858,4.491,18.565,16.6,16.719,28.643 c4.438-3.126,8.033-7.564,10.117-13.045C389.751,449.992,382.411,433.709,367.855,428.202z',
            label: {
              position: ['38%', '40%'],
              formatter: function(params) {
                return Math.round(params.value * TVC.value) + "GB";
              },
              fontSize: 30,
              color: '#294d98'
            },


            itemStyle: {
              normal: {
                opacity: 0.4,
                shadowBlur: 0,
                color: "#3B7BF8",
                shadowColor: "#3B7BF8"
              }
            },
            waveLength: '80%',//波长
            waveHeight: '60',//波长
            amplitude: '15%',//振幅
            data: [
              {
                value: AVC.value,
                name: AVC.name,
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
                value: AVC.value,
                name: AVC.name,
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
                value: AVC.value,
                name: AVC.name,
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
            // outline: {
            //   itemStyle: {
            //     borderColor: "#3F1EB9",
            //     borderWidth: 8
            //   },
            //   borderDistance: 3
            // }
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
                name: TVC.name,
                value: TVC.value,
                label: {
                  show: false,
                  position: "center",
                  color: "#fff",
                  fontSize: 38,
                  fontWeight: "bold",
                  // formatter: function(o) {
                  //   return data;
                  // }
                }
              },

              {
                //画剩余的刻度圆环
                name: RVCores.name,
                value: RVCores.value,
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
