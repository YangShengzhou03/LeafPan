package com.yangshengzhou.backend.controller;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.dto.ChangePasswordRequest;
import com.yangshengzhou.backend.dto.LoginRequest;
import com.yangshengzhou.backend.dto.RegisterRequest;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                loginRequest.getEmail(), 
                loginRequest.getPassword(), 
                ipAddress
            );
            return ResponseEntity.ok(ApiResponse.success("登录成功", result));
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
     * 刷新Token
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Map<String, Object>>> refreshToken(@RequestBody Map<String, String> tokenMap) {
        try {
            String refreshToken = tokenMap.get("refreshToken");
            Map<String, Object> result = authService.refreshToken(refreshToken);
            return ResponseEntity.ok(ApiResponse.success("Token刷新成功", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Token刷新失败: " + e.getMessage()));
        }
    }
    
    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestBody Map<String, String> tokenMap) {
        try {
            String token = tokenMap.get("token");
            authService.logout(token);
            return ResponseEntity.ok(ApiResponse.success("登出成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("登出失败: " + e.getMessage()));
        }
    }
    
    /**
     * 忘记密码
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            authService.sendPasswordResetEmail(email);
            return ResponseEntity.ok(ApiResponse.success("密码重置邮件已发送"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("发送密码重置邮件失败: " + e.getMessage()));
        }
    }
    
    /**
     * 重置密码
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String code = request.get("code");
            String newPassword = request.get("newPassword");
            authService.resetPassword(email, code, newPassword);
            return ResponseEntity.ok(ApiResponse.success("密码重置成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("密码重置失败: " + e.getMessage()));
        }
    }
    
    /**
     * 验证邮箱
     */
    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            authService.verifyEmail(token);
            return ResponseEntity.ok(ApiResponse.success("邮箱验证成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("邮箱验证失败: " + e.getMessage()));
        }
    }
    
    /**
     * 重新发送验证邮件
     */
    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<String>> resendVerificationEmail(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            authService.resendVerificationEmail(email);
            return ResponseEntity.ok(ApiResponse.success("验证邮件已重新发送"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("发送验证邮件失败: " + e.getMessage()));
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