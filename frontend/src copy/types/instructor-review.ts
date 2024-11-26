import User from "./user";

export interface InstructorReviewData {
    rate: number;
    review: string;
}

export default interface InstructorReview {
    id: number;
    rate: number;
    review: string;
    user: User;
    instructorId: number;
}