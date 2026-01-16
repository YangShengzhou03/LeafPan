package com.yangshengzhou.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.yangshengzhou.backend.repository")
@EnableTransactionManagement
public class DatabaseConfig {
    // JPA配置类，启用JPA仓库和事务管理
}