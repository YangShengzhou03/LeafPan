# LeafPan Kubernetes 部署说明

## 部署要求

- Kubernetes 集群 1.19+
- Ingress Controller (推荐使用 Nginx Ingress)
- 持久化存储支持 (MinIO需要)

## 部署顺序

请按照以下顺序部署服务：

1. **创建命名空间**
   ```bash
   kubectl apply -f namespace.yaml
   ```

2. **部署MySQL数据库**
   ```bash
   kubectl apply -f mysql-delop.yaml
   ```
   
   MySQL将自动执行初始化脚本，创建数据库和表结构。

3. **部署MinIO对象存储**
   ```bash
   kubectl apply -f minio-delop.yaml
   ```
   
   MinIO将自动创建存储桶并设置访问策略。

4. **部署后端服务**
   ```bash
   kubectl apply -f backend-delop.yaml
   ```
   
   后端服务将启动2个副本，实现负载均衡。

5. **部署前端服务**
   ```bash
   kubectl apply -f frontend-delop.yaml
   ```

## 服务访问

### 域名配置
在本地hosts文件中添加以下记录：
```
127.0.0.1 leafpan.local
127.0.0.1 minio.leafpan.local
```

### 访问地址
- **前端应用**: http://leafpan.local
- **后端API**: http://leafpan.local/api
- **MinIO控制台**: http://minio.leafpan.local

## 资源配置

### 副本数量
- 前端服务: 1个副本
- 后端服务: 2个副本 (负载均衡)
- MinIO服务: 1个副本
- MySQL服务: 1个副本

### 资源限制
- **前端**: 128Mi/256Mi (请求/限制), 100m/200m CPU
- **后端**: 512Mi/1Gi (请求/限制), 250m/500m CPU  
- **MySQL**: 512Mi/1Gi (请求/限制), 250m/500m CPU
- **MinIO**: 256Mi/512Mi (请求/限制), 250m/500m CPU

## 健康检查

所有服务都配置了健康检查：
- **前端**: HTTP GET /
- **后端**: HTTP GET /actuator/health
- **MySQL**: mysqladmin ping 和 mysql SELECT 1
- **MinIO**: HTTP GET /minio/health/live 和 /minio/health/ready

## 持久化存储

- **MinIO**: 使用32Gi持久化存储卷
- **MySQL**: 不使用持久化存储（根据要求）

## 注意事项

1. 确保Ingress Controller已正确安装和配置
2. 首次部署时，等待MySQL初始化完成后再部署后端服务
3. MinIO存储桶创建可能需要30秒左右时间
4. 后端服务依赖MySQL和MinIO，确保依赖服务正常运行

## 监控和日志

使用以下命令查看服务状态：
```bash
# 查看所有Pod状态
kubectl get pods -n leafpan

# 查看服务状态
kubectl get services -n leafpan

# 查看Ingress状态
kubectl get ingress -n leafpan

# 查看Pod日志
kubectl logs <pod-name> -n leafpan
```