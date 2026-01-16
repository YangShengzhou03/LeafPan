package com.yangshengzhou.backend.controller.admin;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.entity.OperationLog;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.service.AuthService;
import com.yangshengzhou.backend.service.OperationLogService;
import com.yangshengzhou.backend.vo.OperationLogVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/log")
public class AdminLogController {
    
    @Autowired
    private OperationLogService operationLogService;
    
    @Autowired
    private AuthService authService;
    
    /**
     * 获取所有操作日志（分页）
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || currentUser.getRole() != 1) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            Page<OperationLog> logs = operationLogService.getAllOperationLogs(page, size, level, module, startDate, endDate);
            
            // 转换为VO对象
            List<OperationLogVO> logVOs = operationLogService.convertToVOs(logs.getContent());
            
            // 创建分页响应对象，避免直接返回PageImpl
            Map<String, Object> response = new HashMap<>();
            response.put("content", logVOs);
            response.put("pageNumber", logs.getNumber());
            response.put("pageSize", logs.getSize());
            response.put("totalElements", logs.getTotalElements());
            response.put("totalPages", logs.getTotalPages());
            response.put("first", logs.isFirst());
            response.put("last", logs.isLast());
            
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取日志列表失败: " + e.getMessage()));
        }
    }
    
    /**
     * 导出日志
     */
    @GetMapping("/export")
    public ResponseEntity<ApiResponse<List<OperationLogVO>>> exportLogs(
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            List<OperationLogVO> logs = operationLogService.getAllOperationLogs();
            
            return ResponseEntity.ok(ApiResponse.success(logs));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("导出日志失败: " + e.getMessage()));
        }
    }
    
    /**
     * 清空日志
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> clearLogs(HttpServletRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null || currentUser.getRole() != 1) {
                return ResponseEntity.status(403).body(ApiResponse.error("无权限访问"));
            }
            
            // 记录清空日志操作
            operationLogService.logOperation(
                currentUser.getId(),
                "CLEAR_LOGS",
                "SYSTEM",
                "",
                "管理员清空系统日志",
                request.getRemoteAddr(),
                request.getHeader("User-Agent")
            );
            
            // 实际清空所有日志数据
            operationLogService.clearAllLogs();
            
            return ResponseEntity.ok(ApiResponse.success("日志清空成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("清空日志失败: " + e.getMessage()));
        }
    }
}