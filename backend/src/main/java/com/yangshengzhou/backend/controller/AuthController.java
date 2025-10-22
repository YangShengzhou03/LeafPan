package com.yangshengzhou.backend.controller;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.dto.LoginRequest;
import com.yangshengzhou.backend.dto.RegisterRequest;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody LoginRequest loginRequest, 
                                                                HttpServletRequest request) {
        try {
            String ipAddress = getClientIpAddress(request);
            Map<String, Object> result = authService.login(
                loginRequest.getUsername(), 
                loginRequest.getPassword(), 
                ipAddress
            );
            return ResponseEntity.ok(ApiResponse.success("登录成功", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("登录失败: " + e.getMessage()));
        }
    }
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody RegisterRequest registerRequest, 
                                                     HttpServletRequest request) {
        try {
            String ipAddress = getClientIpAddress(request);
            User user = authService.register(
                registerRequest.getUsername(), 
                registerRequest.getEmail(), 
                registerRequest.getPassword(), 
                ipAddress
            );
            // 不返回密码
            user.setPassword(null);
            return ResponseEntity.ok(ApiResponse.success("注册成功", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("注册失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getCurrentUser() {
        try {
            User user = authService.getCurrentUser();
            if (user != null) {
                // 不返回密码
                user.setPassword(null);
                return ResponseEntity.ok(ApiResponse.success(user));
            } else {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取用户信息失败: " + e.getMessage()));
        }
    }
    
    /**
     * 修改密码
     */
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@RequestBody Map<String, String> passwordMap, 
                                                            HttpServletRequest request) {
        try {
            String ipAddress = getClientIpAddress(request);
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            boolean success = authService.changePassword(
                currentUser.getId(), 
                passwordMap.get("oldPassword"), 
                passwordMap.get("newPassword"), 
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