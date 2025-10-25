# LeafPan Auto Build and Deploy Script
Write-Host "========================================" -ForegroundColor Green
Write-Host "   LeafPan Auto Build and Deploy Script" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

# Check if running from project root directory
if (-not (Test-Path "frontend\package.json")) {
    Write-Host "ERROR: Please run this script from project root directory!" -ForegroundColor Red
    Write-Host "Current directory: $PWD" -ForegroundColor Red
    pause
    exit 1
}

# Create deploy directory structure
Write-Host "Creating deploy directory structure..." -ForegroundColor Yellow
if (-not (Test-Path "deploy\frontend")) { New-Item -ItemType Directory -Path "deploy\frontend" -Force | Out-Null }
if (-not (Test-Path "deploy\backend")) { New-Item -ItemType Directory -Path "deploy\backend" -Force | Out-Null }
if (-not (Test-Path "deploy\logs\backend")) { New-Item -ItemType Directory -Path "deploy\logs\backend" -Force | Out-Null }
if (-not (Test-Path "deploy\mysql\init")) { New-Item -ItemType Directory -Path "deploy\mysql\init" -Force | Out-Null }

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "           Building Frontend" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

# Check frontend dependencies
Write-Host "Checking frontend dependencies..." -ForegroundColor Yellow
if (-not (Test-Path "frontend\node_modules")) {
    Write-Host "Installing frontend dependencies..." -ForegroundColor Yellow
    Set-Location "frontend"
    $npmInstall = npm install
    if ($LASTEXITCODE -ne 0) {
        Write-Host "ERROR: Frontend dependency installation failed!" -ForegroundColor Red
        Set-Location ".."
        pause
        exit 1
    }
    Set-Location ".."
}

# Build frontend
Write-Host "Building frontend for production..." -ForegroundColor Yellow
Set-Location "frontend"
$npmBuild = npm run build
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Frontend build failed!" -ForegroundColor Red
    Set-Location ".."
    pause
    exit 1
}
Set-Location ".."

# Copy frontend build files to deploy directory
Write-Host "Copying frontend build files to deploy directory..." -ForegroundColor Yellow
if (Test-Path "frontend\dist") {
    Copy-Item -Path "frontend\dist\*" -Destination "deploy\frontend\" -Recurse -Force
    Write-Host "Frontend files copied successfully!" -ForegroundColor Green
} else {
    Write-Host "ERROR: Frontend dist directory not found!" -ForegroundColor Red
    pause
    exit 1
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "           Building Backend" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

# Check Maven availability
Write-Host "Checking Maven availability..." -ForegroundColor Yellow
$mavenCmd = "mvn"
$mavenCheck = & mvn --version 2>$null
if ($LASTEXITCODE -ne 0) {
    Write-Host "WARNING: Maven not found in PATH, trying mvnw..." -ForegroundColor Yellow
    if (-not (Test-Path "mvnw")) {
        Write-Host "ERROR: Neither Maven nor mvnw found!" -ForegroundColor Red
        pause
        exit 1
    }
    $mavenCmd = ".\mvnw"
}

# Build backend
Write-Host "Building backend..." -ForegroundColor Yellow
$mavenBuild = & $mavenCmd clean package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Backend build failed!" -ForegroundColor Red
    pause
    exit 1
}

# Find and copy JAR file to deploy directory
Write-Host "Finding and copying JAR file..." -ForegroundColor Yellow
$jarFiles = Get-ChildItem -Path "backend" -Filter "*.jar" -Recurse | Where-Object { $_.Name -ne "maven-wrapper.jar" }
if ($jarFiles.Count -eq 0) {
    Write-Host "ERROR: No JAR file found in backend directory!" -ForegroundColor Red
    pause
    exit 1
}

foreach ($jarFile in $jarFiles) {
    Write-Host "Copying JAR file: $($jarFile.Name)" -ForegroundColor Yellow
    Copy-Item -Path $jarFile.FullName -Destination "deploy\backend\" -Force
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "           Deployment Summary" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

Write-Host "Files copied to deploy directory:" -ForegroundColor Cyan
Write-Host "- Frontend: deploy\frontend\ (all dist files)" -ForegroundColor Cyan
Write-Host "- Backend:  deploy\backend\ (JAR file)" -ForegroundColor Cyan
Write-Host ""

Write-Host "Next steps:" -ForegroundColor Cyan
Write-Host "1. Copy the entire 'deploy' folder to your server" -ForegroundColor Cyan
Write-Host "2. On the server, run: docker-compose up -d" -ForegroundColor Cyan
Write-Host "3. Access the application at: http://your-server-ip" -ForegroundColor Cyan
Write-Host ""

Write-Host "========================================" -ForegroundColor Green
Write-Host "           Build Completed!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

# Show deploy directory structure
Write-Host "Deploy directory structure:" -ForegroundColor Cyan
Get-ChildItem -Path "deploy" -Recurse | ForEach-Object {
    $indent = "  " * ($_.FullName.Split('\').Length - $PWD.Path.Split('\').Length)
    if ($_.PSIsContainer) {
        Write-Host "$indent[$($_.Name)]" -ForegroundColor Blue
    } else {
        Write-Host "$indent$($_.Name)" -ForegroundColor White
    }
}

Write-Host ""
Write-Host "Script execution completed successfully!" -ForegroundColor Green
pause