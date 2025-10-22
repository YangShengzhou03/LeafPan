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
    
    /**
     * 用户登录
     */
    public Map<String, Object> login(String username, String password, String ipAddress) {
        // 认证用户
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 获取用户信息
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 生成JWT令牌
        String token = jwtUtil.generateToken(username);
        
        // 记录登录日志
        operationLogService.logOperation(user.getId(), "LOGIN", "用户", user.getId(), user.getUsername(), "用户登录", ipAddress, "");
        
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
    public User register(String username, String email, String password, String ipAddress) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole((byte) 0); // 使用byte类型
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());
        
        // 保存用户
        user = userRepository.save(user);
        
        // 记录注册日志
        operationLogService.logOperation(user.getId(), "REGISTER", "用户", user.getId(), user.getUsername(), "用户注册", ipAddress, "");
        
        return user;
    }
    
    /**
     * 获取当前登录用户
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        }
        return null;
    }
    
    /**
     * 修改密码
     */
    public boolean changePassword(Long userId, String oldPassword, String newPassword, String ipAddress) {
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
        operationLogService.logOperation(userId, "CHANGE_PASSWORD", "用户", userId, user.getUsername(), "修改密码", ipAddress, "");
        
        return true;
    }
    
    /**
     * 重置密码（管理员功能）
     */
    public boolean resetPassword(Long userId, String newPassword, Long operatorId, String ipAddress) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return false;
        }
        
        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedTime(LocalDateTime.now());
        userRepository.save(user);
        
        // 记录重置密码日志
        operationLogService.logOperation(operatorId, "RESET_PASSWORD", "用户", userId, user.getUsername(), 
            "重置用户密码: " + user.getUsername(), ipAddress, "");
        
        return true;
    }
    
    /**
     * 刷新Token
     */
    public Map<String, Object> refreshToken(String refreshToken) {
        // 这里需要实现token刷新逻辑
        // 暂时返回空结果
        throw new RuntimeException("Token刷新功能暂未实现");
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
                currentUser.getUsername(), "用户登出", "", "");
        }
    }
    
    /**
     * 发送密码重置邮件
     */
    public void sendPasswordResetEmail(String email) {
        // 这里需要实现发送密码重置邮件的逻辑
        throw new RuntimeException("密码重置邮件功能暂未实现");
    }
    
    /**
     * 重置密码（通过token）
     */
    public void resetPassword(String token, String newPassword) {
        // 这里需要实现通过token重置密码的逻辑
        throw new RuntimeException("通过token重置密码功能暂未实现");
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