package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 트랜잭션 설정
 * 동시성 제어를 위한 트랜잭션 격리 수준 설정
 * 
 * application.properties에서 설정:
 * spring.jpa.properties.hibernate.connection.isolation=2 (READ_COMMITTED)
 */
@Configuration
@EnableTransactionManagement
public class TransactionConfig {
    // Spring Boot의 기본 JpaTransactionManager 사용
    // 트랜잭션 격리 수준은 application.properties에서 설정
}

