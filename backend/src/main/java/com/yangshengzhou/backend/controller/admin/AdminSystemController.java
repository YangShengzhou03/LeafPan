package com.yangshengzhou.backend.controller.admin;

import com.yangshengzhou.backend.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminSystemController {
    
    /**
     * 系统管理功能正在开发中
     */
    @GetMapping("/system-stats")
    public ResponseEntity<ApiResponse<String>> getSystemStats() {
        return ResponseEntity.ok(ApiResponse.success("系统管理功能正在开发中"));
    }
}