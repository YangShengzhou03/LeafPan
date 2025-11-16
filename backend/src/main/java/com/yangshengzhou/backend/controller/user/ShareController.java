package com.yangshengzhou.backend.controller.user;

import com.yangshengzhou.backend.dto.CreateShareRequest;
import com.yangshengzhou.backend.dto.UpdateShareRequest;
import com.yangshengzhou.backend.entity.Share;
import com.yangshengzhou.backend.response.ApiResponse;
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
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
        }
        
        /**
     * 获取用户的分享列表
     */
    @GetMapping("/user")
    public ResponseEntity<ApiResponse<List<Share>>> getUserShares() {
        try {
            List<Share> shares = shareService.getUserShares();
            return ResponseEntity.ok(ApiResponse.success(shares));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取分享列表失败: " + e.getMessage()));
        }
    }
    
    /**
     * 根据ID获取分享详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Share>> getShareById(@PathVariable Long id) {
        try {
            Optional<Share> share = shareService.getShareById(id);
            if (share.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(share.get()));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("分享不存在"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取分享详情失败: " + e.getMessage()));
        }
    }
    
    /**
     * 更新分享
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Share>> updateShare(
            @PathVariable Long id, 
            @RequestBody CreateShareRequest request, 
            HttpServletRequest httpRequest) {
        try {
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
    
    /**
     * 根据分享码访问分享
     */
    @GetMapping("/access/{shareCode}")
    public ResponseEntity<ApiResponse<Share>> accessShare(@PathVariable String shareCode) {
        try {
            Optional<Share> share = shareService.getShareByCode(shareCode);
            if (share.isPresent()) {
                // 增加查看次数
                shareService.incrementViewCount(shareCode);
                return ResponseEntity.ok(ApiResponse.success(share.get()));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("分享不存在或已失效"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("访问分享失败: " + e.getMessage()));
        }
    }
    
    /**
     * 验证分享密码
     */
    @PostMapping("/verify/{shareCode}")
    public ResponseEntity<ApiResponse<String>> verifySharePassword(
            @PathVariable String shareCode, 
            @RequestBody Map<String, String> request) {
        try {
            String password = request.get("password");
            Optional<Share> shareOpt = shareService.getShareByCode(shareCode);
            
            if (shareOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("分享不存在或已失效"));
            }
            
            Share share = shareOpt.get();
            
            // 检查分享类型
            if (share.getShareType() == 1) { // 密码分享
                if (password == null || !password.equals(share.getPassword())) {
                    return ResponseEntity.badRequest().body(ApiResponse.error("密码错误"));
                }
            }
            
            return ResponseEntity.ok(ApiResponse.success("验证成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("验证分享密码失败: " + e.getMessage()));
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