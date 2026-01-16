package com.yangshengzhou.backend.service;

import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class AvatarService {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserRepository userRepository;

    private static final long MAX_AVATAR_SIZE = 2 * 1024 * 1024; // 2MB
    private static final String[] ALLOWED_TYPES = {"image/jpeg", "image/png", "image/gif"};

    public String uploadAvatar(String userId, MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        if (file.getSize() > MAX_AVATAR_SIZE) {
            throw new IllegalArgumentException("头像文件大小不能超过2MB");
        }

        String contentType = file.getContentType();
        if (!isAllowedType(contentType)) {
            throw new IllegalArgumentException("只支持 JPG、PNG、GIF 格式的图片");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String avatarFileName = "avatar_" + userId + "_" + UUID.randomUUID().toString() + "." + extension;

        String storageKey = fileStorageService.uploadFile(file, userId, 0L, FileStorageService.BucketType.AVATAR);

        user.setAvatar(storageKey);
        userRepository.save(user);

        return fileStorageService.getFileUrl(storageKey, FileStorageService.BucketType.AVATAR);
    }

    public String getAvatarUrl(String userId) throws Exception {
        return userRepository.findById(userId)
                .map(user -> {
                    if (user.getAvatar() != null) {
                        try {
                            return fileStorageService.getFileUrl(user.getAvatar(), FileStorageService.BucketType.AVATAR);
                        } catch (Exception e) {
                            throw new RuntimeException("获取头像URL失败", e);
                        }
                    }
                    return null;
                })
                .orElse(null);
    }

    public void deleteAvatar(String userId) throws Exception {
        userRepository.findById(userId).ifPresent(user -> {
            if (user.getAvatar() != null) {
                try {
                    fileStorageService.deleteFile(user.getAvatar());
                    user.setAvatar(null);
                    userRepository.save(user);
                } catch (Exception e) {
                    throw new RuntimeException("删除头像失败", e);
                }
            }
        });
    }

    public String getAvatarStorageKey(String userId) {
        return userRepository.findById(userId)
                .map(user -> user.getAvatar())
                .orElse(null);
    }

    public InputStream getAvatarInputStream(String storageKey) throws Exception {
        if (storageKey == null) {
            return null;
        }
        return fileStorageService.downloadFile(storageKey, FileStorageService.BucketType.AVATAR);
    }

    private boolean isAllowedType(String contentType) {
        if (contentType == null) {
            return false;
        }
        for (String type : ALLOWED_TYPES) {
            if (type.equals(contentType)) {
                return true;
            }
        }
        return false;
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return "jpg";
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }
}
