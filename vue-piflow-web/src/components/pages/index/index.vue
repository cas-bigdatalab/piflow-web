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
            <WaterPoloChart :content-data="resource.cpu" />
          </div>
        </div>
      </Col>
      <Col span="8">
        <div class="card">
<!--          <div class="card-header">-->
<!--            <span>Memory usage</span>-->
<!--          </div>-->
          <div class="card-body" :style="{height:height+'px'}">
            <RingChart :content-data="resource.memory" />
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
              <img style="width: 100px;height: 100px;background: #CCCCCC;overflow: hidden;border-radius: 10px" class="logo" src="../../../assets/img/logo.png" alt="logo" />
              <ul class="aaa">
                <li><p>FLOW：</p></li>
                <li><p>PROCESSOR：</p></li>
                <li><p>COMPETED：</p></li>
                <li><p>FAILED：</p></li>
                <li><p>KILLED：</p></li>
                <li><p>OTHER：</p></li>
                <li><p>STARTED：</p></li>
              </ul>
            </div>
          </div>
        </Col>
        <Col span="12">
          <div class="card">
            <div class="card-body" :style="{height:height+'px'}">
              22
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
import size from "../../../utils/winSize";
export default {
  name: "sections",
  components: { WaterPoloChart, RingChart },
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
    };
  },
  mounted() {
    this.height = size.PageH - 360;
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
      // this.$axios
      //         .get("/dashboard/flowStatistic")
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
</style>

