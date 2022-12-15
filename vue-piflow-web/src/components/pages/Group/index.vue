<template>
  <section>
    <!-- header -->
    <div class="navbar">
      <div class="left">
        <span>{{$t("sidebar.group")}}</span>
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
        @on-page-size-change="onPageSizeChange"
      />
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
      :title="id?$t('group_columns.update_title'):$t('group_columns.create_title')"
      :ok-text="$t('modal.ok_text')"
      :cancel-text="$t('modal.cancel_text')"
      @on-ok="handleSaveUpdateData">
      <div class="modal-warp">
        <div class="item">
          <label>{{$t('group_columns.group_name')}}：</label>
          <Input
              v-model="name"
              show-word-limit
              maxlength="100"
              :placeholder="$t('modal.placeholder')"
              style="width: 350px" />
        </div>
        <div class="item">
          <label class="self">{{$t('group_columns.description')}}：</label>
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
export default {
  name: "group",
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
          title: this.$t("group_columns.name"),
          key: "name",
          sortable: true
        },
        {
          title: this.$t("group_columns.description"),
          key: "description"
        },
        {
          title: this.$t("group_columns.CreateTime"),
          key: "crtDttmString",
          sortable: true
        },
        {
          title: this.$t("group_columns.action"),
          slot: "action",
          width: 350,
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
      this.row = null;
      this.name = "";
      this.description = "";
    },

    handleButtonSelect(row, key) {
      switch (key) {
        case 1:
           this.$event.emit("crumb", [
            { name: "Group", path: "/group" },
            { name: "drawingBoard", path: "/drawingBoard" }
          ]);
         this.$router.push({
           path: "/drawingBoard",
           query: {
             src:`/drawingBoard/page/flowGroup/mxGraph/index.html?drawingBoardType=GROUP&load=${row.id}`
           }
         });
          break;
        case 2:
          this.getRowData(row);
          break;
        case 3:
          this.handleRun(row);
          break;
        case 4:
          this.handleDeleteRow(row);
          break;
        case 5:
          this.row = row;
          this.isTemplateOpen = true;
          break;
        default:
          break;
      }
    },

    handleSaveUpdateData() {
      let data = {
        name: this.name,
        description: this.description
      };
      if (this.id) {
        //update
        data.id = this.id;
        this.$axios
          .get("/flowGroup/saveOrUpdateFlowGroup", { params: data })
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
        // add
        this.$axios
          .get("/flowGroup/saveOrUpdateFlowGroup", { params: data })
          .then(res => {
            if (res.data.code === 200) {
              // this.$Modal.success({
              //   title: this.$t("tip.title"),
              //   content: `${this.name} ` + this.$t("tip.add_success_content"),
              //   onOk:()=>{
                  this.$router.push({
                    path: "/drawingBoard",
                    query: {
                      src:`/drawingBoard/page/flowGroup/mxGraph/index.html?drawingBoardType=GROUP&load=${res.data.flowGroupId}`
                    }
                  });
              //   }
              // });
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

    handletSetEmplate() {
      let data = {
        load: this.row.id,
        name: this.templateName,
        templateType: "GROUP"
      };
      this.$axios
        .post("/flowTemplate/saveFlowTemplate", this.$qs.stringify(data))
        .then(res => {
          if (res.data.code === 200) {
            this.$Modal.success({
              title: this.$t("tip.title"),
              content:
                `${this.templateName} ` + this.$t("tip.save_success_content")
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
        .catch(error => {
          console.log(error);
          this.$Message.error({
            content: this.$t("tip.fault_content"),
            duration: 3
          });
        });
    },

    handleRun(row) {
      let data = {
        flowGroupId: row.id
      };
      this.$event.emit("loading", true);
      this.$axios
        .post("/flowGroup/runFlowGroup", this.$qs.stringify(data))
        .then(res => {
          if (res.data.code === 200) {
            this.$event.emit("loading", false);
            this.$Modal.success({
              title: this.$t("tip.title"),
              content: `${row.name} ` + this.$t("tip.run_success_content"),
              onOk:()=>{
                let src = "";
                src = `/drawingBoard/page/processGroup/mxGraph/index.html?drawingBoardType=PROCESS&load=${res.data.processGroupId}`;
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
      let data = { load: row.id }
      this.$event.emit("loading", true);
      this.$axios
        .post("/flowGroup/queryFlowGroupData", this.$qs.stringify(data))
        .then(res => {
          if (res.data.code === 200) {
            let flowGroupVo = res.data.flowGroupVo;
            this.id = flowGroupVo.id;
            this.name = flowGroupVo.name;
            this.description = flowGroupVo.description;
            this.$event.emit("loading", false);
            this.isOpen = true;
          } else {
            this.$event.emit("loading", false);
            this.$Message.error({
              content: this.$t("tip.get_fail_content"),
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
            .get("/flowGroup/deleteFlowGroup", { params: data })
            .then(res => {
              if (res.data.code === 200) {
                this.handleReset();
                this.getTableData();
                this.$Modal.success({
                  title: this.$t("tip.title"),
                  content:
                    `${row.name} ` + this.$t("tip.delete_success_content")
                });
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
      });
    },

    getTableData() {
      let data = { page: this.page, limit: this.limit };
      if (this.param) {
        data.param = this.param;
      }
      this.$axios
        .get("/flowGroup/getFlowGroupListPage", {
          params: data
        })
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

    handleModalSwitch() {
      this.isOpen = !this.isOpen;
    }
  }
};
</script>
<style lang="scss" scoped>
@import "./index.scss";
</style>

