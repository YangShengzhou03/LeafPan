package com.yangshengzhou.backend.controller.user;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.entity.File;
import com.yangshengzhou.backend.entity.Share;
import com.yangshengzhou.backend.service.AuthService;
import com.yangshengzhou.backend.service.FileService;
import com.yangshengzhou.backend.service.FileStorageService;
import com.yangshengzhou.backend.service.ShareService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/share")
public class ShareController {
    
    @Autowired
    private ShareService shareService;
    
    @Autowired
    private FileService fileService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @Autowired
    private AuthService authService;
    
    /**
     * 创建文件分享
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Share>> createShare(@RequestBody Map<String, Object> requestBody, 
                                                         HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            Long fileId = Long.valueOf(requestBody.get("fileId").toString());
            Integer expireDays = requestBody.get("expireDays") != null ? 
                (Integer) requestBody.get("expireDays") : null;
            
            // 检查文件是否属于当前用户
            if (!fileService.isFileOwnedByUser(fileId, currentUser.getId())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权分享此文件"));
            }
            
            Share share = shareService.createShare(fileId, currentUser.getId(), expireDays);
            
            return ResponseEntity.ok(ApiResponse.success("分享创建成功", share));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("创建分享失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取用户的分享列表
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<Share>>> getUserShares() {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            List<Share> shares = shareService.getUserShares(currentUser.getId());
            return ResponseEntity.ok(ApiResponse.success(shares));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取分享列表失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取用户的有效分享列表
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<Share>>> getUserActiveShares() {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            List<Share> shares = shareService.getUserValidShares(currentUser.getId());
            return ResponseEntity.ok(ApiResponse.success(shares));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取有效分享列表失败: " + e.getMessage()));
        }
    }
    
    /**
     * 更新分享
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Share>> updateShare(@PathVariable Long id, 
                                                         @RequestBody Map<String, Object> requestBody) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            if (!shareService.isShareOwnedByUser(id, currentUser.getId())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权修改此分享"));
            }
            
            Integer expireDays = requestBody.get("expireDays") != null ? 
                (Integer) requestBody.get("expireDays") : null;
            
            Share updatedShare = shareService.updateShare(id, expireDays);
            if (updatedShare != null) {
                return ResponseEntity.ok(ApiResponse.success("分享更新成功", updatedShare));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("分享更新失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("更新分享失败: " + e.getMessage()));
        }
    }
    
    /**
     * 删除分享
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteShare(@PathVariable Long id) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            if (!shareService.isShareOwnedByUser(id, currentUser.getId())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权删除此分享"));
            }
            
            boolean deleted = shareService.deleteShare(id);
            
            if (deleted) {
                return ResponseEntity.ok(ApiResponse.success("分享删除成功"));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("分享删除失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("删除分享失败: " + e.getMessage()));
        }
    }
    
    /**
     * 通过分享码访问分享文件
     */
    @GetMapping("/public/{shareCode}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSharedFile(@PathVariable String shareCode) {
        try {
            Share share = shareService.getValidShareByCode(shareCode).orElse(null);
            if (share == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("分享不存在或已过期"));
            }
            
            File file = fileService.getFile(share.getFileId()).orElse(null);
            if (file == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("分享的文件不存在"));
            }
            
            try {
                String previewUrl = fileStorageService.getFileUrl(file.getName());
                
                return ResponseEntity.ok(ApiResponse.success(Map.of(
                    "file", file,
                    "previewUrl", previewUrl
                )));
            } catch (Exception e) {
                return ResponseEntity.ok(ApiResponse.success(Map.of(
                    "file", file
                )));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取分享文件失败: " + e.getMessage()));
        }
    }
}