import User, { defaultUser } from "./user";

export const defaultInstructor = {
    id: 0,
    userId: 0,
    description: '',
    rating: 0,
    user: defaultUser,
}

export default interface Instructor {
    id: number;
    userId: number;
    description: string;
    rating: number;
    user: User;
};