import { createContext, useContext, useState, useEffect } from 'react';

const translations = {
  en: {
    welcomeBack: 'Welcome Back',
    accessCredentials: 'Access your gym credentials',
    memberId: 'Member ID',
    enterMemberId: 'Enter your member ID',
    password: 'Password',
    enterPassword: 'Enter your password',
    signInBtn: 'Sign In',
    dontHaveAccount: "Don't have an account?",
    createOne: 'Create one',
    joinGympass: 'Join GymPass',
    createAccount: 'Create your member account',
    fullName: 'Full Name',
    johnDoe: 'John Doe',
    email: 'Email',
    youExample: 'you@example.com',
    minCharacters: 'Min. 6 characters',
    createAccountBtn: 'Create Account',
    alreadyHaveAccount: 'Already have an account?',
    signInLink: 'Sign in',
    errors: {
      required: 'This field is required',
      invalidEmail: 'Please enter a valid email address',
      minPassword: 'Password must be at least 6 characters',
      invalidCredentials: 'Invalid member ID or password',
      emailExists: 'Email already exists',
      memberRegistered: 'Member already registered',
      unexpectedError: 'An unexpected error occurred. Please try again.',
      registrationFailed: 'Registration failed. Please try again.',
      accountCreated: 'Account created successfully!',
      pleaseSignIn: 'Welcome! Please sign in to continue.',
      resourceNotFound: 'The requested resource was not found.',
      externalApiError: 'External service unavailable. Please try again later.',
    },
  },
  pt: {
    welcomeBack: 'Bem-vindo de Volta',
    accessCredentials: 'Acede às tuas credenciais',
    memberId: 'Número de Sócio',
    enterMemberId: 'Introduz o teu número de sócio',
    password: 'Palavra-passe',
    enterPassword: 'Introduz a tua palavra-passe',
    signInBtn: 'Entrar',
    dontHaveAccount: 'Não tens conta?',
    createOne: 'Cria uma',
    joinGympass: 'Junta-te ao GymPass',
    createAccount: 'Cria a tua conta de sócio',
    fullName: 'Nome Completo',
    johnDoe: 'João Silva',
    email: 'Email',
    youExample: 'tu@exemplo.com',
    minCharacters: 'Min. 6 caracteres',
    createAccountBtn: 'Criar Conta',
    alreadyHaveAccount: 'Já tens conta?',
    signInLink: 'Entra',
    errors: {
      required: 'Campo obrigatório',
      invalidEmail: 'Email inválido',
      minPassword: 'A palavra-passe deve ter pelo menos 6 caracteres',
      invalidCredentials: 'Credenciais inválidas',
      emailExists: 'Email já existe',
      memberRegistered: 'Membro já está registado',
      unexpectedError: 'Ocorreu um erro inesperado',
      registrationFailed: 'O registo falhou',
      accountCreated: 'Conta criada com sucesso!',
      pleaseSignIn: 'Bem-vindo! Por favor, entra para continuar.',
      resourceNotFound: 'O recurso solicitado não foi encontrado.',
      externalApiError: 'Serviço externo indisponível',
    },
  },
};

const LanguageContext = createContext();

const errorCodeMap = {
  INVALID_CREDENTIALS: 'errors.invalidCredentials',
  EMAIL_EXISTS: 'errors.emailExists',
  MEMBER_REGISTERED: 'errors.memberRegistered',
  VALIDATION_ERROR: 'errors.required',
  INTERNAL_ERROR: 'errors.unexpectedError',
  RESOURCE_NOT_FOUND: 'errors.resourceNotFound',
  EXTERNAL_API_ERROR: 'errors.externalApiError',
};

export function LanguageProvider({ children }) {
  const [language, setLanguage] = useState(() => {
    return localStorage.getItem('gympass-language') || 'en';
  });

  useEffect(() => {
    localStorage.setItem('gympass-language', language);
  }, [language]);

  const t = (key) => {
    const keys = key.split('.');
    let value = translations[language];
    for (const k of keys) {
      value = value?.[k];
    }
    return value || key;
  };

  const toggleLanguage = () => {
    setLanguage((prev) => (prev === 'en' ? 'pt' : 'en'));
  };

  const mapError = (code) => {
    const key = errorCodeMap[code];
    if (key) return t(key);
    return code;
  };

  return (
    <LanguageContext.Provider value={{ language, t, toggleLanguage, mapError }}>
      {children}
    </LanguageContext.Provider>
  );
}

export function useLanguage() {
  const context = useContext(LanguageContext);
  if (!context) {
    throw new Error('useLanguage must be used within a LanguageProvider');
  }
  return context;
}