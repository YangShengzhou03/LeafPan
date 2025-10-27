<template>
  <div class="user-guide-page">
    <header class="page-header">
      <div class="container">
        <h1>使用指南</h1>
        <p>了解如何充分利用枫叶网盘的功能</p>
      </div>
    </header>

    <main class="page-content">
      <div class="container">
        <div class="guide-sections">
          <section class="guide-section">
            <h2>快速入门</h2>
            <div class="guide-steps">
              <div class="step">
                <div class="step-number">1</div>
                <div class="step-content">
                  <h3>注册账户</h3>
                  <p>点击首页的"注册"按钮，填写基本信息完成账户注册</p>
                </div>
              </div>
              <div class="step">
                <div class="step-number">2</div>
                <div class="step-content">
                  <h3>登录系统</h3>
                  <p>使用注册的账户信息登录枫叶网盘</p>
                </div>
              </div>
              <div class="step">
                <div class="step-number">3</div>
                <div class="step-content">
                  <h3>上传文件</h3>
                  <p>在文件管理页面点击"上传"按钮，选择要上传的文件</p>
                </div>
              </div>
              <div class="step">
                <div class="step-number">4</div>
                <div class="step-content">
                  <h3>管理文件</h3>
                  <p>使用拖拽、右键菜单等功能管理您的文件</p>
                </div>
              </div>
            </div>
          </section>

          <section class="guide-section">
            <h2>文件管理</h2>
            <div class="feature-grid">
              <div class="feature-item">
                <h3>📁 文件上传</h3>
                <p>支持单个文件上传和批量上传，最大支持150MB的文件</p>
              </div>
              <div class="feature-item">
                <h3>📂 文件夹管理</h3>
                <p>创建文件夹，分类管理您的文件</p>
              </div>
              <div class="feature-item">
                <h3>🔍 文件搜索</h3>
                <p>通过文件名快速搜索您需要的文件</p>
              </div>
              <div class="feature-item">
                <h3>📋 文件操作</h3>
                <p>支持复制、移动、重命名、删除等操作</p>
              </div>
            </div>
          </section>

          <section class="guide-section">
            <h2>文件分享</h2>
            <div class="feature-grid">
              <div class="feature-item">
                <h3>🔗 创建分享链接</h3>
                <p>为文件生成分享链接，设置有效期和访问密码</p>
              </div>
              <div class="feature-item">
                <h3>👥 权限管理</h3>
                <p>控制分享文件的访问权限和下载权限</p>
              </div>
              <div class="feature-item">
                <h3>📊 分享统计</h3>
                <p>查看分享链接的访问次数和下载次数</p>
              </div>
            </div>
          </section>

          <section class="guide-section">
            <h2>常见问题解答</h2>
            <div class="faq-list">
              <div class="faq-item">
                <h3>Q: 免费用户有哪些限制？</h3>
                <p>A: 免费用户享有1GB存储空间，支持基础文件管理功能，下载速度不受限制。</p>
              </div>
              <div class="faq-item">
                <h3>Q: 如何升级到付费版本？</h3>
                <p>A: 登录后在设置页面选择"升级套餐"，选择适合您的付费方案。</p>
              </div>
              <div class="faq-item">
                <h3>Q: 文件安全性如何保障？</h3>
                <p>A: 我们采用三重加密技术，确保您的文件在传输和存储过程中的安全。</p>
              </div>
            </div>
          </section>
        </div>
      </div>
    </main>

    <footer class="page-footer">
      <div class="container">
        <p>需要更多帮助？<router-link to="/contact-us">联系我们</router-link></p>
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'UserGuidePage',
  setup() {
    return {};
  }
}
</script>

<style scoped>
.user-guide-page {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 60px 0;
  text-align: center;
}

.page-header h1 {
  font-size: 48px;
  margin-bottom: 20px;
}

.page-header p {
  font-size: 18px;
  opacity: 0.9;
}

.page-content {
  padding: 60px 0;
}

.guide-section {
  background: white;
  border-radius: 8px;
  padding: 40px;
  margin-bottom: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.guide-section h2 {
  color: #303133;
  font-size: 28px;
  margin-bottom: 30px;
  border-bottom: 2px solid #409EFF;
  padding-bottom: 10px;
}

.guide-steps {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.step {
  display: flex;
  align-items: flex-start;
  gap: 20px;
}

.step-number {
  background: #409EFF;
  color: white;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: bold;
  flex-shrink: 0;
}

.step-content h3 {
  color: #303133;
  font-size: 20px;
  margin-bottom: 10px;
}

.step-content p {
  color: #606266;
  line-height: 1.6;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
}

.feature-item {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  border-left: 4px solid #409EFF;
}

.feature-item h3 {
  color: #303133;
  font-size: 18px;
  margin-bottom: 10px;
}

.feature-item p {
  color: #606266;
  line-height: 1.5;
}

.faq-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.faq-item {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
}

.faq-item h3 {
  color: #303133;
  font-size: 16px;
  margin-bottom: 10px;
}

.faq-item p {
  color: #606266;
  line-height: 1.5;
}

.page-footer {
  background: #303133;
  color: white;
  padding: 30px 0;
  text-align: center;
}

.page-footer a {
  color: #409EFF;
  text-decoration: none;
}

.page-footer a:hover {
  text-decoration: underline;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

@media (max-width: 768px) {
  .page-header h1 {
    font-size: 36px;
  }
  
  .guide-section {
    padding: 20px;
  }
  
  .feature-grid {
    grid-template-columns: 1fr;
  }
  
  .step {
    flex-direction: column;
    text-align: center;
  }
}
</style>