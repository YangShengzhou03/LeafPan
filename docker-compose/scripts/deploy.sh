#!/bin/bash

echo "========================================"
echo "LeafPan 部署脚本"
echo "========================================"

# 检查Docker是否安装
if ! command -v docker &> /dev/null; then
    echo "错误: 未检测到Docker，请先安装Docker"
    exit 1
fi

# 检查Docker Compose是否安装
if ! command -v docker-compose &> /dev/null; then
    echo "错误: 未检测到Docker Compose，请先安装Docker Compose"
    exit 1
fi

# 检查Docker守护进程状态
if ! docker info &> /dev/null; then
    echo "错误: Docker守护进程未运行，请启动Docker"
    exit 1
fi

echo "Docker状态正常，开始部署..."

# 检查环境变量文件
if [ ! -f "../.env" ]; then
    echo "警告: 未找到.env文件，使用.env.example作为模板"
    cp .env.example ../.env
    echo "请编辑 ../.env 文件配置环境变量，然后重新运行此脚本"
    exit 1
fi

# 加载环境变量
set -a
source ../.env
set +a

# 检查镜像文件是否存在
echo "检查镜像文件..."
if [ ! -f "../images/frontend/leafpan-frontend-latest.tar" ]; then
    echo "错误: 前端镜像文件不存在"
    echo "请先运行构建脚本: scripts/build-images.sh"
    exit 1
fi

if [ ! -f "../images/backend/leafpan-backend-latest.tar" ]; then
    echo "错误: 后端镜像文件不存在"
    echo "请先运行构建脚本: scripts/build-images.sh"
    exit 1
fi

# 加载镜像
echo "加载Docker镜像..."
echo "加载前端镜像..."
docker load -i ../images/frontend/leafpan-frontend-latest.tar
if [ $? -ne 0 ]; then
    echo "错误: 前端镜像加载失败"
    exit 1
fi

echo "加载后端镜像..."
docker load -i ../images/backend/leafpan-backend-latest.tar
if [ $? -ne 0 ]; then
    echo "错误: 后端镜像加载失败"
    exit 1
fi

# 创建必要的目录
echo "创建必要的目录..."
mkdir -p ../logs/backend
mkdir -p ../mysql/init

# 停止现有服务（如果存在）
echo "停止现有服务..."
docker-compose -f docker-compose.prod.yml down

# 启动服务
echo "启动服务..."
docker-compose -f docker-compose.prod.yml up -d

if [ $? -ne 0 ]; then
    echo "错误: 服务启动失败"
    exit 1
fi

echo "等待服务启动..."
sleep 30

# 检查服务状态
echo "检查服务状态..."
services=("leafpan-mysql" "leafpan-redis" "leafpan-minio" "leafpan-backend" "leafpan-frontend")

for service in "${services[@]}"; do
    if docker ps | grep -q "$service"; then
        echo "✓ $service 运行正常"
    else
        echo "✗ $service 启动失败"
    fi
done

echo
echo "========================================"
echo "部署完成！"
echo "========================================"
echo
echo "服务访问地址："
echo "前端应用: http://服务器IP:80"
echo "后端API: http://服务器IP:8080"
echo "MinIO控制台: http://服务器IP:9001"
echo
echo "管理命令："
echo "查看服务状态: docker-compose -f docker-compose.prod.yml ps"
echo "查看服务日志: docker-compose -f docker-compose.prod.yml logs"
echo "停止服务: docker-compose -f docker-compose.prod.yml down"
echo "重启服务: docker-compose -f docker-compose.prod.yml restart"
echo

# 显示服务状态
echo "当前服务状态："
docker-compose -f docker-compose.prod.yml ps