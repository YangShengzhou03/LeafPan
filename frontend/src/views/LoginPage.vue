<template>
  <div class="login-container">
    <div class="login-form">
      <div class="logo-section">
        <h1>轻羽云盘</h1>
        <p class="subtitle">安全、简单的个人网盘服务</p>
      </div>
      
      <el-form 
        ref="loginFormRef" 
        :model="loginForm" 
        :rules="loginRules" 
        label-width="0"
        class="form-content"
      >
        <el-form-item prop="email">
          <el-input 
            v-model="loginForm.email" 
            placeholder="邮箱地址"
            prefix-icon="Message"
            size="large"
          />
        </el-form-item>
        
        <template v-if="loginType === 'code'">
          <el-form-item prop="verificationCode">
            <div class="verification-code-container">
              <el-input 
                v-model="loginForm.verificationCode" 
                placeholder="邮箱验证码"
                prefix-icon="Key"
                size="large"
                @keyup.enter="handleLoginOrRegister"
              />
              <el-button 
                type="primary" 
                class="send-code-btn"
                :disabled="isSendingCode || countdown > 0"
                @click="sendVerificationCode"
                size="large"
              >
                {{ countdown > 0 ? `${countdown}s后重试` : '发送验证码' }}
              </el-button>
            </div>
          </el-form-item>
        </template>
        
        <template v-if="loginType === 'password'">
          <el-form-item prop="password">
            <el-input 
              v-model="loginForm.password" 
              type="password"
              placeholder="密码"
              prefix-icon="Lock"
              size="large"
              show-password
              @keyup.enter="handlePasswordLogin"
            />
          </el-form-item>
          
          <div class="forgot-password">
            <a href="#" @click.prevent="showForgotPasswordDialog">忘记密码？</a>
          </div>
        </template>
        
        <el-form-item prop="agreeToTerms" v-if="loginType === 'code'">
          <el-checkbox v-model="loginForm.agreeToTerms">
            我已阅读并同意<a href="#" @click.prevent="showTerms">《用户协议》</a>和<a href="#" @click.prevent="showPrivacy">《隐私政策》</a>
          </el-checkbox>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            class="submit-btn" 
            :loading="loginLoading"
            @click="() => loginType === 'code' ? handleLoginOrRegister() : handlePasswordLogin()"
            size="large"
          >
            {{ loginType === 'code' ? '登录 / 注册' : '登录' }}
          </el-button>
        </el-form-item>
        
        <div class="login-switch">
          <template v-if="loginType === 'code'">
            已有账号？<a href="#" @click.prevent="switchLoginType('password')">使用密码登录</a>
          </template>
          <template v-else>
            还没有账号？<a href="#" @click.prevent="switchLoginType('code')">立即注册</a>
          </template>
        </div>
      </el-form>
    </div>
    
    <el-dialog
      v-model="forgotPasswordDialogVisible"
      title="重置密码"
      width="400px"
      :close-on-click-modal="false"
    >
      <el-form 
        ref="resetPasswordFormRef"
        :model="resetPasswordForm"
        :rules="resetPasswordRules"
        label-width="0"
      >
        <el-form-item prop="email">
          <el-input 
            v-model="resetPasswordForm.email" 
            placeholder="邮箱地址"
            prefix-icon="Message"
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="verificationCode">
          <div class="verification-code-container">
            <el-input 
              v-model="resetPasswordForm.verificationCode" 
              placeholder="验证码"
              prefix-icon="Key"
              size="large"
            />
            <el-button 
              type="primary" 
              class="send-code-btn"
              :disabled="isSendingResetCode || resetCountdown > 0"
              @click="sendResetCode"
              size="large"
            >
              {{ resetCountdown > 0 ? `${resetCountdown}s后重试` : '发送验证码' }}
            </el-button>
          </div>
        </el-form-item>
        
        <el-form-item prop="newPassword">
          <el-input 
            v-model="resetPasswordForm.newPassword" 
            type="password"
            placeholder="新密码"
            prefix-icon="Lock"
            size="large"
            show-password
          />
        </el-form-item>
        
        <el-form-item prop="confirmPassword">
          <el-input 
            v-model="resetPasswordForm.confirmPassword" 
            type="password"
            placeholder="确认新密码"
            prefix-icon="Lock"
            size="large"
            show-password
            @keyup.enter="handleResetPassword"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="forgotPasswordDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="resetLoading" @click="handleResetPassword">确认重置</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import store from '@/utils/store.js'

const router = useRouter()

const loginType = ref('code')
const loginFormRef = ref()
const loginForm = reactive({
  email: '',
  verificationCode: '',
  password: '',
  agreeToTerms: false
})

const loginLoading = ref(false)
const isSendingCode = ref(false)
const countdown = ref(0)
let countdownTimer = null

const forgotPasswordDialogVisible = ref(false)
const resetPasswordFormRef = ref(null)
const resetPasswordForm = reactive({
  email: '',
  verificationCode: '',
  newPassword: '',
  confirmPassword: ''
})

const resetLoading = ref(false)
const isSendingResetCode = ref(false)
const resetCountdown = ref(0)
let resetCountdownTimer = null

const loginRules = {
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  verificationCode: [
    { required: true, message: '请输入邮箱验证码', trigger: 'blur' },
    { len: 6, message: '验证码长度应为6位', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  agreeToTerms: [
    {
      validator: (rule, value, callback) => {
        if (!value) {
          callback(new Error('请阅读并同意用户协议和隐私政策'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ]
}

const resetPasswordRules = {
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  verificationCode: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 6, message: '验证码长度应为6位', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== resetPasswordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const switchLoginType = (type) => {
  loginType.value = type
  if (loginFormRef.value) {
    loginFormRef.value.clearValidate()
  }
}

const startCountdown = (countdownRef, timerRef) => {
  countdownRef.value = 60
  timerRef.value = setInterval(() => {
    countdownRef.value--
    if (countdownRef.value <= 0) {
      clearInterval(timerRef.value)
      timerRef.value = null
    }
  }, 1000)
}

const validateEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return email && emailRegex.test(email)
}

const sendVerificationCode = async () => {
  if (!validateEmail(loginForm.email)) {
    ElMessage.error('请输入正确的邮箱地址')
    return
  }
  
  try {
    isSendingCode.value = true
    
    const result = await store.sendVerificationCode(loginForm.email)
    
    if (result.success) {
      ElMessage.success('验证码已发送，请查收邮箱')
      startCountdown(countdown, countdownTimer)
    } else {
      ElMessage.error(result.message || '发送验证码失败，请稍后重试')
    }
  } catch (error) {
    ElMessage.error('发送验证码失败，请稍后重试')
  } finally {
    isSendingCode.value = false
  }
}

const sendResetCode = async () => {
  if (!validateEmail(resetPasswordForm.email)) {
    ElMessage.error('请输入正确的邮箱地址')
    return
  }
  
  try {
    isSendingResetCode.value = true
    
    const result = await store.sendPasswordResetCode(resetPasswordForm.email)
    
    if (result.success) {
      ElMessage.success('验证码已发送，请查收邮箱')
      startCountdown(resetCountdown, resetCountdownTimer)
    } else {
      ElMessage.error(result.message || '发送验证码失败，请稍后重试')
    }
  } catch (error) {
    ElMessage.error('发送验证码失败，请稍后重试')
  } finally {
    isSendingResetCode.value = false
  }
}

const showTerms = () => {
  ElMessage.info('用户协议页面开发中...')
}

const showPrivacy = () => {
  ElMessage.info('隐私政策页面开发中...')
}

const showForgotPasswordDialog = () => {
  forgotPasswordDialogVisible.value = true
}

const handleLoginOrRegister = async () => {
  try {
    const valid = await loginFormRef.value.validate()
    if (!valid) return
    
    loginLoading.value = true
    
    const result = await store.loginOrRegister({
      email: loginForm.email,
      verificationCode: loginForm.verificationCode
    })
    
    if (result.success) {
      ElMessage.success(result.message || '登录成功')
      
      if (store.state.isAdmin) {
        router.push('/admin')
      } else {
        router.push('/user')
      }
    } else {
      ElMessage.error(result.message || '操作失败，请稍后重试')
    }
  } catch (error) {
    ElMessage.error('操作失败，请稍后重试')
  } finally {
    loginLoading.value = false
  }
}

const handlePasswordLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    const valid = await loginFormRef.value.validate()
    if (!valid) return
    
    loginLoading.value = true
    
    const result = await store.passwordLogin({
      email: loginForm.email,
      password: loginForm.password
    })
    
    if (result.success) {
      ElMessage.success(result.message || '登录成功')
      
      if (store.state.isAdmin) {
        router.push('/admin')
      } else {
        router.push('/user')
      }
    } else {
      ElMessage.error(result.message || '登录失败，请检查邮箱和密码')
    }
  } catch (error) {
    ElMessage.error('登录失败，请稍后重试')
  } finally {
    loginLoading.value = false
  }
}

const handleResetPassword = async () => {
  try {
    const valid = await resetPasswordFormRef.value.validate()
    if (!valid) return
    
    resetLoading.value = true
    
    const result = await store.resetPassword({
      email: resetPasswordForm.email,
      verificationCode: resetPasswordForm.verificationCode,
      newPassword: resetPasswordForm.newPassword
    })
    
    if (result.success) {
      ElMessage.success(result.message || '密码重置成功')
      forgotPasswordDialogVisible.value = false
      
      resetPasswordForm.email = ''
      resetPasswordForm.verificationCode = ''
      resetPasswordForm.newPassword = ''
      resetPasswordForm.confirmPassword = ''
    } else {
      ElMessage.error(result.message || '密码重置失败，请稍后重试')
    }
  } catch (error) {
    ElMessage.error('密码重置失败，请稍后重试')
  } finally {
    resetLoading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  padding: 0;
}

.login-form {
  background: #ffffff;
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  width: 100%;
  max-width: 420px;
}

.logo-section {
  text-align: center;
  margin-bottom: 30px;
}

.logo-section h1 {
  color: #2c3e50;
  margin: 0 0 8px;
  font-size: 24px;
  font-weight: 600;
}

.subtitle {
  color: #909399;
  margin: 0;
  font-size: 14px;
  font-weight: 400;
}

.form-content {
  margin-top: 24px;
}

.form-content .el-form-item {
  margin-bottom: 20px;
}

.verification-code-container {
  display: flex;
  gap: 10px;
}

.verification-code-container .el-input {
  flex: 1;
}

.send-code-btn {
  min-width: 120px;
  white-space: nowrap;
}

.forgot-password {
  text-align: right;
  margin-top: -10px;
  margin-bottom: 10px;
}

.forgot-password a {
  color: #409eff;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.2s ease;
}

.forgot-password a:hover {
  color: #66b1ff;
  text-decoration: underline;
}

.login-switch {
  text-align: center;
  margin-top: 16px;
  font-size: 13px;
  color: #606266;
}

.login-switch a {
  color: #409eff;
  text-decoration: none;
  margin-left: 4px;
  transition: color 0.2s ease;
}

.login-switch a:hover {
  color: #66b1ff;
  text-decoration: underline;
}

.submit-btn {
  width: 100%;
  height: 40px;
  font-size: 15px;
  font-weight: 500;
  border-radius: 6px;
  transition: all 0.2s ease;
  margin-top: 8px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
}

:deep(.el-input__wrapper) {
  border-radius: 6px;
  transition: all 0.2s ease;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #c0c4cc inset;
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #409eff inset;
}

:deep(.el-icon) {
  color: #909399;
}

:deep(.el-dialog__body) {
  padding: 20px;
}

@media (max-width: 480px) {
  .login-form {
    margin: 16px;
    padding: 24px;
    border-radius: 8px;
  }
  
  .logo-section h1 {
    font-size: 22px;
  }
}
</style>
