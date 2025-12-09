package com.example.util;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 공통 응답 생성 유틸리티
 */
public class ResponseUtil {
    
    /**
     * 성공 응답 생성
     */
    public static ResponseEntity<Map<String, Object>> success(Map<String, Object> data) {
        return ResponseEntity.ok(data);
    }
    
    /**
     * 성공 응답 생성 (데이터 없음)
     */
    public static ResponseEntity<Map<String, Object>> success() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 에러 응답 생성
     */
    public static ResponseEntity<Map<String, Object>> error(String message, HttpStatus status) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", message);
        return ResponseEntity.status(status).body(error);
    }
    
    /**
     * 에러 응답 생성 (400 Bad Request)
     */
    public static ResponseEntity<Map<String, Object>> badRequest(String message) {
        return error(message, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * 에러 응답 생성 (404 Not Found)
     */
    public static ResponseEntity<Map<String, Object>> notFound(String message) {
        return error(message, HttpStatus.NOT_FOUND);
    }
    
    /**
     * 에러 응답 생성 (500 Internal Server Error)
     */
    public static ResponseEntity<Map<String, Object>> internalError(String message) {
        return error(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    /**
     * 서비스 응답을 ResponseEntity로 변환
     * 서비스에서 반환한 Map에 "error" 키가 있으면 적절한 상태 코드로 변환
     */
    public static ResponseEntity<Map<String, Object>> fromServiceResponse(Map<String, Object> response) {
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        
        if (response.containsKey("error")) {
            String errorMessage = (String) response.get("error");
            if (errorMessage != null && errorMessage.contains("찾을 수 없습니다")) {
                return ResponseEntity.status(404).body(response);
            }
            return ResponseEntity.badRequest().body(response);
        }
        
        return ResponseEntity.ok(response);
    }
}

