import { mockApiResponses, simulateApiDelay } from './mockData.js'

// 模拟API服务
export const mockApiService = {
  // 获取当前用户信息
  async getCurrentUser() {
    return simulateApiDelay(mockApiResponses.getCurrentUser)
  },

  // 获取文件列表
  async getFiles(path = '/') {
    // 模拟根据路径返回不同的文件列表
    // 这里简化处理，实际应该根据路径过滤文件
    return simulateApiDelay(mockApiResponses.getFiles)
  },

  // 上传文件
  async uploadFile(file, path = '/') {
    // 模拟文件上传
    console.log(`上传文件: ${file.name} 到路径: ${path}`)
    return simulateApiDelay(mockApiResponses.uploadFile)
  },

  // 下载文件
  async downloadFile(fileId) {
    // 模拟文件下载
    console.log(`下载文件ID: ${fileId}`)
    return simulateApiDelay({
      success: true,
      data: {
        downloadUrl: `https://minio.example.com/bucket1/download/${fileId}`
      }
    })
  },

  // 删除文件
  async deleteFile(fileId) {
    // 模拟文件删除
    console.log(`删除文件ID: ${fileId}`)
    return simulateApiDelay({
      success: true,
      message: '文件已成功删除'
    })
  },

  // 创建文件夹
  async createFolder(name, path = '/') {
    // 模拟创建文件夹
    console.log(`创建文件夹: ${name} 在路径: ${path}`)
    return simulateApiDelay(mockApiResponses.createFolder)
  },

  // 获取共享文件列表
  async getSharedFiles() {
    return simulateApiDelay(mockApiResponses.getSharedFiles)
  },

  // 分享文件
  async shareFile(fileId, options = {}) {
    // 模拟文件分享
    console.log(`分享文件ID: ${fileId}`, options)
    return simulateApiDelay({
      success: true,
      data: {
        shareId: `share-${Date.now()}`,
        shareUrl: `https://leafpan.com/s/share-${Date.now()}`,
        expiresAt: options.expiresAt || null
      }
    })
  },

  // 取消分享文件
  async unshareFile(shareId) {
    // 模拟取消分享
    console.log(`取消分享ID: ${shareId}`)
    return simulateApiDelay({
      success: true,
      message: '已取消分享'
    })
  },

  // 获取回收站文件列表
  async getRecycleBin() {
    return simulateApiDelay(mockApiResponses.getRecycleBin)
  },

  // 恢复文件
  async restoreFile(fileId) {
    // 模拟恢复文件
    console.log(`恢复文件ID: ${fileId}`)
    return simulateApiDelay({
      success: true,
      message: '文件已成功恢复'
    })
  },

  // 永久删除文件
  async permanentDeleteFile(fileId) {
    // 模拟永久删除
    console.log(`永久删除文件ID: ${fileId}`)
    return simulateApiDelay({
      success: true,
      message: '文件已永久删除'
    })
  },

  // 获取仪表盘统计信息
  async getDashboardStats() {
    return simulateApiDelay(mockApiResponses.getDashboardStats)
  },

  // 更新用户信息
  async updateProfile(userData) {
    // 模拟更新用户信息
    console.log('更新用户信息:', userData)
    return simulateApiDelay({
      success: true,
      message: '用户信息已更新',
      data: {
        ...mockApiResponses.getCurrentUser.data,
        ...userData
      }
    })
  },

  // 修改密码
  async changePassword(passwordData) {
    // 模拟修改密码
    console.log('修改密码')
    return simulateApiDelay({
      success: true,
      message: '密码已成功修改'
    })
  },

  // 收藏/取消收藏文件
  async toggleStarFile(fileId, starred) {
    // 模拟收藏/取消收藏
    console.log(`${starred ? '收藏' : '取消收藏'}文件ID: ${fileId}`)
    return simulateApiDelay({
      success: true,
      message: starred ? '已添加到收藏' : '已取消收藏',
      data: {
        fileId,
        starred
      }
    })
  }
}

export default mockApiService