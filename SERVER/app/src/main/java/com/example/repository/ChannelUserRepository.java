package com.example.repository;

import com.example.entity.ChannelUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelUserRepository extends JpaRepository<ChannelUser, Long> {
    List<ChannelUser> findByChannelId(Long channelId);
    
    @Query("SELECT cu FROM ChannelUser cu WHERE cu.channel.id = :channelId AND cu.user.id = :userId")
    Optional<ChannelUser> findByChannelIdAndUserId(@Param("channelId") Long channelId, @Param("userId") Long userId);
    
    @Query("SELECT cu FROM ChannelUser cu WHERE cu.user.id = :userId AND cu.channel.raidRoom.id = :roomId")
    List<ChannelUser> findByUserIdAndRoomId(@Param("userId") Long userId, @Param("roomId") Long roomId);
}

