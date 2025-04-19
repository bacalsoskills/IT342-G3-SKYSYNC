import axios from "axios";

const API_URL = "http://localhost:8080/api/schedules";

export const scheduleActivity = async (userId, activityId, startTime, endTime) => {
  try {
    const authToken = localStorage.getItem("authToken");
    const response = await axios.post(
      `${API_URL}/create`,
      {
        activityId,
        startTime,
        endTime,
      },
      {
        params: { userId },
        headers: {
          Authorization: `Bearer ${authToken}`,
          "Content-Type": "application/json",
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error scheduling activity:", error);
    throw error;
  }
};

// Fetch the schedule for a specific activity
export const getActivitySchedule = async (activityId) => {
  try {
    const authToken = localStorage.getItem("authToken");
    const response = await axios.get(`${API_URL}/activity/${activityId}`, {
      headers: {
        Authorization: `Bearer ${authToken}`,
      },
    });
    return response.data; // Return the schedule for the activity
  } catch (error) {
    console.error("Error fetching activity schedule:", error);
    throw error;
  }
};

export const updateSchedule = async (scheduleId, startTime, endTime) => {
  try {
    const authToken = localStorage.getItem("authToken"); // Retrieve the token from localStorage
    const response = await axios.put(
      `${API_URL}/${scheduleId}`,
      { startTime, endTime },
      {
        headers: {
          Authorization: `Bearer ${authToken}`, // Include the token in the Authorization header
          "Content-Type": "application/json",
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error updating schedule:", error);
    throw error;
  }
};