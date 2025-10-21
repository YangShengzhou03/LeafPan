import { createRouter, createWebHistory } from 'vue-router';
import IndexLayout from '@/components/IndexLayout.vue';
import IndexPage from '@/views/IndexPage.vue';

const routes = [
  // 未登录布局（首页 + 登录/注册）
  {
    path: '/',
    component: IndexLayout,
    children: [
      { path: '', name: 'IndexPage', component: IndexPage, meta: { title: '枫叶网盘' } },
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