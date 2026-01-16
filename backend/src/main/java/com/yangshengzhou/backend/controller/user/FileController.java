package com.yangshengzhou.backend.controller.user;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.entity.File;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.event.StorageUpdateEvent;
import com.yangshengzhou.backend.service.AuthService;
import com.yangshengzhou.backend.service.FileService;
import com.yangshengzhou.backend.service.FileStorageService;
import com.yangshengzhou.backend.service.OperationLogService;
import com.yangshengzhou.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;

import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;

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
    private UserService userService;
    
    @Autowired
    private OperationLogService operationLogService;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
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
            
            // 检查存储容量是否足够
            Long fileSize = file.getSize();
            if (!userService.hasSufficientStorage(currentUser.getId(), fileSize)) {
                Long availableStorage = userService.getAvailableStorage(currentUser.getId());
                return ResponseEntity.badRequest().body(ApiResponse.error(
                    "存储空间不足。可用空间: " + formatFileSize(availableStorage) + 
                    ", 文件大小: " + formatFileSize(fileSize)
                ));
            }
            
            // 上传文件到存储服务并保存文件信息到数据库
            File uploadedFile = fileStorageService.uploadFileAndSaveInfo(file, currentUser.getId(), folderId);
            
            if (uploadedFile != null) {
                // 记录上传日志
                operationLogService.logOperation(
                    currentUser.getId(), 
                    "UPLOAD", 
                    "FILE", 
                    uploadedFile.getId().toString(),
                    "用户" + currentUser.getEmail() + "上传文件: " + uploadedFile.getName(),
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
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserFilesPage(
            @RequestParam(value = "folderId", required = false) Long folderId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            User currentUser = authService.getCurrentUser();
            
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            Page<File> files;
            if (folderId != null) {
                files = fileService.getUserFilesByFolderId(currentUser.getId(), folderId, page, size);
            } else {
                files = fileService.getUserFiles(currentUser.getId(), page, size);
            }
            
            // 创建分页响应对象，避免直接返回PageImpl
            Map<String, Object> response = new HashMap<>();
            response.put("content", files.getContent());
            response.put("pageNumber", files.getNumber());
            response.put("pageSize", files.getSize());
            response.put("totalElements", files.getTotalElements());
            response.put("totalPages", files.getTotalPages());
            response.put("first", files.isFirst());
            response.put("last", files.isLast());
            
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取文件列表失败: " + e.getMessage()));
        }
    }
    
    /**
     * 搜索文件
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<File>>> searchFiles(
            @RequestParam String keyword,
            @RequestParam(required = false) Long folderId) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            List<File> files;
            if (folderId != null) {
                files = fileService.searchFilesInFolder(currentUser.getId(), folderId, keyword);
            } else {
                files = fileService.searchFiles(currentUser.getId(), keyword);
            }
            
            return ResponseEntity.ok(ApiResponse.success(files));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("搜索文件失败: " + e.getMessage()));
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
            
            if (!fileService.hasPermission(currentUser.getId(), id)) {
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
     * 下载文件 - 修复重复写入流的问题
     */
    @GetMapping("/{id}/download")
    public void downloadFile(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        File file = null;
        User currentUser = null;
        InputStream inputStream = null;
        
        try {
            currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                response.setStatus(401);
                return;
            }
            
            if (!fileService.hasPermission(currentUser.getId(), id)) {
                response.setStatus(403);
                return;
            }
            
            file = fileService.getFile(id).orElse(null);
            if (file == null) {
                response.setStatus(404);
                return;
            }
            
            // 获取文件大小信息
            long fileSize = fileStorageService.getFileSize(file.getStorageKey());
            
            // 获取文件输入流
            inputStream = fileStorageService.downloadFile(file.getStorageKey());
            
            // 设置响应头 - 完全模仿MinIO的正常响应
            String encodedFileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.toString());
            
            // 根据文件类型设置正确的Content-Type
            String contentType = file.getMimeType();
            if (contentType == null || contentType.isEmpty()) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }
            
            response.setContentType(contentType);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"");
            response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0, must-revalidate");
            response.setHeader(HttpHeaders.PRAGMA, "no-cache");
            response.setHeader(HttpHeaders.EXPIRES, "0");
            
            // 关键：设置Content-Length，禁用分块传输
            response.setContentLengthLong(fileSize);
            
            // 禁用Tomcat缓冲，直接传输原始字节
            response.setBufferSize(0);
            
            // 关键修复：确保流只被写入一次
            // 使用try-with-resources确保资源正确关闭
            try (OutputStream outputStream = response.getOutputStream()) {
                long startTime = System.currentTimeMillis();
                
                // 只调用一次IOUtils.copy，确保数据只传输一次
                IOUtils.copy(inputStream, outputStream);
                
                long endTime = System.currentTimeMillis();
                // 不要调用flush()，因为IOUtils.copy会自动处理
            }
            
            // 异步记录下载日志（避免阻塞文件流）
            final User finalUser = currentUser;
            final File finalFile = file;
            new Thread(() -> {
                try {
                    operationLogService.logOperation(
                        finalUser.getId(), 
                        "DOWNLOAD", 
                        "FILE", 
                        finalFile.getId().toString(),
                        "用户" + finalUser.getEmail() + "下载文件: " + finalFile.getName(),
                        getClientIpAddress(request),
                        request.getHeader("User-Agent")
                    );
                } catch (Exception e) {
                    // 日志记录失败不影响文件下载
                }
            }).start();
            
        } catch (Exception e) {
            response.setStatus(400);
        } finally {
            // 确保输入流被正确关闭
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    // 忽略关闭异常
                }
            }
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
                    request.getHeader("User-Agent")
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
                // 发布存储减少事件（异步处理）
                eventPublisher.publishEvent(new StorageUpdateEvent(currentUser.getId(), file.getSize(), false));
                
                // 记录删除日志
                operationLogService.logOperation(
                    currentUser.getId(), 
                    "DELETE", 
                    "FILE", 
                    id.toString(),
                    "用户" + currentUser.getEmail() + "删除文件: " + file.getName(),
                    getClientIpAddress(request),
                    request.getHeader("User-Agent")
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
                                
                                // 发布存储减少事件（异步处理）
                                eventPublisher.publishEvent(new StorageUpdateEvent(currentUser.getId(), file.getSize(), false));
                                
                                // 记录删除日志
                                operationLogService.logOperation(
                                    currentUser.getId(), 
                                    "DELETE", 
                                    "FILE", 
                                    fileId.toString(),
                                    "用户" + currentUser.getEmail() + "批量删除文件: " + file.getName(),
                                    getClientIpAddress(request),
                                    request.getHeader("User-Agent")
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
     * 移动文件到指定文件夹
     */
    @PutMapping("/{id}/move")
    public ResponseEntity<ApiResponse<File>> moveFile(
            @PathVariable Long id,
            @RequestBody Map<String, Long> requestBody,
            HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            if (!fileService.hasPermission(currentUser.getId(), id)) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权移动此文件"));
            }
            
            Long parentId = requestBody.get("parentId");
            if (parentId == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("目标文件夹ID不能为空"));
            }
            
            File movedFile = fileService.moveFile(id, parentId, currentUser.getId());
            if (movedFile != null) {
                // 记录移动日志
                operationLogService.logOperation(
                    currentUser.getId(), 
                    "MOVE", 
                    "FILE", 
                    id.toString(),
                    "移动文件到文件夹: " + parentId,
                    getClientIpAddress(request),
                    request.getHeader("User-Agent")
                );
                
                return ResponseEntity.ok(ApiResponse.success("文件移动成功", movedFile));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件移动失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("移动文件失败: " + e.getMessage()));
        }
    }
    
    /**
     * 复制文件到指定文件夹
     */
    @PostMapping("/{id}/copy")
    public ResponseEntity<ApiResponse<File>> copyFile(
            @PathVariable Long id,
            @RequestBody Map<String, Long> requestBody,
            HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            if (!fileService.hasPermission(currentUser.getId(), id)) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权复制此文件"));
            }
            
            Long parentId = requestBody.get("parentId");
            if (parentId == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("目标文件夹ID不能为空"));
            }
            
            File copiedFile = fileService.copyFile(id, parentId, currentUser.getId());
            if (copiedFile != null) {
                // 记录复制日志
                operationLogService.logOperation(
                    currentUser.getId(), 
                    "COPY", 
                    "FILE", 
                    id.toString(),
                    "复制文件到文件夹: " + parentId,
                    getClientIpAddress(request),
                    request.getHeader("User-Agent")
                );
                
                return ResponseEntity.ok(ApiResponse.success("文件复制成功", copiedFile));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件复制失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("复制文件失败: " + e.getMessage()));
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
    
    /**
     * 格式化文件大小显示
     */
    private String formatFileSize(Long bytes) {
        if (bytes == null || bytes == 0) return "0 B";
        
        final String[] units = {"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
        
        if (digitGroups >= units.length) {
            digitGroups = units.length - 1;
        }
        
        return String.format("%.2f %s", bytes / Math.pow(1024, digitGroups), units[digitGroups]);
    }
}