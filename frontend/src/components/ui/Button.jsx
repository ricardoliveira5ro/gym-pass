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
  const baseStyles = 'inline-flex items-center justify-center px-6 py-3 sm:py-3.5 rounded-lg sm:rounded-xl font-heading font-semibold text-base transition-all duration-200 ease-out disabled:opacity-50 disabled:cursor-not-allowed active:scale-[0.98]';
  
  const variants = {
    primary: 'bg-gradient-to-r from-primary to-[#8ab51a] text-white shadow-lg shadow-primary/20 hover:shadow-xl hover:shadow-primary/30 hover:scale-[1.02] active:scale-[0.98]',
    secondary: 'bg-white/[0.05] border border-white/[0.12] text-white hover:bg-white/[0.1] hover:border-white/[0.2] active:scale-[0.98]',
    tertiary: 'bg-transparent text-white/70 hover:text-white hover:bg-white/[0.05] active:scale-[0.98]',
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