<!-- 首页展示页 -->
<template>
  <div class="index-layout">
    <header class="header">
      <div class="container">
        <div class="logo-area">
          <h1>枫叶网盘</h1>
        </div>
        <div class="auth-buttons">
          <ElButton type="default" class="register-btn">注册</ElButton>
          <ElButton type="primary" class="login-btn">登录</ElButton>
        </div>
      </div>
    </header>

    <main class="main-content">
      <section class="hero-section">
        <div class="container">
          <div class="hero-content">
            <h1>安全可靠的云存储解决方案</h1>
            <p>枫叶网盘为您提供高速、安全、便捷的文件存储与分享服务，随时随地访问您的重要数据</p>
            <div class="cta-buttons">
              <ElButton type="primary" size="large" class="start-btn">立即开始</ElButton>
              <ElButton type="default" size="large" class="demo-btn">功能演示</ElButton>
            </div>
          </div>
          <div class="hero-image">
            <img src="@/assets/hero-image.svg" alt="枫叶网盘" />
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
              <h3>安全存储</h3>
              <p>采用多重加密技术，确保您的文件安全无忧</p>
            </div>
            <div class="feature-card">
              <div class="feature-icon">
                <i class="el-icon-share"></i>
              </div>
              <h3>便捷分享</h3>
              <p>一键分享文件，支持多种分享方式和权限设置</p>
            </div>
            <div class="feature-card">
              <div class="feature-icon">
                <i class="el-icon-mobile-phone"></i>
              </div>
              <h3>多端同步</h3>
              <p>支持PC、手机、平板等多设备同步访问</p>
            </div>
            <div class="feature-card">
              <div class="feature-icon">
                <i class="el-icon-download"></i>
              </div>
              <h3>高速下载</h3>
              <p>全球加速节点，确保文件下载速度</p>
            </div>
          </div>
        </div>
      </section>

      <section class="pricing-section" id="pricing">
        <div class="container">
          <h2>价格方案</h2>
          <div class="pricing-cards">
            <div class="pricing-card">
              <h3>免费版</h3>
              <div class="price">¥0<span>/月</span></div>
              <ul class="features-list">
                <li>5GB 存储空间</li>
                <li>基础文件管理</li>
                <li>标准下载速度</li>
              </ul>
              <ElButton type="default" class="select-btn">选择方案</ElButton>
            </div>
            <div class="pricing-card popular">
              <div class="badge">推荐</div>
              <h3>标准版</h3>
              <div class="price">¥19<span>/月</span></div>
              <ul class="features-list">
                <li>100GB 存储空间</li>
                <li>高级文件管理</li>
                <li>高速下载通道</li>
                <li>文件分享功能</li>
              </ul>
              <ElButton type="primary" class="select-btn">选择方案</ElButton>
            </div>
            <div class="pricing-card">
              <h3>专业版</h3>
              <div class="price">¥49<span>/月</span></div>
              <ul class="features-list">
                <li>1TB 存储空间</li>
                <li>专业文件管理</li>
                <li>极速下载通道</li>
                <li>高级分享功能</li>
                <li>团队协作工具</li>
              </ul>
              <ElButton type="default" class="select-btn">选择方案</ElButton>
            </div>
          </div>
        </div>
      </section>
    </main>

    <footer class="footer">
      <div class="container">
        <div class="footer-content">
          <div class="footer-section">
            <h3>枫叶网盘</h3>
            <p>安全可靠的云存储解决方案</p>
          </div>
          <div class="footer-section">
            <h4>产品</h4>
            <ul>
              <li><a href="#features">功能特点</a></li>
              <li><a href="#pricing">价格方案</a></li>
              <li><a href="#">更新日志</a></li>
            </ul>
          </div>
          <div class="footer-section">
            <h4>支持</h4>
            <ul>
              <li><a href="#">帮助中心</a></li>
              <li><a href="#">联系我们</a></li>
              <li><a href="#">常见问题</a></li>
            </ul>
          </div>
          <div class="footer-section">
            <h4>关于</h4>
            <ul>
              <li><a href="#">关于我们</a></li>
              <li><a href="#">隐私政策</a></li>
              <li><a href="#">服务条款</a></li>
            </ul>
          </div>
        </div>
        <div class="footer-bottom">
          <p>&copy; 2023 枫叶网盘. All rights reserved.</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script>
import { ElButton } from 'element-plus';

export default {
  name: 'IndexLayout',
  data() {
    return {
      isLogin: false
    }
  },
  methods: {
    handleLogin() {
      // 处理登录逻辑
    },
    handleRegister() {
      // 处理注册逻辑
    }
  }
}
</script>

<style scoped>
.index-layout {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
  color: #333;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* Header Styles */
.header {
  background-color: #fff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header .container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
}

.logo-area h1 {
  color: #409EFF;
  font-size: 24px;
  margin: 0;
}

.nav-menu {
  display: flex;
  gap: 20px;
}

.nav-menu a {
  text-decoration: none;
  color: #333;
  font-weight: 500;
  transition: color 0.3s;
}

.nav-menu a:hover {
  color: #409EFF;
}

.auth-buttons {
  display: flex;
  gap: 10px;
}

.register-btn {
  border-color: #409EFF;
  color: #409EFF;
}

/* Main Content */
.main-content {
  flex: 1;
}

/* Hero Section */
.hero-section {
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 80px 0;
}

.hero-section .container {
  display: flex;
  align-items: center;
  gap: 50px;
}

.hero-content {
  flex: 1;
}

.hero-content h1 {
  font-size: 48px;
  line-height: 1.2;
  margin-bottom: 20px;
  color: #303133;
}

.hero-content p {
  font-size: 18px;
  margin-bottom: 30px;
  color: #606266;
  line-height: 1.6;
}

.cta-buttons {
  display: flex;
  gap: 15px;
}

.start-btn, .demo-btn {
  padding: 12px 25px;
  font-size: 16px;
}

.hero-image {
  flex: 1;
  text-align: center;
}

.hero-image img {
  max-width: 100%;
  height: auto;
}

/* Features Section */
.features-section {
  padding: 80px 0;
  text-align: center;
}

.features-section h2 {
  font-size: 36px;
  margin-bottom: 50px;
  color: #303133;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
}

.feature-card {
  background: #fff;
  border-radius: 8px;
  padding: 30px 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s, box-shadow 0.3s;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.feature-icon {
  font-size: 48px;
  color: #409EFF;
  margin-bottom: 20px;
}

.feature-card h3 {
  font-size: 20px;
  margin-bottom: 15px;
  color: #303133;
}

.feature-card p {
  color: #606266;
  line-height: 1.6;
}

/* Pricing Section */
.pricing-section {
  background-color: #f5f7fa;
  padding: 80px 0;
  text-align: center;
}

.pricing-section h2 {
  font-size: 36px;
  margin-bottom: 50px;
  color: #303133;
}

.pricing-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
}

.pricing-card {
  background: #fff;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  position: relative;
  transition: transform 0.3s, box-shadow 0.3s;
}

.pricing-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.pricing-card.popular {
  border: 2px solid #409EFF;
}

.badge {
  position: absolute;
  top: -15px;
  left: 50%;
  transform: translateX(-50%);
  background-color: #409EFF;
  color: white;
  padding: 5px 15px;
  border-radius: 20px;
  font-size: 14px;
}

.pricing-card h3 {
  font-size: 24px;
  margin-bottom: 15px;
  color: #303133;
}

.price {
  font-size: 36px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 20px;
}

.price span {
  font-size: 16px;
  color: #909399;
}

.features-list {
  list-style: none;
  padding: 0;
  margin: 0 0 30px;
  text-align: left;
}

.features-list li {
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
  position: relative;
  padding-left: 25px;
}

.features-list li:before {
  content: "✓";
  position: absolute;
  left: 0;
  color: #67C23A;
  font-weight: bold;
}

.select-btn {
  width: 100%;
}

/* Footer */
.footer {
  background-color: #303133;
  color: #fff;
  padding: 50px 0 20px;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 30px;
  margin-bottom: 30px;
}

.footer-section h3, .footer-section h4 {
  margin-bottom: 20px;
  color: #fff;
}

.footer-section ul {
  list-style: none;
  padding: 0;
}

.footer-section ul li {
  margin-bottom: 10px;
}

.footer-section a {
  color: #bbb;
  text-decoration: none;
  transition: color 0.3s;
}

.footer-section a:hover {
  color: #409EFF;
}

.footer-bottom {
  border-top: 1px solid #444;
  padding-top: 20px;
  text-align: center;
  color: #bbb;
}

/* Responsive Design */
@media (max-width: 768px) {
  .header .container {
    flex-direction: column;
    gap: 15px;
  }
  
  .nav-menu {
    display: none;
  }
  
  .hero-section .container {
    flex-direction: column;
    text-align: center;
  }
  
  .hero-content h1 {
    font-size: 36px;
  }
  
  .cta-buttons {
    justify-content: center;
  }
  
  .pricing-cards {
    grid-template-columns: 1fr;
  }
}
</style>