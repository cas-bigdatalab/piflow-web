<template>
  <section>
    <div class="navbar">
      <div class="left">
        <span>{{$t("sidebar.modification")}}</span>
      </div>
    </div>
    <div class="contentArea">
      <!-- <Form ref="formCustom" :model="formCustom" :rules="ruleCustom" :label-width="80">
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
      </Form> -->
      <PasswordForm/>
    </div>
  </section>
</template>

<script>
import {aesMinEncrypt} from "@/utils/crypto.js"
import { validatePassword } from '@/utils'
import PasswordForm from './PasswordForm.vue'
export default {
  name: "modification",
  components:{PasswordForm},
  data() {
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error(this.$t('user_columns.newPsd')));
      }else if (value == this.formCustom.oldPasswd) {
        callback(new Error(this.$t('user_columns.passwordDiff')));
      }else if(!validatePassword(value)){
        callback(new Error(this.$t('user_columns.passwordComplexity')));
      }else {
        if (this.formCustom.passwdCheck !== '') {
          this.$refs.formCustom.validateField('passwdCheck');
        }
        callback();
      }
    };
    
    const validatePassCheck = (rule, value, callback) => {
      if (value === '') {
        callback(new Error(this.$t('user_columns.newPsdCheck')));
      } else if (value !== this.formCustom.passwd) {
        callback(new Error(this.$t('user_columns.psdCheckDiff')));
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
          { required: true, message: this.$t('user_columns.oldPsd'), trigger: 'blur' }
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
    handleSubmit () {
      this.$refs.formCustom.validate((valid) => {
        if (valid) {
          this.changePassword();

        } else {
          this.$Message.error('Fail!');
        }
      })
    },

    handleReset () {
      this.$refs.formCustom.resetFields();
    },

    changePassword(){
      let parameter= {};
      parameter.oldPassword = aesMinEncrypt(this.formCustom.oldPasswd);
      parameter.password = aesMinEncrypt(this.formCustom.passwdCheck);
      this.$axios
          .post("/sysUser/updatePassword", this.$qs.stringify(parameter))
          .then((res) => {
            if (res.data.code === 200) {
              this.$Message.success('Success!');
              this.handleReset()
              if (this.$route.query.redirect) {
                //如果存在参数
                let redirect = this.$route.query.redirect;
                this.$router.push(redirect); //则跳转至进入登录页前的路由
              } else {
                this.$router.push("/home"); //否则跳转至首页
              }
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

