import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from './auth-context';

const ProtectedRoute: React.FC<{ element: JSX.Element;}> = ({ element }) => {
     const { isAuthenticated } = useAuth();
  
    if (!isAuthenticated) 
    return <Navigate to="/login" />;
  
    return element;
}

export default ProtectedRoute;