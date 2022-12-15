<template>
    <section>
        <div class="navbar">
            <!-- 初始栏的建立 -->
            <div class="left">
                <span>{{$t("sidebar.log")}}</span>
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
    </section>
</template>

<script>
export default {
    name:"log",
    components:{},
    data() {
        return{
            isOpen :false,
            page:1,
            limit:10,
            total:0,
            tableData:[],
            param:"",

            row:null,
            username:"",
            lastLoginIp:"",
            action:"",
            lastUpdateDttm:""
        };
    },

     //设置的是最开始可以看到的数据
    watch:{
        //此处设置页面展示的页数是多少个
        isOpen(state) {
            if (!state) {
                this.handleReset();
            }
        },
        // 此处判断最开始显示第几页并且显示多少条数据
        param(/*val*/) {
            this.page = 1;
            this.limit = 10;
            this.getTableData();
        }
    },

    computed:{
        columns() {
            return[
                {
                    title:this.$t("log_columns.username"),
                    key:"username",
                    sortable:true
                },
                {
                    title:this.$t("log_columns.lastLoginIp"),
                    key:"lastLoginIp"
                },
                {
                    title:this.$t("log_columns.lastUpdateTime"),
                    key:"lastUpdateDttm"
                },
                {
                    title:this.$t("log_columns.action"),
                    key:"action"
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
            this.lastLoginIp = "";
            this.password= "";
            this.status = "";
        },

        getTableData() {
            let data = { page: this.page, limit: this.limit };
            if (this.param) {
                data.param = this.param;
            }
            this.$axios
            .get("/log/getLogListPage", {
                params: data
            })
            .then(res => {
                if (res.data.code === 200) {
                    let data = res.data.data;
                    this.tableData = data.map(item => {
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
                    content: this.$t("tip.fault_content"),
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
</style>

