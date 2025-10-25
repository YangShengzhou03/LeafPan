@echo off
echo ========================================
echo    LeafPan Simple Build Script
echo ========================================
echo.

REM Check if in project root
if not exist "frontend\package.json" (
    echo ERROR: Run from project root directory
    pause
    exit /b 1
)

REM Create deploy directories
if not exist "deploy\frontend" mkdir deploy\frontend
if not exist "deploy\backend" mkdir deploy\backend

REM Build frontend
echo Building frontend...
cd frontend
call npm install
if %errorlevel% neq 0 (
    echo Frontend install failed
    cd ..
    pause
    exit /b 1
)
call npm run build
if %errorlevel% neq 0 (
    echo Frontend build failed
    cd ..
    pause
    exit /b 1
)
cd ..

REM Copy frontend files
if exist "frontend\dist" (
    xcopy "frontend\dist\*" "deploy\frontend\" /E /Y /I
    echo Frontend copied
) else (
    echo Frontend dist not found
    pause
    exit /b 1
)

REM Build backend
echo Building backend...
call mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo Backend build failed
    pause
    exit /b 1
)

REM Copy backend JAR
for /r backend %%i in (*.jar) do (
    if /i "%%~nxi" neq "maven-wrapper.jar" (
        copy "%%i" "deploy\backend\"
        echo Backend JAR copied: %%~nxi
    )
)

echo.
echo ========================================
echo Build completed successfully!
echo ========================================
echo.
echo Files ready in deploy folder:
echo - Frontend: deploy\frontend\
echo - Backend:  deploy\backend\
echo.
echo Next: Copy deploy folder to server and run docker-compose up -d
pause