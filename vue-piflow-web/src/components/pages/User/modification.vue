<template>
  <section>
    <div class="navbar">
      <div class="left">
        <span>{{$t("sidebar.modification")}}</span>
      </div>
    </div>
    <div class="contentArea">
      <Form ref="formCustom" :model="formCustom" :rules="ruleCustom" :label-width="80">
        <FormItem :label="this.$t('modification_columns.oldPasswd')" prop="oldPasswd">
          <Input type="password" v-model="formCustom.oldPasswd"></Input>
        </FormItem>
        <FormItem :label="this.$t('modification_columns.passwd')" prop="passwd">
          <Input type="password" v-model="formCustom.passwd"></Input>
        </FormItem>
        <FormItem :label="this.$t('modification_columns.passwdCheck')" prop="passwdCheck">
          <Input type="password" v-model="formCustom.passwdCheck"></Input>
        </FormItem>
        <FormItem>
          <Button @click="handleReset('formCustom')">{{$t("modification_columns.Reset")}}</Button>
          <Button type="primary" @click="handleSubmit('formCustom')" style="margin-left: 8px">{{$t("modification_columns.Submit")}}</Button>
        </FormItem>
      </Form>
    </div>
  </section>
</template>

<script>
export default {
  name: "modification",
  components:{},
  data() {
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please enter your new password'));
      } else {
        if (this.formCustom.passwdCheck !== '') {
          this.$refs.formCustom.validateField('passwdCheck');
        }
        callback();
      }
    };
    const validatePassCheck = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please enter your new password again'));
      } else if (value !== this.formCustom.passwd) {
        callback(new Error('The two input passwords do not match!'));
      } else {
        callback();
      }
    };
    return{
      formCustom: {
        oldPasswd: '',
        passwd: '',
        passwdCheck: ''
      },
      ruleCustom: {
        oldPasswd: [
          { required: true, message: 'Please enter your old password', trigger: 'blur' }
        ],
        passwd: [
          { required: true, validator: validatePass, trigger: 'blur' }
        ],
        passwdCheck: [
          { required: true, validator: validatePassCheck, trigger: 'blur' }
        ]
      }
    };
  },
  methods:{
    handleSubmit (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.changePassword();

        } else {
          this.$Message.error('Fail!');
        }
      })
    },

    handleReset (name) {
      this.$refs[name].resetFields();
    },

    changePassword(){
      let parameter= {};
      parameter.oldPassword = this.formCustom.oldPasswd;
      parameter.password = this.formCustom.passwdCheck;
      this.$axios
          .post("/sysUser/updatePassword", this.$qs.stringify(parameter))
          .then((res) => {
            if (res.data.code === 200) {
              this.$Message.success('Success!');
              this.formCustom= {
                oldPasswd: '',
                passwd: '',
                passwdCheck: ''
              };

              this.deleteCookies();
              this.$router.push({ path: "/login" })

            } else {
              this.$Message.error('Fail!');

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

    deleteCookies(){
      let keys = document.cookie.match(/[^ =;]+(?==)/g);
      if (keys) {
        for (let i = keys.length; i--;) {
          document.cookie = keys[i] + '=0;path=/;expires=' + new Date(0).toUTCString()
          document.cookie = keys[i] + '=0;path=/;domain=' + document.domain + ';expires=' + new Date(0).toUTCString()
          document.cookie = keys[i] + '=0;path=/;domain=ratingdog.cn;expires=' + new Date(0).toUTCString()
        }
      }
    },
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

