package com.yangshengzhou.backend.repository;

import com.yangshengzhou.backend.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    
    List<File> findByUserId(Long userId);
    
    List<File> findByUserIdAndFolderId(Long userId, Long folderId);
    
    List<File> findByUserIdAndNameContaining(Long userId, String name);
    
    @Query("SELECT f FROM File f WHERE f.userId = :userId AND f.extension = :extension")
    List<File> findByUserIdAndExtension(@Param("userId") Long userId, @Param("extension") String extension);
    
    @Query("SELECT f FROM File f WHERE f.userId = :userId AND f.folderId = :folderId ORDER BY f.name")
    List<File> findByUserIdAndFolderIdOrderByName(@Param("userId") Long userId, @Param("folderId") Long folderId);
    
    @Query("SELECT SUM(f.size) FROM File f WHERE f.userId = :userId")
    Long sumSizeByUserId(@Param("userId") Long userId);
    
    boolean existsByUserIdAndStorageKey(Long userId, String storageKey);
}