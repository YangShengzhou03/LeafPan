package com.yangshengzhou.backend.service;

import com.yangshengzhou.backend.config.MinioConfig;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.repository.UserRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class AvatarService {
    
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;
    private final MinioConfig minioConfig;
    private final MinioClient minioClient;
    
    @Autowired
    public AvatarService(FileStorageService fileStorageService, UserRepository userRepository, MinioConfig minioConfig, MinioClient minioClient) {
        this.fileStorageService = fileStorageService;
        this.userRepository = userRepository;
        this.minioConfig = minioConfig;
        this.minioClient = minioClient;
    }
    
    /**
     * 上传用户头像
     * @param avatarFile 头像文件
     * @param userEmail 用户邮箱
     * @return 头像URL
     */
    public String uploadAvatar(MultipartFile avatarFile, String userEmail) throws Exception {
        // 验证文件类型，只允许图片
        if (!isImageFile(avatarFile)) {
            throw new IllegalArgumentException("只支持图片格式作为头像");
        }
        
        // 获取用户信息
        User user = userRepository.findByEmail(userEmail)
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
                // 删除失败不影响上传新头像
            }
        }
        
        // 上传新头像到avatar桶（不保存到文件表）
        String avatarFileName = uploadAvatarFileToMinio(avatarFile, user.getId());
        
        // 获取头像URL - 使用公网地址替换本地地址
        String avatarUrl = fileStorageService.getFileUrl(avatarFileName, FileStorageService.BucketType.AVATAR);

        // 替换本地地址为公网地址
        avatarUrl = replaceLocalUrlWithPublicUrl(avatarUrl);

        // 如果URL过长，使用简化的直接访问URL
        if (avatarUrl.length() > 500) {
            // 使用MinIO的直接访问URL格式：http://endpoint/bucket/object
            avatarUrl = replaceLocalUrlWithPublicUrl(minioConfig.getEndpoint()) + "/" + minioConfig.getAvatarBucket() + "/" + avatarFileName;
        }
        
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
        
        // 预签名URL通常包含查询参数，需要去掉
        int queryIndex = url.indexOf('?');
        String path = queryIndex > 0 ? url.substring(0, queryIndex) : url;
        
        // 获取路径的最后一部分作为文件名
        int lastSlashIndex = path.lastIndexOf('/');
        return lastSlashIndex >= 0 && lastSlashIndex < path.length() - 1 
            ? path.substring(lastSlashIndex + 1) 
            : null;
    }
    
    /**
     * 上传头像文件到MinIO（不保存到数据库的文件表中）
     * @param avatarFile 头像文件
     * @param userId 用户ID
     * @return 文件名
     */
    private String uploadAvatarFileToMinio(MultipartFile avatarFile, String userId) throws Exception {
        // 验证文件名
        String originalFilename = avatarFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }
        
        // 获取文件扩展名
        String fileExtension = getFileExtension(originalFilename);
        
        // 生成唯一文件名
        String fileName = UUID.randomUUID().toString() + "." + fileExtension;
        
        // 确保头像存储桶存在
        fileStorageService.ensureBucketExists(FileStorageService.BucketType.AVATAR);
        
        // 上传文件到avatar桶
        String bucketName = minioConfig.getAvatarBucket();
        try (InputStream inputStream = avatarFile.getInputStream()) {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(inputStream, avatarFile.getSize(), -1)
                    .contentType(avatarFile.getContentType())
                    .build()
            );
        }
        
        return fileName;
    }
    
    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }
    
    /**
     * 替换本地地址为公网地址
     * @param url 原始URL
     * @return 替换后的URL
     */
    private String replaceLocalUrlWithPublicUrl(String url) {
        if (url == null || url.isEmpty()) {
            return url;
        }
        
        // 从配置中获取公网地址，如果没有配置则使用默认值
        String publicEndpoint = minioConfig.getPublicEndpoint();
        if (publicEndpoint == null || publicEndpoint.isEmpty()) {
            // 如果没有配置公网地址，则使用当前环境的endpoint
            publicEndpoint = minioConfig.getEndpoint();
        }
        
        // 替换本地地址
        if (url.contains("localhost:9000")) {
            return url.replace("http://localhost:9000", publicEndpoint);
        }
        if (url.contains("127.0.0.1:9000")) {
            return url.replace("http://127.0.0.1:9000", publicEndpoint);
        }
        
        return url;
    }
}