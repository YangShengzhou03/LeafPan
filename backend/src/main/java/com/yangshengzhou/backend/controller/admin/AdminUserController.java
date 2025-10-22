package com.yangshengzhou.backend.controller.admin;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.service.AuthService;
import com.yangshengzhou.backend.service.UserService;
import com.yangshengzhou.backend.service.OperationLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/user")
public class AdminUserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private OperationLogService operationLogService;
    
    /**
     * 获取所有用户列表
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Page<User>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            Page<User> users = userService.getAllUsers(page, size);
            
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
                .map(user -> ResponseEntity.ok(ApiResponse.success(user)))
                .orElse(ResponseEntity.badRequest().body(ApiResponse.error("用户不存在")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取用户信息失败: " + e.getMessage()));
        }
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id, @RequestBody Map<String, String> updateData, HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            User updatedUser = userService.updateUser(id, updateData);
            
            if (updatedUser != null) {
                // 记录操作日志
                operationLogService.logOperation(
                    currentUser.getId(),
                    "UPDATE_USER",
                    "USER",
                    updatedUser.getId(),
                    "管理员更新用户信息: " + updatedUser.getUsername(),
                    getClientIpAddress(request),
                    request.getHeader("User-Agent"),
                    ""
                );
                
                return ResponseEntity.ok(ApiResponse.success(updatedUser));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("用户不存在"));
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
            
            // 获取用户信息用于日志
            User user = userService.getUserById(id).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("用户不存在"));
            }
            
            boolean deleted = userService.deleteUser(id);
            
            if (deleted) {
                // 记录操作日志
                operationLogService.logOperation(
                    currentUser.getId(),
                    "DELETE_USER",
                    "USER",
                    user.getId(),
                    "管理员删除用户: " + user.getUsername(),
                    getClientIpAddress(request),
                    request.getHeader("User-Agent"),
                    ""
                );
                
                return ResponseEntity.ok(ApiResponse.success("用户删除成功"));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("用户删除失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("删除用户失败: " + e.getMessage()));
        }
    }
    
    /**
     * 禁用/启用用户
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<String>> updateUserStatus(
            @PathVariable Long id, 
            @RequestParam boolean enabled,
            HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            // 获取用户信息用于日志
            User user = userService.getUserById(id).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("用户不存在"));
            }
            
            boolean updated = userService.updateUserStatus(id, enabled);
            
            if (updated) {
                // 记录操作日志
                operationLogService.logOperation(
                    currentUser.getId(),
                    "UPDATE_USER_STATUS",
                    "USER",
                    user.getId(),
                    "管理员" + (enabled ? "启用" : "禁用") + "用户: " + user.getUsername(),
                    getClientIpAddress(request),
                    request.getHeader("User-Agent"),
                    ""
                );
                
                return ResponseEntity.ok(ApiResponse.success("用户状态更新成功"));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("用户状态更新失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("更新用户状态失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取用户统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserStatistics() {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            Map<String, Object> statistics = userService.getUserStatistics();
            
            return ResponseEntity.ok(ApiResponse.success(statistics));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取用户统计信息失败: " + e.getMessage()));
        }
    }
    
    /**
     * 重置用户密码
     */
    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponse<String>> resetUserPassword(
            @PathVariable Long id, 
            @RequestParam String newPassword,
            HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            // 获取用户信息用于日志
            User user = userService.getUserById(id).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("用户不存在"));
            }
            
            boolean updated = userService.resetUserPassword(id, newPassword);
            
            if (updated) {
                // 记录操作日志
                operationLogService.logOperation(
                    currentUser.getId(),
                    "RESET_USER_PASSWORD",
                    "USER",
                    user.getId(),
                    "管理员重置用户密码: " + user.getUsername(),
                    getClientIpAddress(request),
                    request.getHeader("User-Agent"),
                    ""
                );
                
                return ResponseEntity.ok(ApiResponse.success("用户密码重置成功"));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("用户密码重置失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("重置用户密码失败: " + e.getMessage()));
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