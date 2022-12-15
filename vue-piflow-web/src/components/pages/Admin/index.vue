<template>
  <section>
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
    <div class="input">
      <Input
        suffix="ios-search"
        v-model="param"
        :placeholder="$t('modal.placeholder')"
        style="width: 300px"
      />
    </div>
    <Table border :columns="columns" :data="tableData">
      <template slot-scope="{ row }" slot="action">
        <Tooltip v-if="row.state==='RUNNING'" content="Stop" placement="top-start">
             <span class="button-warp" @click="handleButtonSelect(row,1)">
               <Icon type="ios-square" size="12"/>
             </span>
        </Tooltip>
        <Tooltip v-else content="Run" placement="top-start">
             <span class="button-warp" @click="handleButtonSelect(row,1)">
               <Icon type="ios-play"/>
             </span>
        </Tooltip>
        <Tooltip content="Edit" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,2)">
            <Icon type="ios-create-outline" />
          </span>
        </Tooltip>
        <Tooltip content="Delete" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,3)">
            <Icon type="ios-trash" />
            </span>
        </Tooltip>
      </template>
    </Table>
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
    <Modal
      v-model="isOpen"
      :title="id?$t('admin_columns.update_title'):$t('admin_columns.create_title')"
      :ok-text="$t('modal.ok_text')"
      :cancel-text="$t('modal.cancel_text')"
      @on-ok="handleSaveUpdateData">
      <div class="modal-warp">
        <div class="item">
          <label>{{$t('admin_columns.jobName')}}：</label>
          <Input
              v-model="jobName"
              show-word-limit
              maxlength="100"
              :placeholder="$t('modal.placeholder')"
              style="width: 350px"/>
        </div>
        <div class="item">
          <label>{{$t('admin_columns.jobClass')}}：</label>
          <Input
              v-model="jobClass"
              show-word-limit
              maxlength="100"
              :placeholder="$t('modal.placeholder')"
              style="width: 350px"/>
        </div>
        <div class="item">
          <label>{{$t('admin_columns.cronExpression')}}：</label>
          <Input
            v-model="cronExpression"
            show-word-limit
            maxlength="100"
            :placeholder="$t('modal.placeholder')"
            style="width: 350px"/>
        </div>
      </div>
    </Modal>
  </section>
</template>

<script>
export default {
  name: "admin",
  components: {},
  data() {
    return {
      isOpen: false,
      page: 1,
      limit: 10,
      total: 0,
      tableData: [],
      param: "",

      row: null,
      id: "",
      jobName: "",
      jobClass: "",
      cronExpression: ""
    };
  },
  watch: {
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
        },
        {
          title: this.$t("admin_columns.status"),
          key: "state"
        },
        {
          title: this.$t("admin_columns.createTime"),
          key: "crtDttm"
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
  methods: {
    handleReset() {
      this.page = 1;
      this.limit = 10;
      this.id = "";
      this.jobName = "";
      this.jobClass = "";
      this.cronExpression = "";
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
          this.handleDeleteRow(row);
          break;

        default:
          break;
      }
    },

    handleSaveUpdateData() {
      let data = {
        jobName: this.jobName,
        jobClass: this.jobClass,
        cronExpression: this.cronExpression
      };
      if (this.id) {
        data.id = this.id;
        this.$axios
          .get("/sysSchedule/updateTask", { params: data })
          .then(res => {
            if (res.data.code === 200) {
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
        this.$axios
          .get("/sysSchedule/createTask", { params: data })
          .then(res => {
            if (res.data.code === 200) {
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

    handleStop(row) {
      let data = { sysScheduleId: row.id };
      let url = "/sysSchedule/stopTask";
      if (row.state === "STOP") {
        data.processType = row.processType === "TASK" ? "PROCESS" : "PROCESS_GROUP";
        url = "/sysSchedule/startTask";
      }

      this.$event.emit("loading", true);
      this.$axios
        .post(url, this.$qs.stringify(data))
        .then(res => {
          if (res.data.code === 200) {
            this.$event.emit("loading", false);
            this.$Modal.success({
              title: this.$t("tip.title"),
              content: `${row.jobName} ` + this.$t("tip.stop_success_content")
            });
            this.getTableData();
          } else {
            this.$event.emit("loading", false);
            this.$Modal.success({
              title: this.$t("tip.title"),
              content: `${row.jobName} ` + this.$t("tip.stop_fail_content")
            });
          }
        })
        .catch(error => {
          console.log(error);
          this.$event.emit("loading", false);
          this.$Message.error({
            content: this.$t("tip.fault_content"),
            duration: 3
          });
        });
    },

    getRowData(row) {
      this.$event.emit("loading", true);
      this.$axios
        .get("/sysSchedule/getScheduleById", { params: { scheduleId: row.id }})
        .then(res => {
          this.$event.emit("loading", false);
          if (res.data.code === 200) {
            let data = res.data.sysScheduleVo;
            this.id = data.id;
            this.jobName = data.jobName;
            this.jobClass = data.jobClass;
            this.cronExpression = data.cronExpression;
            this.$event.emit("loading", false);
            this.isOpen = true;
          } else {
            this.$Message.error({
              content: this.$t("tip.get_fail_content"),
              duration: 3
            });
          }
        })
        .catch(error => {
          this.$event.emit("loading", false);
          console.log(error);
          this.$Message.error({
            content: this.$t("tip.fault_content"),
            duration: 3
          });
        });
    },

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
              if (res.data.code === 200) {
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
        }
      })
    },

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
          if (res.data.code === 200) {
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
      this.getTableData()
    },

    onPageSizeChange(pageSize) {
      this.limit = pageSize;
      this.getTableData()
    },

    handleModalSwitch() {
      this.isOpen = !this.isOpen;
    }
  }
};
</script>
<style lang="scss" scoped>
@import "./index.scss";
</style>

