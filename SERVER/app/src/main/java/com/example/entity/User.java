package com.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Discord OAuth 사용자
    @Column(unique = true, nullable = true)
    private String discordId;
    
    private String username; // Discord username 또는 닉네임
    private String displayName; // 표시 이름
    
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.GENERAL; // 기본값: 일반 사용자
    
    // 확장성: 길드/문파 구분
    private String guildName; // 길드명 (선택사항)
    
    // Discord 정보 (OAuth 사용자만)
    private String email;
    private String avatarUrl;
    
    // 접속 기록
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    
    // 참가한 보스 레이드 기록
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<RaidParticipation> participations = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (lastLoginAt == null) {
            lastLoginAt = LocalDateTime.now();
        }
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getDiscordId() { return discordId; }
    public void setDiscordId(String discordId) { this.discordId = discordId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    
    public String getGuildName() { return guildName; }
    public void setGuildName(String guildName) { this.guildName = guildName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    
    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public List<RaidParticipation> getParticipations() { return participations; }
    public void setParticipations(List<RaidParticipation> participations) { this.participations = participations; }
}

