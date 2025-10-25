# LeafPan 前端静态资源404问题 - 快速修复脚本
# 此脚本将应用所有必要的修复并重新部署前端服务

Write-Host "=== LeafPan 前端静态资源404问题快速修复脚本 ===" -ForegroundColor Green
Write-Host ""

# 检查当前目录
if (-not (Test-Path "docker-compose.yml")) {
    Write-Host "错误: 请在deploy目录下运行此脚本" -ForegroundColor Red
    exit 1
}

# 1. 检查Docker服务状态
Write-Host "1. 检查Docker服务状态..." -ForegroundColor Yellow
try {
    $dockerVersion = docker version 2>$null
    if ($LASTEXITCODE -ne 0) {
        Write-Host "错误: Docker服务未运行。请启动Docker Desktop后重试。" -ForegroundColor Red
        exit 1
    }
    Write-Host "Docker服务正常运行" -ForegroundColor Green
} catch {
    Write-Host "错误: 无法连接到Docker服务。请确保Docker Desktop已启动。" -ForegroundColor Red
    exit 1
}

# 2. 停止现有前端容器
Write-Host "2. 停止现有前端容器..." -ForegroundColor Yellow
docker-compose stop frontend 2>$null
docker-compose rm -f frontend 2>$null

# 3. 重新构建前端镜像
Write-Host "3. 重新构建前端镜像..." -ForegroundColor Yellow
docker-compose build --no-cache frontend
if ($LASTEXITCODE -ne 0) {
    Write-Host "错误: 前端镜像构建失败" -ForegroundColor Red
    exit 1
}
Write-Host "前端镜像构建成功" -ForegroundColor Green

# 4. 启动前端服务
Write-Host "4. 启动前端服务..." -ForegroundColor Yellow
docker-compose up -d frontend
if ($LASTEXITCODE -ne 0) {
    Write-Host "错误: 前端服务启动失败" -ForegroundColor Red
    exit 1
}

# 5. 等待服务启动
Write-Host "5. 等待服务启动..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

# 6. 检查容器状态
Write-Host "6. 检查容器状态..." -ForegroundColor Yellow
$containerStatus = docker-compose ps frontend
if ($containerStatus -match "Up") {
    Write-Host "前端容器运行正常" -ForegroundColor Green
} else {
    Write-Host "警告: 前端容器状态异常" -ForegroundColor Yellow
    Write-Host $containerStatus
}

# 7. 测试静态资源访问
Write-Host "7. 测试静态资源访问..." -ForegroundColor Yellow
$server = "localhost"
$resources = @(
    "/",
    "/css/app.a6883c13.css",
    "/js/app.8a5f9e77.js",
    "/js/vendors.f55ba6d7.js",
    "/favicon.ico"
)

$successCount = 0
$failCount = 0

foreach ($resource in $resources) {
    $url = "http://${server}${resource}"
    try {
        $response = Invoke-WebRequest -Uri $url -Method Head -TimeoutSec 5 -ErrorAction Stop
        if ($response.StatusCode -eq 200) {
            Write-Host "✓ $url - 成功 (状态码: $($response.StatusCode))" -ForegroundColor Green
            $successCount++
        } else {
            Write-Host "✗ $url - 失败 (状态码: $($response.StatusCode))" -ForegroundColor Red
            $failCount++
        }
    } catch {
        Write-Host "✗ $url - 失败 (错误: $($_.Exception.Message))" -ForegroundColor Red
        $failCount++
    }
}

# 8. 显示结果
Write-Host ""
Write-Host "=== 测试结果 ===" -ForegroundColor Cyan
Write-Host "成功: $successCount" -ForegroundColor Green
Write-Host "失败: $failCount" -ForegroundColor Red

if ($failCount -eq 0) {
    Write-Host ""
    Write-Host "🎉 所有静态资源访问正常！修复成功！" -ForegroundColor Green
} else {
    Write-Host ""
    Write-Host "⚠️ 部分静态资源访问失败。请检查以下内容：" -ForegroundColor Yellow
    Write-Host "1. 前端是否已构建 (cd ../frontend && npm run build)" -ForegroundColor White
    Write-Host "2. 容器内文件是否存在 (docker exec -it leafpan-frontend ls -la /usr/share/nginx/html/)" -ForegroundColor White
    Write-Host "3. Nginx配置是否正确 (docker exec -it leafpan-frontend cat /etc/nginx/conf.d/default.conf)" -ForegroundColor White
}

Write-Host ""
Write-Host "=== 修复完成 ===" -ForegroundColor Cyan
Write-Host "如果问题仍然存在，请运行 rebuild-and-test-frontend.ps1 进行详细诊断" -ForegroundColor White