<template>
  <section>
    <div class="Introduction" style="width: 100%;
    height: 200px;
    /*height: 100%;*/
    border: 1px solid #cee7ff;
    margin: 10px 0;
    background: #e6f5ff;
    color: #666666;
    padding: 20px ">
      简介：这是一段描述
    </div>
<!--    <ul class="console-list">-->
<!--      <li>-->
<!--        <span>12</span>-->
<!--        <span>主题库数量</span>-->
<!--        <i class="fa fa-database"></i>-->
<!--      </li>-->
<!--      <li>-->
<!--        <span>12</span>-->
<!--        <span>主题库数量</span>-->
<!--        <i class="fa fa-pie-chart"></i>-->
<!--      </li>-->
<!--      <li>-->
<!--        <span>12</span>-->
<!--        <span>主题库数量</span>-->
<!--        <i class="fa fa-cubes"></i>-->
<!--        &lt;!&ndash; <i class="fa fa-object-group"></i> &ndash;&gt;-->
<!--      </li>-->
<!--      <li>-->
<!--        <span>12</span>-->
<!--        <span>主题库数量</span>-->
<!--        <i class="fa fa-retweet"></i>-->
<!--      </li>-->
<!--      <li>-->
<!--        <span>12</span>-->
<!--        <span>主题库数量</span>-->
<!--        <i class="fa fa-plug"></i>-->
<!--      </li>-->
<!--      <li>-->
<!--        <span>12</span>-->
<!--        <i class="fa fa-server"></i>-->
<!--        <span>主题库数量</span>-->
<!--      </li>-->
<!--    </ul>-->
    <div class="card-box">
      <div class="card">
        <div class="card-header">
          <span>cpu使用情况</span>
        </div>
        <div class="card-body" :style="{height:height+'px'}">
          <WaterPoloChart />
        </div>
      </div>
      <div class="card">
        <div class="card-header">
          <span>内存使用情况</span>
        </div>
        <div class="card-body" :style="{height:height+'px'}">
          <WaterPoloChart />
        </div>
      </div>
      <div class="card">
        <div class="card-header">
          <span>硬盘使用情况</span>
        </div>
        <div class="card-body" :style="{height:height+'px'}">
          <RingChart />
        </div>
      </div>
    </div>
    <div style="height: 500px; background: #fddbcb; margin-top: 20px;padding: 10px">
      <div style="height: 300px;width:50%;background: white;border-radius: 10px">

      </div>
    </div>
  </section>
</template>

<script>
import WaterPoloChart from "./module/WaterPoloChart";
import RingChart from "./module/RingChart";
import size from "../../../utils/winSize";
export default {
  name: "sections",
  components: { WaterPoloChart, RingChart },
  props: {
    msg: String
  },
  data() {
    return {
      height: 550
    };
  },
  mounted() {
    this.height = size.PageH - 360;
  }
};
</script>
<style lang="scss" scoped>
@import "./index.scss";
</style>

