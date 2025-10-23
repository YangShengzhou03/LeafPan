# LeafPan API 接口文档

## 概述

LeafPan 是一个基于云存储的文件管理系统，提供了完整的文件管理功能，包括文件上传、下载、分享、回收站等。本文档详细描述了前端与后端交互的所有API接口。

## 基础信息

- **基础URL**: `http://localhost:8081`
- **认证方式**: Bearer Token (JWT)
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
        "email": "admin@example.com",
        "nickname": "管理员",
        "avatar": "https://example.com/avatar.jpg",
        "role": 1,
        "status": 1,
        "storageQuota": 1073741824,
        "usedStorage": 1048576,
        "lastLoginTime": "2023-06-15T10:30:00",
        "lastLoginIp": "192.168.1.100",
        "createdTime": "2023-01-01T00:00:00",
        "updatedTime": "2023-06-15T10:30:00"
      }
    }
  }
  ```

**用户角色说明**:
- `role`: 0-普通用户，1-管理员
- `status`: 0-禁用，1-正常
- `storageQuota`: 存储配额（字节），默认1GB
- `usedStorage`: 已用存储空间（字节）
- `lastLoginIp`: 最后登录IP地址

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
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "注册成功",
    "data": {
      "id": 2,
      "username": "testuser",
      "email": "test@example.com",
      "nickname": null,
      "avatar": null,
      "role": 0,
      "status": 1,
      "storageQuota": 1073741824,
      "usedStorage": 0,
      "lastLoginTime": null,
      "lastLoginIp": null,
      "createdTime": "2023-06-15T10:30:00",
      "updatedTime": "2023-06-15T10:30:00"
    }
  }
  ```

### 获取当前用户信息

- **接口地址**: `/api/auth/me`
- **请求方法**: GET
- **请求头**: `Authorization: Bearer <token>`
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "username": "admin",
      "email": "admin@example.com",
      "nickname": "管理员",
      "avatar": "https://example.com/avatar.jpg",
      "role": 1,
      "status": 1,
      "storageQuota": 1073741824,
      "usedStorage": 1048576,
      "lastLoginTime": "2023-06-15T10:30:00",
      "lastLoginIp": "192.168.1.100",
      "createdTime": "2023-01-01T00:00:00",
      "updatedTime": "2023-06-15T10:30:00"
    }
  }
  ```

### 修改密码

- **接口地址**: `/api/auth/change-password`
- **请求方法**: POST
- **请求参数**:
  ```json
  {
    "oldPassword": "string",
    "newPassword": "string"
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "密码修改成功",
    "data": null
  }
  ```

### 刷新令牌

- **接口地址**: `/api/auth/refresh`
- **请求方法**: POST
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "令牌刷新成功",
    "data": {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "expiresIn": 86400
    }
  }
  ```

### 登出

- **接口地址**: `/api/auth/logout`
- **请求方法**: POST
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "登出成功",
    "data": null
  }
  ```

### 忘记密码

- **接口地址**: `/api/auth/forgot-password`
- **请求方法**: POST
- **请求参数**:
  ```json
  {
    "email": "user@example.com"
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "重置密码链接已发送到您的邮箱",
    "data": null
  }
  ```

### 重置密码

- **接口地址**: `/api/auth/reset-password`
- **请求方法**: POST
- **请求参数**:
  ```json
  {
    "token": "reset_token_here",
    "newPassword": "new_password_here"
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "密码重置成功",
    "data": null
  }
  ```

### 验证邮箱

- **接口地址**: `/api/auth/verify-email`
- **请求方法**: POST
- **请求参数**:
  ```json
  {
    "token": "verification_token_here"
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "邮箱验证成功",
    "data": null
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "邮箱验证成功",
    "data": null
  }
  ```

### 重新发送验证邮件

- **接口地址**: `/api/auth/resend-verification`
- **请求方法**: POST
- **请求参数**:
  ```json
  {
    "email": "user@example.com"
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "验证邮件已重新发送",
    "data": null
  }
  ```

## 验证相关 API

### 1. 发送验证码

**地址**: `/api/verification/send`

**方法**: `POST`

**描述**: 发送邮箱验证码

**请求参数**:
```json
{
  "email": "user@example.com"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "验证码已发送",
  "data": {
    "message": "验证码已发送",
    "email": "user@example.com"
  }
}
```

### 2. 验证验证码

**地址**: `/api/verification/verify`

**方法**: `POST`

**描述**: 验证邮箱验证码

**请求参数**:
```json
{
  "email": "user@example.com",
  "code": "123456"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "验证码验证成功",
  "data": {
    "message": "验证码验证成功"
  }
}
```

## 文件相关 API

### 上传文件

- **接口地址**: `/api/file/upload`
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
      "originalName": "document.pdf",
      "folderId": 1,
      "userId": 1,
      "size": 1024000,
      "mimeType": "application/pdf",
      "extension": "pdf",
      "storageKey": "uuid-generated-key",
      "md5": "d41d8cd98f00b204e9800998ecf8427e",
      "isDeleted": false,
      "version": 1,
      "createdTime": "2023-06-15T10:30:00Z",
      "updatedTime": "2023-06-15T10:30:00Z"
    }
  }
  ```

### 获取文件列表

- **接口地址**: `/api/file/list`
- **请求方法**: GET
- **请求参数**:
  - `path`: 文件路径（可选）
  - `page`: 页码（默认为1）
  - `size`: 每页数量（默认为20）
  - `sort`: 排序字段（可选）
  - `order`: 排序方向（asc/desc，默认为desc）
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "获取文件列表成功",
    "data": {
      "content": [
        {
          "id": 1,
          "name": "document.pdf",
          "originalName": "document.pdf",
          "folderId": 1,
          "userId": 1,
          "size": 1024000,
          "mimeType": "application/pdf",
          "extension": "pdf",
          "storageKey": "uuid-generated-key",
          "md5": "d41d8cd98f00b204e9800998ecf8427e",
          "isDeleted": false,
          "version": 1,
          "createdTime": "2023-06-15T10:30:00Z",
          "updatedTime": "2023-06-15T10:30:00Z",
          "downloadUrl": "/api/file/1/download"
        }
      ],
      "totalElements": 1,
      "totalPages": 1,
      "size": 20,
      "number": 0
    }
  }
  ```

### 获取文件详情

- **接口地址**: `/api/file/{id}`
- **请求方法**: GET
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "获取文件详情成功",
    "data": {
      "id": 1,
      "name": "document.pdf",
      "originalName": "document.pdf",
      "folderId": 1,
      "userId": 1,
      "size": 1024000,
      "mimeType": "application/pdf",
      "extension": "pdf",
      "storageKey": "uuid-generated-key",
      "md5": "d41d8cd98f00b204e9800998ecf8427e",
      "isDeleted": false,
      "version": 1,
      "createdTime": "2023-06-15T10:30:00Z",
      "updatedTime": "2023-06-15T10:30:00Z",
      "downloadUrl": "/api/file/1/download",
      "previewUrl": "/api/file/1/preview"
    }
  }
  ```

### 下载文件

- **接口地址**: `/api/file/{id}/download`
- **请求方法**: GET
- **响应类型**: Blob
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "文件下载成功",
    "data": {
      "fileName": "document.pdf",
      "fileSize": 1024000,
      "contentType": "application/pdf",
      "downloadUrl": "http://localhost:8081/api/file/1/download"
    }
  }
  ```

### 预览文件

- **接口地址**: `/api/file/{id}/preview`
- **请求方法**: GET
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "文件预览成功",
    "data": {
      "fileName": "document.pdf",
      "fileSize": 1024000,
      "contentType": "application/pdf",
      "previewUrl": "http://localhost:8081/api/file/1/preview",
      "isPreviewable": true
    }
  }
  ```

### 重命名文件

- **接口地址**: `/api/file/{id}/rename`
- **请求方法**: PUT
- **请求参数**:
  ```json
  {
    "name": "string"
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "文件重命名成功",
    "data": {
      "id": 1,
      "name": "new-document.pdf",
      "originalName": "document.pdf",
      "folderId": 1,
      "userId": 1,
      "size": 1024000,
      "mimeType": "application/pdf",
      "extension": "pdf",
      "storageKey": "uuid-generated-key",
      "md5": "d41d8cd98f00b204e9800998ecf8427e",
      "isDeleted": false,
      "version": 1,
      "createdTime": "2023-06-15T10:30:00Z",
      "updatedTime": "2023-06-15T11:00:00Z"
    }
  }
  ```

### 删除文件

- **接口地址**: `/api/file/{id}`
- **请求方法**: DELETE
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "文件删除成功",
    "data": null
  }
  ```

### 搜索文件

- **接口地址**: `/api/file/search`
- **请求方法**: GET
- **请求参数**:
  - `name`: 文件名（支持模糊搜索）
  - `page` (可选): 页码，默认为1
  - `size` (可选): 每页大小，默认为20
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "搜索成功",
    "data": {
      "content": [
        {
          "id": 1,
          "name": "example.jpg",
          "originalName": "example.jpg",
          "folderId": 1,
          "userId": 1,
          "size": 1024,
          "mimeType": "image/jpeg",
          "extension": "jpg",
          "storageKey": "uuid-generated-key",
          "md5": "d41d8cd98f00b204e9800998ecf8427e",
          "isDeleted": false,
          "version": 1,
          "createdTime": "2023-01-01T12:00:00",
          "updatedTime": "2023-01-01T12:00:00",
          "downloadUrl": "/api/file/1/download"
        }
      ],
      "totalElements": 1,
      "totalPages": 1,
      "size": 20,
      "number": 0
    }
  }
  ```

### 根据扩展名获取文件

- **接口地址**: `/api/user/file/extension/{extension}`
- **请求方法**: GET
- **请求参数**:
  - `extension`: 文件扩展名 (如: jpg, pdf)
  - `page` (可选): 页码，默认为1
  - `size` (可选): 每页大小，默认为20
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "获取文件列表成功",
    "data": {
      "content": [
        {
          "id": 1,
          "name": "example.jpg",
          "size": 1024,
          "type": "image/jpeg",
          "uploadTime": "2023-01-01T12:00:00",
          "downloadUrl": "/api/user/file/1/download"
        }
      ],
      "totalElements": 1,
      "totalPages": 1,
      "size": 20,
      "number": 0
    }
  }
  ```

### 批量删除文件

- **接口地址**: `/api/user/file/batch`
- **请求方法**: DELETE
- **请求参数**:
  ```json
  {
    "fileIds": [1, 2, 3, 4, 5]
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "批量删除文件成功",
    "data": {
      "deletedCount": 5,
      "failedCount": 0
    }
  }
  ```

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
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "文件夹创建成功",
    "data": {
      "id": 5,
      "name": "新文件夹",
      "parentId": 1,
      "createTime": "2023-06-15T10:30:00",
      "updateTime": "2023-06-15T10:30:00"
    }
  }
  ```

### 获取用户文件夹列表

- **接口地址**: `/api/folder/list`
- **请求方法**: GET
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "获取文件夹列表成功",
    "data": [
      {
        "id": 1,
        "name": "文档",
        "parentId": null,
        "createTime": "2023-06-15T10:30:00",
        "updateTime": "2023-06-15T10:30:00"
      },
      {
        "id": 2,
        "name": "图片",
        "parentId": null,
        "createTime": "2023-06-15T10:30:00",
        "updateTime": "2023-06-15T10:30:00"
      }
    ]
  }
  ```

### 获取子文件夹列表

- **接口地址**: `/api/folder/{parentId}/subfolders`
- **请求方法**: GET
- **描述**: 获取指定文件夹下的子文件夹列表
- **请求头**: `Authorization: Bearer {token}`
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "获取子文件夹列表成功",
    "data": [
      {
        "id": 2,
        "name": "子文件夹1",
        "parentId": 1,
        "createTime": "2023-01-01T12:00:00",
        "updateTime": "2023-01-01T12:00:00"
      },
      {
        "id": 3,
        "name": "子文件夹2",
        "parentId": 1,
        "createTime": "2023-01-01T12:00:00",
        "updateTime": "2023-01-01T12:00:00"
      }
    ]
  }
  ```

### 获取文件夹详情

- **接口地址**: `/api/folder/{id}`
- **请求方法**: GET
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "获取文件夹详情成功",
    "data": {
      "id": 1,
      "name": "文档",
      "parentId": null,
      "createTime": "2023-06-15T10:30:00",
      "updateTime": "2023-06-15T10:30:00",
      "subfolderCount": 3,
      "fileCount": 15
    }
  }
  ```

### 重命名文件夹

- **接口地址**: `/api/folder/{id}/rename`
- **请求方法**: PUT
- **请求参数**:
  ```json
  {
    "name": "string"
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "文件夹重命名成功",
    "data": {
      "id": 1,
      "name": "新文件夹名",
      "parentId": null,
      "createTime": "2023-06-15T10:30:00",
      "updateTime": "2023-06-15T11:00:00"
    }
  }
  ```

### 删除文件夹

- **接口地址**: `/api/folder/{id}`
- **请求方法**: DELETE
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "文件夹删除成功",
    "data": null
  }
  ```

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
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "分享创建成功",
    "data": {
      "id": 1,
      "shareCode": "abc123def",
      "fileId": 5,
      "sharedTo": ["user1@example.com", "user2@example.com"],
      "permission": "read",
      "expiresAt": "2023-07-15T10:30:00",
      "createdTime": "2023-06-15T10:30:00"
    }
  }
  ```

### 获取用户分享列表

- **接口地址**: `/api/share/list`
- **请求方法**: GET
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "获取分享列表成功",
    "data": [
      {
        "id": 1,
        "shareCode": "abc123def",
        "fileId": 5,
        "fileName": "document.pdf",
        "sharedTo": ["user1@example.com"],
        "permission": "read",
        "expiresAt": "2023-07-15T10:30:00",
        "createdTime": "2023-06-15T10:30:00",
        "downloadCount": 5
      }
    ]
  }
  ```

### 获取分享详情

- **接口地址**: `/api/share/{shareCode}`
- **请求方法**: GET
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "获取分享详情成功",
    "data": {
      "id": 1,
      "shareCode": "abc123def",
      "fileId": 5,
      "fileName": "document.pdf",
      "fileSize": 1024000,
      "fileType": "pdf",
      "sharedBy": "admin@example.com",
      "permission": "read",
      "expiresAt": "2023-07-15T10:30:00",
      "createdTime": "2023-06-15T10:30:00",
      "downloadCount": 5,
      "previewUrl": "/api/share/abc123def/preview"
    }
  }
  ```

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
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "分享更新成功",
    "data": {
      "id": 1,
      "shareCode": "abc123def",
      "fileId": 5,
      "sharedTo": ["user1@example.com", "user3@example.com"],
      "permission": "write",
      "expiresAt": "2023-08-15T10:30:00",
      "updatedTime": "2023-06-16T10:30:00"
    }
  }
  ```

### 删除分享

- **接口地址**: `/api/share/{id}`
- **请求方法**: DELETE
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "分享删除成功",
    "data": null
  }
  ```

### 通过分享码访问文件

- **接口地址**: `/api/share/{shareCode}/file`
- **请求方法**: GET
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "获取文件成功",
    "data": {
      "id": 5,
      "name": "document.pdf",
      "size": 1024000,
      "type": "pdf",
      "downloadUrl": "/api/share/abc123def/download",
      "previewUrl": "/api/share/abc123def/preview"
    }
  }
  ```

### 公开访问分享

- **接口地址**: `/api/share/public/{shareCode}`
- **请求方法**: GET
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "获取分享内容成功",
    "data": {
      "shareCode": "abc123def",
      "fileName": "document.pdf",
      "fileSize": 1024000,
      "fileType": "pdf",
      "sharedBy": "admin@example.com",
      "expiresAt": "2023-07-15T10:30:00",
      "previewUrl": "/api/share/public/abc123def/preview",
      "downloadUrl": "/api/share/public/abc123def/download"
    }
  }
  ```

## 用户相关 API

### 获取用户个人资料

- **接口地址**: `/api/user/profile`
- **请求方法**: GET
- **请求头**: `Authorization: Bearer <token>`
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "username": "admin",
      "email": "admin@example.com",
      "nickname": "管理员",
      "avatar": "https://example.com/avatar.jpg",
      "role": 1,
      "status": 1,
      "storageQuota": 1073741824,
      "usedStorage": 1048576,
      "lastLoginTime": "2023-06-15T10:30:00",
      "lastLoginIp": "192.168.1.100",
      "createdTime": "2023-01-01T00:00:00",
      "updatedTime": "2023-06-15T10:30:00"
    }
  }
  ```

### 更新用户个人资料

- **接口地址**: `/api/user/profile`
- **请求方法**: PUT
- **请求头**: `Authorization: Bearer <token>`
- **请求参数**:
  ```json
  {
    "nickname": "string",
    "avatar": "string",
    "email": "string"
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "更新成功",
    "data": {
      "id": 1,
      "username": "admin",
      "email": "admin@example.com",
      "nickname": "新昵称",
      "avatar": "https://example.com/new-avatar.jpg",
      "role": 1,
      "status": 1,
      "storageQuota": 1073741824,
      "usedStorage": 1048576,
      "lastLoginTime": "2023-06-15T10:30:00",
      "lastLoginIp": "192.168.1.100",
      "createdTime": "2023-01-01T00:00:00",
      "updatedTime": "2023-06-15T11:00:00"
    }
  }
  ```

### 获取存储信息

- **接口地址**: `/api/user/storage`
- **请求方法**: GET
- **请求头**: `Authorization: Bearer <token>`
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "totalQuota": 1073741824,
      "usedStorage": 1048576,
      "availableStorage": 1072693248,
      "usagePercentage": 0.1
    }
  }
  ```

### 获取操作日志

- **接口地址**: `/api/user/logs`
- **请求方法**: GET
- **请求头**: `Authorization: Bearer <token>`
- **请求参数**:
  - `page` (可选): 页码，默认1
  - `size` (可选): 每页大小，默认10
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "total": 50,
      "page": 1,
      "size": 10,
      "list": [
        {
          "id": 1,
          "userId": 1,
          "operation": "LOGIN",
          "description": "用户登录",
          "ip": "192.168.1.100",
          "userAgent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
          "createdTime": "2023-06-15T10:30:00"
        }
      ]
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
- **请求参数**:
  - `page` (可选): 页码，默认1
  - `size` (可选): 每页大小，默认10
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "获取回收站文件成功",
    "data": {
      "total": 5,
      "page": 1,
      "size": 10,
      "list": [
        {
          "id": 1,
          "name": "deleted-document.pdf",
          "size": 1024000,
          "type": "file",
          "originalPath": "/documents",
          "deletedTime": "2023-06-15T10:30:00",
          "expiredTime": "2023-07-15T10:30:00"
        }
      ]
    }
  }
  ```

### 恢复文件

- **接口地址**: `/api/user/trash/{id}/restore`
- **请求方法**: PUT
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "文件恢复成功",
    "data": {
      "id": 1,
      "name": "document.pdf",
      "path": "/documents",
      "restoreTime": "2023-06-16T10:30:00"
    }
  }
  ```

### 永久删除文件

- **接口地址**: `/api/user/trash/{id}`
- **请求方法**: DELETE
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "文件永久删除成功",
    "data": null
  }
  ```

### 清空回收站

- **接口地址**: `/api/user/trash`
- **请求方法**: DELETE
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "回收站清空成功",
    "data": {
      "deletedCount": 5
    }
  }
  ```

## 管理员用户管理 API

### 获取用户列表

- **接口地址**: `/api/admin/users/list`
- **请求方法**: GET
- **请求头**: `Authorization: Bearer <token>`
- **请求参数**:
  - `page` (可选): 页码，默认1
  - `size` (可选): 每页大小，默认10
  - `keyword` (可选): 搜索关键词（用户名、邮箱）
  - `role` (可选): 角色筛选（0-普通用户，1-管理员）
  - `status` (可选): 状态筛选（0-禁用，1-正常）
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "total": 100,
      "page": 1,
      "size": 10,
      "list": [
        {
          "id": 1,
          "username": "admin",
          "email": "admin@example.com",
          "nickname": "管理员",
          "avatar": "https://example.com/avatar.jpg",
          "role": 1,
          "status": 1,
          "storageQuota": 1073741824,
          "usedStorage": 1048576,
          "lastLoginTime": "2023-06-15T10:30:00",
          "lastLoginIp": "192.168.1.100",
          "createdTime": "2023-01-01T00:00:00",
          "updatedTime": "2023-06-15T10:30:00"
        }
      ]
    }
  }
  ```

### 获取用户详情

- **接口地址**: `/api/admin/users/{id}`
- **请求方法**: GET
- **请求头**: `Authorization: Bearer <token>`
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "username": "admin",
      "email": "admin@example.com",
      "nickname": "管理员",
      "avatar": "https://example.com/avatar.jpg",
      "role": 1,
      "status": 1,
      "storageQuota": 1073741824,
      "usedStorage": 1048576,
      "lastLoginTime": "2023-06-15T10:30:00",
      "lastLoginIp": "192.168.1.100",
      "createdTime": "2023-01-01T00:00:00",
      "updatedTime": "2023-06-15T10:30:00"
    }
  }
  ```

### 更新用户信息

- **接口地址**: `/api/admin/users/{id}`
- **请求方法**: PUT
- **请求头**: `Authorization: Bearer <token>`
- **请求参数**:
  ```json
  {
    "nickname": "string",
    "avatar": "string",
    "email": "string",
    "role": 0,
    "status": 1,
    "storageQuota": 1073741824
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "更新成功",
    "data": {
      "id": 1,
      "username": "admin",
      "email": "admin@example.com",
      "nickname": "新昵称",
      "avatar": "https://example.com/new-avatar.jpg",
      "role": 1,
      "status": 1,
      "storageQuota": 2147483648,
      "usedStorage": 1048576,
      "lastLoginTime": "2023-06-15T10:30:00",
      "lastLoginIp": "192.168.1.100",
      "createdTime": "2023-01-01T00:00:00",
      "updatedTime": "2023-06-15T11:00:00"
    }
  }
  ```

### 删除用户

- **接口地址**: `/api/admin/users/{id}`
- **请求方法**: DELETE
- **请求头**: `Authorization: Bearer <token>`
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "删除成功",
    "data": null
  }
  ```

### 修改用户状态

- **接口地址**: `/api/admin/users/{id}/status`
- **请求方法**: PUT
- **请求头**: `Authorization: Bearer <token>`
- **请求参数**:
  ```json
  {
    "status": 0
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "状态更新成功",
    "data": {
      "id": 1,
      "status": 0
    }
  }
  ```

### 重置用户密码

- **接口地址**: `/api/admin/users/{id}/password`
- **请求方法**: PUT
- **请求头**: `Authorization: Bearer <token>`
- **请求参数**:
  ```json
  {
    "password": "string"
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "密码重置成功",
    "data": null
  }
  ```

### 获取用户统计信息

- **接口地址**: `/api/admin/users/statistics`
- **请求方法**: GET
- **请求头**: `Authorization: Bearer <token>`
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "totalUsers": 100,
      "activeUsers": 85,
      "inactiveUsers": 15,
      "adminUsers": 5,
      "normalUsers": 95,
      "todayLoginUsers": 10,
      "weekLoginUsers": 50
    }
  }
  ```

## 配置相关 API

### 获取系统配置

**地址**: `/api/config/system`

**方法**: `GET`

**描述**: 获取系统配置信息（需要管理员权限）

**请求头**: `Authorization: Bearer {token}` (需要管理员权限)

**响应示例**:
```json
{
  "code": 200,
  "message": "获取系统配置成功",
  "data": {
    "file": {
      "maxFileSize": "100MB",
      "allowedExtensions": ["jpg", "jpeg", "png", "gif", "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "zip", "rar"]
    },
    "minio": {
      "endpoint": "http://localhost:9000",
      "bucketName": "leafpan"
    },
    "jwt": {
      "expiration": 86400000
    },
    "app": {
      "name": "LeafPan网盘系统",
      "version": "1.0.0",
      "apiVersion": "v1"
    },
    "server": {
      "port": 8080,
      "contextPath": ""
    }
  }
}
```

### 获取公开配置

**地址**: `/api/config/public`

**方法**: `GET`

**描述**: 获取公开配置信息（无需认证）

**响应示例**:
```json
{
  "code": 200,
  "message": "获取公开配置成功",
  "data": {
    "file": {
      "maxFileSize": "100MB",
      "allowedExtensions": ["jpg", "jpeg", "png", "gif", "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "zip", "rar"]
    },
    "app": {
      "name": "LeafPan网盘系统",
      "version": "1.0.0",
      "apiVersion": "v1"
    }
  }
}
```

## 管理员文件管理 API

### 获取所有文件列表

**地址**: `/api/admin/file/list`

**方法**: `GET`

**描述**: 获取所有文件列表（需要管理员权限）

**请求参数**:
- 查询参数: `page` (可选) - 页码，默认为0
- 查询参数: `size` (可选) - 每页大小，默认为20

**请求头**: `Authorization: Bearer {token}` (需要管理员权限)

**响应示例**:
```json
{
  "code": 200,
  "message": "获取文件列表成功",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "example.jpg",
        "size": 1024,
        "type": "image/jpeg",
        "uploadTime": "2023-01-01T12:00:00",
        "userId": 1,
        "username": "user1"
      }
    ],
    "totalElements": 100,
    "totalPages": 5,
    "size": 20,
    "number": 0
  }
}
```

### 获取文件详情

**地址**: `/api/admin/file/{id}`

**方法**: `GET`

**描述**: 根据ID获取文件详情（需要管理员权限）

**请求参数**:
- 路径参数: `id` - 文件ID

**请求头**: `Authorization: Bearer {token}` (需要管理员权限)

**响应示例**:
```json
{
  "code": 200,
  "message": "获取文件详情成功",
  "data": {
    "id": 1,
    "name": "example.jpg",
    "size": 1024,
    "type": "image/jpeg",
    "uploadTime": "2023-01-01T12:00:00",
    "userId": 1,
    "username": "user1",
    "path": "/folder1/example.jpg",
    "downloadCount": 5
  }
}
```

### 删除文件

**地址**: `/api/admin/file/{id}`

**方法**: `DELETE`

**描述**: 删除文件（需要管理员权限）

**请求参数**:
- 路径参数: `id` - 文件ID

**请求头**: `Authorization: Bearer {token}` (需要管理员权限)

**响应示例**:
```json
{
  "code": 200,
  "message": "文件删除成功",
  "data": null
}
```

### 获取文件统计信息

**地址**: `/api/admin/file/statistics`

**方法**: `GET`

**描述**: 获取文件统计信息（需要管理员权限）

**请求头**: `Authorization: Bearer {token}` (需要管理员权限)

**响应示例**:
```json
{
  "code": 200,
  "message": "获取文件统计信息成功",
  "data": {
    "totalFiles": 1000,
    "totalSize": 10737418240,
    "todayUploads": 20,
    "weekUploads": 100,
    "fileTypeStats": {
      "image": 300,
      "document": 400,
      "video": 100,
      "audio": 50,
      "other": 150
    }
  }
}
```

### 获取用户文件列表

**地址**: `/api/admin/file/user/{userId}`

**方法**: `GET`

**描述**: 获取指定用户的文件列表（需要管理员权限）

**请求参数**:
- 路径参数: `userId` - 用户ID
- 查询参数: `page` (可选) - 页码，默认为0
- 查询参数: `size` (可选) - 每页大小，默认为20

**请求头**: `Authorization: Bearer {token}` (需要管理员权限)

**响应示例**:
```json
{
  "code": 200,
  "message": "获取用户文件列表成功",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "example.jpg",
        "size": 1024,
        "type": "image/jpeg",
        "uploadTime": "2023-01-01T12:00:00"
      }
    ],
    "totalElements": 50,
    "totalPages": 3,
    "size": 20,
    "number": 0
  }
}
```

### 获取存储使用统计

**地址**: `/api/admin/file/storage/usage`

**方法**: `GET`

**描述**: 获取存储使用统计（需要管理员权限）

**请求头**: `Authorization: Bearer {token}` (需要管理员权限)

**响应示例**:
```json
{
  "code": 200,
  "message": "获取存储使用统计成功",
  "data": {
    "totalStorage": 107374182400,
    "usedStorage": 53687091200,
    "availableStorage": 53687091200,
    "usagePercentage": 50,
    "userStorageStats": [
      {
        "userId": 1,
        "username": "user1",
        "usedStorage": 1073741824,
        "fileCount": 20
      }
    ]
  }
}
```

### 获取文件类型统计

**地址**: `/api/admin/file/type/statistics`

**方法**: `GET`

**描述**: 获取文件类型统计（需要管理员权限）

**请求头**: `Authorization: Bearer {token}` (需要管理员权限)

**响应示例**:
```json
{
  "code": 200,
  "message": "获取文件类型统计成功",
  "data": {
    "image": {
      "count": 300,
      "size": 3221225472
    },
    "document": {
      "count": 400,
      "size": 2147483648
    },
    "video": {
      "count": 100,
      "size": 5368709120
    },
    "audio": {
      "count": 50,
      "size": 1073741824
    },
    "other": {
      "count": 150,
      "size": 1073741824
    }
  }
}
```

## 错误处理

所有API接口在请求失败时会返回相应的错误信息：

```json
{
  "code": 400,
  "message": "错误描述",
  "data": null
}
```

### HTTP状态码

- 200: 请求成功
- 400: 请求参数错误
- 401: 未授权，需要登录
- 403: 权限不足
- 404: 资源不存在
- 409: 资源冲突
- 413: 请求体过大
- 422: 请求参数格式错误
- 429: 请求过于频繁
- 500: 服务器内部错误

### 业务错误码

#### 用户认证相关错误码

- 1001: 用户名或密码错误
- 1002: 用户不存在
- 1003: 用户已被禁用
- 1004: 密码已过期
- 1005: Token无效或已过期
- 1006: 邮箱已被注册
- 1007: 用户名已被注册
- 1008: 验证码错误
- 1009: 验证码已过期
- 1010: 邮箱格式不正确
- 1011: 密码格式不正确
- 1012: 邮箱未验证

#### 文件操作相关错误码

- 2001: 文件不存在
- 2002: 文件已存在
- 2003: 文件类型不支持
- 2004: 文件大小超出限制
- 2005: 存储空间不足
- 2006: 文件上传失败
- 2007: 文件下载失败
- 2008: 文件删除失败
- 2009: 文件重命名失败
- 2010: 文件移动失败
- 2011: 文件复制失败
- 2012: 文件预览失败
- 2013: 文件分享失败
- 2014: 文件解压失败
- 2015: 文件压缩失败

#### 文件夹操作相关错误码

- 3001: 文件夹不存在
- 3002: 文件夹已存在
- 3003: 文件夹创建失败
- 3004: 文件夹删除失败
- 3005: 文件夹重命名失败
- 3006: 文件夹移动失败
- 3007: 文件夹不为空
- 3008: 不能删除根目录

#### 分享相关错误码

- 4001: 分享不存在
- 4002: 分享已过期
- 4003: 分享密码错误
- 4004: 分享次数已用尽
- 4005: 分享创建失败
- 4006: 分享更新失败
- 4007: 分享删除失败
- 4008: 分享访问失败

#### 系统相关错误码

- 5001: 系统维护中
- 5002: 系统繁忙，请稍后再试
- 5003: 请求过于频繁
- 5004: 服务不可用
- 5005: 数据库连接失败
- 5006: 文件存储服务异常

### 错误响应示例

#### 认证错误示例

```json
{
  "code": 1001,
  "message": "用户名或密码错误",
  "data": null
}
```

#### 文件操作错误示例

```json
{
  "code": 2003,
  "message": "文件类型不支持",
  "data": {
    "supportedTypes": ["jpg", "jpeg", "png", "gif", "pdf", "doc", "docx"],
    "receivedType": "exe"
  }
}
```

#### 权限错误示例

```json
{
  "code": 403,
  "message": "权限不足",
  "data": {
    "requiredPermission": "ADMIN",
    "currentPermission": "USER"
  }
}
```

#### 参数验证错误示例

```json
{
  "code": 422,
  "message": "请求参数格式错误",
  "data": {
    "errors": [
      {
        "field": "email",
        "message": "邮箱格式不正确"
      },
      {
        "field": "password",
        "message": "密码长度至少为8位"
      }
    ]
  }
}
```

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