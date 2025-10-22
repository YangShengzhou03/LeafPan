package com.yangshengzhou.backend.controller.admin;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.service.AuthService;
import com.yangshengzhou.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/admin/user")
public class AdminUserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthService authService;
    
    /**
     * 获取所有用户列表
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Page>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            Page users = userService.getAllUsers(page, size);
            
            // 不返回密码
            users.getContent().forEach(user -> ((User) user).setPassword(null));
            
            return ResponseEntity.ok(ApiResponse.success(users));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取用户列表失败: " + e.getMessage()));
        }
    }
    
    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            return userService.getUserById(id)
                .map(user -> {
                    user.setPassword(null);
                    return ResponseEntity.ok(ApiResponse.success(user));
                })
                .orElse(ResponseEntity.badRequest().body(ApiResponse.error("用户不存在")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取用户信息失败: " + e.getMessage()));
        }
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id, 
                                                       @RequestBody Map<String, Object> updateData) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            User updatedUser = userService.updateUser(id, updateData);
            if (updatedUser != null) {
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
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            // 不能删除自己
            if (currentUser.getId().equals(id)) {
                return ResponseEntity.badRequest().body(ApiResponse.error("不能删除自己"));
            }
            
            boolean deleted = userService.deleteUser(id);
            
            if (deleted) {
                return ResponseEntity.ok(ApiResponse.success("用户删除成功"));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("用户删除失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("删除用户失败: " + e.getMessage()));
        }
    }
    
    /**
     * 重置用户密码
     */
    @PostMapping("/{id}/reset-password")
    public ResponseEntity<ApiResponse<String>> resetUserPassword(@PathVariable Long id, 
                                                               @RequestBody Map<String, String> requestBody,
                                                               HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            String newPassword = requestBody.get("password");
            if (newPassword == null || newPassword.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("新密码不能为空"));
            }
            
            boolean success = authService.resetPassword(id, newPassword, currentUser.getId(), getClientIpAddress(request));
            
            if (success) {
                return ResponseEntity.ok(ApiResponse.success("密码重置成功"));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("密码重置失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("重置密码失败: " + e.getMessage()));
        }
    }
    
    /**
     * 禁用/启用用户
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<String>> updateUserStatus(@PathVariable Long id, 
                                                              @RequestBody Map<String, Boolean> requestBody) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            // 不能禁用自己
            if (currentUser.getId().equals(id)) {
                return ResponseEntity.badRequest().body(ApiResponse.error("不能禁用自己"));
            }
            
            Boolean enabled = requestBody.get("enabled");
            if (enabled == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("状态参数不能为空"));
            }
            
            boolean success = userService.updateUserStatus(id, enabled);
            
            if (success) {
                String message = enabled ? "用户启用成功" : "用户禁用成功";
                return ResponseEntity.ok(ApiResponse.success(message));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("更新用户状态失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("更新用户状态失败: " + e.getMessage()));
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