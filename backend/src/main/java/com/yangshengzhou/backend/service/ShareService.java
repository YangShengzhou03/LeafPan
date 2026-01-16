package com.yangshengzhou.backend.service;

import com.yangshengzhou.backend.dto.CreateShareRequest;
import com.yangshengzhou.backend.entity.Share;
import com.yangshengzhou.backend.entity.File;
import com.yangshengzhou.backend.entity.Folder;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.repository.ShareRepository;
import com.yangshengzhou.backend.repository.FileRepository;
import com.yangshengzhou.backend.repository.FolderRepository;
import com.yangshengzhou.backend.vo.ShareVO;
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
        share.setSharedTo(request.getSharedTo());
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
    public List<ShareVO> getUserShares() {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("用户未登录");
        }
        
        List<Share> shares = shareRepository.findActiveSharesByUserId(currentUser.getId(), LocalDateTime.now());
        return shares.stream()
            .map(this::convertToShareVO)
            .collect(Collectors.toList());
    }
    
    /**
     * 获取与我共享的文件列表
     */
    public List<ShareVO> getSharedWithMe() {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("用户未登录");
        }
        
        List<Share> shares = shareRepository.findSharedWithMe(currentUser.getId(), LocalDateTime.now());
        return shares.stream()
            .map(this::convertToShareVO)
            .collect(Collectors.toList());
    }
    
    /**
     * 将Share转换为ShareVO
     */
    private ShareVO convertToShareVO(Share share) {
        ShareVO vo = new ShareVO();
        vo.setId(share.getId());
        vo.setShareCode(share.getShareCode());
        vo.setFileId(share.getFileId());
        vo.setFolderId(share.getFolderId());
        vo.setShareType(share.getShareType().intValue());
        vo.setPassword(share.getPassword());
        vo.setCreateTime(share.getCreatedTime());
        vo.setExpireTime(share.getExpireTime());
        vo.setViewCount(share.getViewCount());
        vo.setDownloadCount(share.getDownloadCount());
        vo.setIsActive(share.getIsActive() ? 1 : 0);
        
        // 获取文件信息
        if (share.getFileId() != null) {
            Optional<File> fileOpt = fileRepository.findById(share.getFileId());
            if (fileOpt.isPresent()) {
                File file = fileOpt.get();
                vo.setFileName(file.getName());
                vo.setFileSize(file.getSize());
                vo.setFileType(file.getMimeType());
            }
        }
        
        // 获取文件夹信息
        if (share.getFolderId() != null) {
            Optional<Folder> folderOpt = folderRepository.findById(share.getFolderId());
            if (folderOpt.isPresent()) {
                Folder folder = folderOpt.get();
                vo.setFolderName(folder.getName());
            }
        }
        
        return vo;
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
     * 根据分享码获取分享 - 公开方法，无需登录
     */
    public Share getShareByCode(String shareCode) {
        Optional<Share> share = shareRepository.findByShareCodeAndIsActiveTrue(shareCode);
        return share.orElse(null);
    }
    
    /**
     * 验证分享密码
     */
    public boolean verifySharePassword(String shareCode, String password) {
        Share share = getShareByCode(shareCode);
        if (share == null || share.isExpired()) {
            return false;
        }
        // 公开分享无需密码验证
        if (share.getShareType() == 0) {
            return true;
        }
        // 私密分享或密码分享需要验证
        return share.getPassword() != null && share.getPassword().equals(password);
    }
    
    /**
     * 增加分享的查看次数 - 根据ID
     */
    @Transactional
    public void incrementViewCount(Long shareId) {
        Optional<Share> shareOpt = shareRepository.findById(shareId);
        if (shareOpt.isPresent()) {
            Share share = shareOpt.get();
            share.setViewCount(share.getViewCount() + 1);
            shareRepository.save(share);
        }
    }
    
    /**
     * 增加分享的查看次数 - 根据分享码
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
     * 增加分享的下载次数 - 根据ID
     */
    @Transactional
    public void incrementDownloadCount(Long shareId) {
        Optional<Share> shareOpt = shareRepository.findById(shareId);
        if (shareOpt.isPresent()) {
            Share share = shareOpt.get();
            share.setDownloadCount(share.getDownloadCount() + 1);
            shareRepository.save(share);
        }
    }
    
    /**
     * 增加分享的下载次数 - 根据分享码
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

    /**
     * 获取分享总数
     */
    public long getShareCount() {
        return shareRepository.count();
    }
}