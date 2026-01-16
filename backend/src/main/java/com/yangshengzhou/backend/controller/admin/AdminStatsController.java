package com.yangshengzhou.backend.controller.admin;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.service.AuthService;
import com.yangshengzhou.backend.service.FileService;
import com.yangshengzhou.backend.service.FolderService;
import com.yangshengzhou.backend.service.ShareService;
import com.yangshengzhou.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminStatsController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private FolderService folderService;

    @Autowired
    private ShareService shareService;

    @Autowired
    private AuthService authService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || currentUser.getRole() != 1) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }

            Map<String, Object> stats = new HashMap<>();

            stats.put("userCount", userService.getUserCount());
            stats.put("fileCount", fileService.getFileCount());
            stats.put("folderCount", folderService.getFolderCount());
            stats.put("shareCount", shareService.getShareCount());
            stats.put("usedStorage", fileService.getTotalStorageUsed());
            stats.put("totalStorage", userService.getTotalStorageQuota());
            stats.put("uptime", calculateUptime());

            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取统计数据失败: " + e.getMessage()));
        }
    }

    private String calculateUptime() {
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
        LocalDateTime now = LocalDateTime.now();
        
        Duration duration = Duration.between(startTime, now);
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        
        return String.format("%d天 %d小时 %d分钟", days, hours, minutes);
    }
}
