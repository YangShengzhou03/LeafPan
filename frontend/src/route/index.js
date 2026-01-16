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
import FavoritesPage from '@/views/user/FavoritesPage.vue';
import UserGuidePage from '@/views/index/UserGuidePage.vue';
import ContactUsPage from '@/views/index/ContactUsPage.vue';
import FAQPage from '@/views/index/FaqPage.vue';
import AuthorInfoPage from '@/views/index/AuthorInfoPage.vue';
import PrivacyPolicyPage from '@/views/index/PrivacyPolicyPage.vue';
import ShareAccessPage from '@/views/index/ShareAccessPage.vue';
import store from '@/utils/store.js';
import * as utils from '@/utils/utils.js';

const routes = [
  {
    path: '/',
    component: IndexLayout,
    meta: { title: '轻羽云盘', requiresAuth: false }
  },

  {
    path: '/login',
    name: 'LoginPage',
    component: LoginPage,
    meta: {
      title: '登录 - 轻羽云盘',
      requiresAuth: false
    }
  },

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
          title: '轻羽云盘 - 我的文件',
          requiresAuth: true,
          requiresAdmin: false
        }
      },
      {
        path: 'dashboard',
        name: 'DashboardPage',
        component: DashboardPage,
        meta: {
          title: '轻羽云盘 - 仪表盘',
          requiresAuth: true,
          requiresAdmin: false
        }
      },
      {
        path: 'shared',
        name: 'SharedPage',
        component: SharedPage,
        meta: {
          title: '轻羽云盘 - 共享文件',
          requiresAuth: true,
          requiresAdmin: false
        }
      },
      {
        path: 'favorites',
        name: 'FavoritesPage',
        component: FavoritesPage,
        meta: {
          title: '轻羽云盘 - 我的收藏',
          requiresAuth: true,
          requiresAdmin: false
        }
      },
      {
        path: 'trash',
        name: 'TrashPage',
        component: TrashPage,
        meta: {
          title: '轻羽云盘 - 回收站',
          requiresAuth: true,
          requiresAdmin: false
        }
      },
      {
        path: 'settings',
        name: 'SettingsPage',
        component: SettingsPage,
        meta: {
          title: '轻羽云盘 - 设置',
          requiresAuth: true,
          requiresAdmin: false
        }
      },
    ]
  },

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
          title: '轻羽云盘 - 管理员仪表盘',
          requiresAuth: true,
          requiresAdmin: true
        }
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('@/views/admin/UsersPage.vue'),
        meta: {
          title: '轻羽云盘 - 用户管理',
          requiresAuth: true,
          requiresAdmin: true
        }
      },
      {
        path: 'system',
        name: 'AdminSystem',
        component: () => import('@/views/admin/SystemPage.vue'),
        meta: {
          title: '轻羽云盘 - 系统设置',
          requiresAuth: true,
          requiresAdmin: true
        }
      },
      {
        path: 'logs',
        name: 'AdminLogs',
        component: () => import('@/views/admin/LogsPage.vue'),
        meta: {
          title: '轻羽云盘 - 操作日志',
          requiresAuth: true,
          requiresAdmin: true
        }
      },
    ]
  },

  {
    path: '/user-guide',
    name: 'UserGuidePage',
    component: UserGuidePage,
    meta: {
      title: '使用指南 - 轻羽云盘',
      requiresAuth: false
    }
  },
  {
    path: '/contact-us',
    name: 'ContactUsPage',
    component: ContactUsPage,
    meta: {
      title: '联系我们 - 轻羽云盘',
      requiresAuth: false
    }
  },
  {
    path: '/faq',
    name: 'FAQPage',
    component: FAQPage,
    meta: {
      title: '常见问题 - 轻羽云盘',
      requiresAuth: false
    }
  },

  { path: '/author-info', name: 'AuthorInfoPage', component: AuthorInfoPage, meta: { title: '作者介绍 - 轻羽云盘', requiresAuth: false } },
  { path: '/privacy-policy', name: 'PrivacyPolicyPage', component: PrivacyPolicyPage, meta: { title: '隐私保护 - 轻羽云盘', requiresAuth: false } },
  
  { 
    path: '/s/:shareCode', 
    name: 'ShareAccessPage', 
    component: ShareAccessPage, 
    meta: { 
      title: '文件分享 - 轻羽云盘', 
      requiresAuth: false 
    } 
  },

  { path: '/:pathMatch(.*)*', redirect: '/' }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach(async (to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title;
  }

  if (to.meta.requiresAuth) {
    if (!utils.isLoggedIn()) {
      next('/login');
      return;
    }

    if (!store.state.user) {
      try {
        await store.fetchCurrentUser();
        await store.fetchStorageInfo();
      } catch (error) {
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
          utils.removeToken();
        }
        next('/login');
        return;
      }
    }

    if (to.meta.requiresAdmin && !store.state.isAdmin) {
      next('/user');
      return;
    }
  }

  if (to.path === '/login' && utils.isLoggedIn()) {
    if (store.state.isAdmin) {
      next('/admin');
    } else {
      next('/user');
    }
    return;
  }

  next();
});

export default router;
