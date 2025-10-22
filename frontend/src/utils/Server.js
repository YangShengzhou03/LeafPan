import axios from 'axios'
import { getToken } from './utils.js'

const Server = axios.create({
  baseURL: process.env.VUE_APP_API_URL || '',
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

export default Server