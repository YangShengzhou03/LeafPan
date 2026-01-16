package com.yangshengzhou.backend.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * 验证工具类
 */
public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
    );
    
    /**
     * 验证邮箱格式
     * @param email 邮箱地址
     * @return 是否有效
     */
    public static boolean isValidEmail(String email) {
        return StringUtils.hasText(email) && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * 验证密码强度
     * 密码必须包含至少8个字符，包括大小写字母、数字和特殊字符
     * @param password 密码
     * @return 是否有效
     */
    public static boolean isValidPassword(String password) {
        return StringUtils.hasText(password) && PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * 验证验证码格式
     * @param code 验证码
     * @return 是否有效
     */
    public static boolean isValidVerificationCode(String code) {
        return StringUtils.hasText(code) && code.matches("\\d{6}");
    }
    
    /**
     * 验证用户名格式
     * @param username 用户名
     * @return 是否有效
     */
    public static boolean isValidUsername(String username) {
        return StringUtils.hasText(username) && 
               username.length() >= 3 && 
               username.length() <= 20 && 
               username.matches("^[a-zA-Z0-9_]+$");
    }
}