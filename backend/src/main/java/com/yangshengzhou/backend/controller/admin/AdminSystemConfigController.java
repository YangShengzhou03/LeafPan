package com.yangshengzhou.backend.controller.admin;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.entity.SystemConfig;
import com.yangshengzhou.backend.service.AuthService;
import com.yangshengzhou.backend.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/config")
public class AdminSystemConfigController {

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private AuthService authService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<SystemConfig>>> getAllConfigs() {
        try {
            if (!isAdmin()) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            List<SystemConfig> configs = systemConfigService.getAllConfigs();
            return ResponseEntity.ok(ApiResponse.success(configs));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取配置失败: " + e.getMessage()));
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<SystemConfig>>> getConfigsByCategory(@PathVariable String category) {
        try {
            if (!isAdmin()) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            List<SystemConfig> configs = systemConfigService.getConfigsByCategory(category);
            return ResponseEntity.ok(ApiResponse.success(configs));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取配置失败: " + e.getMessage()));
        }
    }

    @GetMapping("/public")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPublicConfigs() {
        try {
            Map<String, Object> configs = systemConfigService.getPublicConfigs();
            return ResponseEntity.ok(ApiResponse.success(configs));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取配置失败: " + e.getMessage()));
        }
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<SystemConfig>> createConfig(@RequestBody SystemConfig config) {
        try {
            if (!isAdmin()) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            SystemConfig createdConfig = systemConfigService.createConfig(config);
            return ResponseEntity.ok(ApiResponse.success(createdConfig));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("创建配置失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SystemConfig>> updateConfig(
            @PathVariable Long id,
            @RequestBody SystemConfig configDetails) {
        try {
            if (!isAdmin()) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            SystemConfig updatedConfig = systemConfigService.updateConfig(id, configDetails);
            return ResponseEntity.ok(ApiResponse.success(updatedConfig));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("更新配置失败: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteConfig(@PathVariable Long id) {
        try {
            if (!isAdmin()) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            systemConfigService.deleteConfig(id);
            return ResponseEntity.ok(ApiResponse.success("配置删除成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("删除配置失败: " + e.getMessage()));
        }
    }

    @PostMapping("/initialize")
    public ResponseEntity<ApiResponse<String>> initializeConfigs() {
        try {
            if (!isAdmin()) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            systemConfigService.initializeDefaultConfigs();
            return ResponseEntity.ok(ApiResponse.success("配置初始化成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("初始化配置失败: " + e.getMessage()));
        }
    }

    private boolean isAdmin() {
        var currentUser = authService.getCurrentUser();
        return currentUser != null && currentUser.getRole() == 1;
    }
}
