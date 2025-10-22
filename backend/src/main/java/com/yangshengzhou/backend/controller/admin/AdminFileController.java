package com.yangshengzhou.backend.controller.admin;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.entity.File;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.service.AuthService;
import com.yangshengzhou.backend.service.FileService;
import com.yangshengzhou.backend.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/file")
public class AdminFileController {
    
    @Autowired
    private FileService fileService;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private OperationLogService operationLogService;
    
    /**
     * 获取所有文件列表
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Page>> getAllFiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            Page files = fileService.getAllFiles(page, size);
            
            return ResponseEntity.ok(ApiResponse.success(files));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取文件列表失败: " + e.getMessage()));
        }
    }
    
    /**
     * 根据ID获取文件
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<File>> getFileById(@PathVariable Long id) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            return fileService.getFile(id)
                .map(ResponseEntity::ok)
                .map(response -> ResponseEntity.ok(ApiResponse.success(response.getBody())))
                .orElse(ResponseEntity.badRequest().body(ApiResponse.error("文件不存在")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取文件信息失败: " + e.getMessage()));
        }
    }
    
    /**
     * 删除文件
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteFile(@PathVariable Long id, HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            // 获取文件信息用于日志
            File file = fileService.getFile(id).orElse(null);
            if (file == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件不存在"));
            }
            
            boolean deleted = fileService.deleteFile(id, currentUser.getId());
            
            if (deleted) {
                // 记录操作日志
                operationLogService.logOperation(
                    currentUser.getId(),
                    "DELETE_FILE",
                    "管理员删除文件: " + file.getFileName(),
                    getClientIpAddress(request)
                );
                
                return ResponseEntity.ok(ApiResponse.success("文件删除成功"));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件删除失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("删除文件失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取文件统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getFileStatistics() {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            Map<String, Object> statistics = fileService.getFileStatistics();
            
            return ResponseEntity.ok(ApiResponse.success(statistics));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取文件统计信息失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取用户文件列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page>> getUserFiles(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            Page files = fileService.getUserFiles(userId, page, size);
            
            return ResponseEntity.ok(ApiResponse.success(files));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取用户文件列表失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取存储使用统计
     */
    @GetMapping("/storage/usage")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStorageUsage() {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            Map<String, Object> usage = fileService.getStorageUsage();
            
            return ResponseEntity.ok(ApiResponse.success(usage));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取存储使用统计失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取文件类型统计
     */
    @GetMapping("/type/statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getFileTypeStatistics() {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            Map<String, Object> statistics = fileService.getFileTypeStatistics();
            
            return ResponseEntity.ok(ApiResponse.success(statistics));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取文件类型统计失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取客户端IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}