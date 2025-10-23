package com.yangshengzhou.backend.service;

import com.yangshengzhou.backend.config.MinioConfig;
import com.yangshengzhou.backend.entity.File;
import com.yangshengzhou.backend.repository.FileRepository;
import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class FileStorageService {
    
    @Autowired
    private MinioClient minioClient;
    
    @Autowired
    private MinioConfig minioConfig;
    
    @Autowired
    private FileRepository fileRepository;
    
    @Value("${app.storage.allowed-extensions}")
    private String allowedExtensions;
    
    /**
     * 上传文件到MinIO
     */
    public String uploadFile(MultipartFile multipartFile, String userId, Long folderId) throws Exception {
        return uploadFile(multipartFile, userId, folderId, BucketType.DATA);
    }
    
    /**
     * 上传文件到MinIO（指定桶类型）
     */
    public String uploadFile(MultipartFile multipartFile, String userId, Long folderId, BucketType bucketType) throws Exception {
        // 验证文件扩展名
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }
        
        String fileExtension = getFileExtension(originalFilename);
        if (!isAllowedExtension(fileExtension)) {
            throw new IllegalArgumentException("不支持的文件类型: " + fileExtension);
        }
        
        // 生成唯一文件名
        String fileName = UUID.randomUUID().toString() + "." + fileExtension;
        
        // 确保存储桶存在
        ensureBucketExists(bucketType);
        
        // 根据桶类型选择桶名
        String bucketName = getBucketNameByType(bucketType);
        
        // 上传文件
        try (InputStream inputStream = multipartFile.getInputStream()) {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(inputStream, multipartFile.getSize(), -1)
                    .contentType(multipartFile.getContentType())
                    .build()
            );
        }
        
        // 保存文件信息到数据库
        File fileEntity = new File();
        fileEntity.setName(fileName);
        fileEntity.setOriginalName(originalFilename);
        fileEntity.setSize(multipartFile.getSize());
        fileEntity.setContentType(multipartFile.getContentType());
        fileEntity.setUserId(userId);
        fileEntity.setFolderId(folderId);
        fileEntity.setPath(fileName);
        fileEntity.setUploadTime(new Date());
        
        fileRepository.save(fileEntity);
        
        return fileName;
    }
    
    /**
     * 根据桶类型获取桶名
     */
    private String getBucketNameByType(BucketType bucketType) {
        switch (bucketType) {
            case DATA:
                return minioConfig.getDataBucket();
            case AVATAR:
                return minioConfig.getAvatarBucket();
            default:
                return minioConfig.getDataBucket();
        }
    }
    
    /**
     * 确保存储桶存在，如果不存在则创建
     * @param bucketType 桶类型
     * @throws Exception 创建桶时可能抛出的异常
     */
    public void ensureBucketExists(BucketType bucketType) throws Exception {
        String bucketName = getBucketNameByType(bucketType);
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!isExist) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }
    
    /**
     * 桶类型枚举
     */
    public enum BucketType {
        DATA,    // 用户数据文件
        AVATAR   // 用户头像
    }
    
    /**
     * 获取文件的预签名URL
     */
    public String getFileUrl(String fileName) throws Exception {
        return getFileUrl(fileName, BucketType.DATA);
    }
    
    /**
     * 获取文件的预签名URL（指定桶类型）
     */
    public String getFileUrl(String fileName, BucketType bucketType) throws Exception {
        String bucketName = getBucketNameByType(bucketType);
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(fileName)
                .expiry(1, TimeUnit.HOURS)
                .build()
        );
    }
    
    /**
     * 删除文件
     */
    public void deleteFile(String fileName) throws Exception {
        deleteFile(fileName, BucketType.DATA);
    }
    
    /**
     * 删除文件（指定桶类型）
     */
    public void deleteFile(String fileName, BucketType bucketType) throws Exception {
        String bucketName = getBucketNameByType(bucketType);
        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build()
        );
    }
    
    /**
     * 下载文件
     */
    public InputStream downloadFile(String fileName) throws Exception {
        return downloadFile(fileName, BucketType.DATA);
    }
    
    /**
     * 下载文件（指定桶类型）
     */
    public InputStream downloadFile(String fileName, BucketType bucketType) throws Exception {
        String bucketName = getBucketNameByType(bucketType);
        return minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build()
        );
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
     * 检查文件扩展名是否允许
     */
    private boolean isAllowedExtension(String extension) {
        if (extension == null || extension.isEmpty()) {
            return false;
        }
        
        String[] allowedExts = allowedExtensions.split(",");
        for (String allowedExt : allowedExts) {
            if (extension.trim().equalsIgnoreCase(allowedExt.trim())) {
                return true;
            }
        }
        return false;
    }
}