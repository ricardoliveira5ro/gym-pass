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
  const inputStyles = 'w-full px-4 py-3 sm:py-3.5 bg-white/[0.03] border border-white/[0.08] text-white placeholder:text-white/30 rounded-lg sm:rounded-xl outline-none transition-all duration-200 ease-out focus:border-primary/50 focus:bg-white/[0.06] focus:ring-2 focus:ring-primary/20';
  
  const errorStyles = error ? 'border-red-500/40 bg-red-500/[0.03]' : '';

  return (
    <div className={`flex flex-col gap-2 ${className}`}>
      {label && (
        <label htmlFor={name} className="text-sm font-medium text-white/60">
          {label}
          {required && <span className="text-red-400 ml-1">*</span>}
        </label>
      )}
      <input
        id={name}
        name={name}
        type={type}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        disabled={disabled}
        required={required}
        className={`${inputStyles} ${errorStyles} ${disabled ? 'opacity-50 cursor-not-allowed' : ''}`}
        {...props}
      />
      {error && (
        <span className="text-sm text-red-400">{error}</span>
      )}
    </div>
  );
}

export default Input;