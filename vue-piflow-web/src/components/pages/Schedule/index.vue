<template>
    <section>
        <!-- 头部分 -->
        <div class="navbar">
            <div class="left">
                <span>{{$t("sidebar.schedule")}}</span>
            </div>
            <div class="right">
        <span class="button-warp" @click="handleModalSwitch">
          <Icon type="md-add"/>
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
                    <Tooltip content="enter" placement="top-start">
                        <span class="button-warp" @click="handleButtonSelect(row,1)">
                            <Icon type="ios-redo" />
                        </span>
                    </Tooltip>
                    <Tooltip content="edit" placement="top-start">
                       <span class="button-warp" @click="handleButtonSelect(row,2)">
                        <Icon type="ios-create-outline"/>
                       </span>
                    </Tooltip>
                    <Tooltip content="stop" placement="top-start" v-if="row.status.text==='RUNNING'">
                        <span class="button-warp" @click="handleButtonSelect(row,3)">
                            <Icon type="ios-square" size="12"/>
                        </span>
                    </Tooltip>
                    <Tooltip v-else content="run" placement="top-start">
                       <span class="button-warp" @click="handleButtonSelect(row,3)">
                         <Icon type="ios-play" />
                       </span>
                    </Tooltip>
                    <Tooltip content="delete" placement="top-start">
                        <span class="button-warp" @click="handleButtonSelect(row,4)">
                            <Icon type="ios-trash" />
                        </span>
                    </Tooltip>
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
        <!-- 弹窗添加/更新部分 -->
        <Modal
                v-model="isOpen"
                :title="id?$t('schedule_columns.update_title'):$t('schedule_columns.create_title')"
                :ok-text="$t('modal.ok_text')"
                :cancel-text="$t('modal.cancel_text')"
                @on-ok="handleSaveUpdateData"
        >
            <div class="modal-warp Schedule">
                <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80">
                    <FormItem :label="$t('schedule_columns.scheduleType')">
                        <Select v-model="formValidateType.type" placeholder="type" style="width: 350px">
                            <Option
                                    v-for="item in formValidateType.typeList"
                                    :value="item.name"
                                    :key="item.name"
                            >{{ item.name }}</Option>
                        </Select>
                    </FormItem>
                    <FormItem :label="$t('schedule_columns.cron')" prop="cron">
                        <Input
                            v-model="formValidate.cron"
                            show-word-limit
                            maxlength="100"
                            :placeholder="$t('modal.placeholder')"
                            style="width: 350px" />
                    </FormItem>
                    <FormItem class="form-content" :label="$t('schedule_columns.startDate')">
                        <Row>
                            <Col span="11">
                                <FormItem prop="date">
                                    <DatePicker type="date" placeholder="Select date"  :value="formValidate.startDate" @on-change="handleChangeStartDate"></DatePicker>
                                </FormItem>
                            </Col>
                            <Col span="1" style="text-align: center">-</Col>
                            <Col span="11">
                                <FormItem prop="time">
                                    <TimePicker type="time" placeholder="Select time" v-model="formValidate.startTime"></TimePicker>
                                </FormItem>
                            </Col>
                        </Row>
                    </FormItem>
                    <FormItem class="form-content" :label="$t('schedule_columns.endDate')">
                        <Row>
                            <Col span="11">
                                <FormItem prop="date">
                                    <DatePicker type="date" placeholder="Select date" :value="formValidate.endDate" @on-change="handleChangeEndDate"></DatePicker>
                                </FormItem>
                            </Col>
                            <Col span="1" style="text-align: center">-</Col>
                            <Col span="11">
                                <FormItem prop="time">
                                    <TimePicker type="time" placeholder="Select time" v-model="formValidate.endTime"></TimePicker>
                                </FormItem>
                            </Col>
                        </Row>
                    </FormItem>
                    <FormItem :label="$t('schedule_columns.flowIsGroup')" prop="suType">
                        <Select v-model="formValidate.suType" style="width: 350px">
                            <Option
                                    v-for="item in subTypeList"
                                    :value="item.id"
                                    :key="item.id"
                            >{{ item.name }}</Option>
                        </Select>
                    </FormItem>
                </Form>
            </div>
        </Modal>
    </section>
</template>

<script>
    export default {
        name: "schedule",
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
                row: null,
                id: "",
                name: "",


                subTypeList: [],
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
                formValidate: {
                    cron: '',
                    startDate: '',
                    startTime: '',
                    endDate: '',
                    endTime: '',
                    suType: "",
                },
                ruleValidate: {
                    cron: [
                        { required: true, message: 'The cron cannot be empty', trigger: 'blur' }
                    ],
                    suType: [
                        { required: true, message: 'Please select the flow / Group', trigger: 'change' }
                    ]
                }
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
            },
            formValidateType:{
                handler(newVal){
                    this.handleGetTemplateData(newVal.type);
                },
                deep:true,
                immediate:true,
            },
            // formValidateType
        },
        computed: {
            columns() {
                return [
                    {
                        title: this.$t("schedule_columns.name"),
                        key: "scheduleRunTemplateName",
                        sortable: true
                    },
                    {
                        title: this.$t("schedule_columns.type"),
                        key: "type",
                        sortable: true
                    },
                    {
                        title: this.$t("schedule_columns.cron"),
                        key: "cronExpression",
                        sortable: true
                    },
                    {
                        title: this.$t("schedule_columns.StartTime"),
                        key: "planStartTimeStr",
                        sortable: true
                    },
                    {
                        title: this.$t("schedule_columns.EndTime"),
                        key: "planEndTimeStr",
                        sortable: true
                    },
                    {
                        title: this.$t("schedule_columns.status"),
                        key: "status",
                        sortable: true,
                        render: (h, params) => {
                            return h('span', params.row.status.text);
                            }
                    },
                    {
                        title: this.$t("schedule_columns.action"),
                        slot: "action",
                        width: 230,
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
                this.formValidateType.type = "FLOW";
                this.row = null;
                this.name = "";
                this.formValidate = {};
                this.formValidate.suType = ' ';
            },
            handleButtonSelect(row, key) {
                switch (key) {
                    case 1:
                        // eslint-disable-next-line no-case-declarations
                        let src = "/drawingBoard/page/flow/mxGraph/index.html?load=" + row.scheduleRunTemplateId + '&BreadcrumbSchedule';
                        if ( row.type === "FLOW_GROUP" ){
                            src = `/drawingBoard/page/flowGroup/mxGraph/index.html?drawingBoardType=GROUP&load=${row.scheduleRunTemplateId}` + '&BreadcrumbSchedule'
                        }
                        this.$event.emit("crumb", [
                            { name: "flow", path: "/schedule" },
                            { name: "drawingBoard", path: "/drawingBoard" },
                        ]);
                        this.$router.push({
                            path: "/drawingBoard",
                            query: {
                                src : src,
                            },
                        });
                        break;
                    case 2:
                        this.getRowData(row);
                        break;
                    case 3:
                        this.handleStop(row);
                        break;
                    case 4:
                        this.handleDeleteRow(row);
                        break;
                    default:
                        break;
                }
            },
            // 新增/更新一条数据
            handleSaveUpdateData() {
                let data = {
                    type: this.formValidateType.type,
                    scheduleRunTemplateId: this.formValidate.suType,
                    cronExpression: this.formValidate.cron,
                    planStartTimeStr: this.formValidate.startDate + ' ' +  this.formValidate.startTime,
                    planEndTimeStr: this.formValidate.endDate + ' ' +  this.formValidate.endTime,
                };
                if (this.id) {
                    //更新数据
                    data.id = this.id;
                    this.$axios
                        .post("/schedule/updateSchedule", this.$qs.stringify(data))
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
                } else {
                    //新增数据
                    this.$axios
                        .post("/schedule/addSchedule", this.$qs.stringify(data))
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
            handleGetTemplateData(type) {
                // let data = {page: this.page, limit: this.limit};
                let data = {page: 1, limit: 10000};
                if (type === 'FLOW'){
                    this.$axios
                        .get("/flow/getFlowListPage", {
                            params: data
                        })
                        .then(res => {
                            if (res.data.code == 200) {
                                this.subTypeList = res.data.data;
                                // this.total = res.data.count;
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
                }else if(type === 'FLOW_GROUP'){
                    this.$axios
                        .get("/flowGroup/getFlowGroupListPage", {
                            params: data
                        })
                        .then(res => {
                            if (res.data.code == 200) {
                                this.subTypeList = res.data.data;
                                // this.total = res.data.count;
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
                }
            },

            //运行停止/开始
            handleStop(row) {
                let data = { scheduleId: row.id };
                let url = "/schedule/stopSchedule";
                // eslint-disable-next-line no-unused-vars
                let flag = false;
                if (row.status.text === "STOP" || row.status.text === "INIT") {
                    // data.processType =
                    //     row.processType === "TASK" ? "PROCESS" : "PROCESS_GROUP";
                    url = "/schedule/startSchedule";
                    flag = true;
                }

                this.$event.emit("looding", true);
                this.$axios
                    .post(url, this.$qs.stringify(data))
                    .then(res => {
                        if (res.data.code == 200) {
                            this.$event.emit("looding", false);
                            this.$Modal.success({
                                title: this.$t("tip.title"),
                                content: flag? `${row.cronExpression} ` + this.$t("tip.run_success_content"): `${row.cronExpression} ` + this.$t("tip.stop_success_content")
                            });
                            this.getTableData();
                        } else {
                            this.$event.emit("looding", false);
                            this.$Message.error({
                              content: flag? `${row.cronExpression} ` + this.$t("tip.run_fail_content"): `${row.cronExpression} ` + this.$t("tip.stop_fail_content"),
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

            //获取行数据(编辑)
            getRowData(row) {
                this.$event.emit("looding", true);
                this.$axios
                    .get("/schedule/getScheduleById", {
                        params: {scheduleId: row.id}
                    })
                    .then(res => {
                        if (res.data.code == 200) {
                            let flow = res.data.scheduleVo;
                            this.id = flow.id;
                            this.formValidateType.type = flow.type;
                            this.formValidate.suType = flow.scheduleRunTemplateId;
                            this.formValidate.cron = flow.cronExpression;
                            if (flow.planStartTimeStr){
                                flow.planStartTimeStr = flow.planStartTimeStr.split(' ');
                                this.formValidate.startDate = flow.planStartTimeStr[0];
                                this.formValidate.startTime = flow.planStartTimeStr[1];
                            }
                            if (flow.planEndTimeStr){
                                flow.planEndTimeStr = flow.planEndTimeStr.split(' ');
                                this.formValidate.endDate = flow.planEndTimeStr[0];
                                this.formValidate.endTime = flow.planEndTimeStr[1];
                            }

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
                if ( row.status.text==='RUNNING' ){
                    this.$Modal.warning({
                        title: this.$t("tip.title"),
                        content:
                            `${row.cronExpression} ` +
                            'In operation, temporarily unable to delete !'
                    });
                }else {
                    this.$Modal.confirm({
                        title: this.$t("tip.title"),
                        okText: this.$t("modal.confirm"),
                        cancelText: this.$t("modal.cancel_text"),
                        content: `${this.$t("modal.delete_content")} ${row.cronExpression}?`,
                        onOk: () => {
                            let data = {
                                scheduleId: row.id
                            };
                            this.$axios
                                .post("/schedule/delSchedule", this.$qs.stringify(data))
                                .then(res => {
                                    if (res.data.code == 200) {
                                        this.$Modal.success({
                                            title: this.$t("tip.title"),
                                            content:
                                                `${row.cronExpression} ` +
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
                }
            },
            //获取表格数据
            getTableData() {
                let data = {page: this.page, limit: this.limit};
                if (this.param) {
                    data.param = this.param;
                }
                this.$axios
                    .get("/schedule/getScheduleVoListPage", {
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
            //  点击页码 / 展示数量
            onPageChange(pageNo) {
                this.page = pageNo;
                this.getTableData();
            },
            onPageSizeChange(pageSize) {
                this.limit = pageSize;
                this.getTableData();
            },
            // 弹窗显隐切换
            handleModalSwitch() {
                this.isOpen = !this.isOpen;
            },
            // 获取创建日期格式转换
            handleChangeStartDate (date) {
                console.log(date)
                this.formValidate.startDate = date;
            },
            handleChangeEndDate (date) {
                this.formValidate.endDate = date;
            },
        }
    }
</script>

<style lang="scss" scoped>
    @import "./index.scss";
</style>