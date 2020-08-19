
<template>
  <div class="main">
    <Header :width="width"></Header>
    <!-- <div class="warp" :style="style"> -->
    <router-view></router-view>
    <!-- </div> -->
    <Footer />
    <transition name="show">
      <div class="fullScreen" v-if="looding">
        <div style="margin-top: 15%;">
          <p>
            <img src="../assets/img/looding.gif" />
          </p>
        </div>
      </div>
    </transition>
  </div>
</template>
<script>
import size from "../utils/winSize";
import Header from "./compon/Header";
import Footer from "./compon/Footer";
export default {
  name: "mains",
  components: {
    Footer,
    Header
  },
  data() {
    return {
      looding: false,
      width: "250px"
    };
  },
  created() {
    this.$event.on("looding", val => {
      this.looding = val;
    });
  }
};
</script>

<style lang="scss" scoped>
.main {
  height: 100%;
}
</style>