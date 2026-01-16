package com.yangshengzhou.backend.repository;

import com.yangshengzhou.backend.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    
    @Query("SELECT f FROM Favorite f WHERE f.userId = :userId AND f.fileId = :fileId")
    Optional<Favorite> findByUserIdAndFileId(@Param("userId") String userId, @Param("fileId") Long fileId);
    
    @Query("SELECT f FROM Favorite f WHERE f.userId = :userId AND f.folderId = :folderId")
    Optional<Favorite> findByUserIdAndFolderId(@Param("userId") String userId, @Param("folderId") Long folderId);
    
    @Query("SELECT f FROM Favorite f WHERE f.userId = :userId")
    List<Favorite> findByUserId(@Param("userId") String userId);
    
    @Query("SELECT f FROM Favorite f WHERE f.userId = :userId AND f.fileId IS NOT NULL")
    List<Favorite> findFilesByUserId(@Param("userId") String userId);
    
    @Query("SELECT f FROM Favorite f WHERE f.userId = :userId AND f.folderId IS NOT NULL")
    List<Favorite> findFoldersByUserId(@Param("userId") String userId);
    
    boolean existsByUserIdAndFileId(String userId, Long fileId);
    
    boolean existsByUserIdAndFolderId(String userId, Long folderId);
    
    void deleteByUserIdAndFileId(String userId, Long fileId);
    
    void deleteByUserIdAndFolderId(String userId, Long folderId);
}
