package com.yangshengzhou.backend.repository;

import com.yangshengzhou.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :username")
    Optional<User> findByUsernameOrEmail(@Param("username") String username);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.status = 1")
    long countActiveUsers();
    
    @Query("SELECT u FROM User u WHERE u.status = :status")
    List<User> findByStatus(@Param("status") Integer status);
    
    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findByRole(@Param("role") String role);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.userId = :userId AND u.operationType = :operationType")
    Long countByUserIdAndOperationType(@Param("userId") Long userId, @Param("operationType") String operationType);
    
    @Query("SELECT u FROM User u WHERE u.operationTime BETWEEN :startTime AND :endTime")
    List<User> findByOperationTimeBetween(@Param("startTime") java.util.Date startTime, @Param("endTime") java.util.Date endTime);
    
    @Query("SELECT u FROM User u WHERE u.operationTime < :cutoffDate")
    List<User> findByOperationTimeBefore(@Param("cutoffDate") java.util.Date cutoffDate);
}