package com.yangshengzhou.backend.service;

import com.yangshengzhou.backend.entity.Share;
import com.yangshengzhou.backend.entity.File;
import com.yangshengzhou.backend.entity.Folder;
import com.yangshengzhou.backend.dto.CreateShareRequest;
import com.yangshengzhou.backend.dto.AccessShareRequest;
import com.yangshengzhou.backend.vo.ShareVO;
import com.yangshengzhou.backend.repository.ShareRepository;
import com.yangshengzhou.backend.repository.FileRepository;
import com.yangshengzhou.backend.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShareService {
    
    @Autowired
    private ShareRepository shareRepository;
    
    @Autowired
    private FileRepository fileRepository;
    
    @Autowired
    private FolderRepository folderRepository;
    
    /**
     * 创建分享
     */
    public Share createShare(Long userId, CreateShareRequest request) {
        // 生成唯一的分享码
        String shareCode;
        do {
            shareCode = generateShareCode();
        } while (shareRepository.existsByShareCode(shareCode));
        
        Share share = new Share();
        share.setShareCode(shareCode);
        share.setUserId(userId);
        share.setShareType(request.getShareType());
        share.setPassword(request.getPassword());
        
        // 设置目标
        if ("file".equals(request.getTargetType())) {
            share.setFileId(request.getTargetId());
        } else if ("folder".equals(request.getTargetType())) {
            share.setFolderId(request.getTargetId());
        }
        
        // 设置过期时间
        if (request.getExpireTime() != null && !request.getExpireTime().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date expireTime = sdf.parse(request.getExpireTime());
                share.setExpireTime(expireTime);
            } catch (ParseException e) {
                // 默认7天后过期
                Date expireTime = new Date();
                expireTime.setTime(expireTime.getTime() + 7L * 24 * 60 * 60 * 1000);
                share.setExpireTime(expireTime);
            }
        }
        
        return shareRepository.save(share);
    }
    
    /**
     * 获取分享
     */
    public Optional<Share> getShare(Long id) {
        return shareRepository.findById(id);
    }
    
    /**
     * 根据分享码获取分享
     */
    public Optional<Share> getShareByCode(String shareCode) {
        return shareRepository.findByShareCode(shareCode);
    }
    
    /**
     * 访问分享
     */
    public ShareVO accessShare(AccessShareRequest request) {
        Optional<Share> shareOptional = getValidShareByCode(request.getShareCode());
        if (!shareOptional.isPresent()) {
            return null;
        }
        
        Share share = shareOptional.get();
        
        // 检查密码
        if (share.getShareType() == 1 && !share.getPassword().equals(request.getPassword())) {
            return null;
        }
        
        // 增加浏览次数
        incrementViewCount(request.getShareCode());
        
        // 转换为VO
        ShareVO shareVO = new ShareVO();
        shareVO.setId(share.getId());
        shareVO.setShareCode(share.getShareCode());
        shareVO.setFileId(share.getFileId());
        shareVO.setFolderId(share.getFolderId());
        shareVO.setShareType(share.getShareType());
        shareVO.setCreateTime(share.getCreateTime());
        shareVO.setExpireTime(share.getExpireTime());
        shareVO.setViewCount(share.getViewCount());
        shareVO.setDownloadCount(share.getDownloadCount());
        shareVO.setIsActive(share.getIsActive());
        
        // 获取文件或文件夹信息
        if (share.getFileId() != null) {
            Optional<File> fileOptional = fileRepository.findById(share.getFileId());
            if (fileOptional.isPresent()) {
                File file = fileOptional.get();
                shareVO.setFileName(file.getName());
                shareVO.setFileSize(file.getSize());
                shareVO.setFileType(file.getExtension());
            }
        } else if (share.getFolderId() != null) {
            Optional<Folder> folderOptional = folderRepository.findById(share.getFolderId());
            if (folderOptional.isPresent()) {
                Folder folder = folderOptional.get();
                shareVO.setFolderName(folder.getName());
            }
        }
        
        return shareVO;
    }
    
    /**
     * 获取用户的分享列表
     */
    public List<ShareVO> getUserShares(Long userId) {
        List<Share> shares = shareRepository.findByUserId(userId);
        List<ShareVO> shareVOs = new ArrayList<>();
        
        for (Share share : shares) {
            ShareVO shareVO = new ShareVO();
            shareVO.setId(share.getId());
            shareVO.setShareCode(share.getShareCode());
            shareVO.setFileId(share.getFileId());
            shareVO.setFolderId(share.getFolderId());
            shareVO.setShareType(share.getShareType());
            shareVO.setCreateTime(share.getCreateTime());
            shareVO.setExpireTime(share.getExpireTime());
            shareVO.setViewCount(share.getViewCount());
            shareVO.setDownloadCount(share.getDownloadCount());
            shareVO.setIsActive(share.getIsActive());
            
            // 获取文件或文件夹信息
            if (share.getFileId() != null) {
                Optional<File> fileOptional = fileRepository.findById(share.getFileId());
                if (fileOptional.isPresent()) {
                    File file = fileOptional.get();
                    shareVO.setFileName(file.getName());
                    shareVO.setFileSize(file.getSize());
                    shareVO.setFileType(file.getExtension());
                }
            } else if (share.getFolderId() != null) {
                Optional<Folder> folderOptional = folderRepository.findById(share.getFolderId());
                if (folderOptional.isPresent()) {
                    Folder folder = folderOptional.get();
                    shareVO.setFolderName(folder.getName());
                }
            }
            
            shareVOs.add(shareVO);
        }
        
        return shareVOs;
    }
    
    /**
     * 更新分享
     */
    public Share updateShare(Long id, CreateShareRequest request) {
        Optional<Share> shareOptional = shareRepository.findById(id);
        if (shareOptional.isPresent()) {
            Share share = shareOptional.get();
            
            share.setShareType(request.getShareType());
            share.setPassword(request.getPassword());
            
            // 更新过期时间
            if (request.getExpireTime() != null && !request.getExpireTime().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date expireTime = sdf.parse(request.getExpireTime());
                    share.setExpireTime(expireTime);
                } catch (ParseException e) {
                    // 默认7天后过期
                    Date expireTime = new Date();
                    expireTime.setTime(expireTime.getTime() + 7L * 24 * 60 * 60 * 1000);
                    share.setExpireTime(expireTime);
                }
            } else {
                share.setExpireTime(null); // 永不过期
            }
            
            return shareRepository.save(share);
        }
        return null;
    }
    
    /**
     * 删除分享
     */
    public boolean deleteShare(Long id, Long userId) {
        Optional<Share> shareOptional = shareRepository.findById(id);
        if (shareOptional.isPresent() && shareOptional.get().getUserId().equals(userId)) {
            shareRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * 增加分享浏览次数
     */
    public void incrementViewCount(String shareCode) {
        Optional<Share> shareOptional = shareRepository.findByShareCode(shareCode);
        if (shareOptional.isPresent()) {
            Share share = shareOptional.get();
            share.setViewCount(share.getViewCount() + 1);
            shareRepository.save(share);
        }
    }
    
    /**
     * 增加分享下载次数
     */
    public void incrementDownloadCount(String shareCode) {
        Optional<Share> shareOptional = shareRepository.findByShareCode(shareCode);
        if (shareOptional.isPresent()) {
            Share share = shareOptional.get();
            share.setDownloadCount(share.getDownloadCount() + 1);
            shareRepository.save(share);
        }
    }
    
    /**
     * 检查分享是否有效
     */
    public boolean isShareValid(String shareCode) {
        return getValidShareByCode(shareCode).isPresent();
    }
    
    /**
     * 检查用户是否有权限操作分享
     */
    public boolean hasPermission(Long userId, Long shareId) {
        Optional<Share> shareOptional = shareRepository.findById(shareId);
        return shareOptional.isPresent() && shareOptional.get().getUserId().equals(userId);
    }
    
    /**
     * 获取有效的分享
     */
    public Optional<Share> getValidShareByCode(String shareCode) {
        return shareRepository.findValidShareByCode(shareCode, new Date());
    }
    
    /**
     * 生成分享码
     */
    private String generateShareCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}