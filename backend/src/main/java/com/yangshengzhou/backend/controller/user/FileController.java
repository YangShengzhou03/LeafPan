package com.yangshengzhou.backend.controller.user;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.entity.File;
import com.yangshengzhou.backend.service.AuthService;
import com.yangshengzhou.backend.service.FileService;
import com.yangshengzhou.backend.service.FileStorageService;
import com.yangshengzhou.backend.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController {
    
    @Autowired
    private FileService fileService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private OperationLogService operationLogService;
    
    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<File>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folderId", required = false) Long folderId,
            HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            // 如果没有指定文件夹，默认使用根目录
            if (folderId == null) {
                folderId = 1L; // 根目录ID
            }
            
            String fileName = fileStorageService.uploadFile(file, currentUser.getId(), folderId);
            
            // 获取保存的文件信息
            List<File> userFiles = fileService.getFilesByFolder(currentUser.getId(), folderId);
            File uploadedFile = userFiles.stream()
                .filter(f -> f.getName().equals(fileName))
                .findFirst()
                .orElse(null);
            
            if (uploadedFile != null) {
                // 记录上传日志
                operationLogService.logOperation(
                    currentUser.getId(), 
                    "UPLOAD", 
                    "上传文件: " + uploadedFile.getOriginalName(), 
                    getClientIpAddress(request)
                );
                
                return ResponseEntity.ok(ApiResponse.success("文件上传成功", uploadedFile));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件上传后保存信息失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("文件上传失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取用户文件列表
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<File>>> getUserFiles(
            @RequestParam(value = "folderId", required = false) Long folderId) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            List<File> files;
            if (folderId != null) {
                files = fileService.getFilesByFolder(currentUser.getId(), folderId);
            } else {
                files = fileService.getUserFiles(currentUser.getId());
            }
            
            return ResponseEntity.ok(ApiResponse.success(files));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取文件列表失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取文件详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<File>> getFile(@PathVariable Long id) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            if (!fileService.isFileOwnedByUser(id, currentUser.getId())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权访问此文件"));
            }
            
            return fileService.getFile(id)
                .map(file -> ResponseEntity.ok(ApiResponse.success(file)))
                .orElse(ResponseEntity.badRequest().body(ApiResponse.error("文件不存在")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取文件信息失败: " + e.getMessage()));
        }
    }
    
    /**
     * 下载文件
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable Long id, HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).build();
            }
            
            if (!fileService.isFileOwnedByUser(id, currentUser.getId())) {
                return ResponseEntity.status(403).build();
            }
            
            File file = fileService.getFile(id).orElse(null);
            if (file == null) {
                return ResponseEntity.notFound().build();
            }
            
            InputStream inputStream = fileStorageService.downloadFile(file.getName());
            
            // 记录下载日志
            operationLogService.logOperation(
                currentUser.getId(), 
                "DOWNLOAD", 
                "下载文件: " + file.getOriginalName(), 
                getClientIpAddress(request)
            );
            
            // 设置响应头
            String encodedFileName = URLEncoder.encode(file.getOriginalName(), StandardCharsets.UTF_8.toString());
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.getSize())
                .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 获取文件预览URL
     */
    @GetMapping("/{id}/preview")
    public ResponseEntity<ApiResponse<Map<String, String>>> getFilePreviewUrl(@PathVariable Long id) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            if (!fileService.isFileOwnedByUser(id, currentUser.getId())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权访问此文件"));
            }
            
            File file = fileService.getFile(id).orElse(null);
            if (file == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件不存在"));
            }
            
            String previewUrl = fileStorageService.getFileUrl(file.getName());
            
            return ResponseEntity.ok(ApiResponse.success(Map.of("url", previewUrl)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取预览URL失败: " + e.getMessage()));
        }
    }
    
    /**
     * 重命名文件
     */
    @PutMapping("/{id}/rename")
    public ResponseEntity<ApiResponse<File>> renameFile(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            if (!fileService.isFileOwnedByUser(id, currentUser.getId())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权修改此文件"));
            }
            
            String newName = requestBody.get("name");
            if (newName == null || newName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件名不能为空"));
            }
            
            File updatedFile = fileService.updateFile(id, newName);
            if (updatedFile != null) {
                return ResponseEntity.ok(ApiResponse.success("重命名成功", updatedFile));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("重命名失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("重命名文件失败: " + e.getMessage()));
        }
    }
    
    /**
     * 删除文件
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteFile(@PathVariable Long id, HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            if (!fileService.isFileOwnedByUser(id, currentUser.getId())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权删除此文件"));
            }
            
            File file = fileService.getFile(id).orElse(null);
            if (file == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件不存在"));
            }
            
            // 删除存储中的文件
            fileStorageService.deleteFile(file.getName());
            
            // 删除数据库记录
            boolean deleted = fileService.deleteFile(id);
            
            if (deleted) {
                // 记录删除日志
                operationLogService.logOperation(
                    currentUser.getId(), 
                    "DELETE", 
                    "删除文件: " + file.getOriginalName(), 
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
     * 搜索文件
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<File>>> searchFiles(@RequestParam String name) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            List<File> files = fileService.searchFilesByName(currentUser.getId(), name);
            return ResponseEntity.ok(ApiResponse.success(files));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("搜索文件失败: " + e.getMessage()));
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