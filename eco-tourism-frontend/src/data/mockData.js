export const MOCK_SERVICES = [
    {
        id: 1,
        title: "Amazon Rainforest Expedition (Mock)",
        description: "Experience the breathtaking biodiversity of the Amazon. This is a mock service for local testing.",
        price: 150.00,
        currency: "USD",
        cityCode: "LET",
        countryCode: "COL",
        images: [{ url: "https://images.unsplash.com/photo-1516908205727-40afad9449a8?ixlib=rb-1.2.1&auto=format&fit=crop&w=900&q=60" }],
        transportType: "Boat",
        startDate: "2023-12-01",
        endDate: "2023-12-05",
        address: "Leticia, Amazonas"
    },
    {
        id: 2,
        title: "Tayrona Park Hiking (Mock)",
        description: "Walk through the jungle to find pristine beaches. Mock data.",
        price: 80.00,
        currency: "USD",
        cityCode: "SMR",
        countryCode: "COL",
        images: [{ url: "https://images.unsplash.com/photo-1596436807771-7a339038a27e?ixlib=rb-1.2.1&auto=format&fit=crop&w=900&q=60" }],
        transportType: "Bus",
        startDate: "2023-11-15",
        endDate: "2023-11-16",
        address: "Santa Marta, Magdalena"
    },
    {
        id: 3,
        title: "Coffee Region Tour (Mock)",
        description: "Taste the best coffee in the world directly from the farm. Mock data.",
        price: 120.00,
        currency: "USD",
        cityCode: "AXM",
        countryCode: "COL",
        images: [{ url: "https://images.unsplash.com/photo-1524230659092-07f99a75c013?ixlib=rb-1.2.1&auto=format&fit=crop&w=900&q=60" }],
        transportType: "Jeep",
        startDate: "2023-10-20",
        endDate: "2023-10-22",
        address: "Armenia, Quindio"
    }
];

export const MOCK_REVIEWS = [
    { id: 1, userId: 101, stars: 5, commentText: "Amazing experience! (Mock Review)" },
    { id: 2, userId: 102, stars: 4, commentText: "Very good, but humid. (Mock Review)" },
    { id: 3, userId: 103, stars: 5, commentText: "Loved the guides. (Mock Review)" }
];
