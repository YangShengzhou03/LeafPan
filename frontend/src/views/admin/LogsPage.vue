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
    const response = await adminAPI.getOperationLogs({
      page: currentPage.value,
      size: pageSize.value,
      level: filter.level,
      module: filter.module,
      startDate: filter.dateRange?.[0],
      endDate: filter.dateRange?.[1]
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
      endDate: filter.dateRange?.[1]
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
  padding: 0px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

/* 统计卡片容器 */
.stats-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 20px;
}

.stat-card {
  border-radius: 8px;
  border: 1px solid #e4e7ed;
  background: white;
  transition: all 0.3s ease;
}

.stat-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
}

.stat-item {
  display: flex;
  align-items: center;
  padding: 16px;
}

.stat-icon {
  margin-right: 12px;
  width: 36px;
  height: 36px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
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
  text-align: center;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #303133;
  line-height: 1;
  margin-bottom: 4px;
}

.stat-title {
  font-size: 12px;
  color: #909399;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

/* 主内容卡片 */
.main-card {
  border-radius: 12px;
  border: 1px solid #e4e7ed;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #f0f2f5;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.header-actions {
  display: flex;
  gap: 8px;
}

/* 筛选区域 */
.filter-section {
  padding: 20px 24px;
  border-bottom: 1px solid #f0f2f5;
  background: #fafbfc;
}

.filter-row {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.filter-row>* {
  flex: 1;
  min-width: 120px;
}

.filter-actions {
  display: flex;
  gap: 8px;
  flex-shrink: 0;
}

/* 表格区域 */
.table-section {
  padding: 0;
}

.pagination-container {
  padding: 16px 24px;
  border-top: 1px solid #f0f2f5;
  display: flex;
  justify-content: center;
  background: #fafbfc;
}

.log-detail-content {
  white-space: pre-wrap;
  word-break: break-all;
  max-height: 300px;
  overflow-y: auto;
  background-color: #f8f9fa;
  padding: 16px;
  border-radius: 6px;
  font-family: 'SF Mono', Monaco, 'Cascadia Code', monospace;
  font-size: 13px;
  border: 1px solid #e9ecef;
  line-height: 1.5;
}

/* 表格样式优化 */
:deep(.el-table) {
  border-radius: 0;
}

:deep(.el-table__header) {
  background-color: #fafbfc;
}

:deep(.el-table th) {
  background-color: #fafbfc;
  color: #606266;
  font-weight: 600;
  font-size: 13px;
}

:deep(.el-table td) {
  border-bottom: 1px solid #f0f2f5;
  font-size: 13px;
}

:deep(.el-table .cell) {
  line-height: 1.4;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .stats-container {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .admin-logs {
    padding: 16px;
  }

  .stats-container {
    grid-template-columns: 1fr;
    gap: 12px;
  }

  .card-header {
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
  }

  .header-actions {
    justify-content: flex-start;
  }

  .filter-row {
    flex-direction: column;
    gap: 12px;
  }

  .filter-row>* {
    width: 100%;
    min-width: auto;
  }

  .filter-actions {
    width: 100%;
    justify-content: flex-end;
  }
}

@media (max-width: 480px) {
  .admin-logs {
    padding: 12px;
  }

  .stat-item {
    padding: 12px;
  }

  .stat-value {
    font-size: 20px;
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
}
</style>