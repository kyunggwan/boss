import React, { useState, FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { guestLogin, discordLogin } from '../services/AuthService';
import { User } from '../types';

interface LoginPageProps {
  onLogin: (user: User) => void;
}

const LoginPage: React.FC<LoginPageProps> = ({ onLogin }) => {
  const [nickname, setNickname] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  // URL 파라미터에서 에러 확인
  React.useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const errorParam = urlParams.get('error');
    if (errorParam) {
      setError(decodeURIComponent(errorParam));
      // URL에서 에러 파라미터 제거
      window.history.replaceState({}, '', '/login');
    }
  }, []);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    if (!nickname.trim()) {
      setError('닉네임을 입력해주세요');
      setLoading(false);
      return;
    }

    try {
      const data = await guestLogin(nickname);
      if (data.success) {
        onLogin(data.user);
        navigate('/');
      }
    } catch (err: unknown) {
      const error = err instanceof Error ? err.message : '로그인 실패';
      setError(error);
    } finally {
      setLoading(false);
    }
  };

  const handleDiscordLogin = () => {
    discordLogin();
  };

  return (
    <div className="login-container">
      <h1>🐉 보스 레이드</h1>
      <p className="subtitle">로그인하여 레이드에 참가하세요</p>

      <form className="login-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="nickname">닉네임</label>
          <input
            type="text"
            id="nickname"
            name="nickname"
            placeholder="닉네임을 입력하세요"
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
            required
          />
          {error && <div className="error-message">{error}</div>}
        </div>
        <button type="submit" className="btn btn-primary" disabled={loading}>
          {loading ? '로그인 중...' : '닉네임으로 시작'}
        </button>
      </form>

      <div className="divider">또는</div>

      <button className="btn btn-discord" onClick={handleDiscordLogin}>
        <span>Discord로 로그인</span>
      </button>
    </div>
  );
};

export default LoginPage;

