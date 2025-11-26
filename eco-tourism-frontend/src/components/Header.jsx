import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Header = () => {
    const { user, logout, isProvider } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <header className="bg-white shadow-sm border-b sticky top-0 z-40">
            <div className="container mx-auto px-4">
                <div className="flex items-center justify-between h-16">
                    {/* Logo */}
                    <Link to="/" className="flex items-center gap-2">
                        <div className="w-10 h-10 bg-gradient-to-br from-blue-600 to-green-600 rounded-lg flex items-center justify-center">
                            <span className="text-white font-bold text-xl">🌿</span>
                        </div>
                        <div className="hidden sm:block">
                            <h1 className="font-bold text-gray-800">Eco-Tourism</h1>
                            <p className="text-xs text-gray-600">Marketplace</p>
                        </div>
                    </Link>

                    {/* Navigation */}
                    <nav className="flex items-center gap-4">
                        {user ? (
                            <>
                                <Link
                                    to="/"
                                    className="text-gray-600 hover:text-blue-600 transition-colors font-medium"
                                >
                                    Explorar
                                </Link>

                                {isProvider() ? (
                                    <Link
                                        to="/provider-dashboard"
                                        className="text-gray-600 hover:text-blue-600 transition-colors font-medium"
                                    >
                                        Mi Dashboard
                                    </Link>
                                ) : null}

                                <div className="flex items-center gap-3 pl-4 border-l">
                                    <div className="hidden sm:block text-right">
                                        <p className="text-sm font-medium text-gray-800">{user.name}</p>
                                        <p className="text-xs text-gray-600">
                                            {isProvider() ? 'Proveedor' : 'Cliente'}
                                        </p>
                                    </div>
                                    <div className="w-10 h-10 bg-blue-600 rounded-full flex items-center justify-center text-white font-bold">
                                        {user.name?.charAt(0) || 'U'}
                                    </div>
                                    <button
                                        onClick={handleLogout}
                                        className="text-gray-600 hover:text-red-600 transition-colors"
                                        title="Cerrar sesión"
                                    >
                                        <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                                        </svg>
                                    </button>
                                </div>
                            </>
                        ) : (
                            <Link to="/login" className="btn-primary py-2">
                                Iniciar Sesión
                            </Link>
                        )}
                    </nav>
                </div>
            </div>
        </header>
    );
};

export default Header;
