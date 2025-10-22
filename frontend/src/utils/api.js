import axios from 'axios'
import { getToken } from './utils.js'

const server = axios.create({
  baseURL: process.env.VUE_APP_API_URL || '/api',
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json'
  }
})

server.interceptors.request.use(
  config => {
    const token = getToken()
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

server.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    if (error.response && error.response.status === 401) {
      // 未授权，清除token并跳转到登录页
      localStorage.removeItem('token')
      window.location.href = '/login'
    }
    return Promise.reject(error.response ? error.response.data : { message: '网络错误' })
  }
)

// 认证相关API
export const authAPI = {
  // 登录
  login: (credentials) => {
    return server.post('/api/auth/login', credentials)
  },
  
  // 注册
  register: (userData) => {
    return server.post('/api/auth/register', userData)
  },
  
  // 获取当前用户信息
  getCurrentUser: () => {
    return server.get('/api/auth/me')
  },
  
  // 修改密码
  changePassword: (passwordData) => {
    return server.put('/api/auth/password', passwordData)
  },
  
  // 刷新令牌
  refreshToken: () => {
    return server.post('/api/auth/refresh')
  },
  
  // 登出
  logout: () => {
    return server.post('/api/auth/logout')
  },
  
  // 忘记密码
  forgotPassword: (email) => {
    return server.post('/api/auth/forgot-password', { email })
  },
  
  // 重置密码
  resetPassword: (token, password) => {
    return server.post('/api/auth/reset-password', { token, password })
  },
  
  // 验证邮箱
  verifyEmail: (token) => {
    return server.get(`/api/auth/verify-email?token=${token}`)
  },
  
  // 重新发送验证邮件
  resendVerificationEmail: () => {
    return server.post('/api/auth/resend-verification')
  },
  
  // 发送邮箱验证码
  sendVerificationCode: (email) => {
    return server.post('/api/verification/send', { email })
  }
}

// 文件相关API
export const fileAPI = {
  // 上传文件
  upload: (formData, onUploadProgress) => {
    return server.post('/api/user/file/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      onUploadProgress
    })
  },
  
  // 获取文件列表
  getFiles: (params) => {
    return server.get('/api/user/file/list', { params })
  },
  
  // 获取文件详情
  getFile: (id) => {
    return server.get(`/api/user/file/${id}`)
  },
  
  // 下载文件
  download: (id) => {
    return server.get(`/api/user/file/${id}/download`, {
      responseType: 'blob'
    })
  },
  
  // 预览文件
  preview: (id) => {
    return server.get(`/api/user/file/${id}/preview`)
  },
  
  // 重命名文件
  rename: (id, newName) => {
    return server.put(`/api/user/file/${id}/rename`, { name: newName })
  },
  
  // 删除文件
  delete: (id) => {
    return server.delete(`/api/user/file/${id}`)
  },
  
  // 搜索文件
  search: (name) => {
    return server.get('/api/user/file/search', { params: { name } })
  },
  
  // 获取存储使用情况
  getStorageUsage: () => {
    return server.get('/api/user/storage/usage')
  }
}

// 文件夹相关API
export const folderAPI = {
  // 创建文件夹
  create: (folderData) => {
    return server.post('/api/folder/create', folderData)
  },
  
  // 获取用户文件夹列表
  getFolders: () => {
    return server.get('/api/folder/list')
  },
  
  // 获取子文件夹
  getSubFolders: (parentId) => {
    return server.get(`/api/folder/${parentId}/subfolders`)
  },
  
  // 获取文件夹详情
  getFolder: (id) => {
    return server.get(`/api/folder/${id}`)
  },
  
  // 重命名文件夹
 rename: (id, newName) => {
    return server.put(`/api/folder/${id}/rename`, { name: newName })
  },
  
  // 删除文件夹
  delete: (id) => {
    return server.delete(`/api/folder/${id}`)
  }
}

// 分享相关API
export const shareAPI = {
  // 创建分享
  create: (shareData) => {
    return server.post('/api/share/create', shareData)
  },
  
  // 获取用户分享列表
  getUserShares: () => {
    return server.get('/api/share/list')
  },
  
  // 获取分享详情
  getShare: (shareCode) => {
    return server.get(`/api/share/${shareCode}`)
  },
  
  // 更新分享
  update: (id, shareData) => {
    return server.put(`/api/share/${id}`, shareData)
  },
  
  // 删除分享
  delete: (id) => {
    return server.delete(`/api/share/${id}`)
  },
  
  // 通过分享码访问文件
  accessSharedFile: (shareCode) => {
    return server.get(`/api/share/${shareCode}/file`)
  },
  
  // 公开访问分享
  publicAccess: (shareCode) => {
    return server.get(`/api/share/public/${shareCode}`)
  }
}

// 用户相关API
export const userAPI = {
  // 获取用户信息
  getUserInfo: () => {
    return server.get('/api/user/info')
  },
  
  // 更新用户信息
  updateUserInfo: (userData) => {
    return server.put('/api/user/info', userData)
  },
  
  // 更新个人资料
  updateProfile: (profileData) => {
    return server.put('/api/user/profile', profileData)
  },
  
  // 获取当前用户信息
  getCurrentUser: () => {
    return server.get('/api/user/me')
  },
  
  // 获取操作日志
  getOperationLogs: (params) => {
    return server.get('/api/user/logs', { params })
  },
  
  // 获取存储信息
  getStorageInfo: () => {
    return server.get('/api/user/storage')
  },
  
  // 获取仪表板统计数据
  getDashboardStats: () => {
    return server.get('/api/user/dashboard/stats')
  }
}

// 回收站相关API
export const trashAPI = {
  // 获取回收站文件
  getTrashFiles: () => {
    return server.get('/api/user/trash')
  },
  
  // 恢复文件
  restoreFile: (id) => {
    return server.put(`/api/user/trash/${id}/restore`)
  },
  
  // 永久删除文件
  deleteFile: (id) => {
    return server.delete(`/api/user/trash/${id}`)
  },
  
  // 清空回收站
  clearTrash: () => {
    return server.delete('/api/user/trash')
  }
}

// 配置相关API
export const configAPI = {
  // 获取系统配置
  getSystemConfig: () => {
    return server.get('/api/config/system')
  },
  
  // 更新系统配置
  updateSystemConfig: (settings) => {
    return server.put('/api/config/system', settings)
  },
  
  // 上传Logo
  uploadLogo: (formData) => {
    return server.post('/api/config/upload/logo', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },
  
  // 获取邮件配置
  getEmailConfig: () => {
    return server.get('/api/config/email')
  },
  
  // 更新邮件配置
  updateEmailConfig: (settings) => {
    return server.put('/api/config/email', settings)
  },
  
  // 测试邮件配置
  testEmailConfig: () => {
    return server.post('/api/config/email/test')
  }
}

// 管理员相关API
export const adminAPI = {
  // 获取用户列表
  getUserList: (params) => {
    return server.get('/admin/user/list', { params })
  },
  
  // 获取用户详情
  getUser: (id) => {
    return server.get(`/admin/user/${id}`)
  },
  
  // 添加用户
  addUser: (userData) => {
    return server.post('/admin/user', userData)
  },
  
  // 更新用户
  updateUser: (id, userData) => {
    return server.put(`/admin/user/${id}`, userData)
  },
  
  // 删除用户
  deleteUser: (id) => {
    return server.delete(`/admin/user/${id}`)
  },
  
  // 更新用户状态
  updateUserStatus: (id, enabled) => {
    return server.put(`/admin/user/${id}/status`, null, { 
      params: { enabled } 
    })
  },
  
  // 获取系统统计信息
  getSystemStats: () => {
    return server.get('/admin/system/stats')
  },
  
  // 获取操作日志
  getOperationLogs: (params) => {
    return server.get('/admin/logs', { params })
  },
  
  // 获取文件统计信息
  getFileStats: () => {
    return server.get('/admin/file/statistics')
  }
}

export default server