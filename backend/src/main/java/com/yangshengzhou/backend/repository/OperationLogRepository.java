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
    
    List<OperationLog> findByUserIdOrderByCreatedTimeDesc(Long userId);
    
    List<OperationLog> findByUserIdAndOperationTypeOrderByCreatedTimeDesc(Long userId, String operationType);
    
    List<OperationLog> findByOperationTypeOrderByCreatedTimeDesc(String operationType);
    
    @Query("SELECT o FROM OperationLog o WHERE o.userId = :userId AND o.createdTime BETWEEN :startTime AND :endTime ORDER BY o.createdTime DESC")
    List<OperationLog> findByUserIdAndCreatedTimeBetween(@Param("userId") Long userId, 
                                                        @Param("startTime") LocalDateTime startTime, 
                                                        @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT COUNT(o) FROM OperationLog o WHERE o.userId = :userId AND o.operationType = :operationType AND o.createdTime >= :since")
    Long countByUserIdAndOperationTypeAndCreatedTimeAfter(@Param("userId") Long userId, 
                                                        @Param("operationType") String operationType, 
                                                        @Param("since") LocalDateTime since);
}