export const defaultCourse = {
    id: 0,
    name: '',
    price: '',
    duration: '',
    description: '',
    category: '',
    transmission: '',
    studyFormat: '',
}

export interface CourseData {
    name: string;
    price: number;
    duration: number;
    description: string;
    category: string;
    transmission: string;
    studyFormat: string;
}

export interface Course {
    id: number;
    name: string;
    price: number;
    duration: number;
    description: string;
    category: string;
    transmission: string;
    studyFormat: string;
}