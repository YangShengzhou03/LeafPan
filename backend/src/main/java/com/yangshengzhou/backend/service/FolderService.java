package com.yangshengzhou.backend.service;

import com.yangshengzhou.backend.entity.Folder;
import com.yangshengzhou.backend.dto.CreateFolderRequest;
import com.yangshengzhou.backend.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FolderService {
    
    @Autowired
    private FolderRepository folderRepository;
    
    /**
     * 获取文件夹
     */
    public Optional<Folder> getFolder(Long id) {
        return folderRepository.findById(id);
    }
    
    /**
     * 获取用户的文件夹列表
     */
    public List<Folder> getUserFolders(String userId) {
        return folderRepository.findByUserId(userId);
    }
    
    /**
     * 获取用户指定父文件夹下的子文件夹列表
     */
    public List<Folder> getUserFoldersByParentId(String userId, Long parentId) {
        return folderRepository.findByUserIdAndParentId(userId, parentId);
    }
    
    /**
     * 创建文件夹
     */
    public Folder createFolder(String userId, CreateFolderRequest request) {
        Folder folder = new Folder();
        folder.setName(request.getName());
        folder.setParentId(request.getParentId());
        folder.setUserId(userId);
        
        // 构建文件夹路径
        String path = "/";
        if (request.getParentId() != null && request.getParentId() > 0) {
            Optional<Folder> parentFolder = folderRepository.findById(request.getParentId());
            if (parentFolder.isPresent()) {
                path = parentFolder.get().getPath() + request.getName() + "/";
            }
        } else {
            path = "/" + request.getName() + "/";
        }
        folder.setPath(path);
        
        return folderRepository.save(folder);
    }
    
    /**
     * 更新文件夹信息
     */
    public Folder updateFolder(Long id, String name) {
        Optional<Folder> folderOptional = folderRepository.findById(id);
        if (folderOptional.isPresent()) {
            Folder folder = folderOptional.get();
            folder.setName(name);
            return folderRepository.save(folder);
        }
        return null;
    }
    
    /**
     * 删除文件夹
     */
    public boolean deleteFolder(Long id, String userId) {
        Optional<Folder> folderOptional = folderRepository.findById(id);
        if (folderOptional.isPresent() && folderOptional.get().getUserId().equals(userId)) {
            folderRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * 检查文件夹名称是否存在
     */
    public boolean existsByUserIdAndName(String userId, String name) {
        return folderRepository.existsByUserIdAndName(userId, name);
    }
    
    /**
     * 检查文件夹名称是否存在（排除指定ID）
     */
    public boolean existsByUserIdAndNameAndIdNot(String userId, String name, Long id) {
        return folderRepository.existsByUserIdAndNameAndIdNot(userId, name, id);
    }
    
    /**
     * 获取文件夹路径
     */
    public List<Folder> getFolderPath(Long folderId) {
        List<Folder> path = new ArrayList<>();
        Optional<Folder> folderOptional = folderRepository.findById(folderId);
        
        while (folderOptional.isPresent()) {
            Folder folder = folderOptional.get();
            path.add(0, folder); // 添加到列表开头
            
            if (folder.getParentId() == null || folder.getParentId() == 0) {
                break;
            }
            
            folderOptional = folderRepository.findById(folder.getParentId());
        }
        
        return path;
    }
    
    /**
     * 获取子文件夹列表
     */
    public List<Folder> getSubFolders(Long parentId, String userId) {
        // 首先验证用户是否有权限访问父文件夹
        if (parentId != null && parentId > 0) {
            if (!hasPermission(userId, parentId)) {
                return new ArrayList<>(); // 用户无权限，返回空列表
            }
        }
        
        // 获取所有子文件夹
        List<Folder> allSubFolders = folderRepository.findByParentId(parentId);
        
        // 过滤出属于当前用户的子文件夹
        List<Folder> userSubFolders = new ArrayList<>();
        for (Folder folder : allSubFolders) {
            if (folder.getUserId().equals(userId)) {
                userSubFolders.add(folder);
            }
        }
        
        return userSubFolders;
    }
    
    /**
     * 检查用户是否有权限访问文件夹
     */
    public boolean hasPermission(String userId, Long folderId) {
        Optional<Folder> folderOptional = folderRepository.findById(folderId);
        return folderOptional.isPresent() && folderOptional.get().getUserId().equals(userId);
    }

    public boolean isFolderOwnedByUser(Long folderId, String userId) {
        Optional<Folder> folderOptional = folderRepository.findById(folderId);
        return folderOptional.isPresent() && folderOptional.get().getUserId().equals(userId);
    }
}