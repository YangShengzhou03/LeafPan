@echo off
echo ========================================
echo    LeafPan 后端构建脚本
echo ========================================
echo.

REM 检查是否在项目根目录
if not exist "backend\pom.xml" (
    echo ERROR: 请在项目根目录运行此脚本
    pause
    exit /b 1
)

REM 检查是否安装了Maven
echo 检查Maven环境...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: 未找到Maven，请先安装Maven
    echo 下载地址: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

REM 构建后端
echo 开始构建后端...
mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo 后端构建失败
    pause
    exit /b 1
)

REM 查找并复制JAR文件
echo 查找JAR文件...
for /r backend %%i in (*.jar) do (
    if /i "%%~nxi" neq "maven-wrapper.jar" (
        echo 找到JAR文件: %%~nxi
        copy "%%i" "deploy\backend\"
        echo 已复制到 deploy\backend\
        goto :success
    )
)

echo ERROR: 未找到后端JAR文件
pause
exit /b 1

:success
echo.
echo ========================================
echo 后端构建成功！
echo ========================================
echo.
echo 现在可以复制 deploy 文件夹到Ubuntu服务器
echo 然后在服务器上运行: docker-compose up -d
pause