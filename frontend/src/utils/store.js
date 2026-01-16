import { reactive } from 'vue'
import Server from './Server.js'
import * as utils from './utils.js'

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

const store = {
  state,

  setUser(user) {
    if (user) {
      state.user = user
      state.isAuthenticated = true
      state.isAdmin = user.role === 1 || user.role === 'ADMIN' || user.role === 'admin'
      if (user.storageInfo) {
        state.storageInfo = user.storageInfo
      }
    }
  },

  clearUser() {
    state.user = null
    state.isAuthenticated = false
    state.isAdmin = false
    utils.removeToken()
    state.storageInfo = {
      totalStorageGB: 0,
      usedStorageGB: 0
    }
  },

  updateStorageInfo(storageInfo) {
    if (storageInfo) {
      state.storageInfo = storageInfo
    }
  },

  async login(credentials) {
    state.loading = true
    try {
      const response = await Server.post('/auth/login', credentials)

      if (response && response.code === 200 && response.data) {
        const { token, user } = response.data

        if (token) {
          utils.saveToken(token)
        }

        if (user) {
          this.setUser(user)
        } else {
          await this.fetchCurrentUser()
        }

        return { success: true, message: response.message || '登录成功', user }
      }

      return { success: false, message: response?.message || '登录失败' }
    } catch (error) {
      this.clearUser()
      return { success: false, message: error.response?.data?.message || '登录失败，请检查网络连接' }
    } finally {
      state.loading = false
    }
  },

  async register(userData) {
    state.loading = true
    try {
      const response = await Server.post('/auth/register', userData)

      if (response && response.code === 200 && response.data) {
        const { token, user } = response.data

        if (token) {
          utils.saveToken(token)

          if (user) {
            this.setUser(user)
          } else {
            await this.fetchCurrentUser()
          }

          return { success: true, message: response.message || '注册成功', user, token }
        } else {
          return { success: true, message: response.message || '注册成功，请登录' }
        }
      }

      return { success: false, message: response?.message || '注册失败' }
    } catch (error) {
      return { success: false, message: error.response?.data?.message || error.message }
    } finally {
      state.loading = false
    }
  },

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

  async fetchCurrentUser() {
    if (!utils.isLoggedIn()) {
      this.clearUser()
      return
    }

    try {
      const response = await Server.get('/auth/me')

      if (response && response.code === 200 && response.data) {
        this.setUser(response.data)

        if (response.data.storageInfo) {
          this.updateStorageInfo(response.data.storageInfo)
        }
      }
    } catch (error) {
      if (error.response && (error.response.status === 401 || error.response.status === 403)) {
        this.clearUser()
      }
    }
  },

  async fetchStorageInfo() {
    if (!utils.isLoggedIn()) {
      return null
    }

    try {
      const response = await Server.get('/user/storage')
      const storageData = response.data || response

      if (storageData) {
        this.updateStorageInfo({
          totalStorageGB: storageData.quotaGB || storageData.totalStorageGB || 0,
          usedStorageGB: storageData.usedGB || storageData.usedStorageGB || 0
        })
      }

      return storageData
    } catch (error) {
      return null
    }
  },

  async logout() {
    try {
      await Server.post('/auth/logout')
    } catch (error) {
    } finally {
      this.clearUser()
    }
  },

  async init() {
    if (utils.isLoggedIn()) {
      await this.fetchCurrentUser()
      await this.fetchStorageInfo()
    }
  },

  async loginOrRegister(credentials) {
    state.loading = true
    try {
      const response = await Server.post('/auth/login-or-register', credentials)

      if (response && response.code === 200 && response.data) {
        const { token, user } = response.data

        if (token) {
          utils.saveToken(token)
        }

        if (user) {
          this.setUser(user)
        } else {
          await this.fetchCurrentUser()
        }

        return { success: true, message: response.message || '登录成功', user }
      }

      return { success: false, message: response?.message || '操作失败' }
    } catch (error) {
      this.clearUser()
      return { success: false, message: error.response?.data?.message || '操作失败，请检查网络连接' }
    } finally {
      state.loading = false
    }
  },

  async passwordLogin(credentials) {
    state.loading = true
    try {
      const response = await Server.post('/auth/login', credentials)

      if (response && response.code === 200 && response.data) {
        const { token, user } = response.data

        if (token) {
          utils.saveToken(token)
        }

        if (user) {
          this.setUser(user)
        } else {
          await this.fetchCurrentUser()
        }

        return { success: true, message: response.message || '登录成功', user }
      }

      return { success: false, message: response?.message || '登录失败' }
    } catch (error) {
      this.clearUser()
      return { success: false, message: error.response?.data?.message || '登录失败，请检查网络连接' }
    } finally {
      state.loading = false
    }
  },

  async sendPasswordResetCode(email) {
    state.loading = true
    try {
      await Server.post('/verification/reset-password', { email })
      return { success: true, message: '验证码发送成功' }
    } catch (error) {
      return { success: false, message: error.response?.data?.message || error.message }
    } finally {
      state.loading = false
    }
  },

  async resetPassword(resetData) {
    state.loading = true
    try {
      const response = await Server.post('/auth/reset-password', resetData)
      return { success: true, message: response.message || '密码重置成功' }
    } catch (error) {
      return { success: false, message: error.response?.data?.message || error.message }
    } finally {
      state.loading = false
    }
  }
}

export default store
