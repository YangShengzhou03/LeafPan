<template>
  <div class="dashboard-page">
    <div class="dashboard-header">
      <h1>仪表盘</h1>
      <p>欢迎回来，{{ currentUser?.username || '用户' }}！这里是您的存储使用情况概览。</p>
    </div>

    <!-- 存储空间卡片 -->
    <div class="storage-card">
      <div class="storage-card-header">
        <h2>存储空间</h2>
        <el-button type="primary" size="small" @click="upgradeStorage">升级存储</el-button>
      </div>
      <div class="storage-info">
        <div class="storage-chart">
          <div class="circular-progress">
            <svg viewBox="0 0 100 100">
              <circle cx="50" cy="50" r="45" fill="none" stroke="#e9ecef" stroke-width="10"></circle>
              <circle 
                cx="50" 
                cy="50" 
                r="45" 
                fill="none" 
                stroke="#409EFF" 
                stroke-width="10" 
                stroke-dasharray="283" 
                stroke-dashoffset="283"
                stroke-linecap="round"
                transform="rotate(-90 50 50)"
              ></circle>
              <text x="50" y="50" text-anchor="middle" dy="0.3em" font-size="20" font-weight="bold" fill="#303133">
                {{ storagePercentage }}%
              </text>
            </svg>
          </div>
          <div class="storage-details">
            <div class="storage-item">
              <span class="label">已使用</span>
              <span class="value">{{ formatFileSize(usedStorageBytes) }}</span>
            </div>
            <div class="storage-item">
              <span class="label">总容量</span>
              <span class="value">{{ formatFileSize(totalStorageBytes) }}</span>
            </div>
            <div class="storage-item">
              <span class="label">可用空间</span>
              <span class="value">{{ formatFileSize(availableStorageBytes) }}</span>
            </div>
          </div>
        </div>
        <div class="storage-breakdown">
          <div class="breakdown-item" v-for="item in storageBreakdown" :key="item.type">
            <div class="breakdown-color" :style="{ backgroundColor: item.color }"></div>
            <div class="breakdown-info">
              <span class="breakdown-label">{{ item.label }}</span>
              <span class="breakdown-value">{{ formatFileSize(item.size) }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 文件统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card" v-for="stat in fileStats" :key="stat.type">
        <div class="stat-icon" :style="{ backgroundColor: stat.color }">
          <i :class="stat.icon"></i>
        </div>
        <div class="stat-content">
          <h3>{{ stat.count }}</h3>
          <p>{{ stat.label }}</p>
        </div>
      </div>
    </div>

    <!-- 最近活动 -->
    <div class="recent-activity">
      <div class="activity-header">
        <h2>最近活动</h2>
        <el-button type="text" @click="viewAllActivity">查看全部</el-button>
      </div>
      <div class="activity-list">
        <div class="activity-item" v-for="activity in recentActivities" :key="activity.id">
          <div class="activity-icon" :style="{ backgroundColor: getActivityColor(activity.type) }">
            <i :class="getActivityIcon(activity.type)"></i>
          </div>
          <div class="activity-content">
            <p class="activity-text">{{ activity.description }}</p>
            <p class="activity-time">{{ formatTime(activity.timestamp) }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import store from '@/utils/store.js'
import mockApiService from '@/utils/mockApiService.js'
import { formatFileSize, formatTime } from '@/utils/api.js'

const router = useRouter()

// 用户信息
const currentUser = computed(() => {
  return store.state.user
})

// 存储信息
const totalStorageGB = ref(5) // 默认5GB
const usedStorageGB = ref(1.2) // 已使用1.2GB

// 计算存储百分比
const storagePercentage = computed(() => {
  return Math.min(100, Math.round((usedStorageGB.value / totalStorageGB.value) * 100))
})

// 计算圆形进度条偏移量
const dashOffset = computed(() => {
  const circumference = 2 * Math.PI * 45
  return circumference - (storagePercentage.value / 100) * circumference
})

// 计算存储字节数
const totalStorageBytes = computed(() => {
  return totalStorageGB.value * 1024 * 1024 * 1024
})

const usedStorageBytes = computed(() => {
  return usedStorageGB.value * 1024 * 1024 * 1024
})

const availableStorageBytes = computed(() => {
  return totalStorageBytes.value - usedStorageBytes.value
})

// 存储分类统计
const storageBreakdown = ref([
  { type: 'documents', label: '文档', size: 450 * 1024 * 1024, color: '#409EFF' },
  { type: 'images', label: '图片', size: 320 * 1024 * 1024, color: '#67C23A' },
  { type: 'videos', label: '视频', size: 280 * 1024 * 1024, color: '#E6A23C' },
  { type: 'others', label: '其他', size: 150 * 1024 * 1024, color: '#909399' }
])

// 文件统计
const fileStats = ref([
  { type: 'files', label: '文件总数', count: 128, icon: 'icon-files', color: '#409EFF' },
  { type: 'folders', label: '文件夹', count: 24, icon: 'icon-folder', color: '#67C23A' },
  { type: 'shared', label: '共享文件', count: 12, icon: 'icon-share', color: '#E6A23C' },
  { type: 'favorites', label: '收藏文件', count: 8, icon: 'icon-star', color: '#F56C6C' }
])

// 最近活动
const recentActivities = ref([
  { id: 1, type: 'upload', description: '上传了文件 "项目报告.pdf"', timestamp: new Date(Date.now() - 3600000) },
  { id: 2, type: 'share', description: '分享了文件夹 "设计稿" 给 3 位用户', timestamp: new Date(Date.now() - 7200000) },
  { id: 3, type: 'delete', description: '删除了文件 "旧版本.docx"', timestamp: new Date(Date.now() - 86400000) },
  { id: 4, type: 'create', description: '创建了文件夹 "会议记录"', timestamp: new Date(Date.now() - 172800000) },
  { id: 5, type: 'download', description: '下载了文件 "产品图片.zip"', timestamp: new Date(Date.now() - 259200000) }
])

// 获取活动图标
const getActivityIcon = (type) => {
  const icons = {
    upload: 'icon-upload',
    download: 'icon-download',
    share: 'icon-share',
    delete: 'icon-delete',
    create: 'icon-folder-add'
  }
  return icons[type] || 'icon-file'
}

// 获取活动颜色
const getActivityColor = (type) => {
  const colors = {
    upload: '#409EFF',
    download: '#67C23A',
    share: '#E6A23C',
    delete: '#F56C6C',
    create: '#909399'
  }
  return colors[type] || '#909399'
}

// 升级存储
const upgradeStorage = () => {
  router.push('/user/settings')
}

// 查看所有活动
const viewAllActivity = () => {
  // 这里可以跳转到活动日志页面
  console.log('查看所有活动')
}

// 获取仪表盘数据
const fetchDashboardData = async () => {
  try {
    const dashboardData = await mockApiService.getDashboardStats()
    
    // 更新存储信息
    if (dashboardData.storage) {
      totalStorageGB.value = dashboardData.storage.total / (1024 * 1024 * 1024)
      usedStorageGB.value = dashboardData.storage.used / (1024 * 1024 * 1024)
    }
    
    // 更新文件统计
    if (dashboardData.fileStats) {
      fileStats.value = dashboardData.fileStats
    }
    
    // 更新存储分类
    if (dashboardData.storageBreakdown) {
      storageBreakdown.value = dashboardData.storageBreakdown
    }
    
    // 更新最近活动
    if (dashboardData.recentActivities) {
      recentActivities.value = dashboardData.recentActivities
    }
  } catch (error) {
    console.error('获取仪表盘数据失败:', error)
  }
}

// 页面加载时获取数据
onMounted(() => {
  fetchDashboardData()
})
</script>

<style scoped>
.dashboard-page {
  max-width: 1200px;
  margin: 0 auto;
}

.dashboard-header {
  margin-bottom: 30px;
}

.dashboard-header h1 {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 10px 0;
}

.dashboard-header p {
  color: #606266;
  margin: 0;
  font-size: 16px;
}

/* 存储卡片 */
.storage-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  padding: 20px;
  margin-bottom: 20px;
}

.storage-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.storage-card-header h2 {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.storage-info {
  display: flex;
  gap: 30px;
}

.storage-chart {
  display: flex;
  align-items: center;
  gap: 30px;
}

.circular-progress {
  width: 120px;
  height: 120px;
}

.storage-details {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.storage-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  min-width: 180px;
}

.storage-item .label {
  color: #606266;
  font-size: 14px;
}

.storage-item .value {
  color: #303133;
  font-weight: 600;
  font-size: 14px;
}

.storage-breakdown {
  flex: 1;
}

.breakdown-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.breakdown-color {
  width: 12px;
  height: 12px;
  border-radius: 2px;
  margin-right: 10px;
}

.breakdown-info {
  display: flex;
  justify-content: space-between;
  flex: 1;
}

.breakdown-label {
  color: #606266;
  font-size: 14px;
}

.breakdown-value {
  color: #303133;
  font-weight: 500;
  font-size: 14px;
}

/* 统计网格 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  padding: 20px;
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
}

.stat-icon i {
  color: #fff;
  font-size: 24px;
}

.stat-content h3 {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 5px 0;
}

.stat-content p {
  color: #606266;
  margin: 0;
  font-size: 14px;
}

/* 最近活动 */
.recent-activity {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  padding: 20px;
}

.activity-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.activity-header h2 {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.activity-item {
  display: flex;
  align-items: flex-start;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.activity-item:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.activity-icon {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  flex-shrink: 0;
}

.activity-icon i {
  color: #fff;
  font-size: 16px;
}

.activity-content {
  flex: 1;
}

.activity-text {
  color: #303133;
  margin: 0 0 5px 0;
  font-size: 14px;
}

.activity-time {
  color: #909399;
  margin: 0;
  font-size: 12px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .dashboard-header h1 {
    font-size: 24px;
  }
  
  .storage-info {
    flex-direction: column;
    gap: 20px;
  }
  
  .storage-chart {
    justify-content: center;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .stat-card {
    padding: 15px;
  }
  
  .stat-icon {
    width: 40px;
    height: 40px;
  }
  
  .stat-icon i {
    font-size: 20px;
  }
  
  .stat-content h3 {
    font-size: 20px;
  }
}
</style>