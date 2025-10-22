package com.yangshengzhou.backend.service;

import com.yangshengzhou.backend.entity.Folder;
import com.yangshengzhou.backend.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FolderService {
    
    @Autowired
    private FolderRepository folderRepository;
    
    /**
     * 创建文件夹
     */
    public Folder createFolder(String name, Long parentId, Long userId) {
        Folder folder = new Folder();
        folder.setName(name);
        folder.setParentId(parentId);
        folder.setUserId(userId);
        folder.setCreateTime(new Date());
        folder.setUpdateTime(new Date());
        
        return folderRepository.save(folder);
    }
    
    /**
     * 获取文件夹
     */
    public Optional<Folder> getFolder(Long id) {
        return folderRepository.findById(id);
    }
    
    /**
     * 获取用户的文件夹列表
     */
    public List<Folder> getUserFolders(Long userId) {
        return folderRepository.findByUserId(userId);
    }
    
    /**
     * 获取用户在指定父文件夹下的子文件夹
     */
    public List<Folder> getSubFolders(Long userId, Long parentId) {
        return folderRepository.findByUserIdAndParentId(userId, parentId);
    }
    
    /**
     * 更新文件夹
     */
    public Folder updateFolder(Long id, String name) {
        Optional<Folder> folderOptional = folderRepository.findById(id);
        if (folderOptional.isPresent()) {
            Folder folder = folderOptional.get();
            folder.setName(name);
            folder.setUpdateTime(new Date());
            return folderRepository.save(folder);
        }
        return null;
    }
    
    /**
     * 删除文件夹
     */
    public boolean deleteFolder(Long id) {
        if (folderRepository.existsById(id)) {
            folderRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * 检查文件夹是否属于用户
     */
    public boolean isFolderOwnedByUser(Long folderId, Long userId) {
        Optional<Folder> folder = folderRepository.findById(folderId);
        return folder.isPresent() && folder.get().getUserId().equals(userId);
    }
}