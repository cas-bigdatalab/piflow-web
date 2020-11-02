<template>
  <div class="main">
    <Header :width="width"></Header>
    <div class="warp" :style="style">
      <Sidebar :width="width"></Sidebar>
      <div class="content-right">
        <!-- <TagNav></TagNav> -->
        <router-view></router-view>
      </div>
    </div>
    <Footer />
    <div class="fullScreen" v-if="looding">
      <div style="margin-top: 15%;">
        <p>
          <img src="../assets/img/looding.gif" />
        </p>
      </div>
    </div>
  </div>
</template>
<script>
import size from "../utils/winSize";
import Header from "./compon/Header";
import Sidebar from "./compon/Sidebar";
import Footer from "./compon/Footer";
export default {
  name: "mains",
  components: {
    Footer,
    Sidebar,
    // TagNav,
    Header
  },
  data() {
    return {
      looding: false,
      style: {
        height: "auto"
      },
      width: "250px"
    };
  },
  created() {
    //监听窗口变化 自适应（ 根据需求自行添加 ）
    window.addEventListener("resize", () => {
      this.setSize();
    });
    this.$event.on("looding", val => {
      this.looding = val;
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
.main {
  height: 100%;
  .warp {
    display: flex;
    .content-right {
      flex-grow: 1;
      max-width: calc(100% - 250px);
      border-left: 1px solid #ddd;
      margin-left: 2px;
      overflow: auto;
    }
  }
}
</style>