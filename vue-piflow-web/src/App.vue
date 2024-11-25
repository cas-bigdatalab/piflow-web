<template>
  <div id="app">
    <transition name="show">
      <router-view/>
    </transition>
       <passwordModal ref="passwordModalRef" :closeAble="false"/>
  </div>
</template>

<script>
import passwordModal from '@/components/pages/User/passwordModal.vue';
export default {
  name: "app",
  components:{ passwordModal },
  created() {
    this.$event.on("changePsd", val => {
      this.openPasswordModal()
    });

  },
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
  },
  methods: {
    openPasswordModal(){
        this.$refs.passwordModalRef.handleOpen()
    }
  },
};
</script>

