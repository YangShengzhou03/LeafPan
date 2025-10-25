# 直接使用 Nginx 基础镜像，不需要 Node 环境
FROM nginx:alpine

# 把本地的 nginx.conf 复制到容器的 Nginx 配置目录
COPY deploy/frontend/nginx.conf /etc/nginx/conf.d/default.conf

# 从项目根目录的 frontend/dist 复制所有文件到 Nginx 静态目录
# 这样 css/、js/、index.html 会直接在 /usr/share/nginx/html 下
COPY frontend/dist/ /usr/share/nginx/html/

# 暴露 80 端口
EXPOSE 80

# Nginx 容器默认会启动，不需要额外的 ENTRYPOINT