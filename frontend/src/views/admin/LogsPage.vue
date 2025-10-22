<template>
  <div class="admin-logs">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-item">
            <div class="stat-icon error">
              <el-icon size="24"><Warning /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-title">错误日志</div>
              <div class="stat-value">{{ stats.errorCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-item">
            <div class="stat-icon warning">
              <el-icon size="24"><InfoFilled /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-title">警告日志</div>
              <div class="stat-value">{{ stats.warnCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-item">
            <div class="stat-icon info">
              <el-icon size="24"><Document /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-title">信息日志</div>
              <div class="stat-value">{{ stats.infoCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-item">
            <div class="stat-icon total">
              <el-icon size="24"><List /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-title">总日志数</div>
              <div class="stat-value">{{ stats.totalCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 筛选条件 -->
    <el-card class="logs-card filter-card">
      <template #header>
        <div class="filter-header">
          <span class="filter-title">筛选条件</span>
          <div class="filter-actions">
            <el-button type="primary" @click="handleFilter" :icon="Search">
              搜索
            </el-button>
            <el-button @click="resetFilter">
              重置
            </el-button>
          </div>
        </div>
      </template>
      
      <div class="filter-bar">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-select v-model="filter.level" placeholder="日志级别" clearable>
              <el-option label="全部" value="" />
              <el-option label="错误" value="ERROR" />
              <el-option label="警告" value="WARN" />
              <el-option label="信息" value="INFO" />
              <el-option label="调试" value="DEBUG" />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select v-model="filter.module" placeholder="模块" clearable>
              <el-option label="全部" value="" />
              <el-option label="用户管理" value="USER" />
              <el-option label="文件管理" value="FILE" />
              <el-option label="系统管理" value="SYSTEM" />
              <el-option label="认证授权" value="AUTH" />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-date-picker
              v-model="filter.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="YYYY-MM-DD"
              clearable
            />
          </el-col>
          <el-col :span="6">
            <el-input
              v-model="filter.keyword"
              placeholder="搜索关键词"
              clearable
              @keyup.enter="handleFilter"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <!-- 日志列表 -->
    <el-card>
      <template #header>
        <div class="card-header">
          <span>操作日志</span>
          <div class="header-actions">
            <el-button
              type="primary"
              :loading="exporting"
              @click="exportLogs"
            >
              <el-icon><Download /></el-icon>
              导出日志
            </el-button>
            <el-button
              type="danger"
              :loading="clearing"
              @click="clearLogs"
            >
              <el-icon><Delete /></el-icon>
              清空日志
            </el-button>
          </div>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="filteredLogs"
        style="width: 100%"
        stripe
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="level" label="级别" width="100">
          <template #default="{ row }">
            <el-tag :type="getLevelType(row.level)" size="small">
              {{ row.level }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="module" label="模块" width="120">
          <template #default="{ row }">
            {{ getModuleName(row.module) }}
          </template>
        </el-table-column>
        <el-table-column prop="message" label="消息" min-width="200" show-overflow-tooltip />
        <el-table-column prop="user" label="用户" width="120" />
        <el-table-column prop="ip" label="IP地址" width="130" />
        <el-table-column prop="createdAt" label="时间" width="160" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              @click="viewLogDetail(row)"
            >
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="totalLogs"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 日志详情对话框 -->
    <el-dialog
      v-model="showLogDetail"
      title="日志详情"
      width="600px"
    >
      <div v-if="selectedLog">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="ID">{{ selectedLog.id }}</el-descriptions-item>
          <el-descriptions-item label="级别">
            <el-tag :type="getLevelType(selectedLog.level)">
              {{ selectedLog.level }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="模块">{{ getModuleName(selectedLog.module) }}</el-descriptions-item>
          <el-descriptions-item label="用户">{{ selectedLog.user }}</el-descriptions-item>
          <el-descriptions-item label="IP地址">{{ selectedLog.ip }}</el-descriptions-item>
          <el-descriptions-item label="时间">{{ selectedLog.createdAt }}</el-descriptions-item>
          <el-descriptions-item label="消息">{{ selectedLog.message }}</el-descriptions-item>
        </el-descriptions>
        <el-divider />
        <h4>详细信息</h4>
        <div class="log-detail-content">{{ selectedLog.details }}</div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Download, Delete, Warning, InfoFilled, Document, List } from '@element-plus/icons-vue'
import { adminAPI } from '@/utils/api.js'

// 响应式数据
const loading = ref(false)
const exporting = ref(false)
const clearing = ref(false)
const logs = ref([])
const totalLogs = ref(0)
const currentPage = ref(1)
const pageSize = ref(20)
const showLogDetail = ref(false)
const selectedLog = ref(null)

// 统计数据
const stats = ref({
  errorCount: 0,
  warnCount: 0,
  infoCount: 0,
  totalCount: 0
})

// 筛选条件
const filter = reactive({
  level: '',
  module: '',
  dateRange: [],
  keyword: ''
})

// 过滤后的日志列表
const filteredLogs = computed(() => {
  let filtered = logs.value
  
  if (filter.level) {
    filtered = filtered.filter(log => log.level === filter.level)
  }
  
  if (filter.module) {
    filtered = filtered.filter(log => log.module === filter.module)
  }
  
  if (filter.dateRange && filter.dateRange.length === 2) {
    const [startDate, endDate] = filter.dateRange
    filtered = filtered.filter(log => {
      const logDate = log.createdAt.substring(0, 10)
      return logDate >= startDate && logDate <= endDate
    })
  }
  
  if (filter.keyword) {
    const keyword = filter.keyword.toLowerCase()
    filtered = filtered.filter(log => 
      log.message.toLowerCase().includes(keyword) ||
      log.user.toLowerCase().includes(keyword) ||
      log.ip.toLowerCase().includes(keyword) ||
      log.details.toLowerCase().includes(keyword)
    )
  }
  
  // 分页处理
  const startIndex = (currentPage.value - 1) * pageSize.value
  return filtered.slice(startIndex, startIndex + pageSize.value)
})

// 获取日志级别对应的标签类型
const getLevelType = (level) => {
  switch (level) {
    case 'ERROR': return 'danger'
    case 'WARN': return 'warning'
    case 'INFO': return 'primary'
    case 'DEBUG': return 'info'
    default: return ''
  }
}

// 获取模块名称
const getModuleName = (module) => {
  switch (module) {
    case 'USER': return '用户管理'
    case 'FILE': return '文件管理'
    case 'SYSTEM': return '系统管理'
    case 'AUTH': return '认证授权'
    default: return module
  }
}

// 重置筛选条件
const resetFilter = () => {
  filter.level = ''
  filter.module = ''
  filter.dateRange = []
  filter.keyword = ''
  currentPage.value = 1
  loadLogs()
}

// 更新统计数据
const updateStats = () => {
  const errorCount = logs.value.filter(log => log.level === 'ERROR').length
  const warnCount = logs.value.filter(log => log.level === 'WARN').length
  const infoCount = logs.value.filter(log => log.level === 'INFO').length
  
  stats.value = {
    errorCount,
    warnCount,
    infoCount,
    totalCount: logs.value.length
  }
}

// 加载日志数据
const loadLogs = async () => {
  loading.value = true
  try {
    const response = await adminAPI.getOperationLogs({
      page: currentPage.value,
      size: pageSize.value,
      level: filter.level,
      module: filter.module,
      startDate: filter.dateRange?.[0],
      endDate: filter.dateRange?.[1],
      keyword: filter.keyword
    })
    
    logs.value = response.data || []
    totalLogs.value = response.total || 0
    updateStats()
  } catch (error) {
    console.error('加载日志数据失败:', error)
    ElMessage.error('加载日志数据失败')
  } finally {
    loading.value = false
  }
}

// 筛选处理
const handleFilter = () => {
  currentPage.value = 1
  loadLogs()
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  loadLogs()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  loadLogs()
}

// 查看日志详情
const viewLogDetail = (log) => {
  selectedLog.value = log
  showLogDetail.value = true
}

// 导出日志
const exportLogs = async () => {
  exporting.value = true
  try {
    const response = await adminAPI.exportLogs({
      level: filter.level,
      module: filter.module,
      startDate: filter.dateRange?.[0],
      endDate: filter.dateRange?.[1],
      keyword: filter.keyword
    })
    
    // 创建下载链接
    const blob = new Blob([response], { type: 'application/json' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `logs_${new Date().toISOString().substring(0, 10)}.json`
    link.click()
    window.URL.revokeObjectURL(url)
    
    ElMessage.success('日志导出成功')
  } catch (error) {
    console.error('导出日志失败:', error)
    ElMessage.error('导出日志失败')
  } finally {
    exporting.value = false
  }
}

// 清空日志
const clearLogs = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要清空所有日志吗？此操作不可恢复！',
      '确认清空',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    clearing.value = true
    
    await adminAPI.clearLogs()
    
    logs.value = []
    totalLogs.value = 0
    ElMessage.success('日志清空成功')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('清空日志失败:', error)
      ElMessage.error('清空日志失败')
    }
  } finally {
    clearing.value = false
  }
}

onMounted(() => {
  loadLogs()
})
</script>

<style scoped>
.admin-logs {
  padding: 20px;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  height: 100%;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
}

.stat-icon {
  margin-right: 15px;
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.stat-icon.error {
  background: linear-gradient(135deg, #f56c6c, #e74c3c);
}

.stat-icon.warning {
  background: linear-gradient(135deg, #e6a23c, #f39c12);
}

.stat-icon.info {
  background: linear-gradient(135deg, #409eff, #3498db);
}

.stat-icon.total {
  background: linear-gradient(135deg, #67c23a, #27ae60);
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

.filter-card {
  margin-bottom: 20px;
}

.filter-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-title {
  font-weight: 600;
  font-size: 16px;
}

.filter-actions {
  display: flex;
  gap: 10px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.filter-bar {
  margin-bottom: 0;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.log-detail-content {
  white-space: pre-wrap;
  word-break: break-all;
  max-height: 300px;
  overflow-y: auto;
  background-color: #f5f5f5;
  padding: 10px;
  border-radius: 4px;
  font-family: monospace;
  font-size: 12px;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .stats-row .el-col {
    margin-bottom: 20px;
  }
}

@media (max-width: 768px) {
  .admin-logs {
    padding: 10px;
  }
  
  .filter-header {
    flex-direction: column;
    gap: 10px;
    align-items: flex-start;
  }
  
  .filter-actions {
    width: 100%;
    justify-content: flex-end;
  }
}
</style>