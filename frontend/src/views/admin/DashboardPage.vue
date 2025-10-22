<template>
  <div class="admin-dashboard">
    <el-card class="dashboard-card">
      <template #header>
        <div class="card-header">
          <span>管理员仪表盘</span>
        </div>
      </template>
      
      <div class="dashboard-content">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card shadow="hover" class="stat-card">
              <div class="stat-item">
                <div class="stat-icon">
                  <el-icon size="24"><User /></el-icon>
                </div>
                <div class="stat-content">
                  <div class="stat-title">用户总数</div>
                  <div class="stat-value">{{ stats.userCount }}</div>
                </div>
              </div>
            </el-card>
          </el-col>
          
          <el-col :span="6">
            <el-card shadow="hover" class="stat-card">
              <div class="stat-item">
                <div class="stat-icon">
                  <el-icon size="24"><Folder /></el-icon>
                </div>
                <div class="stat-content">
                  <div class="stat-title">文件总数</div>
                  <div class="stat-value">{{ stats.fileCount }}</div>
                </div>
              </div>
            </el-card>
          </el-col>
          
          <el-col :span="6">
            <el-card shadow="hover" class="stat-card">
              <div class="stat-item">
                <div class="stat-icon">
                  <el-icon size="24"><Document /></el-icon>
                </div>
                <div class="stat-content">
                  <div class="stat-title">存储使用</div>
                  <div class="stat-value">{{ formatStorage(stats.usedStorage) }}</div>
                </div>
              </div>
            </el-card>
          </el-col>
          
          <el-col :span="6">
            <el-card shadow="hover" class="stat-card">
              <div class="stat-item">
                <div class="stat-icon">
                  <el-icon size="24"><Share /></el-icon>
                </div>
                <div class="stat-content">
                  <div class="stat-title">分享链接</div>
                  <div class="stat-value">{{ stats.shareCount }}</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" style="margin-top: 20px">
          <el-col :span="12">
            <el-card shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>最近注册用户</span>
                </div>
              </template>
              <el-table :data="recentUsers" style="width: 100%">
                <el-table-column prop="username" label="用户名" />
                <el-table-column prop="email" label="邮箱" />
                <el-table-column prop="createdAt" label="注册时间" />
              </el-table>
            </el-card>
          </el-col>
          
          <el-col :span="12">
            <el-card shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>系统信息</span>
                </div>
              </template>
              <div class="system-info">
                <div class="info-item">
                  <span class="info-label">系统版本：</span>
                  <span class="info-value">LeafPan v1.0.0</span>
                </div>
                <div class="info-item">
                  <span class="info-label">运行时间：</span>
                  <span class="info-value">{{ uptime }}</span>
                </div>
                <div class="info-item">
                  <span class="info-label">存储状态：</span>
                  <span class="info-value">正常</span>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { User, Folder, Document, Share } from '@element-plus/icons-vue'

// 统计数据
const stats = ref({
  userCount: 0,
  fileCount: 0,
  usedStorage: 0,
  shareCount: 0
})

// 最近注册用户
const recentUsers = ref([])

// 系统运行时间
const uptime = ref('')

// 格式化存储大小
const formatStorage = (sizeInGB) => {
  if (sizeInGB < 1) {
    return `${(sizeInGB * 1024).toFixed(2)} MB`
  }
  return `${sizeInGB.toFixed(2)} GB`
}

// 加载仪表盘数据
const loadDashboardData = async () => {
  try {
    // 这里应该调用后端API获取真实数据
    // 暂时使用模拟数据
    stats.value = {
      userCount: 128,
      fileCount: 3542,
      usedStorage: 156.78,
      shareCount: 89
    }
    
    recentUsers.value = [
      { username: 'user001', email: 'user001@example.com', createdAt: '2023-10-15' },
      { username: 'user002', email: 'user002@example.com', createdAt: '2023-10-14' },
      { username: 'user003', email: 'user003@example.com', createdAt: '2023-10-13' }
    ]
    
    uptime.value = '15天 8小时 32分钟'
  } catch (error) {
    console.error('加载仪表盘数据失败:', error)
  }
}

onMounted(() => {
  loadDashboardData()
})
</script>

<style scoped>
.admin-dashboard {
  padding: 20px;
}

.dashboard-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.stat-card {
  height: 100%;
}

.stat-item {
  display: flex;
  align-items: center;
}

.stat-icon {
  margin-right: 15px;
  color: #409EFF;
}

.stat-content {
  flex: 1;
}

.stat-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.system-info {
  padding: 10px 0;
}

.info-item {
  margin-bottom: 15px;
  display: flex;
}

.info-label {
  width: 100px;
  color: #909399;
}

.info-value {
  color: #303133;
  font-weight: 500;
}
</style>