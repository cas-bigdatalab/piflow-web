<template>
  <div :style="{height}">
    <iframe :src="src" id="bariframe" style="width: 100%;height: 100%" frameborder="0"></iframe>
    <img id="piflow-bgc" src="../../../assets/img/hbbj.jpg" />
    <!--在线编程-->
    <Modal
        :title="programming_Title"
        v-model="programming_Modal"
        width="60"
        @on-ok="updateStopsProperty"
        :mask-closable="false">
      <div :style="{ height :`${Programming}`}">
        <RadioGroup size="small" v-model="buttonSize" type="button">
          <Radio label="text" :disabled="buttonSize === 'text'?false:true">text</Radio>
          <Radio label="scala" :disabled="buttonSize === 'scala'?false:true">scala</Radio>
          <Radio label="javascript" :disabled="buttonSize === 'javascript'?false:true">java</Radio>
          <Radio label="python" :disabled="buttonSize === 'python'?false:true">python</Radio>
          <Radio label="sh" :disabled="buttonSize === 'sh'?false:true">shell</Radio>
        </RadioGroup>
        <code-editor ref="_firstRefs" class="editor h-100" v-model="editorContent"
                     :readonly="readonly" :language="buttonSize" theme="dracula">
        </code-editor>
      </div>
    </Modal>
  </div>
</template>
 
<script>
import CodeEditor from '../../compon/CodeFormat'
export default {
  name: "DrawingBoard",
  components:{CodeEditor},
  data() {
    return {
      height: "100%",
      Programming: "400px",
      src: "",
      parentsId: '',
      editorContent:'',
      readonly: false,
      programming_Modal: false,
      buttonSize: 'text',
      stopsId: '',
      programming_Title: 'Title',
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
    //  接收可视化编程data
    window["openRightHelpPage"] = (val,id,language,name) => {
      _this.Programming = window.innerHeight > 300 ? window.innerHeight - window.innerHeight*0.3 +'px' : window.innerHeight +'px' ;
      _this.programming_Modal = true;
      _this.editorContent = val;
      _this.stopsId = id;
      _this.programming_Title = name;
      switch (language){
        case 'Text':
          _this.buttonSize = 'text';
          break;
        case 'Scala':
          _this.buttonSize = 'scala';
          break;
        case 'Javascript':
          _this.buttonSize = 'javascript';
          break;
        case 'Python':
          _this.buttonSize = 'python';
          break;
        case 'Shell':
          _this.buttonSize = 'sh';
          break;
        default:
          break;
      }
    };
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

      // let footerName = 'flowGroupList', _this = this;
      // if ( this.src.indexOf("drawingBoardType=GROUP") !== -1 ) {
      // // && this.src.indexOf("drawingBoard/page/flowGroup")!== -1
      //   // GROUP
      //   window.addEventListener('message', function(e){
      //     if ( e.data.parentsId){
      //       if (e.data.parentsId !== 'null'){
      //         _this.$event.emit("crumb", [
      //           { name: "Group", path: "/group" },
      //           { name: footerName, path:"/drawingBoard?src=" + "/drawingBoard" +"/page/flowGroup/mxGraph/index.html?drawingBoardType=GROUP&parentAccessPath=flowGroupList&load=" + e.data.parentsId},
      //           { name: "drawingBoard", path: "/drawingBoard" },
      //         ]);
      //       }else if(e.data.parentsId === 'null') {
      //         _this.$event.emit("crumb", [
      //           { name: "Group", path: "/group" },
      //           { name: "drawingBoard", path: "/drawingBoard" },
      //         ]);
      //       }
      //     }
      //   })
      // }else if ( this.src.indexOf("drawingBoardType=TASK") !== -1 ){
      //   // && this.src.indexOf("drawingBoard/page/flow/mxGraph") !== -1
      //   // TASK
      //   window.addEventListener('message', function(e){
      //     if ( e.data.parentsId){
      //       if (e.data.parentsId !== 'null'){
      //         _this.$event.emit("crumb", [
      //           { name: "Flow", path: "/flow" },
      //           { name: footerName, path:"/drawingBoard?src=" + "/drawingBoard" +"/page/flowGroup/mxGraph/index.html?drawingBoardType=GROUP&parentAccessPath=flowGroupList&load=" + e.data.parentsId},
      //           { name: "drawingBoard", path: "/drawingBoard" },
      //         ]);
      //       }else if(e.data.parentsId === 'null') {
      //         _this.$event.emit("crumb", [
      //           { name: "Flow", path: "/flow" },
      //           { name: "drawingBoard", path: "/drawingBoard" },
      //         ]);
      //       }
      //     }
      //   })
      // }

      //监听窗口变化 自适应（ 根据需求自行添加 ）
      window.addEventListener("resize", () => {
        this.setSize();
      });
    },
  },
  methods: {
    setSize() {
      this.height = document.querySelector("body").offsetHeight-12 + "px";
    },
    GetChildValue(val){
      this.parentsId = val;
    },
    // 保存更改的flow配置信息
    updateStopsProperty(){
      let data = {};
      data.id = this.stopsId;
      data.content = this.editorContent;
      this.$axios
          .post('/stops/updateStopsOne', this.$qs.stringify(data))
          .then(res => {
            var dataMap = res.data;
            if (dataMap.code == 200) {
              document.getElementById("bariframe").contentWindow.document.getElementById(`${this.stopsId}`).value = dataMap.value;
              document.getElementById("bariframe").contentWindow.document.getElementById(`${this.stopsId}`).setAttribute('data',`${dataMap.value}`)

              // console.log(document.frames('bariframe').document)
              // $("#" + stopsPropertyId).val(dataMap.value);
              // $("#" + stopsPropertyId).attr("data", dataMap.value);
            } else {
              // this.$Modal.error({
              //   title: this.$t("tip.title"),
              //   content: `${row.jobName} ` + this.$t("tip.stop_fail_content")
              // });
            }
          })
          .catch(error => {
            console.log(error);
            // this.$Message.error({
            //   content: this.$t("tip.fault_content"),
            //   duration: 3
            // });
          });
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