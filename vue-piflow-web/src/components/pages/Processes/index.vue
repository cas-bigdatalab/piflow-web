<template>
  <section>
    <!-- header -->
    <div class="navbar">
      <div class="left">
        <span>{{$t("sidebar.processes")}}</span>
      </div>
    </div>
    <!-- search -->
    <div class="input">
      <Input
        suffix="ios-search"
        v-model="param"
        :placeholder="$t('modal.placeholder')"
        style="width: 300px"/>
      <div class="left">
        <Icon
          type="ios-apps"
          size="16"
          @click="handleChangeStyle('a')"
          :style="{color:mark==='a'?'#666':'#bbb'}"/>
        <Icon
          type="md-reorder"
          size="22"
          @click="handleChangeStyle('b')"
          :style="{color:mark==='b'?'#666':'#bbb'}"/>
      </div>
    </div>
    <!-- Table button -->
    <Table border :columns="columns" :data="tableData" v-if="mark==='b'">
      <template slot-scope="{ row }" slot="progress">
        <div style="display: flex;align-items: center;">
          <progress max="100" :value="row.progress"></progress>
          <span style="margin-left:10px;">{{row.progress}}%</span>
        </div>
      </template>

      <template slot-scope="{ row }" slot="action">
        <Tooltip v-for="(item, index) in promptContent" :key="index" :content="item.content" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,index+1)">
              <Icon :type="item.icon" />
            </span>
        </Tooltip>
      </template>
    </Table>
    <div class="card-list" v-else>
      <Row :gutter="16" style="width: 100%">
        <Col span="6" v-for="(item,i) in tableData" :key="'df'+i">
          <Card style="margin-bottom: 20px">
            <div slot="title">
              <span v-if="item.name">{{item.name}}</span>
              <span v-else style="visibility: hidden">a</span>
            </div>
            <img src="../../../assets/img/20200714234723.png" class="img-style" />
            <div slot="extra">
              <Dropdown trigger="click">
            <span class="md-more">
              <Icon type="md-more" size="20" />
            </span>
                <DropdownMenu slot="list">
                  <DropdownItem v-for="(items, index) in promptContent" :key="index" @click.native="handleButtonSelect(item,1)">
                    <Icon :type="items.icon" />
                    <span>{{ items.content }}</span>
                  </DropdownItem>
                </DropdownMenu>
              </Dropdown>
            </div>
          </Card>
        </Col>
      </Row>
    </div>
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
  </section>
</template>

<script>
export default {
  name: "processes",
  components: {},
  data() {
    return {
      page: 1,
      limit: 10,
      total: 0,
      tableData: [],

      param: "",
      mark: "b",

      promptContent: [
        {
          content: 'Enter',
          icon: 'ios-redo'
        },{
          content: 'Run',
          icon: 'ios-play'
        },{
          content: 'Stop',
          icon: 'ios-square'
        },{
          content: 'Delete',
          icon: 'ios-trash'
        }
      ]
    };
  },
  watch: {
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
          title: this.$t("progress_columns.name"),
          key: "name",
          sortable: true,
        },
        {
          title: this.$t("progress_columns.description"),
          key: "description",
        },
        {
          title: this.$t("progress_columns.startTime"),
          key: "crtDttm",
          sortable: true,
        },
        {
          title: this.$t("progress_columns.endTime"),
          key: "lastUpdateDttm",
          sortable: true,
        },
        {
          title: this.$t("progress_columns.progress"),
          slot: "progress",
          width: 250,
        },
        {
          title: this.$t("progress_columns.processType"),
          key: "processType",
        },
        {
          title: this.$t("progress_columns.state"),
          key: "state",
          width: 125,
        },
        {
          title: this.$t("progress_columns.action"),
          slot: "action",
          width: 225,
          align: "center",
        },
      ];
    },
  },
  created() {
    this.getTableData();
  },
  methods: {
    handleReset() {
      this.page = 1;
      this.limit = 10;
    },

    handleButtonSelect(row, key) {
      switch (key) {
        case 1:
          this.$event.emit("crumb", [
            { name: "Process", path: "/processes" },
            { name: "drawingBoard", path: "/drawingBoard" },
          ]);

          // 根据类型进入不同的界面
          let src = "";
          if (row.processType === "TASK") {
            src = `/drawingBoard/page/process/mxGraph/index.html?drawingBoardType=PROCESS&processType=${row.processType}&load=${row.id}`;
          } else if (row.processType === "GROUP") {
            src = `/drawingBoard/page/processGroup/mxGraph/index.html?drawingBoardType=PROCESS&processType=${row.processType}&load=${row.id}`;
          };

          this.$router.push({
            path: "/drawingBoard",
            query: { src },
          });
          break;
        case 2:
          this.handleRun(row);
          break;
        case 3:
          this.handleStop(row);
          break;
        case 4:
          this.handleDeleteRow(row);
          break;

        default:
          break;
      }
    },

    handleChangeStyle(val) {
      this.mark = val;
    },

    handleRun(row) {
      let data = {
        id: row.id,
        processType: row.processType === "TASK" ? "PROCESS" : "PROCESS_GROUP",
        runMode: "RUN",
      };
      this.$event.emit("loading", true);
      this.$axios
        .post(
          "/processAndProcessGroup/runProcessOrProcessGroup",
          this.$qs.stringify(data)
        )
        .then((res) => {
          if (res.data.code === 200) {
            this.$event.emit("loading", false);
            this.$Modal.success({
              title: this.$t("tip.title"),
              content: `${row.name} ` + this.$t("tip.run_success_content"),
              onOk:()=>{
                // 根据类型进入不同的界面
                let src = "";
                if (row.processType === "TASK") {
                  src = `/drawingBoard/page/process/mxGraph/index.html?drawingBoardType=PROCESS&processType=${row.processType}&load=${res.data.processId}`;
                } else if (row.processType === "GROUP") {
                  src = `/drawingBoard/page/processGroup/mxGraph/index.html?drawingBoardType=PROCESS&processType=${row.processType}&load=${res.data.processGroupId}`;
                }

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

    handleStop(row) {
      let data = {
        processType: row.processType === "TASK" ? "PROCESS" : "PROCESS_GROUP",
        id: row.id,
      };
      this.$event.emit("loading", true);
      this.$axios
        .post(
          "/processAndProcessGroup/stopProcessOrProcessGroup",
          this.$qs.stringify(data)
        )
        .then((res) => {
          if (res.data.code === 200) {
            this.$event.emit("loading", false);
            this.$Modal.success({
              title: this.$t("tip.title"),
              content: `${row.name} ` + this.$t("tip.stop_success_content"),
              onOk:()=>{
                this.getTableData();
              }
            });
          } else {
            this.$event.emit("loading", false);
            this.$Modal.success({
              title: this.$t("tip.title"),
              content: `${row.name} ` + this.$t("tip.stop_fail_content"),
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

    handleUpdateProgress() {
      let url = "";
      this.tableData.forEach((item) => {
        if (item.state === "STARTED" && item.processType === "TASK") {
          url = url + "taskAppIds=" + item.appId + "&";
        }
        if (item.state === "STARTED" && item.processType === "GROUP") {
          url = url + "groupAppIds=" + item.appId + "&";
        }
      });
      if (url=== '') {
       return;
      }
      this.$axios
        .get(
          "/processAndProcessGroup/getAppInfoList?" +
            url.slice(0, url.length - 1)
        )
        .then((res) => {
          if (res.data.code === 200) {
            // this.$Modal.success({
            //   title: this.$t("tip.title"),
            //   content: `${row.name} ` + this.$t("tip.debug_success_content")
            // });
          } else {
            // this.$event.emit("loading", false);
            // this.$Message.error({
            //   title: this.$t("tip.title"),
            //   content: `${row.name} ` + this.$t("tip.debug_fail_content")
            // });
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

    handleDeleteRow(row) {
      this.$Modal.confirm({
        title: this.$t("tip.title"),
        okText: this.$t("modal.confirm"),
        cancelText: this.$t("modal.cancel_text"),
        content: `${this.$t("modal.delete_content")} ${row.name}?`,
        onOk: () => {
          let data = {
            id: row.id,
            processType:
              row.processType === "TASK" ? "PROCESS" : "PROCESS_GROUP",
          };
          this.$axios
            .get("/processAndProcessGroup/delProcessOrProcessGroup", {
              params: data,
            })
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
                  content: `${row.name} ` + this.$t("tip.delete_fail_content"),
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
      })
    },

    getTableData() {
      let data = { page: this.page, limit: this.limit };
      if (this.param) {
        data.param = this.param;
      }
      this.$axios
        .get("/processAndProcessGroup/processAndProcessGroupListPage", {
          params: data,
        })
        .then((res) => {
          if (res.data.code === 200) {
            this.tableData = res.data.data;
            this.total = res.data.count;
            this.handleUpdateProgress();
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
    }
  }
};
</script>
<style lang="scss" scoped>
@import "./index.scss";
</style>

