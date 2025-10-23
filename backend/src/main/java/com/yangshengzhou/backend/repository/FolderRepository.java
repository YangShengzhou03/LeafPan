package com.yangshengzhou.backend.repository;

import com.yangshengzhou.backend.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    
    List<Folder> findByUserIdAndIsDeletedFalse(String userId);
    
    List<Folder> findByUserIdAndParentIdAndIsDeletedFalse(String userId, Long parentId);
    
    Optional<Folder> findByUserIdAndNameAndParentIdAndIsDeletedFalse(String userId, String name, Long parentId);
    
    @Query("SELECT f FROM Folder f WHERE f.userId = :userId AND f.path LIKE :pathPrefix AND f.isDeleted = false")
    List<Folder> findByUserIdAndPathStartingWith(@Param("userId") String userId, @Param("pathPrefix") String pathPrefix);
    
    @Query("SELECT f FROM Folder f WHERE f.userId = :userId AND f.parentId = :parentId AND f.isDeleted = false ORDER BY f.name")
    List<Folder> findByUserIdAndParentIdOrderByName(@Param("userId") String userId, @Param("parentId") Long parentId);
    
    List<Folder> findByUserId(String userId);
    
    List<Folder> findByUserIdAndParentId(String userId, Long parentId);
    
    List<Folder> findByParentId(Long parentId);
    
    boolean existsByUserIdAndName(String userId, String name);
    
    boolean existsByUserIdAndNameAndIdNot(String userId, String name, Long id);
    
    /**
     * 查找用户的根目录（parentId为0的文件夹）
     */
    @Query("SELECT f FROM Folder f WHERE f.userId = :userId AND f.parentId = :parentId AND f.isDeleted = false")
    Optional<Folder> findUserRootFolder(@Param("userId") String userId, @Param("parentId") Long parentId);
}