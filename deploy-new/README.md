# LeafPan 部署指南

这是一个简化的Docker部署配置，用于部署LeafPan应用。

## 快速开始

1. 确保已安装Docker和Docker Compose

2. 构建前端应用：
   ```bash
   cd frontend
   npm install
   npm run build
   ```

3. 启动所有服务：
   ```bash
   cd deploy-new
   docker-compose up -d
   ```

4. 访问应用：
   - 前端：http://localhost
   - 后端API：http://localhost:8080

## 服务说明

- **frontend**: 基于nginx的Vue前端应用，端口80
- **backend**: Spring Boot后端应用，端口8080
- **mysql**: MySQL数据库，端口3306

## 常用命令

- 查看服务状态：`docker-compose ps`
- 查看日志：`docker-compose logs [service-name]`
- 停止服务：`docker-compose down`
- 重新构建并启动：`docker-compose up -d --build`

## 注意事项

1. 首次启动前，请确保已构建前端应用（frontend/dist目录存在）
2. 如果修改了前端代码，需要重新构建并重启前端服务：
   ```bash
   cd frontend && npm run build
   cd ../deploy-new && docker-compose up -d --build frontend
   ```
3. 如果修改了后端代码，需要重新构建并重启后端服务：
   ```bash
   cd ../deploy-new && docker-compose up -d --build backend
   ```

## 故障排查

如果前端静态资源无法访问，请检查：

1. 前端是否已构建：`ls -la ../frontend/dist`
2. 容器内文件是否存在：`docker exec -it leafpan-frontend ls -la /usr/share/nginx/html`
3. nginx配置是否正确：`docker exec -it leafpan-frontend cat /etc/nginx/conf.d/default.conf`