<template>
    <div class="bootPage">
        <div class="center">
            <div style="text-align: center; font-size: large; color: aliceblue;">
                <span id="promptContent">Component loading, please wait...</span>
            </div>
            <br>
            <Progress :percent="percent" :text-inside="true" :stroke-width="25" status="active" :stroke-color="['#58b368', '#0ef5ca']" />
        </div>
    </div>
</template>

<script>
    export default {
        name: "bootPage",
        data:()=>{
          return {
              percent: 0,
          }
        },
        mounted(){
            this.getLoadingProgress();
            this.loading();
            this.aaa();
            setInterval(()=>{
                if (this.percent <= 55){
                    this.percent += 1
                }
            },100)
        },
        methods: {
            //加载进度
            getLoadingProgress() {
                this.$event.emit("looding", true);
                this.$axios
                    .get("/bootPage/initComponents")
                    .then((res) => {
                        console.log(res);
                        // this.$event.emit("looding", false);
                        // var dataMap = JSON.parse(res.data);
                        // if (200 === dataMap.code) {
                        //     // loadComponents_timer = window.setInterval("loading()", 500);
                        // }
                        // else {
                        //     this.$Modal.success({
                        //         title: this.$t("tip.title"),
                        //         content: this.$t("tip.request_fail_content"),
                        //     });
                        // }
                    })
                    .catch((error) => {
                        console.log(error);
                    });
            },
            loading() {
                this.$axios
                    .get("/bootPage/threadMonitoring")
                    .then((res) => {
                        this.$event.emit("looding", false);
                        console.log(res);
                        // var dataMap = JSON.parse(res.data);
                        // if (200 === dataMap.code) {
                        //     // loadComponents_timer = window.setInterval("loading()", 500);
                        // }
                        // else {
                        //     this.$Modal.success({
                        //         title: this.$t("tip.title"),
                        //         content: this.$t("tip.request_fail_content"),
                        //     });
                        // }
                    })
                    .catch((error) => {
                        console.log(error);
                    });
            },
            aaa(){
                let data = { page: 1, limit: 10 };
                this.$axios
                    .get("/flow/getFlowListPage", {
                        params: data,
                    })
                    .then((res) => {
                        if (res.data.code == 200) {
                            this.tableData = res.data.data;
                            this.total = res.data.count;
                        } else {
                            this.$Modal.error({
                                title: this.$t("tip.title"),
                                content: this.$t("tip.request_fail_content"),
                            });
                        }
                    })
                    .catch((error) => {
                        console.log(error);
                        this.$Modal.error({
                            title: this.$t("tip.title"),
                            content: this.$t("tip.fault_content"),
                        });
                    });
            }
        }
    }
</script>

<style lang="scss" scoped>
    /deep/ .ivu-progress-inner{
        background: url("/img/ProgressBar.e67d85a8.gif") center no-repeat;
        background-size: 136% 135%;
    }
    /deep/ .ivu-progress-inner-text{
        font-size: 14px;
    }
    .center{
        position: fixed;
        top: 40%;
        left: 0;
        right: 0;
        padding: 0 20px;
    }
    .bootPage{
        height: 100vh;
        width: 100vw;
        /*background: black;*/
        background: rgba(0, 0, 0, 0.8);
    }
</style>