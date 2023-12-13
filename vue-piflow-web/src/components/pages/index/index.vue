<template>
  <section>
    <Tabs value="name1">
      <TabPane :label="$t('homeInfo.introduction_title')" name="name1">
        <Card shadow class="Introduction janeCard ">
          {{$t('homeInfo.introduction_Info')}}
        </Card>
      </TabPane>
    </Tabs>
    <Tabs value="name1">
      <TabPane :label="$t('homeInfo.monitor_title')" name="name1">
        <Row :gutter="16" type="flex" justify="center" style="margin-bottom: 20px">
          <Col span="6">
            <Card>
              <h3 class="alignLeft">{{$t('homeInfo.CPU_Disk')}}</h3>
              <i-circle
                  :size="150"
                  :trail-width="6"
                  :stroke-width="8"
                  :percent="cpuPercent"
                  stroke-linecap="square"
                  :stroke-color="color">
                <div class="demo-Circle-custom">
                  <h3>{{resource.cpu.totalVirtualCores}}{{$t('index.cpu')}}</h3>
                  <p>{{$t('homeInfo.totalCapacity')}}</p>
                  <span>{{$t('homeInfo.Used')}}<i>{{ cpuPercent }}%</i></span>
                </div>
              </i-circle>
            </Card>
          </Col>
          <Col span="6" style=" margin: 0 4vw ">
            <Card>
              <h3 class="alignLeft">{{$t('homeInfo.Memory_Disk')}}</h3>
              <i-circle
                  :size="150"
                  :trail-width="6"
                  :stroke-width="8"
                  :percent="memoryPercent"
                  stroke-linecap="square"
                  :stroke-color="color">
                <div class="demo-Circle-custom">
                  <h3>{{resource.memory.totalMemoryGB}}GB</h3>
                  <p>{{$t('homeInfo.totalCapacity')}}</p>
                  <span>{{$t('homeInfo.Used')}}<i>{{ memoryPercent }}%</i></span>
                </div>
              </i-circle>
            </Card>
          </Col>
          <Col span="6">
            <Card>
              <h3 class="alignLeft">{{$t('homeInfo.HDFS_Disk')}}</h3>
              <i-circle
                  :size="150"
                  :trail-width="6"
                  :stroke-width="8"
                  :percent="hdfsPercent"
                  stroke-linecap="square"
                  :stroke-color="color">
                <div class="demo-Circle-custom">
                  <h3>{{resource.hdfs.TotalCapacityGB}}GB</h3>
                  <p>{{$t('homeInfo.totalCapacity')}}</p>
                  <span>{{$t('homeInfo.Used')}}<i>{{ Number(hdfsPercent.toFixed(2)) }}%</i></span>
                </div>
              </i-circle>
            </Card>
          </Col>
        </Row>
      </TabPane>
      </Tabs>
    <Tabs value="name1">
      <TabPane :label="$t('homeInfo.statistics_title')" name="name1">
        <Row :gutter="16" style="margin-bottom: 20px">
          <Col span="8" style="height: 350px" >
            <div class="ContentQuantity">
              <div class="statisticsImg SICutCorner">
                <img src="../../../assets/img/flow.png" alt="flow" />
              </div>
              <ul class="textArea">
                <li><p>Flow：
                  <countTo :EndVal = Number(flowResourceInfo.FLOW_COUNT)></countTo>
                </p></li>
                <li><p>Processor：
                  <countTo :EndVal = Number(flowResourceInfo.PROCESSOR_COUNT)></countTo>
                </p></li>
                <li><p class="textIndex">Started：
                  <countTo :EndVal = Number(flowResourceInfo.PROCESSOR_STARTED_COUNT)></countTo>
                </p></li>
                <li><p class="textIndex">Competed：
                  <countTo :EndVal = Number(flowResourceInfo.PROCESSOR_COMPETED_COUNT)></countTo>
                </p></li>
                <li><p class="textIndex">Failed：
                  <countTo :EndVal = Number(flowResourceInfo.PROCESSOR_FAILED_COUNT)></countTo>
                </p></li>
                <li><p class="textIndex">Killed：
                  <countTo :EndVal = Number(flowResourceInfo.PROCESSOR_KILLED_COUNT)></countTo>
                </p></li>
                <li><p class="textIndex">Other：
                  <countTo :EndVal = Number(flowResourceInfo.PROCESSOR_OTHER_COUNT)></countTo>
                </p></li>
              </ul>
              <div class="leftTop20">
                <Card class="Introduction cardStyle" shadow>
                  {{$t('homeInfo.flowStatistics')}}
                </Card>
              </div>
            </div>
          </Col>
          <Col span="8" style="height: 270px">
            <div class="ContentQuantity">
              <div class="statisticsImg SIRound">
                <img src="../../../assets/img/schedule.png" alt="SCHEDULE" />
              </div>
              <ul class="textArea">
                <li><p>Schedule：
                  <countTo :EndVal = Number(scheduleResourceInfo.SCHEDULE_COUNT)></countTo>
                </p></li>
                <li><p class="textIndex">Init：
                  <countTo :EndVal = Number(scheduleResourceInfo.SCHEDULE_INIT_COUNT)></countTo>
                </p></li>
                <li><p class="textIndex">Running：
                  <countTo :EndVal = Number(scheduleResourceInfo.SCHEDULE_RUNNING_COUNT)></countTo>
                </p></li>
                <li><p class="textIndex">Stop：
                  <countTo :EndVal = Number(scheduleResourceInfo.SCHEDULE_STOP_COUNT)></countTo>
                </p></li>
              </ul>
              <div class="leftTop20">
                <Card class="Introduction cardStyle" shadow>
                  {{$t('homeInfo.scheduleStatistics')}}
                </Card>
              </div>
            </div>
          </Col>
          <Col span="8" style="height: 340px">
            <div class="ContentQuantity">
              <div class="statisticsImg SIRrdial">
                <img src="../../../assets/img/group.png" alt="group" />
              </div>
              <ul class="textArea">
                <li><p>Group：
                  <countTo :EndVal = Number(groupResourceInfo.GROUP_COUNT)></countTo>
                </p></li>
                <li><p>Processor：
                  <countTo :EndVal = Number(groupResourceInfo.PROCESSOR_COUNT)></countTo>
                </p></li>
                <li><p class="textIndex">Started：
                  <countTo :EndVal = Number(groupResourceInfo.PROCESSOR_STARTED_COUNT)></countTo>
                </p></li>
                <li><p class="textIndex">Competed：
                  <countTo :EndVal = Number(groupResourceInfo.PROCESSOR_COMPETED_COUNT)></countTo>
                </p></li>
                <li><p class="textIndex">Failed：
                  <countTo :EndVal = Number(groupResourceInfo.PROCESSOR_FAILED_COUNT)></countTo>
                </p></li>
                <li><p class="textIndex">Killed：
                  <countTo :EndVal = Number(groupResourceInfo.PROCESSOR_KILLED_COUNT)></countTo>
                </p></li>
                <li><p class="textIndex">Other：
                  <countTo :EndVal = Number(groupResourceInfo.PROCESSOR_OTHER_COUNT)></countTo>
                </p></li>
              </ul>
              <div class="leftTop20">
                <Card class="Introduction cardStyle" shadow>
                  {{$t('homeInfo.groupStatistics')}}
                </Card>
              </div>
            </div>
          </Col>
        </Row>
        <Row :gutter="16">
          <Col span="8" style="visibility: hidden">
            <div class="ContentQuantity"></div>
          </Col>
          <Col style="margin-top: -80px;height: 290px" span="8">
            <div class="ContentQuantity">
              <div class="statisticsImg SITilt">
                <img src="../../../assets/img/others.png" alt="others" />
              </div>
              <ul class="textArea">
                <li><p>Datasource：
                  <countTo :EndVal = Number(templateAndDataSourceResourceInfo.DATASOURCE_COUNT)></countTo>
                </p></li>
                <li><p>Stopshub：
                  <countTo :EndVal = Number(templateAndDataSourceResourceInfo.STOPSHUB_COUNT)></countTo>
                </p></li>
                <li><p>Template：
                  <countTo :EndVal = Number(templateAndDataSourceResourceInfo.TEMPLATE_COUNT)></countTo>
                </p></li>
              </ul>
              <div class="leftTop20">
                <Card class="Introduction cardStyle" shadow>
                  {{$t('homeInfo.OtherStatistics')}}
                </Card>
              </div>
            </div>
          </Col>
          <Col style="margin-top: -20px;height: 280px" span="8">
            <div class="ContentQuantity">
              <div class="statisticsImg SIRound">
                <img src="../../../assets/img/stop.png" alt="STOP" />
              </div>
              <ul class="textArea">
                <li><p>Stop：
                  <countTo :EndVal = Number(stopResourceInfo.STOP_COUNT)></countTo>
                </p></li>
                <li><p>Stopgroup：
                  <countTo :EndVal = Number(stopResourceInfo.STOPGROUP_COUNT)></countTo>
                </p></li>
              </ul>
              <div class="leftTop20">
                <Card class="Introduction cardStyle" shadow>
                  {{$t('homeInfo.ComponentStatistics')}}
                </Card>
              </div>
            </div>
          </Col>
        </Row>
      </TabPane>
    </Tabs>
  </section>
</template>

<script>
import countTo from './module/count-to'
import size from "../../../utils/winSize";
export default {
  name: "sections",
  components: { countTo },
  props: {
    msg: String
  },
  data() {
    return {
      height: 500,
      resource: {
        cpu: {
            totalVirtualCores: 0
        },
        memory: {
            totalMemoryGB: 0
        },
        hdfs: {
            TotalCapacityGB: 0
        },
      },
      flowResourceInfo: {
          FLOW_COUNT: 0,
          PROCESSOR_COUNT: 0,
          PROCESSOR_STARTED_COUNT: 0,
          PROCESSOR_COMPETED_COUNT: 0,
          PROCESSOR_FAILED_COUNT: 0,
          PROCESSOR_KILLED_COUNT: 0,
          PROCESSOR_OTHER_COUNT: 0,
      },
      groupResourceInfo: {
          GROUP_COUNT: 0,
          PROCESSOR_COUNT: 0,
          PROCESSOR_STARTED_COUNT: 0,
          PROCESSOR_COMPETED_COUNT: 0,
          PROCESSOR_FAILED_COUNT: 0,
          PROCESSOR_KILLED_COUNT: 0,
          PROCESSOR_OTHER_COUNT: 0,
      },
      scheduleResourceInfo: {
          SCHEDULE_COUNT: 0,
          SCHEDULE_INIT_COUNT: 0,
          SCHEDULE_RUNNING_COUNT: 0,
          SCHEDULE_STOP_COUNT: 0,
      },
        templateAndDataSourceResourceInfo: {
          DATASOURCE_COUNT: 0,
          STOPSHUB_COUNT: 0,
          TEMPLATE_COUNT: 0,
      },
      stopResourceInfo: {
          STOP_COUNT: 0,
          STOPGROUP_COUNT: 0,
      },
        percent: 0,
        cpuPercent: 0,
        memoryPercent: 0,
        hdfsPercent: 0,
    };
  },
  computed: {
    color () {
      let color = '#5cb85c';
      if (this.percent === 100) {
        color = '#5cb85c';
      }
      return color;
    }
  },
  mounted() {
    this.height = size.PageH - 500;
    this.getResources();
    this.getStatistics ();
  },
  methods:{
    getResources(){
      this.$axios
              .get("/dashboard/resource")
              .then(res => {
                if (res.data.code === 200) {
                  let data = JSON.parse(res.data.resourceInfo);
                  this.resource.cpu = data.resource.cpu;
                  this.resource.memory = data.resource.memory;
                  this.resource.hdfs = data.resource.hdfs;

                  this.cpuPercent = data.resource.cpu.allocatedVirtualCores / data.resource.cpu.totalVirtualCores * 100;
                  this.cpuPercent = Number(this.cpuPercent.toFixed(1));
                  this.memoryPercent = data.resource.memory.allocatedMemoryGB / data.resource.memory.totalMemoryGB * 100;
                  this.memoryPercent = Number(this.memoryPercent.toFixed(1));
                  this.hdfsPercent = data.resource.hdfs.allocatedCapacityGB / data.resource.hdfs.TotalCapacityGB * 100;
                  this.hdfsPercent = Number(this.hdfsPercent.toFixed(2));
                  this.$Message.destroy();
                }
              })
              .catch(error => {
                console.log(error);
                this.$Message.error({
                  content: this.$t("tip.fault_content"),
                  duration: 3
                });
              });
  },

    getStatistics(){
      this.$axios
          .get("/dashboard/flowStatistic")
          .then(res => {
            if (res.data.code === 200) {
              let data = res.data.flowResourceInfo;
              this.flowResourceInfo = data;
            }
          })
          .catch(error => {
            console.log(error);
          });

      this.$axios
          .get("/dashboard/groupStatistic")
          .then(res => {
            if (res.data.code === 200) {
              let data = res.data.groupResourceInfo;
              this.groupResourceInfo = data;
            }
          })
          .catch(error => {
            console.log(error);
          });

      this.$axios
          .get("/dashboard/scheduleStatistic")
          .then(res => {
            if (res.data.code === 200) {
              let data = res.data.scheduleResourceInfo;
              this.scheduleResourceInfo = data;
            }
          })
          .catch(error => {
            console.log(error);
          });

      this.$axios
          .get("/dashboard/templateAndDataSourceStatistic")
          .then(res => {
            if (res.data.code === 200) {
              let data = res.data.templateAndDataSourceResourceInfo;
              this.templateAndDataSourceResourceInfo = data;
            }
          })
          .catch(error => {
            console.log(error);
          });

      this.$axios
          .get("/dashboard/stopStatistic")
          .then(res => {
            if (res.data.code === 200) {
              let data = res.data.stopResourceInfo;
              this.stopResourceInfo = data;
            }
          })
          .catch(error => {
            console.log(error);
          });
    }
  }
};
</script>
<style lang="scss" scoped>
@import "./index.scss";
.textArea{
  float:left;
  width: 50%;
  margin-top: 20px;
  letter-spacing: -.02em;
  li{
    font-family: "微软雅黑";
    p{
      color: rgb(102, 102, 102);
      font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Fira Sans", "Droid Sans", "Helvetica Neue", sans-serif;
    }
  }
  li:nth-child(1){
    margin-top: 0;
  }
}
.ContentQuantity{
  width: 100%;
  height: 100%;
  min-height: 0.75rem;
  transform: scale(.97);
  background-color: #fff;
  box-sizing: border-box;
  float: left;
  border-radius: .25rem;
  box-shadow: 0 0.125rem 0.4375rem 0 rgba(0,0,0,.2);

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
.leftTop20{
  float: left;
  .cardStyle{
    ::v-deep .ivu-card-body{
      float:left;
      background: rgba(241, 241, 246, 0.6);
      margin: 10px;
    }
  }
}
.Introduction{
  width: 100%;
  color: #666666;
  letter-spacing:1px;
  .janeCard{
    background: rgba(241, 241, 246, 0.6);
    margin-bottom: 20px
  }
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
::v-deep .ivu-col-span-6{
  text-align: center;
  .alignLeft{
    text-align: left
  }
}
::v-deep .ivu-tabs-ink-bar {
     background-color: var(--sidebar-color);
 }
::v-deep .ivu-tabs-nav .ivu-tabs-tab-active {
    color: #515a6e;
    font-size: 16px;
    font-weight: 600;
    padding-left: 2px;
}
</style>