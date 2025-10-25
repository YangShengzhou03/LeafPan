FROM nginx:alpine

# 复制nginx配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 复制前端静态文件
COPY . /usr/share/nginx/html

EXPOSE 80