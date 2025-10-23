package com.yangshengzhou.backend.security;

import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("=== UserDetailsServiceImpl.loadUserByUsername 开始 ===");
        System.out.println("查找用户邮箱: " + email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("用户不存在: " + email);
                    return new UsernameNotFoundException("用户不存在: " + email);
                });

        System.out.println("找到用户: " + user.getEmail() + ", ID: " + user.getId());
        System.out.println("用户状态: " + user.getStatus() + ", 是否启用: " + user.isEnabled());
        System.out.println("用户角色: " + user.getRole());
        
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_" + (user.getRole() == 1 ? "ADMIN" : "USER"))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.isEnabled())
                .build();
                
        System.out.println("=== UserDetailsServiceImpl.loadUserByUsername 成功 ===");
        return userDetails;
    }
}