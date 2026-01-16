package com.yangshengzhou.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateShareRequest {
    
    @NotNull(message = "分享目标ID不能为空")
    private Long targetId;
    
    @NotBlank(message = "分享目标类型不能为空")
    private String targetType; // "file" 或 "folder"
    
    private Integer shareType = 0; // 0-公开，1-密码，2-私密
    
    private String password;
    
    private String sharedTo; // 分享给的用户ID
    
    private String expireTime; // ISO格式的时间字符串
    
    public CreateShareRequest() {}
    
    public CreateShareRequest(Long targetId, String targetType, Integer shareType, String password, String expireTime) {
        this.targetId = targetId;
        this.targetType = targetType;
        this.shareType = shareType;
        this.password = password;
        this.expireTime = expireTime;
    }
    
    public Long getTargetId() {
        return targetId;
    }
    
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
    
    public String getTargetType() {
        return targetType;
    }
    
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
    
    public Integer getShareType() {
        return shareType;
    }
    
    public void setShareType(Integer shareType) {
        this.shareType = shareType;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getSharedTo() {
        return sharedTo;
    }
    
    public void setSharedTo(String sharedTo) {
        this.sharedTo = sharedTo;
    }
    
    public String getExpireTime() {
        return expireTime;
    }
    
    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }
}