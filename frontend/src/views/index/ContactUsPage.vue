<template>
  <div class="contact-us-page">
    <div class="header-bg">
      <header class="page-header">
        <div class="container">
          <h1 class="fade-in-up">联系我们</h1>
          <p class="fade-in-up">我们随时为您提供帮助和支持</p>
        </div>
      </header>
    </div>

    <main class="page-content">
      <div class="container">
        <div class="contact-sections">
          <section class="contact-section fade-in-up">
            <div class="section-header">
              <h2>联系方式</h2>
              <div class="divider"></div>
            </div>
            <div class="contact-methods">
              <div class="contact-method">
                <div class="contact-icon">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path>
                    <polyline points="22,6 12,13 2,6"></polyline>
                  </svg>
                </div>
                <h3>电子邮件</h3>
                <p>如有任何问题或建议，请通过邮件联系我们</p>
                <a href="mailto:yangsz03@foxmail.com" class="contact-link">yangsz03@foxmail.com</a>
              </div>
              <div class="contact-method">
                <div class="contact-icon">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
                  </svg>
                </div>
                <h3>QQ交流群</h3>
                <p>加入我们的QQ群，与其他用户和开发者交流</p>
                <a href="https://qm.qq.com/q/OXBUbJCmAM" target="_blank" class="contact-link">加入QQ群</a>
              </div>
              <div class="contact-method">
                <div class="contact-icon">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M12 2L2 7L12 12L22 7L12 2Z"></path>
                    <path d="M2 17L12 22L22 17"></path>
                    <path d="M2 12L12 17L22 12"></path>
                  </svg>
                </div>
                <h3>开源项目</h3>
                <p>欢迎访问我们的开源项目，参与贡献和讨论</p>
                <a href="https://gitee.com/Yangshengzhou/leaf-pan" target="_blank" class="contact-link">查看源码</a>
              </div>
            </div>
          </section>

          <section class="contact-section fade-in-up">
            <div class="section-header">
              <h2>留言反馈</h2>
              <div class="divider"></div>
            </div>
            <div class="feedback-form">
              <form @submit.prevent="submitFeedback">
                <div class="form-group">
                  <label for="name">姓名</label>
                  <input type="text" id="name" v-model="feedback.name" placeholder="请输入您的姓名" required>
                </div>
                <div class="form-group">
                  <label for="email">电子邮箱</label>
                  <input type="email" id="email" v-model="feedback.email" placeholder="请输入您的电子邮箱" required>
                </div>
                <div class="form-group">
                  <label for="subject">主题</label>
                  <select id="subject" v-model="feedback.subject" required>
                    <option value="" disabled selected>请选择主题</option>
                    <option value="feature">功能建议</option>
                    <option value="bug">问题反馈</option>
                    <option value="cooperation">合作咨询</option>
                    <option value="other">其他</option>
                  </select>
                </div>
                <div class="form-group">
                  <label for="message">留言内容</label>
                  <textarea id="message" v-model="feedback.message" rows="6" placeholder="请输入您的留言内容" required></textarea>
                </div>
                <div class="form-actions">
                  <button type="submit" class="submit-btn">提交反馈</button>
                </div>
              </form>
            </div>
          </section>

          <section class="contact-section fade-in-up">
            <div class="section-header">
              <h2>常见问题</h2>
              <div class="divider"></div>
            </div>
            <div class="faq-content">
              <div class="faq-item" v-for="(faq, index) in faqs" :key="index">
                <div class="faq-question" @click="toggleFaq(index)">
                  <h3>{{ faq.question }}</h3>
                  <div class="faq-toggle" :class="{ active: faq.open }">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <polyline points="6 9 12 15 18 9"></polyline>
                    </svg>
                  </div>
                </div>
                <div class="faq-answer" v-show="faq.open">
                  <p>{{ faq.answer }}</p>
                </div>
              </div>
            </div>
          </section>

          <section class="contact-section fade-in-up">
            <div class="section-header">
              <h2>工作时间</h2>
              <div class="divider"></div>
            </div>
            <div class="working-hours">
              <div class="hours-grid">
                <div class="hours-item">
                  <div class="day">周一至周五</div>
                  <div class="time">9:00 - 18:00</div>
                </div>
                <div class="hours-item">
                  <div class="day">周六</div>
                  <div class="time">10:00 - 16:00</div>
                </div>
                <div class="hours-item">
                  <div class="day">周日</div>
                  <div class="time">休息</div>
                </div>
                <div class="hours-item">
                  <div class="day">节假日</div>
                  <div class="time">可能延迟回复</div>
                </div>
              </div>
              <div class="hours-note">
                <p>我们会在工作时间内尽快回复您的消息，感谢您的理解与支持。</p>
              </div>
            </div>
          </section>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';

export default {
  name: 'ContactUsPage',
  setup() {
    const feedback = reactive({
      name: '',
      email: '',
      subject: '',
      message: ''
    });

    const faqs = ref([
      {
        question: '枫叶网盘是否免费使用？',
        answer: '枫叶网盘提供免费基础版和高级版服务。免费版提供基本的文件存储和管理功能，高级版提供更多存储空间和高级功能。',
        open: false
      },
      {
        question: '如何重置我的账户密码？',
        answer: '在登录页面点击"忘记密码"链接，输入您的注册邮箱，系统会发送重置密码的链接到您的邮箱，按照邮件提示操作即可重置密码。',
        open: false
      },
      {
        question: '枫叶网盘支持哪些文件格式？',
        answer: '枫叶网盘支持几乎所有常见的文件格式，包括文档、图片、音频、视频、压缩包等。但出于安全考虑，某些可执行文件类型可能会被限制。',
        open: false
      },
      {
        question: '如何提高文件上传速度？',
        answer: '您可以通过使用稳定的网络连接、使用支持断点续传的浏览器、避免同时上传多个大文件等方式提高上传速度。',
        open: false
      }
    ]);

    const toggleFaq = (index) => {
      faqs.value[index].open = !faqs.value[index].open;
    };

    const submitFeedback = () => {
      // 这里应该是实际的表单提交逻辑
      ElMessage.success('感谢您的反馈！我们会尽快处理并回复您。');
      
      // 重置表单
      feedback.name = '';
      feedback.email = '';
      feedback.subject = '';
      feedback.message = '';
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
      feedback,
      faqs,
      toggleFaq,
      submitFeedback
    };
  }
}
</script>

<style scoped>
.contact-us-page {
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

.contact-sections {
  display: flex;
  flex-direction: column;
  gap: 60px;
}

.contact-section {
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

.contact-methods {
  padding: 40px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
}

.contact-method {
  background: #f8fafc;
  border-radius: 12px;
  padding: 30px;
  text-align: center;
  transition: transform 0.3s, box-shadow 0.3s;
}

.contact-method:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
}

.contact-icon {
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

.contact-method h3 {
  font-size: 20px;
  margin-bottom: 15px;
  color: #2c3e50;
  font-weight: 600;
}

.contact-method p {
  color: #4a5568;
  line-height: 1.6;
  margin-bottom: 20px;
}

.contact-link {
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

.contact-link:hover {
  background: #409EFF;
  color: white;
}

.feedback-form {
  padding: 40px;
}

.form-group {
  margin-bottom: 25px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #2c3e50;
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 12px 15px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  font-size: 16px;
  transition: border-color 0.3s;
  background: #f8fafc;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #409EFF;
  background: white;
}

.form-group textarea {
  resize: vertical;
  min-height: 120px;
}

.form-actions {
  text-align: center;
  margin-top: 30px;
}

.submit-btn {
  background: linear-gradient(135deg, #409EFF 0%, #67c23a 100%);
  color: white;
  border: none;
  padding: 12px 30px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 8px;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(64, 158, 255, 0.3);
}

.faq-content {
  padding: 40px;
}

.faq-item {
  margin-bottom: 20px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  overflow: hidden;
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
}

.faq-toggle.active {
  transform: rotate(180deg);
}

.faq-answer {
  padding: 0 20px 20px;
  color: #4a5568;
  line-height: 1.6;
}

.working-hours {
  padding: 40px;
}

.hours-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.hours-item {
  background: #f8fafc;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
  border-left: 3px solid #409EFF;
}

.day {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 10px;
}

.time {
  color: #409EFF;
  font-weight: 500;
}

.hours-note {
  text-align: center;
  color: #4a5568;
  line-height: 1.6;
  font-style: italic;
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
  
  .contact-methods,
  .feedback-form,
  .faq-content,
  .working-hours {
    padding: 25px 20px;
  }
  
  .contact-methods {
    grid-template-columns: 1fr;
  }
  
  .hours-grid {
    grid-template-columns: 1fr;
  }
}
</style>