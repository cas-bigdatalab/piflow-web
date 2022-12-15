<template>
  <section>
    <div class="navbar">
      <div class="left">
        <span>{{$t("sidebar.globalVariable")}}</span>
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
        <Tooltip content="Edit" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,1)">
            <Icon type="ios-create-outline" />
          </span>
        </Tooltip>
        <Tooltip content="Delete" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,2)">
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
        :title="id?$t('globalVariable_columns.update_title'):$t('globalVariable_columns.create_title')"
        :ok-text="$t('modal.ok_text')"
        :cancel-text="$t('modal.cancel_text')"
        @on-ok="handleSaveUpdateData">
      <div class="modal-warp">
        <div class="item">
          <label>{{$t('globalVariable_columns.name')}}：</label>
          <Input
              v-model="name"
              show-word-limit
              maxlength="100"
              :placeholder="$t('modal.placeholder')"
              style="width: 350px"/>
        </div>
        <div class="item">
          <label>{{$t('globalVariable_columns.type')}}：</label>
          <Select class="select_type" v-model="fieldType">
            <Option v-for="item in typeList" :value="item.value" :key="item.value">{{ item.label }}</Option>
          </Select>
        </div>
        <div class="item">
          <label class="self">{{$t('globalVariable_columns.content')}}：</label>
          <Input
              v-model="variableValue"
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
  name: "globalVariable",
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
      name: "",
      variableValue: "",
      fieldType: "",
      typeList: [
        {
          value: 'String',
          label: 'String'
        },
        {
          value: 'Int',
          label: 'Int'
        },
        {
          value: 'Float',
          label: 'Float'
        },
        {
          value: 'Boolean',
          label: 'Boolean'
        },
        {
          value: 'Date',
          label: 'Date'
        },
        {
          value: 'Char',
          label: 'Char'
        },
        {
          value: 'Double',
          label: 'Double'
        },
        {
          value: 'Byte',
          label: 'Byte'
        },
        {
          value: 'Short',
          label: 'Short'
        },
        {
          value: 'Long',
          label: 'Long'
        }
      ],

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
          title: this.$t("globalVariable_columns.name"),
          key: "name",
          sortable: true
        },
        {
          title: this.$t("globalVariable_columns.type"),
          key: "type"
        },
        {
          title: this.$t("globalVariable_columns.content"),
          key: "content"
        },
        {
          title: this.$t("globalVariable_columns.createTime"),
          key: "crtDttm"
        },
        {
          title: this.$t("globalVariable_columns.action"),
          slot: "action",
          width: 150,
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
      this.name = "";
      this.variableValue = "";
      this.fieldType = "";

    },

    handleButtonSelect(row, key) {
      switch (key) {
        case 1:
          this.getRowData(row);
          break;
        case 2:
          this.handleDeleteRow(row);
          break;
        default:
          break;
      }
    },

    handleSaveUpdateData() {
      let data = {
        name: this.name,
        content: this.variableValue,
        type: this.fieldType
      };

      if (this.id) {
        data.id = this.id;
        this.$axios
            .post("/flowGlobalParams/updateGlobalParams", this.$qs.stringify(data))
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
            .post("/flowGlobalParams/addGlobalParams", this.$qs.stringify(data))
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

    getRowData(row) {
      this.$event.emit("loading", true);
      let Params = {
        id: row.id
      }
      this.$axios
          .post("/flowGlobalParams/getGlobalParamsById", this.$qs.stringify(Params))
          .then(res => {
            this.$event.emit("loading", false);
            if (res.data.code === 200) {
              let data = res.data.globalParams;
              this.id = data.id;
              this.name = data.name;
              this.variableValue = data.content;
              this.fieldType = data.type;

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
        content: `${this.$t("modal.delete_content")} ${row.name}?`,
        onOk: () => {
          let data = {
            id: row.id
          };
          this.$axios
              .post("/flowGlobalParams/delGlobalParams", this.$qs.stringify(data))
              .then(res => {
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
          .get("/flowGlobalParams/globalParamsListPage", {
            params: data
          })
          .then(res => {
            if (res.data.code === 200) {
              let data = res.data.data;
              this.tableData = data
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
.select_type{
  width: 350px;

  /*滚动条整体部分*/
  ::v-deep .ivu-select-dropdown::-webkit-scrollbar {
    width: 6px;
    height: 10px;
  }
  /*滚动条的轨道*/
  ::v-deep .ivu-select-dropdown::-webkit-scrollbar-track {
    background-color: #FFFFFF;
  }
  /*滚动条里面的小方块，能向上向下移动*/
  ::v-deep .ivu-select-dropdown::-webkit-scrollbar-thumb {
    background-color: #ebebeb;
    border-radius: 5px;
    border: 1px solid #F1F1F1;
    box-shadow: inset 0 0 6px rgba(0,0,0,.3);
  }
  ::v-deep .ivu-select-dropdown::-webkit-scrollbar-thumb:hover {
    background-color: #A8A8A8;
  }
  ::v-deep .ivu-select-dropdown::-webkit-scrollbar-thumb:active {
    background-color: #787878;
  }
  /*边角，即两个滚动条的交汇处*/
  ::v-deep .ivu-select-dropdown::-webkit-scrollbar-corner {
    background-color: #FFFFFF;
  }

}
</style>

