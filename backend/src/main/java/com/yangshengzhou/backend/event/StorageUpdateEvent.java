package com.yangshengzhou.backend.event;

/**
 * 存储更新事件
 */
public class StorageUpdateEvent {
    
    private final String userId;
    private final Long fileSize;
    private final boolean isIncrease; // true表示增加存储，false表示减少存储
    
    public StorageUpdateEvent(String userId, Long fileSize, boolean isIncrease) {
        this.userId = userId;
        this.fileSize = fileSize;
        this.isIncrease = isIncrease;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public boolean isIncrease() {
        return isIncrease;
    }
}