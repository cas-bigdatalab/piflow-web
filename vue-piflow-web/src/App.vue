<template>
  <div id="app">
    <transition name="show">
      <router-view/>
    </transition>
  </div>
</template>

<script>
export default {
  name: "app",
  mounted() {
    //*****************************解决刷新页面数据丢失开始**************************************** */
    if (sessionStorage.getItem("store")) {
      this.$store.replaceState(
        Object.assign(
          {},
          this.$store.state,
          JSON.parse(sessionStorage.getItem("store"))
        )
      );
      sessionStorage.removeItem("store");
    }
    //在页面刷新时将vuex里的信息保存到sessionStorage里
    window.addEventListener("beforeunload", () => {
      sessionStorage.setItem("store", JSON.stringify(this.$store.state));
    });
  }
};
</script>

