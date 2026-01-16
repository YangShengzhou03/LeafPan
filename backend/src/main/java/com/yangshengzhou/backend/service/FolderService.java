package com.yangshengzhou.backend.service;

import com.yangshengzhou.backend.entity.File;
import com.yangshengzhou.backend.entity.Folder;
import com.yangshengzhou.backend.dto.CreateFolderRequest;
import com.yangshengzhou.backend.repository.FileRepository;
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
    
    @Autowired
    private FileRepository fileRepository;
    
    /**
     * Get folder
     */
    public Optional<Folder> getFolder(Long id) {
        return folderRepository.findById(id);
    }
    
    /**
     * Get user's folder list
     */
    public List<Folder> getUserFolders(String userId) {
        return folderRepository.findByUserId(userId);
    }
    
    /**
     * Get user's subfolder list under specified parent folder
     */
    public List<Folder> getUserFoldersByParentId(String userId, Long parentId) {
        return folderRepository.findByUserIdAndParentId(userId, parentId);
    }
    
    /**
     * Create folder
     */
    public Folder createFolder(String userId, CreateFolderRequest request) {
        Folder folder = new Folder();
        folder.setName(request.getName());
        folder.setParentId(request.getParentId());
        folder.setUserId(userId);
        
        // Build folder path
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
     * Update folder information
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
     * Delete folder (mark as deleted)
     */
    public boolean deleteFolder(Long id, String userId) {
        Optional<Folder> folderOptional = folderRepository.findById(id);
        if (folderOptional.isPresent() && folderOptional.get().getUserId().equals(userId)) {
            Folder folder = folderOptional.get();
            folder.setIsDeleted(true);
            folderRepository.save(folder);
            return true;
        }
        return false;
    }
    
    /**
     * Delete folder recursively with all subfolders and files
     */
    public boolean deleteFolderRecursively(Long id, String userId) {
        Optional<Folder> folderOptional = folderRepository.findById(id);
        if (!folderOptional.isPresent() || !folderOptional.get().getUserId().equals(userId)) {
            return false;
        }
        
        deleteFolderRecursivelyInternal(id, userId);
        return true;
    }
    
    /**
     * Internal recursive method to delete folder and all its contents
     */
    private void deleteFolderRecursivelyInternal(Long folderId, String userId) {
        List<Folder> subFolders = folderRepository.findByUserIdAndParentIdAndIsDeletedFalse(userId, folderId);
        for (Folder subFolder : subFolders) {
            deleteFolderRecursivelyInternal(subFolder.getId(), userId);
        }
        
        List<File> files = fileRepository.findByUserIdAndFolderId(userId, folderId);
        for (File file : files) {
            file.setIsDeleted(true);
            fileRepository.save(file);
        }
        
        Optional<Folder> folderOptional = folderRepository.findById(folderId);
        if (folderOptional.isPresent()) {
            Folder folder = folderOptional.get();
            folder.setIsDeleted(true);
            folderRepository.save(folder);
        }
    }
    
    /**
     * Permanently delete folder
     */
    public boolean permanentDeleteFolder(Long id, String userId) {
        Optional<Folder> folderOptional = folderRepository.findById(id);
        if (folderOptional.isPresent() && folderOptional.get().getUserId().equals(userId)) {
            folderRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Permanently delete folder recursively with all subfolders and files
     */
    public boolean permanentDeleteFolderRecursively(Long id, String userId) {
        Optional<Folder> folderOptional = folderRepository.findById(id);
        if (!folderOptional.isPresent() || !folderOptional.get().getUserId().equals(userId)) {
            return false;
        }
        
        permanentDeleteFolderRecursivelyInternal(id, userId);
        return true;
    }
    
    /**
     * Internal recursive method to permanently delete folder and all its contents
     */
    private void permanentDeleteFolderRecursivelyInternal(Long folderId, String userId) {
        List<Folder> subFolders = folderRepository.findByUserIdAndParentIdAndIsDeletedFalse(userId, folderId);
        for (Folder subFolder : subFolders) {
            permanentDeleteFolderRecursivelyInternal(subFolder.getId(), userId);
        }
        
        List<File> files = fileRepository.findByUserIdAndFolderId(userId, folderId);
        for (File file : files) {
            fileRepository.deleteById(file.getId());
        }
        
        folderRepository.deleteById(folderId);
    }
    
    /**
     * Restore folder
     */
    public boolean restoreFolder(Long id, String userId) {
        Optional<Folder> folderOptional = folderRepository.findById(id);
        if (folderOptional.isPresent() && folderOptional.get().getUserId().equals(userId)) {
            Folder folder = folderOptional.get();
            folder.setIsDeleted(false);
            folderRepository.save(folder);
            return true;
        }
        return false;
    }
    
    /**
     * Restore folder recursively with all subfolders and files
     */
    public boolean restoreFolderRecursively(Long id, String userId) {
        Optional<Folder> folderOptional = folderRepository.findById(id);
        if (!folderOptional.isPresent() || !folderOptional.get().getUserId().equals(userId)) {
            return false;
        }
        
        restoreFolderRecursivelyInternal(id, userId);
        return true;
    }
    
    /**
     * Internal recursive method to restore folder and all its contents
     */
    private void restoreFolderRecursivelyInternal(Long folderId, String userId) {
        List<Folder> subFolders = folderRepository.findDeletedFoldersByUserId(userId);
        for (Folder subFolder : subFolders) {
            if (subFolder.getParentId().equals(folderId)) {
                restoreFolderRecursivelyInternal(subFolder.getId(), userId);
            }
        }
        
        List<File> files = fileRepository.findDeletedFilesByUserId(userId);
        for (File file : files) {
            if (file.getFolderId().equals(folderId)) {
                file.setIsDeleted(false);
                fileRepository.save(file);
            }
        }
        
        Optional<Folder> folderOptional = folderRepository.findById(folderId);
        if (folderOptional.isPresent()) {
            Folder folder = folderOptional.get();
            folder.setIsDeleted(false);
            folderRepository.save(folder);
        }
    }
    
    /**
     * Check if folder name exists
     */
    public boolean existsByUserIdAndName(String userId, String name) {
        return folderRepository.existsByUserIdAndName(userId, name);
    }
    
    /**
     * Check if folder name exists (excluding specified ID)
     */
    public boolean existsByUserIdAndNameAndIdNot(String userId, String name, Long id) {
        return folderRepository.existsByUserIdAndNameAndIdNot(userId, name, id);
    }
    
    /**
     * Get folder path
     */
    public List<Folder> getFolderPath(Long folderId) {
        List<Folder> path = new ArrayList<>();
        Optional<Folder> folderOptional = folderRepository.findById(folderId);
        
        while (folderOptional.isPresent()) {
            Folder folder = folderOptional.get();
            path.add(0, folder); // Add to the beginning of the list
            
            if (folder.getParentId() == null || folder.getParentId() == 0) {
                break;
            }
            
            folderOptional = folderRepository.findById(folder.getParentId());
        }
        
        return path;
    }
    
    /**
     * Get subfolder list
     */
    public List<Folder> getSubFolders(Long parentId, String userId) {
        // First verify if user has permission to access parent folder
        if (parentId != null && parentId > 0) {
            if (!hasPermission(userId, parentId)) {
                return new ArrayList<>(); // User has no permission, return empty list
            }
        }
        
        // Get all subfolders (filter out deleted ones)
        List<Folder> allSubFolders = folderRepository.findByUserIdAndParentIdAndIsDeletedFalse(userId, parentId);
        
        return allSubFolders;
    }
    
    /**
     * Check if user has permission to access folder
     */
    public boolean hasPermission(String userId, Long folderId) {
        Optional<Folder> folderOptional = folderRepository.findById(folderId);
        return folderOptional.isPresent() && folderOptional.get().getUserId().equals(userId);
    }

    public boolean isFolderOwnedByUser(Long folderId, String userId) {
        Optional<Folder> folderOptional = folderRepository.findById(folderId);
        return folderOptional.isPresent() && folderOptional.get().getUserId().equals(userId);
    }
    
    /**
     * Get user's root folder
     */
    public Optional<Folder> getUserRootFolder(String userId) {
        return folderRepository.findUserRootFolder(userId, 0L);
    }
    
    /**
     * Get trash folder list
     */
    public List<Folder> getTrashFolders(String userId) {
        return folderRepository.findDeletedFoldersByUserId(userId);
    }
    
    /**
     * Move folder to specified parent folder
     */
    public Folder moveFolder(Long folderId, Long targetParentId, String userId) {
        Optional<Folder> folderOptional = folderRepository.findById(folderId);
        if (folderOptional.isPresent() && folderOptional.get().getUserId().equals(userId)) {
            Folder folder = folderOptional.get();
            folder.setParentId(targetParentId);
            
            // Rebuild path
            String newPath = "/";
            if (targetParentId != null && targetParentId > 0) {
                Optional<Folder> parentFolder = folderRepository.findById(targetParentId);
                if (parentFolder.isPresent()) {
                    newPath = parentFolder.get().getPath() + folder.getName() + "/";
                }
            } else {
                newPath = "/" + folder.getName() + "/";
            }
            folder.setPath(newPath);
            
            return folderRepository.save(folder);
        }
        return null;
    }
    
    /**
     * Copy folder to specified parent folder
     */
    public Folder copyFolder(Long folderId, Long targetParentId, String userId) {
        Optional<Folder> folderOptional = folderRepository.findById(folderId);
        if (folderOptional.isPresent() && folderOptional.get().getUserId().equals(userId)) {
            Folder originalFolder = folderOptional.get();
            
            // Create new folder record
            Folder newFolder = new Folder();
            newFolder.setName(originalFolder.getName());
            newFolder.setUserId(userId);
            newFolder.setParentId(targetParentId);
            
            // Build new path
            String newPath = "/";
            if (targetParentId != null && targetParentId > 0) {
                Optional<Folder> parentFolder = folderRepository.findById(targetParentId);
                if (parentFolder.isPresent()) {
                    newPath = parentFolder.get().getPath() + originalFolder.getName() + "/";
                }
            } else {
                newPath = "/" + originalFolder.getName() + "/";
            }
            newFolder.setPath(newPath);
            
            return folderRepository.save(newFolder);
        }
        return null;
    }

    /**
     * 获取文件夹总数
     */
    public long getFolderCount() {
        return folderRepository.count();
    }
}
