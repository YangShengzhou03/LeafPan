package com.yangshengzhou.backend.controller.user;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.service.AuthService;
import com.yangshengzhou.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthService authService;
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<User>> getUserProfile() {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser != null) {
                // 不返回密码
                currentUser.setPassword(null);
                return ResponseEntity.ok(ApiResponse.success(currentUser));
            } else {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取用户信息失败: " + e.getMessage()));
        }
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<User>> updateUserProfile(@RequestBody Map<String, String> updateData) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            User updatedUser = userService.updateUser(currentUser.getId(), updateData);
            if (updatedUser != null) {
                // 不返回密码
                updatedUser.setPassword(null);
                return ResponseEntity.ok(ApiResponse.success("更新成功", updatedUser));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("更新失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("更新用户信息失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取用户存储使用情况
     */
    @GetMapping("/storage")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserStorageUsage() {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            Map<String, Object> storageInfo = userService.getUserStorageUsage(currentUser.getId());
            return ResponseEntity.ok(ApiResponse.success(storageInfo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取存储使用情况失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取用户操作日志
     */
    @GetMapping("/logs")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserOperationLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String type) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            Page logs;
            if (type != null && !type.isEmpty()) {
                logs = userService.getUserOperationLogsByType(currentUser.getId(), type, page, size);
            } else {
                logs = userService.getUserOperationLogs(currentUser.getId(), page, size);
            }
            
            // 创建分页响应对象，避免直接返回PageImpl
            Map<String, Object> response = new HashMap<>();
            response.put("content", logs.getContent());
            response.put("pageNumber", logs.getNumber());
            response.put("pageSize", logs.getSize());
            response.put("totalElements", logs.getTotalElements());
            response.put("totalPages", logs.getTotalPages());
            response.put("first", logs.isFirst());
            response.put("last", logs.isLast());
            
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取操作日志失败: " + e.getMessage()));
        }
    }
}