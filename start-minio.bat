@echo off
REM MinIO启动脚本
REM 设置MinIO的访问密钥
set MINIO_ROOT_USER=minioadmin
set MINIO_ROOT_PASSWORD=minioadmin

REM 设置MinIO的数据存储目录
set MINIO_DATA_DIR=D:\Code\minio\data

REM 创建数据目录（如果不存在）
if not exist "%MINIO_DATA_DIR%" (
    mkdir "%MINIO_DATA_DIR%"
)

REM 启动MinIO服务器
echo 正在启动MinIO服务器...
echo 访问地址: http://localhost:9000
echo 控制台地址: http://localhost:9001
echo 用户名: %MINIO_ROOT_USER%
echo 密码: %MINIO_ROOT_PASSWORD%
echo.

D:\Code\minio\minio.exe server %MINIO_DATA_DIR% --console-address ":9001"

pause