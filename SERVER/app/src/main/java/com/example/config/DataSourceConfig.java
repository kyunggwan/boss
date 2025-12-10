package com.example.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DataSourceConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);
    
    @Value("${spring.datasource.url:NOT_SET}")
    private String datasourceUrl;
    
    @Value("${spring.datasource.username:NOT_SET}")
    private String datasourceUsername;
    
    @Value("${spring.datasource.password:NOT_SET}")
    private String datasourcePassword;
    
    @PostConstruct
    public void logDataSourceConfig() {
        logger.info("=== Database Configuration Debug (Before DB Connection) ===");
        logger.info("DATABASE_URL env: {}", System.getenv("DATABASE_URL") != null ? System.getenv("DATABASE_URL") : "NOT SET");
        logger.info("DATABASE_USERNAME env: {}", System.getenv("DATABASE_USERNAME") != null ? System.getenv("DATABASE_USERNAME") : "NOT SET");
        logger.info("DATABASE_PASSWORD env: {}", System.getenv("DATABASE_PASSWORD") != null ? "***" : "NOT SET");
        logger.info("spring.datasource.url: {}", datasourceUrl);
        logger.info("spring.datasource.username: {}", datasourceUsername);
        logger.info("spring.datasource.password: {}", datasourcePassword != null && !datasourcePassword.equals("NOT_SET") ? "***" : datasourcePassword);
        logger.info("==========================================================");
    }
}

