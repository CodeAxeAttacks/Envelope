import React, { useEffect, useState } from "react";
import Header from "../components/header";
import Footer from "../components/footer";
import { Vehicle, VehicleData, defaultVehicle } from "../types/vehicle";
import { Service, ServiceData, defaultService } from "../types/instructor-service";
import { defaultUser } from "../types/user";
import { whoAmI } from "../api/user-client";
import { useNavigate, useParams } from "react-router-dom";
import { addService, getInstructorById, getServicesByInstructorId } from "../api/instructor-client";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast, ToastContainer } from "react-toastify";
import { addVehicle, getVehiclesByInstructorId } from "../api/vehicle-client";
import { defaultInstructor } from "../types/instructor";
import axiosInstance from "../api/axios-instance";
import axios from "axios";
import InstructorReview, { InstructorReviewData } from "../types/instructor-review";

interface DescriptionData {
    description: string;
}

const descriptionSchema = z.object({
    description: z.string().min(1, { message: 'Описание обязательно' })
})

const serviceSchema = z.object({
    name: z.string().min(1, { message: 'Название обязательно' }),
    description: z.string().min(1, { message: 'Описание обязательно' }),
    price: z.preprocess((num) => parseInt(z.string().parse(num), 10), z.number({ message: 'Цена должна быть число' }).gte(0, { message: 'Цена не должна быть отрицательной' }))
})

const vehicleSchema = z.object({
    model: z.string().min(1, { message: 'Модель обязательна' }).max(24, 'Слишком длинная модель'),
    year: z.preprocess((num) => parseInt(z.string().parse(num), 10), z.number({ message: 'Год должен быть число' }).gte(1885, { message: 'Первый автомобиль произвели в 1885' })),
    transmission: z.string({ message: 'Трансмиссия обязательна' }).min(1, { message: 'Трансмиссия обязательна' }),
    category: z.string({ message: 'Категория обязательна' }).min(1, { message: 'Категория обязательна' }),
})

const reviewSchema = z.object({
    review: z.string().min(1, { message: 'Комментарий обязателен' }),
    rate: z.preprocess((num) => parseInt(z.string().parse(num), 10), z.number()),
})

const Instructor = () => {
    const [isCarModalOpen, setIsCarModalOpen] = useState(false);
    const [isServiceModalOpen, setIsServiceModalOpen] = useState(false);
    const [isEditingDescription, setIsEditingDescription] = useState(false);
    const [isSendReviewOpen, setIsSendReviewOpen] = useState(false);
    const [user, setUser] = useState(defaultUser);
    const [instructor, setInstructor] = useState(defaultInstructor);
    const [services, setServices] = useState([] as Service[]);
    const [vehicles, setVehicles] = useState([] as Vehicle[]);
    const [reviews, setReviews] = useState([] as InstructorReview[]);
    const [newCar, setNewCar] = useState(defaultVehicle);
    const [newService, setNewService] = useState(defaultService);

    const { id } = useParams<{ id: string }>();

    const navigate = useNavigate();
    const token = localStorage.getItem('custom-auth-token');

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axiosInstance.get(
                    '/instructor/' + id + '/service',
                    {
                        headers: {
                            'Authorization': 'Bearer ' + token,
                        }
                    },
                );
                setServices(response.data);
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

            try {
                const response = await axiosInstance.get(
                    '/instructor/' + id,
                    {
                        headers: {
                            'Authorization': 'Bearer ' + token,
                        }
                    },
                );
                setInstructor(response.data);
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
                    '/instructor/' + id + "/vehicle",
                    {
                        headers: {
                            'Authorization': 'Bearer ' + token,
                        }
                    },
                );
                setVehicles(response.data);
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
                    '/instructor/' + id + '/review',
                    {
                        headers: {
                            'Authorization': 'Bearer ' + token,
                        }
                    },
                );
                setReviews(response.data);
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
                    '/user/whoami',
                    {
                        headers: {
                            'Authorization': 'Bearer ' + token,
                        }
                    },
                );
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
        };

        fetchData();
    }, []);


    const {
        register: descriptionRegister,
        handleSubmit: handleDescriptionSubmit,
        formState: { errors: descriptionErrors }
    } = useForm<DescriptionData>({
        resolver: zodResolver(descriptionSchema)
    });

    const {
        register: serviceRegister,
        handleSubmit: handleServiceSubmit,
        formState: { errors: serviceErrors },
    } = useForm<ServiceData>({
        resolver: zodResolver(serviceSchema)
    });

    const {
        register: vehicleRegister,
        handleSubmit: handleVehicleSubmit,
        formState: { errors: vehicleErrors },
    } = useForm<VehicleData>({
        resolver: zodResolver(vehicleSchema)
    });

    const {
        register: reviewRegister,
        handleSubmit: handleReviewSubmit,
        formState: { errors: reviewErrors },
    } = useForm<InstructorReviewData>({
        resolver: zodResolver(reviewSchema),
    });

    const onDescription = async (data: DescriptionData) => {
        setIsEditingDescription(false);

        try {
            const response = await axiosInstance.patch(
                '/instructor',
                data,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            const newInstructor = response.data;
            setInstructor(newInstructor);
            toast.success('Данные успешно обновлены');
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const status = error.response?.status;
                if (status === 403) {
                    toast.error('Пользователь не авторизован');
                } else {
                    toast.error('Нет ответа от сервера');
                }
            } else {
                toast.error('Произошла ошибка');
            }
        }
    }

    const onService = async (data: ServiceData) => {
        setIsServiceModalOpen(false);

        try {
            const response = await axiosInstance.post(
                '/instructor/service',
                data,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            const newService: Service = response.data;
            services.push(newService);
            setServices(services);
            toast.success('Сервис добавлен успешно');
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const status = error.response?.status;
                if (status === 403) {
                    toast.error('Пользователь не авторизован');
                } else {
                    toast.error('Нет ответа от сервера');
                }
            } else {
                toast.error('Произошла ошибка');
            }
        }
    };

    const onVehicle = async (data: VehicleData) => {
        setIsCarModalOpen(false);

        if (data.transmission === 'Автоматическая')
            data.transmission = 'AUTOMATIC'
        else
            data.transmission = 'MECHANICAL'

        try {
            const response = await axiosInstance.post(
                '/instructor/vehicle',
                data,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            const newVehicle: Vehicle = response.data;
            vehicles.push(newVehicle);
            setVehicles(vehicles);
            toast.success('Транспортное средство добавлено успешно');
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
    };

    const onReview = async (data: InstructorReviewData) => {
        setIsSendReviewOpen(false);

        try {
            const response = await axiosInstance.post(
                '/instructor/' + id + '/review',
                data,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            const newReview: InstructorReview = response.data;
            reviews.push(newReview);
            setReviews(reviews);

            try {
                const response = await axiosInstance.get(
                    '/instructor/' + id,
                    {
                        headers: {
                            'Authorization': 'Bearer ' + token,
                        }
                    },
                );
                setInstructor(response.data);
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

            toast.success('Комментарий добавлен успешно');
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

    const onServiceDelete = async (id: number) => {
        try {
            await axiosInstance.delete(
                '/instructor/service/' + id,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            const newServices = services.filter((service) => { return service.id != id })
            setServices(newServices);
            toast.success('Услуга успешно удалена');
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

    const onVehicleDelete = async (id: number) => {

        try {
            await axiosInstance.delete(
                '/instructor/vehicle/' + id,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            const newVehicles = vehicles.filter((vehicle) => { return vehicle.id != id })
            setVehicles(newVehicles);
            toast.success('Транспортное средство успешно удалено');
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

            <div className="container mx-auto p-6">
                <div className="bg-gray-800 p-6 rounded-lg shadow-lg">
                    <h1 className="text-3xl font-bold mb-4">{instructor.user.lastName} {user.firstName}</h1>
                    <h2 className="text-2xl font-bold mb-4">Рейтинг: {instructor.rating}</h2>
                    {user.instructorId === instructor.id ?
                        isEditingDescription ? (
                            <div>
                                <textarea
                                    defaultValue={instructor.description}
                                    rows={3}
                                    {...descriptionRegister('description')}
                                    className="w-full p-3 rounded bg-gray-700 text-white placeholder-gray-400 mb-3"
                                />
                                {descriptionErrors.description && <span className="text-red-500">{descriptionErrors.description.message}</span>}
                                <div className="flex space-x-4">
                                    <button
                                        onClick={handleDescriptionSubmit(onDescription)}
                                        className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
                                    >
                                        Сохранить
                                    </button>
                                    <button
                                        onClick={() => setIsEditingDescription(false)}
                                        className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
                                    >
                                        Отмена
                                    </button>
                                </div>
                            </div>
                        ) : (
                            <div className="flex justify-between items-center">
                                <p className="text-gray-300 mb-6">{instructor.description}</p>
                                <button
                                    onClick={() => setIsEditingDescription(true)}
                                    className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                                >
                                    Редактировать
                                </button>
                            </div>
                        ) : <></>}
                    {user.instructorId !== instructor.id && (
                        <div className="flex justify-between items-center">
                            <p className="text-gray-300 mb-6">{instructor.description}</p>
                        </div>
                    )}
                </div>

                <div className="bg-gray-800 p-6 rounded-lg shadow-lg mt-6">
                    <h2 className="text-2xl font-bold mb-4">Услуги</h2>
                    <ul className="list-disc list-inside space-y-2">
                        {services.map((service: Service) => (
                            <li key={service.id} className="text-gray-300">
                                <strong>{service.name}</strong>: {service.description} - {service.price} ₽
                                {user.instructorId === instructor.id ? (
                                    <button
                                        onClick={() => { onServiceDelete(service.id) }}
                                        className="relative text-red-500 hover:text-red-700"
                                    >
                                        <svg
                                            xmlns="http://www.w3.org/2000/svg"
                                            fill="none"
                                            viewBox="0 0 24 24"
                                            strokeWidth={2}
                                            stroke="currentColor"
                                            className="w-6 h-6"
                                        >
                                            <path
                                                strokeLinecap="round"
                                                strokeLinejoin="round"
                                                d="M6 18L18 6M6 6l12 12"
                                            />
                                        </svg>
                                    </button>
                                ) : <></>}
                            </li>
                        ))}
                    </ul>
                    {user.instructorId === instructor.id ? (
                        <button
                            onClick={() => setIsServiceModalOpen(true)}
                            className="mt-4 px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600"
                        >
                            Добавить услугу
                        </button>
                    ) : <></>}
                </div>

                <div className="bg-gray-800 p-6 rounded-lg shadow-lg mt-6">
                    <h2 className="text-2xl font-bold mb-4">Автопарк</h2>
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                        {vehicles.map((vehicle: Vehicle) => (
                            <div
                                key={vehicle.id}
                                className="bg-gray-700 p-4 rounded-lg shadow-lg flex justify-between items-center  space-x-4"
                            >
                                <img
                                    alt={vehicle.model}
                                    className="w-24 h-16 rounded-md object-cover"
                                />
                                <div>
                                    <h3 className="text-lg font-bold">{vehicle.model}</h3>
                                    <p>Год выпуска: {vehicle.year}</p>
                                    <p>Коробка передач: {vehicle.transmission}</p>
                                    <p>Категория: {vehicle.category}</p>
                                </div>

                                {user.instructorId === instructor.id ? (
                                    <button
                                        onClick={() => { onVehicleDelete(vehicle.id) }}
                                        className="relative text-red-500 hover:text-red-700"
                                    >
                                        <svg
                                            xmlns="http://www.w3.org/2000/svg"
                                            fill="none"
                                            viewBox="0 0 24 24"
                                            strokeWidth={2}
                                            stroke="currentColor"
                                            className="w-6 h-6"
                                        >
                                            <path
                                                strokeLinecap="round"
                                                strokeLinejoin="round"
                                                d="M6 18L18 6M6 6l12 12"
                                            />
                                        </svg>
                                    </button>
                                ) : <></>}
                            </div>
                        ))}
                    </div>
                    {user.instructorId === instructor.id ? (
                        <button
                            onClick={() => setIsCarModalOpen(true)}
                            className="mt-4 px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600"
                        >
                            Добавить автомобиль
                        </button>
                    ) : <></>}
                </div>

                <div className="bg-gray-800 p-6 rounded-lg shadow-lg mt-6">
                    <h2 className="text-2xl font-bold mb-4">Отзывы</h2>
                    <button
                        onClick={() => setIsSendReviewOpen(true)}
                        className="mt-4 px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600"
                    >
                        Добавить отзыв
                    </button>
                    <div className="grid grid-cols-1 md:grid-cols-1 gap-4">
                        {reviews.map(review => (
                            <div key={review.id}>
                                <h3 className="text-lg font-bold">{review.user.lastName} {review.user.firstName}</h3>
                                <p>Оценка: {review.rate}</p>
                                <p>Отзыв: {review.review}</p>
                            </div>
                        ))}
                    </div>
                </div>

            </div>

            {isCarModalOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-gray-800 p-6 rounded-lg shadow-lg w-96">
                        <h2 className="text-xl font-bold mb-4 text-white">Добавить автомобиль</h2>
                        <input
                            type="text"
                            placeholder="Модель"
                            defaultValue={newCar.model}
                            {...vehicleRegister('model')}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {vehicleErrors.model && <span className='text-red-500'>{vehicleErrors.model.message}</span>}
                        <input
                            type="number"
                            placeholder="Год выпуска"
                            defaultValue={newCar.year}
                            {...vehicleRegister('year')}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {vehicleErrors.year && <span className='text-red-500'>{vehicleErrors.year.message}</span>}
                        <div className="mb-3">
                            <p className="text-white mb-2">Коробка передач:</p>
                            <div className="flex gap-4">
                                {["Автоматическая", "Механическая"].map((type) => (
                                    <label key={type} className="flex items-center text-white">
                                        <input
                                            type="radio"
                                            value={type}
                                            {...vehicleRegister('transmission')}
                                            className="mr-2"
                                        />
                                        {type}
                                    </label>
                                ))}
                            </div>
                        </div>
                        {vehicleErrors.transmission && <span className='text-red-500'>{vehicleErrors.transmission.message}</span>}
                        <div className="mb-3">
                            <p className="text-white mb-2">Категория:</p>
                            <div className="flex gap-4">
                                {["A", "B", "C", "D"].map((category) => (
                                    <label key={category} className="flex items-center text-white">
                                        <input
                                            type="radio"
                                            value={category}
                                            {...vehicleRegister('category')}
                                            className="mr-2"
                                        />
                                        {category}
                                    </label>
                                ))}
                            </div>
                        </div>
                        {vehicleErrors.category && <span className='text-red-500'>{vehicleErrors.category.message}</span>}
                        <div className="flex justify-end space-x-4">
                            <button
                                onClick={handleVehicleSubmit(onVehicle)}
                                className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
                            >
                                Добавить
                            </button>
                            <button
                                onClick={() => setIsCarModalOpen(false)}
                                className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
                            >
                                Отмена
                            </button>
                        </div>
                    </div>
                </div>

            )}

            {isServiceModalOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-gray-800 p-6 rounded-lg shadow-lg w-96">
                        <h2 className="text-xl font-bold mb-4 text-white">Добавить услугу</h2>
                        <input
                            type="text"
                            placeholder="Название услуги"
                            defaultValue={newService.name}
                            {...serviceRegister('name')}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {serviceErrors.name && <span className='text-red-500'>{serviceErrors.name.message}</span>}
                        <textarea
                            placeholder="Описание услуги"
                            defaultValue={newService.description}
                            {...serviceRegister('description')}
                            rows={3}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {serviceErrors.description && <span className='text-red-500'>{serviceErrors.description.message}</span>}
                        <input
                            type="number"
                            placeholder="Цена (₽)"
                            defaultValue={newService.price}
                            {...serviceRegister('price')}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {serviceErrors.price && <span className='text-red-500'>{serviceErrors.price.message}</span>}
                        <div className="flex justify-end space-x-4">
                            <button
                                onClick={handleServiceSubmit(onService)}
                                className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
                            >
                                Добавить
                            </button>
                            <button
                                onClick={() => setIsServiceModalOpen(false)}
                                className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
                            >
                                Отмена
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {isSendReviewOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-gray-800 p-6 rounded-lg shadow-lg w-96">
                        <h2 className="text-xl font-bold mb-4 text-white">Добавить отзыв</h2>
                        <textarea
                            {...reviewRegister('review')}
                            defaultValue=''
                            placeholder="Ваш комментарий..."
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {reviewErrors.review && <span className='text-red-500'>{reviewErrors.review.message}</span>}

                        <div className="mb-3">
                            <p className="text-white mb-2">Рейтинг:</p>
                            {[1, 2, 3, 4, 5].map(star => (
                                <label key={star} className="flex items-center text-yellow-400 cursor-pointer">
                                    <input
                                        type="radio"
                                        value={star}
                                        defaultChecked={star === 3}
                                        {...reviewRegister('rate')}
                                    />
                                    {'★'.repeat(star)}{' '}
                                </label>
                            ))}
                        </div>
                        {reviewErrors.rate && <span className='text-red-500'>{reviewErrors.rate.message}</span>}
                        <div className="flex justify-end space-x-4">
                            <button
                                onClick={handleReviewSubmit(onReview)}
                                className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
                            >
                                Добавить
                            </button>
                            <button
                                onClick={() => setIsSendReviewOpen(false)}
                                className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
                            >
                                Отмена
                            </button>
                        </div>
                    </div>
                </div>
            )}

            <ToastContainer position="bottom-left" autoClose={3000} />
            <Footer />
        </div>
    );
};

export default Instructor;
