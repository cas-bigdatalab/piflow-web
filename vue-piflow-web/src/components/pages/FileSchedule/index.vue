<template>
  <section>
    <!-- header -->
    <div class="navbar">
      <div class="left">
        <span>{{ $t("sidebar.FileSchedule") }}</span>
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
        style="width: 300px"
      />
    </div>
    <!-- Table button -->
    <Table border :columns="columns" :data="tableData">
      <template slot-scope="{ row }" slot="action">
        <Tooltip content="Enter" placement="top-start">
          <span class="button-warp" @click="handleButtonSelect(row, 1)">
            <Icon type="ios-redo" />
          </span>
        </Tooltip>
        <Tooltip content="Edit" placement="top-start">
          <span class="button-warp" @click="handleButtonSelect(row, 2)">
            <Icon type="ios-create-outline" />
          </span>
        </Tooltip>
        <Tooltip content="Stop" placement="top-start" v-if="row.state === 1">
          <span class="button-warp" @click="handleButtonSelect(row, 3)">
            <Icon type="ios-square" size="12" />
          </span>
        </Tooltip>
        <Tooltip v-else content="Run" placement="top-start">
          <span class="button-warp" @click="handleButtonSelect(row, 3)">
            <Icon type="ios-play" />
          </span>
        </Tooltip>
        <Tooltip content="Delete" placement="top-start">
          <span class="button-warp" @click="handleButtonSelect(row, 4)">
            <Icon type="ios-trash" />
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
        :current="page"
        show-sizer
        @on-change="onPageChange"
        @on-page-size-change="onPageSizeChange"
      />
    </div>
    <!-- add / update -->
    <Modal
      v-model="isOpen"
      :title="
        formValidate.id
          ? $t('flow_schedule.update_title')
          : $t('flow_schedule.create_title')
      "
    >
      <div class="modal-warp Schedule">
        <Form
          ref="formValidate"
          :model="formValidate"
          :rules="ruleValidate"
          :label-width="80"
        >
          <FormItem :label="$t('flow_schedule.scheduleType')">
            <Input
              :value="$t('flow_schedule.file')"
              disabled
              style="width: 340px"
            />
          </FormItem>

          <FormItem :label="$t('flow_schedule.name')" prop="name">
            <Input
              v-model="formValidate.name"
              show-word-limit
              maxlength="100"
              :placeholder="$t('modal.placeholder')"
              style="width: 340px"
            />
          </FormItem>

          <FormItem :label="$t('flow_schedule.description')" prop="description">
            <Input
              v-model="formValidate.description"
              type="textarea"
              :rows="2"
              :placeholder="$t('modal.placeholder')"
              style="width: 340px"
            />
          </FormItem>

          <FormItem :label="$t('flow_schedule.path')" prop="fileDict">
            <Input
              v-model="formValidate.fileDict"
              show-word-limit
              maxlength="100"
              :placeholder="$t('modal.placeholder')"
              style="width: 340px"
            />
          </FormItem>
          <FormItem :label="$t('flow_schedule.prefix')" prop="filePrefix">
            <Input
              v-model="formValidate.filePrefix"
              show-word-limit
              maxlength="100"
              :placeholder="$t('modal.placeholder')"
              style="width: 340px"
            />
          </FormItem>
          <FormItem :label="$t('flow_schedule.suffix')" prop="fileSuffix">
            <Input
              v-model="formValidate.fileSuffix"
              show-word-limit
              maxlength="100"
              :placeholder="$t('modal.placeholder')"
              style="width: 340px"
            />
          </FormItem>

          <FormItem :label="$t('flow_schedule.flowIsGroup')"  prop="associateArr">
            <Cascader :data="subTypeList" v-model="formValidate.associateArr"  style="width: 340px;display: inline-block;" :load-data="loadData"></Cascader>
          </FormItem>

          <FormItem :label="$t('flow_schedule.trigger')" prop="triggerMode">
            <Select v-model="formValidate.triggerMode" style="width: 340px">
              <Option :value="0">{{ $t("flow_schedule.parallel") }}</Option>
              <Option :value="1">{{ $t("flow_schedule.serial") }}</Option>
            </Select>
          </FormItem>

          <FormItem
            v-if="formValidate.triggerMode === 1"
            :label="$t('flow_schedule.serialRule')"
            prop="serialRule"
          >
            <Select v-model="formValidate.serialRule" style="width: 340px">
              <Option :value="0">{{ $t("flow_schedule.fileUpdata") }}</Option>
              <Option :value="1">{{ $t("flow_schedule.fileName") }}</Option>
            </Select>
          </FormItem>

          <FormItem
            v-if="
              formValidate.triggerMode === 1 && formValidate.serialRule === 1
            "
            :label="$t('flow_schedule.regex')"
            prop="regex"
          >
            <Input
              v-model="formValidate.regex"
              show-word-limit
              maxlength="100"
              placeholder="正则表达式，如匹配数字，填写\d+"
              style="width: 340px"
            />
          </FormItem>

          <FormItem
            v-if="formValidate.triggerMode === 1"
            :label="$t('flow_schedule.serialOrder')"
            prop="serialOrder"
          >
            <Select v-model="formValidate.serialOrder" style="width: 340px">
              <Option :value="0">{{
                $t("flow_schedule.ascendingOrder")
              }}</Option>
              <Option :value="1">{{
                $t("flow_schedule.descendingOrder")
              }}</Option>
            </Select>
          </FormItem>
        </Form>
      </div>

      <div slot="footer">
        <Button @click="isOpen = false" class="custom-btn-default">{{
          $t("modal.cancel_text")
        }}</Button>
        <Button @click="handleSaveUpdateData" class="custom-btn-primary">{{
          $t("modal.confirm")
        }}</Button>
      </div>
    </Modal>
  </section>
</template>

<script>
import { getStopsInfoByFlowId } from "@/apis/stops";
export default {
  name: "FlowSchedule",
  components: {},
  data() {
    const validateAssociateArr = (rule, value, callback) => {
      if (value.length < 2) {
        callback(new Error("请选择流水线、组件、参数"));
      }  else {
        callback();
      }
    };

    return {
      isOpen: false,
      page: 1,
      limit: 10,
      total: 0,
      tableData: [],
      modalLoading: true,
      param: "",
      subTypeList: [],
      state: {
        0: "INIT",
        1: "RUNNING",
        2: "STOP",
      },
      triggerList: [
        { label: "并行", value: 0 },
        { label: "串行", value: 1 },
      ],
      formValidate: {
        name: "",
        description: "",
        fileDict: "",
        filePrefix: "",
        fileSuffix: "",
        associateArr: [],
        triggerMode: 0,
        serialRule:'',
        regex:'',
        serialOrder:0,
        
      },
      ruleValidate: {
        name: [
          {
            required: true,
            message: "The name cannot be empty",
            trigger: "blur",
          },
        ],
        fileDict: [
          {
            required: true,
            message: "The path cannot be empty",
            trigger: "blur",
          },
        ],
        associateArr: [
          {
            required: true,
            validator: validateAssociateArr,
            trigger: "change",
          },
        ],
      },
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
    },
  },
  computed: {
    columns() {
      return [
        {
          title: this.$t("flow_schedule.name"),
          key: "name",
        },
        {
          title: this.$t("flow_schedule.path"),
          key: "fileDict",
        },
        {
          title: this.$t("flow_schedule.prefix"),
          key: "filePrefix",
        },
        {
          title: this.$t("flow_schedule.suffix"),
          key: "fileSuffix",
        },
        {
          title: this.$t("flow_schedule.status"),
          key: "status",
          render: (h, params) => {
            return h("span", this.state[params.row.state]);
          },
        },
        {
          title: this.$t("flow_schedule.action"),
          slot: "action",
          width: 230,
          align: "center",
        },
      ];
    },
  },
  created() {
    this.handleGetTemplateData();
    this.getTableData();
  },
  methods: {
    handleReset() {
      this.$refs.formValidate.resetFields();
      this.page = 1;
      this.limit = 10;
      this.formValidate = {
        name: "",
        description: "",
        fileDict: "",
        filePrefix: "",
        fileSuffix: "",
        associateArr: [],
        triggerMode: 0,
        serialRule:'',
        regex:'',
        serialOrder:0,
      };
    },

    handleButtonSelect(row, key) {
      switch (key) {
        case 1:
          let src =
            "/drawingBoard/page/flow/mxGraph/index.html?load=" +
            row.associateId +
            "&BreadcrumbSchedule";
          if (row.type === "FLOW_GROUP") {
            src =
              `/drawingBoard/page/flowGroup/mxGraph/index.html?drawingBoardType=GROUP&load=${row.associateId}` +
              "&BreadcrumbSchedule";
          }
          this.$event.emit("crumb", [
            { name: "flow", path: "/schedule" },
            { name: "drawingBoard", path: "/drawingBoard" },
          ]);
          this.$router.push({
            path: "/drawingBoard",
            query: {
              src: src,
            },
          });
          break;
        case 2:
          this.getRowData(row);
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

    handleSaveUpdateData() {
      this.$refs.formValidate.validate((valid) => {
        if (valid) {
          const data= JSON.parse(JSON.stringify(this.formValidate))
          data.associateId = data.associateArr[0]
          const flow = this.subTypeList.find(v=>v.value === data.associateArr[0])
          const stop = flow.children.find(v=>v.value === data.associateArr[1])
          data.stopId = stop.value
          data.stopName = stop.label
          const property = stop.children.find(v=>v.value === data.associateArr[2])
          data.propertyId = property.value
          data.propertyName = property.label
          delete data.associateArr
          this.$axios
            .post("/schedule/saveFileSchedule", data)
            .then((res) => {
              if (res.data.code === 200) {
                this.$Modal.success({
                  title: this.$t("tip.title"),
                  content:
                    `${data.name} ` + this.$t("tip.update_success_content"),
                });
                this.isOpen = false;
                this.handleReset();
                this.getTableData();
              } else {
                this.$Message.error({
                  content: `${data.name} ` + this.$t("tip.update_fail_content"),
                  duration: 3,
                });
              }
            })
            .catch((error) => {
              console.log(error);
              this.$Message.error({
                content: this.$t("tip.fault_content"),
                duration: 3,
              });
            });
        }
      });
    },

    // 获取列表
    handleGetTemplateData() {
      let data = { page: 1, limit: 10000 };
      this.$axios
        .get("/flow/getFlowListPage", {
          params: data,
        })
        .then((res) => {
          if (res.data.code === 200) {
            this.subTypeList = res.data.data.map((v) => ({
              value: v.id,
              label: v.name,
              children: [],
              loading: false,
              level: 1,
            }));
          } else {
            this.$Message.error({
              content: this.$t("tip.request_fail_content"),
              duration: 3,
            });
          }
        })
        .catch((error) => {
          console.log(error);
          this.$Message.error({
            content: this.$t("tip.fault_content"),
            duration: 3,
          });
        });
    },
    async loadData(item, callback) {
      if (item.level === 1) {
        item.loading = true
       item.children = await this.getStops(item.value);
        item.loading = false
       callback()
      }else if(item.level === 2){
        callback()
      }
    },
    async getStops(id) {
      const res = await getStopsInfoByFlowId(id);
      let children = []
      if (res.data.code === 200) {
       children = res.data.stopsList
          .map((v) => {
            if (v.properties.length) {
              const list = v.properties.map((item) => ({
                value: item.id,
                label: item.name,
              }));

              return {
                value: v.id,
                label: v.name,
                children: list,
                loading: false,
                level: 2,
              };
            } else {
              return "";
            }
          })
          .filter((v) => v);
      }

      return children
    },
    handleStop(row) {
      let url = `/schedule/stopFileSchedule?id=${row.id}`;
      let flag = false;
      if (row.state === 0 || row.state === 2) {
        url = `/schedule/startFileSchedule?id=${row.id}`;
        flag = true;
      }

      this.$event.emit("loading", true);
      this.$axios
        .post(url)
        .then((res) => {
          if (res.data.code === 200) {
            this.$event.emit("loading", false);
            this.$Modal.success({
              title: this.$t("tip.title"),
              content: flag
                ? `${row.name} ` + this.$t("tip.run_success_content")
                : `${row.name} ` + this.$t("tip.stop_success_content"),
            });
            this.getTableData();
          } else {
            this.$event.emit("loading", false);
            this.$Message.error({
              content: flag
                ? `${row.name} ` + this.$t("tip.run_fail_content")
                : `${row.name} ` + this.$t("tip.stop_fail_content"),
              duration: 3,
            });
          }
        })
        .catch((error) => {
          console.log(error);
          this.$event.emit("loading", false);
          this.$Message.error({
            content: this.$t("tip.fault_content"),
            duration: 3,
          });
        });
    },

    getRowData(row) {
      this.$event.emit("loading", true);
      this.$axios
        .get("/schedule/getFileScheduleById", {
          params: { id: row.id },
        })
        .then((res) => {
          if (res.data.code === 200) {
            const {
              id,
              name,
              description,
              fileDict,
              filePrefix,
              fileSuffix,
              associateArr,
              state,
              serialOrder,
              triggerMode,
              regex,
              serialRule,
              version,
              associateId,
              stopId,
              propertyId
            } = res.data.data;
            this.formValidate = {
              id,
              name,
              description,
              fileDict,
              filePrefix,
              fileSuffix,
              associateArr,
              state,
              serialRule,
              serialOrder,
              triggerMode,
              regex,
              version,
              associateArr:[associateId,stopId,propertyId]
            };
            this.$event.emit("loading", false);
            this.isOpen = true;
          } else {
            this.$event.emit("loading", false);
            this.$Message.error({
              content: this.$t("tip.data_fail_content"),
              duration: 3,
            });
          }
        })
        .catch((error) => {
          console.log(error);
          this.$event.emit("loading", false);
          this.$Message.error({
            content: this.$t("tip.fault_content"),
            duration: 3,
          });
        });
    },

    handleDeleteRow(row) {
      if (row.state === 1) {
        this.$Modal.warning({
          title: this.$t("tip.title"),
          content:
            `${row.name} ` + "In operation, temporarily unable to delete !",
        });
      } else {
        this.$Modal.confirm({
          title: this.$t("tip.title"),
          okText: this.$t("modal.confirm"),
          cancelText: this.$t("modal.cancel_text"),
          content: `${this.$t("modal.delete_content")} ${row.name}?`,
          onOk: () => {
            this.$axios
              .post(`/schedule/delFileSchedule?id=${row.id}`)
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
                    content: this.$t("tip.delete_fail_content"),
                    duration: 3,
                  });
                }
              })
              .catch((error) => {
                console.log(error);
                this.$Message.error({
                  content: this.$t("tip.fault_content"),
                  duration: 3,
                });
              });
          },
        });
      }
    },

    getTableData() {
      let data = { page: this.page, limit: this.limit };
      if (this.param) {
        data.keyword = this.param;
      }
      this.$axios
        .get("/schedule/getFileScheduleListByPage", {
          params: data,
        })
        .then((res) => {
          if (res.data.code === 200) {
            this.tableData = res.data.data;
            this.total = res.data.count;
          } else {
            this.$Message.error({
              content: this.$t("tip.request_fail_content"),
              duration: 3,
            });
          }
        })
        .catch((error) => {
          console.log(error);
          this.$Message.error({
            content: this.$t("tip.fault_content"),
            duration: 3,
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
  },
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
