# 使用Node.js作为构建环境
FROM node:18-alpine as build-stage

# 设置工作目录
WORKDIR /app

# 安装编译工具和系统依赖（针对Alpine镜像）
RUN apk add --no-cache git python3 make g++ libpng-dev

# 复制package.json和package-lock.json
COPY package*.json ./

# 安装依赖（使用ci模式，确保依赖一致性）
RUN npm ci --prefer-offline --no-audit

# 复制源代码
COPY . .

# 构建应用（设置内存限制和超时）
RUN npm run build

# 使用Nginx作为生产环境
FROM nginx:alpine as production-stage

# 复制构建好的文件到Nginx目录
COPY --from=build-stage /app/dist /usr/share/nginx/html

# 复制Nginx配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 暴露端口
EXPOSE 80

# 启动Nginx
CMD ["nginx", "-g", "daemon off;"]