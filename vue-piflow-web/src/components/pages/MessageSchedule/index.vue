<template>
  <section>
    <!-- header -->
    <div class="navbar">
      <div class="left">
        <span>{{ $t("sidebar.MessageSchedule") }}</span>
      </div>
      <div class="right">
        <span class="button-warp" @click="openMessageForm()">
          <Icon type="md-add" />
        </span>
      </div>
    </div>
    <!-- search -->
    <div class="input">
      <Input
        suffix="ios-search"
        v-model="search"
        :placeholder="$t('messageSchedule.placeholder')"
        style="width: 300px"
        @on-change="getTableData"
      />
    </div>
    <Table border :columns="columns" :data="tableData">
      <template slot-scope="{ row }" slot="action">
        <Tooltip content="Enter" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,1)">
              <Icon type="ios-redo" />
            </span>
        </Tooltip>
        <Tooltip content="Edit" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,2)">
              <Icon type="ios-create-outline"/>
            </span>
        </Tooltip>
        <Tooltip content="Stop" placement="top-start" v-if="row.status==='ACTIVE'  || row.status==='RECONNECTING'">
            <span class="button-warp" @click="handleButtonSelect(row,3)">
              <Icon type="ios-square"/>
            </span>
        </Tooltip>
        <Tooltip v-if="row.status==='INACTIVE'" content="Run" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,3)">
              <Icon type="ios-play" />
            </span>
        </Tooltip>
        <Tooltip content="Delete" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,4)">
              <Icon type="ios-trash" />
            </span>
        </Tooltip>
      </template>
    </Table>
    <!-- paging -->
    <div class="page">
      <Page
        :total="total"
        :current="page"
        show-elevator
        show-total
        show-sizer
        @on-change="onPageChange"
        @on-page-size-change="onPageSizeChange"
      />
    </div>
    <!-- 新增/编辑事件定义 -->
    <MessageForm
      :visible="showMessageForm"
      :event="currentMessageSource"
      :resetFieldsFlag="resetFieldsFlag"
      @close="showMessageForm=false"
      @save="handleSaveEvent"
    />
    <!-- 运行实例历史 -->
    <InstanceHistory
      :visible="showInstanceHistory"
      :eventId="currentEventId"
      @close="showInstanceHistory=false"
    />
  </section>
</template>

<script>
import MessageForm from './MessageForm.vue'
import InstanceHistory from './InstanceHistory.vue'
export default {
  name: 'EventSchedule',
  components: { MessageForm, InstanceHistory },
  data() {
    return {
      search: '',
      page: 1,
      limit: 10,
      total: 0,
      tableData: [],
      showMessageForm: false,
      showInstanceHistory: false,
      currentMessageSource: {},
      currentEventId: null,
      mockStatus: undefined,
      mockType: undefined,
      resetFieldsFlag: 0,
      formKey: 0
    }
  },
  computed: {
    columns() {
      return [
        { title: this.$t('messageSchedule.name'), key: 'name' },
        { title: this.$t('messageSchedule.protocol'), key: 'protocol' },
        {
          title: this.$t('messageSchedule.topic'),
          key: 'topic',
          render: (h, params) => {
            const text = params.row.topic || '';
            // 你可以自定义多长算“过长”，比如 20 字
            const isLong = text && text.length > 20;
            return isLong
              ? h('Tooltip', {
                  props: { content: text, placement: 'top' }
                }, [
                  h('span', text.slice(0, 20) + '...'),
                  // 你也可以加个icon
                  // h('Icon', { props: { type: 'md-information-circle' }, style: { marginLeft: '4px', color: '#888' } })
                ])
              : h('span', text);
          }
        },
        {
          title: this.$t('messageSchedule.configuration'),
          key: 'properties',
          render: (h, params) => {
            let properties = params.row.properties;
            // 1. 如果是字符串，先解析
            if (typeof properties === 'string') {
              try {
                properties = JSON.parse(properties);
              } catch (e) {
                properties = {};
              }
            }
            // 2. 组装每一行
            const maxLen = 30;
            const lines = Object.keys(properties).map(key => {
              let value = properties[key];
              if (key.toLowerCase().includes('password')) {
                value = '*****';
              } else {
                if (typeof value === 'string') {
                  value = value.replace(/\//g, '');
                  if (value.length > maxLen) {
                    value = value.slice(0, maxLen) + '...';
                  }
                }
                if (typeof value !== 'string') {
                  value = String(value);
                }
              }
              return h('div', { style: { 'margin-bottom': '2px', 'display': 'flex', 'align-items': 'center' } }, [
                h('span', { style: { color: '#e74c3c', 'font-weight': 500, 'flex-shrink': 0 } }, `"${key}": `),
                h('span', {
                  style: {
                    color: '#27ae60',
                    'font-weight': 500,
                    'display': 'inline-block',
                    'max-width': '220px',
                    'overflow': 'hidden',
                    'text-overflow': 'ellipsis',
                    'white-space': 'nowrap',
                    'vertical-align': 'bottom'
                  }
                }, `"${value}"`)
              ]);
            });

            return h('Tooltip', {
              props: { placement: 'top', transfer: true },
              scopedSlots: {
                content: () => h('div', {
                  style: {
                    'max-width': '400px',
                    'max-height': '300px',
                    'overflow': 'auto',
                    'white-space': 'normal',
                    'font-family': 'monospace',
                    'font-size': '13px',
                    'line-height': '1.6',
                    'padding': '8px 12px'
                    // 不再设置 background
                  }
                }, lines.length ? lines : [h('span', { style: { color: '#aaa' } }, this.$t('messageSchedule.noData'))] )
              }
            }, [
              h('span', this.$t('messageSchedule.lookup'  ))
            ]);
          }
        },
        {
          title: this.$t('messageSchedule.workflow'),
          key: 'targetWorkflowName',
          render: (h, params) => {
            return h(
              'a',
              {
                style: { color: '#1890ff', cursor: 'pointer' },
                on: {
                  click: () => this.handleWorkflowClick(params.row)
                }
              },
              params.row.targetWorkflowName
            )
          }
        },
        // { title: this.$t('messageSchedule.status'), key: 'status', render: (h, params) => h('span', params.row.status === 'ACTIVE' ? this.$t('messageSchedule.running') : this.$t('messageSchedule.paused')) },
        {
          title: this.$t('messageSchedule.status'),
          key: 'status',
          render: (h, params) => {
            let statusText = '';
            
            if (params.row.status === 'ACTIVE') {
              statusText = this.$t('messageSchedule.running');
            } else if (params.row.status === 'RECONNECTING') {
              // 添加对 RECONNECTING 状态的判断
              statusText = this.$t('messageSchedule.reconnecting'); 
            } else {
              // 其他所有状态都视为“暂停”
              statusText = this.$t('messageSchedule.paused');
            }

            return h('span', statusText);
          }
        },
        { title: this.$t('messageSchedule.action'), slot: 'action', width: 300, align: 'center' }
      ]
    }
  },
  created() {
    this.getTableData()
  },
  methods: {
    getTableData() {
      let data = { page: this.page, limit: this.limit, search: this.search };
      if (this.param) {
        data.param = this.param;
      }
      this.$axios
        .get("/schedule/messageSources", {
          params: data,
        })
        .then((res) => {
          if (res.data.code === 200) {
            // 适配后端数据
            this.tableData = (res.data.data || res.data || []).map(item => {
              let protocolText = item.protocol === 'RABBITMQ' ? 'RabbitMQ' : (item.protocol === 'KAFKA' ? 'Kafka' : item.protocol);

              // 解析 properties
              let properties = {};
              try {
                properties = item.properties ? JSON.parse(item.properties) : {};
              } catch (e) {
                properties = {};
              }

              // 优先用 properties 里的 queueName 或 topicName
              let topic = properties.topicName || properties.queueName || item.topicName || item.queueName || '';

              let status = (item.status === 'ACTIVE' ) ? 'ACTIVE' : 'INACTIVE';

              return {
                id: item.id,
                name: item.name,
                protocol: protocolText,
                topic: topic,
                topicFull: topic, // 主题/队列全名
                properties: properties, // 保留原始 properties 以便后续用
                status: status,
                ...item
              }
            });
            this.total = res.data.totalCount;
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
    openMessageForm() {
      this.currentMessageSource = {} // 新增时清空数据
      this.showMessageForm = true
    },
    editEvent(row) {
      let propertiesObject;
      if (typeof row.properties === 'string' && row.properties) {
        try {
          propertiesObject = JSON.parse(row.properties);
        } catch (e) {
          console.error("解析properties字段失败:", row.properties, e);
          propertiesObject = {}; // 解析失败则给一个空对象
        }
      } else if (typeof row.properties === 'object' && row.properties !== null) {
        propertiesObject = structuredClone(row.properties);
      } else {
        propertiesObject = {};
      }
      propertiesObject.password = '';
      const properties = JSON.stringify(propertiesObject);
      this.currentMessageSource = { ...row, properties};

      this.showMessageForm = true;
    },
    handleSaveEvent(formData) {
      // 新增或编辑事件定义，调用后端保存接口
      const url = formData.id ? `/schedule/messageSources/${formData.id}` : '/schedule/messageSources';
      const method = formData.id ? 'PUT' : 'POST';

      this.$axios({
        method: method,
        url: url,
        data: formData
      })
      .then(res => {
        if (res.data.code === 200) {
          this.$Message.success(this.$t('tip.save_success_content'));
        } else {
          this.$Message.error(res.data.msg || this.$t('tip.save_fail_content'));
        }
         this.getTableData(); // 刷新列表
        this.showMessageForm = false; // 关闭弹窗
      })
      .catch(err => {
        this.$Message.error(this.$t('messageSchedule.requestFailed') + ': ' + err.message);
      })
    },
    toggleEvent(row) {
      // 切换运行/暂停状态
      
      const action = row.status != 'INACTIVE'  ? 'pause' : 'start' ;
      const confirmText = row.status != 'INACTIVE' ? this.$t('messageSchedule.pause') : this.$t('messageSchedule.start') ;

      this.$Modal.confirm({
        title: this.$t('messageSchedule.confirmTitle'),
        content: confirmText + row.name,
        onOk: () => {
          this.$axios.post(`/schedule/messageSources/${row.id}/${action}`)
            .then(res => {
              if (res.data.code === 200) {
                action === 'start' ? this.$Message.success(this.$t('tip.run_success_content')) : this.$Message.success(this.$t('tip.stop_success_content'));
                this.getTableData(); // 刷新列表
              } else {
                action === 'start' ? this.$Message.error(this.$t('tip.run_fail_content')) : this.$Message.error(this.$t('tip.stop_fail_content'));
              }
            })
            .catch(err => {
              this.$Message.error(this.$t('tip.request_fail_content') + ': ' + err.message);
            });
        }
      });
    },
    deleteEvent(row) {
      this.$Modal.confirm({
        title: this.$t('messageSchedule.confirmTitle'),
        content: this.$t('messageSchedule.confirmDelete', { name: row.name }),
        onOk: () => {
          this.$axios.delete(`/schedule/messageSources/${row.id}`)
            .then(res => {
              if (res.data.code === 200) {
                this.$Message.success(this.$t('tip.delete_success_content'));
                this.getTableData(); // 刷新列表
              } else {
                this.$Message.error(res.data.msg || this.$t('tip.delete_fail_content'));
              }
            })
            .catch(err => {
              this.$Message.error(this.$t('messageSchedule.requestFailed') + ': ' + err.message);
            });
        }
      })
    },
    handleButtonSelect(row, key) {
      switch (key) {
        case 1:
          this.viewInstances(row);
          break;
        case 2:
          this.editEvent(row);
          break;
        case 3:
          this.toggleEvent(row);
          break;
        case 4:
          this.deleteEvent(row);
          break;
        default:
          break;
      }
    },
    viewInstances(row) {
      this.currentEventId = row.id
      this.showInstanceHistory = true
    },
    onPageChange(page) {
      this.page = page
      this.getTableData()
    },
    onPageSizeChange(size) {
      this.pageSize = size
      this.page = 1; // 改变每页大小时，重置到第一页
      this.getTableData()
    },
    handleWorkflowClick(row) {
      // 跳转到/drawingBoard，带src参数
      this.$router.push({
        path: '/drawingBoard',
        query: {
          src: `/drawingBoard/page/flow/mxGraph/index.html?load=${row.targetWorkflowId}`        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
.ivu-tooltip .ivu-tooltip-inner {
  background: #fff !important;
  color: #222 !important;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08) !important;
  border-radius: 4px !important;
}
</style>

