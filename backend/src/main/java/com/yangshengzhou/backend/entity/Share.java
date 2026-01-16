package com.yangshengzhou.backend.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "shares")
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "share_code", unique = true, nullable = false, length = 10)
    private String shareCode;
    
    @Column(name = "file_id")
    private Long fileId;
    
    @Column(name = "folder_id")
    private Long folderId;
    
    @Column(name = "user_id", nullable = false, columnDefinition = "CHAR(36)")
    private String userId;
    
    @Column(name = "shared_to", columnDefinition = "CHAR(36)")
    private String sharedTo;
    
    @Column(name = "share_type", nullable = false)
    private Byte shareType = 0; // 0-public, 1-password, 2-private
    
    @Column(length = 64)
    private String password;
    
    @Column(name = "expire_time")
    private LocalDateTime expireTime;
    
    @Column(name = "download_count", nullable = false)
    private Integer downloadCount = 0;
    
    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;
    
    @Column(name = "is_active", nullable = false)
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.TINYINT)
    private Boolean isActive = true;
    
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;
    
    // Constructor
    public Share() {}
    
    public Share(String shareCode, Long fileId, Long folderId, String userId, Byte shareType) {
        this.shareCode = shareCode;
        this.fileId = fileId;
        this.folderId = folderId;
        this.userId = userId;
        this.shareType = shareType;
    }
    
    // JPA lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }
    
    /**
     * Check if share is expired
     */
    public boolean isExpired() {
        if (expireTime == null) {
            return false; // No expiration time set, never expires
        }
        return LocalDateTime.now().isAfter(expireTime);
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getShareCode() {
        return shareCode;
    }
    
    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }
    
    public Long getFileId() {
        return fileId;
    }
    
    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
    
    public Long getFolderId() {
        return folderId;
    }
    
    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getSharedTo() {
        return sharedTo;
    }
    
    public void setSharedTo(String sharedTo) {
        this.sharedTo = sharedTo;
    }
    
    public Byte getShareType() {
        return shareType;
    }
    
    public void setShareType(Byte shareType) {
        this.shareType = shareType;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public LocalDateTime getExpireTime() {
        return expireTime;
    }
    
    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
    
    public Integer getDownloadCount() {
        return downloadCount;
    }
    
    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }
    
    public Integer getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }
    
    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getCreateTime() {
        return null;
    }
}
