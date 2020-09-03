<template>
  <footer>
    <Breadcrumb id="BreadcrumbFlow">
      <span class="spanPointer" @click="handleClick('flow')">Flow
        <span style="margin: 0 5px;display: inline-block">&gt;&gt;</span>
      </span>
      <a class="spanPointer" id="FlowParents" href="#" style="display: none" @click="handleClick('FlowParents')">flowGroupList
        <span style="margin: 0 5px;display: inline-block">&gt;&gt;</span>
      </a>
      <span style="font-weight: bold">drawingBoard</span>
<!--      <BreadcrumbItem id="FlowList" @click="handleClick('flow')" :to="'#'">Flow</BreadcrumbItem>-->
<!--      <BreadcrumbItem id="FlowBreadcrumbItem" :to="'#'">drawingBoard</BreadcrumbItem>-->

<!--      <BreadcrumbItem v-for="(item,i) in breadcrumb" :key="'f'+i" :to="'#'">{{item.name}}</BreadcrumbItem>-->
      <!-- <BreadcrumbItem to="/components/breadcrumb">Components</BreadcrumbItem>
      <BreadcrumbItem>Breadcrumb</BreadcrumbItem>-->
    </Breadcrumb>

    <Breadcrumb id="BreadcrumbGroup">
      <span id="GroupList" class="spanPointer" @click="handleClick('group')">Group
        <span style="margin: 0 5px;display: inline-block">&gt;&gt;</span>
      </span>
      <a id="GroupParents" href="#" class="spanPointer" style="display: none">flowGroupList
        <span style="margin: 0 5px;display: inline-block">&gt;&gt;</span>
      </a>
      <span id="GroupBreadcrumbItem" style="font-weight: bold">drawingBoard</span>

<!--      <BreadcrumbItem id="GroupList" @click="handleClick('group')">Group</BreadcrumbItem>-->
<!--      <BreadcrumbItem id="GroupParents" to="/drawingBoard?src=/drawingBoard/page/flowGroup/mxGraph/index.html?drawingBoardType=GROUP&parentAccessPath=flowGroupList&load=8a80d5d27442608401744275e09e0000">flowGroupList</BreadcrumbItem>-->
<!--      <BreadcrumbItem id="GroupBreadcrumbItem" :to="'#'">drawingBoard</BreadcrumbItem>-->
    </Breadcrumb>

    <Breadcrumb id="BreadcrumbProcess">
       <span id="ProcessList" class="spanPointer" @click="handleClick('processes')">processes
        <span style="margin: 0 5px;display: inline-block">&gt;&gt;</span>
      </span>
      <a id="ProcessParents" href="#" class="spanPointer" style="display: none" @click="handleClick('ProcessList')">ProcessList
        <span style="margin: 0 5px;display: inline-block">&gt;&gt;</span>
      </a>
      <span id="ProcessBreadcrumbItem" style="font-weight: bold">drawingBoard</span>

<!--      <BreadcrumbItem id="ProcessList" @click="handleClick('process')">Process</BreadcrumbItem>-->
<!--      <BreadcrumbItem id="ProcessParents" :to="'#'">ProcessList</BreadcrumbItem>-->
<!--      <BreadcrumbItem id="ProcessBreadcrumbItem" :to="'#'">drawingBoard</BreadcrumbItem>-->
    </Breadcrumb>

    <Breadcrumb id="BreadcrumbProcessGroup">
       <span id="ProcessGroupList" class="spanPointer" @click="handleClick('processes')">processes
        <span style="margin: 0 5px;display: inline-block">&gt;&gt;</span>
      </span>
      <a id="ProcessGroupParents" href="#" class="spanPointer" style="display: none">ProcessList
        <span style="margin: 0 5px;display: inline-block">&gt;&gt;</span>
      </a>
      <span id="ProcessGroupBreadcrumbItem" style="font-weight: bold">drawingBoard</span>

<!--      <BreadcrumbItem id="ProcessList" @click="handleClick('process')">Process</BreadcrumbItem>-->
<!--      <BreadcrumbItem id="ProcessParents" :to="'#'">ProcessList</BreadcrumbItem>-->
<!--      <BreadcrumbItem id="ProcessBreadcrumbItem" :to="'#'">drawingBoard</BreadcrumbItem>-->
    </Breadcrumb>

    <Breadcrumb id="BreadcrumbSchedule">
      <span class="spanPointer" @click="handleClick('Schedule')">Schedule
        <span style="margin: 0 5px;display: inline-block">&gt;&gt;</span>
      </span>
      <span style="font-weight: bold">drawingBoard</span>
    </Breadcrumb>
  </footer>
</template>

<script>
export default {
  name: "footers",
  data() {
    return {
      // breadcrumb: [],
    };
  },
  created() {
    // let breadcrumb = window.sessionStorage.getItem("breadcrumb");
    // if (breadcrumb) {
    //   this.breadcrumb = JSON.parse(breadcrumb);
    // }
  },
  mounted() {
    // this.$event.on("crumb", (arr) => {
    //   this.breadcrumb = arr;
    //   window.sessionStorage.setItem("breadcrumb", JSON.stringify(arr));
    // });
  },
  methods:{
    handleClick(val){
      switch (val) {
        case 'flow':
          window.sessionStorage.setItem("menuName", 'flow');
          this.$router.push({
            name: 'flow',
            path: "/flow",
            // query: {
            //   src: "/drawingBoard/page/flow/mxGraph/index.html?load=" + row.id,
            // }
          });
          this.$nextTick(()=>{
            document.getElementById('BreadcrumbFlow').style.display = 'none';
          });
          break;
          case 'group':
            window.sessionStorage.setItem("menuName", 'group');
            this.$router.push({
              path: '/group',
              name: 'group',
            });
            this.$nextTick(()=>{
              document.getElementById('BreadcrumbGroup').style.display = 'none';
            });
          break;
          case 'processes':
            window.sessionStorage.setItem("menuName", 'processes');
            this.$router.push({
              name: 'processes',
              path: "/processes",
            });
            this.$nextTick(()=>{
              document.getElementById('BreadcrumbProcess').style.display = 'none';
              document.getElementById('BreadcrumbProcessGroup').style.display = 'none';
            });
          break;
        case 'Schedule':
          window.sessionStorage.setItem("menuName", 'schedule');
          this.$router.push({
            path: '/schedule',
            name: 'schedule',
          });
          this.$nextTick(()=>{
            document.getElementById('BreadcrumbSchedule').style.display = 'none';
          });
          break;
      }
    }
  },
  beforeDestroy() {
    // window.sessionStorage.removeItem("breadcrumb");
    // window.parent.postMessage(false);
  },
};
</script>

<style lang="scss" scoped>
footer {
  position: absolute;
  background: #20784b;
  height: 42px;
  width: 100%;
  padding: 0 20px;
  border-top: 4px solid gainsboro;
  color: #fff;
  z-index: 100;
  padding: 8px 0;
  display: flex;
  justify-content: center;
  /deep/ .ivu-breadcrumb a,
  .ivu-breadcrumb > span:last-child {
    color: #fff;
  }
  #BreadcrumbFlow, #BreadcrumbGroup, #BreadcrumbProcess, #BreadcrumbProcessGroup, #BreadcrumbSchedule{
    display: none;
    span, a{
      color: #ffffff
    }
  .spanPointer{
    cursor: pointer;
  }
  }
}
</style>