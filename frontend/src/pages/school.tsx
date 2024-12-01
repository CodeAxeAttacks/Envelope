import React, { useEffect, useState } from "react";
import Header from "../components/header";
import Footer from "../components/footer";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast, ToastContainer } from "react-toastify";
import axiosInstance from "../api/axios-instance";
import { useNavigate, useParams } from "react-router-dom";


import School, { defaultSchool } from "../types/school"; // Импорт интерфейса автошколы
import { defaultUser } from "../types/user";
import axios from "axios";
import { defaultService, Service } from "../types/school-service";
import { defaultVehicle, Vehicle, VehicleData } from "../types/vehicle";
import { ServiceData } from "../types/instructor-service";

import defaultPhoto from '../assets/default-photo.png';
import Instructor from "../types/instructor";
import { Course, CourseData, defaultCourse } from "../types/course";


interface UpdateSchoolData {
    name: string;
    address: string;
    description: string;
    file: File;
}

interface AddInstructorData {
    email: string;
}

const updateSchoolSchema = z.object({
    name: z.string().min(1, { message: 'Описание обязательно' }),
    address: z.string().min(1, { message: 'Описание обязательно' }),
    description: z.string().min(1, { message: 'Описание обязательно' }),
    file: z.preprocess(
        (file) => (file instanceof FileList && file.length > 0 ? file[0] : file),
        z.instanceof(File).refine(file => file.size <= 2 * 1024 * 1024, {
            message: "Файл должен быть меньше 2MB",
        }).refine(file => ['image/jpeg'].includes(file.type), {
            message: "Допустимые форматы: JPEG, PNG, GIF",
        }).optional()
    ),
});

const courseSchema = z.object({
    name: z.string().min(1, { message: 'Название обязательно' }).max(24, 'Слишком длинное название'),
    price: z.preprocess((num) => parseInt(z.string().parse(num), 10), z.number({ message: 'Цена должна быть число' }).gte(0, { message: 'Стоимость не может быть отрицательной' })),
    duration: z.preprocess((num) => parseInt(z.string().parse(num), 10), z.number({ message: 'Длительность должна быть числом' }).gte(0, { message: 'Длительность не может быть отрицательной' })),
    description: z.string().min(1, { message: 'Описание обязательно' }),
    category: z.string({ message: 'Категория обязательна' }).min(1, { message: 'Категория обязательна' }),
    transmission: z.string({ message: 'Трансмиссия обязательна' }).min(1, { message: 'Трансмиссия обязательна' }),
    studyFormat: z.string({ message: 'Формат обучения обязателен' }).min(1, { message: 'Формат обучения обязателен' }),
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

const addInstructorSchema = z.object({
    email: z.string().email({ message: 'Некорректная электронная почта' }),
})

const SchoolPage = () => {
    const [isCourseModalOpen, setIsCourseModalOpen] = useState(false);
    const [isCarModalOpen, setIsCarModalOpen] = useState(false);
    const [isServiceModalOpen, setIsServiceModalOpen] = useState(false);
    const [isEditingSchool, setIsEditingSchool] = useState(false);
    const [isAddInstructorModalOpen, setIsAddInstructorModalOpen] = useState(false);
    const [school, setSchool] = useState<School>(defaultSchool);
    const [courses, setCourses] = useState<Course[]>([]);
    const [services, setServices] = useState<Service[]>([]);
    const [vehicles, setVehicles] = useState<Vehicle[]>([]);
    const [instructors, setInstructors] = useState<Instructor[]>([]);
    const [user, setUser] = useState(defaultUser);
    const [newCourse, setNewCourse] = useState(defaultCourse);
    const [newCar, setNewCar] = useState(defaultVehicle);
    const [newService, setNewService] = useState(defaultService);
    const [image, setImage] = useState(defaultPhoto);

    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const token = localStorage.getItem('custom-auth-token');

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axiosInstance.get(
                    '/driving-school/' + id + '/image',
                    {
                        headers: {
                            'Authorization': 'Bearer ' + token,
                        },
                        responseType: "blob",
                    },
                );
                const url = URL.createObjectURL(new Blob([response.data]))
                setImage(url);
            } catch (error) {
                if (axios.isAxiosError(error)) {
                    const status = error.response?.status;
                    if (status === 403) {
                        toast.error('Пользователь не авторизован');
                        navigate('/login');
                    } else if (status === 404) {

                    } else {
                        toast.error('Нет ответа от сервера');
                    }
                } else {
                    toast.error('Произошла ошибка');
                }
            }

            try {
                const response = await axiosInstance.get(
                    '/driving-school/' + id,
                    {
                        headers: {
                            'Authorization': 'Bearer ' + token,
                        }
                    },
                );
                setSchool(response.data);
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
                    '/driving-school/' + id + '/service',
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
                    '/driving-school/' + id + "/vehicle",
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
                    '/driving-school/' + id + '/instructor',
                    {
                        headers: {
                            'Authorization': 'Bearer ' + token,
                        }
                    },
                );
                setInstructors(response.data);
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
    }, [])

    const {
        register: courseRegister,
        handleSubmit: handleCourseSubmit,
        formState: { errors: courseErrors }
    } = useForm<CourseData>({
        resolver: zodResolver(courseSchema)
    });

    const {
        register: updateSchoolRegister,
        handleSubmit: handleUpdateSchool,
        formState: { errors: updateSchoolErrors },
    } = useForm<UpdateSchoolData>({
        resolver: zodResolver(updateSchoolSchema),
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
        register: addInstructorRegister,
        handleSubmit: handleAddInstructor,
        formState: { errors: addInstructorErrors }
    } = useForm<AddInstructorData>({
        resolver: zodResolver(addInstructorSchema)
    });

    const onUpdate = async (data: UpdateSchoolData) => {
        setIsEditingSchool(false);

        try {
            const response = await axiosInstance.patch(
                '/driving-school/' + id,
                {
                    name: data.name,
                    address: data.address,
                    description: data.description,
                },
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            const newSchool: School = response.data;
            setSchool(newSchool);
            toast.success('Школа обновлена успешно');
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

        const formData = new FormData();
        formData.append("file", data.file);

        try {
            const response = await axiosInstance.patch(
                '/driving-school/' + id + '/image',
                formData,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        "Content-Type": 'multipart/form-data',
                    }
                },
            );
            const url = URL.createObjectURL(data.file)
            setImage(url);
            toast.success('Фотография успешно обновлена');
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

    const onCourse = async (data: CourseData) => {
        setIsCourseModalOpen(false);

        if (data.studyFormat === 'Очно')
            data.studyFormat = 'OFFLINE';
        else
            data.studyFormat = 'ONLINE';

        if (data.transmission === 'Автоматическая')
            data.transmission = 'AUTOMATIC'
        else
            data.transmission = 'MECHANICAL'


        try {
            const response = await axiosInstance.post(
                '/driving-school/' + id + '/course',
                data,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            const newCourse: Course = response.data;
            courses.push(newCourse);
            setCourses(courses);
            toast.success('Курс добавлен успешно');
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

    const onService = async (data: ServiceData) => {
        setIsServiceModalOpen(false);

        try {
            const response = await axiosInstance.post(
                '/driving-school/' + id + '/additional-service',
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
                    navigate('/login');
                } else {
                    toast.error('Нет ответа от сервера');
                }
            } else {
                toast.error('Произошла ошибка');
            }
        }
    };

    const onServiceDelete = async (serviceId: number) => {
        console.log(1)
        try {
            const response = await axiosInstance.delete(
                '/driving-school/' + id + '/additional-service/' + serviceId,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            const newServices = services.filter((service) => { return service.id !== serviceId });
            setServices(newServices);
            toast.success('Сервис удален успешно');
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

    const onVehicle = async (data: VehicleData) => {
        setIsCarModalOpen(false);

        if (data.transmission === 'Автоматическая')
            data.transmission = 'AUTOMATIC'
        else
            data.transmission = 'MECHANICAL'

        try {
            const response = await axiosInstance.post(
                '/driving-school/' + id + '/vehicle',
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

    const onVehicleDelete = async (vehicleId: number) => {
        try {
            const response = await axiosInstance.delete(
                '/driving-school/' + id + '/vehicle/' + vehicleId,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            const newVehicles = vehicles.filter((vehicle) => { return vehicle.id !== vehicleId });
            setVehicles(newVehicles);
            toast.success('Транспортное средство удалено успешно');
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

    const onAddInstructor = async (data: AddInstructorData) => {
        setIsAddInstructorModalOpen(false);

        try {
            const response = await axiosInstance.post(
                '/driving-school/' + id + '/instructor/' + data.email,
                {},
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            const newInstructor: Instructor = response.data;
            instructors.push(newInstructor);
            setInstructors(instructors);
            toast.success('Инструктор добавлен успешно');
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const status = error.response?.status;
                if (status === 403) {
                    toast.error('Пользователь не авторизован');
                    navigate('/login');
                } else if (status === 404) {
                    toast.error('Ничего не найдено по данному запросу');
                } else {
                    toast.error('Нет ответа от сервера');
                }
            } else {
                toast.error('Произошла ошибка');
            }
        }
    }

    const onInstructorDelete = async (instructorId: number) => {
        try {
            const response = await axiosInstance.delete(
                '/driving-school/' + id + '/instructor/' + instructorId,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            const newInstructors = instructors.filter((instructor) => { return instructor.id !== instructorId });
            setInstructors(newInstructors);
            toast.success('Инструктор успешно удален');
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

    const onCourseDelete = async (courseId: number) => {
        try {
            const response = await axiosInstance.delete(
                '/driving-school/' + id + '/course/' + courseId,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            const newCoures = courses.filter((course) => { return course.id !== courseId });
            setCourses(newCoures);
            toast.success('Курс успешно удален');
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
                <div className="flex bg-gray-800 p-6 rounded-lg shadow-lg">
                    <img src={image} alt="Instructor Avatar" className="rounded-full w-1/4 mr-3" />
                    <div className="container">
                        {school.adminIds.includes(user.id) ? (
                            isEditingSchool ? (
                                <div>
                                    <input
                                        type='text'
                                        placeholder='Название школы'
                                        defaultValue={school.name}
                                        {...updateSchoolRegister('name')}
                                        className="w-full p-3 rounded bg-gray-700 text-white placeholder-gray-400 mb-3"
                                    />
                                    <input
                                        type='text'
                                        placeholder='Адресс школы'
                                        defaultValue={school.address}
                                        {...updateSchoolRegister('address')}
                                        className="w-full p-3 rounded bg-gray-700 text-white placeholder-gray-400 mb-3"
                                    />
                                    <textarea
                                        defaultValue={school.description}
                                        rows={3}
                                        {...updateSchoolRegister('description')}
                                        className="w-full p-3 rounded bg-gray-700 text-white placeholder-gray-400 mb-3"
                                    />
                                    {updateSchoolErrors.description && <span className="text-red-500">{updateSchoolErrors.description.message}</span>}
                                    <div className="flex space-x-4 mb-3">
                                        <h2>Прикрепить изображение:</h2>
                                        <input
                                            type='file'
                                            {...updateSchoolRegister('file')}
                                        />
                                    </div>
                                    <div className="flex space-x-4">
                                        <button
                                            onClick={handleUpdateSchool(onUpdate)}
                                            className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
                                        >
                                            Сохранить
                                        </button>
                                        <button
                                            onClick={() => setIsEditingSchool(false)}
                                            className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
                                        >
                                            Отмена
                                        </button>
                                    </div>
                                </div>
                            ) : (
                                <>
                                    <h1 className="text-3xl font-bold mb-4">{school.name}</h1>
                                    <div className="flex flex-col">
                                        <p className="text-gray-300 mb-6">Адресс: {school.address}</p>
                                        <p className="text-gray-300 mb-6">Описание: {school.description}</p>
                                        <button
                                            onClick={() => setIsEditingSchool(true)}
                                            className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                                        >
                                            Редактировать
                                        </button>
                                    </div>
                                </>
                            )
                        ) : (
                            <>
                                <h1 className="text-3xl font-bold mb-4">{school.name}</h1>
                                <div className="flex flex-col">
                                    <p className="text-gray-300 mb-6">Адресс: {school.address}</p>
                                    <p className="text-gray-300 mb-6">Описание: {school.description}</p>
                                </div>
                            </>
                        )}
                    </div>
                </div>
                <div className="bg-gray-800 p-6 rounded-lg shadow-lg mt-6">
                    <h2 className="text-2xl font-bold mb-4">Курсы</h2>
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                        {courses.map((course) => (
                            <div
                                key={course.id}
                                className="bg-gray-700 p-4 rounded-lg shadow-lg flex justify-between items-center space-x-4"
                            >
                                <div>
                                    <h3 className="text-lg font-bold">{course.name}</h3>
                                    <p>Цена: {course.price}</p>
                                    <p>Длительность: {course.duration} дней</p>
                                    <p>Категория: {course.category}</p>
                                    <p>Формат обучения: {course.studyFormat}</p>
                                </div>
                                {school.adminIds.includes(user.id) && (
                                    <button
                                        onClick={() => { onCourseDelete(course.id) }}
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
                                )}
                            </div>
                        ))}
                    </div>
                    {school.adminIds.includes(user.id) && (
                        <button
                            onClick={() => { setIsCourseModalOpen(true) }}
                            className="mt-4 px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600"
                        >
                            Добавить курс
                        </button>
                    )}
                </div>

                <div className="bg-gray-800 p-6 rounded-lg shadow-lg mt-6">
                    <h2 className="text-2xl font-bold mb-4">Дополнительные услуги</h2>
                    <ul className="list-disc list-inside space-y-2">
                        {services.map((service) => (
                            <li key={service.id} className="text-gray-300">
                                <strong>{service.name}</strong>: {service.description} - {service.price} ₽
                                {school.adminIds.includes(user.id) && (
                                    <button
                                        onClick={() => { onServiceDelete(service.id) }}
                                        className="ml-4 text-red-500 hover:text-red-700"
                                    >
                                        Удалить
                                    </button>
                                )}
                            </li>
                        ))}
                    </ul>
                    {school.adminIds.includes(user.id) && (
                        <button
                            onClick={() => { setIsServiceModalOpen(true) }}
                            className="mt-4 px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600"
                        >
                            Добавить услугу
                        </button>
                    )}
                </div>

                <div className="bg-gray-800 p-6 rounded-lg shadow-lg mt-6">
                    <h2 className="text-2xl font-bold mb-4">Автопарк</h2>
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                        {vehicles.map((vehicle) => (
                            <div
                                key={vehicle.id}
                                className="bg-gray-700 p-4 rounded-lg shadow-lg flex justify-between items-center space-x-4"
                            >
                                <div>
                                    <h3 className="text-lg font-bold">{vehicle.model}</h3>
                                    <p>Год выпуска: {vehicle.year}</p>
                                    <p>Коробка передач: {vehicle.transmission}</p>
                                    <p>Категория: {vehicle.category}</p>
                                </div>
                                {school.adminIds.includes(user.id) && (
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
                                )}
                            </div>
                        ))}
                    </div>
                    {school.adminIds.includes(user.id) && (
                        <button
                            onClick={() => { setIsCarModalOpen(true) }}
                            className="mt-4 px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600"
                        >
                            Добавить автомобиль
                        </button>
                    )}
                </div>

                <div className="bg-gray-800 p-6 rounded-lg shadow-lg mt-6">
                    <h2 className="text-2xl font-bold mb-4">Инструкторы</h2>
                    <ul className="list-disc list-inside space-y-2">
                        {instructors.map((instructor) => (
                            <li key={instructor.id} className="text-gray-300 ">
                                <span onClick={() => { navigate('/instructor/' + instructor.id) }} className="hover:text-orange-500">
                                    {instructor.user.lastName} {instructor.user.firstName}
                                </span>
                                {school.adminIds.includes(user.id) && (
                                    <button
                                        onClick={() => { onInstructorDelete(instructor.id) }}
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
                                )}
                            </li>
                        ))}
                    </ul>
                    {school.adminIds.includes(user.id) && (
                        <button
                            onClick={() => { setIsAddInstructorModalOpen(true) }}
                            className="mt-4 px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600"
                        >
                            Добавить сотрудника
                        </button>
                    )}
                </div>
            </div>

            {isCourseModalOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-gray-800 p-6 rounded-lg shadow-lg w-96">
                        <h2 className="text-xl font-bold mb-4 text-white">Добавить курс</h2>
                        <input
                            type="text"
                            placeholder="Название курса"
                            defaultValue={newCourse.name}
                            {...courseRegister('name')}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {courseErrors.name && <span className='text-red-500'>{courseErrors.name.message}</span>}
                        <input
                            type="number"
                            placeholder="Цена (₽)"
                            defaultValue={newCourse.price}
                            {...courseRegister('price')}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {courseErrors.price && <span className='text-red-500'>{courseErrors.price.message}</span>}
                        <input
                            type="number"
                            placeholder="Длительность (в днях)"
                            defaultValue={newCourse.duration}
                            {...courseRegister('duration')}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {courseErrors.duration && <span className='text-red-500'>{courseErrors.duration.message}</span>}
                        <textarea
                            placeholder="Описание курса"
                            defaultValue={newCourse.description}
                            {...courseRegister('description')}
                            rows={3}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {courseErrors.description && <span className='text-red-500'>{courseErrors.description.message}</span>}
                        <div className="mb-3">
                            <p className="text-white mb-2">Категория:</p>
                            <div className="flex gap-4">
                                {["A", "B", "C", "D"].map((category) => (
                                    <label key={category} className="flex items-center text-white">
                                        <input
                                            type="radio"
                                            value={category}
                                            {...courseRegister('category')}
                                            className="mr-2"
                                        />
                                        {category}
                                    </label>
                                ))}
                            </div>
                        </div>
                        {courseErrors.category && <span className='text-red-500'>{courseErrors.category.message}</span>}
                        <div className="mb-3">
                            <p className="text-white mb-2">Формат обучения:</p>
                            <div className="flex gap-4">
                                {["Очно", "Дистанционно"].map((studyFormat) => (
                                    <label key={studyFormat} className="flex items-center text-white">
                                        <input
                                            type="radio"
                                            value={studyFormat}
                                            {...courseRegister('studyFormat')}
                                            className="mr-2"
                                        />
                                        {studyFormat}
                                    </label>
                                ))}
                            </div>
                        </div>
                        {courseErrors.studyFormat && <span className='text-red-500'>{courseErrors.studyFormat.message}</span>}
                        <div className="mb-3">
                            <p className="text-white mb-2">Коробка передач:</p>
                            <div className="flex gap-4">
                                {["Автоматическая", "Механическая"].map((type) => (
                                    <label key={type} className="flex items-center text-white">
                                        <input
                                            type="radio"
                                            value={type}
                                            {...courseRegister('transmission')}
                                            className="mr-2"
                                        />
                                        {type}
                                    </label>
                                ))}
                            </div>
                        </div>
                        {courseErrors.transmission && <span className='text-red-500'>{courseErrors.transmission.message}</span>}
                        <div className="flex justify-end space-x-4">
                            <button
                                onClick={handleCourseSubmit(onCourse)}
                                className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
                            >
                                Добавить
                            </button>
                            <button
                                onClick={() => setIsCourseModalOpen(false)}
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

            {isAddInstructorModalOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-gray-800 p-6 rounded-lg shadow-lg w-96">
                        <h2 className="text-xl font-bold mb-4 text-white">Добавить инструктора</h2>
                        <input
                            type="text"
                            placeholder="Электронная почта"
                            defaultValue=''
                            {...addInstructorRegister('email')}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {addInstructorErrors.email && <span className='text-red-500'>{addInstructorErrors.email.message}</span>}
                        <div className="flex justify-end space-x-4">
                            <button
                                onClick={handleAddInstructor(onAddInstructor)}
                                className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
                            >
                                Добавить
                            </button>
                            <button
                                onClick={() => setIsAddInstructorModalOpen(false)}
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


export default SchoolPage;
