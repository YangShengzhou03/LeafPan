package com.yangshengzhou.backend.controller.admin;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.service.AuthService;
import com.yangshengzhou.backend.service.FileService;
import com.yangshengzhou.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminDashboardController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private FileService fileService;
    

    
    /**
     * 获取仪表盘统计数据
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats() {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || currentUser.getRole() != 1) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            // 获取用户统计信息
            Map<String, Object> userStats = userService.getUserStatistics();
            
            // 获取文件统计信息
            Map<String, Object> fileStats = fileService.getFileStatistics();
            
            // 获取存储使用统计
            Map<String, Object> storageStats = fileService.getStorageUsage();
            
            // 组合仪表盘数据
            Map<String, Object> dashboardStats = new HashMap<>();
            dashboardStats.put("userCount", userStats.get("totalUsers") != null ? userStats.get("totalUsers") : 0);
            dashboardStats.put("fileCount", fileStats.get("totalFiles") != null ? fileStats.get("totalFiles") : 0);
            dashboardStats.put("usedStorage", storageStats.get("totalUsedStorage") != null ? storageStats.get("totalUsedStorage") : 0);
            dashboardStats.put("shareCount", fileStats.get("totalShares") != null ? fileStats.get("totalShares") : 0);
            dashboardStats.put("uptime", "系统运行正常");
            
            return ResponseEntity.ok(ApiResponse.success(dashboardStats));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取仪表盘数据失败: " + e.getMessage()));
        }
    }
    

}