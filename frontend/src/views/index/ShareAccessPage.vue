<template>
  <div class="share-access-container">
    <div class="share-card" v-if="loading">
      <el-skeleton :rows="6" animated />
    </div>
    
    <div class="share-card" v-else-if="error">
      <div class="error-container">
        <el-icon class="error-icon"><WarningFilled /></el-icon>
        <h2>分享链接异常</h2>
        <p>{{ errorMessage }}</p>
        <el-button type="primary" @click="$router.push('/')">返回首页</el-button>
      </div>
    </div>
    
    <div class="share-card" v-else-if="needPassword && !passwordVerified">
      <div class="password-container">
        <el-icon class="lock-icon"><Lock /></el-icon>
        <h2>分享已加密</h2>
        <p>该分享链接已设置密码保护，请输入密码访问</p>
        <el-form @submit.prevent="verifyPassword">
          <el-form-item label="密码" prop="password" required>
            <el-input 
              v-model="password" 
              type="password" 
              placeholder="请输入分享密码"
              @keyup.enter="verifyPassword"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="verifying" @click="verifyPassword">
              验证密码
            </el-button>
          </el-form-item>
        </el-form>
        <div v-if="passwordError" class="password-error">
          {{ passwordError }}
        </div>
      </div>
    </div>
    
    <div class="share-card" v-else>
      <div class="share-header">
        <h2>文件分享</h2>
        <div class="share-meta">
          <span>分享时间：{{ formatDate(shareInfo?.createdTime) }}</span>
          <span v-if="shareInfo?.expireTime">过期时间：{{ formatDate(shareInfo?.expireTime) }}</span>
        </div>
      </div>
      
      <div class="file-info" v-if="shareInfo?.type === 'file' && shareInfo?.file">
        <div class="file-icon">
          <el-icon class="big-icon"><Document /></el-icon>
        </div>
        <div class="file-details">
          <h3>{{ shareInfo.file.name }}</h3>
          <p class="file-size">文件大小：{{ formatFileSize(shareInfo.file.size) }}</p>
          <p class="file-meta">
            预览次数：{{ shareInfo.viewCount || 0 }} | 
            下载次数：{{ shareInfo.downloadCount || 0 }}
          </p>
        </div>
      </div>
      
      <div class="action-buttons">
        <el-button 
          type="primary" 
          size="large" 
          @click="downloadFile" 
          :loading="downloading"
        >
          <el-icon><Download /></el-icon>
          下载文件
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue';
import { useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import { Lock, Document, Download, WarningFilled } from '@element-plus/icons-vue';
import Server from '@/utils/Server.js';
import { formatFileSize } from '@/utils/utils.js';

export default {
  name: 'ShareAccessPage',
  components: {
    Lock,
    Document,
    Download,
    WarningFilled
  },
  setup() {
    const route = useRoute();
    const shareCode = computed(() => route.params.shareCode);
    
    const loading = ref(true);
    const verifying = ref(false);
    const downloading = ref(false);
    const error = ref(false);
    const errorMessage = ref('');
    const shareInfo = ref(null);
    const needPassword = ref(false);
    const passwordVerified = ref(false);
    const password = ref('');
    const passwordError = ref('');
    
    // 格式化日期
    const formatDate = (dateString) => {
      if (!dateString) return '-';
      const date = new Date(dateString);
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      });
    };
    
    // 获取分享信息
    const fetchShareInfo = async () => {
      try {
        loading.value = true;
        error.value = false;
        const response = await Server.get(`/public/share/${shareCode.value}`);
        
        if (response.data) {
          shareInfo.value = response.data;
          needPassword.value = response.data.needPassword;
          
          if (!needPassword.value) {
            passwordVerified.value = true;
          }
        } else {
          error.value = true;
          errorMessage.value = response?.message || '获取分享信息失败';
        }
      } catch (err) {
        error.value = true;
        if (err.response) {
          errorMessage.value = err.response.data?.message || '分享链接不存在或已过期';
        } else {
          errorMessage.value = '网络错误，请稍后重试';
        }
      } finally {
        loading.value = false;
      }
    };
    
    // 验证密码
    const verifyPassword = async () => {
      if (!password.value.trim()) {
        passwordError.value = '请输入密码';
        return;
      }
      
      try {
        verifying.value = true;
        passwordError.value = '';
        
        const response = await Server.post(`/public/share/${shareCode.value}/verify`, {
          password: password.value.trim()
        });
        
        if (response.data) {
          passwordVerified.value = true;
          ElMessage.success('密码验证成功');
        } else {
          passwordError.value = response?.message || '密码错误';
        }
      } catch (err) {
        passwordError.value = err.response?.data?.message || '密码验证失败，请重试';
      } finally {
        verifying.value = false;
      }
    };
    
    // 下载文件
    const downloadFile = async () => {
      if (!passwordVerified.value) {
        ElMessage.warning('请先验证密码');
        return;
      }
      
      try {
        downloading.value = true;
        
        // 构建下载URL
        let downloadUrl = `/api/public/share/${shareCode.value}/download`;
        
        // 如果需要密码，添加密码参数
        if (needPassword.value) {
          downloadUrl += `?password=${encodeURIComponent(password.value.trim())}`;
        }
        
        // 创建隐藏的下载链接
        const link = document.createElement('a');
        link.href = downloadUrl;
        link.target = '_blank';
        link.download = shareInfo.value?.file?.name || 'download';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        
        ElMessage.success('开始下载文件');
      } catch (err) {
        ElMessage.error('下载失败，请稍后重试');
      } finally {
        downloading.value = false;
      }
    };
    
    onMounted(() => {
      if (shareCode.value) {
        fetchShareInfo();
      } else {
        error.value = true;
        errorMessage.value = '分享码无效';
        loading.value = false;
      }
    });
    
    return {
      loading,
      verifying,
      downloading,
      error,
      errorMessage,
      shareInfo,
      needPassword,
      passwordVerified,
      password,
      passwordError,
      formatDate,
      formatFileSize,
      verifyPassword,
      downloadFile
    };
  }
};
</script>

<style scoped>
.share-access-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.share-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 500px;
  padding: 30px;
  animation: fadeIn 0.5s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.share-header h2 {
  margin: 0 0 20px 0;
  color: #303133;
  font-size: 24px;
  text-align: center;
}

.share-meta {
  display: flex;
  flex-direction: column;
  gap: 8px;
  color: #606266;
  font-size: 14px;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}

.file-info {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
  margin-bottom: 30px;
}

.file-icon .big-icon {
  font-size: 48px;
  color: #409eff;
}

.file-details h3 {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 18px;
  word-break: break-all;
}

.file-size, .file-meta {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.action-buttons {
  text-align: center;
}

.action-buttons .el-button {
  width: 100%;
  max-width: 300px;
}

/* 错误页面样式 */
.error-container {
  text-align: center;
  padding: 20px;
}

.error-icon {
  font-size: 64px;
  color: #f56c6c;
  margin-bottom: 20px;
}

.error-container h2 {
  margin: 0 0 15px 0;
  color: #f56c6c;
  font-size: 24px;
}

.error-container p {
  margin: 0 0 30px 0;
  color: #606266;
}

/* 密码验证页面样式 */
.password-container {
  text-align: center;
}

.lock-icon {
  font-size: 64px;
  color: #409eff;
  margin-bottom: 20px;
}

.password-container h2 {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 24px;
}

.password-container p {
  margin: 0 0 30px 0;
  color: #606266;
  font-size: 14px;
}

.password-container .el-form {
  margin-bottom: 15px;
}

.password-container .el-form-item {
  margin-bottom: 20px;
}

.password-error {
  color: #f56c6c;
  font-size: 14px;
  margin-top: 10px;
}

@media (max-width: 768px) {
  .share-card {
    padding: 20px;
  }
  
  .file-info {
    flex-direction: column;
    text-align: center;
    gap: 15px;
  }
  
  .file-details h3 {
    font-size: 16px;
  }
}
</style>