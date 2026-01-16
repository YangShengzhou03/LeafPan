package com.yangshengzhou.backend.service;

import com.yangshengzhou.backend.config.MinioConfig;
import com.yangshengzhou.backend.entity.File;
import com.yangshengzhou.backend.event.StorageUpdateEvent;
import com.yangshengzhou.backend.repository.FileRepository;
import io.minio.*;
import io.minio.http.Method;
import io.minio.StatObjectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import io.minio.SetBucketPolicyArgs;

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
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    /**
     * 上传文件到MinIO
     */
    public String uploadFile(MultipartFile multipartFile, String userId, Long folderId) throws Exception {
        return uploadFile(multipartFile, userId, folderId, BucketType.DATA);
    }
    
    /**
     * 上传文件到MinIO并返回文件实体
     */
    public File uploadFileAndSaveInfo(MultipartFile multipartFile, String userId, Long folderId) throws Exception {
        return uploadFileAndSaveInfo(multipartFile, userId, folderId, BucketType.DATA);
    }
    
    /**
     * 上传文件到MinIO（指定桶类型）
     */
    public String uploadFile(MultipartFile multipartFile, String userId, Long folderId, BucketType bucketType) throws Exception {
        File fileEntity = uploadFileAndSaveInfo(multipartFile, userId, folderId, bucketType);
        return fileEntity.getName();
    }
    
    /**
     * 上传文件到MinIO并返回文件实体（指定桶类型）
     */
    public File uploadFileAndSaveInfo(MultipartFile multipartFile, String userId, Long folderId, BucketType bucketType) throws Exception {
        // 验证文件名
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }
        
        String fileExtension = getFileExtension(originalFilename);
        
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
        
        // 如果是头像文件，不保存到数据库的文件表中
        File fileEntity = null;
        if (bucketType != BucketType.AVATAR) {
            // 保存文件信息到数据库（仅非头像文件）
            fileEntity = new File();
            fileEntity.setName(fileName);
            fileEntity.setOriginalName(originalFilename);
            fileEntity.setSize(multipartFile.getSize());
            fileEntity.setContentType(multipartFile.getContentType());
            fileEntity.setUserId(userId);
            fileEntity.setFolderId(folderId);
            fileEntity.setPath(fileName);
            fileEntity.setUploadTime(new Date());
            
            fileRepository.save(fileEntity);
            
            // 发布存储更新事件（异步处理）
            eventPublisher.publishEvent(new StorageUpdateEvent(userId, multipartFile.getSize(), true));
        } else {
            // 头像文件，返回一个临时的File对象，只包含存储key
            fileEntity = new File();
            fileEntity.setName(fileName);
            fileEntity.setPath(fileName);
        }
        
        return fileEntity;
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
            
            // 如果是头像桶，设置公共读取权限
            if (bucketType == BucketType.AVATAR) {
                setBucketPublicReadPolicy(bucketName);
            }
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
        String presignedUrl = minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(fileName)
                .expiry(1, TimeUnit.HOURS)
                .build()
        );
        
        // 替换本地地址为公网地址
        return replaceLocalUrlWithPublicUrl(presignedUrl);
    }
    
    /**
     * 获取文件的直接访问URL（无签名）
     */
    public String getDirectFileUrl(String fileName, BucketType bucketType) throws Exception {
        String bucketName = getBucketNameByType(bucketType);
        
        // 直接使用配置的endpoint构建URL
        String baseUrl = minioConfig.getEndpoint();
        String directUrl = baseUrl + "/" + bucketName + "/" + fileName;
        
        return directUrl;
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
     * 获取文件大小信息
     */
    public long getFileSize(String fileName) throws Exception {
        return getFileSize(fileName, BucketType.DATA);
    }
    
    /**
     * 获取文件大小信息（指定桶类型）
     */
    public long getFileSize(String fileName, BucketType bucketType) throws Exception {
        String bucketName = getBucketNameByType(bucketType);
        StatObjectResponse stat = minioClient.statObject(
            StatObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build()
        );
        return stat.size();
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
     * 设置桶的公共读取权限
     */
    private void setBucketPublicReadPolicy(String bucketName) {
        try {
            // 设置桶策略为公共读取
            String policy = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":\"*\",\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::" + bucketName + "/*\"]}]}";
            
            minioClient.setBucketPolicy(
                io.minio.SetBucketPolicyArgs.builder()
                    .bucket(bucketName)
                    .config(policy)
                    .build()
            );
        } catch (Exception e) {
            // 设置失败不影响正常功能，只是头像无法直接访问
        }
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
        
        // 替换localhost为公网IP或域名
        // 这里需要根据实际部署环境配置公网地址
        String publicEndpoint = "http://120.55.50.51:9000"; // 替换为实际的公网地址
        
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