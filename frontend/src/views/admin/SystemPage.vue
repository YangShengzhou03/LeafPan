<template>
  <div class="admin-system">
    <el-card class="system-card">
      <template #header>
        <div class="card-header">
          <span>系统设置</span>
          <el-button type="primary" size="small" @click="handleInitialize" :loading="initializing">
            初始化配置
          </el-button>
        </div>
      </template>
      
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="存储设置" name="storage">
          <el-form :model="storageConfigs" label-width="200px" class="config-form">
            <el-form-item label="最大文件上传大小">
              <el-input-number
                v-model="storageConfigs.storage_max_file_size"
                :min="1"
                :max="10737418240"
                :step="1048576"
                :formatter="formatFileSize"
                :parser="parseFileSize"
                controls-position="right"
                style="width: 300px"
              />
            </el-form-item>
            <el-form-item label="最大总存储空间">
              <el-input-number
                v-model="storageConfigs.storage_max_total_size"
                :min="1"
                :max="1048576"
                :step="100"
                controls-position="right"
                style="width: 300px"
              />
              <span class="unit-text">MB</span>
            </el-form-item>
            <el-form-item label="允许上传的文件类型">
              <el-input
                v-model="storageConfigs.storage_allowed_file_types"
                type="textarea"
                :rows="3"
                placeholder="用逗号分隔，*表示无限制，如：jpg,jpeg,png,gif,pdf"
              />
            </el-form-item>
            <el-form-item label="新用户默认存储容量">
              <el-input-number
                v-model="storageConfigs.storage_default_user_quota"
                :min="1"
                :max="1048576"
                :step="100"
                controls-position="right"
                style="width: 300px"
              />
              <span class="unit-text">MB</span>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveStorageConfig" :loading="saving">
                保存存储设置
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="安全设置" name="security">
          <el-form :model="securityConfigs" label-width="200px" class="config-form">
            <el-form-item label="启用验证码">
              <el-switch v-model="securityConfigs.security_enable_captcha" />
            </el-form-item>
            <el-form-item label="最大登录尝试次数">
              <el-input-number
                v-model="securityConfigs.security_max_login_attempts"
                :min="1"
                :max="10"
                controls-position="right"
                style="width: 200px"
              />
            </el-form-item>
            <el-form-item label="登录锁定时间">
              <el-input-number
                v-model="securityConfigs.security_login_lock_time"
                :min="60"
                :max="86400"
                :step="60"
                controls-position="right"
                style="width: 200px"
              />
              <span class="unit-text">秒</span>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveSecurityConfig" :loading="saving">
                保存安全设置
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="系统设置" name="system">
          <el-form :model="systemConfigs" label-width="200px" class="config-form">
            <el-form-item label="网站名称">
              <el-input v-model="systemConfigs.system_site_name" style="width: 400px" />
            </el-form-item>
            <el-form-item label="网站描述">
              <el-input
                v-model="systemConfigs.system_site_description"
                type="textarea"
                :rows="3"
                style="width: 400px"
              />
            </el-form-item>
            <el-form-item label="开放注册">
              <el-switch v-model="systemConfigs.system_enable_registration" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveSystemConfig" :loading="saving">
                保存系统设置
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import Server from '@/utils/Server.js'

const activeTab = ref('storage')
const initializing = ref(false)
const saving = ref(false)

const storageConfigs = reactive({
  storage_max_file_size: 1073741824,
  storage_max_total_size: 1024,
  storage_allowed_file_types: '*',
  storage_default_user_quota: 1024
})

const securityConfigs = reactive({
  security_enable_captcha: true,
  security_max_login_attempts: 5,
  security_login_lock_time: 1800
})

const systemConfigs = reactive({
  system_site_name: 'LeafPan网盘',
  system_site_description: '安全、快速的云存储服务',
  system_enable_registration: true
})

const formatFileSize = (value) => {
  if (value >= 1073741824) {
    return (value / 1073741824).toFixed(2) + ' GB'
  } else if (value >= 1048576) {
    return (value / 1048576).toFixed(2) + ' MB'
  } else if (value >= 1024) {
    return (value / 1024).toFixed(2) + ' KB'
  }
  return value + ' B'
}

const parseFileSize = (value) => {
  if (typeof value === 'string') {
    const match = value.match(/^(\d+\.?\d*)\s*(GB|MB|KB|B)?$/i)
    if (match) {
      const num = parseFloat(match[1])
      const unit = (match[2] || 'B').toUpperCase()
      switch (unit) {
        case 'GB': return num * 1073741824
        case 'MB': return num * 1048576
        case 'KB': return num * 1024
        default: return num
      }
    }
  }
  return value
}

const loadConfigs = async () => {
  try {
    const response = await Server.get('/admin/config')
    if (response.code === 200 && response.data) {
      response.data.forEach(config => {
        const key = config.configKey
        const value = config.configValue
        
        if (Object.keys(storageConfigs).includes(key)) {
          storageConfigs[key] = parseConfigValue(value, config.configType)
        } else if (Object.keys(securityConfigs).includes(key)) {
          securityConfigs[key] = parseConfigValue(value, config.configType)
        } else if (Object.keys(systemConfigs).includes(key)) {
          systemConfigs[key] = parseConfigValue(value, config.configType)
        }
      })
    }
  } catch (error) {
  }
}

const parseConfigValue = (value, type) => {
  switch (type) {
    case 'integer':
    case 'long':
      return parseInt(value)
    case 'double':
      return parseFloat(value)
    case 'boolean':
      return value === 'true'
    default:
      return value
  }
}

const saveStorageConfig = async () => {
  saving.value = true
  try {
    await Promise.all([
      updateConfig('storage_max_file_size', storageConfigs.storage_max_file_size, 'long'),
      updateConfig('storage_max_total_size', storageConfigs.storage_max_total_size, 'long'),
      updateConfig('storage_allowed_file_types', storageConfigs.storage_allowed_file_types, 'string'),
      updateConfig('storage_default_user_quota', storageConfigs.storage_default_user_quota, 'long')
    ])
    ElMessage.success('存储设置保存成功')
  } catch (error) {
    ElMessage.error('保存失败: ' + error.message)
  } finally {
    saving.value = false
  }
}

const saveSecurityConfig = async () => {
  saving.value = true
  try {
    await Promise.all([
      updateConfig('security_enable_captcha', securityConfigs.security_enable_captcha, 'boolean'),
      updateConfig('security_max_login_attempts', securityConfigs.security_max_login_attempts, 'integer'),
      updateConfig('security_login_lock_time', securityConfigs.security_login_lock_time, 'integer')
    ])
    ElMessage.success('安全设置保存成功')
  } catch (error) {
    ElMessage.error('保存失败: ' + error.message)
  } finally {
    saving.value = false
  }
}

const saveSystemConfig = async () => {
  saving.value = true
  try {
    await Promise.all([
      updateConfig('system_site_name', systemConfigs.system_site_name, 'string'),
      updateConfig('system_site_description', systemConfigs.system_site_description, 'string'),
      updateConfig('system_enable_registration', systemConfigs.system_enable_registration, 'boolean')
    ])
    ElMessage.success('系统设置保存成功')
  } catch (error) {
    ElMessage.error('保存失败: ' + error.message)
  } finally {
    saving.value = false
  }
}

const updateConfig = async (key, value, type) => {
  const config = {
    configKey: key,
    configValue: String(value),
    configType: type
  }
  
  const existingConfig = await Server.get(`/admin/config/category/${key.split('_')[0]}`)
  if (existingConfig.code === 200 && existingConfig.data) {
    const targetConfig = existingConfig.data.find(c => c.configKey === key)
    if (targetConfig) {
      return await Server.put(`/admin/config/${targetConfig.id}`, config)
    }
  }
  
  return await Server.post('/admin/config', config)
}

const handleInitialize = async () => {
  initializing.value = true
  try {
    const response = await Server.post('/admin/config/initialize')
    if (response.code === 200) {
      ElMessage.success('配置初始化成功')
      await loadConfigs()
    }
  } catch (error) {
    ElMessage.error('初始化失败: ' + error.message)
  } finally {
    initializing.value = false
  }
}

onMounted(() => {
  loadConfigs()
})
</script>

<style scoped>
.admin-system {
  padding: 20px;
}

.system-card {
  min-height: 500px;
  border-radius: 4px;
  border: 1px solid #e0e0e0;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  background-color: #ffffff;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  font-size: 16px;
}

.config-form {
  max-width: 800px;
  padding: 24px 20px;
}

.unit-text {
  margin-left: 10px;
  color: #5f6368;
  font-weight: 500;
  font-size: 14px;
}

/* 表单样式优化 */
:deep(.el-form-item__label) {
  color: #202124;
  font-weight: 500;
  font-size: 14px;
}

:deep(.el-input__wrapper) {
  border-radius: 4px;
}

:deep(.el-input-number) {
  width: 100%;
}

:deep(.el-input-number .el-input__wrapper) {
  border-radius: 4px;
}

:deep(.el-textarea__inner) {
  border-radius: 4px;
}

:deep(.el-switch) {
  height: 24px;
}

:deep(.el-switch__core) {
  border-radius: 12px;
}

:deep(.el-switch.is-checked .el-switch__core) {
  background-color: #1a73e8;
}

/* 按钮样式优化 */
:deep(.el-button) {
  border-radius: 4px;
  font-weight: 500;
  padding: 10px 20px;
  font-size: 14px;
}

:deep(.el-button--primary) {
  background-color: #1a73e8;
  border-color: #1a73e8;
}

:deep(.el-button--primary:hover) {
  background-color: #1557b0;
  border-color: #1557b0;
}

:deep(.el-button--small) {
  padding: 8px 16px;
  font-size: 13px;
}

/* 标签页样式优化 */
:deep(.el-tabs--border-card) {
  border-radius: 4px;
  border: 1px solid #e0e0e0;
  box-shadow: none;
}

:deep(.el-tabs--border-card > .el-tabs__header) {
  background-color: #fafbfc;
  border-bottom: 1px solid #e0e0e0;
}

:deep(.el-tabs--border-card > .el-tabs__header .el-tabs__item) {
  color: #5f6368;
  font-weight: 500;
  font-size: 14px;
  border: 1px solid transparent;
}

:deep(.el-tabs--border-card > .el-tabs__header .el-tabs__item.is-active) {
  color: #1a73e8;
  background-color: #ffffff;
  border-right-color: #e0e0e0;
  border-left-color: #e0e0e0;
  border-bottom-color: #ffffff;
  font-weight: 600;
}

:deep(.el-tabs--border-card > .el-tabs__header .el-tabs__item:hover) {
  color: #1a73e8;
}

:deep(.el-tabs__content) {
  padding: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .admin-system {
    padding: 16px;
  }

  .system-card {
    min-height: 450px;
  }

  .card-header {
    font-size: 15px;
  }

  .config-form {
    max-width: 100%;
    padding: 20px 16px;
  }

  :deep(.el-form-item__label) {
    font-size: 13px;
  }

  :deep(.el-button) {
    padding: 8px 16px;
    font-size: 13px;
  }

  :deep(.el-button--small) {
    padding: 6px 12px;
    font-size: 12px;
  }
}

@media (max-width: 480px) {
  .admin-system {
    padding: 12px;
  }

  .system-card {
    min-height: 400px;
  }

  .card-header {
    font-size: 14px;
  }

  .config-form {
    padding: 16px 12px;
  }

  :deep(.el-form-item__label) {
    font-size: 12px;
  }

  :deep(.el-button) {
    padding: 7px 14px;
    font-size: 12px;
  }

  :deep(.el-button--small) {
    padding: 5px 10px;
    font-size: 11px;
  }

  .unit-text {
    font-size: 13px;
  }
}
</style>
