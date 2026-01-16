<template>
  <div class="personal-center-page">
    <div class="personal-center-content">
      <!-- 个人信息卡片 -->
      <div class="personal-info-card modern-card">
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

    <!-- 图片裁剪对话框 -->
    <el-dialog v-model="cropDialogVisible" title="裁剪头像" width="600px" :before-close="handleCropDialogClose">
      <div class="crop-container">
        <Cropper
          ref="cropperRef"
          :src="cropImageUrl"
          :stencil-props="{
            aspectRatio: 1,
          }"
          :canvas="{
            width: 300,
            height: 300,
          }"
          :stencil-size="{
            width: 250,
            height: 250,
          }"
          image-restriction="stencil"
          :resize-image="{
            adjustStencil: false,
          }"
          :move-image="{
            enabled: true,
          }"
          :default-size="{
            width: 250,
            height: 250,
          }"
        />
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

    <!-- 修改密码对话框 -->
    <el-dialog v-model="changePasswordDialogVisible" title="修改密码" width="500px" :before-close="handleChangePasswordDialogClose">
      <el-form ref="changePasswordFormRef" :model="changePasswordForm" :rules="changePasswordRules" label-width="100px">
        <!-- 第一步：邮箱验证 -->
        <div v-if="currentStep === 1">
          <el-form-item label="邮箱地址" prop="email">
            <el-input v-model="changePasswordForm.email" placeholder="请输入您的邮箱地址" />
          </el-form-item>
          <el-form-item label="验证码" prop="verificationCode">
            <div class="verification-code-input">
              <el-input v-model="changePasswordForm.verificationCode" placeholder="请输入验证码" maxlength="6" />
              <el-button 
                type="primary" 
                :disabled="countdown > 0" 
                @click="sendVerificationCode"
                :loading="sendingCode"
                class="send-code-btn"
              >
                {{ countdown > 0 ? `${countdown}秒后重发` : '发送验证码' }}
              </el-button>
            </div>
          </el-form-item>
        </div>

        <!-- 第二步：设置新密码 -->
        <div v-if="currentStep === 2">
          <el-form-item label="新密码" prop="newPassword">
            <el-input 
              v-model="changePasswordForm.newPassword" 
              type="password" 
              placeholder="请输入新密码" 
              show-password 
            />
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input 
              v-model="changePasswordForm.confirmPassword" 
              type="password" 
              placeholder="请再次输入新密码" 
              show-password 
            />
          </el-form-item>
        </div>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleChangePasswordDialogClose">取消</el-button>
          <el-button v-if="currentStep === 1" type="primary" @click="verifyCodeAndNext" :loading="verifyingCode">
            下一步
          </el-button>
          <el-button v-if="currentStep === 2" type="primary" @click="submitNewPassword" :loading="submittingPassword">
            确认修改
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
  Notification, Phone, Setting, Bell,
  FirstAidKit, QuestionFilled, Clock
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import Server from '@/utils/Server.js'
import { Cropper } from 'vue-advanced-cropper'
import 'vue-advanced-cropper/dist/style.css'
import store from '@/utils/store.js'

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
const cropperRef = ref(null)
const currentFile = ref(null)

// 修改密码相关状态
const changePasswordDialogVisible = ref(false)
const changePasswordFormRef = ref()
const currentStep = ref(1) // 1: 邮箱验证, 2: 设置新密码
const countdown = ref(0)
const sendingCode = ref(false)
const verifyingCode = ref(false)
const submittingPassword = ref(false)

// 修改密码表单数据
const changePasswordForm = reactive({
  email: '',
  verificationCode: '',
  newPassword: '',
  confirmPassword: ''
})



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

// 修改密码表单验证规则
const changePasswordRules = {
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  verificationCode: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { pattern: /^\d{6}$/, message: '验证码必须是6位数字', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' },
    { pattern: /^(?=.*[a-zA-Z])(?=.*\d).+$/, message: '密码必须包含字母和数字', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== changePasswordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
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

// 修改密码功能
const changePassword = () => {
  // 重置表单和状态
  currentStep.value = 1
  countdown.value = 0
  Object.assign(changePasswordForm, {
    email: userProfile.email || '',
    verificationCode: '',
    newPassword: '',
    confirmPassword: ''
  })
  changePasswordDialogVisible.value = true
}
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
  // 验证文件
  const isJPG = file.raw.type === 'image/jpeg' || file.raw.type === 'image/png' || file.raw.type === 'image/gif'
  const isLt2M = file.raw.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('头像图片只能是 JPG/PNG/GIF 格式!')
    return
  }
  if (!isLt2M) {
    ElMessage.error('头像图片大小不能超过 2MB!')
    return
  }

  // 保存文件并打开裁剪对话框
  currentFile.value = file.raw
  cropImageUrl.value = URL.createObjectURL(file.raw)
  cropDialogVisible.value = true
}

// 裁剪并上传头像
const cropAndUploadAvatar = async () => {
  if (!cropperRef.value) {
    ElMessage.error('裁剪器未初始化')
    return
  }

  cropLoading.value = true
  try {
    // 获取裁剪后的canvas
    const { canvas } = cropperRef.value.getResult()
    
    // 将canvas转换为blob
    canvas.toBlob(async (blob) => {
      if (!blob) {
        ElMessage.error('图片处理失败')
        cropLoading.value = false
        return
      }

      try {
        // 创建FormData并上传
        const formData = new FormData()
        formData.append('file', blob, 'avatar.jpg')

        // 调用上传API - 注意：不要手动设置Content-Type，让浏览器自动处理
        const response = await Server.post('/avatar/upload', formData)

        // 由于Server.js的响应拦截器，response已经是处理后的数据
        if (response && response.code === 200) {
          // 更新本地头像URL - 使用完整的API地址
          const currentUser = store.state.user
          if (currentUser && currentUser.id) {
            userProfile.avatarUrl = `${process.env.VUE_APP_API_URL}/avatar/view/${currentUser.id}`
          }
          cropDialogVisible.value = false
          ElMessage.success('头像更新成功')
        } else {
          throw new Error(response?.message || '上传失败')
        }
      } catch (error) {
        // 提供更详细的错误信息
        let errorMessage = '头像上传失败'
        if (error.response) {
          // 服务器返回的错误
          errorMessage = error.response.data?.message || `服务器错误: ${error.response.status}`
        } else if (error.request) {
          // 网络错误
          errorMessage = '网络连接失败，请检查网络设置'
        } else {
          // 其他错误
          errorMessage = error.message || '请重试'
        }
        ElMessage.error(errorMessage)
      } finally {
        cropLoading.value = false
      }
    }, 'image/jpeg', 0.9)
  } catch (error) {
    ElMessage.error('图片裁剪失败: ' + (error.message || '请重试'))
    cropLoading.value = false
  }
}

// 关闭裁剪对话框
const handleCropDialogClose = () => {
  cropDialogVisible.value = false
  // 释放URL对象
  if (cropImageUrl.value) {
    URL.revokeObjectURL(cropImageUrl.value)
    cropImageUrl.value = ''
  }
  currentFile.value = null
}

// 修改密码对话框关闭处理
const handleChangePasswordDialogClose = () => {
  changePasswordDialogVisible.value = false
  currentStep.value = 1
  changePasswordFormRef.value?.resetFields()
}

// 发送邮箱验证码
const sendVerificationCode = async () => {
  if (!changePasswordForm.email) {
    ElMessage.error('请输入邮箱地址')
    return
  }

  sendingCode.value = true
  try {
    const response = await Server.post('/verification/send', {
          email: changePasswordForm.email
        })

    if (response.code === 200) {
      ElMessage.success('验证码已发送到您的邮箱')
      // 开始倒计时
      countdown.value = 60
      const timer = setInterval(() => {
        countdown.value--
        if (countdown.value <= 0) {
          clearInterval(timer)
        }
      }, 1000)
    } else {
      ElMessage.error(response.message || '发送验证码失败')
    }
  } catch (error) {
    ElMessage.error('发送验证码失败，请重试')
  } finally {
    sendingCode.value = false
  }
}

// 验证验证码并进入下一步
const verifyCodeAndNext = async () => {
  if (!changePasswordFormRef.value) return

  await changePasswordFormRef.value.validate(async (valid) => {
    if (valid) {
      verifyingCode.value = true
      try {
        const response = await Server.post('/verification/verify', {
          email: changePasswordForm.email,
          code: changePasswordForm.verificationCode,
          type: 'password_reset' // 指定为密码重置类型
        })

        if (response.code === 200) {
          ElMessage.success('验证码验证成功')
          // 保存重置令牌
          changePasswordForm.resetToken = response.data.resetToken
          currentStep.value = 2
        } else {
          ElMessage.error(response.message || '验证码验证失败')
        }
      } catch (error) {
        ElMessage.error('验证码验证失败，请重试')
      } finally {
        verifyingCode.value = false
      }
    }
  })
}

// 提交新密码
const submitNewPassword = async () => {
  if (!changePasswordFormRef.value) return

  await changePasswordFormRef.value.validate(async (valid) => {
    if (valid) {
      submittingPassword.value = true
      try {
        const response = await Server.post('/auth/reset-password', {
          email: changePasswordForm.email,
          resetToken: changePasswordForm.resetToken,
          newPassword: changePasswordForm.newPassword
        })

        if (response.code === 200) {
          ElMessage.success('密码修改成功，请重新登录')
          
          // 清除用户token和状态
          store.clearUser()
          
          // 关闭对话框
          handleChangePasswordDialogClose()
          
          // 延迟跳转到登录页面，确保消息显示完整
          setTimeout(() => {
            window.location.href = '/login'
          }, 1500)
        } else {
          ElMessage.error(response.message || '密码修改失败')
        }
      } catch (error) {
        ElMessage.error('密码修改失败，请重试')
      } finally {
        submittingPassword.value = false
      }
    }
  })
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
        avatarUrl: userData.avatar ? `${process.env.VUE_APP_API_URL}/avatar/view/${userData.id}` : userProfile.avatarUrl
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
  background: #fff;
  padding: 0;
}

.personal-center-content {
  width: 100%;
  margin: 0 auto;
  display: grid;
  gap: 16px;

  @media (min-width: 992px) {
    grid-template-columns: 1fr 1fr;
    align-items: start;
  }
}

.modern-card {
  position: relative;
  border-radius: 4px;
  padding: 16px;
  border: 1px solid #e5e7eb;
  background: #fff;
  z-index: 1;

  .card-content {
    position: relative;
    z-index: 2;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;

    h2,
    h3 {
      margin: 0;
      font-size: 16px;
      font-weight: 600;
      color: #1f2937;
    }
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;

  .avatar-section {
    display: flex;
    align-items: center;
    gap: 12px;

    .user-info {
      .username {
        margin: 0;
        font-size: 18px;
        font-weight: 600;
        color: #1f2937;
        line-height: 1.3;
      }
    }
  }

  .edit-button {
    padding: 6px 12px;
    font-weight: 500;
    transition: all 0.2s;

    &:hover {
      background-color: #f3f4f6;
    }

    .el-icon {
      margin-right: 4px;
    }
  }
}

.avatar-uploader {
  .avatar-container {
    position: relative;
    width: 72px;
    height: 72px;
    border-radius: 4px;
    overflow: hidden;
    cursor: pointer;
    transition: all 0.2s;

    &:hover {
      border-color: #2563eb;
    }

    .avatar {
      width: 100%;
      height: 100%;
      object-fit: cover;
      border-radius: 4px;
      border: 1px solid #e5e7eb;
    }

    .avatar-uploader-icon {
      width: 100%;
      height: 100%;
      font-size: 32px;
      color: #9ca3af;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #f9fafb;
      border: 1px dashed #d1d5db;
      border-radius: 4px;
    }

    .avatar-overlay {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: rgba(0, 0, 0, 0.5);
      border-radius: 4px;
      display: flex;
      align-items: center;
      justify-content: center;
      opacity: 0;
      transition: opacity 0.2s;

      &.show {
        opacity: 1;
      }

      .edit-icon {
        font-size: 20px;
        color: white;
      }
    }
  }
}

.card-body {
  .info-group {
    margin-bottom: 16px;

    &:last-child {
      margin-bottom: 0;
    }

    .info-title {
      font-size: 14px;
      font-weight: 600;
      color: #2563eb;
      margin: 0 0 10px 0;
      padding-bottom: 6px;
      border-bottom: 1px solid #e5e7eb;
    }

    .info-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 10px;

      @media (max-width: 768px) {
        grid-template-columns: 1fr;
      }

      .info-item {
        display: flex;
        align-items: flex-start;
        padding: 10px 12px;
        border-radius: 4px;
        background: #f9fafb;
        border: 1px solid #e5e7eb;
        cursor: default;
        text-align: left;

        &:hover {
          background: #f3f4f6;
          border-color: #2563eb;
        }

        .item-icon {
          font-size: 16px;
          color: #2563eb;
          margin-right: 10px;
          flex-shrink: 0;
          width: 16px;
          text-align: center;
          margin-top: 8px;
        }

        .item-content {
          flex: 1;
          min-width: 0;
          text-align: left;

          .item-label {
            font-size: 12px;
            color: #6b7280;
            margin-bottom: 4px;
            margin-left: 2px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            text-align: left;
          }

          .item-value {
            font-size: 14px;
            font-weight: 500;
            color: #1f2937;
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
    margin-bottom: 12px;
    font-size: 16px;
    font-weight: 600;
    color: #1f2937;

    .title-icon {
      margin-right: 10px;
      color: #2563eb;
      font-size: 1em;
    }
  }

  .actions-grid {
    display: grid;
    grid-template-columns: 1fr;
    gap: 6px;
  }

  .action-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 12px;
    border-radius: 4px;
    cursor: pointer;
    background: #f9fafb;
    border: 1px solid #e5e7eb;

    &:hover {
      background: #f3f4f6;
      border-color: #2563eb;

      .action-content {
        color: #2563eb;
      }

      .arrow-icon {
        color: #2563eb;
      }
    }

    .action-content {
      display: flex;
      align-items: center;
      color: #1f2937;
      transition: color 0.2s;

      .action-icon {
        margin-right: 10px;
        color: #2563eb;
        font-size: 1em;
      }

      span {
        font-weight: 500;
      }
    }

    .arrow-icon {
      color: #9ca3af;
      transition: all 0.2s;
    }
  }
}

.crop-container {
  width: 100%;
  height: 350px;
  margin: 0 auto;
}

:deep(.vue-advanced-cropper) {
  height: 100%;
}

:deep(.vue-advanced-cropper__canvas) {
  max-width: 100%;
  max-height: 100%;
}

:deep(.vue-advanced-cropper__stencil) {
  border: 2px solid #2563eb;
}

:deep(.vue-advanced-cropper__line) {
  border-color: #2563eb;
}

:deep(.vue-advanced-cropper__handle) {
  background-color: #2563eb;
  border-color: #fff;
}

.verification-code-input {
  display: flex;
  gap: 8px;
  align-items: center;
}

.verification-code-input .el-input {
  flex: 1;
}

.send-code-btn {
  min-width: 100px;
  white-space: nowrap;
}
</style>