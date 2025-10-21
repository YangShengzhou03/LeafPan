<template>
  <div class="settings-page">
    <div class="settings-container">
      <!-- 左侧导航 -->
      <div class="settings-nav">
        <div v-for="tab in settingsTabs" :key="tab.id" class="nav-item" :class="{ active: activeTab === tab.id }"
          @click="activeTab = tab.id">
          <i :class="tab.icon"></i>
          <span>{{ tab.label }}</span>
        </div>
      </div>

      <!-- 右侧内容 -->
      <div class="settings-content">
        <!-- 账户设置 -->
        <div v-if="activeTab === 'account'" class="settings-section">
          <h2>账户设置</h2>
          <el-form :model="accountForm" label-width="120px" :rules="accountRules" ref="accountFormRef">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="accountForm.username" />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="accountForm.email" />
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="accountForm.phone" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveAccountInfo" :loading="savingAccount">保存</el-button>
            </el-form-item>
          </el-form>
        </div>

        <!-- 安全设置 -->
        <div v-if="activeTab === 'security'" class="settings-section">
          <h2>安全设置</h2>

          <!-- 修改密码 -->
          <div class="security-item">
            <h3>修改密码</h3>
            <el-form :model="passwordForm" label-width="120px" :rules="passwordRules" ref="passwordFormRef">
              <el-form-item label="当前密码" prop="currentPassword">
                <el-input v-model="passwordForm.currentPassword" type="password" show-password />
              </el-form-item>
              <el-form-item label="新密码" prop="newPassword">
                <el-input v-model="passwordForm.newPassword" type="password" show-password />
              </el-form-item>
              <el-form-item label="确认新密码" prop="confirmPassword">
                <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="changePassword" :loading="changingPassword">修改密码</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 两步验证 -->
          <div class="security-item">
            <h3>两步验证</h3>
            <div class="security-status">
              <el-switch v-model="twoFactorEnabled" active-text="已启用" inactive-text="未启用" @change="toggleTwoFactor" />
              <p class="security-description">
                启用两步验证后，登录时需要输入手机验证码，提高账户安全性。
              </p>
            </div>
          </div>

          <!-- 登录设备 -->
          <div class="security-item">
            <h3>登录设备</h3>
            <div class="device-list">
              <div class="device-item" v-for="device in loginDevices" :key="device.id">
                <div class="device-info">
                  <div class="device-name">{{ device.name }}</div>
                  <div class="device-meta">
                    <span>{{ device.location }}</span>
                    <span>{{ formatDate(device.lastLogin) }}</span>
                  </div>
                </div>
                <el-button type="text" @click="removeDevice(device)" :disabled="device.current">
                  {{ device.current ? '当前设备' : '移除' }}
                </el-button>
              </div>
            </div>
          </div>
        </div>






      </div>
    </div>

    <!-- 升级存储对话框 -->
    <el-dialog v-model="upgradeDialog.visible" title="升级存储" width="600px" :close-on-click-modal="false">
      <div class="upgrade-plans">
        <div class="plan-item" v-for="plan in storagePlans" :key="plan.id" :class="{ active: selectedPlan === plan.id }"
          @click="selectedPlan = plan.id">
          <div class="plan-header">
            <h3>{{ plan.name }}</h3>
            <div class="plan-price">{{ plan.price }}<span>/月</span></div>
          </div>
          <div class="plan-storage">{{ plan.storage }}GB</div>
          <div class="plan-features">
            <div class="feature-item" v-for="(feature, index) in plan.features" :key="index">
              <i class="el-icon-check"></i>
              <span>{{ feature }}</span>
            </div>
          </div>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="upgradeDialog.visible = false">取消</el-button>
          <el-button type="primary" @click="upgradeStorage" :loading="upgradingStorage">
            确认升级
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import store from '@/utils/store.js'
import mockApiService from '@/utils/mockApiService.js'
import { utils } from '@/utils/api.js'

const { formatFileSize, formatDate } = utils

// 当前标签页
const activeTab = ref('account')

// 设置标签页
const settingsTabs = [
  { id: 'account', label: '账户', icon: 'el-icon-user' },
  { id: 'security', label: '安全', icon: 'el-icon-lock' }
]

// 账户设置
const accountFormRef = ref(null)
const savingAccount = ref(false)
const accountForm = ref({
  username: '',
  email: '',
  phone: ''
})

const accountRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

// 安全设置
const passwordFormRef = ref(null)
const changingPassword = ref(false)
const passwordForm = ref({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const passwordRules = {
  currentPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.value.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const twoFactorEnabled = ref(false)
const loginDevices = ref([
  { id: 1, name: 'Chrome on Windows', location: '北京', lastLogin: new Date(), current: true },
  { id: 2, name: 'Safari on iPhone', location: '上海', lastLogin: new Date(Date.now() - 86400000), current: false }
])







// 保存账户信息
const saveAccountInfo = async () => {
  if (!accountFormRef.value) return

  try {
    await accountFormRef.value.validate()
    savingAccount.value = true

    await mockApiService.updateUserInfo(accountForm.value)
    ElMessage.success('账户信息已更新')

    // 更新store中的用户信息
    await store.getUserInfo()
  } catch (error) {
    if (error !== false) { // 不是表单验证错误
      ElMessage.error('更新账户信息失败')
      console.error('更新账户信息失败:', error)
    }
  } finally {
    savingAccount.value = false
  }
}

// 修改密码
const changePassword = async () => {
  if (!passwordFormRef.value) return

  try {
    await passwordFormRef.value.validate()
    changingPassword.value = true

    await mockApiService.changePassword({
      currentPassword: passwordForm.value.currentPassword,
      newPassword: passwordForm.value.newPassword
    })

    ElMessage.success('密码已修改')
    passwordForm.value = {
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    }
  } catch (error) {
    if (error !== false) { // 不是表单验证错误
      ElMessage.error('修改密码失败')
      console.error('修改密码失败:', error)
    }
  } finally {
    changingPassword.value = false
  }
}

// 切换两步验证
const toggleTwoFactor = async (enabled) => {
  try {
    await mockApiService.toggleTwoFactor(enabled)
    ElMessage.success(enabled ? '两步验证已启用' : '两步验证已关闭')
  } catch (error) {
    ElMessage.error('操作失败')
    console.error('切换两步验证失败:', error)
    // 恢复开关状态
    twoFactorEnabled.value = !enabled
  }
}

// 移除设备
const removeDevice = async (device) => {
  try {
    await mockApiService.removeLoginDevice(device.id)
    ElMessage.success('设备已移除')
    // 重新获取设备列表
    await fetchLoginDevices()
  } catch (error) {
    ElMessage.error('移除设备失败')
    console.error('移除设备失败:', error)
  }
}





// 获取账户信息
const fetchAccountInfo = async () => {
  try {
    const user = store.state.user
    if (user) {
      accountForm.value = {
        username: user.username,
        email: user.email,
        phone: user.phone || ''
      }
    }
  } catch (error) {
    console.error('获取账户信息失败:', error)
  }
}



// 获取登录设备
const fetchLoginDevices = async () => {
  try {
    loginDevices.value = await mockApiService.getLoginDevices()
  } catch (error) {
    console.error('获取登录设备失败:', error)
  }
}



// 获取外观设置
const fetchAppearanceSettings = async () => {
  try {
    const settings = await mockApiService.getAppearanceSettings()
    themeMode.value = settings.themeMode || 'light'
    primaryColor.value = settings.primaryColor || '#409EFF'
    fontSize.value = settings.fontSize || 14
  } catch (error) {
    console.error('获取外观设置失败:', error)
  }
}

// 监听外观设置变化
watch([themeMode, primaryColor, fontSize], async () => {
  try {
    await mockApiService.updateAppearanceSettings({
      themeMode: themeMode.value,
      primaryColor: primaryColor.value,
      fontSize: fontSize.value
    })

    // 应用主题设置
    document.documentElement.style.setProperty('--el-color-primary', primaryColor.value)
    document.documentElement.style.setProperty('--font-size', fontSize.value + 'px')

    // 应用主题模式
    if (themeMode.value === 'dark') {
      document.documentElement.classList.add('dark')
    } else if (themeMode.value === 'light') {
      document.documentElement.classList.remove('dark')
    } else {
      // 跟随系统
      const isDarkMode = window.matchMedia('(prefers-color-scheme: dark)').matches
      if (isDarkMode) {
        document.documentElement.classList.add('dark')
      } else {
        document.documentElement.classList.remove('dark')
      }
    }
  } catch (error) {
    console.error('更新外观设置失败:', error)
  }
}, { deep: true })

// 页面加载时获取数据
onMounted(async () => {
  await Promise.all([
    fetchAccountInfo(),
    fetchStorageInfo(),
    fetchLoginDevices(),
    fetchNotificationSettings(),
    fetchAppearanceSettings()
  ])
})
</script>

<style scoped>
.settings-page {
  padding: 24px;
  background-color: #f8fafc;
  min-height: 100vh;
  font-family: 'Inter', 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
}

.settings-header {
  margin-bottom: 30px;
}

.settings-header h1 {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 10px 0;
}

.settings-header p {
  color: #606266;
  margin: 0;
  font-size: 16px;
}

.settings-container {
  display: flex;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

/* 左侧导航 */
.settings-nav {
  width: 200px;
  background-color: #f5f7fa;
  border-right: 1px solid #e9ecef;
  padding: 20px 0;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #606266;
}

.nav-item:hover {
  background-color: rgba(64, 158, 255, 0.1);
  color: #409EFF;
}

.nav-item.active {
  background-color: rgba(64, 158, 255, 0.1);
  color: #409EFF;
  font-weight: 500;
  border-left: 3px solid #409EFF;
}

.nav-item i {
  margin-right: 10px;
  font-size: 18px;
}

/* 右侧内容 */
.settings-content {
  flex: 1;
  padding: 30px;
}

.settings-section {
  max-width: 800px;
}

.settings-section h2 {
  font-size: 22px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 20px 0;
  padding-bottom: 10px;
  border-bottom: 1px solid #e9ecef;
}

/* 安全设置 */
.security-item {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.security-item:last-child {
  border-bottom: none;
}

.security-item h3 {
  font-size: 18px;
  font-weight: 500;
  color: #303133;
  margin: 0 0 15px 0;
}

.security-status {
  display: flex;
  align-items: center;
  gap: 15px;
}

.security-description {
  color: #606266;
  margin: 0;
  font-size: 14px;
}

.device-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.device-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: 6px;
}

.device-name {
  font-weight: 500;
  color: #303133;
  margin-bottom: 5px;
}

.device-meta {
  display: flex;
  gap: 10px;
  font-size: 12px;
  color: #909399;
}

/* 存储设置 */
.storage-item {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.storage-item:last-child {
  border-bottom: none;
}

.storage-item h3 {
  font-size: 18px;
  font-weight: 500;
  color: #303133;
  margin: 0 0 15px 0;
}

.storage-info {
  margin-bottom: 20px;
}

.storage-chart {
  display: flex;
  align-items: center;
  gap: 30px;
}

.circular-progress {
  width: 120px;
  height: 120px;
}

.storage-details {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.storage-details .storage-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0;
  border-bottom: none;
  margin-bottom: 0;
}

.storage-details .storage-item .label {
  color: #606266;
  font-size: 14px;
}

.storage-details .storage-item .value {
  color: #303133;
  font-weight: 600;
  font-size: 14px;
}

.storage-actions {
  display: flex;
  gap: 10px;
}

.storage-breakdown {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.breakdown-item {
  display: flex;
  align-items: center;
}

.breakdown-color {
  width: 12px;
  height: 12px;
  border-radius: 2px;
  margin-right: 10px;
}

.breakdown-info {
  display: flex;
  justify-content: space-between;
  flex: 1;
}

.breakdown-label {
  color: #606266;
  font-size: 14px;
}

.breakdown-value {
  color: #303133;
  font-weight: 500;
  font-size: 14px;
}

/* 通知设置 */
.notification-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.notification-item:last-child {
  border-bottom: none;
}

.notification-title {
  font-weight: 500;
  color: #303133;
  margin-bottom: 5px;
}

.notification-description {
  color: #606266;
  margin: 0;
  font-size: 14px;
}

/* 外观设置 */
.appearance-item {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.appearance-item:last-child {
  border-bottom: none;
}

.appearance-item h3 {
  font-size: 18px;
  font-weight: 500;
  color: #303133;
  margin: 0 0 15px 0;
}

.theme-options {
  display: flex;
  gap: 20px;
}

.theme-option {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  padding: 10px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.theme-option:hover {
  background-color: #f5f7fa;
}

.theme-option.active {
  background-color: rgba(64, 158, 255, 0.1);
  border: 1px solid #409EFF;
}

.theme-preview {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  margin-bottom: 10px;
  border: 1px solid #e9ecef;
}

.light-theme {
  background-color: #fff;
}

.dark-theme {
  background-color: #303133;
}

.auto-theme {
  background: linear-gradient(90deg, #fff 50%, #303133 50%);
}

.color-options {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

.color-option {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 6px;
  transition: all 0.3s ease;
}

.color-option:hover {
  background-color: #f5f7fa;
}

.color-option.active {
  background-color: rgba(64, 158, 255, 0.1);
  border: 1px solid #409EFF;
}

.color-circle {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  margin-right: 8px;
}

/* 升级存储对话框 */
.upgrade-plans {
  display: flex;
  gap: 20px;
}

.plan-item {
  flex: 1;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.plan-item:hover {
  border-color: #409EFF;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.1);
}

.plan-item.active {
  border-color: #409EFF;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
}

.plan-header {
  text-align: center;
  margin-bottom: 15px;
}

.plan-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 10px 0;
}

.plan-price {
  font-size: 24px;
  font-weight: 600;
  color: #409EFF;
}

.plan-price span {
  font-size: 14px;
  color: #909399;
}

.plan-storage {
  text-align: center;
  font-size: 16px;
  color: #606266;
  margin-bottom: 15px;
}

.plan-features {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.feature-item {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #606266;
}

.feature-item i {
  color: #67C23A;
  margin-right: 8px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .settings-header h1 {
    font-size: 24px;
  }

  .settings-container {
    flex-direction: column;
  }

  .settings-nav {
    width: 100%;
    padding: 10px 0;
    display: flex;
    overflow-x: auto;
  }

  .nav-item {
    flex-shrink: 0;
    padding: 10px 15px;
    border-left: none;
    border-bottom: 3px solid transparent;
  }

  .nav-item.active {
    border-left: none;
    border-bottom: 3px solid #409EFF;
  }

  .settings-content {
    padding: 20px;
  }

  .storage-chart {
    flex-direction: column;
    align-items: center;
  }

  .upgrade-plans {
    flex-direction: column;
  }

  .theme-options {
    flex-wrap: wrap;
  }
}
</style>