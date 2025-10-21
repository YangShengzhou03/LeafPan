# 🍁 LeafPan - Modern Cloud Storage Solution

<div align="center">

[![GitHub stars](https://img.shields.io/github/stars/YangShengzhou03/LeafPan?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/LeafPan/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/YangShengzhou03/LeafPan?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/LeafPan/network)
[![GitHub issues](https://img.shields.io/github/issues/YangShengzhou03/LeafPan?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/LeafPan/issues)
[![GitHub license](https://img.shields.io/github/license/YangShengzhou03/LeafPan?style=for-the-badge)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen?style=for-the-badge&logo=spring)](https://spring.io/projects/spring-boot)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.x-green?style=for-the-badge&logo=vue.js)](https://vuejs.org/)

**A modern open-source cloud storage solution**

[English](README_EN.md) | [中文](README.md) | [Documentation](https://github.com/YangShengzhou03/LeafPan/wiki) | [Demo](https://leafpan.demo.com) | [Download](https://github.com/YangShengzhou03/LeafPan/releases)

</div>

## 📖 Overview

LeafPan is an open-source cloud storage solution built with modern web technologies, providing enterprise-grade file management, sharing, and collaboration features. The project adopts a frontend-backend separation architecture and supports multi-tenancy, high concurrency, and distributed deployment.

## 🚀 Core Features

### ✨ Main Features
- 🔐 **Secure Authentication** - JWT token authentication with multi-factor support
- 📁 **File Management** - Multi-level directory structure, drag-and-drop upload, batch operations
- 🔗 **File Sharing** - Public/private links, password protection, expiration time
- 📊 **Storage Analytics** - Real-time storage monitoring, usage analysis
- 🔍 **Full-text Search** - Elasticsearch-based file content search
- 📱 **Multi-platform Support** - Web, mobile, and desktop clients

### 🛠️ Technical Features
- ⚡ **High Performance** - Support for large file resumable uploads, multi-threaded upload/download
- 🔒 **Data Security** - File encryption storage, access control
- 📈 **Scalability** - Microservices architecture, horizontal scaling support
- 🐳 **Containerization** - Docker one-click deployment, Kubernetes support

## 🛠️ Technology Stack

### 🎨 Frontend
- **Vue 3** - Modern frontend framework with Composition API
- **Element Plus** - Enterprise UI component library
- **TypeScript** - Type-safe JavaScript superset
- **Vite** - Next-generation frontend build tool
- **Pinia** - Vue state management library
- **Axios** - HTTP client library
- **Tailwind CSS** - Utility-first CSS framework

### ⚙️ Backend
- **Spring Boot 3.5.6** - Java enterprise application framework
- **Spring Security** - Security authentication and authorization framework
- **Spring Data JPA** - Data persistence solution
- **MySQL 8.0+** - Relational database
- **Redis** - Cache and session storage
- **MinIO** - High-performance object storage service
- **Elasticsearch** - Distributed search and analytics engine
- **RabbitMQ** - Message queue for asynchronous processing

### 🐳 Deployment & Operations
- **Docker** - Containerized deployment
- **Docker Compose** - Multi-container application orchestration
- **Kubernetes** - Container orchestration platform (optional)
- **Nginx** - Reverse proxy and load balancing
- **Prometheus** - Monitoring and alerting system
- **Grafana** - Data visualization platform

## 🚀 Quick Start

### 📋 Prerequisites

Before you begin, ensure your system meets the following requirements:

- **Java 17+** - [Download](https://adoptium.net/)
- **MySQL 8.0+** - [Download](https://dev.mysql.com/downloads/mysql/)
- **Node.js 18+** - [Download](https://nodejs.org/)
- **Docker & Docker Compose** - [Download](https://docs.docker.com/get-docker/)
- **MinIO** - Object storage service (included in Docker configuration)

### 🐳 Quick Deployment with Docker (Recommended)

1. **Clone the project**
```bash
git clone https://github.com/YangShengzhou03/LeafPan.git
cd LeafPan
```

2. **Configure environment variables**
```bash
cp .env.example .env
# Edit .env file to configure database connection, etc.
```

3. **Start all services**
```bash
docker-compose up -d
```

4. **Access the application**
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- MinIO Console: http://localhost:9001
- Database Admin: http://localhost:8081 (Adminer)

### 🔧 Manual Installation

#### Backend Deployment

1. **Configure database**
```sql
CREATE DATABASE leafpan CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. **Configure application**
```bash
cd backend
cp src/main/resources/application.yml.example src/main/resources/application.yml
# Edit application.yml to configure database and MinIO settings
```

3. **Build and run**
```bash
# Using Maven Wrapper
./mvnw clean package
java -jar target/leafpan-backend-1.0.0.jar

# Or using Spring Boot Maven plugin
./mvnw spring-boot:run
```

#### Frontend Deployment

1. **Install dependencies**
```bash
cd frontend
npm install
```

2. **Configure environment**
```bash
cp .env.example .env
# Edit .env file to configure API address, etc.
```

3. **Run in development mode**
```bash
npm run dev
```

4. **Production build**
```bash
npm run build
npm run preview  # Preview production version
```

### 🧪 Testing

Run tests to ensure everything works correctly:

```bash
# Backend tests
cd backend
./mvnw test

# Frontend tests
cd frontend
npm run test
```

## 🤝 Contributing

We welcome community contributions! Please read our [Contributing Guide](CONTRIBUTING.md) to learn how to participate in project development.

### Ways to Contribute
- Report bugs and issues
- Suggest new features
- Submit code improvements
- Improve documentation
- Help with testing

## 📄 License

This project is licensed under the [MIT License](LICENSE).

## 🏆 Community & Support

### 💬 Community Channels
- [GitHub Discussions](https://github.com/YangShengzhou03/LeafPan/discussions)
- [Discord Channel](https://discord.gg/leafpan)
- [Mailing List](mailto:leafpan-dev@googlegroups.com)

### 📊 Project Statistics

[![GitHub Contributors](https://img.shields.io/github/contributors/YangShengzhou03/LeafPan?style=flat-square)](https://github.com/YangShengzhou03/LeafPan/graphs/contributors)
[![GitHub Commit Activity](https://img.shields.io/github/commit-activity/m/YangShengzhou03/LeafPan?style=flat-square)](https://github.com/YangShengzhou03/LeafPan/pulse)
[![GitHub Last Commit](https://img.shields.io/github/last-commit/YangShengzhou03/LeafPan?style=flat-square)](https://github.com/YangShengzhou03/LeafPan/commits/main)

---

<div align="center">

**If this project helps you, please give it a ⭐️ Star!**

[⬆ Back to top](#-leafpan---modern-cloud-storage-solution)

</div>