<template>
  <section>
    <!-- header -->
    <div class="navbar">
      <div class="left">
        <span>{{$t("sidebar.sparkJar")}}</span>
      </div>
      <div class="right">
        <span class="button-warp" @click="handleModalSwitch">
          <Icon type="md-cloud-upload" />
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
        <Tooltip content="Mount" placement="top-start" v-if="row.status.text==='UNMOUNT'">
            <span class="button-warp" @click="handleButtonSelect(row,1)">
              <Icon type="ios-aperture" />
            </span>
        </Tooltip>
        <Tooltip v-else content="Unmount" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,1)">
              <Icon type="md-aperture" />
            </span>
        </Tooltip>
        <Tooltip content="Delete" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,2)">
              <Icon type="ios-trash"/>
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
    <!-- Upload -->
    <Modal
        v-model="isOpen"
        :loading="loading"
        :title="$t('StopHub_columns.upload')"
        :ok-text="$t('modal.upload_text')"
        :cancel-text="$t('modal.cancel_text')"
        @on-ok="uploadSuccess"
        @on-cancel="uploadError">
      <div class="modal-warp">
        <div class="item">
          <Upload
              :action= "this.$url + '/sparkJar/uploadSparkJarFile'"
              :headers="{'Authorization': token}"
              style="width:100%"
              ref="upload"
              :show-upload-list="true"
              :on-success="handleSuccess"
              :on-error="handleError"
              :format="['jar']"
              :before-upload="handleBeforeUpload"
              type="drag">
            <div style="padding: 80px 0; height: 240px">
              <div>
                <Icon type="ios-cloud-upload" size="52" style="color: var(--primary-color)"></Icon>
                <p>{{$t("StopHub_columns.jarDescription")}}</p>
              </div>
            </div>
          </Upload>
        </div>
        <div v-if="file !== null">
          <Icon :color="JarIsShow === false?'red':''" :type="JarIsShow === false?'md-close-circle':''" />
          Upload file: {{ file.name }}
        </div>
      </div>
    </Modal>
  </section>
</template>

<script>
import Cookies from 'js-cookie';
export default {
  name: "sparkJar",
  components: {},
  data() {
    return {
      isOpen: false,
      loading: true,
      page: 1,
      limit: 10,
      total: 0,
      tableData: [],
      param: "",

      file: null,
      JarIsShow: null,
      token: ''
    };
  },
  watch: {
    param() {
      this.page = 1;
      this.limit = 10;
      this.getTableData();
    }
  },
  computed: {
    columns() {
      return [
        {
          title: this.$t("StopHub_columns.name"),
          key: "jarName",
          sortable: true
        },
        {
          title: this.$t("StopHub_columns.version"),
          key: "version",
          sortable: true,
          width: 120,
        },
        {
          title: this.$t("StopHub_columns.jarUrl"),
          key: "jarUrl"
        },
        {
          title: this.$t("StopHub_columns.status"),
          key: "status",
          sortable: true,
          render: (h, params) => {
            return h('span', params.row.status.text);
          },
          width: 150,
        },
        {
          title: this.$t("StopHub_columns.action"),
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
    let token = this.$store.state.variable.token;
    if (!token) {
      token = `${Cookies.get('token')}`
    }
    this.token = `Bearer ${token}`;
  },
  methods: {
    handleButtonSelect(row, key) {
      switch (key) {
        case 1:
          this.handleMount(row);
          break;
        case 2:
          this.handleDeleteRow(row);
          break;

        default:
          break;
      }
    },

    handleSuccess (res, file) {
      this.file = null;
      this.getTableData();
      setTimeout(()=>{
        this.isOpen = false;
      },1000)
    },

    handleError ( error, file) {
      this.JarIsShow = false;
    },

    handleBeforeUpload (file) {
      var testmsg = file.name.substring(file.name.lastIndexOf(".") + 1);
      const extension =
          testmsg === "jar";
      const isLt500M = file.size / 1024 / 1024 < 500;
      if (!extension) {
        this.$Notice.warning({
          title: 'The file format is incorrect',
          desc: 'File format of ' + file.name + ' is incorrect, please select jar.'
        });
        return false;
      }
      if (!isLt500M) {
        this.$Notice.warning({
          title: 'Exceeding file size limit',
          desc: 'File  ' + file.name + ' is too large, no more than 500M.'
        });
        return false;
      } else
        this.file = file;

      return false;
    },

    handleRemove(m, mark) {
      if (mark) {
        this.$Modal.warning({
          title: this.$t("tip.title"),
          content: "此项不可删除，请重新操作！"
        });
        return;
      }
    },

    handleMount(row) {
      let data = { id: row.id };
      let url = "/sparkJar/unmountSparkJar";
      let flag = false;
      if (row.status.text === "UNMOUNT") {
        url = "/sparkJar/mountSparkJar";
        flag = true;
      }

      this.$event.emit("loading", true);
      this.$axios
          .post(url, this.$qs.stringify(data))
          .then(res => {
            if (res.data.code === 200) {
              this.$event.emit("loading", false);
              this.$Modal.success({
                title: this.$t("tip.title"),
                content: flag? `${row.jarName} ` + this.$t("tip.mount_success_content"): `${row.jarName} ` + this.$t("tip.unmount_success_content")
              });
              this.getTableData();
            } else {
              this.$event.emit("loading", false);
              this.$Message.error({
                content: flag? `${row.jarName} ` + this.$t("tip.mount_fail_content"): `${row.jarName} ` + this.$t("tip.unmount_fail_content"),
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
      if ( row.status.text==='MOUNT' ){
        this.$Modal.warning({
          title: this.$t("tip.title"),
          content:
              `${row.jarName} ` +
              'In operation, temporarily unable to delete !'
        });
      }else {
        this.$Modal.confirm({
          title: this.$t("tip.title"),
          okText: this.$t("modal.confirm"),
          cancelText: this.$t("modal.cancel_text"),
          content: `${this.$t("modal.delete_content")} ${row.jarName}?`,
          onOk: () => {
            let data = {
              id: row.id
            };
            this.$axios
                .post("/sparkJar/delSparkJar", this.$qs.stringify(data))
                .then(res => {
                  if (res.data.code === 200) {
                    this.$Modal.success({
                      title: this.$t("tip.title"),
                      content:
                          `${row.jarName} ` +
                          this.$t("tip.delete_success_content")
                    });
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
      }
    },

    uploadSuccess(){
      this.isOpen = true;
      this.$refs.upload.post(this.file);
    },

    uploadError(){
      this.isOpen = false;
      this.$refs.upload.clearFiles();
    },

    getTableData() {
      let data = {page: this.page, limit: this.limit};
      if (this.param) {
        data.param = this.param;
      }
      this.$axios
          .get("/sparkJar/sparkJarListPage", {
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
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
.item {
  display: flex;
  label {
    margin-top: 5px;
  }
}
::v-deep .ivu-upload-drag:hover{
  border: 1px dashed var(--primary-color);
}
</style>