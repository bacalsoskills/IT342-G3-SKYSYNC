import axios from "axios";

const API_URL = "http://localhost:8080/api/users"; // Update with your backend URL if different

export const registerUser = async (user) => {
  try {
    const response = await axios.post(API_URL, user);
    return response.data;
  } catch (error) {
    console.error("Registration error:", error.response?.data || error.message);
    throw new Error(error.response?.data?.message || "Registration failed");
  }
};