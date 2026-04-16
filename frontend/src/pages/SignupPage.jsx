import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';

function SignupPage() {
  const navigate = useNavigate();
  const { register } = useAuth();
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    confirmPassword: '',
  });
  const [errors, setErrors] = useState({});
  const [isLoading, setIsLoading] = useState(false);
  const [apiError, setApiError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  const validateForm = () => {
    const newErrors = {};
    
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
    
    if (!formData.confirmPassword) {
      newErrors.confirmPassword = 'Please confirm your password';
    } else if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = 'Passwords do not match';
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
      const result = await register(formData.name, formData.email, formData.password);
      
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
    <div className="min-h-screen bg-surface flex items-center justify-center p-8">
      <div className="w-full max-w-md">
        <div className="text-center mb-10">
          <h1 className="text-display-lg font-heading text-on-surface mb-3">Join GymPass</h1>
          <p className="text-lg font-body text-on-surface/70">Create your account to get started</p>
        </div>
        
        <form onSubmit={handleSubmit} className="flex flex-col gap-8">
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
          
          <Input
            label="Confirm Password"
            type="password"
            name="confirmPassword"
            value={formData.confirmPassword}
            onChange={handleChange}
            placeholder="Confirm your password"
            error={errors.confirmPassword}
            required
            autoComplete="new-password"
          />
          
          {apiError && (
            <div className="p-4 bg-error/10 border border-error/30 rounded-xl">
              <p className="text-sm font-body text-error">{apiError}</p>
            </div>
          )}
          
          {successMessage && (
            <div className="p-4 bg-primary-container/30 border border-primary-container rounded-xl">
              <p className="text-sm font-body text-primary">{successMessage}</p>
            </div>
          )}
          
          <Button
            type="submit"
            variant="primary"
            loading={isLoading}
            disabled={isLoading}
            className="w-full mt-2"
          >
            Create Account
          </Button>
        </form>
        
        <div className="mt-8 text-center">
          <p className="text-on-surface/70 font-body">
            Already have an account?{' '}
            <a href="/login" className="text-primary font-medium hover:underline">
              Sign in
            </a>
          </p>
        </div>
      </div>
    </div>
  );
}

export default SignupPage;