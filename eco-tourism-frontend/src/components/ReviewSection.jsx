import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { MOCK_REVIEWS } from '../data/mockData';

const ReviewSection = ({ serviceId }) => {
    const [reviews, setReviews] = useState([]);
    const [newReview, setNewReview] = useState({ stars: 5, commentText: '' });
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchReviews();
    }, [serviceId]);

    const fetchReviews = async () => {
        try {
            const apiUrl = import.meta.env.VITE_API_URL;
            const response = await axios.get(`${apiUrl}/reviews?serviceId=${serviceId}`);
            setReviews(response.data.content || []);
            setLoading(false);
        } catch (error) {
            console.warn("Backend not reachable, using MOCK reviews.");
            setReviews(MOCK_REVIEWS);
            setLoading(false);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const apiUrl = import.meta.env.VITE_API_URL;
            const payload = {
                serviceId: parseInt(serviceId),
                userId: 1,
                stars: newReview.stars,
                commentText: newReview.commentText
            };

            await axios.post(`${apiUrl}/reviews`, payload);
            setNewReview({ stars: 5, commentText: '' });
            fetchReviews();
        } catch (error) {
            console.error("Error submitting review:", error);
            alert("Failed to submit review (Backend unreachable)");
        }
    };

    return (
        <div className="mt-12 border-t pt-8">
            <h3 className="text-2xl font-bold text-gray-800 mb-6">Reviews</h3>

            {/* Review Form */}
            <div className="bg-gray-50 p-6 rounded-lg mb-8">
                <h4 className="text-lg font-semibold mb-4">Write a Review</h4>
                <form onSubmit={handleSubmit}>
                    <div className="mb-4">
                        <label className="block text-sm font-medium text-gray-700 mb-1">Rating</label>
                        <select
                            value={newReview.stars}
                            onChange={(e) => setNewReview({ ...newReview, stars: parseInt(e.target.value) })}
                            className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-green-500 focus:border-green-500 sm:text-sm rounded-md"
                        >
                            {[5, 4, 3, 2, 1].map(star => (
                                <option key={star} value={star}>{star} Stars</option>
                            ))}
                        </select>
                    </div>
                    <div className="mb-4">
                        <label className="block text-sm font-medium text-gray-700 mb-1">Comment</label>
                        <textarea
                            required
                            rows={3}
                            value={newReview.commentText}
                            onChange={(e) => setNewReview({ ...newReview, commentText: e.target.value })}
                            className="shadow-sm focus:ring-green-500 focus:border-green-500 block w-full sm:text-sm border-gray-300 rounded-md"
                            placeholder="Share your experience..."
                        />
                    </div>
                    <button type="submit" className="bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700">
                        Submit Review
                    </button>
                </form>
            </div>

            {/* Review List */}
            {loading ? (
                <div>Loading reviews...</div>
            ) : reviews.length === 0 ? (
                <div className="text-gray-500">No reviews yet. Be the first!</div>
            ) : (
                <div className="space-y-6">
                    {reviews.map((review) => (
                        <div key={review.id} className="bg-white p-4 rounded-lg shadow-sm border">
                            <div className="flex items-center mb-2">
                                <div className="text-yellow-400 mr-2">
                                    {"★".repeat(review.stars)}
                                    {"☆".repeat(5 - review.stars)}
                                </div>
                                <span className="text-sm text-gray-500">User #{review.userId}</span>
                            </div>
                            <p className="text-gray-700">{review.commentText}</p>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default ReviewSection;
