-- 기존 데이터의 version 컬럼 초기화
-- 이 스크립트는 기존 레코드에 version 컬럼이 없거나 null인 경우를 처리합니다.

-- channels 테이블의 version 컬럼이 없으면 추가하고, null인 경우 0으로 초기화
DO $$
BEGIN
    -- version 컬럼이 없으면 추가
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'channels' AND column_name = 'version'
    ) THEN
        ALTER TABLE channels ADD COLUMN version BIGINT DEFAULT 0;
    END IF;
    
    -- version이 null인 레코드를 0으로 초기화
    UPDATE channels SET version = 0 WHERE version IS NULL;
END $$;

-- raid_rooms 테이블의 version 컬럼이 없으면 추가하고, null인 경우 0으로 초기화
DO $$
BEGIN
    -- version 컬럼이 없으면 추가
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'raid_rooms' AND column_name = 'version'
    ) THEN
        ALTER TABLE raid_rooms ADD COLUMN version BIGINT DEFAULT 0;
    END IF;
    
    -- version이 null인 레코드를 0으로 초기화
    UPDATE raid_rooms SET version = 0 WHERE version IS NULL;
END $$;

