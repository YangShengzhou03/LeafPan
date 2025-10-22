import axios from 'axios'
import { getToken } from './utils.js'

const server = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000,
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
  
  // 登出
  logout: () => {
    return server.post('/api/auth/logout')
  },
  
  // 获取存储信息
  getStorageInfo: () => {
    return server.get('/api/user/storage')
  },
  
  // 发送邮箱验证码
  sendVerificationCode: (email) => {
    return server.post('/api/auth/send-verification-code', { email })
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
  search: (query) => {
    return server.get('/api/user/file/search', { params: { query } })
  }
}

// 文件夹相关API
export const folderAPI = {
  // 创建文件夹
  create: (folderData) => {
    return server.post('/api/user/folder', folderData)
  },
  
  // 获取文件夹列表
  getFolders: (params) => {
    return server.get('/api/user/folder/list', { params })
  },
  
  // 获取子文件夹
  getSubFolders: (parentId) => {
    return server.get(`/api/user/folder/${parentId}/subfolders`)
  },
  
  // 获取文件夹详情
  getFolder: (id) => {
    return server.get(`/api/user/folder/${id}`)
  },
  
  // 重命名文件夹
  rename: (id, newName) => {
    return server.put(`/api/user/folder/${id}/rename`, { name: newName })
  },
  
  // 删除文件夹
  delete: (id) => {
    return server.delete(`/api/user/folder/${id}`)
  }
}

// 分享相关API
export const shareAPI = {
  // 创建分享
  create: (shareData) => {
    return server.post('/api/user/share', shareData)
  },
  
  // 获取用户分享列表
  getUserShares: (params) => {
    return server.get('/api/user/share/list', { params })
  },
  
  // 获取分享详情
  getShare: (shareCode) => {
    return server.get(`/api/share/${shareCode}`)
  },
  
  // 更新分享
  update: (id, shareData) => {
    return server.put(`/api/user/share/${id}`, shareData)
  },
  
  // 删除分享
  delete: (id) => {
    return server.delete(`/api/user/share/${id}`)
  },
  
  // 通过分享码访问文件
  accessSharedFile: (shareCode) => {
    return server.get(`/api/share/${shareCode}/file`)
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
  
  // 获取操作日志
  getOperationLogs: (params) => {
    return server.get('/api/user/logs', { params })
  }
}

export default server