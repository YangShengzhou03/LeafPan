#!/bin/bash

# LeafPan 静态资源测试脚本
# 用于验证前端静态资源是否可以正常访问

echo "========================================"
echo "   LeafPan 静态资源测试脚本"
echo "========================================"
echo ""

# 设置服务器地址（可以通过参数传入）
SERVER=${1:-localhost}

# 测试URL列表
declare -a URLs=(
    "http://$SERVER/"
    "http://$SERVER/css/app.a6883c13.css"
    "http://$SERVER/css/vendors.4aa35c32.css"
    "http://$SERVER/js/app.8a5f9e77.js"
    "http://$SERVER/js/vendors.f55ba6d7.js"
    "http://$SERVER/favicon.svg"
)

# 测试结果统计
SUCCESS=0
FAILED=0

echo "测试服务器: $SERVER"
echo "测试URL列表:"
for url in "${URLs[@]}"; do
    echo "  - $url"
done
echo ""

# 执行测试
echo "========================================"
echo "           开始测试"
echo "========================================"
echo ""

for url in "${URLs[@]}"; do
    echo -n "测试 $url ... "
    
    # 使用curl测试URL，获取HTTP状态码
    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" "$url")
    
    if [ "$HTTP_CODE" -eq 200 ]; then
        echo "✓ 成功 (200)"
        ((SUCCESS++))
    else
        echo "✗ 失败 ($HTTP_CODE)"
        ((FAILED++))
    fi
done

echo ""
echo "========================================"
echo "           测试结果"
echo "========================================"
echo ""
echo "成功: $SUCCESS"
echo "失败: $FAILED"
echo ""

if [ $FAILED -eq 0 ]; then
    echo "🎉 所有测试通过！静态资源可以正常访问。"
    exit 0
else
    echo "⚠️  有 $FAILED 个资源访问失败，请检查部署配置。"
    echo ""
    echo "排查建议："
    echo "1. 检查容器是否正常运行: docker ps"
    echo "2. 检查容器内文件是否存在: docker exec -it leafpan-frontend ls -la /usr/share/nginx/html/"
    echo "3. 检查Nginx配置: docker exec -it leafpan-frontend cat /etc/nginx/conf.d/default.conf"
    echo "4. 检查Nginx日志: docker logs leafpan-frontend"
    exit 1
fi