package com.example.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bosses")
public class Boss {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private BossType type; // 해골왕, 용 등
    
    private String name; // 보스 이름
    private String description; // 보스 설명
    
    // 해당 보스의 레이드 방 목록
    @OneToMany(mappedBy = "boss", cascade = CascadeType.ALL)
    private List<RaidRoom> raidRooms = new ArrayList<>();
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public BossType getType() { return type; }
    public void setType(BossType type) { this.type = type; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public List<RaidRoom> getRaidRooms() { return raidRooms; }
    public void setRaidRooms(List<RaidRoom> raidRooms) { this.raidRooms = raidRooms; }
}

