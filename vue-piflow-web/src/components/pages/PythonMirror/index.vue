<template>
  <section>
    <div class="navbar">
      <div class="left">
        <span>{{ $t("sidebar.pythonMirror") }}</span>
      </div>
      <div class="right">
        <span class="button-warp" @click="handleAdd">
          <Icon type="md-add"/>
        </span>
      </div>
    </div>
    <Table border :columns="columns" :data="tableData">
      <template slot-scope="{ row }" slot="action">
        <div style="text-align: left">
          <Tooltip
            v-for="(item, index) in promptContent"
            :key="index"
            :content="item.content"
            placement="top-start"
          >
            <span
              class="button-warp"
              @click="handleButtonSelect(row, item.content)"
            >
              <Icon :type="item.icon" />
            </span>
          </Tooltip>
        </div>
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

    <!-- Modal -->
    <Modal
      v-model="open"
      width="520px"
      :title="
        this.mode === 'edit'
          ? $t('python_mirror.update_title')
          : $t('python_mirror.create_title')
      "
      class="custom-modal"
    >
      <div style="width: 100%; height: 100%">
        <Form
          class="formData"
          ref="formRef"
          :model="formData"
          :label-width="100"
        >
          <Form-item :label="$t('python_mirror.name')" prop="baseImageName">
            <Input
              v-model="formData.baseImageName"
              show-word-limit
              maxlength="100"
              :placeholder="$t('modal.placeholder')"
            ></Input>
          </Form-item>

          <Form-item
            :label="$t('python_mirror.version')"
            prop="baseImageVersion"
          >
            <Input
              v-model="formData.baseImageVersion"
              show-word-limit
              maxlength="100"
              :placeholder="$t('modal.placeholder')"
            ></Input>
          </Form-item>

          <Form-item
            :label="$t('python_mirror.description')"
            prop="baseImageDescription"
          >
            <Input
              v-model="formData.baseImageDescription"
              type="textarea"
              :rows="3"
              :placeholder="$t('modal.placeholder')"
            ></Input>
          </Form-item>

          <Form-item :label="$t('python_mirror.harborUser')" prop="harborUser">
            <Input
              v-model="formData.harborUser"
              show-word-limit
              maxlength="100"
              :placeholder="$t('modal.placeholder')"
            ></Input>
          </Form-item>

          <Form-item
            :label="$t('python_mirror.password')"

            prop="harborPassword"
          >
            <Input
              v-model="formData.harborPassword"
              type="password" password
              :placeholder="$t('modal.placeholder')"
            ></Input>
          </Form-item>
        </Form>
      </div>
      <div slot="footer">
        <Button @click="reset()" class="custom-btn-default">{{
          $t("modal.cancel_text")
        }}</Button>
        <Button @click="handleSubmit" class="custom-btn-primary">{{
          $t("modal.confirm")
        }}</Button>
      </div>
    </Modal>
  </section>
</template>

<script>
export default {
  name: "InteractiveAnalysisAudit",
  data() {
    return {
      page: 1,
      limit: 10,
      total: 0,
      mode:'add',
      tableData: [],
      open: false,
      formData: {
        baseImageName: "",
        baseImageVersion: "",
        baseImageDescription: "",
        harborUser: "",
        harborPassword: "",
      },
      promptContent: [
        {
          content: "Edit",
          icon: "ios-create-outline",
        },
        {
          content: "Delete",
          icon: "ios-trash",
        },
      ],
    };
  },
  created() {
    this.getTableData();
  },
  computed: {
    columns() {
      return [
        {
          title: this.$t("python_mirror.name"),
          key: "baseImageName",
        },
        {
          title: this.$t("python_mirror.version"),
          key: "baseImageVersion",
        },
        {
          title: this.$t("python_mirror.description"),
          key: "baseImageDescription",
        },
        {
          title: this.$t("python_mirror.createTime"),
          key: "crtDttm",
        },
        {
          title: this.$t("python_mirror.action"),
          slot: "action",
          width: 140,
          align: "center",
        },
      ];
    },
  },
  methods: {
    handleButtonSelect(row, action) {
      switch (action) {
        case "Edit":
          this.handleEdit(row);
          break;
        case "Delete":
          this.handleDelete(row);
          break;
      }
    },

    handleAdd(){
      this.mode = 'add'
      this.reset()
        this.open  = true
    },
    handleEdit(row) {
      this.mode = 'edit'
      const {baseImageName,baseImageVersion,baseImageDescription,harborUser,harborPassword} = row
        this.formData = {baseImageName,baseImageVersion,baseImageDescription,harborUser,harborPassword}
        this.open  = true
    },
    handleDelete(row) {
      this.$Modal.confirm({
        title: this.$t("tip.title"),
        okText: this.$t("modal.confirm"),
        cancelText: this.$t("modal.cancel_text"),
        content: `${this.$t("modal.delete_content")} ${row.baseImageName}?`,
        onOk: () => {
          this.$axios
            .post("/dockerimage/deleteBaseImage", this.$qs.stringify({baseImageName:row.baseImageName}))
            .then((res) => {
              if (res.data.code === 200) {
                this.$Modal.success({
                  title: this.$t("tip.title"),
                  content:
                    `${row.baseImageName} ` +
                    this.$t("tip.delete_success_content"),
                });
                this.reset();
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
            });
        },
      });
    },

    getTableData() {
      let params = { page: this.page, limit: this.limit };
      this.$axios
        .get("/dockerimage/getBaseImageListByPage", { params })
        .then((res) => {
          this.$event.emit("loading", false);
          if (res.status === 200 && res.data.code == 200) {
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
          this.$event.emit("loading", false);
          this.$Message.error({
            content: this.$t("tip.fault_content"),
            duration: 3,
          });
        });
    },

    // 发送审核请求
    handleSubmit() {
      const url = this.mode === 'add'? '/dockerimage/uploadBaseImage':'/dockerimage/updateBaseImage'
      this.$event.emit("loading", true);
      this.$axios
        ({
          method:'post',
         url,
         params:this.formData
        })
        .then((res) => {
          this.$event.emit("loading", false);
          if (res.data.code === 200) {
            this.reset();
            this.$Message.success({
              content: this.$t("tip.operate_success"),
              duration: 3,
            });
            this.page = 1;
            this.getTableData();
          } else {
            this.$Message.error({
              content: res.data.errorMsg,
              duration: 3,
            });
          }
        })
        .catch((error) => {
          this.$event.emit("loading", false);
          console.log(error);
        });
    },
    // form 重置
    reset() {
      this.open = false;
      this.formData = {
        baseImageName: "",
        baseImageVersion: "",
        baseImageDescription: "",
        harborUser: "",
        harborPassword: "",
      };
    },
    onPageChange(pageNo) {
      this.page = pageNo;
      this.getTableData();
    },

    onPageSizeChange(pageSize) {
      this.limit = pageSize;
      this.getTableData();
    },
  },
};
</script>


<style lang="scss">
@import "./index.scss";
</style>