package com.example.repository;

import com.example.entity.RaidRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RaidRoomRepository extends JpaRepository<RaidRoom, Long> {
    List<RaidRoom> findByRaidDate(LocalDate date);
    
    // 오늘 이후의 모든 레이드 방 조회
    @Query("SELECT r FROM RaidRoom r WHERE r.raidDate >= :date ORDER BY r.raidDate ASC, r.raidTime ASC")
    List<RaidRoom> findByRaidDateGreaterThanEqual(@Param("date") LocalDate date);
    
    @Query("SELECT r FROM RaidRoom r WHERE r.raidDate = :date AND r.boss.id = :bossId")
    Optional<RaidRoom> findByRaidDateAndBossId(@Param("date") LocalDate date, @Param("bossId") Long bossId);
    
    // 완료되지 않은 방 조회
    @Query("SELECT r FROM RaidRoom r WHERE r.raidDate = :date AND r.boss.id = :bossId AND (r.isCompleted = false OR r.isCompleted IS NULL)")
    List<RaidRoom> findActiveByRaidDateAndBossId(@Param("date") LocalDate date, @Param("bossId") Long bossId);
    
    // 완료된 방 조회
    @Query("SELECT r FROM RaidRoom r WHERE r.isCompleted = true ORDER BY r.completedAt DESC")
    List<RaidRoom> findCompletedRooms();
}

