# LeafPan Docker Compose 快速部署指南

## 概述

本指南介绍如何使用 Docker Compose 快速部署 LeafPan 项目到服务器。

## 部署流程

### 1. 本地构建镜像

#### Windows 系统
```bash
cd docker-compose\scripts
build-images.bat
```

#### Linux/Mac 系统
```bash
cd docker-compose/scripts
chmod +x build-images.sh
./build-images.sh
```

### 2. 准备服务器环境

确保服务器已安装：
- Docker
- Docker Compose

### 3. 复制文件到服务器

将整个 `docker-compose` 文件夹复制到服务器：
```bash
scp -r docker-compose/ user@server_ip:/path/to/leafpan/
```

### 4. 配置环境变量

在服务器上编辑环境变量文件：
```bash
cd docker-compose
cp .env.example .env
nano .env  # 编辑配置文件
```

重要配置项：
- `MYSQL_ROOT_PASSWORD`: MySQL root 密码
- `MYSQL_PASSWORD`: 应用数据库用户密码
- `MINIO_ROOT_PASSWORD`: MinIO 管理员密码

### 5. 部署到服务器

```bash
cd docker-compose/scripts
chmod +x deploy.sh
./deploy.sh
```

## 服务访问地址

部署成功后，可以通过以下地址访问服务：

- **前端应用**: http://服务器IP:80
- **后端API**: http://服务器IP:8080
- **MinIO控制台**: http://服务器IP:9001
- **MySQL数据库**: 服务器IP:3306

## 管理命令

### 查看服务状态
```bash
docker-compose -f docker-compose.prod.yml ps
```

### 查看服务日志
```bash
# 查看所有服务日志
docker-compose -f docker-compose.prod.yml logs

# 查看特定服务日志
docker-compose -f docker-compose.prod.yml logs backend
docker-compose -f docker-compose.prod.yml logs frontend
```

### 停止服务
```bash
docker-compose -f docker-compose.prod.yml down
```

### 重启服务
```bash
docker-compose -f docker-compose.prod.yml restart
```

## 故障排除

### 1. 端口冲突
如果端口被占用，可以修改 `docker-compose.prod.yml` 中的端口映射。

### 2. 镜像加载失败
确保镜像文件存在且完整：
```bash
ls -la images/frontend/
ls -la images/backend/
```

### 3. 数据库连接失败
检查 MySQL 服务是否正常启动：
```bash
docker logs leafpan-mysql
```

### 4. 前端无法访问后端 API
检查网络配置和代理设置：
```bash
docker network ls
docker exec leafpan-frontend nginx -t
```

## 备份和恢复

### 备份数据库
```bash
docker exec leafpan-mysql mysqldump -u root -p leafpan > backup.sql
```

### 恢复数据库
```bash
docker exec -i leafpan-mysql mysql -u root -p leafpan < backup.sql
```

### 备份 MinIO 数据
MinIO 数据存储在 Docker 卷中，备份卷数据：
```bash
docker volume ls
docker run --rm -v minio_data:/data -v $(pwd):/backup alpine tar czf /backup/minio_backup.tar.gz /data
```

## 更新部署

当有新版本需要部署时：

1. 在本地重新构建镜像
2. 将新的镜像文件复制到服务器
3. 在服务器上重新运行部署脚本

```bash
# 在服务器上
cd docker-compose/scripts
./deploy.sh
```

## 安全建议

1. 修改所有默认密码
2. 配置防火墙规则
3. 启用 HTTPS
4. 定期更新镜像和安全补丁
5. 配置日志轮转和监控

---

如有问题，请查看详细日志或联系开发团队。