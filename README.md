# 🍁 LeafPan 枫叶网盘

<div align="center">

[![GitHub stars](https://img.shields.io/github/stars/YangShengzhou03/LeafPan?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/LeafPan/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/YangShengzhou03/LeafPan?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/LeafPan/network)
[![GitHub issues](https://img.shields.io/github/issues/YangShengzhou03/LeafPan?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/LeafPan/issues)
[![GitHub license](https://img.shields.io/github/license/YangShengzhou03/LeafPan?style=for-the-badge)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen?style=for-the-badge&logo=spring)](https://spring.io/projects/spring-boot)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.x-green?style=for-the-badge&logo=vue.js)](https://vuejs.org/)

**一个现代化的开源云存储解决方案**

[English](README_EN.md) | [中文](README.md) | [文档](https://github.com/YangShengzhou03/LeafPan/wiki) | [演示](https://leafpan.demo.com) | [下载](https://github.com/YangShengzhou03/LeafPan/releases)

</div>

## 📖 项目概述

LeafPan（枫叶网盘）是一个基于现代Web技术的开源云存储解决方案，提供企业级的文件管理、分享和协作功能。项目采用前后端分离架构，支持多租户、高并发和分布式部署。

## 🚀 核心特性

### ✨ 主要功能
- 🔐 **安全认证** - JWT令牌认证，支持多因素认证
- 📁 **文件管理** - 多级目录结构，拖拽上传，批量操作
- 🔗 **文件分享** - 公开/私有链接，密码保护，过期时间
- 📊 **存储统计** - 实时存储空间监控，使用量分析
- 🔍 **全文搜索** - 基于Elasticsearch的文件内容搜索
- 📱 **多端支持** - Web端、移动端、桌面客户端

### 🛠️ 技术特性
- ⚡ **高性能** - 支持大文件断点续传，多线程上传下载
- 🔒 **数据安全** - 文件加密存储，访问权限控制
- 📈 **可扩展** - 微服务架构，支持水平扩展
- 🐳 **容器化** - Docker一键部署，Kubernetes支持

## 🛠️ 技术栈

### 🎨 前端技术
- **Vue 3** - 现代化前端框架，组合式API
- **Element Plus** - 企业级UI组件库
- **TypeScript** - 类型安全的JavaScript超集
- **Vite** - 下一代前端构建工具
- **Pinia** - Vue状态管理库
- **Axios** - HTTP客户端库
- **Tailwind CSS** - 实用优先的CSS框架

### ⚙️ 后端技术
- **Spring Boot 3.5.6** - Java企业级应用框架
- **Spring Security** - 安全认证和授权框架
- **Spring Data JPA** - 数据持久化解决方案
- **MySQL 8.0+** - 关系型数据库

- **MinIO** - 高性能对象存储服务
- **Elasticsearch** - 分布式搜索和分析引擎
- **RabbitMQ** - 消息队列，支持异步处理

### 🐳 部署和运维
- **Docker** - 容器化部署
- **Docker Compose** - 多容器应用编排
- **Kubernetes** - 容器编排平台（可选）
- **Nginx** - 反向代理和负载均衡
- **Prometheus** - 监控和告警系统
- **Grafana** - 数据可视化平台

## 📁 项目结构

```
LeafPan/
├── backend/                         # Spring Boot后端模块
│   ├── src/main/java/             # Java源代码
│   │   ├── com/leafpan/           # 主包路径
│   │   │   ├── config/            # 配置类
│   │   │   ├── controller/        # REST控制器
│   │   │   ├── service/           # 业务逻辑层
│   │   │   ├── repository/        # 数据访问层
│   │   │   ├── entity/            # 实体类
│   │   │   ├── dto/               # 数据传输对象
│   │   │   └── exception/         # 异常处理
│   │   └── resources/              # 资源文件
│   │       ├── application.yml    # 应用配置
│   │       └── static/             # 静态资源
│   ├── src/test/java/             # 测试代码
│   └── pom.xml                    # Maven配置
├── frontend/                       # Vue 3前端模块
│   ├── src/                       # 源代码
│   │   ├── components/            # 可复用组件
│   │   ├── views/                 # 页面组件
│   │   ├── router/                # 路由配置
│   │   ├── store/                 # 状态管理
│   │   ├── utils/                 # 工具函数
│   │   └── assets/                # 静态资源
│   ├── public/                    # 公共资源
│   ├── package.json               # 依赖配置
│   └── vite.config.js             # 构建配置
├── docs/                          # 项目文档
│   ├── api/                       # API文档
│   ├── deployment/                # 部署文档
│   └── development/               # 开发文档
├── docker/                        # Docker配置
│   ├── docker-compose.yml         # 服务编排
│   ├── nginx/                     # Nginx配置
│   └── mysql/                     # 数据库配置
├── scripts/                       # 脚本文件
├── .github/                       # GitHub Actions配置
├── .gitignore                     # Git忽略配置
├── LICENSE                        # 许可证文件
└── README.md                      # 项目说明文档
```

## 🚀 快速开始

### 📋 环境要求

在开始之前，请确保您的系统满足以下要求：

- **Java 17+** - [下载地址](https://adoptium.net/)
- **MySQL 8.0+** - [下载地址](https://dev.mysql.com/downloads/mysql/)
- **Node.js 18+** - [下载地址](https://nodejs.org/)
- **Docker & Docker Compose** - [下载地址](https://docs.docker.com/get-docker/)
- **MinIO** - 对象存储服务（已包含在Docker配置中）

### 🐳 使用Docker快速部署（推荐）

1. **克隆项目**
```bash
git clone https://github.com/YangShengzhou03/LeafPan.git
cd LeafPan
```

2. **配置环境变量**
```bash
cp .env.example .env
# 编辑.env文件，配置数据库连接等信息
```

3. **启动所有服务**
```bash
docker-compose up -d
```

4. **访问应用**
- 前端应用：http://localhost:3000
- 后端API：http://localhost:8080
- MinIO控制台：http://localhost:9001
- 数据库管理：http://localhost:8081 (Adminer)

### 🔧 手动安装部署

#### 后端部署

1. **配置数据库**
```sql
CREATE DATABASE leafpan CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. **配置应用**
```bash
cd backend
cp src/main/resources/application.yml.example src/main/resources/application.yml
# 编辑application.yml，配置数据库连接和MinIO设置
```

3. **构建和运行**
```bash
# 使用Maven Wrapper
./mvnw clean package
java -jar target/leafpan-backend-1.0.0.jar

# 或者使用Spring Boot Maven插件
./mvnw spring-boot:run
```

#### 前端部署

1. **安装依赖**
```bash
cd frontend
npm install
```

2. **配置环境**
```bash
cp .env.example .env
```

3. **开发模式运行**
```bash
npm run dev
```

4. **生产构建**
```bash
npm run build
npm run preview  # 预览生产版本
```

### 🧪 测试

运行测试以确保一切正常工作：

```bash
# 后端测试
cd backend
./mvnw test

# 前端测试
cd frontend
npm run test
```

## 📊 数据库设计

### 核心表结构

#### 用户表 (users)
存储用户基本信息，包括认证信息、存储配额等。

#### 文件夹表 (folders)
支持多级目录结构，实现树形文件管理。

#### 文件表 (files)
存储文件元数据信息，与MinIO对象存储关联。

#### 分享表 (shares)
管理文件分享功能，支持公开/私有链接和密码保护。

#### 操作日志表 (operation_logs)
记录用户操作历史，用于审计和分析。

### 数据库配置
- **字符集**: utf8mb4 (支持emoji表情)
- **存储引擎**: InnoDB (支持事务和外键)
- **连接池**: HikariCP (高性能连接池)

详细数据库设计请参考 [DATABASE_DESIGN.md](DATABASE_DESIGN.md)

## 📡 API 接口文档

### 基础信息
- **基础URL**: `http://localhost:8081/api`
- **认证方式**: Bearer Token (JWT)
- **数据格式**: JSON
- **字符编码**: UTF-8

### 主要API接口

#### 认证相关
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `GET /api/auth/me` - 获取当前用户信息

#### 文件管理
- `GET /api/files` - 获取文件列表
- `POST /api/files/upload` - 上传文件
- `GET /api/files/{id}` - 下载文件
- `DELETE /api/files/{id}` - 删除文件

#### 用户管理
- `GET /api/user/profile` - 获取用户信息
- `PUT /api/user/profile` - 更新用户信息
- `POST /api/user/avatar` - 上传头像

详细API文档请参考 [API_DOCUMENTATION.md](API_DOCUMENTATION.md)

## 💾 MinIO 文件存储

### 存储桶配置
- **data桶**: 存储用户普通文件
- **avatar桶**: 存储用户头像文件

### 文件上传流程
1. 用户通过API上传文件
2. 系统验证文件类型和大小
3. 生成唯一文件名（UUID + 扩展名）
4. 根据文件类型选择存储桶
5. 上传到MinIO并创建数据库记录

### 配置说明
```yaml
minio:
  endpoint: http://localhost:9000
  access-key: minioadmin
  secret-key: minioadmin
  data-bucket: data      # 用户数据文件存储桶
  avatar-bucket: avatar  # 用户头像存储桶
```

详细MinIO配置请参考 [MinIO_README.md](MinIO_README.md)

## 🤝 贡献指南

我们欢迎社区贡献！请阅读我们的 [贡献指南](CONTRIBUTING.md) 了解如何参与项目开发。

### 贡献方式
- 报告bug和问题
- 建议新功能
- 提交代码改进
- 改进文档
- 帮助测试

### 开发流程
1. Fork项目并创建功能分支
2. 遵循代码规范进行开发
3. 添加必要的测试
4. 提交Pull Request
5. 等待代码审查

详细贡献指南请参考 [CONTRIBUTING.md](CONTRIBUTING.md)

## 📄 许可证

本项目采用 [MIT许可证](LICENSE)。

## 🏆 社区与支持

### 💬 社区渠道
- [GitHub Discussions](https://github.com/YangShengzhou03/LeafPan/discussions)
- [Discord频道](https://discord.gg/leafpan)
- [邮件列表](mailto:leafpan-dev@googlegroups.com)

### 📊 项目统计

[![GitHub Contributors](https://img.shields.io/github/contributors/YangShengzhou03/LeafPan?style=flat-square)](https://github.com/YangShengzhou03/LeafPan/graphs/contributors)
[![GitHub Commit Activity](https://img.shields.io/github/commit-activity/m/YangShengzhou03/LeafPan?style=flat-square)](https://github.com/YangShengzhou03/LeafPan/pulse)
[![GitHub Last Commit](https://img.shields.io/github/last-commit/YangShengzhou03/LeafPan?style=flat-square)](https://github.com/YangShengzhou03/LeafPan/commits/main)

---

<div align="center">

**感谢所有为LeafPan项目做出贡献的开发者！**

</div>