function Button({ 
  children, 
  variant = 'primary', 
  type = 'button',
  disabled = false,
  loading = false,
  className = '',
  onClick,
  ...props 
}) {
  const baseStyles = 'inline-flex items-center justify-center px-6 py-3 rounded-xl font-heading font-medium text-base transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed';
  
  const variants = {
    primary: 'bg-gradient-to-br from-primary to-primary-container text-white shadow-lg hover:shadow-xl hover:scale-[1.02] active:scale-[0.98]',
    secondary: 'bg-surface-container-high text-on-surface hover:bg-surface-container active:scale-[0.98]',
    tertiary: 'bg-transparent text-on-surface hover:bg-surface-container-low active:scale-[0.98]',
  };

  const variantStyles = variants[variant] || variants.primary;

  return (
    <button
      type={type}
      disabled={disabled || loading}
      onClick={onClick}
      className={`${baseStyles} ${variantStyles} ${className}`}
      {...props}
    >
      {loading && (
        <svg className="animate-spin -ml-1 mr-2 h-5 w-5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
          <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
          <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z" />
        </svg>
      )}
      {children}
    </button>
  );
}

export default Button;