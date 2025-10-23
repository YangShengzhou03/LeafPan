package com.yangshengzhou.backend.repository;

import com.yangshengzhou.backend.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {
    
    List<OperationLog> findByUserIdOrderByCreatedTimeDesc(String userId);
    
    List<OperationLog> findByUserIdAndOperationTypeOrderByCreatedTimeDesc(String userId, String operationType);
    
    List<OperationLog> findByOperationTypeOrderByCreatedTimeDesc(String operationType);
    
    @Query("SELECT o FROM OperationLog o WHERE o.userId = :userId AND o.createdTime BETWEEN :startTime AND :endTime ORDER BY o.createdTime DESC")
    List<OperationLog> findByUserIdAndCreatedTimeBetween(@Param("userId") String userId, 
                                                        @Param("startTime") LocalDateTime startTime, 
                                                        @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT COUNT(o) FROM OperationLog o WHERE o.userId = :userId AND o.operationType = :operationType AND o.createdTime >= :since")
    Long countByUserIdAndOperationTypeAndCreatedTimeAfter(@Param("userId") String userId, 
                                                        @Param("operationType") String operationType, 
                                                        @Param("since") LocalDateTime since);
    
    List<OperationLog> findByUserId(String userId);
    
    List<OperationLog> findByUserIdAndOperationType(String userId, String operationType);
    
    org.springframework.data.domain.Page<OperationLog> findByUserId(String userId, org.springframework.data.domain.Pageable pageable);
    
    org.springframework.data.domain.Page<OperationLog> findByUserIdAndOperationType(String userId, String operationType, org.springframework.data.domain.Pageable pageable);
    
    org.springframework.data.domain.Page<OperationLog> findByOperationType(String operationType, org.springframework.data.domain.Pageable pageable);
    
    Long countByUserId(String userId);
    
    Long countByUserIdAndOperationType(String userId, String operationType);
    
    @Query("SELECT o FROM OperationLog o WHERE o.createdTime BETWEEN :startTime AND :endTime")
    List<OperationLog> findByCreatedTimeBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT o FROM OperationLog o WHERE o.createdTime < :cutoffDate")
    List<OperationLog> findByCreatedTimeBefore(@Param("cutoffDate") LocalDateTime cutoffDate);
}