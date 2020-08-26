<template>
  <section>
      <Tabs value="name1">
          <TabPane label="Introduction" name="name1">
              <Card shadow style="background: rgb(241 241 246 / 0.6);margin-bottom: 20px">
                  <div class="Introduction" style="width: 100%;color: #666666;letter-spacing:1px">
                      PiFlow是一款基于分布式计算引擎Spark开发的大数据流水线系统，以所见即所得的方式，实现大数据采集、处理、存储与分析流程化配置、运行与智能监控，提供了100+的标准化组件， 包括Hadoop 、Spark、MLlib、Hive、Solr、ElasticSearch、Redis等，更支持面向领域、灵活的二次组件开发，性能优越。
                  </div>
              </Card>
          </TabPane>
      </Tabs>

<!--    <Row :gutter="16">-->
<!--      <Col span="8">-->
<!--        <div class="card">-->
<!--          <div class="card-body" :style="{height:height+'px'}">-->
<!--              <RingChart :content-data="resource.cpu" />-->
<!--          </div>-->
<!--        </div>-->
<!--      </Col>-->
<!--      <Col span="8">-->
<!--        <div class="card">-->
<!--          <div class="card-body" :style="{height:height+'px'}">-->
<!--&lt;!&ndash;            <RingChart :content-data="resource.memory" />&ndash;&gt;-->
<!--&lt;!&ndash;              <pie-chart :content-data="resource.memory" />&ndash;&gt;-->
<!--          </div>-->
<!--        </div>-->
<!--      </Col>-->
<!--      <Col span="8">-->
<!--        <div class="card">-->
<!--          <div class="card-body" :style="{height:height+'px'}">-->
<!--              <WaterPoloChart :content-data="resource.hdfs" />-->
<!--&lt;!&ndash;            <RingChart :content-data="resource.hdfs" />&ndash;&gt;-->
<!--          </div>-->
<!--        </div>-->
<!--      </Col>-->
<!--    </Row>-->


      <Tabs value="name1">
          <TabPane label="Monitoring Information" name="name1">
              <Row :gutter="16" type="flex" justify="space-around" style="margin-bottom: 20px">
                  <Col span="6" style="text-align: center">
                      <Card>
                          <h3 style="text-align: left">CPU Disk Usage</h3>
                          <i-circle
                                  :size="150"
                                  :trail-width="6"
                                  :stroke-width="8"
                                  :percent="cpuPercent"
                                  stroke-linecap="square"
                                  :stroke-color="color">
                              <div class="demo-Circle-custom">
                                  <h3>{{resource.cpu.totalVirtualCores}}核</h3>
                                  <p>总容量</p>
                                  <span>
                              已使用
                              <i>{{ cpuPercent }}%</i>
                          </span>
                              </div>
                          </i-circle>
                      </Card>
                  </Col>
                  <Col span="6" style="text-align: center">
                      <Card>
                          <h3 style="text-align: left">Memory Disk Usage</h3>
                          <i-circle
                                  :size="150"
                                  :trail-width="6"
                                  :stroke-width="8"
                                  :percent="memoryPercent"
                                  stroke-linecap="square"
                                  :stroke-color="color">
                              <div class="demo-Circle-custom">
                                  <h3>{{resource.memory.totalMemoryGB}}GB</h3>
                                  <p>总容量</p>
                                  <span>
                              已使用
                              <i>{{ memoryPercent }}%</i>
                          </span>
                              </div>
                          </i-circle>
                      </Card>
                  </Col>
                  <Col span="6" style="text-align: center">
                      <Card>
                          <h3 style="text-align: left">HDFS Disk Usage</h3>
                          <i-circle
                                  :size="150"
                                  :trail-width="6"
                                  :stroke-width="8"
                                  :percent="hdfsPercent"
                                  stroke-linecap="square"
                                  :stroke-color="color">
                              <div class="demo-Circle-custom">
                                  <h3>{{resource.hdfs.TotalCapacityGB}}GB</h3>
                                  <p>总容量</p>
                                  <span>
                              已使用
                              <i>{{ Number(hdfsPercent.toFixed(2)) }}%</i>
                          </span>
                              </div>
                          </i-circle>
                      </Card>
                  </Col>
              </Row>
          </TabPane>
      </Tabs>

      <Tabs value="name1">
          <TabPane label="statistics" name="name1">
              <div style="height: 500px">
<!--                  <h2 style="text-align: center;margin-bottom: 20px">-->
<!--                      PiFlow是一款基于分布式计算引擎Spark开发的大数据流水线系统-->
<!--                  </h2>-->
                  <Row :gutter="16" style="margin-bottom: 20px">
                      <Col span="8" style="height: 340px" >
                          <div class="ContentQuantity">
                              <div class="statisticsImg SICutCorner">
                                  <img src="../../../assets/img/flow.png" alt="flow" />
                              </div>
                              <ul class="aaa" style="float:left;width: 50%;margin-top: 20px;font-size: 1vw;letter-spacing: -.02em;">
                                  <!--                  <li>-->
                                  <!--                      <span>FLOW：</span>-->
                                  <!--                      <countTo :EndVal = Number(flowResourceInfo.FLOW_COUNT)></countTo>-->
                                  <!--                  </li>-->
                                  <li><p>FLOW：
                                      <countTo :EndVal = Number(flowResourceInfo.FLOW_COUNT)></countTo>
                                  </p></li>
                                  <li><p>PROCESSOR：
                                      <countTo :EndVal = Number(flowResourceInfo.PROCESSOR_COUNT)></countTo>
                                  </p></li>
                                  <li><p class="textIndex">COMPETED：
                                      <countTo :EndVal = Number(flowResourceInfo.PROCESSOR_COMPETED_COUNT)></countTo>
                                  </p></li>
                                  <li><p class="textIndex">FAILED：
                                      <countTo :EndVal = Number(flowResourceInfo.PROCESSOR_FAILED_COUNT)></countTo>
                                  </p></li>
                                  <li><p class="textIndex">KILLED：
                                      <countTo :EndVal = Number(flowResourceInfo.PROCESSOR_KILLED_COUNT)></countTo>
                                  </p></li>
                                  <li><p class="textIndex">OTHER：
                                      <countTo :EndVal = Number(flowResourceInfo.PROCESSOR_OTHER_COUNT)></countTo>
                                  </p></li>
                                  <li><p class="textIndex">STARTED：
                                      <countTo :EndVal = Number(flowResourceInfo.PROCESSOR_STARTED_COUNT)></countTo>
                                  </p></li>
                              </ul>
                              <Card shadow style="float:left;background: rgb(241 241 246 / 0.6);margin: 10px;    margin-top: 46px;">
                                  <div class="Introduction" style="width: 100%;color: #666666;font-family: '微软雅黑';letter-spacing:1px">
                                      流水线统计信息，包括流水线Flow数量、运行态流水线Processor数量，以及各运行状态下Processor数量。
                                  </div>
                              </Card>
                          </div>
                      </Col>
                      <Col span="8" style="height: 260px">
                          <div class="ContentQuantity">
                              <div class="statisticsImg SIRound">
                                  <img src="../../../assets/img/schedule.png" alt="SCHEDULE" />
                              </div>
                              <ul class="aaa" style="float:left;width: 50%;margin-top: 20px;font-size: 1vw;letter-spacing: -.02em;">
                                  <li><p>SCHEDULE：
                                      <countTo :EndVal = Number(scheduleResourceInfo.SCHEDULE_COUNT)></countTo>
                                  </p></li>
                                  <li><p class="textIndex">INIT：
                                      <countTo :EndVal = Number(scheduleResourceInfo.SCHEDULE_INIT_COUNT)></countTo>
                                  </p></li>
                                  <li><p class="textIndex">RUNNING：
                                      <countTo :EndVal = Number(scheduleResourceInfo.SCHEDULE_RUNNING_COUNT)></countTo>
                                  </p></li>
                                  <li><p class="textIndex">STOP：
                                      <countTo :EndVal = Number(scheduleResourceInfo.SCHEDULE_STOP_COUNT)></countTo>
                                  </p></li>
                              </ul>
                              <Card shadow style="float:left;background: rgb(241 241 246 / 0.6);margin: 10px">
                                  <div class="Introduction" style="width: 100%;color: #666666;font-family: '微软雅黑';letter-spacing:1px">
                                      调度统计信息，包括调度流水线/流水线组数量，以及各个状态下Schedule数量。
                                  </div>
                              </Card>
                          </div>
                      </Col>
                      <Col span="8" style="height: 330px">
                          <div class="ContentQuantity">
                              <div class="statisticsImg SIRrdial">
                                  <img src="../../../assets/img/group.png" alt="group" />
                              </div>
                              <ul class="aaa" style="float:left;width: 50%;margin-top: 20px;font-size: 1vw;letter-spacing: -.02em;">
                                  <li><p>GROUP：
                                      <countTo :EndVal = Number(groupResourceInfo.GROUP_COUNT)></countTo>
                                  </p></li>
                                  <li><p>PROCESSOR：
                                      <countTo :EndVal = Number(groupResourceInfo.PROCESSOR_COUNT)></countTo>
                                  </p></li>
                                  <li><p class="textIndex">COMPETED：
                                      <countTo :EndVal = Number(groupResourceInfo.PROCESSOR_COMPETED_COUNT)></countTo>
                                  </p></li>
                                  <li><p class="textIndex">FAILED：
                                      <countTo :EndVal = Number(groupResourceInfo.PROCESSOR_FAILED_COUNT)></countTo>
                                  </p></li>
                                  <li><p class="textIndex">KILLED：
                                      <countTo :EndVal = Number(groupResourceInfo.PROCESSOR_KILLED_COUNT)></countTo>
                                  </p></li>
                                  <li><p class="textIndex">OTHER：
                                      <countTo :EndVal = Number(groupResourceInfo.PROCESSOR_OTHER_COUNT)></countTo>
                                  </p></li>
                                  <li><p class="textIndex">STARTED：
                                      <countTo :EndVal = Number(groupResourceInfo.PROCESSOR_STARTED_COUNT)></countTo>
                                  </p></li>
                              </ul>
                              <Card shadow style="float:left;background: rgb(241 241 246 / 0.6);margin: 10px;    margin-top: 36px;">
                                  <div class="Introduction" style="width: 100%;color: #666666;font-family: '微软雅黑';letter-spacing:1px">
                                      流水线组统计信息，包括流水线组Group数量、运行态流水线组Processor数量，以及各运行状态下Processor数量。
                                  </div>
                              </Card>
                          </div>
                      </Col>
                  </Row>
                  <Row :gutter="16">
                      <Col span="8" style="visibility: hidden">
                          <div class="ContentQuantity">

                          </div>
                      </Col>
                      <Col style="margin-top: -80px;height: 280px" span="8">
                          <div class="ContentQuantity">

                              <div class="statisticsImg SITilt">
                                  <img src="../../../assets/img/others.png" alt="others" />
                              </div>
                              <ul class="aaa" style="float:left;width: 50%;margin-top: 20px;font-size: 1vw;letter-spacing: -.02em;">
                                  <li><p>DATASOURCE：
                                      <countTo :EndVal = Number(tempalateAndDataSourceResourceInfo.DATASOURCE_COUNT)></countTo>
                                  </p></li>
                                  <li><p>STOPSHUB：
                                      <countTo :EndVal = Number(tempalateAndDataSourceResourceInfo.STOPSHUB_COUNT)></countTo>
                                  </p></li>
                                  <li><p>TEMPLATE：
                                      <countTo :EndVal = Number(tempalateAndDataSourceResourceInfo.TEMPLATE_COUNT)></countTo>
                                  </p></li>
                              </ul>
                              <Card shadow style="float:left;background: rgb(241 241 246 / 0.6);margin: 10px">
                                  <div class="Introduction" style="width: 100%;color: #666666;font-family: '微软雅黑';letter-spacing:1px">
                                      其他统计信息，包括数据源DataSource数量、自定义组件插件StopsHub数量、模板Template数量。
                                  </div>
                              </Card>
                          </div>
                      </Col>
                      <Col style="margin-top: -20px;height: 260px" span="8">
                          <div class="ContentQuantity">
                              <div class="statisticsImg SIRound">
                                  <img src="../../../assets/img/stop.png" alt="STOP" />
                              </div>
                              <ul class="aaa" style="float:left;width: 50%;margin-top: 20px;font-size: 1vw;letter-spacing: -.02em;">
                                  <li><p>STOP：
                                      <countTo :EndVal = Number(stopResourceInfo.STOP_COUNT)></countTo>
                                  </p></li>
                                  <li><p>STOPGROUP：
                                      <countTo :EndVal = Number(stopResourceInfo.STOPGROUP_COUNT)></countTo>
                                  </p></li>
                              </ul>
                              <Card shadow style="float:left;background: rgb(241 241 246 / 0.6);margin: 10px">
                                  <div class="Introduction" style="width: 100%;color: #666666;font-family: '微软雅黑';letter-spacing:1px">
                                      组件统计信息，包括数据处理组件Stop数量，数据处理组件组StopGroup数量。
                                  </div>
                              </Card>
                          </div>
                      </Col>
                  </Row>
              </div>
          </TabPane>
      </Tabs>


  </section>
</template>

<script>
import WaterPoloChart from "./module/WaterPoloChart";
import RingChart from "./module/RingChart";
import PieChart from './module/PieChart'
import countTo from './module/count-to'
import size from "../../../utils/winSize";
export default {
  name: "sections",
  components: { WaterPoloChart, RingChart, PieChart, countTo },
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
      flowResourceInfo: {},
      groupResourceInfo: {},
      scheduleResourceInfo: {},
      tempalateAndDataSourceResourceInfo: {},
      stopResourceInfo: {},
        percent: 0,
        cpuPercent: 0,
        memoryPercent: 0,
        hdfsPercent: 0,
    };
  },
    computed: {
        color () {
            // let color = '#2db7f5';
            let color = '#5cb85c';
            if (this.percent == 100) {
                color = '#5cb85c';
            }
            return color;
        }
    },
  mounted() {
    this.height = size.PageH - 500;
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
                  // this.resource.cpu.parameter = 'cpu';
                  this.resource.memory = data.resource.memory;
                  // this.resource.memory.parameter = 'memory';
                  this.resource.hdfs = data.resource.hdfs;
                  // this.resource.hdfs.parameter = 'hdfs';

                  this.cpuPercent = data.resource.cpu.allocatedVirtualCores / data.resource.cpu.totalVirtualCores * 100;
                  this.memoryPercent = data.resource.memory.allocatedMemoryGB / data.resource.memory.totalMemoryGB * 100;
                  this.hdfsPercent = data.resource.hdfs.allocatedCapacityGB / data.resource.hdfs.TotalCapacityGB * 100;
                    // console.log(this.resource,'this.resource.cpu','remainingVirtualCores')
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
                  this.flowResourceInfo = data;
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
      this.$axios
              .get("/dashboard/groupStatistic")
              .then(res => {
                if (res.data.code == 200) {
                  let data = res.data.groupResourceInfo;
                    this.groupResourceInfo = data;
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

      // 调度
      this.$axios
              .get("/dashboard/scheduleStatistic")
              .then(res => {
                if (res.data.code == 200) {
                    let data = res.data.scheduleResourceInfo;
                    this.scheduleResourceInfo = data;
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

      // 模板和数据源统计
      this.$axios
              .get("/dashboard/templateAndDataSourceStatistic")
              .then(res => {
                if (res.data.code == 200) {
                    let data = res.data.tempalateAndDataSourceResourceInfo;
                    this.tempalateAndDataSourceResourceInfo = data;
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

      // 组件统计
      this.$axios
              .get("/dashboard/stopStatistic")
              .then(res => {
                if (res.data.code == 200) {
                    let data = res.data.stopResourceInfo;
                    this.stopResourceInfo = data;
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
  }
};
</script>
<style lang="scss" scoped>
@import "./index.scss";
  .aaa li p{
    /*font-weight: 400;*/
    /*  line-height: 22px;*/
      color: rgb(102, 102, 102);
      font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Fira Sans", "Droid Sans", "Helvetica Neue", sans-serif;
  }
.aaa li {
    /*margin: 10px 0;*/
    font-family: "微软雅黑";
     }
.aaa li:nth-child(1){
    margin-top: 0;
}
    .ContentQuantity{
        width: 100%;
        height: 100%;
        /*min-height: 18.75rem;*/
        min-height: 0.75rem;
        transform: scale(.97);
        background-color: #fff;
        /*padding-top: 2.5rem;*/
        /*padding-bottom: 2.5rem;*/
        /*padding-left: .9375rem;*/
        /*padding-right: .9375rem;*/
        box-sizing: border-box;
        float: left;
        /*margin: .9375rem;*/
        border-radius: .25rem;
        box-shadow: 0 0.125rem 0.4375rem 0 rgba(0,0,0,.2);
    }
    .ContentQuantity:hover{
        cursor: pointer;
        transition: 1s;
        transform: scale(1,1);
        -ms-transform:scale(1,1);     /* IE 9 */
        -moz-transform:scale(1,1);     /* Firefox */
        -webkit-transform:scale(1,1); /* Safari 和 Chrome */
        -o-transform:scale(1,1);
    }
.demo-Circle-custom{
    & h1{
        color: #3f414d;
        font-size: 28px;
        font-weight: normal;
    }
    & p{
        color: #657180;
        font-size: 14px;
        margin: 10px 0 15px;
    }
    & span{
        display: block;
        padding-top: 15px;
        color: #657180;
        font-size: 14px;
        &:before{
            content: '';
            display: block;
            width: 50px;
            height: 1px;
            margin: 0 auto;
            background: #e0e3e6;
            position: relative;
            top: -15px;
        };
    }
    & span i{
        font-style: normal;
        color: #3f414d;
    }
}
 /deep/ .ivu-tabs-ink-bar {
     background-color: #3da375;
 }
/deep/ .ivu-tabs-nav .ivu-tabs-tab-active {
    color: #515a6e;
    font-size: 16px;
    font-weight: 600;
    padding-left: 2px;
}
    .statisticsImg{
        width: 100px;
        height: 100px;
        display: inline-block;
        margin: 20px;
        padding: 20px;
        float: left;
        background: rgb(232, 236, 238);
        border-radius: 15%;
        img {
            width: 100%;
            height: 100%;
        }
    }
    .SIRound{
        border-radius: 50%;
    }
    .SITilt{
        transform: rotateZ(25deg)
    }
    .textIndex{
        text-indent: 20px;
    }
    .SICutCorner{
        transform: rotateZ(-15deg);
        @mixin beveled-corners($bg, $tl:0, $tr:$tl, $br:$tl, $bl:$tr) { background: $bg; background: linear-gradient(135deg, transparent $tl, $bg 0) top left, linear-gradient(225deg, transparent $tr, $bg 0) top right, linear-gradient(-45deg, transparent $br, $bg 0) bottom right, linear-gradient(45deg, transparent $bl, $bg 0) bottom left; background-size: 50% 50%; background-repeat: no-repeat;}
        @include beveled-corners(#e8ecee, 15px, 5px);
    }
    .SIRrdial{
        transform: rotateZ(8deg);
        background: radial-gradient(circle at top left, transparent 15px, #e8ecee 0) top left, radial-gradient(circle at top right, transparent 15px, #e8ecee 0) top right, radial-gradient(circle at bottom right, transparent 15px, #e8ecee 0) bottom right, radial-gradient(circle at bottom left, transparent 15px, #e8ecee 0) bottom left;
        background-size: 50% 50%;
        background-repeat: no-repeat;
    }
</style>

