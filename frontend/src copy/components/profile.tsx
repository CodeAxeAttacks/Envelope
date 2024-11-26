import React, { MouseEventHandler, useEffect, useState } from 'react';
import { z } from 'zod';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { whoAmI } from '../api/user-client';
import axiosInstance from '../api/axios-instance';
import axios from 'axios';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useAuth } from '../security/auth-context';
import { Navigate, useNavigate } from 'react-router-dom';
import { defaultUser } from '../types/user';

interface UpdateData {
    email: string,
    firstName: string,
    lastName: string,
    // birthDate: string,
    phone: string,
};

interface BecomeInstructorData {
    description: string,
};

const updateSchema = z.object({
    firstName: z.string().min(1, { message: 'Имя обязательно' }),
    lastName: z.string().min(1, { message: 'Фамилия обязательна' }),
    email: z.string().email({ message: 'Некорректная электронная почта' }),
    phone: z.string().min(1, { message: 'Телефонный номер обязателен' }),
})

const becomeInstructorSchema = z.object({
    description: z.string().min(1, { message: 'Описание обязательно' }),
    // experience: z.number({invalid_type_error: 'Опыт должен быть числом'}).min(0, { message: 'Опыт не может быть отрицательным' })
})

const ProfileMenu: React.FC = () => {
    const [isMenuOpen, setIsMenuOpen] = useState(false);

    let [user, setUser] = useState(defaultUser);

    if (user == undefined)
        user = defaultUser;

    const { logout } = useAuth();
    const navigate = useNavigate();

    const {
        register: registerUpdate,
        handleSubmit: handleUpdate,
        formState: { errors: updateErrors },
        reset
    } = useForm<UpdateData>({
        resolver: zodResolver(updateSchema),
    });

    const {
        register: registerBecomeInstructor,
        handleSubmit: handleBecomeInstructor,
        formState: { errors: becomeInstructorErrors }
    } = useForm<BecomeInstructorData>({
        resolver: zodResolver(becomeInstructorSchema)
    });

    useEffect(() => {
        whoAmI()
            .then(response => {
                reset(response);
                setUser(response);
            })
            .catch(() => {
                toast.error('Произошла ошибка');
            });
    }, [])

    const toggleMenu = () => {
        setIsMenuOpen((prev) => !prev);
    };

    const exit = () => {
        logout();
        navigate('/login');
    };

    const onUpdate = async (data: UpdateData) => {
        const token = localStorage.getItem('custom-auth-token');

        try {
            const response = await axiosInstance.patch(
                '/user',
                {
                    firstName: data.firstName,
                    lastName: data.lastName,
                    phone: data.phone,
                },
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    },
                },
            );

            toast.success('Данные успешно обновлены');
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const status = error.response?.status;
                if (status === 403) {
                    toast.error('Пользователь не авторизован');
                    logout();
                    navigate('/login');
                } else {
                    toast.error('Нет ответа от сервера');
                }
            } else {
                toast.error('Произошла ошибка');
            }
        }
    }

    const onBecomeIstructor = async (data: BecomeInstructorData) => {
        const token = localStorage.getItem('custom-auth-token');

        try {
            const response = await axiosInstance.post(
                '/instructor',
                {
                    description: data.description,
                    experience: 10,
                },
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    },
                },
            );

            whoAmI()
                .then(response => {
                    reset(response);
                    setUser(response);
                })
                .catch(() => {
                    toast.error('Произошла ошибка');
                });

            toast.success('Данные успешно обновлены');
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const status = error.response?.status;
                if (status === 403) {
                    toast.error('Пользователь не авторизован');
                    logout();
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
        <>
            {/* Button to toggle the profile menu */}
            <button
                className="bg-orange-500 text-white py-2 px-4 rounded hover:bg-orange-600 transition"
                onClick={toggleMenu}
            >
                Профиль
            </button>

            {/* Off-canvas menu */}
            <div
                className={`fixed top-0 right-0 h-full w-80 bg-gray-800 text-white transform transition-transform ${isMenuOpen ? 'translate-x-0' : 'translate-x-full'
                    } shadow-lg`}
            >
                <div className="p-4 flex justify-between items-center border-b border-gray-700">
                    <h2 className="text-lg font-bold">Профиль</h2>
                    <button
                        className="text-gray-400 hover:text-gray-200"
                        onClick={toggleMenu}
                    >
                        ✕
                    </button>
                </div>

                {/* Profile form */}
                <form className="p-4 flex flex-col gap-4" onSubmit={handleUpdate(onUpdate)}>
                    <label>
                        Имя:
                        <input
                            type="text"
                            defaultValue={user.firstName}
                            {...registerUpdate('firstName')}
                            className="w-full p-2 mt-1 rounded bg-gray-700 text-white"
                        />
                    </label>
                    {updateErrors.firstName && <span className='text-red-500'>{updateErrors.firstName.message}</span>}
                    <label>
                        Фамилия:
                        <input
                            type="text"
                            defaultValue={user.lastName}
                            {...registerUpdate('lastName')}
                            className="w-full p-2 mt-1 rounded bg-gray-700 text-white"
                        />
                    </label>
                    {updateErrors.lastName && <span className='text-red-500'>{updateErrors.lastName.message}</span>}
                    <label>
                        Электронная почта:
                        <input
                            type="email"
                            value={user.email}
                            {...registerUpdate('email')}
                            className="w-full p-2 mt-1 rounded bg-gray-700 text-white"
                        />
                    </label>
                    {/* <label>
                        Дата рождения:
                        <input
                            type="date"
                            name="birthDate"
                            value={formData.birthDate}
                            onChange={handleChange}
                            className="w-full p-2 mt-1 rounded bg-gray-700 text-white"
                        />
                    </label> */}
                    <label>
                        Номер телефона:
                        <input
                            type="tel"
                            defaultValue={user.phone}
                            {...registerUpdate('phone')}
                            className="w-full p-2 mt-1 rounded bg-gray-700 text-white"
                        />
                    </label>
                    {updateErrors.phone && <span className='text-red-500'>{updateErrors.phone.message}</span>}

                    <button
                        type="submit"
                        className="bg-orange-500 text-white py-2 rounded hover:bg-orange-600 transition"
                    >
                        Сохранить
                    </button>
                </form>

                {(user.role === 'INSTRUCTOR' || user.role === 'INSTRUCTOR_AND_ADMINISTRATOR') ? (
                    <div className="p-4 flex flex-col gap-4">
                        <button
                            type='button'
                            className='bg-orange-500 text-white py-2 rounded hover:bg-orange-600 transition'
                            onClick={() => { navigate('/instructor/' + user.instructorId) }}
                        >
                            Мои услуги
                        </button>
                    </div>
                ) : (
                    <div className="p-4 flex flex-col gap-4">
                        <form onSubmit={handleBecomeInstructor(onBecomeIstructor)} className="flex flex-col gap-4">
                            <textarea
                                placeholder="Расскажите о своем опыте"
                                {...registerBecomeInstructor('description')}
                                rows={4}
                                className="p-3 rounded bg-gray-700 text-white placeholder-gray-400 focus:outline-none focus:ring-2"
                            ></textarea>
                            {becomeInstructorErrors.description && <span className='text-red-500'>{becomeInstructorErrors.description.message}</span>}
                            <button
                                type="submit"
                                className="bg-orange-500 text-white py-2 rounded hover:bg-orange-600 transition"
                            >
                                Отправить заявку
                            </button>
                        </form>
                    </div>
                )}

                <div className="p-4 flex flex-col gap-4">
                    <button
                        type='button'
                        className='bg-orange-500 text-white py-2 rounded hover:bg-orange-600 transition'
                        onClick={() => { navigate('/my-schools')}}
                    >
                        Мои автошколы
                    </button>
                </div>

                <div className='p-4 flex flex-col gap-4'>
                    <button
                        type="button"
                        className="bg-orange-500 text-white py-2 rounded hover:bg-orange-600 transition"
                        onClick={exit}
                    >
                        Выход
                    </button>
                </div>
            </div>

        </>
    );
};

export default ProfileMenu;


