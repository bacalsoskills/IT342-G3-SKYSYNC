import axios from "axios";

import { API_BASE_URL } from "./config";

const API_URL = `${API_BASE_URL}/api/auth`;

export const login = async (email, password) => {
  try {
    const response = await axios.post(`${API_URL}/login`, { email, password });
    const { token } = response.data;

    // Save the token to local storage
    localStorage.setItem("authToken", token);

    return response.data;
  } catch (error) {
    console.error("Login error:", error.response?.data || error.message);
    throw new Error(error.response?.data?.message || "Login failed");
  }
};

export const logout = async (token) => {
  try {
    const response = await axios.post(
      `${API_URL}/logout`,
      {}, // No body needed
      {
        headers: {
          Authorization: `Bearer ${token}`, // Send token in Authorization header
        },
        withCredentials: true, // Ensure cookies are sent if needed
      }
    );
    return response.data;
  } catch (error) {
    console.error("Logout error:", error.response?.data || error.message);
    throw new Error(error.response?.data?.message || "Logout failed");
  }
};