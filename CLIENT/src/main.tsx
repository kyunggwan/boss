import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import './index.css';

const backendUrl = import.meta.env.VITE_BACKEND_URL || (window as any).BACKEND_URL || 'http://localhost:8080';
(window as any).BACKEND_URL = backendUrl;

ReactDOM.createRoot(document.getElementById('app')!).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);

