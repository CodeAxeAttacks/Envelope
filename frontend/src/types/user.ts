export const defaultUser = {
    id: 0,
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    role: '',
    instructorId: 0,
}

export default interface User {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    role: string;
    instructorId: number;
}