<template>
  <div class="login">
    <div class="header">
      <div class="logo"></div>
      <div class="title">
        <h1>{{title }}</h1>
      </div>
    </div>

    <i class="map"></i>
    <div class="content" :class="!isLogin?'ivu-logn':'ivu-logn-1'">
      <div class="login-con">
        <div class="ivu-card">
          <div class="ivu-card-head">
            <i class="ivu-icon ivu-icon-log-in"></i>
            <span v-if="isLogin">Welcome to login</span>
            <span v-else>Welcome to register</span>
          </div>

          <div class="ivu-card-body">
            <div class="form-con">
              <div class="ivu-form ivu-form-label-right">
                <!-- 账号 -->
                <div class="ivu-form-item ivu-form-item-required">
                  <div class="ivu-form-item-content">
                    <div class="ivu-input-wrapper ivu-input-wrapper-default ivu-input-type ivu-input-group ivu-input-group-default ivu-input-group-with-prepend">
                      <div class="ivu-input-group-prepend">
                        <i class="ivu-icon ivu-icon-ios-person" style="font-size: 16px;"></i>
                      </div>
                      <input
                        v-model="username"
                        autocomplete="off"
                        spellcheck="false"
                        type="text"
                        placeholder="Username"
                        class="ivu-input ivu-input-default"/>
                    </div>
                  </div>
                </div>
                <!-- 登录注册密码 -->
                <div class="ivu-form-item ivu-form-item-required">
                  <div class="ivu-form-item-content">
                    <div class="ivu-input-wrapper ivu-input-wrapper-default ivu-input-type ivu-input-group ivu-input-group-default ivu-input-group-with-prepend">
                      <div class="ivu-input-group-prepend">
                        <span><i class="ivu-icon ivu-icon-md-lock" style="font-size: 14px;"></i></span>
                      </div>
                      <input
                        v-model="password"
                        autocomplete="off"
                        spellcheck="false"
                        type="password"
                        placeholder="Password"
                        class="ivu-input ivu-input-default"/>
                    </div>
                  </div>
                </div>
                <!-- 注册确认密码 -->
                <div v-if="!isLogin" class="ivu-form-item-i ivu-form-item-required">
                  <div class="ivu-form-item-content">
                    <div class="ivu-input-wrapper ivu-input-wrapper-default ivu-input-type ivu-input-group ivu-input-group-default ivu-input-group-with-prepend">
                      <div class="ivu-input-group-prepend">
                        <span>
                          <Icon type="ios-lock" size="16" />
                        </span>
                      </div>
                      <input
                        v-model="isPassword"
                        autocomplete="off"
                        spellcheck="false"
                        type="password"
                        placeholder="Confirm password"
                        class="ivu-input ivu-input-default"
                        @blur="onBlur"/>
                    </div>
                  </div>
                </div>
                <!-- 用户名 -->
                <div v-if="!isLogin" class="ivu-form-item ivu-form-item-required">
                  <div class="ivu-form-item-content">
                    <div class="ivu-input-wrapper ivu-input-wrapper-default ivu-input-type ivu-input-group ivu-input-group-default ivu-input-group-with-prepend">
                      <div class="ivu-input-group-prepend">
                        <Icon type="ios-contact" size="16" />
                      </div>
                      <input
                        v-model="name"
                        autocomplete="off"
                        spellcheck="false"
                        type="text"
                        placeholder="Name"
                        class="ivu-input ivu-input-default"/>
                    </div>
                  </div>
                </div>
                <div v-if="isLogin" class="login-tip">
                  <p> No account number？<span @click.stop="onChange">register</span></p>
                </div>
                <div v-else class="login-tip">
                  <p> Have account number？ <span @click.stop="onChange">Sign in</span></p>
                </div>
                <div v-if="isLogin">
                  <div class="ivu-form-item-content">
                    <button
                      type="button"
                      class="ivu-btn ivu-btn-primary ivu-btn-long"
                      @click.stop="handleLogin">
                      SIGN IN</button>
                  </div>
                </div>
                <div v-else>
                  <div class="ivu-form-item-content">
                    <button
                      @click.stop="handleRegister"
                      type="button"
                      class="ivu-btn ivu-btn-primary ivu-btn-long">
                      REGISTER</button>
                  </div>
                </div>
                <div class="passport" v-if="showPassPort">
                  <Divider>其他方式登录</Divider>
                  <div class="icons">
                    <div class="icon-item" @click="handlePassPort">
                      <img src="./images/umtIcon.png" alt="科技云通行证登录" >
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="large-bgc"></div>
    <div style="height: 15%; width: 100%; position: fixed; bottom: 0px; background-color: #ffffff;">
      <br/>
      <p style="text-align: center;font-size: 12px;">Technical support ： Computer Network Information Center,Chinese Academy of Scienes</p>
      <p style="text-align: center;font-size: 12px;">Contact us ：010-58815678</p>
    </div>
    <!-- iframe -->
    <img src="./images/close.png" v-if="src" alt="" class="iframe-close"  @click="handleCloseIframe">
    <iframe :src="src"  v-if="src"  id="passportIfame"></iframe>
    <!-- iframe -->
</div>
</template>
<script>
import Cookies from 'js-cookie';
import FingerprintJS from '@fingerprintjs/fingerprintjs';
import {getStatus} from "@/apis/passport"
export default {
  name: "login",
  data() {
    return {
      username: "",
      password: "",
      name: "",
      mobile: "",
      org: "",
      isPassword: "",
      post: "",
      status:"",
      isLogin: true,
      isExist: false,
      title:window.$SYSTEM_TITLE_EN,
      src:'',
      deviceId:'',
      statusTimer:null,
      showPassPort:false
    };
  },
  watch: {
    isPassword(val) {
      this.$Message.destroy();
      if (this.password !== val) {

      } else {
        this.$Message.destroy();
      }
    }
  },
  created(){
    this.getRedirect()
  },
  mounted() {
    this.$Message.destroy();
    window.addEventListener('keydown',this.keyDown);
  },
  methods: {
    onChange() {
      this.isLogin = !this.isLogin;
      this.handleReset();
    },

    handleReset() {
      this.username = "";
      this.password = "";
      this.name = "";
      this.mobile = "";
      this.org = "";
      this.isPassword = "";
      this.post = "";
    },

    handleLogin(type) {
      this.$Message.destroy();
      if(type !== 'passport' && this.username.includes('科技云')){
        this.$Message["error"]({
          background: true,
          content: "第三方账号不允许直接登录！"
        });
        return
      }
      if (!this.username) {
        this.$Message["error"]({
          background: true,
          content: "请填写账号！"
        });
        return;
      }
      if (!this.password) {
        this.$Message["error"]({
          background: true,
          content: "请填写密码！"
        });
        return;
      }

      let data = {
        username: this.username,
        password: this.password
      };
      this.$axios
        .post("/jwtLogin", this.$qs.stringify(data))
        .then(res => {
          if (res.data.code === 200) {
            this.$store.commit("setToken", res.data.token);
            Cookies.set('token', res.data.token);
            this.$store.commit("setUser", res.data.jwtUser);
            Cookies.set('state', "jwtok");
            Cookies.set('usre', this.username);
            Cookies.set('setUser', JSON.stringify(res.data.jwtUser.roles));

            this.getIsInBootPage();
          } else {
            this.$Message["error"]({
              background: true,
              content: "登录失败！"
            });
          }
        })
        .catch(function(error) {
          console.log(error);
        });
    },

    handleRegister() {
      this.$Message.destroy();
      if (!this.username) {
        this.$Message["error"]({
          background: true,
          content: "请填写账号！"
        });
      } else if (this.isExist) {
        this.$Message["error"]({
          background: true,
          content: "账号已存在，请重新输入！"
        });
      } else if (!this.password) {
        this.$Message["error"]({
          background: true,
          content: "请填写密码！"
        });
      } else if (this.password !== this.isPassword) {
        this.$Message["error"]({
          background: true,
          content: "密码与确认密码不一致，请重新输入！"
        });
      } else if (!this.name) {
        this.$Message["error"]({
          background: true,
          content: "请填写用户名！"
        });
      } else if (!this.rEnumber(this.name)) {
        this.$Message["error"]({
          background: true,
          content: "用户名不得为数字，请正确填写用户名！"
        });
      }
      else {
        this.$axios
            .post("/checkUserName", this.$qs.stringify({'username':this.username}))
            .then(res => {
              if (res.data.code === 200) {
                this.registered();
              }else if(res.data.code === 500){
                this.$Modal.error({
                  title: this.$t("tip.title"),
                  content: res.data.errorMsg
                });
              } else {
                this.$Message.error({
                  content: res.data.errorMsg
                });
              }
            })
            .catch(function(error) {
              console.log(error);
            })
      }
    },

    registered(){
      this.$Message.loading({
        content: "注册中，请稍后...",
        duration: 0
      });
      let data = {
        username: this.username,
        pw: this.password,
        name: this.name,
        status:0
      };
      this.$axios
          .post("/register", this.$qs.stringify(data))
          .then(res => {
            if (res.data.code === 200) {
              this.$Message.success('registration success！');
              this.onChange();
            }else if(res.data.code === 500){
              if (res.data.errorMsg === 'save failed'){
                this.$Modal.error({
                  title: this.$t("tip.title"),
                  content: this.$t("tip.existed"),
                  onOk:()=>{
                    this.handleReset();
                  }
                });
              }
            } else {
              this.$Message.error({
                content: res.data.message
              });
            }
          })
          .catch(function(error) {
            console.log(error);
          });
    },
    handleCloseIframe(){
      this.showIframe = false
      this.src = ''
      this.$Message.destroy()
      this.$event.emit('loading',false)
    },

   async getRedirect(){
      const result = await FingerprintJS.load();
      const visitor = await result.get()
      this.deviceId = visitor.visitorId
      this.$axios
        .get('/passport/getRedirect?deviceId='+this.deviceId)
        .then((res) => {
          if (res.data.code === 200 && res.data?.client_id) {
            this.passInfo = res.data
            this.showPassPort = true
          }else{
            this.showPassPort = false
          }
        })
        .catch((error) => {
          this.showPassPort = false
        });

    },
    handlePassPort(){
      this.showIframe = true
      const {redirect_uri,client_id} = this.passInfo
      this.src = `https://passport.escience.cn/oauth2/authorize?response_type=code&redirect_uri=${redirect_uri}&client_id=${client_id}&theme=EMBED&state=${this.deviceId}`
      this.$event.emit('loading',true)
      this.$Message.loading({
        content: "第三方登陆中，请稍后...",
        duration: 0
      });
      this.getPassportStatus()
    },
    async getPassportStatus(){
      const res = await getStatus(this.deviceId)
      if(res.data.code === 200){
        if(res.data.loginThirdParty){
          this.username = res.data.userName
          this.password = res.data.password
          this.src = ''
          this.handleLogin('passport')
      }else if(this.showIframe){
         this.statusTimer =  setTimeout(() => {
            this.getPassportStatus()
          }, 500);
        }
      }
    },

    rEnumber(val) {
      let reg = new RegExp(/^\d+$/);
      return !reg.test(val);
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
                    this.$router.push({
                      name: 'sections',
                      path: '/'
                    });//否则跳转至首页
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

    onBlur(){
      this.$Message.destroy();
      if (this.password !== this.isPassword) {
        this.$Message.error({
          content: "密码与确认密码不一致，请重新输入！"
        });
      } else {
        this.$Message.destroy();
      }
    },

    keyDown(e){
      if(e.keyCode === 13 || e.keyCode === 100){
        this.isLogin?this.handleLogin():this.handleRegister();
      }
    }
  },
  destroyed() {
    window.removeEventListener('keydown',this.keyDown,false);
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>

