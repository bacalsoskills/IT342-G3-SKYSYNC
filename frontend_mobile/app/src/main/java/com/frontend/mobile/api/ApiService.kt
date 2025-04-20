package com.frontend.mobile.api

import com.frontend.mobile.model.User
import com.frontend.mobile.model.AuthResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("users") // Matches POST /api/users
    fun registerUser(@Body user: User): Call<User>

    @POST("auth/login") // Matches POST /api/auth/login
    fun loginUser(@Body loginRequest: Map<String, String>): Call<AuthResponse>

    @POST("auth/logout") // Matches POST /api/auth/logout
    fun logoutUser(@Header("Authorization") token: String): Call<ResponseBody>

    @GET("users") // Fetch user details by ID
    fun getUserDetails(@Query("id") userId: Long, @Header("Authorization") token: String): Call<User>

    @PUT("users") // Update user details
    fun updateUserDetails(
        @Query("id") userId: Long,
        @Header("Authorization") token: String,
        @Body updatedUser: User
    ): Call<AuthResponse>
}
