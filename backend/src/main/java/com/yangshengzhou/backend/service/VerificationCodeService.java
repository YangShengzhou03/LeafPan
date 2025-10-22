package com.yangshengzhou.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
public class VerificationCodeService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String CODE_PREFIX = "verification_code:";
    private static final int CODE_LENGTH = 6;
    private static final int EXPIRE_MINUTES = 5;

    /**
     * 生成6位数字验证码
     * @param email 用户邮箱
     * @return 验证码
     */
    public String generateCode(String email) {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        
        String codeStr = code.toString();
        
        // 存储到Redis，5分钟过期
        redisTemplate.opsForValue().set(
            CODE_PREFIX + email, 
            codeStr, 
            EXPIRE_MINUTES, 
            TimeUnit.MINUTES
        );
        
        return codeStr;
    }

    /**
     * 验证验证码
     * @param email 用户邮箱
     * @param code 用户输入的验证码
     * @return 验证结果
     */
    public boolean verifyCode(String email, String code) {
        String storedCode = redisTemplate.opsForValue().get(CODE_PREFIX + email);
        
        if (storedCode == null) {
            return false; // 验证码不存在或已过期
        }
        
        boolean isValid = storedCode.equals(code);
        
        if (isValid) {
            // 验证成功后删除验证码
            redisTemplate.delete(CODE_PREFIX + email);
        }
        
        return isValid;
    }
}