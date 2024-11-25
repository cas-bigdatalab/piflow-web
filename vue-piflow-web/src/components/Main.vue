
<template>
  <div class="main">
    <Header :width="width"></Header>
    <router-view></router-view>
    <Footer />
    <transition name="show">
      <div class="fullScreen" v-if="loading">
        <div style="margin-top: 15%;">
          <p><img src="../assets/img/loading.gif" /></p>
        </div>
      </div>
    </transition>
  </div>
</template>
<script>
import Header from "./compon/Header";
import Footer from "./compon/Footer";
import Cookies from "js-cookie";
export default {
  name: "mains",
  components: {
    Footer,
    Header
  },
  data() {
    return {
      loading: false,
      width: "250px"
    };
  },
  created() {
    this.$event.on("loading", val => {
      this.loading = val;
    });
    if(Cookies.get('changePsd') == 1){
      this.$event.emit('changePsd')
    }

  }
};
</script>

<style lang="scss" scoped>
.main {
  height: 100%;
}
</style>