# 基于Java 17轻量镜像（slim版本适合生产环境）
FROM openjdk:17-jre-slim

# 暴露后端服务端口（假设后端默认端口是8080，根据实际情况修改）
EXPOSE 8080

# 将本地jar包复制到容器内（确保jar包名称和实际一致，如backend.jar）
COPY backend.jar /app/backend.jar

# 容器启动命令（运行jar包，可添加自定义参数，如端口）
ENTRYPOINT ["java", "-jar", "/app/backend.jar"]