export const defaultService = {
    id: 0,
    name: '',
    description: '',
    price: '',
    drivingSchoolId: '',
}

export interface ServiceData {
    name: string;
    address: string;
    description: string;
    rating: number;
    adminIds: number[];
}

export interface Service {
    id: number;
    name: string;
    description: string;
    price: number;
    drivingSchoolId: number;
}
