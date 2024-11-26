export const defaultService = {
    id: 0,
    name: '',
    description: '',
    price: '',
}

export interface ServiceData {
    name: string;
    description: string;
    price: number;
}

export interface Service {
    id: number;
    name: string;
    description: string;
    price: string;
}