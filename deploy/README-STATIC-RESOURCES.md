# LeafPan 前端静态资源404问题修复指南

## 问题描述

前端静态资源（CSS/JS文件）请求始终返回 **404 Not Found**，涉及文件包括：
- `css/app.a6883c13.css`
- `js/app.8a5f9e77.js`
- `js/vendors.f55ba6d7.js`

## 修复内容

### 1. Nginx配置修复

在 `deploy/frontend/nginx.conf` 中，为静态资源处理规则添加了 `root` 指令：

```nginx
location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot)$ {
    root /usr/share/nginx/html;  # 添加此行
    expires 1y;
    add_header Cache-Control "public, immutable";
}
```

### 2. Docker构建上下文修复

修改了 `deploy/docker-compose.yml` 中的构建上下文路径：

```yaml
frontend:
  build:
    context: ..  # 修改为项目根目录
    dockerfile: deploy/frontend/Dockerfile
```

相应地，修改了 `deploy/frontend/Dockerfile` 中的复制路径：

```dockerfile
COPY deploy/frontend/nginx.conf /etc/nginx/conf.d/default.conf
COPY frontend/dist/ /usr/share/nginx/html/
```

## 部署步骤

1. **构建前端**：
   ```bash
   cd frontend
   npm install
   npm run build
   ```

2. **部署应用**：
   ```bash
   cd deploy
   docker-compose up -d --build
   ```

3. **验证静态资源**：
   ```bash
   # Linux/Mac
   ./test-static-resources.sh
   
   # Windows
   .\test-static-resources.ps1
   
   # 或者指定服务器地址
   .\test-static-resources.ps1 -Server your-server-ip
   ```

## 测试脚本

提供了两个测试脚本：
- `test-static-resources.sh` - Linux/Mac版本
- `test-static-resources.ps1` - Windows PowerShell版本

这些脚本会测试以下URL：
- 主页: `http://server/`
- CSS文件: `http://server/css/app.a6883c13.css`
- JS文件: `http://server/js/app.8a5f9e77.js`
- 其他静态资源

## 故障排查

如果测试失败，可以尝试以下排查步骤：

1. **检查容器状态**：
   ```bash
   docker ps
   ```

2. **检查容器内文件**：
   ```bash
   docker exec -it leafpan-frontend ls -la /usr/share/nginx/html/
   ```

3. **检查Nginx配置**：
   ```bash
   docker exec -it leafpan-frontend cat /etc/nginx/conf.d/default.conf
   ```

4. **查看Nginx日志**：
   ```bash
   docker logs leafpan-frontend
   ```

5. **重新构建容器**：
   ```bash
   docker-compose down frontend
   docker-compose up -d --build frontend
   ```

## 原理说明

1. **Nginx配置问题**：原始配置中，静态资源的location块缺少root指令，导致Nginx不知道从哪里查找静态文件。

2. **Docker构建上下文问题**：原始配置中，Docker构建上下文是`./frontend`，但该目录下没有dist目录，导致文件复制失败。

通过以上修复，确保了静态资源能够被正确复制到容器中，并且Nginx能够正确找到并服务这些资源。