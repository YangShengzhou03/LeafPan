<template>
    <div class="files-page">
        <div class="files-header">
            <h1>我的文件</h1>
            <div class="header-actions">
                <el-button type="primary" @click="handleUpload">
                    <i class="icon-upload"></i>
                    上传文件
                </el-button>
                <el-button @click="handleNewFolder">
                    <i class="icon-folder-add"></i>
                    新建文件夹
                </el-button>
            </div>
        </div>

        <!-- 文件操作工具栏 -->
        <div class="files-toolbar">
            <div class="toolbar-left">
                <el-button-group>
                    <el-button 
                        :type="viewMode === 'grid' ? 'primary' : ''" 
                        @click="viewMode = 'grid'"
                        title="网格视图"
                    >
                        <i class="icon-grid"></i>
                    </el-button>
                    <el-button 
                        :type="viewMode === 'list' ? 'primary' : ''" 
                        @click="viewMode = 'list'"
                        title="列表视图"
                    >
                        <i class="icon-list"></i>
                    </el-button>
                </el-button-group>
                <el-divider direction="vertical" />
                <el-input
                    v-model="searchQuery"
                    placeholder="搜索文件..."
                    prefix-icon="Search"
                    clearable
                    style="width: 250px;"
                />
            </div>
            <div class="toolbar-right">
                <el-dropdown @command="handleSortCommand">
                    <el-button>
                        {{ sortOptions.find(opt => opt.value === sortBy)?.label }}
                        <i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item 
                                v-for="option in sortOptions" 
                                :key="option.value"
                                :command="option.value"
                                :class="{ active: sortBy === option.value }"
                            >
                                {{ option.label }}
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </div>
        </div>

        <!-- 面包屑导航 -->
        <div class="breadcrumb-container">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <a @click="navigateToFolder('')">我的文件</a>
                </el-breadcrumb-item>
                <el-breadcrumb-item v-for="(folder, index) in currentPath" :key="index">
                    <a @click="navigateToFolder(currentPath.slice(0, index + 1).join('/'))">{{ folder }}</a>
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <!-- 文件列表 - 网格视图 -->
        <div v-if="viewMode === 'grid'" class="files-grid">
            <div 
                v-for="item in filteredFiles" 
                :key="item.id" 
                class="file-item"
                @click="handleItemClick(item)"
                @contextmenu.prevent="handleRightClick(item, $event)"
            >
                <div class="file-icon">
                    <i :class="getFileIcon(item.type)"></i>
                </div>
                <div class="file-name" :title="item.name">{{ item.name }}</div>
                <div class="file-meta">
                    {{ item.type === 'folder' ? '' : formatFileSize(item.size) }}
                </div>
                <div class="file-actions">
                    <el-dropdown @command="(command) => handleFileCommand(command, item)">
                        <el-button type="text" size="small" @click.stop>
                            <i class="icon-more"></i>
                        </el-button>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item command="download">下载</el-dropdown-item>
                                <el-dropdown-item command="share">分享</el-dropdown-item>
                                <el-dropdown-item command="rename">重命名</el-dropdown-item>
                                <el-dropdown-item command="move">移动</el-dropdown-item>
                                <el-dropdown-item command="copy">复制</el-dropdown-item>
                                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>
            </div>
        </div>

        <!-- 文件列表 - 列表视图 -->
        <div v-else class="files-list">
            <el-table :data="filteredFiles" style="width: 100%" @row-contextmenu="handleRowRightClick">
                <el-table-column width="50">
                    <template #default="{ row }">
                        <i :class="getFileIcon(row.type)"></i>
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
                        <el-button type="text" @click="handleFileCommand('download', row)">
                            <i class="icon-download"></i>
                        </el-button>
                        <el-button type="text" @click="handleFileCommand('share', row)">
                            <i class="icon-share"></i>
                        </el-button>
                        <el-button type="text" @click="handleFileCommand('rename', row)">
                            <i class="icon-edit"></i>
                        </el-button>
                        <el-dropdown @command="(command) => handleFileCommand(command, row)">
                            <el-button type="text">
                                <i class="icon-more"></i>
                            </el-button>
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item command="move">移动</el-dropdown-item>
                                    <el-dropdown-item command="copy">复制</el-dropdown-item>
                                    <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <!-- 上传文件对话框 -->
        <el-dialog
            v-model="uploadDialogVisible"
            title="上传文件"
            width="500px"
        >
            <el-upload
                class="upload-area"
                drag
                action="#"
                :auto-upload="false"
                :on-change="handleFileChange"
                :file-list="fileList"
                multiple
            >
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">
                    将文件拖到此处，或<em>点击选择文件</em>
                </div>
            </el-upload>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="uploadDialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="confirmUpload" :loading="uploading">
                        上传
                    </el-button>
                </span>
            </template>
        </el-dialog>

        <!-- 新建文件夹对话框 -->
        <el-dialog
            v-model="folderDialogVisible"
            title="新建文件夹"
            width="400px"
        >
            <el-form :model="folderForm" :rules="folderRules" ref="folderFormRef">
                <el-form-item label="文件夹名称" prop="name">
                    <el-input v-model="folderForm.name" placeholder="请输入文件夹名称"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="folderDialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="confirmCreateFolder" :loading="creatingFolder">
                        创建
                    </el-button>
                </span>
            </template>
        </el-dialog>

        <!-- 重命名对话框 -->
        <el-dialog
            v-model="renameDialogVisible"
            title="重命名"
            width="400px"
        >
            <el-form :model="renameForm" :rules="renameRules" ref="renameFormRef">
                <el-form-item label="新名称" prop="name">
                    <el-input v-model="renameForm.name" placeholder="请输入新名称"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="renameDialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="confirmRename" :loading="renaming">
                        确定
                    </el-button>
                </span>
            </template>
        </el-dialog>

        <!-- 右键菜单 -->
        <el-menu
            v-if="contextMenuVisible"
            class="context-menu"
            :style="{ left: contextMenuX + 'px', top: contextMenuY + 'px' }"
            @select="handleContextMenuSelect"
        >
            <el-menu-item index="download">
                <i class="icon-download"></i>
                <span>下载</span>
            </el-menu-item>
            <el-menu-item index="share">
                <i class="icon-share"></i>
                <span>分享</span>
            </el-menu-item>
            <el-menu-item index="rename">
                <i class="icon-edit"></i>
                <span>重命名</span>
            </el-menu-item>
            <el-menu-item index="move">
                <i class="icon-move"></i>
                <span>移动</span>
            </el-menu-item>
            <el-menu-item index="copy">
                <i class="icon-copy"></i>
                <span>复制</span>
            </el-menu-item>
            <el-menu-item index="delete" divided>
                <i class="icon-delete"></i>
                <span>删除</span>
            </el-menu-item>
        </el-menu>
    </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { mockApiService } from '@/utils/mockApiService'

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
        const response = await mockApiService.getFiles(path)
        if (response.success) {
            files.value = response.data
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
        // 模拟上传
        await new Promise(resolve => setTimeout(resolve, 1500))
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
                // 模拟创建文件夹
                await new Promise(resolve => setTimeout(resolve, 1000))
                ElMessage.success(`成功创建文件夹 "${folderForm.name}"`)
                folderDialogVisible.value = false
                // 重新加载数据
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
const handleFileCommand = (command, item) => {
    selectedItem.value = item
    
    switch (command) {
        case 'download':
            handleDownload(item)
            break
        case 'share':
            handleShare(item)
            break
        case 'rename':
            handleRename(item)
            break
        case 'move':
            handleMove(item)
            break
        case 'copy':
            handleCopy(item)
            break
        case 'delete':
            handleDelete(item)
            break
    }
}

// 处理下载
const handleDownload = async (item) => {
    try {
        ElMessage.info(`正在下载 "${item.name}"...`)
        // 模拟下载
        await new Promise(resolve => setTimeout(resolve, 1500))
        ElMessage.success(`文件 "${item.name}" 下载完成`)
    } catch (error) {
        console.error('下载失败:', error)
        ElMessage.error('下载失败')
    }
}

// 处理分享
const handleShare = async (item) => {
    try {
        // 模拟分享
        await new Promise(resolve => setTimeout(resolve, 500))
        ElMessage.success(`已生成 "${item.name}" 的分享链接`)
    } catch (error) {
        console.error('分享失败:', error)
        ElMessage.error('分享失败')
    }
}

// 处理重命名
const handleRename = (item) => {
    selectedItem.value = item
    renameForm.name = item.name
    renameDialogVisible.value = true
}

// 确认重命名
const confirmRename = async () => {
    if (!renameFormRef.value) return

    await renameFormRef.value.validate(async (valid) => {
        if (valid) {
            renaming.value = true
            try {
                // 模拟重命名
                await new Promise(resolve => setTimeout(resolve, 500))
                ElMessage.success(`已将 "${selectedItem.value.name}" 重命名为 "${renameForm.name}"`)
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

// 处理移动
const handleMove = (item) => {
    ElMessage.info(`移动 "${item.name}" 功能正在开发中`)
}

// 处理复制
const handleCopy = (item) => {
    ElMessage.info(`复制 "${item.name}" 功能正在开发中`)
}

// 处理删除
const handleDelete = async (item) => {
    try {
        await ElMessageBox.confirm(
            `确定要删除 "${item.name}" 吗？`,
            '删除确认',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }
        )
        
        // 模拟删除
        await new Promise(resolve => setTimeout(resolve, 500))
        ElMessage.success(`已删除 "${item.name}"`)
        // 重新加载数据
        await loadFiles()
    } catch (error) {
        if (error !== 'cancel') {
            console.error('删除失败:', error)
            ElMessage.error('删除失败')
        }
    }
}

// 处理排序命令
const handleSortCommand = (command) => {
    sortBy.value = command
}

// 处理右键点击
const handleRightClick = (item, event) => {
    event.preventDefault()
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
    return date.toLocaleString()
}

// 获取文件图标
const getFileIcon = (type) => {
    const iconMap = {
        'folder': 'icon-folder',
        'pdf': 'icon-file-pdf',
        'doc': 'icon-file-word',
        'docx': 'icon-file-word',
        'xls': 'icon-file-excel',
        'xlsx': 'icon-file-excel',
        'ppt': 'icon-file-ppt',
        'pptx': 'icon-file-ppt',
        'jpg': 'icon-file-image',
        'jpeg': 'icon-file-image',
        'png': 'icon-file-image',
        'gif': 'icon-file-image',
        'mp4': 'icon-file-video',
        'avi': 'icon-file-video',
        'mkv': 'icon-file-video',
        'mp3': 'icon-file-audio',
        'wav': 'icon-file-audio',
        'zip': 'icon-file-archive',
        'rar': 'icon-file-archive',
        'txt': 'icon-file-text',
        'default': 'icon-file'
    }
    return iconMap[type] || iconMap.default
}
</script>

<style scoped>
.files-page {
    padding: 20px;
}

.files-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.header-actions {
    display: flex;
    gap: 10px;
}

.files-toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.toolbar-left {
    display: flex;
    align-items: center;
    gap: 10px;
}

.breadcrumb-container {
    margin-bottom: 20px;
}

/* 网格视图样式 */
.files-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 20px;
}

.file-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 15px;
    background-color: #fff;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.3s;
    position: relative;
}

.file-item:hover {
    background-color: #f5f7fa;
    transform: translateY(-2px);
}

.file-icon {
    font-size: 48px;
    color: #409eff;
    margin-bottom: 10px;
}

.file-name {
    font-size: 14px;
    color: #303133;
    text-align: center;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    width: 100%;
    margin-bottom: 5px;
}

.file-meta {
    font-size: 12px;
    color: #909399;
}

.file-actions {
    position: absolute;
    top: 5px;
    right: 5px;
    opacity: 0;
    transition: opacity 0.3s;
}

.file-item:hover .file-actions {
    opacity: 1;
}

/* 列表视图样式 */
.files-list .file-name {
    cursor: pointer;
}

.files-list .file-name:hover {
    color: #409eff;
}

/* 上传区域样式 */
.upload-area {
    border: 1px dashed #dcdfe6;
    border-radius: 6px;
    padding: 20px 0;
    text-align: center;
    background-color: #fafafa;
}

/* 右键菜单样式 */
.context-menu {
    position: fixed;
    z-index: 1000;
    border-radius: 4px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.context-menu .el-menu-item {
    height: 40px;
    line-height: 40px;
    padding: 0 20px;
}

.context-menu .el-menu-item i {
    margin-right: 10px;
}

/* 响应式设计 */
@media (max-width: 768px) {
    .files-grid {
        grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
    }
    
    .file-icon {
        font-size: 36px;
    }
    
    .files-toolbar {
        flex-direction: column;
        gap: 10px;
        align-items: flex-start;
    }
    
    .toolbar-right {
        width: 100%;
    }
}
</style>