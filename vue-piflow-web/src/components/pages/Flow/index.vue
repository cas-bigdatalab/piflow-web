<template>
  <section>
    <!-- 头部分 -->
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
    <!-- 检索部分 -->
    <div class="input">
      <Input
        suffix="ios-search"
        v-model="param"
        :placeholder="$t('modal.placeholder')"
        style="width: 300px"
      />
    </div>
    <!-- 表格部分 -->
    <Table border :columns="columns" :data="tableData">
      <template slot-scope="{ row }" slot="action">
        <div>
          <Tooltip content="enter" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,1)">
              <Icon type="ios-redo" />
            </span>
          </Tooltip>
          <Tooltip content="edit" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,2)">
              <Icon type="ios-create-outline" />
            </span>
          </Tooltip>
          <Tooltip content="run" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,3)">
              <Icon type="ios-play" />
            </span>
          </Tooltip>
          <Tooltip content="debug" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,4)">
              <Icon type="ios-bug" />
            </span>
          </Tooltip>
          <Tooltip content="delete" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,5)">
              <Icon type="ios-trash" />
            </span>
          </Tooltip>
          <Tooltip content="Save template" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,6)">
              <Icon type="md-checkbox-outline" />
            </span>
          </Tooltip>
        </div>
      </template>
    </Table>
    <!-- 分页部分 -->
    <div class="page">
      <Page
        :prev-text="$t('page.prev_text')"
        :next-text="$t('page.next_text')"
        show-elevator
        :show-total="true"
        :total="total"
        show-sizer
        @on-change="onPageChange"
        @on-page-size-change="onPageSizeChange"
      />
    </div>
    <!-- 弹窗模板部分 -->
    <Modal
      v-model="isTemplateOpen"
      :title="$t('modal.template_title')"
      :ok-text="$t('modal.ok_text')"
      :cancel-text="$t('modal.cancel_text')"
      @on-ok="handletSetEmplate"
    >
      <div class="modal-warp">
        <div class="item">
          <Input v-model="templateName" :placeholder="$t('modal.placeholder')" />
        </div>
      </div>
    </Modal>
    <!-- 弹窗添加/更新部分 -->
    <Modal
      v-model="isOpen"
      :title="id?$t('flow_columns.update_title'):$t('flow_columns.create_title')"
      :ok-text="$t('modal.ok_text')"
      :cancel-text="$t('modal.cancel_text')"
      @on-ok="handleSaveUpdateData"
    >
      <div class="modal-warp">
        <div class="item">
          <label>{{$t('flow_columns.flow_name')}}：</label>
          <Input v-model="name" :placeholder="$t('modal.placeholder')" style="width: 350px" />
        </div>
        <div class="item">
          <label>{{$t('flow_columns.driverMemory')}}：</label>
          <Input
            v-model="driverMemory"
            :placeholder="$t('modal.placeholder')"
            style="width: 350px"
          />
        </div>
        <div class="item">
          <label>{{$t('flow_columns.executorNumber')}}：</label>
          <Input
            v-model="executorNumber"
            :placeholder="$t('modal.placeholder')"
            style="width: 350px"
          />
        </div>
        <div class="item">
          <label>{{$t('flow_columns.executorMemory')}}：</label>
          <Input
            v-model="executorMemory"
            :placeholder="$t('modal.placeholder')"
            style="width: 350px"
          />
        </div>
        <div class="item">
          <label>{{$t('flow_columns.executorCores')}}：</label>
          <Input
            v-model="executorCores"
            :placeholder="$t('modal.placeholder')"
            style="width: 350px"
          />
        </div>
        <div class="item">
          <label class="self">{{$t('flow_columns.description')}}：</label>
          <Input
            v-model="description"
            type="textarea"
            :rows="4"
            :placeholder="$t('modal.placeholder')"
            style="width: 350px"
          />
        </div>
      </div>
    </Modal>
  </section>
</template>

<script>
// import WaterPoloChart from "./module/WaterPoloChart";

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
      tableData: [
        {
          id: "b0ca399d05004abe9a3321cf7287d9c8",
          name: "text",
          uuid: "b0ca399d05004abe9a3321cf7287d9c8",
          CreateTime: "2020-06-30 13:43:37",
          description: "测试",
          driverMemory: "1g",
          executorNumber: "1",
          executorMemory: "1g",
          executorCores: "1",
          crtDttm: "2020-06-30 13:43:37",
          stopQuantity: 0,
        },
      ],

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
    };
  },
  watch: {
    //控制新增还是更新
    isOpen(state) {
      if (!state) {
        this.handleReset();
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
  mounted() {
    // this.height = size.PageH - 360;
  },
  methods: {
    // 重置
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
          // window.location.href =
          //   window.location.origin +
          //   `/drawingBoard/page/flow/index.html?load=${row.id}`;
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
    // 新增/更新一条数据
    handleSaveUpdateData() {
      let data = {
        name: this.name,
        description: this.description,
        driverMemory: this.driverMemory,
        executorNumber: this.executorNumber,
        executorMemory: this.executorMemory,
        executorCores: this.executorCores,
      };
      if (this.id) {
        //更新数据
        data.id = this.id;
        this.$axios
          .get("/flow/updateFlowInfo", { params: data })
          .then((res) => {
            if (res.data.code == 200) {
              this.$Modal.success({
                title: this.$t("tip.title"),
                content:
                  `${this.name} ` + this.$t("tip.update_success_content"),
              });
              this.isOpen = false;
              this.handleReset();
              this.getTableData();
            } else {
              this.$Modal.error({
                title: this.$t("tip.title"),
                content: `${this.name} ` + this.$t("tip.update_fail_content"),
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
      } else {
        //新增数据
        this.$axios
          .get("/flow/saveFlowInfo", { params: data })
          .then((res) => {
            if (res.data.code == 200) {
              // this.$Modal.success({
              //   title: this.$t("tip.title"),
              //   content: `${this.name} ` + this.$t("tip.add_success_content"),
              //   onOk:()=>{
                  this.$router.push({
                    path: "/drawingBoard",
                    query: {
                      src: "/drawingBoard/page/flow/mxGraph/index.html?load=" + res.data.flowId,
                    },
                  });
                // }
              // });
              this.isOpen = false;
              this.handleReset();
              this.getTableData();
            } else {
              this.$Modal.error({
                title: this.$t("tip.title"),
                content: `${this.name} ` + this.$t("tip.add_fail_content"),
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
    },
    //创建模板
    handletSetEmplate() {
      let data = {
        load: this.row.id,
        name: this.templateName,
        templateType: "TASK",
      };
      this.$axios
        .get("/flowTemplate/saveFlowTemplate", { params: data })
        .then((res) => {
          if (res.data.code == 200) {
            this.$Modal.success({
              title: this.$t("tip.title"),
              content:
                `${this.templateName} ` + this.$t("tip.save_success_content"),
            });
            this.templateName = "";
            this.row = null;
          } else {
            this.$Modal.error({
              title: this.$t("tip.title"),
              content:
                `${this.templateName} ` + this.$t("tip.save_fail_content"),
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
    },
    //开始运行
    handleRun(row) {
      let data = {
        flowId: row.id,
      };
      this.$event.emit("looding", true);
      this.$axios
        .post("/flow/runFlow", this.$qs.stringify(data))
        .then((res) => {
          if (res.data.code == 200) {
            this.$event.emit("looding", false);
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
            this.$event.emit("looding", false);
            this.$Modal.error({
              title: this.$t("tip.title"),
              content: `${row.name} ` + this.$t("tip.run_fail_content"),
            });
          }
        })
        .catch((error) => {
          console.log(error);
          this.$event.emit("looding", false);
          this.$Modal.error({
            title: this.$t("tip.title"),
            content: this.$t("tip.fault_content"),
          });
        });
    },

    //运行Dubug
    handleDubug(row) {
      let data = {
        flowId: row.id,
        runMode: "DEBUG",
      };
      this.$event.emit("looding", true);
      this.$axios
        .post("/flow/runFlow", this.$qs.stringify(data))
        .then((res) => {
          this.$event.emit("looding", false);
          if (res.data.code == 200) {
            this.$Modal.success({
              title: this.$t("tip.title"),
              content: `${row.name} ` + this.$t("tip.debug_success_content"),
            });
          } else {
            this.$Modal.error({
              title: this.$t("tip.title"),
              content: `${row.name} ` + this.$t("tip.debug_fail_content"),
            });
          }
        })
        .catch((error) => {
          console.log(error);
          this.$event.emit("looding", false);
          this.$Modal.error({
            title: this.$t("tip.title"),
            content: this.$t("tip.fault_content"),
          });
        });
    },
    //获取行数据(编辑)
    getRowData(row) {
      this.$event.emit("looding", true);
      this.$axios
        .get("/flow/queryFlowData", {
          params: { load: row.id },
        })
        .then((res) => {
          this.$event.emit("looding", false);
          if (res.data.code == 200) {
            let flow = res.data.flow;
            this.id = flow.id;
            this.name = flow.name;
            this.description = flow.description;
            this.driverMemory = flow.driverMemory;
            this.executorNumber = flow.executorNumber;
            this.executorMemory = flow.executorMemory;
            this.executorCores = flow.executorCores;

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
    //删除某一行数据
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
              if (res.data.code == 200) {
                this.$Modal.success({
                  title: this.$t("tip.title"),
                  content:
                    `${row.name} ` + this.$t("tip.delete_success_content"),
                });
                this.handleReset();
                this.getTableData();
              } else {
                this.$Modal.error({
                  title: this.$t("tip.title"),
                  content: this.$t("tip.delete_fail_content"),
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
        },
        onCancel: () => {
          // this.$Message.info('Clicked cancel');
        },
      });
    },
    //获取表格数据
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
    },
    onPageChange(pageNo) {
      this.page = pageNo;
      this.getTableData();
      // this.spinShow=true;
    },
    onPageSizeChange(pageSize) {
      this.limit = pageSize;
      this.getTableData();
      // this.spinShow=true;
    },
    // 弹窗显隐切换
    handleModalSwitch() {
      this.isOpen = !this.isOpen;
    },
  },
};
</script>
<style lang="scss" scoped>
@import "./index.scss";
</style>

