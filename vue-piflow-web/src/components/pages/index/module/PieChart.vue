<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons');// echarts theme

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
      default: '350px'
    },
    contentData: {
      type: Object,
    }
  },
  watch: {
    contentData: {
      handler(newVal){
        this.content = newVal;
        this.initChart();
      },
      deep:true,
    },
  },
  data() {
    return {
      chart: null,
      content: null,
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
      this.chart = echarts.init(this.$el, 'macarons');
      let MemoryRatio = [], TotalMemory = [], legendData = [];
      if (this.content !== null && this.content.parameter === 'memory'){
        MemoryRatio = []; TotalMemory = [];
        for (let key in this.content){
          let obj = {};
          switch (key) {
            case 'totalMemoryGB':
              obj.value = this.content[key];
              obj.name = key;
              TotalMemory.push(obj);
              legendData.push(key);
              break;
            case 'allocatedMemoryGB':
              obj.value = this.content[key];
              obj.name = key;
              obj.selected = true;
              MemoryRatio.push(obj);
              legendData.push(key);
              break;
            case 'remainingMemoryGB':
              obj.value = this.content[key];
              obj.name = key;
              MemoryRatio.push(obj);
              legendData.push(key);
              break;
            default:
              break;
          }
        }
      }
      this.chart.setOption({
        // tooltip: {
        //   trigger: 'item',
        //   formatter: '{a} <br/>{b}: {c} GB '
        // },
        // legend: {
        //   left: 'center',
        //   bottom: '10',
        //   data: legendData
        // },
        // series: [
        //   {
        //     name: 'WEEKLY WRITE ARTICLES',
        //     type: 'pie',
        //     roseType: 'radius',
        //     // radius: [15, 95],
        //     radius: [0, '40%'],
        //     center: ['50%', '38%'],
        //     data: [
        //       { value: 320, name: 'Industries' },
        //       { value: 40, name: 'Technology' }
        //     ],
        //     animationEasing: 'cubicInOut',
        //     animationDuration: 2600
        //   },
        //   {
        //     name: 'Memory：',
        //     type: 'pie',
        //     radius: ['60%', '70%'],
        //     center: ['50%', '38%'],
        //     data: TotalMemory,
        //     animationEasing: 'cubicInOut',
        //     animationDuration: 2600
        //   }
        // ]

        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} GB '
        },
        // legend: {
        //   left: 'center',
        //   bottom: '10',
        //   data: legendData
        // },
        series: [
          {
            name: 'Memory：',
            type: 'pie',
            selectedMode: 'single',
            radius: [0, '30%'],
            center: ['50%', '50%'],
            label: {
              position: 'inner'
            },
            labelLine: {
              show: false
            },
            data: MemoryRatio
          },
          {
            name: 'Memory：',
            type: 'pie',
            radius: ['50%', '65%'],
            center: ['50%', '50%'],
            data: TotalMemory,
                animationEasing: 'cubicInOut',
                animationDuration: 2600
          }
        ]
      })
    }
  }
}
</script>
