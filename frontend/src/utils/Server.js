import axios from 'axios'
import { getToken, removeToken } from './utils.js'
import { ElMessage } from 'element-plus'

const Server = axios.create({
  baseURL: process.env.VUE_APP_API_URL || 'http://localhost:8081',
  timeout: 10000
})

// 请求拦截器
Server.interceptors.request.use(config => {
  const token = getToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截器
Server.interceptors.response.use(
  response => {
    // 统一处理响应数据格式
    if (response.data && response.data.code !== undefined) {
      if (response.data.code === 200) {
        return response.data
      } else {
        ElMessage.error(response.data.message || '请求失败')
        return Promise.reject(new Error(response.data.message || '请求失败'))
      }
    }
    return response
  },
  error => {
    // 网络错误处理
    if (!error.response) {
      ElMessage.error('网络连接失败，请检查网络设置')
      return Promise.reject(error)
    }
    
    const status = error.response.status
    const message = error.response.data?.message || '请求失败'
    
    switch (status) {
      case 401:
        ElMessage.error('登录已过期，请重新登录')
        removeToken()
        // 触发路由跳转到登录页
        if (window.location.pathname !== '/login') {
          window.location.href = '/login'
        }
        break
      case 403:
        ElMessage.error('权限不足，无法访问该资源')
        break
      case 404:
        ElMessage.error('请求的资源不存在')
        break
      case 500:
        ElMessage.error('服务器内部错误，请稍后重试')
        break
      default:
        ElMessage.error(message)
    }
    
    return Promise.reject(error)
  }
)

// 导出常用的HTTP方法
const http = {
  get: (url, params = {}) => Server.get(url, { params }),
  post: (url, data = {}) => Server.post(url, data),
  put: (url, data = {}) => Server.put(url, data),
  delete: (url, params = {}) => Server.delete(url, { params }),
  upload: (url, formData, onUploadProgress) => {
    return Server.post(url, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      onUploadProgress
    })
  }
}

export default http