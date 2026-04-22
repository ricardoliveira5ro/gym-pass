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
  const baseStyles = `
    relative overflow-hidden
    inline-flex items-center justify-center
    px-6 py-4 
    rounded-xl 
    font-heading font-bold text-sm uppercase tracking-wider
    transition-all duration-300 ease-out
    disabled:opacity-40 disabled:cursor-not-allowed disabled:transform-none
    active:scale-[0.98]
    btn-glow
  `;
  
  const variants = {
    primary: `
      bg-primary 
      text-surface
      shadow-lg shadow-primary/20
      hover:shadow-primary/40 hover:shadow-xl
      hover:scale-[1.02]
      before:absolute before:inset-0
      before:bg-gradient-to-r before:from-white/0 before:via-white/20 before:to-white/0
      before:translate-x-[-200%]
      hover:before:translate-x-[200%]
      before:transition-transform before:duration-700
    `,
    secondary: `
      bg-transparent 
      border border-white/[0.1]
      text-on-surface-muted
      hover:border-white/[0.25] hover:text-on-surface
      hover:bg-white/[0.03]
      active:scale-[0.98]
    `,
    tertiary: `
      bg-transparent 
      text-on-surface-muted
      hover:text-primary
      hover:bg-primary/5
      active:scale-[0.98]
    `,
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
      <span className={`relative flex items-center gap-2 ${loading ? 'opacity-0' : ''}`}>
        {children}
      </span>
      
      {loading && (
        <div className="absolute inset-0 flex items-center justify-center">
          <svg 
            className="animate-spin h-5 w-5 text-current" 
            xmlns="http://www.w3.org/2000/svg" 
            fill="none" 
            viewBox="0 0 24 24"
          >
            <circle 
              className="opacity-20" 
              cx="12" 
              cy="12" 
              r="10" 
              stroke="currentColor" 
              strokeWidth="3" 
            />
            <path 
              className="opacity-70" 
              fill="currentColor" 
              d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z" 
            />
          </svg>
        </div>
      )}
    </button>
  );
}

export default Button;