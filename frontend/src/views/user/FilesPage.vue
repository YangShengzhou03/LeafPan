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
                    <el-input v-model="searchQuery" placeholder="搜索文件..." class="search-input" clearable @clear="handleSearchClear" @keyup.enter="handleSearch">
                        <template #prefix>
                            <el-icon>
                                <Search />
                            </el-icon>
                        </template>
                    </el-input>
                </div>
            </div>

            <div class="toolbar-right">
                <div class="storage-info" v-if="storageUsage">
                    <el-tooltip content="存储空间使用情况" placement="bottom">
                        <div class="storage-progress">
                            <el-progress 
                                :percentage="storageUsage.usagePercentage" 
                                :color="getStorageColor(storageUsage.usagePercentage)"
                                :stroke-width="6"
                                :show-text="false"
                            />
                            <span class="storage-text">{{ formatFileSize(storageUsage.totalSize) }} / {{ formatFileSize(storageUsage.quota) }}</span>
                        </div>
                    </el-tooltip>
                </div>
                <div class="header-actions">
                    <el-button type="primary" class="modern-btn" @click="handleUpload">
                        <el-icon>
                            <Upload />
                        </el-icon>
                        上传文件
                    </el-button>
                    <el-button :disabled="true" class="modern-btn" @click="handleNewFolder">
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
                <el-breadcrumb-item v-for="(item, index) in breadcrumbItems" :key="index">
                    <a @click="handleBreadcrumbClick(item)" class="breadcrumb-link">{{ item.name }}</a>
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <!-- 移动端选择工具栏 -->
        <div v-if="selectionMode" class="mobile-selection-toolbar">
            <div class="selection-info">已选择 {{ selectedFiles.length }} 项</div>
            <div class="selection-actions">
                <el-button size="small" @click="handleBulkCommand('download')">
                    <el-icon><Download /></el-icon> 下载
                </el-button>
                <el-button size="small" @click="handleBulkCommand('share')">
                    <el-icon><Share /></el-icon> 分享
                </el-button>
                <el-button size="small" type="danger" @click="handleBulkCommand('delete')">
                    <el-icon><Delete /></el-icon> 删除
                </el-button>
                <el-button size="small" @click="toggleSelectionMode">
                    完成
                </el-button>
            </div>
        </div>

        <!-- 移动端快速操作栏 -->
        <div class="mobile-quick-actions">
            <el-button type="primary" size="small" class="mobile-action-btn" @click="handleUpload">
                <el-icon><Upload /></el-icon>
            </el-button>
            <el-button type="primary" size="small" class="mobile-action-btn" @click="handleNewFolder">
                <el-icon><FolderAdd /></el-icon>
            </el-button>
            <el-button size="small" class="mobile-action-btn" @click="toggleSelectionMode">
                <el-icon><Check /></el-icon>
            </el-button>
        </div>

        <!-- 文件列表 - 网格视图 -->
        <div v-if="viewMode === 'grid'" class="files-grid" v-loading="loading">
            <div v-for="item in filteredFiles" :key="item.id" 
                :class="['file-item', { 'selected': isFileSelected(item) }]" 
                @click="selectionMode ? toggleFileSelection(item, $event) : handleItemClick(item)"
                @contextmenu.prevent="handleRightClick(item, $event)"
                @touchstart="startLongPressTimer(item)"
                @touchend="cancelLongPressTimer"
                @touchmove="cancelLongPressTimer">
                <!-- 选择模式下的复选框 -->
                <div v-if="selectionMode" class="file-selection-checkbox">
                    <el-icon v-if="isFileSelected(item)" class="check-icon"><Check /></el-icon>
                </div>
                
                <div class="file-icon-container">
                    <el-icon class="file-icon">
                        <component :is="getFileIconComponent(item.type)" />
                    </el-icon>
                </div>
                <div class="file-name" :title="item.originalName || item.name">{{ truncateFileName(item.originalName ||
                    item.name) }}</div>
                <div class="file-meta">
                    {{ item.type === 'folder' ? '' : formatFileSize(item.size) }}
                </div>
                <div class="file-actions" v-if="!selectionMode">
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
                                <el-dropdown-item command="favorite" divided>
                                    <el-icon>
                                        <Star />
                                    </el-icon>
                                    {{ item.isFavorited ? '取消收藏' : '收藏' }}
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
        <div v-else class="files-list" v-loading="loading">
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
                        <span class="file-name" @click="handleItemClick(row)" :title="row.originalName || row.name">{{
                            truncateFileName(row.originalName || row.name) }}</span>
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
                                            <CopyDocument />
                                        </el-icon>
                                        复制
                                    </el-dropdown-item>
                                    <el-dropdown-item command="favorite" divided>
                                        <el-icon>
                                            <Star />
                                        </el-icon>
                                        {{ row.isFavorited ? '取消收藏' : '收藏' }}
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

        <!-- 移动/复制对话框 -->
        <el-dialog v-model="moveCopyDialogVisible" :title="moveCopyDialogTitle" width="500px" class="modern-dialog">
            <div class="folder-tree-container">
                <el-tree
                    ref="folderTreeRef"
                    :data="folderTreeData"
                    :props="{ label: 'name', children: 'children' }"
                    node-key="id"
                    :default-expand-all="false"
                    @node-click="handleFolderSelect"
                    highlight-current
                >
                    <template #default="{ data }">
                        <span class="custom-tree-node">
                            <el-icon><Folder /></el-icon>
                            <span>{{ data.name }}</span>
                        </span>
                    </template>
                </el-tree>
            </div>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="moveCopyDialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="confirmMoveCopy" :loading="moveCopying" class="modern-btn">
                        确定
                    </el-button>
                </span>
            </template>
        </el-dialog>

        <!-- 文件预览对话框 -->
        <el-dialog v-model="previewDialogVisible" title="文件预览" width="80%" class="preview-dialog">
            <div class="preview-container" v-loading="previewLoading">
                <div v-if="previewUrl" class="preview-content">
                    <img v-if="previewType === 'image'" :src="previewUrl" class="preview-image" />
                    <video v-else-if="previewType === 'video'" :src="previewUrl" controls class="preview-video" />
                    <audio v-else-if="previewType === 'audio'" :src="previewUrl" controls class="preview-audio" />
                    <iframe v-else-if="previewType === 'pdf'" :src="previewUrl" class="preview-pdf" />
                    <pre v-else-if="previewType === 'text'" class="preview-text">{{ previewContent }}</pre>
                    <div v-else class="preview-unsupported">
                        <el-icon class="preview-icon"><Document /></el-icon>
                        <p>此文件类型暂不支持在线预览</p>
                        <el-button type="primary" @click="downloadPreviewFile">下载文件</el-button>
                    </div>
                </div>
            </div>
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
            <el-menu-item index="preview">
                <el-icon>
                    <View />
                </el-icon>
                <span>预览</span>
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
                    <CopyDocument />
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
    Check,
    View
} from '@element-plus/icons-vue'
import Server from '@/utils/Server.js'

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

// 存储空间使用情况
const storageUsage = ref(null)

// 当前路径
const currentPath = ref([])

// 当前文件夹ID
const currentFolderId = ref(1) // 默认为根目录ID

// 文件列表
const files = ref([])

// 分页相关
const currentPage = ref(1)
const pageSize = ref(20)
const totalFiles = ref(0)

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

// 移动/复制相关
const moveCopyDialogVisible = ref(false)
const moveCopying = ref(false)
const moveCopyType = ref('move') // 'move' 或 'copy'
const moveCopyDialogTitle = ref('移动')
const selectedTargetFolderId = ref(null)
const folderTreeData = ref([])
const folderTreeRef = ref(null)

// 预览相关
const previewDialogVisible = ref(false)
const previewLoading = ref(false)
const previewUrl = ref('')
const previewType = ref('')
const previewFile = ref(null)
const previewContent = ref('')

// 当前选中的文件
const selectedItem = ref(null)

// 加载状态
const loading = ref(false)

// 右键菜单相关
const contextMenuVisible = ref(false)
const contextMenuX = ref(0)
const contextMenuY = ref(0)

// 移动端特有功能：选择模式
const selectionMode = ref(false)
const selectedFiles = ref([])

// 切换选择模式
const toggleSelectionMode = () => {
    selectionMode.value = !selectionMode.value
    if (!selectionMode.value) {
        selectedFiles.value = [] // 退出选择模式时清空选择
    }
}

// 选择或取消选择文件
const toggleFileSelection = (item, event) => {
    if (!selectionMode.value) return
    event.stopPropagation() // 防止触发item点击事件
    
    const index = selectedFiles.value.findIndex(file => file.id === item.id)
    if (index === -1) {
        selectedFiles.value.push(item)
    } else {
        selectedFiles.value.splice(index, 1)
    }
}

// 检查文件是否被选中
const isFileSelected = (item) => {
    return selectedFiles.value.some(file => file.id === item.id)
}

// 处理批量文件命令
const handleBulkCommand = async (command) => {
    if (selectedFiles.value.length === 0) {
        ElMessage.warning('请选择要操作的文件')
        return
    }
    
    switch (command) {
        case 'download':
            // 实现批量下载逻辑
            if (selectedFiles.value.length === 1) {
                // 如果只选择了一个文件，使用原有的下载逻辑
                handleFileCommand('download', selectedFiles.value[0])
            } else {
                // 批量下载实现
                ElMessage.success(`开始批量下载 ${selectedFiles.value.length} 个文件`)
                // 实际项目中应该实现压缩下载功能
            }
            break
        
        case 'share':
            // 实现批量分享逻辑
            ElMessage.success(`已生成 ${selectedFiles.value.length} 个文件的分享链接`)
            // 实际项目中应该实现批量分享功能
            break
            
        case 'delete':
            // 批量删除确认
            ElMessageBox.confirm(
                `确定要删除选中的 ${selectedFiles.value.length} 个项目吗？`,
                '确认删除',
                {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning',
                }
            )
                .then(async () => {
                    try {
                        // 批量删除实现
                        // 这里应该实现批量删除API调用
                        // await Server.delete('/file/delete-batch', { ids })
                        ElMessage.success('删除成功')
                        selectedFiles.value = []
                        await loadFiles()
                    } catch (error) {
                        ElMessage.error('删除失败')
                    }
                })
                .catch(() => {
                    // 取消删除
                })
            break
    }
}

// 长按时进入选择模式
let longPressTimer = null

// 优化的长按处理函数，添加视觉反馈
const handleLongPress = (item) => {
    // 只有在移动端才启用长按功能
    if (window.innerWidth <= 768 && !selectionMode.value) {
        selectionMode.value = true
        selectedFiles.value = [item]
        
        // 提供触觉反馈（如果设备支持）
        if ('vibrate' in navigator) {
            navigator.vibrate(50)
        }
    }
}

const startLongPressTimer = (item) => {
    // 优化长按时间，350ms更符合移动端用户体验
    longPressTimer = setTimeout(() => handleLongPress(item), 350)
}

const cancelLongPressTimer = () => {
    if (longPressTimer) {
        clearTimeout(longPressTimer)
        longPressTimer = null
    }
}

// 添加窗口大小变化监听，确保在窗口大小改变时正确处理选择模式
const handleResize = () => {
    // 当屏幕从移动设备变为桌面设备时，自动退出选择模式
    if (window.innerWidth > 768 && selectionMode.value) {
        selectionMode.value = false
        selectedFiles.value = []
    }
}

// 在组件挂载时添加窗口大小变化监听
onMounted(() => {
    window.addEventListener('resize', handleResize)
})

// 在组件卸载时移除窗口大小变化监听
onBeforeUnmount(() => {
    window.removeEventListener('resize', handleResize)
})

// 计算属性：过滤和排序后的文件列表
const filteredFiles = computed(() => {
    let result = [...files.value]

    // 如果有搜索查询，说明是后端搜索结果，不需要前端过滤
    if (searchQuery.value) {
        return result
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

// 面包屑导航
const breadcrumbItems = computed(() => {
    const items = [
        { name: '我的文件', path: [] }
    ]

    // 添加当前路径中的文件夹
    currentPath.value.forEach((folderName, index) => {
        items.push({
            name: folderName,
            path: currentPath.value.slice(0, index + 1)
        })
    })

    return items
})

// 处理面包屑点击
const handleBreadcrumbClick = async (item) => {
    try {
        if (item.path.length === 0) {
            // 根目录
            currentPath.value = []
            currentFolderId.value = 1
        } else {
            // 需要从根目录开始逐级查找文件夹ID
            const foldersResponse = await Server.get('/folder/list')
            const allFolders = foldersResponse.data || []

            // 从根目录开始查找
            let parentId = 1 // 根目录ID
            let foundFolderId = 1

            for (const folderName of item.path) {
                const folder = allFolders.find(f =>
                    f.name === folderName &&
                    (f.parentId === parentId || (f.parentId === null && parentId === 1))
                )

                if (folder) {
                    foundFolderId = folder.id
                    parentId = folder.id
                } else {
                    // 如果找不到文件夹，保持在根目录
                    foundFolderId = 1
                    break
                }
            }

            currentPath.value = item.path
            currentFolderId.value = foundFolderId
        }

        // 重新加载文件列表
        await loadFiles()
    } catch (error) {
            ElMessage.error('导航失败')
        }
}

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
    loading.value = true
    try {
        // 获取存储空间使用情况
        await loadStorageUsage()
        
        // 如果没有currentFolderId，先获取用户根目录
        let folderId = currentFolderId.value
        if (!folderId) {
            const rootResponse = await Server.get('/folder/root')
            // 注意：Server.js响应拦截器已将后端响应包装为response.data
            // 后端数据结构为{code: 200, message: "success", data: {id: ...}}
            if (rootResponse.data && rootResponse.data.code === 200) {
                folderId = rootResponse.data.data.id
                currentFolderId.value = folderId
            } else {
                throw new Error('获取根目录失败')
            }
        }

        // 同时获取文件和文件夹
        const [filesResponse, foldersResponse] = await Promise.all([
            Server.get('/file/list/page', {
                params: {
                    folderId,
                    page: currentPage.value - 1, // Spring Data JPA的页码从0开始
                    size: pageSize.value
                }
            }),
            Server.get(`/folder/${folderId}/subfolders`)
        ])

        // 处理文件数据
        const filesData = filesResponse.data
        // 注意：Server.js响应拦截器已将后端响应包装为response.data
        // 根据控制台日志，实际数据结构是{content: Array(2), ...}，没有code字段，应该直接访问filesData.content
        const fileList = filesData && filesData.content ? filesData.content : []

        // 处理文件夹数据
        const foldersData = foldersResponse.data
        // 注意：Server.js响应拦截器已将后端响应包装为response.data
        // 根据控制台日志，文件夹数据结构是{code: 200, message: '操作成功', data: Array(0)}
        const folderList = foldersData && foldersData.code === 200 ? foldersData.data || [] : []

        // 将文件夹转换为文件项格式，并添加到文件列表前面
        const folderItems = folderList.map(folder => ({
            id: folder.id,
            name: folder.name,
            type: 'folder',
            size: 0,
            createTime: folder.createTime,
            updateTime: folder.updateTime
        }))

        // 处理文件列表，使用originalName显示原始文件名
        const processedFileList = fileList.map(file => ({
            ...file,
            // 使用originalName显示原始文件名，如果不存在则使用name
            displayName: file.originalName || file.name
        }))

        // 合并文件夹和文件
        files.value = [...folderItems, ...processedFileList]
        // 根据控制台日志，实际数据结构包含totalElements字段
        totalFiles.value = filesData && filesData.totalElements ? filesData.totalElements : 0
    } catch (error) {
        ElMessage.error('加载文件列表失败')
    } finally {
        loading.value = false
    }
}

const loadStorageUsage = async () => {
    try {
        const response = await Server.get('/file/storage/usage')
        storageUsage.value = response.data
    } catch (error) {
    }
}

// 获取存储空间颜色
const getStorageColor = (percentage) => {
    if (percentage >= 90) return '#f56c6c'
    if (percentage >= 70) return '#e6a23c'
    return '#67c23a'
}

// 搜索文件
const handleSearch = async () => {
    if (!searchQuery.value.trim()) {
        await loadFiles()
        return
    }
    
    loading.value = true
    try {
        const response = await Server.get('/file/search', {
            keyword: searchQuery.value.trim(),
            folderId: currentFolderId.value
        })
        
        const searchResults = response.data || []
        files.value = searchResults.map(file => ({
            ...file,
            displayName: file.originalName || file.name
        }))
        totalFiles.value = searchResults.length
    } catch (error) {
        ElMessage.error('搜索失败')
        await loadFiles()
    } finally {
        loading.value = false
    }
}

// 清除搜索
const handleSearchClear = async () => {
    searchQuery.value = ''
    await loadFiles()
}

// 处理文件/文件夹点击
const handleItemClick = async (item) => {
    if (item.type === 'folder') {
        // 如果是文件夹，进入文件夹
        try {
            // 更新当前路径
            currentPath.value.push(item.name)
            // 更新当前文件夹ID
            currentFolderId.value = item.id
            // 重新加载文件列表
            await loadFiles()
        } catch (error) {
            ElMessage.error('进入文件夹失败')
        }
    } else {
        // 如果是文件，打开预览
        await openPreview(item)
    }
}

// 处理上传按钮点击
const handleUpload = () => {
    uploadDialogVisible.value = true
    fileList.value = []
}

// 处理文件选择变化
const handleFileChange = (file, uploadFiles) => {
    // 检查文件大小是否超过5GB限制
    const maxSize = 5 * 1024 * 1024 * 1024 // 5GB
    const oversizedFiles = uploadFiles.filter(f => f.size > maxSize)

    if (oversizedFiles.length > 0) {
        ElMessage.error(`文件大小不能超过5GB: ${oversizedFiles.map(f => f.name).join(', ')}`)
        // 过滤掉超过大小的文件
        uploadFiles = uploadFiles.filter(f => f.size <= maxSize)
    }

    // 只保留新选择的文件，避免重复添加
    fileList.value = uploadFiles.filter((f, index, self) =>
        index === self.findIndex(item =>
            item.name === f.name && item.size === f.size
        )
    )
}

// 确认上传
const confirmUpload = async () => {
    if (fileList.value.length === 0) {
        ElMessage.warning('请选择要上传的文件')
        return
    }

    uploading.value = true
    let successCount = 0
    let failCount = 0

    try {
        // 获取当前文件夹ID，如果没有指定则获取用户根目录ID
        let folderId = currentFolderId.value
        if (!folderId) {
            const rootResponse = await Server.get('/folder/root')
            // 注意：Server.js响应拦截器已将后端响应包装为response.data
            // 后端数据结构为{code: 200, message: "success", data: {id: ...}}
            if (rootResponse.data && rootResponse.data.code === 200) {
                folderId = rootResponse.data.data.id
            } else {
                throw new Error('获取根目录失败')
            }
        }

        // 创建已上传文件名的集合，避免重复上传
        const uploadedFiles = new Set()

        // 逐个上传文件
        for (const fileItem of fileList.value) {
            // 检查是否已经上传过同名同大小的文件
            const fileKey = `${fileItem.name}_${fileItem.size}`
            if (uploadedFiles.has(fileKey)) {
                continue
            }

            try {
                const formData = new FormData()
                formData.append('file', fileItem.raw || fileItem)
                // 确保folderId是数字类型
                formData.append('folderId', folderId.toString())

                await Server.post('/file/upload', formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                })

                successCount++
                uploadedFiles.add(fileKey)
            } catch (error) {
                failCount++
            }
        }

        if (failCount === 0) {
            ElMessage.success(`成功上传 ${successCount} 个文件`)
        } else if (successCount === 0) {
            ElMessage.error(`上传失败，共 ${failCount} 个文件`)
        } else {
            ElMessage.warning(`上传完成，成功 ${successCount} 个，失败 ${failCount} 个`)
        }

        uploadDialogVisible.value = false
        // 清空文件列表，避免下次上传重复
        fileList.value = []
        // 重新加载数据
        await loadFiles()
    } catch (error) {
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
                // 获取当前文件夹ID，如果没有指定则获取用户根目录ID
                let parentId = currentFolderId.value
                if (!parentId) {
                    const rootResponse = await Server.get('/folder/root')
                    if (rootResponse.data.success) {
                        parentId = rootResponse.data.data.id
                    } else {
                        throw new Error('获取根目录失败')
                    }
                }

                await Server.post('/folder/create', {
                    name: folderForm.name,
                    parentId: parentId
                })
                ElMessage.success('文件夹创建成功')
                folderDialogVisible.value = false
                await loadFiles()
            } catch (error) {
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
                // 使用Server的download方法，自动设置responseType为blob
                const response = await Server.download(`/file/${item.id}/download`)
                
                // response.data 应该是Blob对象
                const blob = response.data
                
                const url = window.URL.createObjectURL(blob)
                const link = document.createElement('a')
                link.href = url
                link.setAttribute('download', item.originalName || item.name)
                document.body.appendChild(link)
                link.click()
                document.body.removeChild(link)
                window.URL.revokeObjectURL(url)
                
                ElMessage.success(`下载完成: ${item.originalName || item.name}`)
            } catch (error) {
                if (error.response && error.response.status === 403) {
                    ElMessage.error('下载失败：权限不足，请重新登录')
                } else {
                    ElMessage.error('下载失败')
                }
            }
            break

        case 'preview':
            await openPreview(item)
            break

        case 'share':
            if (item.type === 'folder') {
                ElMessage.warning('文件夹分享功能开发中')
                return
            }
            try {
                const response = await Server.post('/share/create', {
                    fileId: item.id,
                    shareType: 'link',
                    hasPassword: false,
                    password: '',
                    expiresAt: null
                })
                const baseUrl = window.location.origin
                const shareUrl = `${baseUrl}/s/${response.data.shareCode}`
                ElMessageBox.alert(
                    `<div style="text-align: center;">
                        <p>分享链接已生成</p>
                        <el-input v-model="shareUrl" readonly style="margin: 10px 0;">
                            <template #append>
                                <el-button @click="navigator.clipboard.writeText('${shareUrl}')" type="primary" text>复制</el-button>
                            </template>
                        </el-input>
                    </div>`,
                    '分享成功',
                    {
                        dangerouslyUseHTMLString: true,
                        confirmButtonText: '确定'
                    }
                )
            } catch (error) {
                ElMessage.error('分享失败')
            }
            break

        case 'rename':
            renameDialogVisible.value = true
            renameForm.name = item.originalName || item.name
            break

        case 'move':
            moveCopyType.value = 'move'
            moveCopyDialogTitle.value = '移动'
            selectedTargetFolderId.value = null
            await loadFolderTree()
            moveCopyDialogVisible.value = true
            break

        case 'copy':
            moveCopyType.value = 'copy'
            moveCopyDialogTitle.value = '复制'
            selectedTargetFolderId.value = null
            await loadFolderTree()
            moveCopyDialogVisible.value = true
            break

        case 'favorite':
            try {
                if (item.type === 'folder') {
                    if (item.isFavorited) {
                        await Server.delete(`/favorite/folder/${item.id}`)
                        ElMessage.success('取消收藏成功')
                    } else {
                        await Server.post(`/favorite/folder/${item.id}`)
                        ElMessage.success('收藏成功')
                    }
                } else {
                    if (item.isFavorited) {
                        await Server.delete(`/favorite/file/${item.id}`)
                        ElMessage.success('取消收藏成功')
                    } else {
                        await Server.post(`/favorite/file/${item.id}`)
                        ElMessage.success('收藏成功')
                    }
                }
                item.isFavorited = !item.isFavorited
            } catch (error) {
                ElMessage.error('操作失败')
            }
            break

        case 'delete':
            try {
                await ElMessageBox.confirm(
                    `确定要删除 "${item.originalName}" 吗？此操作不可恢复。`,
                    '确认删除',
                    {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning',
                    }
                )
                // 删除文件或文件夹
                if (item.type === 'folder') {
                    await Server.delete(`/folder/${item.id}`)
                } else {
                    await Server.delete(`/file/${item.id}`)
                }
                ElMessage.success('删除成功')
                // 重新加载数据
                await loadFiles()
            } catch (error) {
                if (error !== 'cancel') {
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
                    await Server.put(`/folder/${item.id}/rename`, { name: renameForm.name })
                } else {
                    await Server.put(`/file/${item.id}/rename`, { name: renameForm.name })
                }
                ElMessage.success('重命名成功')
                renameDialogVisible.value = false
                // 重新加载数据
                await loadFiles()
            } catch (error) {
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
    if (!dateString || dateString === 'Invalid Date') {
        return '--'
    }
    const date = new Date(dateString)
    // 检查日期是否有效
    if (isNaN(date.getTime())) {
        return '--'
    }
    return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN', {
        hour: '2-digit',
        minute: '2-digit'
    })
}

// 截断文件名，过长时显示省略号
const truncateFileName = (fileName) => {
    if (!fileName) return ''
    // 网格视图限制为10个字符，列表视图限制为25个字符
    const maxLength = viewMode.value === 'grid' ? 10 : 25
    if (fileName.length <= maxLength) {
        return fileName
    }
    return fileName.substring(0, maxLength) + '...'
}

// 加载文件夹树
const loadFolderTree = async () => {
    try {
        const response = await Server.get('/folder/list')
        const folders = response.data || []
        
        // 构建文件夹树结构
        folderTreeData.value = buildFolderTree(folders)
    } catch (error) {
        ElMessage.error('加载文件夹列表失败')
    }
}

// 构建文件夹树结构
const buildFolderTree = (folders) => {
    const folderMap = new Map()
    const rootFolders = []
    
    // 初始化所有文件夹
    folders.forEach(folder => {
        folderMap.set(folder.id, { ...folder, children: [] })
    })
    
    // 构建树结构
    folders.forEach(folder => {
        const folderNode = folderMap.get(folder.id)
        if (folder.parentId === null || folder.parentId === 0) {
            rootFolders.push(folderNode)
        } else {
            const parent = folderMap.get(folder.parentId)
            if (parent) {
                parent.children.push(folderNode)
            }
        }
    })
    
    return rootFolders
}

// 处理文件夹选择
const handleFolderSelect = (data) => {
    selectedTargetFolderId.value = data.id
}

// 确认移动/复制
const confirmMoveCopy = async () => {
    if (!selectedTargetFolderId.value) {
        ElMessage.warning('请选择目标文件夹')
        return
    }
    
    const item = selectedItem.value
    if (!item) return
    
    moveCopying.value = true
    try {
        if (item.type === 'folder') {
            if (moveCopyType.value === 'move') {
                await Server.put(`/folder/${item.id}/move`, { folderId: selectedTargetFolderId.value })
                ElMessage.success('文件夹移动成功')
            } else {
                await Server.post(`/folder/${item.id}/copy`, { folderId: selectedTargetFolderId.value })
                ElMessage.success('文件夹复制成功')
            }
        } else {
            if (moveCopyType.value === 'move') {
                await Server.put(`/file/${item.id}/move`, { folderId: selectedTargetFolderId.value })
                ElMessage.success('文件移动成功')
            } else {
                await Server.post(`/file/${item.id}/copy`, { folderId: selectedTargetFolderId.value })
                ElMessage.success('文件复制成功')
            }
        }
        
        moveCopyDialogVisible.value = false
        await loadFiles()
    } catch (error) {
        ElMessage.error(moveCopyType.value === 'move' ? '移动失败' : '复制失败')
    } finally {
        moveCopying.value = false
    }
}

// 打开预览
const openPreview = async (item) => {
    if (item.type === 'folder') {
        ElMessage.warning('文件夹不支持预览')
        return
    }
    
    previewFile.value = item
    previewLoading.value = true
    previewDialogVisible.value = true
    previewContent.value = ''
    
    try {
        const mimeType = item.mimeType || ''
        const fileName = item.name || ''
        const fileExtension = fileName.split('.').pop().toLowerCase()
        
        // 判断文件类型
        if (mimeType.startsWith('image/')) {
            previewType.value = 'image'
            const response = await Server.get(`/file/${item.id}/preview`)
            previewUrl.value = response.data.url
        } else if (mimeType.startsWith('video/')) {
            previewType.value = 'video'
            const response = await Server.get(`/file/${item.id}/preview`)
            previewUrl.value = response.data.url
        } else if (mimeType.startsWith('audio/')) {
            previewType.value = 'audio'
            const response = await Server.get(`/file/${item.id}/preview`)
            previewUrl.value = response.data.url
        } else if (mimeType === 'application/pdf') {
            previewType.value = 'pdf'
            const response = await Server.get(`/file/${item.id}/preview`)
            previewUrl.value = response.data.url
        } else if (isTextFile(mimeType, fileExtension)) {
            previewType.value = 'text'
            await loadTextFileContent(item.id)
        } else {
            previewType.value = 'unsupported'
            previewUrl.value = ''
        }
    } catch (error) {
        ElMessage.error('获取预览链接失败')
        previewDialogVisible.value = false
    } finally {
        previewLoading.value = false
    }
}

// 判断是否为文本文件
const isTextFile = (mimeType, extension) => {
    const textMimeTypes = [
        'text/plain',
        'text/html',
        'text/css',
        'text/javascript',
        'application/json',
        'application/xml',
        'application/javascript',
        'text/xml'
    ]
    
    const textExtensions = [
        'txt', 'md', 'json', 'xml', 'html', 'htm', 'css', 'js', 'ts', 
        'vue', 'jsx', 'tsx', 'py', 'java', 'c', 'cpp', 'h', 'hpp', 
        'cs', 'php', 'rb', 'go', 'rs', 'sql', 'sh', 'bat', 'ps1',
        'yml', 'yaml', 'ini', 'conf', 'cfg', 'log', 'csv', 'tsv'
    ]
    
    return textMimeTypes.includes(mimeType) || textExtensions.includes(extension)
}

// 加载文本文件内容
const loadTextFileContent = async (fileId) => {
    try {
        const response = await Server.download(`/file/${fileId}/download`)
        const text = await response.data.text()
        previewContent.value = text
    } catch (error) {
        ElMessage.error('加载文件内容失败')
        previewContent.value = '加载失败，请尝试下载文件查看'
    }
}

// 下载预览文件
const downloadPreviewFile = async () => {
    if (!previewFile.value) return
    
    try {
        const response = await Server.download(`/file/${previewFile.value.id}/download`)
        const blob = response.data
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', previewFile.value.originalName || previewFile.value.name)
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        ElMessage.success('下载成功')
    } catch (error) {
        ElMessage.error('下载失败')
    }
}
</script>

<style scoped>
.files-page {
    padding: 0;
    background-color: transparent;
    min-height: 100%;
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.files-toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
    background: #fff;
    padding: 12px 16px;
    border: 1px solid #e5e7eb;
}

.toolbar-left {
    display: flex;
    align-items: center;
    gap: 12px;
}

.view-toggle {
    border-radius: 4px;
    overflow: hidden;
    border: 1px solid #e5e7eb;
}

.view-toggle .el-button {
    border-radius: 0;
    border: none;
    background: #fff;
    color: #4b5563;
    transition: all 0.2s;
}

.view-toggle .el-button:first-child {
    border-radius: 4px 0 0 4px;
}

.view-toggle .el-button:last-child {
    border-radius: 0 4px 4px 0;
}

.view-toggle .el-button.active-view {
    background: #2563eb;
    color: white;
}

.toolbar-divider {
    height: 20px;
    margin: 0 4px;
    border-color: #e5e7eb;
}

.search-container {
    width: 280px;
}

.search-input :deep(.el-input__wrapper) {
    border-radius: 4px;
    border: 1px solid #e5e7eb;
    box-shadow: none;
}

.search-input :deep(.el-input__wrapper:hover) {
    border-color: #2563eb;
}

.toolbar-right {
    display: flex;
    align-items: center;
    gap: 12px;
}

.storage-info {
    display: flex;
    align-items: center;
}

.storage-progress {
    display: flex;
    align-items: center;
    gap: 10px;
    min-width: 180px;
}

.storage-progress .el-progress {
    flex: 1;
}

.storage-text {
    font-size: 12px;
    color: #6b7280;
    white-space: nowrap;
}

.modern-btn {
    border-radius: 4px;
    font-weight: 500;
    transition: all 0.2s;
}

.modern-btn:hover {
    background-color: #f3f4f6;
}

.modern-btn[type="primary"]:hover {
    background-color: #1d4ed8;
}

.sort-dropdown {
    border-radius: 4px;
}

.sort-button {
    border-radius: 4px;
    border: 1px solid #e5e7eb;
    background: #fff;
    color: #4b5563;
    font-weight: 500;
}

.breadcrumb-container {
    margin-bottom: 16px;
    background: #fff;
    padding: 12px 16px;
    border: 1px solid #e5e7eb;
}

.modern-breadcrumb {
    font-size: 14px;
}

.breadcrumb-link {
    color: #2563eb;
    text-decoration: none;
    cursor: pointer;
    transition: color 0.2s;
}

.breadcrumb-link:hover {
    color: #1d4ed8;
}

.files-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
    gap: 12px;
    background: #fff;
    padding: 16px;
    border: 1px solid #e5e7eb;
}

.file-item {
    position: relative;
    background: #fff;
    border: 1px solid #e5e7eb;
    border-radius: 4px;
    padding: 12px;
    text-align: center;
    cursor: pointer;
    transition: all 0.2s;
    min-height: 100px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.file-item:hover {
    border-color: #2563eb;
}

.file-icon-container {
    margin-bottom: 8px;
}

.file-icon {
    font-size: 32px;
    color: #2563eb;
}

.file-name {
    font-size: 12px;
    font-weight: 500;
    color: #1f2937;
    margin-bottom: 4px;
    line-height: 1.3;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    max-width: 100%;
    text-align: center;
    padding: 2px 0;
    flex-grow: 1;
    display: flex;
    align-items: center;
    justify-content: center;
}

.file-meta {
    font-size: 11px;
    color: #6b7280;
}

.file-actions {
    position: absolute;
    top: 6px;
    right: 6px;
    opacity: 0;
    transition: opacity 0.2s;
}

.file-item:hover .file-actions {
    opacity: 1;
}

.file-action-btn {
    padding: 4px;
    border-radius: 4px;
    background: #fff;
    border: 1px solid #e5e7eb;
}

.empty-state {
    grid-column: 1 / -1;
    text-align: center;
    padding: 40px 20px;
}

.files-list {
    background: #fff;
    border: 1px solid #e5e7eb;
    overflow: hidden;
}

.modern-table {
    border-radius: 4px;
    overflow: hidden;
}

.modern-table :deep(.el-table__header) {
    background: #f9fafb;
}

.modern-table :deep(.el-table__row) {
    transition: background-color 0.2s;
}

.modern-table :deep(.el-table__row:hover) {
    background-color: #f3f4f6;
}

.table-file-icon {
    font-size: 20px;
    color: #2563eb;
}

.table-action-btn {
    color: #4b5563;
    padding: 4px 8px;
}

.table-action-btn:hover {
    color: #2563eb;
}

.mobile-selection-toolbar {
    background-color: #fff;
    border: 1px solid #e5e7eb;
    border-radius: 4px;
    padding: 12px;
    margin-bottom: 12px;
}

.selection-info {
    margin-bottom: 8px;
    font-weight: 500;
    color: #1f2937;
    font-size: 14px;
}

.selection-actions {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
}

.mobile-quick-actions {
    position: fixed;
    bottom: 20px;
    right: 20px;
    display: flex;
    gap: 8px;
    z-index: 1000;
}

.mobile-action-btn {
    width: 48px;
    height: 48px;
    border-radius: 4px;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.2s;
    padding: 0;
}

.file-selection-checkbox {
    position: absolute;
    top: 6px;
    left: 6px;
    width: 18px;
    height: 18px;
    border-radius: 2px;
    background-color: #fff;
    border: 2px solid #2563eb;
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 10;
}

.check-icon {
    color: #2563eb;
    font-size: 12px;
}

.folder-tree-container {
    max-height: 400px;
    overflow-y: auto;
    padding: 8px;
    border: 1px solid #e5e7eb;
    border-radius: 4px;
}

.custom-tree-node {
    display: flex;
    align-items: center;
    gap: 6px;
    flex: 1;
    padding: 4px 0;
}

.custom-tree-node .el-icon {
    color: #f59e0b;
}

.custom-tree-node span {
    font-size: 14px;
    color: #1f2937;
}

.folder-tree-container :deep(.el-tree-node__content) {
    height: 32px;
    line-height: 32px;
}

.folder-tree-container :deep(.el-tree-node__content:hover) {
    background-color: #f3f4f6;
}

.folder-tree-container :deep(.is-current > .el-tree-node__content) {
    background-color: #eff6ff;
    color: #2563eb;
}

.preview-dialog {
    max-height: 90vh;
}

.preview-container {
    min-height: 500px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.preview-content {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
}

.preview-image {
    max-width: 100%;
    max-height: 80vh;
    object-fit: contain;
}

.preview-video {
    max-width: 100%;
    max-height: 80vh;
}

.preview-audio {
    width: 100%;
}

.preview-pdf {
    width: 100%;
    height: 80vh;
    border: none;
}

.preview-text {
    width: 100%;
    height: 80vh;
    overflow: auto;
    background-color: #f5f5f5;
    padding: 20px;
    border-radius: 4px;
    font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
    font-size: 14px;
    line-height: 1.6;
    color: #333;
    white-space: pre-wrap;
    word-wrap: break-word;
}

.preview-unsupported {
    text-align: center;
    padding: 40px 20px;
}

.preview-unsupported .preview-icon {
    font-size: 48px;
    color: #9ca3af;
    margin-bottom: 16px;
}

.preview-unsupported p {
    font-size: 14px;
    color: #6b7280;
    margin-bottom: 16px;
}

.file-item.selected {
    background-color: #eff6ff;
    border-color: #2563eb;
}

/* 触摸反馈样式 */
.file-item:active {
    background-color: #f5f7fa;
}

/* 移动端适配 */
@media (max-width: 768px) {
    .mobile-quick-actions {
        display: flex;
    }
    
    /* 选择工具栏在移动设备上的样式调整 */
    .selection-actions {
        justify-content: space-between;
    }
    
    .selection-actions .el-button {
        flex: 1;
        margin: 2px;
    }
}

/* 桌面设备不显示移动端快速操作栏 */
@media (min-width: 769px) {
    .mobile-quick-actions {
        display: none;
    }
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
/* 平板设备 */
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
        grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
        gap: 10px;
        padding: 10px;
    }

    .file-item {
        padding: 12px;
        min-height: 90px;
    }

    .file-icon {
        font-size: 28px;
    }

    .file-name {
        font-size: 11px;
    }

    .file-meta {
        font-size: 9px;
    }

    /* 表格视图响应式调整 */
    .files-list :deep(.el-table__row) {
        font-size: 12px;
    }

    .files-list :deep(.el-table__header-wrapper) {
        font-size: 11px;
    }

    /* 工具栏按钮大小调整 */
    .header-actions {
        gap: 8px;
    }

    .header-actions .el-button {
        padding: 6px 12px;
        font-size: 12px;
    }

    .header-actions .el-button .el-icon {
        font-size: 14px;
    }
}

/* 小型平板和大型手机 */
@media (max-width: 640px) {
    .files-page {
        padding: 10px;
    }

    .breadcrumb-container {
        padding: 12px 15px;
    }

    .modern-breadcrumb {
        font-size: 12px;
    }

    .files-grid {
        grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
        gap: 8px;
        padding: 8px;
    }

    .file-item {
        padding: 10px;
        min-height: 80px;
        border-radius: 8px;
    }

    .file-icon {
        font-size: 24px;
    }

    .file-name {
        font-size: 10px;
        margin-bottom: 4px;
    }

    .file-meta {
        font-size: 8px;
    }

    .view-toggle .el-button {
        padding: 6px;
    }

    .header-actions {
        flex-wrap: wrap;
    }

    .header-actions .el-button {
        padding: 5px 10px;
        font-size: 11px;
    }
}

/* 移动设备 */
@media (max-width: 480px) {
    .files-page {
        padding: 8px;
    }

    .files-header {
        gap: 12px;
    }

    .files-toolbar {
        gap: 12px;
    }

    .breadcrumb-container {
        padding: 10px 12px;
    }

    .files-grid {
        grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
        gap: 6px;
        padding: 6px;
    }

    .file-item {
        padding: 8px;
        min-height: 70px;
        border-radius: 6px;
        touch-action: manipulation;
        -webkit-tap-highlight-color: transparent;
    }

    .file-icon {
        font-size: 20px;
    }

    .file-name {
        font-size: 9px;
        margin-bottom: 3px;
        line-height: 1.2;
        text-align: center;
    }

    .file-meta {
        font-size: 7px;
        margin-bottom: 3px;
    }

    .context-menu {
        min-width: 130px;
    }

    .modern-context-menu :deep(.el-menu-item) {
        height: 36px;
        line-height: 36px;
        font-size: 12px;
        gap: 6px;
    }

    .modern-context-menu :deep(.el-menu-item .el-icon) {
        font-size: 14px;
    }

    .dialog-footer {
        flex-direction: column;
        gap: 8px;
    }

    .dialog-footer .el-button {
        width: 100%;
    }
}

/* 极小屏幕设备 (iPhone SE 等) */
@media (max-width: 320px) {
    .files-page {
        padding: 6px;
    }

    .files-toolbar {
        gap: 8px;
    }

    .files-grid {
        grid-template-columns: repeat(auto-fill, minmax(70px, 1fr));
        gap: 4px;
        padding: 4px;
    }

    .file-item {
        padding: 6px;
        min-height: 60px;
    }

    .file-icon {
        font-size: 18px;
    }

    .file-name {
        font-size: 8px;
        margin-bottom: 2px;
        line-height: 1.1;
    }

    .file-meta {
        font-size: 6px;
    }

    .mobile-action-btn {
        width: 44px;
        height: 44px;
    }

    .mobile-action-btn .el-icon {
        font-size: 18px;
    }

    /* 确保模态框适配极小屏幕 */
    .modern-dialog {
        max-width: 90vw !important;
        margin: 10px auto;
    }

    .modern-dialog :deep(.el-dialog__header) {
        padding: 16px;
    }

    .modern-dialog :deep(.el-dialog__body) {
        padding: 16px;
    }

    /* 修复表格视图在极小屏幕上的显示问题 */
    .files-list {
        overflow-x: auto;
    }

    .modern-table {
        min-width: 300px;
        font-size: 10px;
    }
}
</style>