<template>
  <div class="admin-logs">
    <!-- 统计卡片 - 更简洁的设计 -->
    <div class="stats-container">
      <el-card class="stat-card" shadow="never">
        <div class="stat-item">
          <div class="stat-icon error">
            <el-icon size="20">
              <Warning />
            </el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.errorCount }}</div>
            <div class="stat-title">错误</div>
          </div>
        </div>
      </el-card>

      <el-card class="stat-card" shadow="never">
        <div class="stat-item">
          <div class="stat-icon warning">
            <el-icon size="20">
              <InfoFilled />
            </el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.warnCount }}</div>
            <div class="stat-title">警告</div>
          </div>
        </div>
      </el-card>

      <el-card class="stat-card" shadow="never">
        <div class="stat-item">
          <div class="stat-icon info">
            <el-icon size="20">
              <Document />
            </el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.infoCount }}</div>
            <div class="stat-title">信息</div>
          </div>
        </div>
      </el-card>

      <el-card class="stat-card" shadow="never">
        <div class="stat-item">
          <div class="stat-icon total">
            <el-icon size="20">
              <List />
            </el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalCount }}</div>
            <div class="stat-title">总计</div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 主内容卡片 - 整合筛选和表格 -->
    <el-card class="main-card" shadow="never">

      <!-- 日志列表 -->
      <div class="table-section">
        <el-table v-loading="loading" :data="filteredLogs" style="width: 100%" stripe empty-text="暂无数据">
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
              <el-button type="primary" size="small" @click="viewLogDetail(row)">
                详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-container">
          <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :page-sizes="[10, 20, 50, 100]"
            :total="totalLogs" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </div>
      </div>
    </el-card>

    <!-- 日志详情对话框 -->
    <el-dialog v-model="showLogDetail" title="日志详情" width="600px">
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
import Server from '@/utils/Server.js'

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
  dateRange: []
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
    const response = await Server.get('/admin/logs', {
      params: {
        page: currentPage.value - 1,
        size: pageSize.value,
        level: filter.level,
        module: filter.module,
        startDate: filter.dateRange?.[0],
        endDate: filter.dateRange?.[1]
      }
    })

    logs.value = response.data.data.content || []
    totalLogs.value = response.data.data.totalElements || 0
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
    const response = await Server.get('/admin/logs/export', {
      responseType: 'blob',
      params: {
        level: filter.level,
        module: filter.module,
        startDate: filter.dateRange?.[0],
        endDate: filter.dateRange?.[1]
      }
    })

    // 创建下载链接
    const blob = new Blob([response.data], { type: 'application/json' })
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

    await Server.delete('/admin/logs')

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
  padding: 0px;
  background: linear-gradient(135deg, #f5f7fa 0%, #f0f4f8 100%);
}

/* 统计卡片容器 - 优化设计 */
.stats-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  border-radius: 16px;
  border: none;
  background: linear-gradient(145deg, #ffffff 0%, #f8fafc 100%);
  box-shadow: 
    0 4px 6px -1px rgba(0, 0, 0, 0.05),
    0 2px 4px -1px rgba(0, 0, 0, 0.03),
    inset 0 1px 0 rgba(255, 255, 255, 0.8);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, transparent 0%, rgba(255, 255, 255, 0.8) 50%, transparent 100%);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-4px) scale(1.02);
  box-shadow: 
    0 20px 25px -5px rgba(0, 0, 0, 0.1),
    0 10px 10px -5px rgba(0, 0, 0, 0.04),
    0 0 0 1px rgba(255, 255, 255, 0.9);
}

.stat-card:hover::before {
  opacity: 1;
}

.stat-item {
  display: flex;
  align-items: center;
  padding: 20px;
  position: relative;
  z-index: 1;
}

.stat-icon {
  margin-right: 16px;
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}

.stat-card:hover .stat-icon {
  transform: scale(1.1) rotate(5deg);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

.stat-icon.error {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%);
}

.stat-icon.warning {
  background: linear-gradient(135deg, #ffd93d 0%, #ff9f43 100%);
}

.stat-icon.info {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.total {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-content {
  flex: 1;
  text-align: left;
}

.stat-value {
  font-size: 28px;
  font-weight: 800;
  background: linear-gradient(135deg, #2d3748 0%, #4a5568 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  line-height: 1;
  margin-bottom: 6px;
  letter-spacing: -0.5px;
}

.stat-title {
  font-size: 13px;
  color: #718096;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.8px;
  opacity: 0.9;
}

/* 主内容卡片 - 优化设计 */
.main-card {
  border-radius: 20px;
  border: none;
  background: linear-gradient(145deg, #ffffff 0%, #f8fafc 100%);
  box-shadow: 
    0 10px 15px -3px rgba(0, 0, 0, 0.05),
    0 4px 6px -2px rgba(0, 0, 0, 0.03),
    inset 0 1px 0 rgba(255, 255, 255, 0.8);
  transition: all 0.3s ease;
}

.main-card:hover {
  box-shadow: 
    0 20px 25px -5px rgba(0, 0, 0, 0.08),
    0 10px 10px -5px rgba(0, 0, 0, 0.02);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px 28px;
  border-bottom: 1px solid #f0f2f5;
  background: linear-gradient(90deg, #f8fafc 0%, #ffffff 100%);
}

.card-title {
  font-size: 20px;
  font-weight: 700;
  background: linear-gradient(135deg, #2d3748 0%, #4a5568 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: -0.5px;
}

.header-actions {
  display: flex;
  gap: 12px;
}

/* 筛选区域 */
.filter-section {
  padding: 24px 28px;
  border-bottom: 1px solid #f0f2f5;
  background: linear-gradient(180deg, #fafbfc 0%, #ffffff 100%);
}

.filter-row {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.filter-row>* {
  flex: 1;
  min-width: 140px;
}

.filter-actions {
  display: flex;
  gap: 12px;
  flex-shrink: 0;
}

/* 表格区域 */
.table-section {
  padding: 0;
}

.pagination-container {
  padding: 20px 28px;
  border-top: 1px solid #f0f2f5;
  display: flex;
  justify-content: center;
  background: linear-gradient(180deg, #fafbfc 0%, #ffffff 100%);
}

.log-detail-content {
  white-space: pre-wrap;
  word-break: break-all;
  max-height: 300px;
  overflow-y: auto;
  background: linear-gradient(145deg, #f8f9fa 0%, #ffffff 100%);
  padding: 20px;
  border-radius: 12px;
  font-family: 'SF Mono', Monaco, 'Cascadia Code', monospace;
  font-size: 14px;
  border: 1px solid #e9ecef;
  line-height: 1.6;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.02);
}

/* 表格样式优化 */
:deep(.el-table) {
  border-radius: 0;
  background: transparent;
}

:deep(.el-table__header) {
  background: linear-gradient(180deg, #f8fafc 0%, #ffffff 100%);
}

:deep(.el-table th) {
  background: linear-gradient(180deg, #f8fafc 0%, #ffffff 100%);
  color: #4a5568;
  font-weight: 700;
  font-size: 14px;
  border-bottom: 2px solid #e2e8f0;
}

:deep(.el-table td) {
  border-bottom: 1px solid #f0f2f5;
  font-size: 14px;
  transition: background-color 0.2s ease;
}

:deep(.el-table tr:hover td) {
  background-color: #f7fafc !important;
}

:deep(.el-table .cell) {
  line-height: 1.5;
  padding: 12px 16px;
}

/* 按钮样式优化 */
:deep(.el-button) {
  border-radius: 8px;
  font-weight: 600;
  transition: all 0.3s ease;
}

:deep(.el-button--primary) {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  border: none;
}

:deep(.el-button--primary:hover) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(79, 172, 254, 0.3);
}

/* 标签样式优化 */
:deep(.el-tag) {
  border-radius: 6px;
  font-weight: 600;
  border: none;
}

:deep(.el-tag--danger) {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%);
}

:deep(.el-tag--warning) {
  background: linear-gradient(135deg, #ffd93d 0%, #ff9f43 100%);
}

:deep(.el-tag--primary) {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

:deep(.el-tag--info) {
  background: linear-gradient(135deg, #a0aec0 0%, #718096 100%);
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .stats-container {
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }
}

@media (max-width: 768px) {
  .admin-logs {
    padding: 16px;
  }

  .stats-container {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  .stat-card {
    border-radius: 12px;
  }

  .stat-item {
    padding: 16px;
  }

  .stat-icon {
    width: 40px;
    height: 40px;
    margin-right: 12px;
  }

  .stat-value {
    font-size: 24px;
  }

  .main-card {
    border-radius: 16px;
  }

  .card-header {
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
    padding: 20px;
  }

  .header-actions {
    justify-content: flex-start;
    flex-wrap: wrap;
  }

  .filter-section {
    padding: 20px;
  }

  .filter-row {
    flex-direction: column;
    gap: 16px;
  }

  .filter-row>* {
    width: 100%;
    min-width: auto;
  }

  .filter-actions {
    width: 100%;
    justify-content: flex-end;
    flex-wrap: wrap;
  }

  .pagination-container {
    padding: 16px 20px;
  }
}

@media (max-width: 480px) {
  .admin-logs {
    padding: 12px;
  }

  .stats-container {
    gap: 12px;
  }

  .stat-item {
    padding: 12px;
  }

  .stat-icon {
    width: 36px;
    height: 36px;
    margin-right: 10px;
  }

  .stat-value {
    font-size: 22px;
  }

  .card-header {
    padding: 16px;
  }

  .filter-section {
    padding: 16px;
  }

  .pagination-container {
    padding: 12px 16px;
  }

  :deep(.el-table .cell) {
    padding: 8px 12px;
  }

  :deep(.el-table th),
  :deep(.el-table td) {
    font-size: 13px;
  }
}
</style>