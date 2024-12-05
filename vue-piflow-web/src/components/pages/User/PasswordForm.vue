<template>
  <Form
    ref="formCustom"
    :model="formCustom"
    :rules="ruleCustom"
    :label-width="120"
  >
    <FormItem
      :label="this.$t('modification_columns.oldPasswd')"
      prop="oldPasswd"
    >
      <Input type="password" v-model="formCustom.oldPasswd"></Input>
    </FormItem>
    <FormItem :label="this.$t('modification_columns.passwd')" prop="passwd">
      <Input type="password" v-model="formCustom.passwd"></Input>
    </FormItem>
    <FormItem
      :label="this.$t('modification_columns.passwdCheck')"
      prop="passwdCheck"
    >
      <Input type="password" v-model="formCustom.passwdCheck"></Input>
    </FormItem>
    <FormItem>
      <Button @click="handleReset('formCustom')">{{
        $t("modification_columns.Reset")
      }}</Button>
      <Button
        type="primary"
        @click="handleSubmit('formCustom')"
        style="margin-left: 8px"
        >{{ $t("modification_columns.Submit") }}</Button
      >
    </FormItem>
  </Form>
</template>

<script>
import {aesMinEncrypt} from "@/utils/crypto.js"
import { validatePassword } from '@/utils'
import Cookies from "js-cookie";
export default {
  props:{
    mode:String
  },
  data() {
    const validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error(this.$t("user_columns.newPsd")));
      } else if (value == this.formCustom.oldPasswd) {
        callback(new Error(this.$t("user_columns.passwordDiff")));
      } else if (!validatePassword(value)) {
        callback(new Error(this.$t("user_columns.passwordComplexity")));
      } else {
        if (this.formCustom.passwdCheck !== "") {
          this.$refs.formCustom.validateField("passwdCheck");
        }
        callback();
      }
    };

    const validatePassCheck = (rule, value, callback) => {
      if (value === "") {
        callback(new Error(this.$t("user_columns.newPsdCheck")));
      } else if (value !== this.formCustom.passwd) {
        callback(new Error(this.$t("user_columns.psdCheckDiff")));
      } else {
        callback();
      }
    };
    return {
      formCustom: {
        oldPasswd: "",
        passwd: "",
        passwdCheck: "",
      },
      ruleCustom: {
        oldPasswd: [
          {
            required: true,
            message: this.$t("user_columns.oldPsd"),
            trigger: "blur",
          },
        ],
        passwd: [{ required: true, validator: validatePass, trigger: "blur" }],
        passwdCheck: [
          { required: true, validator: validatePassCheck, trigger: "blur" },
        ],
      },
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
              Cookies.set('changePsd',2)
              this.$emit('submit')
              this.handleReset()
              if(this.mode === 'passwordModal'){
                  this.getIsInBootPage()
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
    getIsInBootPage(){
      this.$axios
              .get("/bootPage/isInBootPage")
              .then((res) => {
                var dataMap = res.data;
                if (dataMap.code === 200 && dataMap.isIn === true) {
                  this.$router.push({
                    name: 'bootPage',
                    path: '/bootPage'
                  })
                }else if (dataMap.code === 200 && dataMap.isIn === false){
                  if (this.$route.query.redirect) { //如果存在参数
                    let redirect = this.$route.query.redirect;
                    this.$router.push(redirect)//则跳转至进入登录页前的路由
                  } else {
                    this.$router.push("/"); //否则跳转至首页
                  }
                } else {
                  this.$Modal.success({
                    title: this.$t("tip.title"),
                    content: this.$t("tip.request_fail_content"),
                  });
                }
              })
              .catch((error) => {
                console.log(error);
              });
    },
  }
};
</script>