<template>
    <section>
        <!-- 头部分 -->
        <div class="navbar">
            <div class="left">
                <span>{{$t("sidebar.stopHub")}}</span>
            </div>
            <div class="right">
        <span class="button-warp" @click="handleModalSwitch">
          <Icon type="md-cloud-upload" />
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
                    <Tooltip content="mount" placement="top-start" v-if="row.status.text==='UNMOUNT'">
                        <span class="button-warp" @click="handleButtonSelect(row,1)">
                            <Icon type="ios-aperture" />
                        </span>
                    </Tooltip>
                    <Tooltip v-else content="unmount" placement="top-start">
                       <span class="button-warp" @click="handleButtonSelect(row,1)">
                         <Icon type="md-aperture" />
                       </span>
                    </Tooltip>






<!--                    <Tooltip content="mount" placement="top-start">-->
<!--                        <span class="button-warp" @click="handleButtonSelect(row,1)">-->
<!--                            <Icon type="ios-aperture" />-->
<!--                        </span>-->
<!--                    </Tooltip>-->

<!--                    <Tooltip content="unmount" placement="top-start">-->
<!--                        <span class="button-warp" @click="handleButtonSelect(row,2)">-->
<!--                            <Icon type="md-aperture" />-->
<!--                    </span>-->
<!--                    </Tooltip>-->

                    <Tooltip content="delete" placement="top-start">
                        <span class="button-warp" @click="handleButtonSelect(row,3)">
                            <Icon type="ios-trash"/>
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
        <!-- 上传jar包 -->
        <Modal
                v-model="isOpen"
                :title="$t('modal.upload')"
                :ok-text="$t('modal.upload_text')"
                :cancel-text="$t('modal.cancel_text')"
                footer-hide
                @on-ok="handleSaveUpdateData"
        >
            <div class="modal-warp">
                <div class="item">
                    <Upload
                            action= "/piflow/stops/uploadStopsHubFile"
                            :headers="{'Authorization': token}"
                            style="width:100%"
                            ref="upload"
                            :show-upload-list="true"
                            :on-success="handleSuccess"
                            :on-error="handleError"
                            :format="['jar']"
                            :max-size="2048"
                            :on-format-error="handleFormatError"
                            :on-exceeded-size="handleMaxSize"
                            :before-upload="handleBeforeUpload"
                            type="drag">
                        <div style="padding: 80px 0; height: 240px">
                            <div>
                                <Icon type="ios-cloud-upload" size="52" style="color: #20784b"></Icon>
                                <p>Click or drag jar here to upload</p>
                            </div>
                        </div>
                    </Upload>
                </div>
                <div v-if="JarIsShow !== null">
                    <Icon :color="JarIsShow?'green':'red'" :type="JarIsShow?'md-checkmark-circle':'md-close-circle'" />
                    Upload file: {{ file.name }}
                </div>
            </div>
        </Modal>
    </section>
</template>

<script>
    export default {
        name: "index",
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
                type: "Other",
                description: "",

                typeList: [],

                file: null,
                JarIsShow: null,
                token: ''
            };
        },
        watch: {
            //控制新增还是更新
            isOpen(state) {
                if (!state) {
                    this.handleReset();
                }
            },
            param() {
                this.page = 1;
                this.limit = 10;
                this.getTableData();
            }
        },
        computed: {
            columns() {
                return [
                    {
                        title: this.$t("StopHub_columns.name"),
                        key: "jarName",
                        sortable: true
                    },
                    {
                        title: this.$t("StopHub_columns.version"),
                        key: "version",
                        sortable: true,
                        width: 120,
                    },
                    {
                        title: this.$t("StopHub_columns.jarUrl"),
                        key: "jarUrl"
                    },
                    {
                        title: this.$t("StopHub_columns.status"),
                        key: "status",
                        sortable: true,
                        render: (h, params) => {
                            return h('span', params.row.status.text);
                        },
                        width: 150,
                    },
                    {
                        title: this.$t("StopHub_columns.action"),
                        slot: "action",
                        width: 180,
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
            // console.log(this.$refs.upload.fileList)
            let token = this.$store.state.variable.token;
            if (!token) {
                token = `${window.sessionStorage.getItem("token")}`
            }
            this.token = `Bearer ${token}`;
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
                // this.driverMemory = "1g";
                // this.executorNumber = 1;
                // this.executorMemory = "1g";
                // this.executorCores = 1;
            },
            handleButtonSelect(row, key) {
                switch (key) {
                    case 1:
                        this.handleMount(row);
                        break;
                    case 3:
                        this.handleDeleteRow(row);
                        break;
                    default:
                        break;
                }
            },
            //  上传jar包测试
            handleUpload (file) {
                this.file = file;
                return false;
            },
            // 文件上传成功 response, file, fileList
            handleSuccess (res, file) {
                this.JarIsShow = true;
                console.log(res)
                console.log('==============================')
                console.log(file)
            },
            // 文件上传失败时的钩子，返回字段为 error, file, fileList
            handleError ( error, file) {
                this.JarIsShow = false;
                console.log(error)
                console.log('---------------------------')
                console.log(file)
            },
            // 文件格式验证失败时的钩子，返回字段为 file, fileList
            handleFormatError (file) {
                this.$Notice.warning({
                    title: 'The file format is incorrect',
                    desc: 'File format of ' + file.name + ' is incorrect, please select jar.'
                });
            },
            // 文件超出指定大小限制时的钩子，返回字段为 file, fileList
            handleMaxSize (file) {
                this.$Notice.warning({
                    title: 'Exceeding file size limit',
                    desc: 'File  ' + file.name + ' is too large, no more than 2M.'
                });
            },
            // 上传文件之前的钩子，参数为上传的文件，若返回 false 或者 Promise 则停止上传
            handleBeforeUpload (file) {
                this.file = file;
                const check = true;
                if (!check) {
                    this.$Notice.warning({
                        title: 'Up to five pictures can be uploaded.'
                    });
                }
                return true;
            },


            // 上传jar包
            handleSaveUpdateData() {
                setTimeout(() => {
                    this.file = null;
                    this.$Message.success('Success')
                }, 1500);

                // let data = {
                //     dataSourceType: this.type,
                //     dataSourceName: this.name,
                //     dataSourceDescription: this.description
                // };

                // if (this.id) {
                //     //更新数据
                //     data.id = this.id;
                //     this.$axios
                //         .post("/datasource/saveOrUpdate", this.$qs.stringify(data))
                //         .then(res => {
                //             if (res.data.code == 200) {
                //                 this.$Modal.success({
                //                     title: this.$t("tip.tilte"),
                //                     content: `${this.name} ` + this.$t("tip.update_success_content")
                //                 });
                //                 this.isOpen = false;
                //                 this.handleReset();
                //                 this.getTableData();
                //             } else {
                //                 this.$Modal.error({
                //                     title: this.$t("tip.tilte"),
                //                     content: `${this.name} ` + this.$t("tip.update_fail_content")
                //                 });
                //             }
                //         })
                //         .catch(error => {
                //             console.log(error);
                //             this.$Modal.error({
                //                 title: this.$t("tip.tilte"),
                //                 content: this.$t("tip.fault_content")
                //             });
                //         });
                // }
                // else {
                //     //新增数据
                //     this.$axios
                //         .post("/datasource/saveOrUpdate", this.$qs.stringify(data))
                //         .then(res => {
                //             if (res.data.code == 200) {
                //                 this.$Modal.success({
                //                     title: this.$t("tip.tilte"),
                //                     content: `${this.name} ` + this.$t("tip.add_success_content")
                //                 });
                //                 this.isOpen = false;
                //                 this.handleReset();
                //                 this.getTableData();
                //             } else {
                //                 this.$Modal.error({
                //                     title: this.$t("tip.tilte"),
                //                     content: `${this.name} ` + this.$t("tip.add_fail_content")
                //                 });
                //             }
                //         })
                //         .catch(error => {
                //             console.log(error);
                //             this.$Modal.error({
                //                 title: this.$t("tip.tilte"),
                //                 content: this.$t("tip.fault_content")
                //             });
                //         });
                // }
            },
            handleRemove(m, mark) {
                if (mark) {
                    this.$Modal.warning({
                        title: this.$t("tip.tilte"),
                        content: "此项不可删除，请重新操作！"
                    });
                    return;
                }
                // this.dataSourcePropertyVoList.splice(m, 1);
            },

            // 挂载/卸载
            handleMount(row) {
                let data = { id: row.id };
                let url = "/stops/unmountStopsHub";
                if (row.status.text === "UNMOUNT") {
                    url = "/stops/mountStopsHub";
                }

                this.$event.emit("looding", true);
                this.$axios
                    .post(url, this.$qs.stringify(data))
                    .then(res => {
                        if (res.data.code == 200) {
                            this.$event.emit("looding", false);
                            this.$Modal.success({
                                title: this.$t("tip.tilte"),
                                content: `${row.jarName} ` + this.$t("tip.stop_success_content")
                            });
                            this.getTableData();
                        } else {
                            this.$event.emit("looding", false);
                            this.$Modal.success({
                                title: this.$t("tip.tilte"),
                                content: `${row.jarName} ` + this.$t("tip.stop_fail_content")
                            });
                        }
                    })
                    .catch(error => {
                        console.log(error);
                        this.$event.emit("looding", false);
                        this.$Modal.error({
                            title: this.$t("tip.tilte"),
                            content: this.$t("tip.fault_content")
                        });
                    });
            },

            //删除某一行数据
            handleDeleteRow(row) {
                if ( row.status.text==='MOUNT' ){
                    this.$Modal.warning({
                        title: this.$t("tip.tilte"),
                        content:
                            `${row.jarName} ` +
                            'In operation, temporarily unable to delete !'
                    });
                }else {
                    this.$Modal.confirm({
                        title: this.$t("tip.tilte"),
                        okText: this.$t("modal.confirm"),
                        cancelText: this.$t("modal.cancel_text"),
                        content: `${this.$t("modal.delete_content")} ${row.jarName}?`,
                        onOk: () => {
                            let data = {
                                id: row.id
                            };
                            this.$axios
                                .post("/stops/delStopsHub", this.$qs.stringify(data))
                                .then(res => {
                                    if (res.data.code == 200) {
                                        this.$Modal.success({
                                            title: this.$t("tip.tilte"),
                                            content:
                                                `${row.jarName} ` +
                                                this.$t("tip.delete_success_content")
                                        });
                                        this.handleReset();
                                        this.getTableData();
                                    } else {
                                        this.$Modal.error({
                                            title: this.$t("tip.tilte"),
                                            content: this.$t("tip.delete_fail_content")
                                        });
                                    }
                                })
                                .catch(error => {
                                    console.log(error);
                                    this.$Modal.error({
                                        title: this.$t("tip.tilte"),
                                        content: this.$t("tip.fault_content")
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
                    .get("/stops/stopsHubListPage", {
                        params: data
                    })
                    .then(res => {
                        if (res.data.code == 200) {
                            this.tableData = res.data.data;
                            this.total = res.data.count;
                        } else {
                            this.$Modal.error({
                                title: this.$t("tip.tilte"),
                                content: this.$t("tip.request_fail_content")
                            });
                        }
                    })
                    .catch(error => {
                        console.log(error);
                        this.$Modal.error({
                            title: this.$t("tip.tilte"),
                            content: this.$t("tip.fault_content")
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
                this.isOpen = !this.isOpen;
            }
        }
    }
</script>

<style lang="scss" scoped>
    @import "./index.scss";
    .item {
        display: flex;

        label {
            margin-top: 5px;
        }
    }
    /deep/ .ivu-upload-drag:hover{
                 border: 1px dashed #20784b;
             }
</style>