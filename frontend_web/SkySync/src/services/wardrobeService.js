import axios from "axios";

const API_URL = "http://localhost:8080/wardrobe";

export const getWardrobeRecommendationByCity = async (city) => {
  try {
    const authToken = localStorage.getItem("authToken"); // Retrieve the token from localStorage
    const response = await axios.get(`${API_URL}/todayByCity`, {
      params: { city },
      headers: {
        Authorization: `Bearer ${authToken}`, // Include the token in the Authorization header
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching wardrobe recommendation:", error);
    throw error;
  }
};