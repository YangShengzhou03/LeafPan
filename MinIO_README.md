# MinIO 文件存储配置说明

## 概述

本项目使用MinIO作为对象存储服务，配置了两个存储桶：
- `data`：用于存储用户的普通文件
- `avatar`：用于存储用户的头像文件

## 启动MinIO服务器

1. 确保已安装MinIO服务器，可执行文件位于 `D:\Code\minio\minio.exe`
2. 运行项目根目录下的 `start-minio.bat` 脚本启动MinIO服务器
3. MinIO服务器启动后，可通过以下地址访问：
   - API地址：http://localhost:9000
   - 控制台地址：http://localhost:9001
   - 用户名：minioadmin
   - 密码：minioadmin

## 数据库表结构

文件信息存储在数据库的 `files` 表中，主要字段包括：

- `id`：文件ID
- `name`：存储在MinIO中的文件名（UUID生成）
- `original_name`：用户上传时的原始文件名
- `folder_id`：所属文件夹ID
- `user_id`：所属用户ID
- `size`：文件大小（字节）
- `mime_type`：MIME类型
- `extension`：文件扩展名
- `storage_key`：在MinIO中的存储键（与name相同）
- `md5`：文件MD5值
- `is_deleted`：是否已删除
- `version`：版本号
- `created_time`：创建时间
- `updated_time`：更新时间

用户头像信息存储在 `users` 表的 `avatar` 字段中，存储的是头像文件的URL。

## 文件上传流程

1. 用户通过API上传文件
2. 系统验证文件类型和大小
3. 生成唯一的文件名（UUID + 扩展名）
4. 根据文件类型选择存储桶：
   - 普通文件：存储到 `data` 桶
   - 头像文件：存储到 `avatar` 桶
5. 将文件上传到MinIO
6. 在数据库中创建文件记录
7. 返回文件访问URL

## API接口

### 文件上传

- **路径**：`/api/files/upload`
- **方法**：POST
- **参数**：
  - `file`：上传的文件
  - `folderId`：目标文件夹ID（可选，默认为0）
- **返回**：文件信息

### 头像上传

- **路径**：`/api/user/avatar/upload`
- **方法**：POST
- **参数**：
  - `file`：头像文件（仅支持图片格式）
- **返回**：头像URL

### 获取文件

- **路径**：`/api/files/{id}`
- **方法**：GET
- **返回**：文件流

### 删除文件

- **路径**：`/api/files/{id}`
- **方法**：DELETE
- **返回**：操作结果

## 配置说明

MinIO相关配置在 `application.yml` 文件中：

```yaml
minio:
  endpoint: http://localhost:9000
  access-key: minioadmin
  secret-key: minioadmin
  bucket-name: leafpan
  data-bucket: data      # 用户数据文件存储桶
  avatar-bucket: avatar  # 用户头像存储桶
```

## 注意事项

1. 确保MinIO服务器在应用启动前已启动
2. 文件上传大小限制在 `application.yml` 中配置，默认为100MB
3. 头像文件只支持图片格式（jpg、jpeg、png、gif等）
4. 文件删除采用软删除，实际文件会在30天后自动清理
5. 文件URL为预签名URL，有效期为1小时

## 故障排除

1. **MinIO连接失败**：检查MinIO服务器是否启动，配置是否正确
2. **文件上传失败**：检查文件类型是否在允许范围内，文件大小是否超限
3. **桶创建失败**：检查MinIO服务器权限配置是否正确