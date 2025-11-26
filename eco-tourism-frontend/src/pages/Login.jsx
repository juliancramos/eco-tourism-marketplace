import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const Login = () => {
    const [isLogin, setIsLogin] = useState(true);
    const [role, setRole] = useState('TOURIST');
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        // Mock login - redirect to home
        navigate('/');
    };

    return (
        <div className="min-h-screen flex">
            {/* Left Side - Image */}
            <div className="hidden lg:block lg:w-1/2 relative overflow-hidden">
                <div className="absolute inset-0 bg-gradient-to-br from-blue-600/90 to-green-600/90 z-10"></div>
                <img
                    src="https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=1200"
                    alt="Eco Tourism"
                    className="w-full h-full object-cover"
                />
                <div className="absolute inset-0 z-20 flex flex-col items-center justify-center text-white p-12">
                    <div className="max-w-md text-center">
                        <h1 className="text-5xl font-bold mb-6">
                            Eco-Tourism
                            <span className="block text-yellow-300">Marketplace</span>
                        </h1>
                        <p className="text-xl text-blue-100 mb-8">
                            Descubre experiencias únicas mientras cuidas el planeta
                        </p>
                        <div className="flex items-center justify-center gap-8 text-sm">
                            <div className="text-center">
                                <div className="text-3xl font-bold">500+</div>
                                <div className="text-blue-200">Experiencias</div>
                            </div>
                            <div className="text-center">
                                <div className="text-3xl font-bold">10K+</div>
                                <div className="text-blue-200">Viajeros</div>
                            </div>
                            <div className="text-center">
                                <div className="text-3xl font-bold">4.9</div>
                                <div className="text-blue-200">Rating</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/* Right Side - Form */}
            <div className="w-full lg:w-1/2 flex items-center justify-center p-8 bg-gradient-to-br from-blue-50 to-white">
                <div className="w-full max-w-md">
                    {/* Logo for mobile */}
                    <div className="lg:hidden text-center mb-8">
                        <h1 className="text-3xl font-bold text-blue-600">Eco-Tourism</h1>
                        <p className="text-gray-600">Marketplace</p>
                    </div>

                    <div className="bg-white rounded-2xl shadow-2xl p-8">
                        {/* Toggle Tabs */}
                        <div className="flex gap-2 mb-8 bg-gray-100 p-1 rounded-lg">
                            <button
                                onClick={() => setIsLogin(true)}
                                className={`flex-1 py-3 rounded-lg font-semibold transition-all ${isLogin
                                        ? 'bg-blue-600 text-white shadow-md'
                                        : 'text-gray-600 hover:text-blue-600'
                                    }`}
                            >
                                Iniciar Sesión
                            </button>
                            <button
                                onClick={() => setIsLogin(false)}
                                className={`flex-1 py-3 rounded-lg font-semibold transition-all ${!isLogin
                                        ? 'bg-blue-600 text-white shadow-md'
                                        : 'text-gray-600 hover:text-blue-600'
                                    }`}
                            >
                                Registrarse
                            </button>
                        </div>

                        <form onSubmit={handleSubmit} className="space-y-5">
                            {!isLogin && (
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-2">
                                        Nombre Completo
                                    </label>
                                    <input
                                        type="text"
                                        className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                                        placeholder="Juan Pérez"
                                        required={!isLogin}
                                    />
                                </div>
                            )}

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-2">
                                    Email
                                </label>
                                <input
                                    type="email"
                                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                                    placeholder="tu@email.com"
                                    required
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-2">
                                    Contraseña
                                </label>
                                <input
                                    type="password"
                                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                                    placeholder="••••••••"
                                    required
                                />
                            </div>

                            {!isLogin && (
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-2">
                                        Tipo de Usuario
                                    </label>
                                    <div className="grid grid-cols-2 gap-3">
                                        <button
                                            type="button"
                                            onClick={() => setRole('TOURIST')}
                                            className={`p-4 rounded-lg border-2 transition-all ${role === 'TOURIST'
                                                    ? 'border-blue-600 bg-blue-50 text-blue-600'
                                                    : 'border-gray-200 hover:border-blue-300'
                                                }`}
                                        >
                                            <div className="text-3xl mb-2">🧳</div>
                                            <div className="font-semibold">Turista</div>
                                        </button>
                                        <button
                                            type="button"
                                            onClick={() => setRole('PROVIDER')}
                                            className={`p-4 rounded-lg border-2 transition-all ${role === 'PROVIDER'
                                                    ? 'border-blue-600 bg-blue-50 text-blue-600'
                                                    : 'border-gray-200 hover:border-blue-300'
                                                }`}
                                        >
                                            <div className="text-3xl mb-2">🏢</div>
                                            <div className="font-semibold">Proveedor</div>
                                        </button>
                                    </div>
                                </div>
                            )}

                            {isLogin && (
                                <div className="flex items-center justify-between text-sm">
                                    <label className="flex items-center gap-2 cursor-pointer">
                                        <input type="checkbox" className="rounded text-blue-600 focus:ring-blue-500" />
                                        <span className="text-gray-600">Recordarme</span>
                                    </label>
                                    <a href="#" className="text-blue-600 hover:underline">
                                        ¿Olvidaste tu contraseña?
                                    </a>
                                </div>
                            )}

                            <button
                                type="submit"
                                className="w-full btn-primary text-lg py-4"
                            >
                                {isLogin ? 'Iniciar Sesión' : 'Crear Cuenta'}
                            </button>
                        </form>

                        <div className="mt-6">
                            <div className="relative">
                                <div className="absolute inset-0 flex items-center">
                                    <div className="w-full border-t border-gray-300"></div>
                                </div>
                                <div className="relative flex justify-center text-sm">
                                    <span className="px-4 bg-white text-gray-500">O continúa con</span>
                                </div>
                            </div>

                            <div className="mt-6 grid grid-cols-3 gap-3">
                                <button className="flex items-center justify-center px-4 py-3 border border-gray-300 rounded-lg hover:bg-gray-50 transition-all">
                                    <svg className="w-5 h-5" viewBox="0 0 24 24">
                                        <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" />
                                        <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" />
                                        <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z" />
                                        <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" />
                                    </svg>
                                </button>
                                <button className="flex items-center justify-center px-4 py-3 border border-gray-300 rounded-lg hover:bg-gray-50 transition-all">
                                    <svg className="w-5 h-5" fill="#1877F2" viewBox="0 0 24 24">
                                        <path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z" />
                                    </svg>
                                </button>
                                <button className="flex items-center justify-center px-4 py-3 border border-gray-300 rounded-lg hover:bg-gray-50 transition-all">
                                    <svg className="w-5 h-5" fill="#000000" viewBox="0 0 24 24">
                                        <path d="M12 0C5.373 0 0 5.373 0 12s5.373 12 12 12 12-5.373 12-12S18.627 0 12 0zm.001 18.5c-3.584 0-6.5-2.916-6.5-6.5s2.916-6.5 6.5-6.5 6.5 2.916 6.5 6.5-2.916 6.5-6.5 6.5z" />
                                    </svg>
                                </button>
                            </div>
                        </div>
                    </div>

                    <p className="text-center text-sm text-gray-600 mt-6">
                        Al continuar, aceptas nuestros{' '}
                        <a href="#" className="text-blue-600 hover:underline">Términos de Servicio</a>
                        {' '}y{' '}
                        <a href="#" className="text-blue-600 hover:underline">Política de Privacidad</a>
                    </p>
                </div>
            </div>
        </div>
    );
};

export default Login;
