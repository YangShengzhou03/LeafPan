import { createRouter, createWebHistory } from 'vue-router';
import IndexLayout from '@/components/IndexLayout.vue';
import LoginPage from '@/views/LoginPage.vue';
import UserLayout from '@/components/UserLayout.vue';
import FilesPage from '@/views/user/FilesPage.vue';
import DashboardPage from '@/views/user/DashboardPage.vue';
import SharedPage from '@/views/user/SharedPage.vue';
import TrashPage from '@/views/user/TrashPage.vue';
import SettingsPage from '@/views/user/SettingsPage.vue';

const routes = [
  // 未登录布局（首页 + 登录/注册）
  {
    path: '/',
    component: IndexLayout,
    meta: { requiresAuth: false }
  },

  // 登录页面
  {
    path: '/login',
    name: 'LoginPage',
    component: LoginPage,
    meta: {
      title: '登录 - 枫叶网盘',
      requiresAuth: false
    }
  },

  // 用户布局（首页 + 其他用户页面）
  {
    path: '/user',
    component: UserLayout,
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'FilesPage',
        component: FilesPage,
        meta: {
          title: '枫叶网盘 - 我的文件',
          requiresAuth: true
        }
      },
      {
        path: 'dashboard',
        name: 'DashboardPage',
        component: DashboardPage,
        meta: {
          title: '枫叶网盘 - 仪表盘',
          requiresAuth: true
        }
      },
      {
        path: 'shared',
        name: 'SharedPage',
        component: SharedPage,
        meta: {
          title: '枫叶网盘 - 共享文件',
          requiresAuth: true
        }
      },
      {
        path: 'trash',
        name: 'TrashPage',
        component: TrashPage,
        meta: {
          title: '枫叶网盘 - 回收站',
          requiresAuth: true
        }
      },
      {
        path: 'settings',
        name: 'SettingsPage',
        component: SettingsPage,
        meta: {
          title: '枫叶网盘 - 设置',
          requiresAuth: true
        }
      },
    ]
  },

  // 错误路径重定向（仅保留这一条）
  { path: '/:pathMatch(.*)*', redirect: '/' }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// // 路由守卫
// router.beforeEach((to, from, next) => {
//   // 设置页面标题
//   document.title = to.meta.title || 'Leaf·Pan';

//   // 检查是否需要登录权限
//   const store = require('@/utils/store.js').default;
//   const isAuthenticated = store.state.isAuthenticated;

//   if (to.meta.requiresAuth && !isAuthenticated) {
//     // 需要登录但未登录，重定向到登录页
//     next('/login');
//   } else if ((to.path === '/login' || to.path === '/') && isAuthenticated) {
//     // 已登录用户访问登录页或首页，重定向到用户首页
//     next('/user');
//   } else {
//     // 其他情况正常导航
//     next();
//   }
// });

export default router;