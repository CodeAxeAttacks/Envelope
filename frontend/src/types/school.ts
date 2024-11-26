export const defaultSchool = {
    id: 0,
    name: '',
    phoneNumber: '',
    email: '',
    address: '',
    description: '',
    rating: 0,
    adminIds: [],
}

export interface SchoolData {
    name: string;
    phoneNumber: string;
    email: string;
    address: string;
    description: string;
}

export default interface School {
    id: number;
    name: string;
    phoneNumber: string;
    email: string;
    address: string;
    description: string;
    rating: number;
    adminIds: number[];
}