<template>
    <div class="files-page">
        <!-- 文件操作工具栏 -->
        <div class="files-toolbar">
            <div class="toolbar-left">
                <el-button-group class="view-toggle">
                    <el-button :class="{ 'active-view': viewMode === 'grid' }" @click="viewMode = 'grid'" title="网格视图">
                        <el-icon>
                            <Grid />
                        </el-icon>
                    </el-button>
                    <el-button :class="{ 'active-view': viewMode === 'list' }" @click="viewMode = 'list'" title="列表视图">
                        <el-icon>
                            <List />
                        </el-icon>
                    </el-button>
                </el-button-group>
                <el-divider direction="vertical" class="toolbar-divider" />
                <div class="search-container">
                    <el-input v-model="searchQuery" placeholder="搜索文件..." class="search-input" clearable>
                        <template #prefix>
                            <el-icon>
                                <Search />
                            </el-icon>
                        </template>
                    </el-input>
                </div>
            </div>

            <div class="toolbar-right">
                <div class="header-actions">
                    <el-button type="primary" class="modern-btn" @click="handleUpload">
                        <el-icon>
                            <Upload />
                        </el-icon>
                        上传文件
                    </el-button>
                    <el-button class="modern-btn" @click="handleNewFolder">
                        新建文件夹
                    </el-button>
                </div>
                <el-dropdown @command="handleSortCommand" class="sort-dropdown">
                    <el-button class="sort-button">
                        {{sortOptions.find(opt => opt.value === sortBy)?.label}}
                        <el-icon class="el-icon--right">
                            <ArrowDown />
                        </el-icon>
                    </el-button>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item v-for="option in sortOptions" :key="option.value" :command="option.value"
                                :class="{ 'active-sort': sortBy === option.value }">
                                {{ option.label }}
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </div>
        </div>

        <!-- 面包屑导航 -->
        <div class="breadcrumb-container">
            <el-breadcrumb separator="/" class="modern-breadcrumb">
                <el-breadcrumb-item>
                    <a @click="navigateToFolder('')" class="breadcrumb-link">我的文件</a>
                </el-breadcrumb-item>
                <el-breadcrumb-item v-for="(folder, index) in currentPath" :key="index">
                    <a @click="navigateToFolder(currentPath.slice(0, index + 1).join('/'))" class="breadcrumb-link">{{
                        folder
                    }}</a>
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <!-- 文件列表 - 网格视图 -->
        <div v-if="viewMode === 'grid'" class="files-grid">
            <div v-for="item in filteredFiles" :key="item.id" class="file-item" @click="handleItemClick(item)"
                @contextmenu.prevent="handleRightClick(item, $event)">
                <div class="file-icon-container">
                    <el-icon class="file-icon">
                        <component :is="getFileIconComponent(item.type)" />
                    </el-icon>
                </div>
                <div class="file-name" :title="item.name">{{ item.name }}</div>
                <div class="file-meta">
                    {{ item.type === 'folder' ? '' : formatFileSize(item.size) }}
                </div>
                <div class="file-actions">
                    <el-dropdown @command="(command) => handleFileCommand(command, item)">
                        <el-button type="text" size="small" class="file-action-btn" @click.stop>
                            <el-icon>
                                <More />
                            </el-icon>
                        </el-button>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item command="download">
                                    <el-icon>
                                        <Download />
                                    </el-icon>
                                    下载
                                </el-dropdown-item>
                                <el-dropdown-item command="share">
                                    <el-icon>
                                        <Share />
                                    </el-icon>
                                    分享
                                </el-dropdown-item>
                                <el-dropdown-item command="rename">
                                    <el-icon>
                                        <Edit />
                                    </el-icon>
                                    重命名
                                </el-dropdown-item>
                                <el-dropdown-item command="move">
                                    <el-icon>
                                        <Position />
                                    </el-icon>
                                    移动
                                </el-dropdown-item>
                                <el-dropdown-item command="copy">
                                    <el-icon>
                                        <CopyDocument />
                                    </el-icon>
                                    复制
                                </el-dropdown-item>
                                <el-dropdown-item command="delete" divided class="danger-item">
                                    <el-icon>
                                        <Delete />
                                    </el-icon>
                                    删除
                                </el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>
            </div>

            <!-- 网格视图空状态 -->
            <div v-if="filteredFiles.length === 0" class="empty-state">
                <el-empty description="暂无文件"></el-empty>
            </div>
        </div>

        <!-- 文件列表 - 列表视图 -->
        <div v-else class="files-list">
            <el-table :data="filteredFiles" class="modern-table" @row-contextmenu="handleRowRightClick" stripe>
                <el-table-column width="50">
                    <template #default="{ row }">
                        <el-icon class="table-file-icon">
                            <component :is="getFileIconComponent(row.type)" />
                        </el-icon>
                    </template>
                </el-table-column>
                <el-table-column label="名称" min-width="300">
                    <template #default="{ row }">
                        <span class="file-name" @click="handleItemClick(row)">{{ row.name }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="大小" width="120">
                    <template #default="{ row }">
                        {{ row.type === 'folder' ? '-' : formatFileSize(row.size) }}
                    </template>
                </el-table-column>
                <el-table-column label="修改时间" width="180">
                    <template #default="{ row }">
                        {{ formatDate(row.updatedAt) }}
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="180">
                    <template #default="{ row }">
                        <el-button type="text" class="table-action-btn" @click="handleFileCommand('download', row)">
                            <el-icon>
                                <Download />
                            </el-icon>
                        </el-button>
                        <el-button type="text" class="table-action-btn" @click="handleFileCommand('share', row)">
                            <el-icon>
                                <Share />
                            </el-icon>
                        </el-button>
                        <el-button type="text" class="table-action-btn" @click="handleFileCommand('rename', row)">
                            <el-icon>
                                <Edit />
                            </el-icon>
                        </el-button>
                        <el-dropdown @command="(command) => handleFileCommand(command, row)">
                            <el-button type="text" class="table-action-btn">
                                <el-icon>
                                    <More />
                                </el-icon>
                            </el-button>
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item command="move">
                                        <el-icon>
                                            <Position />
                                        </el-icon>
                                        移动
                                    </el-dropdown-item>
                                    <el-dropdown-item command="copy">
                                        <el-icon>
                                            <Copy />
                                        </el-icon>
                                        复制
                                    </el-dropdown-item>
                                    <el-dropdown-item command="delete" divided class="danger-item">
                                        <el-icon>
                                            <Delete />
                                        </el-icon>
                                        删除
                                    </el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <!-- 上传文件对话框 -->
        <el-dialog v-model="uploadDialogVisible" title="上传文件" width="500px" class="modern-dialog">
            <div class="upload-container">
                <el-upload class="upload-area" drag action="#" :auto-upload="false" :on-change="handleFileChange"
                    :file-list="fileList" multiple>
                    <el-icon class="upload-icon">
                        <Upload />
                    </el-icon>
                    <div class="el-upload__text">
                        将文件拖到此处，或<em>点击选择文件</em>
                    </div>
                </el-upload>
            </div>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="uploadDialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="confirmUpload" :loading="uploading" class="modern-btn">
                        上传
                    </el-button>
                </span>
            </template>
        </el-dialog>

        <!-- 新建文件夹对话框 -->
        <el-dialog v-model="folderDialogVisible" title="新建文件夹" width="400px" class="modern-dialog">
            <el-form :model="folderForm" :rules="folderRules" ref="folderFormRef">
                <el-form-item label="文件夹名称" prop="name">
                    <el-input v-model="folderForm.name" placeholder="请输入文件夹名称" class="modern-input"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="folderDialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="confirmCreateFolder" :loading="creatingFolder" class="modern-btn">
                        创建
                    </el-button>
                </span>
            </template>
        </el-dialog>

        <!-- 重命名对话框 -->
        <el-dialog v-model="renameDialogVisible" title="重命名" width="400px" class="modern-dialog">
            <el-form :model="renameForm" :rules="renameRules" ref="renameFormRef">
                <el-form-item label="新名称" prop="name">
                    <el-input v-model="renameForm.name" placeholder="请输入新名称" class="modern-input"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="renameDialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="confirmRename" :loading="renaming" class="modern-btn">
                        确定
                    </el-button>
                </span>
            </template>
        </el-dialog>

        <!-- 右键菜单 -->
        <el-menu v-if="contextMenuVisible" class="context-menu modern-context-menu"
            :style="{ left: contextMenuX + 'px', top: contextMenuY + 'px' }" @select="handleContextMenuSelect">
            <el-menu-item index="download">
                <el-icon>
                    <Download />
                </el-icon>
                <span>下载</span>
            </el-menu-item>
            <el-menu-item index="share">
                <el-icon>
                    <Share />
                </el-icon>
                <span>分享</span>
            </el-menu-item>
            <el-menu-item index="rename">
                <el-icon>
                    <Edit />
                </el-icon>
                <span>重命名</span>
            </el-menu-item>
            <el-menu-item index="move">
                <el-icon>
                    <Position />
                </el-icon>
                <span>移动</span>
            </el-menu-item>
            <el-menu-item index="copy">
                <el-icon>
                    <Copy />
                </el-icon>
                <span>复制</span>
            </el-menu-item>
            <el-menu-item index="delete" divided class="danger-item">
                <el-icon>
                    <Delete />
                </el-icon>
                <span>删除</span>
            </el-menu-item>
        </el-menu>
    </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
    Upload,
    FolderAdd,
    Grid,
    List,
    Search,
    ArrowDown,
    Download,
    Share,
    Edit,
    More,
    CopyDocument,
    Position,
    Delete,
    Folder,
    Document,
    Picture,
    VideoPlay,
    Headset,
    Files,
    DataLine
} from '@element-plus/icons-vue'
import Server from '@/utils/Server.js'

const router = useRouter()

// 视图模式
const viewMode = ref('grid') // 'grid' 或 'list'

// 排序选项
const sortBy = ref('name')
const sortOptions = [
    { value: 'name', label: '按名称排序' },
    { value: 'size', label: '按大小排序' },
    { value: 'date', label: '按修改时间排序' },
    { value: 'type', label: '按类型排序' }
]

// 搜索查询
const searchQuery = ref('')

// 当前路径
const currentPath = ref([])

// 文件列表
const files = ref([])

// 分页相关
const currentPage = ref(1)
const pageSize = ref(20)
const totalFiles = ref(0)
const pageSizes = [10, 20, 50, 100]

// 上传相关
const uploadDialogVisible = ref(false)
const uploading = ref(false)
const fileList = ref([])

// 新建文件夹相关
const folderDialogVisible = ref(false)
const creatingFolder = ref(false)
const folderForm = reactive({
    name: ''
})
const folderFormRef = ref(null)
const folderRules = {
    name: [
        { required: true, message: '请输入文件夹名称', trigger: 'blur' },
        { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
    ]
}

// 重命名相关
const renameDialogVisible = ref(false)
const renaming = ref(false)
const renameForm = reactive({
    name: ''
})
const renameFormRef = ref(null)
const renameRules = {
    name: [
        { required: true, message: '请输入新名称', trigger: 'blur' },
        { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
    ]
}

// 当前选中的文件
const selectedItem = ref(null)

// 右键菜单相关
const contextMenuVisible = ref(false)
const contextMenuX = ref(0)
const contextMenuY = ref(0)

// 计算属性：过滤和排序后的文件列表
const filteredFiles = computed(() => {
    let result = [...files.value]

    // 过滤
    if (searchQuery.value) {
        const query = searchQuery.value.toLowerCase()
        result = result.filter(file =>
            file.name.toLowerCase().includes(query)
        )
    }

    // 排序
    result.sort((a, b) => {
        switch (sortBy.value) {
            case 'name':
                return a.name.localeCompare(b.name)
            case 'size':
                return b.size - a.size
            case 'date':
                return new Date(b.updatedAt) - new Date(a.updatedAt)
            case 'type':
                return a.type.localeCompare(b.type)
            default:
                return 0
        }
    })

    return result
})

// 页面加载时获取数据
onMounted(async () => {
    await loadFiles()
    // 监听全局点击事件，隐藏右键菜单
    document.addEventListener('click', hideContextMenu)
})

onBeforeUnmount(() => {
    // 移除事件监听
    document.removeEventListener('click', hideContextMenu)
})

// 加载文件列表
const loadFiles = async () => {
    try {
        const path = currentPath.value.join('/')
        // 添加分页参数
        const response = await Server.get('/api/files', {
            params: {
                path,
                page: currentPage.value,
                pageSize: pageSize.value
            }
        })
        if (response.data) {
            files.value = response.data.content || response.data.files || []
            totalFiles.value = response.data.totalElements || response.data.total || 0
        } else {
            ElMessage.error('加载文件列表失败')
        }
    } catch (error) {
        console.error('加载文件列表失败:', error)
        ElMessage.error('加载文件列表失败')
    }
}

// 导航到文件夹
const navigateToFolder = (path) => {
    if (typeof path === 'string') {
        currentPath.value = path ? path.split('/') : []
    } else {
        currentPath.value = path
    }
    loadFiles()
}

// 处理文件/文件夹点击
const handleItemClick = (item) => {
    if (item.type === 'folder') {
        // 如果是文件夹，进入文件夹
        currentPath.value.push(item.name)
        loadFiles()
    } else {
        // 如果是文件，打开文件（这里可以根据文件类型做不同处理）
        ElMessage.info(`打开文件: ${item.name}`)
    }
}

// 处理上传按钮点击
const handleUpload = () => {
    uploadDialogVisible.value = true
    fileList.value = []
}

// 处理文件选择变化
const handleFileChange = (file, uploadFiles) => {
    fileList.value = uploadFiles
}

// 确认上传
const confirmUpload = async () => {
    if (fileList.value.length === 0) {
        ElMessage.warning('请选择要上传的文件')
        return
    }

    uploading.value = true
    try {
        const path = currentPath.value.join('/')
        const formData = new FormData()
        
        // 添加所有选中的文件
        fileList.value.forEach(file => {
            formData.append('files', file.raw || file)
        })
        
        // 添加路径参数
        if (path) {
            formData.append('path', path)
        }
        
        await Server.post('/api/files/upload', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            },
            onUploadProgress: (progressEvent) => {
                const percentCompleted = Math.round(
                    (progressEvent.loaded * 100) / progressEvent.total
                )
                // 这里可以更新上传进度显示
                console.log(`上传进度: ${percentCompleted}%`)
            }
        })
        
        ElMessage.success(`成功上传 ${fileList.value.length} 个文件`)
        uploadDialogVisible.value = false
        // 重新加载数据
        await loadFiles()
    } catch (error) {
        console.error('上传失败:', error)
        ElMessage.error('上传失败')
    } finally {
        uploading.value = false
    }
}

// 处理新建文件夹按钮点击
const handleNewFolder = () => {
    folderDialogVisible.value = true
    folderForm.name = ''
}

// 确认创建文件夹
const confirmCreateFolder = async () => {
    if (!folderFormRef.value) return

    await folderFormRef.value.validate(async (valid) => {
        if (valid) {
            creatingFolder.value = true
            try {
                const parentId = currentPath.value.length > 0 ? 
                    currentPath.value[currentPath.value.length - 1] : 1
                await Server.post('/api/folders', {
                    name: folderForm.name,
                    parentId: parentId
                })
                ElMessage.success('文件夹创建成功')
                folderDialogVisible.value = false
                await loadFiles()
            } catch (error) {
                console.error('创建文件夹失败:', error)
                ElMessage.error('创建文件夹失败')
            } finally {
                creatingFolder.value = false
            }
        }
    })
}

// 处理文件命令
const handleFileCommand = async (command, item) => {
    selectedItem.value = item

    switch (command) {
        case 'download':
            try {
                const response = await Server.get(`/api/files/${item.id}/download`, {
                    responseType: 'blob'
                })
                // 创建下载链接
                const url = window.URL.createObjectURL(new Blob([response.data]))
                const link = document.createElement('a')
                link.href = url
                link.setAttribute('download', item.name)
                document.body.appendChild(link)
                link.click()
                document.body.removeChild(link)
                window.URL.revokeObjectURL(url)
                ElMessage.success(`下载完成: ${item.name}`)
            } catch (error) {
                console.error('下载失败:', error)
                ElMessage.error('下载失败')
            }
            break

        case 'share':
            try {
                const response = await Server.post('/api/share/create', {
                    targetId: item.id,
                    targetType: item.type === 'folder' ? 'folder' : 'file',
                    shareType: 0, // 默认公开分享
                    password: null,
                    expireTime: null
                })
                ElMessage.success('分享链接已生成')
                // 可以在这里处理分享链接的显示
                console.log('分享链接:', response.data.shareCode)
            } catch (error) {
                console.error('分享失败:', error)
                ElMessage.error('分享失败')
            }
            break

        case 'rename':
            renameDialogVisible.value = true
            renameForm.name = item.name
            break

        case 'move':
            ElMessage.info(`移动文件: ${item.name}`)
            // 模拟移动
            await new Promise(resolve => setTimeout(resolve, 1000))
            ElMessage.success('文件移动成功')
            break

        case 'copy':
            ElMessage.info(`复制文件: ${item.name}`)
            // 模拟复制
            await new Promise(resolve => setTimeout(resolve, 1000))
            ElMessage.success('文件复制成功')
            break

        case 'delete':
            try {
                await ElMessageBox.confirm(
                    `确定要删除 "${item.name}" 吗？此操作不可恢复。`,
                    '确认删除',
                    {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning',
                    }
                )
                // 删除文件或文件夹
                if (item.type === 'folder') {
                    await Server.delete(`/api/folders/${item.id}`)
                } else {
                    await Server.delete(`/api/files/${item.id}`)
                }
                ElMessage.success('删除成功')
                // 重新加载数据
                await loadFiles()
            } catch (error) {
                if (error !== 'cancel') {
                    console.error('删除失败:', error)
                    ElMessage.error('删除失败')
                }
            }
            break
    }
}

// 确认重命名
const confirmRename = async () => {
    if (!renameFormRef.value) return

    await renameFormRef.value.validate(async (valid) => {
        if (valid) {
            renaming.value = true
            try {
                const item = selectedItem.value
                if (item.type === 'folder') {
                    await Server.put(`/api/folders/${item.id}/rename`, null, {
                        params: { name: renameForm.name }
                    })
                } else {
                    await Server.put(`/api/files/${item.id}/rename`, null, {
                        params: { name: renameForm.name }
                    })
                }
                ElMessage.success('重命名成功')
                renameDialogVisible.value = false
                // 重新加载数据
                await loadFiles()
            } catch (error) {
                console.error('重命名失败:', error)
                ElMessage.error('重命名失败')
            } finally {
                renaming.value = false
            }
        }
    })
}

// 处理排序命令
const handleSortCommand = (command) => {
    sortBy.value = command
}

// 处理右键点击
const handleRightClick = (item, event) => {
    selectedItem.value = item
    contextMenuX.value = event.clientX
    contextMenuY.value = event.clientY
    contextMenuVisible.value = true
}

// 处理表格行右键点击
const handleRowRightClick = (row, column, event) => {
    event.preventDefault()
    selectedItem.value = row
    contextMenuX.value = event.clientX
    contextMenuY.value = event.clientY
    contextMenuVisible.value = true
}

// 隐藏右键菜单
const hideContextMenu = () => {
    contextMenuVisible.value = false
}

// 处理右键菜单选择
const handleContextMenuSelect = (index) => {
    hideContextMenu()
    handleFileCommand(index, selectedItem.value)
}

// 获取文件图标组件
const getFileIconComponent = (type) => {
    const iconMap = {
        'folder': 'Folder',
        'image': 'Picture',
        'video': 'VideoPlay',
        'audio': 'Headset',
        'document': 'Document',
        'archive': 'Files',
        'spreadsheet': 'DataLine',
        'default': 'Document'
    }
    return iconMap[type] || iconMap.default
}

// 格式化文件大小
const formatFileSize = (bytes) => {
    if (bytes === 0) return '0 B'
    const k = 1024
    const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
    const i = Math.floor(Math.log(bytes) / Math.log(k))
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 格式化日期
const formatDate = (dateString) => {
    const date = new Date(dateString)
    return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN', {
        hour: '2-digit',
        minute: '2-digit'
    })
}

// 处理分页变化
const handleSizeChange = (newSize) => {
    pageSize.value = newSize
    currentPage.value = 1
    loadFiles()
}

const handleCurrentChange = (newPage) => {
    currentPage.value = newPage
    loadFiles()
}
</script>

<style scoped>
.files-page {
    padding: 20px;
    background-color: #f5f7fa;
    min-height: calc(100vh - 60px);
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.files-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
    background: white;
    padding: 20px;
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.files-header h3 {
    margin: 0;
    font-size: 24px;
    font-weight: 600;
    color: #1f2937;
}

.header-actions {
    display: flex;
    gap: 12px;
}

.modern-btn {
    border-radius: 8px;
    font-weight: 500;
    transition: all 0.3s ease;
}

.modern-btn:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.files-toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    background: white;
    padding: 16px 20px;
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.toolbar-left {
    display: flex;
    align-items: center;
    gap: 16px;
}

.view-toggle {
    border-radius: 8px;
    overflow: hidden;
}

.view-toggle .el-button {
    border-radius: 0;
    border: 1px solid #e4e7ed;
    background: white;
    color: #606266;
    transition: all 0.3s ease;
}

.view-toggle .el-button:first-child {
    border-radius: 8px 0 0 8px;
}

.view-toggle .el-button:last-child {
    border-radius: 0 8px 8px 0;
}

.view-toggle .el-button.active-view {
    background: #409eff;
    color: white;
    border-color: #409eff;
}

.toolbar-divider {
    height: 24px;
    margin: 0 8px;
}

.search-container {
    width: 300px;
}

.search-input {
    border-radius: 8px;
}

.search-input :deep(.el-input__wrapper) {
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.toolbar-right {
    display: flex;
    align-items: center;
    gap: 16px;
}

.sort-dropdown {
    border-radius: 8px;
}

.sort-button {
    border-radius: 8px;
    border: 1px solid #e4e7ed;
    background: white;
    color: #606266;
    font-weight: 500;
}

.breadcrumb-container {
    margin-bottom: 10px;
    background: white;
    padding: 16px 20px;
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.modern-breadcrumb {
    font-size: 14px;
}

.breadcrumb-link {
    color: #409eff;
    text-decoration: none;
    cursor: pointer;
    transition: color 0.3s ease;
}

.breadcrumb-link:hover {
    color: #337ecc;
}

.files-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 20px;
    background: white;
    padding: 20px;
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.file-item {
    position: relative;
    background: #f8fafc;
    border: 2px solid transparent;
    border-radius: 12px;
    padding: 20px;
    text-align: center;
    cursor: pointer;
    transition: all 0.3s ease;
    min-height: 140px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.file-item:hover {
    border-color: #409eff;
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(64, 158, 255, 0.15);
}

.file-icon-container {
    margin-bottom: 12px;
}

.file-icon {
    font-size: 48px;
    color: #409eff;
}

.file-name {
    font-size: 14px;
    font-weight: 500;
    color: #1f2937;
    margin-bottom: 8px;
    word-break: break-all;
    line-height: 1.4;
}

.file-meta {
    font-size: 12px;
    color: #6b7280;
    margin-bottom: 8px;
}

.file-actions {
    position: absolute;
    top: 8px;
    right: 8px;
    opacity: 0;
    transition: opacity 0.3s ease;
}

.file-item:hover .file-actions {
    opacity: 1;
}

.file-action-btn {
    padding: 4px;
    border-radius: 6px;
    background: rgba(255, 255, 255, 0.9);
    backdrop-filter: blur(10px);
}

.empty-state {
    grid-column: 1 / -1;
    text-align: center;
    padding: 60px 20px;
}

.files-list {
    background: white;
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
    overflow: hidden;
}

.modern-table {
    border-radius: 12px;
    overflow: hidden;
}

.modern-table :deep(.el-table__header) {
    background: #f8fafc;
}

.modern-table :deep(.el-table__row) {
    transition: background-color 0.3s ease;
}

.modern-table :deep(.el-table__row:hover) {
    background-color: #f0f9ff;
}

.table-file-icon {
    font-size: 24px;
    color: #409eff;
}

.file-name {
    color: #409eff;
    cursor: pointer;
    transition: color 0.3s ease;
}

.file-name:hover {
    color: #337ecc;
    text-decoration: underline;
}

.table-action-btn {
    padding: 4px 8px;
    border-radius: 6px;
    transition: all 0.3s ease;
}

.table-action-btn:hover {
    background: #f0f9ff;
}

.modern-dialog {
    border-radius: 12px;
}

.modern-dialog :deep(.el-dialog__header) {
    background: #f8fafc;
    border-radius: 12px 12px 0 0;
    padding: 20px;
    margin: 0;
}

.modern-dialog :deep(.el-dialog__body) {
    padding: 20px;
}

.modern-dialog :deep(.el-dialog__footer) {
    background: #f8fafc;
    border-radius: 0 0 12px 12px;
    padding: 20px;
    margin: 0;
}

.upload-container {
    padding: 20px;
}

.upload-area {
    border-radius: 12px;
    border: 2px dashed #e4e7ed;
    transition: border-color 0.3s ease;
}

.upload-area:hover {
    border-color: #409eff;
}

.upload-icon {
    font-size: 48px;
    color: #409eff;
    margin-bottom: 16px;
}

.modern-input :deep(.el-input__wrapper) {
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.context-menu {
    position: fixed;
    z-index: 2000;
    border-radius: 8px;
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    border: 1px solid #e4e7ed;
    background: white;
}

.modern-context-menu {
    min-width: 160px;
    border-radius: 8px;
    overflow: hidden;
}

.modern-context-menu :deep(.el-menu-item) {
    height: 40px;
    line-height: 40px;
    font-size: 14px;
    display: flex;
    align-items: center;
    gap: 8px;
    transition: all 0.3s ease;
}

.modern-context-menu :deep(.el-menu-item:hover) {
    background: #f0f9ff;
    color: #409eff;
}

.danger-item {
    color: #f56c6c;
}

.danger-item:hover {
    background: #fef0f0 !important;
    color: #f56c6c !important;
}

.dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
}

/* 响应式设计 */
@media (max-width: 768px) {
    .files-page {
        padding: 12px;
    }

    .files-header {
        flex-direction: column;
        gap: 16px;
        align-items: stretch;
    }

    .files-toolbar {
        flex-direction: column;
        gap: 16px;
    }

    .toolbar-left {
        flex-direction: column;
        width: 100%;
    }

    .search-container {
        width: 100%;
    }

    .files-grid {
        grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
        gap: 12px;
        padding: 12px;
    }

    .file-item {
        padding: 16px;
        min-height: 120px;
    }

    .file-icon {
        font-size: 36px;
    }
}
</style>