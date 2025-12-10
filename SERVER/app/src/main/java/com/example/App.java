package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableCaching
@org.springframework.scheduling.annotation.EnableScheduling
public class App {
    
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    @GetMapping("/status")
    public String status() {
        return "{\"status\": \"running\"}";
    }

    public static void main(String[] args) {
        // 환경 변수 디버깅
        logger.info("=== Environment Variables Check ===");
        logger.info("DATABASE_URL: {}", System.getenv("DATABASE_URL") != null ? System.getenv("DATABASE_URL") : "NOT SET");
        logger.info("DATABASE_USERNAME: {}", System.getenv("DATABASE_USERNAME") != null ? System.getenv("DATABASE_USERNAME") : "NOT SET");
        logger.info("DATABASE_PASSWORD: {}", System.getenv("DATABASE_PASSWORD") != null ? "***" : "NOT SET");
        logger.info("===================================");
        
        SpringApplication.run(App.class, args);
    }
}
