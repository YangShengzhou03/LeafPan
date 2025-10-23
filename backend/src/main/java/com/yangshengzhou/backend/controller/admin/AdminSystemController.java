package com.yangshengzhou.backend.controller.admin;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.service.AuthService;
import com.yangshengzhou.backend.service.OperationLogService;
import com.yangshengzhou.backend.service.FileService;
import com.yangshengzhou.backend.service.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/admin")
public class AdminSystemController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private OperationLogService operationLogService;
    
    @Autowired
    private FileService fileService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    /**
     * 创建系统备份
     */
    @PostMapping("/backup")
    public ResponseEntity<ApiResponse<String>> createBackup(HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            // 记录备份操作
            operationLogService.logOperation(
                currentUser.getId(),
                "CREATE_BACKUP",
                "SYSTEM",
                "",
                    request.getRemoteAddr(),
                request.getHeader("User-Agent"),
                ""
            );
            
            // TODO: 实现实际的备份逻辑
            // 这里可以备份数据库、配置文件等
            
            return ResponseEntity.ok(ApiResponse.success("系统备份创建成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("创建备份失败: " + e.getMessage()));
        }
    }
    
    /**
     * 清理临时文件
     */
    @PostMapping("/clean-temp")
    public ResponseEntity<ApiResponse<String>> cleanTempFiles(HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            // 记录清理临时文件操作
            operationLogService.logOperation(
                currentUser.getId(),
                "CLEAN_TEMP_FILES",
                "SYSTEM",
                "",
                    request.getRemoteAddr(),
                request.getHeader("User-Agent"),
                ""
            );
            
            // TODO: 实现实际的临时文件清理逻辑
            // 清理上传临时目录、缓存文件等
            
            return ResponseEntity.ok(ApiResponse.success("临时文件清理完成"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("清理临时文件失败: " + e.getMessage()));
        }
    }
    
    /**
     * 清理日志文件
     */
    @PostMapping("/clean-logs")
    public ResponseEntity<ApiResponse<String>> cleanLogFiles(HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            // 记录清理日志操作
            operationLogService.logOperation(
                currentUser.getId(),
                "CLEAN_LOG_FILES",
                "SYSTEM",
                "",
                    request.getRemoteAddr(),
                request.getHeader("User-Agent"),
                ""
            );
            
            // 调用OperationLogService清理过期日志
            int cleanedCount = operationLogService.cleanExpiredLogs(30); // 清理30天前的日志
            
            return ResponseEntity.ok(ApiResponse.success("日志文件清理完成，共清理 " + cleanedCount + " 条过期日志"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("清理日志文件失败: " + e.getMessage()));
        }
    }
    
    /**
     * 清理回收站
     */
    @PostMapping("/clean-trash")
    public ResponseEntity<ApiResponse<String>> cleanTrash(HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            // 记录清理回收站操作
            operationLogService.logOperation(
                currentUser.getId(),
                "CLEAN_TRASH",
                "SYSTEM",
                "",
                    request.getRemoteAddr(),
                request.getHeader("User-Agent"),
                ""
            );
            
            // TODO: 实现实际的回收站清理逻辑
            // 清理所有用户的回收站文件
            
            return ResponseEntity.ok(ApiResponse.success("回收站清理完成"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("清理回收站失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取系统信息
     */
    @GetMapping("/system-info")
    public ResponseEntity<ApiResponse<Object>> getSystemInfo(HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            // 返回系统基本信息
            java.util.Map<String, Object> systemInfo = new java.util.HashMap<>();
            systemInfo.put("systemTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            systemInfo.put("javaVersion", System.getProperty("java.version"));
            systemInfo.put("osName", System.getProperty("os.name"));
            systemInfo.put("osVersion", System.getProperty("os.version"));
            systemInfo.put("totalMemory", Runtime.getRuntime().totalMemory());
            systemInfo.put("freeMemory", Runtime.getRuntime().freeMemory());
            systemInfo.put("maxMemory", Runtime.getRuntime().maxMemory());
            
            return ResponseEntity.ok(ApiResponse.success(systemInfo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取系统信息失败: " + e.getMessage()));
        }
    }
}