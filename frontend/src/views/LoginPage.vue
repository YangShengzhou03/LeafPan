<template>
  <div class="login-container">
    <div class="login-form">
      <div class="logo-section">
        <img src="@/assets/logo.png" alt="枫叶网盘" class="logo">
        <h1>枫叶网盘</h1>
        <p class="subtitle">安全、简单的个人网盘服务</p>
      </div>
      
      <div class="form-tabs">
        <el-tabs v-model="activeTab" stretch @tab-click="handleTabClick">
          <el-tab-pane label="登录" name="login">
            <el-form 
              ref="loginFormRef" 
              :model="loginForm" 
              :rules="loginRules" 
              label-width="0"
              class="form-content"
            >
              <el-form-item prop="username">
                <el-input 
                  v-model="loginForm.username" 
                  placeholder="用户名"
                  prefix-icon="User"
                  size="large"
                />
              </el-form-item>
              
              <el-form-item prop="password">
                <el-input 
                  v-model="loginForm.password" 
                  type="password" 
                  placeholder="密码"
                  prefix-icon="Lock"
                  show-password
                  size="large"
                  @keyup.enter="handleLogin"
                />
              </el-form-item>
              
              <el-form-item>
                <el-button 
                  type="primary" 
                  class="submit-btn" 
                  :loading="loginLoading"
                  @click="handleLogin"
                  size="large"
                >
                  登录
                </el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          
          <el-tab-pane label="注册" name="register">
            <el-form 
              ref="registerFormRef" 
              :model="registerForm" 
              :rules="registerRules" 
              label-width="0"
              class="form-content"
            >
              <el-form-item prop="username">
                <el-input 
                  v-model="registerForm.username" 
                  placeholder="用户名"
                  prefix-icon="User"
                  size="large"
                />
              </el-form-item>
              
              <el-form-item prop="email">
                <el-input 
                  v-model="registerForm.email" 
                  placeholder="邮箱地址"
                  prefix-icon="Message"
                  size="large"
                />
              </el-form-item>
              
              <el-form-item prop="password">
                <el-input 
                  v-model="registerForm.password" 
                  type="password" 
                  placeholder="密码"
                  prefix-icon="Lock"
                  show-password
                  size="large"
                />
              </el-form-item>
              
              <el-form-item prop="confirmPassword">
                <el-input 
                  v-model="registerForm.confirmPassword" 
                  type="password" 
                  placeholder="确认密码"
                  prefix-icon="Lock"
                  show-password
                  size="large"
                  @keyup.enter="handleRegister"
                />
              </el-form-item>
              
              <el-form-item>
                <el-button 
                  type="success" 
                  class="submit-btn" 
                  :loading="registerLoading"
                  @click="handleRegister"
                  size="large"
                >
                  注册
                </el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'
import { authAPI } from '@/utils/api.js'
import store from '@/utils/store.js'

const router = useRouter()
const route = useRoute()

// 激活的标签页
const activeTab = ref('login')

// 登录表单
const loginFormRef = ref()
const loginForm = reactive({
  username: '',
  password: ''
})

// 注册表单
const registerFormRef = ref()
const registerForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
})

// 加载状态
const loginLoading = ref(false)
const registerLoading = ref(false)

// 组件挂载时检查路由参数
onMounted(() => {
  // 检查路由中是否有 mode 参数，且值为 register
  if (route.query.mode === 'register') {
    activeTab.value = 'register'
  }
})

// 登录表单验证规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

// 注册表单验证规则
const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 处理标签切换
const handleTabClick = (tab) => {
  // 重置表单
  if (tab.paneName === 'login') {
    registerFormRef.value?.resetFields()
  } else {
    loginFormRef.value?.resetFields()
  }
}

// 处理登录
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    const valid = await loginFormRef.value.validate()
    if (!valid) return
    
    loginLoading.value = true
    
    // 调用登录API
    const result = await store.login(loginForm)
    
    if (result.success) {
      ElMessage.success('登录成功')
      router.push('/')
    } else {
      ElMessage.error(result.message || '登录失败，请检查用户名和密码')
    }
  } catch (error) {
    ElMessage.error('登录失败，请检查用户名和密码')
  } finally {
    loginLoading.value = false
  }
}

// 处理注册
const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  try {
    const valid = await registerFormRef.value.validate()
    if (!valid) return
    
    registerLoading.value = true
    
    // 准备注册数据（移除确认密码字段）
    const { confirmPassword, ...registerData } = registerForm
    
    // 调用注册API
    const result = await store.register(registerData)
    
    if (result.success) {
      ElMessage.success('注册成功，请登录')
      activeTab.value = 'login'
      registerFormRef.value.resetFields()
    } else {
      ElMessage.error(result.message || '注册失败，请稍后重试')
    }
  } catch (error) {
    ElMessage.error('注册失败，请稍后重试')
  } finally {
    registerLoading.value = false
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
  padding: 20px;
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

.logo {
  width: 64px;
  height: 64px;
  margin-bottom: 16px;
}

.logo-section h1 {
  color: #2c3e50;
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
}

.subtitle {
  color: #909399;
  margin: 0;
  font-size: 14px;
  font-weight: 400;
}

.form-tabs {
  margin-top: 20px;
}

.form-content {
  margin-top: 24px;
}

.form-content .el-form-item {
  margin-bottom: 20px;
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

:deep(.el-tabs__item) {
  font-size: 15px;
  font-weight: 500;
  padding: 0 16px 12px;
}

:deep(.el-tabs__item.is-active) {
  color: #409eff;
}

:deep(.el-tabs__active-bar) {
  background-color: #409eff;
  height: 2px;
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

/* 响应式设计 */
@media (max-width: 480px) {
  .login-form {
    margin: 16px;
    padding: 24px;
    border-radius: 8px;
  }
  
  .logo-section h1 {
    font-size: 22px;
  }
  
  .logo {
    width: 56px;
    height: 56px;
  }
}
</style>