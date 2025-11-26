import React, { useEffect, useState } from 'react';
import axios from 'axios';
import ServiceCard from '../components/ServiceCard';
import { MOCK_SERVICES } from '../data/mockData';

const Home = () => {
    const [services, setServices] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchServices = async () => {
            try {
                const apiUrl = import.meta.env.VITE_API_URL;
                const response = await axios.get(`${apiUrl}/services`);
                setServices(response.data.content || []);
                setLoading(false);
            } catch (err) {
                console.warn("Backend not reachable, using MOCK data for testing.");
                setServices(MOCK_SERVICES);
                setLoading(false);
            }
        };

        fetchServices();
    }, []);

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-blue-100">
                <div className="text-center">
                    <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-b-4 border-blue-600 mx-auto mb-4"></div>
                    <p className="text-gray-600 font-medium">Cargando experiencias...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-green-50">
            {/* Hero Section */}
            <div className="relative bg-gradient-to-r from-blue-600 via-blue-700 to-green-600 text-white overflow-hidden">
                <div className="absolute inset-0 bg-black/20"></div>
                <div className="absolute inset-0 bg-[url('https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?w=1200')] bg-cover bg-center opacity-20"></div>

                <div className="relative container mx-auto px-4 py-20 md:py-28">
                    <div className="max-w-3xl">
                        <h1 className="text-4xl md:text-6xl font-bold mb-6 leading-tight">
                            Descubre Experiencias
                            <span className="block text-yellow-300">Eco-Turísticas Únicas</span>
                        </h1>
                        <p className="text-lg md:text-xl mb-8 text-blue-100">
                            Conecta con la naturaleza mientras apoyas el turismo sostenible en Colombia
                        </p>
                        <div className="flex flex-wrap gap-4">
                            <button className="btn-primary">
                                Explorar Ahora
                            </button>
                            <button className="btn-secondary bg-white/10 border-white text-white hover:bg-white/20">
                                Ver Ofertas
                            </button>
                        </div>
                    </div>
                </div>

                {/* Wave Divider */}
                <div className="absolute bottom-0 left-0 right-0">
                    <svg viewBox="0 0 1440 120" className="w-full h-12 md:h-20 fill-current text-white">
                        <path d="M0,64L48,69.3C96,75,192,85,288,80C384,75,480,53,576,48C672,43,768,53,864,58.7C960,64,1056,64,1152,58.7C1248,53,1344,43,1392,37.3L1440,32L1440,120L1392,120C1344,120,1248,120,1152,120C1056,120,960,120,864,120C768,120,672,120,576,120C480,120,384,120,288,120C192,120,96,120,48,120L0,120Z"></path>
                    </svg>
                </div>
            </div>

            {/* Categories */}
            <div className="container mx-auto px-4 py-8">
                <div className="flex gap-4 overflow-x-auto pb-4 scrollbar-hide">
                    {['Aventura', 'Naturaleza', 'Cultura', 'Playa', 'Montaña', 'Selva'].map((cat) => (
                        <button key={cat} className="flex-shrink-0 px-6 py-3 bg-white rounded-full shadow-md hover:shadow-lg hover:bg-blue-50 transition-all duration-300 font-medium text-gray-700 hover:text-blue-600">
                            {cat}
                        </button>
                    ))}
                </div>
            </div>

            {/* Services Grid */}
            <div className="container mx-auto px-4 py-8">
                <div className="flex items-center justify-between mb-8">
                    <div>
                        <h2 className="text-3xl font-bold text-gray-800">Experiencias Destacadas</h2>
                        <p className="text-gray-600 mt-2">Las mejores ofertas en eco-turismo</p>
                    </div>
                    <select className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option>Más Populares</option>
                        <option>Precio: Menor a Mayor</option>
                        <option>Precio: Mayor a Menor</option>
                        <option>Mejor Valorados</option>
                    </select>
                </div>

                {services.length === 0 ? (
                    <div className="text-center py-20">
                        <div className="text-6xl mb-4">🌿</div>
                        <p className="text-gray-500 text-lg">No se encontraron servicios</p>
                    </div>
                ) : (
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                        {services.map(service => (
                            <ServiceCard key={service.id} service={service} />
                        ))}
                    </div>
                )}
            </div>

            {/* CTA Section */}
            <div className="container mx-auto px-4 py-16">
                <div className="bg-gradient-to-r from-green-600 to-blue-600 rounded-2xl p-8 md:p-12 text-white text-center shadow-2xl">
                    <h2 className="text-3xl md:text-4xl font-bold mb-4">¿Eres Proveedor de Servicios?</h2>
                    <p className="text-lg mb-6 text-blue-100">Únete a nuestra plataforma y llega a miles de viajeros</p>
                    <button className="bg-white text-blue-600 px-8 py-3 rounded-lg font-semibold hover:bg-blue-50 transition-colors shadow-lg">
                        Registrar mi Negocio
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Home;
