import { toast } from "react-toastify";
import axiosInstance from "./axios-instance";
import { VehicleData } from "../types/vehicle";

export async function getVehiclesByInstructorId(instructorId: number) {
    const token = localStorage.getItem('custom-auth-token');

    try {
        const response = await axiosInstance.get(
            '/instructor/' + instructorId + "/vehicle",
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

export async function addVehicle(vehicle: VehicleData) {
    const token = localStorage.getItem('custom-auth-token');

    try {
        const response = await axiosInstance.post(
            '/instructor/vehicle',
            vehicle,
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