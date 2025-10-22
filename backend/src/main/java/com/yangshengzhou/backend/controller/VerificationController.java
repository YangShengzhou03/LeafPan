package com.yangshengzhou.backend.controller;

import com.yangshengzhou.backend.service.VerificationCodeService;
import com.yangshengzhou.backend.service.EmailService;
import com.yangshengzhou.backend.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/verification")
public class VerificationController {

    @Autowired
    private VerificationCodeService verificationCodeService;
    
    @Autowired
    private EmailService emailService;

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
     * 验证验证码
     * @param request 包含邮箱和验证码的请求体
     * @return 验证结果
     */
    @PostMapping("/verify")
    public ApiResponse<Object> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        
        if (email == null || email.trim().isEmpty()) {
            return ApiResponse.error(400, "邮箱不能为空");
        }
        
        if (code == null || code.trim().isEmpty()) {
            return ApiResponse.error(400, "验证码不能为空");
        }
        
        boolean isValid = verificationCodeService.verifyCode(email, code);
        
        if (isValid) {
            Map<String, Object> result = new HashMap<>();
            result.put("message", "验证码验证成功");
            return ApiResponse.success(result);
        } else {
            return ApiResponse.error(400, "验证码错误或已过期");
        }
    }
}