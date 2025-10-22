<template>
  <div class="admin-logs">
    <el-card class="logs-card">
      <template #header>
        <div class="card-header">
          <span>操作日志</span>
          <div class="header-actions">
            <el-button type="primary" @click="exportLogs" :loading="exporting">
              <el-icon><Download /></el-icon>
              导出日志
            </el-button>
            <el-button type="danger" @click="clearLogs" :loading="clearing">
              <el-icon><Delete /></el-icon>
              清空日志
            </el-button>
          </div>
        </div>
      </template>
      
      <div class="logs-content">
        <!-- 筛选条件 -->
        <div class="filter-bar">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-select v-model="filters.level" placeholder="日志级别" clearable @change="handleFilter">
                <el-option label="全部" value="" />
                <el-option label="信息" value="INFO" />
                <el-option label="警告" value="WARN" />
                <el-option label="错误" value="ERROR" />
                <el-option label="调试" value="DEBUG" />
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-select v-model="filters.module" placeholder="模块" clearable @change="handleFilter">
                <el-option label="全部" value="" />
                <el-option label="用户管理" value="USER" />
                <el-option label="文件管理" value="FILE" />
                <el-option label="系统管理" value="SYSTEM" />
                <el-option label="认证授权" value="AUTH" />
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-date-picker
                v-model="filters.dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                @change="handleFilter"
              />
            </el-col>
            <el-col :span="6">
              <el-input
                v-model="filters.keyword"
                placeholder="搜索关键词"
                clearable
                @clear="handleFilter"
                @keyup.enter="handleFilter"
              >
                <template #append>
                  <el-button @click="handleFilter">
                    <el-icon><Search /></el-icon>
                  </el-button>
                </template>
              </el-input>
            </el-col>
          </el-row>
        </div>
        
        <!-- 日志表格 -->
        <el-table :data="filteredLogs" style="width: 100%" v-loading="loading" height="500">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="level" label="级别" width="80">
            <template #default="scope">
              <el-tag :type="getLevelType(scope.row.level)">
                {{ scope.row.level }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="module" label="模块" width="100">
            <template #default="scope">
              {{ getModuleName(scope.row.module) }}
            </template>
          </el-table-column>
          <el-table-column prop="message" label="消息" min-width="200" show-overflow-tooltip />
          <el-table-column prop="user" label="用户" width="120" />
          <el-table-column prop="ip" label="IP地址" width="130" />
          <el-table-column prop="createdAt" label="时间" width="180" />
          <el-table-column label="操作" width="100">
            <template #default="scope">
              <el-button size="small" @click="viewLogDetail(scope.row)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <!-- 分页 -->
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[20, 50, 100, 200]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="totalLogs"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </el-card>
    
    <!-- 日志详情对话框 -->
    <el-dialog
      v-model="showLogDetail"
      title="日志详情"
      width="700px"
    >
      <div class="log-detail" v-if="selectedLog">
        <el-descriptions :column="2" border>
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
          <el-descriptions-item label="消息" :span="2">{{ selectedLog.message }}</el-descriptions-item>
          <el-descriptions-item label="详细信息" :span="2">
            <pre class="log-detail-content">{{ selectedLog.details || '无详细信息' }}</pre>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Download, Delete, Search } from '@element-plus/icons-vue'

// 数据状态
const loading = ref(false)
const exporting = ref(false)
const clearing = ref(false)
const logs = ref([])
const currentPage = ref(1)
const pageSize = ref(20)
const totalLogs = ref(0)
const showLogDetail = ref(false)
const selectedLog = ref(null)

// 筛选条件
const filters = reactive({
  level: '',
  module: '',
  dateRange: [],
  keyword: ''
})

// 过滤后的日志列表
const filteredLogs = computed(() => {
  let result = logs.value
  
  if (filters.level) {
    result = result.filter(log => log.level === filters.level)
  }
  
  if (filters.module) {
    result = result.filter(log => log.module === filters.module)
  }
  
  if (filters.dateRange && filters.dateRange.length === 2) {
    const [startDate, endDate] = filters.dateRange
    result = result.filter(log => {
      const logDate = log.createdAt.split(' ')[0]
      return logDate >= startDate && logDate <= endDate
    })
  }
  
  if (filters.keyword) {
    const keyword = filters.keyword.toLowerCase()
    result = result.filter(log => 
      log.message.toLowerCase().includes(keyword) || 
      log.user.toLowerCase().includes(keyword)
    )
  }
  
  return result
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

// 加载日志数据
const loadLogs = async () => {
  loading.value = true
  try {
    // 这里应该调用后端API获取日志数据
    // 暂时使用模拟数据
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 生成模拟日志数据
    const mockLogs = []
    const levels = ['INFO', 'WARN', 'ERROR', 'DEBUG']
    const modules = ['USER', 'FILE', 'SYSTEM', 'AUTH']
    const users = ['admin', 'user001', 'user002', 'user003', 'user004']
    const ips = ['192.168.1.100', '192.168.1.101', '192.168.1.102', '192.168.1.103']
    
    for (let i = 1; i <= 200; i++) {
      const level = levels[Math.floor(Math.random() * levels.length)]
      const module = modules[Math.floor(Math.random() * modules.length)]
      const user = users[Math.floor(Math.random() * users.length)]
      const ip = ips[Math.floor(Math.random() * ips.length)]
      
      let message = ''
      let details = ''
      
      switch (module) {
        case 'USER':
          message = `用户操作: ${user} ${getRandomUserAction()}`
          details = `用户 ${user} 执行了相关操作，IP地址: ${ip}`
          break
        case 'FILE':
          message = `文件操作: ${getRandomFileAction()}`
          details = `文件操作详情，用户: ${user}，IP地址: ${ip}`
          break
        case 'SYSTEM':
          message = `系统操作: ${getRandomSystemAction()}`
          details = `系统操作详情，操作员: ${user}，IP地址: ${ip}`
          break
        case 'AUTH':
          message = `认证操作: ${getRandomAuthAction(user)}`
          details = `认证操作详情，用户: ${user}，IP地址: ${ip}`
          break
      }
      
      const date = new Date()
      date.setDate(date.getDate() - Math.floor(Math.random() * 30))
      
      mockLogs.push({
        id: i,
        level,
        module,
        message,
        user,
        ip,
        createdAt: date.toISOString().replace('T', ' ').substring(0, 19),
        details
      })
    }
    
    // 按时间倒序排列
    mockLogs.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
    
    logs.value = mockLogs
    totalLogs.value = mockLogs.length
  } catch (error) {
    console.error('加载日志数据失败:', error)
    ElMessage.error('加载日志数据失败')
  } finally {
    loading.value = false
  }
}

// 获取随机用户操作
const getRandomUserAction = () => {
  const actions = [
    '登录系统',
    '修改个人信息',
    '修改密码',
    '查看用户列表',
    '创建用户',
    '更新用户信息',
    '删除用户',
    '禁用用户',
    '启用用户'
  ]
  return actions[Math.floor(Math.random() * actions.length)]
}

// 获取随机文件操作
const getRandomFileAction = () => {
  const actions = [
    '上传文件',
    '下载文件',
    '删除文件',
    '移动文件',
    '重命名文件',
    '创建文件夹',
    '删除文件夹',
    '分享文件',
    '取消分享',
    '查看文件列表'
  ]
  return actions[Math.floor(Math.random() * actions.length)]
}

// 获取随机系统操作
const getRandomSystemAction = () => {
  const actions = [
    '查看系统信息',
    '修改系统设置',
    '执行系统备份',
    '清理临时文件',
    '清理日志文件',
    '查看系统监控',
    '重启系统服务',
    '更新系统配置'
  ]
  return actions[Math.floor(Math.random() * actions.length)]
}

// 获取随机认证操作
const getRandomAuthAction = (user) => {
  const actions = [
    '用户登录',
    '用户登出',
    '刷新令牌',
    '验证令牌',
    '密码重置',
    '双因素认证验证'
  ]
  return actions[Math.floor(Math.random() * actions.length)]
}

// 筛选处理
const handleFilter = () => {
  currentPage.value = 1
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
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
    // 这里应该调用后端API导出日志
    await new Promise(resolve => setTimeout(resolve, 2000))
    
    // 模拟下载
    const dataStr = JSON.stringify(filteredLogs.value, null, 2)
    const dataUri = 'data:application/json;charset=utf-8,'+ encodeURIComponent(dataStr)
    
    const exportFileDefaultName = `logs_${new Date().toISOString().substring(0, 10)}.json`
    
    const linkElement = document.createElement('a')
    linkElement.setAttribute('href', dataUri)
    linkElement.setAttribute('download', exportFileDefaultName)
    linkElement.click()
    
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
    
    // 这里应该调用后端API清空日志
    await new Promise(resolve => setTimeout(resolve, 1000))
    
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

.logs-card {
  margin-bottom: 20px;
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
  margin-bottom: 20px;
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
</style>