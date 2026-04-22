function Input({
  label,
  type = 'text',
  name,
  value,
  onChange,
  placeholder,
  error,
  required = false,
  disabled = false,
  className = '',
  ...props
}) {
  return (
    <div className={`flex flex-col gap-2 ${className}`}>
      {label && (
        <label 
          htmlFor={name} 
          className="text-xs font-semibold uppercase tracking-wider text-on-surface-muted"
        >
          {label}
          {required && <span className="text-primary ml-1">*</span>}
        </label>
      )}
      <div className="relative">
        <input
          id={name}
          name={name}
          type={type}
          value={value}
          onChange={onChange}
          placeholder={placeholder}
          disabled={disabled}
          required={required}
          className={`
            w-full px-4 py-4 
            bg-surface-elevated/50 
            border 
            rounded-xl 
            outline-none 
            transition-all 
            duration-300 
            ease-out
            placeholder:text-on-surface-muted/40
            text-on-surface
            font-body
            text-base
            input-focus-ring
            ${error 
              ? 'border-error/50 bg-error/5 focus:border-error/70 focus:ring-error/20' 
              : 'border-white/[0.06] hover:border-white/[0.12] focus:border-primary/50'
            }
            ${disabled ? 'opacity-40 cursor-not-allowed' : ''}
          `}
          {...props}
        />
        {error && (
          <div className="absolute right-3 top-1/2 -translate-y-1/2">
            <svg className="w-5 h-5 text-error/70" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
        )}
      </div>
      {error && (
        <span className="text-xs font-medium text-error mt-1 flex items-center gap-1">
          <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          {error}
        </span>
      )}
    </div>
  );
}

export default Input;