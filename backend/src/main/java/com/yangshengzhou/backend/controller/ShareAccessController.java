package com.yangshengzhou.backend.controller;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.entity.File;
import com.yangshengzhou.backend.entity.Share;
import com.yangshengzhou.backend.service.FileService;
import com.yangshengzhou.backend.service.FileStorageService;
import com.yangshengzhou.backend.service.OperationLogService;
import com.yangshengzhou.backend.service.ShareService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 公开分享访问控制器
 * 处理不需要登录即可访问的分享链接相关功能
 */
@RestController
@RequestMapping("/public/share")
public class ShareAccessController {

    @Autowired
    private ShareService shareService;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 通过分享码获取分享详情
     * 公开接口，无需登录
     */
    @GetMapping("/{shareCode}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getShareInfo(@PathVariable String shareCode) {
        try {
            // 根据分享码查询分享信息
            Share share = shareService.getShareByCode(shareCode);
            if (share == null) {
                return ResponseEntity.status(404).body(ApiResponse.error("分享链接不存在或已过期"));
            }

            // 检查分享是否过期
            if (share.isExpired()) {
                return ResponseEntity.status(410).body(ApiResponse.error("分享链接已过期"));
            }

            // 增加查看次数
            shareService.incrementViewCount(share.getId());

            // 获取分享的文件或文件夹信息
            Map<String, Object> shareInfo = new HashMap<>();
            shareInfo.put("shareCode", share.getShareCode());
            shareInfo.put("shareType", share.getShareType());
            shareInfo.put("needPassword", share.getShareType() == 1);
            shareInfo.put("expireTime", share.getExpireTime());
            shareInfo.put("createdTime", share.getCreatedTime());

            // 如果是文件分享，获取文件信息
            if (share.getFileId() != null) {
                File file = fileService.getFile(share.getFileId()).orElse(null);
                if (file == null) {
                    return ResponseEntity.status(404).body(ApiResponse.error("分享的文件不存在"));
                }
                shareInfo.put("file", file);
                shareInfo.put("type", "file");
            } else if (share.getFolderId() != null) {
                // 文件夹分享，这里只返回文件夹基本信息，具体文件列表需要单独请求
                shareInfo.put("folderId", share.getFolderId());
                shareInfo.put("type", "folder");
            }

            return ResponseEntity.ok(ApiResponse.success(shareInfo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取分享信息失败: " + e.getMessage()));
        }
    }

    /**
     * 验证分享密码
     * 公开接口，无需登录
     */
    @PostMapping("/{shareCode}/verify")
    public ResponseEntity<ApiResponse<Boolean>> verifyPassword(
            @PathVariable String shareCode,
            @RequestBody Map<String, String> requestBody) {
        try {
            String password = requestBody.get("password");
            if (password == null || password.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("密码不能为空"));
            }

            boolean isValid = shareService.verifySharePassword(shareCode, password);
            if (!isValid) {
                return ResponseEntity.status(403).body(ApiResponse.error("密码错误"));
            }

            return ResponseEntity.ok(ApiResponse.success(true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("验证密码失败: " + e.getMessage()));
        }
    }

    /**
     * 通过分享链接直接下载文件
     * 公开接口，无需登录
     */
    @GetMapping("/{shareCode}/download")
    public void downloadFileByShare(
            @PathVariable String shareCode,
            @RequestParam(value = "password", required = false) String password,
            HttpServletRequest request,
            HttpServletResponse response) {
        InputStream inputStream = null;

        try {
            // 验证分享码和密码
            Share share = shareService.getShareByCode(shareCode);
            if (share == null) {
                response.setStatus(404);
                return;
            }

            // 检查分享是否过期
            if (share.isExpired()) {
                response.setStatus(410);
                return;
            }

            // 检查是否需要密码验证
            if (share.getShareType() == 1) {
                if (password == null || !shareService.verifySharePassword(shareCode, password)) {
                    response.setStatus(403);
                    return;
                }
            }

            // 确保是文件分享
            if (share.getFileId() == null) {
                response.setStatus(400);
                return;
            }

            // 获取文件信息
            File file = fileService.getFile(share.getFileId()).orElse(null);
            if (file == null) {
                response.setStatus(404);
                return;
            }

            // 获取文件大小信息
            long fileSize = fileStorageService.getFileSize(file.getStorageKey());

            // 获取文件输入流
            inputStream = fileStorageService.downloadFile(file.getStorageKey());

            // 设置响应头
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
            response.setContentLengthLong(fileSize);
            response.setBufferSize(0);

            // 输出文件内容
            try (OutputStream outputStream = response.getOutputStream()) {
                IOUtils.copy(inputStream, outputStream);
            }

            // 异步增加下载次数
            final Long shareId = share.getId();
            new Thread(() -> {
                try {
                    shareService.incrementDownloadCount(shareId);
                } catch (Exception e) {
                    // 忽略异常
                }
            }).start();

        } catch (Exception e) {
            response.setStatus(400);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    // 忽略关闭异常
                }
            }
        }
    }
}
