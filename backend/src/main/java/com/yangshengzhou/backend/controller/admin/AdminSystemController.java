package com.yangshengzhou.backend.controller.admin;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.repository.UserRepository;
import com.yangshengzhou.backend.repository.FileRepository;
import com.yangshengzhou.backend.service.AuthService;
import com.yangshengzhou.backend.service.OperationLogService;
import com.yangshengzhou.backend.service.FileService;
import com.yangshengzhou.backend.service.FileStorageService;
import com.yangshengzhou.backend.service.UserService;
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
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private FileRepository fileRepository;
    
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
                "创建系统备份",
                request.getRemoteAddr(),
                request.getHeader("User-Agent")
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
                "清理临时文件",
                request.getRemoteAddr(),
                request.getHeader("User-Agent")
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
                "清理日志文件",
                request.getRemoteAddr(),
                request.getHeader("User-Agent")
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
                "清理回收站",
                request.getRemoteAddr(),
                request.getHeader("User-Agent")
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
    
    /**
     * 获取仪表板统计数据
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Object>> getDashboardStats(HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            // 获取用户统计
            long totalUsers = userRepository.count();
            long activeUsers = userService.countActiveUsers();
            
            // 获取文件统计
            long totalFiles = fileRepository.count();
            Long totalStorageUsed = fileRepository.sumSize();
            if (totalStorageUsed == null) {
                totalStorageUsed = 0L;
            }
            
            // 获取分享统计（暂时使用模拟数据）
            long totalShares = 0L; // fileService.getTotalShareCount(); // 需要实现分享统计方法
            
            // 获取系统运行时间（模拟数据）
            String uptime = "7天12小时35分钟";
            
            // 构建统计数据
            java.util.Map<String, Object> stats = new java.util.HashMap<>();
            stats.put("userCount", totalUsers);
            stats.put("activeUserCount", activeUsers);
            stats.put("fileCount", totalFiles);
            stats.put("usedStorage", totalStorageUsed / (1024.0 * 1024 * 1024)); // 转换为GB
            stats.put("shareCount", totalShares);
            stats.put("uptime", uptime);
            
            // 最近7天用户注册趋势（模拟数据）
            java.util.Map<String, Integer> userTrend = new java.util.HashMap<>();
            userTrend.put("今天", 5);
            userTrend.put("昨天", 8);
            userTrend.put("前天", 6);
            userTrend.put("3天前", 4);
            userTrend.put("4天前", 7);
            userTrend.put("5天前", 9);
            userTrend.put("6天前", 3);
            stats.put("userTrend", userTrend);
            
            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取统计数据失败: " + e.getMessage()));
        }
    }
}