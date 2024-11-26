import React, { createContext, useContext, useState } from 'react';

type AuthContextType = {
  isAuthenticated: boolean;
  login: (arg0: string) => void;
  logout: () => void;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const token = localStorage.getItem('custom-auth-token');
  
  const [isAuthenticated, setIsAuthenticated] = useState(token ? true : false);

  const login = (token: string) => {
    localStorage.setItem('custom-auth-token', token);
    setIsAuthenticated(true);
  }
  const logout = () => {
    localStorage.removeItem('custom-auth-token');
    setIsAuthenticated(false);
  }

  return (
    <AuthContext.Provider value={{ isAuthenticated, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};