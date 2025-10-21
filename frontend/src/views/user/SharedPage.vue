<template>
  <div class="shared-page">
    <!-- 标签页 -->
    <el-tabs v-model="activeTab" class="shared-tabs" @tab-click="handleTabClick">
      <el-tab-pane label="我共享的" name="sharedByMe">
        <div class="tab-header">
          <div class="search-bar">
            <el-input v-model="searchQuery" placeholder="搜索共享文件..." prefix-icon="el-icon-search" clearable
              @input="handleSearch" />
          </div>
          <div class="tab-actions">
            <el-button type="primary" @click="createShare">新建共享</el-button>
          </div>
        </div>

        <!-- 共享文件列表 -->
        <div class="shared-files-list" v-if="filteredSharedByMe.length > 0">
          <div class="file-item" v-for="file in filteredSharedByMe" :key="file.id">
            <div class="file-icon" :class="getFileIconClass(file.type)">
              <i :class="getFileIcon(file.type)"></i>
            </div>
            <div class="file-info">
              <div class="file-name">{{ file.name }}</div>
              <div class="file-meta">
                <span class="meta-item">共享给: {{ file.sharedTo.join(', ') }}</span>
                <span class="meta-item">共享时间: {{ formatDate(file.sharedAt) }}</span>
                <span class="meta-item">权限: {{ file.permission === 'read' ? '只读' : '读写' }}</span>
              </div>
            </div>
            <div class="file-actions">
              <el-button type="text" @click="editShare(file)">编辑</el-button>
              <el-button type="text" @click="stopShare(file)">停止共享</el-button>
            </div>
          </div>
        </div>

        <!-- 空状态 -->
        <div class="empty-state" v-else>
          <el-empty description="暂无共享文件"></el-empty>
        </div>
      </el-tab-pane>

      <el-tab-pane label="与我共享" name="sharedWithMe">
        <div class="tab-header">
          <div class="search-bar">
            <el-input v-model="searchQueryWithMe" placeholder="搜索共享文件..." prefix-icon="el-icon-search" clearable
              @input="handleSearchWithMe" />
          </div>
        </div>

        <!-- 与我共享的文件列表 -->
        <div class="shared-files-list" v-if="filteredSharedWithMe.length > 0">
          <div class="file-item" v-for="file in filteredSharedWithMe" :key="file.id">
            <div class="file-icon" :class="getFileIconClass(file.type)">
              <i :class="getFileIcon(file.type)"></i>
            </div>
            <div class="file-info">
              <div class="file-name">{{ file.name }}</div>
              <div class="file-meta">
                <span class="meta-item">共享者: {{ file.sharedBy }}</span>
                <span class="meta-item">共享时间: {{ formatDate(file.sharedAt) }}</span>
                <span class="meta-item">权限: {{ file.permission === 'read' ? '只读' : '读写' }}</span>
              </div>
            </div>
            <div class="file-actions">
              <el-button type="text" @click="downloadFile(file)">下载</el-button>
              <el-button type="text" @click="viewFile(file)">查看</el-button>
              <el-button type="text" @click="removeShare(file)">移除</el-button>
            </div>
          </div>
        </div>

        <!-- 空状态 -->
        <div class="empty-state" v-else>
          <el-empty description="暂无他人共享的文件"></el-empty>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 新建/编辑共享对话框 -->
    <el-dialog :title="shareDialogTitle" v-model="shareDialogVisible" width="500px" :close-on-click-modal="false">
      <el-form :model="shareForm" label-width="100px" :rules="shareRules" ref="shareFormRef">
        <el-form-item label="选择文件" prop="fileId">
          <el-select v-model="shareForm.fileId" placeholder="请选择要共享的文件" style="width: 100%">
            <el-option v-for="file in availableFiles" :key="file.id" :label="file.name" :value="file.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="共享给" prop="sharedTo">
          <el-select v-model="shareForm.sharedTo" placeholder="请选择用户" multiple filterable style="width: 100%">
            <el-option v-for="user in availableUsers" :key="user.id" :label="user.username" :value="user.username" />
          </el-select>
        </el-form-item>
        <el-form-item label="权限" prop="permission">
          <el-radio-group v-model="shareForm.permission">
            <el-radio label="read">只读</el-radio>
            <el-radio label="write">读写</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="过期时间" prop="expiresAt">
          <el-date-picker v-model="shareForm.expiresAt" type="date" placeholder="选择过期时间" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="shareDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitShare" :loading="submitting">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import mockApiService from '@/utils/mockApiService.js'
import { formatDate } from '@/utils/api.js'

// 标签页
const activeTab = ref('sharedByMe')

// 搜索查询
const searchQuery = ref('')
const searchQueryWithMe = ref('')

// 共享文件数据
const sharedByMe = ref([])
const sharedWithMe = ref([])
const availableFiles = ref([])
const availableUsers = ref([])

// 对话框相关
const shareDialogVisible = ref(false)
const shareDialogTitle = ref('新建共享')
const shareFormRef = ref(null)
const submitting = ref(false)
const isEditing = ref(false)

// 共享表单
const shareForm = ref({
  id: null,
  fileId: '',
  sharedTo: [],
  permission: 'read',
  expiresAt: null
})

// 表单验证规则
const shareRules = {
  fileId: [
    { required: true, message: '请选择要共享的文件', trigger: 'change' }
  ],
  sharedTo: [
    { required: true, message: '请选择共享用户', trigger: 'change' }
  ],
  permission: [
    { required: true, message: '请选择权限', trigger: 'change' }
  ]
}

// 过滤后的共享文件
const filteredSharedByMe = computed(() => {
  if (!searchQuery.value) return sharedByMe.value
  const query = searchQuery.value.toLowerCase()
  return sharedByMe.value.filter(file =>
    file.name.toLowerCase().includes(query) ||
    file.sharedTo.some(user => user.toLowerCase().includes(query))
  )
})

const filteredSharedWithMe = computed(() => {
  if (!searchQueryWithMe.value) return sharedWithMe.value
  const query = searchQueryWithMe.value.toLowerCase()
  return sharedWithMe.value.filter(file =>
    file.name.toLowerCase().includes(query) ||
    file.sharedBy.toLowerCase().includes(query)
  )
})

// 获取文件图标
const getFileIcon = (type) => {
  const icons = {
    folder: 'el-icon-folder',
    image: 'el-icon-picture',
    video: 'el-icon-film',
    audio: 'el-icon-headset',
    pdf: 'el-icon-document',
    document: 'el-icon-document',
    archive: 'el-icon-folder-opened',
    code: 'el-icon-document-copy',
    other: 'el-icon-document'
  }
  return icons[type] || icons.other
}

// 获取文件图标类名
const getFileIconClass = (type) => {
  const classes = {
    folder: 'folder-icon',
    image: 'image-icon',
    video: 'video-icon',
    audio: 'audio-icon',
    pdf: 'pdf-icon',
    document: 'document-icon',
    archive: 'archive-icon',
    code: 'code-icon',
    other: 'other-icon'
  }
  return classes[type] || classes.other
}

// 处理标签页切换
const handleTabClick = () => {
  // 重置搜索查询
  if (activeTab.value === 'sharedByMe') {
    searchQuery.value = ''
  } else {
    searchQueryWithMe.value = ''
  }
}

// 处理搜索
const handleSearch = () => {
  // 搜索逻辑已在计算属性中实现
}

const handleSearchWithMe = () => {
  // 搜索逻辑已在计算属性中实现
}

// 创建共享
const createShare = () => {
  isEditing.value = false
  shareDialogTitle.value = '新建共享'
  shareForm.value = {
    id: null,
    fileId: '',
    sharedTo: [],
    permission: 'read',
    expiresAt: null
  }
  shareDialogVisible.value = true
}

// 编辑共享
const editShare = (file) => {
  isEditing.value = true
  shareDialogTitle.value = '编辑共享'
  shareForm.value = {
    id: file.id,
    fileId: file.fileId,
    sharedTo: file.sharedTo,
    permission: file.permission,
    expiresAt: file.expiresAt
  }
  shareDialogVisible.value = true
}

// 停止共享
const stopShare = (file) => {
  ElMessageBox.confirm(
    `确定要停止共享 "${file.name}" 吗？`,
    '确认停止共享',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await mockApiService.stopShare(file.id)
      ElMessage.success('已停止共享')
      await fetchSharedByMe()
    } catch (error) {
      ElMessage.error('停止共享失败')
      console.error('停止共享失败:', error)
    }
  }).catch(() => {
    // 用户取消操作
  })
}

// 下载文件
const downloadFile = (file) => {
  ElMessage.success(`开始下载 "${file.name}"`)
  // 这里应该调用下载API
  console.log('下载文件:', file)
}

// 查看文件
const viewFile = (file) => {
  ElMessage.info(`查看文件 "${file.name}"`)
  // 这里应该跳转到文件查看页面
  console.log('查看文件:', file)
}

// 移除共享
const removeShare = (file) => {
  ElMessageBox.confirm(
    `确定要从您的共享列表中移除 "${file.name}" 吗？`,
    '确认移除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await mockApiService.removeShare(file.id)
      ElMessage.success('已移除共享')
      await fetchSharedWithMe()
    } catch (error) {
      ElMessage.error('移除共享失败')
      console.error('移除共享失败:', error)
    }
  }).catch(() => {
    // 用户取消操作
  })
}

// 提交共享表单
const submitShare = async () => {
  if (!shareFormRef.value) return

  try {
    await shareFormRef.value.validate()
    submitting.value = true

    if (isEditing.value) {
      // 编辑共享
      await mockApiService.updateShare(shareForm.value)
      ElMessage.success('共享已更新')
      await fetchSharedByMe()
    } else {
      // 创建新共享
      await mockApiService.createShare(shareForm.value)
      ElMessage.success('共享已创建')
      await fetchSharedByMe()
    }

    shareDialogVisible.value = false
  } catch (error) {
    if (error !== false) { // 不是表单验证错误
      ElMessage.error(isEditing.value ? '更新共享失败' : '创建共享失败')
      console.error('提交共享失败:', error)
    }
  } finally {
    submitting.value = false
  }
}

// 获取我共享的文件
const fetchSharedByMe = async () => {
  try {
    const response = await mockApiService.getSharedByMe()
    sharedByMe.value = response
  } catch (error) {
    console.error('获取共享文件失败:', error)
  }
}

// 获取与我共享的文件
const fetchSharedWithMe = async () => {
  try {
    const response = await mockApiService.getSharedWithMe()
    sharedWithMe.value = response
  } catch (error) {
    console.error('获取共享文件失败:', error)
  }
}

// 获取可用文件列表
const fetchAvailableFiles = async () => {
  try {
    const response = await mockApiService.getFiles()
    availableFiles.value = response
  } catch (error) {
    console.error('获取文件列表失败:', error)
  }
}

// 获取可用用户列表
const fetchAvailableUsers = async () => {
  try {
    const response = await mockApiService.getAvailableUsers()
    availableUsers.value = response
  } catch (error) {
    console.error('获取用户列表失败:', error)
  }
}

// 页面加载时获取数据
onMounted(async () => {
  await Promise.all([
    fetchSharedByMe(),
    fetchSharedWithMe(),
    fetchAvailableFiles(),
    fetchAvailableUsers()
  ])
})
</script>

<style scoped>
.shared-page {
  padding: 24px;
  background-color: #f8fafc;
  min-height: 100vh;
  font-family: 'Inter', 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
}

.shared-header {
  margin-bottom: 30px;
}

.shared-header h1 {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 10px 0;
}

.shared-header p {
  color: #606266;
  margin: 0;
  font-size: 16px;
}

/* 标签页 */
.shared-tabs {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  padding: 20px;
}

.tab-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-bar {
  width: 300px;
}

/* 文件列表 */
.shared-files-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.file-item {
  display: flex;
  align-items: center;
  padding: 15px;
  border-radius: 8px;
  background-color: #f9f9f9;
  transition: all 0.3s ease;
}

.file-item:hover {
  background-color: #f0f7ff;
}

.file-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  flex-shrink: 0;
}

.file-icon i {
  font-size: 24px;
  color: #fff;
}

.file-info {
  flex: 1;
  min-width: 0;
}

.file-name {
  font-weight: 500;
  color: #303133;
  margin-bottom: 5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.file-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  font-size: 12px;
  color: #909399;
}

.meta-item {
  display: flex;
  align-items: center;
}

.file-actions {
  display: flex;
  gap: 10px;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  color: #909399;
}

.empty-state i {
  font-size: 48px;
  margin-bottom: 15px;
}

.empty-state p {
  margin: 0 0 20px 0;
  font-size: 16px;
}

/* 文件图标样式 */
.folder-icon {
  background-color: #E6A23C;
}

.image-icon {
  background-color: #67C23A;
}

.video-icon {
  background-color: #F56C6C;
}

.audio-icon {
  background-color: #409EFF;
}

.pdf-icon {
  background-color: #F56C6C;
}

.document-icon {
  background-color: #409EFF;
}

.archive-icon {
  background-color: #E6A23C;
}

.code-icon {
  background-color: #909399;
}

.other-icon {
  background-color: #909399;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .shared-header h1 {
    font-size: 24px;
  }

  .tab-header {
    flex-direction: column;
    align-items: stretch;
    gap: 15px;
  }

  .search-bar {
    width: 100%;
  }

  .file-item {
    flex-direction: column;
    align-items: flex-start;
  }

  .file-icon {
    margin-right: 0;
    margin-bottom: 10px;
  }

  .file-actions {
    width: 100%;
    justify-content: flex-end;
    margin-top: 10px;
  }

  .file-meta {
    flex-direction: column;
    gap: 5px;
  }
}
</style>