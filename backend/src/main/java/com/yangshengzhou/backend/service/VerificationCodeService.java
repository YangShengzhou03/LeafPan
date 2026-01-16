package com.yangshengzhou.backend.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.UUID;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class VerificationCodeService {

    private final Map<String, CodeEntry> codeStorage = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleanupScheduler = Executors.newSingleThreadScheduledExecutor();

    private static final String CODE_PREFIX = "verification_code:";
    private static final int CODE_LENGTH = 6;
    private static final int EXPIRE_MINUTES = 5;
    
    private static class CodeEntry {
        String code;
        long expiryTime;
        
        CodeEntry(String code, long expiryTime) {
            this.code = code;
            this.expiryTime = expiryTime;
        }
        
        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }
    
    public VerificationCodeService() {
        // 启动定时清理任务，每分钟清理一次过期验证码
        cleanupScheduler.scheduleAtFixedRate(this::cleanupExpiredCodes, 1, 1, TimeUnit.MINUTES);
    }
    
    private void cleanupExpiredCodes() {
        codeStorage.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

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
        
        // 存储到内存，5分钟过期
        long expiryTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(EXPIRE_MINUTES);
        codeStorage.put(CODE_PREFIX + email, new CodeEntry(codeStr, expiryTime));
        
        return codeStr;
    }

    /**
     * 验证验证码
     * @param email 用户邮箱
     * @param code 用户输入的验证码
     * @return 验证结果
     */
    public boolean verifyCode(String email, String code) {
        CodeEntry entry = codeStorage.get(CODE_PREFIX + email);
        
        if (entry == null || entry.isExpired()) {
            return false; // 验证码不存在或已过期
        }
        
        boolean isValid = entry.code.equals(code);
        
        if (isValid) {
            // 验证成功后删除验证码
            codeStorage.remove(CODE_PREFIX + email);
        }
        
        return isValid;
    }
    

    
    /**
     * 删除指定邮箱的验证码
     * @param email 邮箱地址
     */
    public void deleteCode(String email) {
        codeStorage.remove(CODE_PREFIX + email);
    }
    
    /**
     * 生成密码重置令牌
     * @param email 邮箱地址
     * @return 重置令牌
     */
    public String generateResetToken(String email) {
        // 生成一个简单的重置令牌（实际项目中应该使用更安全的方式）
        String token = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        
        // 存储令牌和邮箱的映射关系，设置过期时间（10分钟）
        String tokenKey = "RESET_TOKEN_" + token;
        long expiryTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10);
        codeStorage.put(tokenKey, new CodeEntry(email, expiryTime));
        
        return token;
    }
    
    /**
     * 验证重置令牌
     * @param token 重置令牌
     * @return 邮箱地址，如果令牌无效返回null
     */
    public String verifyResetToken(String token) {
        String tokenKey = "RESET_TOKEN_" + token;
        CodeEntry entry = codeStorage.get(tokenKey);
        
        if (entry == null || entry.isExpired()) {
            return null; // 令牌不存在或已过期
        }
        
        return entry.code; // 返回存储的邮箱地址
    }
}