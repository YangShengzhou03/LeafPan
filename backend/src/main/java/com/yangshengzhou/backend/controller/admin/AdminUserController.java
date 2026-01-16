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

import java.util.HashMap;
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
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || currentUser.getRole() != 1) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            Page<User> users = userService.getAllUsers(page, size);
            
            // 创建分页响应对象，避免直接返回PageImpl
            Map<String, Object> response = new HashMap<>();
            response.put("content", users.getContent());
            response.put("pageNumber", users.getNumber());
            response.put("pageSize", users.getSize());
            response.put("totalElements", users.getTotalElements());
            response.put("totalPages", users.getTotalPages());
            response.put("first", users.isFirst());
            response.put("last", users.isLast());
            
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取用户列表失败: " + e.getMessage()));
        }
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable String id, HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || currentUser.getRole() != 1) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            // 获取用户信息用于日志
            User user;
            try {
                user = userService.getUserById(id);
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(ApiResponse.error("用户不存在"));
            }
            
            userService.deleteUser(id);
            
            if (true) {
                // 记录操作日志
                operationLogService.logOperation(
                    currentUser.getId(),
                    "DELETE_USER",
                    "USER",
                    user.getId(),
                    "管理员" + currentUser.getEmail() + "删除用户" + user.getEmail(),
                    getClientIpAddress(request),
                    request.getHeader("User-Agent")
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
            @PathVariable String id, 
            @RequestParam boolean enabled,
            HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || currentUser.getRole() != 1) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            // 获取用户信息用于日志
            User user;
            try {
                user = userService.getUserById(id);
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(ApiResponse.error("用户不存在"));
            }
            
            userService.updateUserStatus(id, enabled);
            
            if (true) {
                // 记录操作日志
                operationLogService.logOperation(
                    currentUser.getId(),
                    "UPDATE_USER_STATUS",
                    "USER",
                    user.getId(),
                    "管理员" + currentUser.getEmail() + (enabled ? "启用" : "禁用") + "用户" + user.getEmail(),
                    getClientIpAddress(request),
                    request.getHeader("User-Agent")
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
     * 创建用户
     */
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user, HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || currentUser.getRole() != 1) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            User createdUser = userService.createUser(user);
            
            if (createdUser != null) {
                // 记录操作日志
                operationLogService.logOperation(
                    currentUser.getId(),
                    "CREATE_USER",
                    "USER",
                    createdUser.getId(),
                    "管理员" + currentUser.getEmail() + "创建用户" + createdUser.getEmail(),
                    getClientIpAddress(request),
                    request.getHeader("User-Agent")
                );
                
                return ResponseEntity.ok(ApiResponse.success(createdUser));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("用户创建失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("创建用户失败: " + e.getMessage()));
        }
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @PathVariable String id, 
            @RequestBody Map<String, Object> updateData,
            HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || currentUser.getRole() != 1) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            // 获取用户信息用于日志
            User user;
            try {
                user = userService.getUserById(id);
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(ApiResponse.error("用户不存在"));
            }
            
            // 将Map转换为User对象
            User updatedUser = new User();
            if (updateData.containsKey("email")) {
                updatedUser.setEmail((String) updateData.get("email"));
            }
            if (updateData.containsKey("nickname")) {
                updatedUser.setNickname((String) updateData.get("nickname"));
            }
            if (updateData.containsKey("phone")) {
                updatedUser.setPhone((String) updateData.get("phone"));
            }
            if (updateData.containsKey("gender")) {
                updatedUser.setGender(((Number) updateData.get("gender")).byteValue());
            }
            if (updateData.containsKey("role")) {
                updatedUser.setRole(((Number) updateData.get("role")).byteValue());
            }
            if (updateData.containsKey("status")) {
                updatedUser.setStatus(((Number) updateData.get("status")).byteValue());
            }
            if (updateData.containsKey("storageQuota")) {
                updatedUser.setStorageQuota(((Number) updateData.get("storageQuota")).longValue());
            }
            
            User result = userService.updateUser(id, updatedUser);
            
            if (result != null) {
                // 记录操作日志
                operationLogService.logOperation(
                    currentUser.getId(),
                    "UPDATE_USER",
                    "USER",
                    user.getId(),
                    "管理员" + currentUser.getEmail() + "更新用户" + user.getEmail() + "的信息",
                    getClientIpAddress(request),
                    request.getHeader("User-Agent")
                );
                
                return ResponseEntity.ok(ApiResponse.success(result));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("用户更新失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("更新用户失败: " + e.getMessage()));
        }
    }
    
    /**
     * 重置用户密码
     */
    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponse<String>> resetUserPassword(
            @PathVariable String id, 
            @RequestParam String newPassword,
            HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || currentUser.getRole() != 1) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            // 获取用户信息用于日志
            User user;
            try {
                user = userService.getUserById(id);
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(ApiResponse.error("用户不存在"));
            }
            
            userService.resetUserPassword(id, newPassword);
            
            if (true) {
                // 记录操作日志
                operationLogService.logOperation(
                    currentUser.getId(),
                    "RESET_USER_PASSWORD",
                    "USER",
                    user.getId(),
                    "管理员" + currentUser.getEmail() + "重置用户" + user.getEmail() + "的密码",
                    getClientIpAddress(request),
                    request.getHeader("User-Agent")
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