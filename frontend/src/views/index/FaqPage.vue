<template>
  <div class="faq-page">
    <div class="header-bg">
      <header class="page-header">
        <div class="container">
          <h1 class="fade-in-up">常见问题</h1>
          <p class="fade-in-up">解答您在使用枫叶网盘过程中可能遇到的问题</p>
        </div>
      </header>
    </div>

    <main class="page-content">
      <div class="container">
        <div class="faq-intro fade-in-up">
          <p>以下是我们整理的一些常见问题及其解答，希望能帮助您更好地使用枫叶网盘。如果您有其他问题，欢迎通过<a href="/contact">联系我们</a>页面获取帮助。</p>
        </div>

        <div class="faq-categories fade-in-up">
          <div class="category-tabs">
            <button 
              v-for="category in categories" 
              :key="category.id" 
              class="category-tab" 
              :class="{ active: activeCategory === category.id }"
              @click="activeCategory = category.id"
            >
              {{ category.name }}
            </button>
          </div>

          <div class="category-content">
            <div 
              v-for="category in categories" 
              :key="category.id" 
              class="category-section" 
              v-show="activeCategory === category.id"
            >
              <h2 class="category-title">{{ category.name }}</h2>
              <div class="faq-list">
                <div 
                  v-for="(faq, index) in category.faqs" 
                  :key="index" 
                  class="faq-item"
                >
                  <div class="faq-question" @click="toggleFaq(category.id, index)">
                    <h3>{{ faq.question }}</h3>
                    <div class="faq-toggle" :class="{ active: faq.isOpen }">
                      <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <polyline points="6 9 12 15 18 9"></polyline>
                      </svg>
                    </div>
                  </div>
                  <div class="faq-answer" v-show="faq.isOpen">
                    <p>{{ faq.answer }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="faq-help-section fade-in-up">
          <div class="help-content">
            <h2>没有找到您的问题？</h2>
            <p>如果您在常见问题中没有找到您需要的解答，可以通过以下方式获取帮助：</p>
            <div class="help-options">
              <div class="help-option">
                <div class="help-icon">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path>
                    <polyline points="22,6 12,13 2,6"></polyline>
                  </svg>
                </div>
                <h3>发送邮件</h3>
                <p>发送邮件至 yangsz03@foxmail.com，我们会尽快回复您</p>
                <a href="mailto:yangsz03@foxmail.com" class="help-link">发送邮件</a>
              </div>
              <div class="help-option">
                <div class="help-icon">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
                  </svg>
                </div>
                <h3>加入QQ群</h3>
                <p>加入我们的QQ群，与其他用户和开发者交流</p>
                <a href="https://qm.qq.com/q/OXBUbJCmAM" target="_blank" class="help-link">加入QQ群</a>
              </div>
              <div class="help-option">
                <div class="help-icon">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M12 2L2 7L12 12L22 7L12 2Z"></path>
                    <path d="M2 17L12 22L22 17"></path>
                    <path d="M2 12L12 17L22 12"></path>
                  </svg>
                </div>
                <h3>查看源码</h3>
                <p>访问我们的开源项目，了解更多技术细节</p>
                <a href="https://gitee.com/Yangshengzhou/leaf-pan" target="_blank" class="help-link">查看源码</a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import { reactive, ref, onMounted } from 'vue';

export default {
  name: 'FaqPage',
  setup() {
    const activeCategory = ref('general');

    const categories = reactive([
      {
        id: 'general',
        name: '基本使用',
        faqs: [
          {
            question: '枫叶网盘是什么？',
            answer: '枫叶网盘是一款基于云存储的文件管理平台，提供文件上传、下载、分享等功能，旨在为用户提供便捷、安全的文件存储和管理服务。',
            isOpen: false
          },
          {
            question: '枫叶网盘是否免费使用？',
            answer: '枫叶网盘提供免费基础版和高级版服务。免费版提供基本的文件存储和管理功能，高级版提供更多存储空间和高级功能。',
            isOpen: false
          },
          {
            question: '如何注册枫叶网盘账号？',
            answer: '点击网站右上角的"注册"按钮，填写必要信息，包括用户名、邮箱和密码，完成邮箱验证后即可成功注册。',
            isOpen: false
          },
          {
            question: '忘记密码怎么办？',
            answer: '在登录页面点击"忘记密码"链接，输入您的注册邮箱，系统会发送重置密码的链接到您的邮箱，按照邮件提示操作即可重置密码。',
            isOpen: false
          }
        ]
      },
      {
        id: 'features',
        name: '功能特性',
        faqs: [
          {
            question: '枫叶网盘支持哪些文件格式？',
            answer: '枫叶网盘支持几乎所有常见的文件格式，包括文档、图片、音频、视频、压缩包等。但出于安全考虑，某些可执行文件类型可能会被限制。',
            isOpen: false
          },
          {
            question: '单个文件上传大小限制是多少？',
            answer: '免费用户单文件上传限制为100MB，高级用户单文件上传限制为1GB。如需上传更大文件，请联系管理员申请特殊权限。',
            isOpen: false
          },
          {
            question: '如何分享文件给他人？',
            answer: '选择要分享的文件，点击"分享"按钮，设置分享权限和有效期，生成分享链接后即可发送给他人。您也可以设置提取码以增加安全性。',
            isOpen: false
          },
          {
            question: '是否支持批量操作？',
            answer: '是的，枫叶网盘支持批量选择文件进行上传、下载、移动、删除等操作，提高文件管理效率。',
            isOpen: false
          }
        ]
      },
      {
        id: 'security',
        name: '安全与隐私',
        faqs: [
          {
            question: '我的文件是否安全？',
            answer: '枫叶网盘采用多重安全措施保护您的文件，包括数据加密、安全传输、定期备份等。同时，我们严格遵守隐私政策，不会未经授权访问或分享您的文件。',
            isOpen: false
          },
          {
            question: '如何提高账户安全性？',
            answer: '建议您设置强密码，开启两步验证，定期更换密码，并不要在公共设备上保存登录信息。同时，避免使用简单密码或在多个平台使用相同密码。',
            isOpen: false
          },
          {
            question: '枫叶网盘会查看我的文件内容吗？',
            answer: '我们不会主动查看用户的文件内容。但在特定情况下，如收到法律要求或检测到可能违反服务条款的内容时，我们可能会进行必要的审查。',
            isOpen: false
          },
          {
            question: '文件会被删除吗？',
            answer: '免费用户如果超过90天未登录，且文件超过免费存储容量，我们可能会删除部分文件以释放空间。高级用户的文件在有效期内不会因不活跃而被删除。',
            isOpen: false
          }
        ]
      },
      {
        id: 'technical',
        name: '技术支持',
        faqs: [
          {
            question: '为什么文件上传速度很慢？',
            answer: '文件上传速度受多种因素影响，包括您的网络带宽、服务器负载、文件大小等。建议在网络状况良好时上传大文件，或使用我们的客户端工具以提高上传速度。',
            isOpen: false
          },
          {
            question: '上传文件失败怎么办？',
            answer: '请检查网络连接是否稳定，文件大小是否超过限制，文件格式是否被支持。如果问题持续存在，请联系我们的技术支持。',
            isOpen: false
          },
          {
            question: '是否提供API接口？',
            answer: '我们提供完整的API接口，开发者可以通过API集成枫叶网盘的功能到自己的应用中。详细文档请参考开发者中心。',
            isOpen: false
          },
          {
            question: '如何报告Bug或提出功能建议？',
            answer: '您可以通过"联系我们"页面发送邮件，或在我们的开源项目平台上提交Issue。我们会认真评估每一条反馈，并在后续版本中加以改进。',
            isOpen: false
          }
        ]
      }
    ]);

    const toggleFaq = (categoryId, index) => {
      const category = categories.find(c => c.id === categoryId);
      if (category) {
        category.faqs[index].isOpen = !category.faqs[index].isOpen;
      }
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
      activeCategory,
      categories,
      toggleFaq
    };
  }
}
</script>

<style scoped>
.faq-page {
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

.faq-intro {
  text-align: center;
  max-width: 800px;
  margin: 0 auto 50px;
  font-size: 18px;
  line-height: 1.6;
  color: #4a5568;
}

.faq-intro a {
  color: #409EFF;
  text-decoration: none;
  font-weight: 600;
}

.faq-categories {
  background: white;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  margin-bottom: 60px;
}

.category-tabs {
  display: flex;
  flex-wrap: wrap;
  border-bottom: 1px solid #e2e8f0;
  background: #f8fafc;
}

.category-tab {
  padding: 15px 25px;
  background: none;
  border: none;
  font-size: 16px;
  font-weight: 600;
  color: #4a5568;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
}

.category-tab:hover {
  color: #409EFF;
}

.category-tab.active {
  color: #409EFF;
}

.category-tab.active::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  width: 100%;
  height: 3px;
  background: #409EFF;
}

.category-content {
  padding: 40px;
}

.category-section {
  animation: fadeIn 0.5s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.category-title {
  font-size: 24px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 30px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e2e8f0;
}

.faq-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.faq-item {
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  overflow: hidden;
  transition: box-shadow 0.3s;
}

.faq-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.faq-question {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: #f8fafc;
  cursor: pointer;
  transition: background 0.3s;
}

.faq-question:hover {
  background: #f1f5f9;
}

.faq-question h3 {
  margin: 0;
  font-size: 18px;
  color: #2c3e50;
  font-weight: 600;
}

.faq-toggle {
  transition: transform 0.3s;
  color: #409EFF;
}

.faq-toggle.active {
  transform: rotate(180deg);
}

.faq-answer {
  padding: 0 20px 20px;
  color: #4a5568;
  line-height: 1.6;
}

.faq-help-section {
  background: white;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.help-content {
  padding: 40px;
  text-align: center;
}

.help-content h2 {
  font-size: 28px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 20px;
}

.help-content p {
  font-size: 18px;
  color: #4a5568;
  line-height: 1.6;
  margin-bottom: 40px;
}

.help-options {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
}

.help-option {
  background: #f8fafc;
  border-radius: 12px;
  padding: 30px;
  text-align: center;
  transition: transform 0.3s, box-shadow 0.3s;
}

.help-option:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
}

.help-icon {
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

.help-option h3 {
  font-size: 20px;
  margin-bottom: 15px;
  color: #2c3e50;
  font-weight: 600;
}

.help-option p {
  color: #4a5568;
  line-height: 1.6;
  margin-bottom: 20px;
  font-size: 16px;
}

.help-link {
  display: inline-block;
  color: #409EFF;
  font-weight: 600;
  text-decoration: none;
  position: relative;
  padding: 8px 16px;
  border: 1px solid #409EFF;
  border-radius: 4px;
  transition: all 0.3s;
}

.help-link:hover {
  background: #409EFF;
  color: white;
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
  
  .category-tabs {
    flex-direction: column;
  }
  
  .category-tab {
    padding: 12px 15px;
    text-align: center;
    border-bottom: 1px solid #e2e8f0;
  }
  
  .category-tab.active::after {
    display: none;
  }
  
  .category-tab.active {
    background: #e6f7ff;
  }
  
  .category-content {
    padding: 25px 20px;
  }
  
  .help-content {
    padding: 25px 20px;
  }
  
  .help-options {
    grid-template-columns: 1fr;
  }
}
</style>