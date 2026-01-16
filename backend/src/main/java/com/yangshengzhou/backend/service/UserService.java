package com.yangshengzhou.backend.service;

import com.yangshengzhou.backend.dto.ChangePasswordRequest;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private OperationLogService operationLogService;
    
    @Autowired
    private AvatarService avatarService;
    
    /**
     * 创建用户
     */
    public User createUser(User user) {
        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 设置UUID作为ID
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString());
        }
        
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        return userRepository.save(user);
    }
    
    /**
     * 更新用户信息
     */
    public User updateUser(String id, User userDetails) {
        User user = getUserById(id);
        
        // 更新用户信息
        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDetails.getEmail())) {
                throw new RuntimeException("邮箱已存在");
            }
            user.setEmail(userDetails.getEmail());
        }
        
        if (userDetails.getNickname() != null) {
            user.setNickname(userDetails.getNickname());
        }
        
        if (userDetails.getAvatar() != null) {
            user.setAvatar(userDetails.getAvatar());
        }
        
        if (userDetails.getStorageQuota() != null) {
            user.setStorageQuota(userDetails.getStorageQuota());
        }
        
        if (userDetails.getStatus() != null) {
            user.setStatus(userDetails.getStatus());
        }
        
        if (userDetails.getGender() != null) {
            user.setGender(userDetails.getGender());
        }
        
        if (userDetails.getPhone() != null) {
            user.setPhone(userDetails.getPhone());
        }
        
        return userRepository.save(user);
    }
    
    /**
     * 更新用户信息（从Map中获取数据）
     */
    public User updateUser(String id, java.util.Map<String, String> updateData) {
        User user = getUserById(id);
        
        // 更新用户信息
        if (updateData.containsKey("nickname")) {
            user.setNickname(updateData.get("nickname"));
        }
        
        if (updateData.containsKey("email")) {
            String newEmail = updateData.get("email");
            // 检查邮箱是否已被其他用户使用
            if (!newEmail.equals(user.getEmail()) && userRepository.existsByEmail(newEmail)) {
                throw new RuntimeException("邮箱已被使用");
            }
            user.setEmail(newEmail);
        }
        
        if (updateData.containsKey("avatar")) {
            user.setAvatar(updateData.get("avatar"));
        }
        
        if (updateData.containsKey("gender")) {
            user.setGender(Byte.valueOf(updateData.get("gender")));
        }
        
        if (updateData.containsKey("phone")) {
            user.setPhone(updateData.get("phone"));
        }
        
        if (updateData.containsKey("role")) {
            user.setRole(Byte.valueOf(updateData.get("role")));
        }
        
        if (updateData.containsKey("status")) {
            user.setStatus(Byte.valueOf(updateData.get("status")));
        }
        
        if (updateData.containsKey("storageQuota")) {
            user.setStorageQuota(Long.valueOf(updateData.get("storageQuota")));
        }
        
        return userRepository.save(user);
    }
    
    /**
     * 删除用户
     */
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
    
    /**
     * 根据ID获取用户
     */
    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }
    
    /**
     * 根据邮箱获取用户
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * 获取所有用户
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * 分页获取用户列表
     */
    public Page<User> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        return userRepository.findAll(pageable);
    }
    
    /**
     * 检查邮箱是否存在
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    /**
     * 统计活跃用户数量
     */
    public long countActiveUsers() {
        return userRepository.countActiveUsers();
    }
    
    /**
     * 更新用户最后登录信息
     */
    public User updateLastLogin(String userId, String ipAddress) {
        User user = getUserById(userId);
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(ipAddress);
        return userRepository.save(user);
    }
    
    /**
     * 验证密码
     */
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    
    /**
     * 加密密码
     */
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
    
    /**
     * 修改密码
     */
    public boolean changePassword(String userId, ChangePasswordRequest request) {
        User user = getUserById(userId);
        
        // 验证原密码
        if (!verifyPassword(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("原密码不正确");
        }
        
        // 验证新密码和确认密码是否一致
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("新密码和确认密码不一致");
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        
        return true;
    }
    
    /**
     * 重置密码
     */
    public boolean resetPassword(String userId, String newPassword) {
        User user = getUserById(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }
    
    /**
     * 更新用户状态
     */
    public User updateUserStatus(String userId, Byte status) {
        User user = getUserById(userId);
        user.setStatus(status);
        return userRepository.save(user);
    }
    
    /**
     * 更新用户存储配额
     */
    public User updateStorageQuota(String userId, Long storageQuota) {
        User user = getUserById(userId);
        user.setStorageQuota(storageQuota);
        return userRepository.save(user);
    }
    
    /**
     * 更新用户已用存储容量
     */
    public User updateUsedStorage(String userId, Long fileSize) {
        User user = getUserById(userId);
        user.setUsedStorage(user.getUsedStorage() + fileSize);
        return userRepository.save(user);
    }
    
    /**
     * 减少用户已用存储容量（用于文件删除）
     */
    public User decreaseUsedStorage(String userId, Long fileSize) {
        User user = getUserById(userId);
        user.setUsedStorage(Math.max(0, user.getUsedStorage() - fileSize));
        return userRepository.save(user);
    }
    
    /**
     * 检查用户是否为管理员
     */
    public boolean isAdmin(String userId) {
        User user = getUserById(userId);
        return user.getRole() != null && user.getRole().equals("ADMIN");
    }
    
    /**
     * 获取用户存储使用情况
     */
    public java.util.Map<String, Object> getUserStorageUsage(String userId) {
        User user = getUserById(userId);
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("storageQuota", user.getStorageQuota());
        result.put("usedStorage", user.getUsedStorage());
        result.put("availableStorage", user.getStorageQuota() - user.getUsedStorage());
        result.put("usagePercentage", (double) user.getUsedStorage() / user.getStorageQuota() * 100);
        return result;
    }
    
    /**
     * 检查用户存储容量是否足够
     * @param userId 用户ID
     * @param fileSize 文件大小（字节）
     * @return true表示容量足够，false表示容量不足
     */
    public boolean hasSufficientStorage(String userId, Long fileSize) {
        User user = getUserById(userId);
        Long availableStorage = user.getStorageQuota() - user.getUsedStorage();
        return availableStorage >= fileSize;
    }
    
    /**
     * 获取用户可用存储空间
     * @param userId 用户ID
     * @return 可用存储空间（字节）
     */
    public Long getAvailableStorage(String userId) {
        User user = getUserById(userId);
        return Math.max(0, user.getStorageQuota() - user.getUsedStorage());
    }
    
    /**
     * 根据状态获取用户列表
     */
    public List<User> getUsersByStatus(Integer status) {
        return userRepository.findByStatus(status);
    }
    
    /**
     * 获取用户操作日志
     */
    public Page getUserOperationLogs(String userId, int page, int size) {
        return operationLogService.getUserOperationLogs(userId, page, size);
    }
    
    /**
     * 根据类型获取用户操作日志
     */
    public Page getUserOperationLogsByType(String userId, String type, int page, int size) {
        return operationLogService.getUserOperationLogsByType(userId, type, page, size);
    }
    
    /**
     * 根据角色获取用户列表
     */
    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(Integer.valueOf(role));
    }
    
    /**
     * 更新用户状态（boolean版本）
     */
    public boolean updateUserStatus(String id, boolean enabled) {
        User user = getUserById(id);
        user.setStatus(enabled ? (byte) 1 : (byte) 0);
        userRepository.save(user);
        return true;
    }
    
    /**
     * 重置用户密码
     */
    public boolean resetUserPassword(String id, String newPassword) {
        User user = getUserById(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }
    
    /**
     * 获取用户统计信息
     */
    public Map<String, Object> getUserStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 总用户数
        long totalUsers = userRepository.count();
        statistics.put("totalUsers", totalUsers);
        
        // 活跃用户数
        long activeUsers = userRepository.countActiveUsers();
        statistics.put("activeUsers", activeUsers);
        
        // 管理员用户数
        List<User> adminUsers = userRepository.findByRole(1);
        statistics.put("adminUsers", adminUsers.size());
        
        // 普通用户数
        statistics.put("normalUsers", totalUsers - adminUsers.size());
        
        // 今日新增用户数（需要实现）
        statistics.put("todayNewUsers", 0);
        
        return statistics;
    }
    
    /**
     * 上传用户头像
     */
    public String uploadUserAvatar(String userId, MultipartFile avatarFile) throws Exception {
        return avatarService.uploadAvatar(userId, avatarFile);
    }
    
    /**
     * 删除用户头像
     */
    public void deleteUserAvatar(String userId) throws Exception {
        avatarService.deleteAvatar(userId);
    }
    
    /**
     * 获取用户头像URL
     */
    public String getUserAvatar(String userId) throws Exception {
        return avatarService.getAvatarUrl(userId);
    }

    /**
     * 获取用户总数
     */
    public long getUserCount() {
        return userRepository.count();
    }

    /**
     * 获取所有用户的存储配额总和
     */
    public long getTotalStorageQuota() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .mapToLong(User::getStorageQuota)
                .sum();
    }
}