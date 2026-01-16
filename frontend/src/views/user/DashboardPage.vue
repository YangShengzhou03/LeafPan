<template>
  <div class="dashboard-page">
    <!-- 存储空间卡片 -->
    <div class="storage-card">
      <div class="storage-card-header">
        <h2>存储空间</h2>
      </div>
      <div class="storage-info">
        <div class="storage-chart">
          <div class="circular-progress">
            <svg viewBox="0 0 100 100">
              <circle cx="50" cy="50" r="45" fill="none" stroke="#e9ecef" stroke-width="10"></circle>
              <circle cx="50" cy="50" r="45" fill="none" stroke="#409EFF" stroke-width="10" stroke-dasharray="283"
                :stroke-dashoffset="dashOffset" stroke-linecap="round" transform="rotate(-90 50 50)"></circle>
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
      </div>
    </div>

    <!-- 文件统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card" v-for="stat in fileStats" :key="stat.type">
        <div class="stat-content">
          <h3>{{ stat.count }}</h3>
          <p>{{ stat.label }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { formatFileSize } from '@/utils/utils.js'
import Server from '@/utils/Server.js'

// 存储信息
const totalStorageGB = ref(1) // 默认1GB，与数据库中的默认值一致
const usedStorageGB = ref(0) // 已使用0GB，初始值
const loading = ref(false)

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

// 文件统计
const fileStats = ref([
  { type: 'files', label: '文件总数', count: 0 },
  { type: 'folders', label: '文件夹', count: 0 },
  { type: 'shared', label: '共享文件', count: 0 },
  { type: 'favorites', label: '收藏文件', count: 0 }
])

// 获取存储信息
const fetchStorageInfo = async () => {
  try {
    loading.value = true
    const response = await Server.get('/file/storage/usage')
    
    if (response.data && response.data.data) {
      const data = response.data.data
      // 后端返回的字段是quota和totalSize，单位是字节
      totalStorageGB.value = (data.quota || 1073741824) / (1024 * 1024 * 1024) // 转换为GB，默认1GB
      usedStorageGB.value = (data.totalSize || 0) / (1024 * 1024 * 1024) // 转换为GB
    } else if (response.data) {
      // 如果数据直接位于response.data中（没有嵌套data字段）
      const data = response.data
      totalStorageGB.value = (data.quota || 1073741824) / (1024 * 1024 * 1024) // 转换为GB，默认1GB
      usedStorageGB.value = (data.totalSize || 0) / (1024 * 1024 * 1024) // 转换为GB
    }
  } catch (error) {
    // 设置默认值
    totalStorageGB.value = 1
    usedStorageGB.value = 0
  } finally {
    loading.value = false
  }
}

// 获取文件统计
const fetchFileStats = async () => {
  try {
    const [filesResponse, foldersResponse, sharesResponse, favoritesResponse] = await Promise.all([
      Server.get('/file/list/page', { params: { page: 0, size: 1 } }),
      Server.get('/folder/list'),
      Server.get('/share/user'),
      Server.get('/favorite')
    ])
    
    const fileCount = filesResponse.data?.totalElements || 0
    const folderCount = foldersResponse.data?.data?.length || 0
    const sharedCount = sharesResponse.data?.length || 0
    const favoriteCount = favoritesResponse.data?.length || 0
    
    fileStats.value = [
      { type: 'files', label: '文件总数', count: fileCount },
      { type: 'folders', label: '文件夹', count: folderCount },
      { type: 'shared', label: '共享文件', count: sharedCount },
      { type: 'favorites', label: '收藏文件', count: favoriteCount }
    ]
  } catch (error) {
    fileStats.value = [
      { type: 'files', label: '文件总数', count: 0 },
      { type: 'folders', label: '文件夹', count: 0 },
      { type: 'shared', label: '共享文件', count: 0 },
      { type: 'favorites', label: '收藏文件', count: 0 }
    ]
  }
}

// 页面加载时获取数据
onMounted(async () => {
  await Promise.all([
    fetchStorageInfo(),
    fetchFileStats()
  ])
})
</script>

<style scoped>
.dashboard-page {
  padding: 0;
  background-color: transparent;
  min-height: 100%;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.storage-card {
  background: #fff;
  border-radius: 6px;
  padding: 18px;
  margin-bottom: 16px;
  border: 1px solid #e8eaed;
}

.storage-card-header {
  margin-bottom: 18px;
}

.storage-card-header h2 {
  font-size: 16px;
  font-weight: 600;
  color: #202124;
  margin: 0;
}

.storage-info {
  display: flex;
  align-items: center;
  justify-content: center;
}

.storage-chart {
  display: flex;
  align-items: center;
  gap: 28px;
}

.circular-progress {
  width: 100px;
  height: 100px;
}

.storage-details {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.storage-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  min-width: 160px;
  padding: 10px 12px;
  background: #f8f9fa;
  border-radius: 6px;
  border: 1px solid #e8eaed;
}

.storage-item .label {
  color: #5f6368;
  font-size: 14px;
  font-weight: 500;
}

.storage-item .value {
  color: #202124;
  font-weight: 600;
  font-size: 15px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 10px;
}

.stat-card {
  background: #fff;
  border-radius: 6px;
  padding: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #e8eaed;
  text-align: center;
}

.stat-content h3 {
  font-size: 24px;
  font-weight: 700;
  color: #1a73e8;
  margin: 0 0 6px 0;
}

.stat-content p {
  color: #5f6368;
  margin: 0;
  font-size: 14px;
  font-weight: 500;
}

@media (max-width: 768px) {
  .dashboard-page {
    padding: 0;
  }

  .storage-info {
    flex-direction: column;
    gap: 18px;
  }

  .storage-chart {
    justify-content: center;
  }

  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .storage-card {
    padding: 14px;
  }

  .storage-chart {
    gap: 18px;
  }

  .circular-progress {
    width: 90px;
    height: 90px;
  }

  .storage-item {
    min-width: 140px;
    padding: 8px 10px;
  }

  .storage-item .label {
    font-size: 13px;
  }

  .storage-item .value {
    font-size: 14px;
  }

  .stats-grid {
    grid-template-columns: 1fr;
  }

  .stat-card {
    padding: 14px;
  }

  .stat-content h3 {
    font-size: 20px;
  }

  .stat-content p {
    font-size: 13px;
  }
}

@media (max-width: 320px) {
  .storage-card {
    padding: 12px;
  }

  .circular-progress {
    width: 80px;
    height: 80px;
  }

  .storage-item {
    min-width: 120px;
  }
}
</style>