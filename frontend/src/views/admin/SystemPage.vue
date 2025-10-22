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
        <el-tabs v-model="activeTab">
          <!-- 基本设置 -->
          <el-tab-pane label="基本设置" name="basic">
            <el-form :model="basicSettings" label-width="120px">
              <el-form-item label="系统名称">
                <el-input v-model="basicSettings.systemName" />
              </el-form-item>
              <el-form-item label="系统版本">
                <el-input v-model="basicSettings.systemVersion" disabled />
              </el-form-item>
              <el-form-item label="系统描述">
                <el-input type="textarea" v-model="basicSettings.systemDescription" :rows="3" />
              </el-form-item>
              <el-form-item label="管理员邮箱">
                <el-input v-model="basicSettings.adminEmail" />
              </el-form-item>
              <el-form-item label="系统Logo">
                <el-upload
                  class="logo-uploader"
                  action="#"
                  :show-file-list="false"
                  :before-upload="beforeLogoUpload"
                  :http-request="uploadLogo"
                >
                  <img v-if="basicSettings.systemLogo" :src="basicSettings.systemLogo" class="logo" />
                  <el-icon v-else class="logo-uploader-icon"><Plus /></el-icon>
                </el-upload>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          
          <!-- 存储设置 -->
          <el-tab-pane label="存储设置" name="storage">
            <el-form :model="storageSettings" label-width="120px">
              <el-form-item label="存储类型">
                <el-radio-group v-model="storageSettings.storageType">
                  <el-radio label="local">本地存储</el-radio>
                  <el-radio label="minio">MinIO对象存储</el-radio>
                </el-radio-group>
              </el-form-item>
              
              <!-- 本地存储设置 -->
              <div v-if="storageSettings.storageType === 'local'">
                <el-form-item label="存储路径">
                  <el-input v-model="storageSettings.localPath" />
                </el-form-item>
                <el-form-item label="最大存储空间">
                  <el-input-number v-model="storageSettings.maxStorage" :min="1" :max="10000" />
                  <span style="margin-left: 10px">GB</span>
                </el-form-item>
              </div>
              
              <!-- MinIO设置 -->
              <div v-if="storageSettings.storageType === 'minio'">
                <el-form-item label="服务地址">
                  <el-input v-model="storageSettings.minioEndpoint" />
                </el-form-item>
                <el-form-item label="访问端口">
                  <el-input-number v-model="storageSettings.minioPort" :min="1" :max="65535" />
                </el-form-item>
                <el-form-item label="Access Key">
                  <el-input v-model="storageSettings.minioAccessKey" />
                </el-form-item>
                <el-form-item label="Secret Key">
                  <el-input v-model="storageSettings.minioSecretKey" type="password" show-password />
                </el-form-item>
                <el-form-item label="存储桶名称">
                  <el-input v-model="storageSettings.minioBucket" />
                </el-form-item>
                <el-form-item label="使用HTTPS">
                  <el-switch v-model="storageSettings.minioUseSSL" />
                </el-form-item>
              </div>
              
              <el-form-item label="用户默认配额">
                <el-input-number v-model="storageSettings.defaultUserQuota" :min="1" :max="1000" />
                <span style="margin-left: 10px">GB</span>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          
          <!-- 安全设置 -->
          <el-tab-pane label="安全设置" name="security">
            <el-form :model="securitySettings" label-width="120px">
              <el-form-item label="密码最小长度">
                <el-input-number v-model="securitySettings.minPasswordLength" :min="6" :max="20" />
              </el-form-item>
              <el-form-item label="密码复杂度">
                <el-checkbox-group v-model="securitySettings.passwordComplexity">
                  <el-checkbox label="uppercase">必须包含大写字母</el-checkbox>
                  <el-checkbox label="lowercase">必须包含小写字母</el-checkbox>
                  <el-checkbox label="numbers">必须包含数字</el-checkbox>
                  <el-checkbox label="symbols">必须包含特殊字符</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
              <el-form-item label="登录失败锁定">
                <el-switch v-model="securitySettings.loginLockEnabled" />
              </el-form-item>
              <el-form-item label="最大失败次数" v-if="securitySettings.loginLockEnabled">
                <el-input-number v-model="securitySettings.maxLoginAttempts" :min="3" :max="10" />
              </el-form-item>
              <el-form-item label="锁定时间(分钟)" v-if="securitySettings.loginLockEnabled">
                <el-input-number v-model="securitySettings.lockDuration" :min="5" :max="1440" />
              </el-form-item>
              <el-form-item label="会话超时(小时)">
                <el-input-number v-model="securitySettings.sessionTimeout" :min="1" :max="168" />
              </el-form-item>
              <el-form-item label="启用双因素认证">
                <el-switch v-model="securitySettings.twoFactorEnabled" />
              </el-form-item>
            </el-form>
          </el-tab-pane>
          
          <!-- 邮件设置 -->
          <el-tab-pane label="邮件设置" name="email">
            <el-form :model="emailSettings" label-width="120px">
              <el-form-item label="启用邮件服务">
                <el-switch v-model="emailSettings.enabled" />
              </el-form-item>
              <template v-if="emailSettings.enabled">
                <el-form-item label="SMTP服务器">
                  <el-input v-model="emailSettings.smtpHost" />
                </el-form-item>
                <el-form-item label="SMTP端口">
                  <el-input-number v-model="emailSettings.smtpPort" :min="1" :max="65535" />
                </el-form-item>
                <el-form-item label="用户名">
                  <el-input v-model="emailSettings.username" />
                </el-form-item>
                <el-form-item label="密码">
                  <el-input v-model="emailSettings.password" type="password" show-password />
                </el-form-item>
                <el-form-item label="发件人邮箱">
                  <el-input v-model="emailSettings.fromEmail" />
                </el-form-item>
                <el-form-item label="发件人名称">
                  <el-input v-model="emailSettings.fromName" />
                </el-form-item>
                <el-form-item label="启用SSL/TLS">
                  <el-switch v-model="emailSettings.useSSL" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="testEmail" :loading="testingEmail">
                    测试邮件发送
                  </el-button>
                </el-form-item>
              </template>
            </el-form>
          </el-tab-pane>
          
          <!-- 系统维护 -->
          <el-tab-pane label="系统维护" name="maintenance">
            <div class="maintenance-section">
              <el-card class="maintenance-card">
                <template #header>
                  <div class="card-header">
                    <span>数据备份</span>
                  </div>
                </template>
                <div class="backup-content">
                  <p>上次备份时间: {{ lastBackupTime || '从未备份' }}</p>
                  <el-button type="primary" @click="createBackup" :loading="backingUp">
                    立即备份
                  </el-button>
                  <el-button @click="scheduleBackup">
                    定时备份设置
                  </el-button>
                </div>
              </el-card>
              
              <el-card class="maintenance-card">
                <template #header>
                  <div class="card-header">
                    <span>系统清理</span>
                  </div>
                </template>
                <div class="cleanup-content">
                  <el-form label-width="120px">
                    <el-form-item label="清理临时文件">
                      <el-button @click="cleanTempFiles" :loading="cleaningTemp">
                        清理
                      </el-button>
                    </el-form-item>
                    <el-form-item label="清理日志文件">
                      <el-button @click="cleanLogFiles" :loading="cleaningLogs">
                        清理
                      </el-button>
                    </el-form-item>
                    <el-form-item label="清理回收站">
                      <el-button @click="cleanTrash" :loading="cleaningTrash">
                        清理
                      </el-button>
                    </el-form-item>
                  </el-form>
                </div>
              </el-card>
              
              <el-card class="maintenance-card">
                <template #header>
                  <div class="card-header">
                    <span>系统信息</span>
                  </div>
                </template>
                <div class="system-info">
                  <div class="info-item">
                    <span class="info-label">系统版本:</span>
                    <span class="info-value">LeafPan v1.0.0</span>
                  </div>
                  <div class="info-item">
                    <span class="info-label">Java版本:</span>
                    <span class="info-value">17.0.2</span>
                  </div>
                  <div class="info-item">
                    <span class="info-label">Spring Boot版本:</span>
                    <span class="info-value">3.0.5</span>
                  </div>
                  <div class="info-item">
                    <span class="info-label">运行时间:</span>
                    <span class="info-value">{{ systemUptime }}</span>
                  </div>
                  <div class="info-item">
                    <span class="info-label">服务器时间:</span>
                    <span class="info-value">{{ serverTime }}</span>
                  </div>
                </div>
              </el-card>
            </div>
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
    // 这里应该调用后端API获取系统设置
    // 暂时使用模拟数据
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 模拟数据已在上面初始化
    lastBackupTime.value = '2023-10-15 03:30:00'
    systemUptime.value = '15天 8小时 32分钟'
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
    // 这里应该调用后端API保存系统设置
    await new Promise(resolve => setTimeout(resolve, 1000))
    
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
    // 这里应该调用后端API上传Logo
    // 暂时使用模拟数据
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    // 模拟上传成功，返回一个临时URL
    basicSettings.systemLogo = URL.createObjectURL(options.file)
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
    // 这里应该调用后端API测试邮件发送
    await new Promise(resolve => setTimeout(resolve, 2000))
    
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
    // 这里应该调用后端API创建备份
    await new Promise(resolve => setTimeout(resolve, 3000))
    
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
    // 这里应该调用后端API清理临时文件
    await new Promise(resolve => setTimeout(resolve, 2000))
    
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
    // 这里应该调用后端API清理日志文件
    await new Promise(resolve => setTimeout(resolve, 2000))
    
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
    // 这里应该调用后端API清理回收站
    await new Promise(resolve => setTimeout(resolve, 2000))
    
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