package com.yangshengzhou.backend.service;

import com.yangshengzhou.backend.entity.File;
import com.yangshengzhou.backend.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class FileService {
    
    @Autowired
    private FileRepository fileRepository;
    
    /**
     * 上传文件
     */
    public File uploadFile(MultipartFile file, Long userId, Long folderId, String storageKey) {
        File fileEntity = new File();
        fileEntity.setName(file.getOriginalFilename());
        fileEntity.setOriginalName(file.getOriginalFilename());
        fileEntity.setUserId(userId);
        fileEntity.setFolderId(folderId);
        fileEntity.setSize(file.getSize());
        fileEntity.setStorageKey(storageKey);
        fileEntity.setMimeType(file.getContentType());
        
        // 获取文件扩展名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            fileEntity.setExtension(originalFilename.substring(originalFilename.lastIndexOf(".") + 1));
        }
        
        return fileRepository.save(fileEntity);
    }
    
    /**
     * 获取文件
     */
    public Optional<File> getFile(Long id) {
        return fileRepository.findById(id);
    }
    
    /**
     * 获取用户的文件列表
     */
    public List<File> getUserFiles(Long userId) {
        return fileRepository.findByUserId(userId);
    }
    
    /**
     * 获取用户指定文件夹下的文件列表
     */
    public List<File> getUserFilesByFolderId(Long userId, Long folderId) {
        return fileRepository.findByUserIdAndFolderId(userId, folderId);
    }
    
    /**
     * 更新文件信息
     */
    public File updateFile(Long id, String name) {
        Optional<File> fileOptional = fileRepository.findById(id);
        if (fileOptional.isPresent()) {
            File file = fileOptional.get();
            file.setName(name);
            return fileRepository.save(file);
        }
        return null;
    }
    
    /**
     * 删除文件
     */
    public boolean deleteFile(Long id, Long userId) {
        Optional<File> fileOptional = fileRepository.findById(id);
        if (fileOptional.isPresent() && fileOptional.get().getUserId().equals(userId)) {
            fileRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * 检查用户是否有权限访问文件
     */
    public boolean hasPermission(Long userId, Long fileId) {
        Optional<File> fileOptional = fileRepository.findById(fileId);
        return fileOptional.isPresent() && fileOptional.get().getUserId().equals(userId);
    }
    
    /**
     * 按名称搜索文件
     */
    public List<File> searchFilesByName(Long userId, String keyword) {
        return fileRepository.findByUserIdAndNameContaining(userId, keyword);
    }
    
    /**
     * 按扩展名获取文件
     */
    public List<File> getFilesByExtension(Long userId, String extension) {
        return fileRepository.findByUserIdAndExtension(userId, extension);
    }
    
    /**
     * 获取用户文件总大小
     */
    public Long getUserTotalFileSize(Long userId) {
        Long totalSize = fileRepository.sumSizeByUserId(userId);
        return totalSize != null ? totalSize : 0L;
    }
    
    /**
     * 检查文件存储键是否存在
     */
    public boolean existsByStorageKey(String storageKey) {
        return fileRepository.existsByStorageKey(storageKey);
    }
    
    /**
     * 检查用户是否有相同存储键的文件
     */
    public boolean existsByUserIdAndStorageKey(Long userId, String storageKey) {
        return fileRepository.existsByUserIdAndStorageKey(userId, storageKey);
    }
    
    /**
     * 分页获取用户的文件列表
     */
    public Page<File> getUserFiles(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return fileRepository.findByUserId(userId, pageable);
    }
    
    /**
     * 分页获取用户指定文件夹下的文件列表
     */
    public Page<File> getUserFilesByFolderId(Long userId, Long folderId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return fileRepository.findByUserIdAndFolderId(userId, folderId, pageable);
    }

    public Page<File> getAllFiles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return fileRepository.findAll(pageable);
    }
}