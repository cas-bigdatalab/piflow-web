<template>
  <Modal v-model="visible" :title="form.id ? $t('messageSchedule.editTitle') : $t('messageSchedule.addTitle')" @on-cancel="handleCancel" width="500">
    <div class="modal-warp" style="padding-right: 5px;">
      <Steps :current="currentStep" style="margin-bottom: 24px; margin-right: 40px;">
        <Step :title="$t('messageSchedule.stepCore')" />
        <Step :title="$t('messageSchedule.stepConnection')" />
        <Step :title="$t('messageSchedule.stepProcessing')" />
      </Steps>

      <div v-show="currentStep === 0">
        <Form ref="MessageForm0" :key="formKey" :model="form" :rules="rules" :label-width="120">
          <FormItem :label="$t('messageSchedule.name')" prop="name">
            <Input v-model="form.name" maxlength="100" :placeholder="$t('messageSchedule.inputName')" style="width: 340px" />
          </FormItem>
          <FormItem :label="$t('schedule_columns.scheduleType')" prop="type">
            <Select v-model="form.type" :placeholder="$t('messageSchedule.selectScheduleType')" style="width: 340px">
              <Option v-for="item in formValidateType.typeList" :value="item.name" :key="item.name">{{ item.name }}</Option>
            </Select>
          </FormItem>
          <FormItem :label="$t('messageSchedule.sourceType')" prop="protocol">
            <Select v-model="form.protocol" style="width: 340px" @on-change="onProtocolChange">
              <Option value="KAFKA">{{ $t('messageSchedule.kafka') }}</Option>
              <Option value="RABBITMQ">{{ $t('messageSchedule.rabbitmq') }}</Option>
            </Select>
          </FormItem>
          <FormItem :label="$t('messageSchedule.workflow')" prop="targetWorkflowId">
            <Select v-model="form.targetWorkflowId" filterable :placeholder="$t('messageSchedule.selectWorkflow')" style="width: 340px">
              <Option v-for="w in workflowListComputed" :key="w.id" :value="w.id">{{ w.name }}</Option>
            </Select>
          </FormItem>
          <FormItem :label="$t('messageSchedule.concurrencyLimit')" prop="concurrencyLimit">
            <Input v-model="form.concurrencyLimit" :placeholder="$t('messageSchedule.concurrencyLimitPlaceholder')" style="width: 340px">
            </Input>
          </FormItem>
        </Form>
      </div>

      <div v-show="currentStep === 1">
        <Form ref="MessageForm1" :key="formKey" :model="form" :rules="rules" :label-width="120">
          <template v-if="!isKafkaProtocol">
            <FormItem :label="$t('messageSchedule.host')" prop="host">
              <Input v-model="form.host" placeholder="e.g. 127.0.0.1" style="width: 340px" />
            </FormItem>
            <FormItem :label="$t('messageSchedule.port')" prop="port">
              <Input v-model="form.port" placeholder="e.g. 5672" style="width: 340px" />
            </FormItem>
            <FormItem :label="$t('messageSchedule.virtualHost')" prop="virtualHost">
              <Input v-model="form.virtualHost" placeholder="/" style="width: 340px" />
            </FormItem>
            <FormItem :label="$t('messageSchedule.queueLabel')" prop="queueName">
              <Input v-model="form.queueName" :placeholder="$t('messageSchedule.inputQueue')" style="width: 340px" />
            </FormItem>
            <!-- <FormItem :label="$t('messageSchedule.prefetchCount')" prop="prefetchCount">
              <InputNumber v-model="form.prefetchCount" :min="1" style="width: 340px" />
            </FormItem> -->
          </template>

          <template v-if="isKafkaProtocol">
            <FormItem :label="$t('messageSchedule.bootstrapServers')" prop="bootstrapServers">
              <Input v-model="form.bootstrapServers" :placeholder="$t('messageSchedule.bootstrapServersPlaceholder')" style="width: 340px" />
            </FormItem>
            <FormItem :label="$t('messageSchedule.topic')" prop="topicName">
              <Input v-model="form.topicName" placeholder="e.g. my-topic" style="width: 340px" />
            </FormItem>
            <FormItem :label="$t('messageSchedule.consumerGroupId')" prop="groupId">
              <Input v-model="form.groupId" :placeholder="$t('messageSchedule.consumerGroupIdPlaceholder')" style="width: 340px" />
            </FormItem>
            <FormItem :label="$t('messageSchedule.autoOffsetResetLabel')" prop="autoOffsetReset">
              <Select v-model="form.autoOffsetReset" style="width: 340px">
                <Option value="latest">Latest</Option>
                <Option value="earliest">Earliest</Option>
              </Select>
            </FormItem>
          </template>

          <FormItem :label="$t('messageSchedule.username')" prop="username">
            <Input v-model="form.username" maxlength="100" style="width: 340px" />
          </FormItem>
          <FormItem :label="$t('messageSchedule.password')" prop="password">
            <Input v-model="form.password" type="password" maxlength="100" style="width: 340px" />
          </FormItem>
          <FormItem :label="$t('messageSchedule.connectionTimeout')" prop="connectionTimeout">
            <InputNumber v-model="form.connectionTimeout" :min="0" style="width: 340px" />
          </FormItem>
          <FormItem :label="$t('messageSchedule.advancedConfig')" prop="advancedConfig">
            <Input v-model="form.advancedConfig" type="textarea" :rows="2" :placeholder="$t('messageSchedule.advancedConfigPlaceholder')" style="width: 340px" />
          </FormItem>
        </Form>
        <div style="text-align:right; margin-top:10px;">
          <Button type="primary" :loading="testLoading" @click="handleTestConnection" style="margin-right:8px;">{{ $t('messageSchedule.testConnection') }}</Button>
        </div>
      </div>

        <div v-show="currentStep === 2">
         <Form ref="MessageForm2" :key="formKey" :model="form" :rules="rules" :label-width="120">

          <FormItem :label="$t('messageSchedule.filterRuleMode')">
            <RadioGroup v-model="form.filterRuleType">
              <Radio label="default">{{ $t('messageSchedule.simpleMode') }}</Radio>
              <Radio label="expression">{{ $t('messageSchedule.advancedMode') }}</Radio>
            </RadioGroup>
          </FormItem>

          <div v-if="form.filterRuleType === 'default'">
            <div v-for="(rule, index) in form.filterRulesKv" :key="index" style="margin-bottom: 10px;">
              <Row type="flex" align="middle" :gutter="8">
                <Col :span="8">
                  <Input v-model="rule.key" :placeholder="$t('messageSchedule.keyPlaceholder')" />
                </Col>
                <Col :span="6">
                  <Select v-model="rule.op">
                    <Option value="==">==</Option>
                    <Option value="!=">!=</Option>
                    <Option value=">">&gt;</Option>
                    <Option value="<">&lt;</Option>
                    <Option value=">=">&gt;=</Option>
                    <Option value="<=">&lt;=</Option>
                    <Option value="contains">{{ $t('messageSchedule.opContains') }}</Option>
                    <Option value="regex">{{ $t('messageSchedule.opRegex') }}</Option>
                  </Select>
                </Col>
                <Col :span="8">
                  <Input v-model="rule.value" :placeholder="$t('messageSchedule.valuePlaceholder')" />
                </Col>
                <Col :span="2">
                  <Button type="text" shape="circle" icon="md-remove" @click="removeRule(index)"></Button>
                </Col>
              </Row>
            </div>
            <Button type="dashed" long @click="addRule" icon="md-add">{{ $t('messageSchedule.addCondition') }}</Button>
          </div>

          <div v-if="form.filterRuleType === 'expression'">
            <FormItem :label="$t('messageSchedule.aviatorScript')" prop="filterRuleExpression">
              <CodeEditor v-model="form.filterRuleExpression" :placeholder="$t('messageSchedule.filterRulePlaceholder')" />
            </FormItem>
        </div>

        </Form>
      </div>
    </div>

    <div slot="footer" style="text-align: right;">
      <Button v-if="currentStep > 0" @click="prevStep" style="margin-right: 8px;">{{ $t('messageSchedule.prevStep') }}</Button>
      <Button v-if="currentStep < 2" type="primary" @click="nextStep" style="margin-right: 8px;">{{ $t('messageSchedule.nextStep') }}</Button>
      <Button v-if="currentStep === 2" type="primary" @click="handleSubmit">{{ $t('messageSchedule.save') }}</Button>
    </div>
  </Modal>
</template>
<script>
import CodeEditor from '../../compon/CodeFormat'
import {aesMinEncrypt} from "@/utils/crypto.js"
export default {
  name: 'MessageForm',
  components:{CodeEditor},
  props: {
    visible: Boolean,
    event: {
      type: Object,
      default: () => ({})
    },
    workflowList: {
      type: Array,
      default: () => []
    },
    resetFieldsFlag: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      currentStep: 0,
      formValidateType:{
        type:"FLOW",
        typeList: [
          {
            name: 'FLOW'
          },
          {
            name: 'FLOW_GROUP'
          }
        ],
      },
      form: {
        id: null,
        name: '',
        type: '',
        protocol: '',
        concurrencyLimit: '',
        queueName: '',
        topicName: '',
        groupId: '',
        prefetchCount: 1,
        autoOffsetReset: 'latest',
        bootstrapServers: '',
        username: '',
        password: '',
        connectionTimeout: 5000,
        targetWorkflowId: '',
        targetWorkflowName: '',
        filterRuleType: 'default', // 默认模式, 'default' 或 'expression'
        filterRulesKv: [  // 用于存储简单模式的键值对规则
         { key: '', op: '>', value: '' }
        ],
        filterRuleExpression: '',
        filterRuleJson: '',
        properties: '',
      },
      testLoading: false,
      formKey: 0,
    }
  },
  computed: {
    workflowListComputed() {
      return this.workflowList
    },
    isKafkaProtocol() {
      return this.form.protocol === 'KAFKA'
    },
    rules() {
      return {
        name: [
          { required: true, message: this.$t('messageSchedule.nameRequired'), trigger: 'blur' }
        ],
        protocol: [
          { required: true, message: this.$t('messageSchedule.typeRequired'), trigger: 'blur' }
        ],
        type: [
          { required: true, message: this.$t('messageSchedule.scheduleTypeRequired'), trigger: 'blur' }
        ],
        queueName: [
          { required: true, message: this.$t('messageSchedule.queueRequired'), trigger: 'blur' }
        ],
        prefetchCount: [
          { required: true, type: 'number', message: this.$t('messageSchedule.prefetchCountRequired'), trigger: 'blur' }
        ],
        username: [],
        password: [],
        connectionTimeout: [
          { type: 'number', message: this.$t('messageSchedule.connectionTimeout'), trigger: 'blur' }
        ],
        bootstrapServers: [
          { required: true, message: this.$t('messageSchedule.bootstrapServersRequired'), trigger: 'blur', validator: (rule, value, callback) => {
            if (this.isKafkaProtocol && !this.form.bootstrapServers) {
              callback(new Error(this.$t('messageSchedule.bootstrapServersRequired')))
            } else {
              callback()
            }
          }}
        ],
        host: [
          { required: true, message: this.$t('messageSchedule.hostRequired'), trigger: 'blur', validator: (rule, value, callback) => {
            if (!this.isKafkaProtocol && !this.form.host) {
              callback(new Error(this.$t('messageSchedule.hostRequired')))
            } else {
              callback()
            }
          }}
        ],
        port: [
          { required: true, type: 'number', message: this.$t('messageSchedule.portRequired'), trigger: 'blur', validator: (rule, value, callback) => {
            if (!this.form.port) {
              callback(new Error(this.$t('messageSchedule.portRequired')))
            } else {
              callback()
            }
          }}
        ],
        virtualHost: [],
        advancedConfig: [],
        targetWorkflowId: [
          { required: true, message: this.$t('messageSchedule.workflowRequired'), trigger: 'change' }
        ],
        // contextMappingJson: [],
        filterRuleJson: []
      }
    }
  },
  watch: {
    event: {
      handler(val) {
        // 1. 先合并主表单字段
        let base = {
          id: null,
          name: '',
          type: '',
          protocol: '',
          queueName: '',
          prefetchCount: 1,
          username: '',
          password: '',
          connectionTimeout: 5000,
          targetWorkflowId: '',
          targetWorkflowName: '',
          filterRuleJson: '',
          properties: '',
          filterRuleType: 'default', // 默认设置为简单模式
          filterRulesKv: [
            { key: '', op: '==', value: '' }
          ],
          filterRuleExpression: ''
        };
        // 2. 解析 properties
        let properties = {};
        try {
          properties = val && val.properties ? JSON.parse(val.properties) : {};
          //根据filterRuleType初始化filterRulesKv或filterRuleExpression
          if (val.filterRuleType === 'expression') {
            base.filterRuleType = 'expression';
            base.filterRuleExpression = val.filterRuleJson || '';
          } else {
            // 简单模式，尝试解析 JSON 字符串
            base.filterRuleType = 'default';  
            if (val.filterRuleJson) {
              try {
                const rules = JSON.parse(val.filterRuleJson);
                if (Array.isArray(rules)) {
                   base.filterRulesKv = rules.map(rule => ({
                    key: rule.key || '',
                    op: rule.op || '==',
                    value: rule.value || ''
                  }));
                } else {
                   base.filterRulesKv = [{ key: '', op: '==', value: '' }];
                }
              } catch (e) {
                console.error('Failed to parse filterRuleJson:', val.filterRuleJson, e);
                 base.filterRulesKv = [{ key: '', op: '==', value: '' }];
              }
            } else {
              this.form.filterRulesKv = [{ key: '', op: '==', value: '' }];
            }
          }
       
        } catch (e) {
          console.error('Failed to parse properties JSON string:', val.properties, e);
          properties = {};
        }
        // 3. 合并到 form
        this.form = Object.assign({}, base, val, properties);
        this.formKey += 1;
        this.currentStep = 0;
        this.$nextTick(() => {
          if (this.$refs.MessageForm0) this.$refs.MessageForm0.resetFields();
          if (this.$refs.MessageForm1) this.$refs.MessageForm1.resetFields();
          if (this.$refs.MessageForm2) this.$refs.MessageForm2.resetFields();
        });
      },
      immediate: true
    },
    workflowList: {
      handler(){
        this.syncTargetWorkflowName()
      },
    immediate: true
  },
    resetFieldsFlag() {
      // 外部传入变化时，重置所有表单校验
      this.$nextTick(() => {
        if (this.$refs.MessageForm0) this.$refs.MessageForm0.resetFields();
        if (this.$refs.MessageForm1) this.$refs.MessageForm1.resetFields();
        if (this.$refs.MessageForm2) this.$refs.MessageForm2.resetFields();
      });
    },
    'form.targetWorkflowId': {
    handler() {
      this.syncTargetWorkflowName();
    },
    immediate: true
  },
    'form.type'(val) {
      this.handleGetTemplateData(val);
    }
  },
  methods: {
    nextStep() {
      const refName = `MessageForm${this.currentStep}`
      this.$refs[refName].validate(valid => {
        if (valid) {
          this.currentStep++
        }
      })
    },
    syncTargetWorkflowName() {
    const selected = this.workflowListComputed.find(w => w.id === this.form.targetWorkflowId)
    this.form.targetWorkflowName = selected ? selected.name : 'unkown';
  },
    prevStep() {
      if (this.currentStep > 0) this.currentStep--
    },
    handleCancel() {
      this.$emit('close')
    },
    handleGetTemplateData(type) {
      let data = {page: 1, limit: 10000};
      if (type === 'FLOW'){
        this.$axios
        .get("/flow/getFlowListPage", {
          params: data
        })
        .then((res) => {
          if (res.data.code === 200 && Array.isArray(res.data.data)) {
            // 只保留 id 和 name
            this.workflowList = res.data.data.map(item => ({
              id: item.id,
              name: item.name
            }));
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
      }else if(type === 'FLOW_GROUP'){
        this.$axios
            .get("/flowGroup/getFlowGroupListPage", {
              params: data
            })
        .then((res) => {
          if (res.data.code === 200 && Array.isArray(res.data.data)) {
            // 只保留 id 和 name
            this.workflowList = res.data.data.map(item => ({
              id: item.id,
              name: item.name
            }));
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
      }
    },
handleSubmit() {
        let protocolFields = {};
        if (this.isKafkaProtocol) {
          // Kafka 配置
          
          // 从高级配置中获取其他字段
          protocolFields.topicName = this.form.topicName;
          protocolFields.groupId = this.form.groupId;
          protocolFields.autoOffsetReset = this.form.autoOffsetReset;

          protocolFields.bootstrapServers = this.form.bootstrapServers;
        } else {
          // RabbitMQ 配置

          protocolFields.queueName = this.form.queueName;
          protocolFields.prefetchCount = this.form.prefetchCount;
          protocolFields.virtualHost = this.form.virtualHost;
          // 通用字段
          protocolFields.host = this.form.host;
          protocolFields.port = this.form.port;
        }

        protocolFields.advancedConfig = this.form.advancedConfig;
        protocolFields.connectionTimeout = this.form.connectionTimeout;
        protocolFields.username = this.form.username;
        console.log("this.form.password", this.form.password)
        if (this.form.password !== '') {
          protocolFields.password = aesMinEncrypt(this.form.password);
        }
        // 处理过滤规则
        if (this.form.filterRuleType === 'default') {
          // 简单模式，转换为 JSON 字符串
          const rules = this.form.filterRulesKv
            .filter(rule => rule.key && rule.op && rule.value) // 过滤掉空规则
            .map(rule => ({ key: rule.key, op: rule.op, value: rule.value }));
          this.form.filterRuleJson = JSON.stringify(rules);
        } else {
          // 高级模式，直接使用表达式
          this.form.filterRuleJson = this.form.filterRuleExpression;
        }
        delete this.form.filterRulesKv;
        delete this.form.filterRuleExpression;
        delete this.form.password;
        this.form.properties = JSON.stringify(protocolFields);
        if (this.form.concurrencyLimit === '') {
          this.form.concurrencyLimit = null;
        }
        this.$emit('save', { ...this.form });
  //     }
  //   }
  // );
  },
    handleTestConnection() {
      this.testLoading = true;
  // 组装 properties
      const protocol = this.form.protocol;

      const properties = {};
      if (this.isKafkaProtocol) {
        // Kafka 配置
        properties.topicName = this.form.topicName;
        properties.groupId = this.form.groupId;
        properties.autoOffsetReset = this.form.autoOffsetReset;
        properties.bootstrapServers = this.form.bootstrapServers;

      } else {
        // RabbitMQ 配置
        properties.queueName = this.form.queueName;
        properties.prefetchCount = this.form.prefetchCount;
        properties.virtualHost = this.form.virtualHost;
        properties.host = this.form.host;
        properties.port = this.form.port;
      }
      // 通用字段
      // properties.advancedConfig = this.form.advancedConfig;
      properties.connectionTimeout = this.form.connectionTimeout;
      properties.username = this.form.username;
     
      // 组装最终参数
       const params = {
         protocol: protocol,
       };
      if (this.form.id) {
          // ---- 编辑模式 (有 id) ----
          if (this.form.password) {
            // 用户在编辑时输入了新密码（包括空字符串""），说明要修改密码
            properties.password = this.form.password;
          } else {
            // 用户在编辑时密码框为空，代表不想修改密码，此时发送 id 让后端去查
            params.id = this.form.id;
          }
        } else {
          // ---- 创建模式 (没有 id) ----
          // 直接使用用户在密码框输入的值，即便是空字符串""
          properties.password = this.form.password;
        }
    params.properties = JSON.stringify(properties);

    this.$axios({
        method: 'POST',
        url: '/schedule/messageSources/testConnection',
        data: params
      })
      .then(res => {
        this.testLoading = false;
        if (res.data && res.data.code === 200) {
          this.$Message.success(this.$t('messageSchedule.testSuccess'));
        } else {
          this.$Message.error(res.data.msg || this.$t('messageSchedule.testFailed'));
        }
      })
      .catch(err => {
        this.testLoading = false;
        this.$Message.error(this.$t('messageSchedule.testFailed') + ': ' + (err.message || ''));
      });
  },
    onProtocolChange() {
      this.$nextTick(() => {
        if (this.$refs.MessageForm0) this.$refs.MessageForm0.clearValidate();
        if (this.$refs.MessageForm1) this.$refs.MessageForm1.clearValidate();
        if (this.$refs.MessageForm2) this.$refs.MessageForm2.clearValidate();
      });
    },
    addRule() {
      this.form.filterRulesKv = [
    ...this.form.filterRulesKv, 
    { key: '', op: '==', value: '' }
  ];
    },
    removeRule(index) {
      // 保证至少剩下一条规则
      if (this.form.filterRulesKv.length > 1) {
        this.form.filterRulesKv.splice(index, 1);
      } else {
        // 或者清空内容，而不是删除最后一行
        this.form.filterRulesKv[index] = { key: '', op: '==', value: '' };
        // 您也可以在这里使用 UI 库的 $Message.warning 提示用户
      }
    }
  }
}
</script>

<style scoped>
.modal-warp {
  margin: -52px 0 12px 0;
}
.CodeMirror {
  height: 400px;
}
</style> 