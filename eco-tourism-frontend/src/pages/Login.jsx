import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Login = () => {
    const [isLogin, setIsLogin] = useState(true);
    const [role, setRole] = useState('TOURIST');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const { login, register } = useAuth();

    // Form states
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password: '',
        tradeName: '',
        description: '',
        phoneNumber: '',
        webpage: ''
    });

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            if (isLogin) {
                // Login
                const result = await login(formData.email, formData.password);
                if (result.success) {
                    // Redirect based on role
                    if (result.user.roles?.includes('PROVIDER')) {
                        navigate('/provider-dashboard');
                    } else {
                        navigate('/');
                    }
                } else {
                    setError(result.error || 'Credenciales inválidas');
                }
            } else {
                // Register
                const result = await register({
                    ...formData,
                    role: role === 'PROVIDER' ? 'PROVIDER' : 'CLIENT'
                });

                if (result.success) {
                    // Redirect based on role
                    if (role === 'PROVIDER') {
                        navigate('/provider-dashboard');
                    } else {
                        navigate('/');
                    }
                } else {
                    setError(result.error || 'Error al registrarse');
                }
            }
        } catch (err) {
            setError('Error de conexión');
        } finally {
            setLoading(false);
        }
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

                        {error && (
                            <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-600 text-sm">
                                {error}
                            </div>
                        )}

                        <form onSubmit={handleSubmit} className="space-y-5">
                            {!isLogin && (
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-2">
                                        Nombre Completo
                                    </label>
                                    <input
                                        type="text"
                                        name="name"
                                        value={formData.name}
                                        onChange={handleChange}
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
                                    name="email"
                                    value={formData.email}
                                    onChange={handleChange}
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
                                    name="password"
                                    value={formData.password}
                                    onChange={handleChange}
                                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                                    placeholder="••••••••"
                                    required
                                />
                            </div>

                            {!isLogin && (
                                <>
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

                                    {role === 'PROVIDER' && (
                                        <>
                                            <div>
                                                <label className="block text-sm font-medium text-gray-700 mb-2">
                                                    Nombre del Negocio
                                                </label>
                                                <input
                                                    type="text"
                                                    name="tradeName"
                                                    value={formData.tradeName}
                                                    onChange={handleChange}
                                                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                                                    placeholder="Mi Empresa Eco-Turística"
                                                />
                                            </div>
                                            <div>
                                                <label className="block text-sm font-medium text-gray-700 mb-2">
                                                    Teléfono
                                                </label>
                                                <input
                                                    type="tel"
                                                    name="phoneNumber"
                                                    value={formData.phoneNumber}
                                                    onChange={handleChange}
                                                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                                                    placeholder="+57 300 123 4567"
                                                />
                                            </div>
                                        </>
                                    )}
                                </>
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
                                disabled={loading}
                                className="w-full btn-primary text-lg py-4 disabled:opacity-50 disabled:cursor-not-allowed"
                            >
                                {loading ? 'Procesando...' : (isLogin ? 'Iniciar Sesión' : 'Crear Cuenta')}
                            </button>
                        </form>
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
