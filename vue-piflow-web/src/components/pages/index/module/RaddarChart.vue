<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme

const animationDuration = 3000

export default {
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '300px'
    }
  },
  data() {
    return {
      chart: null
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')

      this.chart.setOption({
        tooltip: {
          trigger: 'item',
          // axisPointer: { // 坐标轴指示器，坐标轴触发有效
          //   type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          // },
          // formatter: '{a} <br/>{b}: {c} GB '
          formatter: function(params) {
            console.log(params)
            return ` ${params.name}：${params.value[0]}GB `
          }
        },
        radar: {
          radius: '66%',
          center: ['50%', '42%'],
          splitNumber: 8,
          splitArea: {
            areaStyle: {
              color: 'rgba(127,95,132,.3)',
              opacity: 1,
              shadowBlur: 45,
              shadowColor: 'rgba(0,0,0,.5)',
              shadowOffsetX: 0,
              shadowOffsetY: 15
            }
          },
          name:{
            show: false, // 是否显示工艺等文字
            formatter: null, // 工艺等文字的显示形式
            textStyle: {
              // color:'#b306b6' // 工艺等文字颜色
            }
          },
          indicator: [
            { name: 'Sales', max: 10 },
            { name: 'Administration', max: 10 },
            { name: 'Information Technology', max: 10 },
            { name: 'Customer Support', max: 10 },
            { name: 'Development', max: 10 },
            { name: 'Marketing', max: 10 }
          ]
        },
        legend: {
          left: 'center',
          bottom: '10',
          data: ['TotalVirtualCores', 'AllocatedVirtualCores', 'RemainingVirtualCores']
        },
        series: [{
          type: 'radar',
          symbolSize: 0,
          areaStyle: {
            normal: {
              shadowBlur: 13,
              shadowColor: 'rgba(0,0,0,.2)',
              shadowOffsetX: 0,
              shadowOffsetY: 10,
              opacity: 1
            }
          },
          data: [
            {
              value: [0, 0, 0, 0, 0, 0],
              name: 'TotalVirtualCores'
            },
            {
              value: [5, 5, 5, 5, 5, 5],
              name: 'AllocatedVirtualCores'
            },
            {
              value: [8, 8, 8, 8, 8, 8],
              name: 'RemainingVirtualCores'
            }
          ],
          animationDuration: animationDuration
        }]
      })
    }
  }
}
</script>
