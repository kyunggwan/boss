package com.example.service;

import com.example.entity.User;
import com.example.entity.UserRole;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Value("${discord.client.id:}")
    private String discordClientId;
    
    @Value("${discord.client.secret:}")
    private String discordClientSecret;
    
    @Value("${discord.redirect.uri:http://localhost:8080/api/auth/discord/callback}")
    private String discordRedirectUri;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    /**
     * 게스트 로그인 (DB에 저장하여 채널 선택 등 기능 사용 가능)
     */
    public Map<String, Object> guestLogin(String nickname, HttpServletRequest request) {
        try {
            // 게스트 사용자도 DB에 저장 (discordId는 null)
            // 같은 닉네임의 게스트가 있으면 재사용 (discordId가 null인 경우만)
            Optional<User> existingGuest = userRepository.findByUsername(nickname)
                .filter(u -> u.getDiscordId() == null);
            
            User user;
            if (existingGuest.isPresent()) {
                // 기존 게스트 사용자 업데이트
                user = existingGuest.get();
                user.setLastLoginAt(LocalDateTime.now());
            } else {
                // 새 게스트 사용자 생성
                user = new User();
                user.setUsername(nickname);
                user.setDisplayName(nickname);
                user.setDiscordId(null); // 게스트는 Discord ID 없음
                user.setRole(UserRole.GENERAL); // 게스트도 일반 사용자 권한
                user.setCreatedAt(LocalDateTime.now());
                user.setLastLoginAt(LocalDateTime.now());
            }
            
            user = userRepository.save(user);
            
            // 세션에 사용자 정보 저장
            HttpSession session = request.getSession();
            session.setAttribute("userId", user.getId());
            session.setAttribute("userRole", user.getRole().name());
            session.setAttribute("isGuest", true);
            session.setAttribute("username", nickname);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId()); // DB ID 사용 (Long 타입)
            userData.put("username", user.getUsername());
            userData.put("displayName", user.getDisplayName());
            userData.put("role", user.getRole().name());
            userData.put("isGuest", true);
            response.put("user", userData);
            
            return response;
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "게스트 로그인 처리 중 오류: " + e.getMessage());
            return error;
        }
    }
    
    /**
     * Discord OAuth 인증 URL 생성
     */
    public String getDiscordAuthUrl() {
        if (discordClientId == null || discordClientId.isEmpty()) {
            throw new RuntimeException("Discord Client ID가 설정되지 않았습니다. application.properties에 discord.client.id를 설정해주세요.");
        }
        
        if (discordRedirectUri == null || discordRedirectUri.isEmpty()) {
            throw new RuntimeException("Discord Redirect URI가 설정되지 않았습니다. application.properties에 discord.redirect.uri를 설정해주세요.");
        }
        
        String scope = "identify email";
        String state = "random_state_" + System.currentTimeMillis(); // CSRF 방지용
        
        // redirect_uri는 URL 인코딩 필요
        String encodedRedirectUri = URLEncoder.encode(discordRedirectUri, StandardCharsets.UTF_8);
        String encodedScope = URLEncoder.encode(scope, StandardCharsets.UTF_8);
        
        String authUrl = String.format(
            "https://discord.com/api/oauth2/authorize?client_id=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s",
            discordClientId,
            encodedRedirectUri,
            encodedScope,
            state
        );
        
        return authUrl;
    }
    
    /**
     * Discord OAuth 로그인 처리
     */
    public Map<String, Object> discordLogin(String code, HttpServletRequest request) {
        try {
            // 1. 액세스 토큰 교환
            String accessToken = exchangeCodeForToken(code);
            if (accessToken == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "토큰 교환 실패");
                return error;
            }
            
            // 2. Discord API로 사용자 정보 가져오기
            Map<String, Object> discordUser = getUserInfoFromDiscord(accessToken);
            if (discordUser == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "사용자 정보 조회 실패");
                return error;
            }
            
            // 3. DB에 사용자 저장/업데이트
            String discordId = (String) discordUser.get("id");
            Optional<User> existingUser = userRepository.findByDiscordId(discordId);
            
            User user;
            if (existingUser.isPresent()) {
                user = existingUser.get();
                user.setLastLoginAt(LocalDateTime.now());
            } else {
                user = new User();
                user.setDiscordId(discordId);
                user.setCreatedAt(LocalDateTime.now());
                user.setLastLoginAt(LocalDateTime.now());
                user.setRole(UserRole.GENERAL); // 기본값: 일반 사용자
            }
            
            // Discord 정보 업데이트
            user.setUsername((String) discordUser.get("username"));
            user.setDisplayName((String) discordUser.getOrDefault("global_name", discordUser.get("username")));
            user.setEmail((String) discordUser.get("email"));
            
            String avatar = (String) discordUser.get("avatar");
            if (avatar != null) {
                user.setAvatarUrl(String.format("https://cdn.discordapp.com/avatars/%s/%s.png", discordId, avatar));
            }
            
            user = userRepository.save(user);
            
            // 4. 세션에 사용자 정보 저장
            HttpSession session = request.getSession();
            session.setAttribute("userId", user.getId());
            session.setAttribute("userRole", user.getRole().name());
            session.setAttribute("isGuest", false);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("username", user.getUsername());
            userData.put("displayName", user.getDisplayName());
            userData.put("role", user.getRole().name());
            userData.put("isGuest", false);
            response.put("user", userData);
            
            return response;
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "로그인 처리 중 오류: " + e.getMessage());
            return error;
        }
    }
    
    /**
     * 인증 코드를 액세스 토큰으로 교환
     */
    private String exchangeCodeForToken(String code) {
        try {
            String url = "https://discord.com/api/oauth2/token";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("client_id", discordClientId);
            body.add("client_secret", discordClientSecret);
            body.add("grant_type", "authorization_code");
            body.add("code", code);
            body.add("redirect_uri", discordRedirectUri);
            
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response = (ResponseEntity<Map<String, Object>>) (ResponseEntity<?>) restTemplate.postForEntity(url, request, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (String) response.getBody().get("access_token");
            }
            
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Discord API로 사용자 정보 가져오기
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getUserInfoFromDiscord(String accessToken) {
        try {
            String url = "https://discord.com/api/users/@me";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            
            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<Map<String, Object>> response = (ResponseEntity<Map<String, Object>>) (ResponseEntity<?>) restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
            
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 현재 사용자 정보 조회 (세션에서)
     */
    public Map<String, Object> getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        
        if (session == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("authenticated", false);
            return response;
        }
        
        Long userId = (Long) session.getAttribute("userId");
        Boolean isGuest = (Boolean) session.getAttribute("isGuest");
        
        if (userId == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("authenticated", false);
            return response;
        }
        
        if (isGuest != null && isGuest) {
            // 게스트 사용자
            Map<String, Object> response = new HashMap<>();
            response.put("authenticated", true);
            response.put("isGuest", true);
            response.put("id", session.getAttribute("userId"));
            response.put("username", session.getAttribute("username"));
            return response;
        }
        
        // DB에서 사용자 정보 조회
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("authenticated", false);
            return response;
        }
        
        User user = userOpt.get();
        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", true);
        response.put("isGuest", false);
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("username", user.getUsername());
        userData.put("displayName", user.getDisplayName());
        userData.put("role", user.getRole().name());
        userData.put("avatarUrl", user.getAvatarUrl());
        response.put("user", userData);
        
        return response;
    }
}

