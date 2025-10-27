<template>
  <div class="author-info-page">
    <!-- 简洁的头部区域 -->
    <header class="page-header">
      <div class="container">
        <h1>关于作者</h1>
        <p>LeafPan网盘系统开发者简介</p>
      </div>
    </header>

    <main class="page-content">
      <div class="container">
        <div class="author-sections">
          <!-- 开发者简介 -->
          <section class="author-section">
            <div class="section-header">
              <h2>开发者简介</h2>
            </div>
            <div class="section-content">
              <div class="author-profile">
                <div class="profile-image">
                  <div class="avatar-placeholder">L</div>
                </div>
                <div class="profile-info">
                  <h3>LeafPan开发者</h3>
                  <p class="profile-title">全栈开发者 & 项目创始人</p>
                  <p class="profile-bio">
                    专注于Web开发技术，致力于为用户提供高效、安全的文件管理解决方案。
                    通过LeafPan项目，希望能够帮助用户更好地管理和分享文件。
                  </p>
                  <div class="profile-links">
                    <a href="mailto:yangsz03@foxmail.com" class="contact-link">
                      联系作者
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </section>

          <!-- 开发历程 -->
          <section class="author-section">
            <div class="section-header">
              <h2>开发历程</h2>
            </div>
            <div class="section-content">
              <div class="timeline">
                <div class="timeline-item">
                  <div class="timeline-marker"></div>
                  <div class="timeline-content">
                    <h3>项目构思</h3>
                    <p class="timeline-date">2024年1月</p>
                    <p>基于对现有网盘系统的分析，构思开发一个轻量级、易用的个人网盘系统</p>
                  </div>
                </div>
                <div class="timeline-item">
                  <div class="timeline-marker"></div>
                  <div class="timeline-content">
                    <h3>技术选型</h3>
                    <p class="timeline-date">2024年2月</p>
                    <p>确定使用Vue 3 + Spring Boot 3的技术栈，开始系统架构设计</p>
                  </div>
                </div>
                <div class="timeline-item">
                  <div class="timeline-marker"></div>
                  <div class="timeline-content">
                    <h3>核心开发</h3>
                    <p class="timeline-date">2024年3月-5月</p>
                    <p>完成前后端核心功能开发，包括文件上传下载、用户管理、权限控制等</p>
                  </div>
                </div>
                <div class="timeline-item">
                  <div class="timeline-marker"></div>
                  <div class="timeline-content">
                    <h3>测试优化</h3>
                    <p class="timeline-date">2024年6月</p>
                    <p>进行系统测试和性能优化，完善用户体验</p>
                  </div>
                </div>
                <div class="timeline-item">
                  <div class="timeline-marker"></div>
                  <div class="timeline-content">
                    <h3>正式发布</h3>
                    <p class="timeline-date">2024年7月</p>
                    <p>LeafPan网盘系统正式上线，为用户提供服务</p>
                  </div>
                </div>
              </div>
            </div>
          </section>

          <!-- 技术栈 -->
          <section class="author-section">
            <div class="section-header">
              <h2>技术栈</h2>
            </div>
            <div class="section-content">
              <div class="tech-grid">
                <div class="tech-category">
                  <h3>前端技术</h3>
                  <div class="tech-items">
                    <div class="tech-item">
                      <h4>Vue 3</h4>
                      <p>现代化的前端框架，提供响应式数据绑定和组件化开发</p>
                    </div>
                    <div class="tech-item">
                      <h4>Element Plus</h4>
                      <p>基于Vue 3的UI组件库，提供丰富的界面组件</p>
                    </div>
                    <div class="tech-item">
                      <h4>Vite</h4>
                      <p>快速的前端构建工具，提供优秀的开发体验</p>
                    </div>
                  </div>
                </div>
                <div class="tech-category">
                  <h3>后端技术</h3>
                  <div class="tech-items">
                    <div class="tech-item">
                      <h4>Spring Boot 3</h4>
                      <p>Java后端框架，提供快速开发和部署能力</p>
                    </div>
                    <div class="tech-item">
                      <h4>MySQL</h4>
                      <p>关系型数据库，存储用户数据和文件信息</p>
                    </div>
                    <div class="tech-item">
                      <h4>JWT</h4>
                      <p>身份认证机制，确保系统安全性</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </section>

          <!-- 项目特色 -->
          <section class="author-section">
            <div class="section-header">
              <h2>项目特色</h2>
            </div>
            <div class="section-content">
              <div class="features-grid">
                <div class="feature-item">
                  <h3>安全可靠</h3>
                  <p>采用JWT身份认证，文件加密存储，确保用户数据安全</p>
                </div>
                <div class="feature-item">
                  <h3>高效传输</h3>
                  <p>支持大文件分片上传，断点续传，提升传输效率</p>
                </div>
                <div class="feature-item">
                  <h3>简洁易用</h3>
                  <p>直观的用户界面，操作简单，无需学习成本</p>
                </div>
                <div class="feature-item">
                  <h3>跨平台</h3>
                  <p>支持Web端访问，兼容主流浏览器，随时随地管理文件</p>
                </div>
              </div>
            </div>
          </section>

          <!-- 未来规划 -->
          <section class="author-section">
            <div class="section-header">
              <h2>未来规划</h2>
            </div>
            <div class="section-content">
              <div class="future-plans">
                <div class="plan-item">
                  <h3>功能扩展</h3>
                  <p>计划添加文件预览、在线编辑、协作功能等，提升用户体验</p>
                </div>
                <div class="plan-item">
                  <h3>性能优化</h3>
                  <p>持续优化系统性能，提升文件传输速度和系统稳定性</p>
                </div>
                <div class="plan-item">
                  <h3>移动端支持</h3>
                  <p>开发移动端应用，提供更好的移动设备使用体验</p>
                </div>
                <div class="plan-item">
                  <h3>开源计划</h3>
                  <p>考虑将项目开源，与开发者社区共同完善产品</p>
                </div>
              </div>
            </div>
          </section>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import { onMounted } from 'vue';

export default {
  name: 'AuthorInfoPage',
  setup() {
    onMounted(() => {
      // 页面加载完成后的逻辑
    });
  }
}
</script>

<style scoped>
.author-info-page {
  min-height: 100vh;
  background-color: #f8fafc;
}

.page-header {
  background: #2d3748;
  color: white;
  padding: 60px 0;
  text-align: center;
}

.page-header h1 {
  font-size: 36px;
  margin-bottom: 16px;
  font-weight: 700;
}

.page-header p {
  font-size: 18px;
  opacity: 0.9;
  max-width: 600px;
  margin: 0 auto;
}

.page-content {
  padding: 60px 0 80px;
}

.author-sections {
  display: flex;
  flex-direction: column;
  gap: 60px;
}

.author-section {
  background: white;
  border-radius: 8px;
  padding: 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.section-header {
  background: #2d3748;
  padding: 24px 32px;
  color: white;
}

.section-header h2 {
  font-size: 24px;
  margin: 0;
  font-weight: 600;
}

.section-content {
  padding: 32px;
}

.author-profile {
  display: flex;
  align-items: center;
  gap: 32px;
}

.profile-image {
  flex-shrink: 0;
}

.avatar-placeholder {
  width: 120px;
  height: 120px;
  background: #3182ce;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 48px;
  font-weight: bold;
}

.profile-info h3 {
  font-size: 28px;
  margin-bottom: 8px;
  color: #2c3e50;
}

.profile-title {
  font-size: 18px;
  color: #4a5568;
  margin-bottom: 16px;
}

.profile-bio {
  font-size: 16px;
  line-height: 1.6;
  color: #4a5568;
  margin-bottom: 24px;
}

.contact-link {
  display: inline-block;
  color: #3182ce;
  font-weight: 600;
  text-decoration: none;
  padding: 8px 16px;
  border: 1px solid #3182ce;
  border-radius: 4px;
  transition: all 0.2s;
}

.contact-link:hover {
  background: #3182ce;
  color: white;
}

.timeline {
  position: relative;
  padding-left: 32px;
}

.timeline::before {
  content: '';
  position: absolute;
  left: 16px;
  top: 0;
  bottom: 0;
  width: 2px;
  background: #e2e8f0;
}

.timeline-item {
  position: relative;
  margin-bottom: 32px;
}

.timeline-marker {
  position: absolute;
  left: -24px;
  top: 8px;
  width: 12px;
  height: 12px;
  background: #3182ce;
  border-radius: 50%;
  border: 2px solid white;
  box-shadow: 0 0 0 2px #3182ce;
}

.timeline-content h3 {
  font-size: 18px;
  margin-bottom: 8px;
  color: #2c3e50;
}

.timeline-date {
  color: #3182ce;
  font-weight: 600;
  margin-bottom: 8px;
}

.timeline-content p {
  color: #4a5568;
  line-height: 1.6;
}

.tech-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 32px;
}

.tech-category h3 {
  font-size: 20px;
  margin-bottom: 16px;
  color: #2c3e50;
  text-align: center;
}

.tech-items {
  display: grid;
  gap: 16px;
}

.tech-item {
  background: #f8fafc;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
}

.tech-item h4 {
  font-size: 16px;
  margin-bottom: 8px;
  color: #2c3e50;
}

.tech-item p {
  color: #4a5568;
  line-height: 1.5;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
}

.feature-item {
  background: #f8fafc;
  border-radius: 8px;
  padding: 24px;
  text-align: center;
}

.feature-item h3 {
  font-size: 18px;
  margin-bottom: 12px;
  color: #2c3e50;
}

.feature-item p {
  color: #4a5568;
  line-height: 1.6;
}

.future-plans {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
}

.plan-item {
  background: #f8fafc;
  border-radius: 8px;
  padding: 24px;
  text-align: center;
}

.plan-item h3 {
  font-size: 18px;
  margin-bottom: 12px;
  color: #2c3e50;
}

.plan-item p {
  color: #4a5568;
  line-height: 1.6;
}

.container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 20px;
}

@media (max-width: 768px) {
  .page-header h1 {
    font-size: 28px;
  }
  
  .page-header p {
    font-size: 16px;
  }
  
  .author-profile {
    flex-direction: column;
    text-align: center;
  }
  
  .avatar-placeholder {
    width: 80px;
    height: 80px;
    font-size: 32px;
  }
  
  .profile-info h3 {
    font-size: 24px;
  }
  
  .section-header {
    padding: 20px;
  }
  
  .section-header h2 {
    font-size: 20px;
  }
  
  .section-content {
    padding: 24px 20px;
  }
  
  .tech-grid {
    grid-template-columns: 1fr;
  }
  
  .features-grid,
  .future-plans {
    grid-template-columns: 1fr;
  }
}
</style>