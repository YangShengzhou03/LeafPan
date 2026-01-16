# 轻羽云盘 - 前后端接口联调文档

## 项目信息

- **项目名称**: 轻羽云盘
- **项目地址**: https://gitee.com/Yangshengzhou/leaf-pan
- **前端地址**: http://localhost:8080
- **后端地址**: http://localhost:8081/api
- **数据库**: MySQL 8.0+
- **对象存储**: MinIO

## 网盘核心功能（按优先级）

### 1. 文件分享 ⭐⭐⭐⭐⭐（最重要）

**后端接口**:
- `POST /share/create` - 创建分享
- `PUT /share/{id}` - 更新分享
- `GET /share/user` - 获取我的分享列表
- `GET /share/shared-with-me` - 获取与我共享的文件
- `DELETE /share/{id}` - 删除分享
- `GET /public/share/{shareCode}` - 通过分享码获取分享详情（公开接口）
- `POST /public/share/{shareCode}/verify` - 验证分享密码（公开接口）
- `GET /public/share/{shareCode}/download` - 通过分享链接下载文件（公开接口）

**前端页面**: [SharedPage.vue](file:///d:/Code/web/LeafPan/frontend/src/views/user/SharedPage.vue)、[ShareAccessPage.vue](file:///d:/Code/web/LeafPan/frontend/src/views/index/ShareAccessPage.vue)

**功能特点**:
- 外链分享（生成分享链接）
- 密码保护
- 过期时间设置
- 分享链接直接下载
- 分享统计（查看次数、下载次数）

**使用示例**:
```javascript
// 创建分享
const response = await Server.post('/share/create', {
  fileId: 123,
  shareType: 'link',
  hasPassword: true,
  password: '123456',
  expiresAt: '2026-01-20T00:00:00'
})

// 分享链接格式
const shareUrl = `${window.location.origin}/s/${response.data.shareCode}`
// 例如: http://localhost:8080/s/AbCdEf1234
```

### 2. 文件上传/下载 ⭐⭐⭐⭐⭐

**后端接口**:
- `POST /file/upload` - 文件上传
- `GET /file/{id}/download` - 下载文件

**前端页面**: [FilesPage.vue](file:///d:/Code/web/LeafPan/frontend/src/views/user/FilesPage.vue)

**功能特点**:
- 支持大文件上传（最大5GB）
- 文件下载
- 上传进度显示

### 3. 文件管理 ⭐⭐⭐⭐

**后端接口**:
- `GET /file/list` - 获取文件列表
- `GET /file/list/page` - 分页获取文件列表
- `GET /file/search` - 搜索文件
- `GET /file/{id}` - 获取文件详情
- `PUT /file/{id}/rename` - 重命名文件
- `DELETE /file/{id}` - 删除文件
- `DELETE /file/batch` - 批量删除文件
- `PUT /file/{id}/move` - 移动文件
- `POST /file/{id}/copy` - 复制文件
- `GET /file/storage/usage` - 存储使用情况

**前端页面**: [FilesPage.vue](file:///d:/Code/web/LeafPan/frontend/src/views/user/FilesPage.vue)

**功能特点**:
- 文件列表（网格/列表视图）
- 文件搜索（支持关键词搜索）
- 文件移动/复制
- 文件重命名/删除
- 文件夹管理
- 存储空间可视化

### 4. 存储空间管理 ⭐⭐⭐⭐

**后端接口**:
- `GET /file/storage/usage` - 存储使用情况
- `GET /user/storage` - 获取用户存储使用情况

**前端页面**: [FilesPage.vue](file:///d:/Code/web/LeafPan/frontend/src/views/user/FilesPage.vue)、[DashboardPage.vue](file:///d:/Code/web/LeafPan/frontend/src/views/user/DashboardPage.vue)

**功能特点**:
- 存储空间统计
- 存储空间可视化（进度条）
- 存储空间预警（颜色提示）

**使用示例**:
```javascript
const response = await Server.get('/file/storage/usage')
// 返回数据:
{
  totalSize: 1073741824,      // 已使用空间（字节）
  quota: 5368709120,          // 总空间（字节）
  usagePercentage: 20.0          // 使用百分比
}
```

### 5. 在线预览 ⭐⭐⭐

**后端接口**:
- `GET /file/{id}/preview` - 获取文件预览URL

**前端页面**: [FilesPage.vue](file:///d:/Code/web/LeafPan/frontend/src/views/user/FilesPage.vue)、[TrashPage.vue](file:///d:/Code/web/LeafPan/frontend/src/views/user/TrashPage.vue)

**功能特点**:
- 图片预览
- 文档预览（PDF、Word等）
- 视频预览
- 音频预览
- 文本文件预览

### 6. 回收站 ⭐⭐⭐

**后端接口**:
- `GET /user/trash/files` - 获取回收站文件列表
- `POST /user/trash/files/{id}/restore` - 恢复文件
- `POST /user/trash/folders/{id}/restore` - 恢复文件夹
- `DELETE /user/trash/files/{id}` - 永久删除文件
- `DELETE /user/trash/folders/{id}` - 永久删除文件夹
- `DELETE /user/trash/clear` - 清空回收站

**前端页面**: [TrashPage.vue](file:///d:/Code/web/LeafPan/frontend/src/views/user/TrashPage.vue)

**功能特点**:
- 回收站列表
- 恢复文件/文件夹
- 永久删除
- 清空回收站
- 剩余时间提示

### 7. 文件收藏 ⭐⭐⭐

**后端接口**:
- `POST /favorite/file/{fileId}` - 收藏文件
- `POST /favorite/folder/{folderId}` - 收藏文件夹
- `DELETE /favorite/file/{fileId}` - 取消收藏文件
- `DELETE /favorite/folder/{folderId}` - 取消收藏文件夹
- `GET /favorite` - 获取用户收藏列表
- `GET /favorite/file/{fileId}` - 检查文件是否收藏
- `GET /favorite/folder/{folderId}` - 检查文件夹是否收藏

**前端页面**: [FilesPage.vue](file:///d:/Code/web/LeafPan/frontend/src/views/user/FilesPage.vue)、[FavoritesPage.vue](file:///d:/Code/web/LeafPan/frontend/src/views/user/FavoritesPage.vue)

**功能特点**:
- 收藏文件/文件夹
- 收藏列表（支持文件和文件夹切换）
- 收藏状态显示

### 8. 文件夹管理 ⭐⭐⭐

**后端接口**:
- `POST /folder/create` - 创建文件夹
- `GET /folder/list` - 获取文件夹列表
- `GET /folder/root` - 获取根文件夹
- `GET /folder/{parentId}/subfolders` - 获取子文件夹
- `GET /folder/{id}` - 获取文件夹详情
- `GET /folder/{id}/path` - 获取文件夹路径
- `PUT /folder/{id}/rename` - 重命名文件夹
- `DELETE /folder/{id}` - 删除文件夹
- `PUT /folder/{id}/move` - 移动文件夹
- `POST /folder/{id}/copy` - 复制文件夹

**前端页面**: [FilesPage.vue](file:///d:/Code/web/LeafPan/frontend/src/views/user/FilesPage.vue)

**功能特点**:
- 创建文件夹
- 文件夹导航
- 文件夹路径显示
- 文件夹移动/复制/删除

### 9. 用户管理 ⭐⭐

**后端接口**:
- `GET /user/profile` - 获取当前用户信息
- `PUT /user/profile` - 更新用户信息
- `GET /user/storage` - 获取用户存储使用情况
- `GET /user/list` - 获取用户列表（用于分享时选择用户）
- `PUT /user/password` - 修改密码

**前端页面**: [SettingsPage.vue](file:///d:/Code/web/LeafPan/frontend/src/views/user/SettingsPage.vue)

**功能特点**:
- 用户信息查看
- 用户信息编辑
- 密码修改
- 头像上传/管理

### 10. 头像管理 ⭐⭐

**后端接口**:
- `POST /avatar/upload` - 上传头像
- `GET /avatar` - 获取头像信息
- `GET /avatar/view/{userId}` - 查看头像图片
- `DELETE /avatar` - 删除头像

**前端页面**: [SettingsPage.vue](file:///d:/Code/web/LeafPan/frontend/src/views/user/SettingsPage.vue)、[UserLayout.vue](file:///d:/Code/web/LeafPan/frontend/src/components/UserLayout.vue)、[AdminLayout.vue](file:///d:/Code/web/LeafPan/frontend/src/components/AdminLayout.vue)

**功能特点**:
- 头像上传（支持裁剪）
- 头像显示
- 头像删除

### 11. 认证功能 ⭐⭐⭐

**后端接口**:
- `POST /auth/login` - 用户登录
- `POST /auth/register` - 用户注册
- `GET /auth/me` - 获取当前用户信息
- `POST /auth/logout` - 用户登出
- `POST /auth/reset-password` - 重置密码

**前端页面**: [LoginPage.vue](file:///d:/Code/web/LeafPan/frontend/src/views/LoginPage.vue)

**功能特点**:
- 用户登录/注册
- JWT认证
- 自动登录
- 登出

### 12. 验证码功能 ⭐⭐

**后端接口**:
- `POST /verification/send` - 发送验证码
- `POST /auth/verify-email` - 验证邮箱
- `POST /auth/resend-verification` - 重新发送验证

**前端页面**: [LoginPage.vue](file:///d:/Code/web/LeafPan/frontend/src/views/LoginPage.vue)

**功能特点**:
- 邮箱验证码发送
- 邮箱验证

### 13. 配置管理 ⭐⭐

**后端接口**:
- `GET /config` - 获取系统配置
- `POST /config` - 更新系统配置

**前端页面**: [SystemPage.vue](file:///d:/Code/web/LeafPan/frontend/src/views/admin/SystemPage.vue)

**功能特点**:
- 系统配置查看
- 系统配置更新

### 14. 管理员功能 ⭐⭐

**后端接口**:
- `GET /admin/users` - 获取用户列表
- `PUT /admin/users/{id}` - 更新用户信息
- `DELETE /admin/users/{id}` - 删除用户
- `GET /admin/stats` - 获取统计数据
- `GET /admin/logs` - 获取操作日志
- `GET /admin/config` - 获取系统配置
- `POST /admin/config` - 更新系统配置

**前端页面**: [UsersPage.vue](file:///d:/Code/web/LeafPan/frontend/src/views/admin/UsersPage.vue)、[DashboardPage.vue](file:///d:/Code/web/LeafPan/frontend/src/views/admin/DashboardPage.vue)、[LogsPage.vue](file:///d:/Code/web/LeafPan/frontend/src/views/admin/LogsPage.vue)、[SystemPage.vue](file:///d:/Code/web/LeafPan/frontend/src/views/admin/SystemPage.vue)

**功能特点**:
- 用户管理
- 统计数据查看
- 操作日志查看
- 系统配置管理

## 接口状态总结

| 功能模块 | 后端接口 | 前端页面 | 状态 |
|---------|---------|---------|------|
| 文件分享 | ✅ | ✅ | 已完成 |
| 文件上传/下载 | ✅ | ✅ | 已完成 |
| 文件管理 | ✅ | ✅ | 已完成 |
| 存储空间管理 | ✅ | ✅ | 已完成 |
| 在线预览 | ✅ | ✅ | 已完成 |
| 回收站 | ✅ | ✅ | 已完成 |
| 文件收藏 | ✅ | ✅ | 已完成 |
| 文件夹管理 | ✅ | ✅ | 已完成 |
| 用户管理 | ✅ | ✅ | 已完成 |
| 头像管理 | ✅ | ✅ | 已完成 |
| 认证功能 | ✅ | ✅ | 已完成 |
| 验证码功能 | ✅ | ✅ | 已完成 |
| 配置管理 | ✅ | ✅ | 已完成 |
| 管理员功能 | ✅ | ✅ | 已完成 |

## 配置说明

### 前端配置 (.env)
```env
VUE_APP_API_URL=http://localhost:8081/api
```

### 后端配置 (application.yml)
```yaml
server:
  port: 8081
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/leafpan?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456

minio:
  endpoint: http://localhost:9000
  access-key: minioadmin
  secret-key: minioadmin
  bucket-name: data
  data-bucket: data
  avatar-bucket: avatar
```

## 启动说明

### 启动后端
```bash
cd backend
mvn spring-boot:run
```

### 启动前端
```bash
cd frontend
npm install
npm run serve
```

## 注意事项

1. **文件分享链接格式**: `http://localhost:8080/s/{shareCode}`
2. **存储空间单位**: 字节（需要在前端转换为KB/MB/GB）
3. **时间格式**: ISO 8601格式（`yyyy-MM-dd HH:mm:ss`）
4. **分页参数**: 后端页码从0开始，前端从1开始
5. **响应格式**: 统一使用`ApiResponse`包装
6. **头像显示**: 使用 `/avatar/view/{userId}` 路径获取头像图片
7. **文件上传最大大小**: 5GB
