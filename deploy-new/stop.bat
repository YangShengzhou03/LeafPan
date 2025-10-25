@echo off
echo === 停止LeafPan服务 ===
echo.

cd /d "%~dp0"
docker-compose down

echo.
echo 所有服务已停止
pause