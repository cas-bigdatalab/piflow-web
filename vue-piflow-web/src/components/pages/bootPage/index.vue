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
            //  初始化页面
            getInitComponents() {
                // this.$event.emit("looding", true);
                this.$axios
                    .get("/bootPage/initComponents")
                    .then((res) => {
                        // this.$event.emit("looding", false);
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
            //  获取加载进度
            GetThreadMonitoring() {
                this.$axios
                    .get("/bootPage/threadMonitoring")
                    .then((res) => {
                        // this.$event.emit("looding", false);
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
            //  是否初始化页面
            getIsInBootPage(){
                // this.$event.emit("looding", true);
                this.$axios
                    .get("/bootPage/isInBootPage")
                    .then((res) => {
                        // this.$event.emit("looding", false);
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
    /*/deep/ .ivu-progress-inner{*/
    /*    background: url("/img/ProgressBar.e67d85a8.gif") center no-repeat;*/
    /*    background-size: 136% 135%;*/
    /*}*/
    /deep/ .ivu-progress-inner-text{
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
        /*background: rgba(0, 0, 0, 0.8);*/
    }
</style>