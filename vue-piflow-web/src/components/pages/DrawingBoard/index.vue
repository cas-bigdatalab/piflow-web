<template>
  <div :style="{height}">
    <iframe :src="src" id="bariframe" style="width: 100%;height: 100%" frameborder="0"></iframe>
    <img id="piflow-bgc" src="../../../assets/img/hbbj.jpg" />
  </div>
</template>
 
<script>
export default {
  name: "DrawingBoard",
  data() {
    return {
      height: "100%",
      src: "",
      parentsId: ''
    };
  },
  created() {
    // let query = this.$router.currentRoute.query;
    if ( this.src === '' ){
      let baseUrl = window.location.origin;
      let herf = window.location.href.split("src=")[1];
      this.src =
              baseUrl +
              herf
                      .replace(/\%2F+/g, "/")
                      .replace(/\%3F+/g, "?")
                      .replace(/\%3D+/g, "=")
                      .replace(/\%26+/g, "&");
      this.$event.emit("looding", true);
    }
    //监听窗口变化 自适应（ 根据需求自行添加 ）
    window.addEventListener("resize", () => {
      this.setSize();
    });
  },
  mounted() {
    let oIframe = document.querySelector("iframe");
    let piflowBgc = document.querySelector("#piflow-bgc");
    let _this = this;
    if (piflowBgc) {
      if (oIframe.attachEvent) {
        oIframe.attachEvent("onload", () => {
          _this.$event.emit("looding", false);

          piflowBgc.remove();
        });
      } else {
        oIframe.onload = function () {
          _this.$event.emit("looding", false);
          piflowBgc.remove();
        };
      }
    }
    // 动态修改footer样式
    document.querySelector("footer").style.cssText =
      "position: fixed; bottom: 0;";
    document.querySelector("header").style.cssText =
      "position: fixed; width: 100%; top: 0;";
    // console.log(document.querySelector('footer'));
    this.setSize();

    window.addEventListener('message', function(event) {
      // 通过origin属性判断消息来源地址
      // if (event.origin == 'localhost') {
      _this.$event.emit("looding", event.data);
      //console.log(event.source);
      //}
    }, false);
  },
  watch:{
    $route(to,from){
      let baseUrl = window.location.origin;
      let herf = to.fullPath.split("src=")[1];
      this.src =
              baseUrl +
              herf
                      .replace(/\%2F+/g, "/")
                      .replace(/\%3F+/g, "?")
                      .replace(/\%3D+/g, "=")
                      .replace(/\%26+/g, "&");
      this.$event.emit("looding", true);

      let footerName = 'flowGroupList', _this = this;
      if ( this.src.indexOf("drawingBoardType=GROUP") !== -1 ) {
      // && this.src.indexOf("drawingBoard/page/flowGroup")!== -1
        // GROUP
        window.addEventListener('message', function(e){
          if ( e.data.parentsId){
            if (e.data.parentsId !== 'null'){
              _this.$event.emit("crumb", [
                { name: "Group", path: "/group" },
                { name: footerName, path:"/drawingBoard?src=" + "/drawingBoard" +"/page/flowGroup/mxGraph/index.html?drawingBoardType=GROUP&parentAccessPath=flowGroupList&load=" + e.data.parentsId},
                { name: "drawingBoard", path: "/drawingBoard" },
              ]);
            }else if(e.data.parentsId === 'null') {
              _this.$event.emit("crumb", [
                { name: "Group", path: "/group" },
                { name: "drawingBoard", path: "/drawingBoard" },
              ]);
            }
          }
        })
      }else if ( this.src.indexOf("drawingBoardType=TASK") !== -1 ){
        // && this.src.indexOf("drawingBoard/page/flow/mxGraph") !== -1
        // TASK
        window.addEventListener('message', function(e){
          if ( e.data.parentsId){
            if (e.data.parentsId !== 'null'){
              _this.$event.emit("crumb", [
                { name: "Flow", path: "/flow" },
                { name: footerName, path:"/drawingBoard?src=" + "/drawingBoard" +"/page/flowGroup/mxGraph/index.html?drawingBoardType=GROUP&parentAccessPath=flowGroupList&load=" + e.data.parentsId},
                { name: "drawingBoard", path: "/drawingBoard" },
              ]);
            }else if(e.data.parentsId === 'null') {
              _this.$event.emit("crumb", [
                { name: "Flow", path: "/flow" },
                { name: "drawingBoard", path: "/drawingBoard" },
              ]);
            }
          }
        })
      }

      //监听窗口变化 自适应（ 根据需求自行添加 ）
      window.addEventListener("resize", () => {
        this.setSize();
      });
    }
  },
  methods: {
    setSize() {
      this.height = document.querySelector("body").offsetHeight-12 + "px";
    },
    GetChildValue(val){
      this.parentsId = val;
    }
  },
  beforeDestroy() {
    document.querySelector("header").style.cssText = "";
    document.querySelector("footer").style.cssText = "";
    this.$event.emit("crumb", []);
    this.$event.emit("looding", false);
  },
};
</script>
<style lang="scss" scoped>
#piflow-bgc {
  position: fixed;
  top: 0;
  width: 100%;
  height: 100%;
  display: block;
  z-index: 0;
}
</style>