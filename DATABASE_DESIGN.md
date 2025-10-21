# LeafPan 数据库设计文档

## 数据库概述
LeafPan使用MySQL作为关系型数据库，存储用户、文件、分享等核心业务数据。

## 表结构设计

### 1. 用户表 (users)
存储用户基本信息

| 字段名 | 类型 | 长度 | 是否主键 | 是否为空 | 默认值 | 说明 |
|--------|------|------|----------|----------|--------|------|
| id | BIGINT | 20 | 是 | 否 | AUTO_INCREMENT | 用户ID |
| username | VARCHAR | 50 | 否 | 否 | | 用户名，唯一 |
| email | VARCHAR | 100 | 否 | 否 | | 邮箱，唯一 |
| password | VARCHAR | 255 | 否 | 否 | | 加密后的密码 |
| nickname | VARCHAR | 50 | 否 | 是 | | 昵称 |
| avatar | VARCHAR | 255 | 否 | 是 | | 头像URL |
| storage_quota | BIGINT | 20 | 否 | 否 | 10737418240 | 存储配额(字节)，默认10GB |
| used_storage | BIGINT | 20 | 否 | 否 | 0 | 已用存储空间(字节) |
| status | TINYINT | 1 | 否 | 否 | 1 | 状态：0-禁用，1-正常 |
| last_login_time | DATETIME | | 否 | 是 | | 最后登录时间 |
| created_time | DATETIME | | 否 | 否 | CURRENT_TIMESTAMP | 创建时间 |
| updated_time | DATETIME | | 否 | 否 | CURRENT_TIMESTAMP | 更新时间 |

**索引：**
- UNIQUE KEY `uk_username` (`username`)
- UNIQUE KEY `uk_email` (`email`)
- KEY `idx_status` (`status`)

### 2. 文件夹表 (folders)
存储文件夹信息，支持多级目录结构

| 字段名 | 类型 | 长度 | 是否主键 | 是否为空 | 默认值 | 说明 |
|--------|------|------|----------|----------|--------|------|
| id | BIGINT | 20 | 是 | 否 | AUTO_INCREMENT | 文件夹ID |
| name | VARCHAR | 255 | 否 | 否 | | 文件夹名称 |
| parent_id | BIGINT | 20 | 否 | 是 | 0 | 父文件夹ID，0表示根目录 |
| user_id | BIGINT | 20 | 否 | 否 | | 所属用户ID |
| path | VARCHAR | 1000 | 否 | 否 | | 完整路径 |
| is_deleted | TINYINT | 1 | 否 | 否 | 0 | 是否删除：0-否，1-是 |
| created_time | DATETIME | | 否 | 否 | CURRENT_TIMESTAMP | 创建时间 |
| updated_time | DATETIME | | 否 | 否 | CURRENT_TIMESTAMP | 更新时间 |

**索引：**
- KEY `idx_user_parent` (`user_id`, `parent_id`)
- KEY `idx_user_path` (`user_id`, `path`)
- KEY `idx_is_deleted` (`is_deleted`)

### 3. 文件表 (files)
存储文件元数据信息

| 字段名 | 类型 | 长度 | 是否主键 | 是否为空 | 默认值 | 说明 |
|--------|------|------|----------|----------|--------|------|
| id | BIGINT | 20 | 是 | 否 | AUTO_INCREMENT | 文件ID |
| name | VARCHAR | 255 | 否 | 否 | | 文件名 |
| original_name | VARCHAR | 255 | 否 | 否 | | 原始文件名 |
| folder_id | BIGINT | 20 | 否 | 是 | 0 | 所属文件夹ID |
| user_id | BIGINT | 20 | 否 | 否 | | 所属用户ID |
| size | BIGINT | 20 | 否 | 否 | 0 | 文件大小(字节) |
| mime_type | VARCHAR | 100 | 否 | 是 | | MIME类型 |
| extension | VARCHAR | 20 | 否 | 是 | | 文件扩展名 |
| storage_key | VARCHAR | 255 | 否 | 否 | | MinIO存储key |
| md5 | VARCHAR | 32 | 否 | 是 | | 文件MD5值 |
| is_deleted | TINYINT | 1 | 否 | 否 | 0 | 是否删除：0-否，1-是 |
| version | INT | 11 | 否 | 否 | 1 | 版本号 |
| created_time | DATETIME | | 否 | 否 | CURRENT_TIMESTAMP | 创建时间 |
| updated_time | DATETIME | | 否 | 否 | CURRENT_TIMESTAMP | 更新时间 |

**索引：**
- KEY `idx_user_folder` (`user_id`, `folder_id`)
- KEY `idx_storage_key` (`storage_key`)
- KEY `idx_md5` (`md5`)
- KEY `idx_is_deleted` (`is_deleted`)

### 4. 分享表 (shares)
存储文件分享信息

| 字段名 | 类型 | 长度 | 是否主键 | 是否为空 | 默认值 | 说明 |
|--------|------|------|----------|----------|--------|------|
| id | BIGINT | 20 | 是 | 否 | AUTO_INCREMENT | 分享ID |
| share_code | VARCHAR | 10 | 否 | 否 | | 分享码，唯一 |
| file_id | BIGINT | 20 | 否 | 否 | | 文件ID |
| folder_id | BIGINT | 20 | 否 | 是 | | 文件夹ID（分享文件夹时） |
| user_id | BIGINT | 20 | 否 | 否 | | 分享者ID |
| share_type | TINYINT | 1 | 否 | 否 | 0 | 分享类型：0-公开，1-密码，2-私密 |
| password | VARCHAR | 64 | 否 | 是 | | 分享密码（加密） |
| expire_time | DATETIME | | 否 | 是 | | 过期时间 |
| download_count | INT | 11 | 否 | 否 | 0 | 下载次数 |
| view_count | INT | 11 | 否 | 否 | 0 | 查看次数 |
| is_active | TINYINT | 1 | 否 | 否 | 1 | 是否有效：0-无效，1-有效 |
| created_time | DATETIME | | 否 | 否 | CURRENT_TIMESTAMP | 创建时间 |

**索引：**
- UNIQUE KEY `uk_share_code` (`share_code`)
- KEY `idx_user_id` (`user_id`)
- KEY `idx_file_id` (`file_id`)
- KEY `idx_expire_time` (`expire_time`)
- KEY `idx_is_active` (`is_active`)

### 5. 操作日志表 (operation_logs)
记录用户操作日志

| 字段名 | 类型 | 长度 | 是否主键 | 是否为空 | 默认值 | 说明 |
|--------|------|------|----------|----------|--------|------|
| id | BIGINT | 20 | 是 | 否 | AUTO_INCREMENT | 日志ID |
| user_id | BIGINT | 20 | 否 | 否 | | 用户ID |
| operation_type | VARCHAR | 50 | 否 | 否 | | 操作类型 |
| target_type | VARCHAR | 50 | 否 | 否 | | 目标类型：file/folder/share |
| target_id | BIGINT | 20 | 否 | 是 | | 目标ID |
| description | VARCHAR | 500 | 否 | 是 | | 操作描述 |
| ip_address | VARCHAR | 45 | 否 | 是 | | IP地址 |
| user_agent | VARCHAR | 500 | 否 | 是 | | 用户代理 |
| created_time | DATETIME | | 否 | 否 | CURRENT_TIMESTAMP | 创建时间 |

**索引：**
- KEY `idx_user_id` (`user_id`)
- KEY `idx_operation_type` (`operation_type`)
- KEY `idx_created_time` (`created_time`)

## 表关系说明

### 1. 用户与文件夹关系
- 一个用户可以拥有多个文件夹
- 文件夹通过`parent_id`实现树形结构
- 根目录的`parent_id`为0

### 2. 用户与文件关系  
- 一个用户可以拥有多个文件
- 文件必须属于某个文件夹（根目录文件夹ID为0）

### 3. 分享关系
- 分享可以针对单个文件或整个文件夹
- 分享记录与用户、文件/文件夹关联

## 数据库配置建议

### 字符集和排序规则
```sql
-- 建议使用utf8mb4字符集以支持emoji表情
ALTER DATABASE leafpan CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
```

### 存储引擎
```sql
-- 建议使用InnoDB存储引擎
-- 支持事务、行级锁、外键约束等特性
```

## 性能优化建议

1. **定期清理操作日志**：操作日志表数据量较大，建议定期归档或清理
2. **文件表分区**：可根据创建时间对文件表进行分区
3. **索引优化**：根据实际查询模式调整索引策略
4. **连接池配置**：合理配置数据库连接池参数

## 数据备份策略

1. **全量备份**：每周执行一次全量备份
2. **增量备份**：每天执行增量备份
3. **日志备份**：实时备份二进制日志

## 版本管理

建议使用Flyway或Liquibase进行数据库版本管理，确保数据库结构的可追溯性和一致性。