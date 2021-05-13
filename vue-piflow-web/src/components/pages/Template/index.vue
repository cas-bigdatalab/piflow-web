<template>
  <section>
    <!-- 头部分 -->
    <div class="navbar">
      <div class="left">
        <span>{{$t("sidebar.template")}}</span>
      </div>
      <div class="right">
        <Upload
          class="button-warp"
          :action="$url+'/flowTemplate/uploadXmlFile'"
          :on-success="handleFileSuccess"
          :on-error="handleFileError"
          :headers="headers"
          :before-upload="handleFileBefore"
          :show-upload-list="false"
        >
          <Icon type="md-cloud-upload" />
        </Upload>
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
          <!-- <span class="button-warp" @click="handleButtonSelect(row,1)">
            <Icon type="ios-redo" />
          </span>
          <span class="button-warp" @click="handleButtonSelect(row,2)">
            <Icon type="ios-create-outline" />
          </span>
          <span class="button-warp" @click="handleButtonSelect(row,3)">
            <Icon type="ios-play" />
          </span>
          <span class="button-warp" @click="handleButtonSelect(row,4)">
            <Icon type="ios-bug" />
          </span>-->
          <Tooltip content="Download" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,1)">
              <Icon type="md-cloud-download" />
            </span>
          </Tooltip>

          <Tooltip content="Delete" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,2)">
              <Icon type="ios-trash" />
            </span>
          </Tooltip>

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
  </section>
</template>

<script>
// import WaterPoloChart from "./module/WaterPoloChart";

export default {
  name: "sdd",
  components: {},
  data() {
    return {
      // isOpen: false,
      // isTemplateOpen: false,
      page: 1,
      limit: 10,
      total: 0,
      tableData: [],
      headers: {
        Authorization: `Bearer ${this.$store.state.variable.token}`
      },
      param: ""
      // templateName: "",

      // row: null,
      // id: "",
      // name: "",
      // description: "",
      // driverMemory: "1g",
      // executorNumber: 1,
      // executorMemory: "1g",
      // executorCores: 1
    };
  },
  watch: {
    //控制新增还是更新
    // isOpen(state) {
    //   if (!state) {
    //     this.handleReset();
    //   }
    // },
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
          title: this.$t("template_columns.name"),
          key: "name",
          sortable: true
        },
        {
          title: this.$t("template_columns.crtDttm"),
          key: "crtDttm"
        },
        {
          title: this.$t("template_columns.action"),
          slot: "action",
          width: 180,
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
      // this.id = "";
      // this.row = null;
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
          this.handleDownload(row);
          break;
        case 2:
          this.handleDeleteRow(row);
          break;
        // case 3:
        //   this.handleRun(row);
        //   break;
        // case 4:
        //   this.handleDubug(row);
        //   break;
        // case 5:
        //   this.handleDeleteRow(row);
        //   break;
        // // case 6:
        // //   this.row = row;
        // //   this.isTemplateOpen = true;
        // //   break;

        default:
          break;
      }
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
            id: row.id
          };
          this.$axios
            .get("/flowTemplate/deleteFlowTemplate", { params: data })
            .then(res => {
              this.$event.emit("looding", false);
              if (res.data.code == 200) {
                this.$Modal.success({
                  title: this.$t("tip.title"),
                  content:
                    `${row.name} ` + this.$t("tip.delete_success_content")
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
              this.$event.emit("looding", false);
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
        .get("/flowTemplate/flowTemplatePage", {
          params: data
        })
        .then(res => {
          this.$event.emit("looding", false);
          if (res.data.code == 200) {
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
          this.$event.emit("looding", false);
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

    // handleUpload() {
    //   this.$axios
    //     .post("/flow/runFlow", this.$qs.stringify(data))
    //     .then(res => {
    //       if (res.data.code == 200) {
    //         this.$Modal.success({
    //           title: this.$t("tip.title"),
    //           content: `${row.name} ` + this.$t("tip.debug_success_content")
    //         });
    //       } else {
    //         this.$event.emit("looding", false);
    //         this.$Message.error({
    //           title: this.$t("tip.title"),
    //           content: `${row.name} ` + this.$t("tip.debug_fail_content")
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
    // 下载模板
    handleDownload(row) {
      this.$axios
        .get(
                "/flowTemplate/templateDownload?flowTemplateId=" + row.id
        )
        .then(res => {
          var blob = new Blob([res.data]);
          var a = document.createElement("a");
          var url = window.URL.createObjectURL(blob);
          a.href = url;
          a.download = row.name + '.xml';
          a.click();
          window.URL.revokeObjectURL(url);
          this.$Modal.success({
            title: this.$t("tip.title"),
            content: row.name + " " + this.$t("tip.download_success_content")
          });
        })
        .catch(error => {
          console.log(error);
          //  console.log(file);
          // this.$event.emit("looding", false);
          this.$Message.error({
            content: row.name + " " + this.$t("tip.download_fail_content"),
            duration: 3
          });
        });
    },
    // 上传模板
    handleFileSuccess(response, file, fileList) {
      //  `${row.name} ` +
      this.$event.emit("looding", false);
      if (response.code === 200) {
        this.$Modal.success({
          title: this.$t("tip.title"),
          content: this.$t("tip.upload_success_content")
        });
        this.getTableData();
      } else {
        this.$Message.error({
          content: this.$t("tip.upload_fail_content"),
          duration: 3
        });
      }
    },
    handleFileError(error, file, fileList) {
      console.log(error);
      //  console.log(file);
      this.$event.emit("looding", false);
      this.$Message.error({
        content: this.$t("tip.upload_fail_content"),
        duration: 3
      });
    },
    //上传文件之前的钩子，参数为上传的文件，若返回 false 或者 Promise 则停止上传
    handleFileBefore() {
      this.$event.emit("looding", true);
    }
  }
};
</script>
<style lang="scss" scoped>
@import "./index.scss";
</style>

