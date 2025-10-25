@echo off
setlocal enabledelayedexpansion

echo ========================================
echo LeafPan 镜像构建脚本
echo ========================================

REM 检查Docker是否安装
docker --version >nul 2>&1
if errorlevel 1 (
    echo 错误: 未检测到Docker，请先安装Docker
    pause
    exit /b 1
)

echo 检查Docker守护进程状态...
docker info >nul 2>&1
if errorlevel 1 (
    echo 错误: Docker守护进程未运行，请启动Docker
    pause
    exit /b 1
)

echo Docker状态正常，开始构建镜像...

REM 创建镜像目录
if not exist "..\images" mkdir "..\images"
if not exist "..\images\frontend" mkdir "..\images\frontend"
if not exist "..\images\backend" mkdir "..\images\backend"

REM 构建前端镜像
echo.
echo 构建前端镜像...
cd ..\frontend

REM 检查前端是否已构建
echo 检查前端构建文件...
if not exist "dist" (
    echo 警告: 未找到dist目录，需要先构建前端
    echo 正在安装依赖并构建前端...
    call npm install
    if errorlevel 1 (
        echo 错误: 前端依赖安装失败
        pause
        exit /b 1
    )
    call npm run build
    if errorlevel 1 (
        echo 错误: 前端构建失败
        pause
        exit /b 1
    )
)

REM 构建前端Docker镜像
cd ..\docker-compose
echo 构建前端Docker镜像...
docker build -t leafpan-frontend:latest ..\frontend
if errorlevel 1 (
    echo 错误: 前端镜像构建失败
    pause
    exit /b 1
)

REM 保存前端镜像
echo 保存前端镜像文件...
docker save -o ..\images\frontend\leafpan-frontend-latest.tar leafpan-frontend:latest
if errorlevel 1 (
    echo 错误: 前端镜像保存失败
    pause
    exit /b 1
)

REM 构建后端镜像
echo.
echo 构建后端镜像...
cd ..\backend

REM 检查后端是否已构建
echo 检查后端构建文件...
if not exist "target" (
    echo 警告: 未找到target目录，需要先构建后端
    echo 正在构建后端应用...
    call mvnw clean package -DskipTests
    if errorlevel 1 (
        echo 错误: 后端构建失败
        pause
        exit /b 1
    )
)

REM 构建后端Docker镜像
cd ..\docker-compose
echo 构建后端Docker镜像...
docker build -t leafpan-backend:latest ..\backend
if errorlevel 1 (
    echo 错误: 后端镜像构建失败
    pause
    exit /b 1
)

REM 保存后端镜像
echo 保存后端镜像文件...
docker save -o ..\images\backend\leafpan-backend-latest.tar leafpan-backend:latest
if errorlevel 1 (
    echo 错误: 后端镜像保存失败
    pause
    exit /b 1
)

echo.
echo ========================================
echo 镜像构建完成！
echo ========================================
echo.
echo 构建的镜像文件已保存到以下位置：
echo 前端镜像: ..\images\frontend\leafpan-frontend-latest.tar
echo 后端镜像: ..\images\backend\leafpan-backend-latest.tar
echo.
echo 下一步：
echo 1. 将整个 docker-compose 文件夹复制到服务器
echo 2. 在服务器上运行部署脚本
echo.

pause