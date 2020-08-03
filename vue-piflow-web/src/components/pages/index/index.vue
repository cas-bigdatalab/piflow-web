<template>
  <section>
    <ul class="console-list">
      <li>
        <span>12</span>
        <span>主题库数量</span>
        <i class="fa fa-database"></i>
      </li>
      <li>
        <span>12</span>
        <span>主题库数量</span>
        <i class="fa fa-pie-chart"></i>
      </li>
      <li>
        <span>12</span>
        <span>主题库数量</span>
        <i class="fa fa-cubes"></i>
        <!-- <i class="fa fa-object-group"></i> -->
      </li>
      <li>
        <span>12</span>
        <span>主题库数量</span>
        <i class="fa fa-retweet"></i>
      </li>
      <li>
        <span>12</span>
        <span>主题库数量</span>
        <i class="fa fa-plug"></i>
      </li>
      <li>
        <span>12</span>
        <i class="fa fa-server"></i>
        <span>主题库数量</span>
      </li>
    </ul>
    <div class="card-box">
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

