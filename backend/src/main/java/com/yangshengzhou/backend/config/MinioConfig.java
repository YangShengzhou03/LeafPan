package com.yangshengzhou.backend.config;

import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
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
            MinioClient client = minioClient();
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
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            logger.error("初始化MinIO失败: {}", e.getMessage());
        }
    }
    
    public String getBucketName() {
        return bucketName;
    }
}