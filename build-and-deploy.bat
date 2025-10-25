@echo off
setlocal enabledelayedexpansion

echo ========================================
echo    LeafPan Auto Build and Deploy Script
echo ========================================
echo.

REM Check if running from project root directory
if not exist "frontend\package.json" (
    echo ERROR: Please run this script from project root directory!
    echo Current directory: %CD%
    pause
    exit /b 1
)

REM Create deploy directory structure
echo Creating deploy directory structure...
if not exist "deploy\frontend" mkdir deploy\frontend
if not exist "deploy\backend" mkdir deploy\backend
if not exist "deploy\logs\backend" mkdir deploy\logs\backend
if not exist "deploy\mysql\init" mkdir deploy\mysql\init

echo.
echo ========================================
echo           Building Frontend
echo ========================================
echo.

REM Check frontend dependencies
echo Checking frontend dependencies...
if not exist "frontend\node_modules" (
    echo Installing frontend dependencies...
    cd frontend
    call npm install
    if !errorlevel! neq 0 (
        echo ERROR: Frontend dependency installation failed!
        cd ..
        pause
        exit /b 1
    )
    cd ..
)

REM Build frontend
echo Building frontend for production...
cd frontend
call npm run build
if !errorlevel! neq 0 (
    echo ERROR: Frontend build failed!
    cd ..
    pause
    exit /b 1
)
cd ..

REM Copy frontend build files to deploy directory
echo Copying frontend build files to deploy directory...
if exist "frontend\dist" (
    xcopy "frontend\dist\*" "deploy\frontend\" /E /Y /I >nul
    echo Frontend files copied successfully!
) else (
    echo ERROR: Frontend dist directory not found!
    pause
    exit /b 1
)

echo.
echo ========================================
echo           Building Backend
echo ========================================
echo.

REM Check Maven availability
echo Checking Maven availability...
call mvn --version >nul 2>&1
if !errorlevel! neq 0 (
    echo WARNING: Maven not found in PATH, trying mvnw...
    if not exist "mvnw" (
        echo ERROR: Neither Maven nor mvnw found!
        pause
        exit /b 1
    )
    set MVN_CMD=mvnw
) else (
    set MVN_CMD=mvn
)

REM Build backend
echo Building backend...
call %MVN_CMD% clean package -DskipTests
if !errorlevel! neq 0 (
    echo ERROR: Backend build failed!
    pause
    exit /b 1
)

REM Find and copy JAR file to deploy directory
echo Finding and copying JAR file...
set JAR_FOUND=0
for /r backend %%i in (*.jar) do (
    if /i "%%~nxi" neq "maven-wrapper.jar" (
        if exist "%%i" (
            echo Copying JAR file: %%~nxi
            copy "%%i" "deploy\backend\" /Y >nul
            set JAR_FOUND=1
        )
    )
)

if !JAR_FOUND! equ 0 (
    echo ERROR: No JAR file found in backend directory!
    pause
    exit /b 1
)

echo.
echo ========================================
echo           Deployment Summary
echo ========================================
echo.

echo Files copied to deploy directory:
echo - Frontend: deploy\frontend\ (all dist files)
echo - Backend:  deploy\backend\ (JAR file)
echo.

echo Next steps:
echo 1. Copy the entire 'deploy' folder to your server
echo 2. On the server, run: docker-compose up -d
echo 3. Access the application at: http://your-server-ip
echo.

echo ========================================
echo           Build Completed!
echo ========================================
echo.

REM Show deploy directory structure
echo Deploy directory structure:
tree deploy /F | more

echo.
echo Script execution completed successfully!
pause