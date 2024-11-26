import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080', 
    headers: {
        'Content-Type': 'application/json', // Заголовок по умолчанию для JSON
    },
});

export default axiosInstance;