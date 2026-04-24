import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';

function SignupPage() {
  const navigate = useNavigate();
  const { register } = useAuth();
  const { t, language, toggleLanguage } = useLanguage();
  const [formData, setFormData] = useState({
    externalId: '',
    name: '',
    email: '',
    password: '',
  });
  const [errors, setErrors] = useState({});
  const [isLoading, setIsLoading] = useState(false);
  const [apiError, setApiError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  const validateForm = () => {
    const newErrors = {};
    
    if (!formData.externalId.trim()) {
      newErrors.externalId = 'Member ID is required';
    }
    
    if (!formData.name.trim()) {
      newErrors.name = 'Name is required';
    }
    
    if (!formData.email.trim()) {
      newErrors.email = 'Email is required';
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = 'Please enter a valid email address';
    }
    
    if (!formData.password) {
      newErrors.password = 'Password is required';
    } else if (formData.password.length < 6) {
      newErrors.password = 'Password must be at least 6 characters';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: '' }));
    }
    setApiError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setApiError('');
    setSuccessMessage('');
    
    if (!validateForm()) {
      return;
    }
    
    setIsLoading(true);
    
    try {
      const result = await register(formData.externalId, formData.name, formData.email, formData.password);
      
      if (result.success) {
        setSuccessMessage('Account created successfully!');
        setTimeout(() => {
          navigate('/login', { state: { message: 'Welcome! Please sign in to continue.' } });
        }, 1500);
      } else {
        setApiError(result.error || 'Registration failed. Please try again.');
      }
    } catch (error) {
      setApiError('An unexpected error occurred. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen relative overflow-hidden">
      <div className="absolute inset-0 bg-gradient-to-b from-[#080808] via-[#0c0c0c] to-[#080808]" />
      
      <div className="absolute inset-0">
        <div className="absolute top-[-20%] right-[-10%] w-[500px] h-[500px] bg-primary/6 rounded-full blur-[150px]" />
        <div className="absolute bottom-[-10%] left-[-5%] w-[400px] h-[400px] bg-primary/4 rounded-full blur-[120px]" />
        <div className="absolute top-[40%] left-[20%] w-[300px] h-[300px] bg-primary/[0.02] rounded-full blur-[100px]" />
      </div>

      <div className="absolute inset-0 opacity-[0.015]" style={{
        backgroundImage: `url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='1'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E")`,
      }} />

      <div className="relative min-h-screen flex flex-col items-center justify-center px-5 py-6">
        <div className="absolute top-5 right-5 z-20">
          <button
            onClick={toggleLanguage}
            className="flex items-center gap-2 px-3 py-2 rounded-lg bg-surface-card/60 backdrop-blur-sm border border-white/10 text-on-surface-muted hover:text-on-surface hover:border-white/20 transition-all duration-200"
          >
            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M3 5h12M9 3v2m1.048 9.5A18.022 18.022 0 016.412 9m6.088 9h7M11 21l5-10 5 10M12.751 5C11.783 10.77 8.07 15.61 3 18.129" />
            </svg>
            <span className="text-sm font-medium uppercase">{language}</span>
          </button>
        </div>
        
        <div className="w-full max-w-[380px]">
          <div className="text-center mb-10">
            <div 
              className="inline-flex items-center justify-center w-20 h-20 rounded-2xl mb-6 animate-scale-in shadow-glow"
              style={{ background: 'linear-gradient(135deg, #a3c621 0%, #8ab418 100%)' }}
            >
              <svg className="w-10 h-10 text-[#080808]" fill="none" stroke="currentColor" viewBox="0 0 24 24" strokeWidth={1.8}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z" />
              </svg>
            </div>
            
            <h1 
              className="text-4xl font-heading font-bold text-on-surface mb-3 animate-fade-up opacity-0"
              style={{ animationFillMode: 'forwards' }}
            >
              {t('joinGympass')}
            </h1>
            
            <p 
              className="text-on-surface-muted text-base animate-fade-up opacity-0"
              style={{ animationDelay: '100ms', animationFillMode: 'forwards' }}
            >
              {t('createAccount')}
            </p>
          </div>
          
          <div 
            className="relative animate-fade-up opacity-0"
            style={{ animationDelay: '200ms', animationFillMode: 'forwards' }}
          >
            <div className="absolute -inset-0.5 bg-gradient-to-r from-primary/20 via-primary/5 to-transparent rounded-2xl blur-lg opacity-50" />
            
            <div className="relative bg-surface-card/80 backdrop-blur-2xl rounded-2xl border border-white/[0.06] p-7 shadow-card">
              <form onSubmit={handleSubmit} className="space-y-5">
                <div className="animate-fade-up opacity-0" style={{ animationDelay: '300ms', animationFillMode: 'forwards' }}>
                  <Input
                    label={t('memberId')}
                    name="externalId"
                    value={formData.externalId}
                    onChange={handleChange}
                    placeholder={t('enterMemberId')}
                    error={errors.externalId}
                    required
                  />
                </div>
                
                <div className="animate-fade-up opacity-0" style={{ animationDelay: '350ms', animationFillMode: 'forwards' }}>
                  <Input
                    label={t('fullName')}
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    placeholder={t('johnDoe')}
                    error={errors.name}
                    required
                    autoComplete="name"
                  />
                </div>
                
                <div className="animate-fade-up opacity-0" style={{ animationDelay: '400ms', animationFillMode: 'forwards' }}>
                  <Input
                    label={t('email')}
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    placeholder={t('youExample')}
                    error={errors.email}
                    required
                    autoComplete="email"
                  />
                </div>
                
                <div className="animate-fade-up opacity-0" style={{ animationDelay: '450ms', animationFillMode: 'forwards' }}>
                  <Input
                    label={t('password')}
                    type="password"
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                    placeholder={t('minCharacters')}
                    error={errors.password}
                    required
                    autoComplete="new-password"
                  />
                </div>
                
                {apiError && (
                  <div className="p-4 bg-error/10 border border-error/20 rounded-xl animate-fade-up">
                    <p className="text-sm text-error font-medium flex items-center gap-2">
                      <svg className="w-4 h-4 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                      </svg>
                      {apiError}
                    </p>
                  </div>
                )}
                
                {successMessage && (
                  <div className="p-4 bg-primary/10 border border-primary/20 rounded-xl animate-fade-up">
                    <p className="text-sm text-primary font-medium flex items-center gap-2">
                      <svg className="w-4 h-4 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                      </svg>
                      {successMessage}
                    </p>
                  </div>
                )}
                
                <div className="animate-fade-up opacity-0 pt-2" style={{ animationDelay: '500ms', animationFillMode: 'forwards' }}>
                  <Button
                    type="submit"
                    variant="primary"
                    loading={isLoading}
                    disabled={isLoading}
                    className="w-full"
                  >
                    {t('createAccountBtn')}
                  </Button>
                </div>
              </form>
            </div>
          </div>
          
          <div 
            className="mt-8 text-center animate-fade-up opacity-0"
            style={{ animationDelay: '600ms', animationFillMode: 'forwards' }}
          >
            <p className="text-on-surface-muted text-sm">
              {t('alreadyHaveAccount')}{' '}
              <Link 
                to="/login" 
                className="text-primary font-semibold hover:text-primary/80 transition-colors duration-200"
              >
                {t('signInLink')}
              </Link>
            </p>
          </div>

          <div 
            className="mt-8 flex items-center justify-center gap-2 text-on-surface-muted/40 animate-fade-up opacity-0"
            style={{ animationDelay: '700ms', animationFillMode: 'forwards' }}
          >
            <div className="w-1.5 h-1.5 rounded-full bg-primary animate-pulse" />
            <span className="text-xs font-medium tracking-widest uppercase">GymPass</span>
          </div>
        </div>
      </div>
    </div>
  );
}

export default SignupPage;