package com.yangshengzhou.backend.controller;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.dto.ChangePasswordRequest;
import com.yangshengzhou.backend.dto.LoginRequest;
import com.yangshengzhou.backend.dto.RegisterRequest;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private AuthService authService;
    
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
            logger.error("登录失败 - 邮箱: {}, 错误: {}", loginRequest.getEmail(), e.getMessage());
            String errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.contains("用户已失效")) {
                errorMessage = "用户已被禁用";
            }
            return ResponseEntity.badRequest().body(ApiResponse.error("登录失败: " + errorMessage));
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Map<String, Object>>> register(@RequestBody RegisterRequest registerRequest, 
                                                     HttpServletRequest request) {
        try {
            String ipAddress = getClientIpAddress(request);
            Map<String, Object> result = authService.register(
                registerRequest.getEmail(), 
                registerRequest.getPassword(), 
                registerRequest.getPhone(), 
                registerRequest.getVerificationCode(),
                ipAddress
            );
            return ResponseEntity.ok(ApiResponse.success("注册成功", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("注册失败: " + e.getMessage()));
        }
    }
    
    /**
     * 登录或注册（使用验证码）
     * @param request 包含邮箱和验证码的请求体
     * @return 登录或注册结果
     */
    @PostMapping("/login-or-register")
    public ResponseEntity<ApiResponse<Map<String, Object>>> loginOrRegister(@RequestBody Map<String, String> request,
                                                                         HttpServletRequest httpRequest) {
        try {
            String email = request.get("email");
            String verificationCode = request.get("verificationCode");
            String ipAddress = getClientIpAddress(httpRequest);
            
            Map<String, Object> result = authService.loginOrRegister(email, verificationCode, ipAddress);
            
            return ResponseEntity.ok(ApiResponse.success("登录成功", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("操作失败: " + e.getMessage()));
        }
    }
    
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getCurrentUser() {
        try {
            User user = authService.getCurrentUser();
            if (user != null) {
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
     * 重置密码（使用重置令牌）
     * @param request 包含邮箱、重置令牌和新密码的请求体
     * @return 重置结果
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String verificationCode = request.get("verificationCode");
            String newPassword = request.get("newPassword");
            
            // 如果提供了 verificationCode，使用验证码重置密码
            if (verificationCode != null && !verificationCode.isEmpty()) {
                authService.resetPassword(email, verificationCode, newPassword);
                return ResponseEntity.ok(ApiResponse.success("密码重置成功"));
            }
            
            // 否则使用重置令牌重置密码
            String resetToken = request.get("resetToken");
            authService.resetPasswordWithToken(email, resetToken, newPassword);
            return ResponseEntity.ok(ApiResponse.success("密码重置成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("密码重置失败: " + e.getMessage()));
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