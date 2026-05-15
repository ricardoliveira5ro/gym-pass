import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';

function DashboardPage() {
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  const { t, language, toggleLanguage } = useLanguage();
  const [activeTab, setActiveTab] = useState('home');

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const mockStats = {
    workoutsThisWeek: 4,
    totalCheckIns: 23,
    daysLeft: 12,
  };

  const mockActivity = [
    { id: 1, gym: 'FitZone Central', time: '2 hours ago', type: 'checkin' },
    { id: 2, gym: 'Power Gym', time: 'Yesterday', type: 'checkin' },
    { id: 3, gym: 'FitZone Central', time: '3 days ago', type: 'checkin' },
  ];

  return (
    <div className="min-h-screen relative overflow-hidden pb-24">
      <div className="absolute inset-0 bg-gradient-to-b from-[#080808] via-[#0a0a0a] to-[#050505]" />
      
      <div className="absolute inset-0">
        <div className="absolute top-0 left-1/4 w-[300px] h-[300px] bg-primary/5 rounded-full blur-[120px]" />
        <div className="absolute bottom-20 right-[-10%] w-[250px] h-[250px] bg-primary/3 rounded-full blur-[100px]" />
        <div className="absolute top-[30%] left-[-5%] w-[200px] h-[200px] bg-primary/[0.02] rounded-full blur-[80px]" />
      </div>

      <div className="absolute inset-0 opacity-[0.02]" style={{
        backgroundImage: `url("data:image/svg+xml,%3Csvg width='40' height='40' viewBox='0 0 40 40' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M20 20.5V18H0v-2h20v-2H0v-2h20v-2H0V8h20V6H0V4h20V2H0V0h22v20h2V0h2v20h2V0h2v20h2V0h2v20h2v2H22v2h2v20h-2v2h-2v2h2v2h-2v2h-2v2h2v2h-2v2h-2v2h2v2H22v-2h-2zm0 18.5v-2h-2v2h2zm0-4v-2h-2v2h2zm0-4v-2h-2v2h2zm0-4v-2h-2v2h2z' fill='%23ffffff' fill-opacity='1' fill-rule='evenodd'/%3E%3C/svg%3E")`,
      }} />

      <div className="relative z-10 px-4 pt-6">
        <div className="flex items-center justify-between mb-8">
          <div className="flex items-center gap-4">
            <div className="relative">
              <div className="w-14 h-14 rounded-2xl bg-gradient-to-br from-primary/20 to-primary/5 flex items-center justify-center border border-primary/20">
                <span className="text-2xl font-heading font-bold text-primary">
                  {user?.name?.charAt(0) || 'U'}
                </span>
              </div>
              <div className="absolute -bottom-1 -right-1 w-5 h-5 bg-primary rounded-full flex items-center justify-center">
                <svg className="w-3 h-3 text-[#080808]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2.5} d="M5 13l4 4L19 7" />
                </svg>
              </div>
            </div>
            <div>
              <p className="text-on-surface-muted text-sm">{t('welcome')}</p>
              <h1 className="text-xl font-heading font-bold text-on-surface">
                {user?.name || 'Member'}
              </h1>
            </div>
          </div>

          <div className="flex items-center gap-2">
            <button
              onClick={toggleLanguage}
              className="px-3 py-2 rounded-xl bg-surface-card/60 backdrop-blur-sm border border-white/[0.06] text-on-surface-muted hover:text-on-surface hover:border-white/10 transition-all duration-200 font-bold text-sm uppercase"
            >
              {language}
            </button>
            <button
              onClick={handleLogout}
              className="p-2.5 rounded-xl bg-surface-card/60 backdrop-blur-sm border border-white/[0.06] text-on-surface-muted hover:text-error hover:border-error/20 transition-all duration-200"
            >
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
              </svg>
            </button>
          </div>
        </div>

        <div className="relative mb-8 animate-fade-up" style={{ animationDelay: '100ms', animationFillMode: 'forwards', opacity: 0 }}>
          <div className="absolute -inset-1 bg-gradient-to-r from-primary/30 via-primary/10 to-transparent rounded-3xl blur-xl" />
          <button
            onClick={() => {}}
            className="relative w-full p-6 rounded-2xl bg-surface-card/90 backdrop-blur-2xl border border-white/[0.08] group overflow-hidden"
          >
            <div className="absolute inset-0 bg-gradient-to-r from-primary/5 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-500" />
            
            <div className="relative flex items-center justify-between">
              <div className="flex items-center gap-4">
                <div className="w-16 h-16 rounded-2xl bg-gradient-to-br from-primary to-primary/80 flex items-center justify-center shadow-lg group-hover:scale-105 transition-transform duration-300">
                  <svg className="w-8 h-8 text-[#080808]" fill="none" stroke="currentColor" viewBox="0 0 24 24" strokeWidth={1.8}>
                    <path strokeLinecap="round" strokeLinejoin="round" d="M12 4v1m6 11h2m-6 0h-2v4m0-11v3m0 0h.01M12 12h4.01M16 20h4M4 12h4m12 0h.01M5 8h2a1 1 0 001-1V5a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1zm12 0h2a1 1 0 001-1V5a1 1 0 00-1-1h-2a1 1 0 00-1 1v2a1 1 0 001 1zM5 20h2a1 1 0 001-1v-2a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1z" />
                  </svg>
                </div>
                <div className="text-left">
                  <h2 className="text-lg font-heading font-bold text-on-surface">{t('qrCode')}</h2>
                  <p className="text-sm text-on-surface-muted">{t('qrCodeDesc')}</p>
                </div>
              </div>
              <div className="w-10 h-10 rounded-xl bg-primary/10 flex items-center justify-center group-hover:bg-primary/20 transition-colors">
                <svg className="w-5 h-5 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                </svg>
              </div>
            </div>

            <div className="absolute bottom-0 left-0 right-0 h-1 bg-gradient-to-r from-primary via-primary/50 to-primary/20" />
          </button>
        </div>

        <div className="grid grid-cols-2 gap-4 mb-8">
          <div className="animate-fade-up" style={{ animationDelay: '200ms', animationFillMode: 'forwards', opacity: 0 }}>
            <div className="relative group">
              <div className="absolute -inset-0.5 bg-gradient-to-br from-primary/20 to-transparent rounded-2xl blur opacity-50 group-hover:opacity-70 transition-opacity" />
              <button
                onClick={() => {}}
                className="relative w-full p-5 rounded-xl bg-surface-card/80 backdrop-blur-xl border border-white/[0.06] hover:border-primary/20 transition-all duration-300"
              >
                <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-[#6366f1]/20 to-[#6366f1]/5 flex items-center justify-center mb-3 group-hover:scale-105 transition-transform">
                  <svg className="w-6 h-6 text-[#6366f1]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                  </svg>
                </div>
                <h3 className="text-base font-heading font-semibold text-on-surface">{t('insights')}</h3>
                <p className="text-xs text-on-surface-muted mt-1">{t('insightsDesc')}</p>
              </button>
            </div>
          </div>

          <div className="animate-fade-up" style={{ animationDelay: '300ms', animationFillMode: 'forwards', opacity: 0 }}>
            <div className="relative group">
              <div className="absolute -inset-0.5 bg-gradient-to-br from-orange-500/20 to-transparent rounded-2xl blur opacity-50 group-hover:opacity-70 transition-opacity" />
              <button
                onClick={() => {}}
                className="relative w-full p-5 rounded-xl bg-surface-card/80 backdrop-blur-xl border border-white/[0.06] hover:border-orange-500/20 transition-all duration-300"
              >
                <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-orange-500/20 to-orange-500/5 flex items-center justify-center mb-3 group-hover:scale-105 transition-transform">
                  <svg className="w-6 h-6 text-orange-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                  </svg>
                </div>
                <h3 className="text-base font-heading font-semibold text-on-surface">{t('gymLocations')}</h3>
                <p className="text-xs text-on-surface-muted mt-1">{t('findGyms')}</p>
              </button>
            </div>
          </div>
        </div>

        <div className="animate-fade-up mb-8" style={{ animationDelay: '400ms', animationFillMode: 'forwards', opacity: 0 }}>
          <div className="relative rounded-2xl bg-surface-card/80 backdrop-blur-xl border border-white/[0.06] p-5 overflow-hidden">
            <div className="absolute top-0 right-0 w-32 h-32 bg-primary/5 rounded-full blur-2xl" />
            
            <div className="relative flex items-center justify-between mb-4">
              <div>
                <p className="text-xs text-on-surface-muted uppercase tracking-wider mb-1">{t('membership')}</p>
                <div className="flex items-center gap-2">
                  <div className="w-2 h-2 rounded-full bg-primary animate-pulse" />
                  <span className="text-sm font-medium text-primary">{t('membershipActive')}</span>
                </div>
              </div>
              <div className="text-right">
                <p className="text-3xl font-heading font-bold text-on-surface">{mockStats.daysLeft}</p>
                <p className="text-xs text-on-surface-muted">{t('membershipDaysLeft')}</p>
              </div>
            </div>

            <div className="h-1.5 bg-white/[0.05] rounded-full overflow-hidden">
              <div 
                className="h-full bg-gradient-to-r from-primary to-primary/60 rounded-full transition-all duration-1000"
                style={{ width: `${(mockStats.daysLeft / 30) * 100}%` }}
              />
            </div>
          </div>
        </div>

        <div className="grid grid-cols-2 gap-4 mb-8 animate-fade-up" style={{ animationDelay: '500ms', animationFillMode: 'forwards', opacity: 0 }}>
          <div className="relative rounded-xl bg-surface-card/60 backdrop-blur-xl border border-white/[0.04] p-4">
            <div className="flex items-center gap-3 mb-2">
              <div className="w-8 h-8 rounded-lg bg-primary/10 flex items-center justify-center">
                <svg className="w-4 h-4 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M13 10V3L4 14h7v7l9-11h-7z" />
                </svg>
              </div>
              <span className="text-xs text-on-surface-muted">{t('workoutsThisWeekLabel')}</span>
            </div>
            <p className="text-2xl font-heading font-bold text-on-surface">{mockStats.workoutsThisWeek}</p>
          </div>

          <div className="relative rounded-xl bg-surface-card/60 backdrop-blur-xl border border-white/[0.04] p-4">
            <div className="flex items-center gap-3 mb-2">
              <div className="w-8 h-8 rounded-lg bg-primary/10 flex items-center justify-center">
                <svg className="w-4 h-4 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </div>
              <span className="text-xs text-on-surface-muted">{t('totalCheckInsLabel')}</span>
            </div>
            <p className="text-2xl font-heading font-bold text-on-surface">{mockStats.totalCheckIns}</p>
          </div>
        </div>
      </div>

      <div className="fixed bottom-0 left-0 right-0 z-50 px-4 pb-4 pt-2 bg-gradient-to-t from-surface via-surface/95 to-transparent">
        <div className="flex items-center justify-around p-2 rounded-2xl bg-surface-card/90 backdrop-blur-2xl border border-white/[0.08]">
          <button 
            onClick={() => setActiveTab('home')}
            className={`flex flex-col items-center gap-1 px-4 py-2 rounded-xl transition-all duration-200 ${activeTab === 'home' ? 'text-primary' : 'text-on-surface-muted hover:text-on-surface'}`}
          >
            <svg className="w-5 h-5" fill={activeTab === 'home' ? 'currentColor' : 'none'} stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
            </svg>
            <span className="text-xs font-medium">Home</span>
          </button>

          <button 
            onClick={() => {}}
            className="flex flex-col items-center gap-1 px-4 py-2 rounded-xl text-on-surface-muted hover:text-on-surface transition-all duration-200"
          >
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
            </svg>
            <span className="text-xs font-medium">{t('gyms')}</span>
          </button>

          <div className="relative -mt-6">
            <button 
              onClick={() => {}}
              className="w-14 h-14 rounded-2xl bg-gradient-to-br from-primary to-primary/80 flex items-center justify-center shadow-lg shadow-primary/30 hover:shadow-primary/50 hover:scale-105 transition-all duration-200"
            >
              <svg className="w-6 h-6 text-[#080808]" fill="none" stroke="currentColor" viewBox="0 0 24 24" strokeWidth={2}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M12 4v1m6 11h2m-6 0h-2v4m0-11v3m0 0h.01M12 12h4.01M16 20h4M4 12h4m12 0h.01M5 8h2a1 1 0 001-1V5a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1zm12 0h2a1 1 0 001-1V5a1 1 0 00-1-1h-2a1 1 0 00-1 1v2a1 1 0 001 1zM5 20h2a1 1 0 001-1v-2a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1z" />
              </svg>
            </button>
          </div>

          <button 
            onClick={() => {}}
            className="flex flex-col items-center gap-1 px-4 py-2 rounded-xl text-on-surface-muted hover:text-on-surface transition-all duration-200"
          >
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
            </svg>
            <span className="text-xs font-medium">{t('alerts')}</span>
          </button>

          <button 
            onClick={() => {}}
            className="flex flex-col items-center gap-1 px-4 py-2 rounded-xl text-on-surface-muted hover:text-on-surface transition-all duration-200"
          >
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
            <span className="text-xs font-medium">{t('account')}</span>
          </button>
        </div>
      </div>

      <style>{`
        @keyframes fadeUp {
          from { opacity: 0; transform: translateY(20px); }
          to { opacity: 1; transform: translateY(0); }
        }
        .animate-fade-up {
          animation: fadeUp 0.6s ease-out forwards;
        }
      `}</style>
    </div>
  );
}

export default DashboardPage;