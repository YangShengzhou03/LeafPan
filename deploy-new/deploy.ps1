# LeafPan 一键部署脚本 (PowerShell版本)

Write-Host "=== LeafPan 一键部署脚本 ===" -ForegroundColor Green
Write-Host ""

# 检查Docker是否运行
try {
    $dockerVersion = docker version 2>$null
    if ($LASTEXITCODE -ne 0) {
        Write-Host "错误: Docker未运行，请启动Docker Desktop" -ForegroundColor Red
        Read-Host "按任意键退出"
        exit 1
    }
    Write-Host "Docker运行正常" -ForegroundColor Green
} catch {
    Write-Host "错误: 无法连接到Docker，请确保Docker Desktop已启动" -ForegroundColor Red
    Read-Host "按任意键退出"
    exit 1
}

# 检查前端是否已构建
if (-not (Test-Path "..\frontend\dist")) {
    Write-Host "正在构建前端应用..." -ForegroundColor Yellow
    Set-Location ..\frontend
    
    # 安装依赖
    Write-Host "安装前端依赖..." -ForegroundColor Yellow
    npm install
    if ($LASTEXITCODE -ne 0) {
        Write-Host "错误: 前端依赖安装失败" -ForegroundColor Red
        Read-Host "按任意键退出"
        exit 1
    }
    
    # 构建应用
    Write-Host "构建前端应用..." -ForegroundColor Yellow
    npm run build
    if ($LASTEXITCODE -ne 0) {
        Write-Host "错误: 前端构建失败" -ForegroundColor Red
        Read-Host "按任意键退出"
        exit 1
    }
    
    Set-Location ..\deploy-new
    Write-Host "前端构建完成" -ForegroundColor Green
}

# 启动服务
Write-Host "正在启动Docker服务..." -ForegroundColor Yellow
docker-compose down
docker-compose up -d --build

if ($LASTEXITCODE -ne 0) {
    Write-Host "错误: 服务启动失败" -ForegroundColor Red
    Read-Host "按任意键退出"
    exit 1
}

Write-Host ""
Write-Host "=== 部署完成 ===" -ForegroundColor Green
Write-Host "前端地址: http://localhost" -ForegroundColor Cyan
Write-Host "后端地址: http://localhost:8080" -ForegroundColor Cyan
Write-Host ""
Write-Host "查看服务状态: docker-compose ps" -ForegroundColor White
Write-Host "查看日志: docker-compose logs" -ForegroundColor White
Write-Host ""
Read-Host "按任意键退出"