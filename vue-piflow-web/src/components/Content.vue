<template>
  <div class="warp" :style="style">
    <Sidebar :width="width"></Sidebar>
    <div class="content-right">
        <router-view></router-view>
    </div>
  </div>
</template>
<script>
import Sidebar from "./compon/Sidebar";
export default {
  name: "contents",
  components: {
    Sidebar
  },
  data() {
    return {
      style: {
        height: "auto"
      },
      width: "250px"
    };
  },
  created() {
    window.addEventListener("resize", () => {
      this.setSize();
    });
  },
  mounted() {
    this.setSize();
  },
  methods: {
    setSize() {
      let headerHeight = document.querySelector("header").offsetHeight;
      let bodyHeight = document.querySelector("body").offsetHeight;
      let footerHeight = document.querySelector("footer").offsetHeight;
      this.style.height = bodyHeight - headerHeight - footerHeight + "px";
    }
  }
};
</script>

<style lang="scss" scoped>
.warp {
  height: 100%;
  display: flex;
  .content-right {
    flex-grow: 1;
    max-width: calc(100% - 250px);
    border-left: 1px solid #ddd;
    margin-left: 2px;
    overflow: auto;
  }
}
</style>





