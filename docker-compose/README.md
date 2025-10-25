# Docker Compose 部署

本目录包含 LeafPan 项目的 Docker Compose 部署配置，用于手动部署到服务器。

## 目录结构

```
docker-compose/
├── images/                    # 存放构建好的镜像文件
│   ├── frontend/              # 前端镜像
│   └── backend/               # 后端镜像
├── scripts/                   # 构建和部署脚本
│   ├── build-images.bat      # Windows 镜像构建脚本
│   ├── build-images.sh       # Linux 镜像构建脚本
│   └── deploy.sh              # 部署脚本
├── docker-compose.yml         # Docker Compose 配置文件
├── docker-compose.prod.yml    # 生产环境配置
└── .env.example              # 环境变量示例文件
```

## 使用说明

1. 构建镜像：运行 `scripts/build-images.bat` (Windows) 或 `scripts/build-images.sh` (Linux)
2. 镜像将保存在 `images/` 目录下
3. 将整个 `docker-compose` 文件夹复制到服务器
4. 在服务器上运行部署脚本

## 注意事项

- 确保服务器已安装 Docker 和 Docker Compose
- 部署前请配置好环境变量