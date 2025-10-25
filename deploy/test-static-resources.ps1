# LeafPan 静态资源测试脚本 (PowerShell版本)
# 用于验证前端静态资源是否可以正常访问

param(
    [string]$Server = "localhost"
)

Write-Host "========================================" -ForegroundColor Green
Write-Host "   LeafPan 静态资源测试脚本" -ForegroundColor Green
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

Write-Host "测试服务器: $Server" -ForegroundColor Cyan
Write-Host "测试URL列表:" -ForegroundColor Cyan
foreach ($url in $URLs) {
    Write-Host "  - $url" -ForegroundColor White
}
Write-Host ""

# 执行测试
Write-Host "========================================" -ForegroundColor Green
Write-Host "           开始测试" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

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
    exit 0
} else {
    Write-Host "⚠️  有 $Failed 个资源访问失败，请检查部署配置。" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "排查建议:" -ForegroundColor Cyan
    Write-Host "1. 检查容器是否正常运行: docker ps" -ForegroundColor White
    Write-Host "2. 检查容器内文件是否存在: docker exec -it leafpan-frontend ls -la /usr/share/nginx/html/" -ForegroundColor White
    Write-Host "3. 检查Nginx配置: docker exec -it leafpan-frontend cat /etc/nginx/conf.d/default.conf" -ForegroundColor White
    Write-Host "4. 检查Nginx日志: docker logs leafpan-frontend" -ForegroundColor White
    exit 1
}