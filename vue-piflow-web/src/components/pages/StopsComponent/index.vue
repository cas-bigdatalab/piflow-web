<template>
  <section>
    <div class="navbar">
      <div class="left">
        <span>{{$t("sidebar.stopsComponent")}}</span>
      </div>
    </div>

    <div class="input">
      <vxe-toolbar>
        <template #buttons>
          <vxe-input v-model="param" type="search" :placeholder="$t('modal.placeholder')" @keyup="searchEvent"  style="width: 300px"></vxe-input>
        </template>
      </vxe-toolbar>
    </div>

    <div :style="style">
      <vxe-table
          resizable
          highlight-hover-row
          row-key
          border
          height="auto"
          ref="xTree"
          size="medium"
          class="mytable-scrollbar"
          row-id="id"
          :loading="loading"
          :tree-config="{children: 'stopsComponentVoList'}"
          :checkbox-config="{labelField: 'groupName', checkRowKeys: checkRowKeys, reserve:true}"
          @checkbox-change="selectChangeEvent"
          @checkbox-all="selectAll"
          :data="tableList">

        <vxe-table-column field="groupName" :title="$t('stopsComponent_columns.name')" type="checkbox"  tree-node>
          <template #default="{ row }">
              <span>
                <span v-if="row.groupName" v-html="row.groupName"></span>
                <span v-else v-html="row.name">{{ row.name }}</span>
              </span>
          </template>
        </vxe-table-column>
        <vxe-table-column field="bundel" title="bundel" type="html"></vxe-table-column>
        <vxe-table-column field="owner" title="owner" type="html"></vxe-table-column>
        <vxe-table-column field="description" :title="$t('stopsComponent_columns.description')" type="html"></vxe-table-column>
      </vxe-table>
    </div>
  </section>
</template>

<script>
import XEUtils from 'xe-utils'
export default {
  name: "stopsComponent",
  components: {},
  data() {
    return {
      page: 1,
      limit: 10,
      total: 0,
      tableData: [],
      param: "",

      style: {
        height: "auto"
      },

      checkRowKeys: [],
      queryJson: {
        bundle: '',
        stopsGroups: '',
        isShow: 0
      },
      tableList: [],
      loading: false

    };
  },
  mounted() {
    this.setSize();
    this.getTableData();
  },
  methods: {
    setSize() {
      let headerHeight = document.querySelector("header").offsetHeight;
      let bodyHeight = document.querySelector("body").offsetHeight;
      let footerHeight = document.querySelector("footer").offsetHeight;
      this.style.height = bodyHeight - headerHeight - footerHeight -16*2-45-59 + "px";
    },

    selectChangeEvent ({ checked, records, row }) {
      let queryJson= {
        bundle: [],
        stopsGroups: [],
        isShow: 1
      };
      if (checked){

        queryJson.isShow = 1;
        records.forEach((item)=>{
          if ( item.groupName){

          }else {
            queryJson.bundle.push(item.bundel);
            queryJson.stopsGroups.push(item.groups);
          }
        })
        this.stopsDisplayed(queryJson);

      }else {
        if (!!row.groupName){
          queryJson.isShow = 0;
          row.stopsComponentVoList.forEach((item)=>{
            queryJson.bundle.push(item.bundel);
            queryJson.stopsGroups.push(item.groups);
          })
          this.stopsDisplayed(queryJson);
        }else {
          queryJson.isShow = 0;
          queryJson.bundle.push(row.bundel);
          queryJson.stopsGroups.push(row.groups);
          this.stopsDisplayed(queryJson);
        }
      }
    },

    selectAll ({ checked, records }) {
      let queryJson= {
        bundle: [],
        stopsGroups: [],
        isShow: 1
      };
      if (checked){
        queryJson.isShow = 1;
        records.forEach((item)=>{
          if ( item.groupName){

          }else {
            queryJson.bundle.push(item.bundel);
            queryJson.stopsGroups.push(item.groups);
          }
        })
      }else {
        queryJson.isShow = 0;
        let getDataList = this.$refs.xTree.getData();
        getDataList.forEach((item)=>{
          item.stopsComponentVoList.forEach((items)=>{
            queryJson.bundle.push(items.bundel);
            queryJson.stopsGroups.push(items.groups);
          })
        })
      }

      this.stopsDisplayed(queryJson);
    },

    stopsDisplayed(queryJson){
      this.loading = true;
      this.$axios
          .post("/stopsManage/updatestopsComponentIsShow", this.$qs.stringify(queryJson))
          .then((res) => {
            if (res.data.code === 200){
              this.loading = false;
              this.$Message.success('Save success');
            }
          })
          .catch((error) => {
            this.loading = false;
            console.log(error);
            this.$event.emit("loading", false);
            this.$Message.error({
              content: this.$t("tip.fault_content"),
              duration: 3
            });
          });
    },

    getTableData() {
      this.$axios
          .post("/stopsManage/stopsComponentList")
          .then((res) => {
            if (res.data.code === 200) {
              let data = res.data, defaultState = [];

              data.stopGroupList.forEach((item)=>{
                item.stopsComponentVoList.forEach((items)=>{
                  items.id = items.groups.replace(/\s*/g,"")+ items.id;
                  if (items.isShow)
                    defaultState.push(items.id)
                })
              })
              this.tableData = data.stopGroupList;
              this.searchEvent();
              this.checkRowKeys = defaultState;
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

    searchEvent () {
      const param = XEUtils.toValueString(this.param).trim().toLowerCase();
      if (param) {
        const filterRE = new RegExp(param, 'gi')
        const options = { children: 'stopsComponentVoList' }
        const searchProps = ['groupName', 'bundel', 'owner', 'description']
        const rest = XEUtils.searchTree(this.tableData, item => searchProps.some(key => XEUtils.toValueString(item[key]).toLowerCase().indexOf(param) > -1), options)
        XEUtils.eachTree(rest, item => {
          searchProps.forEach(key => {
            item[key] = XEUtils.toValueString(item[key]).replace(filterRE, match => `<span class="keyword-lighten">${match}</span>`)
          })
        }, options)
        this.tableList = rest

        this.$nextTick(() => {
          let defaultState = [];
          this.tableList.forEach((item)=>{
            item.stopsComponentVoList.forEach((items)=>{
              items.id = items.groups.replace(/\s*/g,"")+ items.id;
              if (items.isShow){
                defaultState.push(items.id)
              }
            })
          })

          this.checkRowKeys = defaultState;
          this.$refs.xTree.setAllTreeExpand(true)
        })
      } else {
        this.tableList = this.tableData
      }
    },

    onPageChange(pageNo) {
      this.page = pageNo
    },

    onPageSizeChange(pageSize) {
      this.limit = pageSize
    },
  },
};
</script>
<style lang="scss" scoped>
@import "./index.scss";
.tree-node-icon {
  width: 16px;
  text-align: center;
}

::v-deep .keyword-lighten {
  color: #f8f5f1;
  background-color: var(--button-color);
}
.vxe-toolbar{
  background-color: rgba(255, 255, 255, 0);
}
</style>

