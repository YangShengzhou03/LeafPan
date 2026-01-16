package com.yangshengzhou.backend.service;

import com.yangshengzhou.backend.entity.Favorite;
import com.yangshengzhou.backend.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {
    
    @Autowired
    private FavoriteRepository favoriteRepository;
    
    public Favorite addFileFavorite(String userId, Long fileId) {
        if (favoriteRepository.existsByUserIdAndFileId(userId, fileId)) {
            throw new RuntimeException("文件已在收藏夹中");
        }
        
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setFileId(fileId);
        
        return favoriteRepository.save(favorite);
    }
    
    public Favorite addFolderFavorite(String userId, Long folderId) {
        if (favoriteRepository.existsByUserIdAndFolderId(userId, folderId)) {
            throw new RuntimeException("文件夹已在收藏夹中");
        }
        
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setFolderId(folderId);
        
        return favoriteRepository.save(favorite);
    }
    
    public void removeFileFavorite(String userId, Long fileId) {
        favoriteRepository.deleteByUserIdAndFileId(userId, fileId);
    }
    
    public void removeFolderFavorite(String userId, Long folderId) {
        favoriteRepository.deleteByUserIdAndFolderId(userId, folderId);
    }
    
    public List<Favorite> getUserFavorites(String userId) {
        return favoriteRepository.findByUserId(userId);
    }
    
    public List<Favorite> getFileFavorites(String userId) {
        return favoriteRepository.findFilesByUserId(userId);
    }
    
    public List<Favorite> getFolderFavorites(String userId) {
        return favoriteRepository.findFoldersByUserId(userId);
    }
    
    public boolean isFileFavorited(String userId, Long fileId) {
        return favoriteRepository.existsByUserIdAndFileId(userId, fileId);
    }
    
    public boolean isFolderFavorited(String userId, Long folderId) {
        return favoriteRepository.existsByUserIdAndFolderId(userId, folderId);
    }
}
