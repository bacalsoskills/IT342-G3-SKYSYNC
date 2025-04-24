import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/notifications"; // Replace with your backend URL

// Fetch notifications for a specific user
export const getUserNotifications = async (userId) => {
  try {
    const authToken = localStorage.getItem("authToken");
    const response = await axios.get(`${API_BASE_URL}/user/${userId}`, {
      headers: {
        Authorization: `Bearer ${authToken}`,
      },
    });

  } catch (error) {
    console.error("Error fetching user notifications:", error);
    throw error;
  }
};

  }
};