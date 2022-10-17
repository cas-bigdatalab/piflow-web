<template>
  <section>
    <!-- header -->
    <div class="navbar">
      <div class="left">
        <span>{{$t("sidebar.publish")}}</span>
      </div>
    </div>
    <!-- search -->
    <div class="input">
      <Input
          suffix="ios-search"
          v-model="param"
          :placeholder="$t('modal.placeholder')"
          style="width: 300px"/>
    </div>
    <!-- Table button -->
    <Table border :columns="columns" :data="tableData">
      <template slot-scope="{ row }" slot="action">
        <Tooltip v-for="(item, index) in promptContent" :key="index" :content="item.content" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,index+1)">
              <Icon :type="item.icon" />
            </span>
        </Tooltip>
      </template>
    </Table>
    <!-- paging -->
    <div class="page">
      <Page
          :prev-text="$t('page.prev_text')"
          :next-text="$t('page.next_text')"
          show-elevator
          :show-total="true"
          :total="total"
          show-sizer
          @on-change="onPageChange"
          @on-page-size-change="onPageSizeChange"/>
    </div>
    <Modal
        title="Stop List"
        v-model="processToRelease_Modal"
        width="40"
        :mask-closable="false">
      <div>
        <div style="margin-bottom: 10px">
          <label style="width: 110px; text-align: right;display: inline-block">Process Name：</label>
          <Input v-model="publish" :placeholder="$t('modal.placeholder')" style="width: 530px" />
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
              style="width: 530px;display: inline-block"
              class="mytable-scrollbar">
          </vxe-grid>
        </div>
      </div>
      <div slot="footer">
        <Button type="primary" @click="processToRelease_Modal=false">{{ $t("modal.cancel_text") }}</Button>
        <Button type="primary" @click="getSelectEvent()">{{ $t("modal.confirm") }}</Button>
      </div>
    </Modal>
    <Modal
        title="Edit Properties"
        v-model="properties_Modal"
        width="50"
        :mask-closable="false">
      <div>
        <div v-for="(item,index) in stopsDataList" :key="index" :style="{borderBottom: index+1!==stopsDataList.length?'1px solid #e8eaec':''}">
          <p class="title_bar">{{item.name}}</p>
          <div class="basicInfo">
            <div style="margin-bottom: 10px">
              <p style="width: 110px;float: left;line-height:32px;text-align: right;overflow: hidden; white-space: nowrap; text-overflow: ellipsis; ">
                DataSource：</p>
              <Select v-model="!!item.dataSourceVo && !!item.dataSourceVo.id ?item.dataSourceVo.id: item.propertiesVo.id" style="width: calc(100% - 110px);float: left" @on-change="handleChange">
                <Option v-for="item in dataSourceList" :value="item.value" :key="item.value">{{ item.label }}</Option>
              </Select>
            </div>
            <div v-for="items in item.propertiesVo"
                 style="margin-bottom: 10px">
              <p style="width: 110px;float: left;line-height:32px;text-align: right;overflow: hidden; white-space: nowrap; text-overflow: ellipsis; ">
                {{items.name}}：</p>
              <Input v-model="items.customValue" style="width: calc(100% - 110px);float: left" @on-focus="updateText(items.customValue,items.id,items.language,items.name)"/></div>

          </div>
          <p class="display_btn" v-if="!displayList['show'+index]">
            <Icon type="ios-arrow-dropdown" @click="changeState(index)"/>
          </p>
          <div v-else>
            <p class="title_bar" style="font-size: 14px;font-weight: 500;">Stops Properties Example</p>
            <div class="basicInfo">
              <div v-for="items in item.propertiesVo"
                   style="margin-bottom: 10px">
                <p style="width: 110px;float: left;line-height:32px;text-align: right;overflow: hidden; white-space: nowrap; text-overflow: ellipsis; ">
                  {{items.name}}：</p>
                <Input v-model="items.example" disabled style="width: calc(100% - 110px);float: left"/></div>
            </div>
            <p class="display_btn">
              <Icon type="ios-arrow-dropup" @click="changeState(index)"/>
            </p>
          </div>
        </div>
      </div>
      <div slot="footer">
        <Button type="primary" @click="properties_Modal=false">{{ $t("modal.cancel_text") }}</Button>
        <Button type="primary" @click="getSelectEvent()">{{ $t("modal.confirm") }}</Button>
      </div>
    </Modal>
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
  name: "publish",
  components:{CodeEditor},
  data() {
    return {
      page: 1,
      limit: 10,
      total: 0,
      tableData: [],

      param: "",
      id: "",

      promptContent: [
        {
          content: 'Run',
          icon: 'ios-play'
        },
        {
          content: 'Edit',
          icon: 'ios-create-outline'
        },
        {
          content: 'Edit Properties',
          icon: 'ios-create'
        },{
          content: 'Delete',
          icon: 'ios-trash'
        }
      ],
      publish: '',
      publishingId: '',
      processToRelease_Modal: false,
      processToReleaseData: [],
      processToReleaseColumn: [{type: "checkbox", width:"60"}, {  field: 'name', title: 'Stop Name'}],

      properties_Modal: false,
      stopsDataList: [],

      programming_Modal: false,
      Programming: "400px",
      buttonSize: 'text',
      editorContent:'',
      readonly: false,
      stopsInputId: '',

      dataSourceList: [
        {
          value: 'New York',
          label: 'New York'
        },
        {
          value: 'London',
          label: 'London'
        },
        {
          value: 'Sydney',
          label: 'Sydney'
        },
        {
          value: 'Ottawa',
          label: 'Ottawa'
        },
        {
          value: 'Paris',
          label: 'Paris'
        },
        {
          value: 'Canberra',
          label: 'Canberra'
        }
      ],

      displayList: {}
    };
  },
  mounted() {
    this.Programming = window.innerHeight > 300 ? window.innerHeight - window.innerHeight*0.3 +'px' : window.innerHeight +'px' ;
  },
  watch: {
    param(val) {
      this.page = 1;
      this.limit = 10;
      this.getTableData();
    }
  },
  computed: {
    columns() {
      return [
        {
          title: this.$t("dataSource_columns.name"),
          key: "name",
          sortable: true
        },
        {
          title: 'crtDttm',
          key: "crtDttm",
          sortable: true
        },
        {
          title: 'lastUpdateDttm',
          key: "lastUpdateDttm",
          sortable: true
        },
        {
          title: this.$t("dataSource_columns.action"),
          slot: "action",
          width: 230,
          align: "center"
        }];
    }
  },
  created() {
    this.getTableData();
  },
  methods: {
    // Reset
    handleReset() {
      this.page = 1;
      this.limit = 10;
      this.id = "";
    },
    handleButtonSelect(row, key) {
      this.publishingId= row.publishingId;
      switch (key) {
        case 1:
          this.handleRunRow(row);
          break;
        case 2:
          this.gettingStopList(row);
          break;
        case 3:
          this.getDataSourceList();
          this.setProperties(row);
          break;
        case 4:
          this.handleDeleteRow(row);
          break;
        default:
          break;
      }
    },


    // run
    handleRunRow(row) {
      this.$event.emit("crumb", [
        { name: "Process", path: "/processes" },
        { name: "drawingBoard", path: "/drawingBoard" },
      ]);

      this.$axios
          .post("/flow/runFlowByPublishingId", this.$qs.stringify({publishingId: row.publishingId}))
          .then(res => {
            let dataList = res.data;
            if (dataList.code === 200) {
              // 根据类型进入不同的界面
              let src = `/drawingBoard/page/process/mxGraph/index.html?drawingBoardType=PROCESS&processType=TASK&load=${dataList.processId}`;
              this.$router.push({
                path: "/drawingBoard",
                query: { src },
              });
            }
          })
          .catch(error => {
            console.log(error);
          })
    },
    // edit
    gettingStopList(row){
      this.processToRelease_Modal = true;
      let parameter = {flowId: row.flowId};
      this.$axios
          .post('/stops/getStopsNameByFlowId', this.$qs.stringify(parameter))
          .then(res => {
            var dataMap = res.data;
            if (dataMap.code == 200) {
              this.processToReleaseData = dataMap.stopsIdAndNameList;
              this.getSelectStopList(row.publishingId);
            }
          })
          .catch(error => {
            console.log(error);
          });
    },
    // set
    setProperties(row){
      this.getSelectStopList(row.publishingId);
      this.properties_Modal= true;
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
              this.stopsDataList= dataMap.stopsDataList;
              dataMap.stopsDataList.forEach((item,index)=>{
                this.displayList['show'+index]= false
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
              this.processToRelease_Modal=false;
              this.publish= '';
              this.publishingId= '';
              this.getTableData();
            }
          })
          .catch(error => {
            console.log(error);
          });
    },

    // Delete
    handleDeleteRow(row) {
      this.$axios
          .post("/stops/deleteFlowStopsPublishing", this.$qs.stringify({publishingId: row.publishingId}))
          .then(res => {
            let dataList = res.data;
            if (dataList.code === 200) {
              this.$Modal.confirm({
                title: this.$t("tip.title"),
                content: row.name+'  '+ this.$t("tip.reference_content"),
               })
              this.getTableData()
            }  else {
              this.$Message.error({
                content: this.$t("tip.update_fail_content"),
                duration: 3
              });
            }
          })
          .catch(error => {
            console.log(error);
          })
    },

    getTableData() {
      let data = { page: this.page, limit: this.limit };
      if (this.param) {
        data.param = this.param;
      }
      this.$axios
          .post("/stops/getPublishingListPager", this.$qs.stringify(data))
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

    onPageChange(pageNo) {
      this.page = pageNo;
      this.getTableData();
    },
    onPageSizeChange(pageSize) {
      this.limit = pageSize;
      this.getTableData();
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
    getDataSourceList() {
      this.dataSourceList= [];
      this.$axios
          .post("/datasource/getDatasourceList")
          .then(res => {
            if (res.data.code === 200) {
              res.data.data.forEach(item=>{
                this.dataSourceList.push({
                  label: item.dataSourceName+'('+item.dataSourceType+')',
                  value: item.id
                })
              })

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
    }
  }
};
</script>
<style lang="scss" scoped>
@import "./index.scss";
.navbar{
  margin-bottom: 10px;
}
.basicInfo{
  overflow: hidden;
  div{
    width: 50%;
    //float: left;
    display: inline-block;
  }
}
.title_bar{
  font-size: 16px;
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
  background-color: #eee;
  color: #777;
}
.display_btn{
  text-align: center;
  i{
    font-size: 20px;
    cursor: pointer;
  }
}
</style>

