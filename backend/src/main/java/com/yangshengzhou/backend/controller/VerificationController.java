package com.yangshengzhou.backend.controller;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.service.VerificationCodeService;
import com.yangshengzhou.backend.service.EmailService;
import com.yangshengzhou.backend.service.UserService;
import com.yangshengzhou.backend.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/verification")
public class VerificationController {

    @Autowired
    private VerificationCodeService verificationCodeService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private UserService userService;

    /**
     * 发送验证码
     * @param request 包含邮箱的请求体
     * @return 发送结果
     */
    @PostMapping("/send")
    public ApiResponse<Object> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        
        if (email == null || email.trim().isEmpty()) {
            return ApiResponse.error(400, "邮箱不能为空");
        }
        
        if (!ValidationUtils.isValidEmail(email)) {
            return ApiResponse.error(400, "邮箱格式不正确");
        }
        
        // 检查邮箱是否已存在，如果存在则不发送验证码
        if (userService.existsByEmail(email)) {
            return ApiResponse.error(400, "该邮箱已注册，请直接登录");
        }
        
        try {
            // 生成验证码
            String code = verificationCodeService.generateCode(email);
            
            // 发送邮件
            emailService.sendVerificationCode(email, code);
            
            Map<String, Object> result = new HashMap<>();
            result.put("message", "验证码已发送");
            result.put("email", email);
            
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(500, "发送验证码失败: " + e.getMessage());
        }
    }

    /**
     * 发送密码重置验证码
     * @param request 包含邮箱的请求体
     * @return 发送结果
     */
    @PostMapping("/reset-password")
    public ApiResponse<Object> sendPasswordResetCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        
        if (email == null || email.trim().isEmpty()) {
            return ApiResponse.error(400, "邮箱不能为空");
        }
        
        if (!ValidationUtils.isValidEmail(email)) {
            return ApiResponse.error(400, "邮箱格式不正确");
        }
        
        // 检查邮箱是否存在，如果不存在则不发送验证码
        if (!userService.existsByEmail(email)) {
            return ApiResponse.error(400, "该邮箱未注册");
        }
        
        try {
            // 生成验证码
            String code = verificationCodeService.generateCode(email);
            
            // 发送密码重置邮件
            emailService.sendPasswordResetCode(email, code);
            
            Map<String, Object> result = new HashMap<>();
            result.put("message", "验证码已发送");
            result.put("email", email);
            
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(500, "发送验证码失败: " + e.getMessage());
        }
    }

    /**
     * 验证验证码
     * @param request 包含邮箱和验证码的请求体
     * @return 验证结果
     */
    @PostMapping("/verify")
    public ApiResponse<Object> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        String type = request.get("type"); // 新增：验证类型
        
        if (email == null || email.trim().isEmpty()) {
            return ApiResponse.error(400, "邮箱不能为空");
        }
        
        if (!ValidationUtils.isValidEmail(email)) {
            return ApiResponse.error(400, "邮箱格式不正确");
        }
        
        if (code == null || code.trim().isEmpty()) {
            return ApiResponse.error(400, "验证码不能为空");
        }
        
        if (!ValidationUtils.isValidVerificationCode(code)) {
            return ApiResponse.error(400, "验证码格式不正确");
        }
        
        boolean isValid = verificationCodeService.verifyCode(email, code);
        
        if (isValid) {
            Map<String, Object> result = new HashMap<>();
            result.put("message", "验证码验证成功");
            
            // 如果是密码重置验证，生成重置令牌
            if ("password_reset".equals(type)) {
                String resetToken = verificationCodeService.generateResetToken(email);
                result.put("resetToken", resetToken);
            }
            
            return ApiResponse.success(result);
        } else {
            return ApiResponse.error(400, "验证码错误或已过期");
        }
    }
}