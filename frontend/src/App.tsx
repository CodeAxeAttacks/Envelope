import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import AuthPage from './pages/auth';
import { AuthProvider } from './security/auth-context';
import ProtectedRoute from './security/protected-route';
import Home from './pages/home';
import Instructor from './pages/instructor';
import NotFound from './pages/not-found';
import SchoolPage from './pages/school';
import MySchools from './pages/my-schools';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path='/login' element={<AuthPage />} />
          <Route path='/home' element={
            <ProtectedRoute element={<Home />} />
          } />
          <Route path='/instructor/:id' element={
            <ProtectedRoute element={<Instructor />} />
          } />
          <Route path='/my-schools' element={
            <ProtectedRoute element={<MySchools />} />
          } />
          <Route path='/school/:id' element={
            <ProtectedRoute element={<SchoolPage />} />
          } />
          <Route path='*' element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
