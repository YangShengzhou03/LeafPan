import { reactive } from 'vue'
import Server from './Server.js'
import * as utils from './utils.js'

// 全局状态
const state = reactive({
  user: null,
  isAuthenticated: utils.isLoggedIn(),
  loading: false,
  isAdmin: false,
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
    if (user) {
      state.user = user
      state.isAuthenticated = true
      // 检查用户角色，设置管理员标志
      state.isAdmin = user.role === 1 || user.role === 'ADMIN' || user.role === 'admin'
      // 更新存储信息
      if (user.storageInfo) {
        state.storageInfo = user.storageInfo
      }
    }
  },

  // 清除用户信息
  clearUser() {
    state.user = null
    state.isAuthenticated = false
    state.isAdmin = false
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
      const response = await Server.post('/auth/login', credentials)
      
      // 后端返回的数据结构是 {code: 200, message: "登录成功", data: {token: "...", user: {...}}}
      if (response.data && response.data.code === 200 && response.data.data) {
        const { token, user } = response.data.data
        
        if (token) {
          utils.saveToken(token)
        }
        
        // 设置用户信息
        if (user) {
          this.setUser(user)
        } else {
          // 如果没有返回用户信息，需要额外获取
          await this.fetchCurrentUser()
        }
        
        return { success: true, message: response.data.message || '登录成功', user }
      }
      
      return { success: false, message: response.data?.message || '登录失败' }
    } catch (error) {
      this.clearUser()
      console.error('登录失败:', error)
      return { success: false, message: error.response?.data?.message || '登录失败，请检查网络连接' }
    } finally {
      state.loading = false
    }
  },

  // 注册
  async register(userData) {
    state.loading = true
    try {
      await Server.post('/auth/register', userData)
      return { success: true, message: '注册成功' }
    } catch (error) {
      return { success: false, message: error.response?.data?.message || error.message }
    } finally {
      state.loading = false
    }
  },

  // 发送邮箱验证码
  async sendVerificationCode(email) {
    state.loading = true
    try {
      await Server.post('/verification/send', { email })
      return { success: true, message: '验证码发送成功' }
    } catch (error) {
      return { success: false, message: error.response?.data?.message || error.message }
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
      const response = await Server.get('/auth/me')
      this.setUser(response.data)
    } catch (error) {
      console.error('获取用户信息失败:', error)
      // 只有在token无效或过期时才清除token
      if (error.response && (error.response.status === 401 || error.response.status === 403)) {
        this.clearUser()
      }
    }
  },

  // 获取存储信息
  async fetchStorageInfo() {
    if (!utils.isLoggedIn()) {
      return null
    }

    try {
      const response = await Server.get('/user/storage')
      state.storageInfo = response.data
      return response.data
    } catch (error) {
      console.error('获取存储信息失败:', error)
      return null
    }
  },

  // 更新用户信息
  async updateProfile(userData) {
    state.loading = true
    try {
      const response = await Server.put('/user/profile', userData)
      this.setUser(response.data)
      return { success: true, message: '更新成功' }
    } catch (error) {
      return { success: false, message: error.response?.data?.message || error.message }
    } finally {
      state.loading = false
    }
  },

  // 更新密码
  async updatePassword(passwordData) {
    state.loading = true
    try {
      await Server.put('/user/password', passwordData)
      return { success: true, message: '密码更新成功' }
    } catch (error) {
      return { success: false, message: error.response?.data?.message || error.message }
    } finally {
      state.loading = false
    }
  },

  // 登出
  async logout() {
    try {
      // 调用后端登出API
      await Server.post('/auth/logout')
    } catch (error) {
      console.error('登出API调用失败:', error)
    } finally {
      // 无论API调用是否成功，都清除本地状态
      this.clearUser()
    }
  },

  // 初始化应用时检查登录状态
  async init() {
    if (utils.isLoggedIn()) {
      try {
        await this.fetchCurrentUser()
        await this.fetchStorageInfo()
        return true
      } catch (error) {
        console.error('初始化用户状态失败:', error)
        // 只有在token无效或过期时才清除token
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
          this.clearUser()
        }
        return false
      }
    }
    return false
  }
}

export default store