import React, { useEffect, useState } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import ReviewSection from '../components/ReviewSection';
import { MOCK_SERVICES } from '../data/mockData';

const ServiceDetails = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [service, setService] = useState(null);
    const [loading, setLoading] = useState(true);
    const [selectedImage, setSelectedImage] = useState(0);

    useEffect(() => {
        const fetchService = async () => {
            try {
                const apiUrl = import.meta.env.VITE_API_URL;
                const response = await axios.get(`${apiUrl}/services/${id}`);
                setService(response.data);
                setLoading(false);
            } catch (err) {
                console.warn("Backend not reachable, using MOCK data for testing.");
                const mockService = MOCK_SERVICES.find(s => s.id === parseInt(id)) || MOCK_SERVICES[0];
                setService(mockService);
                setLoading(false);
            }
        };

        fetchService();
    }, [id]);

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-blue-100">
                <div className="text-center">
                    <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-b-4 border-blue-600 mx-auto mb-4"></div>
                    <p className="text-gray-600 font-medium">Cargando detalles...</p>
                </div>
            </div>
        );
    }

    if (!service) return <div className="p-8 text-center">Servicio no encontrado</div>;

    const images = service.images && service.images.length > 0
        ? service.images
        : [{ url: 'https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=800' }];

    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-green-50">
            {/* Breadcrumb */}
            <div className="bg-white shadow-sm">
                <div className="container mx-auto px-4 py-4">
                    <div className="flex items-center gap-2 text-sm">
                        <Link to="/" className="text-blue-600 hover:underline">Inicio</Link>
                        <span className="text-gray-400">/</span>
                        <span className="text-gray-600">{service.title}</span>
                    </div>
                </div>
            </div>

            <div className="container mx-auto px-4 py-8">
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    {/* Left Column - Images & Details */}
                    <div className="lg:col-span-2">
                        {/* Image Gallery */}
                        <div className="bg-white rounded-2xl shadow-lg overflow-hidden mb-6">
                            <div className="relative h-96 bg-gray-200">
                                <img
                                    src={images[selectedImage].url}
                                    alt={service.title}
                                    className="w-full h-full object-cover"
                                />
                                <div className="absolute top-4 left-4">
                                    <span className="badge-discount shadow-lg">
                                        -25% OFF
                                    </span>
                                </div>
                            </div>

                            {/* Thumbnail Gallery */}
                            {images.length > 1 && (
                                <div className="p-4 flex gap-2 overflow-x-auto">
                                    {images.map((img, idx) => (
                                        <button
                                            key={idx}
                                            onClick={() => setSelectedImage(idx)}
                                            className={`flex-shrink-0 w-20 h-20 rounded-lg overflow-hidden border-2 transition-all ${selectedImage === idx ? 'border-blue-600 scale-105' : 'border-gray-200 hover:border-blue-300'
                                                }`}
                                        >
                                            <img src={img.url} alt="" className="w-full h-full object-cover" />
                                        </button>
                                    ))}
                                </div>
                            )}
                        </div>

                        {/* Service Info */}
                        <div className="bg-white rounded-2xl shadow-lg p-6 mb-6">
                            <div className="flex items-start justify-between mb-4">
                                <div>
                                    <h1 className="text-3xl font-bold text-gray-800 mb-2">{service.title}</h1>
                                    <div className="flex items-center gap-4 text-sm text-gray-600">
                                        <div className="flex items-center gap-1">
                                            <svg className="w-5 h-5 text-blue-600" fill="currentColor" viewBox="0 0 20 20">
                                                <path fillRule="evenodd" d="M5.05 4.05a7 7 0 119.9 9.9L10 18.9l-4.95-4.95a7 7 0 010-9.9zM10 11a2 2 0 100-4 2 2 0 000 4z" clipRule="evenodd" />
                                            </svg>
                                            <span>{service.cityCode}, {service.countryCode}</span>
                                        </div>
                                        <div className="flex items-center gap-1">
                                            {[...Array(5)].map((_, i) => (
                                                <svg key={i} className="w-4 h-4 text-yellow-400 fill-current" viewBox="0 0 20 20">
                                                    <path d="M10 15l-5.878 3.09 1.123-6.545L.489 6.91l6.572-.955L10 0l2.939 5.955 6.572.955-4.756 4.635 1.123 6.545z" />
                                                </svg>
                                            ))}
                                            <span className="ml-1">(4.8)</span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div className="prose max-w-none mb-6">
                                <h3 className="text-xl font-semibold mb-3 text-gray-800">Descripción</h3>
                                <p className="text-gray-600 leading-relaxed">{service.description}</p>
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 p-6 bg-blue-50 rounded-xl">
                                <div>
                                    <h4 className="font-semibold text-gray-800 mb-3 flex items-center gap-2">
                                        <svg className="w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                                        </svg>
                                        Detalles
                                    </h4>
                                    <ul className="space-y-2 text-gray-600">
                                        <li className="flex items-center gap-2">
                                            <span className="w-2 h-2 bg-blue-600 rounded-full"></span>
                                            <strong>Transporte:</strong> {service.transportType}
                                        </li>
                                        <li className="flex items-center gap-2">
                                            <span className="w-2 h-2 bg-blue-600 rounded-full"></span>
                                            <strong>Inicio:</strong> {service.startDate}
                                        </li>
                                        <li className="flex items-center gap-2">
                                            <span className="w-2 h-2 bg-blue-600 rounded-full"></span>
                                            <strong>Fin:</strong> {service.endDate}
                                        </li>
                                    </ul>
                                </div>
                                <div>
                                    <h4 className="font-semibold text-gray-800 mb-3 flex items-center gap-2">
                                        <svg className="w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                                        </svg>
                                        Ubicación
                                    </h4>
                                    <p className="text-gray-600">{service.address}</p>
                                </div>
                            </div>
                        </div>

                        <ReviewSection serviceId={id} />
                    </div>

                    {/* Right Column - Booking Box */}
                    <div className="lg:col-span-1">
                        <div className="sticky top-4">
                            <div className="bg-white rounded-2xl shadow-2xl p-6 border-2 border-blue-100">
                                <div className="mb-6">
                                    <div className="flex items-baseline gap-2 mb-2">
                                        <span className="text-sm text-gray-400 line-through">
                                            {service.currency} {(service.price * 1.25).toFixed(2)}
                                        </span>
                                        <span className="badge-discount">-25%</span>
                                    </div>
                                    <div className="flex items-baseline gap-2">
                                        <span className="text-4xl font-bold text-orange-600">
                                            {service.currency} {service.price}
                                        </span>
                                        <span className="text-gray-600">/ persona</span>
                                    </div>
                                </div>

                                <div className="space-y-4 mb-6">
                                    <div className="p-4 bg-green-50 rounded-lg border border-green-200">
                                        <div className="flex items-center gap-2 text-green-700">
                                            <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                                                <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                                            </svg>
                                            <span className="font-medium">Disponibilidad confirmada</span>
                                        </div>
                                    </div>

                                    <div className="p-4 bg-blue-50 rounded-lg border border-blue-200">
                                        <div className="flex items-center gap-2 text-blue-700 mb-2">
                                            <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                                                <path d="M8 16.5a1.5 1.5 0 11-3 0 1.5 1.5 0 013 0zM15 16.5a1.5 1.5 0 11-3 0 1.5 1.5 0 013 0z" />
                                                <path d="M3 4a1 1 0 00-1 1v10a1 1 0 001 1h1.05a2.5 2.5 0 014.9 0H10a1 1 0 001-1V5a1 1 0 00-1-1H3zM14 7a1 1 0 00-1 1v6.05A2.5 2.5 0 0115.95 16H17a1 1 0 001-1v-5a1 1 0 00-.293-.707l-2-2A1 1 0 0015 7h-1z" />
                                            </svg>
                                            <span className="font-medium">Incluye transporte</span>
                                        </div>
                                    </div>
                                </div>

                                <button
                                    onClick={() => navigate(`/checkout/${id}`)}
                                    className="w-full btn-primary text-lg py-4 mb-3"
                                >
                                    Reservar Ahora
                                </button>

                                <button className="w-full btn-secondary py-3">
                                    Consultar Disponibilidad
                                </button>

                                <div className="mt-6 pt-6 border-t border-gray-200">
                                    <div className="flex items-center justify-center gap-2 text-sm text-gray-600">
                                        <svg className="w-5 h-5 text-green-600" fill="currentColor" viewBox="0 0 20 20">
                                            <path fillRule="evenodd" d="M2.166 4.999A11.954 11.954 0 0010 1.944 11.954 11.954 0 0017.834 5c.11.65.166 1.32.166 2.001 0 5.225-3.34 9.67-8 11.317C5.34 16.67 2 12.225 2 7c0-.682.057-1.35.166-2.001zm11.541 3.708a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                                        </svg>
                                        <span>Pago 100% seguro</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ServiceDetails;
