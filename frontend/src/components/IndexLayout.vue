<template>
  <div class="index-layout">
    <header class="header">
      <div class="container">
        <div class="logo-area">
          <h1>轻羽云盘</h1>
        </div>
        
        <div class="auth-buttons" v-if="!isAuthenticated">
          <el-button class="register-btn" @click="handleRegister">注册</el-button>
          <el-button type="primary" class="login-btn" @click="handleLogin">登录</el-button>
        </div>
        <div class="user-info" v-else>
          <span class="welcome-text">
            您好，
            <span class="username-link" @click="goToUserLayout">{{ currentUser?.nickname || currentUser?.email || '用户' }}</span>
          </span>
        </div>
      </div>
    </header>

    <main class="main-content">
      <section class="hero-section">
        <div class="container">
          <div class="hero-content">
            <h1>安全可靠的云存储</h1>
            <p>轻松存储，随时访问，让您的数据安全无忧</p>
            <div class="cta-buttons">
              <el-button type="primary" size="large" class="start-btn" @click="handleStart">
                {{ isAuthenticated ? '进入我的网盘' : '免费开始使用' }}
              </el-button>
              <el-button size="large" class="demo-btn" @click="handleDemo">查看演示</el-button>
            </div>
          </div>
        </div>
      </section>

      <section id="features" class="features-section">
        <div class="container">
          <h2>核心功能</h2>
          <div class="features-grid">
            <div class="feature-card">
              <div class="feature-icon">
                <i class="el-icon-cloudy"></i>
              </div>
              <h3>数据安全</h3>
              <p>三重加密保护，数据安全是我们的生命</p>
            </div>
            <div class="feature-card">
              <div class="feature-icon">
                <i class="el-icon-share"></i>
              </div>
              <h3>便捷分享</h3>
              <p>一键分享文件给朋友，自由查看和编辑</p>
            </div>
            <div class="feature-card">
              <div class="feature-icon">
                <i class="el-icon-mobile-phone"></i>
              </div>
              <h3>多设备同步</h3>
              <p>电脑、手机浏览器随时访问，无需APP</p>
            </div>
            <div class="feature-card">
              <div class="feature-icon">
                <i class="el-icon-download"></i>
              </div>
              <h3>全速下载</h3>
              <p>免费用户也能全速下载，您的时间比会员更宝贵</p>
            </div>
          </div>
        </div>
      </section>


    </main>

    <footer class="footer">
      <div class="container">
        <div class="footer-content">
          <div class="footer-section">
            <h3>关于轻羽云盘</h3>
            <p>让每个人都能轻松享受云存储的便利</p>
          </div>
          <div class="footer-section">
            <h4>产品服务</h4>
            <ul>
              <li><a href="#features">功能特色</a></li>
            </ul>
          </div>
          <div class="footer-section">
            <h4>帮助支持</h4>
            <ul>
              <li><a href="/user-guide" target="_blank">使用指南</a></li>
              <li><a href="/contact-us" target="_blank">联系我们</a></li>
              <li><a href="/faq" target="_blank">常见问题</a></li>
            </ul>
          </div>
          <div class="footer-section">
            <h4>法律信息</h4>
            <ul>
              <li><a href="/author-info" target="_blank">作者介绍</a></li>
              <li><a href="/privacy-policy" target="_blank">隐私保护</a></li>
            </ul>
          </div>
        </div>
        <div class="footer-bottom">
          <p>&copy; 2024-2025 Jasun.xyz 版权所有 | ICP证：还在申请中 | 公网安备 还在备案中</p>
          <p>轻羽云盘 - 安全可靠的云存储服务</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script>
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import store from '@/utils/store.js';

export default {
  name: 'IndexLayout',
  setup() {
    const isAuthenticated = computed(() => store.state.isAuthenticated);
    const currentUser = computed(() => store.state.user);
    const router = useRouter();

    const goToUserLayout = () => {
      router.push('/user');
    };

    const handleLogin = () => {
      router.push('/login');
    };

    const handleRegister = () => {
      router.push('/login?mode=register');
    };

    const handleLogout = () => {
      store.logout();
      ElMessage.success('您已成功退出登录');
      router.push('/');
    };

    const handleDemo = () => {
      window.open('https://gitee.com/Yangshengzhou/leaf-pan', '_blank');
    };

    const handleStart = () => {
      if (isAuthenticated.value) {
        if (store.state.isAdmin) {
          router.push('/admin');
        } else {
          router.push('/user');
        }
      } else {
        router.push('/login');
      }
    };

    return {
      isAuthenticated,
      currentUser,
      goToUserLayout,
      handleLogin,
      handleRegister,
      handleLogout,
      handleDemo,
      handleStart
    };
  }
}
</script>

<style scoped>
.index-layout {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  color: #1a1a1a;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #ffffff;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}

.header {
  background-color: #fff;
  border-bottom: 1px solid #e8eaed;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header .container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  height: 56px;
}

.logo-area h1 {
  color: #1a73e8;
  font-size: 18px;
  margin: 0;
  font-weight: 600;
}

.auth-buttons {
  display: flex;
  gap: 10px;
}

.register-btn {
  border: 1px solid #e8eaed;
  color: #5f6368;
  background-color: #fff;
  border-radius: 6px;
  padding: 8px 20px;
  font-size: 14px;
}

.register-btn:hover {
  border-color: #dadce0;
  background-color: #f1f3f4;
}

.login-btn {
  border-radius: 6px;
  padding: 8px 20px;
  font-size: 14px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 4px;
}

.welcome-text {
  color: #5f6368;
  font-size: 14px;
}

.username-link {
  color: #1a73e8;
  font-size: 14px;
  cursor: pointer;
  font-weight: 500;
}

.username-link:hover {
  color: #1557b0;
  text-decoration: underline;
}

.main-content {
  flex: 1;
}

.hero-section {
  background-color: #f8f9fa;
  padding: 72px 0;
}

.hero-section .container {
  display: flex;
  align-items: center;
  justify-content: center;
}

.hero-content {
  text-align: center;
  max-width: 720px;
}

.hero-content h1 {
  font-size: 42px;
  line-height: 1.2;
  margin-bottom: 16px;
  color: #202124;
  font-weight: 600;
}

.hero-content p {
  font-size: 16px;
  margin-bottom: 32px;
  color: #5f6368;
  line-height: 1.6;
}

.cta-buttons {
  display: flex;
  gap: 12px;
  justify-content: center;
}

.start-btn {
  padding: 10px 24px;
  font-size: 14px;
  background-color: #1a73e8;
  border-color: #1a73e8;
  border-radius: 6px;
}

.start-btn:hover {
  background-color: #1557b0;
  border-color: #1557b0;
}

.demo-btn {
  padding: 10px 24px;
  font-size: 14px;
  border-color: #e8eaed;
  color: #5f6368;
  background-color: #fff;
  border-radius: 6px;
}

.demo-btn:hover {
  border-color: #dadce0;
  background-color: #f1f3f4;
}

.features-section {
  padding: 64px 0;
  text-align: center;
}

.features-section h2 {
  font-size: 28px;
  margin-bottom: 40px;
  color: #202124;
  font-weight: 600;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 24px;
}

.feature-card {
  background: #fff;
  padding: 28px 20px;
  border: 1px solid #e8eaed;
  border-radius: 8px;
  transition: all 0.2s;
}

.feature-card:hover {
  border-color: #1a73e8;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.feature-icon {
  font-size: 36px;
  color: #1a73e8;
  margin-bottom: 14px;
}

.feature-card h3 {
  font-size: 16px;
  margin-bottom: 10px;
  color: #202124;
  font-weight: 600;
}

.feature-card p {
  color: #5f6368;
  line-height: 1.5;
  font-size: 14px;
}

.footer {
  background-color: #202124;
  color: #fff;
  padding: 40px 0 20px;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 28px;
  margin-bottom: 28px;
}

.footer-section h3,
.footer-section h4 {
  margin-bottom: 14px;
  color: #fff;
  font-weight: 600;
}

.footer-section h3 {
  font-size: 15px;
}

.footer-section h4 {
  font-size: 14px;
}

.footer-section p {
  color: #9aa0a6;
  font-size: 13px;
  line-height: 1.5;
}

.footer-section ul {
  list-style: none;
  padding: 0;
}

.footer-section ul li {
  margin-bottom: 6px;
}

.footer-section a {
  color: #9aa0a6;
  text-decoration: none;
  font-size: 13px;
  transition: color 0.2s;
}

.footer-section a:hover {
  color: #fff;
}

.footer-bottom {
  border-top: 1px solid #3c4043;
  padding-top: 20px;
  text-align: center;
  color: #9aa0a6;
  font-size: 12px;
  line-height: 1.5;
}

.footer-bottom p {
  margin: 4px 0;
}

/* Responsive Design */
@media (max-width: 768px) {
  .header .container {
    flex-direction: column;
    gap: 12px;
    padding: 12px 20px;
    height: auto;
  }

  .hero-section {
    padding: 48px 0;
  }

  .hero-section .container {
    text-align: center;
  }

  .hero-content h1 {
    font-size: 32px;
  }

  .hero-content p {
    font-size: 15px;
  }

  .cta-buttons {
    justify-content: center;
    flex-direction: column;
    align-items: center;
  }

  .start-btn, .demo-btn {
    width: 200px;
  }

  .features-section {
    padding: 48px 0;
  }

  .features-section h2 {
    font-size: 24px;
  }

  .features-grid {
    grid-template-columns: 1fr;
  }

  .footer-content {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 480px) {
  .container {
    padding: 0 16px;
  }

  .header .container {
    padding: 10px 16px;
  }

  .logo-area h1 {
    font-size: 16px;
  }

  .auth-buttons {
    gap: 8px;
  }

  .register-btn, .login-btn {
    padding: 8px 16px;
    font-size: 13px;
  }

  .hero-section {
    padding: 36px 0;
  }

  .hero-content h1 {
    font-size: 26px;
    margin-bottom: 12px;
  }

  .hero-content p {
    margin-bottom: 24px;
    font-size: 14px;
  }

  .start-btn, .demo-btn {
    width: 180px;
    padding: 10px 20px;
    font-size: 13px;
  }

  .features-section {
    padding: 36px 0;
  }

  .features-section h2 {
    font-size: 20px;
  }

  .feature-card {
    padding: 24px 18px;
  }

  .footer {
    padding: 32px 0 16px;
  }

  .footer-bottom p {
    font-size: 11px;
  }
}
</style>
