package com.yangshengzhou.backend.config;

import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
@ConditionalOnProperty(prefix = "minio", name = "endpoint")
public class MinioConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(MinioConfig.class);
    
    @Value("${minio.endpoint}")
    private String endpoint;
    
    @Value("${minio.access-key}")
    private String accessKey;
    
    @Value("${minio.secret-key}")
    private String secretKey;
    
    @Value("${minio.bucket-name}")
    private String bucketName;
    
    @Value("${minio.data-bucket}")
    private String dataBucket;
    
    @Value("${minio.avatar-bucket}")
    private String avatarBucket;
    
    @Value("${minio.public-endpoint:}")
    private String publicEndpoint;
    
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
    
    @PostConstruct
    public void init() {
        try {
            // 直接创建MinioClient实例，避免循环依赖
            MinioClient client = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();
            
            // 创建并检查默认桶
            createBucketIfNotExists(client, bucketName);
            
            // 创建并检查数据存储桶
            createBucketIfNotExists(client, dataBucket);
            
            // 创建并检查头像存储桶
            createBucketIfNotExists(client, avatarBucket);
            
            // 设置头像存储桶为公共读取权限
            setBucketPublicReadPolicy(client, avatarBucket);
            
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            logger.error("初始化MinIO失败: {}", e.getMessage());
        }
    }
    
    /**
     * 创建桶（如果不存在）
     */
    private void createBucketIfNotExists(MinioClient client, String bucketName) 
            throws MinioException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        boolean bucketExists = client.bucketExists(
            io.minio.BucketExistsArgs.builder()
                .bucket(bucketName)
                .build()
        );
        
        if (!bucketExists) {
            client.makeBucket(
                MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build()
            );
            logger.info("MinIO bucket '{}' 创建成功", bucketName);
        } else {
            logger.info("MinIO bucket '{}' 已存在", bucketName);
        }
    }
    
    /**
     * 设置桶的公共读取权限
     */
    private void setBucketPublicReadPolicy(MinioClient client, String bucketName) 
            throws MinioException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        try {
            // 设置桶策略为公共读取
            String policy = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":\"*\",\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::" + bucketName + "/*\"]}]}";
            
            client.setBucketPolicy(
                io.minio.SetBucketPolicyArgs.builder()
                    .bucket(bucketName)
                    .config(policy)
                    .build()
            );
            
            logger.info("MinIO bucket '{}' 已设置为公共读取权限", bucketName);
        } catch (Exception e) {
            logger.warn("设置桶 '{}' 公共读取权限失败: {}", bucketName, e.getMessage());
        }
    }
    
    public String getBucketName() {
        return bucketName;
    }
    
    public String getDataBucket() {
        return dataBucket;
    }
    
    public String getAvatarBucket() {
        return avatarBucket;
    }
    
    public String getEndpoint() {
        return endpoint;
    }
    
    public String getPublicEndpoint() {
        return publicEndpoint;
    }
}