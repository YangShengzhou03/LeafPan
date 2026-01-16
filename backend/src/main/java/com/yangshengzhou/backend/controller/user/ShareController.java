package com.yangshengzhou.backend.controller.user;

import com.yangshengzhou.backend.dto.CreateShareRequest;
import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.entity.Share;
import com.yangshengzhou.backend.vo.ShareVO;
import com.yangshengzhou.backend.service.ShareService;
import com.yangshengzhou.backend.service.OperationLogService;
import com.yangshengzhou.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/share")
public class ShareController {
    
    @Autowired
    private ShareService shareService;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private OperationLogService operationLogService;
    
    /**
     * 创建分享
     * 适配前端传递的参数格式
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Share>> createShare(@RequestBody Map<String, Object> frontendRequest, HttpServletRequest httpRequest) {
        try {
            // 将前端请求转换为后端所需的CreateShareRequest格式
            CreateShareRequest request = convertFrontendRequest(frontendRequest);
            
            Share share = shareService.createShare(request);
            
            // 记录操作日志
            operationLogService.logOperation(
                authService.getCurrentUser().getId(),
                "CREATE_SHARE",
                "SHARE",
                share.getId().toString(),
                "创建分享",
                getClientIpAddress(httpRequest),
                httpRequest.getHeader("User-Agent")
            );
            
            return ResponseEntity.ok(ApiResponse.success(share));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("创建分享失败: " + e.getMessage()));
        }
    }
    
    /**
     * 更新分享
     * 适配前端传递的参数格式
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Share>> updateShare(
            @PathVariable Long id, 
            @RequestBody Map<String, Object> frontendRequest, 
            HttpServletRequest httpRequest) {
        try {
            // 将前端请求转换为后端所需的CreateShareRequest格式
            CreateShareRequest request = convertFrontendRequest(frontendRequest);
            
            Share share = shareService.updateShare(id, request);
            
            // 记录操作日志
            operationLogService.logOperation(
                authService.getCurrentUser().getId(),
                "UPDATE_SHARE",
                "SHARE",
                share.getId().toString(),
                "更新分享",
                getClientIpAddress(httpRequest),
                httpRequest.getHeader("User-Agent")
            );
            
            return ResponseEntity.ok(ApiResponse.success(share));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("更新分享失败: " + e.getMessage()));
        }
    }
    
    /**
     * 将前端请求转换为CreateShareRequest对象
     */
    @SuppressWarnings("unchecked")
    private CreateShareRequest convertFrontendRequest(Map<String, Object> frontendRequest) {
        CreateShareRequest request = new CreateShareRequest();
        
        // 设置目标ID和类型（目前只支持文件分享）
        if (frontendRequest.containsKey("fileId")) {
            request.setTargetId(Long.valueOf(frontendRequest.get("fileId").toString()));
            request.setTargetType("file");
        }
        
        // 转换分享类型
        Integer shareType = 0; // 默认公开
        String frontendShareType = (String) frontendRequest.get("shareType");
        Boolean hasPassword = (Boolean) frontendRequest.get("hasPassword");
        
        if ("link".equals(frontendShareType)) {
            if (hasPassword != null && hasPassword) {
                shareType = 1; // 密码保护
                request.setPassword((String) frontendRequest.get("password"));
            } else {
                shareType = 0; // 公开
            }
        } else if ("user".equals(frontendShareType)) {
            shareType = 2; // 私密（用户分享）
            // 处理分享给的用户列表
            if (frontendRequest.containsKey("sharedTo")) {
                Object sharedToObj = frontendRequest.get("sharedTo");
                if (sharedToObj instanceof List) {
                    List<String> sharedToList = (List<String>) sharedToObj;
                    if (!sharedToList.isEmpty()) {
                        // 目前只支持分享给单个用户，取第一个
                        request.setSharedTo(sharedToList.get(0));
                    }
                }
            }
        }
        
        request.setShareType(shareType);
        
        // 设置过期时间
        if (frontendRequest.containsKey("expiresAt")) {
            Object expiresAt = frontendRequest.get("expiresAt");
            if (expiresAt != null) {
                request.setExpireTime(expiresAt.toString());
            }
        }
        
        return request;
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
     * 获取用户的分享列表
     */
    @GetMapping("/user")
    public ResponseEntity<ApiResponse<List<ShareVO>>> getUserShares() {
        try {
            List<ShareVO> shares = shareService.getUserShares();
            return ResponseEntity.ok(ApiResponse.success(shares));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取分享列表失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取与我共享的文件列表
     */
    @GetMapping("/shared-with-me")
    public ResponseEntity<ApiResponse<List<ShareVO>>> getSharedWithMe() {
        try {
            List<ShareVO> shares = shareService.getSharedWithMe();
            return ResponseEntity.ok(ApiResponse.success(shares));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取与我共享的文件失败: " + e.getMessage()));
        }
    }
    

    
    /**
     * 删除分享
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteShare(@PathVariable Long id, HttpServletRequest httpRequest) {
        try {
            Optional<Share> shareOpt = shareService.getShareById(id);
            String shareCode = "";
            if (shareOpt.isPresent()) {
                shareCode = shareOpt.get().getShareCode();
            }
            
            boolean deleted = shareService.deleteShare(id);
            
            if (deleted) {
                // 记录操作日志
                operationLogService.logOperation(
                    authService.getCurrentUser().getId(),
                    "DELETE_SHARE",
                    "SHARE",
                    id.toString(),
                    "删除分享",
                    getClientIpAddress(httpRequest),
                    httpRequest.getHeader("User-Agent")
                );
                
                return ResponseEntity.ok(ApiResponse.success("分享删除成功"));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("分享删除失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("删除分享失败: " + e.getMessage()));
        }
    }
    

}