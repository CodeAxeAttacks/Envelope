export const defaultVehicle = {
    id: 0,
    model: '',
    year: '',
    transmission: '',
    category: 'A',
}

export interface VehicleData {
    model: string;
    year: string;
    transmission: string;
    category: string;
}

export interface Vehicle {
    id: number;
    model: string;
    year: string;
    transmission: string;
    category: string;
}