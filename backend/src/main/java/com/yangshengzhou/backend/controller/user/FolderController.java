package com.yangshengzhou.backend.controller.user;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.dto.CreateFolderRequest;
import com.yangshengzhou.backend.entity.Folder;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.service.AuthService;
import com.yangshengzhou.backend.service.FolderService;
import com.yangshengzhou.backend.service.OperationLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/folder")
public class FolderController {
    
    @Autowired
    private FolderService folderService;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private OperationLogService operationLogService;
    
    /**
     * 创建文件夹
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Folder>> createFolder(@RequestBody Map<String, Object> requestBody, 
                                                           HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            String name = (String) requestBody.get("name");
            Long parentId = requestBody.get("parentId") != null ? 
                Long.valueOf(requestBody.get("parentId").toString()) : 1L; // 默认根目录
            
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件夹名不能为空"));
            }
            
            // 创建文件夹请求对象
            CreateFolderRequest createRequest = new CreateFolderRequest();
            createRequest.setName(name);
            createRequest.setParentId(parentId);
            
            Folder folder = folderService.createFolder(currentUser.getId(), createRequest);
            
            // 记录创建文件夹日志
            operationLogService.logOperation(
                currentUser.getId(), 
                "CREATE_FOLDER", 
                "文件夹", 
                folder.getId().toString(),
                "创建文件夹",
                getClientIpAddress(request), 
                request.getHeader("User-Agent")
            );
            
            return ResponseEntity.ok(ApiResponse.success("文件夹创建成功", folder));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("创建文件夹失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取用户文件夹列表
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<Folder>>> getUserFolders() {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            List<Folder> folders = folderService.getUserFolders(currentUser.getId());
            return ResponseEntity.ok(ApiResponse.success(folders));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取文件夹列表失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取用户根目录
     */
    @GetMapping("/root")
    public ResponseEntity<ApiResponse<Folder>> getUserRootFolder() {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            return folderService.getUserRootFolder(currentUser.getId())
                .map(folder -> ResponseEntity.ok(ApiResponse.success(folder)))
                .orElse(ResponseEntity.badRequest().body(ApiResponse.error("根目录不存在")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取根目录失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取子文件夹列表
     */
    @GetMapping("/{parentId}/subfolders")
    public ResponseEntity<ApiResponse<List<Folder>>> getSubFolders(@PathVariable Long parentId) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            List<Folder> folders = folderService.getSubFolders(parentId, currentUser.getId());
            return ResponseEntity.ok(ApiResponse.success(folders));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取子文件夹列表失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取文件夹详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Folder>> getFolder(@PathVariable Long id) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            if (!folderService.hasPermission(currentUser.getId(), id)) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权访问此文件夹"));
            }
            
            return folderService.getFolder(id)
                .map(folder -> ResponseEntity.ok(ApiResponse.success(folder)))
                .orElse(ResponseEntity.badRequest().body(ApiResponse.error("文件夹不存在")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取文件夹信息失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取文件夹路径
     */
    @GetMapping("/{id}/path")
    public ResponseEntity<ApiResponse<List<Folder>>> getFolderPath(@PathVariable Long id) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            if (!folderService.hasPermission(currentUser.getId(), id)) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权访问此文件夹"));
            }
            
            List<Folder> path = folderService.getFolderPath(id);
            return ResponseEntity.ok(ApiResponse.success(path));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取文件夹路径失败: " + e.getMessage()));
        }
    }
    
    /**
     * 重命名文件夹
     */
    @PutMapping("/{id}/rename")
    public ResponseEntity<ApiResponse<Folder>> renameFolder(@PathVariable Long id, 
                                                           @RequestBody Map<String, String> requestBody) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            if (!folderService.isFolderOwnedByUser(id, currentUser.getId())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权修改此文件夹"));
            }
            
            String newName = requestBody.get("name");
            if (newName == null || newName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件夹名不能为空"));
            }
            
            Folder updatedFolder = folderService.updateFolder(id, newName);
            if (updatedFolder != null) {
                return ResponseEntity.ok(ApiResponse.success("重命名成功", updatedFolder));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("重命名失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("重命名文件夹失败: " + e.getMessage()));
        }
    }
    
    /**
     * 删除文件夹
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteFolder(@PathVariable Long id, HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            if (!folderService.isFolderOwnedByUser(id, currentUser.getId())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权删除此文件夹"));
            }
            
            Folder folder = folderService.getFolder(id).orElse(null);
            if (folder == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件夹不存在"));
            }
            
            boolean deleted = folderService.deleteFolderRecursively(id, currentUser.getId());
            
            if (deleted) {
                // 记录删除日志
                operationLogService.logOperation(
                    currentUser.getId(), 
                    "DELETE_FOLDER", 
                    "文件夹", 
                    folder.getId().toString(),
                    "删除文件夹",
                    getClientIpAddress(request), 
                    request.getHeader("User-Agent")
                );
                
                return ResponseEntity.ok(ApiResponse.success("文件夹删除成功"));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件夹删除失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("删除文件夹失败: " + e.getMessage()));
        }
    }
    
    /**
     * 移动文件夹到指定父文件夹
     */
    @PutMapping("/{id}/move")
    public ResponseEntity<ApiResponse<Folder>> moveFolder(
            @PathVariable Long id,
            @RequestBody Map<String, Long> requestBody,
            HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            if (!folderService.hasPermission(currentUser.getId(), id)) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权移动此文件夹"));
            }
            
            Long targetParentId = requestBody.get("parentId");
            if (targetParentId == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("目标父文件夹ID不能为空"));
            }
            
            Folder movedFolder = folderService.moveFolder(id, targetParentId, currentUser.getId());
            if (movedFolder != null) {
                // 记录移动日志
                operationLogService.logOperation(
                    currentUser.getId(), 
                    "MOVE_FOLDER", 
                    "文件夹", 
                    id.toString(),
                    "移动文件夹到: " + targetParentId,
                    getClientIpAddress(request), 
                    request.getHeader("User-Agent")
                );
                
                return ResponseEntity.ok(ApiResponse.success("文件夹移动成功", movedFolder));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件夹移动失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("移动文件夹失败: " + e.getMessage()));
        }
    }
    
    /**
     * 复制文件夹到指定父文件夹
     */
    @PostMapping("/{id}/copy")
    public ResponseEntity<ApiResponse<Folder>> copyFolder(
            @PathVariable Long id,
            @RequestBody Map<String, Long> requestBody,
            HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            if (!folderService.hasPermission(currentUser.getId(), id)) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权复制此文件夹"));
            }
            
            Long targetParentId = requestBody.get("parentId");
            if (targetParentId == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("目标父文件夹ID不能为空"));
            }
            
            Folder copiedFolder = folderService.copyFolder(id, targetParentId, currentUser.getId());
            if (copiedFolder != null) {
                // 记录复制日志
                operationLogService.logOperation(
                    currentUser.getId(), 
                    "COPY_FOLDER", 
                    "文件夹", 
                    id.toString(),
                    "复制文件夹到: " + targetParentId,
                    getClientIpAddress(request), 
                    request.getHeader("User-Agent")
                );
                
                return ResponseEntity.ok(ApiResponse.success("文件夹复制成功", copiedFolder));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件夹复制失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("复制文件夹失败: " + e.getMessage()));
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