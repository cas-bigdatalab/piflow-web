import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router)

export default new Router({
  // mode: 'history',
  // mode: 'hash', //默认 有#号
  routes: [

    {
      path: '/login',
      name: 'login',
      component: () => import('../components/pages/login')
    },
    {
      path: '/bootPage',
      name: 'bootPage',
      component: () => import('../components/pages/bootPage')
    },

    {
      path: '/',
      name: 'main',
      component: () => import('../components/Main.vue'),
      redirect: '/',//设置默认指向的路径
      children: [ //这里就是二级路由的配置
        {
          path: '/',
          name: 'content',
          component: () => import('../components/Content.vue'),
          redirect: '/',//设置默认指向的路径
          children: [ //这里就是二级路由的配置
            {
              path: '/',
              name: 'sections',
              component: () => import('../components/pages/index')
            },
            {
              path: '/flow',
              name: 'flow',
              component: () => import('../components/pages/Flow')
            },
            {
              path: '/group',
              name: 'group',
              component: () => import('../components/pages/Group')
            },
            {
              path: '/processes',
              name: 'processes',
              component: () => import('../components/pages/Processes')
            },
            {
              path: '/template',
              name: 'template',
              component: () => import('../components/pages/Template')
            },
            {
              path: '/datasource',
              name: 'datasource',
              component: () => import('../components/pages/DataSource')
            },
            {
              path: '/schedule',
              name: 'schedule',
              component: () => import('../components/pages/Schedule')
            },
            {
              path: '/stopHub',
              name: 'stopHub',
              component: () => import('../components/pages/StopHub')
            },
            {
              path: '/sparkJar',
              name: 'SparkJar',
              component: () => import('../components/pages/SparkJar')
            },
            {
              path: '/testData',
              name: 'TestData',
              component: () => import('../components/pages/TestData')
            },
            {
              path: '/admin',
              name: 'admin',
              component: () => import('../components/pages/Admin')
            },
            {
              path: '/stopsComponent',
              name: 'stopsComponent',
              component: () => import('../components/pages/StopsComponent')
            }
          ]
        },
        {
          path: '/drawingBoard',
          name: 'drawingboard',
          component: () => import('../components/pages/DrawingBoard')
        }
      ]
    },





    // {
    //   path: '/',
    //   name: 'main',
    //   component: () => import('../components/Main.vue'),
    //   redirect: '/',//设置默认指向的路径
    //   children: [ //这里就是二级路由的配置
    //     {
    //       path: '/',
    //       name: 'flow',
    //       component: () => import('../components/pages/Flow')
    //     },
    //     {
    //       path: '/group',
    //       name: 'group',
    //       component: () => import('../components/pages/Group')
    //     },
    //     {
    //       path: '/processes',
    //       name: 'processes',
    //       component: () => import('../components/pages/Processes')
    //     },
    //     {
    //       path: '/template',
    //       name: 'template',
    //       component: () => import('../components/pages/Template')
    //     },
    //     {
    //       path: '/datasource',
    //       name: 'datasource',
    //       component: () => import('../components/pages/DataSource')
    //     },
    //     {
    //       path: '/admin',
    //       name: 'admin',
    //       component: () => import('../components/pages/Admin')
    //     },
    //   ]
    // },
  ]
})
