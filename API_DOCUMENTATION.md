# LeafPan API 接口文档

## 概述

LeafPan 是一个基于云存储的文件管理系统，提供了完整的文件管理功能，包括文件上传、下载、分享、回收站等。本文档详细描述了前端与后端交互的所有API接口。

## 基础信息

- **基础URL**: `http://localhost:8081`
- **认证方式**: Bearer Token
- **数据格式**: JSON
- **字符编码**: UTF-8

## 认证相关 API

### 用户登录

- **接口地址**: `/api/auth/login`
- **请求方法**: POST
- **请求参数**:
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "登录成功",
    "data": {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "user": {
        "id": 1,
        "username": "admin",
        "email": "admin@example.com"
      }
    }
  }
  ```

### 用户注册

- **接口地址**: `/api/auth/register`
- **请求方法**: POST
- **请求参数**:
  ```json
  {
    "username": "string",
    "password": "string",
    "email": "string"
  }
  ```

### 获取当前用户信息

- **接口地址**: `/api/auth/me`
- **请求方法**: GET
- **请求头**: `Authorization: Bearer <token>`

### 修改密码

- **接口地址**: `/api/auth/password`
- **请求方法**: PUT
- **请求参数**:
  ```json
  {
    "oldPassword": "string",
    "newPassword": "string"
  }
  ```

### 刷新令牌

- **接口地址**: `/api/auth/refresh`
- **请求方法**: POST

### 登出

- **接口地址**: `/api/auth/logout`
- **请求方法**: POST

## 文件相关 API

### 上传文件

- **接口地址**: `/api/user/file/upload`
- **请求方法**: POST
- **请求头**: `Content-Type: multipart/form-data`
- **请求参数**: FormData
  - `file`: 文件对象
  - `path`: 文件路径（可选）
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "上传成功",
    "data": {
      "id": 123,
      "name": "document.pdf",
      "size": 1024000,
      "type": "pdf",
      "path": "/documents",
      "uploadTime": "2023-06-15T10:30:00Z"
    }
  }
  ```

### 获取文件列表

- **接口地址**: `/api/user/file/list`
- **请求方法**: GET
- **请求参数**:
  - `path`: 文件路径（可选）
  - `page`: 页码（默认为1）
  - `size`: 每页数量（默认为20）
  - `sort`: 排序字段（可选）
  - `order`: 排序方向（asc/desc，默认为desc）

### 获取文件详情

- **接口地址**: `/api/user/file/{id}`
- **请求方法**: GET

### 下载文件

- **接口地址**: `/api/user/file/{id}/download`
- **请求方法**: GET
- **响应类型**: Blob

### 预览文件

- **接口地址**: `/api/user/file/{id}/preview`
- **请求方法**: GET

### 重命名文件

- **接口地址**: `/api/user/file/{id}/rename`
- **请求方法**: PUT
- **请求参数**:
  ```json
  {
    "name": "string"
  }
  ```

### 删除文件

- **接口地址**: `/api/user/file/{id}`
- **请求方法**: DELETE

### 搜索文件

- **接口地址**: `/api/user/file/search`
- **请求方法**: GET
- **请求参数**:
  - `name`: 文件名（支持模糊搜索）

### 获取存储使用情况

- **接口地址**: `/api/user/storage/usage`
- **请求方法**: GET
- **响应示例**:
  ```json
  {
    "code": 200,
    "data": {
      "totalSpace": 5368709120,
      "usedSpace": 2147483648,
      "availableSpace": 3221225472,
      "usagePercentage": 40
    }
  }
  ```

## 文件夹相关 API

### 创建文件夹

- **接口地址**: `/api/folder/create`
- **请求方法**: POST
- **请求参数**:
  ```json
  {
    "name": "string",
    "parentId": "number"
  }
  ```

### 获取用户文件夹列表

- **接口地址**: `/api/folder/list`
- **请求方法**: GET

### 获取子文件夹

- **接口地址**: `/api/folder/{parentId}/subfolders`
- **请求方法**: GET

### 获取文件夹详情

- **接口地址**: `/api/folder/{id}`
- **请求方法**: GET

### 重命名文件夹

- **接口地址**: `/api/folder/{id}/rename`
- **请求方法**: PUT
- **请求参数**:
  ```json
  {
    "name": "string"
  }
  ```

### 删除文件夹

- **接口地址**: `/api/folder/{id}`
- **请求方法**: DELETE

## 分享相关 API

### 创建分享

- **接口地址**: `/api/share/create`
- **请求方法**: POST
- **请求参数**:
  ```json
  {
    "fileId": "number",
    "sharedTo": ["string"],
    "permission": "read|write",
    "expiresAt": "string"
  }
  ```

### 获取用户分享列表

- **接口地址**: `/api/share/list`
- **请求方法**: GET

### 获取分享详情

- **接口地址**: `/api/share/{shareCode}`
- **请求方法**: GET

### 更新分享

- **接口地址**: `/api/share/{id}`
- **请求方法**: PUT
- **请求参数**:
  ```json
  {
    "sharedTo": ["string"],
    "permission": "read|write",
    "expiresAt": "string"
  }
  ```

### 删除分享

- **接口地址**: `/api/share/{id}`
- **请求方法**: DELETE

### 通过分享码访问文件

- **接口地址**: `/api/share/{shareCode}/file`
- **请求方法**: GET

### 公开访问分享

- **接口地址**: `/api/share/public/{shareCode}`
- **请求方法**: GET

## 用户相关 API

### 获取用户信息

- **接口地址**: `/api/user/info`
- **请求方法**: GET

### 更新用户信息

- **接口地址**: `/api/user/info`
- **请求方法**: PUT
- **请求参数**:
  ```json
  {
    "username": "string",
    "email": "string",
    "phone": "string"
  }
  ```

### 更新个人资料

- **接口地址**: `/api/user/profile`
- **请求方法**: PUT
- **请求参数**:
  ```json
  {
    "username": "string",
    "email": "string",
    "phone": "string",
    "gender": "string",
    "birthDate": "string",
    "emergencyContact": "string",
    "emergencyPhone": "string"
  }
  ```

### 获取当前用户信息

- **接口地址**: `/api/user/me`
- **请求方法**: GET

### 获取操作日志

- **接口地址**: `/api/user/logs`
- **请求方法**: GET
- **请求参数**:
  - `page`: 页码（默认为1）
  - `size`: 每页数量（默认为20）
  - `type`: 操作类型（可选）

### 获取存储信息

- **接口地址**: `/api/user/storage`
- **请求方法**: GET
- **响应示例**:
  ```json
  {
    "code": 200,
    "data": {
      "totalSpace": 5368709120,
      "usedSpace": 2147483648,
      "availableSpace": 3221225472,
      "usagePercentage": 40
    }
  }
  ```

### 获取仪表板统计数据

- **接口地址**: `/api/user/dashboard/stats`
- **请求方法**: GET
- **响应示例**:
  ```json
  {
    "code": 200,
    "data": {
      "fileCount": 128,
      "folderCount": 24,
      "sharedCount": 12,
      "favoriteCount": 8
    }
  }
  ```

## 回收站相关 API

### 获取回收站文件

- **接口地址**: `/api/user/trash`
- **请求方法**: GET

### 恢复文件

- **接口地址**: `/api/user/trash/{id}/restore`
- **请求方法**: PUT

### 永久删除文件

- **接口地址**: `/api/user/trash/{id}`
- **请求方法**: DELETE

### 清空回收站

- **接口地址**: `/api/user/trash`
- **请求方法**: DELETE

## 错误处理

所有API接口在请求失败时会返回相应的错误信息：

```json
{
  "code": 400,
  "message": "错误描述",
  "data": null
}
```

常见错误码：
- 400: 请求参数错误
- 401: 未授权，需要登录
- 403: 权限不足
- 404: 资源不存在
- 500: 服务器内部错误

## 请求示例

### 使用axios发送请求

```javascript
// 获取文件列表
import { fileAPI } from '@/utils/api'

try {
  const response = await fileAPI.getFiles({ path: '/documents' })
  console.log(response.data)
} catch (error) {
  console.error('获取文件列表失败:', error)
}

// 上传文件
const formData = new FormData()
formData.append('file', fileObject)
formData.append('path', '/documents')

try {
  const response = await fileAPI.upload(formData, (progressEvent) => {
    const percentCompleted = Math.round(
      (progressEvent.loaded * 100) / progressEvent.total
    )
    console.log(`上传进度: ${percentCompleted}%`)
  })
  console.log('上传成功:', response.data)
} catch (error) {
  console.error('上传失败:', error)
}
```

## 注意事项

1. 所有需要认证的接口都需要在请求头中添加 `Authorization: Bearer <token>`
2. 文件上传接口使用 `multipart/form-data` 格式
3. 文件下载接口返回的是二进制数据，需要特殊处理
4. 分页接口的页码从1开始
5. 时间格式统一使用 ISO 8601 格式
6. 文件大小单位为字节