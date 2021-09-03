<template>
  <section>
    <!-- header -->
    <div class="navbar">
      <div class="left">
        <span>{{$t("sidebar.flow")}}</span>
      </div>
      <div class="right">
        <span class="button-warp" @click="handleModalSwitch">
          <Icon type="md-add" />
        </span>
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
    <!-- Save template -->
    <Modal
      v-model="isTemplateOpen"
      :title="$t('modal.template_title')"
      :ok-text="$t('modal.ok_text')"
      :cancel-text="$t('modal.cancel_text')"
      @on-ok="handletSetEmplate">
      <div class="modal-warp">
        <div class="item">
          <Input v-model="templateName" :placeholder="$t('modal.placeholder')" />
        </div>
      </div>
    </Modal>
    <!-- add / update -->
    <Modal
      v-model="isOpen"
      :title="id?$t('flow_columns.update_title'):$t('flow_columns.create_title')"
      :ok-text="$t('modal.ok_text')"
      :cancel-text="$t('modal.cancel_text')"
      @on-ok="handleSaveUpdateData">
      <div class="modal-warp">
        <div class="item">
          <label>{{$t('flow_columns.flow_name')}}：</label>
          <Input
              show-word-limit
              maxlength="100"
              v-model="name"
              :placeholder="$t('modal.placeholder')"
              style="width: 350px" />
        </div>
        <div class="item">
          <label>{{$t('flow_columns.driverMemory')}}：</label>
          <Input
              show-word-limit
              maxlength="100"
              v-model="driverMemory"
              :placeholder="$t('modal.placeholder')"
              style="width: 350px"/>
        </div>
        <div class="item">
          <label>{{$t('flow_columns.executorNumber')}}：</label>
          <Input
              show-word-limit
              maxlength="100"
              v-model="executorNumber"
              :placeholder="$t('modal.placeholder')"
              style="width: 350px"/>
        </div>
        <div class="item">
          <label>{{$t('flow_columns.executorMemory')}}：</label>
          <Input
              show-word-limit
              maxlength="100"
              v-model="executorMemory"
              :placeholder="$t('modal.placeholder')"
              style="width: 350px"/>
        </div>
        <div class="item">
          <label>{{$t('flow_columns.executorCores')}}：</label>
          <Input
              show-word-limit
              maxlength="100"
              v-model="executorCores"
              :placeholder="$t('modal.placeholder')"
              style="width: 350px"/>
        </div>
        <div class="item">
          <label>GlobalVariable：</label>
          <Select class="select_type" v-model="fieldType" multiple>
            <Option v-for="item in typeList" :value="item.id" :key="item.id">{{ item.name }}</Option>
          </Select>
        </div>
        <div class="item">
          <label class="self">{{$t('flow_columns.description')}}：</label>
          <Input
            v-model="description"
            type="textarea"
            :rows="4"
            :placeholder="$t('modal.placeholder')"
            style="width: 350px"/>
        </div>
      </div>
    </Modal>
  </section>
</template>

<script>
export default {
  name: "flow",
  components: {},
  data() {
    return {
      isOpen: false,
      isTemplateOpen: false,
      page: 1,
      limit: 10,
      total: 0,
      tableData: [],

      param: "",
      templateName: "",

      row: null,
      id: "",
      name: "",
      description: "",
      driverMemory: "1g",
      executorNumber: 1,
      executorMemory: "1g",
      executorCores: 1,
      fieldType: [],
      typeList: [
        {
          id: 'String',
          name: 'test1'
        }
      ],

      promptContent: [
        {
          content: 'Enter',
          icon: 'ios-redo'
        },{
          content: 'Edit',
          icon: 'ios-create-outline'
        },{
          content: 'Run',
          icon: 'ios-play'
        },{
          content: 'Debug',
          icon: 'ios-bug'
        },{
          content: 'Delete',
          icon: 'ios-trash'
        },{
          content: 'Save Template',
          icon: 'md-checkbox-outline'
        }
      ]
    };
  },
  watch: {
    isOpen(state) {
      if (!state) {
        this.handleReset();
      }else if (state && this.id === ''){
        this.getGlobalList(false);
      }
    },
    param(val) {
      this.page = 1;
      this.limit = 10;
      this.getTableData();
    },
  },
  computed: {
    columns() {
      return [
        {
          title: this.$t("flow_columns.name"),
          key: "name",
          sortable: true,
        },
        {
          title: this.$t("flow_columns.description"),
          key: "description",
        },
        {
          title: this.$t("flow_columns.CreateTime"),
          key: "crtDttmString",
          sortable: true,
        },
        {
          title: this.$t("flow_columns.action"),
          slot: "action",
          width: 350,
          align: "center",
        },
      ];
    },
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
      this.row = null;
      this.name = "";
      this.description = "";
      this.driverMemory = "1g";
      this.executorNumber = 1;
      this.executorMemory = "1g";
      this.executorCores = 1;
      this.fieldType =[]
    },

    handleButtonSelect(row, key) {
      switch (key) {
        case 1:
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
          break;
        case 2:
          this.getRowData(row);
          break;
        case 3:
          this.handleRun(row);
          break;
        case 4:
          this.handleDubug(row);
          break;
        case 5:
          this.handleDeleteRow(row);
          break;
        case 6:
          this.row = row;
          this.isTemplateOpen = true;
          break;
        default:
          break;
      }
    },

    // add / update
    handleSaveUpdateData() {
      // test-1
      let data = {
        name: this.name,
        description: this.description,
        driverMemory: this.driverMemory,
        executorNumber: this.executorNumber,
        executorMemory: this.executorMemory,
        executorCores: this.executorCores,
        globalParamsIds: this.fieldType
      };
      if (this.id) {
        //update
        data.id = this.id;
        this.$axios
          .post("/flow/updateFlowBaseInfo", this.$qs.stringify(data))
          .then((res) => {
            if (res.data.code === 200) {
              this.$Modal.success({
                title: this.$t("tip.title"),
                content:
                  `${this.name} ` + this.$t("tip.update_success_content"),
              });
              this.isOpen = false;
              this.handleReset();
              this.getTableData();
            } else {
              this.$Message.error({
                content: `${this.name} ` + this.$t("tip.update_fail_content"),
                duration: 3
              });
            }
          })
          .catch((error) => {
            console.log(error);
            this.$Message.error({
              content: this.$t("tip.fault_content"),
              duration: 3
            });
          });
      } else {
        // add

        this.$axios
          .get("/flow/saveFlowInfo", { params: data })
          .then((res) => {
            if (res.data.code === 200) {
              this.$router.push({
                path: "/drawingBoard",
                query: {
                  src: "/drawingBoard/page/flow/mxGraph/index.html?load=" + res.data.flowId,
                },
              });
              this.isOpen = false;
              this.handleReset();
              this.getTableData();
            } else {
              this.$Message.error({
                content: `${this.name} ` + this.$t("tip.add_fail_content"),
                duration: 3
              });
            }
          })
          .catch((error) => {
            console.log(error);
            this.$Message.error({
              content: this.$t("tip.fault_content"),
              duration: 3
            });
          });
      }
    },
    // Create template
    handletSetEmplate() {
      let data = {
        load: this.row.id,
        name: this.templateName,
        templateType: "TASK",
      };
      this.$axios
        .post("/flowTemplate/saveFlowTemplate", this.$qs.stringify(data))
        .then((res) => {
          if (res.data.code === 200) {
            this.$Modal.success({
              title: this.$t("tip.title"),
              content:
                `${this.templateName} ` + this.$t("tip.save_success_content"),
            });
            this.templateName = "";
            this.row = null;
          } else {
            this.$Message.error({
              content:
                `${this.templateName} ` + this.$t("tip.save_fail_content"),
              duration: 3
            });
          }
        })
        .catch((error) => {
          console.log(error);
          this.$Message.error({
            content: this.$t("tip.fault_content"),
            duration: 3
          });
        });
    },

    handleRun(row) {
      let data = {
        flowId: row.id,
      };
      this.$event.emit("loading", true);
      this.$axios
        .post("/flow/runFlow", this.$qs.stringify(data))
        .then((res) => {
          if (res.data.code === 200) {
            this.$event.emit("loading", false);
            this.$Modal.success({
              title: this.$t("tip.title"),
              content: `${row.name} ` + this.$t("tip.run_success_content"),
              onOk:()=>{
                let src = "";
                src = `/drawingBoard/page/process/mxGraph/index.html?drawingBoardType=PROCESS&load=${res.data.processId}`;
                this.$router.push({
                  path: "/drawingBoard",
                  query: { src },
                });
              }
            });
          } else {
            this.$event.emit("loading", false);
            this.$Message.error({
              content: `${row.name} ` + this.$t("tip.run_fail_content"),
              duration: 3
            });
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
    },

    handleDubug(row) {
      let data = {
        flowId: row.id,
        runMode: "DEBUG",
      };
      this.$event.emit("loading", true);
      this.$axios
        .post("/flow/runFlow", this.$qs.stringify(data))
        .then((res) => {
          this.$event.emit("loading", false);
          if (res.data.code === 200) {
            this.$Modal.success({
              title: this.$t("tip.title"),
              content: `${row.name} ` + this.$t("tip.debug_success_content"),
              onOk:()=>{
                let src = "";
                src = `/drawingBoard/page/process/mxGraph/index.html?drawingBoardType=PROCESS&load=${res.data.processId}`;
                this.$router.push({
                  path: "/drawingBoard",
                  query: { src },
                });
              }
            });
          } else {
            this.$Message.error({
              content: `${row.name} ` + this.$t("tip.debug_fail_content"),
              duration: 3
            });
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
    },

    getRowData(row) {
      let data = { load: row.id };
      this.$event.emit("loading", true);
      this.$axios
        .post("/flow/queryFlowData", this.$qs.stringify(data))
        .then((res) => {
          this.$event.emit("loading", false);
          if (res.data.code === 200) {
            let flow = res.data.flow;
            this.id = flow.id;
            this.name = flow.name;
            this.description = flow.description;
            this.driverMemory = flow.driverMemory;
            this.executorNumber = flow.executorNumber;
            this.executorMemory = flow.executorMemory;
            this.executorCores = flow.executorCores;
            if (!!res.data.globalParamsList && res.data.globalParamsList.length !==0){

              res.data.globalParamsList.forEach(item=>{
                this.fieldType .push(item.id)
              })
              this.getGlobalList(true)
            }else {
              this.getGlobalList(true)
            }
            this.isOpen = true;
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
    // Delete
    handleDeleteRow(row) {
      this.$Modal.confirm({
        title: this.$t("tip.title"),
        okText: this.$t("modal.confirm"),
        cancelText: this.$t("modal.cancel_text"),
        content: `${this.$t("modal.delete_content")} ${row.name}?`,
        onOk: () => {
          let data = {
            id: row.id,
          };
          this.$axios
            .get("/flow/deleteFlow", { params: data })
            .then((res) => {
              if (res.data.code === 200) {
                this.$Modal.success({
                  title: this.$t("tip.title"),
                  content:
                    `${row.name} ` + this.$t("tip.delete_success_content"),
                });
                this.handleReset();
                this.getTableData();
              } else {
                this.$Message.error({
                  content: res.data.errorMsg,
                  duration: 3
                });
              }
            })
            .catch((error) => {
              console.log(error);
              this.$Message.error({
                content: this.$t("tip.fault_content"),
                duration: 3
              });
            });
        }
      });
    },

    getTableData() {
      let data = { page: this.page, limit: this.limit };
      if (this.param) {
        data.param = this.param;
      }
      this.$axios
        .get("/flow/getFlowListPage", {
          params: data,
        })
        .then((res) => {
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
        .catch((error) => {
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

    handleModalSwitch() {
      this.isOpen = !this.isOpen;
    },

    getGlobalList(noData){
      this.$axios
          .get("/flowGlobalParams/globalParamsList")
          .then(res => {
            if (res.data.code === 200) {
              let data = res.data.data;
              this.typeList = data;

              if (!noData){
                this.typeList.forEach(item=>{
                  this.fieldType .push(item.id)
                })
              }

            } else {

            }
          })
          .catch(error => {
            console.log(error);
            this.$Message.error({
              content: this.$t("tip.fault_content"),
              duration: 3
            });
          });
    }
  },
};
</script>
<style lang="scss" scoped>
@import "./index.scss";
.select_type{
  width: 350px;

  /*滚动条整体部分*/
  ::v-deep .ivu-select-dropdown::-webkit-scrollbar {
    width: 6px;
    height: 10px;
  }
  /*滚动条的轨道*/
  ::v-deep .ivu-select-dropdown::-webkit-scrollbar-track {
    background-color: #FFFFFF;
  }
  /*滚动条里面的小方块，能向上向下移动*/
  ::v-deep .ivu-select-dropdown::-webkit-scrollbar-thumb {
    background-color: #ebebeb;
    border-radius: 5px;
    border: 1px solid #F1F1F1;
    box-shadow: inset 0 0 6px rgba(0,0,0,.3);
  }
  ::v-deep .ivu-select-dropdown::-webkit-scrollbar-thumb:hover {
    background-color: #A8A8A8;
  }
  ::v-deep .ivu-select-dropdown::-webkit-scrollbar-thumb:active {
    background-color: #787878;
  }
  /*边角，即两个滚动条的交汇处*/
  ::v-deep .ivu-select-dropdown::-webkit-scrollbar-corner {
    background-color: #FFFFFF;
  }

}

</style>