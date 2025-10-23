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
        <el-form :model="settings" label-width="120px" ref="settingsForm">
          <el-divider content-position="left">存储设置</el-divider>
          
          <el-form-item label="默认存储配额">
            <el-input-number 
              v-model="settings.defaultStorageQuota" 
              :min="1" 
              :max="10240" 
              :step="1"
              controls-position="right"
            />
            <span style="margin-left: 8px; color: #909399;">GB</span>
            <div class="form-tip">新用户注册时的默认存储空间大小</div>
          </el-form-item>
          
          <el-form-item label="最大存储配额">
            <el-input-number 
              v-model="settings.maxStorageQuota" 
              :min="10" 
              :max="102400" 
              :step="10"
              controls-position="right"
            />
            <span style="margin-left: 8px; color: #909399;">GB</span>
            <div class="form-tip">单个用户可设置的最大存储空间</div>
          </el-form-item>
          
          <el-divider content-position="left">安全设置</el-divider>
          
          <el-form-item label="密码强度要求">
            <el-select v-model="settings.passwordStrength" placeholder="请选择密码强度要求">
              <el-option label="低（6位以上）" value="low" />
              <el-option label="中（8位以上，包含字母和数字）" value="medium" />
              <el-option label="高（10位以上，包含大小写字母、数字和特殊字符）" value="high" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="会话超时时间">
            <el-input-number 
              v-model="settings.sessionTimeout" 
              :min="15" 
              :max="480" 
              :step="15"
              controls-position="right"
            />
            <span style="margin-left: 8px; color: #909399;">分钟</span>
            <div class="form-tip">用户无操作后自动登出的时间</div>
          </el-form-item>
          
          <el-form-item label="允许的文件类型">
            <el-select v-model="settings.allowedFileTypes" multiple placeholder="请选择允许的文件类型">
              <el-option label="图片（jpg, png, gif, webp）" value="image" />
              <el-option label="文档（pdf, doc, docx, xls, xlsx, ppt, pptx）" value="document" />
              <el-option label="压缩文件（zip, rar, 7z）" value="archive" />
              <el-option label="音频（mp3, wav, flac）" value="audio" />
              <el-option label="视频（mp4, avi, mov）" value="video" />
              <el-option label="其他（所有类型）" value="other" />
            </el-select>
          </el-form-item>
          
          <el-divider content-position="left">系统维护</el-divider>
          
          <el-form-item label="自动备份">
            <el-switch v-model="settings.autoBackup" />
            <div class="form-tip">是否启用系统自动备份功能</div>
          </el-form-item>
          
          <el-form-item label="备份保留天数">
            <el-input-number 
              v-model="settings.backupRetentionDays" 
              :min="1" 
              :max="365" 
              :step="1"
              controls-position="right"
              :disabled="!settings.autoBackup"
            />
            <span style="margin-left: 8px; color: #909399;">天</span>
          </el-form-item>
          
          <el-form-item label="日志保留天数">
            <el-input-number 
              v-model="settings.logRetentionDays" 
              :min="7" 
              :max="365" 
              :step="7"
              controls-position="right"
            />
            <span style="margin-left: 8px; color: #909399;">天</span>
            <div class="form-tip">操作日志保留时间</div>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    
    <!-- 系统操作卡片 -->
    <el-card class="system-actions-card" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <span>系统操作</span>
        </div>
      </template>
      
      <div class="system-actions">
        <el-button type="warning" @click="createBackup">
          <el-icon><DocumentCopy /></el-icon>
          创建系统备份
        </el-button>
        
        <el-button type="info" @click="cleanTempFiles">
          <el-icon><Delete /></el-icon>
          清理临时文件
        </el-button>
        
        <el-button type="info" @click="cleanLogFiles">
          <el-icon><Document /></el-icon>
          清理过期日志
        </el-button>
        
        <el-button type="danger" @click="cleanTrash">
          <el-icon><DeleteFilled /></el-icon>
          清理回收站
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, DocumentCopy, Delete, Document, DeleteFilled } from '@element-plus/icons-vue'
import Server from '@/utils/Server.js'

// 系统设置数据
const settings = reactive({
  defaultStorageQuota: 1,
  maxStorageQuota: 100,
  passwordStrength: 'medium',
  sessionTimeout: 60,
  allowedFileTypes: ['image', 'document', 'archive', 'audio', 'video'],
  autoBackup: true,
  backupRetentionDays: 30,
  logRetentionDays: 90
})

// 保存状态
const saving = ref(false)
const settingsForm = ref()

// 加载系统设置
const loadSettings = async () => {
  try {
    // 这里应该调用后端API获取系统设置
    // const response = await Server.get('/admin/system/settings')
    // Object.assign(settings, response.data)
    
    // 暂时使用默认设置
    ElMessage.info('系统设置功能开发中，当前使用默认设置')
  } catch (error) {
    console.error('加载系统设置失败:', error)
    ElMessage.error('加载系统设置失败')
  }
}

// 保存系统设置
const saveSettings = async () => {
  try {
    saving.value = true
    
    // 这里应该调用后端API保存系统设置
    // await Server.post('/admin/system/settings', settings)
    
    ElMessage.success('系统设置已保存')
  } catch (error) {
    console.error('保存系统设置失败:', error)
    ElMessage.error('保存系统设置失败')
  } finally {
    saving.value = false
  }
}

// 创建系统备份
const createBackup = async () => {
  try {
    await ElMessageBox.confirm('确定要创建系统备份吗？', '系统备份', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await Server.post('/admin/backup')
    ElMessage.success(response.data.message || '系统备份创建成功')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('创建系统备份失败:', error)
      ElMessage.error('创建系统备份失败')
    }
  }
}

// 清理临时文件
const cleanTempFiles = async () => {
  try {
    await ElMessageBox.confirm('确定要清理临时文件吗？', '清理临时文件', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await Server.post('/admin/clean-temp')
    ElMessage.success(response.data.message || '临时文件清理完成')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('清理临时文件失败:', error)
      ElMessage.error('清理临时文件失败')
    }
  }
}

// 清理过期日志
const cleanLogFiles = async () => {
  try {
    await ElMessageBox.confirm('确定要清理过期日志吗？', '清理日志', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await Server.post('/admin/clean-logs')
    ElMessage.success(response.data.message || '日志文件清理完成')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('清理日志文件失败:', error)
      ElMessage.error('清理日志文件失败')
    }
  }
}

// 清理回收站
const cleanTrash = async () => {
  try {
    await ElMessageBox.confirm('确定要清理所有用户的回收站吗？此操作不可恢复！', '清理回收站', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'error'
    })
    
    const response = await Server.post('/admin/clean-trash')
    ElMessage.success(response.data.message || '回收站清理完成')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('清理回收站失败:', error)
      ElMessage.error('清理回收站失败')
    }
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
  min-height: 400px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.system-content {
  padding: 20px 0;
}

.system-actions {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
  line-height: 1.4;
}

.el-divider {
  margin: 24px 0;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-select {
  width: 100%;
}
</style>