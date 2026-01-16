<template>
  <div class="favorites-page">
    <div class="page-header">
      <h2>我的收藏</h2>
      <div class="header-actions">
        <el-input v-model="searchQuery" placeholder="搜索收藏..." prefix-icon="el-icon-search" clearable
          @input="handleSearch" class="search-input" />
      </div>
    </div>

    <el-tabs v-model="activeTab" class="favorites-tabs">
      <el-tab-pane label="收藏的文件" name="files">
        <div class="favorites-list" v-if="filteredFiles.length > 0">
          <div class="file-item" v-for="item in filteredFiles" :key="item.id">
            <div class="file-icon" :class="getFileIconClass(item.type)">
              <i :class="getFileIcon(item.type)"></i>
            </div>
            <div class="file-info">
              <div class="file-name">{{ item.name }}</div>
              <div class="file-meta">
                <span class="meta-item">大小: {{ formatFileSize(item.size) }}</span>
                <span class="meta-item">收藏时间: {{ formatDate(item.favoriteTime) }}</span>
              </div>
            </div>
            <div class="file-actions">
              <el-button type="text" @click="downloadFile(item)">下载</el-button>
              <el-button type="text" @click="removeFavorite(item)">取消收藏</el-button>
            </div>
          </div>
        </div>
        <div class="empty-state" v-else>
          <el-empty description="暂无收藏的文件">
            <template #description>
              <div class="empty-description">
                <p>您还没有收藏任何文件</p>
                <p class="empty-tip">在文件列表中点击收藏按钮添加文件到收藏夹</p>
              </div>
            </template>
          </el-empty>
        </div>
      </el-tab-pane>

      <el-tab-pane label="收藏的文件夹" name="folders">
        <div class="favorites-list" v-if="filteredFolders.length > 0">
          <div class="file-item" v-for="item in filteredFolders" :key="item.id">
            <div class="file-icon folder-icon">
              <i class="el-icon-folder"></i>
            </div>
            <div class="file-info">
              <div class="file-name">{{ item.name }}</div>
              <div class="file-meta">
                <span class="meta-item">收藏时间: {{ formatDate(item.favoriteTime) }}</span>
              </div>
            </div>
            <div class="file-actions">
              <el-button type="text" @click="openFolder(item)">打开</el-button>
              <el-button type="text" @click="removeFavorite(item)">取消收藏</el-button>
            </div>
          </div>
        </div>
        <div class="empty-state" v-else>
          <el-empty description="暂无收藏的文件夹">
            <template #description>
              <div class="empty-description">
                <p>您还没有收藏任何文件夹</p>
                <p class="empty-tip">在文件夹列表中点击收藏按钮添加文件夹到收藏夹</p>
              </div>
            </template>
          </el-empty>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import Server from '@/utils/Server.js'
import { formatFileSize } from '@/utils/utils.js'

const router = useRouter()
const activeTab = ref('files')
const searchQuery = ref('')
const files = ref([])
const folders = ref([])

const filteredFiles = computed(() => {
  if (!searchQuery.value) return files.value
  const query = searchQuery.value.toLowerCase()
  return files.value.filter(file =>
    file.name.toLowerCase().includes(query)
  )
})

const filteredFolders = computed(() => {
  if (!searchQuery.value) return folders.value
  const query = searchQuery.value.toLowerCase()
  return folders.value.filter(folder =>
    folder.name.toLowerCase().includes(query)
  )
})

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

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const handleSearch = () => {
}

const downloadFile = async (item) => {
  try {
    const response = await Server.download(`/file/${item.id}/download`)
    const url = window.URL.createObjectURL(new Blob([response.data], { 
      type: response.headers['content-type'] || 'application/octet-stream' 
    }))
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', item.name)
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    ElMessage.success(`开始下载 "${item.name}"`)
  } catch (error) {
    ElMessage.error('下载失败')
  }
}

const openFolder = (item) => {
  router.push(`/user?folderId=${item.id}`)
}

const removeFavorite = async (item) => {
  try {
    if (item.type === 'folder') {
      await Server.delete(`/favorite/folder/${item.id}`)
    } else {
      await Server.delete(`/favorite/file/${item.id}`)
    }
    ElMessage.success('已取消收藏')
    await fetchFavorites()
  } catch (error) {
    ElMessage.error('取消收藏失败')
  }
}

const fetchFavorites = async () => {
  try {
    const response = await Server.get('/favorite')
    const favoritesData = response.data || []
    
    files.value = []
    folders.value = []
    
    for (const favorite of favoritesData) {
      if (favorite.fileId) {
        try {
          const fileResponse = await Server.get(`/file/${favorite.fileId}`)
          if (fileResponse.data) {
            files.value.push({
              ...fileResponse.data,
              id: favorite.fileId,
              favoriteId: favorite.id,
              favoriteTime: favorite.createTime,
              type: getFileTypeFromMimeType(fileResponse.data.mimeType)
            })
          }
        } catch (error) {
        }
      } else if (favorite.folderId) {
        try {
          const folderResponse = await Server.get(`/folder/${favorite.folderId}`)
          if (folderResponse.data) {
            folders.value.push({
              ...folderResponse.data,
              id: favorite.folderId,
              favoriteId: favorite.id,
              favoriteTime: favorite.createTime,
              type: 'folder'
            })
          }
        } catch (error) {
        }
      }
    }
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

onMounted(async () => {
  await fetchFavorites()
})
</script>

<style scoped>
.favorites-page {
  padding: 20px;
  background-color: #ffffff;
  min-height: 100vh;
  font-family: 'Inter', 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  font-size: 24px;
  font-weight: 600;
  color: #202124;
  margin: 0;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.search-input {
  width: 300px;
}

.favorites-tabs {
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  border: 1px solid #e5e7eb;
  padding: 20px;
}

.favorites-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.file-item {
  display: flex;
  align-items: center;
  padding: 16px;
  background-color: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
  transition: all 0.2s;
}

.file-item:hover {
  background-color: #f1f3f4;
  border-color: #d1d5db;
}

.file-icon {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  margin-right: 16px;
  font-size: 24px;
}

.file-icon.folder-icon {
  background-color: #fff3cd;
  color: #856404;
}

.file-icon.image-icon {
  background-color: #d1ecf1;
  color: #0c5460;
}

.file-icon.video-icon {
  background-color: #f8d7da;
  color: #721c24;
}

.file-icon.audio-icon {
  background-color: #d4edda;
  color: #155724;
}

.file-icon.pdf-icon {
  background-color: #f5c6cb;
  color: #721c24;
}

.file-icon.document-icon {
  background-color: #cce5ff;
  color: #004085;
}

.file-icon.archive-icon {
  background-color: #e2e3e5;
  color: #383d41;
}

.file-icon.code-icon {
  background-color: #d1ecf1;
  color: #0c5460;
}

.file-icon.other-icon {
  background-color: #e2e3e5;
  color: #383d41;
}

.file-info {
  flex: 1;
  min-width: 0;
}

.file-name {
  font-size: 16px;
  font-weight: 500;
  color: #202124;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.file-meta {
  display: flex;
  gap: 16px;
  font-size: 13px;
  color: #5f6368;
}

.file-actions {
  display: flex;
  gap: 8px;
}

.empty-state {
  padding: 60px 20px;
  text-align: center;
}

.empty-description {
  margin-top: 16px;
}

.empty-description p {
  margin: 8px 0;
  color: #5f6368;
}

.empty-tip {
  font-size: 13px;
  color: #9aa0a6;
}

@media (max-width: 768px) {
  .favorites-page {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .search-input {
    width: 100%;
  }

  .favorites-tabs {
    padding: 16px;
  }

  .file-item {
    padding: 12px;
  }

  .file-icon {
    width: 40px;
    height: 40px;
    font-size: 20px;
    margin-right: 12px;
  }

  .file-name {
    font-size: 14px;
  }

  .file-meta {
    flex-direction: column;
    gap: 4px;
    font-size: 12px;
  }

  .file-actions {
    flex-direction: column;
    gap: 4px;
  }
}
</style>
