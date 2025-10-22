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
    
    List<File> findByUserIdAndIsDeletedFalse(Long userId);
    
    List<File> findByUserIdAndFolderIdAndIsDeletedFalse(Long userId, Long folderId);
    
    Optional<File> findByUserIdAndStorageKeyAndIsDeletedFalse(Long userId, String storageKey);
    
    Optional<File> findByUserIdAndMd5AndIsDeletedFalse(Long userId, String md5);
    
    @Query("SELECT f FROM File f WHERE f.userId = :userId AND f.name LIKE %:name% AND f.isDeleted = false")
    List<File> findByUserIdAndNameContaining(@Param("userId") Long userId, @Param("name") String name);
    
    @Query("SELECT f FROM File f WHERE f.userId = :userId AND f.extension = :extension AND f.isDeleted = false")
    List<File> findByUserIdAndExtension(@Param("userId") Long userId, @Param("extension") String extension);
    
    @Query("SELECT f FROM File f WHERE f.userId = :userId AND f.folderId = :folderId AND f.isDeleted = false ORDER BY f.name")
    List<File> findByUserIdAndFolderIdOrderByName(@Param("userId") Long userId, @Param("folderId") Long folderId);
    
    @Query("SELECT SUM(f.size) FROM File f WHERE f.userId = :userId AND f.isDeleted = false")
    Long sumSizeByUserId(@Param("userId") Long userId);
    
    boolean existsByUserIdAndStorageKeyAndIsDeletedFalse(Long userId, String storageKey);
}