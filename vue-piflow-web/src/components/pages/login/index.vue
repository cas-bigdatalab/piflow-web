<template>
  <!-- <div class="login" :style="`width:${width}px;height:${height}px;`"> -->
  <div class="login">
    <div class="header">
      <div class="logo"></div>
      <div class="title">
        <h1>PiFlow Big Data Pipeline System</h1>
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
                    <div
                      class="ivu-input-wrapper ivu-input-wrapper-default ivu-input-type ivu-input-group ivu-input-group-default ivu-input-group-with-prepend"
                    >
                      <div class="ivu-input-group-prepend">
                        <i class="ivu-icon ivu-icon-ios-person" style="font-size: 16px;"></i>
                      </div>
                      <input
                        v-model="username"
                        autocomplete="off"
                        spellcheck="false"
                        type="text"
                        placeholder="Username"
                        class="ivu-input ivu-input-default"
                      />
                    </div>
                  </div>
                </div>
                <!-- 登录注册密码 -->
                <div class="ivu-form-item ivu-form-item-required">
                  <div class="ivu-form-item-content">
                    <div
                      class="ivu-input-wrapper ivu-input-wrapper-default ivu-input-type ivu-input-group ivu-input-group-default ivu-input-group-with-prepend"
                    >
                      <div class="ivu-input-group-prepend">
                        <span>
                          <i class="ivu-icon ivu-icon-md-lock" style="font-size: 14px;"></i>
                        </span>
                      </div>
                      <input
                        v-model="password"
                        autocomplete="off"
                        spellcheck="false"
                        type="password"
                        placeholder="Password"
                        class="ivu-input ivu-input-default"
                      />
                    </div>
                  </div>
                </div>
                <!-- 注册确认密码 -->
                <div v-if="!isLogin" class="ivu-form-item-i ivu-form-item-required">
                  <div class="ivu-form-item-content">
                    <div
                      class="ivu-input-wrapper ivu-input-wrapper-default ivu-input-type ivu-input-group ivu-input-group-default ivu-input-group-with-prepend"
                    >
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
                        @blur="onBlur"
                      />
                    </div>
                  </div>
                </div>
                <!-- 用户名 -->
                <div v-if="!isLogin" class="ivu-form-item ivu-form-item-required">
                  <div class="ivu-form-item-content">
                    <div
                      class="ivu-input-wrapper ivu-input-wrapper-default ivu-input-type ivu-input-group ivu-input-group-default ivu-input-group-with-prepend"
                    >
                      <div class="ivu-input-group-prepend">
                        <Icon type="ios-contact" size="16" />
                      </div>
                      <input
                        v-model="name"
                        autocomplete="off"
                        spellcheck="false"
                        type="text"
                        placeholder="Name"
                        class="ivu-input ivu-input-default"
                      />
                    </div>
                  </div>
                </div>

                <!-- 手机号 -->
                <!-- <div v-if="!isLogin" class="ivu-form-item ivu-form-item-required">
                  <div class="ivu-form-item-content">
                    <div
                      class="ivu-input-wrapper ivu-input-wrapper-default ivu-input-type ivu-input-group ivu-input-group-default ivu-input-group-with-prepend"
                    >
                      <div class="ivu-input-group-prepend">
                        <Icon type="md-call" size="16" />
                      </div>

                      <input
                        v-model="mobile"
                        autocomplete="off"
                        spellcheck="false"
                        type="text"
                        placeholder="请输入手机号"
                        class="ivu-input ivu-input-default"
                      />
                    </div>
                  </div>
                </div>-->

                <!-- 单位 -->
                <!-- <div v-if="!isLogin" class="ivu-form-item ivu-form-item-required">
                  <div class="ivu-form-item-content">
                    <div
                      class="ivu-input-wrapper ivu-input-wrapper-default ivu-input-type ivu-input-group ivu-input-group-default ivu-input-group-with-prepend"
                    >
                      <div class="ivu-input-group-prepend">
                        <Icon type="md-home" size="16" />
                      </div>

                      <input
                        v-model="org"
                        autocomplete="off"
                        spellcheck="false"
                        type="text"
                        placeholder="请输入单位名称"
                        class="ivu-input ivu-input-default"
                      />
                    </div>
                  </div>
                </div>-->
                <!-- 职务 -->
                <!-- <div v-if="!isLogin" class="ivu-form-item ivu-form-item-required">
                  <div class="ivu-form-item-content">
                    <div
                      class="ivu-input-wrapper ivu-input-wrapper-default ivu-input-type ivu-input-group ivu-input-group-default ivu-input-group-with-prepend"
                    >
                      <div class="ivu-input-group-prepend">
                        <Icon type="ios-podium" size="16" />
                      </div>

                      <input
                        v-model="post"
                        autocomplete="off"
                        spellcheck="false"
                        type="text"
                        placeholder="请输入职务"
                        class="ivu-input ivu-input-default"
                      />
                    </div>
                  </div>
                </div>-->
                <div v-if="isLogin" class="login-tip">
                  <p>
                    No account number？
                    <span @click.stop="onChange">register</span>
                  </p>
                </div>
                <div v-else class="login-tip">
                  <p>
                    Have account number？
                    <span @click.stop="onChange">Sign in</span>
                  </p>
                </div>
                <div v-if="isLogin">
                  <div class="ivu-form-item-content">
                    <button
                      type="button"
                      class="ivu-btn ivu-btn-primary ivu-btn-long"
                      @click.stop="handleLogin"
                    >SIGN IN</button>
                  </div>
                </div>
                <div v-else>
                  <div class="ivu-form-item-content">
                    <button
                      @click.stop="handleRegister"
                      type="button"
                      class="ivu-btn ivu-btn-primary ivu-btn-long"
                    >REGISTER</button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- <div class="yjdl" @click.stop="handleLoginTx">使用科协一家登录？</div> -->
      </div>
    </div>
    <div class="large-bgc"></div>
    <div style="height: 15%; width: 100%; position: fixed; bottom: 0px; background-color: #ffffff;">
      <br />
      <p
        style="text-align: center;font-size: 12px;"
      >Technical support ： Computer Network Information Center,Chinese Academy of Scienes</p>
      <p style="text-align: center;font-size: 12px;">Contact us ：010-58815678</p>
    </div>
  </div>
</template>
<script>
// import API from "../../api/index";
// import axios from "axios";
import Cookies from 'js-cookie';
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
      isLogin: true,
      isExist: false
    };
  },
  watch: {
    // account(val) {
    //   if (!this.isLogin && val) {
    //     this.$Message.destroy();
    //     API.getIsLogin({ account: val }, res => {
    //       if (res.data.code != 200) {
    //         this.isExist = true;
    //         this.$Message["error"]({
    //           background: true,
    //           duration: 10,
    //           closable: true,
    //           content: "账号已存在，请重新输入！"
    //         });
    //       } else {
    //         this.isExist = false;
    //         this.$Message.destroy();
    //       }
    //     });
    //   }
    // },

    isPassword(val) {
      this.$Message.destroy();
      if (this.password !== val) {
        // this.$Message["error"]({
        //   background: true,
        //   duration: 10,
        //   closable: true,
        //   content: "密码与确认密码不一致，请重新输入！"
        // });
      } else {
        this.$Message.destroy();
      }
    }
  },
  created() {
    // this.width = this.$globle.client.width;
    // this.height = this.$globle.client.height;
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
    // 重置
    handleReset() {
      this.username = "";
      this.password = "";
      this.name = "";
      this.mobile = "";
      this.org = "";
      this.isPassword = "";
      this.post = "";
    },
    // 登录
    handleLogin() {
      this.$Message.destroy();
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
      // } else {
      //   this.$Message.loading({
      //     content: "登录中，请稍后...",
      //     duration: 0
      //   });

      let data = {
        username: this.username,
        password: this.password
      };
      this.$axios
        .post("/jwtLogin", this.$qs.stringify(data))
        .then(res => {
          if (res.data.code == 200) {
            // window.sessionStorage.setItem(
            //   "jwtUser",
            //   JSON.stringify(res.data.jwtUser)
            // );
            this.$store.commit("setToken", res.data.token);
            // window.sessionStorage.setItem('token',res.data.token);
            Cookies.set('token', res.data.token);
            this.$store.commit("setUser", res.data.jwtUser);
            // window.sessionStorage.setItem("state", "jwtok");
            Cookies.set('state', "jwtok");
            // window.sessionStorage.setItem("usre", this.username);
            Cookies.set('usre', this.username);

            this.getIsInBootPage();

            // window.sessionStorage.setItem("sysMenuVoList", JSON.stringify(res.data.jwtUser.sysMenuVoList));
            // this.handleClear();
            // let path = window.sessionStorage.getItem("path");
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

      // API.getLogin(data, res => {
      //   this.$Message.destroy();
      //   if (res.isOk === "error") {
      //     this.$Message["error"]({
      //       background: true,
      //       duration: 10,
      //       closable: true,
      //       content: "登录失败，请重新登录！"
      //     });
      //   } else if (res.data.code == 200) {
      //     this.$store.commit("setUser", res.data.data);
      //     // axios.defaults.headers.common["Cookieqw"] = "JSESSIONID=" + res.data.data.sessionId;
      //     // let largeHeader = document.getElementById("large-header");
      //     // largeHeader.removeChild(largeHeader);
      //     document.cookie = "STATE=1";
      //     this.$router.push({ path: "/" });
      //   } else {
      //     this.$Message["error"]({
      //       background: true,
      //       duration: 10,
      //       closable: true,
      //       content: res.data.message
      //     });
      //   }
      // });
      // }
    },
    // handleLoginTx() {
    //   window.location.href = API.KXLOGIN;
    // },
    // 注册
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
      // else if (!this.mobile) {
        // this.$Message["error"]({
        //   background: true,
        //   content: "请填写手机号！"
        // });
      // }
      // else if (!this.rEphone(this.mobile)) {
      //   this.$Message["error"]({
      //     background: true,
      //     content: "手机号错误，请正确填写手机号！"
      //   });
      // }
      // else if (!this.org) {
      //   this.$Message["error"]({
      //     background: true,
      //     content: "请填写单位名称！"
      //   });
      // } else if (!this.rEnumber(this.org)) {
      //   this.$Message["error"]({
      //     background: true,
      //     content: "请正确填写单位名称！"
      //   });
      // } else if (!this.post) {
      //   this.$Message["error"]({
      //     background: true,
      //     content: "请填写职务！"
      //   });
      // } else if (!this.rEnumber(this.post)) {
      //   this.$Message["error"]({
      //     background: true,
      //     content: "请正确填写职务！"
      //   });
      // }
      else {
        this.$axios
            .post("/checkUserName", this.$qs.stringify({'username':this.username}))
            .then(res => {
              if (res.data.code == 200) {
                this.registered();
              }else if(res.data.code == 500){
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
            });


        // API.getRegister(data, res => {
        //   this.$Message.destroy();
        //   if (res.data.code == 200) {
        //     this.$Message["info"]({
        //       background: true,
        //       duration: 10,
        //       closable: true,
        //       content: res.data.message
        //     });
        //     this.onChange();
        //   } else {
        //     this.$Message["error"]({
        //       background: true,
        //       duration: 10,
        //       closable: true,
        //       content: res.data.message
        //     });
        //   }
        // });
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
      };
      this.$axios
          .post("/register", this.$qs.stringify(data))
          .then(res => {
            if (res.data.code == 200) {
              this.$Message.success('registration success！');
              this.onChange();
            }else if(res.data.code == 500){
              if (res.data.errorMsg == 'save failed'){
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
    rEphone(val) {
      let reg = new RegExp(
        /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/
      );
      return reg.test(val);
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

