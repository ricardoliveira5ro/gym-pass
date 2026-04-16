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
  const inputStyles = 'w-full px-4 py-3 bg-surface-container-high text-on-surface placeholder:text-on-surface/50 rounded-xl outline-none transition-all duration-200 focus:ring-2 focus:ring-primary/30';
  
  const errorStyles = error ? 'border-2 border-error' : '';

  return (
    <div className={`flex flex-col gap-2 ${className}`}>
      {label && (
        <label htmlFor={name} className="text-sm font-body font-medium text-on-surface">
          {label}
          {required && <span className="text-error ml-1">*</span>}
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
        <span className="text-sm font-body text-error">{error}</span>
      )}
    </div>
  );
}

export default Input;