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
import { shareAPI, fileAPI } from '@/utils/api.js'
import { formatDate } from '@/utils/utils.js'

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
      await shareAPI.delete(file.id)
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
const downloadFile = async (file) => {
  try {
    const response = await fileAPI.download(file.fileId)
    const url = window.URL.createObjectURL(new Blob([response]))
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', file.name)
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    ElMessage.success(`开始下载 "${file.name}"`)
  } catch (error) {
    ElMessage.error('下载失败')
    console.error('下载文件失败:', error)
  }
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
      await shareAPI.delete(file.id)
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
      await shareAPI.update(shareForm.value.id, shareForm.value)
      ElMessage.success('共享已更新')
      await fetchSharedByMe()
    } else {
      // 创建新共享
      await shareAPI.create(shareForm.value)
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
    const response = await shareAPI.getUserShares()
    sharedByMe.value = response.data || []
  } catch (error) {
    console.error('获取共享文件失败:', error)
  }
}

// 获取与我共享的文件
const fetchSharedWithMe = async () => {
  try {
    // 这个API可能需要根据实际后端实现调整
    const response = await shareAPI.getUserShares()
    sharedWithMe.value = response.data || []
  } catch (error) {
    console.error('获取共享文件失败:', error)
  }
}

// 获取可用文件列表
const fetchAvailableFiles = async () => {
  try {
    const response = await fileAPI.getFiles()
    availableFiles.value = response.data || []
  } catch (error) {
    console.error('获取文件列表失败:', error)
  }
}

// 获取可用用户列表
const fetchAvailableUsers = async () => {
  try {
    // 这个API可能需要根据实际后端实现调整
    const response = await shareAPI.getUserShares()
    // 模拟用户数据，实际应该从API获取
    availableUsers.value = [
      { id: 1, username: 'user1' },
      { id: 2, username: 'user2' },
      { id: 3, username: 'user3' }
    ]
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
  padding: 20px;
  background-color: #ffffff;
  min-height: 100vh;
  font-family: 'Inter', 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
}

/* 标签页 */
.shared-tabs {
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  border: 1px solid #e5e7eb;
  padding: 20px;
}

.tab-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.search-bar {
  width: 300px;
}

/* 文件列表 */
.shared-files-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.file-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border-radius: 8px;
  background-color: #f8fafc;
  border: 1px solid #e2e8f0;
  transition: all 0.2s ease;
}

.file-item:hover {
  background-color: #eff6ff;
  border-color: #3b82f6;
  transform: translateY(-1px);
}

.file-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  flex-shrink: 0;
}

.file-icon i {
  font-size: 20px;
  color: #fff;
}

.file-info {
  flex: 1;
  min-width: 0;
}

.file-name {
  font-weight: 500;
  color: #1f2937;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  font-size: 14px;
}

.file-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  font-size: 12px;
  color: #6b7280;
}

.meta-item {
  display: flex;
  align-items: center;
}

.file-actions {
  display: flex;
  gap: 8px;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  color: #9ca3af;
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
  background-color: #f59e0b;
}

.image-icon {
  background-color: #10b981;
}

.video-icon {
  background-color: #ef4444;
}

.audio-icon {
  background-color: #3b82f6;
}

.pdf-icon {
  background-color: #ef4444;
}

.document-icon {
  background-color: #3b82f6;
}

.archive-icon {
  background-color: #f59e0b;
}

.code-icon {
  background-color: #6b7280;
}

.other-icon {
  background-color: #6b7280;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .shared-page {
    padding: 16px;
  }

  .shared-tabs {
    padding: 16px;
  }

  .tab-header {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
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
    margin-bottom: 8px;
  }

  .file-actions {
    width: 100%;
    justify-content: flex-end;
    margin-top: 8px;
  }

  .file-meta {
    flex-direction: column;
    gap: 4px;
  }
}
</style>