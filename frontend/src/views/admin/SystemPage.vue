<template>
  <div class="admin-system">
    <el-card class="system-card">
      <template #header>
        <div class="card-header">
          <span>系统设置</span>
          <el-button type="primary" @click="saveSettings" :loading="saving">
            保存设置
          </el-button>
        </div>
      </template>
      
      <div class="system-content">
        <el-tabs v-model="activeTab" type="card">
          <el-tab-pane label="基本设置" name="basic">
            <el-card shadow="never" class="setting-card">
              <div class="placeholder-content">
                <el-icon size="48" color="#909399"><Plus /></el-icon>
                <p>基本设置功能开发中...</p>
              </div>
            </el-card>
          </el-tab-pane>
          
          <el-tab-pane label="存储设置" name="storage">
            <el-card shadow="never" class="setting-card">
              <div class="placeholder-content">
                <el-icon size="48" color="#909399"><Plus /></el-icon>
                <p>存储设置功能开发中...</p>
              </div>
            </el-card>
          </el-tab-pane>
          
          <el-tab-pane label="安全设置" name="security">
            <el-card shadow="never" class="setting-card">
              <div class="placeholder-content">
                <el-icon size="48" color="#909399"><Plus /></el-icon>
                <p>安全设置功能开发中...</p>
              </div>
            </el-card>
          </el-tab-pane>
          
          <el-tab-pane label="邮件设置" name="email">
            <el-card shadow="never" class="setting-card">
              <div class="placeholder-content">
                <el-icon size="48" color="#909399"><Plus /></el-icon>
                <p>邮件设置功能开发中...</p>
              </div>
            </el-card>
          </el-tab-pane>
          
          <el-tab-pane label="系统维护" name="maintenance">
            <el-card shadow="never" class="setting-card">
              <div class="placeholder-content">
                <el-icon size="48" color="#909399"><Plus /></el-icon>
                <p>系统维护功能开发中...</p>
              </div>
            </el-card>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { configAPI } from '@/utils/api.js'

// 当前选中的标签页
const activeTab = ref('basic')
const saving = ref(false)
const testingEmail = ref(false)
const backingUp = ref(false)
const cleaningTemp = ref(false)
const cleaningLogs = ref(false)
const cleaningTrash = ref(false)
const lastBackupTime = ref('')
const systemUptime = ref('')
const serverTime = ref('')

// 基本设置
const basicSettings = reactive({
  systemName: 'LeafPan 枫叶网盘',
  systemVersion: 'v1.0.0',
  systemDescription: '基于Spring Boot和Vue的现代化网盘系统',
  adminEmail: 'admin@leafpan.com',
  systemLogo: ''
})

// 存储设置
const storageSettings = reactive({
  storageType: 'local',
  localPath: '/data/files',
  maxStorage: 1000,
  minioEndpoint: '',
  minioPort: 9000,
  minioAccessKey: '',
  minioSecretKey: '',
  minioBucket: 'leafpan',
  minioUseSSL: false,
  defaultUserQuota: 10
})

// 安全设置
const securitySettings = reactive({
  minPasswordLength: 6,
  passwordComplexity: ['lowercase', 'numbers'],
  loginLockEnabled: true,
  maxLoginAttempts: 5,
  lockDuration: 30,
  sessionTimeout: 24,
  twoFactorEnabled: false
})

// 邮件设置
const emailSettings = reactive({
  enabled: false,
  smtpHost: '',
  smtpPort: 587,
  username: '',
  password: '',
  fromEmail: '',
  fromName: 'LeafPan',
  useSSL: true
})

// 加载系统设置
const loadSettings = async () => {
  try {
    // 调用后端API获取系统设置
    const response = await configAPI.getSystemConfig()
    
    // 更新基本设置
    Object.assign(basicSettings, response.basic || {})
    
    // 更新存储设置
    Object.assign(storageSettings, response.storage || {})
    
    // 更新安全设置
    Object.assign(securitySettings, response.security || {})
    
    // 更新邮件设置
    Object.assign(emailSettings, response.email || {})
    
    // 更新系统信息
    lastBackupTime.value = response.lastBackupTime || ''
    systemUptime.value = response.systemUptime || ''
    updateServerTime()
    
    // 每秒更新服务器时间
    setInterval(updateServerTime, 1000)
  } catch (error) {
    console.error('加载系统设置失败:', error)
    ElMessage.error('加载系统设置失败')
  }
}

// 更新服务器时间
const updateServerTime = () => {
  serverTime.value = new Date().toLocaleString()
}

// 保存设置
const saveSettings = async () => {
  saving.value = true
  try {
    // 调用后端API保存系统设置
    const settings = {
      basic: basicSettings,
      storage: storageSettings,
      security: securitySettings,
      email: emailSettings
    }
    
    await configAPI.updateSystemConfig(settings)
    
    ElMessage.success('系统设置保存成功')
  } catch (error) {
    console.error('保存系统设置失败:', error)
    ElMessage.error('保存系统设置失败')
  } finally {
    saving.value = false
  }
}

// Logo上传前的验证
const beforeLogoUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('上传Logo只能是 JPG/PNG 格式!')
  }
  if (!isLt2M) {
    ElMessage.error('上传Logo大小不能超过 2MB!')
  }
  return isJPG && isLt2M
}

// 上传Logo
const uploadLogo = async (options) => {
  try {
    // 调用后端API上传Logo
    const formData = new FormData()
    formData.append('file', options.file)
    
    const response = await configAPI.uploadLogo(formData)
    basicSettings.systemLogo = response.logoUrl
    ElMessage.success('Logo上传成功')
  } catch (error) {
    console.error('Logo上传失败:', error)
    ElMessage.error('Logo上传失败')
  }
}

// 测试邮件发送
const testEmail = async () => {
  testingEmail.value = true
  try {
    // 调用后端API测试邮件发送
    await configAPI.testEmail(emailSettings)
    ElMessage.success('测试邮件发送成功，请检查收件箱')
  } catch (error) {
    console.error('测试邮件发送失败:', error)
    ElMessage.error('测试邮件发送失败')
  } finally {
    testingEmail.value = false
  }
}

// 创建备份
const createBackup = async () => {
  backingUp.value = true
  try {
    // 调用后端API创建备份
    await configAPI.createBackup()
    
    const now = new Date()
    lastBackupTime.value = now.toLocaleString()
    ElMessage.success('数据备份创建成功')
  } catch (error) {
    console.error('创建备份失败:', error)
    ElMessage.error('创建备份失败')
  } finally {
    backingUp.value = false
  }
}

// 定时备份设置
const scheduleBackup = () => {
  ElMessage.info('定时备份设置功能开发中...')
}

// 清理临时文件
const cleanTempFiles = async () => {
  cleaningTemp.value = true
  try {
    // 调用后端API清理临时文件
    await configAPI.cleanTempFiles()
    ElMessage.success('临时文件清理完成')
  } catch (error) {
    console.error('清理临时文件失败:', error)
    ElMessage.error('清理临时文件失败')
  } finally {
    cleaningTemp.value = false
  }
}

// 清理日志文件
const cleanLogFiles = async () => {
  cleaningLogs.value = true
  try {
    // 调用后端API清理日志文件
    await configAPI.cleanLogFiles()
    ElMessage.success('日志文件清理完成')
  } catch (error) {
    console.error('清理日志文件失败:', error)
    ElMessage.error('清理日志文件失败')
  } finally {
    cleaningLogs.value = false
  }
}

// 清理回收站
const cleanTrash = async () => {
  cleaningTrash.value = true
  try {
    // 调用后端API清理回收站
    await configAPI.cleanTrash()
    ElMessage.success('回收站清理完成')
  } catch (error) {
    console.error('清理回收站失败:', error)
    ElMessage.error('清理回收站失败')
  } finally {
    cleaningTrash.value = false
  }
}

onMounted(() => {
  loadSettings()
})
</script>

<style scoped>
.admin-system {
  padding: 20px;
}

.system-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.system-content {
  margin-top: 20px;
}

.setting-card {
  margin-top: 20px;
  min-height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.placeholder-content {
  text-align: center;
  color: #909399;
}

.placeholder-content p {
  margin-top: 16px;
  font-size: 16px;
}

.maintenance-section {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.maintenance-card {
  flex: 1;
  min-width: 300px;
}

.backup-content,
.cleanup-content {
  padding: 10px 0;
}

.system-info {
  padding: 10px 0;
}

.info-item {
  margin-bottom: 15px;
  display: flex;
}

.info-label {
  width: 120px;
  color: #909399;
}

.info-value {
  color: #303133;
  font-weight: 500;
}

.logo-uploader .logo {
  width: 100px;
  height: 100px;
  display: block;
}

.logo-uploader :deep(.el-upload) {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.logo-uploader :deep(.el-upload:hover) {
  border-color: var(--el-color-primary);
}

.logo-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  text-align: center;
  line-height: 100px;
}
</style>