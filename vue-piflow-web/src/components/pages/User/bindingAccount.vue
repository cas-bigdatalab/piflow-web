<template>
  <section>
    <div class="navbar">
      <div class="left">
        <span>{{$t("sidebar.bindingAccount")}}</span>
      </div>
    </div>
    <div class="contentArea">
      <Form ref="formCustom" :model="formCustom" :label-width="80">
        <FormItem :label="this.$t('bindingAccount_columns.value')">
          <Input type="password" v-model="formCustom.value"></Input>
        </FormItem>
        <FormItem>
          <Button @click="handleReset('formCustom')">{{$t("bindingAccount_columns.Reset")}}</Button>
          <Button type="primary" @click="handleSubmit('formCustom')" style="margin-left: 8px">{{$t("bindingAccount_columns.Submit")}}</Button>
        </FormItem>
      </Form>
    </div>
  </section>
</template>

<script>
export default {
  name: "bindingAccount",
  components:{},
  data() {
    return{
      formCustom: {
        value: ''
      },
    };
  },
  methods:{
    handleSubmit (name) {
      this.changePassword();
    },

    handleReset () {
      this.formCustom.value= '';
    },

    changePassword(){
      let parameter= {};
      parameter.accessKey = this.formCustom.value;
      this.$axios
          .post("/user/bindDeveloperAccessKey", this.$qs.stringify(parameter))
          .then((res) => {
            if (res.data.code === 200) {
              this.$Message.success({
                content: res.data.errorMsg,
                duration: 3
              });

            } else {
              this.$Message.error(res.data.errorMsg);

            }
          })
          .catch((error) => {
            console.log(error);
          });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
.contentArea{
  background: white;
  padding: 50px 20px 20px 20px;
  border-radius: 20px;
  margin-top: 20px;
  text-align: center;
  form{
    width: 60%;
    margin: 0 auto;
  }
  ::v-deep .ivu-form .ivu-form-item-label{
    line-height: inherit;
    width: 180px!important;
  }
  ::v-deep .ivu-form-item-content{
    margin-left: 180px!important;
  }
}
</style>

