<template>
  <footer>
    <Breadcrumb id="BreadcrumbFlow">
      <div @click="handleClick('flow')">11111</div>
      <BreadcrumbItem id="FlowList" @click="handleClick('flow')" :to="'#'">Flow</BreadcrumbItem>
      <BreadcrumbItem id="FlowBreadcrumbItem" :to="'#'">drawingBoard</BreadcrumbItem>

<!--      <BreadcrumbItem v-for="(item,i) in breadcrumb" :key="'f'+i" :to="'#'">{{item.name}}</BreadcrumbItem>-->
      <!-- <BreadcrumbItem to="/components/breadcrumb">Components</BreadcrumbItem>
      <BreadcrumbItem>Breadcrumb</BreadcrumbItem>-->
    </Breadcrumb>
    <Breadcrumb id="BreadcrumbGroup">
      <BreadcrumbItem id="GroupList" @click="handleClick('group')">Group</BreadcrumbItem>
      <BreadcrumbItem id="GroupParents" :to="'#'">flowGroupList</BreadcrumbItem>
      <BreadcrumbItem id="GroupBreadcrumbItem" :to="'#'">drawingBoard</BreadcrumbItem>
    </Breadcrumb>
    <Breadcrumb id="BreadcrumbProcess">
      <BreadcrumbItem id="ProcessList" @click="handleClick('process')">Process</BreadcrumbItem>
      <BreadcrumbItem id="ProcessParents" :to="'#'">ProcessList</BreadcrumbItem>
      <BreadcrumbItem id="ProcessBreadcrumbItem" :to="'#'">drawingBoard</BreadcrumbItem>
    </Breadcrumb>
  </footer>
</template>

<script>
export default {
  name: "footers",
  data() {
    return {
      breadcrumb: [],
    };
  },
  created() {
    let breadcrumb = window.sessionStorage.getItem("breadcrumb");
    if (breadcrumb) {
      this.breadcrumb = JSON.parse(breadcrumb);
    }
  },
  mounted() {
    this.$event.on("crumb", (arr) => {
      this.breadcrumb = arr;
      window.sessionStorage.setItem("breadcrumb", JSON.stringify(arr));
    });
  },
  methods:{
    handleClick(val){
      console.log(val)
      switch (val) {
        case 'flow':
          this.$router.push({
            path: "/flow",
            // query: {
            //   src: "/drawingBoard/page/flow/mxGraph/index.html?load=" + row.id,
            // }
          });
          document.getElementById('BreadcrumbFlow').style.display = 'none';
          break;
          case 'group':
          break;
          case 'process':
          break;
      }
    }
  },
  beforeDestroy() {
    window.sessionStorage.removeItem("breadcrumb");
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
  #BreadcrumbFlow, #BreadcrumbGroup, #BreadcrumbProcess{
    display: none;
  }
}
</style>