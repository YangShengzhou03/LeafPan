package com.yangshengzhou.backend.service;

import com.yangshengzhou.backend.dto.CreateShareRequest;
import com.yangshengzhou.backend.entity.Share;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.repository.ShareRepository;
import com.yangshengzhou.backend.repository.FileRepository;
import com.yangshengzhou.backend.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ShareService {
    
    @Autowired
    private ShareRepository shareRepository;
    
    @Autowired
    private FileRepository fileRepository;
    
    @Autowired
    private FolderRepository folderRepository;
    
    @Autowired
    private AuthService authService;
    
    /**
     * 创建分享
     */
    @Transactional
    public Share createShare(CreateShareRequest request) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("用户未登录");
        }
        
        // 验证目标是否存在
        if ("file".equals(request.getTargetType())) {
            if (!fileRepository.existsById(request.getTargetId())) {
                throw new RuntimeException("文件不存在");
            }
        } else if ("folder".equals(request.getTargetType())) {
            if (!folderRepository.existsById(request.getTargetId())) {
                throw new RuntimeException("文件夹不存在");
            }
        } else {
            throw new RuntimeException("无效的分享目标类型");
        }
        
        // 生成唯一的分享码
        String shareCode = generateUniqueShareCode();
        
        Share share = new Share();
        share.setShareCode(shareCode);
        share.setUserId(currentUser.getId());
        share.setShareType(request.getShareType().byteValue());
        share.setPassword(request.getPassword());
        share.setIsActive(true);
        
        // 设置过期时间
        if (request.getExpireTime() != null && !request.getExpireTime().isEmpty()) {
            try {
                LocalDateTime expireTime = LocalDateTime.parse(request.getExpireTime(), DateTimeFormatter.ISO_DATE_TIME);
                share.setExpireTime(expireTime);
            } catch (Exception e) {
                throw new RuntimeException("无效的过期时间格式");
            }
        }
        
        // 设置文件或文件夹ID
        if ("file".equals(request.getTargetType())) {
            share.setFileId(request.getTargetId());
        } else {
            share.setFolderId(request.getTargetId());
        }
        
        return shareRepository.save(share);
    }
    
    /**
     * 获取用户的分享列表
     */
    public List<Share> getUserShares() {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("用户未登录");
        }
        
        return shareRepository.findActiveSharesByUserId(currentUser.getId(), LocalDateTime.now());
    }
    
    /**
     * 根据ID获取分享
     */
    public Optional<Share> getShareById(Long id) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("用户未登录");
        }
        
        Optional<Share> share = shareRepository.findById(id);
        if (share.isPresent() && !share.get().getUserId().equals(currentUser.getId())) {
            throw new RuntimeException("无权限访问此分享");
        }
        
        return share;
    }
    
    /**
     * 更新分享
     */
    @Transactional
    public Share updateShare(Long id, CreateShareRequest request) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("用户未登录");
        }
        
        Optional<Share> shareOpt = shareRepository.findById(id);
        if (shareOpt.isEmpty()) {
            throw new RuntimeException("分享不存在");
        }
        
        Share share = shareOpt.get();
        if (!share.getUserId().equals(currentUser.getId())) {
            throw new RuntimeException("无权限修改此分享");
        }
        
        // 更新分享信息
        share.setShareType(request.getShareType().byteValue());
        share.setPassword(request.getPassword());
        
        // 更新过期时间
        if (request.getExpireTime() != null && !request.getExpireTime().isEmpty()) {
            try {
                LocalDateTime expireTime = LocalDateTime.parse(request.getExpireTime(), DateTimeFormatter.ISO_DATE_TIME);
                share.setExpireTime(expireTime);
            } catch (Exception e) {
                throw new RuntimeException("无效的过期时间格式");
            }
        } else {
            share.setExpireTime(null);
        }
        
        return shareRepository.save(share);
    }
    
    /**
     * 删除分享
     */
    @Transactional
    public boolean deleteShare(Long id) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("用户未登录");
        }
        
        Optional<Share> shareOpt = shareRepository.findById(id);
        if (shareOpt.isEmpty()) {
            throw new RuntimeException("分享不存在");
        }
        
        Share share = shareOpt.get();
        if (!share.getUserId().equals(currentUser.getId())) {
            throw new RuntimeException("无权限删除此分享");
        }
        
        share.setIsActive(false);
        shareRepository.save(share);
        return true;
    }
    
    /**
     * 根据分享码获取分享
     */
    public Optional<Share> getShareByCode(String shareCode) {
        return shareRepository.findByShareCodeAndIsActiveTrue(shareCode);
    }
    
    /**
     * 增加分享的查看次数
     */
    @Transactional
    public void incrementViewCount(String shareCode) {
        Optional<Share> shareOpt = shareRepository.findByShareCodeAndIsActiveTrue(shareCode);
        if (shareOpt.isPresent()) {
            Share share = shareOpt.get();
            share.setViewCount(share.getViewCount() + 1);
            shareRepository.save(share);
        }
    }
    
    /**
     * 增加分享的下载次数
     */
    @Transactional
    public void incrementDownloadCount(String shareCode) {
        Optional<Share> shareOpt = shareRepository.findByShareCodeAndIsActiveTrue(shareCode);
        if (shareOpt.isPresent()) {
            Share share = shareOpt.get();
            share.setDownloadCount(share.getDownloadCount() + 1);
            shareRepository.save(share);
        }
    }
    
    /**
     * 生成唯一的分享码
     */
    private String generateUniqueShareCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(10);
        
        String shareCode;
        do {
            sb.setLength(0);
            for (int i = 0; i < 10; i++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            shareCode = sb.toString();
        } while (shareRepository.existsByShareCode(shareCode));
        
        return shareCode;
    }
}