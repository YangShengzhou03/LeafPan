-- LeafPan Database Initialization Script
-- Database Name: leafpan
-- Character Set: utf8mb4
-- Collation: utf8mb4_unicode_ci

-- Create database (drop if exists)
DROP DATABASE IF EXISTS `leafpan`;
CREATE DATABASE `leafpan` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `leafpan`;

-- Users table
CREATE TABLE IF NOT EXISTS `users` (
    `id` CHAR(36) NOT NULL COMMENT 'User ID (UUID)',
    `email` VARCHAR(100) NOT NULL COMMENT 'Email',
    `password` VARCHAR(255) NOT NULL COMMENT 'Password (encrypted)',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT 'Nickname',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT 'Avatar URL',
    `gender` TINYINT DEFAULT NULL COMMENT 'Gender: 0-unknown, 1-male, 2-female',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT 'Phone number',
    `role` TINYINT NOT NULL DEFAULT 0 COMMENT 'Role: 0-user, 1-admin',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'Status: 0-disabled, 1-active',
    `storage_quota` BIGINT NOT NULL DEFAULT 1073741824 COMMENT 'Storage quota (bytes), default 1GB',
    `used_storage` BIGINT NOT NULL DEFAULT 0 COMMENT 'Used storage (bytes)',
    `last_login_time` DATETIME DEFAULT NULL COMMENT 'Last login time',
    `last_login_ip` VARCHAR(45) DEFAULT NULL COMMENT 'Last login IP',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_status` (`status`),
    KEY `idx_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Users table';

-- Folders table
CREATE TABLE IF NOT EXISTS `folders` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Folder ID',
    `name` VARCHAR(255) NOT NULL COMMENT 'Folder name',
    `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'Parent folder ID, 0 means root',
    `user_id` CHAR(36) NOT NULL COMMENT 'Owner user ID',
    `path` VARCHAR(1000) NOT NULL COMMENT 'Folder path',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Deleted: 0-no, 1-yes',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Folders table';

-- Files table
CREATE TABLE IF NOT EXISTS `files` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'File ID',
    `name` VARCHAR(255) NOT NULL COMMENT 'File name',
    `original_name` VARCHAR(255) NOT NULL COMMENT 'Original file name',
    `folder_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'Parent folder ID, 0 means root',
    `user_id` CHAR(36) NOT NULL COMMENT 'Owner user ID',
    `size` BIGINT NOT NULL DEFAULT 0 COMMENT 'File size (bytes)',
    `mime_type` VARCHAR(100) DEFAULT NULL COMMENT 'MIME type',
    `extension` VARCHAR(20) DEFAULT NULL COMMENT 'File extension',
    `storage_key` VARCHAR(255) NOT NULL COMMENT 'Storage key (MinIO object key)',
    `md5` VARCHAR(32) DEFAULT NULL COMMENT 'File MD5 value',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Deleted: 0-no, 1-yes',
    `version` INT NOT NULL DEFAULT 1 COMMENT 'Version number',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_folder_id` (`folder_id`),
    KEY `idx_is_deleted` (`is_deleted`),
    KEY `idx_md5` (`md5`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Files table';

-- Shares table
CREATE TABLE IF NOT EXISTS `shares` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Share ID',
    `share_code` VARCHAR(10) NOT NULL COMMENT 'Share code (10 random characters)',
    `file_id` BIGINT DEFAULT NULL COMMENT 'Shared file ID',
    `folder_id` BIGINT DEFAULT NULL COMMENT 'Shared folder ID',
    `user_id` CHAR(36) NOT NULL COMMENT 'Sharer user ID',
    `shared_to` CHAR(36) DEFAULT NULL COMMENT 'Shared to user ID (user-to-user share)',
    `share_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'Share type: 0-public, 1-password protected, 2-private',
    `password` VARCHAR(64) DEFAULT NULL COMMENT 'Access password',
    `expire_time` DATETIME DEFAULT NULL COMMENT 'Expire time',
    `download_count` INT NOT NULL DEFAULT 0 COMMENT 'Download count',
    `view_count` INT NOT NULL DEFAULT 0 COMMENT 'View count',
    `is_active` TINYINT(1) NOT NULL DEFAULT 1 COMMENT 'Active: 0-no, 1-yes',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_share_code` (`share_code`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_shared_to` (`shared_to`),
    KEY `idx_file_id` (`file_id`),
    KEY `idx_folder_id` (`folder_id`),
    KEY `idx_is_active` (`is_active`),
    KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Shares table';

-- Favorites table
CREATE TABLE IF NOT EXISTS `favorites` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Favorite ID',
    `user_id` CHAR(36) NOT NULL COMMENT 'User ID',
    `file_id` BIGINT DEFAULT NULL COMMENT 'File ID',
    `folder_id` BIGINT DEFAULT NULL COMMENT 'Folder ID',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_file_id` (`file_id`),
    KEY `idx_folder_id` (`folder_id`),
    CONSTRAINT `fk_fav_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_fav_file` FOREIGN KEY (`file_id`) REFERENCES `files` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_fav_folder` FOREIGN KEY (`folder_id`) REFERENCES `folders` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Favorites table';

-- Operation logs table
CREATE TABLE IF NOT EXISTS `operation_logs` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Log ID',
    `user_id` CHAR(36) NOT NULL COMMENT 'User ID',
    `operation_type` VARCHAR(50) NOT NULL COMMENT 'Operation type',
    `target_type` VARCHAR(50) NOT NULL COMMENT 'Target type',
    `target_id` CHAR(36) DEFAULT NULL COMMENT 'Target ID',
    `description` VARCHAR(500) DEFAULT NULL COMMENT 'Operation description',
    `ip_address` VARCHAR(100) DEFAULT NULL COMMENT 'IP address',
    `user_agent` VARCHAR(500) DEFAULT NULL COMMENT 'User agent',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_operation_type` (`operation_type`),
    KEY `idx_created_time` (`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Operation logs table';

-- System configs table
CREATE TABLE IF NOT EXISTS `system_configs` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Config ID',
    `config_key` VARCHAR(100) NOT NULL COMMENT 'Configuration key',
    `config_value` TEXT DEFAULT NULL COMMENT 'Configuration value',
    `config_type` VARCHAR(20) DEFAULT 'string' COMMENT 'Configuration type: string, integer, long, double, boolean',
    `description` TEXT DEFAULT NULL COMMENT 'Configuration description',
    `category` VARCHAR(50) DEFAULT NULL COMMENT 'Configuration category',
    `is_public` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Is public: 0-no, 1-yes',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`),
    KEY `idx_category` (`category`),
    KEY `idx_is_public` (`is_public`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='System configurations table';

-- Insert default admin user (email: admin@qq.com, password: 123456)
-- Password is stored in plain text for simplicity
INSERT INTO `users` (`id`, `email`, `password`, `nickname`, `role`, `status`, `storage_quota`, `used_storage`, `created_time`, `updated_time`) 
VALUES ('00000000-0000-0000-0000-000000000001', 'admin@qq.com', '123456', 'Admin', 1, 1, 10737418240, 0, NOW(), NOW());

-- Insert default regular user (email: user@qq.com, password: 123456)
-- Password is stored in plain text for simplicity
INSERT INTO `users` (`id`, `email`, `password`, `nickname`, `role`, `status`, `storage_quota`, `used_storage`, `created_time`, `updated_time`) 
VALUES ('00000000-0000-0000-0000-000000000002', 'user@qq.com', '123456', 'User', 0, 1, 1073741824, 0, NOW(), NOW());

-- Create root folder for admin user
INSERT INTO `folders` (`name`, `parent_id`, `user_id`, `path`, `is_deleted`, `created_time`, `updated_time`) 
VALUES ('Root', 0, '00000000-0000-0000-0000-000000000001', '/', 0, NOW(), NOW());

-- Create root folder for regular user
INSERT INTO `folders` (`name`, `parent_id`, `user_id`, `path`, `is_deleted`, `created_time`, `updated_time`) 
VALUES ('Root', 0, '00000000-0000-0000-0000-000000000002', '/', 0, NOW(), NOW());

-- Create view: User storage usage statistics
CREATE OR REPLACE VIEW `v_user_storage` AS
SELECT 
    u.id AS user_id,
    u.email,
    u.nickname,
    u.storage_quota,
    u.used_storage,
    (u.storage_quota - u.used_storage) AS available_storage,
    ROUND((u.used_storage / u.storage_quota) * 100, 2) AS usage_percentage,
    COUNT(DISTINCT f.id) AS file_count,
    COUNT(DISTINCT fo.id) AS folder_count
FROM users u
LEFT JOIN files f ON u.id = f.user_id AND f.is_deleted = 0
LEFT JOIN folders fo ON u.id = fo.user_id AND fo.is_deleted = 0
GROUP BY u.id;

-- Create view: Share statistics
CREATE OR REPLACE VIEW `v_share_stats` AS
SELECT 
    s.id AS share_id,
    s.share_code,
    s.user_id AS sharer_id,
    u.email AS sharer_email,
    u.nickname AS sharer_nickname,
    s.file_id,
    f.name AS file_name,
    f.size AS file_size,
    s.folder_id,
    fo.name AS folder_name,
    s.share_type,
    s.download_count,
    s.view_count,
    s.is_active,
    s.expire_time,
    s.created_time,
    CASE 
        WHEN s.expire_time IS NULL THEN 'Permanent'
        WHEN s.expire_time < NOW() THEN 'Expired'
        ELSE 'Active'
    END AS status
FROM shares s
LEFT JOIN users u ON s.user_id = u.id
LEFT JOIN files f ON s.file_id = f.id
LEFT JOIN folders fo ON s.folder_id = fo.id;

-- Create stored procedure: Clean expired shares
DELIMITER //
CREATE PROCEDURE IF NOT EXISTS `sp_clean_expired_shares`()
BEGIN
    UPDATE shares 
    SET is_active = 0 
    WHERE expire_time IS NOT NULL 
    AND expire_time < NOW() 
    AND is_active = 1;
    
    SELECT ROW_COUNT() AS updated_count;
END //
DELIMITER ;

-- Create stored procedure: Clean deleted files (older than 30 days)
DELIMITER //
CREATE PROCEDURE IF NOT EXISTS `sp_cleanup_deleted_files`()
BEGIN
    DELETE FROM files 
    WHERE is_deleted = 1 
    AND updated_time < DATE_SUB(NOW(), INTERVAL 30 DAY);
    
    SELECT ROW_COUNT() AS deleted_count;
END //
DELIMITER ;

-- Create event: Clean expired shares daily at 2 AM
CREATE EVENT IF NOT EXISTS `evt_clean_expired_shares`
ON SCHEDULE EVERY 1 DAY
STARTS (TIMESTAMP(CURRENT_DATE) + INTERVAL 2 DAY + INTERVAL 2 HOUR)
DO
CALL sp_clean_expired_shares();

-- Enable event scheduler
SET GLOBAL event_scheduler = ON;

-- Create indexes to optimize query performance
ALTER TABLE `files` ADD INDEX `idx_user_folder` (`user_id`, `folder_id`, `is_deleted`);
ALTER TABLE `folders` ADD INDEX `idx_user_parent` (`user_id`, `parent_id`, `is_deleted`);
ALTER TABLE `shares` ADD INDEX `idx_user_active` (`user_id`, `is_active`, `expire_time`);

-- Completion message
SELECT 'Database initialization completed!' AS message;
SELECT 'Database name: leafpan' AS database_name;
SELECT 'Character set: utf8mb4' AS charset;
SELECT 'Collation: utf8mb4_unicode_ci' AS collation;
