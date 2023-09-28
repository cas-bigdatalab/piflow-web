import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router)

export default new Router({
  // mode: 'history',
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
      redirect: '/',
      children: [
        {
          path: '/',
          name: 'content',
          component: () => import('../components/Content.vue'),
          redirect: '/',
          children: [
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
              name: 'templates',
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
            //{
             // path: '/code',
           //   name: 'Code',
            //  component: () => import('../components/pages/Code')
           // },
            {
              path: '/publish',
              name: 'publish',
              component: () => import('../components/pages/publish')
            },
            {
              path: '/stopProperties',
              name: 'stopProperties',
              component: () => import('../components/pages/publish/properties')
            },
            //{
            //  path: '/codeDetailed',
            //  name: 'codeBlock',
            //  component: () => import('../components/pages/Code/codeBlock')
           // },
            {
              path: '/admin',
              name: 'admin',
              component: () => import('../components/pages/Admin')
            },
            {
              path: '/stopsComponent',
              name: 'stopsComponent',
              component: () => import('../components/pages/StopsComponent')
            },
            {
              path: '/globalVariable',
              name: 'globalVariable',
              component: () => import('../components/pages/Admin/globalVariable')
            },

            {
              path: '/user',
              name: 'user',
              component: () => import('../components/pages/User')
            },
            {
              path: '/user',
              name: 'user',
              component: () => import('../components/pages/User/index')
            },
            {
              path:'/log',
              name:'log',
              component:() => import('../components/pages/User/log')
            },
            {
              path:'/modification',
              name:'modification',
              component:() => import('../components/pages/User/modification')
            },
            {
              path:'/bindingAccount',
              name:'bindingAccount',
              component:() => import('../components/pages/User/bindingAccount')
            }
          ]
        },
        {
          path: '/drawingBoard',
          name: 'drawingboard',
          component: () => import('../components/pages/DrawingBoard')
        }
      ]
    }
  ]
})
