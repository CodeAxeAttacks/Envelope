import React, { useState, useEffect } from 'react';
import Header from '../components/header';
import Footer from '../components/footer';
import { toast, ToastContainer } from 'react-toastify';
import School from '../types/school';
import { z } from 'zod';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import axios from 'axios';
import axiosInstance from '../api/axios-instance';
import { useNavigate } from 'react-router-dom';

interface SearchResult {
    id: number;
    name: string;
    avgPrice: number;
    rate: number;
}

interface PriceData {
    minPrice: number,
    maxPrice: number,
}

interface FullData {
    minPrice: number,
    maxPrice: number,
    maxDuration: number,
}

const instructorSchema = z.object({
    minPrice: z.preprocess((num) => parseInt(z.string().parse(num), 10), z.number({ message: 'Минимальная цена должна быть числом' }).gte(0, { message: 'Минимальная цена не должна быть отрицательной' })),
    maxPrice: z.preprocess((num) => parseInt(z.string().parse(num), 10), z.number({ message: 'Максимальная цена должна быть числом' }).gte(0, { message: 'Максимальная цена не должна быть отрицательной' })),
})

const schoolSchema = z.object({
    minPrice: z.preprocess((num) => parseInt(z.string().parse(num), 10), z.number({ message: 'Минимальная цена должна быть числом' }).gte(0, { message: 'Минимальная цена не должна быть отрицательной' })),
    maxPrice: z.preprocess((num) => parseInt(z.string().parse(num), 10), z.number({ message: 'Максимальная цена должна быть числом' }).gte(0, { message: 'Максимальная цена не должна быть отрицательной' })),
    maxDuration: z.preprocess((num) => parseInt(z.string().parse(num), 10), z.number({ message: 'Максимальная длительность обучения должна быть числом' }).gte(0, { message: 'Максимальная длительность обучения не должна быть отрицательной' })),
})

const Home = () => {
    const [filters, setFilters] = useState({
        minPrice: 0,
        maxPrice: 50000,
        categories: [] as string[],
        minRating: 0,
        transmissions: [] as string[],
        instructorOrSchool: 'school',
        maxDuration: 100,
        studyFormats: [] as string[],
    });
    const [itemsFound, setItemsFound] = useState<SearchResult[]>([]);
    const token = localStorage.getItem('custom-auth-token');
    const navigate = useNavigate();

    const {
        register: instructorRegister,
        handleSubmit: handleInstructorSubmit,
        formState: { errors: instructorErrors }
    } = useForm<PriceData>({
        resolver: zodResolver(instructorSchema)
    });

    const {
        register: schoolRegister,
        handleSubmit: handleSchoolSubmit,
        formState: { errors: schoolErrors }
    } = useForm<FullData>({
        resolver: zodResolver(schoolSchema)
    });

    const onSchoolSearch = async (data: FullData) => {
        try {
            const response = await axiosInstance.post(
                '/search/driving-school',
                {
                    minPrice: data.minPrice,
                    maxPrice: data.maxPrice,
                    categories: filters.categories,
                    minRating: filters.minRating,
                    transmissions: filters.transmissions,
                    maxDuration: data.maxDuration,
                    studyFormats: filters.studyFormats,
                },
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            setItemsFound(response.data);
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
    }

    const onInstructorSearch = async (data: PriceData) => {
        try {
            const response = await axiosInstance.post(
                '/search/instructor',
                {
                    minPrice: data.minPrice,
                    maxPrice: data.maxPrice,
                    categories: filters.categories,
                    minRating: filters.minRating,
                    transmissions: filters.transmissions,
                },
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            setItemsFound(response.data);
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
    }


    return (
        <div className="min-h-screen bg-gray-900 text-white">
            <Header />
            <main className="flex w-full container mx-auto p-6">
                <section className="w-1/3 p-6 bg-gray-800 rounded-lg shadow-lg mr-3">
                    <h2 className="text-2xl font-bold mb-4">Фильтры</h2>
                    <div className="flex flex-col space-y-4">
                        <div>
                            <label className="block font-semibold mb-2">Тип</label>
                            <select
                                value={filters.instructorOrSchool}
                                onChange={(e) => setFilters({ ...filters, instructorOrSchool: e.target.value })}
                                className="p-2 w-full rounded bg-gray-700"
                            >
                                <option value="school">Автошкола</option>
                                <option value="instructor">Инструктор</option>
                            </select>
                        </div>

                        {filters.instructorOrSchool === 'school' ?
                            <>
                                <div>
                                    <label className="block font-semibold mb-2">Цена</label>
                                    <div className='flex items-center'>
                                        <input
                                            placeholder='От'
                                            defaultValue={filters.minPrice}
                                            {...schoolRegister('minPrice')}
                                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400 mr-1"
                                        />
                                        <input
                                            placeholder='До'
                                            defaultValue={filters.maxPrice}
                                            {...schoolRegister('maxPrice')}
                                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400 ml-1"
                                        />
                                    </div>
                                    {schoolErrors.minPrice && <span className='text-red-500'>{schoolErrors.minPrice.message}</span>}
                                    {schoolErrors.maxPrice && <span className='text-red-500'>{schoolErrors.maxPrice.message}</span>}
                                </div>

                                <div>
                                    <label className="block font-semibold mb-2">Максимальная длительность обучения (в днях)</label>
                                    <div className='flex items-center'>
                                        <input
                                            placeholder='До'
                                            defaultValue={filters.maxDuration}
                                            {...schoolRegister('maxDuration')}
                                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400 mr-1"
                                        />
                                    </div>
                                    {schoolErrors.maxDuration && <span className='text-red-500'>{schoolErrors.maxDuration.message}</span>}
                                </div>
                            </>
                            :
                            <>
                                <div>
                                    <label className="block font-semibold mb-2">Цена</label>
                                    <div className='flex items-center'>
                                        <input
                                            placeholder='От'
                                            defaultValue={filters.minPrice}
                                            {...instructorRegister('minPrice')}
                                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400 mr-1"
                                        />
                                        <input
                                            placeholder='До'
                                            defaultValue={filters.maxPrice}
                                            {...instructorRegister('maxPrice')}
                                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400 ml-1"
                                        />
                                    </div>
                                    {instructorErrors.minPrice && <span className='text-red-500'>{instructorErrors.minPrice.message}</span>}
                                    {instructorErrors.maxPrice && <span className='text-red-500'>{instructorErrors.maxPrice.message}</span>}
                                </div>
                            </>}

                        <div>
                            <label className="block font-semibold mb-2">Категория</label>
                            <div className="flex gap-2">
                                {['A', 'B', 'C', 'D'].map((cat) => (
                                    <label key={cat} className="flex items-center gap-2">
                                        <input
                                            type="checkbox"
                                            onChange={(e) => {
                                                const newCategories = e.target.checked
                                                    ? [...filters.categories, cat]
                                                    : filters.categories.filter((c) => c !== cat);
                                                setFilters({ ...filters, categories: newCategories });
                                            }}
                                            className='accent-orange-500 text-white'
                                        />
                                        {cat}
                                    </label>
                                ))}
                            </div>
                        </div>

                        <div>
                            <label className="block font-semibold mb-2">Рейтинг</label>
                            <input
                                type="range"
                                min="0"
                                max="5"
                                value={filters.minRating}
                                onChange={(e) => setFilters({ ...filters, minRating: parseInt(e.target.value, 10) })}
                                className="w-full accent-orange-500"
                            />
                            <span>{`От ${filters.minRating} звезд`}</span>
                        </div>

                        <div>
                            <label className="block font-semibold mb-2">Тип коробки передач</label>
                            <div className="flex gap-2">
                                {['AUTOMATIC', 'MECHANICAL'].map((trans) => (
                                    <label key={trans} className="flex items-center gap-2">
                                        <input
                                            type="checkbox"
                                            onChange={(e) => {
                                                const newTransmission = e.target.checked
                                                    ? [...filters.transmissions, trans]
                                                    : filters.transmissions.filter((t) => t !== trans);
                                                setFilters({ ...filters, transmissions: newTransmission });
                                            }}
                                            className='accent-orange-500'
                                        />
                                        {trans === 'AUTOMATIC' ? 'Автомат' : 'Механика'}
                                    </label>
                                ))}
                            </div>
                        </div>

                        {filters.instructorOrSchool === 'school' &&
                            <div>
                                <label className="block font-semibold mb-2">Формат обучения</label>
                                <div className="flex gap-2">
                                    {['OFFLINE', 'ONLINE'].map((format) => (
                                        <label key={format} className="flex items-center gap-2">
                                            <input
                                                type="checkbox"
                                                onChange={(e) => {
                                                    const newFormats = e.target.checked
                                                        ? [...filters.studyFormats, format]
                                                        : filters.studyFormats.filter((t) => t !== format);
                                                    setFilters({ ...filters, studyFormats: newFormats });
                                                }}
                                                className='accent-orange-500'
                                            />
                                            {format === 'OFFLINE' ? 'Очный' : 'Дистанционный'}
                                        </label>
                                    ))}
                                </div>
                            </div>
                        }

                    </div>

                    <div className='flex flex-col gap-4'>
                        <button
                            type="button"
                            onClick={filters.instructorOrSchool === 'school' ? handleSchoolSubmit(onSchoolSearch) : handleInstructorSubmit(onInstructorSearch)}
                            className="mt-4 bg-orange-500 text-white py-2 rounded hover:bg-orange-600"
                        >
                            Поиск
                        </button>
                    </div>
                </section>

                <section className="w-2/3 p-6 bg-gray-800 rounded-lg shadow-lg">
                    <h2 className="text-2xl font-bold mb-4">Список {filters.instructorOrSchool === 'school' ? 'автошкол' : 'инструкторов'} (найдено {itemsFound.length})</h2>
                    <table className="min-w-full bg-gray-800 text-left text-sm text-gray-200">
                        <thead className="bg-gray-700">
                            <tr>
                                <th
                                    className="p-4 cursor-pointer"
                                    onClick={() => console.log('Sort by name')}
                                >
                                    {filters.instructorOrSchool === 'school' ? 'Название' : 'Фамилия Имя'}
                                </th>
                                <th
                                    className="p-4 cursor-pointer"
                                    onClick={() => console.log('Sort by price')}
                                >
                                    Цена
                                </th>
                                <th
                                    className="p-4 cursor-pointer"
                                    onClick={() => console.log('Sort by transmission')}
                                >
                                    Оценка
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            {itemsFound.map((item) => (
                                <tr key={item.id} className="border-b border-gray-600 hover:bg-gray-700"
                                    onClick={() => {
                                        if (filters.instructorOrSchool === 'school') {
                                            navigate('/school/' + item.id);
                                        } else {
                                            navigate('/instructor/' + item.id);
                                        }
                                    }}>
                                    <td className="p-4">{item.name}</td>
                                    <td className="p-4">{item.avgPrice}</td>
                                    <td className="p-4">{item.rate}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </section>
            </main>

            <ToastContainer position="bottom-left" autoClose={3000} />
            <Footer />
        </div>
    );
};

export default Home;
