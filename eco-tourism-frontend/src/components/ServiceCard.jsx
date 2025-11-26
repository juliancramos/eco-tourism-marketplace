import React from 'react';
import { Link } from 'react-router-dom';

const ServiceCard = ({ service }) => {
    return (
        <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-xl transition-shadow duration-300">
            <div className="h-48 bg-gray-200 relative">
                {service.images && service.images.length > 0 ? (
                    <img
                        src={service.images[0].url}
                        alt={service.title}
                        className="w-full h-full object-cover"
                    />
                ) : (
                    <div className="flex items-center justify-center h-full text-gray-500">
                        No Image
                    </div>
                )}
                <div className="absolute top-2 right-2 bg-white px-2 py-1 rounded-full text-xs font-bold text-green-600">
                    {service.price} {service.currency}
                </div>
            </div>
            <div className="p-4">
                <h3 className="text-lg font-semibold text-gray-800 mb-2 truncate">{service.title}</h3>
                <p className="text-gray-600 text-sm mb-4 line-clamp-2">{service.description}</p>
                <div className="flex justify-between items-center">
                    <span className="text-sm text-gray-500">{service.cityCode}, {service.countryCode}</span>
                    <Link
                        to={`/services/${service.id}`}
                        className="bg-green-600 text-white px-4 py-2 rounded-md text-sm hover:bg-green-700 transition-colors"
                    >
                        View Details
                    </Link>
                </div>
            </div>
        </div>
    );
};

export default ServiceCard;
