package com.example.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "raid_rooms")
@EntityListeners(com.example.listener.RaidRoomEventListener.class)
public class RaidRoom {
    
    /**
     * 동시성 제어를 위한 버전 필드 (낙관적 잠금)
     * 동시 업데이트 시 OptimisticLockException 발생하여 데이터 일관성 보장
     * 
     * 주의: 기존 데이터에 version 컬럼이 없거나 null인 경우를 위해 일시적으로 비활성화
     * 데이터 마이그레이션 후 다시 활성화 필요
     */
    // @Version
    // @Column(name = "version", nullable = true, columnDefinition = "BIGINT DEFAULT 0")
    // private Long version;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "boss_id")
    private Boss boss;
    
    @Column(name = "raid_date")
    private LocalDate raidDate; // 레이드 날짜
    
    @Column(name = "raid_time")
    private LocalTime raidTime; // 레이드 시간 (24시간 형식)
    
    @Column(name = "is_completed")
    private Boolean isCompleted = false; // 레이드 완료 여부
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt; // 완료 시간
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 해당 방의 채널 목록
    @OneToMany(mappedBy = "raidRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("channelNumber ASC")
    private List<Channel> channels = new ArrayList<>();
    
    // 참가자 목록
    @OneToMany(mappedBy = "raidRoom", cascade = CascadeType.ALL)
    private List<RaidParticipation> participations = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (raidDate == null) {
            raidDate = LocalDate.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Boss getBoss() { return boss; }
    public void setBoss(Boss boss) { this.boss = boss; }
    
    public LocalDate getRaidDate() { return raidDate; }
    public void setRaidDate(LocalDate raidDate) { this.raidDate = raidDate; }
    
    public LocalTime getRaidTime() { return raidTime; }
    public void setRaidTime(LocalTime raidTime) { this.raidTime = raidTime; }
    
    public Boolean getIsCompleted() { return isCompleted != null ? isCompleted : false; }
    public void setIsCompleted(Boolean isCompleted) { this.isCompleted = isCompleted; }
    
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<Channel> getChannels() { return channels; }
    public void setChannels(List<Channel> channels) { this.channels = channels; }
    
    public List<RaidParticipation> getParticipations() { return participations; }
    public void setParticipations(List<RaidParticipation> participations) { this.participations = participations; }
    
    // public Long getVersion() { return version; }
    // public void setVersion(Long version) { this.version = version; }
}

