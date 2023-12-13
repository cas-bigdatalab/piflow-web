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
    </Breadcrumb>

    <Breadcrumb id="BreadcrumbGroup">
      <span id="GroupList" class="spanPointer" @click="handleClick('group')">Group
        <span style="margin: 0 5px;display: inline-block">&gt;&gt;</span>
      </span>
      <a id="GroupParents" href="#" class="spanPointer" style="display: none">flowGroupList
        <span style="margin: 0 5px;display: inline-block">&gt;&gt;</span>
      </a>
      <span id="GroupBreadcrumbItem" style="font-weight: bold">drawingBoard</span>
    </Breadcrumb>

    <Breadcrumb id="BreadcrumbProcess">
       <span id="ProcessList" class="spanPointer" @click="handleClick('processes')">processes
        <span style="margin: 0 5px;display: inline-block">&gt;&gt;</span>
      </span>
      <a id="ProcessParents" href="#" class="spanPointer" style="display: none" @click="handleClick('ProcessList')">ProcessList
        <span style="margin: 0 5px;display: inline-block">&gt;&gt;</span>
      </a>
      <span id="ProcessBreadcrumbItem" style="font-weight: bold">drawingBoard</span>
    </Breadcrumb>

    <Breadcrumb id="BreadcrumbProcessGroup">
       <span id="ProcessGroupList" class="spanPointer" @click="handleClick('processes')">processes
        <span style="margin: 0 5px;display: inline-block">&gt;&gt;</span>
      </span>
      <a id="ProcessGroupParents" href="#" class="spanPointer" style="display: none">ProcessList
        <span style="margin: 0 5px;display: inline-block">&gt;&gt;</span>
      </a>
      <span id="ProcessGroupBreadcrumbItem" style="font-weight: bold">drawingBoard</span>
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
  methods:{
    handleClick(val){
      switch (val) {
        case 'flow':
          window.sessionStorage.setItem("menuName", 'flow');
          this.$router.push({
            name: 'flow',
            path: "/flow"
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
  }
};
</script>

<style lang="scss" scoped>
footer {
  position: absolute;
  background: var(--primary-color);
  height: 42px;
  width: 100%;
  padding: 0 20px;
  border-top: 4px solid gainsboro;
  color: #fff;
  z-index: 100;
  padding: 8px 0;
  display: flex;
  justify-content: center;
  ::v-deep .ivu-breadcrumb a,
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