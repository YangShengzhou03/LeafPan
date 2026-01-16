package com.yangshengzhou.backend.controller.user;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.dto.ChangePasswordRequest;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.service.AuthService;
import com.yangshengzhou.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
     * 获取用户列表（用于分享时选择用户）
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<User>>> getUserList() {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            List<User> users = userService.getAllUsers();
            
            // 过滤掉当前用户，不返回密码
            List<User> filteredUsers = users.stream()
                .filter(user -> !user.getId().equals(currentUser.getId()))
                .peek(user -> user.setPassword(null))
                .toList();
            
            return ResponseEntity.ok(ApiResponse.success(filteredUsers));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取用户列表失败: " + e.getMessage()));
        }
    }
    
    /**
     * 修改密码
     */
    @PutMapping("/password")
    public ResponseEntity<ApiResponse<String>> changePassword(@RequestBody ChangePasswordRequest request, 
                                                            HttpServletRequest httpRequest) {
        try {
            String ipAddress = getClientIpAddress(httpRequest);
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            boolean success = authService.changePassword(
                currentUser.getId(), 
                request.getOldPassword(), 
                request.getNewPassword(), 
                ipAddress
            );
            
            if (success) {
                return ResponseEntity.ok(ApiResponse.success("密码修改成功"));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("旧密码不正确"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("密码修改失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取客户端IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}