import { ElMessage } from 'element-plus'

// API基础配置
const API_BASE_URL = process.env.VUE_APP_API_URL || 'http://localhost:8080/api'

// 统一的请求函数
async function request(url, options = {}) {
  const config = {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  }

  // 添加token到请求头
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }

  try {
    const response = await fetch(`${API_BASE_URL}${url}`, config)
    const data = await response.json()

    if (!response.ok) {
      throw new Error(data.message || '请求失败')
    }

    return data
  } catch (error) {
    console.error('API请求错误:', error)
    ElMessage.error(error.message || '网络错误，请稍后重试')
    throw error
  }
}

// 认证相关的API
export const authAPI = {
  // 用户登录
  async login(credentials) {
    return request('/auth/login', {
      method: 'POST',
      body: JSON.stringify(credentials),
    })
  },

  // 用户注册
  async register(userData) {
    return request('/auth/register', {
      method: 'POST',
      body: JSON.stringify(userData),
    })
  },

  // 发送邮箱验证码
  async sendVerificationCode(email) {
    return request('/auth/send-verification-code', {
      method: 'POST',
      body: JSON.stringify({ email }),
    })
  },

  // 获取当前用户信息
  async getCurrentUser() {
    return request('/auth/me')
  },

  // 刷新token
  async refreshToken() {
    return request('/auth/refresh')
  },

  // 用户登出
  async logout() {
    return request('/auth/logout', {
      method: 'POST',
    })
  },
}

// 用户相关的API
export const userAPI = {
  // 更新用户信息
  async updateProfile(userData) {
    return request('/user/profile', {
      method: 'PUT',
      body: JSON.stringify(userData),
    })
  },

  // 修改密码
  async changePassword(passwordData) {
    return request('/user/password', {
      method: 'PUT',
      body: JSON.stringify(passwordData),
    })
  },
}

// 文件相关的API
export const fileAPI = {
  // 获取文件列表
  async getFiles(path = '/') {
    return request(`/files?path=${encodeURIComponent(path)}`)
  },

  // 上传文件
  async uploadFile(formData) {
    return request('/files/upload', {
      method: 'POST',
      headers: {
        // 上传文件时不需要设置Content-Type，浏览器会自动设置
      },
      body: formData,
    })
  },

  // 下载文件
  async downloadFile(fileId) {
    return request(`/files/download/${fileId}`)
  },

  // 删除文件
  async deleteFile(fileId) {
    return request(`/files/${fileId}`, {
      method: 'DELETE',
    })
  },

  // 创建文件夹
  async createFolder(folderData) {
    return request('/files/folder', {
      method: 'POST',
      body: JSON.stringify(folderData),
    })
  },
}

// 工具函数
export const utils = {
  // 保存token到localStorage
  saveToken(token) {
    localStorage.setItem('token', token)
  },

  // 从localStorage获取token
  getToken() {
    return localStorage.getItem('token')
  },

  // 移除token
  removeToken() {
    localStorage.removeItem('token')
  },

  // 检查是否已登录
  isLoggedIn() {
    return !!localStorage.getItem('token')
  },

  // 处理文件大小显示
  formatFileSize(bytes) {
    if (bytes === 0) return '0 B'
    
    const k = 1024
    const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
    const i = Math.floor(Math.log(bytes) / Math.log(k))
    
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
  },

  // 格式化日期
  formatDate(dateString) {
    const date = new Date(dateString)
    return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN', { 
      hour: '2-digit', 
      minute: '2-digit' 
    })
  },
}

export default request