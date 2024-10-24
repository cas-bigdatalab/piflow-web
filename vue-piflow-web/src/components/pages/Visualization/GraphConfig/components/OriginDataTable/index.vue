<template>
  <vxe-modal
    v-model="open"
    title="原始数据预览"
    resize
    show-zoom
    width="60vw"
    destroy-on-close
  >
    <template #default>
      <div style="height:600px">
        <!-- Table button -->
        <Table
          border
          size="small"
          height="560"
          :columns="columns"
          :data="tableData"
        >
        </Table>
        <!-- paging -->
        <div
          class="page"
          style="margin-top: 10px;display: flex;justify-content: flex-end;"
        >
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
      </div>
    </template>
  </vxe-modal>
</template>

<script>
export default {
  name: "OriginDataTable",
  data() {
    return {
      open:false,
      tableData: [],
      data:[],
      total: 0,
      page: 1,
      limit: 10,
    };
  },
  computed: {
    columns() {
      if (this.tableData.length) {
        return Object.keys(this.tableData[0]).map((v) => ({
          title: v,
          key: v,
          minWidth: 80,
        }));
      }
      return [];
    },
  },

  methods: {
    show(data){
      this.open = true
      this.data =  data
      this.getTableData();
    },
    getTableData() {
      const p = this.page;
      const s = this.limit;
      this.tableData = this.data.slice((p - 1) * s, p * s);
      this.total = this.data.length;

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

<style lang="scss" scoped></style>
