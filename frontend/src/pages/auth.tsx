import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import { useAuth } from '../security/auth-context';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../api/axios-instance';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import Footer from '../components/footer';

interface LoginData {
    email: string;
    password: string;
}

interface RegisterData {
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    password: string;
    repeatPassword: string;
}

interface AuthResponse {
    token: string;
}

const loginSchema = z.object({
    email: z.string().email({ message: 'Некорректная электронная почта' }),
    password: z.string().min(1, { message: 'Пароль обязателен' }),
});

const registerSchema = z.object({
    firstName: z.string().min(1, { message: 'Имя обязательно' }),
    lastName: z.string().min(1, { message: 'Фамилия обязательна' }),
    email: z.string().email({ message: 'Некорректная электронная почта' }),
    phone: z.string().min(1, { message: 'Телефонный номер обязателен' }),
    password: z.string().min(1, { message: 'Пароль обязателен' }),
    repeatPassword: z.string().min(1, { message: 'Повторный пароль обязателен' })
});

function AuthPage() {
    const [isLogin, setIsLogin] = useState(true);

    const toggleMode = () => {
        setIsLogin(!isLogin);
    };

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm<RegisterData>({
        resolver: zodResolver(isLogin ? loginSchema : registerSchema),
    });

    const { login } = useAuth();
    const navigate = useNavigate();

    const onSubmit = async (data: LoginData | RegisterData) => {
        if (isLogin) {
            try {
                const response = await axiosInstance.post('/user/login', {
                    email: data.email,
                    password: data.password,
                });

                const body: AuthResponse = response.data;
                const token = body.token;

                login(token);

                toast.success('Вы успешно вошли!');
                navigate('/home');
            } catch (error) {
                if (axios.isAxiosError(error)) {
                    const status = error.response?.status;
                    if (status === 404) {
                        toast.error('Пользователь с такой почтой не найден');
                    } else if (status === 403) {
                        toast.error('Неверный пароль');
                    } else {
                        toast.error('Нет ответа от сервера');
                    }
                } else {
                    toast.error('Произошла ошибка');
                }
            }
        } else {
            const registerData = data as RegisterData;
            try {
                const response = await axiosInstance.post('/user/register', {
                    email: registerData.email,
                    password: registerData.password,
                    firstName: registerData.firstName,
                    lastName: registerData.lastName,
                    phone: registerData.phone,
                });

                const body: AuthResponse = response.data;
                const token = body.token;

                login(token);
                toast.success('Регистрация успешна!');
                navigate('/home');
            } catch (error) {
                if (axios.isAxiosError(error)) {
                    toast.error('Не удалось зарегистрироваться');
                } else {
                    toast.error('Произошла ошибка');
                }
            }
        }
    };

    return (
        <div>
            <div className="flex items-center justify-center min-h-screen bg-gray-900">
                <div className="bg-gray-800 p-8 rounded-lg shadow-lg w-96">
                    <h2 className="text-2xl font-bold text-white text-center mb-6">
                        {isLogin ? 'Вход' : 'Регистрация'}
                    </h2>
                    <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-4">
                        {!isLogin && (
                            <>
                                <input
                                    type="text"
                                    placeholder="Имя"
                                    {...register('firstName')}
                                    className="p-3 rounded bg-gray-700 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-orange-500"
                                />
                                {errors.firstName && <span className='text-red-500'>{errors.firstName.message}</span>}

                                <input
                                    type="text"
                                    placeholder="Фамилия"
                                    {...register('lastName')}
                                    className="p-3 rounded bg-gray-700 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-orange-500"
                                />
                                {errors.lastName && <span className='text-red-500'>{errors.lastName.message}</span>}
                            </>
                        )}
                        <input
                            type="text"
                            placeholder="Электронная почта"
                            {...register('email')}
                            className="p-3 rounded bg-gray-700 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-orange-500"
                        />
                        {errors.email && <span className='text-red-500'>{errors.email.message}</span>}

                        {!isLogin && (
                            <>
                                <input
                                    type="tel"
                                    placeholder="Номер телефона"
                                    {...register('phone')}
                                    className="p-3 rounded bg-gray-700 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-orange-500"
                                />
                                {errors.phone && <span className='text-red-500'>{errors.phone.message}</span>}
                            </>
                        )}

                        <input
                            type="password"
                            placeholder="Пароль"
                            {...register('password')}
                            className="p-3 rounded bg-gray-700 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-orange-500"
                        />
                        {errors.password && <span className='text-red-500'>{errors.password.message}</span>}

                        {!isLogin && (
                            <>
                                <input
                                    type="password"
                                    placeholder="Повторите пароль"
                                    {...register('repeatPassword')}
                                    className="p-3 rounded bg-gray-700 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-orange-500"
                                />
                                {errors.repeatPassword && <span className='text-red-500'>{errors.repeatPassword.message}</span>}
                            </>
                        )}

                        <button
                            type="submit"
                            className="bg-orange-500 text-white py-3 rounded font-bold hover:bg-orange-600 transition-colors"
                        >
                            {isLogin ? 'Войти' : 'Зарегистрироваться'}
                        </button>
                    </form>
                    <p className="text-gray-400 text-center mt-4">
                        {isLogin ? 'Нет аккаунта?' : 'Уже есть аккаунт?'}{' '}
                        <span
                            onClick={toggleMode}
                            className="text-orange-500 font-bold cursor-pointer hover:underline"
                        >
                            {isLogin ? 'Регистрация' : 'Войти'}
                        </span>
                    </p>
                </div>
                <ToastContainer position="bottom-left" autoClose={3000} />
            </div>
            <Footer />
        </div>
    );
}

export default AuthPage;
