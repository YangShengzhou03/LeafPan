/**
 * 模拟后端数据
 * 用于前端开发和测试
 */

// 用户信息模拟数据
export const mockUser = {
  id: 1,
  username: '枫叶用户',
  email: 'user@leafpan.com',
  avatar: '',
  storage: {
    total: 5368709120, // 5GB，以字节为单位
    used: 1288490188, // 1.2GB，以字节为单位
  },
  membership: {
    type: 'free', // free, premium, ultimate
    expiresAt: '2025-12-31T23:59:59Z'
  },
  createdAt: '2024-01-15T08:30:00Z',
  lastLoginAt: '2025-06-20T14:25:30Z'
}

// 文件和文件夹模拟数据
export const mockFiles = [
  // 文件夹
  {
    id: 'folder-1',
    name: '文档',
    type: 'folder',
    path: '/文档',
    parentId: null,
    size: 0,
    createdAt: '2024-03-15T10:30:00Z',
    updatedAt: '2024-06-10T14:20:00Z',
    shared: false,
    starred: true,
    children: [
      {
        id: 'folder-1-1',
        name: '工作文档',
        type: 'folder',
        path: '/文档/工作文档',
        parentId: 'folder-1',
        size: 0,
        createdAt: '2024-03-20T09:15:00Z',
        updatedAt: '2024-05-28T11:45:00Z',
        shared: false,
        starred: false,
        children: []
      },
      {
        id: 'file-1-1',
        name: '项目计划书.docx',
        type: 'document',
        path: '/文档/项目计划书.docx',
        parentId: 'folder-1',
        size: 245760, // 240KB
        createdAt: '2024-04-10T14:30:00Z',
        updatedAt: '2024-04-15T09:20:00Z',
        shared: true,
        starred: true,
        mimeType: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
        downloadUrl: 'https://minio.example.com/bucket1/documents/project-plan.docx'
      }
    ]
  },
  {
    id: 'folder-2',
    name: '图片',
    type: 'folder',
    path: '/图片',
    parentId: null,
    size: 0,
    createdAt: '2024-02-10T16:45:00Z',
    updatedAt: '2024-06-05T10:30:00Z',
    shared: false,
    starred: false,
    children: [
      {
        id: 'file-2-1',
        name: '风景照片.jpg',
        type: 'image',
        path: '/图片/风景照片.jpg',
        parentId: 'folder-2',
        size: 3670016, // 3.5MB
        createdAt: '2024-05-20T08:15:00Z',
        updatedAt: '2024-05-20T08:15:00Z',
        shared: false,
        starred: true,
        mimeType: 'image/jpeg',
        downloadUrl: 'https://minio.example.com/bucket1/images/landscape.jpg',
        thumbnailUrl: 'https://minio.example.com/bucket1/thumbnails/landscape-thumb.jpg'
      },
      {
        id: 'file-2-2',
        name: '家庭聚会.png',
        type: 'image',
        path: '/图片/家庭聚会.png',
        parentId: 'folder-2',
        size: 5242880, // 5MB
        createdAt: '2024-06-01T19:30:00Z',
        updatedAt: '2024-06-01T19:30:00Z',
        shared: true,
        starred: false,
        mimeType: 'image/png',
        downloadUrl: 'https://minio.example.com/bucket1/images/family-gathering.png',
        thumbnailUrl: 'https://minio.example.com/bucket1/thumbnails/family-gathering-thumb.png'
      }
    ]
  },
  {
    id: 'folder-3',
    name: '视频',
    type: 'folder',
    path: '/视频',
    parentId: null,
    size: 0,
    createdAt: '2024-01-25T11:20:00Z',
    updatedAt: '2024-05-30T16:40:00Z',
    shared: false,
    starred: false,
    children: [
      {
        id: 'file-3-1',
        name: '旅行记录.mp4',
        type: 'video',
        path: '/视频/旅行记录.mp4',
        parentId: 'folder-3',
        size: 104857600, // 100MB
        createdAt: '2024-04-05T14:25:00Z',
        updatedAt: '2024-04-05T14:25:00Z',
        shared: false,
        starred: true,
        mimeType: 'video/mp4',
        downloadUrl: 'https://minio.example.com/bucket1/videos/travel-record.mp4',
        thumbnailUrl: 'https://minio.example.com/bucket1/thumbnails/travel-record-thumb.jpg',
        duration: 360 // 秒
      }
    ]
  },
  // 根目录文件
  {
    id: 'file-4',
    name: '个人简历.pdf',
    type: 'document',
    path: '/个人简历.pdf',
    parentId: null,
    size: 524288, // 512KB
    createdAt: '2024-03-01T09:45:00Z',
    updatedAt: '2024-05-15T16:30:00Z',
    shared: false,
    starred: true,
    mimeType: 'application/pdf',
    downloadUrl: 'https://minio.example.com/bucket1/documents/resume.pdf'
  },
  {
    id: 'file-5',
    name: '财务报表.xlsx',
    type: 'spreadsheet',
    path: '/财务报表.xlsx',
    parentId: null,
    size: 1048576, // 1MB
    createdAt: '2024-06-01T10:15:00Z',
    updatedAt: '2024-06-10T14:20:00Z',
    shared: false,
    starred: false,
    mimeType: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    downloadUrl: 'https://minio.example.com/bucket1/documents/financial-report.xlsx'
  }
]

// 共享文件模拟数据
export const mockSharedFiles = [
  {
    id: 'shared-1',
    fileId: 'file-1-1',
    name: '项目计划书.docx',
    type: 'document',
    size: 245760,
    sharedBy: '张三',
    sharedAt: '2024-05-20T10:30:00Z',
    expiresAt: '2024-12-31T23:59:59Z',
    downloadCount: 15,
    downloadUrl: 'https://minio.example.com/bucket1/shared/project-plan.docx?token=abc123'
  },
  {
    id: 'shared-2',
    fileId: 'file-2-2',
    name: '家庭聚会.png',
    type: 'image',
    size: 5242880,
    sharedBy: '李四',
    sharedAt: '2024-06-05T16:45:00Z',
    expiresAt: null, // 永久有效
    downloadCount: 8,
    downloadUrl: 'https://minio.example.com/bucket1/shared/family-gathering.png?token=def456'
  }
]

// 回收站文件模拟数据
export const mockRecycleBin = [
  {
    id: 'deleted-1',
    originalId: 'file-6',
    name: '旧版简历.pdf',
    type: 'document',
    originalPath: '/旧版简历.pdf',
    size: 4831838, // 4.6MB
    deletedAt: '2024-05-10T14:30:00Z',
    willBeDeletedAt: '2024-07-10T14:30:00Z', // 30天后自动删除
    mimeType: 'application/pdf'
  },
  {
    id: 'deleted-2',
    originalId: 'folder-4',
    name: '临时文件',
    type: 'folder',
    originalPath: '/临时文件',
    size: 0,
    deletedAt: '2024-06-15T09:15:00Z',
    willBeDeletedAt: '2024-07-15T09:15:00Z',
    children: [
      {
        id: 'deleted-2-1',
        name: '草稿.txt',
        type: 'text',
        originalPath: '/临时文件/草稿.txt',
        size: 1024,
        deletedAt: '2024-06-15T09:15:00Z',
        willBeDeletedAt: '2024-07-15T09:15:00Z',
        mimeType: 'text/plain'
      }
    ]
  }
]

// 仪表盘统计信息模拟数据
export const mockDashboardStats = {
  totalFiles: 127,
  totalFolders: 15,
  totalSize: 1288490188, // 1.2GB
  sharedFiles: 5,
  starredFiles: 8,
  recentFiles: [
    {
      id: 'file-5',
      name: '财务报表.xlsx',
      type: 'spreadsheet',
      size: 1048576,
      updatedAt: '2024-06-10T14:20:00Z',
      path: '/财务报表.xlsx'
    },
    {
      id: 'file-2-2',
      name: '家庭聚会.png',
      type: 'image',
      size: 5242880,
      updatedAt: '2024-06-01T19:30:00Z',
      path: '/图片/家庭聚会.png'
    },
    {
      id: 'file-3-1',
      name: '旅行记录.mp4',
      type: 'video',
      size: 104857600,
      updatedAt: '2024-04-05T14:25:00Z',
      path: '/视频/旅行记录.mp4'
    }
  ],
  storageUsage: {
    documents: 524288, // 512KB
    images: 8912896, // 8.5MB
    videos: 104857600, // 100MB
    others: 226492992 // 216MB
  }
}

// 模拟API响应
export const mockApiResponses = {
  // 获取当前用户信息
  getCurrentUser: {
    success: true,
    data: mockUser
  },
  
  // 获取文件列表
  getFiles: {
    success: true,
    data: mockFiles
  },
  
  // 获取共享文件列表
  getSharedFiles: {
    success: true,
    data: mockSharedFiles
  },
  
  // 获取回收站文件列表
  getRecycleBin: {
    success: true,
    data: mockRecycleBin
  },
  
  // 获取仪表盘统计信息
  getDashboardStats: {
    success: true,
    data: mockDashboardStats
  },
  
  // 上传文件响应
  uploadFile: {
    success: true,
    data: {
      id: 'file-new',
      name: '新上传文件.txt',
      type: 'text',
      size: 1024,
      path: '/新上传文件.txt',
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      shared: false,
      starred: false,
      mimeType: 'text/plain',
      downloadUrl: 'https://minio.example.com/bucket1/documents/new-file.txt'
    }
  },
  
  // 创建文件夹响应
  createFolder: {
    success: true,
    data: {
      id: 'folder-new',
      name: '新文件夹',
      type: 'folder',
      path: '/新文件夹',
      parentId: null,
      size: 0,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      shared: false,
      starred: false,
      children: []
    }
  }
}

// 模拟API延迟
export const simulateApiDelay = (response, delay = 500) => {
  return new Promise(resolve => {
    setTimeout(() => {
      resolve(response)
    }, delay)
  })
}