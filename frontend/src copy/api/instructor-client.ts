import axios from "axios";
import { ServiceData } from "../types/instructor-service";
import axiosInstance from "./axios-instance";
import { toast } from "react-toastify";

export async function getInstructorById(instructorId: number) {
    const token = localStorage.getItem('custom-auth-token');

    try {
        const response = await axiosInstance.get(
            '/instructor/' + instructorId,
            {
                headers: {
                    'Authorization': 'Bearer ' + token,
                }
            },
        );

        return response.data;
    } catch (error) {
        toast.error('Произошла ошибка');
    }

}

export async function getServicesByInstructorId(instructorId: number) {
    const token = localStorage.getItem('custom-auth-token');

    try {
        const response = await axiosInstance.get(
            '/instructor/' + instructorId + '/service',
            {
                headers: {
                    'Authorization': 'Bearer ' + token,
                }
            },
        );

        return response.data;
    } catch (error) {
        toast.error('Произошла ошибка');
    }
}

export async function addService(service: ServiceData) {
    const token = localStorage.getItem('custom-auth-token');

    try {
        const response = await axiosInstance.post(
            '/instructor/service',
            service,
            {
                headers: {
                    'Authorization': 'Bearer ' + token,
                }
            },
        );

        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {

        } else {
            toast.error('Произошла ошибка');
            
        }
    }
}