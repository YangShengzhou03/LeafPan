# 环境配置说明

本项目已配置多环境支持，包括开发环境和生产环境。

## 配置文件结构

- `application.properties` - 默认配置，指定激活的开发环境
- `application.yml` - 所有环境共享的通用配置
- `application-dev.yml` - 开发环境专用配置
- `application-prod.yml` - 生产环境专用配置

## 开发环境配置

开发环境配置了以下特性：

1. **数据库配置**
   - 本地MySQL数据库
   - JPA自动更新表结构
   - 显示SQL语句

2. **日志配置**
   - 详细的DEBUG级别日志
   - 控制台输出格式化日志

3. **服务配置**
   - 本地MinIO对象存储
   

## 生产环境配置

生产环境配置了以下特性：

1. **数据库配置**
   - 使用环境变量配置密码
   - 连接池优化
   - JPA验证表结构

2. **日志配置**
   - INFO级别日志
   - 日志文件输出
   - 日志文件滚动策略

3. **服务配置**
   - 生产环境服务地址
   - 性能优化参数

## 使用方法

### 开发环境

1. 启动开发环境依赖服务：
   ```bash
   docker-compose -f docker-compose.dev.yml up -d
   ```

2. 启动后端应用：
   ```bash
   cd backend
   mvn spring-boot:run
   ```
   或者直接在IDE中运行主类

3. 应用将自动使用开发环境配置

### 生产环境

1. 设置环境变量：
   ```bash
   export MYSQL_PASSWORD=your_mysql_password
   
   export MINIO_ACCESS_KEY=your_minio_access_key
   export MINIO_SECRET_KEY=your_minio_secret_key
   export JWT_SECRET=your_jwt_secret
   ```

2. 使用生产环境配置启动：
   ```bash
   java -jar -Dspring.profiles.active=prod your-app.jar
   ```

## 环境变量说明

生产环境支持以下环境变量：

- `MYSQL_PASSWORD` - MySQL数据库密码

- `MINIO_ACCESS_KEY` - MinIO访问密钥
- `MINIO_SECRET_KEY` - MinIO秘密密钥
- `EMAIL_SENDER` - 邮件发送者地址
- `EMAIL_PASSWORD` - 邮件密码
- `EMAIL_SMTP_SERVER` - SMTP服务器地址
- `EMAIL_SMTP_PORT` - SMTP服务器端口
- `JWT_SECRET` - JWT密钥

## 注意事项

1. 生产环境请务必修改默认的JWT密钥
2. 生产环境请使用强密码
3. 生产环境请确保HTTPS配置
4. 生产环境请配置防火墙规则