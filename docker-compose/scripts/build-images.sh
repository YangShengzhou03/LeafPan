#!/bin/bash

echo "========================================"
echo "LeafPan 镜像构建脚本"
echo "========================================"

# 检查Docker是否安装
if ! command -v docker &> /dev/null; then
    echo "错误: 未检测到Docker，请先安装Docker"
    exit 1
fi

# 检查Docker守护进程状态
if ! docker info &> /dev/null; then
    echo "错误: Docker守护进程未运行，请启动Docker"
    exit 1
fi

echo "Docker状态正常，开始构建镜像..."

# 创建镜像目录
mkdir -p ../images/frontend
mkdir -p ../images/backend

# 构建前端镜像
echo
echo "构建前端镜像..."
cd ../frontend

# 检查前端是否已构建
echo "检查前端构建文件..."
if [ ! -d "dist" ]; then
    echo "警告: 未找到dist目录，需要先构建前端"
    echo "正在安装依赖并构建前端..."
    
    # 安装依赖
    if ! npm install; then
        echo "错误: 前端依赖安装失败"
        exit 1
    fi
    
    # 构建前端
    if ! npm run build; then
        echo "错误: 前端构建失败"
        exit 1
    fi
fi

# 构建前端Docker镜像
cd ../docker-compose
echo "构建前端Docker镜像..."
if ! docker build -t leafpan-frontend:latest ../frontend; then
    echo "错误: 前端镜像构建失败"
    exit 1
fi

# 保存前端镜像
echo "保存前端镜像文件..."
if ! docker save -o ../images/frontend/leafpan-frontend-latest.tar leafpan-frontend:latest; then
    echo "错误: 前端镜像保存失败"
    exit 1
fi

# 构建后端镜像
echo
echo "构建后端镜像..."
cd ../backend

# 检查后端是否已构建
echo "检查后端构建文件..."
if [ ! -d "target" ]; then
    echo "警告: 未找到target目录，需要先构建后端"
    echo "正在构建后端应用..."
    
    # 构建后端
    if ! ./mvnw clean package -DskipTests; then
        echo "错误: 后端构建失败"
        exit 1
    fi
fi

# 构建后端Docker镜像
cd ../docker-compose
echo "构建后端Docker镜像..."
if ! docker build -t leafpan-backend:latest ../backend; then
    echo "错误: 后端镜像构建失败"
    exit 1
fi

# 保存后端镜像
echo "保存后端镜像文件..."
if ! docker save -o ../images/backend/leafpan-backend-latest.tar leafpan-backend:latest; then
    echo "错误: 后端镜像保存失败"
    exit 1
fi

echo
echo "========================================"
echo "镜像构建完成！"
echo "========================================"
echo
echo "构建的镜像文件已保存到以下位置："
echo "前端镜像: ../images/frontend/leafpan-frontend-latest.tar"
echo "后端镜像: ../images/backend/leafpan-backend-latest.tar"
echo
echo "下一步："
echo "1. 将整个 docker-compose 文件夹复制到服务器"
echo "2. 在服务器上运行部署脚本"
echo

# 给脚本添加执行权限
chmod +x deploy.sh