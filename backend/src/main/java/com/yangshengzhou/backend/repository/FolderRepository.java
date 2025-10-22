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
    
    List<Folder> findByUserIdAndIsDeletedFalse(Long userId);
    
    List<Folder> findByUserIdAndParentIdAndIsDeletedFalse(Long userId, Long parentId);
    
    Optional<Folder> findByUserIdAndNameAndParentIdAndIsDeletedFalse(Long userId, String name, Long parentId);
    
    @Query("SELECT f FROM Folder f WHERE f.userId = :userId AND f.path LIKE :pathPrefix AND f.isDeleted = false")
    List<Folder> findByUserIdAndPathStartingWith(@Param("userId") Long userId, @Param("pathPrefix") String pathPrefix);
    
    @Query("SELECT f FROM Folder f WHERE f.userId = :userId AND f.parentId = :parentId AND f.isDeleted = false ORDER BY f.name")
    List<Folder> findByUserIdAndParentIdOrderByName(@Param("userId") Long userId, @Param("parentId") Long parentId);
    
    List<Folder> findByUserId(Long userId);
    
    List<Folder> findByUserIdAndParentId(Long userId, Long parentId);
    
    boolean existsByUserIdAndName(Long userId, String name);
    
    boolean existsByUserIdAndNameAndIdNot(Long userId, String name, Long id);
}