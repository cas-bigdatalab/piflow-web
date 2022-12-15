<template>
  <section>
    <!-- header -->
    <div class="navbar">
      <div class="left">
        <span>{{$t("sidebar.publish")}}</span>
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
import CodeEditor from '../../compon/CodeFormat'
export default {
  name: "publish",
  components:{CodeEditor},
  data() {
    return {
      page: 1,
      limit: 10,
      total: 0,
      tableData: [],

      param: "",
      id: "",

      promptContent: [
        {
          content: 'Run',
          icon: 'ios-play'
        },
        {
          content: 'Edit Properties',
          icon: 'ios-create'
        },
        {
          content: 'Copy Url',
          icon: 'md-copy'
        },
        {
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
          title: this.$t("dataSource_columns.name"),
          key: "name",
          sortable: true
        },
        {
          title: 'crtDttm',
          key: "crtDttm",
          sortable: true
        },
        {
          title: 'lastUpdateDttm',
          key: "lastUpdateDttm",
          sortable: true
        },
        {
          title: this.$t("dataSource_columns.action"),
          slot: "action",
          width: 230,
          align: "center"
        }];
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
    },

    handleButtonSelect(row, key) {
      switch (key) {
        case 1:
          this.handleRunRow(row);
          break;
        case 2:
          this.$router.push({
            path: "/stopProperties",
            query: { publishingId: row.publishingId },
          })
          break;
        case 3:
          const path = window.location.origin+'/#/stopProperties?publishingId='+row.publishingId;
          this.copyToClipboard(path)
          this.$Message.success({
            content: 'Copy success',
            duration: 3
          });
          break;
        case 4:
          this.handleDeleteRow(row);
          break;
        default:
          break;
      }
    },

    copyToClipboard(text){
      let input = document.createElement('textarea')
      input.value = text;
      document.body.appendChild(input)
      input.select()
      document.execCommand('copy')
      document.body.removeChild(input)
    },

    handleRunRow(row) {
      this.$event.emit("crumb", [
        { name: "Process", path: "/processes" },
        { name: "drawingBoard", path: "/drawingBoard" },
      ]);
      this.$event.emit("loading", true);
      this.$axios
          .post("/flow/runFlowByPublishingId", this.$qs.stringify({publishingId: row.publishingId}))
          .then(res => {
            let dataList = res.data;
            if (dataList.code === 200) {
              // 根据类型进入不同的界面
              let src = `/drawingBoard/page/process/mxGraph/index.html?drawingBoardType=PROCESS&processType=TASK&load=${dataList.processId}`;
              this.$router.push({
                path: "/drawingBoard",
                query: { src },
              });
            }else{
              this.$event.emit("loading", false);
              this.$Message.error({
                content: dataList.errorMsg,
                duration: 3
              });
            }
          })
          .catch(error => {
            console.log(error);
          })
    },

    handleDeleteRow(row) {
      this.$axios
          .post("/stops/deleteFlowStopsPublishing", this.$qs.stringify({publishingId: row.publishingId}))
          .then(res => {
            let dataList = res.data;
            if (dataList.code === 200) {
              this.$Modal.confirm({
                title: this.$t("tip.title"),
                content: row.name+'  '+ this.$t("tip.reference_content"),
               })
              this.getTableData()
            }  else {
              this.$Message.error({
                content: this.$t("tip.update_fail_content"),
                duration: 3
              });
            }
          })
          .catch(error => {
            console.log(error);
          })
    },

    getTableData() {
      let data = { page: this.page, limit: this.limit };
      if (this.param) {
        data.param = this.param;
      }
      this.$axios
          .post("/stops/getPublishingListPager", this.$qs.stringify(data))
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
    }
  }
};
</script>
<style lang="scss" scoped>
@import "./index.scss";
.navbar{
  margin-bottom: 10px;
}
.basicInfo{
  overflow: hidden;
  div{
    width: 50%;
    //float: left;
    display: inline-block;
  }
}
.title_bar{
  font-size: 16px;
  font-weight: 700;
  margin: 10px 0;
}
.list-item{
  border-bottom: 1px solid #e8eaec;
  border-top: 1px solid #e8eaec;
  margin: 10px 0;
  padding-bottom: 20px;
}
::v-deep .ivu-input-disabled{
  background-color: #eee;
  color: #777;
}
.display_btn{
  text-align: center;
  i{
    font-size: 20px;
    cursor: pointer;
  }
}
</style>

