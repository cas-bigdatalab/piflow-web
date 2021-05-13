
<template>
  <Menu
    :active-name="menuName"
    theme="light"
    class="warp-sider"
    accordion
    :width="width"
    v-if="!isCollapsed"
    @on-select="handleMenuSelect"
  >
    <div v-for="(item,i) in menulist" :key="i+'q1'">
      <MenuItem v-if="!item.children" :name="item.name" class="item-par" :to="item.router">
        <Icon :type="item.icoName" size="18" />
        <span>{{item.btnName}}</span>
      </MenuItem>
      <Submenu v-else :name="i+'q1'">
        <template slot="title">
          <Icon :type="item.icoName" size="18" />
          <span>{{item.btnName}}</span>
        </template>
        <MenuItem
          v-for="(item,key) in item.children "
          :key="key+'w1'"
          :to="item.router"
          :name="item.name"
        >{{item.btnName}}</MenuItem>
      </Submenu>
    </div>
  </Menu>

  <div class="side-nav" v-else>
    <div class="side-list" v-for="(item,i) in menulist" :key="i+'q'">
      <Dropdown>
        <div>
          <Icon :type="item.icoName" size="18" />
        </div>
        <DropdownMenu slot="list" v-if="item.children">
          <DropdownItem v-for="(item,i) in item.children " :key="i+'w'">{{item.btnName}}</DropdownItem>
        </DropdownMenu>
      </Dropdown>
    </div>
  </div>
</template>
<script>
import { log } from 'util';
export default {
  data() {
    return {
      isCollapsed: false,
      menuName: "home",
      mouseOver: ""
      // menulist: []
    };
  },
  props: ["width"],
  computed: {
    // menuitemClasses() {
    //   return [this.isCollapsed ? "collapsed-menu" : ""];
    // },
    menulist() {
      return [
        {
          btnName: this.$t("sidebar.dashboard"),
          icoName: "ios-home",
          router: "/",
          name: 'home'
        },
        {
          btnName: this.$t("sidebar.flow"),
          icoName: "ios-ionitron",
          router: "/flow",
          name: "flow"
        },
        {
          btnName: this.$t("sidebar.group"),
          icoName: "ios-apps",
          router: "/group",
          name: "group"
        },
        {
          btnName: this.$t("sidebar.processes"),
          icoName: "md-analytics",
          router: "/processes",
          name: "processes"
        },
        {
          btnName: this.$t("sidebar.template"),
          icoName: "md-list-box",
          router: "/template",
          name: "template"
        },
        {
          btnName: this.$t("sidebar.data_source"),
          icoName: "ios-color-filter",
          router: "datasource",
          name: "datasource"
        },
        {
          btnName: this.$t("sidebar.schedule"),
          icoName: "ios-timer",
          router: "schedule",
          name: "schedule"
        },
        {
          btnName: this.$t("sidebar.stopHub"),
          icoName: "ios-speedometer",
          router: "StopHub",
          name: "StopHub"
        },
        {
          btnName: this.$t("sidebar.sparkJar"),
          icoName: "md-cog",
          router: "SparkJar",
          name: "SparkJar"
        },
        {
          btnName: this.$t("sidebar.testData"),
          icoName: "md-list",
          router: "TestData",
          name: "TestData"
        },
        {
          btnName: this.$t("sidebar.example"),
          icoName: "md-cube",
          children: [
            {
              icoName: "ios-paper",
              btnName: "FlowExample",
              name: "FlowExample",
              router: {
                path: "/drawingBoard",
                query: {
                  src: `/drawingBoard/page/flow/mxGraph/index.html?load=0641076d5ae840c09d2be5b71fw00001`,
                },
              },
            },
            {
              icoName: "ios-paper",
              btnName: "GroupExample",
              name: "GroupExample",
              router: {
                path: "/drawingBoard",
                query: {
                  src:`/drawingBoard/page/flowGroup/mxGraph/index.html?drawingBoardType=GROUP&load=ff808181725050fe017250group10002`
                }
              },
            }
          ]
        },
        {
          btnName: this.$t("sidebar.admin"),
          icoName: "ios-people",
          children: [
            {
              router: "/admin",
              icoName: "ios-paper",
              btnName:  this.$t("sidebar.admin_schedule"),
              name: "admin",
            },{
              router: "/stopsComponent",
              icoName: "ios-paper",
              btnName: this.$t("sidebar.stopsComponent"),
              name: "stopsComponent",
            }
          ]
        }
      ];
    }
  },
  created() {
    this.$event.on("isCollapsed", e => {
      this.isCollapsed = e;
    });
  },
  mounted() {
    let menuName = window.sessionStorage.getItem("menuName");

    if (menuName && this.menuName !== menuName) {
      this.menuName = menuName;
    }
    if (!menuName){
      window.sessionStorage.setItem("menuName", this.menuName);
      this.menuName = window.sessionStorage.getItem("menuName");

    }
    // this.$refs.sideNav;
    // window.sessionStorage.removeItem("menuName");


    // let list = JSON.parse(window.sessionStorage.getItem("sysMenuVoList"));

  },
  watch: {
    $route() {
      //路由变化时就重新执行这个方法 更新传来的参数
      this.getRouter();
    }
  },
  methods: {
    // handleMouseOver(name) {
    //   this.mouseOver = name;
    // },
    handleMenuSelect(name) {
      if (this.menuName !== name) {
        window.sessionStorage.setItem("menuName", name);
      }
    },
    //监听路由动态
    getRouter() {
      let menuName = window.sessionStorage.getItem("menuName");
      // console.log(this.$route.path);//为当前所在页面路由
      let url = this.$route.path;
      let arr = url.split("/");

      if (url === '/'){
        this.menuName = 'home';
      }else {
        this.menuName = arr[1];
      }
      if (menuName && this.menuName !== menuName){
        window.sessionStorage.setItem("menuName", this.menuName);
      }
    }
  }
};
</script>
<style lang="scss" scoped>
.warp-sider {
  padding-top: 15px;
  // box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
  // border-right: 1px solid #ddd;
  background: transparent;
  overflow: auto;
  > div {
    margin: 0 20px;
  }
  .item-par,
  /deep/ .ivu-menu-submenu-title {
    margin-top: 1px;
    color: #333333;
    font-size: 12px;
    font-weight: bold;
    display: block;
    padding: 14px 24px;
    border: 1px solid #d5d5d5;
    font-family: "Open Sans", sans-serif;
    font-weight: 600;
    background: #f5f5f5;
    background: linear-gradient(to bottom, #ffffff 0%, #f5f5f5 100%);
    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#ffffff', endColorstr='#f5f5f5',GradientType=0 );
  }

  .item-par:hover,
  /deep/ .ivu-menu-submenu-title:hover {
    filter: progid:DXImageTransform.Microsoft.gradient(enabled = false);
    background: #fcfcfc;
    border-color: #ccc;
    color: #497b95;
  }

  /deep/ .ivu-menu-item-selected {
    color: #fff !important;
    vertical-align: middle;
    background: #3da375 !important;
    border-color: #47869e;
    box-shadow: 0 1px 1px rgba(255, 255, 255, 0.3) inset;
  }
}

/deep/ .ivu-menu {
  .ivu-menu-item {
    border: 1px solid #ddd;
    border-top: 0;
    font-size: 12px;
  }
  .ivu-menu-item:hover {
    color: #497b95;
  }

}
.side-nav {
  color: #fff;
}
</style>