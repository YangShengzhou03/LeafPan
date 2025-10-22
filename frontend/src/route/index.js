import { createRouter, createWebHistory } from 'vue-router';
import IndexLayout from '@/components/IndexLayout.vue';
import LoginPage from '@/views/LoginPage.vue';
import UserLayout from '@/components/UserLayout.vue';
import AdminLayout from '@/components/AdminLayout.vue';
import FilesPage from '@/views/user/FilesPage.vue';
import DashboardPage from '@/views/user/DashboardPage.vue';
import SharedPage from '@/views/user/SharedPage.vue';
import TrashPage from '@/views/user/TrashPage.vue';
import SettingsPage from '@/views/user/SettingsPage.vue';
import store from '@/utils/store.js';

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
    meta: { requiresAuth: true, requiresAdmin: false },
    children: [
      {
        path: '',
        name: 'FilesPage',
        component: FilesPage,
        meta: {
          title: '枫叶网盘 - 我的文件',
          requiresAuth: true,
          requiresAdmin: false
        }
      },
      {
        path: 'dashboard',
        name: 'DashboardPage',
        component: DashboardPage,
        meta: {
          title: '枫叶网盘 - 仪表盘',
          requiresAuth: true,
          requiresAdmin: false
        }
      },
      {
        path: 'shared',
        name: 'SharedPage',
        component: SharedPage,
        meta: {
          title: '枫叶网盘 - 共享文件',
          requiresAuth: true,
          requiresAdmin: false
        }
      },
      {
        path: 'trash',
        name: 'TrashPage',
        component: TrashPage,
        meta: {
          title: '枫叶网盘 - 回收站',
          requiresAuth: true,
          requiresAdmin: false
        }
      },
      {
        path: 'settings',
        name: 'SettingsPage',
        component: SettingsPage,
        meta: {
          title: '枫叶网盘 - 设置',
          requiresAuth: true,
          requiresAdmin: false
        }
      },
    ]
  },

  // 管理员布局（管理员专属页面）
  {
    path: '/admin',
    component: AdminLayout,
    meta: { requiresAuth: true, requiresAdmin: true },
    children: [
      {
        path: '',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/DashboardPage.vue'),
        meta: {
          title: '枫叶网盘 - 管理员仪表盘',
          requiresAuth: true,
          requiresAdmin: true
        }
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('@/views/admin/UsersPage.vue'),
        meta: {
          title: '枫叶网盘 - 用户管理',
          requiresAuth: true,
          requiresAdmin: true
        }
      },
      {
        path: 'system',
        name: 'AdminSystem',
        component: () => import('@/views/admin/SystemPage.vue'),
        meta: {
          title: '枫叶网盘 - 系统设置',
          requiresAuth: true,
          requiresAdmin: true
        }
      },
      {
        path: 'logs',
        name: 'AdminLogs',
        component: () => import('@/views/admin/LogsPage.vue'),
        meta: {
          title: '枫叶网盘 - 操作日志',
          requiresAuth: true,
          requiresAdmin: true
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

export default router;