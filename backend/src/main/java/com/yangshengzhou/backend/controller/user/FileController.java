package com.yangshengzhou.backend.controller.user;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.entity.File;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.service.AuthService;
import com.yangshengzhou.backend.service.FileService;
import com.yangshengzhou.backend.service.FileStorageService;
import com.yangshengzhou.backend.service.OperationLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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
            
            // 上传文件到存储服务
            String fileName = fileStorageService.uploadFile(file, currentUser.getId(), folderId);
            
            // 保存文件信息到数据库
            File uploadedFile = fileService.uploadFile(file, currentUser.getId(), folderId, fileName);
            
            if (uploadedFile != null) {
                // 记录上传日志
                operationLogService.logOperation(
                    currentUser.getId(), 
                    "UPLOAD", 
                    "FILE", 
                    uploadedFile.getId().toString(), 
                    uploadedFile.getName(), 
                    "上传文件",
                    getClientIpAddress(request),
                    request.getHeader("User-Agent")
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
        System.out.println("到底读取文件列表");
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            List<File> files;
            if (folderId != null) {
                files = fileService.getUserFilesByFolderId(currentUser.getId(), folderId);
            } else {
                 files = fileService.getUserFiles(currentUser.getId());
              }
            
            return ResponseEntity.ok(ApiResponse.success(files));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取文件列表失败: " + e.getMessage()));
        }
    }
    
    /**
     * 分页获取用户文件列表
     */
    @GetMapping("/list/page")
    public ResponseEntity<ApiResponse<Page<File>>> getUserFilesPage(
            @RequestParam(value = "folderId", required = false) Long folderId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            System.out.println("=== 文件列表API调用开始 ===");
            System.out.println("请求参数 - folderId: " + folderId + ", page: " + page + ", size: " + size);
            
            User currentUser = authService.getCurrentUser();
            System.out.println("当前用户: " + (currentUser != null ? currentUser.getEmail() : "null"));
            
            if (currentUser == null) {
                System.out.println("错误: 用户未登录，返回401");
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            Page<File> files;
            if (folderId != null) {
                System.out.println("获取指定文件夹下的文件，folderId: " + folderId);
                files = fileService.getUserFilesByFolderId(currentUser.getId(), folderId, page, size);
            } else {
                System.out.println("获取用户所有文件");
                files = fileService.getUserFiles(currentUser.getId(), page, size);
            }
            
            System.out.println("获取文件成功，数量: " + (files != null ? files.getTotalElements() : 0));
            System.out.println("=== 文件列表API调用结束 ===");
            
            return ResponseEntity.ok(ApiResponse.success(files));
        } catch (Exception e) {
            System.err.println("文件列表API异常: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(ApiResponse.error("获取文件列表失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取文件详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<File>> getFile(@PathVariable Long id, HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            if (!fileService.hasPermission(currentUser.getId(), id)) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权访问此文件"));
            }
            
            File file = fileService.getFile(id).orElse(null);
            if (file == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件不存在"));
            }
            
            // 记录下载日志
            operationLogService.logOperation(
                currentUser.getId(), 
                "DOWNLOAD", 
                "FILE", 
                id.toString(), 
                file.getName(), 
                "下载文件",
                getClientIpAddress(request),
                request.getHeader("User-Agent")
            );
            
            return ResponseEntity.ok(ApiResponse.success(file));
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
            
            if (!fileService.hasPermission(currentUser.getId(), id)) {
                return ResponseEntity.status(403).build();
            }
            
            File file = fileService.getFile(id).orElse(null);
            if (file == null) {
                return ResponseEntity.notFound().build();
            }
            
            InputStream inputStream = fileStorageService.downloadFile(file.getStorageKey());
            
            // 记录下载日志
            operationLogService.logOperation(
                currentUser.getId(), 
                "DOWNLOAD", 
                "FILE", 
                file.getId().toString(), 
                file.getName(), 
                "下载文件",
                getClientIpAddress(request),
                request.getHeader("User-Agent")
            );
            
            // 设置响应头
            String encodedFileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.toString());
            
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
    public ResponseEntity<ApiResponse<Map<String, Object>>> getFilePreviewUrl(@PathVariable Long id) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            if (!fileService.hasPermission(currentUser.getId(), id)) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权访问此文件"));
            }
            
            File file = fileService.getFile(id).orElse(null);
            if (file == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件不存在"));
            }
            
            String previewUrl = fileStorageService.getFileUrl(file.getStorageKey());
            
            Map<String, Object> result = new HashMap<>();
            result.put("url", previewUrl);
            result.put("fileName", file.getName());
            result.put("fileSize", file.getSize());
            result.put("contentType", file.getMimeType());
            
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取预览URL失败: " + e.getMessage()));
        }
    }
    
    /**
     * 重命名文件
     */
    @PutMapping("/{id}/rename")
    public ResponseEntity<ApiResponse<File>> renameFile(@PathVariable Long id, @RequestBody Map<String, String> requestBody, HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            if (!fileService.hasPermission(currentUser.getId(), id)) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权修改此文件"));
            }
            
            String newName = requestBody.get("name");
            if (newName == null || newName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件名不能为空"));
            }
            
            File updatedFile = fileService.updateFile(id, newName);
            if (updatedFile != null) {
                // 记录重命名日志
                operationLogService.logOperation(
                    currentUser.getId(), 
                    "RENAME", 
                    "FILE", 
                    id.toString(), 
                    "重命名文件: " + newName, 
                    getClientIpAddress(request),
                    request.getHeader("User-Agent"),
                    ""
                );
                
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
            
            if (!fileService.hasPermission(currentUser.getId(), id)) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权删除此文件"));
            }
            
            File file = fileService.getFile(id).orElse(null);
            if (file == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件不存在"));
            }
            
            // 删除存储中的文件
            fileStorageService.deleteFile(file.getStorageKey());
            
            // 删除数据库记录
            boolean deleted = fileService.deleteFile(id, currentUser.getId());
            
            if (deleted) {
                // 记录删除日志
                operationLogService.logOperation(
                    currentUser.getId(), 
                    "DELETE", 
                    "FILE", 
                    id.toString(), 
                    "删除文件: " + file.getName(), 
                    getClientIpAddress(request),
                    request.getHeader("User-Agent"),
                    ""
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
     * 按扩展名获取文件
     */
    @GetMapping("/extension/{extension}")
    public ResponseEntity<ApiResponse<List<File>>> getFilesByExtension(@PathVariable String extension) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            List<File> files = fileService.getFilesByExtension(currentUser.getId(), extension);
            return ResponseEntity.ok(ApiResponse.success(files));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取文件失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取用户存储使用情况
     */
    @GetMapping("/storage/usage")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStorageUsage() {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            Long totalSize = fileService.getUserTotalFileSize(currentUser.getId());
            Long quota = currentUser.getStorageQuota();
            
            Map<String, Object> result = new HashMap<>();
            result.put("totalSize", totalSize);
            result.put("quota", quota);
            result.put("usagePercentage", quota > 0 ? (totalSize * 100.0 / quota) : 0);
            
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取存储使用情况失败: " + e.getMessage()));
        }
    }
    
    /**
     * 批量删除文件
     */
    @DeleteMapping("/batch")
    public ResponseEntity<ApiResponse<String>> batchDeleteFiles(@RequestBody List<Long> fileIds, HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            int successCount = 0;
            int failCount = 0;
            
            for (Long fileId : fileIds) {
                try {
                    if (fileService.hasPermission(currentUser.getId(), fileId)) {
                        File file = fileService.getFile(fileId).orElse(null);
                        if (file != null) {
                            // 删除存储中的文件
                            fileStorageService.deleteFile(file.getStorageKey());
                            
                            // 删除数据库记录
                            if (fileService.deleteFile(fileId, currentUser.getId())) {
                                successCount++;
                                
                                // 记录删除日志
                                operationLogService.logOperation(
                                    currentUser.getId(), 
                                    "DELETE", 
                                    "FILE", 
                                    fileId.toString(), 
                                    "批量删除文件: " + file.getName(), 
                                    getClientIpAddress(request),
                                    "",
                                    ""
                                );
                            } else {
                                failCount++;
                            }
                        } else {
                            failCount++;
                        }
                    } else {
                        failCount++;
                    }
                } catch (Exception e) {
                    failCount++;
                }
            }
            
            return ResponseEntity.ok(ApiResponse.success("批量删除完成，成功: " + successCount + "，失败: " + failCount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("批量删除文件失败: " + e.getMessage()));
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