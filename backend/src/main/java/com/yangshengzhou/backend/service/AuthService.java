package com.yangshengzhou.backend.service;

import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.repository.UserRepository;
import com.yangshengzhou.backend.service.OperationLogService;
import com.yangshengzhou.backend.utils.JwtUtil;
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
    
    /**
     * 用户登录
     */
    public Map<String, Object> login(String email, String password, String ipAddress) {
        // 通过邮箱查找用户
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("邮箱或密码错误"));
        
        // 认证用户
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getEmail(), password)
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 生成JWT令牌
        String token = jwtUtil.generateToken(user.getEmail());
        
        // 记录登录日志
        operationLogService.logOperation(user.getId(), "LOGIN", "用户", user.getId(), user.getEmail(), "用户登录", ipAddress, "");
        
        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);
        
        // 返回登录结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        
        return result;
    }
    
    /**
     * 用户注册
     */
    public User register(String email, String password, String ipAddress) {
        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 创建新用户
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname("JaSun"); // 设置默认昵称
        user.setRole((byte) 0); // 使用byte类型
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());
        
        // 保存用户
        user = userRepository.save(user);
        
        // 记录注册日志
        operationLogService.logOperation(user.getId(), "REGISTER", "用户", user.getId(), user.getEmail(), "用户注册", ipAddress, "");
        
        return user;
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
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedTime(LocalDateTime.now());
        userRepository.save(user);
        
        // 记录修改密码日志
        operationLogService.logOperation(userId, "CHANGE_PASSWORD", "用户", userId, user.getEmail(), "修改密码", ipAddress, "");
        
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
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedTime(LocalDateTime.now());
        userRepository.save(user);
        
        // 记录重置密码日志
        operationLogService.logOperation(operatorId, "RESET_PASSWORD", "用户", userId, user.getEmail(), 
            "重置用户密码: " + user.getEmail(), ipAddress, "");
        
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
                currentUser.getEmail(), "用户登出", "", "");
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
     * 重置密码
     */
    public void resetPassword(String email, String code, String newPassword) {
        try {
            // 验证验证码
            boolean isValid = verificationCodeService.verifyCode(email, code);
            if (!isValid) {
                throw new RuntimeException("验证码无效或已过期");
            }
            
            // 查找用户
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
            
            // 更新密码
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            
            // 记录操作日志
            operationLogService.logOperation(user.getId(), "PASSWORD_RESET", "USER", user.getId(), user.getEmail(), "用户重置密码", "", "");
        } catch (Exception e) {
            throw new RuntimeException("重置密码失败: " + e.getMessage());
        }
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
}