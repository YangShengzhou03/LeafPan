package com.yangshengzhou.backend.service;

import com.yangshengzhou.backend.entity.File;
import com.yangshengzhou.backend.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {
    
    @Autowired
    private FileRepository fileRepository;
    
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
     * 获取用户在指定文件夹下的文件
     */
    public List<File> getFilesByFolder(Long userId, Long folderId) {
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
    public boolean deleteFile(Long id) {
        if (fileRepository.existsById(id)) {
            fileRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * 检查文件是否属于用户
     */
    public boolean isFileOwnedByUser(Long fileId, Long userId) {
        Optional<File> file = fileRepository.findById(fileId);
        return file.isPresent() && file.get().getUserId().equals(userId);
    }
    
    /**
     * 根据文件名查找文件
     */
    public List<File> searchFilesByName(Long userId, String name) {
        return fileRepository.findByUserIdAndNameContaining(userId, name);
    }
}