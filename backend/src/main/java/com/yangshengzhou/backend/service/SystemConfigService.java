package com.yangshengzhou.backend.service;

import com.yangshengzhou.backend.entity.SystemConfig;
import com.yangshengzhou.backend.repository.SystemConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SystemConfigService {

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    public List<SystemConfig> getAllConfigs() {
        return systemConfigRepository.findAll();
    }

    public List<SystemConfig> getConfigsByCategory(String category) {
        return systemConfigRepository.findByCategory(category);
    }

    public Optional<SystemConfig> getConfigByKey(String configKey) {
        return systemConfigRepository.findByConfigKey(configKey);
    }

    public SystemConfig createConfig(SystemConfig config) {
        if (systemConfigRepository.existsByConfigKey(config.getConfigKey())) {
            throw new RuntimeException("配置键已存在");
        }
        return systemConfigRepository.save(config);
    }

    public SystemConfig updateConfig(Long id, SystemConfig configDetails) {
        SystemConfig config = systemConfigRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("配置不存在"));

        if (configDetails.getConfigValue() != null) {
            config.setConfigValue(configDetails.getConfigValue());
        }
        if (configDetails.getDescription() != null) {
            config.setDescription(configDetails.getDescription());
        }
        if (configDetails.getIsPublic() != null) {
            config.setIsPublic(configDetails.getIsPublic());
        }

        return systemConfigRepository.save(config);
    }

    public void deleteConfig(Long id) {
        systemConfigRepository.deleteById(id);
    }

    public Map<String, Object> getPublicConfigs() {
        Map<String, Object> result = new HashMap<>();
        List<SystemConfig> publicConfigs = systemConfigRepository.findByIsPublic(true);
        
        for (SystemConfig config : publicConfigs) {
            Object value = parseConfigValue(config.getConfigValue(), config.getConfigType());
            result.put(config.getConfigKey(), value);
        }
        
        return result;
    }

    private Object parseConfigValue(String value, String type) {
        if (value == null) {
            return null;
        }
        
        switch (type) {
            case "integer":
                return Integer.parseInt(value);
            case "long":
                return Long.parseLong(value);
            case "double":
                return Double.parseDouble(value);
            case "boolean":
                return Boolean.parseBoolean(value);
            case "string":
            default:
                return value;
        }
    }

    public void initializeDefaultConfigs() {
        createIfNotExists("storage.max_file_size", "1073741824", "long", "最大文件上传大小(字节)", "storage", true);
        createIfNotExists("storage.max_total_size", "1024", "long", "最大总存储空间(MB)", "storage", true);
        createIfNotExists("storage.allowed_file_types", "*", "string", "允许上传的文件类型(*表示无限制)", "storage", true);
        createIfNotExists("storage.default_user_quota", "1024", "long", "新用户注册默认存储容量(MB)", "storage", false);
        createIfNotExists("security.enable_captcha", "true", "boolean", "是否启用验证码", "security", false);
        createIfNotExists("security.max_login_attempts", "5", "integer", "最大登录尝试次数", "security", false);
        createIfNotExists("security.login_lock_time", "1800", "integer", "登录锁定时间(秒)", "security", false);
        createIfNotExists("system.site_name", "LeafPan网盘", "string", "网站名称", "system", true);
        createIfNotExists("system.site_description", "安全、快速的云存储服务", "string", "网站描述", "system", true);
        createIfNotExists("system.enable_registration", "true", "boolean", "是否开放注册", "system", false);
    }

    private void createIfNotExists(String key, String value, String type, String description, String category, boolean isPublic) {
        if (!systemConfigRepository.existsByConfigKey(key)) {
            SystemConfig config = new SystemConfig();
            config.setConfigKey(key);
            config.setConfigValue(value);
            config.setConfigType(type);
            config.setDescription(description);
            config.setCategory(category);
            config.setIsPublic(isPublic);
            systemConfigRepository.save(config);
        }
    }
}
