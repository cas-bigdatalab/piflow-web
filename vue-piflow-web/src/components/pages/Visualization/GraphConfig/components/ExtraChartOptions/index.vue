<template>
  <!-- <section class="card">
    <h1>图表配置</h1> -->
    <component :is="chartType" />
  <!-- </section> -->
</template>
  
  <script>
import LineChart from './LineChart'
import BarChart from './BarChart'
import PieChart from './PieChart'
import { mapGetters } from "vuex";
export default {
  name: "ExtraChartOptions",
  components: {
    LineChart,
    BarChart,
    PieChart
  },
  computed: {
    ...mapGetters("graphConf", ["chartType"]),
  },
};
</script>
<style lang="scss" scoped>
@import "../../index.scss";
</style>
  
  