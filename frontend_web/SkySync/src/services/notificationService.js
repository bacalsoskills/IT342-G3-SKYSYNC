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

    // Sort notifications by id (descending order)
    const sortedData = response.data.sort((a, b) => b.id - a.id);
    console.log("Sorted Notifications (Descending by ID):", sortedData); // Debugging: Check the sorted data
    return sortedData;
  } catch (error) {
    console.error("Error fetching user notifications:", error);
    throw error;
  }
};

const fetchNotifications = async () => {
  setLoading(true);
  try {
    const data = await getUserNotifications(userId);
    console.log("Notifications to Render:", data); // Debugging: Check the data passed to setNotifications
    setNotifications(data);
  } catch (error) {
    console.error("Failed to fetch notifications:", error);
  } finally {
    setLoading(false);
  }
};