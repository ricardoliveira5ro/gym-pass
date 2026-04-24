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
  },
};

const LanguageContext = createContext();

export function LanguageProvider({ children }) {
  const [language, setLanguage] = useState(() => {
    return localStorage.getItem('gympass-language') || 'en';
  });

  useEffect(() => {
    localStorage.setItem('gympass-language', language);
  }, [language]);

  const t = (key) => {
    return translations[language][key] || key;
  };

  const toggleLanguage = () => {
    setLanguage((prev) => (prev === 'en' ? 'pt' : 'en'));
  };

  return (
    <LanguageContext.Provider value={{ language, t, toggleLanguage }}>
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