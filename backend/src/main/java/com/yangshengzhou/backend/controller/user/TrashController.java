package com.yangshengzhou.backend.controller.user;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.entity.File;
import com.yangshengzhou.backend.entity.Folder;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.service.AuthService;
import com.yangshengzhou.backend.service.FileService;
import com.yangshengzhou.backend.service.FolderService;
import com.yangshengzhou.backend.service.OperationLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/trash")
public class TrashController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private FileService fileService;
    
    @Autowired
    private FolderService folderService;
    
    @Autowired
    private OperationLogService operationLogService;
    
    /**
     * 获取回收站文件列表
     */
    @GetMapping("/files")
    public ResponseEntity<ApiResponse<List<File>>> getTrashFiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedTime"));
            Page<File> files = fileService.getTrashFiles(currentUser.getId(), pageable);
            
            return ResponseEntity.ok(ApiResponse.success("获取回收站文件成功", files.getContent()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取回收站文件失败: " + e.getMessage()));
        }
    }
    
    /**
     * 恢复文件
     */
    @PostMapping("/files/{id}/restore")
    public ResponseEntity<ApiResponse<String>> restoreFile(@PathVariable Long id, HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            boolean restored = fileService.restoreFile(id, currentUser.getId());
            
            if (restored) {
                // 记录恢复日志
                operationLogService.logOperation(
                    currentUser.getId(), 
                    "RESTORE_FILE", 
                    "文件", 
                    id.toString(),
                    "恢复文件",
                    getClientIpAddress(request), 
                    request.getHeader("User-Agent")
                );
                
                return ResponseEntity.ok(ApiResponse.success("文件恢复成功"));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件恢复失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("恢复文件失败: " + e.getMessage()));
        }
    }
    
    /**
     * 恢复文件夹
     */
    @PostMapping("/folders/{id}/restore")
    public ResponseEntity<ApiResponse<String>> restoreFolder(@PathVariable Long id, HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            boolean restored = folderService.restoreFolderRecursively(id, currentUser.getId());
            
            if (restored) {
                // 记录恢复日志
                operationLogService.logOperation(
                    currentUser.getId(), 
                    "RESTORE_FOLDER", 
                    "文件夹", 
                    id.toString(),
                    "恢复文件夹",
                    getClientIpAddress(request), 
                    request.getHeader("User-Agent")
                );
                
                return ResponseEntity.ok(ApiResponse.success("文件夹恢复成功"));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件夹恢复失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("恢复文件夹失败: " + e.getMessage()));
        }
    }
    
    /**
     * 永久删除文件
     */
    @DeleteMapping("/files/{id}")
    public ResponseEntity<ApiResponse<String>> permanentDeleteFile(@PathVariable Long id, HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            File file = fileService.getFile(id).orElse(null);
            if (file == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件不存在"));
            }
            
            boolean deleted = fileService.permanentDeleteFile(id, currentUser.getId());
            
            if (deleted) {
                // 记录永久删除日志
                operationLogService.logOperation(
                    currentUser.getId(), 
                    "PERMANENT_DELETE_FILE", 
                    "文件", 
                    id.toString(),
                    "永久删除文件: " + file.getName(),
                    getClientIpAddress(request), 
                    request.getHeader("User-Agent")
                );
                
                return ResponseEntity.ok(ApiResponse.success("文件永久删除成功"));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件永久删除失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("永久删除文件失败: " + e.getMessage()));
        }
    }
    
    /**
     * 永久删除文件夹
     */
    @DeleteMapping("/folders/{id}")
    public ResponseEntity<ApiResponse<String>> permanentDeleteFolder(@PathVariable Long id, HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            Folder folder = folderService.getFolder(id).orElse(null);
            if (folder == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件夹不存在"));
            }
            
            boolean deleted = folderService.permanentDeleteFolderRecursively(id, currentUser.getId());
            
            if (deleted) {
                // 记录永久删除日志
                operationLogService.logOperation(
                    currentUser.getId(), 
                    "PERMANENT_DELETE_FOLDER", 
                    "文件夹", 
                    id.toString(),
                    "永久删除文件夹: " + folder.getName(),
                    getClientIpAddress(request), 
                    request.getHeader("User-Agent")
                );
                
                return ResponseEntity.ok(ApiResponse.success("文件夹永久删除成功"));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件夹永久删除失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("永久删除文件夹失败: " + e.getMessage()));
        }
    }
    
    /**
     * 清空回收站
     */
    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<String>> clearTrash(HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            // 获取用户的回收站文件
            List<File> trashFiles = fileService.getTrashFiles(currentUser.getId());
            List<Folder> trashFolders = folderService.getTrashFolders(currentUser.getId());
            
            int fileCount = 0;
            int folderCount = 0;
            
            // 永久删除所有回收站文件
            for (File file : trashFiles) {
                if (fileService.permanentDeleteFile(file.getId(), currentUser.getId())) {
                    fileCount++;
                }
            }
            
            // 永久删除所有回收站文件夹
            for (Folder folder : trashFolders) {
                if (folderService.permanentDeleteFolder(folder.getId(), currentUser.getId())) {
                    folderCount++;
                }
            }
            
            // 记录清空回收站日志
            operationLogService.logOperation(
                currentUser.getId(), 
                "CLEAR_TRASH", 
                "回收站", 
                null,
                "清空回收站，删除文件: " + fileCount + "个，文件夹: " + folderCount + "个",
                getClientIpAddress(request), 
                request.getHeader("User-Agent")
            );
            
            return ResponseEntity.ok(ApiResponse.success("回收站清空成功，删除文件: " + fileCount + "个，文件夹: " + folderCount + "个"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("清空回收站失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取客户端IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}