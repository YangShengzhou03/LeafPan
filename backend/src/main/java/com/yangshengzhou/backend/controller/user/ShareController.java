package com.yangshengzhou.backend.controller.user;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.dto.CreateShareRequest;
import com.yangshengzhou.backend.entity.Share;
import com.yangshengzhou.backend.service.AuthService;
import com.yangshengzhou.backend.service.ShareService;
import com.yangshengzhou.backend.service.OperationLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/share")
public class ShareController {
    
    @Autowired
    private ShareService shareService;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private OperationLogService operationLogService;
    
    /**
     * 创建分享
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Share>> createShare(@RequestBody CreateShareRequest request, HttpServletRequest httpRequest) {
        try {
            Share share = shareService.createShare(request);
            
            // 记录操作日志
            operationLogService.logOperation(
                authService.getCurrentUser().getId(),
                "CREATE_SHARE",
                "SHARE",
                share.getId(),
                "创建分享: " + share.getShareCode(),
                getClientIpAddress(httpRequest),
                httpRequest.getHeader("User-Agent"),
                ""
            );
            
            return ResponseEntity.ok(ApiResponse.success(share));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("创建分享失败: " + e.getMessage()));
        }
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
                share.getId(),
                "更新分享: " + share.getShareCode(),
                getClientIpAddress(httpRequest),
                httpRequest.getHeader("User-Agent"),
                ""
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
                    id,
                    "删除分享: " + shareCode,
                    getClientIpAddress(httpRequest),
                    httpRequest.getHeader("User-Agent"),
                    ""
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