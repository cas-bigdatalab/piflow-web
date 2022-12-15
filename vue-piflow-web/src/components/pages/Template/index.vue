<template>
  <section>
    <!-- header -->
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
          :show-upload-list="false">
          <Icon type="md-cloud-upload" />
        </Upload>
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
  </section>
</template>

<script>
export default {
  name: "templates",
  components: {},
  data() {
    return {
      page: 1,
      limit: 10,
      total: 0,
      tableData: [],
      headers: {
        Authorization: `Bearer ${this.$store.state.variable.token}`
      },
      param: "",

      promptContent: [
        {
          content: 'Download',
          icon: 'md-cloud-download'
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
  methods: {
    handleReset() {
      this.page = 1;
      this.limit = 10;
    },

    handleButtonSelect(row, key) {
      switch (key) {
        case 1:
          this.handleDownload(row);
          break;
        case 2:
          this.handleDeleteRow(row);
          break;

        default:
          break;
      }
    },

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
              this.$event.emit("loading", false);
              if (res.data.code === 200) {
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
              this.$event.emit("loading", false);
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
        .get("/flowTemplate/flowTemplatePage", {
          params: data
        })
        .then(res => {
          this.$event.emit("loading", false);
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
          this.$event.emit("loading", false);
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

    handleDownload(row) {
      this.$axios
        .get("/flowTemplate/templateDownload?flowTemplateId=" + row.id)
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
          this.$Message.error({
            content: row.name + " " + this.$t("tip.download_fail_content"),
            duration: 3
          });
        });
    },

    handleFileSuccess(response, file, fileList) {
      this.$event.emit("loading", false);
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
      this.$event.emit("loading", false);
      this.$Message.error({
        content: this.$t("tip.upload_fail_content"),
        duration: 3
      });
    },

    handleFileBefore() {
      this.$event.emit("loading", true);
    }
  }
}
</script>
<style lang="scss" scoped>
@import "./index.scss";
</style>

