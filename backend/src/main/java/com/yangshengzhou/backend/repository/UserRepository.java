package com.yangshengzhou.backend.repository;

import com.yangshengzhou.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.status = 1")
    long countActiveUsers();
    
    @Query("SELECT u FROM User u WHERE u.status = :status")
    List<User> findByStatus(@Param("status") Byte status);
    
    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findByRole(@Param("role") Byte role);

    // 分页查询用户列表，使用JPA默认的findAll方法即可，无需自定义查询
}