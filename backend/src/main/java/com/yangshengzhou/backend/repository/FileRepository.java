package com.yangshengzhou.backend.repository;

import com.yangshengzhou.backend.entity.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    
    List<File> findByUserId(String userId);
    
    List<File> findByUserIdAndFolderId(String userId, Long folderId);
    
    List<File> findByUserIdAndNameContaining(String userId, String name);
    
    @Query("SELECT f FROM File f WHERE f.userId = :userId AND f.extension = :extension")
    List<File> findByUserIdAndExtension(@Param("userId") String userId, @Param("extension") String extension);
    
    @Query("SELECT f FROM File f WHERE f.userId = :userId AND f.folderId = :folderId ORDER BY f.name")
    List<File> findByUserIdAndFolderIdOrderByName(@Param("userId") String userId, @Param("folderId") Long folderId);
    
    @Query("SELECT SUM(f.size) FROM File f WHERE f.userId = :userId")
    Long sumSizeByUserId(@Param("userId") String userId);
    
    @Query("SELECT SUM(f.size) FROM File f")
    Long sumSize();
    
    boolean existsByUserIdAndStorageKey(String userId, String storageKey);
    
    Page<File> findByUserId(String userId, Pageable pageable);
    
    Page<File> findByUserIdAndFolderId(String userId, Long folderId, Pageable pageable);
    
    boolean existsByStorageKey(String storageKey);
}