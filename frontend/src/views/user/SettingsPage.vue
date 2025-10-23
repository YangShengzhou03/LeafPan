<template>
  <div class="personal-center-page" @mousemove="handleMouseMove">
    <div class="personal-center-content">
      <!-- 个人信息卡片 -->
      <div class="personal-info-card modern-card" :class="{ 'panel-hover': isHoveringPanel }">
        <div class="card-header">
          <div class="avatar-section">
            <el-upload class="avatar-uploader" action="#" :show-file-list="false" :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload">
              <img v-if="userProfile.avatarUrl" :src="userProfile.avatarUrl" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon">
                <Plus />
              </el-icon>
            </el-upload>
            <div class="user-info">
              <h2 class="username">{{ userProfile.name }}</h2>
            </div>
          </div>
          <el-button type="primary" size="small" @click="openEditDialog" class="edit-button" round>
            <el-icon>
              <Edit />
            </el-icon> 编辑资料
          </el-button>
        </div>

        <div class="card-body">
          <div class="info-group">
            <div class="info-grid">
              <div class="info-item">
                <el-icon class="item-icon">
                  <User />
                </el-icon>
                <div class="item-content">
                  <p class="item-label">性别</p>
                  <p class="item-value">{{ getGenderText(userProfile.gender) }}</p>
                </div>
              </div>

              <div class="info-item">
                <el-icon class="item-icon">
                  <Clock />
                </el-icon>
                <div class="item-content">
                  <p class="item-label">最后登录时间</p>
                  <p class="item-value">{{ formatDateTime(userProfile.lastLoginTime) }}</p>
                </div>
              </div>

              <div class="info-item">
                <el-icon class="item-icon">
                  <Notification />
                </el-icon>
                <div class="item-content">
                  <p class="item-label">邮箱</p>
                  <p class="item-value">{{ userProfile.email }}</p>
                </div>
              </div>

              <div class="info-item">
                <el-icon class="item-icon">
                  <Phone />
                </el-icon>
                <div class="item-content">
                  <p class="item-label">手机号码</p>
                  <p class="item-value">{{ userProfile.phone }}</p>
                </div>
              </div>
            </div>
          </div>


        </div>
      </div>

      <!-- 快捷操作卡片 -->
      <div class="quick-actions-card modern-card">
        <div class="card-title">
          <el-icon class="title-icon">
            <Setting />
          </el-icon>
          <span>账户设置</span>
        </div>

        <div class="actions-grid">
          <div v-for="action in quickActions" :key="action.name" class="action-item" @click="action.handler">
            <div class="action-content">
              <el-icon class="action-icon">
                <component :is="action.icon" />
              </el-icon>
              <span>{{ action.name }}</span>
            </div>
            <el-icon class="arrow-icon">
              <ArrowRight />
            </el-icon>
          </div>
        </div>
      </div>
    </div>

    <!-- 编辑个人信息对话框 -->
    <el-dialog v-model="editDialogVisible" title="编辑个人信息" width="600px" :before-close="handleDialogClose">
      <el-form ref="editFormRef" :model="editForm" :rules="editFormRules" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="editForm.name" placeholder="请输入姓名" />
        </el-form-item>
        
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="editForm.gender">
            <el-radio label="male">男</el-radio>
            <el-radio label="female">女</el-radio>
            <el-radio label="other">其他</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editForm.email" placeholder="请输入邮箱地址" />
        </el-form-item>
        
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="editForm.phone" placeholder="请输入手机号码" />
        </el-form-item>
        
        <!-- 移除不允许修改的字段：学院和班级 -->
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button type="primary" @click="submitEditForm" :loading="submitLoading">
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 图像裁剪对话框 -->
    <el-dialog v-model="cropDialogVisible" title="裁剪头像" width="600px" :before-close="handleCropDialogClose">
      <div class="crop-container">
        <img ref="cropImage" :src="cropImageUrl" style="max-width: 100%; display: block;">
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleCropDialogClose">取消</el-button>
          <el-button type="primary" @click="cropAndUploadAvatar" :loading="cropLoading">
            确认裁剪并上传
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import {
  Plus, User, Edit, ArrowRight, Lock,
  Calendar, Notification, Phone, Setting, Bell,
  FirstAidKit, QuestionFilled, Clock
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import Server from '@/utils/Server.js'
import Cropper from 'cropperjs'

// 编辑对话框相关状态
const editDialogVisible = ref(false)
const submitLoading = ref(false)
const editFormRef = ref()

// 裁剪相关状态
const cropDialogVisible = ref(false)
const cropLoading = ref(false)
const cropImage = ref()
const cropImageUrl = ref('')
let cropper = null
const currentFile = ref(null)

// 编辑表单数据
const editForm = reactive({
  name: '',
  gender: 'male',
  email: '',
  phone: ''
})

// 表单验证规则
const editFormRules = {
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

// 首先定义所有函数
const openEditDialog = () => {
  // 将当前用户信息填充到编辑表单
  Object.assign(editForm, {
    name: userProfile.name,
    gender: userProfile.gender,
    email: userProfile.email,
    phone: userProfile.phone
  })
  editDialogVisible.value = true
}

const changePassword = () => ElMessage.info('修改密码功能将在未来版本中实现')
const notificationSettings = () => ElMessage.info('通知设置功能将在未来版本中实现')
const securitySettings = () => ElMessage.info('安全设置功能将在未来版本中实现')
const helpCenter = () => ElMessage.info('帮助中心功能将在未来版本中实现')

// 然后定义 quickActions 数组
const quickActions = [
  { name: '修改密码', icon: Lock, handler: changePassword },
  { name: '通知设置', icon: Bell, handler: notificationSettings },
  { name: '安全设置', icon: FirstAidKit, handler: securitySettings },
  { name: '帮助中心', icon: QuestionFilled, handler: helpCenter }
]

// 个人信息
const userProfile = reactive({
  name: '--',
  gender: '--',
  birthdate: '--',
  email: '--',
  phone: '--',
  lastLoginTime: '--',
  avatarUrl: 'https://picsum.photos/id/1005/200/200'
})

// 卡片悬停状态
const isHoveringPanel = ref(false)

// 鼠标位置跟踪
const handleMouseMove = (e) => {
  document.documentElement.style.setProperty('--mouse-x', `${e.clientX}px`)
  document.documentElement.style.setProperty('--mouse-y', `${e.clientY}px`)
}

// 格式化日期显示
const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('zh-CN')
}

// 格式化日期时间显示
const formatDateTime = (dateTimeString) => {
  if (!dateTimeString || dateTimeString === '--') return '--'
  const date = new Date(dateTimeString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// 获取性别的文本表示
const getGenderText = (gender) => {
  const genderMap = {
    'male': '男',
    'female': '女',
    'other': '其他'
  }
  return genderMap[gender] || '未知'
}

// 将英文性别转换为中文
const convertGenderToChinese = (gender) => {
  const genderMap = {
    'male': '男',
    'female': '女',
    'other': '其他'
  }
  return genderMap[gender] || gender
}

// 编辑对话框关闭处理
const handleDialogClose = () => {
  editDialogVisible.value = false
  editFormRef.value?.resetFields()
}

// 提交编辑表单
const submitEditForm = async () => {
  if (!editFormRef.value) return
  
  await editFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 转换性别值
        const genderValue = editForm.gender === 'male' ? 'MALE' : 
                           editForm.gender === 'female' ? 'FEMALE' : 
                           'NOT_SET'
        
        // 使用后端期望的字段名
        const response = await Server.put('/user/profile', {
          nickname: editForm.name, // 前端的name对应后端的nickname
          email: editForm.email,
          gender: genderValue, // 添加gender字段
          phone: editForm.phone // 添加phone字段
        })
        
        if (response.data && response.data.code === 200) {
          // 更新本地用户信息
          userProfile.name = editForm.name
          userProfile.email = editForm.email
          userProfile.gender = genderValue
          userProfile.phone = editForm.phone
          editDialogVisible.value = false
          ElMessage.success('个人信息更新成功')
        }
      } catch (error) {
        console.error('更新个人信息失败:', error)
        ElMessage.error('更新个人信息失败')
      }
    }
  })
}

// 组件挂载时获取用户信息
onMounted(() => {
  fetchUserInfo()
})

// 头像上传相关方法
const handleAvatarSuccess = (res, file) => {
  userProfile.avatarUrl = URL.createObjectURL(file.raw)
  ElMessage.success('头像更新成功')
}

const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('上传头像图片只能是 JPG 或 PNG 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('上传头像图片大小不能超过 2MB!')
    return false
  }
  
  // 保存文件并打开裁剪对话框
  currentFile.value = file
  cropImageUrl.value = URL.createObjectURL(file.raw)
  cropDialogVisible.value = true
  
  // 等待DOM更新后初始化cropper
  nextTick(() => {
    initCropper()
  })
  
  return false // 阻止自动上传
}

// 初始化裁剪器
const initCropper = () => {
  if (cropper) {
    cropper.destroy()
  }
  
  cropper = new Cropper(cropImage.value, {
    aspectRatio: 1, // 设置为1:1的比例
    viewMode: 1,
    dragMode: 'move',
    autoCropArea: 0.8,
    restore: false,
    guides: true,
    center: true,
    highlight: false,
    cropBoxMovable: true,
    cropBoxResizable: true,
    toggleDragModeOnDblclick: false,
  })
}

// 关闭裁剪对话框
const handleCropDialogClose = () => {
  cropDialogVisible.value = false
  if (cropper) {
    cropper.destroy()
    cropper = null
  }
  if (cropImageUrl.value) {
    URL.revokeObjectURL(cropImageUrl.value)
    cropImageUrl.value = ''
  }
  currentFile.value = null
}

// 裁剪并上传头像
const cropAndUploadAvatar = () => {
  if (!cropper || !currentFile.value) {
    ElMessage.error('裁剪失败，请重试')
    return
  }
  
  cropLoading.value = true
  
  // 获取裁剪后的canvas
  const canvas = cropper.getCroppedCanvas({
    maxWidth: 4096,
    maxHeight: 4096,
    fillColor: '#fff',
    imageSmoothingEnabled: true,
    imageSmoothingQuality: 'high',
  })
  
  // 将canvas转换为blob
  canvas.toBlob((blob) => {
    if (!blob) {
      ElMessage.error('图像处理失败，请重试')
      cropLoading.value = false
      return
    }
    
    // 创建新的File对象
    const croppedFile = new File([blob], currentFile.value.name, {
      type: currentFile.value.type,
      lastModified: Date.now(),
    })
    
    // 创建FormData并上传
    const formData = new FormData()
    formData.append('file', croppedFile)
    
    // 这里应该调用上传API，目前先模拟上传成功
    // 实际项目中应该替换为真实的API调用
    // Server.post('/upload/avatar', formData).then(response => {...})
    
    // 模拟上传成功
    setTimeout(() => {
      userProfile.avatarUrl = URL.createObjectURL(blob)
      cropLoading.value = false
      handleCropDialogClose()
      ElMessage.success('头像更新成功')
    }, 1000)
  }, currentFile.value.type, 0.9)
}

// 获取用户信息
const fetchUserInfo = async () => {
  try {
    const response = await Server.get('/user/profile')
    console.log('获取用户信息响应:', response)
    if (response.data && response.data.data) {
      // 更新用户信息 - 使用后端实际返回的字段名
      Object.assign(userProfile, {
        name: response.data.data.email || '--', // 使用email作为显示名称
        gender: response.data.data.gender || 'NOT_SET', // 使用后端返回的gender字段
        birthdate: '--', // 后端暂无birthdate字段
        email: response.data.data.email || '--',
        phone: response.data.data.phone || '--', // 使用后端返回的phone字段
        lastLoginTime: response.data.data.lastLoginTime || '--',
        avatarUrl: response.data.data.avatar || userProfile.avatarUrl
      })
      
      // 填充表单数据
      editForm.name = userProfile.name
      editForm.gender = userProfile.gender === 'MALE' ? 'male' : 
                      userProfile.gender === 'FEMALE' ? 'female' : 
                      userProfile.gender === 'NOT_SET' ? 'other' : 'male'
      editForm.email = userProfile.email
      editForm.phone = userProfile.phone
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    ElMessage.error('获取用户信息失败')
  }
}
</script>

<style scoped lang="scss">
:root {
  --bg-color: #ffffff;
  --card-bg: #ffffff;
  --card-border: 1px solid #e5e7eb;
  --card-border-hover: 1px solid #3b82f6;
  --text-primary: #1f2937;
  --text-secondary: #4b5563;
  --text-tertiary: #6b7280;
  --accent-color: #3b82f6;
  --hover-bg: #eff6ff;
  --item-bg: #f8fafc;
  --shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  --shadow-hover: 0 2px 4px rgba(0, 0, 0, 0.1);
  --avatar-border: 3px solid #ffffff;
  --uploader-bg: #f9fafb;
  --uploader-border: 2px dashed #d1d5db;
  --info-title-color: #3b82f6;
}

@media (prefers-color-scheme: dark) {
  :root {
    --bg-color: #1a1c23;
    --card-bg: #1f2937;
    --card-border: 1px solid #374151;
    --card-border-hover: 1px solid #3b82f6;
    --text-primary: #f9fafb;
    --text-secondary: #d1d5db;
    --text-tertiary: #9ca3af;
    --accent-color: #60a5fa;
    --hover-bg: #374151;
    --item-bg: #374151;
    --shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
    --shadow-hover: 0 2px 4px rgba(0, 0, 0, 0.2);
    --avatar-border: 3px solid #1f2937;
    --uploader-bg: #374151;
    --uploader-border: 2px dashed #4b5563;
    --info-title-color: #60a5fa;
  }
}

.personal-center-page {
  background: var(--bg-color);
  padding: 20px;
  --mouse-x: 0;
  --mouse-y: 0;
}

.personal-center-content {
  width: 100%;
  margin: 0 auto;
  display: grid;
  gap: 20px;

  @media (min-width: 992px) {
    grid-template-columns: 1fr 1fr;
    align-items: start;
  }
}

.modern-card {
  position: relative;
  border-radius: 12px;
  padding: 20px;
  transition: all 0.2s ease;
  overflow: hidden;
  z-index: 1;
  background: var(--card-bg);
  border: var(--card-border);
  box-shadow: var(--shadow);

  .card-content {
    position: relative;
    z-index: 2;
  }

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: radial-gradient(600px circle at var(--mouse-x) var(--mouse-y),
        rgba(59, 130, 246, 0.05) 0%,
        transparent 70%);
    opacity: 0;
    transition: opacity 0.3s ease;
    z-index: -1;
    pointer-events: none;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;

    h2,
    h3 {
      margin: 0;
      font-size: 18px;
      font-weight: 600;
      color: var(--text-primary);
    }

    .progress-indicator {
      background-color: rgba(59, 130, 246, 0.1);
      color: #3b82f6;
      padding: 6px 12px;
      border-radius: 999px;
      font-size: 14px;
    }
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;

  .avatar-section {
    display: flex;
    align-items: center;
    gap: 16px;

    .user-info {
      .username {
        margin: 0;
        font-size: 1.25rem;
        font-weight: 600;
        color: var(--text-primary);
        line-height: 1.3;
      }

      .student-id {
        margin: 6px 0 0;
        font-size: 0.9rem;
        color: var(--text-secondary);
      }
    }
  }

  .edit-button {
    padding: 8px 16px;
    font-weight: 500;
    transition: all 0.2s ease;

    &:hover {
      transform: translateY(-1px);
    }

    .el-icon {
      margin-right: 6px;
    }
  }
}

.avatar-uploader {
  .avatar {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    object-fit: cover;
    border: var(--avatar-border);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    transition: all 0.2s ease;
    cursor: pointer;

    &:hover {
      transform: scale(1.05);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }
  }

  .avatar-uploader-icon {
    width: 80px;
    height: 80px;
    display: flex;
    align-items: center;
    justify-content: center;
    border: var(--uploader-border);
    border-radius: 50%;
    background-color: var(--uploader-bg);
    color: var(--text-tertiary);
    font-size: 20px;
    cursor: pointer;
    transition: all 0.2s ease;

    &:hover {
      border-color: var(--accent-color);
      background-color: rgba(59, 130, 246, 0.1);
      color: var(--accent-color);
    }
  }
}

.card-body {
  .info-group {
    margin-bottom: 20px;

    &:last-child {
      margin-bottom: 0;
    }

    .info-title {
      font-size: 0.9rem;
      font-weight: 600;
      color: var(--info-title-color);
      margin: 0 0 12px 0;
      padding-bottom: 8px;
      border-bottom: 1px solid rgba(59, 130, 246, 0.2);
    }

    .info-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 12px;

      @media (max-width: 768px) {
        grid-template-columns: 1fr;
      }

      .info-item {
        display: flex;
        align-items: flex-start;
        padding: 12px 16px;
        border-radius: 8px;
        background: var(--item-bg);
        border: 1px solid #e2e8f0;
        transition: all 0.2s ease;
        cursor: default;
        text-align: left;

        &:hover {
          background: var(--hover-bg);
          border-color: var(--accent-color);
          transform: translateY(-1px);
        }

        .item-icon {
          font-size: 18px;
          color: var(--accent-color);
          margin-right: 12px;
          flex-shrink: 0;
          width: 18px;
          text-align: center;
          margin-top: 10px;
        }

        .item-content {
          flex: 1;
          min-width: 0;
          text-align: left;
          
          .item-label {
            font-size: 0.8rem;
            color: var(--text-secondary);
            margin-bottom: 4px;
            margin-left: 2px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            text-align: left;
          }

          .item-value {
            font-size: 0.9rem;
            font-weight: 500;
            color: var(--text-primary);
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            text-align: left;
          }
        }
      }
    }
  }
}

.quick-actions-card {
  .card-title {
    display: flex;
    align-items: center;
    margin-bottom: 16px;
    font-size: 1.1rem;
    font-weight: 600;
    color: var(--text-primary);

    .title-icon {
      margin-right: 12px;
      color: var(--accent-color);
      font-size: 1.1em;
    }
  }

  .actions-grid {
    display: grid;
    grid-template-columns: 1fr;
    gap: 8px;
  }

  .action-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 16px;
    border-radius: 8px;
    transition: all 0.2s ease;
    cursor: pointer;
    background: var(--item-bg);
    border: 1px solid #e2e8f0;

    &:hover {
      background: var(--hover-bg);
      border-color: var(--accent-color);
      transform: translateY(-1px);

      .action-content {
        color: var(--accent-color);
      }

      .arrow-icon {
        color: var(--accent-color);
        transform: translateX(2px);
      }
    }

    .action-content {
      display: flex;
      align-items: center;
      color: var(--text-primary);
      transition: color 0.2s ease;

      .action-icon {
        margin-right: 12px;
        color: var(--accent-color);
        font-size: 1em;
      }

      span {
        font-weight: 500;
      }
    }

    .arrow-icon {
      color: var(--text-tertiary);
      transition: all 0.2s ease;
    }
  }
}

.crop-container {
  height: 400px;
  width: 100%;
}

/* 覆盖cropperjs的默认样式，使其适应Element Plus的对话框 */
:deep(.cropper-container) {
  max-height: 400px;
}

:deep(.cropper-view-box) {
  outline: 1px solid rgba(59, 130, 246, 0.5);
  outline-color: rgba(59, 130, 246, 0.5);
}

:deep(.cropper-face) {
  background-color: inherit;
}
</style>









