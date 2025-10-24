# 直接使用Nginx作为基础镜像，复用GitHub Actions中已构建的dist目录
FROM nginx:alpine as production-stage

# 复制已构建好的dist目录到Nginx目录
COPY ./dist /usr/share/nginx/html

# 复制Nginx配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 暴露端口
EXPOSE 80

# 启动Nginx
CMD ["nginx", "-g", "daemon off;"]