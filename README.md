# 轻羽云盘 - 启动指南

## 项目简介
轻羽云盘是一个功能全面、安全可靠的云存储系统，支持文件上传下载、在线预览、分享、移动复制等核心功能。

**项目地址**: https://gitee.com/Yangshengzhou/leaf-pan

## 技术栈

### 后端
- Spring Boot 3.5.6
- Spring Data JPA
- Spring Security + JWT
- MySQL 8.0+
- MinIO对象存储

### 前端
- Vue 3.2.13
- Element Plus 2.11.5
- Vue Router 4.6.3
- Axios 1.6.0
- Vue Advanced Cropper 2.8.9

## 环境要求

### 必需环境
- JDK 17+
- Node.js 16+
- MySQL 8.0+
- MinIO Server

### 可选环境
- Maven 3.6+ (用于后端构建)
- npm (用于前端构建)

## 快速启动

### 1. 数据库初始化

```bash
# 登录MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE leafpan CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 配置后端

后端配置文件位于 `backend/src/main/resources/application.yml`

**开发环境配置（默认）：**
```yaml
spring:
  profiles:
    active: dev
  datasource:
    url: jdbc:mysql://localhost:3306/leafpan?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456

minio:
  endpoint: http://localhost:9000
  access-key: minioadmin
  secret-key: minioadmin
  data-bucket: data
  avatar-bucket: avatar
```

### 3. 启动MinIO

```bash
# Windows
minio server D:\minio\data --console-address ":9001"

# Linux/Mac
minio server /data/minio --console-address ":9001"
```

访问 MinIO 控制台：http://localhost:9001
- 用户名：minioadmin
- 密码：minioadmin

创建存储桶：
- data（用于文件存储）
- avatar（用于头像存储）

### 4. 启动后端

```bash
cd backend
mvn spring-boot:run
```

或使用IDE直接运行 `LeafPanApplication.java`

后端启动成功后，访问：http://localhost:8081/api

### 5. 配置前端

前端配置文件位于 `frontend/.env`

```env
VUE_APP_API_URL=http://localhost:8081/api
```

### 6. 启动前端

```bash
cd frontend
npm install
npm run serve
```

前端启动成功后，访问：http://localhost:8080

## 功能特性

### 核心功能
- 用户注册/登录
- 文件上传/下载
- 文件夹管理
- 文件在线预览（图片、视频、音频、PDF、文本）
- 文件分享（外链分享、密码保护、用户间分享）
- 文件移动/复制
- 文件收藏
- 存储空间管理
- 回收站管理
- 操作日志记录
- 用户头像管理

### 分享功能
- 公开分享：生成10位随机分享码
- 密码保护：可选设置访问密码
- 过期时间：可设置分享过期时间
- 用户间分享：私密分享给指定用户
- 分享统计：查看次数、下载次数

### 在线预览
- 图片预览（image/*）
- 视频预览（video/*）
- 音频预览（audio/*）
- PDF预览（application/pdf）
- 文本文件预览（text/*）

### 管理员功能
- 用户管理
- 系统统计
- 操作日志查看
- 系统配置管理

## API接口文档

### 认证相关（/auth）
- POST /auth/login - 用户登录
- POST /auth/register - 用户注册
- GET /auth/me - 获取当前用户信息
- POST /auth/logout - 用户登出
- POST /auth/reset-password - 重置密码
- POST /auth/verify-email - 验证邮箱
- POST /auth/resend-verification - 重新发送验证

### 用户管理（/user）
- GET /user/profile - 获取当前用户信息
- PUT /user/profile - 更新用户信息
- GET /user/storage - 获取用户存储使用情况
- GET /user/list - 获取用户列表（用于分享时选择用户）
- PUT /user/password - 修改密码

### 头像管理（/avatar）
- POST /avatar/upload - 上传头像
- GET /avatar - 获取头像信息
- GET /avatar/view/{userId} - 查看头像图片
- DELETE /avatar - 删除头像

### 文件管理（/file）
- POST /file/upload - 上传文件
- GET /file/list - 获取文件列表
- GET /file/list/page - 分页获取文件列表
- GET /file/search - 搜索文件
- GET /file/{id} - 获取文件详情
- GET /file/{id}/download - 下载文件
- GET /file/{id}/preview - 获取预览URL
- PUT /file/{id}/rename - 重命名文件
- DELETE /file/{id} - 删除文件
- PUT /file/{id}/move - 移动文件
- POST /file/{id}/copy - 复制文件
- DELETE /file/batch - 批量删除文件
- GET /file/storage/usage - 获取存储空间使用情况

### 文件夹管理（/folder）
- POST /folder/create - 创建文件夹
- GET /folder/list - 获取文件夹列表
- GET /folder/root - 获取根文件夹
- GET /folder/{parentId}/subfolders - 获取子文件夹
- GET /folder/{id} - 获取文件夹详情
- GET /folder/{id}/path - 获取文件夹路径
- PUT /folder/{id}/rename - 重命名文件夹
- DELETE /folder/{id} - 删除文件夹
- PUT /folder/{id}/move - 移动文件夹
- POST /folder/{id}/copy - 复制文件夹

### 分享管理（/share）
- POST /share/create - 创建分享
- PUT /share/{id} - 更新分享
- DELETE /share/{id} - 删除分享
- GET /share/user - 获取我的分享列表
- GET /share/shared-with-me - 获取与我共享的文件列表

### 公开分享（/public/share）
- GET /public/share/{shareCode} - 通过分享码获取分享信息
- POST /public/share/{shareCode}/verify - 验证分享密码
- GET /public/share/{shareCode}/download - 通过分享链接下载文件

### 回收站（/user/trash）
- GET /user/trash/files - 获取回收站文件列表
- POST /user/trash/files/{id}/restore - 恢复文件
- POST /user/trash/folders/{id}/restore - 恢复文件夹
- DELETE /user/trash/files/{id} - 永久删除文件
- DELETE /user/trash/folders/{id} - 永久删除文件夹
- DELETE /user/trash/clear - 清空回收站

### 收藏管理（/favorite）
- POST /favorite/file/{fileId} - 收藏文件
- POST /favorite/folder/{folderId} - 收藏文件夹
- DELETE /favorite/file/{fileId} - 取消收藏文件
- DELETE /favorite/folder/{folderId} - 取消收藏文件夹
- GET /favorite - 获取用户收藏列表
- GET /favorite/file/{fileId} - 检查文件是否收藏
- GET /favorite/folder/{folderId} - 检查文件夹是否收藏

### 验证码（/verification）
- POST /verification/send - 发送验证码

### 配置管理（/config）
- GET /config - 获取系统配置
- POST /config - 更新系统配置

### 管理员功能（/admin）
- GET /admin/users - 获取用户列表
- PUT /admin/users/{id} - 更新用户信息
- DELETE /admin/users/{id} - 删除用户
- GET /admin/stats - 获取统计数据
- GET /admin/logs - 获取操作日志
- GET /admin/config - 获取系统配置
- POST /admin/config - 更新系统配置

## 数据库结构

### 核心表
- users - 用户表
- folders - 文件夹表
- files - 文件表
- shares - 分享表
- favorites - 收藏表
- operation_logs - 操作日志表
- system_configs - 系统配置表

## 常见问题

### 后端启动失败
1. 检查MySQL是否启动
2. 检查数据库配置是否正确
3. 检查MinIO是否启动
4. 检查端口是否被占用

### 前端启动失败
1. 检查Node.js版本是否正确
2. 删除node_modules重新安装
3. 检查API地址配置是否正确

### 文件上传失败
1. 检查MinIO连接是否正常
2. 检查存储桶是否存在
3. 检查用户存储配额

### 分享功能异常
1. 检查分享码是否正确
2. 检查分享是否过期
3. 检查密码是否正确

### 头像显示异常
1. 检查头像是否上传成功
2. 检查头像存储桶是否存在
3. 检查头像访问路径是否正确

## 生产环境部署

### 后端部署
1. 修改配置文件，激活生产环境配置
2. 打包：`mvn clean package`
3. 运行：`java -jar backend-0.0.1-SNAPSHOT.jar`

### 前端部署
1. 构建：`npm run build`
2. 将dist目录部署到Web服务器

### 数据库优化
1. 根据实际需求调整存储配额
2. 定期清理过期分享和已删除文件
3. 监控数据库性能

## 项目结构

```
LeafPan/
├── backend/                 # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/       # Java源代码
│   │   │   └── resources/  # 配置文件
│   │   └── test/          # 测试代码
│   └── pom.xml            # Maven配置
├── frontend/               # 前端项目
│   ├── public/            # 静态资源
│   ├── src/               # Vue源代码
│   │   ├── components/    # 组件
│   │   ├── views/         # 页面
│   │   ├── router/        # 路由
│   │   └── utils/         # 工具
│   ├── package.json       # npm配置
│   └── .env              # 环境变量
└── API_INTEGRATION.md     # API接口文档
```

## 许可证
MIT License
