import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import axios from 'axios';

const ProviderDashboard = () => {
    const { user, logout, isProvider } = useAuth();
    const navigate = useNavigate();
    const [services, setServices] = useState([]);
    const [showCreateForm, setShowCreateForm] = useState(false);
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (!user || !isProvider()) {
            navigate('/login');
            return;
        }

        fetchServices();
        fetchCategories();
    }, [user]);

    const fetchServices = async () => {
        try {
            const apiUrl = import.meta.env.VITE_API_URL;
            const response = await axios.get(`${apiUrl}/services`);
            // Filter services by current provider
            const providerServices = response.data.content?.filter(
                s => s.providerId === user.id
            ) || [];
            setServices(providerServices);
            setLoading(false);
        } catch (error) {
            console.error('Error fetching services:', error);
            setLoading(false);
        }
    };

    const fetchCategories = async () => {
        try {
            const apiUrl = import.meta.env.VITE_API_URL;
            const response = await axios.get(`${apiUrl}/categories`);
            setCategories(response.data || []);
        } catch (error) {
            console.error('Error fetching categories:', error);
        }
    };

    const handleLogout = () => {
        logout();
        navigate('/');
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-green-50">
            {/* Header */}
            <div className="bg-white shadow-sm border-b">
                <div className="container mx-auto px-4 py-4">
                    <div className="flex items-center justify-between">
                        <div className="flex items-center gap-4">
                            <div className="w-12 h-12 bg-blue-600 rounded-full flex items-center justify-center text-white font-bold text-xl">
                                {user?.name?.charAt(0) || 'P'}
                            </div>
                            <div>
                                <h2 className="font-semibold text-gray-800">{user?.tradeName || user?.name}</h2>
                                <p className="text-sm text-gray-600">Panel de Proveedor</p>
                            </div>
                        </div>
                        <div className="flex items-center gap-4">
                            <button
                                onClick={() => navigate('/')}
                                className="text-gray-600 hover:text-blue-600 transition-colors"
                            >
                                Ver Marketplace
                            </button>
                            <button
                                onClick={handleLogout}
                                className="btn-secondary py-2"
                            >
                                Cerrar Sesión
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <div className="container mx-auto px-4 py-8">
                {/* Stats */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
                    <div className="bg-white rounded-xl shadow-md p-6">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-gray-600 mb-1">Servicios Activos</p>
                                <p className="text-3xl font-bold text-blue-600">{services.filter(s => s.active).length}</p>
                            </div>
                            <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
                                <svg className="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                                </svg>
                            </div>
                        </div>
                    </div>

                    <div className="bg-white rounded-xl shadow-md p-6">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-gray-600 mb-1">Total Servicios</p>
                                <p className="text-3xl font-bold text-green-600">{services.length}</p>
                            </div>
                            <div className="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
                                <svg className="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                                </svg>
                            </div>
                        </div>
                    </div>

                    <div className="bg-white rounded-xl shadow-md p-6">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-gray-600 mb-1">Promedio Rating</p>
                                <p className="text-3xl font-bold text-orange-600">4.8</p>
                            </div>
                            <div className="w-12 h-12 bg-orange-100 rounded-lg flex items-center justify-center">
                                <svg className="w-6 h-6 text-orange-600" fill="currentColor" viewBox="0 0 20 20">
                                    <path d="M10 15l-5.878 3.09 1.123-6.545L.489 6.91l6.572-.955L10 0l2.939 5.955 6.572.955-4.756 4.635 1.123 6.545z" />
                                </svg>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Actions */}
                <div className="mb-6 flex items-center justify-between">
                    <h1 className="text-2xl font-bold text-gray-800">Mis Servicios</h1>
                    <button
                        onClick={() => setShowCreateForm(true)}
                        className="btn-primary flex items-center gap-2"
                    >
                        <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
                        </svg>
                        Crear Nuevo Servicio
                    </button>
                </div>

                {/* Services List */}
                {loading ? (
                    <div className="text-center py-12">
                        <div className="animate-spin rounded-full h-12 w-12 border-t-4 border-b-4 border-blue-600 mx-auto"></div>
                        <p className="text-gray-600 mt-4">Cargando servicios...</p>
                    </div>
                ) : services.length === 0 ? (
                    <div className="text-center py-16 bg-white rounded-xl shadow-md">
                        <div className="text-6xl mb-4">🌿</div>
                        <h3 className="text-xl font-semibold text-gray-800 mb-2">No tienes servicios aún</h3>
                        <p className="text-gray-600 mb-6">Crea tu primer servicio para comenzar a vender</p>
                        <button
                            onClick={() => setShowCreateForm(true)}
                            className="btn-primary"
                        >
                            Crear Mi Primer Servicio
                        </button>
                    </div>
                ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {services.map((service) => (
                            <div key={service.id} className="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-xl transition-all">
                                <div className="h-48 bg-gray-200">
                                    {service.images && service.images.length > 0 ? (
                                        <img src={service.images[0].url} alt={service.title} className="w-full h-full object-cover" />
                                    ) : (
                                        <div className="flex items-center justify-center h-full text-gray-400">
                                            Sin imagen
                                        </div>
                                    )}
                                </div>
                                <div className="p-4">
                                    <div className="flex items-center justify-between mb-2">
                                        <span className={`px-2 py-1 rounded-full text-xs font-semibold ${service.active ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-700'
                                            }`}>
                                            {service.active ? 'Activo' : 'Inactivo'}
                                        </span>
                                        <span className="text-sm text-gray-600">{service.cityCode}</span>
                                    </div>
                                    <h3 className="font-semibold text-gray-800 mb-1">{service.title}</h3>
                                    <p className="text-sm text-gray-600 mb-3 line-clamp-2">{service.description}</p>
                                    <div className="flex items-center justify-between">
                                        <span className="text-xl font-bold text-orange-600">
                                            {service.currency} {service.price}
                                        </span>
                                        <button className="text-blue-600 hover:underline text-sm font-medium">
                                            Editar
                                        </button>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            {/* Create Service Modal */}
            {showCreateForm && (
                <CreateServiceModal
                    onClose={() => setShowCreateForm(false)}
                    onSuccess={() => {
                        setShowCreateForm(false);
                        fetchServices();
                    }}
                    providerId={user?.id}
                    categories={categories}
                />
            )}
        </div>
    );
};

// Create Service Modal Component
const CreateServiceModal = ({ onClose, onSuccess, providerId, categories }) => {
    const [formData, setFormData] = useState({
        categoryId: '',
        title: '',
        description: '',
        price: '',
        currency: 'USD',
        countryCode: 'COL',
        cityCode: '',
        address: '',
        startDate: '',
        endDate: '',
        transportType: '',
        imageUrl: ''
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            const apiUrl = import.meta.env.VITE_API_URL;

            const payload = {
                providerId: providerId,
                categoryId: parseInt(formData.categoryId),
                title: formData.title,
                description: formData.description,
                price: parseFloat(formData.price),
                currency: formData.currency,
                active: true,
                countryCode: formData.countryCode,
                cityCode: formData.cityCode,
                startDate: formData.startDate ? `${formData.startDate}T00:00:00` : null,
                endDate: formData.endDate ? `${formData.endDate}T00:00:00` : null,
                transportType: formData.transportType || null,
                routeOrigin: null,
                routeDestination: null,
                address: formData.address,
                latitude: null,
                longitude: null,
                images: formData.imageUrl ? [{ url: formData.imageUrl, position: 1 }] : []
            };

            await axios.post(`${apiUrl}/services`, payload);
            onSuccess();
        } catch (err) {
            console.error('Error creating service:', err);
            setError(err.response?.data?.message || 'Error al crear el servicio');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
            <div className="bg-white rounded-2xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
                <div className="p-6 border-b sticky top-0 bg-white">
                    <div className="flex items-center justify-between">
                        <h2 className="text-2xl font-bold text-gray-800">Crear Nuevo Servicio</h2>
                        <button onClick={onClose} className="text-gray-400 hover:text-gray-600">
                            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                            </svg>
                        </button>
                    </div>
                </div>

                <form onSubmit={handleSubmit} className="p-6 space-y-4">
                    {error && (
                        <div className="p-3 bg-red-50 border border-red-200 rounded-lg text-red-600 text-sm">
                            {error}
                        </div>
                    )}

                    <div className="grid grid-cols-2 gap-4">
                        <div className="col-span-2">
                            <label className="block text-sm font-medium text-gray-700 mb-2">Título *</label>
                            <input
                                type="text"
                                name="title"
                                value={formData.title}
                                onChange={handleChange}
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                required
                            />
                        </div>

                        <div className="col-span-2">
                            <label className="block text-sm font-medium text-gray-700 mb-2">Descripción *</label>
                            <textarea
                                name="description"
                                value={formData.description}
                                onChange={handleChange}
                                rows={3}
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                required
                            />
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">Categoría *</label>
                            <select
                                name="categoryId"
                                value={formData.categoryId}
                                onChange={handleChange}
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                required
                            >
                                <option value="">Selecciona...</option>
                                {categories.map(cat => (
                                    <option key={cat.id} value={cat.id}>{cat.name}</option>
                                ))}
                            </select>
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">Precio *</label>
                            <input
                                type="number"
                                name="price"
                                value={formData.price}
                                onChange={handleChange}
                                step="0.01"
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                required
                            />
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">Ciudad *</label>
                            <input
                                type="text"
                                name="cityCode"
                                value={formData.cityCode}
                                onChange={handleChange}
                                placeholder="BOG, MED, CTG..."
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                required
                            />
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">Dirección</label>
                            <input
                                type="text"
                                name="address"
                                value={formData.address}
                                onChange={handleChange}
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                            />
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">Fecha Inicio</label>
                            <input
                                type="date"
                                name="startDate"
                                value={formData.startDate}
                                onChange={handleChange}
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                            />
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">Fecha Fin</label>
                            <input
                                type="date"
                                name="endDate"
                                value={formData.endDate}
                                onChange={handleChange}
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                            />
                        </div>

                        <div className="col-span-2">
                            <label className="block text-sm font-medium text-gray-700 mb-2">URL de Imagen</label>
                            <input
                                type="url"
                                name="imageUrl"
                                value={formData.imageUrl}
                                onChange={handleChange}
                                placeholder="https://ejemplo.com/imagen.jpg"
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                            />
                        </div>
                    </div>

                    <div className="flex gap-3 pt-4">
                        <button
                            type="button"
                            onClick={onClose}
                            className="flex-1 btn-secondary py-3"
                        >
                            Cancelar
                        </button>
                        <button
                            type="submit"
                            disabled={loading}
                            className="flex-1 btn-primary py-3 disabled:opacity-50"
                        >
                            {loading ? 'Creando...' : 'Crear Servicio'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default ProviderDashboard;
