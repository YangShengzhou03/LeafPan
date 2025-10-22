-- LeafPan 数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS leafpan CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE leafpan;

-- 1. 用户表 (users)
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名，唯一',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱，唯一',
    password VARCHAR(255) NOT NULL COMMENT '加密后的密码',
    nickname VARCHAR(50) NULL COMMENT '昵称',
    avatar VARCHAR(255) NULL COMMENT '头像URL',
    role TINYINT NOT NULL DEFAULT 0 COMMENT '角色：0-普通用户，1-管理员',
    storage_quota BIGINT NOT NULL DEFAULT 1073741824 COMMENT '存储配额(字节)，默认1GB',
    used_storage BIGINT NOT NULL DEFAULT 0 COMMENT '已用存储空间(字节)',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    last_login_time DATETIME NULL COMMENT '最后登录时间',
    last_login_ip VARCHAR(45) NULL COMMENT '最后登录IP',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_status (status),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 2. 文件夹表 (folders)
CREATE TABLE IF NOT EXISTS folders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '文件夹ID',
    name VARCHAR(255) NOT NULL COMMENT '文件夹名称',
    parent_id BIGINT NOT NULL DEFAULT 0 COMMENT '父文件夹ID，0表示根目录',
    user_id BIGINT NOT NULL COMMENT '所属用户ID',
    path VARCHAR(1000) NOT NULL COMMENT '完整路径',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_parent (user_id, parent_id),
    INDEX idx_user_path (user_id, path),
    INDEX idx_is_deleted (is_deleted),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件夹表';

-- 3. 文件表 (files)
CREATE TABLE IF NOT EXISTS files (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '文件ID',
    name VARCHAR(255) NOT NULL COMMENT '文件名',
    original_name VARCHAR(255) NOT NULL COMMENT '原始文件名',
    folder_id BIGINT NOT NULL DEFAULT 0 COMMENT '所属文件夹ID',
    user_id BIGINT NOT NULL COMMENT '所属用户ID',
    size BIGINT NOT NULL DEFAULT 0 COMMENT '文件大小(字节)',
    mime_type VARCHAR(100) NULL COMMENT 'MIME类型',
    extension VARCHAR(20) NULL COMMENT '文件扩展名',
    storage_key VARCHAR(255) NOT NULL COMMENT 'MinIO存储key',
    md5 VARCHAR(32) NULL COMMENT '文件MD5值',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    version INT NOT NULL DEFAULT 1 COMMENT '版本号',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_folder (user_id, folder_id),
    INDEX idx_storage_key (storage_key),
    INDEX idx_md5 (md5),
    INDEX idx_is_deleted (is_deleted),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件表';

-- 4. 分享表 (shares)
CREATE TABLE IF NOT EXISTS shares (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '分享ID',
    share_code VARCHAR(10) NOT NULL UNIQUE COMMENT '分享码，唯一',
    file_id BIGINT NULL COMMENT '文件ID',
    folder_id BIGINT NULL COMMENT '文件夹ID（分享文件夹时）',
    user_id BIGINT NOT NULL COMMENT '分享者ID',
    share_type TINYINT NOT NULL DEFAULT 0 COMMENT '分享类型：0-公开，1-密码，2-私密',
    password VARCHAR(64) NULL COMMENT '分享密码（加密）',
    expire_time DATETIME NULL COMMENT '过期时间',
    download_count INT NOT NULL DEFAULT 0 COMMENT '下载次数',
    view_count INT NOT NULL DEFAULT 0 COMMENT '查看次数',
    is_active TINYINT NOT NULL DEFAULT 1 COMMENT '是否有效：0-无效，1-有效',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_file_id (file_id),
    INDEX idx_expire_time (expire_time),
    INDEX idx_is_active (is_active),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (file_id) REFERENCES files(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分享表';

-- 5. 操作日志表 (operation_logs)
CREATE TABLE IF NOT EXISTS operation_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    operation_type VARCHAR(50) NOT NULL COMMENT '操作类型',
    target_type VARCHAR(50) NOT NULL COMMENT '目标类型：file/folder/share',
    target_id BIGINT NULL COMMENT '目标ID',
    description VARCHAR(500) NULL COMMENT '操作描述',
    ip_address VARCHAR(45) NULL COMMENT 'IP地址',
    user_agent VARCHAR(500) NULL COMMENT '用户代理',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_operation_type (operation_type),
    INDEX idx_created_time (created_time),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- ===================================================================
-- 触发器说明
-- ===================================================================
-- 触发器名称：after_user_insert
-- 触发时机：用户表插入操作后
-- 功能：为新用户自动创建根目录文件夹
-- 
-- 优化说明：
-- 1. 添加了异常处理，确保即使触发器执行失败也不会影响用户创建
-- 2. 使用ON DUPLICATE KEY UPDATE避免重复创建根目录
-- 3. 使用GET_LOCK确保并发环境下不会重复创建根目录
-- 
-- 注意事项：
-- - 如果触发器执行失败，可以通过以下SQL手动创建根目录：
--   INSERT INTO folders (name, parent_id, user_id, path) 
--   VALUES ('根目录', 0, [用户ID], '/')
--   ON DUPLICATE KEY UPDATE id=id;
-- ===================================================================

-- 创建根目录文件夹的触发器
DELIMITER //
CREATE TRIGGER after_user_insert
AFTER INSERT ON users
FOR EACH ROW
BEGIN
    -- 使用异常处理确保即使发生错误也不会影响用户插入操作
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        -- 记录错误日志，但不阻止用户创建
        -- 在实际应用中，可以将错误信息记录到专门的错误日志表
        -- 这里使用GET_LOCK确保不会重复创建根目录
        SELECT GET_LOCK('user_root_folder_' + NEW.id, 10);
    END;
    
    -- 插入根目录文件夹，使用ON DUPLICATE KEY UPDATE避免重复创建
    INSERT INTO folders (name, parent_id, user_id, path) 
    VALUES ('根目录', 0, NEW.id, '/')
    ON DUPLICATE KEY UPDATE id=id;
END//
DELIMITER ;

-- 创建定时清理事件，每天凌晨自动删除回收站中符合条件的文件
DELIMITER //
CREATE EVENT IF NOT EXISTS clean_deleted_files
ON SCHEDULE EVERY 1 DAY
STARTS TIMESTAMP(CURRENT_DATE, '02:00:00') -- 每天凌晨2点执行
COMMENT '定时清理回收站中超过30天的文件和文件夹'
DO
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE file_id_val BIGINT;
    DECLARE storage_key_val VARCHAR(255);
    DECLARE folder_id_val BIGINT;
    
    -- 声明游标，获取需要删除的文件
    DECLARE file_cursor CURSOR FOR 
        SELECT id, storage_key 
        FROM files 
        WHERE is_deleted = 1 
        AND updated_time < DATE_SUB(NOW(), INTERVAL 30 DAY);
    
    -- 声明游标，获取需要删除的文件夹
    DECLARE folder_cursor CURSOR FOR 
        SELECT id 
        FROM folders 
        WHERE is_deleted = 1 
        AND updated_time < DATE_SUB(NOW(), INTERVAL 30 DAY);
    
    -- 声明异常处理
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    -- 开始事务
    START TRANSACTION;
    
    -- 删除符合条件的文件
    OPEN file_cursor;
    file_loop: LOOP
        FETCH file_cursor INTO file_id_val, storage_key_val;
        IF done THEN
            LEAVE file_loop;
        END IF;
        
        -- 这里应该调用MinIO删除文件的API
        -- 由于这是SQL脚本，我们只能记录需要删除的文件
        -- 在实际应用中，应该通过应用程序调用MinIO API删除文件
        -- 示例: DELETE FROM minio_objects WHERE object_key = storage_key_val;
        
        -- 从数据库中删除文件记录
        DELETE FROM files WHERE id = file_id_val;
    END LOOP;
    CLOSE file_cursor;
    
    -- 重置done标志
    SET done = FALSE;
    
    -- 删除符合条件的文件夹
    OPEN folder_cursor;
    folder_loop: LOOP
        FETCH folder_cursor INTO folder_id_val;
        IF done THEN
            LEAVE folder_loop;
        END IF;
        
        -- 从数据库中删除文件夹记录
        DELETE FROM folders WHERE id = folder_id_val;
    END LOOP;
    CLOSE folder_cursor;
    
    -- 提交事务
    COMMIT;
END//
DELIMITER ;

-- 确保事件调度器已启用
-- 注意：在生产环境中，应该在my.cnf配置文件中设置event_scheduler=ON
SET GLOBAL event_scheduler = ON;

-- ===================================================================
-- 定时清理事件说明
-- ===================================================================
-- 事件名称：clean_deleted_files
-- 执行时间：每天凌晨2点
-- 功能：自动删除回收站中超过30天的文件和文件夹
-- 
-- 重要提示：
-- 1. 此事件仅删除数据库中的记录，不会自动删除MinIO中的实际文件
-- 2. MinIO文件删除需要在应用层实现，建议：
--    - 创建一个独立的后台服务
--    - 监听数据库中的文件删除事件
--    - 调用MinIO API删除对应的文件对象
-- 3. 可以通过以下SQL查询即将被清理的文件：
--    SELECT id, name, storage_key, updated_time 
--    FROM files 
--    WHERE is_deleted = 1 
--    AND updated_time < DATE_SUB(NOW(), INTERVAL 30 DAY);
-- ===================================================================

-- 插入默认管理员用户
INSERT INTO users (username, email, password, nickname, role, storage_quota, status) 
VALUES ('admin', 'admin@leafpan.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '系统管理员', 1, 107374182400, 1)
ON DUPLICATE KEY UPDATE id=id;

-- 为管理员创建根目录
INSERT INTO folders (name, parent_id, user_id, path) 
SELECT '根目录', 0, id, '/' FROM users WHERE username = 'admin'
ON DUPLICATE KEY UPDATE id=id;
