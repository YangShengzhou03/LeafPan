<template>
  <div class="legal-info-page">
    <div class="header-bg">
      <header class="page-header">
        <div class="container">
          <h1 class="fade-in-up">法律信息</h1>
          <p class="fade-in-up">了解枫叶网盘的法律条款和相关信息</p>
        </div>
      </header>
    </div>

    <main class="page-content">
      <div class="container">
        <div class="legal-intro fade-in-up">
          <p>以下是枫叶网盘的法律信息，包括隐私政策、服务条款等重要内容。使用本服务即表示您同意这些条款。</p>
        </div>

        <div class="legal-docs fade-in-up">
          <div class="doc-card" @click="navigateTo('/privacy-policy')">
            <div class="doc-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path>
              </svg>
            </div>
            <h3>隐私政策</h3>
            <p>了解我们如何收集、使用和保护您的个人信息</p>
            <div class="doc-link">
              <span>查看详情</span>
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <polyline points="9 18 15 12 9 6"></polyline>
              </svg>
            </div>
          </div>

          <div class="doc-card" @click="navigateTo('/terms-of-service')">
            <div class="doc-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                <polyline points="14 2 14 8 20 8"></polyline>
                <line x1="16" y1="13" x2="8" y2="13"></line>
                <line x1="16" y1="17" x2="8" y2="17"></line>
                <polyline points="10 9 9 9 8 9"></polyline>
              </svg>
            </div>
            <h3>服务条款</h3>
            <p>了解使用枫叶网盘服务的条款和条件</p>
            <div class="doc-link">
              <span>查看详情</span>
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <polyline points="9 18 15 12 9 6"></polyline>
              </svg>
            </div>
          </div>

          <div class="doc-card" @click="navigateTo('/cookie-policy')">
            <div class="doc-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M21 15.546c-.523 0-1.046.151-1.5.454a2.704 2.704 0 0 1-3 0 2.704 2.704 0 0 0-3 0 2.704 2.704 0 0 1-3 0 2.704 2.704 0 0 1-3 0 2.701 2.701 0 0 0-1.5-.454"></path>
                <path d="M9 6v2m3-2v2m3-2v2M9 3h.01M12 3h.01M15 3h.01M21 21v-7a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2v7h18zm-3-9v-2a2 2 0 0 0-2-2H8a2 2 0 0 0-2 2v2h12z"></path>
              </svg>
            </div>
            <h3>Cookie 政策</h3>
            <p>了解我们如何使用 Cookie 和类似技术</p>
            <div class="doc-link">
              <span>查看详情</span>
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <polyline points="9 18 15 12 9 6"></polyline>
              </svg>
            </div>
          </div>
        </div>

        <div class="legal-content fade-in-up">
          <div class="content-section">
            <h2>法律声明</h2>
            <p>枫叶网盘（以下简称"本站"）是由个人开发并运营的云存储服务。本站致力于为用户提供安全、便捷的文件存储和管理服务。</p>
            <p>本站的所有内容，包括但不限于文本、图片、音频、视频、软件、程序、数据等，均受相关法律法规的保护。未经本站书面许可，任何人不得擅自使用、传播、修改或用于商业目的。</p>
            <p>用户在使用本站服务时，应当遵守中华人民共和国相关法律法规，不得利用本站服务从事任何违法活动。本站有权对用户上传的内容进行审查，如发现违法内容，有权立即删除并保留追究相关责任的权利。</p>
            <p>本站保留随时修改、中断或终止本站服务的权利，无需事先通知用户。对于因不可抗力或非本站原因造成的 service 中断或数据丢失，本站不承担责任。</p>
          </div>

          <div class="content-section">
            <h2>知识产权</h2>
            <p>本站的所有内容，包括网站设计、文字、图片、图表、音频、视频、软件、程序、数据等，均受著作权法、商标法、专利法等相关法律法规的保护。</p>
            <p>未经本站书面许可，任何人不得擅自复制、传播、修改、展示或以其他方式使用本站的内容。对于侵犯本站知识产权的行为，本站将依法追究其法律责任。</p>
            <p>用户在本站上传的内容，其知识产权归用户所有。但用户上传内容即视为授权本站在全球范围内、非独占性地使用、复制、修改、改编、出版、翻译、分发该内容，用于提供和改进本站服务。</p>
          </div>

          <div class="content-section">
            <h2>免责声明</h2>
            <p>本站提供的服务按"现状"提供，不提供任何明示或暗示的保证，包括但不限于适销性、特定用途适用性和非侵权性的保证。</p>
            <p>对于因使用或无法使用本站服务而导致的任何直接、间接、偶然、特殊、后果性或惩罚性损害，本站不承担责任，即使本站已被告知可能发生此类损害。</p>
            <p>本站不保证服务的准确性、可靠性、完整性或及时性。用户在本站获取的任何信息、建议或服务，均不构成专业建议。用户应根据自身情况，独立判断并承担相应风险。</p>
            <p>本站有权随时修改本法律声明，修改后的声明将在本站公布。用户继续使用本站服务，即视为接受修改后的声明。</p>
          </div>

          <div class="content-section">
            <h2>联系我们</h2>
            <p>如果您对本法律声明有任何疑问或建议，请通过以下方式联系我们：</p>
            <div class="contact-info">
              <div class="contact-item">
                <div class="contact-icon">
                  <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path>
                    <polyline points="22,6 12,13 2,6"></polyline>
                  </svg>
                </div>
                <div>
                  <h3>电子邮箱</h3>
                  <p>yangsz03@foxmail.com</p>
                </div>
              </div>
              <div class="contact-item">
                <div class="contact-icon">
                  <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
                  </svg>
                </div>
                <div>
                  <h3>QQ群</h3>
                  <p>加入我们的QQ群获取实时帮助</p>
                  <a href="https://qm.qq.com/q/OXBUbJCmAM" target="_blank">点击加入</a>
                </div>
              </div>
              <div class="contact-item">
                <div class="contact-icon">
                  <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M12 2L2 7L12 12L22 7L12 2Z"></path>
                    <path d="M2 17L12 22L22 17"></path>
                    <path d="M2 12L12 17L22 12"></path>
                  </svg>
                </div>
                <div>
                  <h3>开源项目</h3>
                  <p>访问我们的开源项目了解更多</p>
                  <a href="https://gitee.com/Yangshengzhou/leaf-pan" target="_blank">查看源码</a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';

export default {
  name: 'LegalInfoPage',
  setup() {
    const router = useRouter();

    const navigateTo = (path) => {
      router.push(path);
    };

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

    return {
      navigateTo
    };
  }
}
</script>

<style scoped>
.legal-info-page {
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

.legal-intro {
  text-align: center;
  max-width: 800px;
  margin: 0 auto 50px;
  font-size: 18px;
  line-height: 1.6;
  color: #4a5568;
}

.legal-docs {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
  margin-bottom: 60px;
}

.doc-card {
  background: white;
  border-radius: 16px;
  padding: 40px 30px;
  text-align: center;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
}

.doc-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.1);
}

.doc-icon {
  width: 70px;
  height: 70px;
  background: linear-gradient(135deg, #409EFF 0%, #67c23a 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  color: white;
}

.doc-card h3 {
  font-size: 22px;
  margin-bottom: 15px;
  color: #2c3e50;
  font-weight: 600;
}

.doc-card p {
  color: #4a5568;
  line-height: 1.6;
  margin-bottom: 25px;
  font-size: 16px;
}

.doc-link {
  display: inline-flex;
  align-items: center;
  color: #409EFF;
  font-weight: 600;
  text-decoration: none;
  transition: color 0.3s;
}

.doc-link:hover {
  color: #66b1ff;
}

.doc-link svg {
  margin-left: 5px;
  transition: transform 0.3s;
}

.doc-link:hover svg {
  transform: translateX(3px);
}

.legal-content {
  background: white;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.content-section {
  padding: 40px;
  border-bottom: 1px solid #e2e8f0;
}

.content-section:last-child {
  border-bottom: none;
}

.content-section h2 {
  font-size: 28px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e2e8f0;
}

.content-section p {
  color: #4a5568;
  line-height: 1.8;
  margin-bottom: 20px;
  font-size: 16px;
}

.content-section p:last-child {
  margin-bottom: 0;
}

.contact-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
  margin-top: 30px;
}

.contact-item {
  display: flex;
  align-items: flex-start;
  gap: 15px;
}

.contact-icon {
  width: 40px;
  height: 40px;
  background: #f1f5f9;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #409EFF;
  flex-shrink: 0;
}

.contact-item h3 {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 5px 0;
}

.contact-item p {
  color: #4a5568;
  margin: 0 0 5px 0;
  font-size: 16px;
}

.contact-item a {
  color: #409EFF;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s;
}

.contact-item a:hover {
  color: #66b1ff;
  text-decoration: underline;
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
  
  .legal-docs {
    grid-template-columns: 1fr;
  }
  
  .content-section {
    padding: 30px 20px;
  }
  
  .content-section h2 {
    font-size: 24px;
  }
  
  .contact-info {
    grid-template-columns: 1fr;
  }
}
</style>