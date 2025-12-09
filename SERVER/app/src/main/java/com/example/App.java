package com.example;

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

    @GetMapping("/status")
    public String status() {
        return "{\"status\": \"running\"}";
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
