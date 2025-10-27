<template>
  <div class="author-info-page">
    <div class="header-bg">
      <header class="page-header">
        <div class="container">
          <h1 class="fade-in-up">关于作者</h1>
          <p class="fade-in-up">了解枫叶网盘的创作者和开发理念</p>
        </div>
      </header>
    </div>

    <main class="page-content">
      <div class="container">
        <div class="author-sections">
          <section class="author-section fade-in-up">
            <div class="section-header">
              <h2>开发者简介</h2>
              <div class="divider"></div>
            </div>
            <div class="author-content">
              <div class="author-profile">
                <div class="profile-image">
                  <div class="avatar-placeholder"></div>
                </div>
                <div class="profile-info">
                  <h3>杨顺哲</h3>
                  <p class="profile-title">全栈开发工程师</p>
                  <p class="profile-bio">专注于Web应用开发，具有丰富的前后端开发经验。热爱技术，致力于打造简洁、高效、用户友好的产品。</p>
                  <div class="profile-links">
                    <a href="mailto:yangsz03@foxmail.com" class="contact-link">
                      <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path>
                        <polyline points="22,6 12,13 2,6"></polyline>
                      </svg>
                      yangsz03@foxmail.com
                    </a>
                    <a href="https://qm.qq.com/q/OXBUbJCmAM" target="_blank" class="contact-link">
                      <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
                      </svg>
                      QQ交流群
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </section>

          <section class="author-section fade-in-up">
            <div class="section-header">
              <h2>开发历程</h2>
              <div class="divider"></div>
            </div>
            <div class="timeline-content">
              <div class="timeline">
                <div class="timeline-item">
                  <div class="timeline-marker"></div>
                  <div class="timeline-content">
                    <h3>项目构思</h3>
                    <p class="timeline-date">2024年初</p>
                    <p>基于个人文件存储需求，开始构思一个简洁、安全、易用的个人网盘系统。</p>
                  </div>
                </div>
                <div class="timeline-item">
                  <div class="timeline-marker"></div>
                  <div class="timeline-content">
                    <h3>技术选型</h3>
                    <p class="timeline-date">2024年中</p>
                    <p>确定使用Vue3 + SpringBoot3技术栈，选择MySQL作为数据库，采用JWT进行身份认证。</p>
                  </div>
                </div>
                <div class="timeline-item">
                  <div class="timeline-marker"></div>
                  <div class="timeline-content">
                    <h3>核心开发</h3>
                    <p class="timeline-date">2024年下半年</p>
                    <p>完成核心功能开发，包括文件上传下载、用户管理、权限控制等模块。</p>
                  </div>
                </div>
                <div class="timeline-item">
                  <div class="timeline-marker"></div>
                  <div class="timeline-content">
                    <h3>测试优化</h3>
                    <p class="timeline-date">2025年初</p>
                    <p>进行系统测试和性能优化，修复bug，提升用户体验。</p>
                  </div>
                </div>
                <div class="timeline-item">
                  <div class="timeline-marker"></div>
                  <div class="timeline-content">
                    <h3>正式发布</h3>
                    <p class="timeline-date">2025年3月</p>
                    <p>枫叶网盘正式发布，为用户提供稳定可靠的文件存储服务。</p>
                  </div>
                </div>
              </div>
            </div>
          </section>

          <section class="author-section fade-in-up">
            <div class="section-header">
              <h2>技术栈</h2>
              <div class="divider"></div>
            </div>
            <div class="tech-content">
              <div class="tech-grid">
                <div class="tech-category">
                  <h3>前端技术</h3>
                  <div class="tech-items">
                    <div class="tech-item">
                      <div class="tech-icon">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                          <path d="M12 2L2 7L12 12L22 7L12 2Z"></path>
                          <path d="M2 17L12 22L22 17"></path>
                          <path d="M2 12L12 17L22 12"></path>
                        </svg>
                      </div>
                      <h4>Vue 3</h4>
                      <p>现代前端框架，提供响应式数据绑定和组件化开发</p>
                    </div>
                    <div class="tech-item">
                      <div class="tech-icon">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                          <rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect>
                          <line x1="9" y1="9" x2="15" y2="15"></line>
                          <line x1="15" y1="9" x2="9" y2="15"></line>
                        </svg>
                      </div>
                      <h4>Element Plus</h4>
                      <p>基于Vue 3的组件库，提供丰富的UI组件</p>
                    </div>
                    <div class="tech-item">
                      <div class="tech-icon">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                          <path d="M12 2L2 7L12 12L22 7L12 2Z"></path>
                          <path d="M2 17L12 22L22 17"></path>
                          <path d="M2 12L12 17L22 12"></path>
                        </svg>
                      </div>
                      <h4>Vite</h4>
                      <p>下一代前端构建工具，提供快速的开发体验</p>
                    </div>
                  </div>
                </div>
                <div class="tech-category">
                  <h3>后端技术</h3>
                  <div class="tech-items">
                    <div class="tech-item">
                      <div class="tech-icon">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                          <path d="M12 2L2 7L12 12L22 7L12 2Z"></path>
                          <path d="M2 17L12 22L22 17"></path>
                          <path d="M2 12L12 17L22 12"></path>
                        </svg>
                      </div>
                      <h4>Spring Boot 3</h4>
                      <p>Java企业级应用开发框架，简化Spring应用开发</p>
                    </div>
                    <div class="tech-item">
                      <div class="tech-icon">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                          <path d="M12 2L2 7L12 12L22 7L12 2Z"></path>
                          <path d="M2 17L12 22L22 17"></path>
                          <path d="M2 12L12 17L22 12"></path>
                        </svg>
                      </div>
                      <h4>MySQL</h4>
                      <p>关系型数据库，存储用户数据和文件元信息</p>
                    </div>
                    <div class="tech-item">
                      <div class="tech-icon">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                          <path d="M12 2L2 7L12 12L22 7L12 2Z"></path>
                          <path d="M2 17L12 22L22 17"></path>
                          <path d="M2 12L12 17L22 12"></path>
                        </svg>
                      </div>
                      <h4>JWT</h4>
                      <p>JSON Web Token，用于用户身份认证和授权</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </section>

          <section class="author-section fade-in-up">
            <div class="section-header">
              <h2>项目特色</h2>
              <div class="divider"></div>
            </div>
            <div class="features-content">
              <div class="features-grid">
                <div class="feature-item">
                  <div class="feature-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path>
                    </svg>
                  </div>
                  <h3>安全可靠</h3>
                  <p>采用JWT身份认证，文件加密存储，确保用户数据安全</p>
                </div>
                <div class="feature-item">
                  <div class="feature-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"></path>
                    </svg>
                  </div>
                  <h3>高效传输</h3>
                  <p>支持大文件分片上传，断点续传，提升传输效率</p>
                </div>
                <div class="feature-item">
                  <div class="feature-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect>
                      <line x1="9" y1="9" x2="15" y2="15"></line>
                      <line x1="15" y1="9" x2="9" y2="15"></line>
                    </svg>
                  </div>
                  <h3>简洁易用</h3>
                  <p>直观的用户界面，操作简单，无需学习成本</p>
                </div>
                <div class="feature-item">
                  <div class="feature-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <path d="M12 2L2 7L12 12L22 7L12 2Z"></path>
                      <path d="M2 17L12 22L22 17"></path>
                      <path d="M2 12L12 17L22 12"></path>
                    </svg>
                  </div>
                  <h3>跨平台</h3>
                  <p>支持Web端访问，兼容主流浏览器，随时随地管理文件</p>
                </div>
              </div>
            </div>
          </section>

          <section class="author-section fade-in-up">
            <div class="section-header">
              <h2>未来规划</h2>
              <div class="divider"></div>
            </div>
            <div class="future-content">
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
      // 添加滚动动画
      const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
      };

      const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
          if (entry.isIntersecting) {
            entry.target.classList.add('visible');
          }
        });
      }, observerOptions);

      document.querySelectorAll('.fade-in-up').forEach(el => {
        observer.observe(el);
      });
    });

    return {};
  }
}
</script>

<style scoped>
.author-info-page {
  min-height: 100vh;
  background-color: #f8fafc;
  overflow-x: hidden;
}

.header-bg {
  position: relative;
  background: linear-gradient(135deg, #409EFF 0%, #67c23a 100%);
  color: white;
  padding: 80px 0 60px;
  overflow: hidden;
}

.page-header {
  position: relative;
  z-index: 1;
  text-align: center;
}

.page-header h1 {
  font-size: 48px;
  margin-bottom: 20px;
  font-weight: 700;
  letter-spacing: -0.5px;
}

.page-header p {
  font-size: 20px;
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
  border-radius: 16px;
  padding: 0;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.section-header {
  background: linear-gradient(135deg, #409EFF 0%, #67c23a 100%);
  padding: 30px 40px;
  color: white;
}

.section-header h2 {
  font-size: 28px;
  margin: 0;
  font-weight: 600;
}

.divider {
  width: 60px;
  height: 4px;
  background: rgba(255, 255, 255, 0.7);
  margin-top: 15px;
  border-radius: 2px;
}

.author-content {
  padding: 40px;
}

.author-profile {
  display: flex;
  gap: 40px;
  align-items: center;
}

.profile-image {
  flex-shrink: 0;
}

.avatar-placeholder {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  background: linear-gradient(135deg, #409EFF 0%, #67c23a 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 48px;
  font-weight: bold;
}

.profile-info {
  flex: 1;
}

.profile-info h3 {
  font-size: 32px;
  margin: 0 0 10px 0;
  color: #2c3e50;
}

.profile-title {
  font-size: 18px;
  color: #409EFF;
  margin-bottom: 15px;
  font-weight: 500;
}

.profile-bio {
  color: #4a5568;
  line-height: 1.7;
  margin-bottom: 20px;
  font-size: 16px;
}

.profile-links {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.contact-link {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #409EFF;
  text-decoration: none;
  font-weight: 500;
  padding: 8px 16px;
  border: 1px solid #409EFF;
  border-radius: 4px;
  transition: all 0.3s;
}

.contact-link:hover {
  background: #409EFF;
  color: white;
}

.timeline-content {
  padding: 40px;
}

.timeline {
  position: relative;
  padding-left: 30px;
}

.timeline::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  height: 100%;
  width: 2px;
  background: #e2e8f0;
}

.timeline-item {
  position: relative;
  margin-bottom: 40px;
}

.timeline-item:last-child {
  margin-bottom: 0;
}

.timeline-marker {
  position: absolute;
  left: -34px;
  top: 5px;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: #409EFF;
  border: 2px solid white;
  box-shadow: 0 0 0 2px #e2e8f0;
}

.timeline-content {
  background: #f8fafc;
  padding: 20px;
  border-radius: 8px;
  border-left: 3px solid #409EFF;
}

.timeline-content h3 {
  margin: 0 0 5px 0;
  color: #2c3e50;
  font-size: 20px;
}

.timeline-date {
  color: #409EFF;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 10px;
}

.timeline-content p {
  margin: 0;
  color: #4a5568;
  line-height: 1.6;
}

.tech-content {
  padding: 40px;
}

.tech-grid {
  display: flex;
  flex-direction: column;
  gap: 40px;
}

.tech-category h3 {
  font-size: 22px;
  margin-bottom: 20px;
  color: #2c3e50;
  font-weight: 600;
}

.tech-items {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.tech-item {
  background: #f8fafc;
  border-radius: 8px;
  padding: 25px;
  border-left: 3px solid #409EFF;
  transition: transform 0.3s, box-shadow 0.3s;
}

.tech-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.05);
}

.tech-icon {
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, #409EFF 0%, #67c23a 100%);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 15px;
  color: white;
}

.tech-item h4 {
  font-size: 18px;
  margin: 0 0 10px 0;
  color: #2c3e50;
  font-weight: 600;
}

.tech-item p {
  color: #4a5568;
  line-height: 1.6;
  margin: 0;
}

.features-content {
  padding: 40px;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
}

.feature-item {
  background: #f8fafc;
  border-radius: 12px;
  padding: 30px;
  text-align: center;
  transition: transform 0.3s, box-shadow 0.3s;
}

.feature-item:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
}

.feature-icon {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #409EFF 0%, #67c23a 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  color: white;
}

.feature-item h3 {
  font-size: 18px;
  margin-bottom: 15px;
  color: #2c3e50;
  font-weight: 600;
}

.feature-item p {
  color: #4a5568;
  line-height: 1.6;
  margin: 0;
}

.future-content {
  padding: 40px;
}

.future-plans {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
}

.plan-item {
  background: #f8fafc;
  border-radius: 12px;
  padding: 30px;
  border-left: 3px solid #409EFF;
  transition: transform 0.3s, box-shadow 0.3s;
}

.plan-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.05);
}

.plan-item h3 {
  font-size: 20px;
  margin: 0 0 15px 0;
  color: #2c3e50;
  font-weight: 600;
}

.plan-item p {
  color: #4a5568;
  line-height: 1.6;
  margin: 0;
}

.container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 20px;
}

.fade-in-up {
  opacity: 0;
  transform: translateY(30px);
  transition: opacity 0.6s ease, transform 0.6s ease;
}

.fade-in-up.visible {
  opacity: 1;
  transform: translateY(0);
}

@media (max-width: 768px) {
  .page-header h1 {
    font-size: 36px;
  }
  
  .page-header p {
    font-size: 18px;
  }
  
  .section-header {
    padding: 25px 20px;
  }
  
  .section-header h2 {
    font-size: 24px;
  }
  
  .author-content,
  .timeline-content,
  .tech-content,
  .features-content,
  .future-content {
    padding: 25px 20px;
  }
  
  .author-profile {
    flex-direction: column;
    text-align: center;
    gap: 30px;
  }
  
  .profile-links {
    justify-content: center;
  }
  
  .tech-items,
  .features-grid,
  .future-plans {
    grid-template-columns: 1fr;
  }
  
  .timeline {
    padding-left: 20px;
  }
  
  .timeline-marker {
    left: -24px;
  }
}
</style>