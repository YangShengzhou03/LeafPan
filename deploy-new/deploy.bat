@echo off
echo === LeafPan 一键部署脚本 ===
echo.

REM 检查Docker是否运行
docker version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: Docker未运行，请启动Docker Desktop
    pause
    exit /b 1
)

REM 检查前端是否已构建
if not exist "..\frontend\dist" (
    echo 正在构建前端应用...
    cd ..\frontend
    call npm install
    if %errorlevel% neq 0 (
        echo 错误: 前端依赖安装失败
        pause
        exit /b 1
    )
    call npm run build
    if %errorlevel% neq 0 (
        echo 错误: 前端构建失败
        pause
        exit /b 1
    )
    cd ..\deploy-new
    echo 前端构建完成
)

REM 启动服务
echo 正在启动Docker服务...
docker-compose down
docker-compose up -d --build

if %errorlevel% neq 0 (
    echo 错误: 服务启动失败
    pause
    exit /b 1
)

echo.
echo === 部署完成 ===
echo 前端地址: http://localhost
echo 后端地址: http://localhost:8080
echo.
echo 查看服务状态: docker-compose ps
echo 查看日志: docker-compose logs
echo.
pause