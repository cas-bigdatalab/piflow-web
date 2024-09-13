<template>
  <section class="card chart_type">
    <h1>图表类型</h1>
    <div class="chart_select">
        <div v-for="item  in  typeList"  :class="{'active':item.type === chartType}" :key="item.name" @click="handleClick(item.type)">
          <img :src="item.img" alt="">
          <p>{{item.name}}</p>
          </div>
    </div>
  </section>
</template>
  
  <script>
import { mapGetters } from "vuex";
export default {
  name: "ChartType",
  components: {},
  data() {
    return {
      typeList: [
        {
          type: "lineChart",
          name: "折线图",
          img:require('../../images/line.png')
        },
        {
          type: "barChart",
          name: "柱状图",
          img:require('../../images/bar.png')
        },
        // {
        //   type: "PieChart",
        //   name: "饼状图",
        //   img:require('../../images/pie.png')
        // },
      ],
    };
  },
  computed: {
    ...mapGetters("graphConf", ["chartType"]),
  },
  methods:{
    handleClick(val){
      this.$store.dispatch("graphConf/changeChartType", val);
    }
  }

};
</script>
<style lang="scss" scoped>
@import "../../index.scss";
.chart_type{
  height: calc(20vh - 20px);
}
.chart_select{
  display: flex;
  padding-left: 20px;
  padding-top: 22px;
  >div{
    padding: 4px;
    text-align: center;
    margin-right: 10px;
    cursor: pointer;
    border-radius: 5px;
    &.active{
      background: #eee;
    }
  }
  img{
    width: 40px;
    height: 40px;
    border: 1px solid #eee;
  }
}
</style>
  
  