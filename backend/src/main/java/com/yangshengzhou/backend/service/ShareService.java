package com.yangshengzhou.backend.service;

import com.yangshengzhou.backend.entity.Share;
import com.yangshengzhou.backend.repository.ShareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShareService {
    
    @Autowired
    private ShareRepository shareRepository;
    
    /**
     * 创建分享
     */
    public Share createShare(Long fileId, Long userId, Integer expireDays) {
        // 生成唯一的分享码
        String shareCode;
        do {
            shareCode = generateShareCode();
        } while (shareRepository.existsByShareCode(shareCode));
        
        Share share = new Share();
        share.setShareCode(shareCode);
        share.setFileId(fileId);
        share.setUserId(userId);
        share.setCreateTime(new Date());
        
        // 设置过期时间
        if (expireDays != null && expireDays > 0) {
            Date expireTime = new Date();
            expireTime.setTime(expireTime.getTime() + (long) expireDays * 24 * 60 * 60 * 1000);
            share.setExpireTime(expireTime);
        }
        
        return shareRepository.save(share);
    }
    
    /**
     * 根据分享码获取分享
     */
    public Optional<Share> getShareByCode(String shareCode) {
        return shareRepository.findByShareCode(shareCode);
    }
    
    /**
     * 获取有效的分享
     */
    public Optional<Share> getValidShareByCode(String shareCode) {
        return shareRepository.findValidShareByCode(shareCode, new Date());
    }
    
    /**
     * 获取用户的分享列表
     */
    public List<Share> getUserShares(Long userId) {
        return shareRepository.findByUserId(userId);
    }
    
    /**
     * 获取用户的有效分享
     */
    public List<Share> getUserValidShares(Long userId) {
        return shareRepository.findValidSharesByUserId(userId, new Date());
    }
    
    /**
     * 更新分享
     */
    public Share updateShare(Long id, Integer expireDays) {
        Optional<Share> shareOptional = shareRepository.findById(id);
        if (shareOptional.isPresent()) {
            Share share = shareOptional.get();
            
            // 更新过期时间
            if (expireDays != null) {
                if (expireDays > 0) {
                    Date expireTime = new Date();
                    expireTime.setTime(expireTime.getTime() + (long) expireDays * 24 * 60 * 60 * 1000);
                    share.setExpireTime(expireTime);
                } else {
                    share.setExpireTime(null); // 永不过期
                }
            }
            
            return shareRepository.save(share);
        }
        return null;
    }
    
    /**
     * 删除分享
     */
    public boolean deleteShare(Long id) {
        if (shareRepository.existsById(id)) {
            shareRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * 检查分享是否属于用户
     */
    public boolean isShareOwnedByUser(Long shareId, Long userId) {
        Optional<Share> share = shareRepository.findById(shareId);
        return share.isPresent() && share.get().getUserId().equals(userId);
    }
    
    /**
     * 生成分享码
     */
    private String generateShareCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}