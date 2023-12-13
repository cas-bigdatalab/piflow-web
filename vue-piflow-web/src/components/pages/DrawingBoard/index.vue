<template>
  <div :style="{height}">
    <iframe :src="src" id="bariframe" style="width: 100%;height: 100%" frameborder="0"></iframe>
    <!-- <img id="piflow-bgc" src="" /> -->

    <!--The online programming-->
    <Modal
        :title="programming_Title"
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

    <!--testData-->
    <Modal
        :title="programming_Title"
        v-model="NeedSource_Modal"
        width="60"
        @on-ok="runParameters"
        :mask-closable="false">
      <div :style="{ height :`${Programming}`}">
        <Tabs type="card"
              :value="tabName"
              @on-click="tabClick">
          <TabPane v-for="(tab, i) in queryJson.ports" :key="tab+i" :name="tab+i" :label="'Port：' + tab">
            <vxe-table
                border
                resizable
                highlight-hover-row
                height="360"
                row-key
                ref="parameters"
                size="medium"
                :auto-resize="true"
                @radio-change="selectChangeEvent"
                :radio-config="{labelField: 'name'}"
                :data="tableData">
              <vxe-table-column type="radio" title="Name"></vxe-table-column>
              <vxe-table-column field="description" title="Description"></vxe-table-column>
              <vxe-table-column field="crtDttm" title="CrtDttm"></vxe-table-column>
              <vxe-table-column title="Actions" width="70">
                <template #default="{ row }">
                  <Tooltip content="To View" placement="left">
                    <Button icon="md-search" @click="showDetailEvent(row)"></Button>
                  </Tooltip>
                </template>
              </vxe-table-column>

            </vxe-table>
          </TabPane>
        </Tabs>
        <vxe-modal v-model="showDetails" title="Check the details" width="800" height="400" resize>
          <template #default>
            <vxe-grid
                border
                show-overflow
                resizable
                keep-source
                ref="xTable"
                max-height="400"
                :data="editableData"
                :columns="tableColumn"
                class="mytable-scrollbar"
                :toolbar-config="tableToolbar">
            </vxe-grid>
          </template>
        </vxe-modal>
      </div>
    </Modal>

    <!--visualization-->
    <Modal
        title="View Charts"
        v-model="visualization_Modal"
        width="60"
        @on-ok="visualization_Modal=false"
        :mask-closable="false">
      <div>
          <vxe-grid
              border
              show-overflow
              resizable
              keep-source
              ref="xTable"
              max-height="500"
              :data="visualizationData"
              :columns="tableColumn"
              class="mytable-scrollbar"
              :export-config="tableExport"
              :toolbar-config="visualization_Toolbar">
          </vxe-grid>
      </div>
    </Modal>

    <!--processToRelease-->
    <Modal
        title="Stop List"
        v-model="processToRelease_Modal"
        width="40"
        :mask-closable="false">
      <div>
        <div style="margin-bottom: 10px">
          <label style="width: 110px; text-align: right;display: inline-block">Process Name：</label>
          <Input v-model="publish" :placeholder="$t('modal.placeholder')" style="width: 100%" />
        </div>
        <div>
          <label style="width: 110px;text-align: right;display: inline-block;vertical-align: top">Component：</label>
          <vxe-grid
              border
              show-overflow
              resizable
              keep-source
              ref="processToRelease"
              max-height="300"
              :data="processToReleaseData"
              :columns="processToReleaseColumn"
              style="width: 100%;display: inline-block"
              class="mytable-scrollbar">
          </vxe-grid>
        </div>
      </div>
      <div slot="footer">
        <Button @click="removeStop">{{ $t("modal.cancel_text") }}</Button>
        <Button type="primary" @click="getSelectEvent()">{{ $t("modal.confirm") }}</Button>
      </div>
    </Modal>

    <Drawer :closable="false" width="640" v-model="visualization_Drawer">
      <div slot="header"><a target="_blank" :href="visualization_url">查看全部</a></div>
      <iframe :src="visualization_url" style="width: 100%;height: 100%" frameborder="0"></iframe>
    </Drawer>
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
      editorContent:'',
      readonly: false,
      programming_Modal: false,
      buttonSize: 'text',
      stopsId: '',
      programming_Title: 'Set Data For Each Port',

      NeedSource_Modal: false,
      tableData: [],
      page: 1,
      limit: 10,
      total: 0,
      single: false,

      queryJson: {
        isRunFollowUp: true,
        stopsId: '',
        ports: [],
        testDataIds: [],
      },

      portsList: [],
      testDataIdList: [],
      list: [],
      tabName: '',

      showDetails: false,
      tableToolbar: {
        perfect: true,
        zoom: true,
        custom: true,
      },
      editableData: [],
      tableColumn: [],

      visualization_Modal: false,
      visualizationData: [],
      visualization_Toolbar: {
        perfect: true,
        zoom: true,
        custom: true,
        export: true,
      },
      tableExport: {
        type: 'csv',
        types: [ 'csv', 'html', 'xml', 'txt']
      },


      publish: '',
      publishingId: '',
      processToRelease_Modal: false,
      processToReleaseData: [{name:'aa'}],
      processToReleaseColumn: [{type: "checkbox", width:"60"}, {  field: 'name', title: 'Stop Name'}],

      visualization_Drawer: false,
      visualization_url: ''
    };
  },
  computed: {
    columns() {
      return [
        {
          title: this.$t("testData_columns.action"),
          slot: "action",
          width: 100,
          align: "center"
        },
        {
          title: this.$t("testData_columns.name"),
          key: "name",
          sortable: true
        },
        {
          title: this.$t("testData_columns.description"),
          key: "description"
        },
        {
          title: this.$t("testData_columns.CreateTime"),
          key: "crtDttm",
          sortable: true
        }
      ];
    }
  },
  created() {
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
      this.$event.emit("loading", true);
    }
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
          _this.$event.emit("loading", false);

          piflowBgc.remove();
        });
      } else {
        oIframe.onload = function () {
          _this.$event.emit("loading", false);
          piflowBgc.remove();
        };
      }
    }
    // 动态修改footer样式
    document.querySelector("footer").style.cssText =
      "position: fixed; bottom: 0;";
    document.querySelector("header").style.cssText =
      "position: fixed; width: 100%; top: 0;";
    this.setSize();
    window.addEventListener('message', function(event) {
      _this.$event.emit("loading", event.data);
    }, false);


    //  Visual programming data
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
        case 'Sql':
          _this.buttonSize = 'sql';
          break;
        default:
          break;
      }
    };

    //  StopsComponent is neeed source data
    window["StopsComponentIsNeeedSourceData"] = ({id,isRunFollowUp}) => {
      _this.NeedSource_Modal = true;
       let data = {
         stopsId: id
       };
       let inputJson = {
         isRunFollowUp: isRunFollowUp,
         stopsId: id,
         ports: [],
         testDataIds: [],
       };
       let isNeedSource = false;

      this.$axios
          .post("/stopsManage/isNeedSource", this.$qs.stringify(data))
          .then((res) => {
            let data = res.data;
            if (data.code === 200){
              isNeedSource = data.isNeedSource;
              inputJson.ports = data.ports;
              this.queryJson = inputJson;
              this.tabName = this.queryJson.ports[0]+0;
              if (data.isNeedSource){

                this.getTableData();
              }else {
                this.queryJson = inputJson;
                this.runStops(isNeedSource,this.queryJson)
              }
            }
          })
          .catch((error) => {
            console.log(error);
            this.$event.emit("loading", false);
            this.$Message.error({
              content: this.$t("tip.fault_content"),
              duration: 3
            });
          });

    };

    //  visualization
    window["visualizationTable"] = ({value}) => {
      _this.visualization_Modal = true;
      _this.visualizationData = value;
      _this.getTitle(_this.visualizationData);
    }
    //  process to release
    window["processToRelease"] = ({value,type,id}) => {
      _this.gettingStopList(value,type,id);
      this.publishingId= id;
    }

    //  visualization_Drawer
    window["visualization_Drawer"] = ({value}) => {
      _this.visualization_url = value;
      _this.visualization_Drawer = true;
    }
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
      this.$event.emit("loading", true);

      window.addEventListener("resize", () => {
        this.setSize();
      });
    },
  },
  methods: {
    setSize() {
      this.height = document.querySelector("body").offsetHeight-12 + "px";
    },

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
            }
          })
          .catch(error => {
            console.log(error);
          });
    },

    runParameters(){
      this.list.forEach((item)=>{
        Object.keys(item).forEach(key=>{
          switch (key){
            case 'ports':
              this.portsList.push(item[key]);
              break;
            case 'testDataId':
              this.testDataIdList.push(item[key]);
              break;
          }
        })
      })

      this.queryJson.testDataIds= this.testDataIdList;

      this.runStops(true,this.queryJson)
    },

    runStops(isNeedSource,queryJson){
      this.$event.emit("loading", true);
      let data = {};
      if (isNeedSource){
        data = queryJson;
      }else {
        data = queryJson;
      }
      data.ports= this.portsList;
      this.testDataIdList= [];
      this.portsList = [];

      this.$axios
          .post("/stopsManage/runStops", this.$qs.stringify(data))
          .then((res) => {
            let data = res.data;
            if (data.code === 200){
              this.$event.emit("loading", false);
              this.JumpToMonitor(data.processId,data.errorMsg)
            }
          })
          .catch((error) => {
            console.log(error);
            this.$event.emit("loading", false);
          })
    },

    JumpToMonitor(id,msg){
      let pageURl = {
        pageURl: id,
        pageMsg: msg
      }
      document.getElementById("bariframe").contentWindow.postMessage(pageURl,'*');
    },

    showDetailEvent (row) {
      this.showDetails = true;
      let data = { testDataId: row.id };
      this.$axios
          .post("/testData/testDataSchemaValuesList", this.$qs.stringify(data))
          .then(res => {
            let data = res.data;
            if (data.code === 200) {
              this.editableData = data.schemaValue;
              this.getTitle(data.schemaValue);

            } else {
              this.$Message.error({
                content: this.$t("tip.request_fail_content"),
                duration: 3
              });
            }
          })
          .catch(error => {
            console.log(error);
            this.$Message.error({
              content: this.$t("tip.fault_content"),
              duration: 3
            });
          })
    },

    getTitle(schemaValuesList) {
      this.tableColumn= [];
      let tableTitle = Object.keys(schemaValuesList[0]);
      for (let i=0;i<tableTitle.length;i++){
        if (tableTitle[i]==='dataRow'){

        }else {
          this.tableColumn.push({
            field: tableTitle[i],
            title: tableTitle[i]
          })
        }
      }
    },

    getTableData() {
      let data = { page: this.page, limit: this.limit };
      if (this.param) {
        data.param = this.param;
      }
      this.$axios
          .post("/testData/testDataListPage", this.$qs.stringify(data))
          .then(res => {
            if (res.data.code === 200) {
              this.tableData = res.data.data;
              this.total = res.data.count;
            } else {
              this.$Message.error({
                content: this.$t("tip.request_fail_content"),
                duration: 3
              });
            }
          })
          .catch(error => {
            console.log(error);
            this.$Message.error({
              content: this.$t("tip.fault_content"),
              duration: 3
            });
          });
    },

    tabClick(name){
      this.tabName = name;
    },

    selectChangeEvent ({ row }) {
      let obj = {}, selectPort = ''
      this.queryJson.ports.forEach((item,i)=>{
        if (this.tabName === item+i){
          selectPort = item;
        }
      })

      obj.ports= selectPort;
      obj.testDataId= row.id;

      if (this.list.length=== 0){
        this.list.push(obj);
      }else {
        this.list.forEach((item,index)=>{
          if(item.ports === obj.ports){
            this.list.splice(index, 1);
          }
        })
        this.list.push(obj);
      }
    },

    gettingStopList(value,type,id){
      this.processToRelease_Modal = true;

      let parameter = {flowId: value};
      this.$axios
          .post('/stops/getStopsNameByFlowId', this.$qs.stringify(parameter))
          .then(res => {
            var dataMap = res.data;
            if (dataMap.code == 200) {
              this.processToReleaseData = dataMap.stopsIdAndNameList;
              if (type==='edit')
                this.getSelectStopList(id);
            }
          })
          .catch(error => {
            console.log(error);
          });
    },

    getSelectStopList(value){
      let parameter = {publishingId: value};
      this.$axios
          .post('/stops/getPublishingById', this.$qs.stringify(parameter))
          .then(res => {
            var dataMap = res.data;
            if (dataMap.code == 200) {
              this.publish= dataMap.name;
              let checkboxRow= [];
              dataMap.stopsDataList.forEach(item=>{
                this.processToReleaseData.forEach((node,inx)=>{
                  if ( item.name ===  node.name)
                    checkboxRow.push(this.processToReleaseData[inx])
                })
              })
              this.$refs.processToRelease.setCheckboxRow(checkboxRow, true)
            }
          })
          .catch(error => {
            console.log(error);
          });
    },

    removeStop(){
      this.processToRelease_Modal= false;
      this.publish= '';
    },

    getSelectEvent () {
      let selectRecords = this.$refs.processToRelease.getCheckboxRecords();
      if (!this.publish){
        this.$Message.warning({
          content: 'Please enter the Process Name',
          duration: 3
        });
          return;
      }else if (selectRecords.length===0){
        this.$Message.warning({
          content: 'Please select a Component',
          duration: 3
        });
        return;
      }
      let stopsIds = '';
      selectRecords.forEach(item=>{
        stopsIds+= item.id+','
      })

      let parameter = {name: this.publish, stopsIds: stopsIds.substr(0,stopsIds.length -1)};
      let url = '/stops/publishingStops';
      if (this.publishingId){
        parameter.publishingId= this.publishingId;
        url= '/stops/updatePublishing';
      }

      this.$axios
          .post(url, this.$qs.stringify(parameter))
          .then(res => {
            var dataMap = res.data;
            if (dataMap.code == 200) {
              this.processToRelease_Modal=false;
              this.publish= '';
              this.publishingId= '';
              this.$Message.success({
                content: dataMap.errorMsg,
                duration: 3
              });
              this.$router.push({
                name: 'publish',
                path: "/publish",
              })
            }
            else
              this.$Message.error({
                content: dataMap.errorMsg,
                duration: 3
              });
          })
          .catch(error => {
            this.$Message.error({
              content: error,
              duration: 3
            });
            console.log(error);
          });
    },

  },
  beforeDestroy() {
    document.querySelector("header").style.cssText = "";
    document.querySelector("footer").style.cssText = "";
    this.$event.emit("crumb", []);
    this.$event.emit("loading", false);
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
::v-deep .ivu-tabs.ivu-tabs-card > .ivu-tabs-bar .ivu-tabs-tab-active{
  border-color: #dcdee2!important;
}
::v-deep .ivu-tabs-nav .ivu-tabs-tab:hover {
  color: var(--button-color);
}
/*滚动条整体部分*/
.mytable-scrollbar ::-webkit-scrollbar {
  width: 5px;
  height: 5px;
}
/*滚动条的轨道*/
.mytable-scrollbar ::-webkit-scrollbar-track {
  background-color: #FFFFFF;
}
/*滚动条里面的小方块，能向上向下移动*/
.mytable-scrollbar ::-webkit-scrollbar-thumb {
  background-color: #eeeeee;
  border-radius: 5px;
  border: 1px solid #F1F1F1;
  box-shadow: inset 0 0 6px rgba(66, 65, 65, 0.3);
}
.mytable-scrollbar ::-webkit-scrollbar-thumb:hover {
  background-color: #A8A8A8;
}
.mytable-scrollbar ::-webkit-scrollbar-thumb:active {
  background-color: #787878;
}
/*边角，即两个滚动条的交汇处*/
.mytable-scrollbar ::-webkit-scrollbar-corner {
  background-color: #FFFFFF;
}
</style>