import axios from 'axios'
import { getToken, removeToken } from './utils.js'

const Server = axios.create({
  baseURL: process.env.VUE_APP_API_URL || 'http://localhost:8081/api',
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
    return response
  },
  error => {
    // 只有在token无效或过期时才清除token
    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
      console.error('Token无效或已过期，清除登录状态')
      removeToken()
      // 可以在这里添加重定向到登录页的逻辑，但为了避免循环重定向，
      // 我们让路由守卫来处理重定向
    }
    return Promise.reject(error)
  }
)

export default Server