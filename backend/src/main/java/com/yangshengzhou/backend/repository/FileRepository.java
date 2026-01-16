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
    
    @Query("SELECT f FROM File f WHERE f.userId = :userId AND f.isDeleted = false")
    List<File> findByUserId(@Param("userId") String userId);
    
    @Query("SELECT f FROM File f WHERE f.userId = :userId AND f.folderId = :folderId AND f.isDeleted = false")
    List<File> findByUserIdAndFolderId(@Param("userId") String userId, @Param("folderId") Long folderId);
    
    @Query("SELECT f FROM File f WHERE f.userId = :userId AND f.name LIKE %:name% AND f.isDeleted = false")
    List<File> findByUserIdAndNameContaining(@Param("userId") String userId, @Param("name") String name);
    
    @Query("SELECT f FROM File f WHERE f.userId = :userId AND f.folderId = :folderId AND f.name LIKE %:name% AND f.isDeleted = false")
    List<File> findByUserIdAndFolderIdAndNameContaining(@Param("userId") String userId, @Param("folderId") Long folderId, @Param("name") String name);
    
    @Query("SELECT f FROM File f WHERE f.userId = :userId AND f.extension = :extension AND f.isDeleted = false")
    List<File> findByUserIdAndExtension(@Param("userId") String userId, @Param("extension") String extension);
    
    @Query("SELECT f FROM File f WHERE f.userId = :userId AND f.folderId = :folderId AND f.isDeleted = false ORDER BY f.name")
    List<File> findByUserIdAndFolderIdOrderByName(@Param("userId") String userId, @Param("folderId") Long folderId);
    
    @Query("SELECT SUM(f.size) FROM File f WHERE f.userId = :userId")
    Long sumSizeByUserId(@Param("userId") String userId);
    
    @Query("SELECT SUM(f.size) FROM File f")
    Long sumSize();
    
    boolean existsByUserIdAndStorageKey(String userId, String storageKey);
    
    @Query("SELECT f FROM File f WHERE f.userId = :userId AND f.isDeleted = false")
    Page<File> findByUserId(@Param("userId") String userId, Pageable pageable);
    
    @Query("SELECT f FROM File f WHERE f.userId = :userId AND f.folderId = :folderId AND f.isDeleted = false")
    Page<File> findByUserIdAndFolderId(@Param("userId") String userId, @Param("folderId") Long folderId, Pageable pageable);
    
    boolean existsByStorageKey(String storageKey);
    
    /**
     * 获取用户的回收站文件列表
     */
    @Query("SELECT f FROM File f WHERE f.userId = :userId AND f.isDeleted = true")
    List<File> findDeletedFilesByUserId(@Param("userId") String userId);
    
    /**
     * 分页获取用户的回收站文件列表
     */
    @Query("SELECT f FROM File f WHERE f.userId = :userId AND f.isDeleted = true")
    Page<File> findDeletedFilesByUserId(@Param("userId") String userId, Pageable pageable);
}