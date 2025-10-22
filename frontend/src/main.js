// main.js
import { createApp } from 'vue';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import router from './route';
import App from './App.vue';
import { loadConfig } from './utils/config';

// 引入所有图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue';

// 创建Vue应用实例
const app = createApp(App);

// 加载系统配置
loadConfig().then(config => {
  console.log('系统配置加载成功:', config);
  
  // 使用插件
  app.use(ElementPlus);
  app.use(router);
  
  // 全局注册图标组件
  for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component);
  }
  
  // 挂载应用
  app.mount('#app');
}).catch(error => {
  console.error('系统配置加载失败:', error);
  
  // 即使配置加载失败，也要启动应用
  app.use(ElementPlus);
  app.use(router);
  
  // 全局注册图标组件
  for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component);
  }
  
  // 挂载应用
  app.mount('#app');
});