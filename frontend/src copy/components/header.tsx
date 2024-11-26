import { useEffect, useState } from 'react';
import logo from '../assets/logo.svg';
import Profile from './profile';
import { whoAmI } from '../api/user-client';

function Header() {
    const [user, setUser] = useState({
        role: ''
    });

    useEffect(() => {
        const fetchUserData = async () => {
            setUser(await whoAmI());
        }

        fetchUserData();
    }, []);

    return (
        <header className="bg-gray-800 p-4 flex justify-between items-center border-b border-gray-700">
            <div className=" font-bold text-orange-500">
                <img src={logo} alt='logo' className='w-1/12' />
            </div>
            <nav className='w-1/3 flex items-center justify-end'>
                <a href="/home" className="text-white hover:text-orange-500 mx-4">Главная</a>
                <a href="/individuals" className="text-white hover:text-orange-500 mx-4">Физические лица</a>
                <Profile />
            </nav>
        </header>
    );
};

export default Header;
