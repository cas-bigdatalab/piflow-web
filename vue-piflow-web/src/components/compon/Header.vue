<template>
  <header>
    <div class="left" @click="handleClick">
      <!-- <div :style="{width:logoWidth}">
        <img src="../../assets/img/logo.png" alt="logo" />
        <h1>前端模板</h1>
      </div>-->
      <div class="warp">
        <!-- <Icon @click="collapsedSider" :class="rotateIcon" type="md-menu"></Icon> -->
        <img class="logo" src="../../assets/img/logo.png" alt="logo" />
        <span>{{$t("title")}}</span>
      </div>
    </div>
    <ul class="right">
      <!-- <li>
        <Tooltip content="全屏放大" placement="bottom">
          <Icon type="ios-move" size="26"></Icon>
        </Tooltip>
      </li>
      <li>
        <Badge dot>
          <Tooltip content="消息" placement="bottom">
            <Icon type="ios-notifications-outline" size="26"></Icon>
          </Tooltip>
        </Badge>
      </li>
      <li>
        <Tooltip content="锁屏" placement="bottom">
          <Icon type="ios-unlock-outline" size="26"></Icon>
        </Tooltip>
      </li>-->
      <li>
        <Dropdown trigger="click" style="margin-left: 20px">
          <div class="author">
            <img src="../../assets/img/logo.png" alt="logo" />
            <span>{{user.username?user.username:'游客'}}</span>
            <Icon type="md-arrow-dropdown" size="22" />
          </div>
          <DropdownMenu slot="list">
<!--            <DropdownItem>个人中心</DropdownItem>-->
<!--            <DropdownItem>修改密码</DropdownItem>-->
            <DropdownItem divided @click.native="handleQuit">{{$t("logOut")}}</DropdownItem>
          </DropdownMenu>
        </Dropdown>
      </li>
      <li>
        <Dropdown trigger="click">
          <Icon type="md-globe" size="22" />
          <DropdownMenu slot="list">
            <DropdownItem @click.native="handleLanguageSwitch('zh')">简体中文</DropdownItem>
            <DropdownItem @click.native="handleLanguageSwitch('en')">English</DropdownItem>
          </DropdownMenu>
        </Dropdown>
      </li>
    </ul>
  </header>
</template>

<script>
    import Cookies from 'js-cookie';
export default {
  data() {
    return {
      user: {},
      // isCollapsed: false,
      // logoWidth: this.width
    };
  },
  // computed: {
  //   rotateIcon() {
  //     return ["menu-icon", this.isCollapsed ? "rotate-icon" : ""];
  //   }
  // },
  // props: ["width"],
  mounted() {
    // this.user = this.$store.state.variable.user;
    // this.user = { username: window.sessionStorage.usre };
    this.user = { username: Cookies.get('usre') };
      // console.log(window.sessionStorage.getItem("user"));
    // console.log(this.user);
  },
  methods: {
    handleLanguageSwitch(val) {
      this.$i18n.locale = val; //关键语句
      // 使用 localStorage 存储语言状态
      // window.sessionStorage.setItem("lang", val);
      Cookies.set('lang', val);
      // 刷新页面更新视图
      // window.location.reload();
    },
    // collapsedSider() {
    //   this.isCollapsed = !this.isCollapsed;
    //   this.logoWidth = this.isCollapsed ? "64px" : this.width;
    //   this.$event.emit("isCollapsed", this.isCollapsed);
    // }
    handleQuit() {
      // window.sessionStorage.removeItem("state");
      window.sessionStorage.removeItem("menuName");
      // window.sessionStorage.removeItem("user");
      Cookies.remove('state');
      // Cookies.remove('basePath');
      Cookies.remove('token');
      // Cookies.remove('menuName');
      Cookies.remove('user');
      this.$store.commit("setToken", "");
      this.$store.commit("setUser", {});
      this.$router.push({ path: "/login" });
    },
    handleClick(){
        this.$router.push({
            path: '/',
            name: 'sections',
        });
    }
  },
};
</script>

<style lang="scss" scoped>
header {
  display: flex;
  justify-content: space-between;
  // color: #fff;
  // box-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
  background: #37714b;
  color: #fff;
  padding: 6px 20px;
  border-bottom: 4px solid gainsboro;
  box-shadow: 0 1px 0 0 rgba(0, 0, 0, 0.1);
  .left {
    cursor: pointer;
    display: flex;
    .logo {
      width: 30px;
      height: 30px;
      margin-right: 8px;
    }
    .warp {
      display: flex;
      align-items: center;
      // .ivu-breadcrumb{
      //   color: #fff;
      // }
    }
  }
  .right {
    display: flex;
    align-items: center;
    margin-right: 20px;
    > li {
      margin: 0 20px;
      text-align: center;
      cursor: pointer;
    }
    .author {
      display: flex;
      align-items: center;
      span {
        margin-left: 8px;
      }
      img {
        width: 20px;
        height: 20px;
      }
    }
  }
}
/deep/ .ivu-icon-md-menu {
  transition: all 0.3s;
  cursor: pointer;
  font-size: 24px;
  padding: 5px 20px;
}
/deep/ .rotate-icon {
  transform: rotate(-90deg);
}
</style>
