import { reactive } from 'vue'
import { authAPI, utils } from './api.js'

// 全局状态
const state = reactive({
  user: null,
  isAuthenticated: utils.isLoggedIn(),
  loading: false,
})

// 状态管理方法
const store = {
  state,

  // 设置用户信息
  setUser(user) {
    state.user = user
    state.isAuthenticated = true
  },

  // 清除用户信息
  clearUser() {
    state.user = null
    state.isAuthenticated = false
    utils.removeToken()
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
    }
  }
}

// 初始化应用状态
store.init()

export default store