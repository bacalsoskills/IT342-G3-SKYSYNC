import axios from "axios";

const API_URL = "http://localhost:8080/api/activities";

export const getActivityRecommendationsByCity = async (city) => {
  try {
    const authToken = localStorage.getItem("authToken"); // Retrieve the token from localStorage
    const response = await axios.get(`${API_URL}/recommendationsByCity`, {
      params: { city },
      headers: {
        Authorization: `Bearer ${authToken}`, // Include the token in the Authorization header
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching activity recommendations:", error);
    throw error;
  }
};