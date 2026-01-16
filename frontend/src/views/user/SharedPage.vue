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
              <span class="meta-item" v-if="file.shareType === 'user'">共享给: {{ file.sharedTo?.join(', ') || '未知' }}</span>
              <span class="meta-item" v-else-if="file.shareType === 'link'">
                <el-tag size="small" type="primary">外链分享</el-tag>
                <span v-if="file.hasPassword" class="password-indicator">🔒</span>
              </span>
              <span class="meta-item">共享时间: {{ formatDate(file.sharedAt || file.createdAt) }}</span>
              <span class="meta-item">权限: {{ file.permission === 'read' ? '只读' : '读写' }}</span>
              <span class="meta-item" v-if="file.viewCount !== undefined">访问: {{ file.viewCount }}次</span>
              <span class="meta-item" v-if="file.downloadCount !== undefined">下载: {{ file.downloadCount }}次</span>
            </div>
            </div>
            <div class="file-actions">
        <el-button 
          type="text" 
          @click="editShare(file)"
          v-if="file.shareType === 'user'"
        >编辑</el-button>
        <el-button 
          type="text" 
          @click="copyShareLink(file)"
          v-else-if="file.shareType === 'link'"
        >复制链接</el-button>
        <el-button type="text" @click="stopShare(file)">停止共享</el-button>
      </div>
          </div>
        </div>

        <!-- 空状态 -->
        <div class="empty-state" v-else>
          <el-empty description="暂无共享文件">
            <template #description>
              <div class="empty-description">
                <p>您还没有共享任何文件</p>
                <p class="empty-tip">点击"新建共享"开始共享文件或生成外链</p>
              </div>
            </template>
          </el-empty>
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
        <el-form-item label="共享方式" prop="shareType">
          <el-radio-group v-model="shareForm.shareType" @change="onShareTypeChange">
            <el-radio label="user">用户共享</el-radio>
            <el-radio label="link">外链分享</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="选择文件" prop="fileId">
          <el-select v-model="shareForm.fileId" placeholder="请选择要共享的文件" style="width: 100%">
            <el-option v-for="file in availableFiles" :key="file.id" :label="file.name" :value="file.id" />
          </el-select>
        </el-form-item>
        <!-- 用户共享选项 -->
        <el-form-item label="共享给" prop="sharedTo" v-if="shareForm.shareType === 'user'">
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
        <!-- 外链分享选项 -->
        <el-form-item label="是否加密" prop="hasPassword" v-if="shareForm.shareType === 'link'">
          <el-switch v-model="shareForm.hasPassword" @change="onPasswordChange" />
        </el-form-item>
        <el-form-item label="访问密码" prop="password" v-if="shareForm.shareType === 'link' && shareForm.hasPassword">
          <el-input v-model="shareForm.password" placeholder="设置访问密码" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="shareDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitShare" :loading="submitting">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 外链分享成功对话框 -->
    <el-dialog 
      title="外链分享成功" 
      v-model="shareLinkDialogVisible" 
      width="450px" 
      :close-on-click-modal="false"
    >
      <div class="share-link-success">
        <div class="share-info-item">
          <div class="info-label">分享链接</div>
          <el-input 
            v-model="shareLinkInfo.url" 
            readonly 
            style="margin-top: 8px;"
          >
            <template #append>
              <el-button @click="copyToClipboard(shareLinkInfo.url)" type="primary" text>
                复制
              </el-button>
            </template>
          </el-input>
        </div>
        
        <div class="share-info-item" v-if="shareLinkInfo.password">
          <div class="info-label">访问密码</div>
          <el-input 
            v-model="shareLinkInfo.password" 
            readonly 
            type="password"
            style="margin-top: 8px;"
          >
            <template #append>
              <el-button @click="copyToClipboard(shareLinkInfo.password)" type="primary" text>
                复制
              </el-button>
            </template>
          </el-input>
        </div>
        
        <div class="share-tips">
          <el-alert 
            title="温馨提示" 
            type="info" 
            :closable="false"
            description="请妥善保管您的分享链接和密码，避免泄露个人信息。"
          />
        </div>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="shareLinkDialogVisible = false">完成</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import Server from '@/utils/Server.js'
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
  shareType: 'user', // 'user' 或 'link'
  sharedTo: [],
  permission: 'read',
  expiresAt: null,
  hasPassword: false,
  password: ''
})

// 表单验证规则
const shareRules = {
  fileId: [
    { required: true, message: '请选择要共享的文件', trigger: 'change' }
  ],
  sharedTo: [
    { 
      required: () => shareForm.value.shareType === 'user', 
      message: '请选择共享用户', 
      trigger: 'change' 
    }
  ],
  permission: [
    { required: true, message: '请选择权限', trigger: 'change' }
  ],
  password: [
    { 
      required: () => shareForm.value.shareType === 'link' && shareForm.value.hasPassword, 
      message: '请设置访问密码', 
      trigger: 'blur' 
    }
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
    shareType: 'user',
    sharedTo: [],
    permission: 'read',
    expiresAt: null,
    hasPassword: false,
    password: ''
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
    shareType: file.shareType || 'user',
    sharedTo: file.sharedTo || [],
    permission: file.permission,
    expiresAt: file.expiresAt,
    hasPassword: file.hasPassword || false,
    password: '' // 编辑时不显示密码
  }
  shareDialogVisible.value = true
}

// 复制分享链接
const copyShareLink = (file) => {
  const baseUrl = window.location.origin
  const shareUrl = `${baseUrl}/s/${file.shareCode}`
  
  // 显示链接信息对话框
  shareLinkInfo.value = {
    url: shareUrl,
    code: file.shareCode,
    password: file.password || '' // 实际密码不会返回，这里只是占位
  }
  shareLinkDialogVisible.value = true
}

// 停止共享
const stopShare = (file) => {
  ElMessageBox.confirm(
    `确定要停止共享 "${file.name}" 吗？停止后将无法通过链接访问。`,
    '确认停止共享',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await Server.delete(`/share/${file.id}`)
      ElMessage.success('已停止共享')
      await fetchSharedByMe()
    } catch (error) {
      ElMessage.error('停止共享失败')
    }
  }).catch(() => {
    // 用户取消操作
  })
}

// 下载文件
const downloadFile = async (file) => {
    try {
        const response = await Server.download(`/file/${file.fileId}/download`)
        const url = window.URL.createObjectURL(new Blob([response.data], { 
            type: response.headers['content-type'] || 'application/octet-stream' 
        }))
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
    }
}

// 查看文件
const viewFile = (file) => {
  ElMessage.info(`查看文件 "${file.name}"`)
  // 这里应该跳转到文件查看页面
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
      await Server.delete(`/share/${file.id}`)
      ElMessage.success('已移除共享')
      await fetchSharedWithMe()
    } catch (error) {
      ElMessage.error('移除共享失败')
    }
  }).catch(() => {
    // 用户取消操作
  })
}

// 外链分享成功对话框
const shareLinkDialogVisible = ref(false)
const shareLinkInfo = ref({
  url: '',
  code: '',
  password: ''
})

// 共享类型切换处理
const onShareTypeChange = () => {
  // 切换共享类型时重置相关字段
  if (shareForm.value.shareType === 'user') {
    shareForm.value.hasPassword = false
    shareForm.value.password = ''
  } else {
    shareForm.value.sharedTo = []
  }
}

// 密码设置切换处理
const onPasswordChange = () => {
  if (!shareForm.value.hasPassword) {
    shareForm.value.password = ''
  }
}

// 复制到剪贴板
const copyToClipboard = (text) => {
  navigator.clipboard.writeText(text).then(() => {
    ElMessage.success('已复制到剪贴板')
  }).catch(() => {
    ElMessage.error('复制失败，请手动复制')
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
      await Server.put(`/share/${shareForm.value.id}`, shareForm.value)
      ElMessage.success('共享已更新')
      await fetchSharedByMe()
    } else {
      // 创建新共享
      const response = await Server.post('/share/create', shareForm.value)
      
      // 如果是外链分享，显示分享链接对话框
      if (shareForm.value.shareType === 'link') {
        const baseUrl = window.location.origin
        shareLinkInfo.value = {
          url: `${baseUrl}/s/${response.data.shareCode}`,
          code: response.data.shareCode,
          password: shareForm.value.password
        }
        shareLinkDialogVisible.value = true
      } else {
        ElMessage.success('共享已创建')
      }
      await fetchSharedByMe()
    }

    // 只有在用户共享时才关闭对话框
    if (shareForm.value.shareType === 'user') {
      shareDialogVisible.value = false
    }
  } catch (error) {
    if (error !== false) { // 不是表单验证错误
      ElMessage.error(isEditing.value ? '更新共享失败' : '创建共享失败')
    }
  } finally {
    submitting.value = false
  }
}

const fetchSharedByMe = async () => {
  try {
    const response = await Server.get('/share/user')
    const shares = response.data || []
    
    sharedByMe.value = shares.map(share => ({
      ...share,
      name: share.fileName || share.name,
      type: getFileTypeFromMimeType(share.fileType),
      size: share.fileSize,
      shareType: share.shareType === 0 ? 'link' : 'user',
      hasPassword: share.shareType === 1,
      password: share.password,
      sharedAt: share.createTime,
      viewCount: share.viewCount,
      downloadCount: share.downloadCount,
      fileId: share.fileId
    }))
  } catch (error) {
  }
}

const fetchSharedWithMe = async () => {
  try {
    const response = await Server.get('/share/shared-with-me')
    const shares = response.data || []
    
    sharedWithMe.value = shares.map(share => ({
      ...share,
      name: share.fileName || share.name,
      type: getFileTypeFromMimeType(share.fileType),
      size: share.fileSize,
      sharedAt: share.createTime,
      fileId: share.fileId
    }))
  } catch (error) {
  }
}

const getFileTypeFromMimeType = (mimeType) => {
  if (!mimeType) return 'other'
  if (mimeType.startsWith('image/')) return 'image'
  if (mimeType.startsWith('video/')) return 'video'
  if (mimeType.startsWith('audio/')) return 'audio'
  if (mimeType.includes('pdf')) return 'pdf'
  if (mimeType.includes('word') || mimeType.includes('document')) return 'document'
  if (mimeType.includes('excel') || mimeType.includes('spreadsheet')) return 'spreadsheet'
  if (mimeType.includes('powerpoint') || mimeType.includes('presentation')) return 'presentation'
  if (mimeType.includes('zip') || mimeType.includes('rar') || mimeType.includes('tar')) return 'archive'
  if (mimeType.includes('text') || mimeType.includes('javascript') || mimeType.includes('json')) return 'code'
  return 'other'
}

const fetchAvailableFiles = async () => {
  try {
    const response = await Server.get('/file/list')
    availableFiles.value = response.data || []
  } catch (error) {
  }
}

// 获取可用用户列表
const fetchAvailableUsers = async () => {
  try {
    const response = await Server.get('/user/list')
    availableUsers.value = response.data || []
  } catch (error) {
    availableUsers.value = []
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

.empty-description {
  text-align: center;
}

.empty-tip {
  font-size: 14px !important;
  color: #9ca3af;
  margin-top: 8px !important;
}

/* 外链分享对话框样式 */
.share-link-success {
  padding: 10px 0;
}

.share-info-item {
  margin-bottom: 20px;
}

.info-label {
  font-weight: 500;
  color: #374151;
  margin-bottom: 8px;
}

.share-tips {
  margin-top: 20px;
}

.password-indicator {
  margin-left: 4px;
  font-size: 14px;
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
    padding: 12px;
  }

  .file-icon {
    margin-right: 0;
    margin-bottom: 8px;
    width: 40px;
    height: 40px;
  }

  .file-icon i {
    font-size: 18px;
  }

  .file-actions {
    width: 100%;
    justify-content: flex-end;
    margin-top: 8px;
    gap: 6px;
  }

  .file-actions .el-button {
    padding: 6px 8px;
    font-size: 12px;
  }

  .file-meta {
    flex-direction: column;
    gap: 4px;
  }

  /* 对话框适配 */
  .el-dialog {
    width: 90% !important;
    margin: 10px auto;
  }
}

/* 移动设备 (480px 以下) */
@media (max-width: 480px) {
  .shared-page {
    padding: 12px;
  }

  .shared-tabs {
    padding: 12px;
  }

  .tab-header {
    gap: 8px;
  }

  .file-item {
    padding: 10px;
  }

  .file-name {
    font-size: 13px;
  }

  .file-meta {
    font-size: 11px;
  }

  .tab-actions .el-button {
    width: 100%;
  }

  /* 优化对话框中的表单元素 */
  .el-form-item__label {
    font-size: 12px;
  }

  .el-input,
  .el-select,
  .el-date-picker,
  .el-radio,
  .el-radio__label {
    font-size: 12px;
  }
}

/* 极小屏幕设备 (iPhone SE 等) */
@media (max-width: 320px) {
  .shared-page {
    padding: 8px;
  }

  .shared-tabs {
    padding: 8px;
  }

  .file-item {
    padding: 8px;
  }

  .file-icon {
    width: 36px;
    height: 36px;
  }

  .file-icon i {
    font-size: 16px;
  }

  .file-name {
    font-size: 12px;
  }

  .file-meta {
    font-size: 10px;
    gap: 3px;
  }

  .file-actions {
    gap: 4px;
  }

  .file-actions .el-button {
    padding: 4px 6px;
    font-size: 11px;
  }

  /* 对话框进一步适配 */
  .el-dialog {
    width: 95% !important;
    margin: 5px auto;
  }

  .el-dialog__header {
    padding: 12px 16px;
  }

  .el-dialog__title {
    font-size: 16px;
  }

  .el-dialog__body {
    padding: 16px;
  }

  .dialog-footer {
    flex-direction: column;
    gap: 8px;
  }

  .dialog-footer .el-button {
    width: 100%;
  }
}
</style>