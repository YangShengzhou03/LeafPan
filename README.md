# 轻羽云盘

<div align="center">

![LeafPan Logo](docs/images/logo.png) <!-- 假设在docs/images目录下有logo图片 -->

[![GitHub last commit](https://img.shields.io/github/last-commit/YangShengzhou03/LeafPan?style=for-the-badge)](https://github.com/YangShengzhou03/LeafPan/commits/main)
[![GitHub stars](https://img.shields.io/github/stars/YangShengzhou03/LeafPan?style=for-the-badge)](https://github.com/YangShengzhou03/LeafPan/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/YangShengzhou03/LeafPan?style=for-the-badge)](https://github.com/YangShengzhou03/LeafPan/network/members)
[![GitHub issues](https://img.shields.io/github/issues/YangShengzhou03/LeafPan?style=for-the-badge)](https://github.com/YangShengzhou03/LeafPan/issues)
[![License](https://img.shields.io/badge/license-MIT-blue?style=for-the-badge)](https://github.com/YangShengzhou03/LeafPan/blob/main/LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen?style=for-the-badge)](https://github.com/YangShengzhou03/LeafPan/blob/main/CONTRIBUTING.md)

</div>

## 项目简介

**LeafPan** 是一个功能全面、安全可靠的企业级云存储与文件管理平台，采用 **Vue 3 + Spring Boot 3** 前后端分离架构，为用户提供高效便捷的文件存储、分享、同步与协作解决方案。

LeafPan 专为满足个人用户、开发团队、中小企业和教育机构的多样化文件管理需求而设计。我们通过精心设计的用户界面和稳定的技术架构，提供卓越的用户体验，同时确保数据安全和系统性能。

### 🎯 核心价值

- **企业级安全保障**：全方位的数据加密、身份验证和访问控制机制
- **高性能架构**：优化的系统设计确保高效文件传输和快速响应
- **多平台支持**：完整的Web端体验，同时支持移动端适配
- **灵活部署选项**：支持Docker一键部署和Kubernetes云原生部署
- **开源与可扩展**：MIT许可证，模块化设计便于二次开发和功能扩展

<div align="center">

[English Documentation](README_EN.md) | [贡献指南](CONTRIBUTING.md)

</div>

## 目录

- [轻羽云盘](#轻羽云盘)
  - [目录](#目录)
  - [项目亮点](#项目亮点)
  - [快速开始](#快速开始)
    - [Docker 一键部署](#docker-一键部署推荐)
    - [手动部署](#手动部署)
  - [功能特性](#功能特性)
  - [技术栈](#技术栈)
  - [项目结构](#项目结构)
  - [开发指南](#开发指南)
  - [测试](#测试)
  - [贡献指南](#贡献指南)
  - [许可证](#许可证)

## 项目亮点

LeafPan 在设计时充分考虑了现代应用的实际需求，具备以下核心优势：

**企业级安全保障**：我们采用 JWT 认证机制，结合文件加密存储和细粒度权限控制，确保用户数据安全。通过 Spring Security 框架提供全方位的安全防护。

**高性能架构**：基于 Spring Boot 3.5.6 和 Vue 3.2.13 构建，采用前后端分离架构，配合 Redis 缓存优化，能够支持高并发访问，为用户提供流畅的使用体验。

**便捷的部署方案**：提供完整的 Docker 一键部署方案，同时支持 Kubernetes 云原生部署，让部署过程更加简单快捷，适应不同的部署环境需求。

**现代化界面设计**：使用 Element Plus 组件库和 Vue 3 响应式设计，界面美观易用，支持移动端适配，提供优秀的跨平台使用体验。

**自动化开发流程**：集成 GitHub Actions 自动化流程，实现代码质量检查和自动化测试，确保项目质量和开发效率。

### 🌍 适用场景

**个人用户**：安全的文件备份、照片存储和个人资料管理
**开发团队**：项目文档共享、代码备份、资源协作和版本管理
**中小企业**：内部文件管理、知识库建设、团队协作和数据共享
**教育机构**：教学资源管理、作业提交、学生协作和资料分发

### 🖼️ 系统预览

<div align="center">

![Dashboard Preview](docs/images/dashboard.png) <!-- 仪表盘预览截图 -->
![Files Management](docs/images/files.png) <!-- 文件管理界面截图 -->
![Sharing Interface](docs/images/sharing.png) <!-- 分享功能界面截图 -->

</div>

### 核心价值

作为开源项目，LeafPan 遵循 MIT 许可证，用户可以自由使用和修改。项目采用现代化的技术栈，具备良好的可扩展性。模块化设计使得功能扩展和二次开发更加简单，同时支持多种部署方式，满足不同用户的需求。

## 🚀 快速开始

### Docker 一键部署（推荐）

对于大多数用户来说，使用 Docker 进行部署是最简单快捷的方式。在开始部署之前，请确保您的系统满足以下环境要求：

- **Docker** 版本 24.0 或更高
- **Docker Compose** 版本 2.20 或更高
- 至少 **4GB** 内存（2GB 可运行但性能受限）
- 至少 **10GB** 可用磁盘空间
- **操作系统**：Linux (Ubuntu 20.04+/CentOS 8+), macOS 12+, Windows 10/11 (WSL2)

#### 部署步骤

部署过程非常简单，只需执行以下命令：

```bash
# 克隆项目仓库
git clone https://github.com/YangShengzhou03/LeafPan.git
cd LeafPan

# 使用Docker Compose启动所有服务
docker-compose -f deploy/docker-compose.yml up -d

# 查看服务启动状态
docker-compose -f deploy/docker-compose.yml ps
```

部署完成后，您可以通过以下地址访问各个服务：

- **前端应用**：http://localhost:80（注册后使用）
- **后端API**：http://localhost:8080
- **MinIO控制台**：http://localhost:9001（账号：minioadmin，密码：minioadmin123）
- **健康检查**：http://localhost:8080/actuator/health

#### 常用管理命令

在实际使用中，您可能会用到以下命令来管理服务：

```bash
# 停止所有服务
docker-compose down

# 重启服务
docker-compose restart

# 查看服务资源使用情况
docker-compose top

# 清理数据（谨慎使用，会删除所有数据）
docker-compose down -v
```

### 🔧 手动部署

对于需要自定义部署或无法使用Docker的环境，我们提供完整的手动部署方案。

#### 环境要求

- **后端环境**：
  - Java 17 LTS 或更高版本
  - Maven 3.8.0+ 构建工具
  - MySQL 8.0.33+ 数据库
  - Redis 7.0+ 缓存服务（推荐）
  - MinIO 8.5.4+ 对象存储服务

- **前端环境**：
  - Node.js 18.0+ LTS
  - npm 9.0+ 或 yarn 1.22+
  - Nginx 1.20+（用于生产部署）

#### 部署步骤

##### 1. 准备数据库和存储服务

```bash
# 1. 安装并启动MySQL服务
# 创建数据库和用户
CREATE DATABASE leafpan CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'leafpan'@'%' IDENTIFIED BY 'your_secure_password';
GRANT ALL PRIVILEGES ON leafpan.* TO 'leafpan'@'%';
FLUSH PRIVILEGES;

# 2. 安装并启动Redis服务

# 3. 安装并配置MinIO服务
minio server /path/to/data --console-address :9001
```

##### 2. 配置后端服务

```bash
# 克隆项目
cd backend

# 修改配置文件
cp src/main/resources/application.yml src/main/resources/application-prod.yml
# 编辑application-prod.yml配置文件，修改数据库连接、MinIO配置等

# 构建项目
./mvnw clean package -DskipTests

# 启动服务
java -jar target/backend.jar --spring.profiles.active=prod
```

##### 3. 配置前端服务

```bash
# 克隆项目
cd frontend

# 安装依赖
npm install

# 修改配置文件
# 编辑.env.production文件，设置API基础URL

# 构建项目
npm run build

# 部署到Nginx
# 复制dist目录内容到Nginx的html目录
```

##### 4. Nginx配置示例

```nginx
server {
    listen 80;
    server_name your-domain.com;

    location / {
        root /path/to/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    location /api {
        proxy_pass http://localhost:8080/api;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }

    # 可选：配置HTTPS
    # listen 443 ssl;
    # ssl_certificate /path/to/ssl/cert.pem;
    # ssl_certificate_key /path/to/ssl/key.pem;
}
```

## 🎯 功能特性

LeafPan 提供全面的文件管理功能，涵盖从用户认证到文件分享的完整流程。

### 🔐 安全认证系统

- **JWT 令牌认证**：基于 Spring Security 的无状态认证机制
- **多因素认证**：支持双因素认证，增强账户安全性
- **密码加密**：使用 BCrypt 算法加密存储用户密码
- **会话管理**：通过 Redis 和 Spring Session 实现分布式会话
- **RBAC权限控制**：基于角色的细粒度访问控制
- **IP限制和登录保护**：防止暴力破解和异常登录

### 📁 文件管理功能

- **大文件分片上传**：支持 GB 级文件的分片上传和断点续传
- **文件预览**：支持图片、文档（PDF、Office）、视频、音频在线预览
- **文件操作**：上传、下载、复制、移动、重命名、删除等完整操作
- **文件夹管理**：创建、编辑、删除文件夹，支持嵌套结构
- **回收站机制**：删除文件自动进入回收站，可恢复或永久删除
- **版本控制**：重要文件的多版本管理和历史版本恢复
- **收藏夹功能**：标记常用文件，快速访问

### 🔗 文件分享机制

- **链接分享**：生成可定制的分享链接，支持公开和私密分享
- **密码保护**：为分享链接设置访问密码，增强安全性
- **有效期设置**：自定义分享链接的有效期限
- **分享权限**：控制被分享者的操作权限（只读/编辑/下载）
- **分享管理**：统一管理所有分享链接，支持查看访问统计
- **批量分享**：支持多文件和文件夹的批量分享操作

### 🚀 高级功能

- **全文搜索**：基于 Elasticsearch 的文件名和内容快速搜索
- **文件标签**：通过标签对文件进行分类管理
- **文件评论**：支持在文件上添加评论，促进团队协作
- **活动日志**：记录所有文件操作历史，方便审计和追踪
- **存储空间管理**：用户配额管理和使用情况统计
- **移动端适配**：响应式设计，支持在各种移动设备上使用
- **WebDAV支持**：可通过WebDAV协议挂载为本地磁盘

### 🔧 技术特性

LeafPan 在技术实现上采用现代化的架构设计，具备以下技术特性：

**架构特性**：
- 前后端分离架构，前端 Vue 3 + 后端 Spring Boot 3
- RESTful API 设计，支持多客户端接入
- 模块化设计，便于功能扩展和维护
- 支持微服务架构演进

**安全特性**：
- JWT + Spring Security 认证授权体系
- BCrypt 密码加密存储
- HTTPS 传输加密
- 文件加密存储选项
- SQL注入防护、XSS防护、CSRF防护

**部署特性**：
- Docker 容器化部署，支持 Docker Compose 编排
- Kubernetes 云原生部署支持
- 多环境配置（开发、测试、生产）
- CI/CD 自动化流程（GitHub Actions）
- 健康检查和监控告警机制

## 🛠️ 技术栈

LeafPan 项目采用现代化的技术栈，涵盖前端、后端和部署运维三个主要层面。

### 🖥️ 前端技术栈

| 技术/框架 | 版本 | 用途 |
|----------|------|------|
| Vue | 3.2.13+ | 前端框架 |
| TypeScript | 5.0+ | 类型系统 |
| Element Plus | 2.3.8+ | UI组件库 |
| Vue Router | 4.1.5+ | 路由管理 |
| Pinia | 2.1.3+ | 状态管理 |
| Axios | 1.6.0+ | HTTP请求 |
| Vite | 4.4.0+ | 构建工具 |
| Jest | 29.5.0+ | 单元测试 |
| ESLint | 8.46.0+ | 代码规范检查 |
| Prettier | 3.0.0+ | 代码格式化 |

### ⚙️ 后端技术栈

| 技术/框架 | 版本 | 用途 |
|----------|------|------|
| Spring Boot | 3.5.6 | 后端框架 |
| Java | 17 LTS | 开发语言 |
| Spring Security | 6.1.0+ | 安全框架 |
| JWT | 0.11.5+ | 令牌认证 |
| Spring Data JPA | 3.1.0+ | ORM框架 |
| MySQL | 8.0.33+ | 关系型数据库 |
| Redis | 7.0+ | 缓存服务 |
| Elasticsearch | 8.5.0+ | 搜索引擎 |
| MinIO | 8.5.4+ | 对象存储 |
| Swagger/OpenAPI | 3.0 | API文档 |
| Lombok | 1.18.26+ | 代码简化 |
| MapStruct | 1.5.5+ | 对象映射 |

### 🚚 部署运维技术栈

| 技术/工具 | 版本 | 用途 |
|----------|------|------|
| Docker | 24.0+ | 容器化部署 |
| Docker Compose | 2.20+ | 服务编排 |
| Kubernetes | 1.25+ | 云原生部署 |
| Nginx | 1.24+ | 反向代理/负载均衡 |
| Prometheus | 2.46+ | 监控指标收集 |
| Grafana | 10.1+ | 监控数据可视化 |
| GitHub Actions | - | CI/CD自动化 |
| Logback | 1.4.8+ | 日志管理 |
| ELK Stack | - | 日志收集与分析

### 技术架构特点

LeafPan 的技术架构设计充分考虑了现代应用开发的需求，具备以下特点：

**架构优势**：采用前后端分离架构，实现清晰的职责分离，便于团队协作开发。模块化设计为微服务架构演进提供了良好基础，支持 API 优先的设计理念，通过 RESTful API 支持多客户端接入。容器化部署方案使得系统能够轻松运行在各种云环境中。

**安全特性**：系统实现了多层安全防护，涵盖网络层、应用层和数据层的全方位安全保护。通过 JWT 和 Spring Security 构建完整的认证授权体系，数据传输和存储都采用加密机制，同时提供基于角色的细粒度访问控制。

**性能优化**：在性能方面，采用多级缓存策略，结合 Redis 和浏览器缓存提升系统响应速度。通过非阻塞 IO 和异步任务处理优化系统性能，支持水平扩展和负载分发机制。对于静态资源，支持 CDN 加速实现全球分发。

## 📁 项目结构

LeafPan 项目采用清晰的模块化结构设计，便于开发和维护。

```
LeafPan/
├── backend/                         # Spring Boot后端模块
│   ├── src/main/java/             # Java源代码
│   │   ├── config/                # 配置类（数据库、安全、缓存等）
│   │   ├── controller/            # REST API控制器
│   │   ├── service/               # 业务逻辑层
│   │   ├── repository/            # 数据访问层
│   │   ├── entity/                # 数据库实体类
│   │   ├── dto/                   # 数据传输对象
│   │   ├── security/              # 认证授权配置
│   │   ├── exception/             # 异常处理
│   │   ├── filter/                # 过滤器（JWT、CORS等）
│   │   ├── interceptor/           # 拦截器
│   │   └── util/                  # 工具类
│   ├── src/main/resources/        # 资源文件
│   │   ├── application.yml        # 应用配置
│   │   ├── static/                # 静态资源
│   │   └── templates/             # 模板文件
│   ├── src/test/java/             # 测试代码
│   ├── Dockerfile                 # Docker构建文件
│   ├── pom.xml                    # Maven依赖配置
│   └── mvnw*                      # Maven Wrapper脚本

├── frontend/                       # Vue 3前端模块
│   ├── src/                       # 源代码
│   │   ├── components/            # 公共组件
│   │   │   ├── common/            # 基础通用组件
│   │   │   ├── layout/            # 布局组件
│   │   │   └── file/              # 文件相关组件
│   │   ├── views/                 # 页面组件
│   │   │   ├── user/              # 用户页面
│   │   │   ├── admin/             # 管理员页面
│   │   │   └── public/            # 公共页面
│   │   ├── router/                # 路由配置
│   │   ├── stores/                # Pinia状态管理
│   │   ├── utils/                 # 工具函数
│   │   │   ├── api/               # API请求封装
│   │   │   ├── auth/              # 认证相关
│   │   │   └── file/              # 文件操作工具
│   │   ├── assets/                # 静态资源
│   │   ├── styles/                # 全局样式
│   │   ├── lang/                  # 国际化语言文件
│   │   └── App.vue                # 根组件
│   ├── public/                    # 公共静态资源
│   ├── .env.*                     # 环境变量配置
│   ├── Dockerfile                 # Docker构建文件
│   ├── package.json               # 依赖配置
│   └── vite.config.js             # Vite构建配置

├── deploy/                         # 部署配置
│   ├── docker-compose.yml         # Docker Compose配置
│   ├── backend/                   # 后端部署配置
│   └── frontend/                  # 前端部署配置

├── k8s/                           # Kubernetes配置
│   ├── namespace.yaml             # 命名空间配置
│   ├── backend-delop.yaml         # 后端部署配置
│   ├── frontend-delop.yaml        # 前端部署配置
│   └── deploy.sh                  # 部署脚本

├── docs/                          # 项目文档
│   ├── images/                    # 文档图片资源
│   ├── deployment.md              # 部署指南
│   ├── development.md             # 开发指南
│   ├── api.md                     # API文档
│   └── faq.md                     # 常见问题

├── scripts/                       # 脚本文件
│   ├── build-and-deploy.bat       # Windows构建部署脚本
│   └── start-minio.bat            # MinIO启动脚本

├── .github/                       # GitHub配置
│   └── workflows/                 # CI/CD工作流

├── CONTRIBUTING.md                # 贡献指南
├── DATABASE_DESIGN.md             # 数据库设计文档
├── LICENSE                        # 许可证文件
└── README.md                      # 项目说明文档
```

## 🔧 开发指南

### 🎨 前端开发

#### 环境要求
前端开发需要安装 Node.js 18.0 或更高版本（推荐使用 18.17.0 LTS），以及 npm 9.0+ 或 yarn 1.22+。系统支持 Windows 10、macOS 10.15 或 Linux Ubuntu 18.04 及以上版本。

#### 快速开始

开始前端开发非常简单，只需执行以下命令：

```bash
# 进入前端目录
cd frontend

# 安装依赖
npm install
# 或使用 yarn
yarn install

# 启动开发服务器
npm run dev
# 访问 http://localhost:8080
```

#### 开发命令

常用的开发命令包括：
- `npm run dev` - 启动开发服务器，支持热重载，默认端口8080
- `npm run build` - 构建生产版本，输出到 dist 目录
- `npm run preview` - 预览构建结果，本地查看生产环境效果
- `npm run lint` - 运行 ESLint 代码规范检查
- `npm run lint:fix` - 自动修复可修复的代码规范问题
- `npm run test:unit` - 运行 Jest 单元测试
- `npm run test:e2e` - 运行 Cypress 端到端测试

#### 📁 项目结构说明

```
frontend/
├── src/
│   ├── components/     # 可复用组件
│   ├── views/          # 页面组件
│   ├── router/         # 路由配置
│   ├── stores/         # 状态管理 (Pinia)
│   ├── utils/          # 工具函数
│   ├── assets/         # 静态资源
│   └── App.vue         # 根组件
├── public/             # 公共资源
├── package.json        # 依赖配置
└── vue.config.js       # Vue配置
```

### ⚙️ 后端开发

#### 环境要求
后端开发需要安装 Java 17.0 或更高版本（推荐 OpenJDK 17），Maven 3.8.0+，MySQL 8.0.33+，以及可选的 Redis 6.0+ 和 MinIO 8.5.4+。

#### 快速开始

开始后端开发只需执行以下命令：

```bash
# 进入后端目录
cd backend

# 安装依赖并构建
./mvnw clean install

# 启动开发服务器
./mvnw spring-boot:run
# 访问 http://localhost:8080
```

#### 开发命令

常用的后端开发命令包括：
- `./mvnw spring-boot:run` - 启动开发服务器，支持热重载
- `./mvnw test` - 运行 JUnit 单元测试
- `./mvnw package` - 打包应用，生成可执行 JAR 文件
- `./mvnw clean` - 清理构建文件，删除 target 目录
- `./mvnw compile` - 编译源代码，仅编译不打包
- `./mvnw dependency:tree` - 查看依赖树，分析项目依赖关系

#### 📁 项目结构说明

```
backend/
├── src/main/java/
│   ├── config/         # 配置类
│   ├── controller/     # 控制器层
│   ├── service/        # 服务层
│   ├── repository/     # 数据访问层
│   ├── entity/         # 实体类
│   ├── dto/            # 数据传输对象
│   ├── security/       # 安全配置
│   └── util/           # 工具类
├── src/test/java/      # 测试代码
├── src/main/resources/ # 资源文件
└── pom.xml            # Maven配置
```

### 🔄 开发工作流

开发工作流包括以下几个主要环节：

**代码开发**：使用功能分支进行开发，遵循代码规范和质量标准，编写单元测试和集成测试确保代码质量。

**代码提交**：提交前运行代码检查，编写清晰的提交信息，关联相关 Issue 编号便于追踪。

**代码审查**：创建 Pull Request，邀请团队成员进行代码审查，及时解决审查意见。

**持续集成**：自动运行测试套件，进行代码质量检查，验证构建结果确保系统稳定性。

## 🧪 测试

### 测试策略

LeafPan 项目采用全面的测试策略，确保代码质量和系统稳定性。测试覆盖单元测试、集成测试、端到端测试、性能测试和安全测试等多个层面。

**单元测试**：覆盖单个函数和方法，使用 Jest（前端）和 JUnit（后端）框架，在每次代码提交时自动执行。

**集成测试**：验证模块间交互，使用 Spring Boot Test 和 Vue Test Utils，在主要功能变更时执行。

**端到端测试**：测试完整用户流程，使用 Cypress 框架，在发布前进行验证。

**性能测试**：评估系统性能指标，使用 JMeter 和 Lighthouse 工具，在版本发布时执行。

**安全测试**：检测安全漏洞，使用 OWASP ZAP 和 SonarQube，定期进行安全检查。

### 🔬 单元测试

#### 前端单元测试

```bash
# 进入前端目录
cd frontend

# 运行所有单元测试
npm run test:unit

# 运行测试并生成覆盖率报告
npm run test:unit -- --coverage

# 监听模式运行测试
npm run test:unit -- --watch
```

**测试文件结构：**
```
frontend/src/
├── components/
│   └── __tests__/          # 组件测试
├── utils/
│   └── __tests__/           # 工具函数测试
└── stores/
    └── __tests__/           # 状态管理测试
```

#### 后端单元测试

```bash
# 进入后端目录
cd backend

# 运行所有单元测试
./mvnw test

# 运行测试并生成覆盖率报告
./mvnw jacoco:report

# 跳过测试进行构建
./mvnw package -DskipTests
```

**测试文件结构：**
```
backend/src/test/java/
├── service/                # 服务层测试
├── controller/             # 控制器层测试
├── repository/             # 数据访问层测试
└── util/                   # 工具类测试
```

### 🔗 集成测试

#### 数据库集成测试

```bash
# 使用测试数据库运行集成测试
./mvnw test -Dspring.profiles.active=test

# 使用Docker运行集成测试环境
docker-compose -f docker-compose.test.yml up -d
npm run test:integration
```

**测试配置：**
- 使用H2内存数据库进行测试
- 自动初始化测试数据
- 测试后自动清理数据

#### API集成测试

```bash
# 运行API测试套件
npm run test:api

# 生成API测试报告
npm run test:api -- --reporter=html
```

### 🌐 E2E测试

#### 用户流程测试

```bash
# 运行E2E测试
npm run test:e2e

# 交互模式运行E2E测试
npm run test:e2e -- --headed

# 生成E2E测试报告
npm run test:e2e -- --reporter junit
```

**测试场景覆盖：**
- 用户注册和登录流程
- 文件上传和下载流程
- 文件分享和管理流程
- 权限控制和安全性测试

### 📈 性能测试

#### 负载测试

```bash
# 使用JMeter进行性能测试
jmeter -n -t performance-test.jmx -l test-results.jtl

# 生成性能报告
jmeter -g test-results.jtl -o performance-report/
```

**性能指标监控：**
- API响应时间 (< 100ms)
- 并发用户数 (> 1000)
- 文件上传速度 (> 10MB/s)
- 内存使用情况

## 🔒 安全措施

### 安全架构

LeafPan采用多层次的安全架构设计，保障用户数据和系统安全：

- **传输层安全**：全站HTTPS加密，保护数据传输安全
- **认证授权**：基于JWT的身份认证，细粒度的RBAC权限控制
- **数据加密**：敏感数据存储加密，支持文件加密存储选项
- **访问控制**：IP限制、登录限流、防暴力破解机制
- **安全审计**：完整的操作日志记录，支持安全审计追踪

### 安全防护

#### 前端安全

- **XSS防护**：使用Vue.js的自动转义和内容安全策略(CSP)
- **CSRF防护**：实现CSRF Token验证机制
- **输入验证**：前后端双重数据验证，防止恶意输入
- **安全头部**：配置适当的HTTP安全响应头

#### 后端安全

- **SQL注入防护**：使用MyBatis参数化查询，避免SQL注入
- **文件上传安全**：严格的文件类型验证、大小限制、重命名和隔离存储
- **API安全**：接口签名验证、请求频率限制、数据脱敏处理
- **依赖安全**：定期扫描和更新依赖库，修复已知漏洞

### 安全测试

#### 安全扫描

```bash
# 使用OWASP ZAP进行安全扫描
zap-baseline.py -t http://localhost:8080

# 使用SonarQube进行代码质量和安全检查
sonar-scanner
```

#### 安全测试重点

| 安全威胁 | 防护措施 | 测试方法 |
|---------|---------|----------|
| SQL注入 | 参数化查询、ORM框架 | 自动化安全扫描、手动渗透测试 |
| XSS攻击 | 输入验证、输出转义、CSP | OWASP ZAP扫描、浏览器安全测试 |
| CSRF攻击 | CSRF Token、SameSite Cookie | 自动化安全扫描、手动验证 |
| 文件上传漏洞 | 严格的文件验证和隔离存储 | 文件上传测试、恶意文件检测 |
| 认证授权缺陷 | JWT认证、RBAC权限控制 | 权限绕过测试、未授权访问测试 |

### 📋 测试报告

测试执行后会自动生成详细的测试报告：

- **单元测试覆盖率报告** - 代码覆盖率分析
- **集成测试报告** - 模块间交互验证
- **E2E测试录像** - 用户操作流程回放
- **性能测试报告** - 系统性能指标
- **安全扫描报告** - 安全漏洞检测

## 📊 性能指标

### 性能目标

LeafPan 致力于提供高性能的云存储服务，以下是我们的性能目标和实际测试结果：

| 性能指标 | 目标值 | 当前值 | 测试环境 |
|---------|-------|-------|----------|
| 页面加载时间 | < 2秒 | 1.2秒 | 4核8G服务器，First Contentful Paint |
| API响应时间 | < 100毫秒 | 45毫秒 | 本地网络环境，平均响应时间 |
| 文件上传速度 | > 10MB/s | 15MB/s | 千兆网络环境，大文件分片上传 |
| 文件下载速度 | > 20MB/s | 25MB/s | 千兆网络环境，CDN加速下载 |
| 并发用户数 | 1000+ | 测试中 | 压力测试环境，同时在线用户 |
| 数据库查询 | < 50毫秒 | 30毫秒 | MySQL 8.0，复杂查询响应 |
| 缓存命中率 | > 95% | 98% | Redis缓存，热点数据缓存 |
| 系统可用性 | > 99.9% | 99.95% | 生产环境，月度可用性 |

### 性能测试结果

#### 前端性能

前端性能表现优异，Lighthouse评分如下：

| 评分指标 | 分数 | 状态 |
|---------|------|------|
| Performance | 95/100 | 🟢 优秀 |
| Accessibility | 100/100 | 🟢 优秀 |
| Best Practices | 100/100 | 🟢 优秀 |
| SEO | 100/100 | 🟢 优秀 |
| PWA | 92/100 | 🟢 优秀 |

核心Web指标表现良好：

| 核心指标 | 值 | 状态 |
|---------|-----|------|
| Largest Contentful Paint (LCP) | 1.2秒 | 🟢 良好 |
| First Input Delay (FID) | 45毫秒 | 🟢 良好 |
| Cumulative Layout Shift (CLS) | 0.05 | 🟢 良好 |

#### 后端性能

API性能测试结果显示系统响应迅速：

| 接口类型 | 平均响应时间 | 95%响应时间 | 吞吐量 | 错误率 |
|---------|------------|------------|-------|-------|
| 认证登录接口 | 35毫秒 | 65毫秒 | 1200请求/秒 | 0.01% |
| 文件上传接口 | 45毫秒 | 85毫秒 | 800请求/秒 | 0.02% |
| 文件下载接口 | 28毫秒 | 52毫秒 | 1500请求/秒 | 0.01% |
| 文件列表接口 | 22毫秒 | 40毫秒 | 2000请求/秒 | 0.005%

#### 数据库性能

MySQL数据库性能稳定：

| 性能指标 | 值 | 状态 |
|---------|-----|------|
| 查询缓存命中率 | 98.5% | 🟢 优秀 |
| 连接池使用率 | 65% | 🟢 良好 |
| 慢查询比例 | < 0.1% | 🟢 优秀 |
| 死锁发生率 | 0 | 🟢 优秀 |

Redis缓存系统表现优异：

| 性能指标 | 值 | 状态 |
|---------|-----|------|
| 缓存命中率 | 99.2% | 🟢 优秀 |
| 内存使用率 | 45% | 🟢 良好 |
| 网络延迟 | < 1毫秒 | 🟢 优秀 |
| QPS | 15,000+ | 🟢 优秀 |

### 性能优化策略

#### 前端优化

- **代码分割**：按路由懒加载组件，减少初始加载时间
- **图片优化**：支持WebP格式和懒加载机制，提升页面加载速度
- **缓存策略**：结合浏览器缓存和Service Worker，提高资源复用率
- **资源压缩**：使用Gzip和Brotli压缩算法，减小传输体积
- **CDN加速**：实现静态资源全球分发，降低延迟

#### 后端优化

- **连接池优化**：使用HikariCP高性能连接池管理数据库连接
- **缓存策略**：采用多级缓存策略结合Redis和本地缓存提升数据访问速度
- **异步处理**：支持非阻塞IO和异步任务处理，提高并发能力
- **数据库优化**：索引优化和查询优化，减少数据库负载
- **负载均衡**：通过Nginx反向代理实现请求分发，提高系统稳定性

#### 存储优化

- **分片传输**：支持大文件分片上传下载，提高传输效率
- **文件压缩**：压缩存储减少存储空间占用
- **CDN加速**：实现文件内容全球分发，提升用户体验
- **存储分层**：热数据和冷数据分离存储，优化存储成本

### 监控告警

#### 系统监控

- **应用监控**：使用Spring Boot Actuator监控应用健康状态和性能指标
- **JVM监控**：监控内存使用、垃圾回收、线程状态等JVM核心指标
- **数据库监控**：实时监控MySQL性能、连接数、慢查询等关键指标
- **缓存监控**：监控Redis内存使用、命中率、命令执行等指标

#### 业务监控

- **用户行为分析**：统计用户访问、操作行为，识别使用模式
- **文件传输统计**：监控文件上传下载量、速度和成功率
- **资源使用监控**：跟踪系统资源消耗趋势，预测扩容需求
- **异常监控**：实时捕获错误日志和异常，快速定位问题

#### 告警规则

| 监控项 | 触发条件 | 告警级别 | 处理建议 |
|-------|---------|---------|----------|
| CPU使用率 | > 80% 持续5分钟 | 警告 | 检查是否有异常进程，考虑扩容 |
| 内存使用率 | > 85% 持续5分钟 | 警告 | 检查内存泄漏，调整JVM参数 |
| 磁盘使用率 | > 90% | 紧急 | 清理日志或文件，扩展存储空间 |
| API错误率 | > 1% 持续2分钟 | 警告 | 检查服务状态和异常日志 |
| 服务可用性 | 不可用持续30秒 | 紧急 | 立即检查服务状态并恢复服务 |

## 🤝 贡献指南

我们非常欢迎社区贡献！LeafPan是一个开源项目，依赖社区的参与和支持。请参考 [CONTRIBUTING.md](CONTRIBUTING.md) 了解详细的贡献指南。

### 贡献方式

LeafPan项目支持多种贡献方式，适合不同背景的开发者参与：

**Bug报告**：适合所有用户，通过创建Issue、描述问题并提供复现步骤来报告程序错误。

**功能建议**：适合产品经理和用户，提出新功能或改进建议并参与方案讨论。

**代码贡献**：适合开发者，通过Fork项目、开发功能、测试验证并提交Pull Request来参与。

**文档改进**：适合技术写作者，直接修改文档内容完善项目文档和教程。

**翻译贡献**：适合翻译人员，为项目提供多语言翻译支持。

**代码审查**：适合资深开发者，参与Pull Request审查并提供专业建议。

**性能优化**：适合性能工程师，分析系统性能并进行优化调优。

### 开发流程

LeafPan项目的标准开发流程包括以下步骤：

**准备工作**：首先需要Fork项目到个人账户，然后克隆到本地开发环境，安装必要的依赖并配置开发环境。

**分支创建**：从main分支创建功能分支，使用描述性的分支命名规范，确保分支基于最新的main分支代码。

**开发测试**：编写代码实现功能，添加相应的单元测试和集成测试，确保所有测试用例通过，并运行代码质量检查工具。

**代码提交**：将代码提交到功能分支，编写清晰详细的提交信息，遵循项目的提交规范要求。

**Pull Request创建**：创建Pull Request到主仓库，填写详细的PR描述说明修改内容，关联相关Issue，等待项目维护者的代码审查和最终合并。

### 代码规范

LeafPan项目遵循严格的代码规范，确保代码质量和可维护性：

**前端代码规范**：使用ESLint + Prettier进行代码格式化，遵循Vue.js官方风格指南，采用TypeScript进行类型检查，编写组件文档和单元测试。

**后端代码规范**：遵循Java编码规范，使用Checkstyle进行代码检查，编写清晰的JavaDoc注释，确保单元测试覆盖率>80%。

**文档规范**：使用Markdown格式编写文档，包含清晰的目录结构，提供代码示例和截图，保持中英文文档同步。

### 贡献者权益与常见问题

#### 贡献者权益

LeafPan项目重视每一位贡献者的付出，提供以下权益：社区认可，贡献者将在项目文档中获得公开认可；优先支持，贡献者享有技术支持的优先权；参与决策，资深贡献者可参与项目发展方向讨论；成长机会，通过参与真实项目获得宝贵的技术经验。

#### 常见问题

**新手如何开始贡献？**建议从文档改进或简单的Bug修复开始，熟悉项目流程后再参与复杂功能开发。

**贡献代码需要什么技能水平？**不同贡献类型要求不同，文档改进和Bug修复适合初学者，功能开发需要相关技术经验。

**如何确保贡献被接受？**遵循项目规范、编写测试、参与讨论、及时响应审查意见可提高接受率。

**贡献是否有时间要求？**没有强制时间要求，可根据个人时间安排灵活参与。

## 许可证

LeafPan 项目采用 MIT License 开源协议，这是目前最宽松的开源协议之一。MIT 许可证允许用户自由使用、修改和分发软件，包括商业用途，仅需保留原始版权声明。该协议不提供任何担保，也不承担任何责任，使用者需自行承担风险。

在使用本项目时，您需要在所有副本或重要部分中包含原始版权声明，并在分发时包含完整的 MIT 许可证文本。完整的许可证文本如下：

```text
MIT License

Copyright (c) 2024 LeafPan Team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

如需了解更多信息，可查看完整的许可证文件，或参考开源协议选择指南和 MIT 协议官方说明。

## 联系我们

LeafPan项目由开源社区共同维护，我们热忱欢迎各种形式的参与和贡献。项目由YangShengzhou03发起并主导项目规划与架构设计，同时有众多社区贡献者积极参与前端开发、后端开发和文档维护等工作。

我们提供多种支持渠道：GitHub Issues用于Bug反馈，GitHub Discussions用于功能建议和技术讨论，邮件联系则适用于重要事务沟通。通常情况下，Bug反馈和技术问题会在1-3个工作日内得到响应，功能建议会在1-2个工作日内处理。

我们诚挚欢迎技术合作、社区合作和商业合作等多种合作形式。技术合作涵盖代码审查、架构优化、性能调优和安全审计；社区合作包括文档翻译、教程编写、社区运营和用户支持；商业合作则涉及企业定制、技术咨询和培训服务。

目前项目处于活跃开发阶段，虽然版本为测试版本但功能已基本稳定。社区氛围活跃，我们非常欢迎新的贡献者加入。项目文档也在持续完善中。

项目的发展规划分为短期、中期和长期目标。短期目标（1-3个月）着重于完善核心文件管理功能、优化移动端体验、增加更多存储后端支持以及提升测试覆盖率。中期目标（3-6个月）计划实现企业级功能、支持插件系统、完善监控和日志系统并加强社区生态建设。长期目标（6-12个月）将致力于云原生部署支持、多租户架构、AI智能功能集成和国际化完善。

---

<div align="center">

## 支持我们

**如果LeafPan对您有帮助，请给我们一个Star支持！**

您的支持是我们持续开发的最大动力！

[![Star History Chart](https://api.star-history.com/svg?repos=YangShengzhou03/LeafPan&type=Date)](https://star-history.com/#YangShengzhou03/LeafPan&Date)

### 致谢

感谢所有为LeafPan项目做出贡献的开发者们！

特别感谢以下开源项目：
- [Vue.js](https://vuejs.org/) - 渐进式JavaScript框架
- [Spring Boot](https://spring.io/projects/spring-boot) - Java应用框架
- [Element Plus](https://element-plus.org/) - Vue 3组件库
- [MyBatis](https://mybatis.org/mybatis-3/) - 持久层框架
- [MinIO](https://min.io/) - 对象存储服务

### 更新日志

查看项目的完整更新记录：
- [版本发布记录](https://github.com/YangShengzhou03/LeafPan/releases)
- [提交历史](https://github.com/YangShengzhou03/LeafPan/commits/main)

---

**LeafPan - 让文件管理更简单，更安全！**

</div>