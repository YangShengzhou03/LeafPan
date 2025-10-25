# LeafPan 部署文件

## 使用方法

1. **前端打包**：在前端目录运行 `npm run build`，将生成的 `dist` 文件夹复制到 `frontend/` 目录
2. **后端打包**：在后端目录运行 `mvn clean package`，将生成的 `jar` 文件复制到 `backend/` 目录
3. **复制到服务器**：将整个 `deploy` 文件夹复制到服务器
4. **服务器部署**：参考服务器上的部署说明

## 文件夹结构

```
deploy/
├── frontend/          # 前端文件（放入 dist 文件夹内容）
├── backend/           # 后端文件（放入 jar 文件）
├── docker-compose.yml # Docker 部署配置
└── README.md          # 说明文档
```

就是这么简单！