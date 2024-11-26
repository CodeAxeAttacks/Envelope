// pages/MySchools.tsx
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast, ToastContainer } from 'react-toastify';
import axiosInstance from '../api/axios-instance';
import School, { SchoolData } from '../types/school';
import Header from '../components/header';
import Footer from '../components/footer';
import User, { defaultUser } from '../types/user';
import axios from 'axios';
import { z } from 'zod';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';

const schoolSchema = z.object({
    name: z.string().min(1, { message: 'Название обязательно' }).max(24, 'Слишком длинное название'),
    phoneNumber: z.string().min(1, { message: 'Телефон обязателен' }).max(24, 'Слишком длинный телефон'),
    email:  z.string().min(1, { message: 'Электронная почта обязательна' }).max(24, 'Слишком длинная электронная почта').email({message: 'Некорректная электронная почта'}),
    address: z.string().min(1, { message: 'Адрес обязателен' }).max(24, 'Слишком длинный адрес'),
    description: z.string().min(1, { message: 'Описание обязательно' }).max(24, 'Слишком длинное описание'),
})

const MySchools = () => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [user, setUser] = useState<User>(defaultUser);
    const [schools, setSchools] = useState<School[]>([]);

    const navigate = useNavigate();
    const token = localStorage.getItem('custom-auth-token');

    useEffect(() => {
        let userId = 0;

        const fetchData = async () => {
            try {
                const response = await axiosInstance.get(
                    '/user/whoami',
                    {
                        headers: {
                            'Authorization': 'Bearer ' + token,
                        }
                    },
                );
                userId = response.data.id;
                setUser(response.data);
            } catch (error) {
                if (axios.isAxiosError(error)) {
                    const status = error.response?.status;
                    if (status === 403) {
                        toast.error('Пользователь не авторизован');
                        navigate('/login');
                    } else {
                        toast.error('Нет ответа от сервера');
                    }
                } else {
                    toast.error('Произошла ошибка');
                }
            }

            try {
                const response = await axiosInstance.get(
                    '/user/' + userId + '/driving-school',
                    {
                        headers: {
                            'Authorization': 'Bearer ' + token,
                        }
                    },
                );
                setSchools(response.data);
            } catch (error) {
                if (axios.isAxiosError(error)) {
                    const status = error.response?.status;
                    if (status === 403) {
                        toast.error('Пользователь не авторизован');
                        navigate('/login');
                    } else if (status === 404) {
                        navigate('/');
                    } else {
                        toast.error('Нет ответа от сервера');
                    }
                } else {
                    toast.error('Произошла ошибка');
                }
            }

        };

        fetchData();
    }, [])

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm<SchoolData>({
        resolver: zodResolver(schoolSchema),
    });

    const onSchool = async (data: SchoolData) => {
        setIsModalOpen(false);

        try {
            const response = await axiosInstance.post(
                '/driving-school',
                data,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            schools.push(response.data);
            setSchools(schools);
            toast.success('Автошкола успешно добавлена')
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const status = error.response?.status;
                if (status === 400) {
                    toast.error('Автошкола с такой почтой уже зарегистрирована')
                } else if (status === 403) {
                    toast.error('Пользователь не авторизован');
                    navigate('/login');
                } else {
                    toast.error('Нет ответа от сервера');
                }
            } else {
                toast.error('Произошла ошибка');
            }
        }
    }
    
    const onDelete = async (id: number) => {
        try {
            const response = await axiosInstance.delete(
                '/driving-school/' + id,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            const newSchools = schools.filter((school) => {return school.id !== id});
            setSchools(newSchools);
            toast.success('Автошкола успешно удалена')
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const status = error.response?.status;
                if (status === 403) {
                    toast.error('Пользователь не авторизован');
                    navigate('/login');
                } else {
                    toast.error('Нет ответа от сервера');
                }
            } else {
                toast.error('Произошла ошибка');
            }
        }
    }

    return (
        <div className="min-h-screen bg-gray-900 text-white">
            <Header />

            {isModalOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
                    <div className="bg-gray-800 p-6 rounded-lg shadow-lg max-w-md w-full">
                        <h2 className="text-xl text-white font-semibold mb-4">Создать автошколу</h2>
                        <form className="space-y-4">
                            <div>
                                <label htmlFor="name" className="block text-gray-400">Название</label>
                                <input
                                    type="text"
                                    {...register('name')}                                                                            
                                    className="w-full px-4 py-2 rounded-lg bg-gray-700 text-white border border-gray-600 focus:outline-none focus:ring-2 focus:ring-orange-500"
                                />
                                {errors.name && <span className="text-red-500">{errors.name.message}</span>}
                            </div>
                            <div>
                                <label htmlFor="phoneNumber" className="block text-gray-400">Номер телефона</label>
                                <input
                                    type="text"
                                    {...register('phoneNumber')}
                                    className="w-full px-4 py-2 rounded-lg bg-gray-700 text-white border border-gray-600 focus:outline-none focus:ring-2 focus:ring-orange-500"
                                />
                                {errors.phoneNumber && <span className="text-red-500">{errors.phoneNumber.message}</span>}
                            </div>
                            <div>
                                <label htmlFor="email" className="block text-gray-400">Email</label>
                                <input
                                    type="email"
                                    {...register('email')}
                                    className="w-full px-4 py-2 rounded-lg bg-gray-700 text-white border border-gray-600 focus:outline-none focus:ring-2 focus:ring-orange-500"
                                />
                                {errors.email && <span className="text-red-500">{errors.email.message}</span>}
                                </div>
                            <div>
                                <label htmlFor="address" className="block text-gray-400">Адрес</label>
                                <input
                                    type="text"
                                    {...register('address')}
                                    className="w-full px-4 py-2 rounded-lg bg-gray-700 text-white border border-gray-600 focus:outline-none focus:ring-2 focus:ring-orange-500"
                                />
                                {errors.address && <span className="text-red-500">{errors.address.message}</span>}
                                </div>
                            <div>
                                <label htmlFor="description" className="block text-gray-400">Описание</label>
                                <textarea
                                    {...register('description')}
                                    className="w-full px-4 py-2 rounded-lg bg-gray-700 text-white border border-gray-600 focus:outline-none focus:ring-2 focus:ring-orange-500"
                                    rows={4}
                                />
                                {errors.description && <span className="text-red-500">{errors.description.message}</span>}
                                </div>
                            <div className="flex justify-end space-x-4">
                                <button
                                    type="button"
                                    onClick={() => setIsModalOpen(false)}
                                    className="px-4 py-2 rounded-lg bg-gray-600 text-white hover:bg-gray-700 transition"
                                >
                                    Отмена
                                </button>
                                <button
                                    type="submit"
                                    onClick={handleSubmit(onSchool)}
                                    className="px-6 py-2 rounded-lg bg-orange-500 text-white hover:bg-orange-600 transition disabled:opacity-50"
                                >
                                    Зарегистрировать
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}


            <div className="max-w-5xl mx-auto py-8 px-4">
                <div className='flex justify-between items-center mb-5'>
                    <h1 className="text-2xl text-white font-semibold">Мои автошколы</h1>
                    <button
                        type="submit"
                        onClick={() => {setIsModalOpen(true)}}
                        className="px-3 py-2 rounded-lg bg-orange-500 text-white hover:bg-orange-600 transition disabled:opacity-50"
                    >
                        Зарегистрировать автошколу
                    </button>
                </div>
                {schools.length > 0 ? (
                    <div className="space-y-4">
                        {schools.map((school) => (
                            <div
                                key={school.id}
                                className="bg-gray-800 p-6 rounded-lg shadow-lg hover:shadow-xl transition"
                            >
                                <div className="flex justify-between items-center">
                                    <div>
                                        <h2 className="text-lg text-white font-medium">{school.name}</h2>
                                        <p className="text-gray-400 text-sm">{school.address}</p>
                                    </div>
                                    <div>
                                        <button
                                            onClick={() => navigate(`/school/${school.id}`)}
                                            className="bg-orange-500 text-white px-4 py-2 rounded-lg hover:bg-orange-600 transition mr-1"
                                        >
                                            Подробнее
                                        </button>
                                        <button
                                            onClick={() => onDelete(school.id)}
                                            className='bg-orange-500 text-white px-4 py-2 rounded-lg hover:bg-orange-600 transition'
                                        >
                                            Удалить
                                        </button>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                ) : (
                    <p className="text-gray-400">Вы пока не являетесь администратором ни одной автошколы.</p>
                )}
            </div>

            <ToastContainer position="bottom-left" autoClose={3000} />
            <Footer />
        </div>
    );
};

export default MySchools;
