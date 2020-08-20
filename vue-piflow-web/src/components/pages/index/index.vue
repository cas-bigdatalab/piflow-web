<template>
  <section>
    <div class="Introduction" style="width: 100%;
    height: 200px;
    /*height: 100%;*/
    border: 1px solid #cee7ff;
    margin: 10px 0;
    background: rgb(241 241 246 / 0.6);
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

    <Row :gutter="16">
      <Col span="8">
        <div class="card">
<!--          <div class="card-header">-->
<!--            <span>cpu usage</span>-->
<!--          </div>-->
          <div class="card-body" :style="{height:height+'px'}">
<!--            <WaterPoloChart :content-data="resource.cpu" />-->
<!--              <RingChart :content-data="resource.cpu" />-->
              <raddar-chart />
          </div>
        </div>
      </Col>
      <Col span="8">
        <div class="card">
<!--          <div class="card-header">-->
<!--            <span>Memory usage</span>-->
<!--          </div>-->
          <div class="card-body" :style="{height:height+'px'}">
<!--            <RingChart :content-data="resource.memory" />-->
              <pie-chart :content-data="resource.memory" />
          </div>
        </div>
      </Col>
      <Col span="8">
        <div class="card">
<!--          <div class="card-header">-->
<!--            <span>Hard disk usage</span>-->
<!--          </div>-->
          <div class="card-body" :style="{height:height+'px'}">
            <RingChart :content-data="resource.hdfs" />
          </div>
        </div>
      </Col>
    </Row>
<!--    <div class="card-box">-->

<!--      <div class="card">-->
<!--        <div class="card-header">-->
<!--          <span>内存使用情况</span>-->
<!--        </div>-->
<!--        <div class="card-body" :style="{height:height+'px'}">-->
<!--          <WaterPoloChart />-->
<!--        </div>-->
<!--      </div>-->

<!--    </div>-->
    <div style="height: 500px; margin-top: 20px;">
      <Row :gutter="16">
          <Col span="12">
          <div class="card">
            <div class="card-body" :style="{height:300+'px'}">
              <img style="width: 100px;margin:20px;height: 100px;background: #e8ecee;overflow: hidden;border-radius: 50%;float: left" class="flow" src="../../../assets/img/flow.png" alt="flow" />
              <ul class="aaa" style="float:left;width: 65%;margin-top: 20px;margin-left: 20px;font-size: 1.5vw;letter-spacing: -.02em;">
                  <li>
                      <span>FLOW:</span>
                      <span class="example">
                          <countTo :startVal='startVal' :endVal='endVal' :duration='3000'></countTo>
                      </span>
                  </li>
                  <li><p>FLOW：15</p></li>
                <li><p>PROCESSOR：123</p></li>
                <li><p>COMPETED：69</p></li>
                <li><p>FAILED：0</p></li>
                <li><p>KILLED：0</p></li>
                <li><p>OTHER：2</p></li>
                <li><p>STARTED：52</p></li>
              </ul>
            </div>
          </div>
        </Col>
          <Col span="12">
              <div class="card">
            <div class="card-body" :style="{height:300+'px'}">
              <img style="width: 100px;margin:20px;height: 100px;background: #e8ecee;overflow: hidden;border-radius: 10px;float: left" class="flow" src="../../../assets/img/group1.png" alt="flow" />
              <ul class="aaa" style="float:left;width: 65%;margin-top: 20px;margin-left: 20px;font-size: 1.5vw;letter-spacing: -.02em;">
                <li><p>FLOW：15</p></li>
                <li><p>PROCESSOR：123</p></li>
                <li><p>COMPETED：69</p></li>
                <li><p>FAILED：0</p></li>
                <li><p>KILLED：0</p></li>
                <li><p>OTHER：2</p></li>
                <li><p>STARTED：52</p></li>
              </ul>
            </div>
          </div>
          </Col>
      </Row>
    </div>
  </section>
</template>

<script>
import WaterPoloChart from "./module/WaterPoloChart";
import RingChart from "./module/RingChart";
import RaddarChart from './module/RaddarChart'
import PieChart from './module/PieChart'
import size from "../../../utils/winSize";
import countTo from 'vue-count-to';
export default {
  name: "sections",
  components: { WaterPoloChart, RingChart, RaddarChart, PieChart, countTo },
  props: {
    msg: String
  },
  data() {
    return {
      height: 500,
      resource: {
        cpu: {},
        memory: {},
        hdfs: {},
      },
        startVal: 0,
        endVal: 2017
    };
  },
  mounted() {
    this.height = size.PageH - 300;
    this.getResources();
    this.getFlowInfo();
  },
  methods:{
    getResources(){
      this.$axios
              .get("/dashboard/resource")
              .then(res => {
                if (res.data.code == 200) {
                  let data = JSON.parse(res.data.resourceInfo);
                  this.resource.cpu = data.resource.cpu;
                  this.resource.cpu.parameter = 'cpu';
                  this.resource.memory = data.resource.memory;
                  this.resource.memory.parameter = 'memory';
                  this.resource.hdfs = data.resource.hdfs;
                  this.resource.hdfs.parameter = 'hdfs';
                } else {
                  this.$Modal.error({
                    title: this.$t("tip.tilte"),
                    content: this.$t("tip.request_fail_content")
                  });
                }
              })
              .catch(error => {
                console.log(error);
                this.$Modal.error({
                  title: this.$t("tip.tilte"),
                  content: this.$t("tip.fault_content")
                });
              });
    },
    getFlowInfo(){
      // flow
      this.$axios
              .get("/dashboard/flowStatistic")
              .then(res => {
                if (res.data.code == 200) {
                  let data = res.data.flowResourceInfo;
                  // console.log(data)
                } else {
                  this.$Modal.error({
                    title: this.$t("tip.tilte"),
                    content: this.$t("tip.request_fail_content")
                  });
                }
              })
              .catch(error => {
                console.log(error);
                this.$Modal.error({
                  title: this.$t("tip.tilte"),
                  content: this.$t("tip.fault_content")
                });
              });

      // 流水线组统计
      // this.$axios
      //         .get("/dashboard/groupStatistic")
      //         .then(res => {
      //           if (res.data.code == 200) {
      //             let data = res.data.flowResourceInfo;
      //             // console.log(data)
      //           } else {
      //             this.$Modal.error({
      //               title: this.$t("tip.tilte"),
      //               content: this.$t("tip.request_fail_content")
      //             });
      //           }
      //         })
      //         .catch(error => {
      //           console.log(error);
      //           this.$Modal.error({
      //             title: this.$t("tip.tilte"),
      //             content: this.$t("tip.fault_content")
      //           });
      //         });

      // 调度
      // this.$axios
      //         .get("/dashboard/scheduleStatistic")
      //         .then(res => {
      //           if (res.data.code == 200) {
      //             let data = res.data.flowResourceInfo;
      //             // console.log(data)
      //           } else {
      //             this.$Modal.error({
      //               title: this.$t("tip.tilte"),
      //               content: this.$t("tip.request_fail_content")
      //             });
      //           }
      //         })
      //         .catch(error => {
      //           console.log(error);
      //           this.$Modal.error({
      //             title: this.$t("tip.tilte"),
      //             content: this.$t("tip.fault_content")
      //           });
      //         });

      // 模板和数据源统计
      // this.$axios
      //         .get("/dashboard/templateAndDataSourceStatistic")
      //         .then(res => {
      //           if (res.data.code == 200) {
      //             let data = res.data.flowResourceInfo;
      //             // console.log(data)
      //           } else {
      //             this.$Modal.error({
      //               title: this.$t("tip.tilte"),
      //               content: this.$t("tip.request_fail_content")
      //             });
      //           }
      //         })
      //         .catch(error => {
      //           console.log(error);
      //           this.$Modal.error({
      //             title: this.$t("tip.tilte"),
      //             content: this.$t("tip.fault_content")
      //           });
      //         });

      // 组件统计
      // this.$axios
      //         .get("/dashboard/stopStatistic")
      //         .then(res => {
      //           if (res.data.code == 200) {
      //             let data = res.data.flowResourceInfo;
      //             // console.log(data)
      //           } else {
      //             this.$Modal.error({
      //               title: this.$t("tip.tilte"),
      //               content: this.$t("tip.request_fail_content")
      //             });
      //           }
      //         })
      //         .catch(error => {
      //           console.log(error);
      //           this.$Modal.error({
      //             title: this.$t("tip.tilte"),
      //             content: this.$t("tip.fault_content")
      //           });
      //         });
    }
  }
};
</script>
<style lang="scss" scoped>
@import "./index.scss";
  .aaa li p{
    font-weight: 400; line-height: 22px; color: #999;
  }
.aaa li {
    margin: 10px 0;
     }
.aaa li:nth-child(1){
    margin-top: 0;
}.example{
     color: #999;
     display: inline-block;
     margin: 10px 0;
     text-align: center;
     font-size: 40px;
     font-weight: 500;
 }
</style>

