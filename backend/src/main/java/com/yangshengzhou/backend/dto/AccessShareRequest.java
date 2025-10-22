package com.yangshengzhou.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class AccessShareRequest {
    
    @NotBlank(message = "分享码不能为空")
    private String shareCode;
    
    private String password; // 如果是密码保护的分享
    
    public AccessShareRequest() {}
    
    public AccessShareRequest(String shareCode, String password) {
        this.shareCode = shareCode;
        this.password = password;
    }
    
    public String getShareCode() {
        return shareCode;
    }
    
    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}