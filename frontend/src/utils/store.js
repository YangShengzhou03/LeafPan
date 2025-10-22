import { reactive } from 'vue'
import { authAPI, userAPI } from './api.js'
import * as utils from './utils.js'

// 全局状态
const state = reactive({
  user: null,
  isAuthenticated: utils.isLoggedIn(),
  loading: false,
  storageInfo: {
    totalStorageGB: 0,
    usedStorageGB: 0
  }
})

// 状态管理方法
const store = {
  state,

  // 设置用户信息
  setUser(user) {
    state.user = user
    state.isAuthenticated = true
    // 更新存储信息
    if (user.storageInfo) {
      state.storageInfo = user.storageInfo
    }
  },

  // 清除用户信息
  clearUser() {
    state.user = null
    state.isAuthenticated = false
    utils.removeToken()
    // 重置存储信息为默认值
    state.storageInfo = {
      totalStorageGB: 0,
      usedStorageGB: 0
    }
  },

  // 更新存储信息
  updateStorageInfo(storageInfo) {
    if (storageInfo) {
      state.storageInfo = storageInfo
    }
  },

  // 登录
  async login(credentials) {
    state.loading = true
    try {
      const response = await authAPI.login(credentials)
      
      if (response.token) {
        utils.saveToken(response.token)
        await this.fetchCurrentUser()
        return { success: true }
      }
      
      return { success: false, message: '登录失败' }
    } catch (error) {
      this.clearUser()
      return { success: false, message: error.message }
    } finally {
      state.loading = false
    }
  },

  // 注册
  async register(userData) {
    state.loading = true
    try {
      const response = await authAPI.register(userData)
      return { success: true, message: '注册成功' }
    } catch (error) {
      return { success: false, message: error.message }
    } finally {
      state.loading = false
    }
  },

  // 发送邮箱验证码
  async sendVerificationCode(email) {
    state.loading = true
    try {
      const response = await authAPI.sendVerificationCode(email)
      return { success: true, message: '验证码发送成功' }
    } catch (error) {
      return { success: false, message: error.message }
    } finally {
      state.loading = false
    }
  },

  // 获取当前用户信息
  async fetchCurrentUser() {
    if (!utils.isLoggedIn()) {
      this.clearUser()
      return
    }

    try {
      const user = await authAPI.getCurrentUser()
      this.setUser(user)
    } catch (error) {
      this.clearUser()
      console.error('获取用户信息失败:', error)
    }
  },

  // 获取存储信息
  async fetchStorageInfo() {
    if (!utils.isLoggedIn()) {
      return
    }

    try {
      const storageInfo = await userAPI.getStorageInfo()
      this.updateStorageInfo(storageInfo)
    } catch (error) {
      console.error('获取存储信息失败:', error)
    }
  },

  // 登出
  async logout() {
    try {
      await authAPI.logout()
    } catch (error) {
      console.error('登出失败:', error)
    } finally {
      this.clearUser()
    }
  },

  // 初始化应用时检查登录状态
  async init() {
    if (utils.isLoggedIn()) {
      await this.fetchCurrentUser()
      await this.fetchStorageInfo()
    }
  }
}

// 初始化应用状态
store.init()

export default store