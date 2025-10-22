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
import { ref, computed } from 'vue'
import { formatFileSize } from '@/utils/utils.js'

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

// 文件统计
const fileStats = ref([
  { type: 'files', label: '文件总数', count: 128 },
  { type: 'folders', label: '文件夹', count: 24 },
  { type: 'shared', label: '共享文件', count: 12 },
  { type: 'favorites', label: '收藏文件', count: 8 }
])
</script>

<style scoped>
.dashboard-page {
  padding: 24px;
  background-color: #f8fafc;
  min-height: 100vh;
  font-family: 'Inter', 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
}

/* 存储卡片 */
.storage-card {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 24px;
  margin-bottom: 24px;
  border: 1px solid #e9ecef;
  transition: all 0.3s ease;
}

.storage-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.storage-card-header {
  margin-bottom: 24px;
}

.storage-card-header h2 {
  font-size: 20px;
  font-weight: 600;
  color: #1e293b;
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
  gap: 40px;
}

.circular-progress {
  width: 120px;
  height: 120px;
}

.storage-details {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.storage-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  min-width: 180px;
  padding: 12px 16px;
  background: #f8fafc;
  border-radius: 8px;
  border: 1px solid #e9ecef;
  transition: all 0.2s ease;
}

.storage-item:hover {
  background: #f1f5f9;
  border-color: #cbd5e1;
}

.storage-item .label {
  color: #64748b;
  font-size: 14px;
  font-weight: 500;
}

.storage-item .value {
  color: #1e293b;
  font-weight: 600;
  font-size: 16px;
}

/* 统计网格 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 16px;
}

.stat-card {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #e9ecef;
  transition: all 0.3s ease;
  text-align: center;
}

.stat-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.stat-content h3 {
  font-size: 28px;
  font-weight: 700;
  color: #409EFF;
  margin: 0 0 8px 0;
}

.stat-content p {
  color: #64748b;
  margin: 0;
  font-size: 14px;
  font-weight: 500;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .dashboard-page {
    padding: 16px;
  }

  .storage-info {
    flex-direction: column;
    gap: 24px;
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
    padding: 20px;
  }

  .circular-progress {
    width: 100px;
    height: 100px;
  }

  .storage-item {
    min-width: 140px;
    padding: 10px 12px;
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

  .stat-content h3 {
    font-size: 24px;
  }

  .stat-content p {
    font-size: 13px;
  }
}
</style>