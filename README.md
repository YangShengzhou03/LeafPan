# 🍁 LeafPan 枫叶网盘

<div align="center">

[![GitHub stars](https://img.shields.io/github/stars/YangShengzhou03/LeafPan?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/LeafPan/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/YangShengzhou03/LeafPan?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/LeafPan/network)
[![GitHub issues](https://img.shields.io/github/issues/YangShengzhou03/LeafPan?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/LeafPan/issues)
[![GitHub license](https://img.shields.io/github/license/YangShengzhou03/LeafPan?style=for-the-badge)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen?style=for-the-badge&logo=spring)](https://spring.io/projects/spring-boot)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.x-green?style=for-the-badge&logo=vue.js)](https://vuejs.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue?style=for-the-badge&logo=mysql)](https://www.mysql.com/)
[![MinIO](https://img.shields.io/badge/MinIO-RELEASE.2024-orange?style=for-the-badge&logo=minio)](https://min.io/)
[![Docker](https://img.shields.io/badge/Docker-24.0+-blue?style=for-the-badge&logo=docker)](https://www.docker.com/)

**🌐 一个现代化的开源云存储解决方案**

*企业级文件管理、安全存储、智能分享平台*

[English](README_EN.md) | [中文](README.md) | [文档](https://github.com/YangShengzhou03/LeafPan/wiki) | [演示](https://leafpan.demo.com) | [下载](https://github.com/YangShengzhou03/LeafPan/releases) | [在线体验](https://demo.leafpan.com)

</div>

## 📖 项目概述

LeafPan（枫叶网盘）是一个基于现代Web技术的开源云存储解决方案，提供企业级的文件管理、分享和协作功能。项目采用前后端分离架构，支持多租户、高并发和分布式部署。

### 🎯 项目愿景

LeafPan致力于为个人用户、开发团队和企业提供安全、稳定、高性能的云存储服务。通过现代化的技术栈和优雅的设计，让文件管理变得更加简单高效。

### 🎯 目标用户

- **个人用户** - 个人文件备份、照片存储、文档管理
- **开发团队** - 代码仓库备份、项目文档共享、团队协作
- **中小企业** - 企业文件管理、内部文档共享、客户文件分发
- **教育机构** - 教学资源管理、学生作业提交、在线学习资料

### 🌟 核心价值

- **开源免费** - 完全开源，遵循MIT许可证，可自由使用和修改
- **安全可靠** - 多层次安全防护，数据加密存储，权限精细控制
- **高性能** - 支持大文件断点续传，多线程上传下载，智能缓存
- **易于部署** - 提供Docker一键部署，支持多种部署方式
- **可扩展** - 模块化设计，支持水平扩展，适应不同规模需求

## 🚀 核心特性

### ✨ 主要功能

#### 🔐 **安全认证系统**
- **JWT令牌认证** - 无状态认证机制，支持分布式部署
- **多因素认证** - 支持TOTP、短信验证码等多种认证方式
- **会话管理** - 支持多设备同时登录，会话超时控制
- **权限控制** - 基于RBAC的细粒度权限管理

#### 📁 **智能文件管理**
- **多级目录结构** - 支持无限级文件夹嵌套，树形结构展示
- **拖拽上传** - 支持文件/文件夹拖拽上传，批量选择
- **文件预览** - 支持图片、视频、文档、代码等多种格式预览
- **版本控制** - 文件版本管理，支持历史版本恢复
- **回收站** - 文件删除保护，支持恢复已删除文件
- **批量操作** - 支持文件/文件夹批量移动、复制、删除

#### 🔗 **灵活文件分享**
- **多种分享方式** - 公开链接、私有链接、密码保护链接
- **分享权限控制** - 可设置下载次数、有效期、密码保护
- **分享统计** - 实时监控分享链接访问情况
- **外链管理** - 支持分享链接的创建、修改、撤销

#### 📊 **详细存储统计**
- **实时监控** - 存储空间使用情况实时显示
- **使用量分析** - 按时间、文件类型统计存储使用情况
- **配额管理** - 支持用户存储空间配额设置
- **趋势分析** - 存储使用趋势图表展示

#### 🔍 **智能全文搜索**
- **内容搜索** - 基于Elasticsearch的文件内容全文检索
- **元数据搜索** - 支持文件名、标签、描述等搜索
- **高级搜索** - 支持文件类型、大小、时间范围等条件筛选
- **搜索历史** - 记录用户搜索历史，提升搜索效率

#### 📱 **多端支持**
- **Web端** - 响应式设计，支持PC和移动端浏览器
- **移动端App** - 原生移动应用（iOS/Android）
- **桌面客户端** - Windows、macOS、Linux桌面客户端
- **API接口** - 完整的RESTful API，支持第三方集成

### 🛠️ 技术特性

#### ⚡ **高性能架构**
- **断点续传** - 支持大文件分片上传，网络中断后可续传
- **多线程下载** - 支持文件分片并行下载，提升下载速度
- **智能缓存** - 多级缓存机制，提升系统响应速度
- **负载均衡** - 支持多实例部署，自动负载分配

#### 🔒 **全方位安全**
- **数据加密** - 文件存储加密，传输过程SSL加密
- **访问控制** - 基于角色的权限控制，细粒度权限管理
- **安全审计** - 完整的操作日志记录，支持安全审计
- **防病毒扫描** - 集成病毒扫描，确保文件安全

#### 📈 **高可扩展性**
- **微服务架构** - 模块化设计，支持独立部署和扩展
- **水平扩展** - 支持无状态服务水平扩展
- **插件机制** - 支持功能插件扩展，便于定制开发
- **API网关** - 统一的API入口，支持流量控制

#### 🐳 **现代化部署**
- **容器化部署** - 完整的Docker支持，一键部署
- **Kubernetes支持** - 原生Kubernetes部署配置
- **CI/CD集成** - 支持GitHub Actions等CI/CD工具
- **监控告警** - 集成Prometheus+Grafana监控体系

## 🛠️ 技术栈

### 🎨 前端技术栈

#### 核心框架
- **Vue 3** (v3.4+) - 现代化前端框架，组合式API
- **TypeScript** (v5.0+) - 类型安全的JavaScript超集
- **Vite** (v5.0+) - 下一代前端构建工具

#### UI组件库
- **Element Plus** (v2.4+) - 企业级UI组件库
- **Tailwind CSS** (v3.3+) - 实用优先的CSS框架
- **Iconify** - 统一图标库，支持多种图标集

#### 状态管理
- **Pinia** (v2.1+) - Vue状态管理库
- **Vue Router** (v4.2+) - 官方路由管理器

#### 网络请求
- **Axios** (v1.6+) - HTTP客户端库
- **WebSocket** - 实时通信支持

#### 开发工具
- **ESLint** + **Prettier** - 代码规范和格式化
- **Husky** + **lint-staged** - Git钩子管理
- **Vitest** - 单元测试框架

### ⚙️ 后端技术栈

#### 核心框架
- **Spring Boot 3.5.6** - Java企业级应用框架
- **Java 17+** - 长期支持版本
- **Maven** - 项目构建和依赖管理

#### 安全认证
- **Spring Security 6.2+** - 安全认证和授权框架
- **JWT** - JSON Web Token认证
- **BCrypt** - 密码加密算法

#### 数据持久化
- **Spring Data JPA** - 数据持久化解决方案
- **MySQL 8.0+** - 关系型数据库
- **HikariCP** - 高性能数据库连接池
- **Flyway** - 数据库版本管理

#### 文件存储
- **MinIO** (RELEASE.2024+) - 高性能对象存储服务
- **文件分片上传** - 大文件分片处理
- **MD5校验** - 文件完整性验证

#### 搜索服务
- **Elasticsearch 8.11+** - 分布式搜索和分析引擎
- **IK分词器** - 中文分词支持
- **Logstash** - 数据采集和传输

#### 消息队列
- **RabbitMQ 3.12+** - 消息队列，支持异步处理
- **延迟队列** - 定时任务处理
- **死信队列** - 消息重试机制

#### 缓存系统
- **Redis 7.2+** - 内存数据存储
- **Spring Cache** - 缓存抽象层
- **分布式锁** - 并发控制

### 🐳 部署和运维

#### 容器化
- **Docker 24.0+** - 容器化部署
- **Docker Compose 2.20+** - 多容器应用编排

#### 容器编排
- **Kubernetes 1.28+** - 容器编排平台（可选）
- **Helm Charts** - Kubernetes包管理

#### 反向代理
- **Nginx 1.24+** - 反向代理和负载均衡
- **SSL/TLS** - HTTPS安全传输
- **Gzip压缩** - 网络传输优化

#### 监控告警
- **Prometheus 2.47+** - 监控和告警系统
- **Grafana 10.1+** - 数据可视化平台
- **Alertmanager** - 告警管理

#### 日志管理
- **ELK Stack** - 集中式日志管理
- **Logback** - Java日志框架
- **Filebeat** - 日志收集器

#### CI/CD
- **GitHub Actions** - 自动化构建部署
- **Jenkins** - 持续集成工具（可选）
- **SonarQube** - 代码质量检测

## 📁 项目结构

```
LeafPan/
├── backend/                         # Spring Boot后端模块
│   ├── src/main/java/             # Java源代码
│   │   ├── com/leafpan/           # 主包路径
│   │   │   ├── config/            # 配置类（Spring配置、安全配置等）
│   │   │   ├── controller/        # REST控制器（API接口层）
│   │   │   ├── service/           # 业务逻辑层（核心业务处理）
│   │   │   ├── repository/        # 数据访问层（数据库操作）
│   │   │   ├── entity/            # 实体类（数据库表映射）
│   │   │   ├── dto/               # 数据传输对象（API数据传输）
│   │   │   ├── vo/                # 视图对象（前端展示数据）
│   │   │   ├── exception/         # 异常处理（全局异常处理）
│   │   │   ├── aspect/            # 切面编程（日志、权限等）
│   │   │   ├── util/              # 工具类（通用工具函数）
│   │   │   ├── constant/          # 常量定义
│   │   │   ├── annotation/        # 自定义注解
│   │   │   ├── interceptor/       # 拦截器（请求拦截处理）
│   │   │   ├── filter/            # 过滤器（请求过滤）
│   │   │   ├── listener/          # 监听器（事件监听）
│   │   │   ├── task/              # 定时任务
│   │   │   ├── mq/                # 消息队列处理
│   │   │   └── cache/             # 缓存相关
│   │   └── resources/              # 资源文件
│   │       ├── application.yml    # 应用配置文件
│   │       ├── application-dev.yml # 开发环境配置
│   │       ├── application-prod.yml # 生产环境配置
│   │       ├── static/             # 静态资源
│   │       ├── templates/          # 模板文件
│   │       ├── db/migration/      # 数据库迁移脚本
│   │       └── logback-spring.xml # 日志配置文件
│   ├── src/test/java/             # 测试代码
│   │   ├── unit/                  # 单元测试
│   │   ├── integration/           # 集成测试
│   │   └── e2e/                   # 端到端测试
│   ├── Dockerfile                 # Docker构建文件
│   ├── pom.xml                    # Maven配置
│   └── mvnw                       # Maven Wrapper
├── frontend/                       # Vue 3前端模块
│   ├── src/                       # 源代码
│   │   ├── components/            # 可复用组件
│   │   │   ├── common/           # 通用组件
│   │   │   ├── layout/           # 布局组件
│   │   │   ├── file/             # 文件相关组件
│   │   │   ├── user/             # 用户相关组件
│   │   │   └── share/            # 分享相关组件
│   │   ├── views/                 # 页面组件
│   │   │   ├── login/            # 登录页面
│   │   │   ├── dashboard/        # 仪表板页面
│   │   │   ├── file-manager/     # 文件管理页面
│   │   │   ├── share/            # 分享管理页面
│   │   │   └── settings/         # 设置页面
│   │   ├── router/                # 路由配置
│   │   │   ├── index.ts          # 路由主文件
│   │   │   └── guards/           # 路由守卫
│   │   ├── stores/                # 状态管理
│   │   │   ├── user.ts           # 用户状态
│   │   │   ├── file.ts           # 文件状态
│   │   │   ├── share.ts          # 分享状态
│   │   │   └── app.ts            # 应用状态
│   │   ├── utils/                 # 工具函数
│   │   │   ├── request.ts        # 请求封装
│   │   │   ├── storage.ts         # 本地存储
│   │   │   ├── validate.ts       # 表单验证
│   │   │   ├── upload.ts         # 上传工具
│   │   │   └── common.ts         # 通用工具
│   │   ├── assets/                # 静态资源
│   │   │   ├── images/           # 图片资源
│   │   │   ├── styles/           # 样式文件
│   │   │   └── icons/            # 图标资源
│   │   ├── types/                 # TypeScript类型定义
│   │   ├── api/                   # API接口定义
│   │   ├── plugins/               # Vue插件
│   │   └── main.ts                # 应用入口
│   ├── public/                    # 公共资源
│   │   ├── index.html             # HTML模板
│   │   ├── favicon.ico            # 网站图标
│   │   └── manifest.json          # PWA配置
│   ├── tests/                     # 测试文件
│   │   ├── unit/                  # 单元测试
│   │   └── e2e/                   # 端到端测试
│   ├── Dockerfile                 # Docker构建文件
│   ├── package.json               # 依赖配置
│   ├── vite.config.ts             # Vite配置
│   ├── tsconfig.json              # TypeScript配置
│   ├── eslint.config.js           # ESLint配置
│   └── tailwind.config.js         # Tailwind配置
├── deploy/                         # 部署配置
│   ├── docker-compose.yml         # Docker Compose配置
│   ├── nginx/                      # Nginx配置
│   │   ├── nginx.conf             # Nginx主配置
│   │   └── conf.d/                # 虚拟主机配置
│   ├── mysql/                      # MySQL配置
│   │   └── init/                  # 数据库初始化脚本
│   ├── minio/                      # MinIO配置
│   ├── redis/                      # Redis配置
│   └── elasticsearch/              # Elasticsearch配置
├── k8s/                           # Kubernetes配置
│   ├── namespace.yaml             # 命名空间
│   ├── backend-deploy.yaml        # 后端部署配置
│   ├── frontend-deploy.yaml       # 前端部署配置
│   ├── mysql-deploy.yaml          # MySQL部署配置
│   ├── minio-deploy.yaml          # MinIO部署配置
│   └── deploy.sh                  # 部署脚本
├── docs/                          # 项目文档
│   ├── api/                       # API文档
│   ├── deployment/                # 部署文档
│   ├── development/               # 开发文档
│   ├── architecture/              # 架构设计文档
│   └── user-guide/                # 用户指南
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
└── README.md                      # 项目说明文档
```

## 🚀 快速开始

### 📋 环境要求

在开始之前，请确保您的系统满足以下要求：

#### 基础环境
- **操作系统**: Windows 10/11, macOS 10.15+, Ubuntu 18.04+, CentOS 7+
- **内存**: 最低 4GB RAM，推荐 8GB RAM 或更高
- **存储**: 至少 10GB 可用磁盘空间
- **网络**: 稳定的网络连接

#### 开发环境
- **Java 17+** - [下载地址](https://adoptium.net/)
- **Node.js 18+** - [下载地址](https://nodejs.org/)
- **Maven 3.6+** - [下载地址](https://maven.apache.org/)
- **Git 2.20+** - [下载地址](https://git-scm.com/)

#### 生产环境
- **Docker 24.0+** - [下载地址](https://docs.docker.com/get-docker/)
- **Docker Compose 2.20+** - [下载地址](https://docs.docker.com/compose/install/)
- **MySQL 8.0+** - [下载地址](https://dev.mysql.com/downloads/mysql/)
- **MinIO** - 对象存储服务（已包含在Docker配置中）

#### 可选组件
- **Redis 7.2+** - 缓存服务（推荐生产环境使用）
- **Elasticsearch 8.11+** - 搜索服务（可选）
- **RabbitMQ 3.12+** - 消息队列（可选）

### 🐳 使用Docker快速部署（推荐）

#### 1. 克隆项目
```bash
git clone https://github.com/YangShengzhou03/LeafPan.git
cd LeafPan
```

#### 2. 配置环境变量
```bash
# 复制环境变量模板
cp .env.example .env

# 编辑环境变量文件
nano .env  # 或使用其他编辑器
```

**环境变量配置说明：**
```bash
# 数据库配置
MYSQL_ROOT_PASSWORD=your_mysql_root_password
MYSQL_DATABASE=leafpan
MYSQL_USER=leafpan_user
MYSQL_PASSWORD=your_mysql_password

# MinIO配置
MINIO_ROOT_USER=minioadmin
MINIO_ROOT_PASSWORD=minioadmin123
MINIO_DATA_BUCKET=data
MINIO_AVATAR_BUCKET=avatar

# 应用配置
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080
FRONTEND_PORT=3000

# Redis配置（可选）
REDIS_PASSWORD=your_redis_password
```

#### 3. 启动所有服务
```bash
# 启动所有服务（后台运行）
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看服务日志
docker-compose logs -f
```

#### 4. 服务健康检查
```bash
# 检查后端服务是否正常
curl http://localhost:8080/api/health

# 检查前端服务是否正常
curl http://localhost:3000

# 检查MinIO服务是否正常
curl http://localhost:9000/minio/health/live
```

#### 5. 访问应用
- **前端应用**: http://localhost:3000
- **后端API**: http://localhost:8080
- **MinIO控制台**: http://localhost:9001 (用户名: minioadmin, 密码: minioadmin123)
- **数据库管理**: http://localhost:8081 (Adminer)
- **Redis管理**: http://localhost:8082 (RedisInsight，如果启用)

#### 6. 服务管理命令
```bash
# 停止所有服务
docker-compose down

# 停止并删除所有数据（谨慎使用）
docker-compose down -v

# 重启特定服务
docker-compose restart backend

# 更新服务（重新构建镜像）
docker-compose up -d --build

# 查看资源使用情况
docker-compose stats
```

#### 7. 生产环境部署建议

**使用生产环境配置：**
```bash
# 使用生产环境配置
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
```

**配置SSL证书：**
```bash
# 在nginx配置中启用SSL
# 参考 deploy/nginx/conf.d/ssl.conf
```

**配置数据持久化：**
```bash
# 确保数据卷正确挂载
# 检查 docker-compose.yml 中的 volumes 配置
```

### 🔧 手动安装部署

#### 后端部署

##### 1. 环境准备
```bash
# 检查Java版本
java -version

# 检查Maven版本
mvn -version

# 检查MySQL服务
systemctl status mysql  # Linux
# 或检查MySQL服务是否运行
```

##### 2. 数据库配置
```sql
-- 创建数据库
CREATE DATABASE leafpan CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户并授权
CREATE USER 'leafpan_user'@'%' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON leafpan.* TO 'leafpan_user'@'%';
FLUSH PRIVILEGES;

-- 或者使用root用户（不推荐生产环境）
-- GRANT ALL PRIVILEGES ON leafpan.* TO 'root'@'%';
```

##### 3. 应用配置
```bash
cd backend

# 复制配置文件
cp src/main/resources/application.yml.example src/main/resources/application.yml

# 编辑配置文件
nano src/main/resources/application.yml
```

**application.yml 配置示例：**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/leafpan?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
    username: leafpan_user
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true

minio:
  endpoint: http://localhost:9000
  access-key: minioadmin
  secret-key: minioadmin123
  bucket:
    data: data
    avatar: avatar

server:
  port: 8080
  servlet:
    context-path: /api

logging:
  level:
    com.leafpan: DEBUG
    org.springframework.security: DEBUG
```

##### 4. 构建和运行
```bash
# 方式一：使用Maven Wrapper构建
./mvnw clean package

# 运行应用
java -jar target/leafpan-backend-1.0.0.jar

# 方式二：使用Spring Boot Maven插件（开发模式）
./mvnw spring-boot:run

# 方式三：使用系统Maven
mvn clean package
java -jar target/leafpan-backend-1.0.0.jar
```

##### 5. 验证后端服务
```bash
# 检查健康状态
curl http://localhost:8080/api/health

# 检查API文档
curl http://localhost:8080/api/swagger-ui.html
```

#### 前端部署

##### 1. 环境准备
```bash
# 检查Node.js版本
node --version

# 检查npm版本
npm --version
```

##### 2. 安装依赖
```bash
cd frontend

# 安装依赖包
npm install

# 或者使用yarn（如果已安装）
yarn install
```

##### 3. 环境配置
```bash
# 复制环境变量文件
cp .env.example .env

# 编辑环境变量
nano .env
```

**.env 配置示例：**
```bash
# API基础URL
VITE_API_BASE_URL=http://localhost:8080/api

# 应用标题
VITE_APP_TITLE=LeafPan 枫叶网盘

# 上传文件大小限制（MB）
VITE_UPLOAD_MAX_SIZE=100

# 是否启用PWA
VITE_ENABLE_PWA=true

# 开发服务器端口
VITE_DEV_SERVER_PORT=3000
```

##### 4. 开发模式运行
```bash
# 启动开发服务器
npm run dev

# 或者使用yarn
yarn dev
```

##### 5. 生产构建
```bash
# 构建生产版本
npm run build

# 预览生产版本
npm run preview

# 或者使用yarn
yarn build
yarn preview
```

##### 6. 部署到Web服务器
```bash
# 构建后的文件在 dist/ 目录
# 可以将 dist/ 目录部署到Nginx、Apache等Web服务器

# Nginx配置示例
sudo cp -r dist/* /var/www/html/
sudo systemctl restart nginx
```

#### 独立服务部署

##### MinIO部署
```bash
# 下载MinIO
wget https://dl.min.io/server/minio/release/linux-amd64/minio
chmod +x minio

# 启动MinIO
./minio server /data/minio --console-address ":9001"

# 或者使用Docker
docker run -p 9000:9000 -p 9001:9001 \
  -v /mnt/data:/data \
  minio/minio server /data --console-address ":9001"
```

##### Redis部署
```bash
# 使用Docker部署Redis
docker run -d --name redis \
  -p 6379:6379 \
  -v /mnt/redis/data:/data \
  redis:7.2-alpine redis-server --requirepass your_password
```

##### Elasticsearch部署
```bash
# 使用Docker部署Elasticsearch
docker run -d --name elasticsearch \
  -p 9200:9200 \
  -p 9300:9300 \
  -e "discovery.type=single-node" \
  -e "xpack.security.enabled=false" \
  elasticsearch:8.11.1
```

### 🧪 测试

#### 后端测试

##### 1. 单元测试
```bash
cd backend

# 运行所有单元测试
./mvnw test

# 运行特定测试类
./mvnw test -Dtest=UserServiceTest

# 运行特定包下的测试
./mvnw test -Dtest="com.leafpan.service.*Test"

# 生成测试覆盖率报告
./mvnw jacoco:report
```

##### 2. 集成测试
```bash
# 运行集成测试（需要启动测试数据库）
./mvnw verify

# 使用测试配置文件
./mvnw test -Dspring.profiles.active=test
```

##### 3. API测试
```bash
# 使用curl测试API接口
curl -X GET "http://localhost:8080/api/health"
curl -X POST "http://localhost:8080/api/auth/login" -H "Content-Type: application/json" -d '{"username":"test","password":"test"}'

# 使用Postman或Swagger UI进行API测试
# 访问 http://localhost:8080/api/swagger-ui.html
```

##### 4. 性能测试
```bash
# 使用JMeter或Gatling进行性能测试
# 参考 scripts/performance-test/ 目录
```

#### 前端测试

##### 1. 单元测试
```bash
cd frontend

# 运行单元测试
npm run test:unit

# 运行测试并生成覆盖率报告
npm run test:coverage

# 监听模式运行测试
npm run test:watch
```

##### 2. 组件测试
```bash
# 测试特定组件
npm run test:unit -- components/FileUploader.spec.ts

# 测试特定目录
npm run test:unit -- components/**/*.spec.ts
```

##### 3. E2E测试
```bash
# 运行端到端测试
npm run test:e2e

# 在开发模式下运行E2E测试
npm run test:e2e:dev

# 生成E2E测试报告
npm run test:e2e:report
```

##### 4. 代码质量检查
```bash
# 运行ESLint检查
npm run lint

# 自动修复ESLint问题
npm run lint:fix

# 检查TypeScript类型
npm run type-check
```

#### 集成测试

##### 1. 端到端测试
```bash
# 启动所有服务进行E2E测试
docker-compose -f docker-compose.test.yml up -d

# 运行集成测试套件
npm run test:integration

# 清理测试环境
docker-compose -f docker-compose.test.yml down
```

##### 2. API集成测试
```bash
# 使用Newman运行Postman集合
npm run test:api

# 使用Supertest进行API测试
npm run test:supertest
```

#### 测试报告

##### 1. 生成测试报告
```bash
# 后端测试报告
open backend/target/site/jacoco/index.html

# 前端测试报告
open frontend/coverage/lcov-report/index.html

# E2E测试报告
open frontend/e2e/reports/index.html
```

##### 2. 持续集成测试
```bash
# GitHub Actions自动运行测试
# 查看 .github/workflows/ci-cd.yml

# 本地模拟CI环境
npm run test:ci
```

#### 测试最佳实践

##### 1. 测试数据管理
```bash
# 使用测试数据库
# 配置 test/resources/application-test.yml

# 使用测试数据夹具
# 参考 backend/src/test/resources/fixtures/
```

##### 2. Mock和Stub
```bash
# 使用Mockito进行Mock
@Mock
private UserRepository userRepository;

# 使用Jest进行前端Mock
jest.mock('@/api/user');
```

##### 3. 测试环境配置
```bash
# 测试环境变量
# 创建 .env.test 文件

# 测试数据库配置
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    username: sa
    password: 
```

## 📊 数据库设计

### 核心表结构

#### 1. 用户表 (users)
存储用户基本信息、认证信息和存储配额。

**字段说明：**
- `id` - 主键，自增ID
- `username` - 用户名，唯一索引
- `email` - 邮箱地址，唯一索引
- `password` - 加密密码（BCrypt）
- `nickname` - 昵称
- `avatar` - 头像URL
- `storage_quota` - 存储配额（字节）
- `used_storage` - 已用存储空间（字节）
- `status` - 用户状态（激活/禁用）
- `last_login_at` - 最后登录时间
- `created_at` - 创建时间
- `updated_at` - 更新时间

#### 2. 文件夹表 (folders)
支持多级目录结构，实现树形文件管理。

**字段说明：**
- `id` - 主键，自增ID
- `name` - 文件夹名称
- `path` - 完整路径
- `parent_id` - 父文件夹ID（支持嵌套）
- `user_id` - 所属用户ID
- `created_at` - 创建时间
- `updated_at` - 更新时间

#### 3. 文件表 (files)
存储文件元数据信息，与MinIO对象存储关联。

**字段说明：**
- `id` - 主键，自增ID
- `name` - 文件名
- `original_name` - 原始文件名
- `size` - 文件大小（字节）
- `mime_type` - MIME类型
- `extension` - 文件扩展名
- `folder_id` - 所属文件夹ID
- `user_id` - 所属用户ID
- `object_name` - MinIO对象名称
- `bucket_name` - 存储桶名称
- `md5_hash` - 文件MD5哈希值
- `is_public` - 是否公开
- `download_count` - 下载次数
- `created_at` - 创建时间
- `updated_at` - 更新时间

#### 4. 分享表 (shares)
管理文件分享功能，支持公开/私有链接和密码保护。

**字段说明：**
- `id` - 主键，自增ID
- `share_code` - 分享码（唯一）
- `file_id` - 文件ID
- `user_id` - 分享者ID
- `password` - 访问密码（可选）
- `expire_at` - 过期时间
- `max_downloads` - 最大下载次数
- `download_count` - 已下载次数
- `is_public` - 是否公开分享
- `created_at` - 创建时间

#### 5. 操作日志表 (operation_logs)
记录用户操作历史，用于审计和分析。

**字段说明：**
- `id` - 主键，自增ID
- `user_id` - 用户ID
- `operation_type` - 操作类型（上传/下载/删除等）
- `resource_type` - 资源类型（文件/文件夹）
- `resource_id` - 资源ID
- `description` - 操作描述
- `ip_address` - IP地址
- `user_agent` - 用户代理
- `created_at` - 创建时间

#### 6. 系统配置表 (system_configs)
存储系统配置信息。

**字段说明：**
- `id` - 主键，自增ID
- `config_key` - 配置键
- `config_value` - 配置值
- `description` - 配置描述
- `updated_at` - 更新时间

### 数据库关系图

```
users (1) ←→ (n) folders
users (1) ←→ (n) files
users (1) ←→ (n) shares
users (1) ←→ (n) operation_logs
folders (1) ←→ (n) files
folders (1) ←→ (n) folders (自关联)
files (1) ←→ (n) shares
```

### 索引设计

#### 主键索引
- 所有表的主键字段自动创建主键索引

#### 唯一索引
- `users.username` - 用户名唯一索引
- `users.email` - 邮箱唯一索引
- `shares.share_code` - 分享码唯一索引

#### 普通索引
- `files.user_id` - 用户文件查询优化
- `files.folder_id` - 文件夹文件查询优化
- `folders.user_id` - 用户文件夹查询优化
- `folders.parent_id` - 文件夹层级查询优化
- `operation_logs.user_id` - 用户操作日志查询优化
- `operation_logs.created_at` - 时间范围查询优化

### 数据库配置

#### 字符集和排序规则
- **字符集**: utf8mb4 (支持emoji表情和特殊字符)
- **排序规则**: utf8mb4_unicode_ci (支持多语言排序)

#### 存储引擎
- **存储引擎**: InnoDB (支持事务、行级锁、外键约束)
- **事务隔离级别**: READ COMMITTED

#### 连接池配置
- **连接池**: HikariCP (高性能连接池)
- **最大连接数**: 根据服务器配置调整
- **连接超时**: 30秒
- **空闲超时**: 10分钟

#### 性能优化配置
```sql
-- 调整InnoDB缓冲池大小（根据服务器内存调整）
SET GLOBAL innodb_buffer_pool_size = 2G;

-- 启用查询缓存
SET GLOBAL query_cache_size = 64M;

-- 调整最大连接数
SET GLOBAL max_connections = 200;
```

### 数据备份策略

#### 自动备份
```bash
# 使用mysqldump进行定期备份
mysqldump -u root -p leafpan > /backup/leafpan_$(date +%Y%m%d).sql

# 使用cron定时任务
0 2 * * * /usr/bin/mysqldump -u root -p leafpan > /backup/leafpan_$(date +%Y%m%d).sql
```

#### 增量备份
```bash
# 使用MySQL二进制日志进行增量备份
# 配置 my.cnf 启用二进制日志
log_bin = /var/log/mysql/mysql-bin.log
```

详细数据库设计请参考 [DATABASE_DESIGN.md](DATABASE_DESIGN.md)

## 📡 API 接口文档

### 基础信息
- **基础URL**: `http://localhost:8080/api`
- **认证方式**: Bearer Token (JWT)
- **数据格式**: JSON
- **字符编码**: UTF-8

## 📁 MinIO文件存储配置

### MinIO简介

MinIO是一个高性能、分布式的对象存储系统，兼容Amazon S3 API。它采用Golang开发，具有轻量级、高性能、易部署等特点，是云原生应用的首选存储方案。

### 核心特性

- **高性能**: 支持高并发读写，单节点可达183 GB/s的读取速度和171 GB/s的写入速度
- **可扩展**: 支持分布式部署，可扩展到数千个节点
- **安全可靠**: 支持数据加密、访问控制、版本控制等安全特性
- **S3兼容**: 完全兼容Amazon S3 API，便于迁移和集成
- **轻量级**: 单个二进制文件，资源占用低，易于部署

### 配置说明

#### 1. 基础配置

**访问地址**:
- **API端点**: http://localhost:9000
- **控制台地址**: http://localhost:9001
- **默认存储桶**: leafpan

**默认凭据**:
- **访问密钥**: minioadmin
- **秘密密钥**: minioadmin

#### 2. 环境变量配置

**Docker环境变量**:
```yaml
MINIO_ROOT_USER=minioadmin
MINIO_ROOT_PASSWORD=minioadmin
MINIO_BROWSER=on
MINIO_DOMAIN=localhost
```

**应用环境变量**:
```yaml
# MinIO连接配置
MINIO_ENDPOINT=http://localhost:9000
MINIO_ACCESS_KEY=minioadmin
MINIO_SECRET_KEY=minioadmin
MINIO_BUCKET_NAME=leafpan

# 高级配置
MINIO_REGION=us-east-1
MINIO_PATH_STYLE=true
MINIO_SECURE=false
```

#### 3. 存储桶配置

**创建存储桶**:
```bash
# 使用MinIO客户端创建存储桶
mc mb leafpan/leafpan

# 设置存储桶策略
mc policy set public leafpan/leafpan
```

**存储桶策略**:
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": "*",
      "Action": ["s3:GetObject"],
      "Resource": "arn:aws:s3:::leafpan/*",
      "Condition": {
        "Bool": {
          "aws:SecureTransport": "true"
        }
      }
    }
  ]
}
```

#### 4. 安全配置

**TLS加密传输**:
```bash
# 生成自签名证书
openssl genrsa -out private.key 2048
openssl req -new -x509 -days 3650 -key private.key -out public.crt -subj "/C=US/ST=State/L=City/O=Organization/CN=localhost"

# 配置MinIO使用TLS
minio server --certs-dir /etc/minio/certs /data
```

**访问策略配置**:
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "s3:GetObject",
        "s3:PutObject",
        "s3:DeleteObject"
      ],
      "Resource": "arn:aws:s3:::leafpan/*",
      "Principal": {
        "AWS": ["*"]
      }
    }
  ]
}
```

**定期轮换访问密钥**:
```bash
# 创建新访问密钥
mc admin user svcaccount add leafpan new-user

# 删除旧访问密钥
mc admin user svcaccount remove leafpan old-user
```

### 存储策略

#### 1. 数据分片存储

**纠删码配置**:
```bash
# 使用纠删码模式启动（4个数据盘，2个校验盘）
minio server --address :9000 http://host{1...6}/disk{1...4}
```

**存储类配置**:
```yaml
# 定义存储类
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  name: minio-ssd
provisioner: kubernetes.io/minio
parameters:
  type: ssd
  redundancy: "2"
```

#### 2. 自动备份机制

**数据备份策略**:
```bash
# 使用mc mirror命令备份数据
mc mirror leafpan backup-bucket

# 设置定时备份任务
0 2 * * * /usr/bin/mc mirror leafpan backup-bucket
```

**版本控制配置**:
```bash
# 启用版本控制
mc version enable leafpan/leafpan

# 配置生命周期规则
mc ilm add leafpan/leafpan --json '
{
  "Rules": [
    {
      "ID": "backup-rule",
      "Status": "Enabled",
      "Filter": {
        "Prefix": ""
      },
      "Transition": {
        "Days": 30,
        "StorageClass": "GLACIER"
      }
    }
  ]
}'
```

#### 3. 存储生命周期管理

**生命周期策略**:
```json
{
  "Rules": [
    {
      "ID": "cleanup-old-versions",
      "Status": "Enabled",
      "Filter": {
        "Prefix": ""
      },
      "NoncurrentVersionExpiration": {
        "NoncurrentDays": 30
      }
    },
    {
      "ID": "archive-old-files",
      "Status": "Enabled",
      "Filter": {
        "Prefix": "archive/"
      },
      "Transition": {
        "Days": 90,
        "StorageClass": "DEEP_ARCHIVE"
      }
    }
  ]
}
```

### 性能优化

#### 1. 网络优化
```yaml
# 调整网络参数
net.core.rmem_max = 16777216
net.core.wmem_max = 16777216
net.ipv4.tcp_rmem = 4096 87380 16777216
net.ipv4.tcp_wmem = 4096 16384 16777216
```

#### 2. 磁盘优化
```bash
# 使用XFS文件系统（推荐）
mkfs.xfs /dev/sdb1

# 挂载参数优化
mount -o noatime,nodiratime,largeio /dev/sdb1 /data
```

#### 3. MinIO配置优化
```bash
# 启动参数优化
minio server --address :9000 \
  --console-address :9001 \
  --config-dir /etc/minio \
  --certs-dir /etc/minio/certs \
  /data{1...4}
```

### 监控和日志

#### 1. 监控配置
```yaml
# Prometheus监控配置
scrape_configs:
  - job_name: 'minio'
    static_configs:
      - targets: ['localhost:9000']
    metrics_path: /minio/v2/metrics/cluster
    scheme: http
    basic_auth:
      username: minioadmin
      password: minioadmin
```

#### 2. 日志配置
```yaml
# 日志级别配置
MINIO_LOG_QUERY_URL=http://localhost:9000/minio/logs/query
MINIO_AUDIT_WEBHOOK_ENDPOINT=http://audit-service:8080
MINIO_AUDIT_WEBHOOK_AUTH_TOKEN=your-token
```

### 故障排除

#### 1. 常见问题

**连接问题**:
```bash
# 检查MinIO服务状态
curl http://localhost:9000/minio/health/live

# 检查存储桶访问
mc ls leafpan
```

**权限问题**:
```bash
# 检查用户权限
mc admin policy list leafpan

# 重新设置权限
mc policy set download leafpan/leafpan
```

#### 2. 性能问题诊断
```bash
# 检查磁盘IO
iostat -x 1

# 检查网络带宽
iftop -i eth0

# MinIO性能指标
mc admin info leafpan
```

### 高可用部署

#### 1. 分布式部署
```bash
# 启动分布式MinIO集群（4节点）
minio server http://minio{1...4}.example.com/data{1...4}
```

#### 2. Kubernetes部署
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: minio
spec:
  replicas: 4
  selector:
    matchLabels:
      app: minio
  template:
    metadata:
      labels:
        app: minio
    spec:
      containers:
      - name: minio
        image: minio/minio:latest
        args:
        - server
        - --console-address
        - ":9001"
        - http://minio-{0...3}.minio-hl.default.svc.cluster.local/data
        env:
        - name: MINIO_ROOT_USER
          value: "minioadmin"
        - name: MINIO_ROOT_PASSWORD
          value: "minioadmin"
```

详细配置请参考 [MINIO_CONFIG.md](MINIO_CONFIG.md)

### 认证接口

#### 1. 用户登录
**接口地址**: `POST /api/auth/login`

**请求参数**:
```json
{
  "username": "admin",
  "password": "password123"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userInfo": {
      "id": 1,
      "username": "admin",
      "email": "admin@leafpan.com",
      "nickname": "管理员",
      "avatar": "/avatar/default.png",
      "storageQuota": 10737418240,
      "usedStorage": 10485760
    }
  }
}
```

#### 2. 用户注册
**接口地址**: `POST /api/auth/register`

**请求参数**:
```json
{
  "username": "newuser",
  "email": "newuser@example.com",
  "password": "password123",
  "nickname": "新用户"
}
```

#### 3. 刷新令牌
**接口地址**: `POST /api/auth/refresh`

**请求参数**:
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### 4. 用户登出
**接口地址**: `POST /api/auth/logout`

**请求头**:
```
Authorization: Bearer {token}
```

### 文件管理接口

#### 1. 文件上传
**接口地址**: `POST /api/files/upload`

**请求参数**:
- `file`: 文件数据（multipart/form-data）
- `folderId`: 文件夹ID（可选）
- `isPublic`: 是否公开（可选，默认false）

**响应示例**:
```json
{
  "code": 200,
  "message": "上传成功",
  "data": {
    "id": 123,
    "name": "document.pdf",
    "size": 1048576,
    "url": "/api/files/download/123",
    "createdAt": "2024-01-15T10:30:00Z"
  }
}
```

#### 2. 文件下载
**接口地址**: `GET /api/files/download/{fileId}`

**请求头**:
```
Authorization: Bearer {token}
```

**响应**: 文件流

#### 3. 文件列表
**接口地址**: `GET /api/files`

**查询参数**:
- `folderId`: 文件夹ID（可选）
- `page`: 页码（默认1）
- `size`: 每页大小（默认20）
- `keyword`: 搜索关键词（可选）

**响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "list": [
      {
        "id": 123,
        "name": "document.pdf",
        "size": 1048576,
        "mimeType": "application/pdf",
        "createdAt": "2024-01-15T10:30:00Z"
      }
    ],
    "total": 1,
    "page": 1,
    "size": 20
  }
}
```

#### 4. 文件删除
**接口地址**: `DELETE /api/files/{fileId}`

**请求头**:
```
Authorization: Bearer {token}
```

#### 5. 文件重命名
**接口地址**: `PUT /api/files/{fileId}/rename`

**请求参数**:
```json
{
  "newName": "new_document.pdf"
}
```

#### 6. 文件移动
**接口地址**: `PUT /api/files/{fileId}/move`

**请求参数**:
```json
{
  "targetFolderId": 456
}
```

### 文件夹管理接口

#### 1. 创建文件夹
**接口地址**: `POST /api/folders`

**请求参数**:
```json
{
  "name": "新文件夹",
  "parentId": 123
}
```

#### 2. 删除文件夹
**接口地址**: `DELETE /api/folders/{folderId}`

#### 3. 文件夹列表
**接口地址**: `GET /api/folders`

**查询参数**:
- `parentId`: 父文件夹ID（可选）

#### 4. 文件夹重命名
**接口地址**: `PUT /api/folders/{folderId}/rename`

**请求参数**:
```json
{
  "newName": "新文件夹名"
}
```

### 分享管理接口

#### 1. 创建分享
**接口地址**: `POST /api/shares`

**请求参数**:
```json
{
  "fileId": 123,
  "expireAt": "2024-12-31T23:59:59Z",
  "password": "share123",
  "maxDownloads": 100,
  "isPublic": true
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "分享创建成功",
  "data": {
    "shareCode": "ABC123DEF",
    "shareUrl": "https://leafpan.com/share/ABC123DEF",
    "expireAt": "2024-12-31T23:59:59Z"
  }
}
```

#### 2. 获取分享信息
**接口地址**: `GET /api/shares/{shareCode}`

**响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "file": {
      "name": "document.pdf",
      "size": 1048576,
      "createdAt": "2024-01-15T10:30:00Z"
    },
    "share": {
      "expireAt": "2024-12-31T23:59:59Z",
      "downloadCount": 5,
      "maxDownloads": 100,
      "hasPassword": true
    }
  }
}
```

#### 3. 下载分享文件
**接口地址**: `GET /api/shares/{shareCode}/download`

**请求参数**:
```json
{
  "password": "share123"
}
```

#### 4. 删除分享
**接口地址**: `DELETE /api/shares/{shareCode}`

### 用户管理接口

#### 1. 获取用户信息
**接口地址**: `GET /api/users/profile`

**响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 1,
    "username": "admin",
    "email": "admin@leafpan.com",
    "nickname": "管理员",
    "avatar": "/avatar/default.png",
    "storageQuota": 10737418240,
    "usedStorage": 10485760,
    "lastLoginAt": "2024-01-15T10:30:00Z"
  }
}
```

#### 2. 更新用户信息
**接口地址**: `PUT /api/users/profile`

**请求参数**:
```json
{
  "nickname": "新昵称",
  "avatar": "/avatar/new.png"
}
```

#### 3. 修改密码
**接口地址**: `PUT /api/users/password`

**请求参数**:
```json
{
  "oldPassword": "old123",
  "newPassword": "new123"
}
```

### 系统管理接口

#### 1. 获取系统统计
**接口地址**: `GET /api/admin/stats`

**响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "totalUsers": 1000,
    "totalFiles": 50000,
    "totalStorage": 536870912000,
    "activeUsers": 150,
    "todayUploads": 250
  }
}
```

#### 2. 获取操作日志
**接口地址**: `GET /api/admin/logs`

**查询参数**:
- `page`: 页码（默认1）
- `size`: 每页大小（默认20）
- `userId`: 用户ID（可选）
- `operationType`: 操作类型（可选）

### API错误码说明

| 错误码 | 说明 | 处理建议 |
|--------|------|----------|
| 200 | 成功 | 操作成功 |
| 400 | 请求参数错误 | 检查请求参数格式 |
| 401 | 未授权 | 检查token是否有效 |
| 403 | 权限不足 | 检查用户权限 |
| 404 | 资源不存在 | 检查资源ID是否正确 |
| 500 | 服务器内部错误 | 联系系统管理员 |

### API调用示例

#### JavaScript (Fetch)
```javascript
// 用户登录
const login = async (username, password) => {
  const response = await fetch('/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ username, password })
  });
  return await response.json();
};

// 文件上传
const uploadFile = async (file, folderId = null) => {
  const formData = new FormData();
  formData.append('file', file);
  if (folderId) formData.append('folderId', folderId);
  
  const response = await fetch('/api/files/upload', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`
    },
    body: formData
  });
  return await response.json();
};
```

#### Python (Requests)
```python
import requests

# 用户登录
response = requests.post('http://localhost:8080/api/auth/login', json={
    'username': 'admin',
    'password': 'password123'
})
token = response.json()['data']['token']

# 文件上传
files = {'file': open('document.pdf', 'rb')}
headers = {'Authorization': f'Bearer {token}'}
response = requests.post('http://localhost:8080/api/files/upload', 
                        files=files, headers=headers)
```

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

### 如何贡献

#### 1. 报告问题

**Bug报告模板**:
```markdown
## 问题描述
[清晰描述遇到的问题]

## 复现步骤
1. 第一步
2. 第二步
3. ...

## 期望行为
[描述期望的正确行为]

## 实际行为
[描述实际发生的错误行为]

## 环境信息
- 操作系统: [e.g. Windows 10, macOS 12.0]
- 浏览器: [e.g. Chrome 96, Firefox 95]
- 项目版本: [e.g. v1.0.0]

## 附加信息
[截图、日志文件、相关配置等]
```

**功能建议模板**:
```markdown
## 功能描述
[详细描述建议的功能]

## 使用场景
[描述该功能的使用场景和用户价值]

## 实现建议
[如果有实现建议，可以在这里描述]

## 相关功能
[描述与现有功能的关联性]
```

#### 2. 提交代码

**分支命名规范**:
- `feature/` - 新功能开发
- `bugfix/` - Bug修复
- `hotfix/` - 紧急修复
- `docs/` - 文档更新
- `refactor/` - 代码重构

**提交信息规范**:
```bash
# 格式: <类型>(<范围>): <描述>

# 示例:
feat(auth): 添加OAuth2登录支持
fix(file): 修复文件上传大小限制问题
docs(readme): 更新安装说明
refactor(api): 重构用户管理接口
```

**Pull Request流程**:
1. Fork项目仓库到个人账户
2. 创建功能分支: `git checkout -b feature/your-feature`
3. 开发并测试功能
4. 提交更改: `git commit -m 'feat: 添加新功能'`
5. 推送到远程: `git push origin feature/your-feature`
6. 创建Pull Request
7. 等待代码审查和合并

#### 3. 代码规范

**后端代码规范**:
```java
// 类命名规范
public class UserService {
    
    // 方法命名规范
    public UserDTO getUserById(Long userId) {
        // 业务逻辑
    }
    
    // 常量命名规范
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 100;
}
```

**前端代码规范**:
```typescript
// 组件命名规范
const UserProfile: React.FC<UserProfileProps> = ({ user }) => {
    // 状态命名规范
    const [isLoading, setIsLoading] = useState(false);
    
    // 函数命名规范
    const handleUserUpdate = async (userData: UserData) => {
        // 业务逻辑
    };
    
    return (
        <div className="user-profile">
            {/* JSX结构 */}
        </div>
    );
};
```

**注释规范**:
```java
/**
 * 用户服务类
 * 
 * @author Your Name
 * @version 1.0
 * @since 2024-01-01
 */
@Service
public class UserService {
    
    /**
     * 根据用户ID获取用户信息
     * 
     * @param userId 用户ID
     * @return 用户数据传输对象
     * @throws UserNotFoundException 用户不存在时抛出异常
     */
    public UserDTO getUserById(Long userId) {
        // 实现逻辑
    }
}
```

### 开发环境搭建

#### 1. 后端开发环境

**环境要求**:
- Java 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

**设置步骤**:
```bash
# 克隆项目
git clone https://github.com/your-username/leafpan.git

# 进入后端目录
cd leafpan/backend

# 安装依赖
mvn clean install -DskipTests

# 配置数据库
mysql -u root -p < docs/sql/schema.sql
mysql -u root -p < docs/sql/data.sql

# 配置应用属性
cp src/main/resources/application.yml.example src/main/resources/application.yml
# 编辑 application.yml 文件，配置数据库连接等信息

# 启动开发服务器
mvn spring-boot:run
```

**开发工具配置**:
```xml
<!-- pom.xml 开发依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

#### 2. 前端开发环境

**环境要求**:
- Node.js 16+
- npm 8+

**设置步骤**:
```bash
# 进入前端目录
cd leafpan/frontend

# 安装依赖
npm install

# 配置环境变量
cp .env.example .env
# 编辑 .env 文件，配置API地址等信息

# 启动开发服务器
npm run dev
```

**开发工具配置**:
```json
// package.json 开发脚本
{
  "scripts": {
    "dev": "vite",
    "build": "vue-tsc --noEmit && vite build",
    "preview": "vite preview",
    "lint": "eslint . --ext .vue,.js,.jsx,.cjs,.mjs,.ts,.tsx,.cts,.mts --fix --ignore-path .gitignore"
  }
}
```

### 测试规范

#### 1. 后端测试

**单元测试**:
```java
@SpringBootTest
class UserServiceTest {
    
    @Autowired
    private UserService userService;
    
    @Test
    @DisplayName("根据ID获取用户信息 - 成功")
    void testGetUserById_Success() {
        // 准备测试数据
        Long userId = 1L;
        
        // 执行测试
        UserDTO result = userService.getUserById(userId);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(userId, result.getId());
    }
}
```

**集成测试**:
```java
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserIntegrationTest {
    
    @Test
    @Sql("/sql/test-users.sql")
    void testUserCreationFlow() {
        // 集成测试逻辑
    }
}
```

**测试覆盖率要求**:
- 单元测试覆盖率: ≥80%
- 集成测试覆盖率: ≥70%
- 整体测试覆盖率: ≥75%

#### 2. 前端测试

**组件测试**:
```typescript
import { render, screen } from '@testing-library/vue';
import UserProfile from './UserProfile.vue';

describe('UserProfile', () => {
    test('显示用户信息', () => {
        const user = { name: '张三', email: 'zhangsan@example.com' };
        
        render(UserProfile, { props: { user } });
        
        expect(screen.getByText('张三')).toBeInTheDocument();
        expect(screen.getByText('zhangsan@example.com')).toBeInTheDocument();
    });
});
```

**E2E测试**:
```typescript
describe('用户登录流程', () => {
    it('应该成功登录并跳转到仪表板', () => {
        cy.visit('/login');
        cy.get('[data-testid=username]').type('testuser');
        cy.get('[data-testid=password]').type('password123');
        cy.get('[data-testid=login-button]').click();
        cy.url().should('include', '/dashboard');
    });
});
```

### 代码审查流程

#### 1. 审查标准

**功能完整性**:
- [ ] 功能按需求实现完整
- [ ] 边界情况处理完善
- [ ] 错误处理机制健全

**代码质量**:
- [ ] 代码符合项目规范
- [ ] 命名清晰易懂
- [ ] 函数职责单一
- [ ] 无重复代码

**测试覆盖**:
- [ ] 单元测试覆盖核心逻辑
- [ ] 集成测试覆盖关键流程
- [ ] 测试用例设计合理

**文档完整性**:
- [ ] 代码注释清晰
- [ ] API文档更新
- [ ] README文档更新

#### 2. 审查流程

1. **创建PR**: 开发者创建Pull Request
2. **自动检查**: CI/CD流水线自动运行测试和检查
3. **代码审查**: 至少需要2名核心开发者审查通过
4. **修改反馈**: 根据审查意见修改代码
5. **最终合并**: 审查通过后由维护者合并

### 发布流程

#### 1. 版本管理

**语义化版本**:
- **主版本号**: 不兼容的API修改
- **次版本号**: 向下兼容的功能性新增
- **修订号**: 向下兼容的问题修正

**发布分支策略**:
```
main        - 稳定版本分支
develop     - 开发分支
release/*   - 发布准备分支
hotfix/*    - 热修复分支
```

#### 2. 发布检查清单

**功能检查**:
- [ ] 所有功能测试通过
- [ ] 性能测试达标
- [ ] 安全扫描通过

**文档检查**:
- [ ] 更新变更日志
- [ ] 更新API文档
- [ ] 更新安装指南

**部署检查**:
- [ ] Docker镜像构建成功
- [ ] 部署脚本测试通过
- [ ] 回滚方案准备就绪

### 社区交流

#### 1. 沟通渠道

- **GitHub Issues**: 技术讨论和问题报告
- **Discord/Slack**: 实时技术交流
- **邮件列表**: 重要公告和讨论

#### 2. 行为准则

**社区准则**:
1. 尊重所有社区成员
2. 建设性讨论技术问题
3. 帮助新成员融入社区
4. 遵守开源协议规定

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