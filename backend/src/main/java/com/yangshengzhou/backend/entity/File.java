package com.yangshengzhou.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "original_name", nullable = false)
    private String originalName;
    
    @Column(name = "folder_id", nullable = false)
    private Long folderId = 0L;
    
    @Column(name = "user_id", nullable = false, columnDefinition = "CHAR(36)")
    private String userId;
    
    @Column(nullable = false)
    private Long size = 0L;
    
    @Column(name = "mime_type", length = 100)
    private String mimeType;
    
    @Column(length = 20)
    private String extension;
    
    @Column(name = "storage_key", nullable = false)
    private String storageKey;
    
    @Column(length = 32)
    private String md5;
    
    @Column(name = "is_deleted", nullable = false)
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.TINYINT)
    private Boolean isDeleted = false;
    
    @Column(nullable = false)
    private Integer version = 1;
    
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;
    
    @Column(name = "updated_time", nullable = false)
    private LocalDateTime updatedTime;
    
    // Constructor
    public File() {}
    
    public File(String filename, String originalFilename, String contentType, Long size, String path, Long folderId, String userId) {
        this.name = filename;
        this.originalName = originalFilename;
        this.mimeType = contentType;
        this.size = size;
        this.storageKey = path;
        this.folderId = folderId;
        this.userId = userId;
    }
    
    // JPA lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
        updatedTime = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getOriginalName() {
        return originalName;
    }
    
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
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
    
    public Long getSize() {
        return size;
    }
    
    public void setSize(Long size) {
        this.size = size;
    }
    
    public String getMimeType() {
        return mimeType;
    }
    
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    
    public String getExtension() {
        return extension;
    }
    
    public void setExtension(String extension) {
        this.extension = extension;
    }
    
    public String getStorageKey() {
        return storageKey;
    }
    
    public void setStorageKey(String storageKey) {
        this.storageKey = storageKey;
    }
    
    public String getMd5() {
        return md5;
    }
    
    public void setMd5(String md5) {
        this.md5 = md5;
    }
    
    public Boolean getIsDeleted() {
        return isDeleted;
    }
    
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public Integer getVersion() {
        return version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }
    
    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
    
    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }
    
    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public void setContentType(String contentType) {
        this.mimeType = contentType;
    }

    public void setPath(String fileName) {
        this.storageKey = fileName;
    }

    public void setUploadTime(Date date) {
        this.createdTime = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
    }
}
