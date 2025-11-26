import React from 'react';
import { Link } from 'react-router-dom';

const ServiceCard = ({ service }) => {
    const imageUrl = service.images && service.images.length > 0
        ? service.images[0].url
        : 'https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=500';

    // Calculate discount percentage (mock)
    const hasDiscount = Math.random() > 0.5;
    const discountPercent = hasDiscount ? Math.floor(Math.random() * 30) + 10 : 0;

    return (
        <Link to={`/services/${service.id}`} className="block group">
            <div className="card h-full transform group-hover:scale-105 transition-transform duration-300">
                {/* Image Container */}
                <div className="relative h-48 overflow-hidden">
                    <img
                        src={imageUrl}
                        alt={service.title}
                        className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-500"
                    />

                    {/* Badges */}
                    <div className="absolute top-3 left-3 flex flex-col gap-2">
                        {hasDiscount && (
                            <span className="badge-discount shadow-lg">
                                -{discountPercent}% OFF
                            </span>
                        )}
                    </div>

                    {/* Location Badge */}
                    <div className="absolute bottom-3 left-3 bg-white/90 backdrop-blur-sm px-3 py-1 rounded-full flex items-center gap-1 shadow-md">
                        <svg className="w-4 h-4 text-blue-600" fill="currentColor" viewBox="0 0 20 20">
                            <path fillRule="evenodd" d="M5.05 4.05a7 7 0 119.9 9.9L10 18.9l-4.95-4.95a7 7 0 010-9.9zM10 11a2 2 0 100-4 2 2 0 000 4z" clipRule="evenodd" />
                        </svg>
                        <span className="text-xs font-medium text-gray-700">
                            {service.cityCode}, {service.countryCode}
                        </span>
                    </div>
                </div>

                {/* Content */}
                <div className="p-4">
                    <h3 className="font-semibold text-lg text-gray-800 mb-2 line-clamp-2 group-hover:text-blue-600 transition-colors">
                        {service.title}
                    </h3>

                    <p className="text-sm text-gray-600 mb-3 line-clamp-2">
                        {service.description}
                    </p>

                    {/* Rating */}
                    <div className="flex items-center gap-1 mb-3">
                        {[...Array(5)].map((_, i) => (
                            <svg key={i} className="w-4 h-4 text-yellow-400 fill-current" viewBox="0 0 20 20">
                                <path d="M10 15l-5.878 3.09 1.123-6.545L.489 6.91l6.572-.955L10 0l2.939 5.955 6.572.955-4.756 4.635 1.123 6.545z" />
                            </svg>
                        ))}
                        <span className="text-xs text-gray-600 ml-1">(4.8)</span>
                    </div>

                    {/* Price */}
                    <div className="flex items-center justify-between">
                        <div>
                            {hasDiscount && (
                                <span className="text-sm text-gray-400 line-through mr-2">
                                    {service.currency} {(service.price * 1.3).toFixed(2)}
                                </span>
                            )}
                            <span className="text-2xl font-bold text-orange-600">
                                {service.currency} {service.price}
                            </span>
                        </div>
                        <button className="bg-blue-600 text-white px-4 py-2 rounded-lg text-sm font-semibold hover:bg-blue-700 transition-colors">
                            Ver más
                        </button>
                    </div>
                </div>
            </div>
        </Link>
    );
};

export default ServiceCard;
