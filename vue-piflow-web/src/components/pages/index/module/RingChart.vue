<template>
  <div class="chart" ref="chart"></div>
</template>

<script>
import "echarts-liquidfill/src/liquidFill.js"; //在这里引入
export default {
  name: "chart",
  data: () => ({
    content: null,
    myEcharts: null,
  }),
  mounted() {
    this.$nextTick(() => {
      this.initChart();
    });
  },
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

  methods: {
    // 使用 function 传参
    initChart() {
      let echarts = this.$echarts;
      this.myEcharts = echarts.init(this.$refs.chart);
      let contentData = [], color='';
      if (this.content !== null && this.content.parameter === 'cpu'){
        contentData = [];
        color='#a181fc';
        for (let key in this.content){
          let obj = {};
          if (key === 'allocatedVirtualCores'){
            obj.value = this.content[key];
            obj.name = key;
            obj.itemStyle = {
              normal: {
                color: "#a181fc",
                shadowColor: "#a181fc"
              },
            };
            contentData.push(obj)
          }
          if (key === 'remainingVirtualCores'){
            obj.value = this.content[key];
            obj.name = key;
            obj.itemStyle = {
              normal: {
                color: "#e3d9fe"
              }
            }
            contentData.push(obj)
          }
        }
      }
      if (this.content !== null && this.content.parameter === 'memory'){
        contentData = [];
        color='#a8e6cf';
        for (let key in this.content){
          let obj = {};
          if (key === 'allocatedMemoryGB'){
            obj.value = this.content[key];
            obj.name = key;
            obj.itemStyle = {
              normal: {
                color: "#a8e6cf",
                shadowColor: "#a8e6cf"
              },
            };
            contentData.push(obj)
          }
          if (key === 'remainingMemoryGB'){
            obj.value = this.content[key];
            obj.name = key;
            obj.itemStyle = {
              normal: {
                color: "#dcedc1"
              }
            }
            contentData.push(obj)
          }
        }
      }
      if (this.content !== null && this.content.parameter === 'hdfs'){
        contentData = [];
        color='#68b0ab';
        for (let key in this.content){
          let obj = {};
          if (key === 'allocatedCapacityGB'){
            obj.value = this.content[key];
            obj.name = key;
            obj.itemStyle = {
              normal: {
                color: "#68b0ab",
                shadowColor: "#68b0ab"
              },
            };
            contentData.push(obj)
          }
          if (key === 'remainingCapacityGB'){
            obj.value = this.content[key];
            obj.name = key;
            obj.itemStyle = {
              normal: {
                color: "#e3d9fe"
              }
            }
            contentData.push(obj)
          }
        }
      }
      let option = {
        tooltip: {
          trigger: "item",
          formatter: function(params) {
            return ` ${params.name}：${params.value}GB `
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
            data: contentData
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
            data: contentData
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
