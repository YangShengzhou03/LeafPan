# LeafPan 数据库设计文档

## 数据库概述
LeafPan使用MySQL作为关系型数据库，存储用户、文件、分享等核心业务数据。本文档详细描述数据库结构设计、表关系、索引优化以及数据安全策略。

## 表结构设计

### 设计原则

- **范式遵循**：主要遵循第三范式(3NF)，减少数据冗余
- **性能优先**：在必要时采用适当的反范式设计优化查询性能
- **扩展性**：表结构设计考虑未来功能扩展需求
- **数据完整性**：通过主键、外键、约束等保证数据一致性

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
- 一个用户可以拥有多个文件夹（一对多关系）
- 文件夹通过`parent_id`实现树形结构
- 根目录的`parent_id`为0

### 2. 用户与文件关系  
- 一个用户可以拥有多个文件（一对多关系）
- 文件必须属于某个文件夹（根目录文件夹ID为0）

### 3. 分享关系
- 分享可以针对单个文件或整个文件夹
- 分享记录与用户、文件/文件夹关联
- 一个文件/文件夹可以被多次分享

### 4. 操作日志关系
- 一个用户可以产生多条操作日志（一对多关系）
- 操作日志记录了对文件/文件夹/分享的具体操作

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

### 外键约束设计

虽然在高性能场景下有时会移除外键约束以提高性能，但在数据一致性要求较高的场景下，建议保留以下外键关系：

```sql
-- 用户与文件的外键关系
ALTER TABLE files ADD CONSTRAINT fk_files_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

-- 用户与文件夹的外键关系
ALTER TABLE folders ADD CONSTRAINT fk_folders_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

-- 文件夹与文件的外键关系
ALTER TABLE files ADD CONSTRAINT fk_files_folder FOREIGN KEY (folder_id) REFERENCES folders(id) ON DELETE SET NULL;

-- 用户与分享的外键关系
ALTER TABLE shares ADD CONSTRAINT fk_shares_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

-- 文件与分享的外键关系
ALTER TABLE shares ADD CONSTRAINT fk_shares_file FOREIGN KEY (file_id) REFERENCES files(id) ON DELETE CASCADE;

-- 文件夹与分享的外键关系
ALTER TABLE shares ADD CONSTRAINT fk_shares_folder FOREIGN KEY (folder_id) REFERENCES folders(id) ON DELETE CASCADE;

-- 用户与操作日志的外键关系
ALTER TABLE operation_logs ADD CONSTRAINT fk_logs_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
```

## 性能优化建议

### 索引优化策略

1. **覆盖索引**：为常用查询创建覆盖索引，避免回表操作
   ```sql
   -- 示例：优化文件列表查询
   CREATE INDEX idx_user_folder_isdeleted ON files(user_id, folder_id, is_deleted);
   ```

2. **复合索引顺序**：遵循最左前缀原则，将区分度高的字段放在前面

3. **定期维护索引**：使用`ANALYZE TABLE`更新统计信息
   ```sql
   ANALYZE TABLE users, folders, files, shares, operation_logs;
   ```

### 表分区设计

对于大表，建议实施分区策略：

```sql
-- 按时间分区操作日志表
ALTER TABLE operation_logs PARTITION BY RANGE (UNIX_TIMESTAMP(created_time)) (
    PARTITION p_history VALUES LESS THAN (UNIX_TIMESTAMP('2024-01-01')),
    PARTITION p_2024_01 VALUES LESS THAN (UNIX_TIMESTAMP('2024-02-01')),
    PARTITION p_2024_02 VALUES LESS THAN (UNIX_TIMESTAMP('2024-03-01')),
    PARTITION p_future VALUES LESS THAN MAXVALUE
);

-- 按用户ID分区文件表
ALTER TABLE files PARTITION BY HASH (user_id) PARTITIONS 8;
```

### 其他优化建议

1. **定期清理操作日志**：操作日志表数据量较大，建议定期归档或清理
2. **查询优化**：避免使用SELECT *，只查询必要字段
3. **连接池配置**：使用HikariCP连接池，合理配置参数
   - 最小空闲连接数：10
   - 最大连接数：50
   - 连接超时：30秒
4. **缓存热点数据**：使用Redis缓存频繁访问的数据
5. **读写分离**：在高并发场景下，考虑实施读写分离架构

## 数据备份策略

### 备份计划

| 备份类型 | 频率 | 保留期 | 备份方式 |
|---------|------|-------|--------|
| 全量备份 | 每周一次 | 30天 | mysqldump + binlog |
| 增量备份 | 每天一次 | 7天 | 基于binlog的增量备份 |
| 日志备份 | 实时 | 30天 | binlog实时备份 |

### 备份命令示例

```bash
# 全量备份
mysqldump -u root -p --single-transaction --quick --lock-tables=false --all-databases > leafpan_full_backup_$(date +%Y%m%d).sql

# 增量备份（需要开启binlog）
mysqlbinlog --start-position=xxx mysql-bin.000001 > leafpan_inc_backup_$(date +%Y%m%d).sql

# 压缩备份文件
gzip leafpan_full_backup_$(date +%Y%m%d).sql
```

### 恢复策略

1. **全量恢复**：使用最新的全量备份文件
   ```bash
   mysql -u root -p < leafpan_full_backup_20240501.sql
   ```

2. **增量恢复**：应用最新全量备份后的所有增量备份
   ```bash
   mysqlbinlog leafpan_inc_backup_20240502.sql leafpan_inc_backup_20240503.sql | mysql -u root -p
   ```

3. **时间点恢复**：恢复到特定时间点
   ```bash
   mysqlbinlog --start-datetime="2024-05-01 10:00:00" --stop-datetime="2024-05-01 11:00:00" mysql-bin.000001 | mysql -u root -p
   ```

## 版本管理

### Flyway配置与使用

建议使用Flyway进行数据库版本管理，确保数据库结构的可追溯性和一致性。

#### 配置示例

```yaml
# application.yml中的Flyway配置
spring:
  flyway:
    enabled: true
    url: jdbc:mysql://localhost:3306/leafpan
    user: root
    password: password
    locations: classpath:db/migration
    baseline-on-migrate: true
    validate-on-migrate: true
```

#### 迁移脚本示例

```sql
-- V1__Initial_Schema.sql
CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  email VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  nickname VARCHAR(50),
  avatar VARCHAR(255),
  storage_quota BIGINT NOT NULL DEFAULT 10737418240,
  used_storage BIGINT NOT NULL DEFAULT 0,
  status TINYINT NOT NULL DEFAULT 1,
  last_login_time DATETIME,
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 更多表创建语句...

-- V2__Add_Indexes.sql
CREATE INDEX idx_user_parent ON folders(user_id, parent_id);
CREATE INDEX idx_user_folder ON files(user_id, folder_id);
-- 更多索引创建语句...
```

### 迁移流程

1. **版本控制**：每个数据库变更都通过版本化的SQL脚本管理
2. **自动执行**：应用启动时自动执行未运行的迁移脚本
3. **审计跟踪**：维护schema_version表记录所有已执行的迁移

## 数据安全

### 安全措施

1. **敏感数据加密**：
   - 密码使用bcrypt等强哈希算法存储
   - 敏感信息（如分享密码）使用AES加密

2. **访问控制**：
   - 最小权限原则配置数据库用户
   - 生产环境禁用root远程访问

3. **数据脱敏**：
   - 在日志和监控中脱敏敏感信息
   - 开发环境使用测试数据而非真实数据

4. **SQL注入防护**：
   - 使用参数化查询和ORM框架
   - 避免拼接SQL语句

### 安全配置建议

```sql
-- 创建应用专用用户，限制权限
CREATE USER 'leafpan_app'@'%' IDENTIFIED BY 'secure_password';
GRANT SELECT, INSERT, UPDATE, DELETE ON leafpan.* TO 'leafpan_app'@'%';

-- 仅允许备份用户进行备份操作
CREATE USER 'leafpan_backup'@'localhost' IDENTIFIED BY 'backup_password';
GRANT SELECT, LOCK TABLES, SHOW VIEW, RELOAD, REPLICATION CLIENT ON *.* TO 'leafpan_backup'@'localhost';

-- 限制root只能本地访问
DELETE FROM mysql.user WHERE User='root' AND Host NOT IN ('localhost', '127.0.0.1', '::1');
FLUSH PRIVILEGES;
```

## 数据库扩展规划

### 短期规划（3-6个月）

1. **读写分离**：实现主从复制架构，读请求分发到从库
2. **连接池优化**：根据负载动态调整连接池参数
3. **监控增强**：部署Prometheus + Grafana监控数据库性能

### 中期规划（6-12个月）

1. **分库分表**：对用户表和文件表按用户ID进行水平分表
2. **缓存策略优化**：实现多级缓存架构
3. **热数据识别**：识别并优化热数据访问模式

### 长期规划（1年以上）

1. **分布式数据库**：评估并可能迁移到分布式数据库
2. **混合存储**：将不常用数据迁移到成本更低的存储系统
3. **智能索引优化**：基于查询模式自动优化索引结构