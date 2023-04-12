<template>
  <div class="bootPage">
    <div class="center">
      <div style="text-align: center; font-size: large; color: white;">
        <span id="promptContent">Component loading, please wait...</span>
      </div>
      <br>
      <Progress :percent="percent" :text-inside="true" :stroke-width="28" status="active" :stroke-color="['#58b368', '#0ef5ca']" />
    </div>
  </div>
</template>

<script>
export default {
  name: "bootPage",
  data:()=>{
    return {
      percent: 20,
      loadComponents_timer: null,
    }
  },
  mounted(){
    this.getIsInBootPage();
  },
  methods: {
    getInitComponents() {
      this.$axios
          .get("/bootPage/initComponents")
          .then((res) => {
            var dataMap = res.data;
            if (dataMap.code === 200) {
              this.loadComponents_timer = window.setInterval(()=>{
                this.GetThreadMonitoring()
              },500);
            }
            else {
              this.$Modal.success({
                title: this.$t("tip.title"),
                content: this.$t("tip.request_fail_content"),
              });
            }
          })
          .catch((error) => {
            console.log(error);
          });
    },

    GetThreadMonitoring() {
      this.$axios
          .get("/bootPage/threadMonitoring")
          .then((res) => {
            var dataMap = res.data;
            if (200 === dataMap.code) {
              this.percent = dataMap.progress;
              if (this.percent === 100){
                window.clearInterval(this.loadComponents_timer);
                setTimeout(()=>{
                  this.$router.push({
                    name: 'sections',
                    path: '/'
                  })
                },300)
              }else if (typeof this.percent === 'number' && this.percent<100){
                this.GetThreadMonitoring();
              }
            }else if (500 === dataMap.code || !dataMap.progress){
              this.getInitComponents();
            } else {
              this.$Modal.success({
                title: this.$t("tip.title"),
                content: this.$t("tip.request_fail_content"),
              });
            }
          })
          .catch((error) => {
            console.log(error);
          });
    },

    getIsInBootPage(){
      this.$axios
          .get("/bootPage/isInBootPage")
          .then((res) => {
            var dataMap = res.data;
            if (dataMap.code === 200 && dataMap.isIn === true) {
              this.GetThreadMonitoring();
            }else if (dataMap.code === 200 && dataMap.isIn === false){
              this.$router.push({
                name: 'sections',
                path: '/'
              })
            } else {
              this.$Modal.success({
                title: this.$t("tip.title"),
                content: this.$t("tip.request_fail_content"),
              });
            }
          })
          .catch((error) => {
            console.log(error);
          });
    }
  }
}
</script>

<style lang="scss" scoped>
    ::v-deep .ivu-progress-inner-text{
        font-size: 14px;
    }
    .center{
      position: fixed;
      top: 45%;
      left: 0;
      right: 0;
      padding: 0 10%;
    }
    .bootPage{
        height: 100vh;
        width: 100vw;
        background: black;
    }
</style>