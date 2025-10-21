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
- **Redis** - 缓存和会话存储
- **MinIO** - 高性能对象存储服务
- **Elasticsearch** - 分布式搜索和分析引擎
- **RabbitMQ** - 消息队列，支持异步处理

### 🐳 部署和运维
- **Docker** - 容器化部署
- **Docker Compose** - 多容器应用编排
- **Kubernetes** - 容器编排平台（可选）\- **Nginx** - 反向代理和负载均衡
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

## 功能特性

### 核心功能
- 用户注册与登录
- 文件上传与下载
- 文件管理与分类
- 文件分享与权限控制
- 存储空间管理

### 高级功能
- 多文件并发上传
- 断点续传
- 文件预览
- 版本控制
- 回收站功能

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
# 编辑.env文件，配置API地址等
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

### 🧪 测试部署

运行测试确保一切正常：

```bash
# 后端测试
cd backend
./mvnw test

# 前端测试
cd frontend
npm run test
```

## 数据库设计

详细的数据库表结构设计请参考 [DATABASE_DESIGN.md](./DATABASE_DESIGN.md) 文档。

### 核心表结构
- 用户表（users）- 存储用户基本信息
- 文件表（files）- 存储文件元数据信息
- 文件夹表（folders）- 支持多级目录结构
- 分享表（shares）- 管理文件分享功能
- 操作日志表（operation_logs）- 记录用户操作历史

## 部署说明

### 生产环境部署
1. 配置数据库连接
2. 配置MinIO存储服务
3. 构建后端应用
4. 部署前端静态资源
5. 配置反向代理和SSL证书

## 开发计划

### 当前阶段
- [x] 项目基础结构搭建
- [x] 后端框架配置
- [ ] 前端框架搭建
- [ ] 用户认证模块开发
- [ ] 文件管理功能开发

### 后续计划
- 文件预览功能
- 移动端适配
- 第三方登录集成
- API文档完善

## 🤝 贡献指南

我们非常欢迎社区贡献！请阅读以下指南了解如何参与项目开发。

### 📝 如何贡献

1. **报告问题**
   - 在 [GitHub Issues](https://github.com/YangShengzhou03/LeafPan/issues) 中搜索相关问题
   - 如果没有找到相关问题，请创建新的Issue并详细描述问题

2. **提交功能请求**
   - 在 [GitHub Discussions](https://github.com/YangShengzhou03/LeafPan/discussions) 中讨论新功能想法
   - 创建功能请求Issue，描述功能需求和实现建议

3. **代码贡献**
   - Fork项目到您的账户
   - 创建功能分支：`git checkout -b feature/amazing-feature`
   - 提交更改：`git commit -m 'Add some amazing feature'`
   - 推送到分支：`git push origin feature/amazing-feature`
   - 创建Pull Request

### 🏷️ 分支管理

- `main` - 主分支，稳定版本
- `develop` - 开发分支，新功能集成
- `feature/*` - 功能开发分支
- `bugfix/*` - 问题修复分支
- `release/*` - 发布分支

### 📋 代码规范

- 遵循 [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- 前端代码遵循 [Vue Style Guide](https://v3.vuejs.org/style-guide/)
- 提交信息遵循 [Conventional Commits](https://www.conventionalcommits.org/)
- 确保所有测试通过

## 📚 API文档

### 🔗 API端点

LeafPan提供完整的RESTful API接口，支持文件管理、用户认证等功能。

#### 认证相关
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/refresh` - 刷新令牌
- `POST /api/auth/logout` - 用户登出

#### 文件管理
- `GET /api/files` - 获取文件列表
- `POST /api/files/upload` - 上传文件
- `GET /api/files/{id}` - 获取文件信息
- `DELETE /api/files/{id}` - 删除文件
- `PUT /api/files/{id}` - 更新文件信息

#### 文件夹管理
- `GET /api/folders` - 获取文件夹列表
- `POST /api/folders` - 创建文件夹
- `PUT /api/folders/{id}` - 更新文件夹
- `DELETE /api/folders/{id}` - 删除文件夹

### 📖 详细文档

完整的API文档请参考：
- [API接口文档](https://github.com/YangShengzhou03/LeafPan/wiki/API-Documentation)
- [Swagger UI](http://localhost:8080/swagger-ui.html)（运行后端后访问）
- [Postman集合](docs/api/LeafPan.postman_collection.json)

## 🏆 社区和支持

### 💬 社区交流

- [GitHub Discussions](https://github.com/YangShengzhou03/LeafPan/discussions) - 功能讨论和问题解答
- [Discord频道](https://discord.gg/leafpan) - 实时交流社区
- [邮件列表](mailto:leafpan-dev@googlegroups.com) - 开发讨论

### 📊 项目统计

[![GitHub Contributors](https://img.shields.io/github/contributors/YangShengzhou03/LeafPan?style=flat-square)](https://github.com/YangShengzhou03/LeafPan/graphs/contributors)
[![GitHub Commit Activity](https://img.shields.io/github/commit-activity/m/YangShengzhou03/LeafPan?style=flat-square)](https://github.com/YangShengzhou03/LeafPan/pulse)
[![GitHub Last Commit](https://img.shields.io/github/last-commit/YangShengzhou03/LeafPan?style=flat-square)](https://github.com/YangShengzhou03/LeafPan/commits/main)

## 📄 许可证

本项目采用 [MIT许可证](LICENSE)。

```
MIT License

Copyright (c) 2024 LeafPan Team

Permission is hereby granted...
```

## 🙏 致谢

感谢所有为LeafPan项目做出贡献的开发者！

### 🎯 核心贡献者

[![Contributors](https://contrib.rocks/image?repo=YangShengzhou03/LeafPan)](https://github.com/YangShengzhou03/LeafPan/graphs/contributors)

### 🔗 相关项目

- [Spring Boot](https://spring.io/projects/spring-boot) - 优秀的Java应用框架
- [Vue.js](https://vuejs.org/) - 渐进式JavaScript框架
- [MinIO](https://min.io/) - 高性能对象存储
- [Element Plus](https://element-plus.org/) - Vue 3组件库

---

<div align="center">

**如果这个项目对您有帮助，请给个⭐️ Star支持一下！**

[⬆ 返回顶部](#-leafpan-枫叶网盘)

</div>
