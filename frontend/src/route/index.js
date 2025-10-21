import { createRouter, createWebHistory } from 'vue-router';
import IndexLayout from '@/components/IndexLayout.vue';
import LoginPage from '@/views/LoginPage.vue';

const routes = [
  // 未登录布局（首页 + 登录/注册）
  {
    path: '/',
    component: IndexLayout,
  },
  
  // 登录页面
  { path: '/login', name: 'LoginPage', component: LoginPage, meta: { title: '登录 - 枫叶网盘' } },

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