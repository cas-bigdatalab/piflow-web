<template>
  <Modal v-model="localVisible" :title="$t('messageSchedule.instanceHistory')" width="900" @on-cancel="handleClose">
    <div class="instance-history-filter">
      <Select v-model="filter.status" :placeholder="$t('messageSchedule.status')" style="width:120px;margin-right:8px" @on-change="fetchList">
        <Option value="">{{ $t('messageSchedule.all') }}</Option>
        <Option value="SUCCESS">{{ $t('messageSchedule.success') }}</Option>
        <Option value="FAILED">{{ $t('messageSchedule.failed') }}</Option>
        <Option value="RUNNING">{{ $t('messageSchedule.runningStatus') }}</Option>
      </Select>
      <DatePicker v-model="filter.dateRange" type="daterange" :placeholder="$t('messageSchedule.selectDateRange')" style="width:240px;margin-right:8px" @on-change="fetchList" />
    </div>
    <Table :columns="columns" :data="tableData" style="margin-top:10px">
      <template slot-scope="{ row }" slot="action">
        <span class="button-warp" @click="viewLogs(row)">{{ $t('messageSchedule.viewLog') }}</span>
        <span class="button-warp" @click="viewDetail(row)">{{ $t('messageSchedule.viewDetail') }}</span>
        <span v-if="row.status==='FAILED'" class="button-warp" @click="retry(row)">{{ $t('messageSchedule.retry') }}</span>
      </template>
    </Table>
    <div class="page">
      <Page :total="total" :current="page" :page-size="pageSize" @on-change="onPageChange" />
    </div>
    <Modal v-model="showLog" :title="$t('messageSchedule.viewLog')" width="900" footer-hide>
      <pre style="max-height:400px;overflow:auto">{{ logContent }}</pre>
    </Modal>
  </Modal>
</template>

<script>
import ExecutionListDetail from './ExecutionListDetail.vue';
export default {
  name: 'InstanceHistory',
  props: {
    visible: Boolean,
    eventId: [String, Number]
  },
   components: {
    ExecutionListDetail
  },
  data() {
    return {
      localVisible: this.visible, // Initialize localVisible with the prop's value
      filter: {
        status: ''
      },
      tableData: [],
      total: 0,
      page: 1,
      pageSize: 10,
      showLog: false,
      logContent: ''
    }
  },
  computed: {
    columns() {
      return [
         {
          type: 'expand',
          width: 50,
          align: 'center',
          // 使用 render 函数来定义展开内容
          render: (h, params) => {
            return h(ExecutionListDetail, {
              props: {
                executionList: params.row.executionList
              },
              on: {
              'view-detail': (execution) => this.viewDetail(execution),
              // 查看日志通常是看整个实例的日志，所以我们传递父级的 row
              'view-log': () => this.viewLogs(params.row)
            }
            })
          }
        },
        { title: this.$t('messageSchedule.instanceId'), key: 'id' },
        {
          title: this.$t('messageSchedule.triggerTime'),
          key: 'triggerTime',
          width: 160,
          ellipsis: true,
          render: (h, params) => {
            return h('Tooltip', { props: { content: params.row.triggerTime } }, [
              h('span', params.row.triggerTime)
            ]);
          }
        },
        { title: this.$t('messageSchedule.status'), key: 'status' },
        { title: this.$t('messageSchedule.duration'), key: 'duration' },
        { title: this.$t('messageSchedule.action'), slot: 'action', width: 220, align: 'center' }
      ]
    }
  },
  watch: {
    // Watch the prop and update the local data property
    visible(newVal) {
      this.localVisible = newVal;
      if (newVal) { // Only fetch list when modal becomes visible
        this.fetchList();
      }
    },
    eventId() { // eventId changes, fetch new list only if already visible
      if (this.localVisible) this.fetchList();
    }
  },
  methods: {
    fetchList() {
      if (!this.eventId) return;
      const params = {
        status: this.filter.status || undefined,
        page: this.page,
        size: this.pageSize
      };
      this.$axios.get(`/schedule/messageSources/${this.eventId}/instances`, { params })
        .then(res => {
          if (res.data && res.data.code === 200) {
            const list = Array.isArray(res.data.data) ? res.data.data : [];
            this.tableData = list.map(item => ({
              ...item,
              triggerTime: this.formatTime(item.triggerTime),
              duration: this.formatDuration(item.durationMillis),
              executionList: this.parseExecutionList(item.executionList) 
            }));
            this.total = res.data.totalCount ;
          } else {
            this.tableData = [];
            this.total = 0;
            this.$Message.error(res.data.msg || this.$t('messageSchedule.requestFailed'));
          }
        })
        .catch(err => {
          this.tableData = [];
          this.total = 0;
          this.$Message.error(this.$t('messageSchedule.requestFailed') + ': ' + (err.message || ''));
        });
    },
    onPageChange(p) {
      this.page = p
      this.fetchList()
    },
    parseExecutionList(listStr) {
    // 检查输入是否为非空字符串
      if (typeof listStr !== 'string' || !listStr.trim()) {
        return [];
      }
      try {
        // 尝试解析JSON字符串
        const parsedList = JSON.parse(listStr);
        // 确保解析结果确实是一个数组
        return Array.isArray(parsedList) ? parsedList : [];
      } catch (e) {
        // 如果解析失败，在控制台打印错误并返回空数组
        console.error('Failed to parse executionList JSON string:', listStr, e);
        return [];
      }
  },
    viewLogs(row) {
      this.$axios.get(`/schedule/messageSources/instance/logs/${row.id}`)
        .then(res => {
          if (res.data && res.data.code === 200) {
            const list = Array.isArray(res.data.data) ? res.data.data : [];
            this.logContent = list.join('\n');
          } else {
            this.$Message.error(res.data.msg || this.$t('messageSchedule.requestFailed'));
          }
        })
        .catch(err => {
          this.$Message.error(this.$t('messageSchedule.requestFailed') + ': ' + (err.message || ''));
        });
      this.showLog = true
    },
    viewDetail(row) {
      // 根据类型进入不同的界面
          let src = "";
          if (row.processType === "TASK") {
            src = `/drawingBoard/page/process/mxGraph/index.html?drawingBoardType=PROCESS&processType=${row.processType}&load=${row.id}`;
          } else if (row.processType === "GROUP") {
            src = `/drawingBoard/page/processGroup/mxGraph/index.html?drawingBoardType=PROCESS&processType=${row.processType}&load=${row.id}`;
          };
          src = `/drawingBoard/page/process/mxGraph/index.html?drawingBoardType=PROCESS&processType=TASK&load=${row.processId}`;

          this.$router.push({
            path: "/drawingBoard",
            query: { src },
          });
    },
    retry(row) {
      this.$axios.post(`/schedule/messageSources/instance/${row.id}`)
        .then(res => {
          if (res.data && res.data.code === 200) {
            this.$Message.success(this.$t('messageSchedule.retrySubmitted'));
            this.fetchList();
          } else {
            this.$Message.error(res.data.msg || this.$t('messageSchedule.retryFailed'));
          }
        })
        .catch(err => {
          this.$Message.error(this.$t('messageSchedule.retryFailed') + ': ' + (err.message || ''));
        });
      this.fetchList()
    },
    handleClose() {
      // When the modal wants to close itself (e.g., by clicking X or mask)
      // We update our local state and emit to the parent
      this.localVisible = false; // Close the local modal state
      this.$emit('close'); // Notify the parent to close the modal as well
    },
    formatTime(isoStr) {
      if (!isoStr) return '';
      const date = new Date(isoStr);
      const pad = n => n < 10 ? '0' + n : n;
      return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
    },
    formatDuration(ms) {
    if (typeof ms !== 'number' || ms < 0) {
        return '';
    }
    const msPart = ms % 1000;
    let s = Math.floor(ms / 1000);

    if (s === 0) {
        return `${ms}ms`;
    }
    const h = Math.floor(s / 3600);
    s %= 3600;
    const m = Math.floor(s / 60);
    s %= 60;

    let result = '';
    if (h > 0) {
        result += `${h}h `;
    }
    if (m > 0) {
        result += `${m}m `;
    }
    if (s > 0) {
        result += `${s}s `;
    }
        if (msPart > 0) {
        result += `${msPart}ms`;
    }
    return result.trim();
}
  }
}
</script>

<style scoped>
.instance-history-filter {
  margin-bottom: 8px;
}
.page {
  padding: 12px;
  display: flex;
  justify-content: center;
  background-color: #fff;
}
.button-warp {
  margin: 0 4px;
  color: var(--button-color);
  cursor: pointer;
}
</style>