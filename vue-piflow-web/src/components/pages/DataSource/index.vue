<template>
  <section>
    <!-- 头部分 -->
    <div class="navbar">
      <div class="left">
        <span>{{$t("sidebar.data_source")}}</span>
      </div>
      <div class="right">
        <span class="button-warp" @click="handleModalSwitch">
          <Icon type="md-add" />
        </span>
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
          </span>-->
          <Tooltip content="Edit" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,1)">
              <Icon type="ios-create-outline" />
            </span>
          </Tooltip>

          <!--  <span class="button-warp" @click="handleButtonSelect(row,3)">
            <Icon type="ios-play" />
          </span>
         <span class="button-warp" @click="handleButtonSelect(row,4)">
            <Icon type="ios-bug" />
          </span>-->
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
    <Modal
      v-model="isOpen"
      :title="id?$t('dataSource_columns.update_title'):$t('dataSource_columns.create_title')"
      :ok-text="$t('modal.ok_text')"
      :cancel-text="$t('modal.cancel_text')"
      @on-ok="handleSaveUpdateData"
    >
      <div class="modal-warp">
        <div class="item">
          <label>{{$t('dataSource_columns.type')}}：</label>
          <!-- <Input v-model="type" :placeholder="$t('modal.placeholder')" style="width: 350px" /> -->
          <Select v-model="type" style="width:350px" @on-change="handleSelectChange">
            <Option
              v-for="item in typeList"
              :value="item.dataSourceType"
              :key="item.id"
            >{{ item.dataSourceType }}</Option>
          </Select>
        </div>
        <div class="item">
          <label>{{$t('dataSource_columns.dataSource_name')}}：</label>
          <Input
              v-model="name"
              show-word-limit
              maxlength="100"
              :placeholder="$t('modal.placeholder')"
              style="width: 350px" />
        </div>
        <div class="item">
          <label class="self">{{$t('dataSource_columns.description')}}：</label>
          <Input
            v-model="description"
            type="textarea"
            :rows="4"
            :placeholder="$t('modal.placeholder')"
            style="width: 350px"
          />
        </div>
        <div class="item" v-if="type==='Other'">
          <label class="self" style="margin-top:15px;">{{$t('dataSource_columns.addProperty')}}：</label>
          <ul class="relationship">
            <li v-for="(item,m) in dataSourcePropertyVoList" :key="'ve'+m">
              <Input
                v-model="item.name"
                show-word-limit
                maxlength="100"
                :placeholder="$t('modal.placeholder')"
                style="width: 100px"
              />
              <Input
                v-model="item.value"
                show-word-limit
                maxlength="100"
                :placeholder="$t('modal.placeholder')"
                style="width: 180px"
              />
              <Icon
                @click="handleRemove(m,dataSourcePropertyVoList.length===1)"
                type="ios-remove-circle-outline"
              />
              <Icon
                v-if="m==(dataSourcePropertyVoList.length-1)"
                @click="handleAdd"
                type="ios-add-circle-outline"
              />
            </li>
          </ul>
        </div>
        <div v-else>
          <div class="item" v-for="(item,m) in dataSourcePropertyVoList" :key="'vqe'+m">
            <label class="self">{{item.name}}：</label>
            <Input
              v-model="item.value"
              :show-word-limit="item.name === 'url' ? false : true"
              maxlength="100"
              :placeholder="$t('modal.placeholder')"
              style="width: 350px"
            />
          </div>
        </div>
      </div>
    </Modal>
  </section>
</template>

<script>
// import WaterPoloChart from "./module/WaterPoloChart";

export default {
  name: "flow",
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
      type: "Other",
      description: "",

      typeList: [],
      dataSourcePropertyVoList: [
        {
          name: "",
          value: "",
          id: ''
        }
      ],
      editDataSourceList: []
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
    }
  },
  computed: {
    columns() {
      return [
        {
          title: this.$t("dataSource_columns.name"),
          key: "dataSourceName",
          sortable: true
        },
        {
          title: this.$t("dataSource_columns.description"),
          key: "dataSourceDescription"
        },
        {
          title: this.$t("dataSource_columns.dataSourceType"),
          key: "dataSourceType",
          sortable: true
        },
        {
          title: this.$t("dataSource_columns.action"),
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
  mounted() {
    // this.height = size.PageH - 360;
  },
  methods: {
    // 重置
    handleReset() {
      this.page = 1;
      this.limit = 10;
      this.id = "";
      this.type = "Other";
      this.row = null;
      this.name = "";
      this.description = "";
      this.dataSourcePropertyVoList = [{ name: "", value: "", id: '' }];
      // this.driverMemory = "1g";
      // this.executorNumber = 1;
      // this.executorMemory = "1g";
      // this.executorCores = 1;
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
    // 新增/更新一条数据
    handleSaveUpdateData() {
      let data = {
        dataSourceType: this.type,
        dataSourceName: this.name,
        dataSourceDescription: this.description
      },
          contrastData = {};
      if (
        this.dataSourcePropertyVoList[0].name &&
        this.dataSourcePropertyVoList[0].value
      ) {
        this.dataSourcePropertyVoList.forEach((item, i) => {
          data[`dataSourcePropertyVoList[${i}].name`] = item.name;
          data[`dataSourcePropertyVoList[${i}].value`] = item.value;
          data[`dataSourcePropertyVoList[${i}].id`] = item.id;

        });

        this.editDataSourceList.forEach((item, i) => {
          contrastData[`dataSourcePropertyVoList[${i}].name`] = item.name;
        });

      }
      if (this.id) {
        //更新数据
        data.id = this.id;
        let flag = false;
        for (let key in contrastData){
          for (let keys in data){
            if (key === keys && contrastData[key] !== data[keys]){
              flag = true;
            }
          }
        }

        if (flag){
          this.$axios
              .post("/datasource/checkDatasourceLinked", this.$qs.stringify({dataSourceId: data.id}))
              .then(res => {
                let dataList = res.data;
                if (dataList.code === 200 && dataList.isLinked) {
                  this.$Modal.confirm({
                    title: this.$t("tip.title"),
                    content: this.$t("tip.update_fail_content") + dataList.stopsNameList,
                    onOk: () => {
                      this.saveModifiedData(data);
                    },
                    onCancel: () => {
                      // this.$Message.info('Clicked cancel');
                    }})
                } else {
                  this.$Message.error({
                    content: `${this.name} ` + this.$t("tip.update_fail_content"),
                    duration: 3
                  });
                }
              })
              .catch(error => {
                console.log(error);
              });
        }else {
          this.saveModifiedData(data);
        }
      } else {
        //新增数据
        this.$axios
          .post("/datasource/saveOrUpdate", this.$qs.stringify(data))
          .then(res => {
            if (res.data.code == 200) {
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
    handleSelectChange(val) {
      let data = this.typeList.filter(item => {
        return item.dataSourceName === val;
      });

      if (data.length === 0) {
        this.dataSourcePropertyVoList = [{ name: "", value: "" }];
      } else {
        this.dataSourcePropertyVoList = data[0].dataSourcePropertyVoList.map(
          item => {
            return {
              name: item.name,
              value: ""
            };
          }
        );
      }
    },
    handleGetInputData() {
      this.$axios
        .post("/datasource/getDataSourceInputData")
        .then(res => {
          if (res.data.code == 200) {
            let data = res.data.templateList;
            data.push({ id: "", dataSourceType: "Other" });
            this.typeList = data;
          } else {
            this.$Message.error({
              content: this.$t("tip.get_fail_content"),
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
    handleAdd() {
      this.dataSourcePropertyVoList.push({
        name: "",
        value: ""
      });
    },
    handleRemove(m, mark) {
      if (mark) {
        this.$Modal.warning({
          title: this.$t("tip.title"),
          content: "此项不可删除，请重新操作！"
        });
        return;
      }
      this.dataSourcePropertyVoList.splice(m, 1);
    },


    //保存修改数据
    saveModifiedData(data){
      this.$axios
        .post("/datasource/saveOrUpdate", this.$qs.stringify(data))
        .then(res => {
          if (res.data.code == 200) {
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
    },
    //获取行数据(编辑)
    getRowData(row) {
      this.$event.emit("looding", true);
      this.$axios
        .post("/datasource/getDataSourceInputData", this.$qs.stringify({dataSourceId:row.id}))
        .then(res => {
          if (res.data.code == 200) {
            let data = res.data.templateList;
            data.push({ id: "", dataSourceType: "Other" });
            this.typeList = data;

            let flow = res.data.dataSourceVo;
            this.id = flow.id;
            this.type = flow.dataSourceType;
            this.name = flow.dataSourceName;
            this.description = flow.dataSourceDescription;
            this.dataSourcePropertyVoList = flow.dataSourcePropertyVoList;
            this.editDataSourceList = JSON.parse(JSON.stringify(flow.dataSourcePropertyVoList));
            // this.driverMemory = flow.driverMemory;
            // this.executorNumber = flow.executorNumber;
            // this.executorMemory = flow.executorMemory;
            // this.executorCores = flow.executorCores;
            this.$event.emit("looding", false);
            this.isOpen = true;
          } else {
            this.$event.emit("looding", false);
            this.$Message.error({
              content: this.$t("tip.data_fail_content"),
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
    //删除某一行数据
    handleDeleteRow(row) {
      this.$Modal.confirm({
        title: this.$t("tip.title"),
        okText: this.$t("modal.confirm"),
        cancelText: this.$t("modal.cancel_text"),
        content: `${this.$t("modal.delete_content")} ${row.dataSourceName}?`,
        onOk: () => {
          let data = {
            dataSourceId: row.id
          };
          this.$axios
            .post("/datasource/deleteDataSource", this.$qs.stringify(data))
            .then(res => {
              if (res.data.code == 200) {
                this.$Modal.success({
                  title: this.$t("tip.title"),
                  content:
                    `${row.dataSourceName} ` +
                    this.$t("tip.delete_success_content")
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
        .get("/datasource/getDataSourceListPagination", {
          params: data
        })
        .then(res => {
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
    handleModalSwitch() {
      this.handleGetInputData();
      this.isOpen = !this.isOpen;
    }
  }
};
</script>
<style lang="scss" scoped>
@import "./index.scss";
.relationship {
  li > div {
    margin-right: 10px;
  }
  i {
    margin-right: 10px;
    font-size: 18px;
  }
  li {
    margin: 10px 0;
  }
}
.item {
  display: flex;
  label {
    margin-top: 5px;
  }
}
</style>

