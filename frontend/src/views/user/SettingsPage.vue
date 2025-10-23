<template>
  <div class="personal-center-page" @mousemove="handleMouseMove">
    <div class="personal-center-content">
      <!-- 个人信息卡片 -->
      <div class="personal-info-card modern-card" :class="{ 'panel-hover': isHoveringPanel }">
        <div class="card-header">
          <div class="avatar-section">
            <el-upload class="avatar-uploader" :auto-upload="false" :show-file-list="false"
              :on-change="handleAvatarChange" :before-upload="beforeAvatarUpload">
              <div class="avatar-container" @mouseenter="showAvatarOverlay = true"
                @mouseleave="showAvatarOverlay = false">
                <img v-if="userProfile.avatarUrl" :src="userProfile.avatarUrl" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon">
                  <Plus />
                </el-icon>
                <div class="avatar-overlay" :class="{ 'show': showAvatarOverlay }">
                  <el-icon class="edit-icon">
                    <Edit />
                  </el-icon>
                </div>
              </div>
            </el-upload>
            <div class="user-info">
              <h1 class="username">{{ userProfile.name }}</h1>
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
          <el-input v-model="editForm.name" placeholder="请输入姓名" maxlength="12" show-word-limit />
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
          <el-input v-model="editForm.phone" placeholder="请输入手机号码" maxlength="11" />
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
        <vue-cropper 
          ref="cropper" 
          :src="cropImageUrl" 
          :aspect-ratio="1"
          :view-mode="2"
          :auto-crop-area="0.8"
          :movable="true"
          :zoomable="true"
          :rotatable="true"
          :scalable="true"
          style="width: 100%; height: 400px;"
        ></vue-cropper>
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
import { ref, reactive, onMounted } from 'vue'
import {
  Plus, User, Edit, ArrowRight, Lock,
  Calendar, Notification, Phone, Setting, Bell,
  FirstAidKit, QuestionFilled, Clock
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import Server from '@/utils/Server.js'
import VueCropper from 'vue-cropper'

// 头像遮罩层显示状态
const showAvatarOverlay = ref(false)

// 编辑对话框相关状态
const editDialogVisible = ref(false)
const submitLoading = ref(false)
const editFormRef = ref()

// 裁剪相关状态
const cropDialogVisible = ref(false)
const cropLoading = ref(false)
const cropImageUrl = ref('')
const cropper = ref()
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
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { max: 12, message: '昵称长度不能超过12个字符', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的11位手机号码', trigger: 'blur' }
  ]
}

// 首先定义所有函数
const openEditDialog = () => {
  // 将当前用户信息填充到编辑表单
  Object.assign(editForm, {
    name: userProfile.name,
    gender: userProfile.gender === 1 ? 'male' :
      userProfile.gender === 2 ? 'female' :
        'other', // 0(未知)或其他值映射为other
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
  // 后端使用Byte类型：0-未知，1-男，2-女
  const genderMap = {
    0: '未知',
    1: '男',
    2: '女'
  }
  return genderMap[gender] || '未知'
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
      submitLoading.value = true
      try {
        // 转换性别值：后端使用Byte类型，0-未知，1-男，2-女
        const genderValue = editForm.gender === 'male' ? 1 :
          editForm.gender === 'female' ? 2 :
            0 // other映射为0(未知)

        // 使用后端期望的字段名，注意后端接收Map<String, String>，所有值必须是字符串
        const response = await Server.put('/user/profile', {
          nickname: editForm.name, // 前端的name对应后端的nickname
          email: editForm.email,
          gender: genderValue.toString(), // 转换为字符串，因为后端接收Map<String, String>
          phone: editForm.phone // 添加phone字段
        })

        // 检查响应结构，注意Server.js拦截器已经处理了响应
        // 如果后端返回{code: 200, message: "更新成功", data: User对象}
        // 拦截器会直接返回这个完整的响应对象
        if (response && response.code === 200) {
          // 更新本地用户信息
          userProfile.name = editForm.name
          userProfile.email = editForm.email
          userProfile.gender = genderValue
          userProfile.phone = editForm.phone
          editDialogVisible.value = false
          ElMessage.success('个人信息更新成功')
        } else if (response && response.status === 200) {
          // 如果后端直接返回用户数据（没有code字段）
          // 更新本地用户信息
          userProfile.name = editForm.name
          userProfile.email = editForm.email
          userProfile.gender = genderValue
          userProfile.phone = editForm.phone
          editDialogVisible.value = false
          ElMessage.success('个人信息更新成功')
        } else {
          ElMessage.error(response?.message || '更新失败，请重试')
        }
      } catch (error) {
        ElMessage.error('更新个人信息失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 组件挂载时获取用户信息
onMounted(() => {
  fetchUserInfo()
})

// 头像选择变化处理
const handleAvatarChange = (file) => {
  // 当用户选择文件后，触发裁剪流程
  beforeAvatarUpload(file.raw)
}

// 头像上传前验证
const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('头像图片只能是 JPG/PNG/GIF 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('头像图片大小不能超过 2MB!')
    return false
  }

  // 验证通过后打开裁剪对话框
  cropImageUrl.value = URL.createObjectURL(file)
  cropDialogVisible.value = true

  // 保存当前文件用于后续裁剪
  currentFile.value = file

  return false // 阻止自动上传
}

// 关闭裁剪对话框
const handleCropDialogClose = () => {
  cropDialogVisible.value = false
  if (cropImageUrl.value) {
    URL.revokeObjectURL(cropImageUrl.value)
    cropImageUrl.value = ''
  }
  currentFile.value = null
}

// 裁剪并上传头像
const cropAndUploadAvatar = async () => {
  if (!cropper.value || !currentFile.value) {
    ElMessage.error('裁剪失败，请重试')
    return
  }

  cropLoading.value = true

  try {
    // 使用vue-cropper的getCroppedCanvas方法获取裁剪后的canvas
    const canvas = cropper.value.getCroppedCanvas({
      width: 200,
      height: 200,
      fillColor: '#fff',
      imageSmoothingEnabled: true,
      imageSmoothingQuality: 'high',
    })

    if (!canvas) {
      throw new Error('无法获取裁剪后的图像')
    }

    // 将canvas转换为blob
    const blob = await new Promise((resolve) => {
      canvas.toBlob((blob) => {
        resolve(blob)
      }, currentFile.value.type || 'image/jpeg', 0.9)
    })

    if (!blob) {
      ElMessage.error('图像处理失败，请重试')
      cropLoading.value = false
      return
    }

    // 创建新的File对象
    const croppedFile = new File([blob], 'avatar.' + (currentFile.value.type === 'image/png' ? 'png' : 'jpg'), {
      type: currentFile.value.type || 'image/jpeg',
      lastModified: Date.now(),
    })

    // 创建FormData并上传
    const formData = new FormData()
    formData.append('file', croppedFile)

    // 调用真实的上传API
    const response = await Server.post('/user/avatar', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    if (response.data && response.data.code === 200) {
      // 更新本地头像URL
      userProfile.avatarUrl = response.data.data
      cropLoading.value = false
      handleCropDialogClose()
      ElMessage.success('头像更新成功')
    } else {
      throw new Error(response.data?.message || '上传失败')
    }
  } catch (error) {
    cropLoading.value = false
    ElMessage.error('头像上传失败: ' + (error.message || '请重试'))
  }
}

// 获取用户信息
const fetchUserInfo = async () => {
  try {
    const response = await Server.get('/user/profile')
    if (response.data) {
      // 更新用户信息 - Server.js响应拦截器已将后端响应包装为response.data
      // 后端数据结构为{code: 200, message: "操作成功", data: {user: {...}}}
      const userData = response.data.data || response.data
      Object.assign(userProfile, {
        name: userData.nickname || userData.email || '--', // 优先使用nickname，如果没有则使用email
        gender: userData.gender || 0, // 使用后端返回的gender字段，默认为0(未知)
        birthdate: '--', // 后端暂无birthdate字段
        email: userData.email || '--',
        phone: userData.phone || '--', // 使用后端返回的phone字段
        lastLoginTime: userData.lastLoginTime || '--',
        avatarUrl: userData.avatar || userProfile.avatarUrl
      })

      // 填充表单数据
      editForm.name = userProfile.name
      editForm.gender = userProfile.gender === 1 ? 'male' :
        userProfile.gender === 2 ? 'female' :
          'other' // 0(未知)或其他值映射为other
      editForm.email = userProfile.email
      editForm.phone = userProfile.phone
    }
  } catch (error) {
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
  .avatar-container {
    position: relative;
    width: 80px;
    height: 80px;
    border-radius: 50%;
    overflow: hidden;
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
      transform: scale(1.05);
    }

    .avatar {
      width: 100%;
      height: 100%;
      object-fit: cover;
      border-radius: 50%;
      border: var(--avatar-border);
    }

    .avatar-uploader-icon {
      width: 100%;
      height: 100%;
      font-size: 40px;
      color: var(--text-tertiary);
      display: flex;
      align-items: center;
      justify-content: center;
      background: var(--uploader-bg);
      border: var(--uploader-border);
      border-radius: 50%;
    }

    .avatar-overlay {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: rgba(0, 0, 0, 0.6);
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      opacity: 0;
      transition: opacity 0.3s ease;

      &.show {
        opacity: 1;
      }

      .edit-icon {
        font-size: 24px;
        color: white;
      }
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
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f5f5;
  border-radius: 8px;
  overflow: hidden;
}

.crop-container img {
  max-width: 100%;
  max-height: 100%;
}

/* 覆盖cropperjs的默认样式，使其适应Element Plus的对话框 */
:deep(.cropper-container) {
  max-height: 400px;
  max-width: 100%;
}

:deep(.cropper-view-box) {
  outline: 2px solid #409eff;
  outline-color: #409eff;
  border-radius: 50%;
}

:deep(.cropper-face) {
  background-color: inherit;
}

:deep(.cropper-modal) {
  background-color: rgba(0, 0, 0, 0.5);
}

:deep(.cropper-point) {
  background-color: #409eff;
  width: 8px;
  height: 8px;
}

:deep(.cropper-line) {
  background-color: #409eff;
}
</style>
