<template>
  <div class="config-demo">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>系统配置信息</span>
          <el-button type="primary" @click="refreshConfig">刷新配置</el-button>
        </div>
      </template>
      
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="5" animated />
      </div>
      
      <div v-else-if="error" class="error-container">
        <el-alert
          title="加载配置失败"
          :description="error"
          type="error"
          show-icon
          :closable="false"
        />
      </div>
      
      <div v-else class="config-content">
        <!-- 应用信息 -->
        <el-descriptions title="应用信息" :column="2" border>
          <el-descriptions-item label="应用名称">
            {{ config.app?.name || '未知' }}
          </el-descriptions-item>
          <el-descriptions-item label="应用版本">
            {{ config.app?.version || '未知' }}
          </el-descriptions-item>
          <el-descriptions-item label="API版本">
            {{ config.app?.apiVersion || '未知' }}
          </el-descriptions-item>
        </el-descriptions>
        
        <!-- 文件上传配置 -->
        <el-descriptions title="文件上传配置" :column="2" border class="mt-20">
          <el-descriptions-item label="最大文件大小">
            {{ config.file?.maxFileSize || '未知' }}
          </el-descriptions-item>
          <el-descriptions-item label="允许的文件类型">
            <el-tag
              v-for="(ext, index) in (config.file?.allowedExtensions || [])"
              :key="index"
              size="small"
              class="mr-5"
            >
              {{ ext }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
        
        <!-- MinIO配置 -->
        <el-descriptions title="存储配置" :column="2" border class="mt-20">
          <el-descriptions-item label="存储端点">
            {{ config.minio?.endpoint || '未知' }}
          </el-descriptions-item>
          <el-descriptions-item label="存储桶名称">
            {{ config.minio?.bucketName || '未知' }}
          </el-descriptions-item>
        </el-descriptions>
        
        <!-- 服务器配置 -->
        <el-descriptions title="服务器配置" :column="2" border class="mt-20">
          <el-descriptions-item label="服务器端口">
            {{ config.server?.port || '未知' }}
          </el-descriptions-item>
          <el-descriptions-item label="上下文路径">
            {{ config.server?.contextPath || '/' }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { configAPI } from '../utils/api'
import { getConfig } from '../utils/config'

export default {
  name: 'ConfigDemo',
  setup() {
    const loading = ref(false)
    const error = ref('')
    const config = ref({})
    
    // 加载配置
    const loadConfig = async () => {
      loading.value = true
      error.value = ''
      
      try {
        // 从配置管理工具获取配置
        config.value = getConfig()
        
        // 如果配置为空或需要刷新，则从API获取
        if (!config.value || Object.keys(config.value).length === 0) {
          const response = await configAPI.getSystemConfig()
          if (response && response.data) {
            config.value = response.data
          }
        }
      } catch (err) {
        console.error('加载配置失败:', err)
        error.value = err.message || '未知错误'
      } finally {
        loading.value = false
      }
    }
    
    // 刷新配置
    const refreshConfig = async () => {
      await loadConfig()
    }
    
    // 组件挂载时加载配置
    onMounted(() => {
      loadConfig()
    })
    
    return {
      loading,
      error,
      config,
      refreshConfig
    }
  }
}
</script>

<style scoped>
.config-demo {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.loading-container,
.error-container {
  padding: 20px 0;
}

.mt-20 {
  margin-top: 20px;
}

.mr-5 {
  margin-right: 5px;
}
</style>