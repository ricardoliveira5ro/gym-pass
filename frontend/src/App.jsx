import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import SignupPage from './pages/SignupPage';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <div className="min-h-screen bg-surface">
          <Routes>
            <Route path="/signup" element={<SignupPage />} />
            <Route path="/login" element={<Navigate to="/" replace />} />
            <Route path="/" element={<Navigate to="/signup" replace />} />
          </Routes>
        </div>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;