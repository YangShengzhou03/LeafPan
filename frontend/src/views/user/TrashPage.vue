<template>
  <div class="trash-page">
    <!-- 工具栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <el-button type="primary" @click="restoreSelected" :disabled="!hasSelected">
          恢复选中
        </el-button>
        <el-button type="danger" @click="deleteSelected" :disabled="!hasSelected">
          永久删除
        </el-button>
        <el-button @click="clearTrash" :disabled="trashFiles.length === 0">
          清空回收站
        </el-button>
      </div>
      <div class="toolbar-right">
        <el-input v-model="searchQuery" placeholder="搜索回收站中的文件..." prefix-icon="el-icon-search" clearable
          @input="handleSearch" style="width: 250px" />
        <el-select v-model="sortBy" @change="handleSort" style="width: 150px; margin-left: 10px">
          <el-option label="按删除时间" value="deletedAt" />
          <el-option label="按文件名" value="name" />
          <el-option label="按文件大小" value="size" />
          <el-option label="按文件类型" value="type" />
        </el-select>
      </div>
    </div>

    <!-- 文件列表 -->
    <div class="file-list">
      <el-table :data="filteredTrashFiles" style="width: 100%" @selection-change="handleSelectionChange"
        empty-text="回收站为空">
        <el-table-column type="selection" width="55" />
        <el-table-column label="文件名" min-width="300">
          <template #default="{ row }">
            <div class="file-name-cell">
              <div class="file-icon" :class="getFileIconClass(row.type)">
                <i :class="getFileIcon(row.type)"></i>
              </div>
              <div class="file-info">
                <div class="file-name">{{ row.name }}</div>
                <div class="file-path">{{ row.originalPath }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="大小" width="120">
          <template #default="{ row }">
            {{ formatFileSize(row.size) }}
          </template>
        </el-table-column>
        <el-table-column label="类型" width="120">
          <template #default="{ row }">
            {{ getFileTypeLabel(row.type) }}
          </template>
        </el-table-column>
        <el-table-column label="删除时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.deletedAt) }}
          </template>
        </el-table-column>
        <el-table-column label="剩余时间" width="150">
          <template #default="{ row }">
            <span :class="getRemainingTimeClass(row.remainingDays)">
              {{ getRemainingTimeText(row.remainingDays) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="text" @click="restoreFile(row)">恢复</el-button>
            <el-button type="text" @click="deleteFile(row)">永久删除</el-button>
            <el-button type="text" @click="previewFile(row)">预览</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 批量操作确认对话框 -->
    <el-dialog v-model="batchActionDialog.visible" :title="batchActionDialog.title" width="400px"
      :close-on-click-modal="false">
      <p>{{ batchActionDialog.message }}</p>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="batchActionDialog.visible = false">取消</el-button>
          <el-button type="primary" @click="confirmBatchAction" :loading="batchActionDialog.loading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 清空回收站确认对话框 -->
    <el-dialog v-model="clearTrashDialog.visible" title="清空回收站" width="400px" :close-on-click-modal="false">
      <p>确定要清空回收站吗？此操作将永久删除所有文件，且无法恢复。</p>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="clearTrashDialog.visible = false">取消</el-button>
          <el-button type="danger" @click="confirmClearTrash" :loading="clearTrashDialog.loading">
            确认清空
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import mockApiService from '@/utils/mockApiService.js'
import { utils } from '@/utils/api.js'

// 数据
const trashFiles = ref([])
const selectedFiles = ref([])
const searchQuery = ref('')
const sortBy = ref('deletedAt')

// 使用utils中的函数
const formatFileSize = utils.formatFileSize
const formatDate = utils.formatDate

// 对话框
const batchActionDialog = ref({
  visible: false,
  title: '',
  message: '',
  action: null,
  loading: false
})

const clearTrashDialog = ref({
  visible: false,
  loading: false
})

// 计算属性
const filteredTrashFiles = computed(() => {
  let result = [...trashFiles.value]

  // 搜索过滤
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(file =>
      file.name.toLowerCase().includes(query) ||
      file.originalPath.toLowerCase().includes(query)
    )
  }

  // 排序
  result.sort((a, b) => {
    switch (sortBy.value) {
      case 'deletedAt':
        return new Date(b.deletedAt) - new Date(a.deletedAt)
      case 'name':
        return a.name.localeCompare(b.name)
      case 'size':
        return b.size - a.size
      case 'type':
        return a.type.localeCompare(b.type)
      default:
        return 0
    }
  })

  return result
})

const hasSelected = computed(() => {
  return selectedFiles.value.length > 0
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

// 获取文件类型标签
const getFileTypeLabel = (type) => {
  const labels = {
    folder: '文件夹',
    image: '图片',
    video: '视频',
    audio: '音频',
    pdf: 'PDF',
    document: '文档',
    archive: '压缩包',
    code: '代码',
    other: '其他'
  }
  return labels[type] || labels.other
}

// 获取剩余时间类名
const getRemainingTimeClass = (days) => {
  if (days <= 3) return 'urgent'
  if (days <= 7) return 'warning'
  return 'normal'
}

// 获取剩余时间文本
const getRemainingTimeText = (days) => {
  if (days <= 0) return '即将删除'
  if (days === 1) return '剩余1天'
  return `剩余${days}天`
}

// 处理选择变化
const handleSelectionChange = (selection) => {
  selectedFiles.value = selection
}

// 处理搜索
const handleSearch = () => {
  // 搜索逻辑已在计算属性中实现
}

// 处理排序
const handleSort = () => {
  // 排序逻辑已在计算属性中实现
}

// 恢复文件
const restoreFile = async (file) => {
  try {
    await mockApiService.restoreFromTrash(file.id)
    ElMessage.success(`"${file.name}" 已恢复`)
    await fetchTrashFiles()
  } catch (error) {
    ElMessage.error('恢复文件失败')
    console.error('恢复文件失败:', error)
  }
}

// 删除文件
const deleteFile = (file) => {
  ElMessageBox.confirm(
    `确定要永久删除 "${file.name}" 吗？此操作无法撤销。`,
    '确认删除',
    {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await mockApiService.deleteFromTrash(file.id)
      ElMessage.success('文件已永久删除')
      await fetchTrashFiles()
    } catch (error) {
      ElMessage.error('删除文件失败')
      console.error('删除文件失败:', error)
    }
  }).catch(() => {
    // 用户取消操作
  })
}

// 预览文件
const previewFile = (file) => {
  ElMessage.info(`预览文件 "${file.name}"`)
  // 这里应该实现文件预览逻辑
  console.log('预览文件:', file)
}

// 恢复选中文件
const restoreSelected = () => {
  if (selectedFiles.value.length === 0) return

  batchActionDialog.value = {
    visible: true,
    title: '恢复文件',
    message: `确定要恢复选中的 ${selectedFiles.value.length} 个文件吗？`,
    action: 'restore',
    loading: false
  }
}

// 删除选中文件
const deleteSelected = () => {
  if (selectedFiles.value.length === 0) return

  batchActionDialog.value = {
    visible: true,
    title: '永久删除',
    message: `确定要永久删除选中的 ${selectedFiles.value.length} 个文件吗？此操作无法撤销。`,
    action: 'delete',
    loading: false
  }
}

// 清空回收站
const clearTrash = () => {
  if (trashFiles.value.length === 0) return

  clearTrashDialog.value.visible = true
}

// 确认批量操作
const confirmBatchAction = async () => {
  const { action } = batchActionDialog.value
  batchActionDialog.value.loading = true

  try {
    if (action === 'restore') {
      await Promise.all(selectedFiles.value.map(file =>
        mockApiService.restoreFromTrash(file.id)
      ))
      ElMessage.success(`已恢复 ${selectedFiles.value.length} 个文件`)
    } else if (action === 'delete') {
      await Promise.all(selectedFiles.value.map(file =>
        mockApiService.deleteFromTrash(file.id)
      ))
      ElMessage.success(`已永久删除 ${selectedFiles.value.length} 个文件`)
    }

    batchActionDialog.value.visible = false
    await fetchTrashFiles()
  } catch (error) {
    ElMessage.error('操作失败')
    console.error('批量操作失败:', error)
  } finally {
    batchActionDialog.value.loading = false
  }
}

// 确认清空回收站
const confirmClearTrash = async () => {
  clearTrashDialog.value.loading = true

  try {
    await mockApiService.clearTrash()
    ElMessage.success('回收站已清空')
    clearTrashDialog.value.visible = false
    await fetchTrashFiles()
  } catch (error) {
    ElMessage.error('清空回收站失败')
    console.error('清空回收站失败:', error)
  } finally {
    clearTrashDialog.value.loading = false
  }
}

// 获取回收站文件
const fetchTrashFiles = async () => {
  try {
    const response = await mockApiService.getTrashFiles()
    trashFiles.value = response
  } catch (error) {
    ElMessage.error('获取回收站文件失败')
    console.error('获取回收站文件失败:', error)
  }
}

// 页面加载时获取数据
onMounted(() => {
  fetchTrashFiles()
})
</script>

<style scoped>
.trash-page {
  padding: 20px;
  background-color: #ffffff;
  min-height: 100vh;
  font-family: 'Inter', 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
}

/* 工具栏 */
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding: 16px;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  border: 1px solid #e5e7eb;
}

.toolbar-left {
  display: flex;
  gap: 8px;
}

.toolbar-right {
  display: flex;
  align-items: center;
}

/* 文件列表 */
.file-list {
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  border: 1px solid #e5e7eb;
  overflow: hidden;
}

.file-name-cell {
  display: flex;
  align-items: center;
}

.file-icon {
  width: 32px;
  height: 32px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  flex-shrink: 0;
}

.file-icon i {
  font-size: 16px;
  color: #fff;
}

.file-info {
  min-width: 0;
}

.file-name {
  font-weight: 500;
  color: #1f2937;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  font-size: 14px;
}

.file-path {
  font-size: 12px;
  color: #6b7280;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 剩余时间样式 */
.urgent {
  color: #ef4444;
  font-weight: 600;
}

.warning {
  color: #f59e0b;
  font-weight: 500;
}

.normal {
  color: #10b981;
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
  .trash-page {
    padding: 16px;
  }

  .toolbar {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }

  .toolbar-left {
    justify-content: center;
  }

  .toolbar-right {
    flex-direction: column;
    gap: 8px;
  }

  .toolbar-right .el-input,
  .toolbar-right .el-select {
    width: 100% !important;
  }
}
</style>