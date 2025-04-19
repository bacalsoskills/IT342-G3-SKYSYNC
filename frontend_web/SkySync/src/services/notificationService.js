import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/notifications"; // Replace with your backend URL

// Fetch notifications for a specific user
export const getUserNotifications = async (userId) => {
  try {
    const authToken = localStorage.getItem("authToken"); // Retrieve the token from localStorage
    const response = await axios.get(`${API_BASE_URL}/user/${userId}`, {
      headers: {
        Authorization: `Bearer ${authToken}`, // Include the token in the Authorization header
      },
    });

    // Sort notifications by creation time in descending order
    return response.data.sort((a, b) => new Date(b.triggerTime) - new Date(a.triggerTime));
  } catch (error) {
    console.error("Error fetching user notifications:", error);
    throw error;
  }
};