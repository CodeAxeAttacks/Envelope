import axiosInstance from "./axios-instance";
import { toast } from "react-toastify";

export async function whoAmI() {
    const token = localStorage.getItem('custom-auth-token');

    try {
        const response = await axiosInstance.get(
            '/user/whoami',
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