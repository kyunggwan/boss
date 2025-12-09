// WebSocket 실시간 동기화 서비스

export interface WebSocketMessage {
  type: string;
  data: any;
}

class WebSocketService {
  private socket: WebSocket | null = null;
  private reconnectAttempts = 0;
  private maxReconnectAttempts = 5;
  private reconnectDelay = 3000;
  private listeners: Map<string, Set<(data: any) => void>> = new Map();

  /**
   * WebSocket 연결
   */
  connect(): void {
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const wsUrl = `${protocol}//${window.location.host}/ws`;
    
    try {
      this.socket = new WebSocket(wsUrl);
      
      this.socket.onopen = () => {
        console.log('WebSocket 연결 성공');
        this.reconnectAttempts = 0;
        // 보스 목록 구독
        this.subscribe('/topic/bosses/today');
      };
      
      this.socket.onmessage = (event) => {
        try {
          const message = JSON.parse(event.data);
          this.handleMessage(message);
        } catch (e) {
          console.error('WebSocket 메시지 파싱 오류:', e);
        }
      };
      
      this.socket.onerror = (error) => {
        console.error('WebSocket 오류:', error);
      };
      
      this.socket.onclose = () => {
        console.log('WebSocket 연결 종료');
        this.attemptReconnect();
      };
    } catch (error) {
      console.error('WebSocket 연결 실패:', error);
      this.attemptReconnect();
    }
  }

  /**
   * 재연결 시도
   */
  private attemptReconnect(): void {
    if (this.reconnectAttempts < this.maxReconnectAttempts) {
      this.reconnectAttempts++;
      console.log(`WebSocket 재연결 시도 ${this.reconnectAttempts}/${this.maxReconnectAttempts}`);
      setTimeout(() => this.connect(), this.reconnectDelay);
    } else {
      console.error('WebSocket 재연결 실패 - 최대 시도 횟수 초과');
    }
  }

  /**
   * 메시지 처리
   */
  private handleMessage(message: any): void {
    // STOMP 프로토콜 메시지 처리
    if (message.destination) {
      const destination = message.destination;
      const listeners = this.listeners.get(destination);
      if (listeners) {
        listeners.forEach(listener => {
          try {
            listener(message.body || message);
          } catch (e) {
            console.error('WebSocket 리스너 실행 오류:', e);
          }
        });
      }
    }
  }

  /**
   * 구독
   */
  subscribe(destination: string, callback: (data: any) => void): void {
    if (!this.listeners.has(destination)) {
      this.listeners.set(destination, new Set());
    }
    this.listeners.get(destination)!.add(callback);
  }

  /**
   * 구독 해제
   */
  unsubscribe(destination: string, callback: (data: any) => void): void {
    const listeners = this.listeners.get(destination);
    if (listeners) {
      listeners.delete(callback);
      if (listeners.size === 0) {
        this.listeners.delete(destination);
      }
    }
  }

  /**
   * 연결 종료
   */
  disconnect(): void {
    if (this.socket) {
      this.socket.close();
      this.socket = null;
    }
    this.listeners.clear();
  }
}

// 싱글톤 인스턴스
export const websocketService = new WebSocketService();

