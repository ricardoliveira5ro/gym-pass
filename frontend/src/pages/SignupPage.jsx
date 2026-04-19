import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';

function SignupPage() {
  const navigate = useNavigate();
  const { register } = useAuth();
  const [formData, setFormData] = useState({
    memberId: '',
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
    
    if (!formData.memberId.trim()) {
      newErrors.memberId = 'Member ID is required';
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
      const result = await register(formData.memberId, formData.name, formData.email, formData.password);
      
      if (result.success) {
        setSuccessMessage('Registration successful! Redirecting to login...');
        setTimeout(() => {
          navigate('/login', { state: { message: 'Registration successful! Please log in.' } });
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
      <div className="absolute inset-0 bg-gradient-to-b from-[#0d0d0d] via-[#111111] to-[#0a0a0a]" />
      
      <div className="absolute inset-0 hidden sm:block">
        <div className="absolute top-0 right-1/4 w-[600px] h-[600px] bg-primary/8 rounded-full blur-[180px]" />
        <div className="absolute bottom-0 left-1/4 w-[500px] h-[500px] bg-primary/5 rounded-full blur-[150px]" />
      </div>

      <div className="absolute inset-0 bg-[url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjAiIGhlaWdodD0iNjAiIHZpZXdCb3g9IjAgMCA2MCA2MCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48ZyBmaWxsPSJub25lIiBmaWxsLXJ1bGU9ImV2ZW5vZGQiPjxnIGZpbGw9IiMyMDIwMjAiIGZpbGwtb3BhY2l0eT0iMC4yIj48cGF0aCBkPSJNMzYgMzRoLTJ2LTRoMnYyaDR2MmgtdnptLTQtOGgydjJoLTJ2LTJ6bTAtOGgydjJoLTJ2LTJ6Ii8+PC9nPjwvZz48L3N2Zz4=')] opacity-20 sm:opacity-30" />

      <div className="relative min-h-screen flex flex-col items-center justify-start sm:justify-center pt-8 sm:pt-0 px-4">
        <div className="w-full max-w-sm sm:max-w-md">
          <div className="text-center mb-6 sm:mb-10">
            <div className="inline-flex items-center justify-center w-16 h-16 sm:w-20 sm:h-20 rounded-2xl bg-gradient-to-br from-primary to-[#8ab51a] mb-4 sm:mb-6 shadow-xl sm:shadow-2xl shadow-primary/30 ring-1 ring-primary/20">
              <svg className="w-8 h-8 sm:w-10 sm:h-10 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z" />
              </svg>
            </div>
            <h1 className="text-3xl sm:text-4xl lg:text-5xl font-heading font-bold text-white mb-2 sm:mb-3 tracking-tight">
              Join GymPass
            </h1>
            <p className="text-base sm:text-lg text-white/50">
              Create your account
            </p>
          </div>
          
          <div className="relative">
            <div className="absolute -inset-1 bg-gradient-to-r from-primary/30 via-primary/10 to-transparent rounded-2xl sm:rounded-3xl blur-xl opacity-50 hidden sm:block" />
            <div className="relative bg-[#0f0f0f]/90 sm:bg-[#0f0f0f]/80 backdrop-blur-xl sm:backdrop-blur-2xl rounded-2xl sm:rounded-3xl border border-white/8 p-5 sm:p-8 shadow-xl sm:shadow-2xl">
              <form onSubmit={handleSubmit} className="space-y-4 sm:space-y-5">
                <Input
                  label="Member ID"
                  name="memberId"
                  value={formData.memberId}
                  onChange={handleChange}
                  placeholder="Enter your member ID"
                  error={errors.memberId}
                  required
                />
                
                <Input
                  label="Full Name"
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  placeholder="Enter your full name"
                  error={errors.name}
                  required
                  autoComplete="name"
                />
                
                <Input
                  label="Email"
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  placeholder="Enter your email"
                  error={errors.email}
                  required
                  autoComplete="email"
                />
                
                <Input
                  label="Password"
                  type="password"
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
                  placeholder="Create a password"
                  error={errors.password}
                  required
                  autoComplete="new-password"
                />
                
                {apiError && (
                  <div className="p-3 sm:p-4 bg-red-500/10 border border-red-500/20 rounded-xl">
                    <p className="text-sm text-red-400">{apiError}</p>
                  </div>
                )}
                
                {successMessage && (
                  <div className="p-3 sm:p-4 bg-primary/10 border border-primary/20 rounded-xl">
                    <p className="text-sm text-primary font-medium">{successMessage}</p>
                  </div>
                )}
                
                <Button
                  type="submit"
                  variant="primary"
                  loading={isLoading}
                  disabled={isLoading}
                  className="w-full mt-1 sm:mt-2"
                >
                  Create Account
                </Button>
              </form>
            </div>
          </div>
          
          <div className="mt-6 sm:mt-8 text-center">
            <p className="text-white/50 text-sm sm:text-base">
              Already have an account?{' '}
              <Link to="/login" className="text-primary font-semibold hover:text-primary/80 transition-colors duration-200">
                Sign in
              </Link>
            </p>
          </div>

          <div className="mt-4 sm:mt-6 flex items-center justify-center gap-2 text-white/30">
            <div className="w-2 h-2 rounded-full bg-primary/50 animate-pulse" />
            <span className="text-xs">GymPass</span>
          </div>
        </div>
      </div>
    </div>
  );
}

export default SignupPage;