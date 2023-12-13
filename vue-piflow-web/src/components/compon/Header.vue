<template>
  <header>
    <div class="left" @click="handleClick">
      <div class="warp">
        <img class="logo" src="/img/logo.png" alt="logo" />
        <span>{{$t("title")}}</span>
      </div>
    </div>
    <ul class="right">
      <li>
        <Dropdown trigger="click" style="margin-left: 20px">
          <div class="author">
            <img src="/img/logo.png" alt="logo" />
            <span>{{user.username?user.username:'游客'}}</span>
            <Icon type="md-arrow-dropdown" size="22" />
          </div>
          <DropdownMenu slot="list">
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
      user: {}
    };
  },
  mounted() {
    this.user = { username: Cookies.get('usre') };
  },
  methods: {
    handleLanguageSwitch(val) {
      this.$i18n.locale = val;
      Cookies.set('lang', val);
    },
    handleQuit() {
      window.sessionStorage.removeItem("menuName");
      Cookies.remove('state');
      Cookies.remove('token');
      Cookies.remove('user');
      Cookies.remove('setUser');
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
  background: var(--primary-color);
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
::v-deep .ivu-icon-md-menu {
  transition: all 0.3s;
  cursor: pointer;
  font-size: 24px;
  padding: 5px 20px;
}
::v-deep .rotate-icon {
  transform: rotate(-90deg);
}
</style>
