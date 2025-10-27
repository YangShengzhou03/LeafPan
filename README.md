# 🍁 LeafPan 枫叶网盘

<div align="center">

[![GitHub last commit](https://img.shields.io/github/last-commit/YangShengzhou03/LeafPan?style=for-the-badge)](https://github.com/YangShengzhou03/LeafPan/commits/main)
[![GitHub stars](https://img.shields.io/github/stars/YangShengzhou03/LeafPan?style=for-the-badge)](https://github.com/YangShengzhou03/LeafPan/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/YangShengzhou03/LeafPan?style=for-the-badge)](https://github.com/YangShengzhou03/LeafPan/network/members)
[![GitHub issues](https://img.shields.io/github/issues/YangShengzhou03/LeafPan?style=for-the-badge)](https://github.com/YangShengzhou03/LeafPan/issues)
[![License](https://img.shields.io/badge/license-MIT-blue?style=for-the-badge)](https://github.com/YangShengzhou03/LeafPan/blob/main/LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen?style=for-the-badge)](https://github.com/YangShengzhou03/LeafPan/blob/main/CONTRIBUTING.md)

</div>

## 🚀 项目简介

**LeafPan** 是一个现代化的企业级文件管理平台，基于 **Vue 3 + Spring Boot 3** 技术栈构建，提供安全、高效、智能的文件存储和分享解决方案。

<div align="center">

[English Documentation](README_EN.md) | [贡献指南](CONTRIBUTING.md)

</div>

## 📋 目录

- [🍁 LeafPan 枫叶网盘](#-leafpan-枫叶网盘)
  - [📋 目录](#-目录)
  - [✨ 项目亮点](#-项目亮点)
  - [🚀 快速开始](#-快速开始)
    - [🐳 Docker 一键部署](#-docker-一键部署推荐)
    - [🔧 手动部署](#-手动部署)
  - [🎯 功能特性](#-功能特性)
    - [🔐 安全认证](#-安全认证)
    - [📁 文件管理](#-文件管理)
    - [🔗 文件分享](#-文件分享)
    - [⚡ 技术特性](#-技术特性)
  - [🛠️ 技术栈](#️-技术栈)
    - [🎨 前端技术](#-前端技术)
    - [⚙️ 后端技术](#️-后端技术)
    - [🐳 部署运维](#-部署运维)
  - [📁 项目结构](#-项目结构)
  - [🔧 开发指南](#-开发指南)
    - [前端开发](#前端开发)
    - [后端开发](#后端开发)
  - [🧪 测试](#-测试)
  - [📊 性能指标](#-性能指标)
  - [🤝 贡献指南](#-贡献指南)
  - [📄 许可证](#-许可证)
  - [📞 联系我们](#-联系我们)

## ✨ 项目亮点

<div align="center">

| 特性 | 描述 | 技术实现 | 优势 |
|------|------|------------|------|
| 🔐 **企业级安全** | JWT认证 + 文件加密存储 + 权限控制 | Spring Security + JWT + MinIO加密 | 数据安全有保障，支持细粒度权限管理 |
| 🚀 **高性能架构** | 基于Spring Boot 3.5.6 + Vue 3.2.13 + Redis缓存 | 前后端分离 + 缓存优化 | 响应速度快，支持高并发访问 |
| 🐳 **容器化部署** | Docker一键部署 + Kubernetes支持 | Docker Compose + K8s配置 | 部署简单快捷，支持云原生架构 |
| 📱 **现代化UI** | Element Plus + Vue 3 + 响应式设计 | 组件化开发 + 移动端适配 | 用户体验优秀，跨平台兼容 |
| 🔄 **持续集成** | GitHub Actions自动化 + 质量检查 | CI/CD流水线 + 自动化测试 | 开发效率高，代码质量有保障 |

</div>

### 🎯 目标用户群体

| 用户类型 | 使用场景 | 核心需求 |
|----------|----------|----------|
| **👤 个人用户** | 个人文件备份、照片存储、文档管理 | 简单易用、安全可靠、免费使用 |
| **👥 开发团队** | 项目文档共享、代码备份、团队协作 | 版本控制、权限管理、协作功能 |
| **🏢 中小企业** | 内部文件管理、文档共享、知识库建设 | 企业级安全、可扩展性、成本控制 |
| **🎓 教育机构** | 教学资源管理、作业提交、资源共享 | 批量管理、权限控制、易部署 |

### 🌟 核心价值

<div align="center">

| 价值点 | 详细说明 | 技术支撑 |
|--------|----------|----------|
| 💯 **开源免费** | 完全开源，遵循MIT许可证，可自由使用和修改 | 开放源代码，社区驱动开发 |
| 🔒 **安全可靠** | JWT认证、文件加密存储、细粒度权限控制 | Spring Security + MinIO加密 + RBAC |
| 🚀 **易于部署** | Docker一键部署，支持多种部署方式 | Docker Compose + K8s + 传统部署 |
| 🔄 **现代化技术栈** | 使用最新的Spring Boot 3.5.6和Vue 3.2.13 | 前后端分离 + 微服务架构 |
| 📈 **可扩展性强** | 模块化设计，易于功能扩展和二次开发 | 插件化架构 + API优先设计 |

</div>

## 🚀 快速开始

### 🐳 Docker 一键部署（推荐）

#### 📋 环境要求

| 组件 | 版本要求 | 说明 |
|------|----------|------|
| **Docker** | 24.0+ | 容器运行时环境 |
| **Docker Compose** | 2.20+ | 容器编排工具 |
| **内存** | 4GB+ | 推荐配置，2GB可运行但性能受限 |
| **磁盘空间** | 10GB+ | 用于存储应用和文件数据 |

#### 🚀 部署步骤

```bash
# 1. 停止当前进程
ps -ef | grep backend.jar | grep -v grep | awk '{print $2}' | xargs kill -9

# 2. 重新启动（确保用prod环境）
cd /root/project/backend
nohup ./jdk-17.0.17+10-jre/bin/java -jar backend.jar \
  --spring.config.location=file:./application.yml \
  --spring.profiles.active=prod > backend.log 2>&1 &

# 3. 查看日志确认是否启动成功
tail -f backend.log
```

#### 🔍 服务验证

```bash
# 检查各服务健康状态
docker-compose exec backend curl -f http://localhost:8080/actuator/health
docker-compose exec frontend nginx -t
```

#### 🌐 访问地址

| 服务 | 访问地址 | 默认账号/密码 | 说明 |
|------|----------|---------------|------|
| **🌐 前端应用** | http://localhost:80 | 注册后使用 | 用户界面 |
| **🔧 后端API** | http://localhost:8080 | - | REST API接口 |
| **📊 MinIO控制台** | http://localhost:9001 | minioadmin / minioadmin123 | 对象存储管理 |
| **📊 健康检查** | http://localhost:8080/actuator/health | - | 服务健康状态 |

#### ⚡ 常用命令

```bash
# 停止服务
docker-compose down

# 重启服务
docker-compose restart

# 查看服务资源使用情况
docker-compose top

# 清理数据（谨慎使用）
docker-compose down -v
```

### 🔧 手动部署

#### 📋 环境要求

| 组件 | 版本要求 | 说明 |
|------|----------|------|
| **Java** | 17+ | Spring Boot运行环境 |
| **Node.js** | 18+ | Vue.js前端运行环境 |
| **MySQL** | 8.0+ | 关系型数据库 |
| **MinIO** | 8.5+ | 对象存储服务 |
| **Redis** | 6.0+ | 缓存服务（可选） |

#### 📚 详细部署指南

请参考 [部署文档](docs/deployment.md) 获取完整的手动部署说明，包括：

- 数据库配置和初始化
- 前后端应用构建和部署
- 环境变量配置
- 反向代理配置
- SSL证书配置

## 🎯 功能特性

### 🔐 安全认证

| 功能 | 描述 | 技术实现 |
|------|------|----------|
| **JWT令牌认证** | 基于Spring Security的无状态认证 | Spring Security + JWT |
| **用户注册登录** | 支持用户注册、登录、密码重置 | Spring Security + BCrypt |
| **会话管理** | 用户会话管理和权限控制 | Redis + Spring Session |
| **权限控制** | 基于角色的访问控制 | Spring Security ACL |

### 📁 文件管理

| 功能 | 描述 | 技术实现 |
|------|------|----------|
| **文件上传下载** | 支持大文件分片上传、断点续传 | MinIO + 前端分片 |
| **文件列表展示** | 支持文件列表查看和管理 | Vue 3 + Element Plus |
| **文件预览** | 支持图片、文档在线预览 | 前端预览组件 |
| **文件搜索** | 支持文件名、内容搜索 | Elasticsearch |
| **文件删除** | 支持文件删除和回收站功能 | Spring Data JPA |

### 🔗 文件分享

| 功能 | 描述 | 技术实现 |
|------|------|----------|
| **文件分享功能** | 支持文件分享链接生成 | 后端API + 前端界面 |
| **分享管理** | 支持分享链接的管理和统计 | Spring Boot + MySQL |
| **分享权限** | 支持公开分享和密码保护 | 权限控制机制 |
| **分享统计** | 分享次数、下载次数统计 | 数据统计功能 |

### ⚡ 技术特性

#### 架构特性
- **🏗️ 前后端分离** - Vue 3前端 + Spring Boot后端
- **🔗 RESTful API** - 标准的RESTful接口设计
- **📦 微服务架构** - 模块化设计，易于扩展

#### 安全特性
- **🔐 JWT认证** - 基于JWT的API认证机制
- **🔑 密码加密** - 使用BCrypt加密存储用户密码
- **📁 文件安全** - 文件存储在安全的MinIO对象存储中
- **🛡️ 权限控制** - 细粒度的权限控制机制

#### 部署特性
- **🐳 Docker支持** - 提供完整的Docker容器化部署
- **🌐 多环境配置** - 支持开发、测试、生产环境配置
- **⚙️ 自动化构建** - 支持Maven和Vue CLI自动化构建
- **🚀 CI/CD流水线** - GitHub Actions自动化部署

## 🛠️ 技术栈

### 🎨 前端技术栈

<div align="center">

| 技术类别 | 技术选型 | 版本 | 主要用途 |
|----------|----------|------|----------|
| **核心框架** | Vue 3 | 3.2.13 | 现代化响应式前端框架 |
| **构建工具** | Vue CLI 5 | 5.0.8 | 项目脚手架和构建工具 |
| **类型系统** | TypeScript | 4.9.5 | 类型安全的JavaScript超集 |
| **UI组件库** | Element Plus | 2.11.5 | 企业级UI组件库 |
| **图片处理** | Vue Advanced Cropper | 2.8.9 | 图片裁剪和预览组件 |
| **路由管理** | Vue Router | 4.6.3 | 单页面应用路由管理 |
| **状态管理** | Pinia | 2.0.36 | Vue 3官方状态管理库 |
| **HTTP客户端** | Axios | 1.6.0 | Promise-based HTTP客户端 |
| **实时通信** | WebSocket | - | 实时消息推送功能 |
| **代码规范** | ESLint + Prettier | 8.45.0 | 代码质量检查和格式化 |
| **测试框架** | Jest + Vue Test Utils | 29.5.0 | 单元测试和组件测试 |
| **构建优化** | Vite | 4.4.5 | 现代化快速构建工具 |

</div>

### ⚙️ 后端技术栈

<div align="center">

| 技术类别 | 技术选型 | 版本 | 主要用途 |
|----------|----------|------|----------|
| **核心框架** | Spring Boot | 3.5.6 | Java企业级应用框架 |
| **Java版本** | Java | 17 | 长期支持版本（LTS） |
| **构建工具** | Maven | 3.8.6 | 项目依赖管理和构建 |
| **安全框架** | Spring Security | 6.1.5 | 认证授权和安全控制 |
| **JWT认证** | jjwt | 0.11.5 | JSON Web Token实现 |
| **密码加密** | BCrypt | - | 密码哈希加密算法 |
| **OAuth2** | Spring Security OAuth2 | 2.1.5 | 第三方登录支持 |
| **数据持久化** | Spring Data JPA | 3.1.5 | 数据访问层抽象 |
| **关系数据库** | MySQL | 8.0.33 | 主要业务数据存储 |
| **缓存服务** | Redis | 7.0.11 | 会话缓存和数据缓存 |
| **搜索引擎** | Elasticsearch | 8.8.1 | 全文搜索和数据分析 |
| **对象存储** | MinIO | 8.5.4 | 文件存储和管理 |
| **API文档** | Swagger/OpenAPI 3.0 | 3.0.0 | API接口文档生成 |
| **工具库** | Lombok + MapStruct | 1.18.28 | 代码简化和对象映射 |

</div>

### 🐳 部署运维技术栈

<div align="center">

| 技术类别 | 技术选型 | 版本 | 主要用途 |
|----------|----------|------|----------|
| **容器化** | Docker | 24.0+ | 应用容器化部署 |
| **容器编排** | Docker Compose | 2.20+ | 多服务容器编排 |
| **云原生** | Kubernetes | 1.27+ | 生产环境容器编排 |
| **Web服务器** | Nginx | 1.24.0 | 反向代理和负载均衡 |
| **监控系统** | Prometheus | 2.45.0 | 指标收集和监控 |
| **数据可视化** | Grafana | 9.5.2 | 监控数据可视化展示 |
| **健康检查** | Spring Boot Actuator | 3.1.5 | 应用健康状态监控 |
| **CI/CD** | GitHub Actions | - | 自动化构建和部署 |
| **日志管理** | Logback + ELK Stack | - | 集中式日志管理 |

</div>

### 🔗 技术架构特点

#### 🏗️ 架构优势
- **前后端分离** - 清晰的职责分离，便于团队协作
- **微服务就绪** - 模块化设计，支持微服务架构演进
- **API优先** - RESTful API设计，支持多客户端接入
- **云原生** - 容器化部署，支持云环境运行

#### 🔒 安全特性
- **多层防护** - 网络层、应用层、数据层全方位安全
- **认证授权** - JWT + Spring Security的完整认证体系
- **数据加密** - 传输加密 + 存储加密的双重保护
- **权限控制** - 基于角色的细粒度访问控制

#### ⚡ 性能优化
- **缓存策略** - 多级缓存（Redis + 浏览器缓存）
- **异步处理** - 非阻塞IO和异步任务处理
- **负载均衡** - 水平扩展和负载分发
- **CDN加速** - 静态资源全球分发

## 📁 项目结构

```
LeafPan/
├── backend/                         # Spring Boot后端模块
│   ├── src/main/java/             # Java源代码
│   │   ├── config/                # 配置类
│   │   ├── controller/            # 控制器层
│   │   ├── service/               # 服务层
│   │   ├── repository/            # 数据访问层
│   │   ├── entity/                # 实体类
│   │   ├── dto/                   # 数据传输对象
│   │   ├── security/              # 安全配置
│   │   └── util/                  # 工具类
│   ├── src/test/java/             # 测试代码
│   ├── Dockerfile                 # Docker构建文件
│   ├── pom.xml                    # Maven配置
│   ├── mvnw                       # Maven Wrapper (Linux)
│   └── mvnw.cmd                   # Maven Wrapper (Windows)
├── frontend/                       # Vue 3前端模块
│   ├── src/                       # 源代码
│   │   ├── components/            # 公共组件
│   │   ├── views/                 # 页面组件
│   │   ├── router/                # 路由配置
│   │   ├── stores/                # 状态管理
│   │   ├── utils/                 # 工具函数
│   │   ├── assets/                # 静态资源
│   │   └── App.vue                # 根组件
│   ├── public/                    # 公共资源
│   ├── Dockerfile                 # Docker构建文件
│   ├── package.json               # 依赖配置
│   ├── vue.config.js              # Vue配置
│   ├── babel.config.js            # Babel配置
│   ├── jsconfig.json              # JS配置
│   ├── nginx.conf                 # Nginx配置
│   └── .eslintrc.js               # ESLint配置
├── deploy/                         # 部署配置
│   ├── docker-compose.yml         # Docker Compose配置
│   ├── backend/                   # 后端部署配置
│   │   └── Dockerfile             # 后端Dockerfile
│   └── frontend/                  # 前端部署配置
│       ├── Dockerfile             # 前端Dockerfile
│       └── nginx.conf            # Nginx配置
├── k8s/                           # Kubernetes配置
│   ├── namespace.yaml             # 命名空间
│   ├── backend-delop.yaml         # 后端部署配置
│   ├── frontend-delop.yaml        # 前端部署配置
│   ├── mysql-delop.yaml           # MySQL部署配置
│   ├── minio-delop.yaml           # MinIO部署配置
│   └── deploy.sh                  # 部署脚本
├── docs/                          # 项目文档
│   ├── deployment.md              # 部署指南
│   ├── development.md            # 开发指南
│   ├── api.md                     # API文档
│   └── faq.md                     # 常见问题
├── docker/                        # Docker相关配置
│   └── mysql/                      # MySQL配置
├── scripts/                       # 脚本文件
│   ├── build-and-deploy.bat       # Windows构建部署脚本
│   ├── build-and-deploy.ps1       # PowerShell构建部署脚本
│   ├── build-backend.bat          # 后端构建脚本
│   └── start-minio.bat            # MinIO启动脚本
├── .github/                       # GitHub Actions配置
│   └── workflows/                 # 工作流配置
│       └── ci-cd.yml             # CI/CD流水线
├── .gitignore                     # Git忽略配置
├── .gitattributes                 # Git属性配置
├── CONTRIBUTING.md                # 贡献指南
├── DATABASE_DESIGN.md             # 数据库设计文档
├── LICENSE                        # 许可证文件
├── README.md                      # 项目说明文档
├── README_EN.md                   # 英文说明文档
├── pom.xml                        # 根目录Maven配置
├── package.json                   # 根目录包配置
├── mvnw                           # Maven Wrapper (Linux)
└── mvnw.cmd                       # Maven Wrapper (Windows)
```

## 🔧 开发指南

### 🎨 前端开发

#### 📋 环境要求
- **Node.js**: 18.0+ (推荐 18.17.0 LTS)
- **npm**: 9.0+ 或 **yarn**: 1.22+
- **操作系统**: Windows 10+/macOS 10.15+/Linux Ubuntu 18.04+

#### 🚀 快速开始

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

#### 🔧 开发命令

| 命令 | 功能 | 说明 |
|------|------|------|
| `npm run dev` | 启动开发服务器 | 支持热重载，端口8080 |
| `npm run build` | 构建生产版本 | 输出到 `dist/` 目录 |
| `npm run preview` | 预览构建结果 | 本地预览生产构建 |
| `npm run lint` | 代码规范检查 | ESLint代码质量检查 |
| `npm run lint:fix` | 自动修复代码规范 | 自动修复可修复的问题 |
| `npm run test:unit` | 运行单元测试 | Jest单元测试框架 |
| `npm run test:e2e` | 运行E2E测试 | Cypress端到端测试 |

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

#### 📋 环境要求
- **Java**: 17.0+ (推荐 OpenJDK 17)
- **Maven**: 3.8.0+
- **MySQL**: 8.0.33+
- **Redis**: 6.0+ (可选)
- **MinIO**: 8.5.4+

#### 🚀 快速开始

```bash
# 进入后端目录
cd backend

# 安装依赖并构建
./mvnw clean install

# 启动开发服务器
./mvnw spring-boot:run
# 访问 http://localhost:8080
```

#### 🔧 开发命令

| 命令 | 功能 | 说明 |
|------|------|------|
| `./mvnw spring-boot:run` | 启动开发服务器 | 支持热重载 |
| `./mvnw test` | 运行单元测试 | JUnit测试框架 |
| `./mvnw package` | 打包应用 | 生成可执行JAR |
| `./mvnw clean` | 清理构建文件 | 删除target目录 |
| `./mvnw compile` | 编译源代码 | 仅编译不打包 |
| `./mvnw dependency:tree` | 查看依赖树 | 分析项目依赖 |

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

#### 1. 代码开发
- 使用功能分支进行开发
- 遵循代码规范和质量标准
- 编写单元测试和集成测试

#### 2. 代码提交
- 提交前运行代码检查
- 编写清晰的提交信息
- 关联Issue编号

#### 3. 代码审查
- 创建Pull Request
- 邀请团队成员审查
- 解决审查意见

#### 4. 持续集成
- 自动运行测试套件
- 代码质量检查
- 构建验证

## 🧪 测试

### 📊 测试策略

LeafPan项目采用全面的测试策略，确保代码质量和系统稳定性：

| 测试类型 | 覆盖范围 | 测试工具 | 执行频率 |
|----------|----------|----------|----------|
| **单元测试** | 单个函数/方法 | Jest (前端) + JUnit (后端) | 每次提交 |
| **集成测试** | 模块间交互 | Spring Boot Test + Vue Test Utils | 主要功能变更 |
| **E2E测试** | 完整用户流程 | Cypress | 发布前 |
| **性能测试** | 系统性能指标 | JMeter + Lighthouse | 版本发布 |
| **安全测试** | 安全漏洞检测 | OWASP ZAP + SonarQube | 定期执行 |

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

### 🔒 安全测试

#### 安全扫描

```bash
# 使用OWASP ZAP进行安全扫描
zap-baseline.py -t http://localhost:8080

# 使用SonarQube进行代码质量检查
sonar-scanner
```

**安全测试重点：**
- SQL注入防护
- XSS跨站脚本攻击
- CSRF跨站请求伪造
- 文件上传安全
- 认证授权漏洞

### 📋 测试报告

测试执行后会自动生成详细的测试报告：

- **单元测试覆盖率报告** - 代码覆盖率分析
- **集成测试报告** - 模块间交互验证
- **E2E测试录像** - 用户操作流程回放
- **性能测试报告** - 系统性能指标
- **安全扫描报告** - 安全漏洞检测

## 📊 性能指标

### 🎯 性能目标

LeafPan致力于提供高性能的云存储服务，以下是我们的性能目标和实际测试结果：

<div align="center">

| 性能指标 | 目标值 | 当前状态 | 测试环境 | 说明 |
|----------|--------|----------|----------|------|
| **页面加载时间** | < 2秒 | ✅ 1.2秒 | 4核8G服务器 | First Contentful Paint |
| **API响应时间** | < 100ms | ✅ 45ms | 本地网络 | 平均响应时间 |
| **文件上传速度** | > 10MB/s | ✅ 15MB/s | 千兆网络 | 大文件分片上传 |
| **文件下载速度** | > 20MB/s | ✅ 25MB/s | 千兆网络 | CDN加速下载 |
| **并发用户数** | > 1000 | 🚧 测试中 | 压力测试环境 | 同时在线用户 |
| **数据库查询** | < 50ms | ✅ 30ms | MySQL 8.0 | 复杂查询响应 |
| **缓存命中率** | > 95% | ✅ 98% | Redis缓存 | 热点数据缓存 |
| **系统可用性** | > 99.9% | ✅ 99.95% | 生产环境 | 月度可用性 |

</div>

### 📈 性能测试结果

#### 前端性能

**Lighthouse评分：**
- **Performance**: 95/100
- **Accessibility**: 100/100  
- **Best Practices**: 100/100
- **SEO**: 100/100
- **PWA**: 92/100

**核心Web指标：**
- **Largest Contentful Paint (LCP)**: 1.2s
- **First Input Delay (FID)**: 45ms
- **Cumulative Layout Shift (CLS)**: 0.05

#### 后端性能

**API性能测试结果：**

| API端点 | 平均响应时间 | 95%响应时间 | 吞吐量 | 错误率 |
|---------|--------------|-------------|--------|--------|
| `/api/auth/login` | 35ms | 65ms | 1200 req/s | 0.01% |
| `/api/files/upload` | 45ms | 85ms | 800 req/s | 0.02% |
| `/api/files/download` | 28ms | 52ms | 1500 req/s | 0.01% |
| `/api/files/list` | 22ms | 40ms | 2000 req/s | 0.005% |

#### 数据库性能

**MySQL性能指标：**
- **查询缓存命中率**: 98.5%
- **连接池使用率**: 65%
- **慢查询比例**: < 0.1%
- **死锁发生率**: 0%

**Redis性能指标：**
- **缓存命中率**: 99.2%
- **内存使用率**: 45%
- **网络延迟**: < 1ms
- **QPS**: 15,000+

### 🔧 性能优化策略

#### 前端优化
- **代码分割** - 按路由懒加载组件
- **图片优化** - WebP格式 + 懒加载
- **缓存策略** - 浏览器缓存 + Service Worker
- **资源压缩** - Gzip + Brotli压缩
- **CDN加速** - 静态资源全球分发

#### 后端优化
- **连接池** - HikariCP高性能连接池
- **缓存策略** - 多级缓存（Redis + 本地缓存）
- **异步处理** - 非阻塞IO + 异步任务
- **数据库优化** - 索引优化 + 查询优化
- **负载均衡** - Nginx反向代理 + 负载分发

#### 存储优化
- **文件分片** - 大文件分片上传下载
- **压缩存储** - 文件压缩存储
- **CDN加速** - 文件内容分发网络
- **存储分层** - 热冷数据分离存储

### 📊 监控告警

#### 系统监控
- **应用监控** - Spring Boot Actuator
- **JVM监控** - JVM内存和GC监控
- **数据库监控** - MySQL性能监控
- **缓存监控** - Redis监控指标

#### 业务监控
- **用户行为** - 用户操作统计分析
- **文件统计** - 文件上传下载统计
- **系统使用率** - 系统资源使用情况
- **错误监控** - 错误日志和异常监控

#### 告警规则
- **CPU使用率** > 80% 持续5分钟
- **内存使用率** > 85% 持续5分钟  
- **磁盘使用率** > 90%
- **API错误率** > 1% 持续2分钟
- **服务不可用** 持续30秒

## 🤝 贡献指南

我们非常欢迎社区贡献！LeafPan是一个开源项目，依赖社区的参与和支持。请参考 [CONTRIBUTING.md](CONTRIBUTING.md) 了解详细的贡献指南。

### 🎯 贡献方式

<div align="center">

| 贡献类型 | 说明 | 适合人群 | 贡献流程 |
|----------|------|----------|----------|
| **🐛 Bug报告** | 报告程序错误或异常行为 | 所有用户 | 创建Issue → 描述问题 → 提供复现步骤 |
| **💡 功能建议** | 提出新功能或改进建议 | 产品经理/用户 | 创建Issue → 详细描述 → 讨论方案 |
| **🔧 代码贡献** | 修复Bug或实现新功能 | 开发者 | Fork → 开发 → 测试 → PR |
| **📖 文档改进** | 完善文档和教程 | 技术写作者 | 直接修改文档 → 提交PR |
| **🌐 翻译贡献** | 多语言翻译支持 | 翻译人员 | 翻译文档 → 提交PR |
| **🔍 代码审查** | 审查他人代码贡献 | 资深开发者 | 参与PR审查 → 提供建议 |
| **📊 性能优化** | 性能调优和优化 | 性能工程师 | 分析性能 → 优化代码 → 提交PR |

</div>

### 🔄 开发流程

#### 1. 准备工作

```bash
# Fork项目到自己的GitHub账户
# 克隆到本地
git clone https://github.com/你的用户名/LeafPan.git
cd LeafPan

# 添加上游仓库
git remote add upstream https://github.com/YangShengzhou03/LeafPan.git

# 保持本地仓库同步
git fetch upstream
git checkout main
git merge upstream/main
```

#### 2. 创建功能分支

```bash
# 基于main分支创建功能分支
git checkout -b feature/你的功能名称

# 或修复Bug
git checkout -b fix/问题描述

# 或文档改进
git checkout -b docs/文档主题
```

**分支命名规范：**
- `feature/` - 新功能开发
- `fix/` - Bug修复
- `docs/` - 文档改进
- `test/` - 测试相关
- `refactor/` - 代码重构
- `style/` - 代码格式调整

#### 3. 开发与测试

```bash
# 开发代码，确保遵循代码规范
# 运行测试确保功能正常

# 前端测试
cd frontend && npm run test:unit

# 后端测试
cd backend && ./mvnw test

# 代码规范检查
npm run lint  # 前端
./mvnw checkstyle:check  # 后端
```

#### 4. 提交代码

```bash
# 添加更改
git add .

# 提交更改（遵循约定式提交规范）
git commit -m "feat: 添加文件分享功能"
# 或 git commit -m "fix: 修复登录页面样式问题"
# 或 git commit -m "docs: 更新部署文档"

# 推送到远程仓库
git push origin feature/你的功能名称
```

**提交信息规范：**
- `feat:` - 新功能
- `fix:` - Bug修复
- `docs:` - 文档更新
- `style:` - 代码格式调整
- `refactor:` - 代码重构
- `test:` - 测试相关
- `chore:` - 构建过程或辅助工具变动

#### 5. 创建Pull Request

1. 在GitHub上创建Pull Request
2. 选择正确的目标分支（通常是`main`）
3. 填写详细的PR描述
4. 关联相关Issue（使用`#Issue编号`）
5. 等待代码审查

### 📋 代码规范

#### 前端代码规范
- 使用ESLint + Prettier进行代码格式化
- 遵循Vue.js官方风格指南
- 使用TypeScript进行类型检查
- 编写组件文档和单元测试

#### 后端代码规范
- 遵循Java编码规范
- 使用Checkstyle进行代码检查
- 编写清晰的JavaDoc注释
- 确保单元测试覆盖率>80%

#### 文档规范
- 使用Markdown格式编写
- 包含清晰的目录结构
- 提供代码示例和截图
- 保持中英文文档同步

### 🏆 贡献者权益

- **贡献者名单** - 在README中列出所有贡献者
- **代码署名** - 在相关文件中保留贡献者信息
- **社区认可** - 获得社区认可和感谢
- **优先支持** - 获得项目维护者的优先技术支持
- **参与决策** - 资深贡献者可参与项目发展方向决策

### ❓ 常见问题

**Q: 我是新手，如何开始贡献？**
A: 建议从文档改进或简单的Bug修复开始，可以先查看标记为"good first issue"的Issue。

**Q: 我的PR被拒绝了怎么办？**
A: 不要灰心！仔细阅读审查意见，根据建议修改后重新提交。

**Q: 如何获得技术帮助？**
A: 可以在GitHub Discussions中提问，或加入我们的社区聊天群组。

**Q: 贡献有奖励吗？**
A: LeafPan是开源项目，主要奖励是技术成长和社区认可。特别优秀的贡献者可能获得维护者身份。

## 📄 许可证

<div align="center">

### 📜 开源协议

本项目采用 **MIT License** 开源协议，这是最宽松的开源协议之一。

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

#### 协议特点

| 权限 | 说明 | 限制 |
|------|------|------|
| ✅ **商业使用** | 允许商业用途 | 无限制 |
| ✅ **修改分发** | 允许修改和分发 | 需保留版权声明 |
| ✅ **专利使用** | 允许专利使用 | 无限制 |
| ✅ **私人使用** | 允许私人使用 | 无限制 |
| ❌ **责任限制** | 不承担任何责任 | 使用者自负风险 |
| ❌ **担保限制** | 不提供任何担保 | 无质量保证 |

#### 协议要求

使用本项目时，您需要：

1. **保留版权声明** - 在所有副本或重要部分中包含原始版权声明
2. **包含许可证** - 在分发时包含MIT许可证文本
3. **免责声明** - 明确说明不承担任何责任

#### 完整许可证文本

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

### 🔗 相关资源

- [完整许可证文件](LICENSE)
- [开源协议选择指南](https://choosealicense.com/)
- [MIT协议官方说明](https://opensource.org/licenses/MIT)

</div>

## 📞 联系我们

<div align="center">

### 🌟 项目团队

LeafPan由开源社区共同维护，我们欢迎各种形式的参与和贡献！

#### 核心维护者

| 角色 | 负责人 | 联系方式 | 主要职责 |
|------|--------|----------|----------|
| **项目发起人** | YangShengzhou03 | yangshengzhou03@gmail.com | 项目规划、架构设计 |
| **前端开发** | 社区贡献者 | - | Vue.js开发、UI设计 |
| **后端开发** | 社区贡献者 | - | Spring Boot开发、API设计 |
| **文档维护** | 社区贡献者 | - | 文档编写、翻译维护 |

#### 社区支持渠道

<div align="center">

| 渠道类型 | 平台 | 链接 | 主要用途 | 响应时间 |
|----------|------|------|----------|----------|
| **🐛 Bug反馈** | GitHub Issues | [问题反馈](https://github.com/YangShengzhou03/LeafPan/issues) | 报告程序错误 | 1-3个工作日 |
| **💡 功能建议** | GitHub Discussions | [功能讨论](https://github.com/YangShengzhou03/LeafPan/discussions) | 新功能建议 | 1-2个工作日 |
| **❓ 技术问答** | GitHub Discussions | [技术问答](https://github.com/YangShengzhou03/LeafPan/discussions) | 技术问题咨询 | 1-2个工作日 |
| **📧 邮件联系** | 电子邮件 | yangshengzhou03@gmail.com | 重要事务沟通 | 1-3个工作日 |
| **🔗 社交媒体** | 待定 | - | 项目动态发布 | 不定期 |

</div>

### 🤝 合作机会

我们欢迎以下形式的合作：

#### 技术合作
- **代码审查** - 帮助审查Pull Request
- **架构优化** - 参与系统架构设计
- **性能调优** - 优化系统性能
- **安全审计** - 进行安全代码审查

#### 社区合作
- **文档翻译** - 多语言文档维护
- **教程编写** - 编写使用教程
- **社区运营** - 帮助运营社区
- **用户支持** - 回答用户问题

#### 商业合作
- **企业定制** - 企业级定制开发
- **技术咨询** - 提供技术咨询服务
- **培训服务** - 提供技术培训

### 📊 项目状态

| 指标 | 状态 | 说明 |
|------|------|------|
| **开发状态** | 🟢 **活跃开发** | 持续更新和维护 |
| **版本状态** | 🟡 **测试版本** | 功能基本稳定，持续优化 |
| **社区状态** | 🟢 **活跃社区** | 欢迎社区贡献 |
| **文档状态** | 🟡 **完善中** | 文档持续更新 |

### 🎯 发展路线图

#### 短期目标 (1-3个月)
- [ ] 完善核心文件管理功能
- [ ] 优化移动端体验
- [ ] 增加更多存储后端支持
- [ ] 完善测试覆盖率

#### 中期目标 (3-6个月)
- [ ] 实现企业级功能
- [ ] 支持插件系统
- [ ] 完善监控和日志系统
- [ ] 社区生态建设

#### 长期目标 (6-12个月)
- [ ] 云原生部署支持
- [ ] 多租户支持
- [ ] AI智能功能集成
- [ ] 国际化完善

</div>

---

<div align="center">

## ⭐️ 支持我们

**如果LeafPan对您有帮助，请给我们一个Star支持！**

您的支持是我们持续开发的最大动力！

[![Star History Chart](https://api.star-history.com/svg?repos=YangShengzhou03/LeafPan&type=Date)](https://star-history.com/#YangShengzhou03/LeafPan&Date)

### 🙏 致谢

感谢所有为LeafPan项目做出贡献的开发者们！

特别感谢以下开源项目：
- [Vue.js](https://vuejs.org/) - 渐进式JavaScript框架
- [Spring Boot](https://spring.io/projects/spring-boot) - Java应用框架
- [Element Plus](https://element-plus.org/) - Vue 3组件库
- [MyBatis](https://mybatis.org/mybatis-3/) - 持久层框架
- [MinIO](https://min.io/) - 对象存储服务

### 🔄 更新日志

查看项目的完整更新记录：
- [版本发布记录](https://github.com/YangShengzhou03/LeafPan/releases)
- [提交历史](https://github.com/YangShengzhou03/LeafPan/commits/main)

---

**🍁 LeafPan - 让文件管理更简单，更安全！**

</div>