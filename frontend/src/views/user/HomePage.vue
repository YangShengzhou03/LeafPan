<template>
    <div class="home-page">
        <div class="dashboard-header">
            <h1>仪表盘</h1>
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

        <!-- 存储使用情况 -->
        <div class="storage-overview">
            <el-card class="storage-card">
                <template #header>
                    <div class="card-header">
                        <span>存储空间</span>
                        <el-button type="text" @click="handleStorageDetail">详情</el-button>
                    </div>
                </template>
                <div class="storage-info">
                    <div class="storage-chart">
                        <el-progress 
                            type="circle" 
                            :percentage="storagePercentage" 
                            :color="storageColor"
                            :width="120"
                        >
                            <template #default="{ percentage }">
                                <span class="percentage-label">{{ percentage }}%</span>
                            </template>
                        </el-progress>
                    </div>
                    <div class="storage-details">
                        <div class="storage-text">
                            <span class="used">{{ usedStorage }}</span>
                            <span class="total"> / {{ totalStorage }}</span>
                        </div>
                        <div class="storage-breakdown">
                            <div class="breakdown-item">
                                <span class="color-dot document"></span>
                                <span>文档: {{ formatFileSize(storageUsage.documents) }}</span>
                            </div>
                            <div class="breakdown-item">
                                <span class="color-dot image"></span>
                                <span>图片: {{ formatFileSize(storageUsage.images) }}</span>
                            </div>
                            <div class="breakdown-item">
                                <span class="color-dot video"></span>
                                <span>视频: {{ formatFileSize(storageUsage.videos) }}</span>
                            </div>
                            <div class="breakdown-item">
                                <span class="color-dot other"></span>
                                <span>其他: {{ formatFileSize(storageUsage.others) }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </el-card>

            <!-- 文件统计 -->
            <el-card class="stats-card">
                <template #header>
                    <div class="card-header">
                        <span>文件统计</span>
                    </div>
                </template>
                <div class="stats-grid">
                    <div class="stat-item">
                        <div class="stat-value">{{ dashboardStats.totalFiles }}</div>
                        <div class="stat-label">文件总数</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-value">{{ dashboardStats.totalFolders }}</div>
                        <div class="stat-label">文件夹</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-value">{{ dashboardStats.sharedFiles }}</div>
                        <div class="stat-label">共享文件</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-value">{{ dashboardStats.starredFiles }}</div>
                        <div class="stat-label">收藏文件</div>
                    </div>
                </div>
            </el-card>
        </div>

        <!-- 最近文件 -->
        <el-card class="recent-files-card">
            <template #header>
                <div class="card-header">
                    <span>最近文件</span>
                    <el-button type="text" @click="viewAllFiles">查看全部</el-button>
                </div>
            </template>
            <div class="recent-files">
                <el-table :data="recentFiles" style="width: 100%">
                    <el-table-column label="名称" min-width="300">
                        <template #default="{ row }">
                            <div class="file-info">
                                <i :class="getFileIcon(row.type)"></i>
                                <span class="file-name">{{ row.name }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column label="大小" width="120">
                        <template #default="{ row }">
                            {{ formatFileSize(row.size) }}
                        </template>
                    </el-table-column>
                    <el-table-column label="修改时间" width="180">
                        <template #default="{ row }">
                            {{ formatDate(row.updatedAt) }}
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="180">
                        <template #default="{ row }">
                            <el-button type="text" @click="handleDownload(row)">
                                <i class="icon-download"></i>
                            </el-button>
                            <el-button type="text" @click="handleShare(row)">
                                <i class="icon-share"></i>
                            </el-button>
                            <el-button type="text" @click="handleStar(row)">
                                <i :class="row.starred ? 'icon-star-filled' : 'icon-star'"></i>
                            </el-button>
                            <el-dropdown @command="(command) => handleMoreCommand(command, row)">
                                <el-button type="text">
                                    <i class="icon-more"></i>
                                </el-button>
                                <template #dropdown>
                                    <el-dropdown-menu>
                                        <el-dropdown-item command="rename">重命名</el-dropdown-item>
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
        </el-card>

        <!-- 快速访问 -->
        <div class="quick-access">
            <el-card class="quick-access-card">
                <template #header>
                    <div class="card-header">
                        <span>快速访问</span>
                    </div>
                </template>
                <div class="quick-access-grid">
                    <div class="quick-access-item" @click="navigateTo('/user/files')">
                        <i class="icon-files"></i>
                        <span>我的文件</span>
                    </div>
                    <div class="quick-access-item" @click="navigateTo('/shared')">
                        <i class="icon-share"></i>
                        <span>共享文件</span>
                    </div>
                    <div class="quick-access-item" @click="navigateTo('/recycle')">
                        <i class="icon-recycle"></i>
                        <span>回收站</span>
                    </div>
                    <div class="quick-access-item" @click="navigateTo('/settings')">
                        <i class="icon-settings"></i>
                        <span>设置</span>
                    </div>
                </div>
            </el-card>
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
    </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { mockApiService } from '@/utils/mockApiService'
import { mockApiResponses } from '@/utils/mockData'

const router = useRouter()
const isCollapsed = ref(false)

// 存储信息
const totalStorageGB = ref(15)
const usedStorageGB = ref(7.2)

const storagePercentage = computed(() => {
    return Math.round((usedStorageGB.value / totalStorageGB.value) * 100)
})

const storageColor = computed(() => {
    if (storagePercentage.value < 50) return '#67C23A'
    if (storagePercentage.value < 80) return '#E6A23C'
    return '#F56C6C'
})

const usedStorage = computed(() => {
    return `${usedStorageGB.value.toFixed(1)} GB`
})

const totalStorage = computed(() => {
    return `${totalStorageGB.value} GB`
})

const storageUsage = ref({
    documents: 2.1 * 1024 * 1024 * 1024, // 2.1 GB
    images: 3.5 * 1024 * 1024 * 1024,   // 3.5 GB
    videos: 1.2 * 1024 * 1024 * 1024,   // 1.2 GB
    others: 0.4 * 1024 * 1024 * 1024    // 0.4 GB
})

// 仪表盘统计
const dashboardStats = ref({
    totalFiles: 156,
    totalFolders: 24,
    sharedFiles: 12,
    starredFiles: 8
})

// 最近文件
const recentFiles = ref([])

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

// 页面加载时获取数据
onMounted(async () => {
    await loadDashboardData()
    // 自动重定向到文件列表页面
    router.replace('/user/files')
})

// 加载仪表盘数据
const loadDashboardData = async () => {
    try {
        // 获取用户信息
        const userResponse = await mockApiService.getCurrentUser()
        if (userResponse.success) {
            totalStorageGB.value = userResponse.data.storageLimit / (1024 * 1024 * 1024)
        }

        // 获取文件列表
        const filesResponse = await mockApiService.getFiles()
        if (filesResponse.success) {
            recentFiles.value = filesResponse.data.slice(0, 5) // 只显示最近的5个文件
        }

        // 获取仪表盘统计
        const statsResponse = await mockApiService.getDashboardStats()
        if (statsResponse.success) {
            dashboardStats.value = statsResponse.data
        }
    } catch (error) {
        console.error('加载仪表盘数据失败:', error)
        ElMessage.error('加载仪表盘数据失败')
    }
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
        await loadDashboardData()
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
                await loadDashboardData()
            } catch (error) {
                console.error('创建文件夹失败:', error)
                ElMessage.error('创建文件夹失败')
            } finally {
                creatingFolder.value = false
            }
        }
    })
}

// 处理存储详情点击
const handleStorageDetail = () => {
    router.push('/storage')
}

// 查看所有文件
const viewAllFiles = () => {
    router.push('/user/files')
}

// 处理文件下载
const handleDownload = async (file) => {
    try {
        ElMessage.info(`正在下载 "${file.name}"...`)
        // 模拟下载
        await new Promise(resolve => setTimeout(resolve, 1500))
        ElMessage.success(`文件 "${file.name}" 下载完成`)
    } catch (error) {
        console.error('下载失败:', error)
        ElMessage.error('下载失败')
    }
}

// 处理文件分享
const handleShare = async (file) => {
    try {
        // 模拟分享
        await new Promise(resolve => setTimeout(resolve, 500))
        ElMessage.success(`已生成 "${file.name}" 的分享链接`)
    } catch (error) {
        console.error('分享失败:', error)
        ElMessage.error('分享失败')
    }
}

// 处理文件收藏/取消收藏
const handleStar = async (file) => {
    try {
        // 模拟收藏/取消收藏
        file.starred = !file.starred
        ElMessage.success(file.starred ? '已添加到收藏' : '已取消收藏')
    } catch (error) {
        console.error('操作失败:', error)
        ElMessage.error('操作失败')
    }
}

// 处理更多操作命令
const handleMoreCommand = async (command, file) => {
    switch (command) {
        case 'rename':
            ElMessageBox.prompt('请输入新名称', '重命名', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                inputValue: file.name,
                inputValidator: (value) => {
                    if (!value) {
                        return '名称不能为空'
                    }
                    return true
                }
            }).then(({ value }) => {
                file.name = value
                ElMessage.success('重命名成功')
            }).catch(() => {
                // 用户取消操作
            })
            break
        case 'move':
            ElMessage.info('移动功能开发中')
            break
        case 'copy':
            ElMessage.info('复制功能开发中')
            break
        case 'delete':
            ElMessageBox.confirm(
                `确定要删除 "${file.name}" 吗？文件将被移至回收站。`,
                '删除确认',
                {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }
            ).then(async () => {
                try {
                    // 模拟删除
                    await new Promise(resolve => setTimeout(resolve, 500))
                    ElMessage.success('文件已移至回收站')
                    // 重新加载数据
                    await loadDashboardData()
                } catch (error) {
                    console.error('删除失败:', error)
                    ElMessage.error('删除失败')
                }
            }).catch(() => {
                // 用户取消操作
            })
            break
    }
}

// 导航到指定页面
const navigateTo = (path) => {
    router.push(path)
}
</script>

<style scoped>
.home-page {
    padding: 20px;
    max-width: 1200px;
    margin: 0 auto;
}

.dashboard-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
}

.dashboard-header h1 {
    font-size: 24px;
    font-weight: 600;
    margin: 0;
    color: #1f2937;
}

.header-actions {
    display: flex;
    gap: 12px;
}

/* 存储概览区域 */
.storage-overview {
    display: grid;
    grid-template-columns: 2fr 1fr;
    gap: 20px;
    margin-bottom: 24px;
}

.storage-card, .stats-card {
    height: 100%;
    background-color: #ffffff;
    border: 1px solid #e5e7eb;
    border-radius: 12px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 20px;
    border-bottom: 1px solid #f3f4f6;
}

.card-header span {
    font-size: 16px;
    font-weight: 600;
    color: #1f2937;
}

.storage-info {
    display: flex;
    align-items: center;
    gap: 24px;
    padding: 20px;
}

.storage-chart {
    flex-shrink: 0;
}

.percentage-label {
    font-size: 16px;
    font-weight: bold;
    color: #1f2937;
}

.storage-details {
    flex-grow: 1;
}

.storage-text {
    font-size: 18px;
    font-weight: 500;
    margin-bottom: 16px;
}

.storage-text .used {
    color: #3b82f6;
}

.storage-text .total {
    color: #6b7280;
}

.storage-breakdown {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.breakdown-item {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 14px;
    color: #4b5563;
}

.color-dot {
    width: 12px;
    height: 12px;
    border-radius: 50%;
}

.color-dot.document {
    background-color: #3b82f6;
}

.color-dot.image {
    background-color: #10b981;
}

.color-dot.video {
    background-color: #f59e0b;
}

.color-dot.other {
    background-color: #6b7280;
}

/* 文件统计区域 */
.stats-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
    padding: 20px;
}

.stat-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 16px;
    background-color: #f8fafc;
    border-radius: 8px;
    border: 1px solid #e2e8f0;
}

.stat-value {
    font-size: 24px;
    font-weight: 600;
    color: #1f2937;
    margin-bottom: 8px;
}

.stat-label {
    font-size: 14px;
    color: #6b7280;
}

/* 最近文件区域 */
.recent-files-card {
    margin-bottom: 24px;
    background-color: #ffffff;
    border: 1px solid #e5e7eb;
    border-radius: 12px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.recent-files-card .card-header {
    padding: 16px 20px;
    border-bottom: 1px solid #f3f4f6;
}

.file-info {
    display: flex;
    align-items: center;
    gap: 8px;
}

.file-name {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    color: #1f2937;
}

/* 快速访问区域 */
.quick-access {
    margin-bottom: 24px;
}

.quick-access-card {
    background-color: #ffffff;
    border: 1px solid #e5e7eb;
    border-radius: 12px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.quick-access-card .card-header {
    padding: 16px 20px;
    border-bottom: 1px solid #f3f4f6;
}

.quick-access-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 16px;
    padding: 20px;
}

.quick-access-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 20px 10px;
    background-color: #f8fafc;
    border-radius: 8px;
    border: 1px solid #e2e8f0;
    cursor: pointer;
    transition: all 0.2s ease;
}

.quick-access-item:hover {
    background-color: #eff6ff;
    border-color: #3b82f6;
    transform: translateY(-1px);
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.quick-access-item i {
    font-size: 24px;
    margin-bottom: 8px;
    color: #3b82f6;
}

.quick-access-item span {
    font-size: 14px;
    color: #4b5563;
    font-weight: 500;
}

/* 上传区域样式 */
.upload-area {
    border: 1px dashed #d1d5db;
    border-radius: 8px;
    padding: 20px 0;
    text-align: center;
    background-color: #f9fafb;
}

.upload-area:hover {
    border-color: #3b82f6;
    background-color: #f0f9ff;
}

/* 响应式设计 */
@media (max-width: 768px) {
    .storage-overview {
        grid-template-columns: 1fr;
    }
    
    .stats-grid {
        grid-template-columns: repeat(2, 1fr);
    }
    
    .quick-access-grid {
        grid-template-columns: repeat(2, 1fr);
    }
    
    .dashboard-header {
        flex-direction: column;
        align-items: flex-start;
        gap: 16px;
    }
    
    .header-actions {
        width: 100%;
        justify-content: space-between;
    }
}

@media (max-width: 480px) {
    .home-page {
        padding: 12px;
    }
    
    .storage-info {
        flex-direction: column;
        gap: 16px;
    }
    
    .stats-grid {
        grid-template-columns: 1fr;
    }
    
    .quick-access-grid {
        grid-template-columns: 1fr;
    }
    
    .header-actions {
        flex-direction: column;
        gap: 8px;
    }
    
    .header-actions .el-button {
        width: 100%;
    }
}
</style>
