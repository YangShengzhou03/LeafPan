package com.yangshengzhou.backend.controller;

import com.yangshengzhou.backend.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统配置控制器
 * 提供前端所需的系统配置信息
 */
@RestController
@RequestMapping("/config")
public class ConfigController {
    
    @Value("${server.port}")
    private int serverPort;
    
    @Value("${server.servlet.context-path:}")
    private String contextPath;
    
    @Value("${minio.endpoint}")
    private String minioEndpoint;
    
    @Value("${minio.bucket-name}")
    private String minioBucketName;
    
    @Value("${app.storage.max-file-size}")
    private String maxFileSize;
    
    @Value("${app.jwt.expiration}")
    private long jwtExpiration;
    
    /**
     * 获取默认配置信息
     * 重定向到公开配置
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDefaultConfig() {
        try {
            Map<String, Object> config = new HashMap<>();
            
            // 文件上传配置
            Map<String, Object> fileConfig = new HashMap<>();
            fileConfig.put("maxFileSize", maxFileSize);
            config.put("file", fileConfig);
            
            // 应用信息
            Map<String, Object> appInfo = new HashMap<>();
            appInfo.put("name", "LeafPan网盘系统");
            appInfo.put("version", "1.0.0");
            appInfo.put("apiVersion", "v1");
            config.put("app", appInfo);
            
            return ResponseEntity.ok(ApiResponse.success(config));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取配置失败: " + e.getMessage()));
        }
    }
}