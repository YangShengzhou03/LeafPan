import { createRouter, createWebHistory } from 'vue-router';
import IndexLayout from '@/components/IndexLayout.vue';
import LoginPage from '@/views/LoginPage.vue';
import UserLayout from '@/components/UserLayout.vue';
import HomePage from '@/views/user/HomePage.vue';
import FilesPage from '@/views/user/FilesPage.vue';

const routes = [
  // 未登录布局（首页 + 登录/注册）
  {
    path: '/',
    component: IndexLayout,
  },
  
  // 登录页面
  { path: '/login', name: 'LoginPage', component: LoginPage, meta: { title: '登录 - 枫叶网盘' } },

  // 用户布局（首页 + 其他用户页面）
  {
    path: '/user',
    component: UserLayout,
    children: [
      { path: '', name: 'HomePage', component: HomePage, meta: { title: '枫叶网盘 - 首页' } },
      { path: 'files', name: 'FilesPage', component: FilesPage, meta: { title: '枫叶网盘 - 我的文件' } },
    ]
  },

  // 错误路径重定向（仅保留这一条）
  { path: '/:pathMatch(.*)*', redirect: '/' }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 路由守卫
router.beforeEach((to) => {
  // 设置页面标题
  document.title = to.meta.title || 'Lucky SMS';
});

export default router;