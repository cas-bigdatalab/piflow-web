<template>
  <footer>
    <Breadcrumb>
      <BreadcrumbItem v-for="(item,i) in breadcrumb" :key="'f'+i" :to="item.path">{{item.name}}</BreadcrumbItem>
      <!-- <BreadcrumbItem to="/components/breadcrumb">Components</BreadcrumbItem>
      <BreadcrumbItem>Breadcrumb</BreadcrumbItem>-->
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
}
</style>