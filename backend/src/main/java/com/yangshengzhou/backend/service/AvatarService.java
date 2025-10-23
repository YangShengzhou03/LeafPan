package com.yangshengzhou.backend.service;

import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.repository.UserRepository;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class AvatarService {
    
    // 使用构造器注入替代字段注入，避免循环依赖
    private final FileStorageService fileStorageService;
    
    @Autowired
    public AvatarService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 上传用户头像
     * @param avatarFile 头像文件
     * @param userId 用户ID
     * @return 头像URL
     */
    public String uploadAvatar(MultipartFile avatarFile, String userId) throws Exception {
        // 验证文件类型，只允许图片
        if (!isImageFile(avatarFile)) {
            throw new IllegalArgumentException("只支持图片格式作为头像");
        }
        
        // 获取用户信息
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        
        // 如果用户已有头像，先删除旧头像
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            try {
                // 从URL中提取文件名
                String oldAvatarFileName = extractFileNameFromUrl(user.getAvatar());
                if (oldAvatarFileName != null) {
                    fileStorageService.deleteFile(oldAvatarFileName, FileStorageService.BucketType.AVATAR);
                }
            } catch (Exception e) {
                // 删除失败不影响上传新头像，只记录日志
                // 删除旧头像失败，继续处理新头像上传
            }
        }
        
        // 上传新头像到avatar桶
        String avatarFileName = fileStorageService.uploadFile(
            avatarFile, 
            userId, 
            0L, // 头像不属于任何文件夹
            FileStorageService.BucketType.AVATAR
        );
        
        // 获取头像URL
        String avatarUrl = fileStorageService.getFileUrl(avatarFileName, FileStorageService.BucketType.AVATAR);
        
        // 更新用户头像URL
        user.setAvatar(avatarUrl);
        userRepository.save(user);
        
        return avatarUrl;
    }
    
    /**
     * 删除用户头像
     * @param userId 用户ID
     */
    public void deleteAvatar(String userId) throws Exception {
        // 获取用户信息
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            // 从URL中提取文件名
            String avatarFileName = extractFileNameFromUrl(user.getAvatar());
            if (avatarFileName != null) {
                fileStorageService.deleteFile(avatarFileName, FileStorageService.BucketType.AVATAR);
            }
            
            // 清空用户头像URL
            user.setAvatar(null);
            userRepository.save(user);
        }
    }
    
    /**
     * 获取用户头像URL
     * @param userId 用户ID
     * @return 头像URL
     */
    public String getAvatarUrl(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null ? user.getAvatar() : null;
    }
    
    /**
     * 检查文件是否为图片
     * @param file 文件
     * @return 是否为图片
     */
    private boolean isImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        
        String contentType = file.getContentType();
        if (contentType == null) {
            return false;
        }
        
        return contentType.startsWith("image/");
    }
    
    /**
     * 从URL中提取文件名
     * @param url 文件URL
     * @return 文件名
     */
    private String extractFileNameFromUrl(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        
        try {
            // 预签名URL通常包含查询参数，需要去掉
            String[] parts = url.split("\\?");
            if (parts.length > 0) {
                String path = parts[0];
                // 获取路径的最后一部分作为文件名
                String[] pathParts = path.split("/");
                return pathParts[pathParts.length - 1];
            }
        } catch (Exception e) {
            // 提取文件名失败
        }
        
        return null;
    }
}