package com.example.util;

/**
 * 에러 메시지 상수
 */
public class ErrorMessages {
    
    // 공통
    public static final String NOT_FOUND = "찾을 수 없습니다";
    public static final String INVALID_INPUT = "잘못된 입력입니다";
    
    // 레이드 방
    public static final String RAID_ROOM_NOT_FOUND = "레이드 방을 찾을 수 없습니다";
    public static final String RAID_ROOM_FETCH_ERROR = "레이드 방 조회 중 오류가 발생했습니다";
    public static final String RAID_ROOM_CREATE_ERROR = "방 생성 중 오류가 발생했습니다";
    public static final String RAID_ROOM_DELETE_ERROR = "레이드 방 삭제 중 오류가 발생했습니다";
    public static final String RAID_ROOM_COMPLETE_ERROR = "레이드 완료 처리 중 오류가 발생했습니다";
    
    // 채널
    public static final String CHANNEL_NOT_FOUND = "채널을 찾을 수 없습니다";
    public static final String CHANNEL_NUMBER_REQUIRED = "채널 번호를 입력해주세요";
    public static final String CHANNEL_DELETE_ERROR = "채널 삭제 중 오류가 발생했습니다";
    public static final String CHANNEL_SELECT_ERROR = "채널 선택 중 오류가 발생했습니다";
    
    // 보스
    public static final String BOSS_LIST_FETCH_ERROR = "보스 목록 조회 중 오류가 발생했습니다";
    public static final String BOSS_TYPE_REQUIRED = "보스 종류를 선택해주세요";
    public static final String BOSS_TYPE_INVALID = "잘못된 보스 종류입니다. (DRAGON 또는 SKELETON_KING)";
    
    // 레이드 날짜/시간
    public static final String RAID_DATE_REQUIRED = "레이드 날짜를 선택해주세요";
    public static final String RAID_TIME_REQUIRED = "레이드 시간을 선택해주세요";
    
    // 사용자
    public static final String USER_ID_REQUIRED = "사용자 ID가 필요합니다";
    public static final String USER_NOT_FOUND = "사용자를 찾을 수 없습니다";
    
    // 보스 색상
    public static final String BOSS_COLOR_REQUIRED = "보스 타입과 색상이 필요합니다";
    
    private ErrorMessages() {
        // 유틸리티 클래스는 인스턴스화 불가
    }
}

