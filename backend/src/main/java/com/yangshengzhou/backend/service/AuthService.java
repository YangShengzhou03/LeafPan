package com.yangshengzhou.backend.service;

import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.repository.UserRepository;
import com.yangshengzhou.backend.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private OperationLogService operationLogService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private VerificationCodeService verificationCodeService;
    
    @Autowired
    private SystemConfigService systemConfigService;
    
    /**
     * 用户登录
     */
    public Map<String, Object> login(String email, String password, String ipAddress) {
        try {
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("邮箱或密码错误"));
            
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), password)
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            String token = jwtUtil.generateToken(user.getEmail());
            
            operationLogService.logOperation(user.getId(), "LOGIN", "用户", user.getId(), "用户" + user.getEmail() + "登录", ipAddress, "");
            
            user.setLastLoginTime(LocalDateTime.now());
            userRepository.save(user);
            
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("user", user);
            
            return result;
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * 用户注册
     */
    public Map<String, Object> register(String email, String password, String phone, String verificationCode, String ipAddress) {
        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 验证验证码
        if (verificationCode == null || verificationCode.trim().isEmpty()) {
            throw new RuntimeException("验证码不能为空");
        }
        
        boolean isCodeValid = verificationCodeService.verifyCode(email, verificationCode);
        if (!isCodeValid) {
            throw new RuntimeException("验证码错误或已过期");
        }
        
        // 获取新用户默认存储容量（MB转换为字节）
        Long defaultUserQuota = getDefaultUserQuotaInBytes();
        
        // 创建新用户
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone); // 设置手机号
        user.setNickname("JaSun"); // 设置默认昵称
        user.setRole((byte) 0); // 使用byte类型
        user.setStorageQuota(defaultUserQuota); // 设置默认存储容量
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());
        
        // 保存用户
        user = userRepository.save(user);
        
        // 记录注册日志
        operationLogService.logOperation(user.getId(), "REGISTER", "用户", user.getId(), "用户" + email + "注册", ipAddress, "");
        
        // 注册成功后自动登录
        try {
            // 生成JWT令牌
            String token = jwtUtil.generateToken(user.getEmail());
            
            // 设置认证上下文
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), null, null
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 更新最后登录时间
            user.setLastLoginTime(LocalDateTime.now());
            userRepository.save(user);
            
            // 记录登录日志
            operationLogService.logOperation(user.getId(), "LOGIN", "用户", user.getId(), "用户" + user.getEmail() + "注册后自动登录", ipAddress, "");
            
            // 不返回密码
            user.setPassword(null);
            
            // 返回登录结果
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("user", user);
            
            return result;
        } catch (Exception e) {
            // 如果自动登录失败，仍然返回用户信息（但不包含token）
            user.setPassword(null);
            Map<String, Object> result = new HashMap<>();
            result.put("user", user);
            return result;
        }
    }
    
    /**
     * 登录或注册（使用验证码）
     * @param email 邮箱地址
     * @param verificationCode 验证码
     * @param ipAddress IP地址
     * @return 登录或注册结果
     */
    public Map<String, Object> loginOrRegister(String email, String verificationCode, String ipAddress) {
        // 验证验证码
        if (verificationCode == null || verificationCode.trim().isEmpty()) {
            throw new RuntimeException("验证码不能为空");
        }
        
        boolean isCodeValid = verificationCodeService.verifyCode(email, verificationCode);
        if (!isCodeValid) {
            throw new RuntimeException("验证码错误或已过期");
        }
        
        // 检查邮箱是否存在
        if (userRepository.existsByEmail(email)) {
            // 邮箱已存在，执行登录
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
            
            // 生成JWT令牌
            String token = jwtUtil.generateToken(user.getEmail());
            
            // 设置认证上下文
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), null, null
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 记录登录日志
            operationLogService.logOperation(user.getId(), "LOGIN", "用户", user.getId(), "用户" + user.getEmail() + "登录", ipAddress, "");
            
            // 更新最后登录时间
            user.setLastLoginTime(LocalDateTime.now());
            userRepository.save(user);
            
            // 不返回密码
            user.setPassword(null);
            
            // 返回登录结果
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("user", user);
            
            return result;
        } else {
            // 邮箱不存在，执行注册
            // 获取新用户默认存储容量（MB转换为字节）
            Long defaultUserQuota = getDefaultUserQuotaInBytes();
            
            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setEmail(email);
            user.setPassword(""); // 验证码登录不设置密码
            user.setPhone("");
            user.setNickname("JaSun"); // 设置默认昵称
            user.setRole((byte) 0); // 使用byte类型
            user.setStorageQuota(defaultUserQuota); // 设置默认存储容量
            user.setCreatedTime(LocalDateTime.now());
            user.setUpdatedTime(LocalDateTime.now());
            
            // 保存用户
            user = userRepository.save(user);
            
            // 记录注册日志
            operationLogService.logOperation(user.getId(), "REGISTER", "用户", user.getId(), "用户" + email + "注册", ipAddress, "");
            
            // 生成JWT令牌
            String token = jwtUtil.generateToken(user.getEmail());
            
            // 设置认证上下文
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), null, null
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 更新最后登录时间
            user.setLastLoginTime(LocalDateTime.now());
            userRepository.save(user);
            
            // 记录登录日志
            operationLogService.logOperation(user.getId(), "LOGIN", "用户", user.getId(), "用户" + user.getEmail() + "注册后自动登录", ipAddress, "");
            
            // 不返回密码
            user.setPassword(null);
            
            // 返回登录结果
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("user", user);
            
            return result;
        }
    }
    
    /**
     * 获取当前登录用户
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            
            if (authentication.isAuthenticated()) {
                String email = authentication.getName();
                User user = userRepository.findByEmail(email).orElse(null);
                return user;
            }
        }
        return null;
    }
    
    /**
     * 修改密码
     */
    public boolean changePassword(String userId, String oldPassword, String newPassword, String ipAddress) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return false;
        }
        
        User user = userOptional.get();
        
        // 验证旧密码
        if (!oldPassword.equals(user.getPassword())) {
            return false;
        }
        
        // 更新密码
        user.setPassword(newPassword);
        user.setUpdatedTime(LocalDateTime.now());
        userRepository.save(user);
        
        // 记录修改密码日志
        operationLogService.logOperation(userId, "CHANGE_PASSWORD", "用户", userId, "用户" + user.getEmail() + "修改密码", ipAddress, "");
        
        return true;
    }
    
    /**
     * 重置密码（管理员功能）
     */
    public boolean resetPassword(String userId, String newPassword, String operatorId, String ipAddress) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return false;
        }
        
        User user = userOptional.get();
        user.setPassword(newPassword);
        user.setUpdatedTime(LocalDateTime.now());
        userRepository.save(user);
        
        // 记录重置密码日志
        operationLogService.logOperation(operatorId, "RESET_PASSWORD", "用户", userId,
                "管理员重置用户" + user.getEmail() + "的密码", ipAddress, "");
        
        return true;
    }
    
    /**
     * 刷新Token
     */
    public Map<String, Object> refreshToken(String token) {
        try {
            // 验证token是否有效
            String email = jwtUtil.getUsernameFromToken(token);
            
            if (email == null) {
                throw new RuntimeException("无效的token");
            }
            
            // 获取用户信息
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
            
            // 生成新的token
            String newToken = jwtUtil.generateToken(email);
            
            // 返回新的token和用户信息
            Map<String, Object> result = new HashMap<>();
            result.put("token", newToken);
            result.put("user", user);
            
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Token刷新失败: " + e.getMessage());
        }
    }
    
    /**
     * 用户登出
     */
    public void logout(String token) {
        // 这里需要实现登出逻辑，比如将token加入黑名单
        // 暂时只记录日志
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            operationLogService.logOperation(currentUser.getId(), "LOGOUT", "用户", currentUser.getId(),
                    "用户" + currentUser.getEmail() + "登出", "", "");
        }
    }
    
    /**
     * 发送密码重置邮件
     */
    public void sendPasswordResetEmail(String email) {
        try {
            // 检查邮箱是否存在
            if (!userRepository.existsByEmail(email)) {
                throw new RuntimeException("该邮箱未注册");
            }
            
            // 生成重置密码的验证码
            String code = verificationCodeService.generateCode(email);
            
            // 发送密码重置邮件
            emailService.sendPasswordResetCode(email, code);
        } catch (Exception e) {
            throw new RuntimeException("发送密码重置邮件失败: " + e.getMessage());
        }
    }
    
    /**
     * 重置密码（使用重置令牌）
     * @param email 邮箱地址
     * @param resetToken 重置令牌
     * @param newPassword 新密码
     */
    public void resetPasswordWithToken(String email, String resetToken, String newPassword) {
        try {
            // 验证重置令牌
            String verifiedEmail = verificationCodeService.verifyResetToken(resetToken);
            
            if (verifiedEmail == null || !verifiedEmail.equals(email)) {
                throw new RuntimeException("重置令牌无效或已过期");
            }
            
            // 查找用户
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
            
            // 更新密码
            user.setPassword(newPassword);
            userRepository.save(user);
            
            // 密码重置成功后删除验证码（如果存在）
            verificationCodeService.deleteCode(email);
            
            // 记录操作日志
            operationLogService.logOperation(user.getId(), "PASSWORD_RESET", "USER", user.getId(), "用户" + email + "使用重置令牌重置密码", "", "");
        } catch (Exception e) {
            throw new RuntimeException("重置密码失败: " + e.getMessage());
        }
    }
    
    /**
     * 重置密码（使用验证码）
     * @param email 邮箱地址
     * @param code 验证码
     * @param newPassword 新密码
     */
    public void resetPassword(String email, String code, String newPassword) {
        try {
            // 验证验证码但不删除（使用自定义验证逻辑）
            boolean isValid = verifyCodeWithoutDelete(email, code);
            if (!isValid) {
                throw new RuntimeException("验证码无效或已过期");
            }
            
            // 查找用户
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
            
            // 更新密码
            user.setPassword(newPassword);
            userRepository.save(user);
            
            // 密码重置成功后删除验证码
            verificationCodeService.deleteCode(email);
            
            // 记录操作日志
            operationLogService.logOperation(user.getId(), "PASSWORD_RESET", "USER", user.getId(), "用户" + email + "重置密码", "", "");
        } catch (Exception e) {
            throw new RuntimeException("重置密码失败: " + e.getMessage());
        }
    }
    
    /**
     * 验证验证码但不删除（用于密码重置流程）
     */
    private boolean verifyCodeWithoutDelete(String email, String code) {
        // 使用VerificationCodeService的公共方法进行验证
        // 先验证验证码，如果验证成功则立即重新存储验证码（模拟不删除的效果）
        boolean isValid = verificationCodeService.verifyCode(email, code);
        
        if (isValid) {
            // 如果验证成功，重新生成相同的验证码（模拟不删除的效果）
            // 注意：这里需要重新存储验证码，因为verifyCode方法会删除验证码
            String newCode = verificationCodeService.generateCode(email);
            // 这里我们实际上不需要使用newCode，因为验证已经通过
            return true;
        }
        
        return false;
    }
    
    /**
     * 验证邮箱
     */
    public void verifyEmail(String token) {
        // 这里需要实现邮箱验证的逻辑
        throw new RuntimeException("邮箱验证功能暂未实现");
    }
    
    /**
     * 重新发送验证邮件
     */
    public void resendVerificationEmail(String email) {
        // 这里需要实现重新发送验证邮件的逻辑
        throw new RuntimeException("重新发送验证邮件功能暂未实现");
    }
    
    /**
     * 获取新用户默认存储容量（字节）
     */
    private Long getDefaultUserQuotaInBytes() {
        try {
            var config = systemConfigService.getConfigByKey("storage.default_user_quota");
            if (config.isPresent()) {
                Long quotaMB = Long.parseLong(config.get().getConfigValue());
                return quotaMB * 1024 * 1024; // MB转换为字节
            }
        } catch (Exception e) {
            // 如果获取配置失败，使用默认值1024MB
        }
        return 1024L * 1024 * 1024; // 默认1GB
    }
}