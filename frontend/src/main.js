import { createApp } from 'vue';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import router from './route';
import App from './App.vue';
import { loadConfig } from './utils/config';
import store from './utils/store.js';

import * as ElementPlusIconsVue from '@element-plus/icons-vue';

const app = createApp(App);

app.use(ElementPlus);
app.use(router);

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

app.config.globalProperties.$store = store;

store.init().catch(() => {});
app.mount('#app');

loadConfig().catch(() => {});
