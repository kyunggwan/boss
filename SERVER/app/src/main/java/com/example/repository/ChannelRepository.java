package com.example.repository;

import com.example.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {
    List<Channel> findByRaidRoomId(Long raidRoomId);
    
    @Query("SELECT c FROM Channel c WHERE c.raidRoom.id = :raidRoomId AND c.channelNumber = :channelNumber")
    Optional<Channel> findByRaidRoomIdAndChannelNumber(@Param("raidRoomId") Long raidRoomId, @Param("channelNumber") Integer channelNumber);
}

