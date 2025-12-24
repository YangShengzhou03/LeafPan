package com.yangshengzhou.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "operation_logs")
public class OperationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false, columnDefinition = "CHAR(36)")
    private String userId;
    
    @Column(name = "operation_type", nullable = false, length = 50)
    private String operationType;
    
    @Column(name = "target_type", nullable = false, length = 50)
    private String targetType;
    
    @Column(name = "target_id", columnDefinition = "CHAR(36)")
    private String targetId;
    
    @Column(length = 500)
    private String description;
    
    @Column(name = "ip_address", length = 100)
    private String ipAddress;
    
    @Column(name = "user_agent", length = 500)
    private String userAgent;
    
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;
    
    // 构造函数
    public OperationLog() {}
    
    public OperationLog(String userId, String operationType, String targetType, String targetId) {
        this.userId = userId;
        this.operationType = operationType;
        this.targetType = targetType;
        this.targetId = targetId;
    }
    
    // JPA生命周期回调
    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }
    
    // Getter和Setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getOperationType() {
        return operationType;
    }
    
    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
    
    public String getTargetType() {
        return targetType;
    }
    
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
    
    public String getTargetId() {
        return targetId;
    }
    
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }
    
    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}