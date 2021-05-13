<template>
  <section>
    <!-- 头部分 -->
    <div class="navbar">
      <div class="left">
        <span>{{$t("sidebar.admin_schedule")}}</span>
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
          <span class="button-warp" @click="handleButtonSelect(row,1)">
            <!-- <Icon type="ios-redo" /> -->
            <Icon v-if="row.state==='RUNNING'" type="ios-square" size="12"/>
            <Icon v-else type="ios-play" />
          </span>
          <span class="button-warp" @click="handleButtonSelect(row,2)">
            <Icon type="ios-create-outline" />
          </span>
          <!-- <span class="button-warp" @click="handleButtonSelect(row,3)">
            <Icon type="ios-play" />
          </span>-->
          <!-- <span class="button-warp" @click="handleButtonSelect(row,4)">
            <Icon type="ios-bug" />
          </span>-->
          <span class="button-warp" @click="handleButtonSelect(row,3)">
            <Icon type="ios-trash" />
          </span>
          <!-- <span class="button-warp" @click="handleButtonSelect(row,6)">
            <Icon type="md-checkbox-outline" />
          </span>-->
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
    <!-- <Modal
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
    </Modal>-->
    <!-- 弹窗添加/更新部分 -->
    <Modal
      v-model="isOpen"
      :title="id?$t('admin_columns.update_title'):$t('admin_columns.create_title')"
      :ok-text="$t('modal.ok_text')"
      :cancel-text="$t('modal.cancel_text')"
      @on-ok="handleSaveUpdateData"
    >
      <div class="modal-warp">
        <div class="item">
          <label>{{$t('admin_columns.jobName')}}：</label>
          <Input
              v-model="jobName"
              show-word-limit
              maxlength="100"
              :placeholder="$t('modal.placeholder')"
              style="width: 350px" />
        </div>
        <div class="item">
          <label>{{$t('admin_columns.jobClass')}}：</label>
          <Input
              v-model="jobClass"
              show-word-limit
              maxlength="100"
              :placeholder="$t('modal.placeholder')"
              style="width: 350px" />
        </div>
        <div class="item">
          <label>{{$t('admin_columns.cronExpression')}}：</label>
          <Input
            v-model="cronExpression"
            show-word-limit
            maxlength="100"
            :placeholder="$t('modal.placeholder')"
            style="width: 350px"
          />
        </div>
        <!--  <div class="item">
          <label>{{$t('modal.executorMemory')}}：</label>
          <Input
            v-model="executorMemory"
            :placeholder="$t('modal.placeholder')"
            style="width: 350px"
          />
        </div>
        <div class="item">
          <label>{{$t('modal.executorCores')}}：</label>
          <Input
            v-model="executorCores"
            :placeholder="$t('modal.placeholder')"
            style="width: 350px"
          />
        </div>-->
        <!-- <div class="item">
          <label class="self">{{$t('admin_columns.description')}}：</label>
          <Input
            v-model="description"
            type="textarea"
            :rows="4"
            :placeholder="$t('modal.placeholder')"
            style="width: 350px"
          />
        </div>-->
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
      // isTemplateOpen: false,
      page: 1,
      limit: 10,
      total: 0,
      tableData: [],

      param: "",
      // templateName: "",

      row: null,
      id: "",
      jobName: "",
      jobClass: "",
      cronExpression: ""
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
    }
  },
  computed: {
    columns() {
      return [
        {
          title: this.$t("admin_columns.name"),
          key: "jobName",
          sortable: true
        },
        {
          title: this.$t("admin_columns.class"),
          key: "jobClass"
        },
        {
          title: this.$t("admin_columns.cron"),
          key: "cronExpression"
          // sortable: true
        },
        {
          title: this.$t("admin_columns.status"),
          key: "state"
          // sortable: true
        },
        {
          title: this.$t("admin_columns.createTime"),
          key: "crtDttm"
          // sortable: true
        },
        {
          title: this.$t("admin_columns.action"),
          slot: "action",
          width: 200,
          align: "center"
        }
      ];
    }
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
      this.jobName = "";
      this.jobClass = "";
      this.cronExpression = "";
      // this.name = "";
      // this.description = "";
      // this.driverMemory = "1g";
      // this.executorNumber = 1;
      // this.executorMemory = "1g";
      // this.executorCores = 1;
    },
    handleButtonSelect(row, key) {
      switch (key) {
        case 1:
          this.handleStop(row);
          break;
        case 2:
          this.getRowData(row);
          break;
        case 3:
          // this.handleRun(row);
          this.handleDeleteRow(row);
          break;
        // case 4:
        //   this.handleDubug(row);
        //   break;
        // case 5:
        //   this.handleDeleteRow(row);
        //   break;
        // case 6:
        //   this.row = row;
        //   this.isTemplateOpen = true;
        //   break;

        default:
          break;
      }
    },
    // 新增/更新一条数据
    handleSaveUpdateData() {
      let data = {
        // name: this.name,
        // description: this.description
        // driverMemory: this.driverMemory,
        // executorNumber: this.executorNumber,
        // executorMemory: this.executorMemory,
        // executorCores: this.executorCores
        jobName: this.jobName,
        jobClass: this.jobClass,
        cronExpression: this.cronExpression
      };
      if (this.id) {
        //更新数据
        data.id = this.id;
        this.$axios
          .get("/sysSchedule/updateTask", { params: data })
          .then(res => {
            if (res.data.code == 200) {
              this.$Modal.success({
                title: this.$t("tip.title"),
                content: `${this.name} ` + this.$t("tip.update_success_content")
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
          .catch(error => {
            console.log(error);
            this.$Message.error({
              content: this.$t("tip.fault_content"),
              duration: 3
            });
          });
      } else {
        //新增数据
        this.$axios
          .get("/sysSchedule/createTask", { params: data })
          .then(res => {
            if (res.data.code == 200) {
              this.$Modal.success({
                title: this.$t("tip.title"),
                content: `${this.name} ` + this.$t("tip.add_success_content")
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
          .catch(error => {
            console.log(error);
            this.$Message.error({
              content: this.$t("tip.fault_content"),
              duration: 3
            });
          });
      }
    },
    //创建模板
    // handletSetEmplate() {
    //   let data = {
    //     load: this.row.id,
    //     name: this.templateName,
    //     templateType: "GROUP"
    //   };
    //   this.$axios
    //     .get("/flowTemplate/saveFlowTemplate", { params: data })
    //     .then(res => {
    //       if (res.data.code == 200) {
    //         this.$Modal.success({
    //           title: this.$t("tip.title"),
    //           content:
    //             `${this.templateName} ` + this.$t("tip.save_success_content")
    //         });
    //         this.templateName = "";
    //         this.row = null;
    //       } else {
    //         this.$Message.error({
    //           title: this.$t("tip.title"),
    //           content:
    //             `${this.templateName} ` + this.$t("tip.save_fail_content")
    //         });
    //       }
    //     })
    //     .catch(error => {
    //       console.log(error);
    //       this.$Message.error({
    //         title: this.$t("tip.title"),
    //         content: this.$t("tip.fault_content")
    //       });
    //     });
    // },
    //开始运行
    // handleRun(row) {
    //   // console.log(row);

    //   let data = {
    //     flowGroupId: row.id
    //   };
    //   this.$event.emit("looding", true);
    //   this.$axios
    //     .post("/flowGroup/runFlowGroup", this.$qs.stringify(data))
    //     .then(res => {
    //       if (res.data.code == 200) {
    //         this.$event.emit("looding", false);
    //         this.$Modal.success({
    //           title: this.$t("tip.title"),
    //           content: `${row.name} ` + this.$t("tip.run_success_content")
    //         });
    //       } else {
    //         this.$event.emit("looding", false);
    //         this.$Message.error({
    //           title: this.$t("tip.title"),
    //           content: `${row.name} ` + this.$t("tip.run_fail_content")
    //         });
    //       }
    //     })
    //     .catch(error => {
    //       console.log(error);
    //       this.$event.emit("looding", false);
    //       this.$Message.error({
    //         title: this.$t("tip.title"),
    //         content: this.$t("tip.fault_content")
    //       });
    //     });
    // },

    //运行停止/开始
    handleStop(row) {
      // console.log();
      let data = { sysScheduleId: row.id };
      let url = "/sysSchedule/stopTask";
      if (row.state === "STOP") {
        data.processType =
          row.processType === "TASK" ? "PROCESS" : "PROCESS_GROUP";
        url = "/sysSchedule/startTask";
      }

      this.$event.emit("looding", true);
      this.$axios
        .post(url, this.$qs.stringify(data))
        .then(res => {
          if (res.data.code == 200) {
            this.$event.emit("looding", false);
            this.$Modal.success({
              title: this.$t("tip.title"),
              content: `${row.jobName} ` + this.$t("tip.stop_success_content")
            });
            this.getTableData();
          } else {
            this.$event.emit("looding", false);
            this.$Modal.success({
              title: this.$t("tip.title"),
              content: `${row.jobName} ` + this.$t("tip.stop_fail_content")
            });
          }
        })
        .catch(error => {
          console.log(error);
          this.$event.emit("looding", false);
          this.$Message.error({
            content: this.$t("tip.fault_content"),
            duration: 3
          });
        });
    },
    //获取行数据(编辑)
    getRowData(row) {
      this.$event.emit("looding", true);
      this.$axios
        .get("/sysSchedule/getScheduleById", {
          params: { scheduleId: row.id }
        })
        .then(res => {
          this.$event.emit("looding", false);
          if (res.data.code == 200) {
            let data = res.data.sysScheduleVo;
            this.id = data.id;
            // this.name = flow.name;
            this.jobName = data.jobName;
            this.jobClass = data.jobClass;
            this.cronExpression = data.cronExpression;
            // this.description = flow.description;
            // this.driverMemory = flow.driverMemory;
            // this.executorNumber = flow.executorNumber;
            // this.executorMemory = flow.executorMemory;
            // this.executorCores = flow.executorCores;
            this.$event.emit("looding", false);
            this.isOpen = true;
          } else {
            this.$Message.error({
              content: this.$t("tip.get_fail_content"),
              duration: 3
            });
          }
        })
        .catch(error => {
          this.$event.emit("looding", false);
          console.log(error);
          this.$Message.error({
            content: this.$t("tip.fault_content"),
            duration: 3
          });
        });
    },
    //删除某一行数据
    handleDeleteRow(row) {
      this.$Modal.confirm({
        title: this.$t("tip.title"),
        okText: this.$t("modal.confirm"),
        cancelText: this.$t("modal.cancel_text"),
        content: `${this.$t("modal.delete_content")} ${row.jobName}?`,
        onOk: () => {
          let data = {
            sysScheduleId: row.id
          };
          this.$axios
            .get("/sysSchedule/deleteTask", { params: data })
            .then(res => {
              if (res.data.code == 200) {
                this.$Modal.success({
                  title: this.$t("tip.title"),
                  content:
                    `${row.jobName} ` + this.$t("tip.delete_success_content")
                });
                this.handleReset();
                this.getTableData();
              } else {
                this.$Message.error({
                  content: this.$t("tip.delete_fail_content"),
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
        onCancel: () => {
          // this.$Message.info('Clicked cancel');
        }
      });
    },
    //获取表格数据
    getTableData() {
      let data = { page: this.page, limit: this.limit };
      if (this.param) {
        data.param = this.param;
      }
      this.$axios
        .get("/sysSchedule/getScheduleListPage", {
          params: data
        })
        .then(res => {
          if (res.data.code == 200) {
            let data = res.data.data;
            this.tableData = data.map(item => {
              item.state = item.status.text;
              return item;
            });
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
    }
  }
};
</script>
<style lang="scss" scoped>
@import "./index.scss";
</style>

