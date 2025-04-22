package com.frontend.mobile.api

import com.frontend.mobile.model.User
import com.frontend.mobile.model.AuthResponse
import com.frontend.mobile.model.DailyWeatherDTO
import com.frontend.mobile.model.WardrobeRecommendation
import com.frontend.mobile.model.ActivityDTO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("api/users") // Matches POST /api/users
    fun registerUser(@Body user: User): Call<User>

    @POST("api/auth/login") // Matches POST /api/auth/login
    fun loginUser(@Body loginRequest: Map<String, String>): Call<AuthResponse>

    @POST("api/auth/logout") // Matches POST /api/auth/logout
    fun logoutUser(@Header("Authorization") token: String): Call<ResponseBody>

    @GET("api/users") // Fetch user details by ID
    fun getUserDetails(@Query("id") userId: Long, @Header("Authorization") token: String): Call<User>

    @PUT("api/users") // Update user details
    fun updateUserDetails(
        @Query("id") userId: Long,
        @Header("Authorization") token: String,
        @Body updatedUser: User
    ): Call<AuthResponse>

    @GET("weather/todayByCity")
    fun getTodaysWeatherByCity(
        @Query("city") city: String,
        @Header("Authorization") token: String
    ): Call<DailyWeatherDTO>

    @GET("wardrobe/todayByCity")
    fun getTodayWardrobeByCity(
        @Query("city") city: String,
        @Header("Authorization") token: String
    ): Call<List<WardrobeRecommendation>>

    @GET("api/activities/recommendationsByCity")
    fun getActivityRecommendationsByCity(
        @Query("city") city: String,
        @Header("Authorization") token: String
    ): Call<List<ActivityDTO>>
}
