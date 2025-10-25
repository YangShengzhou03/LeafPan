# LeafPan 前端服务重新构建和测试脚本
param(
    [string]$Server = "localhost"
)

Write-Host "========================================" -ForegroundColor Green
Write-Host "   LeafPan 前端服务重新构建和测试脚本" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

# 检查Docker是否运行
Write-Host "检查Docker服务状态..." -ForegroundColor Yellow
try {
    $dockerVersion = docker version 2>$null
    if ($LASTEXITCODE -ne 0) {
        Write-Host "ERROR: Docker未运行或连接失败！" -ForegroundColor Red
        Write-Host "请启动Docker Desktop后重试。" -ForegroundColor Yellow
        pause
        exit 1
    }
    Write-Host "Docker服务正常运行" -ForegroundColor Green
} catch {
    Write-Host "ERROR: Docker未运行或连接失败！" -ForegroundColor Red
    Write-Host "请启动Docker Desktop后重试。" -ForegroundColor Yellow
    pause
    exit 1
}

# 停止并删除现有的前端容器
Write-Host "停止现有的前端容器..." -ForegroundColor Yellow
try {
    docker stop leafpan-frontend 2>$null
    docker rm leafpan-frontend 2>$null
    Write-Host "前端容器已停止" -ForegroundColor Green
} catch {
    Write-Host "没有运行中的前端容器" -ForegroundColor Cyan
}

# 重新构建前端镜像（使用--no-cache避免缓存）
Write-Host "重新构建前端镜像..." -ForegroundColor Yellow
Set-Location "deploy"
$buildResult = docker-compose build --no-cache frontend
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: 前端镜像构建失败！" -ForegroundColor Red
    Write-Host "请检查Dockerfile和构建上下文。" -ForegroundColor Yellow
    pause
    exit 1
}
Write-Host "前端镜像构建成功" -ForegroundColor Green

# 启动前端服务
Write-Host "启动前端服务..." -ForegroundColor Yellow
$upResult = docker-compose up -d frontend
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: 前端服务启动失败！" -ForegroundColor Red
    pause
    exit 1
}
Write-Host "前端服务启动成功" -ForegroundColor Green

# 等待服务启动
Write-Host "等待服务启动..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

# 检查容器状态
Write-Host "检查容器状态..." -ForegroundColor Yellow
$containerStatus = docker ps --filter "name=leafpan-frontend" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
Write-Host $containerStatus -ForegroundColor Cyan

# 测试静态资源访问
Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "           测试静态资源访问" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

# 测试URL列表
$URLs = @(
    "http://$Server/",
    "http://$Server/css/app.a6883c13.css",
    "http://$Server/css/vendors.4aa35c32.css",
    "http://$Server/js/app.8a5f9e77.js",
    "http://$Server/js/vendors.f55ba6d7.js",
    "http://$Server/favicon.svg"
)

# 测试结果统计
$Success = 0
$Failed = 0

foreach ($url in $URLs) {
    Write-Host -NoNewline "测试 $url ... "
    
    try {
        # 使用Invoke-WebRequest测试URL，获取HTTP状态码
        $response = Invoke-WebRequest -Uri $url -UseBasicParsing -ErrorAction Stop
        $statusCode = $response.StatusCode
        
        if ($statusCode -eq 200) {
            Write-Host "✓ 成功 (200)" -ForegroundColor Green
            $Success++
        } else {
            Write-Host "✗ 失败 ($statusCode)" -ForegroundColor Red
            $Failed++
        }
    } catch {
        Write-Host "✗ 失败 (请求异常)" -ForegroundColor Red
        $Failed++
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "           测试结果" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

Write-Host "成功: $Success" -ForegroundColor Green
Write-Host "失败: $Failed" -ForegroundColor Red
Write-Host ""

if ($Failed -eq 0) {
    Write-Host "🎉 所有测试通过！静态资源可以正常访问。" -ForegroundColor Green
    Write-Host "前端服务已成功部署在: http://$Server/" -ForegroundColor Cyan
} else {
    Write-Host "⚠️  有 $Failed 个资源访问失败，请检查部署配置。" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "排查建议:" -ForegroundColor Cyan
    Write-Host "1. 检查容器内文件是否存在: docker exec -it leafpan-frontend ls -la /usr/share/nginx/html/" -ForegroundColor White
    Write-Host "2. 检查Nginx配置: docker exec -it leafpan-frontend cat /etc/nginx/conf.d/default.conf" -ForegroundColor White
    Write-Host "3. 检查Nginx日志: docker logs leafpan-frontend" -ForegroundColor White
    Write-Host "4. 进入容器内部测试: docker exec -it leafpan-frontend wget -O- http://localhost/js/vendors.f55ba6d7.js" -ForegroundColor White
}

Set-Location ".."
Write-Host ""
Write-Host "脚本执行完成！" -ForegroundColor Green
pause