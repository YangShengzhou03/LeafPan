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
     * 获取系统配置信息
     * 这个接口不需要认证，任何人都可以访问
     */
    @GetMapping("/system")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSystemConfig() {
        try {
            Map<String, Object> config = new HashMap<>();
            
            // 文件上传配置
            Map<String, Object> fileConfig = new HashMap<>();
            fileConfig.put("maxFileSize", maxFileSize);
            config.put("file", fileConfig);
            
            // MinIO配置
            Map<String, Object> minioConfig = new HashMap<>();
            minioConfig.put("endpoint", minioEndpoint);
            minioConfig.put("bucketName", minioBucketName);
            config.put("minio", minioConfig);
            
            // JWT配置
            Map<String, Object> jwtConfig = new HashMap<>();
            jwtConfig.put("expiration", jwtExpiration);
            config.put("jwt", jwtConfig);
            
            // 应用信息
            Map<String, Object> appInfo = new HashMap<>();
            appInfo.put("name", "LeafPan网盘系统");
            appInfo.put("version", "1.0.0");
            appInfo.put("apiVersion", "v1");
            config.put("app", appInfo);
            
            // 服务器配置
            Map<String, Object> serverConfig = new HashMap<>();
            serverConfig.put("port", serverPort);
            serverConfig.put("contextPath", contextPath);
            config.put("server", serverConfig);
            
            return ResponseEntity.ok(ApiResponse.success(config));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取系统配置失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取默认配置信息
     * 重定向到公开配置
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDefaultConfig() {
        return getPublicConfig();
    }
    
    /**
     * 获取公开配置信息
     * 只包含前端需要的公开配置
     */
    @GetMapping("/public")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPublicConfig() {
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
            return ResponseEntity.badRequest().body(ApiResponse.error("获取公开配置失败: " + e.getMessage()));
        }
    }
}