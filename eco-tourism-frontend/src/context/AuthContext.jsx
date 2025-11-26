import React, { createContext, useState, useContext, useEffect } from 'react';
import axios from 'axios';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Check if user is logged in (from localStorage)
        const savedUser = localStorage.getItem('user');
        if (savedUser) {
            setUser(JSON.parse(savedUser));
        }
        setLoading(false);
    }, []);

    const login = async (email, password) => {
        try {
            const apiUrl = import.meta.env.VITE_API_URL;

            // For simplicity, we'll create a mock login that just fetches the user
            // In production, this should use Keycloak tokens
            const response = await axios.get(`${apiUrl}/users`);
            const users = response.data;

            // Find user by email (mock authentication)
            const foundUser = users.find(u => u.email === email);

            if (foundUser) {
                setUser(foundUser);
                localStorage.setItem('user', JSON.stringify(foundUser));
                return { success: true, user: foundUser };
            } else {
                return { success: false, error: 'Usuario no encontrado' };
            }
        } catch (error) {
            console.error('Login error:', error);
            return { success: false, error: 'Error de conexión' };
        }
    };

    const register = async (userData) => {
        try {
            const apiUrl = import.meta.env.VITE_API_URL;

            // Determine endpoint based on role
            const endpoint = userData.role === 'PROVIDER'
                ? `${apiUrl}/users/provider`
                : `${apiUrl}/users/client`;

            const payload = userData.role === 'PROVIDER'
                ? {
                    email: userData.email,
                    password: userData.password,
                    name: userData.name,
                    imgUrl: '',
                    roles: ['PROVIDER'],
                    tradeName: userData.tradeName || userData.name,
                    description: userData.description || '',
                    phoneNumber: userData.phoneNumber || '',
                    webpage: userData.webpage || ''
                }
                : {
                    email: userData.email,
                    password: userData.password,
                    name: userData.name,
                    imgUrl: '',
                    roles: ['CLIENT']
                };

            const response = await axios.post(endpoint, payload);

            if (response.data) {
                setUser(response.data);
                localStorage.setItem('user', JSON.stringify(response.data));
                return { success: true, user: response.data };
            }

            return { success: false, error: 'Error al registrar' };
        } catch (error) {
            console.error('Register error:', error);
            return { success: false, error: error.response?.data?.message || 'Error al registrar' };
        }
    };

    const logout = () => {
        setUser(null);
        localStorage.removeItem('user');
    };

    const isProvider = () => {
        return user?.roles?.includes('PROVIDER');
    };

    return (
        <AuthContext.Provider value={{ user, login, register, logout, isProvider, loading }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within AuthProvider');
    }
    return context;
};
