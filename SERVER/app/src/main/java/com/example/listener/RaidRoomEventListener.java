package com.example.listener;

import com.example.config.ApplicationContextProvider;
import com.example.entity.RaidRoom;
import com.example.service.RealtimeBossService;
import jakarta.persistence.*;
import org.springframework.context.ApplicationContext;

/**
 * RaidRoom 엔티티의 DB 변경 이벤트를 감지하여
 * 캐시 무효화 및 실시간 브로드캐스트를 수행하는 리스너
 * 
 * 동작 방식:
 * 1. DB에 Create/Update/Delete 발생
 * 2. JPA가 이벤트 리스너 호출
 * 3. 캐시 무효화 및 WebSocket 브로드캐스트
 * 4. 변경이 없으면 캐시된 데이터 재사용 (DB 조회 없음)
 */
public class RaidRoomEventListener {
    
    /**
     * RaidRoom 생성 후 호출
     */
    @PostPersist
    public void onPostPersist(RaidRoom room) {
        invalidateCacheAndBroadcast();
    }
    
    /**
     * RaidRoom 업데이트 후 호출
     */
    @PostUpdate
    public void onPostUpdate(RaidRoom room) {
        invalidateCacheAndBroadcast();
    }
    
    /**
     * RaidRoom 삭제 후 호출
     */
    @PostRemove
    public void onPostRemove(RaidRoom room) {
        invalidateCacheAndBroadcast();
    }
    
    /**
     * 캐시 무효화 및 브로드캐스트
     */
    private void invalidateCacheAndBroadcast() {
        try {
            ApplicationContext context = ApplicationContextProvider.getApplicationContext();
            if (context != null) {
                try {
                    RealtimeBossService realtimeService = context.getBean(RealtimeBossService.class);
                    if (realtimeService != null) {
                        realtimeService.broadcastBossListUpdate();
                    }
                } catch (Exception e) {
                    // Bean 조회 실패 시 무시 (초기화 중일 수 있음)
                }
            }
        } catch (Exception e) {
            // ApplicationContext가 아직 초기화되지 않았을 수 있음
        }
    }
}

