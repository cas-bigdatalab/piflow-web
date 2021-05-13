<template>
  <section>
    <!-- 头部分 -->
    <div class="navbar">
      <div class="left">
        <span>{{$t("sidebar.processes")}}</span>
      </div>
      <!-- <div class="right">
        <span class="button-warp" @click="handleModalSwitch">
          <Icon type="md-add" />
        </span>
      </div>-->
    </div>
    <!-- 检索部分 -->
    <div class="input">
      <Input
        suffix="ios-search"
        v-model="param"
        :placeholder="$t('modal.placeholder')"
        style="width: 300px"
      />
      <div class="left">
        <Icon
          type="ios-apps"
          size="16"
          @click="handleChangeStyle('a')"
          :style="{color:mark==='a'?'#666':'#bbb'}"
        />
        <Icon
          type="md-reorder"
          size="22"
          @click="handleChangeStyle('b')"
          :style="{color:mark==='b'?'#666':'#bbb'}"
        />
      </div>
    </div>
    <!-- 表格部分 -->
    <Table border :columns="columns" :data="tableData" v-if="mark==='b'">
      <!-- 进程 -->
      <template slot-scope="{ row }" slot="progress">
        <div style="display: flex;align-items: center;">
          <progress max="100" :value="row.progress"></progress>
          <span style="margin-left:10px;">{{row.progress}}%</span>
        </div>
      </template>
      <!-- 操作按钮 -->
      <template slot-scope="{ row }" slot="action">
        <div>
          <Tooltip content="Enter" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,1)">
              <Icon type="ios-redo" />
            </span>
          </Tooltip>
          <Tooltip content="Run" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,2)">
              <Icon type="ios-play" />
            </span>
          </Tooltip>
          <Tooltip content="Stop" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,3)">
              <Icon type="ios-square" />
            </span>
          </Tooltip>
          <Tooltip content="Delete" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,4)">
              <Icon type="ios-trash" />
            </span>
          </Tooltip>
        </div>
      </template>
    </Table>
    <div class="card-list" v-else>
      <Row :gutter="16">
        <Col span="6" v-for="(item,i) in tableData" :key="'df'+i">
          <Card style="margin-bottom: 20px">
            <div slot="title">
              <!-- <Icon type="ios-film-outline"></Icon> -->
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
                  <DropdownItem @click.native="handleButtonSelect(item,1)">
                    <Icon type="ios-redo" />
                    <span>Jump</span>
                  </DropdownItem>
                  <DropdownItem @click.native="handleButtonSelect(item,2)">
                    <Icon type="ios-play" />
                    <span>Run</span>
                  </DropdownItem>
                  <DropdownItem @click.native="handleButtonSelect(item,3)">
                    <Icon type="ios-square" />
                    <span>Stop</span>
                  </DropdownItem>
                  <DropdownItem @click.native="handleButtonSelect(item,4)">
                    <Icon type="ios-trash" />
                    <span>Delete</span>
                  </DropdownItem>
                </DropdownMenu>
              </Dropdown>
            </div>
            <!--   <ul>
                <li>
                    <a :href="item.url" target="_blank">{{ item.name }}</a>
                    <span>
                        <Icon type="ios-star" v-for="n in 4" :key="n"></Icon><Icon type="ios-star" v-if="item.rate >= 9.5"></Icon><Icon type="ios-star-half" v-else></Icon>
                        {{ item.rate }}
                    </span>
                </li>
            </ul>-->
          </Card>
        </Col>
      </Row>



<!--      <Card style="width:19%" v-for="(item,i) in tableData" :key="'df'+i">-->
<!--        <div slot="title">-->
<!--          &lt;!&ndash; <Icon type="ios-film-outline"></Icon> &ndash;&gt;-->
<!--          {{item.name}}-->
<!--        </div>-->
<!--        <img src="../../../assets/img/20200714234723.png" class="img-style" />-->
<!--        <div slot="extra">-->
<!--          <Dropdown trigger="click">-->
<!--            <span class="md-more">-->
<!--              <Icon type="md-more" size="20" />-->
<!--            </span>-->
<!--            <DropdownMenu slot="list">-->
<!--              <DropdownItem @click.native="handleButtonSelect(item,1)">-->
<!--                <Icon type="ios-redo" />-->
<!--                <span>Jump</span>-->
<!--              </DropdownItem>-->
<!--              <DropdownItem @click.native="handleButtonSelect(item,2)">-->
<!--                <Icon type="ios-play" />-->
<!--                <span>Run</span>-->
<!--              </DropdownItem>-->
<!--              <DropdownItem @click.native="handleButtonSelect(item,3)">-->
<!--                <Icon type="ios-square" />-->
<!--                <span>Stop</span>-->
<!--              </DropdownItem>-->
<!--              <DropdownItem @click.native="handleButtonSelect(item,4)">-->
<!--                <Icon type="ios-trash" />-->
<!--                <span>Delete</span>-->
<!--              </DropdownItem>-->
<!--            </DropdownMenu>-->
<!--          </Dropdown>-->
<!--        </div>-->
<!--        &lt;!&ndash;   <ul>-->
<!--            <li>-->
<!--                <a :href="item.url" target="_blank">{{ item.name }}</a>-->
<!--                <span>-->
<!--                    <Icon type="ios-star" v-for="n in 4" :key="n"></Icon><Icon type="ios-star" v-if="item.rate >= 9.5"></Icon><Icon type="ios-star-half" v-else></Icon>-->
<!--                    {{ item.rate }}-->
<!--                </span>-->
<!--            </li>-->
<!--        </ul>&ndash;&gt;-->
<!--      </Card>-->
    </div>
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
    <!-- <Modal
      v-model="isOpen"
      :title="id?$t('modal.udate_title'):$t('modal.create_title')"
      :ok-text="$t('modal.ok_text')"
      :cancel-text="$t('modal.cancel_text')"
      @on-ok="handleSaveUpdateData"
    >
      <div class="modal-warp">
        <div class="item">
          <label>{{$t('modal.flow_name')}}：</label>
          <Input v-model="name" :placeholder="$t('modal.placeholder')" style="width: 350px" />
        </div>
        <div class="item">
          <label>{{$t('modal.driverMemory')}}：</label>
          <Input
            v-model="driverMemory"
            :placeholder="$t('modal.placeholder')"
            style="width: 350px"
          />
        </div>
        <div class="item">
          <label>{{$t('modal.executorNumber')}}：</label>
          <Input
            v-model="executorNumber"
            :placeholder="$t('modal.placeholder')"
            style="width: 350px"
          />
        </div>
        <div class="item">
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
        </div>
        <div class="item">
          <label class="self">{{$t('modal.description')}}：</label>
          <Input
            v-model="description"
            type="textarea"
            :rows="4"
            :placeholder="$t('modal.placeholder')"
            style="width: 350px"
          />
        </div>
      </div>
    </Modal>-->
  </section>
</template>

<script>
// import WaterPoloChart from "./module/WaterPoloChart";

export default {
  name: "processes",
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
          // uuid: "b0ca399d05004abe9a3321cf7287d9c8",
          // crtDttmString: "2020-06-30 13:43:37",
          description: "测试",
          // driverMemory: "1g",
          // executorNumber: "1",
          // executorMemory: "1g",
          // executorCores: "1",
          crtDttm: "2020-06-30 13:43:37",
          // stopQuantity: 0
        },
      ],

      param: "",
      templateName: "",

      row: null,
      id: "",
      mark: "b",
      name: "",
      description: "",
      // driverMemory: "1g",
      // executorNumber: 1,
      // executorMemory: "1g",
      // executorCores: 1
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
      // this.driverMemory = "1g";
      // this.executorNumber = 1;
      // this.executorMemory = "1g";
      // this.executorCores = 1;
    },
    handleButtonSelect(row, key) {
      // console.log(row);
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
    //开始运行
    handleRun(row) {
      let data = {
        id: row.id,
        processType: row.processType === "TASK" ? "PROCESS" : "PROCESS_GROUP",
        runMode: "RUN",
      };
      this.$event.emit("looding", true);
      this.$axios
        .post(
          "/processAndProcessGroup/runProcessOrProcessGroup",
          this.$qs.stringify(data)
        )
        .then((res) => {
          if (res.data.code == 200) {
            this.$event.emit("looding", false);
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
                };

                this.$router.push({
                  path: "/drawingBoard",
                  query: { src },
                });
              }
            });
          } else {
            this.$event.emit("looding", false);
            this.$Message.error({
              content: `${row.name} ` + this.$t("tip.run_fail_content"),
              duration: 3
            });
          }
        })
        .catch((error) => {
          console.log(error);
          this.$event.emit("looding", false);
          this.$Message.error({
            content: this.$t("tip.fault_content"),
            duration: 3
          });
        });
    },
    //运行停止
    handleStop(row) {
      let data = {
        processType: row.processType === "TASK" ? "PROCESS" : "PROCESS_GROUP",
        id: row.id,
      };
      this.$event.emit("looding", true);
      this.$axios
        .post(
          "/processAndProcessGroup/stopProcessOrProcessGroup",
          this.$qs.stringify(data)
        )
        .then((res) => {
          if (res.data.code == 200) {
            this.$event.emit("looding", false);
            this.$Modal.success({
              title: this.$t("tip.title"),
              content: `${row.name} ` + this.$t("tip.stop_success_content"),
              onOk:()=>{
                this.getTableData();
              }
            });
          } else {
            this.$event.emit("looding", false);
            this.$Modal.success({
              title: this.$t("tip.title"),
              content: `${row.name} ` + this.$t("tip.stop_fail_content"),
            });
          }
        })
        .catch((error) => {
          console.log(error);
          this.$event.emit("looding", false);
          this.$Message.error({
            content: this.$t("tip.fault_content"),
            duration: 3
          });
        });
    },
    //更新进程
    handleUpdateProgress() {
      let arrayObj = [];
      let url = "";

      let data = {};
      this.tableData.forEach((item) => {
        if (item.state == "STARTED" && item.processType == "TASK") {
          // data['taskAppIds'] = item.appId;
          // data['groupAppIds'] = item.processType;
          // arrayObj.push(item.appId);
          url = url + "taskAppIds=" + item.appId + "&";
        }
        if (item.state == "STARTED" && item.processType == "GROUP") {
          // data['taskAppIds'] = item.appId;
          // data['groupAppIds'] = item.processType;
          // arrayObj.push(item.appId);
          url = url + "groupAppIds=" + item.appId + "&";
        }
      });
      this.$axios
        .get(
          "/processAndProcessGroup/getAppInfoList?" +
            url.slice(0, url.length - 1)
        )
        .then((res) => {
          if (res.data.code == 200) {
            // this.$Modal.success({
            //   title: this.$t("tip.title"),
            //   content: `${row.name} ` + this.$t("tip.debug_success_content")
            // });
          } else {
            // this.$event.emit("looding", false);
            // this.$Message.error({
            //   title: this.$t("tip.title"),
            //   content: `${row.name} ` + this.$t("tip.debug_fail_content")
            // });
          }
        })
        .catch((error) => {
          console.log(error);
          // this.$event.emit("looding", false);
          this.$Message.error({
            content: this.$t("tip.fault_content"),
            duration: 3
          });
        });
    },
    //获取行数据(编辑)
    // getRowData(row) {
    //   this.$event.emit("looding", true);
    //   this.$axios
    //     .get("/flow/queryFlowData", {
    //       params: { load: row.id }
    //     })
    //     .then(res => {
    //       if (res.data.code == 200) {
    //         let flow = res.data.flow;
    //         this.id = flow.id;
    //         this.name = flow.name;
    //         this.description = flow.description;
    //         this.driverMemory = flow.driverMemory;
    //         this.executorNumber = flow.executorNumber;
    //         this.executorMemory = flow.executorMemory;
    //         this.executorCores = flow.executorCores;
    //         this.$event.emit("looding", false);
    //         this.isOpen = true;
    //       } else {
    //       }
    //     })
    //     .catch(error => {
    //       console.log(error);
    //     });
    // },
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
            processType:
              row.processType === "TASK" ? "PROCESS" : "PROCESS_GROUP",
          };
          this.$axios
            .get("/processAndProcessGroup/delProcessOrProcessGroup", {
              params: data,
            })
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
        .get("/processAndProcessGroup/processAndProcessGroupListPage", {
          params: data,
        })
        .then((res) => {
          if (res.data.code == 200) {
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
      // this.spinShow=true;
    },
    onPageSizeChange(pageSize) {
      this.limit = pageSize;
      this.getTableData();
      // this.spinShow=true;
    },
    // 弹窗显隐切换
    // handleModalSwitch() {
    //   this.isOpen = !this.isOpen;
    // }
  },
};
</script>
<style lang="scss" scoped>
@import "./index.scss";
</style>

