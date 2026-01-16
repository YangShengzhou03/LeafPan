package com.yangshengzhou.backend.controller.user;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.service.AuthService;
import com.yangshengzhou.backend.service.AvatarService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/avatar")
public class AvatarController {

    @Autowired
    private AvatarService avatarService;

    @Autowired
    private AuthService authService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            String userId = authService.getCurrentUser().getId();
            String avatarUrl = avatarService.uploadAvatar(userId, file);
            return ResponseEntity.ok(ApiResponse.success("头像上传成功", avatarUrl));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("头像上传失败: " + e.getMessage()));
        }
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAvatar() {
        try {
            String userId = authService.getCurrentUser().getId();
            String avatarUrl = avatarService.getAvatarUrl(userId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("avatarUrl", avatarUrl);
            result.put("hasAvatar", avatarUrl != null);
            
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取头像失败: " + e.getMessage()));
        }
    }

    @GetMapping("/view/{userId}")
    public void viewAvatar(@PathVariable String userId, HttpServletResponse response) {
        try {
            String storageKey = avatarService.getAvatarStorageKey(userId);
            if (storageKey == null) {
                response.setStatus(404);
                return;
            }

            InputStream inputStream = avatarService.getAvatarInputStream(storageKey);
            if (inputStream == null) {
                response.setStatus(404);
                return;
            }

            response.setContentType("image/jpeg");
            response.setHeader("Cache-Control", "public, max-age=3600");
            
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, bytesRead);
            }
            
            inputStream.close();
        } catch (Exception e) {
            response.setStatus(500);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<ApiResponse<String>> deleteAvatar() {
        try {
            String userId = authService.getCurrentUser().getId();
            avatarService.deleteAvatar(userId);
            return ResponseEntity.ok(ApiResponse.success("头像删除成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("头像删除失败: " + e.getMessage()));
        }
    }
}
