package com.yangshengzhou.backend.controller.user;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.service.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user/avatar")
public class AvatarController {
    
    @Autowired
    private AvatarService avatarService;
    
    /**
     * 上传用户头像
     * @param file 头像文件
     * @return 头像URL
     */
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            // 获取当前登录用户ID
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();
            
            // 上传头像
            String avatarUrl = avatarService.uploadAvatar(file, userId);
            
            return ResponseEntity.ok(ApiResponse.success(avatarUrl, "头像上传成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 删除用户头像
     * @return 操作结果
     */
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteAvatar() {
        try {
            // 获取当前登录用户ID
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();
            
            // 删除头像
            avatarService.deleteAvatar(userId);
            
            return ResponseEntity.ok(ApiResponse.success(null, "头像删除成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 获取用户头像URL
     * @return 头像URL
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<String>> getAvatar() {
        try {
            // 获取当前登录用户ID
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();
            
            // 获取头像URL
            String avatarUrl = avatarService.getAvatarUrl(userId);
            
            return ResponseEntity.ok(ApiResponse.success(avatarUrl, "获取头像成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}