export function isLoggedIn() {
  return !!localStorage.getItem('token')
}

export function saveToken(token) {
  localStorage.setItem('token', token)
}

export function removeToken() {
  localStorage.removeItem('token')
}

export function getToken() {
  return localStorage.getItem('token')
}

export function formatFileSize(bytes) {
  if (bytes === null || bytes === undefined || bytes === 0) return '0 Bytes'
  
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

export function formatDate(dateString) {
  const date = new Date(dateString)
  return date.toLocaleDateString() + ' ' + date.toLocaleTimeString()
}

export function getFileExtension(filename) {
  return filename.slice((filename.lastIndexOf('.') - 1 >>> 0) + 2)
}

export function isImageFile(filename) {
  const imageExtensions = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp']
  const extension = getFileExtension(filename).toLowerCase()
  return imageExtensions.includes(extension)
}

export function isVideoFile(filename) {
  const videoExtensions = ['mp4', 'avi', 'mov', 'wmv', 'flv', 'webm']
  const extension = getFileExtension(filename).toLowerCase()
  return videoExtensions.includes(extension)
}

export function isAudioFile(filename) {
  const audioExtensions = ['mp3', 'wav', 'flac', 'aac', 'ogg']
  const extension = getFileExtension(filename).toLowerCase()
  return audioExtensions.includes(extension)
}

export function isDocumentFile(filename) {
  const documentExtensions = ['pdf', 'doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx', 'txt']
  const extension = getFileExtension(filename).toLowerCase()
  return documentExtensions.includes(extension)
}

export function getFileIcon(filename) {
  if (isImageFile(filename)) return 'el-icon-picture'
  if (isVideoFile(filename)) return 'el-icon-video-play'
  if (isAudioFile(filename)) return 'el-icon-headset'
  if (isDocumentFile(filename)) return 'el-icon-document'
  
  return 'el-icon-document'
}

export function getFileType(filename, mimeType) {
  if (mimeType) {
    if (mimeType.startsWith('image/')) return 'image'
    if (mimeType.startsWith('video/')) return 'video'
    if (mimeType.startsWith('audio/')) return 'audio'
    if (mimeType.includes('pdf')) return 'pdf'
    if (mimeType.includes('word') || mimeType.includes('document')) return 'document'
    if (mimeType.includes('zip') || mimeType.includes('rar') || mimeType.includes('tar')) return 'archive'
    if (mimeType.includes('text') || mimeType.includes('javascript') || mimeType.includes('json')) return 'code'
  }
  
  if (filename) {
    const ext = getFileExtension(filename).toLowerCase()
    const imageExts = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'svg']
    const videoExts = ['mp4', 'avi', 'mov', 'wmv', 'flv', 'mkv']
    const audioExts = ['mp3', 'wav', 'flac', 'aac', 'ogg']
    const archiveExts = ['zip', 'rar', '7z', 'tar', 'gz']
    const codeExts = ['js', 'ts', 'java', 'py', 'cpp', 'c', 'html', 'css', 'php', 'xml', 'json']
    const documentExts = ['doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx', 'txt', 'rtf']
    
    if (imageExts.includes(ext)) return 'image'
    if (videoExts.includes(ext)) return 'video'
    if (audioExts.includes(ext)) return 'audio'
    if (archiveExts.includes(ext)) return 'archive'
    if (codeExts.includes(ext)) return 'code'
    if (documentExts.includes(ext)) return 'document'
    if (ext === 'pdf') return 'pdf'
  }
  
  return 'other'
}

export function getFileIconClass(type) {
  const classes = {
    folder: 'folder-icon',
    image: 'image-icon',
    video: 'video-icon',
    audio: 'audio-icon',
    pdf: 'pdf-icon',
    document: 'document-icon',
    archive: 'archive-icon',
    code: 'code-icon',
    other: 'other-icon'
  }
  return classes[type] || classes.other
}

export function getFileTypeLabel(type) {
  const labels = {
    folder: '文件夹',
    image: '图片',
    video: '视频',
    audio: '音频',
    pdf: 'PDF',
    document: '文档',
    archive: '压缩包',
    code: '代码',
    other: '其他'
  }
  return labels[type] || labels.other
}

export function generateRandomString(length = 8) {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'
  let result = ''
  for (let i = 0; i < length; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length))
  }
  return result
}

export function debounce(func, wait) {
  let timeout
  return function(...args) {
    const context = this
    clearTimeout(timeout)
    timeout = setTimeout(() => func.apply(context, args), wait)
  }
}

export function throttle(func, limit) {
  let inThrottle
  return function(...args) {
    const context = this
    if (!inThrottle) {
      func.apply(context, args)
      inThrottle = true
      setTimeout(() => inThrottle = false, limit)
    }
  }
}
