package com.yangshengzhou.backend.repository;

import com.yangshengzhou.backend.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShareRepository extends JpaRepository<Share, Long> {
    
    Optional<Share> findByShareCodeAndIsActiveTrue(String shareCode);
    
    List<Share> findByUserIdAndIsActiveTrue(String userId);
    
    List<Share> findByUserId(String userId);
    
    @Query("SELECT s FROM Share s WHERE s.userId = :userId AND s.isActive = true AND (s.expireTime IS NULL OR s.expireTime > :now)")
    List<Share> findActiveSharesByUserId(@Param("userId") String userId, @Param("now") LocalDateTime now);
    
    @Query("SELECT s FROM Share s WHERE s.shareCode = :shareCode AND s.isActive = true AND (s.expireTime IS NULL OR s.expireTime > :now)")
    Optional<Share> findValidShareByCode(@Param("shareCode") String shareCode, @Param("now") Date now);
    
    @Query("SELECT s FROM Share s WHERE s.userId = :userId AND s.fileId = :fileId AND s.isActive = true")
    List<Share> findByUserIdAndFileIdAndIsActiveTrue(@Param("userId") String userId, @Param("fileId") Long fileId);
    
    @Query("SELECT s FROM Share s WHERE s.userId = :userId AND s.folderId = :folderId AND s.isActive = true")
    List<Share> findByUserIdAndFolderIdAndIsActiveTrue(@Param("userId") String userId, @Param("folderId") Long folderId);
    
    @Query("SELECT s FROM Share s WHERE s.sharedTo = :userId AND s.isActive = true AND (s.expireTime IS NULL OR s.expireTime > :now)")
    List<Share> findSharedWithMe(@Param("userId") String userId, @Param("now") LocalDateTime now);
    
    boolean existsByShareCode(String shareCode);

    Optional<Share> findByShareCode(String shareCode);
}