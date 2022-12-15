<template>
    <section>
        <div class="navbar">
            <div class="left">
                <span>{{$t("sidebar.user")}}</span>
            </div>

        </div>

        <div class="input">
            <Input
                suffix="ios-search"
                v-model="param"
                :placeholder="$t('modal.placeholder')"
                style="width: 300px"
            />
        </div>

        <Table border :columns="columns" :data="tableData">
            <template slot-scope="{ row }" slot="action">
                <Tooltip content="Edit" placement="top-start">
                    <span class="button-warp" @click="handleButtonSelect(row,1)">
                    <Icon type="ios-create-outline" />
                    </span>
                </Tooltip>
                <Tooltip content="Delete" placement="top-start">
                    <span class="button-warp" @click="handleButtonSelect(row,2)">
                    <Icon type="ios-trash" />
                    </span>
                </Tooltip>
            </template>
        </Table>

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

        <Modal
            v-model="isOpen"
            :title="id?$t('user_columns.update_title'):$t('user_columns.create_title')"
            :ok-text="$t('modal.ok_text')"
            :cancel-text="$t('modal.cancel_text')"
            @on-ok="handleSaveUpdateData">
            <div class="modal-warp">
                <div class="item">
                    <label>{{$t('user_columns.username')}}：</label>
                <Input
                    v-model="username"
                    show-word-limit
                    maxlength="100"
                    disabled placeholder="$t('modal.placeholder')"
                    style="width: 350px"/>
                </div>
                
                <div class="item">
                    <label>{{$t('user_columns.name')}}：</label>
                    <Input
                    v-model="name"
                    show-word-limit
                    maxlength="100"
                    :placeholder="$t('modal.placeholder')"
                    style="width: 350px"/>
                </div>
                
                <div class="item">
                    <label>{{$t('user_columns.password')}}：</label>
                    <Input
                        v-model="password"
                        show-word-limit
                        maxlength="100"
                        disabled placeholder="$t('modal.placeholder')"
                        style="width: 290px"/>
                        <Button class="button-warp" 
                        @click="handleResetPassword">{{$t('modal.reset_text')}}</Button>
                </div>

                <div class="item">
                    <label>{{$t('user_columns.status')}}：</label>
                        <Select 
                        v-model="status" 
                        :placeholder="$t('modal.placeholder')"
                        style="width:350px">
                            <Option v-for="(item, index) in statusList" :key="index" :label="item" :value="index" />
                        </Select>
                </div>

            </div>
        </Modal>
    </section>
</template>
<script>
export default {
    name:"user",
    components:{},
    data() {
        return{
            isOpen :false,
            page:1,
            limit:10,
            total:0,
            tableData:[],
            param:"",
            statusList:['Working','Freezing','Closing'],

            row:null,
            id:"",
            name:"",
            username:"",
            password:"",
            crtDttm:"",
            status:""
        };
    },
    
    watch:{
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
    computed:{
        columns() {
            return[
                {
                    title:this.$t("user_columns.name"),
                    key:"name",
                    sortable:true
                },
                {
                    title:this.$t("user_columns.username"),
                    key:"username",
                    sortable:true
                },
                {
                    title:this.$t("user_columns.createTime"),
                    key:"crtDttm"
                },
                {
                    title:this.$t("user_columns.status"),
                    key:"status",
                    
                    render: (h, params) => {
                            const row = params.row;
                            const color = row.status === 0 ? 'primary' : row.status === 2 ? 'success' : 'error';
                            const text = this.statusList[row.status];
                            return h('Tag', {
                                props: {
                                    type: 'dot',
                                    color: color
                                }
                            }, text);
                        }
                },
                {
                    title:this.$t("user_columns.role"),
                    key:"role"
                },
                {
                title: this.$t("user_columns.action"),
                slot: "action",
                width: 200,
                align: "center"
                }
            ];
        }
    },

    created() {
        this.getTableData();
    },
    methods:{
      handleReset() {
            this.page = 1;
            this.limit = 10;
            this.id = "";
            this.name = "";
            this.username = "";
            this.password= "";
            this.status = "";
        },

      handleResetPassword() {
            this.password= this.username;
        },

      handleButtonSelect(row,key) {
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

      handleSaveUpdateData() {
            let data = {
                name : this.name,
                username: this.username,
                password: this.password,
                status: this.status,
                role:this.role
            };
            if (this.id) {
                data.id = this.id;
                this.$axios
                .get("/user/updateUser",{params : data})
                .then(res=>{
                    if(res.data.code === 200) {
                        this.$Modal.success({
                            title:this.$t("tip.title"),
                            content:`${this.name} ` + this.$t("tip.update_success_content")
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
            }
        },

      getRowData(row) {
            this.$event.emit("loading", true);
            this.$axios
                .get("/user/getUserById", { params: { userId: row.id }})
                .then(res => {
                    this.$event.emit("loading", false);
                    if (res.data.code === 200) {
                    let data = res.data.sysUserVo;
                    this.id = data.id;
                    this.name = data.name;
                    this.username = data.username;
                    this.password = data.password;
                    this.status = data.status;
                    this.role  = data.role;
                    this.$event.emit("loading", false);
                    this.isOpen = true;
                } else {
                    this.$Message.error({
                        content: this.$t("tip.get_fail_content"),
                        duration: 3
                        });
                    }
                })
            .catch(error => {
                this.$event.emit("loading", false);
                console.log(error);
                this.$Message.error({
                    content: this.$t("tip.get_fail_content"),
                    duration: 3
                });
            });
        },

      handleDeleteRow(row) {
            this.$Modal.confirm({
                title: this.$t("tip.title"),
                okText: this.$t("modal.confirm"),
                cancelText: this.$t("modal.cancel_text"),
                content: `${this.$t("modal.delete_content")} ${row.name}?`,
                onOk: () => {
                let data = {
                    sysUserId: row.id
                };
                this.$axios
                .get("/user/delUser", { params: data })
                .then(res => {
                if (res.data.code === 200) {
                    this.$Modal.success({
                    title: this.$t("tip.title"),
                    content:
                    `${row.name} ` + this.$t("tip.delete_success_content")
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
                content: this.$t("tip.get_fail_content"),
                duration: 3
                });
                });
            }
        })
    },

      getTableData() {
        let data = { page: this.page, limit: this.limit };
        if (this.param) {
            data.param = this.param;
        }
        this.$axios
        .get("/user/getUserListPage", {
            params: data
        })
        .then(res => {
            if (res.data.code === 200) {
                let data = res.data.data;
                this.tableData = data.map(item => {
                item.role = item.role.role.stringValue;
                return item;
                });
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
                content: this.$t("tip.get_user_content"),
                duration: 3
            });
        });
    },

      onPageChange(pageNo) {
        this.page = pageNo;
        this.getTableData()
    },

      onPageSizeChange(pageSize) {
        this.limit = pageSize;
        this.getTableData()
    },

      handleModalSwitch() {
        this.isOpen = !this.isOpen;
        }
    }
};
</script>
<style lang="scss" scoped>
@import "./index.scss";
::v-deep .ivu-tag-dot{
  border: none!important;
}
</style>