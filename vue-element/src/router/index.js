import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/* Router Modules */
import componentsRouter from './modules/components'
import chartsRouter from './modules/charts'
import tableRouter from './modules/table'
import nestedRouter from './modules/nested'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'Dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: '欢迎页面', icon: 'dashboard', affix: true }
      }
    ]
  },
  // 前端页面的路由配置
  {
    path: '/indexs',
    component: () => import('@/views/zhuye/index'),
    hidden: true
  },
  {
    path: '/teamlist',
    component: () => import('@/views/pages/teamlist/index'),
    hidden: true
  },

  {
    path: '/teaminfo',
    component: () => import('@/views/pages/pageinfo/index'),
    hidden: true
  },
  {
    path: '/myteam',
    component: () => import('@/views/pages/myteam/index'),
    hidden: true
  },

  {
    path: '/leave',
    component: () => import('@/views/pages/leave/index'),
    hidden: true
  },
  {
    path: '/profile',
    component: Layout,
    redirect: '/profile/index',
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/profile/index'),
        name: 'Profile',
        meta: { title: 'Profile', icon: 'user', noCache: true }
      }
    ]
  }
]
/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [
  {
    path: '/users',
    component: Layout,
    redirect: '/users/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/users/index'),
        name: 'Guide',
        meta: {
          title: '用户信息管理',
          icon: 'peoples',
          noCache: true,
          roles: ['Admin', 'manager']
        }
      }
    ]
  },
  {
    path: '/type',
    component: Layout,
    redirect: '/type/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/type/index'),
        name: 'Guide',
        meta: {
          title: '社团类型管理',
          icon: 'documentation',
          noCache: true,
          roles: ['Admin'] }
      }
    ]
  },
  {
    path: '/team',
    component: Layout,
    redirect: '/team/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/team/index'),
        name: 'Guide',
        meta: {
          title: '社团信息管理',
          icon: 'list',
          noCache: true,
          roles: ['Admin']
        }
      }
    ]
  },
  {
    path: '/reqlist',
    component: Layout,
    redirect: '/reqlist/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/reqlist/index'),
        name: 'Guide',
        meta: {
          title: '入团申请管理',
          icon: 'nested',
          noCache: true,
          roles: ['Admin', 'manager']
        }
      }
    ]
  },
  {
    path: '/activityinfo',
    component: Layout,
    redirect: '/activityinfo/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/activityinfo/index'),
        name: 'Guide',
        meta: {
          title: '社团活动管理',
          icon: 'clipboard',
          noCache: true,
          roles: ['Admin', 'manager']
        }
      }
    ]
  },
  {
    path: '/teamuser',
    component: Layout,
    redirect: '/teamuser/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/teamuser/index'),
        name: 'Guide',
        meta: {
          title: '社团成员管理',
          icon: 'people',
          noCache: true,
          roles: ['manager']
        }
      }
    ]
  },
  {
    path: '/costlist',
    component: Layout,
    redirect: '/costlist/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/costlist/index'),
        name: 'Guide',
        meta: {
          title: '社团费用管理',
          icon: 'peoples',
          noCache: true,
          roles: ['manager']
        }
      }
    ]
  },
  {
    path: '/leaveinfo',
    component: Layout,
    redirect: '/leaveinfo/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/leaveinfo/index'),
        name: 'Guide',
        meta: {
          title: '留言信息管理',
          icon: 'email',
          noCache: true,
          roles: ['Admin']
        }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
