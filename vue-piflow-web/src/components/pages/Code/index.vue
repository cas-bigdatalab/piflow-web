<template>
  <section>
    <!-- header -->
    <div class="navbar">
      <div class="left">
        <span>{{$t("sidebar.code")}}</span>
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

    <!-- add / update -->
    <Modal
      v-model="isOpen"
      :title="id?$t('code_columns.update_title'):$t('code_columns.create_title')"
      :ok-text="$t('modal.ok_text')"
      :cancel-text="$t('modal.cancel_text')"
      @on-ok="handleSaveUpdateData">
      <div class="modal-warp">
        <Form ref="formCustom" :model="formData" :rules="ruleCustom" :label-colon="true" :label-width="110">
          <FormItem :label="$t('code_columns.code_name')" prop="name">
            <Input
                show-word-limit
                maxlength="100"
                v-model="formData.name"
                :placeholder="$t('modal.placeholder')"
                style="width: 350px" />
          </FormItem>

          <FormItem :label="$t('code_columns.description')">
            <Input
                v-model="formData.description"
                type="textarea"
                :rows="4"
                :placeholder="$t('modal.placeholder')"
                style="width: 350px"/>
          </FormItem>
        </Form>
      </div>
    </Modal>
  </section>
</template>

<script>
export default {
  name: "Code",
  components: {},
  data() {
    const validateName = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('CodeName cannot be empty'));
      }
      this.$axios
          .post("/noteBoot/checkNoteBookName", this.$qs.stringify({noteBookName:value}))
          .then(res => {
            let data = res.data;
            if (data.code === 200) {
              callback();
            } else {
              callback(new Error(data.errorMsg));
            }
          })
          .catch(error => {
            console.log(error);
          });
    }

    return {
      isOpen: false,
      page: 1,
      limit: 10,
      total: 0,
      tableData: [],

      param: "",

      row: null,
      id: "",
      formData: {
        name: "",
        description: "",
      },

      promptContent: [
        {
          content: 'Enter',
          icon: 'ios-redo'
        },{
          content: 'Edit',
          icon: 'ios-create-outline'
        },{
          content: 'Delete',
          icon: 'ios-trash'
        }
      ],

      ruleCustom: {
        name: [
          { validator: validateName, trigger: 'blur' }
        ]
      },

      runSession_timer: null,
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
          title: this.$t("code_columns.name"),
          key: "name",
          sortable: true,
        },
        {
          title: this.$t("code_columns.description"),
          key: "description",
        },
        {
          title: this.$t("code_columns.CreateTime"),
          key: "crtDttmString",
          sortable: true,
        },
        {
          title: this.$t("code_columns.UpdateTime"),
          key: "lastUpdateDttmString",
          sortable: true,
        },
        {
          title: this.$t("code_columns.action"),
          slot: "action",
          width: 200,
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
      this.id = "";
      this.row = null;
      this.formData = {
        name: "",
        description: ""
      };
    },

    handleButtonSelect(row, key) {
      switch (key) {
        case 1:
          this.startSession(row.id);
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
        name: this.formData.name,
        description: this.formData.description,
        codeType: 'scala'
      };
      if (this.id) {
        //update
        data.id = this.id;
        this.$axios
            .post("/noteBoot/saveOrUpdateNoteBook", this.$qs.stringify(data))
            .then((res) => {
              let data = res.data;
              if (data.code === 200) {
                this.$Modal.success({
                  title: this.$t("tip.title"),
                  content:
                      `${this.formData.name} ` + this.$t("tip.update_success_content"),
                });
                this.isOpen = false;
                this.handleReset();
                this.getTableData();
              } else {
                this.$Message.error({
                  content: `${this.formData.name} ` + this.$t("tip.update_fail_content"),
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
          .post("/noteBoot/saveOrUpdateNoteBook", this.$qs.stringify(data))
          .then((res) => {
            if (res.data.code === 200) {
              this.startSession(res.data.noteBookId);
              this.isOpen = false;
              this.handleReset();
              this.getTableData();
            } else {
              this.$Message.error({
                content: `${this.formData.name} ` + this.$t("tip.add_fail_content"),
                duration: 3
              });
            }
          })
          .catch((error) => {
            console.log(error);
          });
      }
    },

    startSession(id){
      let data = {
        noteBookId: id
      };
      this.$event.emit("loading", true);
      this.$axios
          .post("/noteBoot/startNoteBookSession", this.$qs.stringify(data))
          .then((res) => {
            let data = res.data;
            if (data.code === 200){
              this.runSession_timer = window.setInterval(()=>{
                this.getSessionState(id)
              },2000);
            }
          })
          .catch((error) => {
            console.log(error);
          });
    },

    getRowData(row) {
      this.$event.emit("loading", true);
      this.$axios
          .post("/noteBoot/getNoteBookById", this.$qs.stringify({ id: row.id }))
          .then((res) => {
            let data = res.data;
            this.$event.emit("loading", false);
            if (data.code === 200) {
              let code = data.noteBook;
              this.id = code.id;
              this.formData.name = code.name;
              this.formData.description = code.description;
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

    handleDeleteRow(row) {
      this.$Modal.confirm({
        title: this.$t("tip.title"),
        okText: this.$t("modal.confirm"),
        cancelText: this.$t("modal.cancel_text"),
        content: `${this.$t("modal.delete_content")} ${row.name}?`,
        onOk: () => {
          let data = {
            noteBookId: row.id,
          };
          this.$axios
            .post("/noteBoot/deleteNoteBook", this.$qs.stringify(data))
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
        .post("/noteBoot/noteBookListPage", this.$qs.stringify(data))
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

    getSessionState(id) {
      let data = {
        noteBookId: id
      };
      this.$event.emit("loading", true);
      this.$axios
          .post("/noteBoot/getNoteBookSessionState", this.$qs.stringify(data))
          .then((res) => {
            let data = res.data;
            if (data.code === 200){
              data.data = JSON.parse(data.data);
              //  Session状态  not_started,  starting,  idle,  busy, success,  shutting_down,  error,  dead

              switch ( data.data.state ){
                case 'not_started':

                  break;
                case 'starting':

                  break;
                case 'idle':
                  this.$event.emit("loading", false);
                  window.clearInterval(this.runSession_timer);
                  this.$router.push({
                    path: "/codeDetailed",
                    query: { id: id }
                  });
                  break;
                case 'busy':
                  window.clearInterval(this.runSession_timer);
                  this.$event.emit("loading", false);
                  this.$Modal.warning({
                    title: this.$t("tip.title"),
                    content: "Start status BUSY！"
                  });
                  break;
                case 'shutting_down':

                  break;
                case 'error':
                  window.clearInterval(this.runSession_timer);
                  this.$event.emit("loading", false);
                  this.$Modal.warning({
                    title: this.$t("tip.title"),
                    content: "Boot failure！"
                  });
                  break;
                case 'dead':
                  window.clearInterval(this.runSession_timer);
                  this.$event.emit("loading", false);
                  this.$Modal.warning({
                    title: this.$t("tip.title"),
                    content: "Insufficient resources！"
                  });
                  break;
                case 'success':
                  this.$event.emit("loading", false);
                  window.clearInterval(this.runSession_timer);
                  this.$router.push({
                    path: "/codeDetailed",
                    query: { id: id }
                  });
                  break;
              }

            }
          })
          .catch((error) => {
            this.$event.emit("loading", false);
            console.log(error);
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
  },
};
</script>
<style lang="scss" scoped>
@import "./index.scss";
</style>