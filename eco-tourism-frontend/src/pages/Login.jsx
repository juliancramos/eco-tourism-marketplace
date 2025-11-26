import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const Login = () => {
    const [isLogin, setIsLogin] = useState(true);
    const [role, setRole] = useState('USER'); // USER or PROVIDER
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        // Here you would handle the actual authentication logic
        console.log("Form submitted", { isLogin, role });
        // Simulate successful login
        navigate('/');
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-100 px-4">
            <div className="max-w-md w-full bg-white rounded-lg shadow-xl overflow-hidden">
                <div className="flex">
                    <button
                        className={`flex-1 py-4 text-center font-semibold ${isLogin ? 'bg-green-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'}`}
                        onClick={() => setIsLogin(true)}
                    >
                        Login
                    </button>
                    <button
                        className={`flex-1 py-4 text-center font-semibold ${!isLogin ? 'bg-green-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'}`}
                        onClick={() => setIsLogin(false)}
                    >
                        Register
                    </button>
                </div>

                <div className="p-8">
                    <h2 className="text-2xl font-bold text-center text-gray-800 mb-8">
                        {isLogin ? 'Welcome Back!' : 'Create an Account'}
                    </h2>

                    <form onSubmit={handleSubmit} className="space-y-6">
                        {!isLogin && (
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-2">I am a...</label>
                                <div className="flex space-x-4">
                                    <label className={`flex-1 border rounded-lg p-3 text-center cursor-pointer transition-colors ${role === 'USER' ? 'border-green-600 bg-green-50 text-green-700' : 'border-gray-300 hover:border-green-400'}`}>
                                        <input
                                            type="radio"
                                            name="role"
                                            value="USER"
                                            checked={role === 'USER'}
                                            onChange={(e) => setRole(e.target.value)}
                                            className="hidden"
                                        />
                                        <span className="font-medium">Tourist</span>
                                    </label>
                                    <label className={`flex-1 border rounded-lg p-3 text-center cursor-pointer transition-colors ${role === 'PROVIDER' ? 'border-green-600 bg-green-50 text-green-700' : 'border-gray-300 hover:border-green-400'}`}>
                                        <input
                                            type="radio"
                                            name="role"
                                            value="PROVIDER"
                                            checked={role === 'PROVIDER'}
                                            onChange={(e) => setRole(e.target.value)}
                                            className="hidden"
                                        />
                                        <span className="font-medium">Provider</span>
                                    </label>
                                </div>
                            </div>
                        )}

                        {!isLogin && (
                            <div>
                                <label className="block text-sm font-medium text-gray-700">Full Name</label>
                                <input
                                    type="text"
                                    required
                                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500"
                                />
                            </div>
                        )}

                        <div>
                            <label className="block text-sm font-medium text-gray-700">Email Address</label>
                            <input
                                type="email"
                                required
                                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500"
                            />
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700">Password</label>
                            <input
                                type="password"
                                required
                                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500"
                            />
                        </div>

                        <button
                            type="submit"
                            className="w-full flex justify-center py-3 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"
                        >
                            {isLogin ? 'Sign In' : 'Sign Up'}
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default Login;
