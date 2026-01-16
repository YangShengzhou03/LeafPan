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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    /**
     * Upload file
     */
    public File uploadFile(MultipartFile file, String userId, Long folderId, String storageKey) {
        File fileEntity = new File();
        fileEntity.setName(file.getOriginalFilename());
        fileEntity.setOriginalName(file.getOriginalFilename());
        fileEntity.setUserId(userId);
        fileEntity.setFolderId(folderId);
        fileEntity.setSize(file.getSize());
        fileEntity.setStorageKey(storageKey);
        fileEntity.setMimeType(file.getContentType());

        // Get file extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            fileEntity.setExtension(originalFilename.substring(originalFilename.lastIndexOf(".") + 1));
        }

        return fileRepository.save(fileEntity);
    }

    /**
     * Get file
     */
    public Optional<File> getFile(Long id) {
        return fileRepository.findById(id);
    }

    /**
     * Get user's file list
     */
    public List<File> getUserFiles(String userId) {
        return fileRepository.findByUserId(userId);
    }

    /**
     * Get user's file list with pagination
     */
    public Page<File> getUserFiles(String userId, Pageable pageable) {
        return fileRepository.findByUserId(userId, pageable);
    }

    /**
     * Get user's file list with pagination
     */
    public Page<File> getUserFiles(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        return fileRepository.findByUserId(userId, pageable);
    }

    /**
     * Get user's file list in specified folder
     */
    public List<File> getUserFilesByFolderId(String userId, Long folderId) {
        return fileRepository.findByUserIdAndFolderId(userId, folderId);
    }

    /**
     * Get user's file list in specified folder with pagination
     */
    public Page<File> getUserFilesByFolderId(String userId, Long folderId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        return fileRepository.findByUserIdAndFolderId(userId, folderId, pageable);
    }

    /**
     * Update file information
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
     * Delete file (mark as deleted)
     */
    public boolean deleteFile(Long id, String userId) {
        Optional<File> fileOptional = fileRepository.findById(id);
        if (fileOptional.isPresent() && fileOptional.get().getUserId().equals(userId)) {
            File file = fileOptional.get();
            file.setIsDeleted(true);
            fileRepository.save(file);
            return true;
        }
        return false;
    }

    /**
     * Permanently delete file
     */
    public boolean permanentDeleteFile(Long id, String userId) {
        Optional<File> fileOptional = fileRepository.findById(id);
        if (fileOptional.isPresent() && fileOptional.get().getUserId().equals(userId)) {
            fileRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Get trash files
     */
    public List<File> getTrashFiles(String userId) {
        return fileRepository.findDeletedFilesByUserId(userId);
    }

    /**
     * Get trash files with pagination
     */
    public Page<File> getTrashFiles(String userId, Pageable pageable) {
        return fileRepository.findDeletedFilesByUserId(userId, pageable);
    }

    /**
     * Restore file
     */
    public boolean restoreFile(Long id, String userId) {
        Optional<File> fileOptional = fileRepository.findById(id);
        if (fileOptional.isPresent() && fileOptional.get().getUserId().equals(userId)) {
            File file = fileOptional.get();
            file.setIsDeleted(false);
            fileRepository.save(file);
            return true;
        }
        return false;
    }

    /**
     * Search files
     */
    public List<File> searchFiles(String userId, String keyword) {
        return fileRepository.findByUserIdAndNameContaining(userId, keyword);
    }

    /**
     * Search files in folder
     */
    public List<File> searchFilesInFolder(String userId, Long folderId, String keyword) {
        return fileRepository.findByUserIdAndFolderIdAndNameContaining(userId, folderId, keyword);
    }

    /**
     * Check if user has permission to access file
     */
    public boolean hasPermission(String userId, Long fileId) {
        Optional<File> fileOptional = fileRepository.findById(fileId);
        return fileOptional.isPresent() && fileOptional.get().getUserId().equals(userId);
    }

    /**
     * Get user's total file size
     */
    public Long getUserTotalFileSize(String userId) {
        Long totalSize = fileRepository.sumSizeByUserId(userId);
        return totalSize != null ? totalSize : 0L;
    }

    /**
     * Move file to specified folder
     */
    public File moveFile(Long fileId, Long targetFolderId, String userId) {
        Optional<File> fileOptional = fileRepository.findById(fileId);
        if (fileOptional.isPresent() && fileOptional.get().getUserId().equals(userId)) {
            File file = fileOptional.get();
            file.setFolderId(targetFolderId);
            return fileRepository.save(file);
        }
        return null;
    }

    /**
     * Copy file to specified folder
     */
    public File copyFile(Long fileId, Long targetFolderId, String userId) {
        Optional<File> fileOptional = fileRepository.findById(fileId);
        if (fileOptional.isPresent() && fileOptional.get().getUserId().equals(userId)) {
            File originalFile = fileOptional.get();
            
            File newFile = new File();
            newFile.setName(originalFile.getName());
            newFile.setOriginalName(originalFile.getOriginalName());
            newFile.setUserId(userId);
            newFile.setFolderId(targetFolderId);
            newFile.setSize(originalFile.getSize());
            newFile.setStorageKey(originalFile.getStorageKey());
            newFile.setMimeType(originalFile.getMimeType());
            newFile.setExtension(originalFile.getExtension());
            newFile.setMd5(originalFile.getMd5());
            
            return fileRepository.save(newFile);
        }
        return null;
    }

    /**
     * 获取文件总数
     */
    public long getFileCount() {
        return fileRepository.count();
    }

    /**
     * 获取所有文件占用的总存储空间
     */
    public long getTotalStorageUsed() {
        List<File> allFiles = fileRepository.findAll();
        return allFiles.stream()
                .filter(file -> !file.getIsDeleted())
                .mapToLong(File::getSize)
                .sum();
    }
}