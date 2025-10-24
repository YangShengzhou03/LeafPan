#!/bin/bash

# LeafPan Kubernetes部署脚本
# 用法: ./deploy.sh [environment]
# environment: dev|staging|production (默认: production)

set -e

# 配置变量
ENVIRONMENT=${1:-production}
NAMESPACE="leafpan"
TIMESTAMP=$(date +%Y%m%d%H%M%S)

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${GREEN}[INFO]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

# 检查kubectl是否安装
check_kubectl() {
    if ! command -v kubectl &> /dev/null; then
        log_error "kubectl未安装，请先安装kubectl"
        exit 1
    fi
    log_info "kubectl版本: $(kubectl version --client --short)"
}

# 检查Kubernetes集群连接
check_cluster() {
    log_info "检查Kubernetes集群连接..."
    if ! kubectl cluster-info &> /dev/null; then
        log_error "无法连接到Kubernetes集群"
        exit 1
    fi
    log_info "集群信息: $(kubectl cluster-info | head -n1)"
}

# 创建命名空间
create_namespace() {
    log_info "检查命名空间 ${NAMESPACE}..."
    if ! kubectl get namespace "${NAMESPACE}" &> /dev/null; then
        log_info "创建命名空间 ${NAMESPACE}..."
        kubectl apply -f namespace.yaml
        sleep 2
    else
        log_info "命名空间 ${NAMESPACE} 已存在"
    fi
}

# 部署MySQL
deploy_mysql() {
    log_info "部署MySQL..."
    kubectl apply -f mysql-delop.yaml
    
    # 等待MySQL启动
    log_info "等待MySQL启动..."
    kubectl wait --for=condition=ready pod -l app=mysql -n "${NAMESPACE}" --timeout=300s
    
    # 检查MySQL连接
    log_info "检查MySQL连接..."
    kubectl exec -n "${NAMESPACE}" deployment/mysql -- mysql -h localhost -u root -pleafpan123 -e "SELECT 1;" &> /dev/null
    log_info "MySQL部署完成"
}

# 部署MinIO
deploy_minio() {
    log_info "部署MinIO..."
    kubectl apply -f minio-delop.yaml
    
    # 等待MinIO启动
    log_info "等待MinIO启动..."
    kubectl wait --for=condition=ready pod -l app=minio -n "${NAMESPACE}" --timeout=300s
    
    # 等待MinIO设置作业完成
    log_info "等待MinIO设置完成..."
    kubectl wait --for=condition=complete job/minio-setup -n "${NAMESPACE}" --timeout=300s
    
    log_info "MinIO部署完成"
}

# 部署后端应用
deploy_backend() {
    log_info "部署后端应用..."
    kubectl apply -f backend-delop.yaml
    
    # 等待后端启动
    log_info "等待后端应用启动..."
    kubectl wait --for=condition=ready pod -l app=backend -n "${NAMESPACE}" --timeout=300s
    
    # 健康检查
    log_info "检查后端健康状态..."
    kubectl exec -n "${NAMESPACE}" deployment/backend -- curl -s http://localhost:8080/actuator/health | grep -q '"status":"UP"'
    log_info "后端应用部署完成"
}

# 部署前端应用
deploy_frontend() {
    log_info "部署前端应用..."
    kubectl apply -f frontend-delop.yaml
    
    # 等待前端启动
    log_info "等待前端应用启动..."
    kubectl wait --for=condition=ready pod -l app=frontend -n "${NAMESPACE}" --timeout=300s
    
    # 健康检查
    log_info "检查前端健康状态..."
    kubectl exec -n "${NAMESPACE}" deployment/frontend -- curl -s http://localhost:80 | grep -q 'LeafPan'
    log_info "前端应用部署完成"
}

# 检查所有服务状态
check_services() {
    log_info "检查所有服务状态..."
    
    echo ""
    echo "=== 服务状态 ==="
    kubectl get pods -n "${NAMESPACE}"
    
    echo ""
    echo "=== 服务详情 ==="
    kubectl get services -n "${NAMESPACE}"
    
    echo ""
    echo "=== 部署状态 ==="
    kubectl get deployments -n "${NAMESPACE}"
    
    echo ""
    echo "=== Ingress状态 ==="
    kubectl get ingress -n "${NAMESPACE}"
}

# 执行滚动更新
rolling_update() {
    log_info "执行滚动更新..."
    
    # 后端滚动更新
    kubectl rollout restart deployment/backend -n "${NAMESPACE}"
    kubectl rollout status deployment/backend -n "${NAMESPACE}" --timeout=300s
    
    # 前端滚动更新
    kubectl rollout restart deployment/frontend -n "${NAMESPACE}"
    kubectl rollout status deployment/frontend -n "${NAMESPACE}" --timeout=300s
    
    log_info "滚动更新完成"
}

# 备份数据库
backup_database() {
    log_info "备份数据库..."
    BACKUP_FILE="leafpan_backup_${TIMESTAMP}.sql"
    
    kubectl exec -n "${NAMESPACE}" deployment/mysql -- mysqldump -h localhost -u root -pleafpan123 leafpan > "${BACKUP_FILE}"
    
    if [ $? -eq 0 ]; then
        log_info "数据库备份完成: ${BACKUP_FILE}"
    else
        log_error "数据库备份失败"
    fi
}

# 主部署函数
main() {
    log_info "开始部署LeafPan到 ${ENVIRONMENT} 环境"
    
    # 检查依赖
    check_kubectl
    check_cluster
    
    # 创建命名空间
    create_namespace
    
    # 部署基础设施
    deploy_mysql
    deploy_minio
    
    # 部署应用
    deploy_backend
    deploy_frontend
    
    # 检查状态
    check_services
    
    # 备份数据库
    backup_database
    
    log_info "LeafPan部署完成!"
    log_info "访问地址: http://leafpan.local"
    log_info "MinIO管理界面: http://minio.leafpan.local"
}

# 处理命令行参数
case "${1}" in
    "dev"|"staging"|"production")
        ENVIRONMENT="${1}"
        ;;
    "--help"|"-h")
        echo "用法: $0 [environment]"
        echo "environment: dev|staging|production (默认: production)"
        echo ""
        echo "示例:"
        echo "  $0              # 部署到生产环境"
        echo "  $0 dev          # 部署到开发环境"
        echo "  $0 staging      # 部署到预发布环境"
        exit 0
        ;;
    "")
        # 使用默认环境
        ;;
    *)
        log_error "未知环境: ${1}"
        echo "用法: $0 [dev|staging|production]"
        exit 1
        ;;
esac

# 执行主函数
main