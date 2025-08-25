<template>
  <div class="expand-row">
    <Table
      :columns="executionColumns"
      :data="executionList"
      :show-header="true"
      size="small"
    ></Table>
  </div>
</template>

<script>
export default {
  name: 'ExecutionListDetail',
  props: {
    executionList: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      executionColumns: [
        {
          title: this.$t('messageSchedule.createTime'),
          key: 'createTime',
          width: 200, // 3. 为列设置固定宽度，减少留白
          render: (h, params) => h('span', this.formatTime(params.row.createTime))
        },
        {
          title: this.$t('messageSchedule.status'),
          key: 'status',
          width: 120 // 3. 为列设置固定宽度
        },
        {
          title: this.$t('messageSchedule.duration'),
          key: 'durationMillis',
          width: 150, // 3. 为列设置固定宽度
          render: (h, params) => h('span', this.formatDuration(params.row.durationMillis))
        },
        // 1. Process ID 列已被移除
        // 2. 添加新的操作列
        {
          title: this.$t('messageSchedule.action'),
          key: 'action',
          align: 'center',
          render: (h, params) => {
            return h('div', [
              h('span', {
                class: 'button-warp',
                on: {
                  // 4. 在按钮点击时通过 $emit 通知父组件
                  click: () => this.$emit('view-detail', params.row)
                }
              },  this.$t('messageSchedule.lookup')),
  
            ])
          }
        }
      ]
    }
  },
  methods: {
    // ... formatTime 和 formatDuration 方法保持不变 ...
    formatTime(isoStr) {
      if (!isoStr) return '';
      const date = new Date(isoStr);
      const pad = n => n < 10 ? '0' + n : n;
      return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
    },
    formatDuration(ms) {
       if (typeof ms !== 'number' || ms < 0) return '';
       const seconds = Math.floor(ms / 1000);
       const minutes = Math.floor(seconds / 60);
       const hours = Math.floor(minutes / 60);
       if (hours > 0) return `${hours}h ${minutes % 60}m ${seconds % 60}s`;
       if (minutes > 0) return `${minutes}m ${seconds % 60}s`;
       if (seconds > 0) return `${seconds}s ${ms % 1000}ms`;
       return `${ms}ms`;
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
::v-deep .ivu-table-expanded-cell {
  padding: 0px 0px 5px 48px; /* 上下留白减小，左右根据需要调整 */
}

/*
 * 解决问题一：外圈留白过多
 * .ivu-table-expanded-cell 是 iView/ViewUI 展开行单元格的默认类名
 * 我们将它的内边距设置为0，去掉外圈的留白。
 */
.expand-row >>> .ivu-table-expanded-cell {
  padding: 0 !important;
}

/*
 * 我们自己的容器也不需要额外的内边距了
 */
.expand-row {
  background-color: #f8f8f9;
}

/*
 * 解决问题二：按钮样式不生效
 * 在 .expand-row 后面使用 '>>>' 深度选择器，
 * 确保样式能穿透到子组件 <Table> 内部渲染出来的按钮上。
 */
.expand-row >>> .button-warp {
  margin: 0 8px;
  color: #1A8B5F;
  cursor: pointer; /* 强制鼠标指针为手型 */
  transition: all 0.2s ease-in-out;
}

.expand-row >>> .button-warp:hover {
  text-decoration: underline;
  filter: brightness(1.2);
}
</style>