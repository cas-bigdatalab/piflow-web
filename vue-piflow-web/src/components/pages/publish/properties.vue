<template>
  <section class="box_warp">
    <div>
      <div style="padding: 10px; border-bottom: 1px solid rgb(192, 192, 192);">
        <div style="padding: 0px 0px 6px; white-space: nowrap; font-weight: bold;overflow: hidden;font-size: 16px">
          Flow Basic Information
          <span class="btn_group">
            <span class="button-warp" @click="handleButtonSelect(1)"><Icon type="ios-play"/></span>
            <span class="button-warp" @click="handleButtonSelect(2)"><Icon type="md-return-left" /></span>
          </span>
        </div>
        <div class="flowBasicInfo">
          <ul>
            <li><span>UUID</span>：<span>{{ flowInfo.uuid }}</span></li>
            <li><span>FlowName</span>：<span @click="routerPush(flowInfo)" class="highlighted">{{ flowInfo.name }}</span></li>
            <li><span>Description</span>：<span>{{ flowInfo.description }}</span></li>
            <li><span>CreateTime</span>：<span>{{ flowInfo.crtDttmString }}</span></li>
            <li><span>Stops</span>：<span>{{ flowInfo.stopQuantity }}</span></li>
          </ul>
        </div>
      </div>

      <div v-for="(item,index) in stopsDataList" :key="index" :style="{borderBottom: index+1!==stopsDataList.length?'1px solid #e8eaec':''}">
        <div id="tab_content1" style="display:block;">
          <div style="padding: 10px;">
            <div style="padding: 0px 0px 6px; white-space: nowrap; overflow: hidden; width: 200px; font-weight: bold;font-size: 16px">
              Stop Basic Information
            </div>
            <div class="stopBasicInfo">
              <ul>
                <li><span>StopsName</span>：<span>{{ item.name }}</span></li>
                <li><span>Description</span>：<span>{{ item.description }}</span></li>
                <li><span>Groups</span>：<span>{{ item.groups }}</span></li>
                <li><span>Bundle</span>：<span>{{ item.bundel }}</span></li>
                <li><span>ModifyCounts</span>：<span>{{ item.version }}</span></li>
                <li><span>Owner</span>：<span>{{ item.owner }}</span></li>
                <li><span>CreateTime</span>：<span>{{ item.crtDttmString }}</span></li>
              </ul>
            </div>

          </div>
        </div>

        <div style="padding: 10px 20px;">
          <p class="title_bar">{{item.name}}</p>
          <div style="overflow: hidden">
            <div style=" width: 50%;float: left;" class="basicInfo">
              <div v-for="items in item.propertiesVo"
                   style="overflow: hidden">
                <p style="width: 110px;float: left;line-height:32px;text-align: right;overflow: hidden; white-space: nowrap; text-overflow: ellipsis;margin: 6px 0; ">
                  {{items.name}}
                  <Tooltip max-width="200" :content="items.description" theme="light">
                  <Icon type="md-help-circle" />
                </Tooltip></p>
                <Input v-model="items.customValue" style="width: calc(100% - 120px);float: left" @on-focus="updateText(items.customValue,items.id,items.language,items.name)"/>
                <span style="color: red;line-height: 42px;display: inline-block;">*</span></div>

            </div>
<!--            <p class="display_btn" v-if="!displayList['show'+index]">-->
<!--              <Icon type="ios-arrow-dropdown" @click="changeState(index)"/>-->
<!--            </p>-->
            <div style=" width: 50%;float: left;">
              <div class="basicInfo">
                <div v-for="items in item.propertiesVo"
                     style="overflow: hidden">
                  <p style="width: 110px;float: left;line-height:32px;text-align: right;overflow: hidden; white-space: nowrap; text-overflow: ellipsis; margin: 6px 0;">
                    {{items.name}}
                    <Tooltip max-width="200" :content="items.description" theme="light">
                      <Icon type="md-help-circle" />
                    </Tooltip></p>
                  <Input v-model="items.example" disabled style="width: calc(100% - 120px);float: left"/></div>
              </div>
<!--              <p class="display_btn">-->
<!--                <Icon type="ios-arrow-dropup" @click="changeState(index)"/>-->
<!--              </p>-->
            </div>
          </div>

        </div>
      </div>
    </div>
<!--    <div class="btn_group">-->
<!--      <Button @click="properties_Modal=false">{{ $t("modal.cancel_text") }}</Button>-->
<!--      <Button type="primary" @click="getSelectEvent()">{{ $t("modal.confirm") }}</Button>-->
<!--    </div>-->
    <!--The online programming-->
    <Modal
        title="schema"
        v-model="programming_Modal"
        width="60"
        @on-ok="updateStopsProperty"
        :mask-closable="false">
      <div :style="{ height :`${Programming}`}">
        <RadioGroup size="small" v-model="buttonSize" type="button">
          <Radio label="text" :disabled="buttonSize === 'text'? false: true">text</Radio>
          <Radio label="scala" :disabled="buttonSize === 'scala'? false: true">scala</Radio>
          <Radio label="javascript" :disabled="buttonSize === 'javascript'? false: true">java</Radio>
          <Radio label="python" :disabled="buttonSize === 'python'? false: true">python</Radio>
          <Radio label="sh" :disabled="buttonSize === 'sh'? false: true">shell</Radio>
          <Radio label="sql" :disabled="buttonSize === 'sql'? false: true">sql</Radio>
        </RadioGroup>
        <code-editor
            ref="_firstRefs"
            class="editor h-100"
            v-model="editorContent"
            :readonly="readonly" :language="buttonSize" theme="dracula">
        </code-editor>
      </div>
    </Modal>
  </section>
</template>

<script>
import CodeEditor from '../../compon/CodeFormat'
export default {
  name: "properties",
  components:{CodeEditor},
  data() {
    return {
      id: "",

      flowInfo: {},
      publish: '',
      publishingId: '',
      processToReleaseData: [],
      processToReleaseColumn: [{type: "checkbox", width:"60"}, {  field: 'name', title: 'Stop Name'}],

      stopsDataList: [],

      programming_Modal: false,
      Programming: "400px",
      buttonSize: 'text',
      editorContent:'',
      readonly: false,
      stopsInputId: '',

      displayList: {}
    };
  },
  mounted() {
    this.Programming = window.innerHeight > 300 ? window.innerHeight - window.innerHeight*0.3 +'px' : window.innerHeight +'px' ;
  },

  created() {
    this.publishingId= this.$route.query.publishingId;
    this.setProperties(this.$route.query.publishingId);
  },
  methods: {
    // set
    setProperties(publishingId){
      this.getSelectStopList(publishingId);
    },
    updateText(val,id,language,name){
      let _this = this;
      _this.programming_Modal = true;
      _this.editorContent = val;
      _this.stopsInputId = id;

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
        case 'Sql':
          _this.buttonSize = 'sql';
          break;
        default:
          break;
      }
    },

    getSelectStopList(Id){
      this.stopsDataList= [];
      let parameter = {publishingId: Id};
      this.$axios
          .post('/stops/getPublishingById', this.$qs.stringify(parameter))
          .then(res => {
            var dataMap = res.data;
            if (dataMap.code === 200) {
              this.publish= dataMap.name;
              let checkboxRow= [];
              this.flowInfo = dataMap.flowVo;
              this.stopsDataList= dataMap.stopsDataList;
              dataMap.stopsDataList.forEach((item,index)=>{
                this.displayList['show'+index]= false
                this.processToReleaseData.forEach((node,inx)=>{
                  if ( item.name ===  node.name)
                    checkboxRow.push(this.processToReleaseData[inx])
                })
              })
              // this.$refs.processToRelease.setCheckboxRow(checkboxRow, true)
            }
          })
          .catch(error => {
            console.log(error);
          });
    },
    // save
    getSelectEvent () {
      let selectRecords = this.$refs.processToRelease.getCheckboxRecords();
      if (selectRecords.length===0 || !this.publish)
        return;
      let stopsIds = '';
      selectRecords.forEach(item=>{
        stopsIds+= item.id+','
      })

      let parameter = {
        name: this.publish,
        stopsIds: stopsIds.substr(0,stopsIds.length -1),
        publishingId: this.publishingId};
      let  url= '/stops/updatePublishing';

      this.$axios
          .post(url, this.$qs.stringify(parameter))
          .then(res => {
            var dataMap = res.data;
            if (dataMap.code == 200) {
              this.publish= '';
              this.publishingId= '';
            }
          })
          .catch(error => {
            console.log(error);
          });
    },


    // Save the changed flow configuration information
    updateStopsProperty(){
      let data = {};
      data.id = this.stopsInputId;
      data.content = this.editorContent;
      this.$axios
          .post('/stops/updateStopsOne', this.$qs.stringify(data))
          .then(res => {
            var dataMap = res.data;
            if (dataMap.code == 200) {
              this.getSelectStopList(this.publishingId);

            }
          })
          .catch(error => {
            console.log(error);
          });
    },
    handleChange(val){
      let data = { dataSourceId: val, stopId: this.publishingId };
      this.$axios
          .post('/datasource/fillDatasource', this.$qs.stringify(data))
          .then(res => {
            var dataMap = res.data;
            if (dataMap.code == 200) {
              this.$Message.success({
                content: 'Save success',
                duration: 3
              });

            }else {
              this.$Message.error({
                content: 'Save failed',
                duration: 3
              });
            }
          })
          .catch(error => {
            console.log(error);
          });
    },
    changeState(index){
      this.displayList['show'+index] = !this.displayList['show'+index];
      this.displayList = Object.assign({},this.displayList)
    },

    routerPush(row){
      this.$event.emit("crumb", [
        { name: "Flow", path: "/flow" },
        { name: "drawingBoard", path: "/drawingBoard" },
      ]);
      this.$router.push({
        path: "/drawingBoard",
        query: {
          src: "/drawingBoard/page/flow/mxGraph/index.html?load=" + row.id,
        },
      });
    },

    handleButtonSelect(key) {
      switch (key) {
        case 1:
          this.handleRunRow();
          break;
        case 2:
          this.$router.push({ path: "/publish" })
          break;
        default:
          break;
      }
    },

    // run
    handleRunRow() {
      this.$event.emit("crumb", [
        { name: "Process", path: "/processes" },
        { name: "drawingBoard", path: "/drawingBoard" },
      ]);
      this.$event.emit("loading", true);
      this.$axios
          .post("/flow/runFlowByPublishingId", this.$qs.stringify({publishingId: this.publishingId}))
          .then(res => {
            let dataList = res.data;
            if (dataList.code === 200) {
              // 根据类型进入不同的界面
              let src = `/drawingBoard/page/process/mxGraph/index.html?drawingBoardType=PROCESS&processType=TASK&load=${dataList.processId}`;
              this.$router.push({
                path: "/drawingBoard",
                query: { src },
              });
            }else{
              this.$event.emit("loading", false);
              this.$Message.error({
                content: dataList.errorMsg,
                duration: 3
              });
            }
          })
          .catch(error => {
            console.log(error);
          })
    },

  }
};
</script>
<style lang="scss" scoped>
@import "./index.scss";

.basicInfo{
  overflow: hidden;
  div{
    //width: 50%;
    //float: left;
    //display: inline-block;
  }
}
.title_bar{
  font-size: 14px;
  font-weight: 700;
  margin: 10px 0;
}
.list-item{
  border-bottom: 1px solid #e8eaec;
  border-top: 1px solid #e8eaec;
  margin: 10px 0;
  padding-bottom: 20px;
}
::v-deep .ivu-input-disabled{
  background-color: #f9f9f9;
  color: #777;
}
.display_btn{
  text-align: center;
  i{
    font-size: 20px;
    cursor: pointer;
  }
}
.box_warp{
  background: white;
  padding: 10px;
  border-radius: 10px;
  .btn_group{
    margin-top: 40px;
    text-align: center;
    button:first-child{
      margin-right: 20px;
    }
  }
  #span_flowVo_name{
    cursor: pointer;
    color: #2D8cF0;
  }
}
</style>

