import axios from 'axios';

// 공통 axios 인스턴스 생성
export const getBackendUrl = (): string => {
  const url = import.meta.env.VITE_BACKEND_URL || (window as any).BACKEND_URL || 'http://localhost:8080';
  return url;
};

// 개발 환경에서는 Vite 프록시 사용 (CORS 방지), 프로덕션에서는 직접 URL 사용
// 개발: 빈 문자열 → 상대 경로 → Vite 프록시가 localhost:8080으로 전달
// 프로덕션: 직접 백엔드 URL 사용
const baseURL = import.meta.env.DEV ? '' : getBackendUrl();

export const apiClient = axios.create({
  baseURL: baseURL,
  headers: {
    'Content-Type': 'application/json'
  },
  withCredentials: true // 세션 쿠키 포함
});

export const getBackendUrlForRedirect = (): string => {
  return getBackendUrl();
};

