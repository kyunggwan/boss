package com.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "raid_participations")
public class RaidParticipation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "raid_room_id")
    private RaidRoom raidRoom;
    
    private LocalDateTime participatedAt;
    
    @PrePersist
    protected void onCreate() {
        participatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public RaidRoom getRaidRoom() { return raidRoom; }
    public void setRaidRoom(RaidRoom raidRoom) { this.raidRoom = raidRoom; }
    
    public LocalDateTime getParticipatedAt() { return participatedAt; }
    public void setParticipatedAt(LocalDateTime participatedAt) { this.participatedAt = participatedAt; }
}

